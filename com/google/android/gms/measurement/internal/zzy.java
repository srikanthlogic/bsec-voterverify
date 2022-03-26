package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.zzel;
import com.google.android.gms.internal.measurement.zzes;
import com.google.android.gms.internal.measurement.zzgh;
import com.google.android.gms.internal.measurement.zzoe;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzy extends zzx {
    final /* synthetic */ zzz zza;
    private final zzes zzh;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzy(zzz zzz, String str, int i, zzes zzes) {
        super(str, i);
        this.zza = zzz;
        this.zzh = zzes;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.measurement.internal.zzx
    public final int zza() {
        return this.zzh.zza();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.measurement.internal.zzx
    public final boolean zzb() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.measurement.internal.zzx
    public final boolean zzc() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean zzd(Long l, Long l2, zzgh zzgh, boolean z) {
        Object[] objArr;
        zzoe.zzc();
        boolean zzs = this.zza.zzs.zzf().zzs(this.zzb, zzdw.zzW);
        boolean zzg = this.zzh.zzg();
        boolean zzh = this.zzh.zzh();
        boolean zzi = this.zzh.zzi();
        if (zzg || zzh) {
            objArr = 1;
        } else {
            objArr = zzi ? 1 : null;
        }
        Boolean bool = null;
        Integer num = null;
        bool = null;
        bool = null;
        bool = null;
        bool = null;
        if (!z || objArr != null) {
            zzel zzb = this.zzh.zzb();
            boolean zzg2 = zzb.zzg();
            if (zzgh.zzr()) {
                if (!zzb.zzi()) {
                    this.zza.zzs.zzay().zzk().zzb("No number filter for long property. property", this.zza.zzs.zzj().zze(zzgh.zzf()));
                } else {
                    bool = zzj(zzh(zzgh.zzb(), zzb.zzc()), zzg2);
                }
            } else if (zzgh.zzq()) {
                if (!zzb.zzi()) {
                    this.zza.zzs.zzay().zzk().zzb("No number filter for double property. property", this.zza.zzs.zzj().zze(zzgh.zzf()));
                } else {
                    bool = zzj(zzg(zzgh.zza(), zzb.zzc()), zzg2);
                }
            } else if (!zzgh.zzt()) {
                this.zza.zzs.zzay().zzk().zzb("User property has no value, property", this.zza.zzs.zzj().zze(zzgh.zzf()));
            } else if (zzb.zzk()) {
                bool = zzj(zzf(zzgh.zzg(), zzb.zzd(), this.zza.zzs.zzay()), zzg2);
            } else if (!zzb.zzi()) {
                this.zza.zzs.zzay().zzk().zzb("No string or number filter defined. property", this.zza.zzs.zzj().zze(zzgh.zzf()));
            } else if (zzkp.zzy(zzgh.zzg())) {
                bool = zzj(zzi(zzgh.zzg(), zzb.zzc()), zzg2);
            } else {
                this.zza.zzs.zzay().zzk().zzc("Invalid user property value for Numeric number filter. property, value", this.zza.zzs.zzj().zze(zzgh.zzf()), zzgh.zzg());
            }
            this.zza.zzs.zzay().zzj().zzb("Property filter result", bool == null ? "null" : bool);
            if (bool == null) {
                return false;
            }
            this.zzd = true;
            if (zzi && !bool.booleanValue()) {
                return true;
            }
            if (!z || this.zzh.zzg()) {
                this.zze = bool;
            }
            if (bool.booleanValue() && objArr != null && zzgh.zzs()) {
                long zzc = zzgh.zzc();
                if (l != null) {
                    zzc = l.longValue();
                }
                if (zzs && this.zzh.zzg() && !this.zzh.zzh() && l2 != null) {
                    zzc = l2.longValue();
                }
                if (this.zzh.zzh()) {
                    this.zzg = Long.valueOf(zzc);
                } else {
                    this.zzf = Long.valueOf(zzc);
                }
            }
            return true;
        }
        zzeg zzj = this.zza.zzs.zzay().zzj();
        Integer valueOf = Integer.valueOf(this.zzc);
        if (this.zzh.zzj()) {
            num = Integer.valueOf(this.zzh.zza());
        }
        zzj.zzc("Property filter already evaluated true and it is not associated with an enhanced audience. audience ID, filter ID", valueOf, num);
        return true;
    }
}
