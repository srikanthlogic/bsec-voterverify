package com.google.android.gms.location;

import com.google.android.gms.tasks.TaskCompletionSource;
/* compiled from: com.google.android.gms:play-services-location@@18.0.0 */
/* loaded from: classes.dex */
final class zzam extends zzao {
    private final zzan zza;

    public zzam(TaskCompletionSource<Void> taskCompletionSource, zzan zzan) {
        super(taskCompletionSource);
        this.zza = zzan;
    }

    @Override // com.google.android.gms.location.zzao, com.google.android.gms.internal.location.zzai
    public final void zzc() {
        this.zza.zza();
    }
}
