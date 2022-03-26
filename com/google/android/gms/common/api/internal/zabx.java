package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.util.BiConsumer;
import com.google.android.gms.tasks.TaskCompletionSource;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
final /* synthetic */ class zabx implements RemoteCall {
    private final BiConsumer zaa;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zabx(BiConsumer biConsumer) {
        this.zaa = biConsumer;
    }

    @Override // com.google.android.gms.common.api.internal.RemoteCall
    public final void accept(Object obj, Object obj2) {
        this.zaa.accept((Api.AnyClient) obj, (TaskCompletionSource) obj2);
    }
}
