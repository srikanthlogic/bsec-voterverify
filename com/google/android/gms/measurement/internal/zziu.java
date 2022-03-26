package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import com.google.android.gms.internal.measurement.zzcf;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zziu implements Runnable {
    final /* synthetic */ zzat zza;
    final /* synthetic */ String zzb;
    final /* synthetic */ zzcf zzc;
    final /* synthetic */ zzjj zzd;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zziu(zzjj zzjj, zzat zzat, String str, zzcf zzcf) {
        this.zzd = zzjj;
        this.zza = zzat;
        this.zzb = str;
        this.zzc = zzcf;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Throwable th;
        byte[] bArr;
        RemoteException e;
        zzfs zzfs;
        try {
            bArr = null;
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            zzdz zzdz = this.zzd.zzb;
            if (zzdz == null) {
                this.zzd.zzs.zzay().zzd().zza("Discarding data. Failed to send event to service to bundle");
                zzfs = this.zzd.zzs;
            } else {
                bArr = zzdz.zzu(this.zza, this.zzb);
                try {
                    this.zzd.zzQ();
                    zzfs = this.zzd.zzs;
                } catch (RemoteException e2) {
                    e = e2;
                    this.zzd.zzs.zzay().zzd().zzb("Failed to send event to the service to bundle", e);
                    zzfs = this.zzd.zzs;
                    zzfs.zzv().zzR(this.zzc, bArr);
                }
            }
        } catch (RemoteException e3) {
            e = e3;
        } catch (Throwable th3) {
            th = th3;
            this.zzd.zzs.zzv().zzR(this.zzc, bArr);
            throw th;
        }
        zzfs.zzv().zzR(this.zzc, bArr);
    }
}
