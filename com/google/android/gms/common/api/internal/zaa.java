package com.google.android.gms.common.api.internal;

import android.app.Activity;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class zaa extends ActivityLifecycleObserver {
    private final WeakReference<C0000zaa> zaa;

    public zaa(Activity activity) {
        this(C0000zaa.zab(activity));
    }

    private zaa(C0000zaa zaa) {
        this.zaa = new WeakReference<>(zaa);
    }

    @Override // com.google.android.gms.common.api.internal.ActivityLifecycleObserver
    public final ActivityLifecycleObserver onStopCallOnce(Runnable runnable) {
        C0000zaa zaa = this.zaa.get();
        if (zaa != null) {
            zaa.zaa(runnable);
            return this;
        }
        throw new IllegalStateException("The target activity has already been GC'd");
    }

    /* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
    /* renamed from: com.google.android.gms.common.api.internal.zaa$zaa  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    static class C0000zaa extends LifecycleCallback {
        private List<Runnable> zaa = new ArrayList();

        /* JADX INFO: Access modifiers changed from: private */
        public static C0000zaa zab(Activity activity) {
            C0000zaa zaa;
            synchronized (activity) {
                LifecycleFragment fragment = getFragment(activity);
                zaa = (C0000zaa) fragment.getCallbackOrNull("LifecycleObserverOnStop", C0000zaa.class);
                if (zaa == null) {
                    zaa = new C0000zaa(fragment);
                }
            }
            return zaa;
        }

        private C0000zaa(LifecycleFragment lifecycleFragment) {
            super(lifecycleFragment);
            this.mLifecycleFragment.addCallback("LifecycleObserverOnStop", this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final synchronized void zaa(Runnable runnable) {
            this.zaa.add(runnable);
        }

        @Override // com.google.android.gms.common.api.internal.LifecycleCallback
        public void onStop() {
            List<Runnable> list;
            synchronized (this) {
                list = this.zaa;
                this.zaa = new ArrayList();
            }
            for (Runnable runnable : list) {
                runnable.run();
            }
        }
    }
}
