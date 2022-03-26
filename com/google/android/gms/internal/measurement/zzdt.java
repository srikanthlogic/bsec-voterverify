package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
/* compiled from: com.google.android.gms:play-services-measurement-sdk-api@@19.0.1 */
/* loaded from: classes.dex */
public abstract class zzdt implements Runnable {
    final long zzh;
    final long zzi;
    final boolean zzj;
    final /* synthetic */ zzee zzk;

    public zzdt(zzee zzee, boolean z) {
        this.zzk = zzee;
        this.zzh = zzee.zza.currentTimeMillis();
        this.zzi = zzee.zza.elapsedRealtime();
        this.zzj = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.zzk.zzh) {
            zzb();
            return;
        }
        try {
            zza();
        } catch (Exception e) {
            this.zzk.zzS(e, false, this.zzj);
            zzb();
        }
    }

    abstract void zza() throws RemoteException;

    protected void zzb() {
    }
}
