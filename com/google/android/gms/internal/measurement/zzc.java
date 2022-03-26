package com.google.android.gms.internal.measurement;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzc {
    final zzf zza;
    zzg zzb;
    final zzab zzc = new zzab();
    private final zzz zzd = new zzz();

    public zzc() {
        zzf zzf = new zzf();
        this.zza = zzf;
        this.zzb = zzf.zzb.zza();
        zzf zzf2 = this.zza;
        zzf2.zzd.zza("internal.registerCallback", new Callable() { // from class: com.google.android.gms.internal.measurement.zza
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return zzc.this.zzb();
            }
        });
        zzf zzf3 = this.zza;
        zzf3.zzd.zza("internal.eventLogger", new Callable() { // from class: com.google.android.gms.internal.measurement.zzb
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return new zzk(zzc.this.zzc);
            }
        });
    }

    public final zzab zza() {
        return this.zzc;
    }

    public final /* synthetic */ zzai zzb() throws Exception {
        return new zzv(this.zzd);
    }

    public final void zzc(zzgo zzgo) throws zzd {
        zzai zzai;
        try {
            this.zzb = this.zza.zzb.zza();
            if (!(this.zza.zza(this.zzb, (zzgt[]) zzgo.zzc().toArray(new zzgt[0])) instanceof zzag)) {
                for (zzgm zzgm : zzgo.zza().zzd()) {
                    List<zzgt> zzc = zzgm.zzc();
                    String zzb = zzgm.zzb();
                    Iterator<zzgt> it = zzc.iterator();
                    while (it.hasNext()) {
                        zzap zza = this.zza.zza(this.zzb, it.next());
                        if (zza instanceof zzam) {
                            zzg zzg = this.zzb;
                            if (!zzg.zzh(zzb)) {
                                zzai = null;
                            } else {
                                zzap zzd = zzg.zzd(zzb);
                                if (!(zzd instanceof zzai)) {
                                    String valueOf = String.valueOf(zzb);
                                    throw new IllegalStateException(valueOf.length() != 0 ? "Invalid function name: ".concat(valueOf) : new String("Invalid function name: "));
                                }
                                zzai = (zzai) zzd;
                            }
                            if (zzai == null) {
                                String valueOf2 = String.valueOf(zzb);
                                throw new IllegalStateException(valueOf2.length() != 0 ? "Rule function is undefined: ".concat(valueOf2) : new String("Rule function is undefined: "));
                            }
                            zzai.zza(this.zzb, Collections.singletonList(zza));
                        } else {
                            throw new IllegalArgumentException("Invalid rule definition");
                        }
                    }
                }
                return;
            }
            throw new IllegalStateException("Program loading failed");
        } catch (Throwable th) {
            throw new zzd(th);
        }
    }

    public final void zzd(String str, Callable<? extends zzai> callable) {
        this.zza.zzd.zza(str, callable);
    }

    public final boolean zze(zzaa zzaa) throws zzd {
        try {
            this.zzc.zzd(zzaa);
            this.zza.zzc.zzg("runtime.counter", new zzah(Double.valueOf(0.0d)));
            this.zzd.zzb(this.zzb.zza(), this.zzc);
            if (zzg()) {
                return true;
            }
            if (zzf()) {
                return true;
            }
            return false;
        } catch (Throwable th) {
            throw new zzd(th);
        }
    }

    public final boolean zzf() {
        return !this.zzc.zzc().isEmpty();
    }

    public final boolean zzg() {
        return !this.zzc.zzb().equals(this.zzc.zza());
    }
}
