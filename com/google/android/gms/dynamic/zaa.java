package com.google.android.gms.dynamic;

import com.google.android.gms.dynamic.DeferredLifecycleHelper;
import java.util.Iterator;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
final class zaa implements OnDelegateCreatedListener<T> {
    private final /* synthetic */ DeferredLifecycleHelper zaa;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zaa(DeferredLifecycleHelper deferredLifecycleHelper) {
        this.zaa = deferredLifecycleHelper;
    }

    /* JADX WARN: Incorrect types in method signature: (TT;)V */
    @Override // com.google.android.gms.dynamic.OnDelegateCreatedListener
    public final void onDelegateCreated(LifecycleDelegate lifecycleDelegate) {
        this.zaa.zaa = lifecycleDelegate;
        Iterator it = this.zaa.zac.iterator();
        while (it.hasNext()) {
            ((DeferredLifecycleHelper.zaa) it.next()).zaa(this.zaa.zaa);
        }
        this.zaa.zac.clear();
        this.zaa.zab = null;
    }
}
