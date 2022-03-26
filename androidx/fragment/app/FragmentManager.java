package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.ActivityResultRegistryOwner;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.collection.ArraySet;
import androidx.core.os.CancellationSignal;
import androidx.fragment.R;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentAnim;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentTransition;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public abstract class FragmentManager implements FragmentResultOwner {
    private static final String EXTRA_CREATED_FILLIN_INTENT;
    public static final int POP_BACK_STACK_INCLUSIVE;
    static final String TAG;
    ArrayList<BackStackRecord> mBackStack;
    private ArrayList<OnBackStackChangedListener> mBackStackChangeListeners;
    private FragmentContainer mContainer;
    private ArrayList<Fragment> mCreatedMenus;
    private boolean mDestroyed;
    private boolean mExecutingActions;
    private boolean mHavePendingDeferredStart;
    private FragmentHostCallback<?> mHost;
    private boolean mNeedMenuInvalidate;
    private FragmentManagerViewModel mNonConfig;
    private OnBackPressedDispatcher mOnBackPressedDispatcher;
    private Fragment mParent;
    private ArrayList<StartEnterTransitionListener> mPostponedTransactions;
    Fragment mPrimaryNav;
    private ActivityResultLauncher<String[]> mRequestPermissions;
    private ActivityResultLauncher<Intent> mStartActivityForResult;
    private ActivityResultLauncher<IntentSenderRequest> mStartIntentSenderForResult;
    private boolean mStateSaved;
    private boolean mStopped;
    private ArrayList<Fragment> mTmpAddedFragments;
    private ArrayList<Boolean> mTmpIsPop;
    private ArrayList<BackStackRecord> mTmpRecords;
    private static boolean DEBUG = false;
    static boolean USE_STATE_MANAGER = true;
    private final ArrayList<OpGenerator> mPendingActions = new ArrayList<>();
    private final FragmentStore mFragmentStore = new FragmentStore();
    private final FragmentLayoutInflaterFactory mLayoutInflaterFactory = new FragmentLayoutInflaterFactory(this);
    private final OnBackPressedCallback mOnBackPressedCallback = new OnBackPressedCallback(false) { // from class: androidx.fragment.app.FragmentManager.1
        @Override // androidx.activity.OnBackPressedCallback
        public void handleOnBackPressed() {
            FragmentManager.this.handleOnBackPressed();
        }
    };
    private final AtomicInteger mBackStackIndex = new AtomicInteger();
    private final Map<String, Bundle> mResults = Collections.synchronizedMap(new HashMap());
    private final Map<String, LifecycleAwareResultListener> mResultListeners = Collections.synchronizedMap(new HashMap());
    private Map<Fragment, HashSet<CancellationSignal>> mExitAnimationCancellationSignals = Collections.synchronizedMap(new HashMap());
    private final FragmentTransition.Callback mFragmentTransitionCallback = new FragmentTransition.Callback() { // from class: androidx.fragment.app.FragmentManager.2
        @Override // androidx.fragment.app.FragmentTransition.Callback
        public void onStart(Fragment fragment, CancellationSignal signal) {
            FragmentManager.this.addCancellationSignal(fragment, signal);
        }

        @Override // androidx.fragment.app.FragmentTransition.Callback
        public void onComplete(Fragment f, CancellationSignal signal) {
            if (!signal.isCanceled()) {
                FragmentManager.this.removeCancellationSignal(f, signal);
            }
        }
    };
    private final FragmentLifecycleCallbacksDispatcher mLifecycleCallbacksDispatcher = new FragmentLifecycleCallbacksDispatcher(this);
    private final CopyOnWriteArrayList<FragmentOnAttachListener> mOnAttachListeners = new CopyOnWriteArrayList<>();
    int mCurState = -1;
    private FragmentFactory mFragmentFactory = null;
    private FragmentFactory mHostFragmentFactory = new FragmentFactory() { // from class: androidx.fragment.app.FragmentManager.3
        @Override // androidx.fragment.app.FragmentFactory
        public Fragment instantiate(ClassLoader classLoader, String className) {
            return FragmentManager.this.getHost().instantiate(FragmentManager.this.getHost().getContext(), className, null);
        }
    };
    private SpecialEffectsControllerFactory mSpecialEffectsControllerFactory = null;
    private SpecialEffectsControllerFactory mDefaultSpecialEffectsControllerFactory = new SpecialEffectsControllerFactory() { // from class: androidx.fragment.app.FragmentManager.4
        @Override // androidx.fragment.app.SpecialEffectsControllerFactory
        public SpecialEffectsController createController(ViewGroup container) {
            return new DefaultSpecialEffectsController(container);
        }
    };
    ArrayDeque<LaunchedFragmentInfo> mLaunchedFragments = new ArrayDeque<>();
    private Runnable mExecCommit = new Runnable() { // from class: androidx.fragment.app.FragmentManager.5
        @Override // java.lang.Runnable
        public void run() {
            FragmentManager.this.execPendingActions(true);
        }
    };

    /* loaded from: classes.dex */
    public interface BackStackEntry {
        @Deprecated
        CharSequence getBreadCrumbShortTitle();

        @Deprecated
        int getBreadCrumbShortTitleRes();

        @Deprecated
        CharSequence getBreadCrumbTitle();

        @Deprecated
        int getBreadCrumbTitleRes();

        int getId();

        String getName();
    }

    /* loaded from: classes.dex */
    public interface OnBackStackChangedListener {
        void onBackStackChanged();
    }

    /* loaded from: classes.dex */
    public interface OpGenerator {
        boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2);
    }

    public static void enableNewStateManager(boolean enabled) {
        USE_STATE_MANAGER = enabled;
    }

    @Deprecated
    public static void enableDebugLogging(boolean enabled) {
        DEBUG = enabled;
    }

    public static boolean isLoggingEnabled(int level) {
        return DEBUG || Log.isLoggable(TAG, level);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class LifecycleAwareResultListener implements FragmentResultListener {
        private final Lifecycle mLifecycle;
        private final FragmentResultListener mListener;
        private final LifecycleEventObserver mObserver;

        LifecycleAwareResultListener(Lifecycle lifecycle, FragmentResultListener listener, LifecycleEventObserver observer) {
            this.mLifecycle = lifecycle;
            this.mListener = listener;
            this.mObserver = observer;
        }

        public boolean isAtLeast(Lifecycle.State state) {
            return this.mLifecycle.getCurrentState().isAtLeast(state);
        }

        @Override // androidx.fragment.app.FragmentResultListener
        public void onFragmentResult(String requestKey, Bundle result) {
            this.mListener.onFragmentResult(requestKey, result);
        }

        public void removeObserver() {
            this.mLifecycle.removeObserver(this.mObserver);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class FragmentLifecycleCallbacks {
        public void onFragmentPreAttached(FragmentManager fm, Fragment f, Context context) {
        }

        public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
        }

        public void onFragmentPreCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        }

        public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        }

        @Deprecated
        public void onFragmentActivityCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        }

        public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
        }

        public void onFragmentStarted(FragmentManager fm, Fragment f) {
        }

        public void onFragmentResumed(FragmentManager fm, Fragment f) {
        }

        public void onFragmentPaused(FragmentManager fm, Fragment f) {
        }

        public void onFragmentStopped(FragmentManager fm, Fragment f) {
        }

        public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
        }

        public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
        }

        public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
        }

        public void onFragmentDetached(FragmentManager fm, Fragment f) {
        }
    }

    private void throwException(RuntimeException ex) {
        Log.e(TAG, ex.getMessage());
        Log.e(TAG, "Activity state:");
        PrintWriter pw = new PrintWriter(new LogWriter(TAG));
        FragmentHostCallback<?> fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            try {
                fragmentHostCallback.onDump("  ", null, pw, new String[0]);
            } catch (Exception e) {
                Log.e(TAG, "Failed dumping state", e);
            }
        } else {
            try {
                dump("  ", null, pw, new String[0]);
            } catch (Exception e2) {
                Log.e(TAG, "Failed dumping state", e2);
            }
        }
        throw ex;
    }

    @Deprecated
    public FragmentTransaction openTransaction() {
        return beginTransaction();
    }

    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }

    public boolean executePendingTransactions() {
        boolean updates = execPendingActions(true);
        forcePostponedTransactions();
        return updates;
    }

    private void updateOnBackPressedCallbackEnabled() {
        synchronized (this.mPendingActions) {
            boolean z = true;
            if (!this.mPendingActions.isEmpty()) {
                this.mOnBackPressedCallback.setEnabled(true);
                return;
            }
            OnBackPressedCallback onBackPressedCallback = this.mOnBackPressedCallback;
            if (getBackStackEntryCount() <= 0 || !isPrimaryNavigation(this.mParent)) {
                z = false;
            }
            onBackPressedCallback.setEnabled(z);
        }
    }

    public boolean isPrimaryNavigation(Fragment parent) {
        if (parent == null) {
            return true;
        }
        FragmentManager parentFragmentManager = parent.mFragmentManager;
        if (!parent.equals(parentFragmentManager.getPrimaryNavigationFragment()) || !isPrimaryNavigation(parentFragmentManager.mParent)) {
            return false;
        }
        return true;
    }

    public boolean isParentMenuVisible(Fragment parent) {
        if (parent == null) {
            return true;
        }
        return parent.isMenuVisible();
    }

    void handleOnBackPressed() {
        execPendingActions(true);
        if (this.mOnBackPressedCallback.isEnabled()) {
            popBackStackImmediate();
        } else {
            this.mOnBackPressedDispatcher.onBackPressed();
        }
    }

    public void popBackStack() {
        enqueueAction(new PopBackStackState(null, -1, 0), false);
    }

    public boolean popBackStackImmediate() {
        return popBackStackImmediate(null, -1, 0);
    }

    public void popBackStack(String name, int flags) {
        enqueueAction(new PopBackStackState(name, -1, flags), false);
    }

    public boolean popBackStackImmediate(String name, int flags) {
        return popBackStackImmediate(name, -1, flags);
    }

    public void popBackStack(int id, int flags) {
        if (id >= 0) {
            enqueueAction(new PopBackStackState(null, id, flags), false);
            return;
        }
        throw new IllegalArgumentException("Bad id: " + id);
    }

    public boolean popBackStackImmediate(int id, int flags) {
        if (id >= 0) {
            return popBackStackImmediate(null, id, flags);
        }
        throw new IllegalArgumentException("Bad id: " + id);
    }

    private boolean popBackStackImmediate(String name, int id, int flags) {
        execPendingActions(false);
        ensureExecReady(true);
        Fragment fragment = this.mPrimaryNav;
        if (fragment != null && id < 0 && name == null && fragment.getChildFragmentManager().popBackStackImmediate()) {
            return true;
        }
        boolean executePop = popBackStackState(this.mTmpRecords, this.mTmpIsPop, name, id, flags);
        if (executePop) {
            this.mExecutingActions = true;
            try {
                removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            } finally {
                cleanupExec();
            }
        }
        updateOnBackPressedCallbackEnabled();
        doPendingDeferredStart();
        this.mFragmentStore.burpActive();
        return executePop;
    }

    public int getBackStackEntryCount() {
        ArrayList<BackStackRecord> arrayList = this.mBackStack;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    public BackStackEntry getBackStackEntryAt(int index) {
        return this.mBackStack.get(index);
    }

    public void addOnBackStackChangedListener(OnBackStackChangedListener listener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList<>();
        }
        this.mBackStackChangeListeners.add(listener);
    }

    public void removeOnBackStackChangedListener(OnBackStackChangedListener listener) {
        ArrayList<OnBackStackChangedListener> arrayList = this.mBackStackChangeListeners;
        if (arrayList != null) {
            arrayList.remove(listener);
        }
    }

    void addCancellationSignal(Fragment f, CancellationSignal signal) {
        if (this.mExitAnimationCancellationSignals.get(f) == null) {
            this.mExitAnimationCancellationSignals.put(f, new HashSet<>());
        }
        this.mExitAnimationCancellationSignals.get(f).add(signal);
    }

    void removeCancellationSignal(Fragment f, CancellationSignal signal) {
        HashSet<CancellationSignal> signals = this.mExitAnimationCancellationSignals.get(f);
        if (signals != null && signals.remove(signal) && signals.isEmpty()) {
            this.mExitAnimationCancellationSignals.remove(f);
            if (f.mState < 5) {
                destroyFragmentView(f);
                moveToState(f);
            }
        }
    }

    @Override // androidx.fragment.app.FragmentResultOwner
    public final void setFragmentResult(String requestKey, Bundle result) {
        LifecycleAwareResultListener resultListener = this.mResultListeners.get(requestKey);
        if (resultListener == null || !resultListener.isAtLeast(Lifecycle.State.STARTED)) {
            this.mResults.put(requestKey, result);
        } else {
            resultListener.onFragmentResult(requestKey, result);
        }
    }

    @Override // androidx.fragment.app.FragmentResultOwner
    public final void clearFragmentResult(String requestKey) {
        this.mResults.remove(requestKey);
    }

    @Override // androidx.fragment.app.FragmentResultOwner
    public final void setFragmentResultListener(final String requestKey, LifecycleOwner lifecycleOwner, final FragmentResultListener listener) {
        final Lifecycle lifecycle = lifecycleOwner.getLifecycle();
        if (lifecycle.getCurrentState() != Lifecycle.State.DESTROYED) {
            LifecycleEventObserver observer = new LifecycleEventObserver() { // from class: androidx.fragment.app.FragmentManager.6
                @Override // androidx.lifecycle.LifecycleEventObserver
                public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
                    Bundle storedResult;
                    if (event == Lifecycle.Event.ON_START && (storedResult = (Bundle) FragmentManager.this.mResults.get(requestKey)) != null) {
                        listener.onFragmentResult(requestKey, storedResult);
                        FragmentManager.this.clearFragmentResult(requestKey);
                    }
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        lifecycle.removeObserver(this);
                        FragmentManager.this.mResultListeners.remove(requestKey);
                    }
                }
            };
            lifecycle.addObserver(observer);
            LifecycleAwareResultListener storedListener = this.mResultListeners.put(requestKey, new LifecycleAwareResultListener(lifecycle, listener, observer));
            if (storedListener != null) {
                storedListener.removeObserver();
            }
        }
    }

    @Override // androidx.fragment.app.FragmentResultOwner
    public final void clearFragmentResultListener(String requestKey) {
        LifecycleAwareResultListener listener = this.mResultListeners.remove(requestKey);
        if (listener != null) {
            listener.removeObserver();
        }
    }

    public void putFragment(Bundle bundle, String key, Fragment fragment) {
        if (fragment.mFragmentManager != this) {
            throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        bundle.putString(key, fragment.mWho);
    }

    public Fragment getFragment(Bundle bundle, String key) {
        String who = bundle.getString(key);
        if (who == null) {
            return null;
        }
        Fragment f = findActiveFragment(who);
        if (f == null) {
            throwException(new IllegalStateException("Fragment no longer exists for key " + key + ": unique id " + who));
        }
        return f;
    }

    public static <F extends Fragment> F findFragment(View view) {
        F f = (F) findViewFragment(view);
        if (f != null) {
            return f;
        }
        throw new IllegalStateException("View " + view + " does not have a Fragment set");
    }

    private static Fragment findViewFragment(View view) {
        while (true) {
            View view2 = null;
            if (view == null) {
                return null;
            }
            Fragment fragment = getViewFragment(view);
            if (fragment != null) {
                return fragment;
            }
            ViewParent parent = view.getParent();
            if (parent instanceof View) {
                view2 = (View) parent;
            }
            view = view2;
        }
    }

    public static Fragment getViewFragment(View view) {
        Object tag = view.getTag(R.id.fragment_container_view_tag);
        if (tag instanceof Fragment) {
            return (Fragment) tag;
        }
        return null;
    }

    public void onContainerAvailable(FragmentContainerView container) {
        for (FragmentStateManager fragmentStateManager : this.mFragmentStore.getActiveFragmentStateManagers()) {
            Fragment fragment = fragmentStateManager.getFragment();
            if (fragment.mContainerId == container.getId() && fragment.mView != null && fragment.mView.getParent() == null) {
                fragment.mContainer = container;
                fragmentStateManager.addViewToContainer();
            }
        }
    }

    static FragmentManager findFragmentManager(View view) {
        Fragment fragment = findViewFragment(view);
        if (fragment == null) {
            Context context = view.getContext();
            FragmentActivity fragmentActivity = null;
            while (true) {
                if (!(context instanceof ContextWrapper)) {
                    break;
                } else if (context instanceof FragmentActivity) {
                    fragmentActivity = (FragmentActivity) context;
                    break;
                } else {
                    context = ((ContextWrapper) context).getBaseContext();
                }
            }
            if (fragmentActivity != null) {
                return fragmentActivity.getSupportFragmentManager();
            }
            throw new IllegalStateException("View " + view + " is not within a subclass of FragmentActivity.");
        } else if (fragment.isAdded()) {
            return fragment.getChildFragmentManager();
        } else {
            throw new IllegalStateException("The Fragment " + fragment + " that owns View " + view + " has already been destroyed. Nested fragments should always use the child FragmentManager.");
        }
    }

    public List<Fragment> getFragments() {
        return this.mFragmentStore.getFragments();
    }

    public ViewModelStore getViewModelStore(Fragment f) {
        return this.mNonConfig.getViewModelStore(f);
    }

    private FragmentManagerViewModel getChildNonConfig(Fragment f) {
        return this.mNonConfig.getChildNonConfig(f);
    }

    public void addRetainedFragment(Fragment f) {
        this.mNonConfig.addRetainedFragment(f);
    }

    public void removeRetainedFragment(Fragment f) {
        this.mNonConfig.removeRetainedFragment(f);
    }

    public List<Fragment> getActiveFragments() {
        return this.mFragmentStore.getActiveFragments();
    }

    public int getActiveFragmentCount() {
        return this.mFragmentStore.getActiveFragmentCount();
    }

    public Fragment.SavedState saveFragmentInstanceState(Fragment fragment) {
        FragmentStateManager fragmentStateManager = this.mFragmentStore.getFragmentStateManager(fragment.mWho);
        if (fragmentStateManager == null || !fragmentStateManager.getFragment().equals(fragment)) {
            throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        return fragmentStateManager.saveInstanceState();
    }

    public boolean isDestroyed() {
        return this.mDestroyed;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        Fragment fragment = this.mParent;
        if (fragment != null) {
            sb.append(fragment.getClass().getSimpleName());
            sb.append("{");
            sb.append(Integer.toHexString(System.identityHashCode(this.mParent)));
            sb.append("}");
        } else {
            FragmentHostCallback<?> fragmentHostCallback = this.mHost;
            if (fragmentHostCallback != null) {
                sb.append(fragmentHostCallback.getClass().getSimpleName());
                sb.append("{");
                sb.append(Integer.toHexString(System.identityHashCode(this.mHost)));
                sb.append("}");
            } else {
                sb.append("null");
            }
        }
        sb.append("}}");
        return sb.toString();
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        int count;
        int count2;
        String innerPrefix = prefix + "    ";
        this.mFragmentStore.dump(prefix, fd, writer, args);
        ArrayList<Fragment> arrayList = this.mCreatedMenus;
        if (arrayList != null && (count2 = arrayList.size()) > 0) {
            writer.print(prefix);
            writer.println("Fragments Created Menus:");
            for (int i = 0; i < count2; i++) {
                writer.print(prefix);
                writer.print("  #");
                writer.print(i);
                writer.print(": ");
                writer.println(this.mCreatedMenus.get(i).toString());
            }
        }
        ArrayList<BackStackRecord> arrayList2 = this.mBackStack;
        if (arrayList2 != null && (count = arrayList2.size()) > 0) {
            writer.print(prefix);
            writer.println("Back Stack:");
            for (int i2 = 0; i2 < count; i2++) {
                BackStackRecord bs = this.mBackStack.get(i2);
                writer.print(prefix);
                writer.print("  #");
                writer.print(i2);
                writer.print(": ");
                writer.println(bs.toString());
                bs.dump(innerPrefix, writer);
            }
        }
        writer.print(prefix);
        writer.println("Back Stack Index: " + this.mBackStackIndex.get());
        synchronized (this.mPendingActions) {
            int count3 = this.mPendingActions.size();
            if (count3 > 0) {
                writer.print(prefix);
                writer.println("Pending Actions:");
                for (int i3 = 0; i3 < count3; i3++) {
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i3);
                    writer.print(": ");
                    writer.println(this.mPendingActions.get(i3));
                }
            }
        }
        writer.print(prefix);
        writer.println("FragmentManager misc state:");
        writer.print(prefix);
        writer.print("  mHost=");
        writer.println(this.mHost);
        writer.print(prefix);
        writer.print("  mContainer=");
        writer.println(this.mContainer);
        if (this.mParent != null) {
            writer.print(prefix);
            writer.print("  mParent=");
            writer.println(this.mParent);
        }
        writer.print(prefix);
        writer.print("  mCurState=");
        writer.print(this.mCurState);
        writer.print(" mStateSaved=");
        writer.print(this.mStateSaved);
        writer.print(" mStopped=");
        writer.print(this.mStopped);
        writer.print(" mDestroyed=");
        writer.println(this.mDestroyed);
        if (this.mNeedMenuInvalidate) {
            writer.print(prefix);
            writer.print("  mNeedMenuInvalidate=");
            writer.println(this.mNeedMenuInvalidate);
        }
    }

    public void performPendingDeferredStart(FragmentStateManager fragmentStateManager) {
        Fragment f = fragmentStateManager.getFragment();
        if (!f.mDeferStart) {
            return;
        }
        if (this.mExecutingActions) {
            this.mHavePendingDeferredStart = true;
            return;
        }
        f.mDeferStart = false;
        if (USE_STATE_MANAGER) {
            fragmentStateManager.moveToExpectedState();
        } else {
            moveToState(f);
        }
    }

    public boolean isStateAtLeast(int state) {
        return this.mCurState >= state;
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0056, code lost:
        if (r2 != 5) goto L_0x016d;
     */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x006a  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x006f  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0074  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0079  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    void moveToState(Fragment f, int newState) {
        FragmentStateManager fragmentStateManager = this.mFragmentStore.getFragmentStateManager(f.mWho);
        if (fragmentStateManager == null) {
            fragmentStateManager = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, f);
            fragmentStateManager.setFragmentManagerState(1);
        }
        if (f.mFromLayout && f.mInLayout && f.mState == 2) {
            newState = Math.max(newState, 2);
        }
        int newState2 = Math.min(newState, fragmentStateManager.computeExpectedState());
        if (f.mState <= newState2) {
            if (f.mState < newState2 && !this.mExitAnimationCancellationSignals.isEmpty()) {
                cancelExitAnimation(f);
            }
            int i = f.mState;
            if (i != -1) {
                if (i != 0) {
                    if (i != 1) {
                        if (i != 2) {
                            if (i != 4) {
                            }
                            if (newState2 > 4) {
                                fragmentStateManager.start();
                            }
                            if (newState2 > 5) {
                                fragmentStateManager.resume();
                            }
                        }
                        if (newState2 > 2) {
                            fragmentStateManager.activityCreated();
                        }
                        if (newState2 > 4) {
                        }
                        if (newState2 > 5) {
                        }
                    }
                    if (newState2 > -1) {
                        fragmentStateManager.ensureInflatedView();
                    }
                    if (newState2 > 1) {
                        fragmentStateManager.createView();
                    }
                    if (newState2 > 2) {
                    }
                    if (newState2 > 4) {
                    }
                    if (newState2 > 5) {
                    }
                }
            } else if (newState2 > -1) {
                fragmentStateManager.attach();
            }
            if (newState2 > 0) {
                fragmentStateManager.create();
            }
            if (newState2 > -1) {
            }
            if (newState2 > 1) {
            }
            if (newState2 > 2) {
            }
            if (newState2 > 4) {
            }
            if (newState2 > 5) {
            }
        } else if (f.mState > newState2) {
            int i2 = f.mState;
            if (i2 != 0) {
                if (i2 != 1) {
                    if (i2 != 2) {
                        if (i2 != 4) {
                            if (i2 != 5) {
                                if (i2 == 7) {
                                    if (newState2 < 7) {
                                        fragmentStateManager.pause();
                                    }
                                }
                            }
                            if (newState2 < 5) {
                                fragmentStateManager.stop();
                            }
                        }
                        if (newState2 < 4) {
                            if (isLoggingEnabled(3)) {
                                Log.d(TAG, "movefrom ACTIVITY_CREATED: " + f);
                            }
                            if (f.mView != null && this.mHost.onShouldSaveFragmentState(f) && f.mSavedViewState == null) {
                                fragmentStateManager.saveViewState();
                            }
                        }
                    }
                    if (newState2 < 2) {
                        FragmentAnim.AnimationOrAnimator anim = null;
                        if (!(f.mView == null || f.mContainer == null)) {
                            f.mContainer.endViewTransition(f.mView);
                            f.mView.clearAnimation();
                            if (!f.isRemovingParent()) {
                                if (this.mCurState > -1 && !this.mDestroyed && f.mView.getVisibility() == 0 && f.mPostponedAlpha >= 0.0f) {
                                    anim = FragmentAnim.loadAnimation(this.mHost.getContext(), f, false, f.getPopDirection());
                                }
                                f.mPostponedAlpha = 0.0f;
                                ViewGroup container = f.mContainer;
                                View view = f.mView;
                                if (anim != null) {
                                    FragmentAnim.animateRemoveFragment(f, anim, this.mFragmentTransitionCallback);
                                }
                                container.removeView(view);
                                if (isLoggingEnabled(2)) {
                                    Log.v(TAG, "Removing view " + view + " for fragment " + f + " from container " + container);
                                }
                                if (container != f.mContainer) {
                                    return;
                                }
                            }
                        }
                        if (this.mExitAnimationCancellationSignals.get(f) == null) {
                            fragmentStateManager.destroyFragmentView();
                        }
                    }
                }
                if (newState2 < 1) {
                    if (this.mExitAnimationCancellationSignals.get(f) != null) {
                        newState2 = 1;
                    } else {
                        fragmentStateManager.destroy();
                    }
                }
            }
            if (newState2 < 0) {
                fragmentStateManager.detach();
            }
        }
        if (f.mState != newState2) {
            if (isLoggingEnabled(3)) {
                Log.d(TAG, "moveToState: Fragment state for " + f + " not updated inline; expected state " + newState2 + " found " + f.mState);
            }
            f.mState = newState2;
        }
    }

    private void cancelExitAnimation(Fragment f) {
        HashSet<CancellationSignal> signals = this.mExitAnimationCancellationSignals.get(f);
        if (signals != null) {
            Iterator<CancellationSignal> it = signals.iterator();
            while (it.hasNext()) {
                it.next().cancel();
            }
            signals.clear();
            destroyFragmentView(f);
            this.mExitAnimationCancellationSignals.remove(f);
        }
    }

    public void setExitAnimationOrder(Fragment f, boolean isPop) {
        ViewGroup container = getFragmentContainer(f);
        if (container != null && (container instanceof FragmentContainerView)) {
            ((FragmentContainerView) container).setDrawDisappearingViewsLast(!isPop);
        }
    }

    private void destroyFragmentView(Fragment fragment) {
        fragment.performDestroyView();
        this.mLifecycleCallbacksDispatcher.dispatchOnFragmentViewDestroyed(fragment, false);
        fragment.mContainer = null;
        fragment.mView = null;
        fragment.mViewLifecycleOwner = null;
        fragment.mViewLifecycleOwnerLiveData.setValue(null);
        fragment.mInLayout = false;
    }

    public void moveToState(Fragment f) {
        moveToState(f, this.mCurState);
    }

    private void completeShowHideFragment(final Fragment fragment) {
        int visibility;
        if (fragment.mView != null) {
            FragmentAnim.AnimationOrAnimator anim = FragmentAnim.loadAnimation(this.mHost.getContext(), fragment, !fragment.mHidden, fragment.getPopDirection());
            if (anim == null || anim.animator == null) {
                if (anim != null) {
                    fragment.mView.startAnimation(anim.animation);
                    anim.animation.start();
                }
                if (!fragment.mHidden || fragment.isHideReplaced()) {
                    visibility = 0;
                } else {
                    visibility = 8;
                }
                fragment.mView.setVisibility(visibility);
                if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                }
            } else {
                anim.animator.setTarget(fragment.mView);
                if (!fragment.mHidden) {
                    fragment.mView.setVisibility(0);
                } else if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                } else {
                    final ViewGroup container = fragment.mContainer;
                    final View animatingView = fragment.mView;
                    container.startViewTransition(animatingView);
                    anim.animator.addListener(new AnimatorListenerAdapter() { // from class: androidx.fragment.app.FragmentManager.7
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animation) {
                            container.endViewTransition(animatingView);
                            animation.removeListener(this);
                            if (fragment.mView != null && fragment.mHidden) {
                                fragment.mView.setVisibility(8);
                            }
                        }
                    });
                }
                anim.animator.start();
            }
        }
        invalidateMenuForFragment(fragment);
        fragment.mHiddenChanged = false;
        fragment.onHiddenChanged(fragment.mHidden);
    }

    public void moveFragmentToExpectedState(Fragment f) {
        if (this.mFragmentStore.containsActiveFragment(f.mWho)) {
            moveToState(f);
            if (!(f.mView == null || !f.mIsNewlyAdded || f.mContainer == null)) {
                if (f.mPostponedAlpha > 0.0f) {
                    f.mView.setAlpha(f.mPostponedAlpha);
                }
                f.mPostponedAlpha = 0.0f;
                f.mIsNewlyAdded = false;
                FragmentAnim.AnimationOrAnimator anim = FragmentAnim.loadAnimation(this.mHost.getContext(), f, true, f.getPopDirection());
                if (anim != null) {
                    if (anim.animation != null) {
                        f.mView.startAnimation(anim.animation);
                    } else {
                        anim.animator.setTarget(f.mView);
                        anim.animator.start();
                    }
                }
            }
            if (f.mHiddenChanged) {
                completeShowHideFragment(f);
            }
        } else if (isLoggingEnabled(3)) {
            Log.d(TAG, "Ignoring moving " + f + " to state " + this.mCurState + "since it is not added to " + this);
        }
    }

    public void moveToState(int newState, boolean always) {
        FragmentHostCallback<?> fragmentHostCallback;
        if (this.mHost == null && newState != -1) {
            throw new IllegalStateException("No activity");
        } else if (always || newState != this.mCurState) {
            this.mCurState = newState;
            if (USE_STATE_MANAGER) {
                this.mFragmentStore.moveToExpectedState();
            } else {
                for (Fragment f : this.mFragmentStore.getFragments()) {
                    moveFragmentToExpectedState(f);
                }
                for (FragmentStateManager fragmentStateManager : this.mFragmentStore.getActiveFragmentStateManagers()) {
                    Fragment f2 = fragmentStateManager.getFragment();
                    if (!f2.mIsNewlyAdded) {
                        moveFragmentToExpectedState(f2);
                    }
                    if (f2.mRemoving && !f2.isInBackStack()) {
                        this.mFragmentStore.makeInactive(fragmentStateManager);
                    }
                }
            }
            startPendingDeferredFragments();
            if (this.mNeedMenuInvalidate && (fragmentHostCallback = this.mHost) != null && this.mCurState == 7) {
                fragmentHostCallback.onSupportInvalidateOptionsMenu();
                this.mNeedMenuInvalidate = false;
            }
        }
    }

    private void startPendingDeferredFragments() {
        for (FragmentStateManager fragmentStateManager : this.mFragmentStore.getActiveFragmentStateManagers()) {
            performPendingDeferredStart(fragmentStateManager);
        }
    }

    public FragmentStateManager createOrGetFragmentStateManager(Fragment f) {
        FragmentStateManager existing = this.mFragmentStore.getFragmentStateManager(f.mWho);
        if (existing != null) {
            return existing;
        }
        FragmentStateManager fragmentStateManager = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, f);
        fragmentStateManager.restoreState(this.mHost.getContext().getClassLoader());
        fragmentStateManager.setFragmentManagerState(this.mCurState);
        return fragmentStateManager;
    }

    public FragmentStateManager addFragment(Fragment fragment) {
        if (isLoggingEnabled(2)) {
            Log.v(TAG, "add: " + fragment);
        }
        FragmentStateManager fragmentStateManager = createOrGetFragmentStateManager(fragment);
        fragment.mFragmentManager = this;
        this.mFragmentStore.makeActive(fragmentStateManager);
        if (!fragment.mDetached) {
            this.mFragmentStore.addFragment(fragment);
            fragment.mRemoving = false;
            if (fragment.mView == null) {
                fragment.mHiddenChanged = false;
            }
            if (isMenuAvailable(fragment)) {
                this.mNeedMenuInvalidate = true;
            }
        }
        return fragmentStateManager;
    }

    public void removeFragment(Fragment fragment) {
        if (isLoggingEnabled(2)) {
            Log.v(TAG, "remove: " + fragment + " nesting=" + fragment.mBackStackNesting);
        }
        boolean inactive = !fragment.isInBackStack();
        if (!fragment.mDetached || inactive) {
            this.mFragmentStore.removeFragment(fragment);
            if (isMenuAvailable(fragment)) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mRemoving = true;
            setVisibleRemovingFragment(fragment);
        }
    }

    public void hideFragment(Fragment fragment) {
        if (isLoggingEnabled(2)) {
            Log.v(TAG, "hide: " + fragment);
        }
        if (!fragment.mHidden) {
            fragment.mHidden = true;
            fragment.mHiddenChanged = true ^ fragment.mHiddenChanged;
            setVisibleRemovingFragment(fragment);
        }
    }

    public void showFragment(Fragment fragment) {
        if (isLoggingEnabled(2)) {
            Log.v(TAG, "show: " + fragment);
        }
        if (fragment.mHidden) {
            fragment.mHidden = false;
            fragment.mHiddenChanged = !fragment.mHiddenChanged;
        }
    }

    public void detachFragment(Fragment fragment) {
        if (isLoggingEnabled(2)) {
            Log.v(TAG, "detach: " + fragment);
        }
        if (!fragment.mDetached) {
            fragment.mDetached = true;
            if (fragment.mAdded) {
                if (isLoggingEnabled(2)) {
                    Log.v(TAG, "remove from detach: " + fragment);
                }
                this.mFragmentStore.removeFragment(fragment);
                if (isMenuAvailable(fragment)) {
                    this.mNeedMenuInvalidate = true;
                }
                setVisibleRemovingFragment(fragment);
            }
        }
    }

    public void attachFragment(Fragment fragment) {
        if (isLoggingEnabled(2)) {
            Log.v(TAG, "attach: " + fragment);
        }
        if (fragment.mDetached) {
            fragment.mDetached = false;
            if (!fragment.mAdded) {
                this.mFragmentStore.addFragment(fragment);
                if (isLoggingEnabled(2)) {
                    Log.v(TAG, "add from attach: " + fragment);
                }
                if (isMenuAvailable(fragment)) {
                    this.mNeedMenuInvalidate = true;
                }
            }
        }
    }

    public Fragment findFragmentById(int id) {
        return this.mFragmentStore.findFragmentById(id);
    }

    public Fragment findFragmentByTag(String tag) {
        return this.mFragmentStore.findFragmentByTag(tag);
    }

    public Fragment findFragmentByWho(String who) {
        return this.mFragmentStore.findFragmentByWho(who);
    }

    public Fragment findActiveFragment(String who) {
        return this.mFragmentStore.findActiveFragment(who);
    }

    private void checkStateLoss() {
        if (isStateSaved()) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        }
    }

    public boolean isStateSaved() {
        return this.mStateSaved || this.mStopped;
    }

    public void enqueueAction(OpGenerator action, boolean allowStateLoss) {
        if (!allowStateLoss) {
            if (this.mHost != null) {
                checkStateLoss();
            } else if (this.mDestroyed) {
                throw new IllegalStateException("FragmentManager has been destroyed");
            } else {
                throw new IllegalStateException("FragmentManager has not been attached to a host.");
            }
        }
        synchronized (this.mPendingActions) {
            if (this.mHost != null) {
                this.mPendingActions.add(action);
                scheduleCommit();
            } else if (!allowStateLoss) {
                throw new IllegalStateException("Activity has been destroyed");
            }
        }
    }

    void scheduleCommit() {
        synchronized (this.mPendingActions) {
            boolean pendingReady = false;
            boolean postponeReady = this.mPostponedTransactions != null && !this.mPostponedTransactions.isEmpty();
            if (this.mPendingActions.size() == 1) {
                pendingReady = true;
            }
            if (postponeReady || pendingReady) {
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                this.mHost.getHandler().post(this.mExecCommit);
                updateOnBackPressedCallbackEnabled();
            }
        }
    }

    public int allocBackStackIndex() {
        return this.mBackStackIndex.getAndIncrement();
    }

    private void ensureExecReady(boolean allowStateLoss) {
        if (this.mExecutingActions) {
            throw new IllegalStateException("FragmentManager is already executing transactions");
        } else if (this.mHost == null) {
            if (this.mDestroyed) {
                throw new IllegalStateException("FragmentManager has been destroyed");
            }
            throw new IllegalStateException("FragmentManager has not been attached to a host.");
        } else if (Looper.myLooper() == this.mHost.getHandler().getLooper()) {
            if (!allowStateLoss) {
                checkStateLoss();
            }
            if (this.mTmpRecords == null) {
                this.mTmpRecords = new ArrayList<>();
                this.mTmpIsPop = new ArrayList<>();
            }
            this.mExecutingActions = true;
            try {
                executePostponedTransaction(null, null);
            } finally {
                this.mExecutingActions = false;
            }
        } else {
            throw new IllegalStateException("Must be called from main thread of fragment host");
        }
    }

    public void execSingleAction(OpGenerator action, boolean allowStateLoss) {
        if (!allowStateLoss || (this.mHost != null && !this.mDestroyed)) {
            ensureExecReady(allowStateLoss);
            if (action.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
                this.mExecutingActions = true;
                try {
                    removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                } finally {
                    cleanupExec();
                }
            }
            updateOnBackPressedCallbackEnabled();
            doPendingDeferredStart();
            this.mFragmentStore.burpActive();
        }
    }

    private void cleanupExec() {
        this.mExecutingActions = false;
        this.mTmpIsPop.clear();
        this.mTmpRecords.clear();
    }

    /* JADX WARN: Finally extract failed */
    public boolean execPendingActions(boolean allowStateLoss) {
        ensureExecReady(allowStateLoss);
        boolean didSomething = false;
        while (generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                cleanupExec();
                didSomething = true;
            } catch (Throwable th) {
                cleanupExec();
                throw th;
            }
        }
        updateOnBackPressedCallbackEnabled();
        doPendingDeferredStart();
        this.mFragmentStore.burpActive();
        return didSomething;
    }

    private void executePostponedTransaction(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop) {
        int index;
        int index2;
        ArrayList<StartEnterTransitionListener> arrayList = this.mPostponedTransactions;
        int numPostponed = arrayList == null ? 0 : arrayList.size();
        int i = 0;
        while (i < numPostponed) {
            StartEnterTransitionListener listener = this.mPostponedTransactions.get(i);
            if (records != null && !listener.mIsBack && (index2 = records.indexOf(listener.mRecord)) != -1 && isRecordPop != null && isRecordPop.get(index2).booleanValue()) {
                this.mPostponedTransactions.remove(i);
                i--;
                numPostponed--;
                listener.cancelTransaction();
            } else if (listener.isReady() || (records != null && listener.mRecord.interactsWith(records, 0, records.size()))) {
                this.mPostponedTransactions.remove(i);
                i--;
                numPostponed--;
                if (records == null || listener.mIsBack || (index = records.indexOf(listener.mRecord)) == -1 || isRecordPop == null || !isRecordPop.get(index).booleanValue()) {
                    listener.completeTransaction();
                } else {
                    listener.cancelTransaction();
                }
            }
            i++;
        }
    }

    private void removeRedundantOperationsAndExecute(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop) {
        if (!records.isEmpty()) {
            if (records.size() == isRecordPop.size()) {
                executePostponedTransaction(records, isRecordPop);
                int numRecords = records.size();
                int startIndex = 0;
                int recordNum = 0;
                while (recordNum < numRecords) {
                    if (!records.get(recordNum).mReorderingAllowed) {
                        if (startIndex != recordNum) {
                            executeOpsTogether(records, isRecordPop, startIndex, recordNum);
                        }
                        int reorderingEnd = recordNum + 1;
                        if (isRecordPop.get(recordNum).booleanValue()) {
                            while (reorderingEnd < numRecords && isRecordPop.get(reorderingEnd).booleanValue() && !records.get(reorderingEnd).mReorderingAllowed) {
                                reorderingEnd++;
                            }
                        }
                        executeOpsTogether(records, isRecordPop, recordNum, reorderingEnd);
                        startIndex = reorderingEnd;
                        recordNum = reorderingEnd - 1;
                    }
                    recordNum++;
                }
                if (startIndex != numRecords) {
                    executeOpsTogether(records, isRecordPop, startIndex, numRecords);
                    return;
                }
                return;
            }
            throw new IllegalStateException("Internal error with the back stack records");
        }
    }

    private void executeOpsTogether(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex) {
        int i;
        ArrayList<Boolean> arrayList;
        boolean allowReordering;
        int i2;
        int i3;
        int i4;
        ArrayList<Boolean> arrayList2;
        int postponeIndex;
        boolean z;
        boolean allowReordering2 = records.get(startIndex).mReorderingAllowed;
        ArrayList<Fragment> arrayList3 = this.mTmpAddedFragments;
        if (arrayList3 == null) {
            this.mTmpAddedFragments = new ArrayList<>();
        } else {
            arrayList3.clear();
        }
        this.mTmpAddedFragments.addAll(this.mFragmentStore.getFragments());
        Fragment oldPrimaryNav = getPrimaryNavigationFragment();
        int recordNum = startIndex;
        boolean addToBackStack = false;
        while (true) {
            boolean addToBackStack2 = true;
            if (recordNum >= endIndex) {
                break;
            }
            BackStackRecord record = records.get(recordNum);
            if (!isRecordPop.get(recordNum).booleanValue()) {
                oldPrimaryNav = record.expandOps(this.mTmpAddedFragments, oldPrimaryNav);
            } else {
                oldPrimaryNav = record.trackAddedFragmentsInPop(this.mTmpAddedFragments, oldPrimaryNav);
            }
            if (!addToBackStack && !record.mAddToBackStack) {
                addToBackStack2 = false;
            }
            addToBackStack = addToBackStack2;
            recordNum++;
        }
        this.mTmpAddedFragments.clear();
        if (!allowReordering2 && this.mCurState >= 1) {
            if (USE_STATE_MANAGER) {
                for (int index = startIndex; index < endIndex; index++) {
                    Iterator it = records.get(index).mOps.iterator();
                    while (it.hasNext()) {
                        Fragment fragment = ((FragmentTransaction.Op) it.next()).mFragment;
                        if (!(fragment == null || fragment.mFragmentManager == null)) {
                            this.mFragmentStore.makeActive(createOrGetFragmentStateManager(fragment));
                        }
                    }
                }
            } else {
                FragmentTransition.startTransitions(this.mHost.getContext(), this.mContainer, records, isRecordPop, startIndex, endIndex, false, this.mFragmentTransitionCallback);
            }
        }
        executeOps(records, isRecordPop, startIndex, endIndex);
        if (USE_STATE_MANAGER) {
            boolean isPop = isRecordPop.get(endIndex - 1).booleanValue();
            for (int index2 = startIndex; index2 < endIndex; index2++) {
                BackStackRecord record2 = records.get(index2);
                if (isPop) {
                    for (int opIndex = record2.mOps.size() - 1; opIndex >= 0; opIndex--) {
                        Fragment fragment2 = ((FragmentTransaction.Op) record2.mOps.get(opIndex)).mFragment;
                        if (fragment2 != null) {
                            createOrGetFragmentStateManager(fragment2).moveToExpectedState();
                        }
                    }
                } else {
                    Iterator it2 = record2.mOps.iterator();
                    while (it2.hasNext()) {
                        Fragment fragment3 = ((FragmentTransaction.Op) it2.next()).mFragment;
                        if (fragment3 != null) {
                            createOrGetFragmentStateManager(fragment3).moveToExpectedState();
                        }
                    }
                }
            }
            moveToState(this.mCurState, true);
            for (SpecialEffectsController controller : collectChangedControllers(records, startIndex, endIndex)) {
                controller.updateOperationDirection(isPop);
                controller.markPostponedState();
                controller.executePendingOperations();
            }
            i = endIndex;
            arrayList = isRecordPop;
        } else {
            if (allowReordering2) {
                ArraySet<Fragment> addedFragments = new ArraySet<>();
                addAddedFragments(addedFragments);
                i2 = 1;
                allowReordering = allowReordering2;
                i3 = endIndex;
                i4 = startIndex;
                arrayList2 = isRecordPop;
                postponeIndex = postponePostponableTransactions(records, isRecordPop, startIndex, endIndex, addedFragments);
                makeRemovedFragmentsInvisible(addedFragments);
            } else {
                i2 = 1;
                allowReordering = allowReordering2;
                i3 = endIndex;
                i4 = startIndex;
                arrayList2 = isRecordPop;
                postponeIndex = endIndex;
            }
            if (postponeIndex == i4 || !allowReordering) {
                arrayList = arrayList2;
                i = i3;
            } else {
                if (this.mCurState >= i2) {
                    arrayList = arrayList2;
                    i = i3;
                    z = i2;
                    FragmentTransition.startTransitions(this.mHost.getContext(), this.mContainer, records, isRecordPop, startIndex, postponeIndex, true, this.mFragmentTransitionCallback);
                } else {
                    arrayList = arrayList2;
                    i = i3;
                    z = i2;
                }
                moveToState(this.mCurState, z);
            }
        }
        for (int recordNum2 = startIndex; recordNum2 < i; recordNum2++) {
            BackStackRecord record3 = records.get(recordNum2);
            if (arrayList.get(recordNum2).booleanValue() && record3.mIndex >= 0) {
                record3.mIndex = -1;
            }
            record3.runOnCommitRunnables();
        }
        if (addToBackStack) {
            reportBackStackChanged();
        }
    }

    private Set<SpecialEffectsController> collectChangedControllers(ArrayList<BackStackRecord> records, int startIndex, int endIndex) {
        ViewGroup container;
        Set<SpecialEffectsController> controllers = new HashSet<>();
        for (int index = startIndex; index < endIndex; index++) {
            Iterator it = records.get(index).mOps.iterator();
            while (it.hasNext()) {
                Fragment fragment = ((FragmentTransaction.Op) it.next()).mFragment;
                if (!(fragment == null || (container = fragment.mContainer) == null)) {
                    controllers.add(SpecialEffectsController.getOrCreateController(container, this));
                }
            }
        }
        return controllers;
    }

    private void makeRemovedFragmentsInvisible(ArraySet<Fragment> fragments) {
        int numAdded = fragments.size();
        for (int i = 0; i < numAdded; i++) {
            Fragment fragment = fragments.valueAt(i);
            if (!fragment.mAdded) {
                View view = fragment.requireView();
                fragment.mPostponedAlpha = view.getAlpha();
                view.setAlpha(0.0f);
            }
        }
    }

    private int postponePostponableTransactions(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex, ArraySet<Fragment> added) {
        int postponeIndex = endIndex;
        for (int i = endIndex - 1; i >= startIndex; i--) {
            BackStackRecord record = records.get(i);
            boolean isPop = isRecordPop.get(i).booleanValue();
            if (record.isPostponed() && !record.interactsWith(records, i + 1, endIndex)) {
                if (this.mPostponedTransactions == null) {
                    this.mPostponedTransactions = new ArrayList<>();
                }
                StartEnterTransitionListener listener = new StartEnterTransitionListener(record, isPop);
                this.mPostponedTransactions.add(listener);
                record.setOnStartPostponedListener(listener);
                if (isPop) {
                    record.executeOps();
                } else {
                    record.executePopOps(false);
                }
                postponeIndex--;
                if (i != postponeIndex) {
                    records.remove(i);
                    records.add(postponeIndex, record);
                }
                addAddedFragments(added);
            }
        }
        return postponeIndex;
    }

    void completeExecute(BackStackRecord record, boolean isPop, boolean runTransitions, boolean moveToState) {
        if (isPop) {
            record.executePopOps(moveToState);
        } else {
            record.executeOps();
        }
        ArrayList<BackStackRecord> records = new ArrayList<>(1);
        ArrayList<Boolean> isRecordPop = new ArrayList<>(1);
        records.add(record);
        isRecordPop.add(Boolean.valueOf(isPop));
        if (runTransitions && this.mCurState >= 1) {
            FragmentTransition.startTransitions(this.mHost.getContext(), this.mContainer, records, isRecordPop, 0, 1, true, this.mFragmentTransitionCallback);
        }
        if (moveToState) {
            moveToState(this.mCurState, true);
        }
        for (Fragment fragment : this.mFragmentStore.getActiveFragments()) {
            if (fragment != null && fragment.mView != null && fragment.mIsNewlyAdded && record.interactsWith(fragment.mContainerId)) {
                if (fragment.mPostponedAlpha > 0.0f) {
                    fragment.mView.setAlpha(fragment.mPostponedAlpha);
                }
                if (moveToState) {
                    fragment.mPostponedAlpha = 0.0f;
                } else {
                    fragment.mPostponedAlpha = -1.0f;
                    fragment.mIsNewlyAdded = false;
                }
            }
        }
    }

    private static void executeOps(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            BackStackRecord record = records.get(i);
            boolean moveToState = true;
            if (isRecordPop.get(i).booleanValue()) {
                record.bumpBackStackNesting(-1);
                if (i != endIndex - 1) {
                    moveToState = false;
                }
                record.executePopOps(moveToState);
            } else {
                record.bumpBackStackNesting(1);
                record.executeOps();
            }
        }
    }

    private void setVisibleRemovingFragment(Fragment f) {
        ViewGroup container = getFragmentContainer(f);
        if (container != null && f.getEnterAnim() + f.getExitAnim() + f.getPopEnterAnim() + f.getPopExitAnim() > 0) {
            if (container.getTag(R.id.visible_removing_fragment_view_tag) == null) {
                container.setTag(R.id.visible_removing_fragment_view_tag, f);
            }
            ((Fragment) container.getTag(R.id.visible_removing_fragment_view_tag)).setPopDirection(f.getPopDirection());
        }
    }

    private ViewGroup getFragmentContainer(Fragment f) {
        if (f.mContainer != null) {
            return f.mContainer;
        }
        if (f.mContainerId > 0 && this.mContainer.onHasView()) {
            View view = this.mContainer.onFindViewById(f.mContainerId);
            if (view instanceof ViewGroup) {
                return (ViewGroup) view;
            }
        }
        return null;
    }

    private void addAddedFragments(ArraySet<Fragment> added) {
        int i = this.mCurState;
        if (i >= 1) {
            int state = Math.min(i, 5);
            for (Fragment fragment : this.mFragmentStore.getFragments()) {
                if (fragment.mState < state) {
                    moveToState(fragment, state);
                    if (fragment.mView != null && !fragment.mHidden && fragment.mIsNewlyAdded) {
                        added.add(fragment);
                    }
                }
            }
        }
    }

    private void forcePostponedTransactions() {
        if (USE_STATE_MANAGER) {
            for (SpecialEffectsController controller : collectAllSpecialEffectsController()) {
                controller.forcePostponedExecutePendingOperations();
            }
        } else if (this.mPostponedTransactions != null) {
            while (!this.mPostponedTransactions.isEmpty()) {
                this.mPostponedTransactions.remove(0).completeTransaction();
            }
        }
    }

    private void endAnimatingAwayFragments() {
        if (USE_STATE_MANAGER) {
            for (SpecialEffectsController controller : collectAllSpecialEffectsController()) {
                controller.forceCompleteAllOperations();
            }
        } else if (!this.mExitAnimationCancellationSignals.isEmpty()) {
            for (Fragment fragment : this.mExitAnimationCancellationSignals.keySet()) {
                cancelExitAnimation(fragment);
                moveToState(fragment);
            }
        }
    }

    private Set<SpecialEffectsController> collectAllSpecialEffectsController() {
        Set<SpecialEffectsController> controllers = new HashSet<>();
        for (FragmentStateManager fragmentStateManager : this.mFragmentStore.getActiveFragmentStateManagers()) {
            ViewGroup container = fragmentStateManager.getFragment().mContainer;
            if (container != null) {
                controllers.add(SpecialEffectsController.getOrCreateController(container, getSpecialEffectsControllerFactory()));
            }
        }
        return controllers;
    }

    private boolean generateOpsForPendingActions(ArrayList<BackStackRecord> records, ArrayList<Boolean> isPop) {
        boolean didSomething = false;
        synchronized (this.mPendingActions) {
            if (this.mPendingActions.isEmpty()) {
                return false;
            }
            int numActions = this.mPendingActions.size();
            for (int i = 0; i < numActions; i++) {
                didSomething |= this.mPendingActions.get(i).generateOps(records, isPop);
            }
            this.mPendingActions.clear();
            this.mHost.getHandler().removeCallbacks(this.mExecCommit);
            return didSomething;
        }
    }

    private void doPendingDeferredStart() {
        if (this.mHavePendingDeferredStart) {
            this.mHavePendingDeferredStart = false;
            startPendingDeferredFragments();
        }
    }

    private void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); i++) {
                this.mBackStackChangeListeners.get(i).onBackStackChanged();
            }
        }
    }

    public void addBackStackState(BackStackRecord state) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList<>();
        }
        this.mBackStack.add(state);
    }

    boolean popBackStackState(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, String name, int id, int flags) {
        ArrayList<BackStackRecord> arrayList = this.mBackStack;
        if (arrayList == null) {
            return false;
        }
        if (name == null && id < 0 && (flags & 1) == 0) {
            int last = arrayList.size() - 1;
            if (last < 0) {
                return false;
            }
            records.add(this.mBackStack.remove(last));
            isRecordPop.add(true);
        } else {
            int index = -1;
            if (name != null || id >= 0) {
                index = this.mBackStack.size() - 1;
                while (index >= 0) {
                    BackStackRecord bss = this.mBackStack.get(index);
                    if ((name != null && name.equals(bss.getName())) || (id >= 0 && id == bss.mIndex)) {
                        break;
                    }
                    index--;
                }
                if (index < 0) {
                    return false;
                }
                if ((flags & 1) != 0) {
                    index--;
                    while (index >= 0) {
                        BackStackRecord bss2 = this.mBackStack.get(index);
                        if ((name == null || !name.equals(bss2.getName())) && (id < 0 || id != bss2.mIndex)) {
                            break;
                        }
                        index--;
                    }
                }
            }
            if (index == this.mBackStack.size() - 1) {
                return false;
            }
            for (int i = this.mBackStack.size() - 1; i > index; i--) {
                records.add(this.mBackStack.remove(i));
                isRecordPop.add(true);
            }
        }
        return true;
    }

    @Deprecated
    public FragmentManagerNonConfig retainNonConfig() {
        if (this.mHost instanceof ViewModelStoreOwner) {
            throwException(new IllegalStateException("You cannot use retainNonConfig when your FragmentHostCallback implements ViewModelStoreOwner."));
        }
        return this.mNonConfig.getSnapshot();
    }

    public Parcelable saveAllState() {
        int size;
        forcePostponedTransactions();
        endAnimatingAwayFragments();
        execPendingActions(true);
        this.mStateSaved = true;
        this.mNonConfig.setIsStateSaved(true);
        ArrayList<FragmentState> active = this.mFragmentStore.saveActiveFragments();
        if (!active.isEmpty()) {
            ArrayList<String> added = this.mFragmentStore.saveAddedFragments();
            BackStackState[] backStack = null;
            ArrayList<BackStackRecord> arrayList = this.mBackStack;
            if (arrayList != null && (size = arrayList.size()) > 0) {
                backStack = new BackStackState[size];
                for (int i = 0; i < size; i++) {
                    backStack[i] = new BackStackState(this.mBackStack.get(i));
                    if (isLoggingEnabled(2)) {
                        Log.v(TAG, "saveAllState: adding back stack #" + i + ": " + this.mBackStack.get(i));
                    }
                }
            }
            FragmentManagerState fms = new FragmentManagerState();
            fms.mActive = active;
            fms.mAdded = added;
            fms.mBackStack = backStack;
            fms.mBackStackIndex = this.mBackStackIndex.get();
            Fragment fragment = this.mPrimaryNav;
            if (fragment != null) {
                fms.mPrimaryNavActiveWho = fragment.mWho;
            }
            fms.mResultKeys.addAll(this.mResults.keySet());
            fms.mResults.addAll(this.mResults.values());
            fms.mLaunchedFragments = new ArrayList<>(this.mLaunchedFragments);
            return fms;
        } else if (!isLoggingEnabled(2)) {
            return null;
        } else {
            Log.v(TAG, "saveAllState: no fragments!");
            return null;
        }
    }

    public void restoreAllState(Parcelable state, FragmentManagerNonConfig nonConfig) {
        if (this.mHost instanceof ViewModelStoreOwner) {
            throwException(new IllegalStateException("You must use restoreSaveState when your FragmentHostCallback implements ViewModelStoreOwner"));
        }
        this.mNonConfig.restoreFromSnapshot(nonConfig);
        restoreSaveState(state);
    }

    public void restoreSaveState(Parcelable state) {
        FragmentStateManager fragmentStateManager;
        if (state != null) {
            FragmentManagerState fms = (FragmentManagerState) state;
            if (fms.mActive != null) {
                this.mFragmentStore.resetActiveFragments();
                Iterator<FragmentState> it = fms.mActive.iterator();
                while (it.hasNext()) {
                    FragmentState fs = it.next();
                    if (fs != null) {
                        Fragment retainedFragment = this.mNonConfig.findRetainedFragmentByWho(fs.mWho);
                        if (retainedFragment != null) {
                            if (isLoggingEnabled(2)) {
                                Log.v(TAG, "restoreSaveState: re-attaching retained " + retainedFragment);
                            }
                            fragmentStateManager = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, retainedFragment, fs);
                        } else {
                            fragmentStateManager = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, this.mHost.getContext().getClassLoader(), getFragmentFactory(), fs);
                        }
                        Fragment f = fragmentStateManager.getFragment();
                        f.mFragmentManager = this;
                        if (isLoggingEnabled(2)) {
                            Log.v(TAG, "restoreSaveState: active (" + f.mWho + "): " + f);
                        }
                        fragmentStateManager.restoreState(this.mHost.getContext().getClassLoader());
                        this.mFragmentStore.makeActive(fragmentStateManager);
                        fragmentStateManager.setFragmentManagerState(this.mCurState);
                    }
                }
                for (Fragment f2 : this.mNonConfig.getRetainedFragments()) {
                    if (!this.mFragmentStore.containsActiveFragment(f2.mWho)) {
                        if (isLoggingEnabled(2)) {
                            Log.v(TAG, "Discarding retained Fragment " + f2 + " that was not found in the set of active Fragments " + fms.mActive);
                        }
                        this.mNonConfig.removeRetainedFragment(f2);
                        f2.mFragmentManager = this;
                        FragmentStateManager fragmentStateManager2 = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, f2);
                        fragmentStateManager2.setFragmentManagerState(1);
                        fragmentStateManager2.moveToExpectedState();
                        f2.mRemoving = true;
                        fragmentStateManager2.moveToExpectedState();
                    }
                }
                this.mFragmentStore.restoreAddedFragments(fms.mAdded);
                if (fms.mBackStack != null) {
                    this.mBackStack = new ArrayList<>(fms.mBackStack.length);
                    for (int i = 0; i < fms.mBackStack.length; i++) {
                        BackStackRecord bse = fms.mBackStack[i].instantiate(this);
                        if (isLoggingEnabled(2)) {
                            Log.v(TAG, "restoreAllState: back stack #" + i + " (index " + bse.mIndex + "): " + bse);
                            PrintWriter pw = new PrintWriter(new LogWriter(TAG));
                            bse.dump("  ", pw, false);
                            pw.close();
                        }
                        this.mBackStack.add(bse);
                    }
                } else {
                    this.mBackStack = null;
                }
                this.mBackStackIndex.set(fms.mBackStackIndex);
                if (fms.mPrimaryNavActiveWho != null) {
                    this.mPrimaryNav = findActiveFragment(fms.mPrimaryNavActiveWho);
                    dispatchParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
                }
                ArrayList<String> savedResultKeys = fms.mResultKeys;
                if (savedResultKeys != null) {
                    for (int i2 = 0; i2 < savedResultKeys.size(); i2++) {
                        Bundle savedResult = fms.mResults.get(i2);
                        savedResult.setClassLoader(this.mHost.getContext().getClassLoader());
                        this.mResults.put(savedResultKeys.get(i2), savedResult);
                    }
                }
                this.mLaunchedFragments = new ArrayDeque<>(fms.mLaunchedFragments);
            }
        }
    }

    public FragmentHostCallback<?> getHost() {
        return this.mHost;
    }

    public Fragment getParent() {
        return this.mParent;
    }

    public FragmentContainer getContainer() {
        return this.mContainer;
    }

    public FragmentStore getFragmentStore() {
        return this.mFragmentStore;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void attachController(FragmentHostCallback<?> host, FragmentContainer container, final Fragment parent) {
        String parentId;
        if (this.mHost == null) {
            this.mHost = host;
            this.mContainer = container;
            this.mParent = parent;
            if (this.mParent != null) {
                addFragmentOnAttachListener(new FragmentOnAttachListener() { // from class: androidx.fragment.app.FragmentManager.8
                    @Override // androidx.fragment.app.FragmentOnAttachListener
                    public void onAttachFragment(FragmentManager fragmentManager, Fragment fragment) {
                        parent.onAttachFragment(fragment);
                    }
                });
            } else if (host instanceof FragmentOnAttachListener) {
                addFragmentOnAttachListener((FragmentOnAttachListener) host);
            }
            if (this.mParent != null) {
                updateOnBackPressedCallbackEnabled();
            }
            if (host instanceof OnBackPressedDispatcherOwner) {
                OnBackPressedDispatcherOwner dispatcherOwner = (OnBackPressedDispatcherOwner) host;
                this.mOnBackPressedDispatcher = dispatcherOwner.getOnBackPressedDispatcher();
                this.mOnBackPressedDispatcher.addCallback(parent != null ? parent : dispatcherOwner, this.mOnBackPressedCallback);
            }
            if (parent != null) {
                this.mNonConfig = parent.mFragmentManager.getChildNonConfig(parent);
            } else if (host instanceof ViewModelStoreOwner) {
                this.mNonConfig = FragmentManagerViewModel.getInstance(((ViewModelStoreOwner) host).getViewModelStore());
            } else {
                this.mNonConfig = new FragmentManagerViewModel(false);
            }
            this.mNonConfig.setIsStateSaved(isStateSaved());
            this.mFragmentStore.setNonConfig(this.mNonConfig);
            FragmentHostCallback<?> fragmentHostCallback = this.mHost;
            if (fragmentHostCallback instanceof ActivityResultRegistryOwner) {
                ActivityResultRegistry registry = ((ActivityResultRegistryOwner) fragmentHostCallback).getActivityResultRegistry();
                if (parent != null) {
                    parentId = parent.mWho + ":";
                } else {
                    parentId = "";
                }
                String keyPrefix = "FragmentManager:" + parentId;
                this.mStartActivityForResult = registry.register(keyPrefix + "StartActivityForResult", new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() { // from class: androidx.fragment.app.FragmentManager.9
                    public void onActivityResult(ActivityResult result) {
                        LaunchedFragmentInfo requestInfo = FragmentManager.this.mLaunchedFragments.pollFirst();
                        if (requestInfo == null) {
                            Log.w(FragmentManager.TAG, "No Activities were started for result for " + this);
                            return;
                        }
                        String fragmentWho = requestInfo.mWho;
                        int requestCode = requestInfo.mRequestCode;
                        Fragment fragment = FragmentManager.this.mFragmentStore.findFragmentByWho(fragmentWho);
                        if (fragment == null) {
                            Log.w(FragmentManager.TAG, "Activity result delivered for unknown Fragment " + fragmentWho);
                            return;
                        }
                        fragment.onActivityResult(requestCode, result.getResultCode(), result.getData());
                    }
                });
                this.mStartIntentSenderForResult = registry.register(keyPrefix + "StartIntentSenderForResult", new FragmentIntentSenderContract(), new ActivityResultCallback<ActivityResult>() { // from class: androidx.fragment.app.FragmentManager.10
                    public void onActivityResult(ActivityResult result) {
                        LaunchedFragmentInfo requestInfo = FragmentManager.this.mLaunchedFragments.pollFirst();
                        if (requestInfo == null) {
                            Log.w(FragmentManager.TAG, "No IntentSenders were started for " + this);
                            return;
                        }
                        String fragmentWho = requestInfo.mWho;
                        int requestCode = requestInfo.mRequestCode;
                        Fragment fragment = FragmentManager.this.mFragmentStore.findFragmentByWho(fragmentWho);
                        if (fragment == null) {
                            Log.w(FragmentManager.TAG, "Intent Sender result delivered for unknown Fragment " + fragmentWho);
                            return;
                        }
                        fragment.onActivityResult(requestCode, result.getResultCode(), result.getData());
                    }
                });
                this.mRequestPermissions = registry.register(keyPrefix + "RequestPermissions", new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() { // from class: androidx.fragment.app.FragmentManager.11
                    public void onActivityResult(Map<String, Boolean> result) {
                        int i;
                        String[] permissions = (String[]) result.keySet().toArray(new String[0]);
                        ArrayList<Boolean> resultValues = new ArrayList<>(result.values());
                        int[] grantResults = new int[resultValues.size()];
                        for (int i2 = 0; i2 < resultValues.size(); i2++) {
                            if (resultValues.get(i2).booleanValue()) {
                                i = 0;
                            } else {
                                i = -1;
                            }
                            grantResults[i2] = i;
                        }
                        LaunchedFragmentInfo requestInfo = FragmentManager.this.mLaunchedFragments.pollFirst();
                        if (requestInfo == null) {
                            Log.w(FragmentManager.TAG, "No permissions were requested for " + this);
                            return;
                        }
                        String fragmentWho = requestInfo.mWho;
                        int requestCode = requestInfo.mRequestCode;
                        Fragment fragment = FragmentManager.this.mFragmentStore.findFragmentByWho(fragmentWho);
                        if (fragment == null) {
                            Log.w(FragmentManager.TAG, "Permission request result delivered for unknown Fragment " + fragmentWho);
                            return;
                        }
                        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    }
                });
                return;
            }
            return;
        }
        throw new IllegalStateException("Already attached");
    }

    public void noteStateNotSaved() {
        if (this.mHost != null) {
            this.mStateSaved = false;
            this.mStopped = false;
            this.mNonConfig.setIsStateSaved(false);
            for (Fragment fragment : this.mFragmentStore.getFragments()) {
                if (fragment != null) {
                    fragment.noteStateNotSaved();
                }
            }
        }
    }

    public void launchStartActivityForResult(Fragment f, Intent intent, int requestCode, Bundle options) {
        if (this.mStartActivityForResult != null) {
            this.mLaunchedFragments.addLast(new LaunchedFragmentInfo(f.mWho, requestCode));
            if (!(intent == null || options == null)) {
                intent.putExtra(ActivityResultContracts.StartActivityForResult.EXTRA_ACTIVITY_OPTIONS_BUNDLE, options);
            }
            this.mStartActivityForResult.launch(intent);
            return;
        }
        this.mHost.onStartActivityFromFragment(f, intent, requestCode, options);
    }

    public void launchStartIntentSenderForResult(Fragment f, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        Intent fillInIntent2;
        if (this.mStartIntentSenderForResult != null) {
            if (options != null) {
                if (fillInIntent == null) {
                    fillInIntent2 = new Intent();
                    fillInIntent2.putExtra(EXTRA_CREATED_FILLIN_INTENT, true);
                } else {
                    fillInIntent2 = fillInIntent;
                }
                if (isLoggingEnabled(2)) {
                    Log.v(TAG, "ActivityOptions " + options + " were added to fillInIntent " + fillInIntent2 + " for fragment " + f);
                }
                fillInIntent2.putExtra(ActivityResultContracts.StartActivityForResult.EXTRA_ACTIVITY_OPTIONS_BUNDLE, options);
            } else {
                fillInIntent2 = fillInIntent;
            }
            IntentSenderRequest request = new IntentSenderRequest.Builder(intent).setFillInIntent(fillInIntent2).setFlags(flagsValues, flagsMask).build();
            this.mLaunchedFragments.addLast(new LaunchedFragmentInfo(f.mWho, requestCode));
            if (isLoggingEnabled(2)) {
                Log.v(TAG, "Fragment " + f + "is launching an IntentSender for result ");
            }
            this.mStartIntentSenderForResult.launch(request);
            return;
        }
        this.mHost.onStartIntentSenderFromFragment(f, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
    }

    public void launchRequestPermissions(Fragment f, String[] permissions, int requestCode) {
        if (this.mRequestPermissions != null) {
            this.mLaunchedFragments.addLast(new LaunchedFragmentInfo(f.mWho, requestCode));
            this.mRequestPermissions.launch(permissions);
            return;
        }
        this.mHost.onRequestPermissionsFromFragment(f, permissions, requestCode);
    }

    public void dispatchAttach() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.mNonConfig.setIsStateSaved(false);
        dispatchStateChange(0);
    }

    public void dispatchCreate() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.mNonConfig.setIsStateSaved(false);
        dispatchStateChange(1);
    }

    public void dispatchViewCreated() {
        dispatchStateChange(2);
    }

    public void dispatchActivityCreated() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.mNonConfig.setIsStateSaved(false);
        dispatchStateChange(4);
    }

    public void dispatchStart() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.mNonConfig.setIsStateSaved(false);
        dispatchStateChange(5);
    }

    public void dispatchResume() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.mNonConfig.setIsStateSaved(false);
        dispatchStateChange(7);
    }

    public void dispatchPause() {
        dispatchStateChange(5);
    }

    public void dispatchStop() {
        this.mStopped = true;
        this.mNonConfig.setIsStateSaved(true);
        dispatchStateChange(4);
    }

    public void dispatchDestroyView() {
        dispatchStateChange(1);
    }

    public void dispatchDestroy() {
        this.mDestroyed = true;
        execPendingActions(true);
        endAnimatingAwayFragments();
        dispatchStateChange(-1);
        this.mHost = null;
        this.mContainer = null;
        this.mParent = null;
        if (this.mOnBackPressedDispatcher != null) {
            this.mOnBackPressedCallback.remove();
            this.mOnBackPressedDispatcher = null;
        }
        ActivityResultLauncher<Intent> activityResultLauncher = this.mStartActivityForResult;
        if (activityResultLauncher != null) {
            activityResultLauncher.unregister();
            this.mStartIntentSenderForResult.unregister();
            this.mRequestPermissions.unregister();
        }
    }

    /* JADX WARN: Finally extract failed */
    private void dispatchStateChange(int nextState) {
        try {
            this.mExecutingActions = true;
            this.mFragmentStore.dispatchStateChange(nextState);
            moveToState(nextState, false);
            if (USE_STATE_MANAGER) {
                for (SpecialEffectsController controller : collectAllSpecialEffectsController()) {
                    controller.forceCompleteAllOperations();
                }
            }
            this.mExecutingActions = false;
            execPendingActions(true);
        } catch (Throwable th) {
            this.mExecutingActions = false;
            throw th;
        }
    }

    public void dispatchMultiWindowModeChanged(boolean isInMultiWindowMode) {
        for (Fragment f : this.mFragmentStore.getFragments()) {
            if (f != null) {
                f.performMultiWindowModeChanged(isInMultiWindowMode);
            }
        }
    }

    public void dispatchPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        for (Fragment f : this.mFragmentStore.getFragments()) {
            if (f != null) {
                f.performPictureInPictureModeChanged(isInPictureInPictureMode);
            }
        }
    }

    public void dispatchConfigurationChanged(Configuration newConfig) {
        for (Fragment f : this.mFragmentStore.getFragments()) {
            if (f != null) {
                f.performConfigurationChanged(newConfig);
            }
        }
    }

    public void dispatchLowMemory() {
        for (Fragment f : this.mFragmentStore.getFragments()) {
            if (f != null) {
                f.performLowMemory();
            }
        }
    }

    public boolean dispatchCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (this.mCurState < 1) {
            return false;
        }
        boolean show = false;
        ArrayList<Fragment> newMenus = null;
        for (Fragment f : this.mFragmentStore.getFragments()) {
            if (f != null && isParentMenuVisible(f) && f.performCreateOptionsMenu(menu, inflater)) {
                show = true;
                if (newMenus == null) {
                    newMenus = new ArrayList<>();
                }
                newMenus.add(f);
            }
        }
        if (this.mCreatedMenus != null) {
            for (int i = 0; i < this.mCreatedMenus.size(); i++) {
                Fragment f2 = this.mCreatedMenus.get(i);
                if (newMenus == null || !newMenus.contains(f2)) {
                    f2.onDestroyOptionsMenu();
                }
            }
        }
        this.mCreatedMenus = newMenus;
        return show;
    }

    public boolean dispatchPrepareOptionsMenu(Menu menu) {
        if (this.mCurState < 1) {
            return false;
        }
        boolean show = false;
        for (Fragment f : this.mFragmentStore.getFragments()) {
            if (f != null && isParentMenuVisible(f) && f.performPrepareOptionsMenu(menu)) {
                show = true;
            }
        }
        return show;
    }

    public boolean dispatchOptionsItemSelected(MenuItem item) {
        if (this.mCurState < 1) {
            return false;
        }
        for (Fragment f : this.mFragmentStore.getFragments()) {
            if (f != null && f.performOptionsItemSelected(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean dispatchContextItemSelected(MenuItem item) {
        if (this.mCurState < 1) {
            return false;
        }
        for (Fragment f : this.mFragmentStore.getFragments()) {
            if (f != null && f.performContextItemSelected(item)) {
                return true;
            }
        }
        return false;
    }

    public void dispatchOptionsMenuClosed(Menu menu) {
        if (this.mCurState >= 1) {
            for (Fragment f : this.mFragmentStore.getFragments()) {
                if (f != null) {
                    f.performOptionsMenuClosed(menu);
                }
            }
        }
    }

    public void setPrimaryNavigationFragment(Fragment f) {
        if (f == null || (f.equals(findActiveFragment(f.mWho)) && (f.mHost == null || f.mFragmentManager == this))) {
            Fragment previousPrimaryNav = this.mPrimaryNav;
            this.mPrimaryNav = f;
            dispatchParentPrimaryNavigationFragmentChanged(previousPrimaryNav);
            dispatchParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
            return;
        }
        throw new IllegalArgumentException("Fragment " + f + " is not an active fragment of FragmentManager " + this);
    }

    private void dispatchParentPrimaryNavigationFragmentChanged(Fragment f) {
        if (f != null && f.equals(findActiveFragment(f.mWho))) {
            f.performPrimaryNavigationFragmentChanged();
        }
    }

    public void dispatchPrimaryNavigationFragmentChanged() {
        updateOnBackPressedCallbackEnabled();
        dispatchParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
    }

    public Fragment getPrimaryNavigationFragment() {
        return this.mPrimaryNav;
    }

    public void setMaxLifecycle(Fragment f, Lifecycle.State state) {
        if (!f.equals(findActiveFragment(f.mWho)) || !(f.mHost == null || f.mFragmentManager == this)) {
            throw new IllegalArgumentException("Fragment " + f + " is not an active fragment of FragmentManager " + this);
        }
        f.mMaxState = state;
    }

    public void setFragmentFactory(FragmentFactory fragmentFactory) {
        this.mFragmentFactory = fragmentFactory;
    }

    public FragmentFactory getFragmentFactory() {
        FragmentFactory fragmentFactory = this.mFragmentFactory;
        if (fragmentFactory != null) {
            return fragmentFactory;
        }
        Fragment fragment = this.mParent;
        if (fragment != null) {
            return fragment.mFragmentManager.getFragmentFactory();
        }
        return this.mHostFragmentFactory;
    }

    void setSpecialEffectsControllerFactory(SpecialEffectsControllerFactory specialEffectsControllerFactory) {
        this.mSpecialEffectsControllerFactory = specialEffectsControllerFactory;
    }

    public SpecialEffectsControllerFactory getSpecialEffectsControllerFactory() {
        SpecialEffectsControllerFactory specialEffectsControllerFactory = this.mSpecialEffectsControllerFactory;
        if (specialEffectsControllerFactory != null) {
            return specialEffectsControllerFactory;
        }
        Fragment fragment = this.mParent;
        if (fragment != null) {
            return fragment.mFragmentManager.getSpecialEffectsControllerFactory();
        }
        return this.mDefaultSpecialEffectsControllerFactory;
    }

    public FragmentLifecycleCallbacksDispatcher getLifecycleCallbacksDispatcher() {
        return this.mLifecycleCallbacksDispatcher;
    }

    public void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb, boolean recursive) {
        this.mLifecycleCallbacksDispatcher.registerFragmentLifecycleCallbacks(cb, recursive);
    }

    public void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb) {
        this.mLifecycleCallbacksDispatcher.unregisterFragmentLifecycleCallbacks(cb);
    }

    public void addFragmentOnAttachListener(FragmentOnAttachListener listener) {
        this.mOnAttachListeners.add(listener);
    }

    public void dispatchOnAttachFragment(Fragment fragment) {
        Iterator<FragmentOnAttachListener> it = this.mOnAttachListeners.iterator();
        while (it.hasNext()) {
            it.next().onAttachFragment(this, fragment);
        }
    }

    public void removeFragmentOnAttachListener(FragmentOnAttachListener listener) {
        this.mOnAttachListeners.remove(listener);
    }

    boolean checkForMenus() {
        boolean hasMenu = false;
        for (Fragment fragment : this.mFragmentStore.getActiveFragments()) {
            if (fragment != null) {
                hasMenu = isMenuAvailable(fragment);
                continue;
            }
            if (hasMenu) {
                return true;
            }
        }
        return false;
    }

    private boolean isMenuAvailable(Fragment f) {
        return (f.mHasMenu && f.mMenuVisible) || f.mChildFragmentManager.checkForMenus();
    }

    public void invalidateMenuForFragment(Fragment f) {
        if (f.mAdded && isMenuAvailable(f)) {
            this.mNeedMenuInvalidate = true;
        }
    }

    public static int reverseTransit(int transit) {
        if (transit == 4097) {
            return 8194;
        }
        if (transit == 4099) {
            return FragmentTransaction.TRANSIT_FRAGMENT_FADE;
        }
        if (transit != 8194) {
            return 0;
        }
        return FragmentTransaction.TRANSIT_FRAGMENT_OPEN;
    }

    public LayoutInflater.Factory2 getLayoutInflaterFactory() {
        return this.mLayoutInflaterFactory;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class PopBackStackState implements OpGenerator {
        final int mFlags;
        final int mId;
        final String mName;

        PopBackStackState(String name, int id, int flags) {
            FragmentManager.this = r1;
            this.mName = name;
            this.mId = id;
            this.mFlags = flags;
        }

        @Override // androidx.fragment.app.FragmentManager.OpGenerator
        public boolean generateOps(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop) {
            if (FragmentManager.this.mPrimaryNav == null || this.mId >= 0 || this.mName != null || !FragmentManager.this.mPrimaryNav.getChildFragmentManager().popBackStackImmediate()) {
                return FragmentManager.this.popBackStackState(records, isRecordPop, this.mName, this.mId, this.mFlags);
            }
            return false;
        }
    }

    /* loaded from: classes.dex */
    public static class StartEnterTransitionListener implements Fragment.OnStartEnterTransitionListener {
        final boolean mIsBack;
        private int mNumPostponed;
        final BackStackRecord mRecord;

        StartEnterTransitionListener(BackStackRecord record, boolean isBack) {
            this.mIsBack = isBack;
            this.mRecord = record;
        }

        @Override // androidx.fragment.app.Fragment.OnStartEnterTransitionListener
        public void onStartEnterTransition() {
            this.mNumPostponed--;
            if (this.mNumPostponed == 0) {
                this.mRecord.mManager.scheduleCommit();
            }
        }

        @Override // androidx.fragment.app.Fragment.OnStartEnterTransitionListener
        public void startListening() {
            this.mNumPostponed++;
        }

        public boolean isReady() {
            return this.mNumPostponed == 0;
        }

        void completeTransaction() {
            boolean z = false;
            boolean canceled = this.mNumPostponed > 0;
            for (Fragment fragment : this.mRecord.mManager.getFragments()) {
                fragment.setOnStartEnterTransitionListener(null);
                if (canceled && fragment.isPostponed()) {
                    fragment.startPostponedEnterTransition();
                }
            }
            FragmentManager fragmentManager = this.mRecord.mManager;
            BackStackRecord backStackRecord = this.mRecord;
            boolean z2 = this.mIsBack;
            if (!canceled) {
                z = true;
            }
            fragmentManager.completeExecute(backStackRecord, z2, z, true);
        }

        void cancelTransaction() {
            this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
        }
    }

    /* loaded from: classes.dex */
    public static class LaunchedFragmentInfo implements Parcelable {
        public static final Parcelable.Creator<LaunchedFragmentInfo> CREATOR = new Parcelable.Creator<LaunchedFragmentInfo>() { // from class: androidx.fragment.app.FragmentManager.LaunchedFragmentInfo.1
            @Override // android.os.Parcelable.Creator
            public LaunchedFragmentInfo createFromParcel(Parcel in) {
                return new LaunchedFragmentInfo(in);
            }

            @Override // android.os.Parcelable.Creator
            public LaunchedFragmentInfo[] newArray(int size) {
                return new LaunchedFragmentInfo[size];
            }
        };
        int mRequestCode;
        String mWho;

        LaunchedFragmentInfo(String who, int requestCode) {
            this.mWho = who;
            this.mRequestCode = requestCode;
        }

        LaunchedFragmentInfo(Parcel in) {
            this.mWho = in.readString();
            this.mRequestCode = in.readInt();
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mWho);
            dest.writeInt(this.mRequestCode);
        }
    }

    /* loaded from: classes.dex */
    public static class FragmentIntentSenderContract extends ActivityResultContract<IntentSenderRequest, ActivityResult> {
        FragmentIntentSenderContract() {
        }

        public Intent createIntent(Context context, IntentSenderRequest input) {
            Bundle activityOptions;
            Intent result = new Intent(ActivityResultContracts.StartIntentSenderForResult.ACTION_INTENT_SENDER_REQUEST);
            Intent fillInIntent = input.getFillInIntent();
            if (!(fillInIntent == null || (activityOptions = fillInIntent.getBundleExtra(ActivityResultContracts.StartActivityForResult.EXTRA_ACTIVITY_OPTIONS_BUNDLE)) == null)) {
                result.putExtra(ActivityResultContracts.StartActivityForResult.EXTRA_ACTIVITY_OPTIONS_BUNDLE, activityOptions);
                fillInIntent.removeExtra(ActivityResultContracts.StartActivityForResult.EXTRA_ACTIVITY_OPTIONS_BUNDLE);
                if (fillInIntent.getBooleanExtra(FragmentManager.EXTRA_CREATED_FILLIN_INTENT, false)) {
                    input = new IntentSenderRequest.Builder(input.getIntentSender()).setFillInIntent(null).setFlags(input.getFlagsValues(), input.getFlagsMask()).build();
                }
            }
            result.putExtra(ActivityResultContracts.StartIntentSenderForResult.EXTRA_INTENT_SENDER_REQUEST, input);
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v(FragmentManager.TAG, "CreateIntent created the following intent: " + result);
            }
            return result;
        }

        @Override // androidx.activity.result.contract.ActivityResultContract
        public ActivityResult parseResult(int resultCode, Intent intent) {
            return new ActivityResult(resultCode, intent);
        }
    }
}
