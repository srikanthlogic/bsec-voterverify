package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.zzr;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzfh implements zzr {
    final /* synthetic */ zzfj zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzfh(zzfj zzfj) {
        this.zza = zzfj;
    }

    @Override // com.google.android.gms.internal.measurement.zzr
    public final void zza(int i, String str, List<String> list, boolean z, boolean z2) {
        zzeg zzeg;
        int i2 = i - 1;
        if (i2 == 0) {
            zzeg = this.zza.zzs.zzay().zzc();
        } else if (i2 != 1) {
            if (i2 == 3) {
                zzeg = this.zza.zzs.zzay().zzj();
            } else if (i2 != 4) {
                zzeg = this.zza.zzs.zzay().zzi();
            } else if (z) {
                zzeg = this.zza.zzs.zzay().zzm();
            } else if (!z2) {
                zzeg = this.zza.zzs.zzay().zzl();
            } else {
                zzeg = this.zza.zzs.zzay().zzk();
            }
        } else if (z) {
            zzeg = this.zza.zzs.zzay().zzh();
        } else if (!z2) {
            zzeg = this.zza.zzs.zzay().zze();
        } else {
            zzeg = this.zza.zzs.zzay().zzd();
        }
        int size = list.size();
        if (size == 1) {
            zzeg.zzb(str, list.get(0));
        } else if (size == 2) {
            zzeg.zzc(str, list.get(0), list.get(1));
        } else if (size != 3) {
            zzeg.zza(str);
        } else {
            zzeg.zzd(str, list.get(0), list.get(1), list.get(2));
        }
    }
}
