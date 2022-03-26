package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzcf;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzik implements Runnable {
    final /* synthetic */ String zza;
    final /* synthetic */ String zzb;
    final /* synthetic */ zzp zzc;
    final /* synthetic */ boolean zzd;
    final /* synthetic */ zzcf zze;
    final /* synthetic */ zzjj zzf;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzik(zzjj zzjj, String str, String str2, zzp zzp, boolean z, zzcf zzcf) {
        this.zzf = zzjj;
        this.zza = str;
        this.zzb = str2;
        this.zzc = zzp;
        this.zzd = z;
        this.zze = zzcf;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Bundle bundle;
        Throwable th;
        RemoteException e;
        Bundle bundle2 = new Bundle();
        try {
            zzdz zzdz = this.zzf.zzb;
            if (zzdz == null) {
                this.zzf.zzs.zzay().zzd().zzc("Failed to get user properties; not connected to service", this.zza, this.zzb);
                this.zzf.zzs.zzv().zzQ(this.zze, bundle2);
                return;
            }
            Preconditions.checkNotNull(this.zzc);
            List<zzkq> zzh = zzdz.zzh(this.zza, this.zzb, this.zzd, this.zzc);
            bundle = new Bundle();
            if (zzh != null) {
                for (zzkq zzkq : zzh) {
                    String str = zzkq.zze;
                    if (str != null) {
                        bundle.putString(zzkq.zzb, str);
                    } else {
                        Long l = zzkq.zzd;
                        if (l != null) {
                            bundle.putLong(zzkq.zzb, l.longValue());
                        } else {
                            Double d = zzkq.zzg;
                            if (d != null) {
                                bundle.putDouble(zzkq.zzb, d.doubleValue());
                            }
                        }
                    }
                }
            }
            try {
                try {
                    this.zzf.zzQ();
                    this.zzf.zzs.zzv().zzQ(this.zze, bundle);
                } catch (RemoteException e2) {
                    e = e2;
                    this.zzf.zzs.zzay().zzd().zzc("Failed to get user properties; remote exception", this.zza, e);
                    this.zzf.zzs.zzv().zzQ(this.zze, bundle);
                }
            } catch (Throwable th2) {
                th = th2;
                this.zzf.zzs.zzv().zzQ(this.zze, bundle);
                throw th;
            }
        } catch (RemoteException e3) {
            e = e3;
            bundle = bundle2;
        } catch (Throwable th3) {
            bundle = bundle2;
            th = th3;
            this.zzf.zzs.zzv().zzQ(this.zze, bundle);
            throw th;
        }
    }
}
