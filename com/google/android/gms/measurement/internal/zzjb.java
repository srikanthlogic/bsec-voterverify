package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzcf;
import java.util.ArrayList;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzjb implements Runnable {
    final /* synthetic */ String zza;
    final /* synthetic */ String zzb;
    final /* synthetic */ zzp zzc;
    final /* synthetic */ zzcf zzd;
    final /* synthetic */ zzjj zze;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzjb(zzjj zzjj, String str, String str2, zzp zzp, zzcf zzcf) {
        this.zze = zzjj;
        this.zza = str;
        this.zzb = str2;
        this.zzc = zzp;
        this.zzd = zzcf;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Throwable th;
        RemoteException e;
        zzfs zzfs;
        ArrayList<Bundle> arrayList = new ArrayList<>();
        try {
            zzdz zzdz = this.zze.zzb;
            if (zzdz == null) {
                this.zze.zzs.zzay().zzd().zzc("Failed to get conditional properties; not connected to service", this.zza, this.zzb);
                zzfs = this.zze.zzs;
            } else {
                Preconditions.checkNotNull(this.zzc);
                arrayList = zzku.zzG(zzdz.zzf(this.zza, this.zzb, this.zzc));
                try {
                    try {
                        this.zze.zzQ();
                        zzfs = this.zze.zzs;
                    } catch (RemoteException e2) {
                        e = e2;
                        this.zze.zzs.zzay().zzd().zzd("Failed to get conditional properties; remote exception", this.zza, this.zzb, e);
                        zzfs = this.zze.zzs;
                        zzfs.zzv().zzP(this.zzd, arrayList);
                    }
                } catch (Throwable th2) {
                    th = th2;
                    this.zze.zzs.zzv().zzP(this.zzd, arrayList);
                    throw th;
                }
            }
        } catch (RemoteException e3) {
            e = e3;
        } catch (Throwable th3) {
            th = th3;
            this.zze.zzs.zzv().zzP(this.zzd, arrayList);
            throw th;
        }
        zzfs.zzv().zzP(this.zzd, arrayList);
    }
}
