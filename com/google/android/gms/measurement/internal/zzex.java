package com.google.android.gms.measurement.internal;

import android.content.SharedPreferences;
import android.util.Pair;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.internal.Preconditions;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzex extends zzgm {
    static final Pair<String, Long> zza = new Pair<>("", 0L);
    public zzev zzb;
    public boolean zzk;
    private SharedPreferences zzt;
    private String zzu;
    private boolean zzv;
    private long zzw;
    public final zzet zzf = new zzet(this, "session_timeout", 1800000);
    public final zzer zzg = new zzer(this, "start_new_session", true);
    public final zzet zzj = new zzet(this, "last_pause_time", 0);
    public final zzew zzh = new zzew(this, "non_personalized_ads", null);
    public final zzer zzi = new zzer(this, "allow_remote_dynamite", false);
    public final zzet zzc = new zzet(this, "first_open_time", 0);
    public final zzet zzd = new zzet(this, "app_install_time", 0);
    public final zzew zze = new zzew(this, "app_instance_id", null);
    public final zzer zzl = new zzer(this, "app_backgrounded", false);
    public final zzer zzm = new zzer(this, "deep_link_retrieval_complete", false);
    public final zzet zzn = new zzet(this, "deep_link_retrieval_attempts", 0);
    public final zzew zzo = new zzew(this, "firebase_feature_rollouts", null);
    public final zzew zzp = new zzew(this, "deferred_attribution_cache", null);
    public final zzet zzq = new zzet(this, "deferred_attribution_cache_timestamp", 0);
    public final zzes zzr = new zzes(this, "default_event_parameters", null);

    public zzex(zzfs zzfs) {
        super(zzfs);
    }

    public final SharedPreferences zza() {
        zzg();
        zzu();
        Preconditions.checkNotNull(this.zzt);
        return this.zzt;
    }

    @Override // com.google.android.gms.measurement.internal.zzgm
    @EnsuresNonNull.List({@EnsuresNonNull({"this.preferences"}), @EnsuresNonNull({"this.monitoringSample"})})
    protected final void zzaA() {
        this.zzt = this.zzs.zzau().getSharedPreferences("com.google.android.gms.measurement.prefs", 0);
        boolean z = this.zzt.getBoolean("has_been_opened", false);
        this.zzk = z;
        if (!z) {
            SharedPreferences.Editor edit = this.zzt.edit();
            edit.putBoolean("has_been_opened", true);
            edit.apply();
        }
        this.zzs.zzf();
        this.zzb = new zzev(this, "health_monitor", Math.max(0L, zzdw.zzb.zza(null).longValue()), null);
    }

    public final Pair<String, Boolean> zzb(String str) {
        zzg();
        long elapsedRealtime = this.zzs.zzav().elapsedRealtime();
        String str2 = this.zzu;
        if (str2 != null && elapsedRealtime < this.zzw) {
            return new Pair<>(str2, Boolean.valueOf(this.zzv));
        }
        this.zzw = elapsedRealtime + this.zzs.zzf().zzi(str, zzdw.zza);
        AdvertisingIdClient.setShouldSkipGmsCoreVersionCheck(true);
        try {
            AdvertisingIdClient.Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(this.zzs.zzau());
            this.zzu = "";
            String id = advertisingIdInfo.getId();
            if (id != null) {
                this.zzu = id;
            }
            this.zzv = advertisingIdInfo.isLimitAdTrackingEnabled();
        } catch (Exception e) {
            this.zzs.zzay().zzc().zzb("Unable to get advertising id", e);
            this.zzu = "";
        }
        AdvertisingIdClient.setShouldSkipGmsCoreVersionCheck(false);
        return new Pair<>(this.zzu, Boolean.valueOf(this.zzv));
    }

    public final zzag zzc() {
        zzg();
        return zzag.zzb(zza().getString("consent_settings", "G1"));
    }

    public final Boolean zzd() {
        zzg();
        if (zza().contains("measurement_enabled")) {
            return Boolean.valueOf(zza().getBoolean("measurement_enabled", true));
        }
        return null;
    }

    @Override // com.google.android.gms.measurement.internal.zzgm
    protected final boolean zzf() {
        return true;
    }

    public final void zzh(Boolean bool) {
        zzg();
        SharedPreferences.Editor edit = zza().edit();
        if (bool != null) {
            edit.putBoolean("measurement_enabled", bool.booleanValue());
        } else {
            edit.remove("measurement_enabled");
        }
        edit.apply();
    }

    public final void zzi(boolean z) {
        zzg();
        this.zzs.zzay().zzj().zzb("App measurement setting deferred collection", Boolean.valueOf(z));
        SharedPreferences.Editor edit = zza().edit();
        edit.putBoolean("deferred_analytics_collection", z);
        edit.apply();
    }

    public final boolean zzj() {
        SharedPreferences sharedPreferences = this.zzt;
        if (sharedPreferences == null) {
            return false;
        }
        return sharedPreferences.contains("deferred_analytics_collection");
    }

    public final boolean zzk(long j) {
        return j - this.zzf.zza() > this.zzj.zza();
    }

    public final boolean zzl(int i) {
        return zzag.zzl(i, zza().getInt("consent_source", 100));
    }
}
