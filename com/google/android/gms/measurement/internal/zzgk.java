package com.google.android.gms.measurement.internal;

import android.content.ContentValues;
import android.database.sqlite.SQLiteException;
import android.os.Binder;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.GoogleSignatureVerifier;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.UidVerifier;
import com.google.android.gms.internal.measurement.zzaa;
import com.google.android.gms.internal.measurement.zzc;
import com.google.android.gms.internal.measurement.zzd;
import com.google.android.gms.internal.measurement.zzpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzgk extends zzdy {
    private final zzkn zza;
    private Boolean zzb;
    private String zzc = null;

    public zzgk(zzkn zzkn, String str) {
        Preconditions.checkNotNull(zzkn);
        this.zza = zzkn;
    }

    private final void zzA(String str, boolean z) {
        boolean z2;
        if (!TextUtils.isEmpty(str)) {
            if (z) {
                try {
                    if (this.zzb == null) {
                        if ("com.google.android.gms".equals(this.zzc)) {
                            z2 = true;
                        } else if (!UidVerifier.isGooglePlayServicesUid(this.zza.zzau(), Binder.getCallingUid())) {
                            z2 = GoogleSignatureVerifier.getInstance(this.zza.zzau()).isUidGoogleSigned(Binder.getCallingUid());
                        } else {
                            z2 = true;
                        }
                        this.zzb = Boolean.valueOf(z2);
                    }
                    if (this.zzb.booleanValue()) {
                        return;
                    }
                } catch (SecurityException e) {
                    this.zza.zzay().zzd().zzb("Measurement Service called with invalid calling package. appId", zzei.zzn(str));
                    throw e;
                }
            }
            if (this.zzc == null && GooglePlayServicesUtilLight.uidHasPackageName(this.zza.zzau(), Binder.getCallingUid(), str)) {
                this.zzc = str;
            }
            if (!str.equals(this.zzc)) {
                throw new SecurityException(String.format("Unknown calling package name '%s'.", str));
            }
            return;
        }
        this.zza.zzay().zzd().zza("Measurement Service called without app package");
        throw new SecurityException("Measurement Service called without app package");
    }

    public final void zzB(zzat zzat, zzp zzp) {
        this.zza.zzA();
        this.zza.zzD(zzat, zzp);
    }

    private final void zzz(zzp zzp, boolean z) {
        Preconditions.checkNotNull(zzp);
        Preconditions.checkNotEmpty(zzp.zza);
        zzA(zzp.zza, false);
        this.zza.zzv().zzW(zzp.zzb, zzp.zzq, zzp.zzu);
    }

    public final zzat zzb(zzat zzat, zzp zzp) {
        zzar zzar;
        if (!(!"_cmp".equals(zzat.zza) || (zzar = zzat.zzb) == null || zzar.zza() == 0)) {
            String zzg = zzat.zzb.zzg("_cis");
            if ("referrer broadcast".equals(zzg) || "referrer API".equals(zzg)) {
                this.zza.zzay().zzi().zzb("Event has been filtered ", zzat.toString());
                return new zzat("_cmpx", zzat.zzb, zzat.zzc, zzat.zzd);
            }
        }
        return zzat;
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final String zzd(zzp zzp) {
        zzz(zzp, false);
        return this.zza.zzx(zzp);
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final List<zzkq> zze(zzp zzp, boolean z) {
        zzz(zzp, false);
        String str = zzp.zza;
        Preconditions.checkNotNull(str);
        try {
            List<zzks> list = (List) this.zza.zzaz().zzh(new zzgh(this, str)).get();
            ArrayList arrayList = new ArrayList(list.size());
            for (zzks zzks : list) {
                if (z || !zzku.zzag(zzks.zzc)) {
                    arrayList.add(new zzkq(zzks));
                }
            }
            return arrayList;
        } catch (InterruptedException | ExecutionException e) {
            this.zza.zzay().zzd().zzc("Failed to get user properties. appId", zzei.zzn(zzp.zza), e);
            return null;
        }
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final List<zzab> zzf(String str, String str2, zzp zzp) {
        zzz(zzp, false);
        String str3 = zzp.zza;
        Preconditions.checkNotNull(str3);
        try {
            return (List) this.zza.zzaz().zzh(new zzfy(this, str3, str, str2)).get();
        } catch (InterruptedException | ExecutionException e) {
            this.zza.zzay().zzd().zzb("Failed to get conditional user properties", e);
            return Collections.emptyList();
        }
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final List<zzab> zzg(String str, String str2, String str3) {
        zzA(str, true);
        try {
            return (List) this.zza.zzaz().zzh(new zzfz(this, str, str2, str3)).get();
        } catch (InterruptedException | ExecutionException e) {
            this.zza.zzay().zzd().zzb("Failed to get conditional user properties as", e);
            return Collections.emptyList();
        }
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final List<zzkq> zzh(String str, String str2, boolean z, zzp zzp) {
        zzz(zzp, false);
        String str3 = zzp.zza;
        Preconditions.checkNotNull(str3);
        try {
            List<zzks> list = (List) this.zza.zzaz().zzh(new zzfw(this, str3, str, str2)).get();
            ArrayList arrayList = new ArrayList(list.size());
            for (zzks zzks : list) {
                if (z || !zzku.zzag(zzks.zzc)) {
                    arrayList.add(new zzkq(zzks));
                }
            }
            return arrayList;
        } catch (InterruptedException | ExecutionException e) {
            this.zza.zzay().zzd().zzc("Failed to query user properties. appId", zzei.zzn(zzp.zza), e);
            return Collections.emptyList();
        }
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final List<zzkq> zzi(String str, String str2, String str3, boolean z) {
        zzA(str, true);
        try {
            List<zzks> list = (List) this.zza.zzaz().zzh(new zzfx(this, str, str2, str3)).get();
            ArrayList arrayList = new ArrayList(list.size());
            for (zzks zzks : list) {
                if (z || !zzku.zzag(zzks.zzc)) {
                    arrayList.add(new zzkq(zzks));
                }
            }
            return arrayList;
        } catch (InterruptedException | ExecutionException e) {
            this.zza.zzay().zzd().zzc("Failed to get user properties as. appId", zzei.zzn(str), e);
            return Collections.emptyList();
        }
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzj(zzp zzp) {
        zzz(zzp, false);
        zzy(new zzgi(this, zzp));
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzk(zzat zzat, zzp zzp) {
        Preconditions.checkNotNull(zzat);
        zzz(zzp, false);
        zzy(new zzgd(this, zzat, zzp));
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzl(zzat zzat, String str, String str2) {
        Preconditions.checkNotNull(zzat);
        Preconditions.checkNotEmpty(str);
        zzA(str, true);
        zzy(new zzge(this, zzat, str));
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzm(zzp zzp) {
        Preconditions.checkNotEmpty(zzp.zza);
        zzA(zzp.zza, false);
        zzy(new zzga(this, zzp));
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzn(zzab zzab, zzp zzp) {
        Preconditions.checkNotNull(zzab);
        Preconditions.checkNotNull(zzab.zzc);
        zzz(zzp, false);
        zzab zzab2 = new zzab(zzab);
        zzab2.zza = zzp.zza;
        zzy(new zzfu(this, zzab2, zzp));
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzo(zzab zzab) {
        Preconditions.checkNotNull(zzab);
        Preconditions.checkNotNull(zzab.zzc);
        Preconditions.checkNotEmpty(zzab.zza);
        zzA(zzab.zza, true);
        zzy(new zzfv(this, new zzab(zzab)));
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzp(zzp zzp) {
        Preconditions.checkNotEmpty(zzp.zza);
        Preconditions.checkNotNull(zzp.zzv);
        zzgc zzgc = new zzgc(this, zzp);
        Preconditions.checkNotNull(zzgc);
        if (this.zza.zzaz().zzs()) {
            zzgc.run();
        } else {
            this.zza.zzaz().zzq(zzgc);
        }
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzq(long j, String str, String str2, String str3) {
        zzy(new zzgj(this, str2, str3, str, j));
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzr(Bundle bundle, zzp zzp) {
        zzz(zzp, false);
        String str = zzp.zza;
        Preconditions.checkNotNull(str);
        zzy(new Runnable(str, bundle) { // from class: com.google.android.gms.measurement.internal.zzft
            public final /* synthetic */ String zzb;
            public final /* synthetic */ Bundle zzc;

            {
                this.zzb = r2;
                this.zzc = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                zzgk.this.zzx(this.zzb, this.zzc);
            }
        });
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzs(zzp zzp) {
        zzz(zzp, false);
        zzy(new zzgb(this, zzp));
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzt(zzkq zzkq, zzp zzp) {
        Preconditions.checkNotNull(zzkq);
        zzz(zzp, false);
        zzy(new zzgg(this, zzkq, zzp));
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final byte[] zzu(zzat zzat, String str) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(zzat);
        zzA(str, true);
        this.zza.zzay().zzc().zzb("Log and bundle. event", this.zza.zzj().zzc(zzat.zza));
        long nanoTime = this.zza.zzav().nanoTime() / 1000000;
        try {
            byte[] bArr = (byte[]) this.zza.zzaz().zzi(new zzgf(this, zzat, str)).get();
            if (bArr == null) {
                this.zza.zzay().zzd().zzb("Log and bundle returned null. appId", zzei.zzn(str));
                bArr = new byte[0];
            }
            this.zza.zzay().zzc().zzd("Log and bundle processed. event, size, time_ms", this.zza.zzj().zzc(zzat.zza), Integer.valueOf(bArr.length), Long.valueOf((this.zza.zzav().nanoTime() / 1000000) - nanoTime));
            return bArr;
        } catch (InterruptedException | ExecutionException e) {
            this.zza.zzay().zzd().zzd("Failed to log and bundle. appId, event, error", zzei.zzn(str), this.zza.zzj().zzc(zzat.zza), e);
            return null;
        }
    }

    public final void zzw(zzat zzat, zzp zzp) {
        if (!this.zza.zzo().zzl(zzp.zza)) {
            zzB(zzat, zzp);
            return;
        }
        this.zza.zzay().zzj().zzb("EES config found for", zzp.zza);
        zzfj zzo = this.zza.zzo();
        String str = zzp.zza;
        zzpl.zzc();
        zzc zzc = null;
        if (zzo.zzs.zzf().zzs(null, zzdw.zzav) && !TextUtils.isEmpty(str)) {
            zzc = zzo.zza.get(str);
        }
        if (zzc != null) {
            try {
                Map<String, Object> zzt = zzkp.zzt(zzat.zzb.zzc(), true);
                String zza = zzgp.zza(zzat.zza);
                if (zza == null) {
                    zza = zzat.zza;
                }
                if (zzc.zze(new zzaa(zza, zzat.zzd, zzt))) {
                    if (zzc.zzg()) {
                        this.zza.zzay().zzj().zzb("EES edited event", zzat.zza);
                        zzB(zzkp.zzi(zzc.zza().zzb()), zzp);
                    } else {
                        zzB(zzat, zzp);
                    }
                    if (zzc.zzf()) {
                        for (zzaa zzaa : zzc.zza().zzc()) {
                            this.zza.zzay().zzj().zzb("EES logging created event", zzaa.zzd());
                            zzB(zzkp.zzi(zzaa), zzp);
                        }
                        return;
                    }
                    return;
                }
            } catch (zzd e) {
                this.zza.zzay().zzd().zzc("EES error. appId, eventName", zzp.zzb, zzat.zza);
            }
            this.zza.zzay().zzj().zzb("EES was not applied to event", zzat.zza);
            zzB(zzat, zzp);
            return;
        }
        this.zza.zzay().zzj().zzb("EES not loaded for", zzp.zza);
        zzB(zzat, zzp);
    }

    public final /* synthetic */ void zzx(String str, Bundle bundle) {
        zzaj zzi = this.zza.zzi();
        zzi.zzg();
        zzi.zzY();
        byte[] zzbs = zzi.zzf.zzu().zzj(new zzao(zzi.zzs, "", str, "dep", 0, 0, bundle)).zzbs();
        zzi.zzs.zzay().zzj().zzc("Saving default event parameters, appId, data size", zzi.zzs.zzj().zzc(str), Integer.valueOf(zzbs.length));
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", str);
        contentValues.put("parameters", zzbs);
        try {
            if (zzi.zzh().insertWithOnConflict("default_event_params", null, contentValues, 5) == -1) {
                zzi.zzs.zzay().zzd().zzb("Failed to insert default event parameters (got -1). appId", zzei.zzn(str));
            }
        } catch (SQLiteException e) {
            zzi.zzs.zzay().zzd().zzc("Error storing default event parameters. appId", zzei.zzn(str), e);
        }
    }

    final void zzy(Runnable runnable) {
        Preconditions.checkNotNull(runnable);
        if (this.zza.zzaz().zzs()) {
            runnable.run();
        } else {
            this.zza.zzaz().zzp(runnable);
        }
    }
}
