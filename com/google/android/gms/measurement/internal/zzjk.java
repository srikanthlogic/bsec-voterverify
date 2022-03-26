package com.google.android.gms.measurement.internal;

import android.util.Pair;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Locale;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzjk extends zzkd {
    public final zzet zza;
    public final zzet zzb;
    public final zzet zzc;
    public final zzet zzd;
    public final zzet zze;
    private String zzg;
    private boolean zzh;
    private long zzi;

    public zzjk(zzkn zzkn) {
        super(zzkn);
        zzex zzm = this.zzs.zzm();
        zzm.getClass();
        this.zza = new zzet(zzm, "last_delete_stale", 0);
        zzex zzm2 = this.zzs.zzm();
        zzm2.getClass();
        this.zzb = new zzet(zzm2, "backoff", 0);
        zzex zzm3 = this.zzs.zzm();
        zzm3.getClass();
        this.zzc = new zzet(zzm3, "last_upload", 0);
        zzex zzm4 = this.zzs.zzm();
        zzm4.getClass();
        this.zzd = new zzet(zzm4, "last_upload_attempt", 0);
        zzex zzm5 = this.zzs.zzm();
        zzm5.getClass();
        this.zze = new zzet(zzm5, "midnight_offset", 0);
    }

    @Deprecated
    final Pair<String, Boolean> zza(String str) {
        zzg();
        long elapsedRealtime = this.zzs.zzav().elapsedRealtime();
        String str2 = this.zzg;
        if (str2 != null && elapsedRealtime < this.zzi) {
            return new Pair<>(str2, Boolean.valueOf(this.zzh));
        }
        this.zzi = elapsedRealtime + this.zzs.zzf().zzi(str, zzdw.zza);
        AdvertisingIdClient.setShouldSkipGmsCoreVersionCheck(true);
        try {
            AdvertisingIdClient.Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(this.zzs.zzau());
            this.zzg = "";
            String id = advertisingIdInfo.getId();
            if (id != null) {
                this.zzg = id;
            }
            this.zzh = advertisingIdInfo.isLimitAdTrackingEnabled();
        } catch (Exception e) {
            this.zzs.zzay().zzc().zzb("Unable to get advertising id", e);
            this.zzg = "";
        }
        AdvertisingIdClient.setShouldSkipGmsCoreVersionCheck(false);
        return new Pair<>(this.zzg, Boolean.valueOf(this.zzh));
    }

    @Override // com.google.android.gms.measurement.internal.zzkd
    protected final boolean zzb() {
        return false;
    }

    public final Pair<String, Boolean> zzd(String str, zzag zzag) {
        if (zzag.zzj()) {
            return zza(str);
        }
        return new Pair<>("", false);
    }

    @Deprecated
    public final String zzf(String str) {
        zzg();
        String str2 = (String) zza(str).first;
        MessageDigest zzE = zzku.zzE(MessageDigestAlgorithms.MD5);
        if (zzE == null) {
            return null;
        }
        return String.format(Locale.US, "%032X", new BigInteger(1, zzE.digest(str2.getBytes())));
    }
}
