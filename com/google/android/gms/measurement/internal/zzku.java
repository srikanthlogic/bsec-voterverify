package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.example.aadhaarfpoffline.tatvik.database.DBHelper;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.common.wrappers.Wrappers;
import com.google.android.gms.internal.measurement.zzcf;
import com.google.android.gms.internal.measurement.zzoq;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import javax.security.auth.x500.X500Principal;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzku extends zzgm {
    public static final /* synthetic */ int zza;
    private static final String[] zzb = {"firebase_", "google_", "ga_"};
    private static final String[] zzc = {"_err"};
    private static final Pattern zzd = Pattern.compile("^([^\\s@]+)@([^\\s@]+\\.[^\\s@]+)$");
    private static final Pattern zze = Pattern.compile("^(gmail|googlemail)\\.");
    private static final Pattern zzf = Pattern.compile("[\\s-()/.]+");
    private static final Pattern zzg = Pattern.compile("^\\+\\d{11,15}$");
    private static final Pattern zzh = Pattern.compile("[0-9`~!@#$%^&*()_\\-+=:;<>,.?|/\\\\\\[\\]]+");
    private static final Pattern zzi = Pattern.compile("\\s+");
    private SecureRandom zzj;
    private int zzl;
    private Integer zzm = null;
    private final AtomicLong zzk = new AtomicLong(0);

    public zzku(zzfs zzfs) {
        super(zzfs);
    }

    public static MessageDigest zzE(String str) {
        MessageDigest instance;
        for (int i = 0; i < 2; i++) {
            try {
                instance = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            } catch (NoSuchAlgorithmException e) {
            }
            if (instance != null) {
                return instance;
            }
        }
        return null;
    }

    public static ArrayList<Bundle> zzG(List<zzab> list) {
        if (list == null) {
            return new ArrayList<>(0);
        }
        ArrayList<Bundle> arrayList = new ArrayList<>(list.size());
        for (zzab zzab : list) {
            Bundle bundle = new Bundle();
            bundle.putString("app_id", zzab.zza);
            bundle.putString("origin", zzab.zzb);
            bundle.putLong(AppMeasurementSdk.ConditionalUserProperty.CREATION_TIMESTAMP, zzab.zzd);
            bundle.putString(AppMeasurementSdk.ConditionalUserProperty.NAME, zzab.zzc.zzb);
            zzgo.zzb(bundle, zzab.zzc.zza());
            bundle.putBoolean(AppMeasurementSdk.ConditionalUserProperty.ACTIVE, zzab.zze);
            String str = zzab.zzf;
            if (str != null) {
                bundle.putString(AppMeasurementSdk.ConditionalUserProperty.TRIGGER_EVENT_NAME, str);
            }
            zzat zzat = zzab.zzg;
            if (zzat != null) {
                bundle.putString(AppMeasurementSdk.ConditionalUserProperty.TIMED_OUT_EVENT_NAME, zzat.zza);
                zzar zzar = zzab.zzg.zzb;
                if (zzar != null) {
                    bundle.putBundle(AppMeasurementSdk.ConditionalUserProperty.TIMED_OUT_EVENT_PARAMS, zzar.zzc());
                }
            }
            bundle.putLong(AppMeasurementSdk.ConditionalUserProperty.TRIGGER_TIMEOUT, zzab.zzh);
            zzat zzat2 = zzab.zzi;
            if (zzat2 != null) {
                bundle.putString(AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_EVENT_NAME, zzat2.zza);
                zzar zzar2 = zzab.zzi.zzb;
                if (zzar2 != null) {
                    bundle.putBundle(AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_EVENT_PARAMS, zzar2.zzc());
                }
            }
            bundle.putLong(AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_TIMESTAMP, zzab.zzc.zzc);
            bundle.putLong(AppMeasurementSdk.ConditionalUserProperty.TIME_TO_LIVE, zzab.zzj);
            zzat zzat3 = zzab.zzk;
            if (zzat3 != null) {
                bundle.putString(AppMeasurementSdk.ConditionalUserProperty.EXPIRED_EVENT_NAME, zzat3.zza);
                zzar zzar3 = zzab.zzk.zzb;
                if (zzar3 != null) {
                    bundle.putBundle(AppMeasurementSdk.ConditionalUserProperty.EXPIRED_EVENT_PARAMS, zzar3.zzc());
                }
            }
            arrayList.add(bundle);
        }
        return arrayList;
    }

    public static void zzJ(zzic zzic, Bundle bundle, boolean z) {
        if (!(bundle == null || zzic == null)) {
            if (!bundle.containsKey("_sc") || z) {
                String str = zzic.zza;
                if (str != null) {
                    bundle.putString("_sn", str);
                } else {
                    bundle.remove("_sn");
                }
                String str2 = zzic.zzb;
                if (str2 != null) {
                    bundle.putString("_sc", str2);
                } else {
                    bundle.remove("_sc");
                }
                bundle.putLong("_si", zzic.zzc);
                return;
            }
            z = false;
        }
        if (bundle != null && zzic == null && z) {
            bundle.remove("_sn");
            bundle.remove("_sc");
            bundle.remove("_si");
        }
    }

    public static boolean zzag(String str) {
        return !TextUtils.isEmpty(str) && str.startsWith("_");
    }

    public static boolean zzah(String str) {
        Preconditions.checkNotEmpty(str);
        return str.charAt(0) != '_' || str.equals("_ep");
    }

    public static boolean zzai(Context context) {
        ActivityInfo receiverInfo;
        Preconditions.checkNotNull(context);
        try {
            PackageManager packageManager = context.getPackageManager();
            if (!(packageManager == null || (receiverInfo = packageManager.getReceiverInfo(new ComponentName(context, "com.google.android.gms.measurement.AppMeasurementReceiver"), 0)) == null)) {
                if (receiverInfo.enabled) {
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

    public static boolean zzaj(Context context, boolean z) {
        Preconditions.checkNotNull(context);
        if (Build.VERSION.SDK_INT >= 24) {
            return zzat(context, "com.google.android.gms.measurement.AppMeasurementJobService");
        }
        return zzat(context, "com.google.android.gms.measurement.AppMeasurementService");
    }

    public static boolean zzak(String str, String str2) {
        if (str == null && str2 == null) {
            return true;
        }
        if (str == null) {
            return false;
        }
        return str.equals(str2);
    }

    public static boolean zzal(String str) {
        return !zzc[0].equals(str);
    }

    static final boolean zzao(Bundle bundle, int i) {
        if (bundle.getLong("_err") != 0) {
            return false;
        }
        bundle.putLong("_err", (long) i);
        return true;
    }

    static final boolean zzap(String str) {
        Preconditions.checkNotNull(str);
        return str.matches("^(1:\\d+:android:[a-f0-9]+|ca-app-pub-.*)$");
    }

    private final int zzaq(String str) {
        if ("_ldl".equals(str)) {
            this.zzs.zzf();
            return 2048;
        } else if (DBHelper.Key_ID.equals(str)) {
            this.zzs.zzf();
            return 256;
        } else if (!this.zzs.zzf().zzs(null, zzdw.zzab) || !"_lgclid".equals(str)) {
            this.zzs.zzf();
            return 36;
        } else {
            this.zzs.zzf();
            return 100;
        }
    }

    private final Object zzar(int i, Object obj, boolean z, boolean z2) {
        if (obj == null) {
            return null;
        }
        if ((obj instanceof Long) || (obj instanceof Double)) {
            return obj;
        }
        if (obj instanceof Integer) {
            return Long.valueOf((long) ((Integer) obj).intValue());
        }
        if (obj instanceof Byte) {
            return Long.valueOf((long) ((Byte) obj).byteValue());
        }
        if (obj instanceof Short) {
            return Long.valueOf((long) ((Short) obj).shortValue());
        }
        if (obj instanceof Boolean) {
            return Long.valueOf(true != ((Boolean) obj).booleanValue() ? 0 : 1);
        } else if (obj instanceof Float) {
            return Double.valueOf(((Float) obj).doubleValue());
        } else {
            if ((obj instanceof String) || (obj instanceof Character) || (obj instanceof CharSequence)) {
                return zzC(String.valueOf(obj), i, z);
            }
            if (!z2 || (!(obj instanceof Bundle[]) && !(obj instanceof Parcelable[]))) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            Parcelable[] parcelableArr = (Parcelable[]) obj;
            for (Parcelable parcelable : parcelableArr) {
                if (parcelable instanceof Bundle) {
                    Bundle zzt = zzt((Bundle) parcelable);
                    if (!zzt.isEmpty()) {
                        arrayList.add(zzt);
                    }
                }
            }
            return arrayList.toArray(new Bundle[arrayList.size()]);
        }
    }

    private static boolean zzas(String str, String[] strArr) {
        Preconditions.checkNotNull(strArr);
        for (String str2 : strArr) {
            if (zzak(str, str2)) {
                return true;
            }
        }
        return false;
    }

    private static boolean zzat(Context context, String str) {
        ServiceInfo serviceInfo;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (!(packageManager == null || (serviceInfo = packageManager.getServiceInfo(new ComponentName(context, str), 0)) == null)) {
                if (serviceInfo.enabled) {
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

    public static long zzp(byte[] bArr) {
        boolean z;
        Preconditions.checkNotNull(bArr);
        int length = bArr.length;
        int i = 0;
        if (length > 0) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkState(z);
        int i2 = length - 1;
        long j = 0;
        while (i2 >= 0 && i2 >= bArr.length - 8) {
            j += (((long) bArr[i2]) & 255) << i;
            i += 8;
            i2--;
        }
        return j;
    }

    public final Object zzA(String str, Object obj) {
        int i = 256;
        if ("_ev".equals(str)) {
            this.zzs.zzf();
            return zzar(256, obj, true, true);
        }
        if (zzag(str)) {
            this.zzs.zzf();
        } else {
            this.zzs.zzf();
            i = 100;
        }
        return zzar(i, obj, false, true);
    }

    public final Object zzB(String str, Object obj) {
        if ("_ldl".equals(str)) {
            return zzar(zzaq(str), obj, true, false);
        }
        return zzar(zzaq(str), obj, false, false);
    }

    public final String zzC(String str, int i, boolean z) {
        if (str == null) {
            return null;
        }
        if (str.codePointCount(0, str.length()) <= i) {
            return str;
        }
        if (z) {
            return String.valueOf(str.substring(0, str.offsetByCodePoints(0, i))).concat("...");
        }
        return null;
    }

    public final URL zzD(long j, String str, String str2, long j2) {
        try {
            Preconditions.checkNotEmpty(str2);
            Preconditions.checkNotEmpty(str);
            String format = String.format("https://www.googleadservices.com/pagead/conversion/app/deeplink?id_type=adid&sdk_version=%s&rdid=%s&bundleid=%s&retry=%s", String.format("v%s.%s", 42097L, Integer.valueOf(zzm())), str2, str, Long.valueOf(j2));
            if (str.equals(this.zzs.zzf().zzm())) {
                format = format.concat("&ddl_test=1");
            }
            return new URL(format);
        } catch (IllegalArgumentException | MalformedURLException e) {
            this.zzs.zzay().zzd().zzb("Failed to create BOW URL for Deferred Deep Link. exception", e.getMessage());
            return null;
        }
    }

    @EnsuresNonNull({"this.secureRandom"})
    public final SecureRandom zzF() {
        zzg();
        if (this.zzj == null) {
            this.zzj = new SecureRandom();
        }
        return this.zzj;
    }

    public final void zzH(Bundle bundle, long j) {
        long j2 = bundle.getLong("_et");
        if (j2 != 0) {
            this.zzs.zzay().zzk().zzb("Params already contained engagement", Long.valueOf(j2));
        }
        bundle.putLong("_et", j + j2);
    }

    final void zzI(Bundle bundle, int i, String str, String str2, Object obj) {
        if (zzao(bundle, i)) {
            this.zzs.zzf();
            bundle.putString("_ev", zzC(str, 40, true));
            if (obj != null) {
                Preconditions.checkNotNull(bundle);
                if ((obj instanceof String) || (obj instanceof CharSequence)) {
                    bundle.putLong("_el", (long) String.valueOf(obj).length());
                }
            }
        }
    }

    public final void zzK(Bundle bundle, Bundle bundle2) {
        if (bundle2 != null) {
            for (String str : bundle2.keySet()) {
                if (!bundle.containsKey(str)) {
                    this.zzs.zzv().zzN(bundle, str, bundle2.get(str));
                }
            }
        }
    }

    public final void zzL(zzej zzej, int i) {
        int i2 = 0;
        for (String str : new TreeSet(zzej.zzd.keySet())) {
            if (zzah(str) && (i2 = i2 + 1) > i) {
                StringBuilder sb = new StringBuilder(48);
                sb.append("Event can't contain more than ");
                sb.append(i);
                sb.append(" params");
                this.zzs.zzay().zze().zzc(sb.toString(), this.zzs.zzj().zzc(zzej.zza), this.zzs.zzj().zzb(zzej.zzd));
                zzao(zzej.zzd, 5);
                zzej.zzd.remove(str);
            }
        }
    }

    public final void zzM(zzkt zzkt, String str, int i, String str2, String str3, int i2) {
        Bundle bundle = new Bundle();
        zzao(bundle, i);
        if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3)) {
            bundle.putString(str2, str3);
        }
        if (i == 6 || i == 7 || i == 2) {
            bundle.putLong("_el", (long) i2);
        }
        zzkt.zza(str, "_err", bundle);
    }

    public final void zzN(Bundle bundle, String str, Object obj) {
        String str2;
        if (bundle != null) {
            if (obj instanceof Long) {
                bundle.putLong(str, ((Long) obj).longValue());
            } else if (obj instanceof String) {
                bundle.putString(str, String.valueOf(obj));
            } else if (obj instanceof Double) {
                bundle.putDouble(str, ((Double) obj).doubleValue());
            } else if (obj instanceof Bundle[]) {
                bundle.putParcelableArray(str, (Bundle[]) obj);
            } else if (str != null) {
                if (obj != null) {
                    str2 = obj.getClass().getSimpleName();
                } else {
                    str2 = null;
                }
                this.zzs.zzay().zzl().zzc("Not putting event parameter. Invalid value type. name, type", this.zzs.zzj().zzd(str), str2);
            }
        }
    }

    public final void zzO(zzcf zzcf, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("r", z);
        try {
            zzcf.zzd(bundle);
        } catch (RemoteException e) {
            this.zzs.zzay().zzk().zzb("Error returning boolean value to wrapper", e);
        }
    }

    public final void zzP(zzcf zzcf, ArrayList<Bundle> arrayList) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("r", arrayList);
        try {
            zzcf.zzd(bundle);
        } catch (RemoteException e) {
            this.zzs.zzay().zzk().zzb("Error returning bundle list to wrapper", e);
        }
    }

    public final void zzQ(zzcf zzcf, Bundle bundle) {
        try {
            zzcf.zzd(bundle);
        } catch (RemoteException e) {
            this.zzs.zzay().zzk().zzb("Error returning bundle value to wrapper", e);
        }
    }

    public final void zzR(zzcf zzcf, byte[] bArr) {
        Bundle bundle = new Bundle();
        bundle.putByteArray("r", bArr);
        try {
            zzcf.zzd(bundle);
        } catch (RemoteException e) {
            this.zzs.zzay().zzk().zzb("Error returning byte array to wrapper", e);
        }
    }

    public final void zzS(zzcf zzcf, int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("r", i);
        try {
            zzcf.zzd(bundle);
        } catch (RemoteException e) {
            this.zzs.zzay().zzk().zzb("Error returning int value to wrapper", e);
        }
    }

    public final void zzT(zzcf zzcf, long j) {
        Bundle bundle = new Bundle();
        bundle.putLong("r", j);
        try {
            zzcf.zzd(bundle);
        } catch (RemoteException e) {
            this.zzs.zzay().zzk().zzb("Error returning long value to wrapper", e);
        }
    }

    public final void zzU(zzcf zzcf, String str) {
        Bundle bundle = new Bundle();
        bundle.putString("r", str);
        try {
            zzcf.zzd(bundle);
        } catch (RemoteException e) {
            this.zzs.zzay().zzk().zzb("Error returning string value to wrapper", e);
        }
    }

    final void zzV(String str, String str2, String str3, Bundle bundle, List<String> list, boolean z) {
        int i;
        String str4;
        String str5;
        int i2;
        int i3;
        if (bundle != null) {
            this.zzs.zzf();
            int i4 = 0;
            for (String str6 : new TreeSet(bundle.keySet())) {
                if (list == null || !list.contains(str6)) {
                    if (!z) {
                        i3 = zzj(str6);
                    } else {
                        i3 = 0;
                    }
                    if (i3 == 0) {
                        i = zzi(str6);
                    } else {
                        i = i3;
                    }
                } else {
                    i = 0;
                }
                if (i != 0) {
                    if (i == 3) {
                        str4 = str6;
                    } else {
                        str4 = null;
                    }
                    zzI(bundle, i, str6, str6, str4);
                    bundle.remove(str6);
                } else {
                    if (zzae(bundle.get(str6))) {
                        this.zzs.zzay().zzl().zzd("Nested Bundle parameters are not allowed; discarded. event name, param name, child param name", str2, str3, str6);
                        i2 = 22;
                        str5 = str6;
                    } else {
                        str5 = str6;
                        i2 = zza(str, str2, str6, bundle.get(str6), bundle, list, z, false);
                    }
                    if (i2 != 0 && !"_ev".equals(str5)) {
                        zzI(bundle, i2, str5, str5, bundle.get(str5));
                        bundle.remove(str5);
                    } else if (zzah(str5) && !zzas(str5, zzgq.zzd) && (i4 = i4 + 1) > 0) {
                        this.zzs.zzay().zze().zzc("Item cannot contain custom parameters", this.zzs.zzj().zzc(str2), this.zzs.zzj().zzb(bundle));
                        zzao(bundle, 23);
                        bundle.remove(str5);
                    }
                }
            }
        }
    }

    public final boolean zzW(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str)) {
            zzoq.zzc();
            if (this.zzs.zzf().zzs(null, zzdw.zzad) && !TextUtils.isEmpty(str3)) {
                return true;
            }
            if (TextUtils.isEmpty(str2)) {
                if (this.zzs.zzL()) {
                    this.zzs.zzay().zze().zza("Missing google_app_id. Firebase Analytics disabled. See https://goo.gl/NAOOOI");
                }
                return false;
            } else if (zzap(str2)) {
                return true;
            } else {
                this.zzs.zzay().zze().zzb("Invalid admob_app_id. Analytics disabled.", zzei.zzn(str2));
                return false;
            }
        } else if (zzap(str)) {
            return true;
        } else {
            if (this.zzs.zzL()) {
                this.zzs.zzay().zze().zzb("Invalid google_app_id. Firebase Analytics disabled. See https://goo.gl/NAOOOI. provided id", zzei.zzn(str));
            }
            return false;
        }
    }

    public final boolean zzX(String str, int i, String str2) {
        if (str2 == null) {
            this.zzs.zzay().zze().zzb("Name is required and can't be null. Type", str);
            return false;
        } else if (str2.codePointCount(0, str2.length()) <= i) {
            return true;
        } else {
            this.zzs.zzay().zze().zzd("Name is too long. Type, maximum supported length, name", str, Integer.valueOf(i), str2);
            return false;
        }
    }

    public final boolean zzY(String str, String[] strArr, String[] strArr2, String str2) {
        if (str2 == null) {
            this.zzs.zzay().zze().zzb("Name is required and can't be null. Type", str);
            return false;
        }
        Preconditions.checkNotNull(str2);
        String[] strArr3 = zzb;
        for (int i = 0; i < 3; i++) {
            if (str2.startsWith(strArr3[i])) {
                this.zzs.zzay().zze().zzc("Name starts with reserved prefix. Type, name", str, str2);
                return false;
            }
        }
        if (strArr == null || !zzas(str2, strArr)) {
            return true;
        }
        if (strArr2 != null && zzas(str2, strArr2)) {
            return true;
        }
        this.zzs.zzay().zze().zzc("Name is reserved. Type, name", str, str2);
        return false;
    }

    public final boolean zzZ(String str, String str2, int i, Object obj) {
        if (obj == null || (obj instanceof Long) || (obj instanceof Float) || (obj instanceof Integer) || (obj instanceof Byte) || (obj instanceof Short) || (obj instanceof Boolean) || (obj instanceof Double)) {
            return true;
        }
        if (!(obj instanceof String) && !(obj instanceof Character) && !(obj instanceof CharSequence)) {
            return false;
        }
        String valueOf = String.valueOf(obj);
        if (valueOf.codePointCount(0, valueOf.length()) <= i) {
            return true;
        }
        this.zzs.zzay().zzl().zzd("Value is too long; discarded. Value kind, name, value length", str, str2, Integer.valueOf(valueOf.length()));
        return false;
    }

    final int zza(String str, String str2, String str3, Object obj, Bundle bundle, List<String> list, boolean z, boolean z2) {
        int i;
        int i2;
        Object obj2;
        int i3;
        zzg();
        if (!zzae(obj)) {
            i = 0;
        } else if (!z2) {
            return 21;
        } else {
            if (!zzas(str3, zzgq.zzc)) {
                return 20;
            }
            zzjj zzt = this.zzs.zzt();
            zzt.zzg();
            zzt.zza();
            if (zzt.zzN() && zzt.zzs.zzv().zzm() < 200900) {
                return 25;
            }
            this.zzs.zzf();
            boolean z3 = obj instanceof Parcelable[];
            if (z3) {
                i3 = ((Parcelable[]) obj).length;
            } else if (obj instanceof ArrayList) {
                i3 = ((ArrayList) obj).size();
            } else {
                i = 0;
            }
            if (i3 > 200) {
                this.zzs.zzay().zzl().zzd("Parameter array is too long; discarded. Value kind, name, array length", "param", str3, Integer.valueOf(i3));
                this.zzs.zzf();
                if (z3) {
                    Parcelable[] parcelableArr = (Parcelable[]) obj;
                    if (parcelableArr.length > 200) {
                        bundle.putParcelableArray(str3, (Parcelable[]) Arrays.copyOf(parcelableArr, (int) ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION));
                        i = 17;
                    } else {
                        i = 17;
                    }
                } else {
                    if (obj instanceof ArrayList) {
                        ArrayList arrayList = (ArrayList) obj;
                        if (arrayList.size() > 200) {
                            bundle.putParcelableArrayList(str3, new ArrayList<>(arrayList.subList(0, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION)));
                        }
                    }
                    i = 17;
                }
            } else {
                i = 0;
            }
        }
        if ((!this.zzs.zzf().zzs(str, zzdw.zzR) || !zzag(str2)) && !zzag(str3)) {
            this.zzs.zzf();
            i2 = 100;
        } else {
            this.zzs.zzf();
            i2 = 256;
        }
        if (zzZ("param", str3, i2, obj)) {
            return i;
        }
        if (!z2) {
            return 4;
        }
        if (obj instanceof Bundle) {
            zzV(str, str2, str3, (Bundle) obj, list, z);
        } else if (obj instanceof Parcelable[]) {
            Parcelable[] parcelableArr2 = (Parcelable[]) obj;
            for (Parcelable parcelable : parcelableArr2) {
                if (!(parcelable instanceof Bundle)) {
                    this.zzs.zzay().zzl().zzc("All Parcelable[] elements must be of type Bundle. Value type, name", parcelable.getClass(), str3);
                    return 4;
                }
                zzV(str, str2, str3, (Bundle) parcelable, list, z);
            }
        } else if (!(obj instanceof ArrayList)) {
            return 4;
        } else {
            ArrayList arrayList2 = (ArrayList) obj;
            int size = arrayList2.size();
            for (int i4 = 0; i4 < size; i4++) {
                Object obj3 = arrayList2.get(i4);
                if (!(obj3 instanceof Bundle)) {
                    zzeg zzl = this.zzs.zzay().zzl();
                    if (obj3 != null) {
                        obj2 = obj3.getClass();
                    } else {
                        obj2 = "null";
                    }
                    zzl.zzc("All ArrayList elements must be of type Bundle. Value type, name", obj2, str3);
                    return 4;
                }
                zzV(str, str2, str3, (Bundle) obj3, list, z);
            }
        }
        return i;
    }

    @Override // com.google.android.gms.measurement.internal.zzgm
    protected final void zzaA() {
        zzg();
        SecureRandom secureRandom = new SecureRandom();
        long nextLong = secureRandom.nextLong();
        if (nextLong == 0) {
            nextLong = secureRandom.nextLong();
            if (nextLong == 0) {
                this.zzs.zzay().zzk().zza("Utils falling back to Random for random id");
            }
        }
        this.zzk.set(nextLong);
    }

    public final boolean zzaa(String str, String str2) {
        if (str2 == null) {
            this.zzs.zzay().zze().zzb("Name is required and can't be null. Type", str);
            return false;
        } else if (str2.length() == 0) {
            this.zzs.zzay().zze().zzb("Name is required and can't be empty. Type", str);
            return false;
        } else {
            int codePointAt = str2.codePointAt(0);
            if (!Character.isLetter(codePointAt)) {
                if (codePointAt == 95) {
                    codePointAt = 95;
                } else {
                    this.zzs.zzay().zze().zzc("Name must start with a letter or _ (underscore). Type, name", str, str2);
                    return false;
                }
            }
            int length = str2.length();
            int charCount = Character.charCount(codePointAt);
            while (charCount < length) {
                int codePointAt2 = str2.codePointAt(charCount);
                if (codePointAt2 == 95 || Character.isLetterOrDigit(codePointAt2)) {
                    charCount += Character.charCount(codePointAt2);
                } else {
                    this.zzs.zzay().zze().zzc("Name must consist of letters, digits or _ (underscores). Type, name", str, str2);
                    return false;
                }
            }
            return true;
        }
    }

    public final boolean zzab(String str, String str2) {
        if (str2 == null) {
            this.zzs.zzay().zze().zzb("Name is required and can't be null. Type", str);
            return false;
        } else if (str2.length() == 0) {
            this.zzs.zzay().zze().zzb("Name is required and can't be empty. Type", str);
            return false;
        } else {
            int codePointAt = str2.codePointAt(0);
            if (!Character.isLetter(codePointAt)) {
                this.zzs.zzay().zze().zzc("Name must start with a letter. Type, name", str, str2);
                return false;
            }
            int length = str2.length();
            int charCount = Character.charCount(codePointAt);
            while (charCount < length) {
                int codePointAt2 = str2.codePointAt(charCount);
                if (codePointAt2 == 95 || Character.isLetterOrDigit(codePointAt2)) {
                    charCount += Character.charCount(codePointAt2);
                } else {
                    this.zzs.zzay().zze().zzc("Name must consist of letters, digits or _ (underscores). Type, name", str, str2);
                    return false;
                }
            }
            return true;
        }
    }

    public final boolean zzac(String str) {
        zzg();
        if (Wrappers.packageManager(this.zzs.zzau()).checkCallingOrSelfPermission(str) == 0) {
            return true;
        }
        this.zzs.zzay().zzc().zzb("Permission not granted", str);
        return false;
    }

    public final boolean zzad(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String zzl = this.zzs.zzf().zzl();
        this.zzs.zzaw();
        return zzl.equals(str);
    }

    public final boolean zzae(Object obj) {
        return (obj instanceof Parcelable[]) || (obj instanceof ArrayList) || (obj instanceof Bundle);
    }

    public final boolean zzaf(Context context, String str) {
        X500Principal x500Principal = new X500Principal("CN=Android Debug,O=Android,C=US");
        try {
            PackageInfo packageInfo = Wrappers.packageManager(context).getPackageInfo(str, 64);
            if (packageInfo == null || packageInfo.signatures == null || packageInfo.signatures.length <= 0) {
                return true;
            }
            return ((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(packageInfo.signatures[0].toByteArray()))).getSubjectX500Principal().equals(x500Principal);
        } catch (PackageManager.NameNotFoundException e) {
            this.zzs.zzay().zzd().zzb("Package name not found", e);
            return true;
        } catch (CertificateException e2) {
            this.zzs.zzay().zzd().zzb("Error obtaining certificate", e2);
            return true;
        }
    }

    public final boolean zzam(String str, String str2, String str3, String str4) {
        boolean isEmpty = TextUtils.isEmpty(str);
        boolean isEmpty2 = TextUtils.isEmpty(str2);
        if (!isEmpty && !isEmpty2) {
            Preconditions.checkNotNull(str);
            return !str.equals(str2);
        } else if (isEmpty && isEmpty2) {
            return (TextUtils.isEmpty(str3) || TextUtils.isEmpty(str4)) ? !TextUtils.isEmpty(str4) : !str3.equals(str4);
        } else {
            if (isEmpty) {
                return TextUtils.isEmpty(str3) || !str3.equals(str4);
            }
            if (TextUtils.isEmpty(str4)) {
                return false;
            }
            return TextUtils.isEmpty(str3) || !str3.equals(str4);
        }
    }

    public final byte[] zzan(Parcelable parcelable) {
        if (parcelable == null) {
            return null;
        }
        Parcel obtain = Parcel.obtain();
        try {
            parcelable.writeToParcel(obtain, 0);
            return obtain.marshall();
        } finally {
            obtain.recycle();
        }
    }

    public final int zzd(String str, Object obj) {
        boolean z;
        if ("_ldl".equals(str)) {
            z = zzZ("user property referrer", str, zzaq(str), obj);
        } else {
            z = zzZ("user property", str, zzaq(str), obj);
        }
        return z ? 0 : 7;
    }

    @Override // com.google.android.gms.measurement.internal.zzgm
    protected final boolean zzf() {
        return true;
    }

    public final int zzh(String str) {
        if (!zzaa(NotificationCompat.CATEGORY_EVENT, str)) {
            return 2;
        }
        if (!zzY(NotificationCompat.CATEGORY_EVENT, zzgp.zza, zzgp.zzb, str)) {
            return 13;
        }
        this.zzs.zzf();
        if (!zzX(NotificationCompat.CATEGORY_EVENT, 40, str)) {
            return 2;
        }
        return 0;
    }

    final int zzi(String str) {
        if (!zzaa("event param", str)) {
            return 3;
        }
        if (!zzY("event param", null, null, str)) {
            return 14;
        }
        this.zzs.zzf();
        if (!zzX("event param", 40, str)) {
            return 3;
        }
        return 0;
    }

    final int zzj(String str) {
        if (!zzab("event param", str)) {
            return 3;
        }
        if (!zzY("event param", null, null, str)) {
            return 14;
        }
        this.zzs.zzf();
        if (!zzX("event param", 40, str)) {
            return 3;
        }
        return 0;
    }

    public final int zzl(String str) {
        if (!zzaa("user property", str)) {
            return 6;
        }
        if (!zzY("user property", zzgr.zza, null, str)) {
            return 15;
        }
        this.zzs.zzf();
        if (!zzX("user property", 24, str)) {
            return 6;
        }
        return 0;
    }

    @EnsuresNonNull({"this.apkVersion"})
    public final int zzm() {
        if (this.zzm == null) {
            this.zzm = Integer.valueOf(GoogleApiAvailabilityLight.getInstance().getApkVersion(this.zzs.zzau()) / 1000);
        }
        return this.zzm.intValue();
    }

    public final int zzo(int i) {
        return GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(this.zzs.zzau(), 12451000);
    }

    public final long zzq() {
        long andIncrement;
        long j;
        if (this.zzk.get() == 0) {
            synchronized (this.zzk) {
                long nextLong = new Random(System.nanoTime() ^ this.zzs.zzav().currentTimeMillis()).nextLong();
                int i = this.zzl + 1;
                this.zzl = i;
                j = nextLong + ((long) i);
            }
            return j;
        }
        synchronized (this.zzk) {
            this.zzk.compareAndSet(-1, 1);
            andIncrement = this.zzk.getAndIncrement();
        }
        return andIncrement;
    }

    public final long zzr(long j, long j2) {
        return (j + (j2 * 60000)) / 86400000;
    }

    public final Bundle zzs(Uri uri) {
        String str;
        String str2;
        String str3;
        String str4;
        if (uri == null) {
            return null;
        }
        try {
            if (uri.isHierarchical()) {
                str4 = uri.getQueryParameter("utm_campaign");
                str3 = uri.getQueryParameter("utm_source");
                str2 = uri.getQueryParameter("utm_medium");
                str = uri.getQueryParameter("gclid");
            } else {
                str4 = null;
                str3 = null;
                str2 = null;
                str = null;
            }
            if (TextUtils.isEmpty(str4) && TextUtils.isEmpty(str3) && TextUtils.isEmpty(str2) && TextUtils.isEmpty(str)) {
                return null;
            }
            Bundle bundle = new Bundle();
            if (!TextUtils.isEmpty(str4)) {
                bundle.putString(FirebaseAnalytics.Param.CAMPAIGN, str4);
            }
            if (!TextUtils.isEmpty(str3)) {
                bundle.putString(FirebaseAnalytics.Param.SOURCE, str3);
            }
            if (!TextUtils.isEmpty(str2)) {
                bundle.putString(FirebaseAnalytics.Param.MEDIUM, str2);
            }
            if (!TextUtils.isEmpty(str)) {
                bundle.putString("gclid", str);
            }
            String queryParameter = uri.getQueryParameter("utm_term");
            if (!TextUtils.isEmpty(queryParameter)) {
                bundle.putString(FirebaseAnalytics.Param.TERM, queryParameter);
            }
            String queryParameter2 = uri.getQueryParameter("utm_content");
            if (!TextUtils.isEmpty(queryParameter2)) {
                bundle.putString("content", queryParameter2);
            }
            String queryParameter3 = uri.getQueryParameter(FirebaseAnalytics.Param.ACLID);
            if (!TextUtils.isEmpty(queryParameter3)) {
                bundle.putString(FirebaseAnalytics.Param.ACLID, queryParameter3);
            }
            String queryParameter4 = uri.getQueryParameter(FirebaseAnalytics.Param.CP1);
            if (!TextUtils.isEmpty(queryParameter4)) {
                bundle.putString(FirebaseAnalytics.Param.CP1, queryParameter4);
            }
            String queryParameter5 = uri.getQueryParameter("anid");
            if (!TextUtils.isEmpty(queryParameter5)) {
                bundle.putString("anid", queryParameter5);
            }
            return bundle;
        } catch (UnsupportedOperationException e) {
            this.zzs.zzay().zzk().zzb("Install referrer url isn't a hierarchical URI", e);
            return null;
        }
    }

    public final Bundle zzt(Bundle bundle) {
        Bundle bundle2 = new Bundle();
        if (bundle != null) {
            for (String str : bundle.keySet()) {
                Object zzA = zzA(str, bundle.get(str));
                if (zzA == null) {
                    this.zzs.zzay().zzl().zzb("Param value can't be null", this.zzs.zzj().zzd(str));
                } else {
                    zzN(bundle2, str, zzA);
                }
            }
        }
        return bundle2;
    }

    public final Bundle zzy(String str, String str2, Bundle bundle, List<String> list, boolean z) {
        int i;
        int i2;
        String str3;
        String str4;
        int i3;
        boolean zzas = zzas(str2, zzgp.zzd);
        if (bundle == null) {
            return null;
        }
        Bundle bundle2 = new Bundle(bundle);
        int zzc2 = this.zzs.zzf().zzc();
        int i4 = 0;
        for (String str5 : new TreeSet(bundle.keySet())) {
            if (list == null || !list.contains(str5)) {
                if (!z) {
                    i3 = zzj(str5);
                } else {
                    i3 = 0;
                }
                if (i3 == 0) {
                    i = zzi(str5);
                } else {
                    i = i3;
                }
            } else {
                i = 0;
            }
            if (i != 0) {
                if (i == 3) {
                    str4 = str5;
                } else {
                    str4 = null;
                }
                zzI(bundle2, i, str5, str5, str4);
                bundle2.remove(str5);
                i2 = zzc2;
            } else {
                i2 = zzc2;
                int zza2 = zza(str, str2, str5, bundle.get(str5), bundle2, list, z, zzas);
                if (zza2 == 17) {
                    zzI(bundle2, 17, str5, str5, false);
                    str3 = str5;
                } else if (zza2 != 0) {
                    str3 = str5;
                    if (!"_ev".equals(str3)) {
                        zzI(bundle2, zza2, zza2 == 21 ? str2 : str3, str3, bundle.get(str3));
                        bundle2.remove(str3);
                    }
                } else {
                    str3 = str5;
                }
                if (zzah(str3)) {
                    int i5 = i4 + 1;
                    if (i5 > i2) {
                        StringBuilder sb = new StringBuilder(48);
                        sb.append("Event can't contain more than ");
                        sb.append(i2);
                        sb.append(" params");
                        this.zzs.zzay().zze().zzc(sb.toString(), this.zzs.zzj().zzc(str2), this.zzs.zzj().zzb(bundle));
                        zzao(bundle2, 5);
                        bundle2.remove(str3);
                    }
                    i4 = i5;
                }
            }
            zzc2 = i2;
        }
        return bundle2;
    }

    public final zzat zzz(String str, String str2, Bundle bundle, String str3, long j, boolean z, boolean z2) {
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        if (zzh(str2) == 0) {
            Bundle bundle2 = bundle != null ? new Bundle(bundle) : new Bundle();
            bundle2.putString("_o", str3);
            Bundle zzy = zzy(str, str2, bundle2, CollectionUtils.listOf("_o"), true);
            if (z) {
                zzy = zzt(zzy);
            }
            Preconditions.checkNotNull(zzy);
            return new zzat(str2, new zzar(zzy), str3, j);
        }
        this.zzs.zzay().zzd().zzb("Invalid conditional property event name", this.zzs.zzj().zze(str2));
        throw new IllegalArgumentException();
    }
}
