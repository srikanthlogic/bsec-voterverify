package com.google.android.gms.measurement.internal;

import android.util.Log;
import androidx.collection.ArrayMap;
import com.google.android.gms.internal.measurement.zzej;
import com.google.android.gms.internal.measurement.zzel;
import com.google.android.gms.internal.measurement.zzfo;
import com.google.android.gms.internal.measurement.zzfs;
import com.google.android.gms.internal.measurement.zzoe;
import java.util.HashSet;
import java.util.Iterator;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzw extends zzx {
    final /* synthetic */ zzz zza;
    private final zzej zzh;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzw(zzz zzz, String str, int i, zzej zzej) {
        super(str, i);
        this.zza = zzz;
        this.zzh = zzej;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.measurement.internal.zzx
    public final int zza() {
        return this.zzh.zzb();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.measurement.internal.zzx
    public final boolean zzb() {
        return this.zzh.zzo();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.measurement.internal.zzx
    public final boolean zzc() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0400  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0403  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x040b A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:127:0x040c  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final boolean zzd(Long l, Long l2, zzfo zzfo, long j, zzap zzap, boolean z) {
        Object[] objArr;
        Object obj;
        Boolean bool;
        Double d;
        zzoe.zzc();
        boolean zzs = this.zza.zzs.zzf().zzs(this.zzb, zzdw.zzY);
        long j2 = this.zzh.zzn() ? zzap.zze : j;
        Integer num = null;
        Integer num2 = null;
        r5 = null;
        r5 = null;
        r5 = null;
        r5 = null;
        r5 = null;
        r5 = null;
        r5 = null;
        r5 = null;
        r5 = null;
        r5 = null;
        r5 = null;
        Boolean bool2 = null;
        if (Log.isLoggable(this.zza.zzs.zzay().zzq(), 2)) {
            this.zza.zzs.zzay().zzj().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(this.zzc), this.zzh.zzp() ? Integer.valueOf(this.zzh.zzb()) : null, this.zza.zzs.zzj().zzc(this.zzh.zzg()));
            this.zza.zzs.zzay().zzj().zzb("Filter definition", this.zza.zzf.zzu().zzo(this.zzh));
        }
        if (!this.zzh.zzp() || this.zzh.zzb() > 256) {
            zzeg zzk = this.zza.zzs.zzay().zzk();
            Object zzn = zzei.zzn(this.zzb);
            if (this.zzh.zzp()) {
                num = Integer.valueOf(this.zzh.zzb());
            }
            zzk.zzc("Invalid event filter ID. appId, id", zzn, String.valueOf(num));
            return false;
        }
        boolean zzk2 = this.zzh.zzk();
        boolean zzm = this.zzh.zzm();
        boolean zzn2 = this.zzh.zzn();
        if (zzk2 || zzm) {
            objArr = 1;
        } else {
            objArr = zzn2 ? 1 : null;
        }
        if (!z || objArr != null) {
            zzej zzej = this.zzh;
            String zzh = zzfo.zzh();
            if (zzej.zzo()) {
                Boolean zzh2 = zzh(j2, zzej.zzf());
                if (zzh2 != null) {
                    if (!zzh2.booleanValue()) {
                        bool2 = false;
                    }
                }
                zzeg zzj = this.zza.zzs.zzay().zzj();
                if (bool2 != null) {
                    obj = "null";
                } else {
                    obj = bool2;
                }
                zzj.zzb("Event filter result", obj);
                if (bool2 != null) {
                    return false;
                }
                this.zzd = true;
                if (!bool2.booleanValue()) {
                    return true;
                }
                this.zze = true;
                if (objArr != null && zzfo.zzu()) {
                    Long valueOf = Long.valueOf(zzfo.zzd());
                    if (this.zzh.zzm()) {
                        if (zzs && this.zzh.zzo()) {
                            valueOf = l;
                        }
                        this.zzg = valueOf;
                    } else {
                        if (zzs && this.zzh.zzo()) {
                            valueOf = l2;
                        }
                        this.zzf = valueOf;
                    }
                }
                return true;
            }
            HashSet hashSet = new HashSet();
            Iterator<zzel> it = zzej.zzh().iterator();
            while (true) {
                if (!it.hasNext()) {
                    ArrayMap arrayMap = new ArrayMap();
                    Iterator<zzfs> it2 = zzfo.zzi().iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            Iterator<zzel> it3 = zzej.zzh().iterator();
                            while (true) {
                                if (!it3.hasNext()) {
                                    bool2 = true;
                                    break;
                                }
                                zzel next = it3.next();
                                boolean z2 = next.zzh() && next.zzg();
                                String zze = next.zze();
                                if (zze.isEmpty()) {
                                    this.zza.zzs.zzay().zzk().zzb("Event has empty param name. event", this.zza.zzs.zzj().zzc(zzh));
                                    break;
                                }
                                Object obj2 = arrayMap.get(zze);
                                if (obj2 instanceof Long) {
                                    if (next.zzi()) {
                                        Boolean zzh3 = zzh(((Long) obj2).longValue(), next.zzc());
                                        if (zzh3 == null) {
                                            break;
                                        } else if (zzh3.booleanValue() == z2) {
                                            bool2 = false;
                                            break;
                                        }
                                    } else {
                                        this.zza.zzs.zzay().zzk().zzc("No number filter for long param. event, param", this.zza.zzs.zzj().zzc(zzh), this.zza.zzs.zzj().zzd(zze));
                                        break;
                                    }
                                } else if (obj2 instanceof Double) {
                                    if (next.zzi()) {
                                        Boolean zzg = zzg(((Double) obj2).doubleValue(), next.zzc());
                                        if (zzg == null) {
                                            break;
                                        } else if (zzg.booleanValue() == z2) {
                                            bool2 = false;
                                            break;
                                        }
                                    } else {
                                        this.zza.zzs.zzay().zzk().zzc("No number filter for double param. event, param", this.zza.zzs.zzj().zzc(zzh), this.zza.zzs.zzj().zzd(zze));
                                        break;
                                    }
                                } else if (obj2 instanceof String) {
                                    if (!next.zzk()) {
                                        if (!next.zzi()) {
                                            this.zza.zzs.zzay().zzk().zzc("No filter for String param. event, param", this.zza.zzs.zzj().zzc(zzh), this.zza.zzs.zzj().zzd(zze));
                                            break;
                                        }
                                        String str = (String) obj2;
                                        if (!zzkp.zzy(str)) {
                                            this.zza.zzs.zzay().zzk().zzc("Invalid param value for number filter. event, param", this.zza.zzs.zzj().zzc(zzh), this.zza.zzs.zzj().zzd(zze));
                                            break;
                                        }
                                        bool = zzi(str, next.zzc());
                                    } else {
                                        bool = zzf((String) obj2, next.zzd(), this.zza.zzs.zzay());
                                    }
                                    if (bool == null) {
                                        break;
                                    } else if (bool.booleanValue() == z2) {
                                        bool2 = false;
                                        break;
                                    }
                                } else if (obj2 == null) {
                                    this.zza.zzs.zzay().zzj().zzc("Missing param for filter. event, param", this.zza.zzs.zzj().zzc(zzh), this.zza.zzs.zzj().zzd(zze));
                                    bool2 = false;
                                } else {
                                    this.zza.zzs.zzay().zzk().zzc("Unknown param type. event, param", this.zza.zzs.zzj().zzc(zzh), this.zza.zzs.zzj().zzd(zze));
                                }
                            }
                        } else {
                            zzfs next2 = it2.next();
                            if (hashSet.contains(next2.zzg())) {
                                if (!next2.zzw()) {
                                    if (!next2.zzu()) {
                                        if (!next2.zzy()) {
                                            this.zza.zzs.zzay().zzk().zzc("Unknown value for param. event, param", this.zza.zzs.zzj().zzc(zzh), this.zza.zzs.zzj().zzd(next2.zzg()));
                                            break;
                                        }
                                        arrayMap.put(next2.zzg(), next2.zzh());
                                    } else {
                                        String zzg2 = next2.zzg();
                                        if (next2.zzu()) {
                                            d = Double.valueOf(next2.zza());
                                        } else {
                                            d = null;
                                        }
                                        arrayMap.put(zzg2, d);
                                    }
                                } else {
                                    arrayMap.put(next2.zzg(), next2.zzw() ? Long.valueOf(next2.zzd()) : null);
                                }
                            }
                        }
                    }
                } else {
                    zzel next3 = it.next();
                    if (next3.zze().isEmpty()) {
                        this.zza.zzs.zzay().zzk().zzb("null or empty param name in filter. event", this.zza.zzs.zzj().zzc(zzh));
                        break;
                    }
                    hashSet.add(next3.zze());
                }
            }
            zzeg zzj2 = this.zza.zzs.zzay().zzj();
            if (bool2 != null) {
            }
            zzj2.zzb("Event filter result", obj);
            if (bool2 != null) {
            }
        } else {
            zzeg zzj3 = this.zza.zzs.zzay().zzj();
            Integer valueOf2 = Integer.valueOf(this.zzc);
            if (this.zzh.zzp()) {
                num2 = Integer.valueOf(this.zzh.zzb());
            }
            zzj3.zzc("Event filter already evaluated true and it is not associated with an enhanced audience. audience ID, filter ID", valueOf2, num2);
            return true;
        }
    }
}
