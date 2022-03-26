package androidx.appcompat.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.VectorEnabledTintResources;
import androidx.collection.ArraySet;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.Iterator;
/* loaded from: classes.dex */
public abstract class AppCompatDelegate {
    static final boolean DEBUG;
    public static final int FEATURE_ACTION_MODE_OVERLAY;
    public static final int FEATURE_SUPPORT_ACTION_BAR;
    public static final int FEATURE_SUPPORT_ACTION_BAR_OVERLAY;
    @Deprecated
    public static final int MODE_NIGHT_AUTO;
    public static final int MODE_NIGHT_AUTO_BATTERY;
    @Deprecated
    public static final int MODE_NIGHT_AUTO_TIME;
    public static final int MODE_NIGHT_FOLLOW_SYSTEM;
    public static final int MODE_NIGHT_NO;
    public static final int MODE_NIGHT_UNSPECIFIED;
    public static final int MODE_NIGHT_YES;
    static final String TAG;
    private static int sDefaultNightMode = -100;
    private static final ArraySet<WeakReference<AppCompatDelegate>> sActivityDelegates = new ArraySet<>();
    private static final Object sActivityDelegatesLock = new Object();

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface NightMode {
    }

    public abstract void addContentView(View view, ViewGroup.LayoutParams layoutParams);

    public abstract boolean applyDayNight();

    public abstract View createView(View view, String str, Context context, AttributeSet attributeSet);

    public abstract <T extends View> T findViewById(int i);

    public abstract ActionBarDrawerToggle.Delegate getDrawerToggleDelegate();

    public abstract MenuInflater getMenuInflater();

    public abstract ActionBar getSupportActionBar();

    public abstract boolean hasWindowFeature(int i);

    public abstract void installViewFactory();

    public abstract void invalidateOptionsMenu();

    public abstract boolean isHandleNativeActionModesEnabled();

    public abstract void onConfigurationChanged(Configuration configuration);

    public abstract void onCreate(Bundle bundle);

    public abstract void onDestroy();

    public abstract void onPostCreate(Bundle bundle);

    public abstract void onPostResume();

    public abstract void onSaveInstanceState(Bundle bundle);

    public abstract void onStart();

    public abstract void onStop();

    public abstract boolean requestWindowFeature(int i);

    public abstract void setContentView(int i);

    public abstract void setContentView(View view);

    public abstract void setContentView(View view, ViewGroup.LayoutParams layoutParams);

    public abstract void setHandleNativeActionModesEnabled(boolean z);

    public abstract void setLocalNightMode(int i);

    public abstract void setSupportActionBar(Toolbar toolbar);

    public abstract void setTitle(CharSequence charSequence);

    public abstract ActionMode startSupportActionMode(ActionMode.Callback callback);

    public static AppCompatDelegate create(Activity activity, AppCompatCallback callback) {
        return new AppCompatDelegateImpl(activity, callback);
    }

    public static AppCompatDelegate create(Dialog dialog, AppCompatCallback callback) {
        return new AppCompatDelegateImpl(dialog, callback);
    }

    public static AppCompatDelegate create(Context context, Window window, AppCompatCallback callback) {
        return new AppCompatDelegateImpl(context, window, callback);
    }

    public static AppCompatDelegate create(Context context, Activity activity, AppCompatCallback callback) {
        return new AppCompatDelegateImpl(context, activity, callback);
    }

    public void setTheme(int themeResId) {
    }

    @Deprecated
    public void attachBaseContext(Context context) {
    }

    public Context attachBaseContext2(Context context) {
        attachBaseContext(context);
        return context;
    }

    public int getLocalNightMode() {
        return -100;
    }

    public static void setDefaultNightMode(int mode) {
        if (mode != -1 && mode != 0 && mode != 1 && mode != 2 && mode != 3) {
            Log.d(TAG, "setDefaultNightMode() called with an unknown mode");
        } else if (sDefaultNightMode != mode) {
            sDefaultNightMode = mode;
            applyDayNightToActiveDelegates();
        }
    }

    public static int getDefaultNightMode() {
        return sDefaultNightMode;
    }

    public static void setCompatVectorFromResourcesEnabled(boolean enabled) {
        VectorEnabledTintResources.setCompatVectorFromResourcesEnabled(enabled);
    }

    public static boolean isCompatVectorFromResourcesEnabled() {
        return VectorEnabledTintResources.isCompatVectorFromResourcesEnabled();
    }

    static void addActiveDelegate(AppCompatDelegate delegate) {
        synchronized (sActivityDelegatesLock) {
            removeDelegateFromActives(delegate);
            sActivityDelegates.add(new WeakReference<>(delegate));
        }
    }

    static void removeActivityDelegate(AppCompatDelegate delegate) {
        synchronized (sActivityDelegatesLock) {
            removeDelegateFromActives(delegate);
        }
    }

    private static void removeDelegateFromActives(AppCompatDelegate toRemove) {
        synchronized (sActivityDelegatesLock) {
            Iterator<WeakReference<AppCompatDelegate>> i = sActivityDelegates.iterator();
            while (i.hasNext()) {
                AppCompatDelegate delegate = i.next().get();
                if (delegate == toRemove || delegate == null) {
                    i.remove();
                }
            }
        }
    }

    private static void applyDayNightToActiveDelegates() {
        synchronized (sActivityDelegatesLock) {
            Iterator<WeakReference<AppCompatDelegate>> it = sActivityDelegates.iterator();
            while (it.hasNext()) {
                AppCompatDelegate delegate = it.next().get();
                if (delegate != null) {
                    delegate.applyDayNight();
                }
            }
        }
    }
}
