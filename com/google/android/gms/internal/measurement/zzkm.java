package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public class zzkm {
    private static final zzjl zzb = zzjl.zza();
    protected volatile zzlg zza;
    private volatile zziy zzc;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzkm)) {
            return false;
        }
        zzkm zzkm = (zzkm) obj;
        zzlg zzlg = this.zza;
        zzlg zzlg2 = zzkm.zza;
        if (zzlg == null && zzlg2 == null) {
            return zzb().equals(zzkm.zzb());
        }
        if (zzlg != null && zzlg2 != null) {
            return zzlg.equals(zzlg2);
        }
        if (zzlg != null) {
            zzkm.zzc(zzlg.zzbL());
            return zzlg.equals(zzkm.zza);
        }
        zzc(zzlg2.zzbL());
        return this.zza.equals(zzlg2);
    }

    public int hashCode() {
        return 1;
    }

    public final int zza() {
        if (this.zzc != null) {
            return ((zziv) this.zzc).zza.length;
        }
        if (this.zza != null) {
            return this.zza.zzbt();
        }
        return 0;
    }

    public final zziy zzb() {
        if (this.zzc != null) {
            return this.zzc;
        }
        synchronized (this) {
            if (this.zzc != null) {
                return this.zzc;
            }
            if (this.zza == null) {
                this.zzc = zziy.zzb;
            } else {
                this.zzc = this.zza.zzbp();
            }
            return this.zzc;
        }
    }

    protected final void zzc(zzlg zzlg) {
        if (this.zza == null) {
            synchronized (this) {
                if (this.zza == null) {
                    try {
                        this.zza = zzlg;
                        this.zzc = zziy.zzb;
                    } catch (zzkj e) {
                        this.zza = zzlg;
                        this.zzc = zziy.zzb;
                    }
                }
            }
        }
    }
}
