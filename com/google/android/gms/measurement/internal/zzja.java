package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzja implements Runnable {
    final /* synthetic */ AtomicReference zza;
    final /* synthetic */ String zzb;
    final /* synthetic */ String zzc;
    final /* synthetic */ zzp zzd;
    final /* synthetic */ zzjj zze;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzja(zzjj zzjj, AtomicReference atomicReference, String str, String str2, String str3, zzp zzp) {
        this.zze = zzjj;
        this.zza = atomicReference;
        this.zzb = str2;
        this.zzc = str3;
        this.zzd = zzp;
    }

    @Override // java.lang.Runnable
    public final void run() {
        AtomicReference atomicReference;
        zzdz zzdz;
        synchronized (this.zza) {
            try {
                zzdz = this.zze.zzb;
            } catch (RemoteException e) {
                this.zze.zzs.zzay().zzd().zzd("(legacy) Failed to get conditional properties; remote exception", null, this.zzb, e);
                this.zza.set(Collections.emptyList());
                atomicReference = this.zza;
            }
            if (zzdz == null) {
                this.zze.zzs.zzay().zzd().zzd("(legacy) Failed to get conditional properties; not connected to service", null, this.zzb, this.zzc);
                this.zza.set(Collections.emptyList());
                this.zza.notify();
                return;
            }
            if (TextUtils.isEmpty(null)) {
                Preconditions.checkNotNull(this.zzd);
                this.zza.set(zzdz.zzf(this.zzb, this.zzc, this.zzd));
            } else {
                this.zza.set(zzdz.zzg(null, this.zzb, this.zzc));
            }
            this.zze.zzQ();
            atomicReference = this.zza;
            atomicReference.notify();
        }
    }
}
