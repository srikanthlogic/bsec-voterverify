package com.google.android.gms.measurement.internal;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.DefaultClock;
import com.google.android.gms.common.wrappers.Wrappers;
import com.google.android.gms.internal.measurement.zzcl;
import com.google.android.gms.internal.measurement.zzhu;
import com.google.android.gms.internal.measurement.zzny;
import com.google.android.gms.internal.measurement.zzob;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import kotlinx.coroutines.DebugKt;
import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.dataflow.qual.SideEffectFree;
import org.json.JSONException;
import org.json.JSONObject;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzfs implements zzgn {
    private static volatile zzfs zzd;
    private zzea zzA;
    private Boolean zzC;
    private long zzD;
    private volatile Boolean zzE;
    private int zzG;
    protected Boolean zza;
    protected Boolean zzb;
    final long zzc;
    private final Context zze;
    private final String zzf;
    private final String zzg;
    private final String zzh;
    private final boolean zzi;
    private final zzaa zzj;
    private final zzaf zzk;
    private final zzex zzl;
    private final zzei zzm;
    private final zzfp zzn;
    private final zzjy zzo;
    private final zzku zzp;
    private final zzed zzq;
    private final Clock zzr;
    private final zzij zzs;
    private final zzhv zzt;
    private final zzd zzu;
    private final zzhz zzv;
    private final String zzw;
    private zzec zzx;
    private zzjj zzy;
    private zzan zzz;
    private boolean zzB = false;
    private final AtomicInteger zzH = new AtomicInteger(0);
    private volatile boolean zzF = true;

    zzfs(zzgu zzgu) {
        long j;
        Bundle bundle;
        Preconditions.checkNotNull(zzgu);
        this.zzj = new zzaa(zzgu.zza);
        zzdt.zza = this.zzj;
        this.zze = zzgu.zza;
        this.zzf = zzgu.zzb;
        this.zzg = zzgu.zzc;
        this.zzh = zzgu.zzd;
        this.zzi = zzgu.zzh;
        this.zzE = zzgu.zze;
        this.zzw = zzgu.zzj;
        boolean z = true;
        zzcl zzcl = zzgu.zzg;
        if (!(zzcl == null || (bundle = zzcl.zzg) == null)) {
            Object obj = bundle.get("measurementEnabled");
            if (obj instanceof Boolean) {
                this.zza = (Boolean) obj;
            }
            Object obj2 = zzcl.zzg.get("measurementDeactivated");
            if (obj2 instanceof Boolean) {
                this.zzb = (Boolean) obj2;
            }
        }
        zzhu.zzd(this.zze);
        this.zzr = DefaultClock.getInstance();
        Long l = zzgu.zzi;
        if (l != null) {
            j = l.longValue();
        } else {
            j = this.zzr.currentTimeMillis();
        }
        this.zzc = j;
        this.zzk = new zzaf(this);
        zzex zzex = new zzex(this);
        zzex.zzv();
        this.zzl = zzex;
        zzei zzei = new zzei(this);
        zzei.zzv();
        this.zzm = zzei;
        zzku zzku = new zzku(this);
        zzku.zzv();
        this.zzp = zzku;
        zzed zzed = new zzed(this);
        zzed.zzv();
        this.zzq = zzed;
        this.zzu = new zzd(this);
        zzij zzij = new zzij(this);
        zzij.zzb();
        this.zzs = zzij;
        zzhv zzhv = new zzhv(this);
        zzhv.zzb();
        this.zzt = zzhv;
        zzjy zzjy = new zzjy(this);
        zzjy.zzb();
        this.zzo = zzjy;
        zzhz zzhz = new zzhz(this);
        zzhz.zzv();
        this.zzv = zzhz;
        zzfp zzfp = new zzfp(this);
        zzfp.zzv();
        this.zzn = zzfp;
        zzcl zzcl2 = zzgu.zzg;
        if (!(zzcl2 == null || zzcl2.zzb == 0)) {
            z = false;
        }
        if (this.zze.getApplicationContext() instanceof Application) {
            zzhv zzq = zzq();
            if (zzq.zzs.zze.getApplicationContext() instanceof Application) {
                Application application = (Application) zzq.zzs.zze.getApplicationContext();
                if (zzq.zza == null) {
                    zzq.zza = new zzhu(zzq, null);
                }
                if (z) {
                    application.unregisterActivityLifecycleCallbacks(zzq.zza);
                    application.registerActivityLifecycleCallbacks(zzq.zza);
                    zzq.zzs.zzay().zzj().zza("Registered activity lifecycle callback");
                }
            }
        } else {
            zzay().zzk().zza("Application context is not an Application");
        }
        this.zzn.zzp(new zzfr(this, zzgu));
    }

    public static /* bridge */ /* synthetic */ void zzA(zzfs zzfs, zzgu zzgu) {
        zzfs.zzaz().zzg();
        zzfs.zzk.zzn();
        zzan zzan = new zzan(zzfs);
        zzan.zzv();
        zzfs.zzz = zzan;
        zzea zzea = new zzea(zzfs, zzgu.zzf);
        zzea.zzb();
        zzfs.zzA = zzea;
        zzec zzec = new zzec(zzfs);
        zzec.zzb();
        zzfs.zzx = zzec;
        zzjj zzjj = new zzjj(zzfs);
        zzjj.zzb();
        zzfs.zzy = zzjj;
        zzfs.zzp.zzw();
        zzfs.zzl.zzw();
        zzfs.zzA.zzc();
        zzeg zzi = zzfs.zzay().zzi();
        zzfs.zzk.zzh();
        zzi.zzb("App measurement initialized, version", 42097L);
        zzfs.zzay().zzi().zza("To enable debug logging run: adb shell setprop log.tag.FA VERBOSE");
        String zzl = zzea.zzl();
        if (TextUtils.isEmpty(zzfs.zzf)) {
            if (zzfs.zzv().zzad(zzl)) {
                zzfs.zzay().zzi().zza("Faster debug mode event logging enabled. To disable, run:\n  adb shell setprop debug.firebase.analytics.app .none.");
            } else {
                zzeg zzi2 = zzfs.zzay().zzi();
                String valueOf = String.valueOf(zzl);
                zzi2.zza(valueOf.length() != 0 ? "To enable faster debug mode event logging run:\n  adb shell setprop debug.firebase.analytics.app ".concat(valueOf) : new String("To enable faster debug mode event logging run:\n  adb shell setprop debug.firebase.analytics.app "));
            }
        }
        zzfs.zzay().zzc().zza("Debug-level message logging enabled");
        if (zzfs.zzG != zzfs.zzH.get()) {
            zzfs.zzay().zzd().zzc("Not all components initialized", Integer.valueOf(zzfs.zzG), Integer.valueOf(zzfs.zzH.get()));
        }
        zzfs.zzB = true;
    }

    public static final void zzO() {
        throw new IllegalStateException("Unexpected call on client side");
    }

    private static final void zzP(zzgl zzgl) {
        if (zzgl == null) {
            throw new IllegalStateException("Component not created");
        }
    }

    private static final void zzQ(zzf zzf) {
        if (zzf == null) {
            throw new IllegalStateException("Component not created");
        } else if (!zzf.zze()) {
            String valueOf = String.valueOf(zzf.getClass());
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 27);
            sb.append("Component not initialized: ");
            sb.append(valueOf);
            throw new IllegalStateException(sb.toString());
        }
    }

    private static final void zzR(zzgm zzgm) {
        if (zzgm == null) {
            throw new IllegalStateException("Component not created");
        } else if (!zzgm.zzx()) {
            String valueOf = String.valueOf(zzgm.getClass());
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 27);
            sb.append("Component not initialized: ");
            sb.append(valueOf);
            throw new IllegalStateException(sb.toString());
        }
    }

    public static zzfs zzp(Context context, zzcl zzcl, Long l) {
        Bundle bundle;
        if (zzcl != null && (zzcl.zze == null || zzcl.zzf == null)) {
            zzcl = new zzcl(zzcl.zza, zzcl.zzb, zzcl.zzc, zzcl.zzd, null, null, zzcl.zzg, null);
        }
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(context.getApplicationContext());
        if (zzd == null) {
            synchronized (zzfs.class) {
                if (zzd == null) {
                    zzd = new zzfs(new zzgu(context, zzcl, l));
                }
            }
        } else if (!(zzcl == null || (bundle = zzcl.zzg) == null || !bundle.containsKey("dataCollectionDefaultEnabled"))) {
            Preconditions.checkNotNull(zzd);
            zzd.zzE = Boolean.valueOf(zzcl.zzg.getBoolean("dataCollectionDefaultEnabled"));
        }
        Preconditions.checkNotNull(zzd);
        return zzd;
    }

    public final void zzB() {
        this.zzH.incrementAndGet();
    }

    public final void zzD() {
        this.zzG++;
    }

    public final void zzE() {
        zzaz().zzg();
        zzR(zzr());
        String zzl = zzh().zzl();
        Pair<String, Boolean> zzb = zzm().zzb(zzl);
        if (!this.zzk.zzr() || ((Boolean) zzb.second).booleanValue() || TextUtils.isEmpty((CharSequence) zzb.first)) {
            zzay().zzc().zza("ADID unavailable to retrieve Deferred Deep Link. Skipping");
            return;
        }
        zzhz zzr = zzr();
        zzr.zzu();
        ConnectivityManager connectivityManager = (ConnectivityManager) zzr.zzs.zze.getSystemService("connectivity");
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            try {
                networkInfo = connectivityManager.getActiveNetworkInfo();
            } catch (SecurityException e) {
            }
        }
        if (networkInfo == null || !networkInfo.isConnected()) {
            zzay().zzk().zza("Network is not available for Deferred Deep Link request. Skipping");
            return;
        }
        zzku zzv = zzv();
        zzh().zzs.zzk.zzh();
        URL zzD = zzv.zzD(42097, zzl, (String) zzb.first, zzm().zzn.zza() - 1);
        if (zzD != null) {
            zzhz zzr2 = zzr();
            zzfq zzfq = new zzfq(this);
            zzr2.zzg();
            zzr2.zzu();
            Preconditions.checkNotNull(zzD);
            Preconditions.checkNotNull(zzfq);
            zzr2.zzs.zzaz().zzo(new zzhy(zzr2, zzl, zzD, null, null, zzfq, null));
        }
    }

    public final void zzF(boolean z) {
        this.zzE = Boolean.valueOf(z);
    }

    public final void zzG(boolean z) {
        zzaz().zzg();
        this.zzF = z;
    }

    public final void zzH(zzcl zzcl) {
        zzag zzag;
        zzaz().zzg();
        zzag zzc = zzm().zzc();
        zzex zzm = zzm();
        zzfs zzfs = zzm.zzs;
        zzm.zzg();
        int i = 100;
        int i2 = zzm.zza().getInt("consent_source", 100);
        zzaf zzaf = this.zzk;
        zzfs zzfs2 = zzaf.zzs;
        Boolean zzk = zzaf.zzk("google_analytics_default_allow_ad_storage");
        zzaf zzaf2 = this.zzk;
        zzfs zzfs3 = zzaf2.zzs;
        Boolean zzk2 = zzaf2.zzk("google_analytics_default_allow_analytics_storage");
        if (!(zzk == null && zzk2 == null) && zzm().zzl(-10)) {
            zzag = new zzag(zzk, zzk2);
            i = -10;
        } else if (TextUtils.isEmpty(zzh().zzn()) || !(i2 == 0 || i2 == 30 || i2 == 10 || i2 == 30 || i2 == 30 || i2 == 40)) {
            zzob.zzc();
            if ((!this.zzk.zzs(null, zzdw.zzau) || TextUtils.isEmpty(zzh().zzn())) && zzcl != null && zzcl.zzg != null && zzm().zzl(30)) {
                zzag = zzag.zza(zzcl.zzg);
                if (!zzag.equals(zzag.zza)) {
                    i = 30;
                }
            }
            zzag = null;
        } else {
            zzq().zzR(zzag.zza, -10, this.zzc);
            zzag = null;
        }
        if (zzag != null) {
            zzq().zzR(zzag, i, this.zzc);
        } else {
            zzag = zzc;
        }
        zzq().zzU(zzag);
        if (zzm().zzc.zza() == 0) {
            zzay().zzj().zzb("Persisting first open", Long.valueOf(this.zzc));
            zzm().zzc.zzb(this.zzc);
        }
        zzq().zzb.zzc();
        if (zzM()) {
            if (!TextUtils.isEmpty(zzh().zzn()) || !TextUtils.isEmpty(zzh().zzk())) {
                zzku zzv = zzv();
                String zzn = zzh().zzn();
                zzex zzm2 = zzm();
                zzm2.zzg();
                String string = zzm2.zza().getString("gmp_app_id", null);
                String zzk3 = zzh().zzk();
                zzex zzm3 = zzm();
                zzm3.zzg();
                if (zzv.zzam(zzn, string, zzk3, zzm3.zza().getString("admob_app_id", null))) {
                    zzay().zzi().zza("Rechecking which service to use due to a GMP App Id change");
                    zzex zzm4 = zzm();
                    zzm4.zzg();
                    Boolean zzd2 = zzm4.zzd();
                    SharedPreferences.Editor edit = zzm4.zza().edit();
                    edit.clear();
                    edit.apply();
                    if (zzd2 != null) {
                        zzm4.zzh(zzd2);
                    }
                    zzi().zzj();
                    this.zzy.zzs();
                    this.zzy.zzr();
                    zzm().zzc.zzb(this.zzc);
                    zzm().zze.zzb(null);
                }
                zzex zzm5 = zzm();
                String zzn2 = zzh().zzn();
                zzm5.zzg();
                SharedPreferences.Editor edit2 = zzm5.zza().edit();
                edit2.putString("gmp_app_id", zzn2);
                edit2.apply();
                zzex zzm6 = zzm();
                String zzk4 = zzh().zzk();
                zzm6.zzg();
                SharedPreferences.Editor edit3 = zzm6.zza().edit();
                edit3.putString("admob_app_id", zzk4);
                edit3.apply();
            }
            if (!zzm().zzc().zzk()) {
                zzm().zze.zzb(null);
            }
            zzq().zzN(zzm().zze.zza());
            zzny.zzc();
            if (this.zzk.zzs(null, zzdw.zzai)) {
                try {
                    zzv().zzs.zze.getClassLoader().loadClass("com.google.firebase.remoteconfig.FirebaseRemoteConfig");
                } catch (ClassNotFoundException e) {
                    if (!TextUtils.isEmpty(zzm().zzo.zza())) {
                        zzay().zzk().zza("Remote config removed with active feature rollouts");
                        zzm().zzo.zzb(null);
                    }
                }
            }
            if (!TextUtils.isEmpty(zzh().zzn()) || !TextUtils.isEmpty(zzh().zzk())) {
                boolean zzJ = zzJ();
                if (!zzm().zzj() && !this.zzk.zzv()) {
                    zzm().zzi(!zzJ);
                }
                if (zzJ) {
                    zzq().zzy();
                }
                zzu().zza.zza();
                zzt().zzu(new AtomicReference<>());
                zzt().zzH(zzm().zzr.zza());
            }
        } else if (zzJ()) {
            if (!zzv().zzac("android.permission.INTERNET")) {
                zzay().zzd().zza("App is missing INTERNET permission");
            }
            if (!zzv().zzac("android.permission.ACCESS_NETWORK_STATE")) {
                zzay().zzd().zza("App is missing ACCESS_NETWORK_STATE permission");
            }
            if (!Wrappers.packageManager(this.zze).isCallerInstantApp() && !this.zzk.zzx()) {
                if (!zzku.zzai(this.zze)) {
                    zzay().zzd().zza("AppMeasurementReceiver not registered/enabled");
                }
                if (!zzku.zzaj(this.zze, false)) {
                    zzay().zzd().zza("AppMeasurementService not registered/enabled");
                }
            }
            zzay().zzd().zza("Uploading is not possible. App measurement disabled");
        }
        zzm().zzi.zza(true);
    }

    public final boolean zzI() {
        return this.zzE != null && this.zzE.booleanValue();
    }

    public final boolean zzJ() {
        return zza() == 0;
    }

    public final boolean zzK() {
        zzaz().zzg();
        return this.zzF;
    }

    @Pure
    public final boolean zzL() {
        return TextUtils.isEmpty(this.zzf);
    }

    public final boolean zzM() {
        boolean z;
        if (this.zzB) {
            zzaz().zzg();
            Boolean bool = this.zzC;
            if (bool == null || this.zzD == 0 || (!bool.booleanValue() && Math.abs(this.zzr.elapsedRealtime() - this.zzD) > 1000)) {
                this.zzD = this.zzr.elapsedRealtime();
                boolean z2 = true;
                if (zzv().zzac("android.permission.INTERNET")) {
                    if (zzv().zzac("android.permission.ACCESS_NETWORK_STATE")) {
                        if (Wrappers.packageManager(this.zze).isCallerInstantApp() || this.zzk.zzx()) {
                            z = true;
                        } else if (zzku.zzai(this.zze) && zzku.zzaj(this.zze, false)) {
                            z = true;
                        }
                    }
                    z = false;
                } else {
                    z = false;
                }
                this.zzC = Boolean.valueOf(z);
                if (this.zzC.booleanValue()) {
                    if (!zzv().zzW(zzh().zzn(), zzh().zzk(), zzh().zzm()) && TextUtils.isEmpty(zzh().zzk())) {
                        z2 = false;
                    }
                    this.zzC = Boolean.valueOf(z2);
                }
            }
            return this.zzC.booleanValue();
        }
        throw new IllegalStateException("AppMeasurement is not initialized");
    }

    @Pure
    public final boolean zzN() {
        return this.zzi;
    }

    public final int zza() {
        zzaz().zzg();
        if (this.zzk.zzv()) {
            return 1;
        }
        Boolean bool = this.zzb;
        if (bool != null && bool.booleanValue()) {
            return 2;
        }
        zzaz().zzg();
        if (!this.zzF) {
            return 8;
        }
        Boolean zzd2 = zzm().zzd();
        if (zzd2 == null) {
            zzaf zzaf = this.zzk;
            zzaa zzaa = zzaf.zzs.zzj;
            Boolean zzk = zzaf.zzk("firebase_analytics_collection_enabled");
            if (zzk == null) {
                Boolean bool2 = this.zza;
                if (bool2 != null) {
                    if (bool2.booleanValue()) {
                        return 0;
                    }
                    return 5;
                } else if (!this.zzk.zzs(null, zzdw.zzS) || this.zzE == null || this.zzE.booleanValue()) {
                    return 0;
                } else {
                    return 7;
                }
            } else if (zzk.booleanValue()) {
                return 0;
            } else {
                return 4;
            }
        } else if (zzd2.booleanValue()) {
            return 0;
        } else {
            return 3;
        }
    }

    @Override // com.google.android.gms.measurement.internal.zzgn
    @Pure
    public final Context zzau() {
        return this.zze;
    }

    @Override // com.google.android.gms.measurement.internal.zzgn
    @Pure
    public final Clock zzav() {
        return this.zzr;
    }

    @Override // com.google.android.gms.measurement.internal.zzgn
    @Pure
    public final zzaa zzaw() {
        return this.zzj;
    }

    @Override // com.google.android.gms.measurement.internal.zzgn
    @Pure
    public final zzei zzay() {
        zzR(this.zzm);
        return this.zzm;
    }

    @Override // com.google.android.gms.measurement.internal.zzgn
    @Pure
    public final zzfp zzaz() {
        zzR(this.zzn);
        return this.zzn;
    }

    @Pure
    public final zzd zzd() {
        zzd zzd2 = this.zzu;
        if (zzd2 != null) {
            return zzd2;
        }
        throw new IllegalStateException("Component not created");
    }

    @Pure
    public final zzaf zzf() {
        return this.zzk;
    }

    @Pure
    public final zzan zzg() {
        zzR(this.zzz);
        return this.zzz;
    }

    @Pure
    public final zzea zzh() {
        zzQ(this.zzA);
        return this.zzA;
    }

    @Pure
    public final zzec zzi() {
        zzQ(this.zzx);
        return this.zzx;
    }

    @Pure
    public final zzed zzj() {
        zzP(this.zzq);
        return this.zzq;
    }

    public final zzei zzl() {
        zzei zzei = this.zzm;
        if (zzei == null || !zzei.zzx()) {
            return null;
        }
        return this.zzm;
    }

    @Pure
    public final zzex zzm() {
        zzP(this.zzl);
        return this.zzl;
    }

    @SideEffectFree
    public final zzfp zzo() {
        return this.zzn;
    }

    @Pure
    public final zzhv zzq() {
        zzQ(this.zzt);
        return this.zzt;
    }

    @Pure
    public final zzhz zzr() {
        zzR(this.zzv);
        return this.zzv;
    }

    @Pure
    public final zzij zzs() {
        zzQ(this.zzs);
        return this.zzs;
    }

    @Pure
    public final zzjj zzt() {
        zzQ(this.zzy);
        return this.zzy;
    }

    @Pure
    public final zzjy zzu() {
        zzQ(this.zzo);
        return this.zzo;
    }

    @Pure
    public final zzku zzv() {
        zzP(this.zzp);
        return this.zzp;
    }

    @Pure
    public final String zzw() {
        return this.zzf;
    }

    @Pure
    public final String zzx() {
        return this.zzg;
    }

    @Pure
    public final String zzy() {
        return this.zzh;
    }

    @Pure
    public final String zzz() {
        return this.zzw;
    }

    public final /* synthetic */ void zzC(String str, int i, Throwable th, byte[] bArr, Map map) {
        List<ResolveInfo> queryIntentActivities;
        if (!(i == 200 || i == 204)) {
            if (i == 304) {
                i = 304;
            }
            zzay().zzk().zzc("Network Request for Deferred Deep Link failed. response, exception", Integer.valueOf(i), th);
        }
        if (th == null) {
            zzm().zzm.zza(true);
            if (bArr == null || bArr.length == 0) {
                zzay().zzc().zza("Deferred Deep Link response empty.");
                return;
            }
            try {
                JSONObject jSONObject = new JSONObject(new String(bArr));
                String optString = jSONObject.optString("deeplink", "");
                String optString2 = jSONObject.optString("gclid", "");
                double optDouble = jSONObject.optDouble("timestamp", 0.0d);
                if (TextUtils.isEmpty(optString)) {
                    zzay().zzc().zza("Deferred Deep Link is empty.");
                    return;
                }
                zzku zzv = zzv();
                zzfs zzfs = zzv.zzs;
                if (!TextUtils.isEmpty(optString) && (queryIntentActivities = zzv.zzs.zze.getPackageManager().queryIntentActivities(new Intent("android.intent.action.VIEW", Uri.parse(optString)), 0)) != null && !queryIntentActivities.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("gclid", optString2);
                    bundle.putString("_cis", "ddp");
                    this.zzt.zzF(DebugKt.DEBUG_PROPERTY_VALUE_AUTO, "_cmp", bundle);
                    zzku zzv2 = zzv();
                    if (!TextUtils.isEmpty(optString)) {
                        try {
                            SharedPreferences.Editor edit = zzv2.zzs.zze.getSharedPreferences("google.analytics.deferred.deeplink.prefs", 0).edit();
                            edit.putString("deeplink", optString);
                            edit.putLong("timestamp", Double.doubleToRawLongBits(optDouble));
                            if (edit.commit()) {
                                zzv2.zzs.zze.sendBroadcast(new Intent("android.google.analytics.action.DEEPLINK_ACTION"));
                                return;
                            }
                            return;
                        } catch (RuntimeException e) {
                            zzv2.zzs.zzay().zzd().zzb("Failed to persist Deferred Deep Link. exception", e);
                            return;
                        }
                    } else {
                        return;
                    }
                }
                zzay().zzk().zzc("Deferred Deep Link validation failed. gclid, deep link", optString2, optString);
                return;
            } catch (JSONException e2) {
                zzay().zzd().zzb("Failed to parse the Deferred Deep Link response. exception", e2);
                return;
            }
        }
        zzay().zzk().zzc("Network Request for Deferred Deep Link failed. response, exception", Integer.valueOf(i), th);
    }
}
