package com.facebook.common.activitylistener;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import com.facebook.common.internal.Preconditions;
import java.lang.ref.WeakReference;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class ActivityListenerManager {
    public static void register(ActivityListener activityListener, Context context) {
        ListenableActivity activity = getListenableActivity(context);
        if (activity != null) {
            activity.addActivityListener(new Listener(activityListener));
        }
    }

    @Nullable
    public static ListenableActivity getListenableActivity(Context context) {
        if (!(context instanceof ListenableActivity) && (context instanceof ContextWrapper)) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        if (context instanceof ListenableActivity) {
            return (ListenableActivity) context;
        }
        return null;
    }

    /* loaded from: classes.dex */
    private static class Listener extends BaseActivityListener {
        private final WeakReference<ActivityListener> mActivityListenerRef;

        public Listener(ActivityListener activityListener) {
            this.mActivityListenerRef = new WeakReference<>(activityListener);
        }

        @Override // com.facebook.common.activitylistener.BaseActivityListener, com.facebook.common.activitylistener.ActivityListener
        public void onActivityCreate(Activity activity) {
            ActivityListener activityVisibilityListener = getListenerOrCleanUp(activity);
            if (activityVisibilityListener != null) {
                activityVisibilityListener.onActivityCreate(activity);
            }
        }

        @Override // com.facebook.common.activitylistener.BaseActivityListener, com.facebook.common.activitylistener.ActivityListener
        public void onDestroy(Activity activity) {
            ActivityListener activityVisibilityListener = getListenerOrCleanUp(activity);
            if (activityVisibilityListener != null) {
                activityVisibilityListener.onDestroy(activity);
            }
        }

        @Override // com.facebook.common.activitylistener.BaseActivityListener, com.facebook.common.activitylistener.ActivityListener
        public void onStart(Activity activity) {
            ActivityListener activityVisibilityListener = getListenerOrCleanUp(activity);
            if (activityVisibilityListener != null) {
                activityVisibilityListener.onStart(activity);
            }
        }

        @Override // com.facebook.common.activitylistener.BaseActivityListener, com.facebook.common.activitylistener.ActivityListener
        public void onStop(Activity activity) {
            ActivityListener activityVisibilityListener = getListenerOrCleanUp(activity);
            if (activityVisibilityListener != null) {
                activityVisibilityListener.onStop(activity);
            }
        }

        @Override // com.facebook.common.activitylistener.BaseActivityListener, com.facebook.common.activitylistener.ActivityListener
        public void onResume(Activity activity) {
            ActivityListener activityVisibilityListener = getListenerOrCleanUp(activity);
            if (activityVisibilityListener != null) {
                activityVisibilityListener.onResume(activity);
            }
        }

        @Override // com.facebook.common.activitylistener.BaseActivityListener, com.facebook.common.activitylistener.ActivityListener
        public void onPause(Activity activity) {
            ActivityListener activityVisibilityListener = getListenerOrCleanUp(activity);
            if (activityVisibilityListener != null) {
                activityVisibilityListener.onPause(activity);
            }
        }

        @Nullable
        private ActivityListener getListenerOrCleanUp(Activity activity) {
            ActivityListener activityVisibilityListener = this.mActivityListenerRef.get();
            if (activityVisibilityListener == null) {
                Preconditions.checkArgument(activity instanceof ListenableActivity);
                ((ListenableActivity) activity).removeActivityListener(this);
            }
            return activityVisibilityListener;
        }
    }
}
