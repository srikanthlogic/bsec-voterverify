package androidx.transition;

import android.util.Log;
import android.view.View;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ViewUtilsApi19 extends ViewUtilsBase {
    private static final String TAG;
    private static Method sGetTransitionAlphaMethod;
    private static boolean sGetTransitionAlphaMethodFetched;
    private static Method sSetTransitionAlphaMethod;
    private static boolean sSetTransitionAlphaMethodFetched;

    @Override // androidx.transition.ViewUtilsBase
    public void setTransitionAlpha(View view, float alpha) {
        fetchSetTransitionAlphaMethod();
        Method method = sSetTransitionAlphaMethod;
        if (method != null) {
            try {
                method.invoke(view, Float.valueOf(alpha));
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e2) {
                throw new RuntimeException(e2.getCause());
            }
        } else {
            view.setAlpha(alpha);
        }
    }

    @Override // androidx.transition.ViewUtilsBase
    public float getTransitionAlpha(View view) {
        fetchGetTransitionAlphaMethod();
        Method method = sGetTransitionAlphaMethod;
        if (method != null) {
            try {
                return ((Float) method.invoke(view, new Object[0])).floatValue();
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e2) {
                throw new RuntimeException(e2.getCause());
            }
        }
        return super.getTransitionAlpha(view);
    }

    @Override // androidx.transition.ViewUtilsBase
    public void saveNonTransitionAlpha(View view) {
    }

    @Override // androidx.transition.ViewUtilsBase
    public void clearNonTransitionAlpha(View view) {
    }

    private void fetchSetTransitionAlphaMethod() {
        if (!sSetTransitionAlphaMethodFetched) {
            try {
                sSetTransitionAlphaMethod = View.class.getDeclaredMethod("setTransitionAlpha", Float.TYPE);
                sSetTransitionAlphaMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                Log.i(TAG, "Failed to retrieve setTransitionAlpha method", e);
            }
            sSetTransitionAlphaMethodFetched = true;
        }
    }

    private void fetchGetTransitionAlphaMethod() {
        if (!sGetTransitionAlphaMethodFetched) {
            try {
                sGetTransitionAlphaMethod = View.class.getDeclaredMethod("getTransitionAlpha", new Class[0]);
                sGetTransitionAlphaMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                Log.i(TAG, "Failed to retrieve getTransitionAlpha method", e);
            }
            sGetTransitionAlphaMethodFetched = true;
        }
    }
}
