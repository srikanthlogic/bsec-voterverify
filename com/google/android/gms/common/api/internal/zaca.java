package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.internal.RegistrationMethods;
import com.google.android.gms.tasks.TaskCompletionSource;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
final /* synthetic */ class zaca implements RemoteCall {
    private final RegistrationMethods.Builder zaa;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zaca(RegistrationMethods.Builder builder) {
        this.zaa = builder;
    }

    @Override // com.google.android.gms.common.api.internal.RemoteCall
    public final void accept(Object obj, Object obj2) {
        this.zaa.zaa((Api.AnyClient) obj, (TaskCompletionSource) obj2);
    }
}
