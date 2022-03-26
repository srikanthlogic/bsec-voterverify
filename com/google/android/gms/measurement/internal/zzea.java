package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.text.TextUtils;
import androidx.core.os.EnvironmentCompat;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.wrappers.InstantApps;
import com.google.android.gms.common.wrappers.Wrappers;
import com.google.android.gms.internal.measurement.zzoq;
import com.google.android.gms.internal.measurement.zzqd;
import java.security.MessageDigest;
import java.util.List;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzea extends zzf {
    private String zza;
    private String zzb;
    private int zzc;
    private String zzd;
    private String zze;
    private long zzf;
    private final long zzg;
    private List<String> zzh;
    private int zzi;
    private String zzj;
    private String zzk;
    private String zzl;

    public zzea(zzfs zzfs, long j) {
        super(zzfs);
        this.zzg = j;
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x00d6  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00e7  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00f7  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0107  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0117  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0127  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0137  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0147  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0157  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0173  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0194  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0196  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x01ac A[Catch: IllegalStateException -> 0x023a, TRY_ENTER, TryCatch #3 {IllegalStateException -> 0x023a, blocks: (B:47:0x017c, B:51:0x0197, B:54:0x01ac, B:57:0x01c6, B:58:0x01ca, B:61:0x01d9, B:63:0x01e1, B:65:0x01e7, B:66:0x01ee, B:68:0x01f4, B:71:0x020e, B:72:0x0212, B:74:0x021b, B:76:0x0231, B:77:0x0234, B:78:0x0236), top: B:103:0x017c }] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x01ee A[Catch: IllegalStateException -> 0x023a, TryCatch #3 {IllegalStateException -> 0x023a, blocks: (B:47:0x017c, B:51:0x0197, B:54:0x01ac, B:57:0x01c6, B:58:0x01ca, B:61:0x01d9, B:63:0x01e1, B:65:0x01e7, B:66:0x01ee, B:68:0x01f4, B:71:0x020e, B:72:0x0212, B:74:0x021b, B:76:0x0231, B:77:0x0234, B:78:0x0236), top: B:103:0x017c }] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x021b A[Catch: IllegalStateException -> 0x023a, TryCatch #3 {IllegalStateException -> 0x023a, blocks: (B:47:0x017c, B:51:0x0197, B:54:0x01ac, B:57:0x01c6, B:58:0x01ca, B:61:0x01d9, B:63:0x01e1, B:65:0x01e7, B:66:0x01ee, B:68:0x01f4, B:71:0x020e, B:72:0x0212, B:74:0x021b, B:76:0x0231, B:77:0x0234, B:78:0x0236), top: B:103:0x017c }] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0264  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x029d  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x02aa  */
    @Override // com.google.android.gms.measurement.internal.zzf
    @EnsuresNonNull({"appId", "appStore", "appName", "gmpAppId", "gaAppId"})
    /* Code decompiled incorrectly, please refer to instructions dump */
    protected final void zzd() {
        String str;
        String str2;
        Object[] objArr;
        int zza;
        List<String> zzp;
        String packageName = this.zzs.zzau().getPackageName();
        PackageManager packageManager = this.zzs.zzau().getPackageManager();
        String str3 = "Unknown";
        int i = Integer.MIN_VALUE;
        String str4 = "";
        String str5 = EnvironmentCompat.MEDIA_UNKNOWN;
        if (packageManager == null) {
            this.zzs.zzay().zzd().zzb("PackageManager is null, app identity information might be inaccurate. appId", zzei.zzn(packageName));
            str2 = str3;
        } else {
            try {
                str5 = packageManager.getInstallerPackageName(packageName);
            } catch (IllegalArgumentException e) {
                this.zzs.zzay().zzd().zzb("Error retrieving app installer package name. appId", zzei.zzn(packageName));
            }
            if (str5 == null) {
                str5 = "manual_install";
            } else if ("com.android.vending".equals(str5)) {
                str5 = str4;
            }
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(this.zzs.zzau().getPackageName(), 0);
                if (packageInfo != null) {
                    CharSequence applicationLabel = packageManager.getApplicationLabel(packageInfo.applicationInfo);
                    if (!TextUtils.isEmpty(applicationLabel)) {
                        str2 = applicationLabel.toString();
                    } else {
                        str2 = str3;
                    }
                    try {
                        str3 = packageInfo.versionName;
                        i = packageInfo.versionCode;
                    } catch (PackageManager.NameNotFoundException e2) {
                        str = str3;
                        str3 = str2;
                        this.zzs.zzay().zzd().zzc("Error retrieving package info. appId, appName", zzei.zzn(packageName), str3);
                        str2 = str3;
                        str3 = str;
                        this.zza = packageName;
                        this.zzd = str5;
                        this.zzb = str3;
                        this.zzc = i;
                        this.zze = str2;
                        this.zzf = 0;
                        if (!TextUtils.isEmpty(this.zzs.zzw())) {
                        }
                        zza = this.zzs.zza();
                        switch (zza) {
                        }
                        this.zzj = str4;
                        this.zzk = str4;
                        this.zzl = str4;
                        this.zzs.zzaw();
                        if (objArr != null) {
                        }
                        String zzc = zzib.zzc(this.zzs.zzau(), "google_app_id", this.zzs.zzz());
                        this.zzj = true == TextUtils.isEmpty(zzc) ? zzc : str4;
                        zzoq.zzc();
                        if (!this.zzs.zzf().zzs(null, zzdw.zzad)) {
                        }
                        if (zza == 0) {
                        }
                        this.zzh = null;
                        this.zzs.zzaw();
                        zzp = this.zzs.zzf().zzp("analytics.safelisted_events");
                        if (zzp != null) {
                        }
                        this.zzh = zzp;
                        if (packageManager != null) {
                        }
                    }
                } else {
                    str2 = str3;
                }
            } catch (PackageManager.NameNotFoundException e3) {
                str = str3;
            }
        }
        this.zza = packageName;
        this.zzd = str5;
        this.zzb = str3;
        this.zzc = i;
        this.zze = str2;
        this.zzf = 0;
        objArr = (!TextUtils.isEmpty(this.zzs.zzw()) || !"am".equals(this.zzs.zzx())) ? null : 1;
        zza = this.zzs.zza();
        switch (zza) {
            case 0:
                this.zzs.zzay().zzj().zza("App measurement collection enabled");
                break;
            case 1:
                this.zzs.zzay().zzi().zza("App measurement deactivated via the manifest");
                break;
            case 2:
                this.zzs.zzay().zzj().zza("App measurement deactivated via the init parameters");
                break;
            case 3:
                this.zzs.zzay().zzi().zza("App measurement disabled by setAnalyticsCollectionEnabled(false)");
                break;
            case 4:
                this.zzs.zzay().zzi().zza("App measurement disabled via the manifest");
                break;
            case 5:
                this.zzs.zzay().zzj().zza("App measurement disabled via the init parameters");
                break;
            case 6:
                this.zzs.zzay().zzl().zza("App measurement deactivated via resources. This method is being deprecated. Please refer to https://firebase.google.com/support/guides/disable-analytics");
                break;
            case 7:
                this.zzs.zzay().zzi().zza("App measurement disabled via the global data collection setting");
                break;
            default:
                this.zzs.zzay().zzi().zza("App measurement disabled due to denied storage consent");
                break;
        }
        this.zzj = str4;
        this.zzk = str4;
        this.zzl = str4;
        this.zzs.zzaw();
        if (objArr != null) {
            this.zzk = this.zzs.zzw();
        }
        try {
            String zzc2 = zzib.zzc(this.zzs.zzau(), "google_app_id", this.zzs.zzz());
            this.zzj = true == TextUtils.isEmpty(zzc2) ? zzc2 : str4;
            zzoq.zzc();
            if (!this.zzs.zzf().zzs(null, zzdw.zzad)) {
                Context zzau = this.zzs.zzau();
                String zzz = this.zzs.zzz();
                Preconditions.checkNotNull(zzau);
                Resources resources = zzau.getResources();
                if (TextUtils.isEmpty(zzz)) {
                    zzz = zzfk.zza(zzau);
                }
                String zzb = zzfk.zzb("ga_app_id", resources, zzz);
                if (true != TextUtils.isEmpty(zzb)) {
                    str4 = zzb;
                }
                this.zzl = str4;
                if (!TextUtils.isEmpty(zzc2) || !TextUtils.isEmpty(zzb)) {
                    this.zzk = zzfk.zzb("admob_app_id", resources, zzz);
                }
            } else if (!TextUtils.isEmpty(zzc2)) {
                Context zzau2 = this.zzs.zzau();
                String zzz2 = this.zzs.zzz();
                Preconditions.checkNotNull(zzau2);
                Resources resources2 = zzau2.getResources();
                if (TextUtils.isEmpty(zzz2)) {
                    zzz2 = zzfk.zza(zzau2);
                }
                this.zzk = zzfk.zzb("admob_app_id", resources2, zzz2);
            }
            if (zza == 0) {
                this.zzs.zzay().zzj().zzc("App measurement enabled for app package, google app id", this.zza, TextUtils.isEmpty(this.zzj) ? this.zzk : this.zzj);
            }
        } catch (IllegalStateException e4) {
            this.zzs.zzay().zzd().zzc("Fetching Google App Id failed with exception. appId", zzei.zzn(packageName), e4);
        }
        this.zzh = null;
        this.zzs.zzaw();
        zzp = this.zzs.zzf().zzp("analytics.safelisted_events");
        if (zzp != null) {
            if (zzp.size() == 0) {
                this.zzs.zzay().zzl().zza("Safelisted event list is empty. Ignoring");
            } else {
                for (String str6 : zzp) {
                    if (!this.zzs.zzv().zzaa("safelisted event", str6)) {
                    }
                }
            }
            if (packageManager != null) {
                this.zzi = InstantApps.isInstantApp(this.zzs.zzau()) ? 1 : 0;
                return;
            } else {
                this.zzi = 0;
                return;
            }
        }
        this.zzh = zzp;
        if (packageManager != null) {
        }
    }

    @Override // com.google.android.gms.measurement.internal.zzf
    protected final boolean zzf() {
        return true;
    }

    public final int zzh() {
        zza();
        return this.zzi;
    }

    public final int zzi() {
        zza();
        return this.zzc;
    }

    public final zzp zzj(String str) {
        long j;
        String str2;
        long j2;
        String str3;
        Boolean bool;
        String str4;
        long j3;
        zzg();
        String zzl = zzl();
        String zzn = zzn();
        zza();
        String str5 = this.zzb;
        zza();
        long j4 = (long) this.zzc;
        zza();
        Preconditions.checkNotNull(this.zzd);
        String str6 = this.zzd;
        this.zzs.zzf().zzh();
        zza();
        zzg();
        long j5 = this.zzf;
        if (j5 == 0) {
            zzku zzv = this.zzs.zzv();
            Context zzau = this.zzs.zzau();
            String packageName = this.zzs.zzau().getPackageName();
            zzv.zzg();
            Preconditions.checkNotNull(zzau);
            Preconditions.checkNotEmpty(packageName);
            PackageManager packageManager = zzau.getPackageManager();
            MessageDigest zzE = zzku.zzE(MessageDigestAlgorithms.MD5);
            long j6 = -1;
            if (zzE == null) {
                zzv.zzs.zzay().zzd().zza("Could not get MD5 instance");
                j3 = -1;
            } else if (packageManager != null) {
                try {
                    if (!zzv.zzaf(zzau, packageName)) {
                        PackageInfo packageInfo = Wrappers.packageManager(zzau).getPackageInfo(zzv.zzs.zzau().getPackageName(), 64);
                        if (packageInfo.signatures == null || packageInfo.signatures.length <= 0) {
                            zzv.zzs.zzay().zzk().zza("Could not get signatures");
                        } else {
                            j6 = zzku.zzp(zzE.digest(packageInfo.signatures[0].toByteArray()));
                        }
                    } else {
                        j6 = 0;
                    }
                    j3 = j6;
                } catch (PackageManager.NameNotFoundException e) {
                    zzv.zzs.zzay().zzd().zzb("Package name not found", e);
                    j3 = 0;
                }
            } else {
                j3 = 0;
            }
            this.zzf = j3;
            j = j3;
        } else {
            j = j5;
        }
        boolean zzJ = this.zzs.zzJ();
        boolean z = !this.zzs.zzm().zzk;
        zzg();
        if (!this.zzs.zzJ()) {
            str2 = null;
        } else {
            zzqd.zzc();
            if (this.zzs.zzf().zzs(null, zzdw.zzaf)) {
                this.zzs.zzay().zzj().zza("Disabled IID for tests.");
                str2 = null;
            } else {
                try {
                    Class<?> loadClass = this.zzs.zzau().getClassLoader().loadClass("com.google.firebase.analytics.FirebaseAnalytics");
                    if (loadClass == null) {
                        str2 = null;
                    } else {
                        try {
                            Object invoke = loadClass.getDeclaredMethod("getInstance", Context.class).invoke(null, this.zzs.zzau());
                            if (invoke == null) {
                                str2 = null;
                            } else {
                                try {
                                    str2 = (String) loadClass.getDeclaredMethod("getFirebaseInstanceId", new Class[0]).invoke(invoke, new Object[0]);
                                } catch (Exception e2) {
                                    this.zzs.zzay().zzl().zza("Failed to retrieve Firebase Instance Id");
                                    str2 = null;
                                }
                            }
                        } catch (Exception e3) {
                            this.zzs.zzay().zzm().zza("Failed to obtain Firebase Analytics instance");
                            str2 = null;
                        }
                    }
                } catch (ClassNotFoundException e4) {
                    str2 = null;
                }
            }
        }
        zzfs zzfs = this.zzs;
        long zza = zzfs.zzm().zzc.zza();
        if (zza == 0) {
            j2 = zzfs.zzc;
            str3 = zzl;
        } else {
            str3 = zzl;
            j2 = Math.min(zzfs.zzc, zza);
        }
        zza();
        int i = this.zzi;
        boolean zzr = this.zzs.zzf().zzr();
        zzex zzm = this.zzs.zzm();
        zzm.zzg();
        boolean z2 = zzm.zza().getBoolean("deferred_analytics_collection", false);
        zza();
        String str7 = this.zzk;
        Boolean zzk = this.zzs.zzf().zzk("google_analytics_default_allow_ad_personalization_signals");
        if (zzk == null) {
            bool = null;
        } else {
            bool = Boolean.valueOf(!zzk.booleanValue());
        }
        long j7 = this.zzg;
        List<String> list = this.zzh;
        zzoq.zzc();
        if (this.zzs.zzf().zzs(null, zzdw.zzad)) {
            str4 = zzm();
        } else {
            str4 = null;
        }
        return new zzp(str3, zzn, str5, j4, str6, 42097, j, str, zzJ, z, str2, 0, j2, i, zzr, z2, str7, bool, j7, list, str4, this.zzs.zzm().zzc().zzi());
    }

    public final String zzk() {
        zza();
        return this.zzk;
    }

    public final String zzl() {
        zza();
        Preconditions.checkNotNull(this.zza);
        return this.zza;
    }

    public final String zzm() {
        zza();
        Preconditions.checkNotNull(this.zzl);
        return this.zzl;
    }

    public final String zzn() {
        zza();
        Preconditions.checkNotNull(this.zzj);
        return this.zzj;
    }

    public final List<String> zzo() {
        return this.zzh;
    }
}
