package com.google.android.gms.measurement.internal;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.ProcessUtils;
import com.google.android.gms.common.wrappers.Wrappers;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import okhttp3.internal.cache.DiskLruCache;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzaf extends zzgl {
    private Boolean zza;
    private zzae zzb = zzad.zza;
    private Boolean zzc;

    public zzaf(zzfs zzfs) {
        super(zzfs);
    }

    public static final long zzA() {
        return zzdw.zzC.zza(null).longValue();
    }

    private final String zzB(String str, String str2) {
        try {
            String str3 = (String) Class.forName("android.os.SystemProperties").getMethod("get", String.class, String.class).invoke(null, str, "");
            Preconditions.checkNotNull(str3);
            return str3;
        } catch (ClassNotFoundException e) {
            this.zzs.zzay().zzd().zzb("Could not find SystemProperties class", e);
            return "";
        } catch (IllegalAccessException e2) {
            this.zzs.zzay().zzd().zzb("Could not access SystemProperties.get()", e2);
            return "";
        } catch (NoSuchMethodException e3) {
            this.zzs.zzay().zzd().zzb("Could not find SystemProperties.get() method", e3);
            return "";
        } catch (InvocationTargetException e4) {
            this.zzs.zzay().zzd().zzb("SystemProperties.get() threw an exception", e4);
            return "";
        }
    }

    public static final long zzz() {
        return zzdw.zzc.zza(null).longValue();
    }

    public final double zza(String str, zzdv<Double> zzdv) {
        if (str == null) {
            return zzdv.zza(null).doubleValue();
        }
        String zza = this.zzb.zza(str, zzdv.zzb());
        if (TextUtils.isEmpty(zza)) {
            return zzdv.zza(null).doubleValue();
        }
        try {
            return zzdv.zza(Double.valueOf(Double.parseDouble(zza))).doubleValue();
        } catch (NumberFormatException e) {
            return zzdv.zza(null).doubleValue();
        }
    }

    public final int zzb(String str) {
        return zzf(str, zzdw.zzG, 500, 2000);
    }

    public final int zzc() {
        zzku zzv = this.zzs.zzv();
        Boolean zzj = zzv.zzs.zzt().zzj();
        if (zzv.zzm() < 201500) {
            return (zzj == null || zzj.booleanValue()) ? 25 : 100;
        }
        return 100;
    }

    public final int zzd(String str) {
        return zzf(str, zzdw.zzH, 25, 100);
    }

    public final int zze(String str, zzdv<Integer> zzdv) {
        if (str == null) {
            return zzdv.zza(null).intValue();
        }
        String zza = this.zzb.zza(str, zzdv.zzb());
        if (TextUtils.isEmpty(zza)) {
            return zzdv.zza(null).intValue();
        }
        try {
            return zzdv.zza(Integer.valueOf(Integer.parseInt(zza))).intValue();
        } catch (NumberFormatException e) {
            return zzdv.zza(null).intValue();
        }
    }

    public final int zzf(String str, zzdv<Integer> zzdv, int i, int i2) {
        return Math.max(Math.min(zze(str, zzdv), i2), i);
    }

    public final long zzh() {
        this.zzs.zzaw();
        return 42097;
    }

    public final long zzi(String str, zzdv<Long> zzdv) {
        if (str == null) {
            return zzdv.zza(null).longValue();
        }
        String zza = this.zzb.zza(str, zzdv.zzb());
        if (TextUtils.isEmpty(zza)) {
            return zzdv.zza(null).longValue();
        }
        try {
            return zzdv.zza(Long.valueOf(Long.parseLong(zza))).longValue();
        } catch (NumberFormatException e) {
            return zzdv.zza(null).longValue();
        }
    }

    final Bundle zzj() {
        try {
            if (this.zzs.zzau().getPackageManager() == null) {
                this.zzs.zzay().zzd().zza("Failed to load metadata: PackageManager is null");
                return null;
            }
            ApplicationInfo applicationInfo = Wrappers.packageManager(this.zzs.zzau()).getApplicationInfo(this.zzs.zzau().getPackageName(), 128);
            if (applicationInfo != null) {
                return applicationInfo.metaData;
            }
            this.zzs.zzay().zzd().zza("Failed to load metadata: ApplicationInfo is null");
            return null;
        } catch (PackageManager.NameNotFoundException e) {
            this.zzs.zzay().zzd().zzb("Failed to load metadata: Package name not found", e);
            return null;
        }
    }

    public final Boolean zzk(String str) {
        Preconditions.checkNotEmpty(str);
        Bundle zzj = zzj();
        if (zzj == null) {
            this.zzs.zzay().zzd().zza("Failed to load metadata: Metadata bundle is null");
            return null;
        } else if (!zzj.containsKey(str)) {
            return null;
        } else {
            return Boolean.valueOf(zzj.getBoolean(str));
        }
    }

    public final String zzl() {
        return zzB("debug.firebase.analytics.app", "");
    }

    public final String zzm() {
        return zzB("debug.deferred.deeplink", "");
    }

    public final String zzn() {
        this.zzs.zzaw();
        return "FA";
    }

    public final String zzo(String str, zzdv<String> zzdv) {
        if (str == null) {
            return zzdv.zza(null);
        }
        return zzdv.zza(this.zzb.zza(str, zzdv.zzb()));
    }

    public final List<String> zzp(String str) {
        Integer num;
        Preconditions.checkNotEmpty("analytics.safelisted_events");
        Bundle zzj = zzj();
        if (zzj == null) {
            this.zzs.zzay().zzd().zza("Failed to load metadata: Metadata bundle is null");
            num = null;
        } else if (!zzj.containsKey("analytics.safelisted_events")) {
            num = null;
        } else {
            num = Integer.valueOf(zzj.getInt("analytics.safelisted_events"));
        }
        if (num == null) {
            return null;
        }
        try {
            String[] stringArray = this.zzs.zzau().getResources().getStringArray(num.intValue());
            if (stringArray == null) {
                return null;
            }
            return Arrays.asList(stringArray);
        } catch (Resources.NotFoundException e) {
            this.zzs.zzay().zzd().zzb("Failed to load string array from metadata: resource not found", e);
            return null;
        }
    }

    public final void zzq(zzae zzae) {
        this.zzb = zzae;
    }

    public final boolean zzr() {
        Boolean zzk = zzk("google_analytics_adid_collection_enabled");
        return zzk == null || zzk.booleanValue();
    }

    public final boolean zzs(String str, zzdv<Boolean> zzdv) {
        if (str == null) {
            return zzdv.zza(null).booleanValue();
        }
        String zza = this.zzb.zza(str, zzdv.zzb());
        if (TextUtils.isEmpty(zza)) {
            return zzdv.zza(null).booleanValue();
        }
        return zzdv.zza(Boolean.valueOf(Boolean.parseBoolean(zza))).booleanValue();
    }

    public final boolean zzt(String str) {
        return DiskLruCache.VERSION_1.equals(this.zzb.zza(str, "gaia_collection_enabled"));
    }

    public final boolean zzu() {
        Boolean zzk = zzk("google_analytics_automatic_screen_reporting_enabled");
        return zzk == null || zzk.booleanValue();
    }

    public final boolean zzv() {
        this.zzs.zzaw();
        Boolean zzk = zzk("firebase_analytics_collection_deactivated");
        return zzk != null && zzk.booleanValue();
    }

    public final boolean zzw(String str) {
        return DiskLruCache.VERSION_1.equals(this.zzb.zza(str, "measurement.event_sampling_enabled"));
    }

    public final boolean zzx() {
        if (this.zza == null) {
            this.zza = zzk("app_measurement_lite");
            if (this.zza == null) {
                this.zza = false;
            }
        }
        if (this.zza.booleanValue() || !this.zzs.zzN()) {
            return true;
        }
        return false;
    }

    @EnsuresNonNull({"this.isMainProcess"})
    public final boolean zzy() {
        if (this.zzc == null) {
            synchronized (this) {
                if (this.zzc == null) {
                    ApplicationInfo applicationInfo = this.zzs.zzau().getApplicationInfo();
                    String myProcessName = ProcessUtils.getMyProcessName();
                    if (applicationInfo != null) {
                        String str = applicationInfo.processName;
                        boolean z = false;
                        if (str != null && str.equals(myProcessName)) {
                            z = true;
                        }
                        this.zzc = Boolean.valueOf(z);
                    }
                    if (this.zzc == null) {
                        this.zzc = Boolean.TRUE;
                        this.zzs.zzay().zzd().zza("My process not in the list of running processes");
                    }
                }
            }
        }
        return this.zzc.booleanValue();
    }
}
