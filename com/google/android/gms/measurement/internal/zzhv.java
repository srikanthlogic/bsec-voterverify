package com.google.android.gms.measurement.internal;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import androidx.collection.ArrayMap;
import androidx.core.app.NotificationCompat;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.internal.measurement.zzny;
import com.google.android.gms.internal.measurement.zzoh;
import com.google.android.gms.internal.measurement.zzpx;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlinx.coroutines.DebugKt;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzhv extends zzf {
    protected zzhu zza;
    final zzr zzb;
    private zzgs zzd;
    private boolean zzf;
    private final Set<zzgt> zze = new CopyOnWriteArraySet();
    private final Object zzh = new Object();
    protected boolean zzc = true;
    private final zzkt zzn = new zzhj(this);
    private final AtomicReference<String> zzg = new AtomicReference<>();
    private zzag zzi = new zzag(null, null);
    private int zzj = 100;
    private long zzl = -1;
    private int zzm = 100;
    private final AtomicLong zzk = new AtomicLong(0);

    public zzhv(zzfs zzfs) {
        super(zzfs);
        this.zzb = new zzr(zzfs);
    }

    public final void zzZ(Boolean bool, boolean z) {
        zzg();
        zza();
        this.zzs.zzay().zzc().zzb("Setting app measurement enabled (FE)", bool);
        this.zzs.zzm().zzh(bool);
        if (z) {
            zzex zzm = this.zzs.zzm();
            zzfs zzfs = zzm.zzs;
            zzm.zzg();
            SharedPreferences.Editor edit = zzm.zza().edit();
            if (bool != null) {
                edit.putBoolean("measurement_enabled_from_api", bool.booleanValue());
            } else {
                edit.remove("measurement_enabled_from_api");
            }
            edit.apply();
        }
        if (this.zzs.zzK() || (bool != null && !bool.booleanValue())) {
            zzaa();
        }
    }

    public final void zzaa() {
        long j;
        zzg();
        String zza = this.zzs.zzm().zzh.zza();
        if (zza != null) {
            if ("unset".equals(zza)) {
                zzX("app", "_npa", null, this.zzs.zzav().currentTimeMillis());
            } else {
                if (true != "true".equals(zza)) {
                    j = 0;
                } else {
                    j = 1;
                }
                zzX("app", "_npa", Long.valueOf(j), this.zzs.zzav().currentTimeMillis());
            }
        }
        if (!this.zzs.zzJ() || !this.zzc) {
            this.zzs.zzay().zzc().zza("Updating Scion state (FE)");
            ((zze) this).zzs.zzt().zzI();
            return;
        }
        this.zzs.zzay().zzc().zza("Recording app launch after enabling measurement for the first time (FE)");
        zzy();
        zzoh.zzc();
        if (this.zzs.zzf().zzs(null, zzdw.zzaj)) {
            ((zze) this).zzs.zzu().zza.zza();
        }
        this.zzs.zzaz().zzp(new zzgy(this));
    }

    public static /* bridge */ /* synthetic */ void zzv(zzhv zzhv, zzag zzag, int i, long j, boolean z, boolean z2) {
        zzhv.zzg();
        zzhv.zza();
        if (j > zzhv.zzl || !zzag.zzl(zzhv.zzm, i)) {
            zzex zzm = zzhv.zzs.zzm();
            zzfs zzfs = zzm.zzs;
            zzm.zzg();
            if (zzm.zzl(i)) {
                SharedPreferences.Editor edit = zzm.zza().edit();
                edit.putString("consent_settings", zzag.zzi());
                edit.putInt("consent_source", i);
                edit.apply();
                zzhv.zzl = j;
                zzhv.zzm = i;
                ((zze) zzhv).zzs.zzt().zzF(z);
                if (z2) {
                    ((zze) zzhv).zzs.zzt().zzu(new AtomicReference<>());
                    return;
                }
                return;
            }
            zzhv.zzs.zzay().zzi().zzb("Lower precedence consent source ignored, proposed source", Integer.valueOf(i));
            return;
        }
        zzhv.zzs.zzay().zzi().zzb("Dropped out-of-date consent setting, proposed settings", zzag);
    }

    public final void zzA() {
        if ((this.zzs.zzau().getApplicationContext() instanceof Application) && this.zza != null) {
            ((Application) this.zzs.zzau().getApplicationContext()).unregisterActivityLifecycleCallbacks(this.zza);
        }
    }

    public final /* synthetic */ void zzB(Bundle bundle) {
        if (bundle == null) {
            this.zzs.zzm().zzr.zzb(new Bundle());
            return;
        }
        Bundle zza = this.zzs.zzm().zzr.zza();
        for (String str : bundle.keySet()) {
            Object obj = bundle.get(str);
            if (obj != null && !(obj instanceof String) && !(obj instanceof Long) && !(obj instanceof Double)) {
                if (this.zzs.zzv().zzae(obj)) {
                    this.zzs.zzv().zzM(this.zzn, null, 27, null, null, 0);
                }
                this.zzs.zzay().zzl().zzc("Invalid default event parameter type. Name, value", str, obj);
            } else if (zzku.zzag(str)) {
                this.zzs.zzay().zzl().zzb("Invalid default event parameter name. Name", str);
            } else if (obj == null) {
                zza.remove(str);
            } else {
                zzku zzv = this.zzs.zzv();
                this.zzs.zzf();
                if (zzv.zzZ("param", str, 100, obj)) {
                    this.zzs.zzv().zzN(zza, str, obj);
                }
            }
        }
        this.zzs.zzv();
        int zzc = this.zzs.zzf().zzc();
        if (zza.size() > zzc) {
            int i = 0;
            for (String str2 : new TreeSet(zza.keySet())) {
                i++;
                if (i > zzc) {
                    zza.remove(str2);
                }
            }
            this.zzs.zzv().zzM(this.zzn, null, 26, null, null, 0);
            this.zzs.zzay().zzl().zza("Too many default event parameters set. Discarding beyond event parameter limit");
        }
        this.zzs.zzm().zzr.zzb(zza);
        ((zze) this).zzs.zzt().zzH(zza);
    }

    public final void zzC(String str, String str2, Bundle bundle) {
        zzD(str, str2, bundle, true, true, this.zzs.zzav().currentTimeMillis());
    }

    public final void zzD(String str, String str2, Bundle bundle, boolean z, boolean z2, long j) {
        boolean z3;
        String str3 = str == null ? "app" : str;
        Bundle bundle2 = bundle == null ? new Bundle() : bundle;
        if (zzku.zzak(str2, FirebaseAnalytics.Event.SCREEN_VIEW)) {
            ((zze) this).zzs.zzs().zzx(bundle2, j);
            return;
        }
        boolean z4 = true;
        if (z2 && this.zzd != null) {
            if (zzku.zzag(str2)) {
                z3 = true;
                zzL(str3, str2, j, bundle2, z2, z3, z, null);
            }
            z4 = false;
        }
        z3 = z4;
        zzL(str3, str2, j, bundle2, z2, z3, z, null);
    }

    public final void zzE(String str, String str2, Bundle bundle, String str3) {
        zzfs.zzO();
        zzL(DebugKt.DEBUG_PROPERTY_VALUE_AUTO, str2, this.zzs.zzav().currentTimeMillis(), bundle, false, true, true, str3);
    }

    public final void zzF(String str, String str2, Bundle bundle) {
        zzg();
        zzG(str, str2, this.zzs.zzav().currentTimeMillis(), bundle);
    }

    public final void zzG(String str, String str2, long j, Bundle bundle) {
        boolean z;
        zzg();
        if (this.zzd != null) {
            z = zzku.zzag(str2);
        } else {
            z = true;
        }
        zzH(str, str2, j, bundle, true, z, true, null);
    }

    public final void zzH(String str, String str2, long j, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        boolean z4;
        String str4;
        long j2;
        String str5;
        Bundle bundle2;
        Bundle[] bundleArr;
        boolean z5;
        Class<?> cls;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(bundle);
        zzg();
        zza();
        if (this.zzs.zzJ()) {
            List<String> zzo = ((zze) this).zzs.zzh().zzo();
            if (zzo == null || zzo.contains(str2)) {
                int i = 0;
                if (!this.zzf) {
                    this.zzf = true;
                    try {
                        if (!this.zzs.zzN()) {
                            cls = Class.forName("com.google.android.gms.tagmanager.TagManagerService", true, this.zzs.zzau().getClassLoader());
                        } else {
                            cls = Class.forName("com.google.android.gms.tagmanager.TagManagerService");
                        }
                        try {
                            cls.getDeclaredMethod("initialize", Context.class).invoke(null, this.zzs.zzau());
                        } catch (Exception e) {
                            this.zzs.zzay().zzk().zzb("Failed to invoke Tag Manager's initialize() method", e);
                        }
                    } catch (ClassNotFoundException e2) {
                        this.zzs.zzay().zzi().zza("Tag Manager is not found and thus will not be used");
                    }
                }
                if ("_cmp".equals(str2) && bundle.containsKey("gclid")) {
                    this.zzs.zzaw();
                    zzX(DebugKt.DEBUG_PROPERTY_VALUE_AUTO, "_lgclid", bundle.getString("gclid"), this.zzs.zzav().currentTimeMillis());
                }
                this.zzs.zzaw();
                if (z && zzku.zzal(str2)) {
                    this.zzs.zzv().zzK(bundle, this.zzs.zzm().zzr.zza());
                }
                if (!z3) {
                    this.zzs.zzaw();
                    if (!"_iap".equals(str2)) {
                        zzku zzv = this.zzs.zzv();
                        int i2 = 2;
                        if (zzv.zzab(NotificationCompat.CATEGORY_EVENT, str2)) {
                            if (!zzv.zzY(NotificationCompat.CATEGORY_EVENT, zzgp.zza, zzgp.zzb, str2)) {
                                i2 = 13;
                            } else {
                                zzv.zzs.zzf();
                                if (zzv.zzX(NotificationCompat.CATEGORY_EVENT, 40, str2)) {
                                    i2 = 0;
                                }
                            }
                        }
                        if (i2 != 0) {
                            this.zzs.zzay().zze().zzb("Invalid public event name. Event will not be logged (FE)", this.zzs.zzj().zzc(str2));
                            zzku zzv2 = this.zzs.zzv();
                            this.zzs.zzf();
                            String zzC = zzv2.zzC(str2, 40, true);
                            if (str2 != null) {
                                i = str2.length();
                            }
                            this.zzs.zzv().zzM(this.zzn, null, i2, "_ev", zzC, i);
                            return;
                        }
                    }
                }
                zzpx.zzc();
                if (this.zzs.zzf().zzs(null, zzdw.zzaA)) {
                    this.zzs.zzaw();
                    zzic zzj = ((zze) this).zzs.zzs().zzj(false);
                    if (zzj != null && !bundle.containsKey("_sc")) {
                        zzj.zzd = true;
                    }
                    zzku.zzJ(zzj, bundle, z && !z3);
                } else {
                    this.zzs.zzaw();
                    zzic zzj2 = ((zze) this).zzs.zzs().zzj(false);
                    if (zzj2 != null && !bundle.containsKey("_sc")) {
                        zzj2.zzd = true;
                    }
                    if (!z || z3) {
                        z5 = false;
                    } else {
                        z5 = true;
                    }
                    zzku.zzJ(zzj2, bundle, z5);
                }
                boolean equals = "am".equals(str);
                boolean zzag = zzku.zzag(str2);
                if (!z || this.zzd == null || zzag) {
                    z4 = equals;
                } else if (equals) {
                    z4 = true;
                } else {
                    this.zzs.zzay().zzc().zzc("Passing event to registered event handler (FE)", this.zzs.zzj().zzc(str2), this.zzs.zzj().zzb(bundle));
                    Preconditions.checkNotNull(this.zzd);
                    this.zzd.interceptEvent(str, str2, bundle, j);
                    return;
                }
                if (this.zzs.zzM()) {
                    int zzh = this.zzs.zzv().zzh(str2);
                    if (zzh != 0) {
                        this.zzs.zzay().zze().zzb("Invalid event name. Event will not be logged (FE)", this.zzs.zzj().zzc(str2));
                        zzku zzv3 = this.zzs.zzv();
                        this.zzs.zzf();
                        String zzC2 = zzv3.zzC(str2, 40, true);
                        if (str2 != null) {
                            i = str2.length();
                        }
                        this.zzs.zzv().zzM(this.zzn, str3, zzh, "_ev", zzC2, i);
                        return;
                    }
                    Bundle zzy = this.zzs.zzv().zzy(str3, str2, bundle, CollectionUtils.listOf((Object[]) new String[]{"_o", "_sn", "_sc", "_si"}), z3);
                    Preconditions.checkNotNull(zzy);
                    this.zzs.zzaw();
                    if (((zze) this).zzs.zzs().zzj(false) != null && "_ae".equals(str2)) {
                        zzjw zzjw = ((zze) this).zzs.zzu().zzb;
                        long elapsedRealtime = zzjw.zzc.zzs.zzav().elapsedRealtime();
                        long j3 = elapsedRealtime - zzjw.zzb;
                        zzjw.zzb = elapsedRealtime;
                        if (j3 > 0) {
                            this.zzs.zzv().zzH(zzy, j3);
                        }
                    }
                    zzny.zzc();
                    if (this.zzs.zzf().zzs(null, zzdw.zzai)) {
                        if (!DebugKt.DEBUG_PROPERTY_VALUE_AUTO.equals(str) && "_ssr".equals(str2)) {
                            zzku zzv4 = this.zzs.zzv();
                            String string = zzy.getString("_ffr");
                            if (Strings.isEmptyOrWhitespace(string)) {
                                string = null;
                            } else if (string != null) {
                                string = string.trim();
                            }
                            if (!zzku.zzak(string, zzv4.zzs.zzm().zzo.zza())) {
                                zzv4.zzs.zzm().zzo.zzb(string);
                            } else {
                                zzv4.zzs.zzay().zzc().zza("Not logging duplicate session_start_with_rollout event");
                                return;
                            }
                        } else if ("_ae".equals(str2)) {
                            String zza = this.zzs.zzv().zzs.zzm().zzo.zza();
                            if (!TextUtils.isEmpty(zza)) {
                                zzy.putString("_ffr", zza);
                            }
                        }
                    }
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(zzy);
                    if (this.zzs.zzm().zzj.zza() <= 0) {
                        str4 = "_ae";
                        j2 = 0;
                    } else if (!this.zzs.zzm().zzk(j)) {
                        str4 = "_ae";
                        j2 = 0;
                    } else if (this.zzs.zzm().zzl.zzb()) {
                        this.zzs.zzay().zzj().zza("Current session is expired, remove the session number, ID, and engagement time");
                        str4 = "_ae";
                        j2 = 0;
                        zzX(DebugKt.DEBUG_PROPERTY_VALUE_AUTO, "_sid", null, this.zzs.zzav().currentTimeMillis());
                        zzX(DebugKt.DEBUG_PROPERTY_VALUE_AUTO, "_sno", null, this.zzs.zzav().currentTimeMillis());
                        zzX(DebugKt.DEBUG_PROPERTY_VALUE_AUTO, "_se", null, this.zzs.zzav().currentTimeMillis());
                    } else {
                        str4 = "_ae";
                        j2 = 0;
                    }
                    if (zzy.getLong(FirebaseAnalytics.Param.EXTEND_SESSION, j2) == 1) {
                        this.zzs.zzay().zzj().zza("EXTEND_SESSION param attached: initiate a new session or extend the current active session");
                        this.zzs.zzu().zza.zzb(j, true);
                    }
                    ArrayList arrayList2 = new ArrayList(zzy.keySet());
                    Collections.sort(arrayList2);
                    int size = arrayList2.size();
                    for (int i3 = 0; i3 < size; i3++) {
                        String str6 = (String) arrayList2.get(i3);
                        if (str6 != null) {
                            this.zzs.zzv();
                            Object obj = zzy.get(str6);
                            if (obj instanceof Bundle) {
                                bundleArr = new Bundle[]{(Bundle) obj};
                            } else if (obj instanceof Parcelable[]) {
                                Parcelable[] parcelableArr = (Parcelable[]) obj;
                                bundleArr = (Bundle[]) Arrays.copyOf(parcelableArr, parcelableArr.length, Bundle[].class);
                            } else if (obj instanceof ArrayList) {
                                ArrayList arrayList3 = (ArrayList) obj;
                                bundleArr = (Bundle[]) arrayList3.toArray(new Bundle[arrayList3.size()]);
                            } else {
                                bundleArr = null;
                            }
                            if (bundleArr != null) {
                                zzy.putParcelableArray(str6, bundleArr);
                            }
                        }
                    }
                    for (int i4 = 0; i4 < arrayList.size(); i4++) {
                        Bundle bundle3 = (Bundle) arrayList.get(i4);
                        if (i4 != 0) {
                            str5 = "_ep";
                        } else {
                            str5 = str2;
                        }
                        bundle3.putString("_o", str);
                        if (z2) {
                            bundle2 = this.zzs.zzv().zzt(bundle3);
                        } else {
                            bundle2 = bundle3;
                        }
                        ((zze) this).zzs.zzt().zzA(new zzat(str5, new zzar(bundle2), str, j), str3);
                        if (!z4) {
                            for (zzgt zzgt : this.zze) {
                                zzgt.onEvent(str, str2, new Bundle(bundle2), j);
                            }
                        }
                    }
                    this.zzs.zzaw();
                    if (((zze) this).zzs.zzs().zzj(false) != null && str4.equals(str2)) {
                        ((zze) this).zzs.zzu().zzb.zzd(true, true, this.zzs.zzav().elapsedRealtime());
                        return;
                    }
                    return;
                }
                return;
            }
            this.zzs.zzay().zzc().zzc("Dropping non-safelisted event. event name, origin", str2, str);
            return;
        }
        this.zzs.zzay().zzc().zza("Event not sent since app measurement is disabled");
    }

    public final void zzI(zzgt zzgt) {
        zza();
        Preconditions.checkNotNull(zzgt);
        if (!this.zze.add(zzgt)) {
            this.zzs.zzay().zzk().zza("OnEventListener already registered");
        }
    }

    public final void zzJ(long j) {
        this.zzg.set(null);
        this.zzs.zzaz().zzp(new zzhd(this, j));
    }

    public final void zzK(long j, boolean z) {
        zzg();
        zza();
        this.zzs.zzay().zzc().zza("Resetting analytics data (FE)");
        zzjy zzu = ((zze) this).zzs.zzu();
        zzu.zzg();
        zzjx zzjx = zzu.zza;
        zzu.zzb.zza();
        boolean zzJ = this.zzs.zzJ();
        zzex zzm = this.zzs.zzm();
        zzm.zzc.zzb(j);
        if (!TextUtils.isEmpty(zzm.zzs.zzm().zzo.zza())) {
            zzm.zzo.zzb(null);
        }
        zzoh.zzc();
        if (zzm.zzs.zzf().zzs(null, zzdw.zzaj)) {
            zzm.zzj.zzb(0);
        }
        if (!zzm.zzs.zzf().zzv()) {
            zzm.zzi(!zzJ);
        }
        zzm.zzp.zzb(null);
        zzm.zzq.zzb(0);
        zzm.zzr.zzb(null);
        if (z) {
            ((zze) this).zzs.zzt().zzC();
        }
        zzoh.zzc();
        if (this.zzs.zzf().zzs(null, zzdw.zzaj)) {
            ((zze) this).zzs.zzu().zza.zza();
        }
        this.zzc = !zzJ;
    }

    protected final void zzL(String str, String str2, long j, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        Bundle bundle2 = new Bundle(bundle);
        for (String str4 : bundle2.keySet()) {
            Object obj = bundle2.get(str4);
            if (obj instanceof Bundle) {
                bundle2.putBundle(str4, new Bundle((Bundle) obj));
            } else {
                int i = 0;
                if (obj instanceof Parcelable[]) {
                    Parcelable[] parcelableArr = (Parcelable[]) obj;
                    while (i < parcelableArr.length) {
                        Parcelable parcelable = parcelableArr[i];
                        if (parcelable instanceof Bundle) {
                            parcelableArr[i] = new Bundle((Bundle) parcelable);
                        }
                        i++;
                    }
                } else if (obj instanceof List) {
                    List list = (List) obj;
                    while (i < list.size()) {
                        Object obj2 = list.get(i);
                        if (obj2 instanceof Bundle) {
                            list.set(i, new Bundle((Bundle) obj2));
                        }
                        i++;
                    }
                }
            }
        }
        this.zzs.zzaz().zzp(new zzha(this, str, str2, j, bundle2, z, z2, z3, str3));
    }

    final void zzM(String str, String str2, long j, Object obj) {
        this.zzs.zzaz().zzp(new zzhb(this, str, str2, obj, j));
    }

    public final void zzN(String str) {
        this.zzg.set(str);
    }

    public final void zzO(Bundle bundle) {
        zzP(bundle, this.zzs.zzav().currentTimeMillis());
    }

    public final void zzP(Bundle bundle, long j) {
        Preconditions.checkNotNull(bundle);
        Bundle bundle2 = new Bundle(bundle);
        if (!TextUtils.isEmpty(bundle2.getString("app_id"))) {
            this.zzs.zzay().zzk().zza("Package name should be null when calling setConditionalUserProperty");
        }
        bundle2.remove("app_id");
        Preconditions.checkNotNull(bundle2);
        zzgo.zza(bundle2, "app_id", String.class, null);
        zzgo.zza(bundle2, "origin", String.class, null);
        zzgo.zza(bundle2, AppMeasurementSdk.ConditionalUserProperty.NAME, String.class, null);
        zzgo.zza(bundle2, "value", Object.class, null);
        zzgo.zza(bundle2, AppMeasurementSdk.ConditionalUserProperty.TRIGGER_EVENT_NAME, String.class, null);
        zzgo.zza(bundle2, AppMeasurementSdk.ConditionalUserProperty.TRIGGER_TIMEOUT, Long.class, 0L);
        zzgo.zza(bundle2, AppMeasurementSdk.ConditionalUserProperty.TIMED_OUT_EVENT_NAME, String.class, null);
        zzgo.zza(bundle2, AppMeasurementSdk.ConditionalUserProperty.TIMED_OUT_EVENT_PARAMS, Bundle.class, null);
        zzgo.zza(bundle2, AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_EVENT_NAME, String.class, null);
        zzgo.zza(bundle2, AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_EVENT_PARAMS, Bundle.class, null);
        zzgo.zza(bundle2, AppMeasurementSdk.ConditionalUserProperty.TIME_TO_LIVE, Long.class, 0L);
        zzgo.zza(bundle2, AppMeasurementSdk.ConditionalUserProperty.EXPIRED_EVENT_NAME, String.class, null);
        zzgo.zza(bundle2, AppMeasurementSdk.ConditionalUserProperty.EXPIRED_EVENT_PARAMS, Bundle.class, null);
        Preconditions.checkNotEmpty(bundle2.getString(AppMeasurementSdk.ConditionalUserProperty.NAME));
        Preconditions.checkNotEmpty(bundle2.getString("origin"));
        Preconditions.checkNotNull(bundle2.get("value"));
        bundle2.putLong(AppMeasurementSdk.ConditionalUserProperty.CREATION_TIMESTAMP, j);
        String string = bundle2.getString(AppMeasurementSdk.ConditionalUserProperty.NAME);
        Object obj = bundle2.get("value");
        if (this.zzs.zzv().zzl(string) != 0) {
            this.zzs.zzay().zzd().zzb("Invalid conditional user property name", this.zzs.zzj().zze(string));
        } else if (this.zzs.zzv().zzd(string, obj) == 0) {
            Object zzB = this.zzs.zzv().zzB(string, obj);
            if (zzB == null) {
                this.zzs.zzay().zzd().zzc("Unable to normalize conditional user property value", this.zzs.zzj().zze(string), obj);
                return;
            }
            zzgo.zzb(bundle2, zzB);
            long j2 = bundle2.getLong(AppMeasurementSdk.ConditionalUserProperty.TRIGGER_TIMEOUT);
            if (!TextUtils.isEmpty(bundle2.getString(AppMeasurementSdk.ConditionalUserProperty.TRIGGER_EVENT_NAME))) {
                this.zzs.zzf();
                if (j2 > 15552000000L || j2 < 1) {
                    this.zzs.zzay().zzd().zzc("Invalid conditional user property timeout", this.zzs.zzj().zze(string), Long.valueOf(j2));
                    return;
                }
            }
            long j3 = bundle2.getLong(AppMeasurementSdk.ConditionalUserProperty.TIME_TO_LIVE);
            this.zzs.zzf();
            if (j3 > 15552000000L || j3 < 1) {
                this.zzs.zzay().zzd().zzc("Invalid conditional user property time to live", this.zzs.zzj().zze(string), Long.valueOf(j3));
            } else {
                this.zzs.zzaz().zzp(new zzhe(this, bundle2));
            }
        } else {
            this.zzs.zzay().zzd().zzc("Invalid conditional user property value", this.zzs.zzj().zze(string), obj);
        }
    }

    public final void zzQ(Bundle bundle, int i, long j) {
        zza();
        String zzh = zzag.zzh(bundle);
        if (zzh != null) {
            this.zzs.zzay().zzl().zzb("Ignoring invalid consent setting", zzh);
            this.zzs.zzay().zzl().zza("Valid consent values are 'granted', 'denied'");
        }
        zzR(zzag.zza(bundle), i, j);
    }

    public final void zzR(zzag zzag, int i, long j) {
        boolean z;
        boolean z2;
        boolean z3;
        zzag zzag2 = zzag;
        zza();
        if (i != -10 && zzag.zze() == null && zzag.zzf() == null) {
            this.zzs.zzay().zzl().zza("Discarding empty consent settings");
            return;
        }
        synchronized (this.zzh) {
            z = true;
            boolean z4 = false;
            if (zzag.zzl(i, this.zzj)) {
                z3 = zzag.zzm(this.zzi);
                if (zzag.zzk() && !this.zzi.zzk()) {
                    z4 = true;
                }
                zzag2 = zzag.zzd(this.zzi);
                this.zzi = zzag2;
                this.zzj = i;
                z2 = z4;
            } else {
                z3 = false;
                z = false;
                z2 = false;
            }
        }
        if (!z) {
            this.zzs.zzay().zzi().zzb("Ignoring lower-priority consent settings, proposed settings", zzag2);
            return;
        }
        long andIncrement = this.zzk.getAndIncrement();
        if (z3) {
            this.zzg.set(null);
            this.zzs.zzaz().zzq(new zzhp(this, zzag2, j, i, andIncrement, z2));
        } else if (i == 30 || i == -10) {
            this.zzs.zzaz().zzq(new zzhq(this, zzag2, i, andIncrement, z2));
        } else {
            this.zzs.zzaz().zzp(new zzhr(this, zzag2, i, andIncrement, z2));
        }
    }

    public final void zzS(zzgs zzgs) {
        zzgs zzgs2;
        boolean z;
        zzg();
        zza();
        if (!(zzgs == null || zzgs == (zzgs2 = this.zzd))) {
            if (zzgs2 == null) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkState(z, "EventInterceptor already set.");
        }
        this.zzd = zzgs;
    }

    public final void zzT(Boolean bool) {
        zza();
        this.zzs.zzaz().zzp(new zzho(this, bool));
    }

    public final void zzU(zzag zzag) {
        boolean z;
        Boolean bool;
        zzg();
        if (!zzag.zzk() || !zzag.zzj()) {
            z = ((zze) this).zzs.zzt().zzM();
        } else {
            z = true;
        }
        if (z != this.zzs.zzK()) {
            this.zzs.zzG(z);
            zzex zzm = this.zzs.zzm();
            zzfs zzfs = zzm.zzs;
            zzm.zzg();
            if (zzm.zza().contains("measurement_enabled_from_api")) {
                bool = Boolean.valueOf(zzm.zza().getBoolean("measurement_enabled_from_api", true));
            } else {
                bool = null;
            }
            if (!z || bool == null || bool.booleanValue()) {
                zzZ(Boolean.valueOf(z), false);
            }
        }
    }

    public final void zzV(String str, String str2, Object obj, boolean z) {
        zzW(DebugKt.DEBUG_PROPERTY_VALUE_AUTO, "_ldl", obj, true, this.zzs.zzav().currentTimeMillis());
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0074  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0084  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final void zzX(String str, String str2, Object obj, long j) {
        Long l;
        String str3;
        long j2;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzg();
        zza();
        if (FirebaseAnalytics.UserProperty.ALLOW_AD_PERSONALIZATION_SIGNALS.equals(str2)) {
            if (obj instanceof String) {
                String str4 = (String) obj;
                if (!TextUtils.isEmpty(str4)) {
                    String lowerCase = str4.toLowerCase(Locale.ENGLISH);
                    String str5 = "false";
                    if (true != str5.equals(lowerCase)) {
                        j2 = 0;
                    } else {
                        j2 = 1;
                    }
                    Long valueOf = Long.valueOf(j2);
                    zzew zzew = this.zzs.zzm().zzh;
                    if (valueOf.longValue() == 1) {
                        str5 = "true";
                    }
                    zzew.zzb(str5);
                    l = valueOf;
                    str3 = "_npa";
                    if (!this.zzs.zzJ()) {
                        this.zzs.zzay().zzj().zza("User property not set since app measurement is disabled");
                        return;
                    } else if (this.zzs.zzM()) {
                        ((zze) this).zzs.zzt().zzK(new zzkq(str3, j, l, str));
                        return;
                    } else {
                        return;
                    }
                }
            }
            if (obj == null) {
                this.zzs.zzm().zzh.zzb("unset");
                l = obj;
                str3 = "_npa";
                if (!this.zzs.zzJ()) {
                }
            }
        }
        str3 = str2;
        l = obj;
        if (!this.zzs.zzJ()) {
        }
    }

    public final void zzY(zzgt zzgt) {
        zza();
        Preconditions.checkNotNull(zzgt);
        if (!this.zze.remove(zzgt)) {
            this.zzs.zzay().zzk().zza("OnEventListener had not been registered");
        }
    }

    @Override // com.google.android.gms.measurement.internal.zzf
    protected final boolean zzf() {
        return false;
    }

    public final int zzh(String str) {
        Preconditions.checkNotEmpty(str);
        this.zzs.zzf();
        return 25;
    }

    public final Boolean zzi() {
        AtomicReference atomicReference = new AtomicReference();
        return (Boolean) this.zzs.zzaz().zzd(atomicReference, 15000, "boolean test flag value", new zzhg(this, atomicReference));
    }

    public final Double zzj() {
        AtomicReference atomicReference = new AtomicReference();
        return (Double) this.zzs.zzaz().zzd(atomicReference, 15000, "double test flag value", new zzhn(this, atomicReference));
    }

    public final Integer zzl() {
        AtomicReference atomicReference = new AtomicReference();
        return (Integer) this.zzs.zzaz().zzd(atomicReference, 15000, "int test flag value", new zzhm(this, atomicReference));
    }

    public final Long zzm() {
        AtomicReference atomicReference = new AtomicReference();
        return (Long) this.zzs.zzaz().zzd(atomicReference, 15000, "long test flag value", new zzhl(this, atomicReference));
    }

    public final String zzo() {
        return this.zzg.get();
    }

    public final String zzp() {
        zzic zzi = this.zzs.zzs().zzi();
        if (zzi != null) {
            return zzi.zzb;
        }
        return null;
    }

    public final String zzq() {
        zzic zzi = this.zzs.zzs().zzi();
        if (zzi != null) {
            return zzi.zza;
        }
        return null;
    }

    public final String zzr() {
        AtomicReference atomicReference = new AtomicReference();
        return (String) this.zzs.zzaz().zzd(atomicReference, 15000, "String test flag value", new zzhk(this, atomicReference));
    }

    public final ArrayList<Bundle> zzs(String str, String str2) {
        if (this.zzs.zzaz().zzs()) {
            this.zzs.zzay().zzd().zza("Cannot get conditional user properties from analytics worker thread");
            return new ArrayList<>(0);
        }
        this.zzs.zzaw();
        if (zzaa.zza()) {
            this.zzs.zzay().zzd().zza("Cannot get conditional user properties from main thread");
            return new ArrayList<>(0);
        }
        AtomicReference atomicReference = new AtomicReference();
        this.zzs.zzaz().zzd(atomicReference, 5000, "get conditional user properties", new zzhh(this, atomicReference, null, str, str2));
        List list = (List) atomicReference.get();
        if (list != null) {
            return zzku.zzG(list);
        }
        this.zzs.zzay().zzd().zzb("Timed out waiting for get conditional user properties", null);
        return new ArrayList<>();
    }

    public final List<zzkq> zzt(boolean z) {
        zza();
        this.zzs.zzay().zzj().zza("Getting user properties (FE)");
        if (!this.zzs.zzaz().zzs()) {
            this.zzs.zzaw();
            if (zzaa.zza()) {
                this.zzs.zzay().zzd().zza("Cannot get all user properties from main thread");
                return Collections.emptyList();
            }
            AtomicReference atomicReference = new AtomicReference();
            this.zzs.zzaz().zzd(atomicReference, 5000, "get user properties", new zzhc(this, atomicReference, z));
            List<zzkq> list = (List) atomicReference.get();
            if (list != null) {
                return list;
            }
            this.zzs.zzay().zzd().zzb("Timed out waiting for get user properties, includeInternal", Boolean.valueOf(z));
            return Collections.emptyList();
        }
        this.zzs.zzay().zzd().zza("Cannot get all user properties from analytics worker thread");
        return Collections.emptyList();
    }

    public final Map<String, Object> zzu(String str, String str2, boolean z) {
        if (this.zzs.zzaz().zzs()) {
            this.zzs.zzay().zzd().zza("Cannot get user properties from analytics worker thread");
            return Collections.emptyMap();
        }
        this.zzs.zzaw();
        if (zzaa.zza()) {
            this.zzs.zzay().zzd().zza("Cannot get user properties from main thread");
            return Collections.emptyMap();
        }
        AtomicReference atomicReference = new AtomicReference();
        this.zzs.zzaz().zzd(atomicReference, 5000, "get user properties", new zzhi(this, atomicReference, null, str, str2, z));
        List<zzkq> list = (List) atomicReference.get();
        if (list == null) {
            this.zzs.zzay().zzd().zzb("Timed out waiting for handle get user properties, includeInternal", Boolean.valueOf(z));
            return Collections.emptyMap();
        }
        ArrayMap arrayMap = new ArrayMap(list.size());
        for (zzkq zzkq : list) {
            Object zza = zzkq.zza();
            if (zza != null) {
                arrayMap.put(zzkq.zzb, zza);
            }
        }
        return arrayMap;
    }

    public final void zzy() {
        zzg();
        zza();
        if (this.zzs.zzM()) {
            if (this.zzs.zzf().zzs(null, zzdw.zzZ)) {
                zzaf zzf = this.zzs.zzf();
                zzf.zzs.zzaw();
                Boolean zzk = zzf.zzk("google_analytics_deferred_deep_link_enabled");
                if (zzk != null && zzk.booleanValue()) {
                    this.zzs.zzay().zzc().zza("Deferred Deep Link feature enabled.");
                    this.zzs.zzaz().zzp(new Runnable() { // from class: com.google.android.gms.measurement.internal.zzgv
                        @Override // java.lang.Runnable
                        public final void run() {
                            zzhv zzhv = zzhv.this;
                            zzhv.zzg();
                            if (!zzhv.zzs.zzm().zzm.zzb()) {
                                long zza = zzhv.zzs.zzm().zzn.zza();
                                zzhv.zzs.zzm().zzn.zzb(1 + zza);
                                zzhv.zzs.zzf();
                                if (zza >= 5) {
                                    zzhv.zzs.zzay().zzk().zza("Permanently failed to retrieve Deferred Deep Link. Reached maximum retries.");
                                    zzhv.zzs.zzm().zzm.zza(true);
                                    return;
                                }
                                zzhv.zzs.zzE();
                                return;
                            }
                            zzhv.zzs.zzay().zzc().zza("Deferred Deep Link already retrieved. Not fetching again.");
                        }
                    });
                }
            }
            ((zze) this).zzs.zzt().zzq();
            this.zzc = false;
            zzex zzm = this.zzs.zzm();
            zzm.zzg();
            String string = zzm.zza().getString("previous_os_version", null);
            zzm.zzs.zzg().zzu();
            String str = Build.VERSION.RELEASE;
            if (!TextUtils.isEmpty(str) && !str.equals(string)) {
                SharedPreferences.Editor edit = zzm.zza().edit();
                edit.putString("previous_os_version", str);
                edit.apply();
            }
            if (!TextUtils.isEmpty(string)) {
                this.zzs.zzg().zzu();
                if (!string.equals(Build.VERSION.RELEASE)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("_po", string);
                    zzF(DebugKt.DEBUG_PROPERTY_VALUE_AUTO, "_ou", bundle);
                }
            }
        }
    }

    public final void zzz(String str, String str2, Bundle bundle) {
        long currentTimeMillis = this.zzs.zzav().currentTimeMillis();
        Preconditions.checkNotEmpty(str);
        Bundle bundle2 = new Bundle();
        bundle2.putString(AppMeasurementSdk.ConditionalUserProperty.NAME, str);
        bundle2.putLong(AppMeasurementSdk.ConditionalUserProperty.CREATION_TIMESTAMP, currentTimeMillis);
        if (str2 != null) {
            bundle2.putString(AppMeasurementSdk.ConditionalUserProperty.EXPIRED_EVENT_NAME, str2);
            bundle2.putBundle(AppMeasurementSdk.ConditionalUserProperty.EXPIRED_EVENT_PARAMS, bundle);
        }
        this.zzs.zzaz().zzp(new zzhf(this, bundle2));
    }

    public final void zzW(String str, String str2, Object obj, boolean z, long j) {
        int i;
        int i2;
        int i3;
        String str3 = str == null ? "app" : str;
        int i4 = 6;
        if (z) {
            i = this.zzs.zzv().zzl(str2);
        } else {
            zzku zzv = this.zzs.zzv();
            if (zzv.zzab("user property", str2)) {
                if (!zzv.zzY("user property", zzgr.zza, null, str2)) {
                    i4 = 15;
                } else {
                    zzv.zzs.zzf();
                    if (zzv.zzX("user property", 24, str2)) {
                        i = 0;
                    }
                }
            }
            i = i4;
        }
        if (i != 0) {
            zzku zzv2 = this.zzs.zzv();
            this.zzs.zzf();
            String zzC = zzv2.zzC(str2, 24, true);
            if (str2 != null) {
                i3 = str2.length();
            } else {
                i3 = 0;
            }
            this.zzs.zzv().zzM(this.zzn, null, i, "_ev", zzC, i3);
        } else if (obj != null) {
            int zzd = this.zzs.zzv().zzd(str2, obj);
            if (zzd != 0) {
                zzku zzv3 = this.zzs.zzv();
                this.zzs.zzf();
                String zzC2 = zzv3.zzC(str2, 24, true);
                if ((obj instanceof String) || (obj instanceof CharSequence)) {
                    i2 = String.valueOf(obj).length();
                } else {
                    i2 = 0;
                }
                this.zzs.zzv().zzM(this.zzn, null, zzd, "_ev", zzC2, i2);
                return;
            }
            Object zzB = this.zzs.zzv().zzB(str2, obj);
            if (zzB != null) {
                zzM(str3, str2, j, zzB);
            }
        } else {
            zzM(str3, str2, j, null);
        }
    }
}
