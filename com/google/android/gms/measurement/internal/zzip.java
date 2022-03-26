package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzcf;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzip implements Runnable {
    final /* synthetic */ zzp zza;
    final /* synthetic */ zzcf zzb;
    final /* synthetic */ zzjj zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzip(zzjj zzjj, zzp zzp, zzcf zzcf) {
        this.zzc = zzjj;
        this.zza = zzp;
        this.zzb = zzcf;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v6 */
    /* JADX WARN: Type inference failed for: r0v9, types: [com.google.android.gms.measurement.internal.zzku] */
    @Override // java.lang.Runnable
    public final void run() {
        Throwable th;
        String str;
        String str2;
        RemoteException e;
        zzfs zzfs;
        try {
            str = "Failed to get app instance id";
            str2 = null;
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            if (!this.zzc.zzs.zzm().zzc().zzk()) {
                this.zzc.zzs.zzay().zzl().zza("Analytics storage consent denied; will not get app instance id");
                ((zze) this.zzc).zzs.zzq().zzN(null);
                this.zzc.zzs.zzm().zze.zzb(null);
                zzfs = this.zzc.zzs;
            } else {
                zzdz zzdz = this.zzc.zzb;
                if (zzdz == null) {
                    this.zzc.zzs.zzay().zzd().zza(str);
                    zzfs = this.zzc.zzs;
                } else {
                    Preconditions.checkNotNull(this.zza);
                    str2 = zzdz.zzd(this.zza);
                    if (str2 != null) {
                        try {
                            ((zze) this.zzc).zzs.zzq().zzN(str2);
                            this.zzc.zzs.zzm().zze.zzb(str2);
                        } catch (RemoteException e2) {
                            e = e2;
                            this.zzc.zzs.zzay().zzd().zzb(str, e);
                            zzfs = this.zzc.zzs;
                            str = zzfs.zzv();
                            str.zzU(this.zzb, str2);
                        }
                    }
                    this.zzc.zzQ();
                    zzfs = this.zzc.zzs;
                }
            }
        } catch (RemoteException e3) {
            e = e3;
        } catch (Throwable th3) {
            th = th3;
            this.zzc.zzs.zzv().zzU(this.zzb, str2);
            throw th;
        }
        str = zzfs.zzv();
        str.zzU(this.zzb, str2);
    }
}
