package com.google.android.gms.measurement.internal;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzfn;
import com.google.android.gms.internal.measurement.zzfo;
import com.google.android.gms.internal.measurement.zzfp;
import com.google.android.gms.internal.measurement.zzfq;
import com.google.android.gms.internal.measurement.zzfr;
import com.google.android.gms.internal.measurement.zzfs;
import com.google.android.gms.internal.measurement.zzfv;
import com.google.android.gms.internal.measurement.zzfw;
import com.google.android.gms.internal.measurement.zzfx;
import com.google.android.gms.internal.measurement.zzfy;
import com.google.android.gms.internal.measurement.zzfz;
import com.google.android.gms.internal.measurement.zzgb;
import com.google.android.gms.internal.measurement.zzgg;
import com.google.android.gms.internal.measurement.zzgh;
import com.google.android.gms.internal.measurement.zzoq;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import kotlinx.coroutines.DebugKt;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
final class zzgf implements Callable<byte[]> {
    final /* synthetic */ zzat zza;
    final /* synthetic */ String zzb;
    final /* synthetic */ zzgk zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzgf(zzgk zzgk, zzat zzat, String str) {
        this.zzc = zzgk;
        this.zza = zzat;
        this.zzb = str;
    }

    @Override // java.util.concurrent.Callable
    public final /* bridge */ /* synthetic */ byte[] call() throws Exception {
        byte[] bArr;
        zzkn zzkn;
        zzks zzks;
        byte[] bArr2;
        zzfv zzfv;
        Bundle bundle;
        zzfx zzfx;
        zzg zzg;
        String str;
        long j;
        zzap zzap;
        this.zzc.zza.zzA();
        zzia zzr = this.zzc.zza.zzr();
        zzat zzat = this.zza;
        String str2 = this.zzb;
        zzr.zzg();
        zzfs.zzO();
        Preconditions.checkNotNull(zzat);
        Preconditions.checkNotEmpty(str2);
        if (!zzr.zzs.zzf().zzs(str2, zzdw.zzU)) {
            zzr.zzs.zzay().zzc().zzb("Generating ScionPayload disabled. packageName", str2);
            return new byte[0];
        } else if ("_iap".equals(zzat.zza) || "_iapx".equals(zzat.zza)) {
            zzfv zza = zzfw.zza();
            zzr.zzf.zzi().zzw();
            try {
                zzg zzj = zzr.zzf.zzi().zzj(str2);
                if (zzj == null) {
                    zzr.zzs.zzay().zzc().zzb("Log and bundle not available. package_name", str2);
                    bArr = new byte[0];
                    zzkn = zzr.zzf;
                } else if (!zzj.zzaj()) {
                    zzr.zzs.zzay().zzc().zzb("Log and bundle disabled. package_name", str2);
                    bArr = new byte[0];
                    zzkn = zzr.zzf;
                } else {
                    zzfx zzu = zzfy.zzu();
                    zzu.zzaa(1);
                    zzu.zzW("android");
                    if (!TextUtils.isEmpty(zzj.zzt())) {
                        zzu.zzA(zzj.zzt());
                    }
                    if (!TextUtils.isEmpty(zzj.zzv())) {
                        zzu.zzC((String) Preconditions.checkNotNull(zzj.zzv()));
                    }
                    if (!TextUtils.isEmpty(zzj.zzw())) {
                        zzu.zzD((String) Preconditions.checkNotNull(zzj.zzw()));
                    }
                    if (zzj.zzb() != -2147483648L) {
                        zzu.zzE((int) zzj.zzb());
                    }
                    zzu.zzS(zzj.zzm());
                    zzu.zzM(zzj.zzk());
                    String zzz = zzj.zzz();
                    String zzr2 = zzj.zzr();
                    zzoq.zzc();
                    if (zzr.zzs.zzf().zzs(zzj.zzt(), zzdw.zzad)) {
                        String zzy = zzj.zzy();
                        if (!TextUtils.isEmpty(zzz)) {
                            zzu.zzR(zzz);
                        } else if (!TextUtils.isEmpty(zzy)) {
                            zzu.zzQ(zzy);
                        } else if (!TextUtils.isEmpty(zzr2)) {
                            zzu.zzy(zzr2);
                        }
                    } else if (!TextUtils.isEmpty(zzz)) {
                        zzu.zzR(zzz);
                    } else if (!TextUtils.isEmpty(zzr2)) {
                        zzu.zzy(zzr2);
                    }
                    zzag zzh = zzr.zzf.zzh(str2);
                    zzu.zzJ(zzj.zzj());
                    if (zzr.zzs.zzJ() && zzr.zzs.zzf().zzt(zzu.zzal()) && zzh.zzj() && !TextUtils.isEmpty(null)) {
                        zzu.zzL(null);
                    }
                    zzu.zzI(zzh.zzi());
                    if (zzh.zzj()) {
                        Pair<String, Boolean> zzd = zzr.zzf.zzs().zzd(zzj.zzt(), zzh);
                        if (zzj.zzai() && !TextUtils.isEmpty((CharSequence) zzd.first)) {
                            try {
                                zzu.zzab(zzia.zza((String) zzd.first, Long.toString(zzat.zzd)));
                                if (zzd.second != null) {
                                    zzu.zzU(((Boolean) zzd.second).booleanValue());
                                }
                            } catch (SecurityException e) {
                                zzr.zzs.zzay().zzc().zzb("Resettable device id encryption failed", e.getMessage());
                                bArr = new byte[0];
                                zzkn = zzr.zzf;
                            }
                        }
                    }
                    zzr.zzs.zzg().zzu();
                    zzu.zzK(Build.MODEL);
                    zzr.zzs.zzg().zzu();
                    zzu.zzV(Build.VERSION.RELEASE);
                    zzu.zzaf((int) zzr.zzs.zzg().zzb());
                    zzu.zzaj(zzr.zzs.zzg().zzc());
                    try {
                        if (zzh.zzk() && zzj.zzu() != null) {
                            zzu.zzB(zzia.zza((String) Preconditions.checkNotNull(zzj.zzu()), Long.toString(zzat.zzd)));
                        }
                        if (!TextUtils.isEmpty(zzj.zzx())) {
                            zzu.zzP((String) Preconditions.checkNotNull(zzj.zzx()));
                        }
                        String zzt = zzj.zzt();
                        List<zzks> zzu2 = zzr.zzf.zzi().zzu(zzt);
                        Iterator<zzks> it = zzu2.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                zzks = null;
                                break;
                            }
                            zzks = it.next();
                            if ("_lte".equals(zzks.zzc)) {
                                break;
                            }
                        }
                        if (zzks == null || zzks.zze == null) {
                            zzks zzks2 = new zzks(zzt, DebugKt.DEBUG_PROPERTY_VALUE_AUTO, "_lte", zzr.zzs.zzav().currentTimeMillis(), 0L);
                            zzu2.add(zzks2);
                            zzr.zzf.zzi().zzN(zzks2);
                        }
                        zzkp zzu3 = zzr.zzf.zzu();
                        zzu3.zzs.zzay().zzj().zza("Checking account type status for ad personalization signals");
                        if (zzu3.zzs.zzg().zze()) {
                            String zzt2 = zzj.zzt();
                            Preconditions.checkNotNull(zzt2);
                            if (zzj.zzai() && zzu3.zzf.zzo().zzk(zzt2)) {
                                zzu3.zzs.zzay().zzc().zza("Turning off ad personalization due to account type");
                                Iterator<zzks> it2 = zzu2.iterator();
                                while (true) {
                                    if (!it2.hasNext()) {
                                        break;
                                    } else if ("_npa".equals(it2.next().zzc)) {
                                        it2.remove();
                                        break;
                                    }
                                }
                                zzu2.add(new zzks(zzt2, DebugKt.DEBUG_PROPERTY_VALUE_AUTO, "_npa", zzu3.zzs.zzav().currentTimeMillis(), 1L));
                            }
                        }
                        zzgh[] zzghArr = new zzgh[zzu2.size()];
                        for (int i = 0; i < zzu2.size(); i++) {
                            zzgg zzd2 = zzgh.zzd();
                            zzd2.zzf(zzu2.get(i).zzc);
                            zzd2.zzg(zzu2.get(i).zzd);
                            zzr.zzf.zzu().zzv(zzd2, zzu2.get(i).zze);
                            zzghArr[i] = zzd2.zzaA();
                        }
                        zzu.zzi(Arrays.asList(zzghArr));
                        zzej zzb = zzej.zzb(zzat);
                        zzr.zzs.zzv().zzK(zzb.zzd, zzr.zzf.zzi().zzi(str2));
                        zzr.zzs.zzv().zzL(zzb, zzr.zzs.zzf().zzd(str2));
                        Bundle bundle2 = zzb.zzd;
                        bundle2.putLong("_c", 1);
                        zzr.zzs.zzay().zzc().zza("Marking in-app purchase as real-time");
                        bundle2.putLong("_r", 1);
                        bundle2.putString("_o", zzat.zzc);
                        if (zzr.zzs.zzv().zzad(zzu.zzal())) {
                            zzr.zzs.zzv().zzN(bundle2, "_dbg", 1L);
                            zzr.zzs.zzv().zzN(bundle2, "_r", 1L);
                        }
                        zzap zzn = zzr.zzf.zzi().zzn(str2, zzat.zza);
                        if (zzn == null) {
                            zzfx = zzu;
                            zzg = zzj;
                            zzfv = zza;
                            str = str2;
                            bundle = bundle2;
                            bArr2 = null;
                            zzap = new zzap(str2, zzat.zza, 0, 0, 0, zzat.zzd, 0, null, null, null, null);
                            j = 0;
                        } else {
                            zzg = zzj;
                            zzfv = zza;
                            str = str2;
                            bundle = bundle2;
                            zzfx = zzu;
                            bArr2 = null;
                            long j2 = zzn.zzf;
                            zzap = zzn.zzc(zzat.zzd);
                            j = j2;
                        }
                        zzr.zzf.zzi().zzE(zzap);
                        zzao zzao = new zzao(zzr.zzs, zzat.zzc, str, zzat.zza, zzat.zzd, j, bundle);
                        zzfn zze = zzfo.zze();
                        zze.zzm(zzao.zzd);
                        zze.zzi(zzao.zzb);
                        zze.zzl(zzao.zze);
                        zzaq zzaq = new zzaq(zzao.zzf);
                        while (zzaq.hasNext()) {
                            String zza2 = zzaq.next();
                            zzfr zze2 = zzfs.zze();
                            zze2.zzj(zza2);
                            Object zzf = zzao.zzf.zzf(zza2);
                            if (zzf != null) {
                                zzr.zzf.zzu().zzu(zze2, zzf);
                                zze.zze(zze2);
                            }
                        }
                        zzfx.zzj(zze);
                        zzfz zza3 = zzgb.zza();
                        zzfp zza4 = zzfq.zza();
                        zza4.zza(zzap.zzc);
                        zza4.zzb(zzat.zza);
                        zza3.zza(zza4);
                        zzfx.zzX(zza3);
                        zzfx.zzf(zzr.zzf.zzf().zza(zzg.zzt(), Collections.emptyList(), zzfx.zzap(), Long.valueOf(zze.zzc()), Long.valueOf(zze.zzc())));
                        if (zze.zzq()) {
                            zzfx.zzae(zze.zzc());
                            zzfx.zzN(zze.zzc());
                        }
                        long zzn2 = zzg.zzn();
                        int i2 = (zzn2 > 0 ? 1 : (zzn2 == 0 ? 0 : -1));
                        if (i2 != 0) {
                            zzfx.zzY(zzn2);
                        }
                        long zzp = zzg.zzp();
                        if (zzp != 0) {
                            zzfx.zzZ(zzp);
                        } else if (i2 != 0) {
                            zzfx.zzZ(zzn2);
                        }
                        zzg.zzE();
                        zzfx.zzF((int) zzg.zzo());
                        zzr.zzs.zzf().zzh();
                        zzfx.zzah(42097);
                        zzfx.zzag(zzr.zzs.zzav().currentTimeMillis());
                        zzfx.zzad(Boolean.TRUE.booleanValue());
                        zzfv.zza(zzfx);
                        zzg.zzad(zzfx.zzd());
                        zzg.zzab(zzfx.zzc());
                        zzr.zzf.zzi().zzD(zzg);
                        zzr.zzf.zzi().zzC();
                        try {
                            return zzr.zzf.zzu().zzz(zzfv.zzaA().zzbs());
                        } catch (IOException e2) {
                            zzr.zzs.zzay().zzd().zzc("Data loss. Failed to bundle and serialize. appId", zzei.zzn(str), e2);
                            return bArr2;
                        }
                    } catch (SecurityException e3) {
                        zzr.zzs.zzay().zzc().zzb("app instance id encryption failed", e3.getMessage());
                        bArr = new byte[0];
                        zzkn = zzr.zzf;
                    }
                }
                zzkn.zzi().zzx();
                return bArr;
            } finally {
                zzr.zzf.zzi().zzx();
            }
        } else {
            zzr.zzs.zzay().zzc().zzc("Generating a payload for this event is not available. package_name, event_name", str2, zzat.zza);
            return null;
        }
    }
}
