package com.google.android.gms.measurement.internal;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzdv<V> {
    private static final Object zza = new Object();
    private final String zzb;
    private final zzds<V> zzc;
    private final V zzd;
    private final V zze;
    private final Object zzf = new Object();
    private volatile V zzg = null;
    private volatile V zzh = null;

    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ zzdv(String str, Object obj, Object obj2, zzds zzds, zzdu zzdu) {
        this.zzb = str;
        this.zzd = obj;
        this.zze = obj2;
        this.zzc = zzds;
    }

    public final V zza(V v) {
        V v2;
        synchronized (this.zzf) {
            V v3 = this.zzg;
        }
        if (v != null) {
            return v;
        }
        if (zzdt.zza == null) {
            return this.zzd;
        }
        synchronized (zza) {
            if (zzaa.zza()) {
                if (this.zzh == null) {
                    v2 = this.zzd;
                } else {
                    v2 = this.zzh;
                }
                return v2;
            }
            try {
                for (zzdv zzdv : zzdw.zzaB) {
                    if (!zzaa.zza()) {
                        V v4 = null;
                        try {
                            zzds<V> zzds = zzdv.zzc;
                            if (zzds != null) {
                                v4 = zzds.zza();
                            }
                        } catch (IllegalStateException e) {
                        }
                        synchronized (zza) {
                            zzdv.zzh = v4;
                        }
                    } else {
                        throw new IllegalStateException("Refreshing flag cache must be done on a worker thread.");
                    }
                }
            } catch (SecurityException e2) {
            }
            zzds<V> zzds2 = this.zzc;
            if (zzds2 != null) {
                try {
                    return zzds2.zza();
                } catch (IllegalStateException e3) {
                } catch (SecurityException e4) {
                }
            }
            return this.zzd;
        }
    }

    public final String zzb() {
        return this.zzb;
    }
}
