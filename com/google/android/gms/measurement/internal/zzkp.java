package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import com.google.android.gms.internal.measurement.zzaa;
import com.google.android.gms.internal.measurement.zzej;
import com.google.android.gms.internal.measurement.zzel;
import com.google.android.gms.internal.measurement.zzeq;
import com.google.android.gms.internal.measurement.zzes;
import com.google.android.gms.internal.measurement.zzex;
import com.google.android.gms.internal.measurement.zzfk;
import com.google.android.gms.internal.measurement.zzfm;
import com.google.android.gms.internal.measurement.zzfn;
import com.google.android.gms.internal.measurement.zzfo;
import com.google.android.gms.internal.measurement.zzfr;
import com.google.android.gms.internal.measurement.zzfs;
import com.google.android.gms.internal.measurement.zzfw;
import com.google.android.gms.internal.measurement.zzfx;
import com.google.android.gms.internal.measurement.zzfy;
import com.google.android.gms.internal.measurement.zzgd;
import com.google.android.gms.internal.measurement.zzgf;
import com.google.android.gms.internal.measurement.zzgg;
import com.google.android.gms.internal.measurement.zzgh;
import com.google.android.gms.internal.measurement.zzjl;
import com.google.android.gms.internal.measurement.zzkj;
import com.google.android.gms.internal.measurement.zzlf;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.io.IOUtils;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzkp extends zzkd {
    public zzkp(zzkn zzkn) {
        super(zzkn);
    }

    public static final void zzA(zzfn zzfn, String str, Object obj) {
        List<zzfs> zzp = zzfn.zzp();
        int i = 0;
        while (true) {
            if (i >= zzp.size()) {
                i = -1;
                break;
            } else if (str.equals(zzp.get(i).zzg())) {
                break;
            } else {
                i++;
            }
        }
        zzfr zze = zzfs.zze();
        zze.zzj(str);
        if (obj instanceof Long) {
            zze.zzi(((Long) obj).longValue());
        } else if (obj instanceof String) {
            zze.zzk((String) obj);
        } else if (obj instanceof Double) {
            zze.zzh(((Double) obj).doubleValue());
        } else if (obj instanceof Bundle[]) {
            zze.zzb(zzq((Bundle[]) obj));
        }
        if (i >= 0) {
            zzfn.zzj(i, zze);
        } else {
            zzfn.zze(zze);
        }
    }

    public static final boolean zzB(zzat zzat, zzp zzp) {
        Preconditions.checkNotNull(zzat);
        Preconditions.checkNotNull(zzp);
        return !TextUtils.isEmpty(zzp.zzb) || !TextUtils.isEmpty(zzp.zzq);
    }

    public static final zzfs zzC(zzfo zzfo, String str) {
        for (zzfs zzfs : zzfo.zzi()) {
            if (zzfs.zzg().equals(str)) {
                return zzfs;
            }
        }
        return null;
    }

    public static final Object zzD(zzfo zzfo, String str) {
        zzfs zzC = zzC(zzfo, str);
        if (zzC == null) {
            return null;
        }
        if (zzC.zzy()) {
            return zzC.zzh();
        }
        if (zzC.zzw()) {
            return Long.valueOf(zzC.zzd());
        }
        if (zzC.zzu()) {
            return Double.valueOf(zzC.zza());
        }
        if (zzC.zzc() <= 0) {
            return null;
        }
        List<zzfs> zzi = zzC.zzi();
        ArrayList arrayList = new ArrayList();
        for (zzfs zzfs : zzi) {
            if (zzfs != null) {
                Bundle bundle = new Bundle();
                for (zzfs zzfs2 : zzfs.zzi()) {
                    if (zzfs2.zzy()) {
                        bundle.putString(zzfs2.zzg(), zzfs2.zzh());
                    } else if (zzfs2.zzw()) {
                        bundle.putLong(zzfs2.zzg(), zzfs2.zzd());
                    } else if (zzfs2.zzu()) {
                        bundle.putDouble(zzfs2.zzg(), zzfs2.zza());
                    }
                }
                if (!bundle.isEmpty()) {
                    arrayList.add(bundle);
                }
            }
        }
        return (Bundle[]) arrayList.toArray(new Bundle[arrayList.size()]);
    }

    private final void zzE(StringBuilder sb, int i, List<zzfs> list) {
        String str;
        String str2;
        Long l;
        if (list != null) {
            int i2 = i + 1;
            for (zzfs zzfs : list) {
                if (zzfs != null) {
                    zzG(sb, i2);
                    sb.append("param {\n");
                    Double d = null;
                    if (zzfs.zzx()) {
                        str = this.zzs.zzj().zzd(zzfs.zzg());
                    } else {
                        str = null;
                    }
                    zzJ(sb, i2, AppMeasurementSdk.ConditionalUserProperty.NAME, str);
                    if (zzfs.zzy()) {
                        str2 = zzfs.zzh();
                    } else {
                        str2 = null;
                    }
                    zzJ(sb, i2, "string_value", str2);
                    if (zzfs.zzw()) {
                        l = Long.valueOf(zzfs.zzd());
                    } else {
                        l = null;
                    }
                    zzJ(sb, i2, "int_value", l);
                    if (zzfs.zzu()) {
                        d = Double.valueOf(zzfs.zza());
                    }
                    zzJ(sb, i2, "double_value", d);
                    if (zzfs.zzc() > 0) {
                        zzE(sb, i2, zzfs.zzi());
                    }
                    zzG(sb, i2);
                    sb.append("}\n");
                }
            }
        }
    }

    private final void zzF(StringBuilder sb, int i, zzel zzel) {
        String str;
        if (zzel != null) {
            zzG(sb, i);
            sb.append("filter {\n");
            if (zzel.zzh()) {
                zzJ(sb, i, "complement", Boolean.valueOf(zzel.zzg()));
            }
            if (zzel.zzj()) {
                zzJ(sb, i, "param_name", this.zzs.zzj().zzd(zzel.zze()));
            }
            if (zzel.zzk()) {
                int i2 = i + 1;
                zzex zzd = zzel.zzd();
                if (zzd != null) {
                    zzG(sb, i2);
                    sb.append("string_filter {\n");
                    if (zzd.zzi()) {
                        switch (zzd.zzj()) {
                            case 1:
                                str = "UNKNOWN_MATCH_TYPE";
                                break;
                            case 2:
                                str = "REGEXP";
                                break;
                            case 3:
                                str = "BEGINS_WITH";
                                break;
                            case 4:
                                str = "ENDS_WITH";
                                break;
                            case 5:
                                str = "PARTIAL";
                                break;
                            case 6:
                                str = "EXACT";
                                break;
                            default:
                                str = "IN_LIST";
                                break;
                        }
                        zzJ(sb, i2, "match_type", str);
                    }
                    if (zzd.zzh()) {
                        zzJ(sb, i2, "expression", zzd.zzd());
                    }
                    if (zzd.zzg()) {
                        zzJ(sb, i2, "case_sensitive", Boolean.valueOf(zzd.zzf()));
                    }
                    if (zzd.zza() > 0) {
                        zzG(sb, i2 + 1);
                        sb.append("expression_list {\n");
                        for (String str2 : zzd.zze()) {
                            zzG(sb, i2 + 2);
                            sb.append(str2);
                            sb.append(IOUtils.LINE_SEPARATOR_UNIX);
                        }
                        sb.append("}\n");
                    }
                    zzG(sb, i2);
                    sb.append("}\n");
                }
            }
            if (zzel.zzi()) {
                zzK(sb, i + 1, "number_filter", zzel.zzc());
            }
            zzG(sb, i);
            sb.append("}\n");
        }
    }

    private static final void zzG(StringBuilder sb, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            sb.append("  ");
        }
    }

    private static final String zzH(boolean z, boolean z2, boolean z3) {
        StringBuilder sb = new StringBuilder();
        if (z) {
            sb.append("Dynamic ");
        }
        if (z2) {
            sb.append("Sequence ");
        }
        if (z3) {
            sb.append("Session-Scoped ");
        }
        return sb.toString();
    }

    private static final void zzI(StringBuilder sb, int i, String str, zzgd zzgd) {
        Integer num;
        Integer num2;
        if (zzgd != null) {
            zzG(sb, 3);
            sb.append(str);
            sb.append(" {\n");
            if (zzgd.zzb() != 0) {
                zzG(sb, 4);
                sb.append("results: ");
                int i2 = 0;
                for (Long l : zzgd.zzk()) {
                    int i3 = i2 + 1;
                    if (i2 != 0) {
                        sb.append(", ");
                    }
                    sb.append(l);
                    i2 = i3;
                }
                sb.append('\n');
            }
            if (zzgd.zzd() != 0) {
                zzG(sb, 4);
                sb.append("status: ");
                int i4 = 0;
                for (Long l2 : zzgd.zzn()) {
                    int i5 = i4 + 1;
                    if (i4 != 0) {
                        sb.append(", ");
                    }
                    sb.append(l2);
                    i4 = i5;
                }
                sb.append('\n');
            }
            if (zzgd.zza() != 0) {
                zzG(sb, 4);
                sb.append("dynamic_filter_timestamps: {");
                int i6 = 0;
                for (zzfm zzfm : zzgd.zzj()) {
                    int i7 = i6 + 1;
                    if (i6 != 0) {
                        sb.append(", ");
                    }
                    if (zzfm.zzh()) {
                        num2 = Integer.valueOf(zzfm.zza());
                    } else {
                        num2 = null;
                    }
                    sb.append(num2);
                    sb.append(":");
                    sb.append(zzfm.zzg() ? Long.valueOf(zzfm.zzb()) : null);
                    i6 = i7;
                }
                sb.append("}\n");
            }
            if (zzgd.zzc() != 0) {
                zzG(sb, 4);
                sb.append("sequence_filter_timestamps: {");
                int i8 = 0;
                for (zzgf zzgf : zzgd.zzm()) {
                    int i9 = i8 + 1;
                    if (i8 != 0) {
                        sb.append(", ");
                    }
                    if (zzgf.zzi()) {
                        num = Integer.valueOf(zzgf.zzb());
                    } else {
                        num = null;
                    }
                    sb.append(num);
                    sb.append(": [");
                    int i10 = 0;
                    for (Long l3 : zzgf.zzf()) {
                        long longValue = l3.longValue();
                        int i11 = i10 + 1;
                        if (i10 != 0) {
                            sb.append(", ");
                        }
                        sb.append(longValue);
                        i10 = i11;
                    }
                    sb.append("]");
                    i8 = i9;
                }
                sb.append("}\n");
            }
            zzG(sb, 3);
            sb.append("}\n");
        }
    }

    private static final void zzJ(StringBuilder sb, int i, String str, Object obj) {
        if (obj != null) {
            zzG(sb, i + 1);
            sb.append(str);
            sb.append(": ");
            sb.append(obj);
            sb.append('\n');
        }
    }

    private static final void zzK(StringBuilder sb, int i, String str, zzeq zzeq) {
        if (zzeq != null) {
            zzG(sb, i);
            sb.append(str);
            sb.append(" {\n");
            if (zzeq.zzg()) {
                int zzm = zzeq.zzm();
                zzJ(sb, i, "comparison_type", zzm != 1 ? zzm != 2 ? zzm != 3 ? zzm != 4 ? "BETWEEN" : "EQUAL" : "GREATER_THAN" : "LESS_THAN" : "UNKNOWN_COMPARISON_TYPE");
            }
            if (zzeq.zzi()) {
                zzJ(sb, i, "match_as_float", Boolean.valueOf(zzeq.zzf()));
            }
            if (zzeq.zzh()) {
                zzJ(sb, i, "comparison_value", zzeq.zzc());
            }
            if (zzeq.zzk()) {
                zzJ(sb, i, "min_comparison_value", zzeq.zze());
            }
            if (zzeq.zzj()) {
                zzJ(sb, i, "max_comparison_value", zzeq.zzd());
            }
            zzG(sb, i);
            sb.append("}\n");
        }
    }

    public static int zza(zzfx zzfx, String str) {
        for (int i = 0; i < zzfx.zzb(); i++) {
            if (str.equals(zzfx.zzak(i).zzf())) {
                return i;
            }
        }
        return -1;
    }

    static Bundle zzf(Map<String, Object> map, boolean z) {
        Bundle bundle = new Bundle();
        for (String str : map.keySet()) {
            Object obj = map.get(str);
            if (obj == null) {
                bundle.putString(str, null);
            } else if (obj instanceof Long) {
                bundle.putLong(str, ((Long) obj).longValue());
            } else if (obj instanceof Double) {
                bundle.putDouble(str, ((Double) obj).doubleValue());
            } else if (!(obj instanceof ArrayList)) {
                bundle.putString(str, obj.toString());
            } else if (z) {
                ArrayList arrayList = (ArrayList) obj;
                ArrayList<? extends Parcelable> arrayList2 = new ArrayList<>();
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    arrayList2.add(zzf((Map) arrayList.get(i), false));
                }
                bundle.putParcelableArrayList(str, arrayList2);
            }
        }
        return bundle;
    }

    public static zzat zzi(zzaa zzaa) {
        String str;
        String str2;
        Object obj;
        Bundle zzf = zzf(zzaa.zze(), true);
        if (!zzf.containsKey("_o") || (obj = zzf.get("_o")) == null) {
            str = "app";
        } else {
            str = obj.toString();
        }
        String zzb = zzgp.zzb(zzaa.zzd());
        if (zzb == null) {
            str2 = zzaa.zzd();
        } else {
            str2 = zzb;
        }
        return new zzat(str2, new zzar(zzf), str, zzaa.zza());
    }

    public static <Builder extends zzlf> Builder zzl(Builder builder, byte[] bArr) throws zzkj {
        zzjl zzb = zzjl.zzb();
        if (zzb != null) {
            return (Builder) builder.zzaw(bArr, zzb);
        }
        return (Builder) builder.zzav(bArr);
    }

    static List<zzfs> zzq(Bundle[] bundleArr) {
        ArrayList arrayList = new ArrayList();
        for (Bundle bundle : bundleArr) {
            if (bundle != null) {
                zzfr zze = zzfs.zze();
                for (String str : bundle.keySet()) {
                    zzfr zze2 = zzfs.zze();
                    zze2.zzj(str);
                    Object obj = bundle.get(str);
                    if (obj instanceof Long) {
                        zze2.zzi(((Long) obj).longValue());
                    } else if (obj instanceof String) {
                        zze2.zzk((String) obj);
                    } else if (obj instanceof Double) {
                        zze2.zzh(((Double) obj).doubleValue());
                    }
                    zze.zzc(zze2);
                }
                if (zze.zza() > 0) {
                    arrayList.add(zze.zzaA());
                }
            }
        }
        return arrayList;
    }

    public static List<Long> zzs(BitSet bitSet) {
        int length = (bitSet.length() + 63) / 64;
        ArrayList arrayList = new ArrayList(length);
        for (int i = 0; i < length; i++) {
            long j = 0;
            for (int i2 = 0; i2 < 64; i2++) {
                int i3 = (i * 64) + i2;
                if (i3 >= bitSet.length()) {
                    break;
                }
                if (bitSet.get(i3)) {
                    j |= 1 << i2;
                }
            }
            arrayList.add(Long.valueOf(j));
        }
        return arrayList;
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0032, code lost:
        r4 = new java.util.ArrayList();
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x003a, code lost:
        if ((r3 instanceof android.os.Parcelable[]) == false) goto L_0x0054;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x003c, code lost:
        r3 = (android.os.Parcelable[]) r3;
        r5 = r3.length;
        r7 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0040, code lost:
        if (r7 >= r5) goto L_0x0082;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0042, code lost:
        r8 = r3[r7];
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0046, code lost:
        if ((r8 instanceof android.os.Bundle) == false) goto L_0x0051;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0048, code lost:
        r4.add(zzt((android.os.Bundle) r8, false));
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0051, code lost:
        r7 = r7 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0056, code lost:
        if ((r3 instanceof java.util.ArrayList) == false) goto L_0x0075;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0058, code lost:
        r3 = (java.util.ArrayList) r3;
        r5 = r3.size();
        r7 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x005f, code lost:
        if (r7 >= r5) goto L_0x0082;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0061, code lost:
        r8 = r3.get(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0067, code lost:
        if ((r8 instanceof android.os.Bundle) == false) goto L_0x0072;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0069, code lost:
        r4.add(zzt((android.os.Bundle) r8, false));
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0072, code lost:
        r7 = r7 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0077, code lost:
        if ((r3 instanceof android.os.Bundle) == false) goto L_0x0082;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0079, code lost:
        r4.add(zzt((android.os.Bundle) r3, false));
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0082, code lost:
        r0.put(r2, r4);
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static Map<String, Object> zzt(Bundle bundle, boolean z) {
        HashMap hashMap = new HashMap();
        Iterator<String> it = bundle.keySet().iterator();
        while (it.hasNext()) {
            String next = it.next();
            Object obj = bundle.get(next);
            if (!(obj instanceof Bundle[]) && !(obj instanceof ArrayList) && !(obj instanceof Bundle)) {
                if (obj != null) {
                    hashMap.put(next, obj);
                }
            }
        }
        return hashMap;
    }

    public static boolean zzw(List<Long> list, int i) {
        if (i >= list.size() * 64) {
            return false;
        }
        return ((1 << (i % 64)) & list.get(i / 64).longValue()) != 0;
    }

    public static boolean zzy(String str) {
        return str != null && str.matches("([+-])?([0-9]+\\.?[0-9]*|[0-9]*\\.?[0-9]+)") && str.length() <= 310;
    }

    @Override // com.google.android.gms.measurement.internal.zzkd
    protected final boolean zzb() {
        return false;
    }

    public final long zzd(byte[] bArr) {
        Preconditions.checkNotNull(bArr);
        this.zzs.zzv().zzg();
        MessageDigest zzE = zzku.zzE(MessageDigestAlgorithms.MD5);
        if (zzE != null) {
            return zzku.zzp(zzE.digest(bArr));
        }
        this.zzs.zzay().zzd().zza("Failed to get MD5");
        return 0;
    }

    public final <T extends Parcelable> T zzh(byte[] bArr, Parcelable.Creator<T> creator) {
        if (bArr == null) {
            return null;
        }
        Parcel obtain = Parcel.obtain();
        try {
            obtain.unmarshall(bArr, 0, bArr.length);
            obtain.setDataPosition(0);
            return creator.createFromParcel(obtain);
        } catch (SafeParcelReader.ParseException e) {
            this.zzs.zzay().zzd().zza("Failed to load parcelable from buffer");
            return null;
        } finally {
            obtain.recycle();
        }
    }

    public final zzfo zzj(zzao zzao) {
        zzfn zze = zzfo.zze();
        zze.zzl(zzao.zze);
        zzaq zzaq = new zzaq(zzao.zzf);
        while (zzaq.hasNext()) {
            String zza = zzaq.next();
            zzfr zze2 = zzfs.zze();
            zze2.zzj(zza);
            Object zzf = zzao.zzf.zzf(zza);
            Preconditions.checkNotNull(zzf);
            zzu(zze2, zzf);
            zze.zze(zze2);
        }
        return zze.zzaA();
    }

    public final String zzm(zzfw zzfw) {
        Long l;
        Long l2;
        Double d;
        if (zzfw == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\nbatch {\n");
        for (zzfy zzfy : zzfw.zzd()) {
            if (zzfy != null) {
                zzG(sb, 1);
                sb.append("bundle {\n");
                if (zzfy.zzbh()) {
                    zzJ(sb, 1, "protocol_version", Integer.valueOf(zzfy.zzd()));
                }
                zzJ(sb, 1, "platform", zzfy.zzK());
                if (zzfy.zzbd()) {
                    zzJ(sb, 1, "gmp_version", Long.valueOf(zzfy.zzn()));
                }
                if (zzfy.zzbn()) {
                    zzJ(sb, 1, "uploading_gmp_version", Long.valueOf(zzfy.zzs()));
                }
                if (zzfy.zzbb()) {
                    zzJ(sb, 1, "dynamite_version", Long.valueOf(zzfy.zzk()));
                }
                if (zzfy.zzaY()) {
                    zzJ(sb, 1, "config_version", Long.valueOf(zzfy.zzi()));
                }
                zzJ(sb, 1, "gmp_app_id", zzfy.zzH());
                zzJ(sb, 1, "admob_app_id", zzfy.zzx());
                zzJ(sb, 1, "app_id", zzfy.zzy());
                zzJ(sb, 1, "app_version", zzfy.zzB());
                if (zzfy.zzaW()) {
                    zzJ(sb, 1, "app_version_major", Integer.valueOf(zzfy.zza()));
                }
                zzJ(sb, 1, "firebase_instance_id", zzfy.zzF());
                if (zzfy.zzba()) {
                    zzJ(sb, 1, "dev_cert_hash", Long.valueOf(zzfy.zzj()));
                }
                zzJ(sb, 1, "app_store", zzfy.zzA());
                if (zzfy.zzbm()) {
                    zzJ(sb, 1, "upload_timestamp_millis", Long.valueOf(zzfy.zzr()));
                }
                if (zzfy.zzbk()) {
                    zzJ(sb, 1, "start_timestamp_millis", Long.valueOf(zzfy.zzq()));
                }
                if (zzfy.zzbc()) {
                    zzJ(sb, 1, "end_timestamp_millis", Long.valueOf(zzfy.zzm()));
                }
                if (zzfy.zzbg()) {
                    zzJ(sb, 1, "previous_bundle_start_timestamp_millis", Long.valueOf(zzfy.zzp()));
                }
                if (zzfy.zzbf()) {
                    zzJ(sb, 1, "previous_bundle_end_timestamp_millis", Long.valueOf(zzfy.zzo()));
                }
                zzJ(sb, 1, "app_instance_id", zzfy.zzz());
                zzJ(sb, 1, "resettable_device_id", zzfy.zzL());
                zzJ(sb, 1, "ds_id", zzfy.zzE());
                if (zzfy.zzbe()) {
                    zzJ(sb, 1, "limited_ad_tracking", Boolean.valueOf(zzfy.zzaT()));
                }
                zzJ(sb, 1, "os_version", zzfy.zzJ());
                zzJ(sb, 1, "device_model", zzfy.zzD());
                zzJ(sb, 1, "user_default_language", zzfy.zzM());
                if (zzfy.zzbl()) {
                    zzJ(sb, 1, "time_zone_offset_minutes", Integer.valueOf(zzfy.zzf()));
                }
                if (zzfy.zzaX()) {
                    zzJ(sb, 1, "bundle_sequential_index", Integer.valueOf(zzfy.zzb()));
                }
                if (zzfy.zzbj()) {
                    zzJ(sb, 1, "service_upload", Boolean.valueOf(zzfy.zzaU()));
                }
                zzJ(sb, 1, "health_monitor", zzfy.zzI());
                if (!this.zzs.zzf().zzs(null, zzdw.zzan) && zzfy.zzaV() && zzfy.zzh() != 0) {
                    zzJ(sb, 1, "android_id", Long.valueOf(zzfy.zzh()));
                }
                if (zzfy.zzbi()) {
                    zzJ(sb, 1, "retry_counter", Integer.valueOf(zzfy.zze()));
                }
                if (zzfy.zzaZ()) {
                    zzJ(sb, 1, "consent_signals", zzfy.zzC());
                }
                List<zzgh> zzP = zzfy.zzP();
                if (zzP != null) {
                    for (zzgh zzgh : zzP) {
                        if (zzgh != null) {
                            zzG(sb, 2);
                            sb.append("user_property {\n");
                            if (zzgh.zzs()) {
                                l = Long.valueOf(zzgh.zzc());
                            } else {
                                l = null;
                            }
                            zzJ(sb, 2, "set_timestamp_millis", l);
                            zzJ(sb, 2, AppMeasurementSdk.ConditionalUserProperty.NAME, this.zzs.zzj().zze(zzgh.zzf()));
                            zzJ(sb, 2, "string_value", zzgh.zzg());
                            if (zzgh.zzr()) {
                                l2 = Long.valueOf(zzgh.zzb());
                            } else {
                                l2 = null;
                            }
                            zzJ(sb, 2, "int_value", l2);
                            if (zzgh.zzq()) {
                                d = Double.valueOf(zzgh.zza());
                            } else {
                                d = null;
                            }
                            zzJ(sb, 2, "double_value", d);
                            zzG(sb, 2);
                            sb.append("}\n");
                        }
                    }
                }
                List<zzfk> zzN = zzfy.zzN();
                if (zzN != null) {
                    for (zzfk zzfk : zzN) {
                        if (zzfk != null) {
                            zzG(sb, 2);
                            sb.append("audience_membership {\n");
                            if (zzfk.zzk()) {
                                zzJ(sb, 2, "audience_id", Integer.valueOf(zzfk.zza()));
                            }
                            if (zzfk.zzm()) {
                                zzJ(sb, 2, "new_audience", Boolean.valueOf(zzfk.zzj()));
                            }
                            zzI(sb, 2, "current_data", zzfk.zzd());
                            if (zzfk.zzn()) {
                                zzI(sb, 2, "previous_data", zzfk.zze());
                            }
                            zzG(sb, 2);
                            sb.append("}\n");
                        }
                    }
                }
                List<zzfo> zzO = zzfy.zzO();
                if (zzO != null) {
                    for (zzfo zzfo : zzO) {
                        if (zzfo != null) {
                            zzG(sb, 2);
                            sb.append("event {\n");
                            zzJ(sb, 2, AppMeasurementSdk.ConditionalUserProperty.NAME, this.zzs.zzj().zzc(zzfo.zzh()));
                            if (zzfo.zzu()) {
                                zzJ(sb, 2, "timestamp_millis", Long.valueOf(zzfo.zzd()));
                            }
                            if (zzfo.zzt()) {
                                zzJ(sb, 2, "previous_timestamp_millis", Long.valueOf(zzfo.zzc()));
                            }
                            if (zzfo.zzs()) {
                                zzJ(sb, 2, "count", Integer.valueOf(zzfo.zza()));
                            }
                            if (zzfo.zzb() != 0) {
                                zzE(sb, 2, zzfo.zzi());
                            }
                            zzG(sb, 2);
                            sb.append("}\n");
                        }
                    }
                }
                zzG(sb, 1);
                sb.append("}\n");
            }
        }
        sb.append("}\n");
        return sb.toString();
    }

    public final String zzo(zzej zzej) {
        if (zzej == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\nevent_filter {\n");
        if (zzej.zzp()) {
            zzJ(sb, 0, "filter_id", Integer.valueOf(zzej.zzb()));
        }
        zzJ(sb, 0, "event_name", this.zzs.zzj().zzc(zzej.zzg()));
        String zzH = zzH(zzej.zzk(), zzej.zzm(), zzej.zzn());
        if (!zzH.isEmpty()) {
            zzJ(sb, 0, "filter_type", zzH);
        }
        if (zzej.zzo()) {
            zzK(sb, 1, "event_count_filter", zzej.zzf());
        }
        if (zzej.zza() > 0) {
            sb.append("  filters {\n");
            for (zzel zzel : zzej.zzh()) {
                zzF(sb, 2, zzel);
            }
        }
        zzG(sb, 1);
        sb.append("}\n}\n");
        return sb.toString();
    }

    public final String zzp(zzes zzes) {
        if (zzes == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\nproperty_filter {\n");
        if (zzes.zzj()) {
            zzJ(sb, 0, "filter_id", Integer.valueOf(zzes.zza()));
        }
        zzJ(sb, 0, "property_name", this.zzs.zzj().zze(zzes.zze()));
        String zzH = zzH(zzes.zzg(), zzes.zzh(), zzes.zzi());
        if (!zzH.isEmpty()) {
            zzJ(sb, 0, "filter_type", zzH);
        }
        zzF(sb, 1, zzes.zzb());
        sb.append("}\n");
        return sb.toString();
    }

    public final List<Long> zzr(List<Long> list, List<Integer> list2) {
        ArrayList arrayList = new ArrayList(list);
        for (Integer num : list2) {
            if (num.intValue() < 0) {
                this.zzs.zzay().zzk().zzb("Ignoring negative bit index to be cleared", num);
            } else {
                int intValue = num.intValue() / 64;
                if (intValue >= arrayList.size()) {
                    this.zzs.zzay().zzk().zzc("Ignoring bit index greater than bitSet size", num, Integer.valueOf(arrayList.size()));
                } else {
                    arrayList.set(intValue, Long.valueOf(((Long) arrayList.get(intValue)).longValue() & (~(1 << (num.intValue() % 64)))));
                }
            }
        }
        int size = arrayList.size();
        int size2 = arrayList.size() - 1;
        int i = size;
        while (size2 >= 0 && ((Long) arrayList.get(size2)).longValue() == 0) {
            size2--;
            i = size2;
        }
        return arrayList.subList(0, i);
    }

    public final void zzu(zzfr zzfr, Object obj) {
        Preconditions.checkNotNull(obj);
        zzfr.zzg();
        zzfr.zze();
        zzfr.zzd();
        zzfr.zzf();
        if (obj instanceof String) {
            zzfr.zzk((String) obj);
        } else if (obj instanceof Long) {
            zzfr.zzi(((Long) obj).longValue());
        } else if (obj instanceof Double) {
            zzfr.zzh(((Double) obj).doubleValue());
        } else if (obj instanceof Bundle[]) {
            zzfr.zzb(zzq((Bundle[]) obj));
        } else {
            this.zzs.zzay().zzd().zzb("Ignoring invalid (type) event param value", obj);
        }
    }

    public final void zzv(zzgg zzgg, Object obj) {
        Preconditions.checkNotNull(obj);
        zzgg.zzc();
        zzgg.zzb();
        zzgg.zza();
        if (obj instanceof String) {
            zzgg.zzh((String) obj);
        } else if (obj instanceof Long) {
            zzgg.zze(((Long) obj).longValue());
        } else if (obj instanceof Double) {
            zzgg.zzd(((Double) obj).doubleValue());
        } else {
            this.zzs.zzay().zzd().zzb("Ignoring invalid (type) user attribute value", obj);
        }
    }

    public final boolean zzx(long j, long j2) {
        return j == 0 || j2 <= 0 || Math.abs(this.zzs.zzav().currentTimeMillis() - j) > j2;
    }

    public final byte[] zzz(byte[] bArr) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(bArr);
            gZIPOutputStream.close();
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            this.zzs.zzay().zzd().zzb("Failed to gzip content", e);
            throw e;
        }
    }
}
