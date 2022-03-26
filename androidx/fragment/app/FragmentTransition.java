package androidx.fragment.app;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.collection.ArrayMap;
import androidx.core.app.SharedElementCallback;
import androidx.core.os.CancellationSignal;
import androidx.core.view.OneShotPreDrawListener;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class FragmentTransition {
    private static final int[] INVERSE_OPS = {0, 3, 0, 1, 5, 4, 7, 6, 9, 8, 10};
    static final FragmentTransitionImpl PLATFORM_IMPL;
    static final FragmentTransitionImpl SUPPORT_IMPL;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface Callback {
        void onComplete(Fragment fragment, CancellationSignal cancellationSignal);

        void onStart(Fragment fragment, CancellationSignal cancellationSignal);
    }

    static {
        FragmentTransitionCompat21 fragmentTransitionCompat21;
        if (Build.VERSION.SDK_INT >= 21) {
            fragmentTransitionCompat21 = new FragmentTransitionCompat21();
        } else {
            fragmentTransitionCompat21 = null;
        }
        PLATFORM_IMPL = fragmentTransitionCompat21;
        SUPPORT_IMPL = resolveSupportImpl();
    }

    private static FragmentTransitionImpl resolveSupportImpl() {
        try {
            return (FragmentTransitionImpl) Class.forName("androidx.transition.FragmentTransitionSupport").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception e) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void startTransitions(Context context, FragmentContainer fragmentContainer, ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex, boolean isReordered, Callback callback) {
        ViewGroup container;
        SparseArray<FragmentContainerTransition> transitioningFragments = new SparseArray<>();
        for (int i = startIndex; i < endIndex; i++) {
            BackStackRecord record = records.get(i);
            if (isRecordPop.get(i).booleanValue()) {
                calculatePopFragments(record, transitioningFragments, isReordered);
            } else {
                calculateFragments(record, transitioningFragments, isReordered);
            }
        }
        if (transitioningFragments.size() != 0) {
            View nonExistentView = new View(context);
            int numContainers = transitioningFragments.size();
            for (int i2 = 0; i2 < numContainers; i2++) {
                int containerId = transitioningFragments.keyAt(i2);
                ArrayMap<String, String> nameOverrides = calculateNameOverrides(containerId, records, isRecordPop, startIndex, endIndex);
                FragmentContainerTransition containerTransition = transitioningFragments.valueAt(i2);
                if (fragmentContainer.onHasView() && (container = (ViewGroup) fragmentContainer.onFindViewById(containerId)) != null) {
                    if (isReordered) {
                        configureTransitionsReordered(container, containerTransition, nonExistentView, nameOverrides, callback);
                    } else {
                        configureTransitionsOrdered(container, containerTransition, nonExistentView, nameOverrides, callback);
                    }
                }
            }
        }
    }

    private static ArrayMap<String, String> calculateNameOverrides(int containerId, ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex) {
        ArrayList<String> sources;
        ArrayList<String> targets;
        ArrayMap<String, String> nameOverrides = new ArrayMap<>();
        for (int recordNum = endIndex - 1; recordNum >= startIndex; recordNum--) {
            BackStackRecord record = records.get(recordNum);
            if (record.interactsWith(containerId)) {
                boolean isPop = isRecordPop.get(recordNum).booleanValue();
                if (record.mSharedElementSourceNames != null) {
                    int numSharedElements = record.mSharedElementSourceNames.size();
                    if (isPop) {
                        targets = record.mSharedElementSourceNames;
                        sources = record.mSharedElementTargetNames;
                    } else {
                        sources = record.mSharedElementSourceNames;
                        targets = record.mSharedElementTargetNames;
                    }
                    for (int i = 0; i < numSharedElements; i++) {
                        String sourceName = sources.get(i);
                        String targetName = targets.get(i);
                        String previousTarget = nameOverrides.remove(targetName);
                        if (previousTarget != null) {
                            nameOverrides.put(sourceName, previousTarget);
                        } else {
                            nameOverrides.put(sourceName, targetName);
                        }
                    }
                }
            }
        }
        return nameOverrides;
    }

    private static void configureTransitionsReordered(ViewGroup container, FragmentContainerTransition fragments, View nonExistentView, ArrayMap<String, String> nameOverrides, final Callback callback) {
        Object exitTransition;
        ArrayList<View> sharedElementsIn;
        Fragment inFragment = fragments.lastIn;
        final Fragment outFragment = fragments.firstOut;
        FragmentTransitionImpl impl = chooseImpl(outFragment, inFragment);
        if (impl != null) {
            boolean inIsPop = fragments.lastInIsPop;
            boolean outIsPop = fragments.firstOutIsPop;
            ArrayList<View> sharedElementsIn2 = new ArrayList<>();
            ArrayList<View> sharedElementsOut = new ArrayList<>();
            Object enterTransition = getEnterTransition(impl, inFragment, inIsPop);
            Object exitTransition2 = getExitTransition(impl, outFragment, outIsPop);
            Object sharedElementTransition = configureSharedElementsReordered(impl, container, nonExistentView, nameOverrides, fragments, sharedElementsOut, sharedElementsIn2, enterTransition, exitTransition2);
            if (enterTransition == null && sharedElementTransition == null) {
                exitTransition = exitTransition2;
                if (exitTransition == null) {
                    return;
                }
            } else {
                exitTransition = exitTransition2;
            }
            ArrayList<View> exitingViews = configureEnteringExitingViews(impl, exitTransition, outFragment, sharedElementsOut, nonExistentView);
            ArrayList<View> enteringViews = configureEnteringExitingViews(impl, enterTransition, inFragment, sharedElementsIn2, nonExistentView);
            setViewVisibility(enteringViews, 4);
            Object transition = mergeTransitions(impl, enterTransition, exitTransition, sharedElementTransition, inFragment, inIsPop);
            if (outFragment == null || exitingViews == null) {
                sharedElementsIn = sharedElementsIn2;
            } else if (exitingViews.size() > 0 || sharedElementsOut.size() > 0) {
                final CancellationSignal signal = new CancellationSignal();
                sharedElementsIn = sharedElementsIn2;
                callback.onStart(outFragment, signal);
                impl.setListenerForTransitionEnd(outFragment, transition, signal, new Runnable() { // from class: androidx.fragment.app.FragmentTransition.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Callback.this.onComplete(outFragment, signal);
                    }
                });
            } else {
                sharedElementsIn = sharedElementsIn2;
            }
            if (transition != null) {
                replaceHide(impl, exitTransition, outFragment, exitingViews);
                ArrayList<String> inNames = impl.prepareSetNameOverridesReordered(sharedElementsIn);
                impl.scheduleRemoveTargets(transition, enterTransition, enteringViews, exitTransition, exitingViews, sharedElementTransition, sharedElementsIn);
                impl.beginDelayedTransition(container, transition);
                impl.setNameOverridesReordered(container, sharedElementsOut, sharedElementsIn, inNames, nameOverrides);
                setViewVisibility(enteringViews, 0);
                impl.swapSharedElementTargets(sharedElementTransition, sharedElementsOut, sharedElementsIn);
            }
        }
    }

    private static void replaceHide(FragmentTransitionImpl impl, Object exitTransition, Fragment exitingFragment, final ArrayList<View> exitingViews) {
        if (exitingFragment != null && exitTransition != null && exitingFragment.mAdded && exitingFragment.mHidden && exitingFragment.mHiddenChanged) {
            exitingFragment.setHideReplaced(true);
            impl.scheduleHideFragmentView(exitTransition, exitingFragment.getView(), exitingViews);
            OneShotPreDrawListener.add(exitingFragment.mContainer, new Runnable() { // from class: androidx.fragment.app.FragmentTransition.2
                @Override // java.lang.Runnable
                public void run() {
                    FragmentTransition.setViewVisibility(exitingViews, 4);
                }
            });
        }
    }

    private static void configureTransitionsOrdered(ViewGroup container, FragmentContainerTransition fragments, View nonExistentView, ArrayMap<String, String> nameOverrides, final Callback callback) {
        Object exitTransition;
        Object exitTransition2;
        Fragment inFragment = fragments.lastIn;
        final Fragment outFragment = fragments.firstOut;
        FragmentTransitionImpl impl = chooseImpl(outFragment, inFragment);
        if (impl != null) {
            boolean inIsPop = fragments.lastInIsPop;
            boolean outIsPop = fragments.firstOutIsPop;
            Object enterTransition = getEnterTransition(impl, inFragment, inIsPop);
            Object exitTransition3 = getExitTransition(impl, outFragment, outIsPop);
            ArrayList<View> sharedElementsOut = new ArrayList<>();
            ArrayList<View> sharedElementsIn = new ArrayList<>();
            Object sharedElementTransition = configureSharedElementsOrdered(impl, container, nonExistentView, nameOverrides, fragments, sharedElementsOut, sharedElementsIn, enterTransition, exitTransition3);
            if (enterTransition == null && sharedElementTransition == null) {
                exitTransition = exitTransition3;
                if (exitTransition == null) {
                    return;
                }
            } else {
                exitTransition = exitTransition3;
            }
            ArrayList<View> exitingViews = configureEnteringExitingViews(impl, exitTransition, outFragment, sharedElementsOut, nonExistentView);
            if (exitingViews == null || exitingViews.isEmpty()) {
                exitTransition2 = null;
            } else {
                exitTransition2 = exitTransition;
            }
            impl.addTarget(enterTransition, nonExistentView);
            Object transition = mergeTransitions(impl, enterTransition, exitTransition2, sharedElementTransition, inFragment, fragments.lastInIsPop);
            if (!(outFragment == null || exitingViews == null || (exitingViews.size() <= 0 && sharedElementsOut.size() <= 0))) {
                final CancellationSignal signal = new CancellationSignal();
                callback.onStart(outFragment, signal);
                impl.setListenerForTransitionEnd(outFragment, transition, signal, new Runnable() { // from class: androidx.fragment.app.FragmentTransition.3
                    @Override // java.lang.Runnable
                    public void run() {
                        Callback.this.onComplete(outFragment, signal);
                    }
                });
            }
            if (transition != null) {
                ArrayList<View> enteringViews = new ArrayList<>();
                impl.scheduleRemoveTargets(transition, enterTransition, enteringViews, exitTransition2, exitingViews, sharedElementTransition, sharedElementsIn);
                scheduleTargetChange(impl, container, inFragment, nonExistentView, sharedElementsIn, enterTransition, enteringViews, exitTransition2, exitingViews);
                impl.setNameOverridesOrdered(container, sharedElementsIn, nameOverrides);
                impl.beginDelayedTransition(container, transition);
                impl.scheduleNameReset(container, sharedElementsIn, nameOverrides);
            }
        }
    }

    private static void scheduleTargetChange(final FragmentTransitionImpl impl, ViewGroup sceneRoot, final Fragment inFragment, final View nonExistentView, final ArrayList<View> sharedElementsIn, final Object enterTransition, final ArrayList<View> enteringViews, final Object exitTransition, final ArrayList<View> exitingViews) {
        OneShotPreDrawListener.add(sceneRoot, new Runnable() { // from class: androidx.fragment.app.FragmentTransition.4
            @Override // java.lang.Runnable
            public void run() {
                Object obj = enterTransition;
                if (obj != null) {
                    impl.removeTarget(obj, nonExistentView);
                    enteringViews.addAll(FragmentTransition.configureEnteringExitingViews(impl, enterTransition, inFragment, sharedElementsIn, nonExistentView));
                }
                if (exitingViews != null) {
                    if (exitTransition != null) {
                        ArrayList<View> tempExiting = new ArrayList<>();
                        tempExiting.add(nonExistentView);
                        impl.replaceTargets(exitTransition, exitingViews, tempExiting);
                    }
                    exitingViews.clear();
                    exitingViews.add(nonExistentView);
                }
            }
        });
    }

    private static FragmentTransitionImpl chooseImpl(Fragment outFragment, Fragment inFragment) {
        ArrayList<Object> transitions = new ArrayList<>();
        if (outFragment != null) {
            Object exitTransition = outFragment.getExitTransition();
            if (exitTransition != null) {
                transitions.add(exitTransition);
            }
            Object returnTransition = outFragment.getReturnTransition();
            if (returnTransition != null) {
                transitions.add(returnTransition);
            }
            Object sharedReturnTransition = outFragment.getSharedElementReturnTransition();
            if (sharedReturnTransition != null) {
                transitions.add(sharedReturnTransition);
            }
        }
        if (inFragment != null) {
            Object enterTransition = inFragment.getEnterTransition();
            if (enterTransition != null) {
                transitions.add(enterTransition);
            }
            Object reenterTransition = inFragment.getReenterTransition();
            if (reenterTransition != null) {
                transitions.add(reenterTransition);
            }
            Object sharedEnterTransition = inFragment.getSharedElementEnterTransition();
            if (sharedEnterTransition != null) {
                transitions.add(sharedEnterTransition);
            }
        }
        if (transitions.isEmpty()) {
            return null;
        }
        FragmentTransitionImpl fragmentTransitionImpl = PLATFORM_IMPL;
        if (fragmentTransitionImpl != null && canHandleAll(fragmentTransitionImpl, transitions)) {
            return PLATFORM_IMPL;
        }
        FragmentTransitionImpl fragmentTransitionImpl2 = SUPPORT_IMPL;
        if (fragmentTransitionImpl2 != null && canHandleAll(fragmentTransitionImpl2, transitions)) {
            return SUPPORT_IMPL;
        }
        if (PLATFORM_IMPL == null && SUPPORT_IMPL == null) {
            return null;
        }
        throw new IllegalArgumentException("Invalid Transition types");
    }

    private static boolean canHandleAll(FragmentTransitionImpl impl, List<Object> transitions) {
        int size = transitions.size();
        for (int i = 0; i < size; i++) {
            if (!impl.canHandle(transitions.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static Object getSharedElementTransition(FragmentTransitionImpl impl, Fragment inFragment, Fragment outFragment, boolean isPop) {
        Object obj;
        if (inFragment == null || outFragment == null) {
            return null;
        }
        if (isPop) {
            obj = outFragment.getSharedElementReturnTransition();
        } else {
            obj = inFragment.getSharedElementEnterTransition();
        }
        return impl.wrapTransitionInSet(impl.cloneTransition(obj));
    }

    private static Object getEnterTransition(FragmentTransitionImpl impl, Fragment inFragment, boolean isPop) {
        Object obj;
        if (inFragment == null) {
            return null;
        }
        if (isPop) {
            obj = inFragment.getReenterTransition();
        } else {
            obj = inFragment.getEnterTransition();
        }
        return impl.cloneTransition(obj);
    }

    private static Object getExitTransition(FragmentTransitionImpl impl, Fragment outFragment, boolean isPop) {
        Object obj;
        if (outFragment == null) {
            return null;
        }
        if (isPop) {
            obj = outFragment.getReturnTransition();
        } else {
            obj = outFragment.getExitTransition();
        }
        return impl.cloneTransition(obj);
    }

    private static Object configureSharedElementsReordered(final FragmentTransitionImpl impl, ViewGroup sceneRoot, View nonExistentView, ArrayMap<String, String> nameOverrides, FragmentContainerTransition fragments, ArrayList<View> sharedElementsOut, ArrayList<View> sharedElementsIn, Object enterTransition, Object exitTransition) {
        Object sharedElementTransition;
        Object sharedElementTransition2;
        Object sharedElementTransition3;
        final Rect epicenter;
        final View epicenterView;
        final ArrayMap<String, View> inSharedElements;
        final Fragment inFragment = fragments.lastIn;
        final Fragment outFragment = fragments.firstOut;
        if (inFragment != null) {
            inFragment.requireView().setVisibility(0);
        }
        if (!(inFragment == null || outFragment == null)) {
            final boolean inIsPop = fragments.lastInIsPop;
            if (nameOverrides.isEmpty()) {
                sharedElementTransition = null;
            } else {
                sharedElementTransition = getSharedElementTransition(impl, inFragment, outFragment, inIsPop);
            }
            ArrayMap<String, View> outSharedElements = captureOutSharedElements(impl, nameOverrides, sharedElementTransition, fragments);
            ArrayMap<String, View> inSharedElements2 = captureInSharedElements(impl, nameOverrides, sharedElementTransition, fragments);
            if (nameOverrides.isEmpty()) {
                if (outSharedElements != null) {
                    outSharedElements.clear();
                }
                if (inSharedElements2 != null) {
                    inSharedElements2.clear();
                }
                sharedElementTransition2 = null;
            } else {
                addSharedElementsWithMatchingNames(sharedElementsOut, outSharedElements, nameOverrides.keySet());
                addSharedElementsWithMatchingNames(sharedElementsIn, inSharedElements2, nameOverrides.values());
                sharedElementTransition2 = sharedElementTransition;
            }
            if (enterTransition == null && exitTransition == null && sharedElementTransition2 == null) {
                return null;
            }
            callSharedElementStartEnd(inFragment, outFragment, inIsPop, outSharedElements, true);
            if (sharedElementTransition2 != null) {
                sharedElementsIn.add(nonExistentView);
                impl.setSharedElementTargets(sharedElementTransition2, nonExistentView, sharedElementsOut);
                sharedElementTransition3 = sharedElementTransition2;
                inSharedElements = inSharedElements2;
                setOutEpicenter(impl, sharedElementTransition2, exitTransition, outSharedElements, fragments.firstOutIsPop, fragments.firstOutTransaction);
                Rect epicenter2 = new Rect();
                View epicenterView2 = getInEpicenterView(inSharedElements, fragments, enterTransition, inIsPop);
                if (epicenterView2 != null) {
                    impl.setEpicenter(enterTransition, epicenter2);
                }
                epicenter = epicenter2;
                epicenterView = epicenterView2;
            } else {
                sharedElementTransition3 = sharedElementTransition2;
                inSharedElements = inSharedElements2;
                epicenterView = null;
                epicenter = null;
            }
            OneShotPreDrawListener.add(sceneRoot, new Runnable() { // from class: androidx.fragment.app.FragmentTransition.5
                @Override // java.lang.Runnable
                public void run() {
                    FragmentTransition.callSharedElementStartEnd(Fragment.this, outFragment, inIsPop, inSharedElements, false);
                    View view = epicenterView;
                    if (view != null) {
                        impl.getBoundsOnScreen(view, epicenter);
                    }
                }
            });
            return sharedElementTransition3;
        }
        return null;
    }

    private static void addSharedElementsWithMatchingNames(ArrayList<View> views, ArrayMap<String, View> sharedElements, Collection<String> nameOverridesSet) {
        for (int i = sharedElements.size() - 1; i >= 0; i--) {
            View view = sharedElements.valueAt(i);
            if (nameOverridesSet.contains(ViewCompat.getTransitionName(view))) {
                views.add(view);
            }
        }
    }

    private static Object configureSharedElementsOrdered(final FragmentTransitionImpl impl, ViewGroup sceneRoot, final View nonExistentView, final ArrayMap<String, String> nameOverrides, final FragmentContainerTransition fragments, final ArrayList<View> sharedElementsOut, final ArrayList<View> sharedElementsIn, final Object enterTransition, Object exitTransition) {
        Object sharedElementTransition;
        final Object sharedElementTransition2;
        final Rect inEpicenter;
        final Fragment inFragment = fragments.lastIn;
        final Fragment outFragment = fragments.firstOut;
        if (!(inFragment == null || outFragment == null)) {
            final boolean inIsPop = fragments.lastInIsPop;
            if (nameOverrides.isEmpty()) {
                sharedElementTransition = null;
            } else {
                sharedElementTransition = getSharedElementTransition(impl, inFragment, outFragment, inIsPop);
            }
            ArrayMap<String, View> outSharedElements = captureOutSharedElements(impl, nameOverrides, sharedElementTransition, fragments);
            if (nameOverrides.isEmpty()) {
                sharedElementTransition2 = null;
            } else {
                sharedElementsOut.addAll(outSharedElements.values());
                sharedElementTransition2 = sharedElementTransition;
            }
            if (enterTransition == null && exitTransition == null && sharedElementTransition2 == null) {
                return null;
            }
            callSharedElementStartEnd(inFragment, outFragment, inIsPop, outSharedElements, true);
            if (sharedElementTransition2 != null) {
                Rect inEpicenter2 = new Rect();
                impl.setSharedElementTargets(sharedElementTransition2, nonExistentView, sharedElementsOut);
                setOutEpicenter(impl, sharedElementTransition2, exitTransition, outSharedElements, fragments.firstOutIsPop, fragments.firstOutTransaction);
                if (enterTransition != null) {
                    impl.setEpicenter(enterTransition, inEpicenter2);
                }
                inEpicenter = inEpicenter2;
            } else {
                inEpicenter = null;
            }
            OneShotPreDrawListener.add(sceneRoot, new Runnable() { // from class: androidx.fragment.app.FragmentTransition.6
                @Override // java.lang.Runnable
                public void run() {
                    ArrayMap<String, View> inSharedElements = FragmentTransition.captureInSharedElements(FragmentTransitionImpl.this, nameOverrides, sharedElementTransition2, fragments);
                    if (inSharedElements != null) {
                        sharedElementsIn.addAll(inSharedElements.values());
                        sharedElementsIn.add(nonExistentView);
                    }
                    FragmentTransition.callSharedElementStartEnd(inFragment, outFragment, inIsPop, inSharedElements, false);
                    Object obj = sharedElementTransition2;
                    if (obj != null) {
                        FragmentTransitionImpl.this.swapSharedElementTargets(obj, sharedElementsOut, sharedElementsIn);
                        View inEpicenterView = FragmentTransition.getInEpicenterView(inSharedElements, fragments, enterTransition, inIsPop);
                        if (inEpicenterView != null) {
                            FragmentTransitionImpl.this.getBoundsOnScreen(inEpicenterView, inEpicenter);
                        }
                    }
                }
            });
            return sharedElementTransition2;
        }
        return null;
    }

    private static ArrayMap<String, View> captureOutSharedElements(FragmentTransitionImpl impl, ArrayMap<String, String> nameOverrides, Object sharedElementTransition, FragmentContainerTransition fragments) {
        ArrayList<String> names;
        SharedElementCallback sharedElementCallback;
        if (nameOverrides.isEmpty() || sharedElementTransition == null) {
            nameOverrides.clear();
            return null;
        }
        Fragment outFragment = fragments.firstOut;
        ArrayMap<String, View> outSharedElements = new ArrayMap<>();
        impl.findNamedViews(outSharedElements, outFragment.requireView());
        BackStackRecord outTransaction = fragments.firstOutTransaction;
        if (fragments.firstOutIsPop) {
            sharedElementCallback = outFragment.getEnterTransitionCallback();
            names = outTransaction.mSharedElementTargetNames;
        } else {
            sharedElementCallback = outFragment.getExitTransitionCallback();
            names = outTransaction.mSharedElementSourceNames;
        }
        if (names != null) {
            outSharedElements.retainAll(names);
        }
        if (sharedElementCallback != null) {
            sharedElementCallback.onMapSharedElements(names, outSharedElements);
            for (int i = names.size() - 1; i >= 0; i--) {
                String name = names.get(i);
                View view = outSharedElements.get(name);
                if (view == null) {
                    nameOverrides.remove(name);
                } else if (!name.equals(ViewCompat.getTransitionName(view))) {
                    nameOverrides.put(ViewCompat.getTransitionName(view), nameOverrides.remove(name));
                }
            }
        } else {
            nameOverrides.retainAll(outSharedElements.keySet());
        }
        return outSharedElements;
    }

    static ArrayMap<String, View> captureInSharedElements(FragmentTransitionImpl impl, ArrayMap<String, String> nameOverrides, Object sharedElementTransition, FragmentContainerTransition fragments) {
        ArrayList<String> names;
        SharedElementCallback sharedElementCallback;
        String key;
        Fragment inFragment = fragments.lastIn;
        View fragmentView = inFragment.getView();
        if (nameOverrides.isEmpty() || sharedElementTransition == null || fragmentView == null) {
            nameOverrides.clear();
            return null;
        }
        ArrayMap<String, View> inSharedElements = new ArrayMap<>();
        impl.findNamedViews(inSharedElements, fragmentView);
        BackStackRecord inTransaction = fragments.lastInTransaction;
        if (fragments.lastInIsPop) {
            sharedElementCallback = inFragment.getExitTransitionCallback();
            names = inTransaction.mSharedElementSourceNames;
        } else {
            sharedElementCallback = inFragment.getEnterTransitionCallback();
            names = inTransaction.mSharedElementTargetNames;
        }
        if (names != null) {
            inSharedElements.retainAll(names);
            inSharedElements.retainAll(nameOverrides.values());
        }
        if (sharedElementCallback != null) {
            sharedElementCallback.onMapSharedElements(names, inSharedElements);
            for (int i = names.size() - 1; i >= 0; i--) {
                String name = names.get(i);
                View view = inSharedElements.get(name);
                if (view == null) {
                    String key2 = findKeyForValue(nameOverrides, name);
                    if (key2 != null) {
                        nameOverrides.remove(key2);
                    }
                } else if (!name.equals(ViewCompat.getTransitionName(view)) && (key = findKeyForValue(nameOverrides, name)) != null) {
                    nameOverrides.put(key, ViewCompat.getTransitionName(view));
                }
            }
        } else {
            retainValues(nameOverrides, inSharedElements);
        }
        return inSharedElements;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String findKeyForValue(ArrayMap<String, String> map, String value) {
        int numElements = map.size();
        for (int i = 0; i < numElements; i++) {
            if (value.equals(map.valueAt(i))) {
                return map.keyAt(i);
            }
        }
        return null;
    }

    static View getInEpicenterView(ArrayMap<String, View> inSharedElements, FragmentContainerTransition fragments, Object enterTransition, boolean inIsPop) {
        String targetName;
        BackStackRecord inTransaction = fragments.lastInTransaction;
        if (enterTransition == null || inSharedElements == null || inTransaction.mSharedElementSourceNames == null || inTransaction.mSharedElementSourceNames.isEmpty()) {
            return null;
        }
        if (inIsPop) {
            targetName = (String) inTransaction.mSharedElementSourceNames.get(0);
        } else {
            targetName = (String) inTransaction.mSharedElementTargetNames.get(0);
        }
        return inSharedElements.get(targetName);
    }

    private static void setOutEpicenter(FragmentTransitionImpl impl, Object sharedElementTransition, Object exitTransition, ArrayMap<String, View> outSharedElements, boolean outIsPop, BackStackRecord outTransaction) {
        String sourceName;
        if (outTransaction.mSharedElementSourceNames != null && !outTransaction.mSharedElementSourceNames.isEmpty()) {
            if (outIsPop) {
                sourceName = (String) outTransaction.mSharedElementTargetNames.get(0);
            } else {
                sourceName = (String) outTransaction.mSharedElementSourceNames.get(0);
            }
            View outEpicenterView = outSharedElements.get(sourceName);
            impl.setEpicenter(sharedElementTransition, outEpicenterView);
            if (exitTransition != null) {
                impl.setEpicenter(exitTransition, outEpicenterView);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void retainValues(ArrayMap<String, String> nameOverrides, ArrayMap<String, View> namedViews) {
        for (int i = nameOverrides.size() - 1; i >= 0; i--) {
            if (!namedViews.containsKey(nameOverrides.valueAt(i))) {
                nameOverrides.removeAt(i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void callSharedElementStartEnd(Fragment inFragment, Fragment outFragment, boolean isPop, ArrayMap<String, View> sharedElements, boolean isStart) {
        SharedElementCallback sharedElementCallback;
        if (isPop) {
            sharedElementCallback = outFragment.getEnterTransitionCallback();
        } else {
            sharedElementCallback = inFragment.getEnterTransitionCallback();
        }
        if (sharedElementCallback != null) {
            ArrayList<View> views = new ArrayList<>();
            ArrayList<String> names = new ArrayList<>();
            int count = sharedElements == null ? 0 : sharedElements.size();
            for (int i = 0; i < count; i++) {
                names.add(sharedElements.keyAt(i));
                views.add(sharedElements.valueAt(i));
            }
            if (isStart) {
                sharedElementCallback.onSharedElementStart(names, views, null);
            } else {
                sharedElementCallback.onSharedElementEnd(names, views, null);
            }
        }
    }

    static ArrayList<View> configureEnteringExitingViews(FragmentTransitionImpl impl, Object transition, Fragment fragment, ArrayList<View> sharedElements, View nonExistentView) {
        ArrayList<View> viewList = null;
        if (transition != null) {
            viewList = new ArrayList<>();
            View root = fragment.getView();
            if (root != null) {
                impl.captureTransitioningViews(viewList, root);
            }
            if (sharedElements != null) {
                viewList.removeAll(sharedElements);
            }
            if (!viewList.isEmpty()) {
                viewList.add(nonExistentView);
                impl.addTargets(transition, viewList);
            }
        }
        return viewList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setViewVisibility(ArrayList<View> views, int visibility) {
        if (views != null) {
            for (int i = views.size() - 1; i >= 0; i--) {
                views.get(i).setVisibility(visibility);
            }
        }
    }

    private static Object mergeTransitions(FragmentTransitionImpl impl, Object enterTransition, Object exitTransition, Object sharedElementTransition, Fragment inFragment, boolean isPop) {
        boolean z;
        boolean overlap = true;
        if (!(enterTransition == null || exitTransition == null || inFragment == null)) {
            if (isPop) {
                z = inFragment.getAllowReturnTransitionOverlap();
            } else {
                z = inFragment.getAllowEnterTransitionOverlap();
            }
            overlap = z;
        }
        if (overlap) {
            return impl.mergeTransitionsTogether(exitTransition, enterTransition, sharedElementTransition);
        }
        return impl.mergeTransitionsInSequence(exitTransition, enterTransition, sharedElementTransition);
    }

    public static void calculateFragments(BackStackRecord transaction, SparseArray<FragmentContainerTransition> transitioningFragments, boolean isReordered) {
        int numOps = transaction.mOps.size();
        for (int opNum = 0; opNum < numOps; opNum++) {
            addToFirstInLastOut(transaction, (FragmentTransaction.Op) transaction.mOps.get(opNum), transitioningFragments, false, isReordered);
        }
    }

    public static void calculatePopFragments(BackStackRecord transaction, SparseArray<FragmentContainerTransition> transitioningFragments, boolean isReordered) {
        if (transaction.mManager.getContainer().onHasView()) {
            for (int opNum = transaction.mOps.size() - 1; opNum >= 0; opNum--) {
                addToFirstInLastOut(transaction, (FragmentTransaction.Op) transaction.mOps.get(opNum), transitioningFragments, true, isReordered);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean supportsTransition() {
        return (PLATFORM_IMPL == null && SUPPORT_IMPL == null) ? false : true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0032, code lost:
        if (r6 != 7) goto L_0x00a4;
     */
    /* JADX WARN: Removed duplicated region for block: B:102:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x00ad  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x00ba A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x00c8  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x00db A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x00ee A[ADDED_TO_REGION] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private static void addToFirstInLastOut(BackStackRecord transaction, FragmentTransaction.Op op, SparseArray<FragmentContainerTransition> transitioningFragments, boolean isPop, boolean isReorderedTransaction) {
        int containerId;
        FragmentContainerTransition containerTransition;
        Fragment fragment = op.mFragment;
        if (fragment != null && (containerId = fragment.mContainerId) != 0) {
            int command = isPop ? INVERSE_OPS[op.mCmd] : op.mCmd;
            boolean setLastIn = false;
            boolean wasRemoved = false;
            boolean setFirstOut = false;
            boolean wasAdded = false;
            boolean z = false;
            if (command != 1) {
                if (command != 3) {
                    if (command == 4) {
                        if (isReorderedTransaction) {
                            if (fragment.mHiddenChanged && fragment.mAdded && fragment.mHidden) {
                                z = true;
                            }
                            setFirstOut = z;
                        } else {
                            if (fragment.mAdded && !fragment.mHidden) {
                                z = true;
                            }
                            setFirstOut = z;
                        }
                        wasRemoved = true;
                    } else if (command == 5) {
                        if (isReorderedTransaction) {
                            if (fragment.mHiddenChanged && !fragment.mHidden && fragment.mAdded) {
                                z = true;
                            }
                            setLastIn = z;
                        } else {
                            setLastIn = fragment.mHidden;
                        }
                        wasAdded = true;
                    } else if (command != 6) {
                    }
                    containerTransition = transitioningFragments.get(containerId);
                    if (setLastIn) {
                        containerTransition = ensureContainer(containerTransition, transitioningFragments, containerId);
                        containerTransition.lastIn = fragment;
                        containerTransition.lastInIsPop = isPop;
                        containerTransition.lastInTransaction = transaction;
                    }
                    if (!isReorderedTransaction && wasAdded) {
                        if (containerTransition != null && containerTransition.firstOut == fragment) {
                            containerTransition.firstOut = null;
                        }
                        if (!transaction.mReorderingAllowed) {
                            FragmentManager manager = transaction.mManager;
                            manager.getFragmentStore().makeActive(manager.createOrGetFragmentStateManager(fragment));
                            manager.moveToState(fragment);
                        }
                    }
                    if (setFirstOut && (containerTransition == null || containerTransition.firstOut == null)) {
                        containerTransition = ensureContainer(containerTransition, transitioningFragments, containerId);
                        containerTransition.firstOut = fragment;
                        containerTransition.firstOutIsPop = isPop;
                        containerTransition.firstOutTransaction = transaction;
                    }
                    if (isReorderedTransaction && wasRemoved && containerTransition != null && containerTransition.lastIn == fragment) {
                        containerTransition.lastIn = null;
                        return;
                    }
                    return;
                }
                if (isReorderedTransaction) {
                    if (!fragment.mAdded && fragment.mView != null && fragment.mView.getVisibility() == 0 && fragment.mPostponedAlpha >= 0.0f) {
                        z = true;
                    }
                    setFirstOut = z;
                } else {
                    if (fragment.mAdded && !fragment.mHidden) {
                        z = true;
                    }
                    setFirstOut = z;
                }
                wasRemoved = true;
                containerTransition = transitioningFragments.get(containerId);
                if (setLastIn) {
                }
                if (!isReorderedTransaction) {
                    if (containerTransition != null) {
                        containerTransition.firstOut = null;
                    }
                    if (!transaction.mReorderingAllowed) {
                    }
                }
                if (setFirstOut) {
                    containerTransition = ensureContainer(containerTransition, transitioningFragments, containerId);
                    containerTransition.firstOut = fragment;
                    containerTransition.firstOutIsPop = isPop;
                    containerTransition.firstOutTransaction = transaction;
                }
                if (isReorderedTransaction) {
                    return;
                }
                return;
            }
            if (isReorderedTransaction) {
                setLastIn = fragment.mIsNewlyAdded;
            } else {
                if (!fragment.mAdded && !fragment.mHidden) {
                    z = true;
                }
                setLastIn = z;
            }
            wasAdded = true;
            containerTransition = transitioningFragments.get(containerId);
            if (setLastIn) {
            }
            if (!isReorderedTransaction) {
            }
            if (setFirstOut) {
            }
            if (isReorderedTransaction) {
            }
        }
    }

    private static FragmentContainerTransition ensureContainer(FragmentContainerTransition containerTransition, SparseArray<FragmentContainerTransition> transitioningFragments, int containerId) {
        if (containerTransition != null) {
            return containerTransition;
        }
        FragmentContainerTransition containerTransition2 = new FragmentContainerTransition();
        transitioningFragments.put(containerId, containerTransition2);
        return containerTransition2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class FragmentContainerTransition {
        public Fragment firstOut;
        public boolean firstOutIsPop;
        public BackStackRecord firstOutTransaction;
        public Fragment lastIn;
        public boolean lastInIsPop;
        public BackStackRecord lastInTransaction;

        FragmentContainerTransition() {
        }
    }

    private FragmentTransition() {
    }
}
