package com.google.android.gms.measurement.internal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import androidx.collection.ArrayMap;
import com.facebook.common.util.UriUtil;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzei;
import com.google.android.gms.internal.measurement.zzej;
import com.google.android.gms.internal.measurement.zzer;
import com.google.android.gms.internal.measurement.zzes;
import com.google.android.gms.internal.measurement.zzfk;
import com.google.android.gms.internal.measurement.zzfm;
import com.google.android.gms.internal.measurement.zzfo;
import com.google.android.gms.internal.measurement.zzgc;
import com.google.android.gms.internal.measurement.zzgd;
import com.google.android.gms.internal.measurement.zzgf;
import com.google.android.gms.internal.measurement.zzgh;
import com.google.android.gms.internal.measurement.zzoe;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzz extends zzkd {
    private String zza;
    private Set<Integer> zzb;
    private Map<Integer, zzt> zzc;
    private Long zzd;
    private Long zze;

    public zzz(zzkn zzkn) {
        super(zzkn);
    }

    private final zzt zzd(Integer num) {
        if (this.zzc.containsKey(num)) {
            return this.zzc.get(num);
        }
        zzt zzt = new zzt(this, this.zza, null);
        this.zzc.put(num, zzt);
        return zzt;
    }

    private final boolean zzf(int i, int i2) {
        zzt zzt = this.zzc.get(Integer.valueOf(i));
        if (zzt == null) {
            return false;
        }
        return zzt.zze.get(i2);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(24:2|(2:3|(2:5|(2:475|7))(2:474|8))|9|(3:11|436|12)|15|(6:18|464|19|468|20|(19:(7:22|430|23|24|(1:26)(3:27|(1:29)(1:30)|31)|34|(1:476)(1:39))|(1:37)|38|56|469|57|442|58|(3:60|(1:62)|63)(4:64|(6:65|422|66|67|74|(1:477)(1:79))|(1:77)|78)|95|(1:97)(6:98|(3:100|(6:102|472|103|471|104|(2:(3:106|(1:108)|109)|478)(1:113))|149)(1:154)|155|(9:158|(3:162|(4:165|(5:503|167|(1:169)(1:170)|171|506)(1:505)|504|163)|502)|172|(2:174|(1:176)(4:177|(4:180|(2:185|513)(3:510|184|512)|511|178)|508|186))(1:187)|(4:189|(6:192|(2:194|(3:196|516|199))(1:197)|198|515|199|190)|514|200)(1:201)|202|(3:211|(8:214|(1:216)|217|(1:219)|220|(3:517|222|520)(1:519)|518|212)|500)|223|156)|495|224)|225|(1:227)(4:228|(4:231|(10:523|233|(1:235)(1:236)|237|(10:239|455|240|241|459|242|440|243|(3:(11:245|434|246|461|247|248|249|(3:251|424|252)(1:253)|254|259|(1:527)(1:264))|(1:262)|263)(2:267|268)|287)(1:292)|293|(4:296|(3:531|298|534)(4:529|299|(2:300|(2:302|(1:304)(2:537|305))(2:538|306))|(3:532|308|535)(3:530|309|536))|533|294)|528|310|525)(3:522|311|526)|524|229)|521|312)|313|(1:315)(4:316|(6:319|(7:321|466|322|420|323|(3:(9:325|462|326|327|328|(1:330)(1:331)|332|337|(1:545)(1:342))|(1:340)|341)(2:343|344)|358)(1:363)|364|(2:365|(2:367|(3:541|369|543)(3:370|(2:371|(4:373|(3:375|(1:377)(1:378)|379)(1:380)|381|(1:1)(2:385|(1:387)(2:553|388)))(2:554|394))|(4:549|396|551|550)(4:546|397|552|550)))(3:540|398|544))|542|317)|539|399)|400|(10:403|438|404|405|432|406|558|(4:557|408|409|560)(3:555|410|561)|559|401)|556|415)(1:40))|55|56|469|57|442|58|(0)(0)|95|(0)(0)|225|(0)(0)|313|(0)(0)|400|(1:401)|556|415|(9:(1:429)|(1:458)|(0)|(1:452)|(1:446)|(0)|(0)|(0)|(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x02f0, code lost:
        if (r5 != null) goto L_0x02f2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x02f2, code lost:
        r5.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x02fb, code lost:
        if (r5 != null) goto L_0x02f2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x0320, code lost:
        if (r5 == null) goto L_0x0323;
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x0323, code lost:
        r1 = r13.keySet().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x032f, code lost:
        if (r1.hasNext() == false) goto L_0x03f6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:126:0x0331, code lost:
        r4 = ((java.lang.Integer) r1.next()).intValue();
        r5 = java.lang.Integer.valueOf(r4);
        r6 = (com.google.android.gms.internal.measurement.zzgd) r13.get(r5);
        r7 = (java.util.List) r0.get(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:127:0x034b, code lost:
        if (r7 == null) goto L_0x03ec;
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x0351, code lost:
        if (r7.isEmpty() == false) goto L_0x0359;
     */
    /* JADX WARN: Code restructure failed: missing block: B:130:0x0359, code lost:
        r0 = r65.zzf.zzu().zzr(r6.zzk(), r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x036d, code lost:
        if (r0.isEmpty() != false) goto L_0x03e2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:132:0x036f, code lost:
        r5 = r6.zzbv();
        r5.zze();
        r5.zzb(r0);
        r0 = r65.zzf.zzu().zzr(r6.zzn(), r7);
        r5.zzf();
        r5.zzd(r0);
        r0 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x0396, code lost:
        if (r0 >= r6.zza()) goto L_0x03b0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x03a8, code lost:
        if (r7.contains(java.lang.Integer.valueOf(r6.zze(r0).zza())) == false) goto L_0x03ad;
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x03aa, code lost:
        r5.zzg(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x03ad, code lost:
        r0 = r0 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x03b0, code lost:
        r0 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x03b5, code lost:
        if (r0 >= r6.zzc()) goto L_0x03cf;
     */
    /* JADX WARN: Code restructure failed: missing block: B:143:0x03c7, code lost:
        if (r7.contains(java.lang.Integer.valueOf(r6.zzi(r0).zzb())) == false) goto L_0x03cc;
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x03c9, code lost:
        r5.zzh(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x03cc, code lost:
        r0 = r0 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x03cf, code lost:
        r3.put(java.lang.Integer.valueOf(r4), r5.zzaA());
        r0 = r0;
        r1 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:147:0x03e2, code lost:
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x03ec, code lost:
        r3.put(r5, r6);
        r0 = r0;
        r1 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:269:0x0767, code lost:
        if (r11 != null) goto L_0x0769;
     */
    /* JADX WARN: Code restructure failed: missing block: B:270:0x0769, code lost:
        r11.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x07a3, code lost:
        if (r11 != null) goto L_0x0769;
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x094e, code lost:
        if (r13 != null) goto L_0x0950;
     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x0950, code lost:
        r13.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x097a, code lost:
        if (r13 == null) goto L_0x097d;
     */
    /* JADX WARN: Code restructure failed: missing block: B:389:0x0a7f, code lost:
        r0 = r65.zzs.zzay().zzk();
        r6 = com.google.android.gms.measurement.internal.zzei.zzn(r65.zza);
     */
    /* JADX WARN: Code restructure failed: missing block: B:390:0x0a93, code lost:
        if (r7.zzj() == false) goto L_0x0a9e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:391:0x0a95, code lost:
        r7 = java.lang.Integer.valueOf(r7.zza());
     */
    /* JADX WARN: Code restructure failed: missing block: B:392:0x0a9e, code lost:
        r7 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:393:0x0a9f, code lost:
        r0.zzc("Invalid property filter ID. appId, id", r6, java.lang.String.valueOf(r7));
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0155, code lost:
        if (r5 != null) goto L_0x0157;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0157, code lost:
        r5.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x017b, code lost:
        if (r5 == null) goto L_0x0185;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x0233, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x0234, code lost:
        r18 = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0239, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x023b, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x023c, code lost:
        r18 = r6;
        r19 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0241, code lost:
        r5 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x0244, code lost:
        r5 = null;
     */
    /* JADX WARN: Removed duplicated region for block: B:152:0x03fb  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x05d3  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x05d7  */
    /* JADX WARN: Removed duplicated region for block: B:290:0x07ad  */
    /* JADX WARN: Removed duplicated region for block: B:315:0x087e  */
    /* JADX WARN: Removed duplicated region for block: B:316:0x0882  */
    /* JADX WARN: Removed duplicated region for block: B:361:0x0984  */
    /* JADX WARN: Removed duplicated region for block: B:403:0x0ae2  */
    /* JADX WARN: Removed duplicated region for block: B:418:0x0b7a  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0181  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x01bb A[Catch: SQLiteException -> 0x0233, all -> 0x0230, TRY_LEAVE, TryCatch #14 {all -> 0x0230, blocks: (B:58:0x01b5, B:60:0x01bb, B:64:0x01cb, B:65:0x01d0, B:66:0x01db, B:67:0x01eb, B:69:0x01fa, B:71:0x020a, B:73:0x0210, B:74:0x0217), top: B:442:0x01b5 }] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x01cb A[Catch: SQLiteException -> 0x0233, all -> 0x0230, TRY_ENTER, TryCatch #14 {all -> 0x0230, blocks: (B:58:0x01b5, B:60:0x01bb, B:64:0x01cb, B:65:0x01d0, B:66:0x01db, B:67:0x01eb, B:69:0x01fa, B:71:0x020a, B:73:0x0210, B:74:0x0217), top: B:442:0x01b5 }] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x025e  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0269  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0273  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final List<zzfk> zza(String str, List<zzfo> list, List<zzgh> list2, Long l, Long l2) {
        int i;
        int i2;
        boolean z;
        ArrayMap arrayMap;
        String str2;
        String str3;
        Cursor cursor;
        SQLiteException e;
        ArrayMap arrayMap2;
        String str4;
        String str5;
        String str6;
        zzu zzu;
        String str7;
        String str8;
        String str9;
        SQLiteException e2;
        Iterator<zzgh> it;
        String str10;
        String str11;
        Map map;
        Cursor cursor2;
        SQLiteException e3;
        List list3;
        zzap zzap;
        Iterator<zzfo> it2;
        String str12;
        zzv zzv;
        String str13;
        Map map2;
        Iterator it3;
        Cursor cursor3;
        SQLiteException e4;
        ArrayMap arrayMap3;
        List list4;
        ArrayMap arrayMap4;
        Map map3;
        String str14;
        String str15;
        List<zzej> list5;
        String str16;
        String str17;
        Long l3;
        Cursor cursor4;
        SQLiteException e5;
        ArrayMap arrayMap5;
        Cursor cursor5;
        SQLiteException e6;
        List list6;
        String str18 = "current_results";
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(list);
        Preconditions.checkNotNull(list2);
        this.zza = str;
        this.zzb = new HashSet();
        this.zzc = new ArrayMap();
        this.zzd = l;
        this.zze = l2;
        Iterator<zzfo> it4 = list.iterator();
        while (true) {
            i = 0;
            i2 = 1;
            if (it4.hasNext()) {
                if ("_s".equals(it4.next().zzh())) {
                    z = true;
                    break;
                }
            } else {
                z = false;
                break;
            }
        }
        zzoe.zzc();
        boolean zzs = this.zzs.zzf().zzs(this.zza, zzdw.zzY);
        zzoe.zzc();
        boolean zzs2 = this.zzs.zzf().zzs(this.zza, zzdw.zzX);
        if (z) {
            zzaj zzi = this.zzf.zzi();
            String str19 = this.zza;
            zzi.zzY();
            zzi.zzg();
            Preconditions.checkNotEmpty(str19);
            ContentValues contentValues = new ContentValues();
            contentValues.put("current_session_count", (Integer) 0);
            try {
                zzi.zzh().update("events", contentValues, "app_id = ?", new String[]{str19});
            } catch (SQLiteException e7) {
                zzi.zzs.zzay().zzd().zzc("Error resetting session-scoped event counts. appId", zzei.zzn(str19), e7);
            }
        }
        Map emptyMap = Collections.emptyMap();
        String str20 = "Failed to merge filter. appId";
        String str21 = "Database error querying filters. appId";
        String str22 = UriUtil.DATA_SCHEME;
        String str23 = "audience_id";
        try {
            if (zzs2 && zzs) {
                zzaj zzi2 = this.zzf.zzi();
                String str24 = this.zza;
                Preconditions.checkNotEmpty(str24);
                ArrayMap arrayMap6 = new ArrayMap();
                try {
                    cursor5 = zzi2.zzh().query("event_filters", new String[]{str23, str22}, "app_id=?", new String[]{str24}, null, null, null);
                } catch (SQLiteException e8) {
                    e6 = e8;
                    cursor5 = null;
                } catch (Throwable th) {
                    th = th;
                    cursor5 = null;
                }
                try {
                } catch (SQLiteException e9) {
                    e6 = e9;
                    try {
                        zzi2.zzs.zzay().zzd().zzc(str21, zzei.zzn(str24), e6);
                        emptyMap = Collections.emptyMap();
                    } catch (Throwable th2) {
                        th = th2;
                        if (cursor5 != null) {
                            cursor5.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (cursor5 != null) {
                    }
                    throw th;
                }
                if (cursor5.moveToFirst()) {
                    while (true) {
                        try {
                            zzej zzaA = ((zzei) zzkp.zzl(zzej.zzc(), cursor5.getBlob(i2))).zzaA();
                            if (zzaA.zzo()) {
                                Integer valueOf = Integer.valueOf(cursor5.getInt(i));
                                List list7 = (List) arrayMap6.get(valueOf);
                                if (list7 == null) {
                                    list6 = new ArrayList();
                                    arrayMap6.put(valueOf, list6);
                                } else {
                                    list6 = list7;
                                }
                                list6.add(zzaA);
                            }
                        } catch (IOException e10) {
                            zzi2.zzs.zzay().zzd().zzc(str20, zzei.zzn(str24), e10);
                        }
                        if (!cursor5.moveToNext()) {
                            break;
                        }
                        i = 0;
                        i2 = 1;
                    }
                    if (cursor5 != null) {
                        cursor5.close();
                    }
                    arrayMap = arrayMap6;
                    zzaj zzi3 = this.zzf.zzi();
                    String str25 = this.zza;
                    zzi3.zzY();
                    zzi3.zzg();
                    Preconditions.checkNotEmpty(str25);
                    cursor = zzi3.zzh().query("audience_filter_values", new String[]{str23, str18}, "app_id=?", new String[]{str25}, null, null, null);
                    if (cursor.moveToFirst()) {
                        Map emptyMap2 = Collections.emptyMap();
                        if (cursor != null) {
                            cursor.close();
                        }
                        arrayMap2 = emptyMap2;
                        str3 = str23;
                        str2 = str22;
                    } else {
                        ArrayMap arrayMap7 = new ArrayMap();
                        while (true) {
                            int i3 = cursor.getInt(0);
                            try {
                                arrayMap7.put(Integer.valueOf(i3), ((zzgc) zzkp.zzl(zzgd.zzf(), cursor.getBlob(1))).zzaA());
                                arrayMap5 = arrayMap7;
                                str3 = str23;
                                str2 = str22;
                            } catch (IOException e11) {
                                arrayMap5 = arrayMap7;
                                str3 = str23;
                                try {
                                    str2 = str22;
                                    try {
                                        zzi3.zzs.zzay().zzd().zzd("Failed to merge filter results. appId, audienceId, error", zzei.zzn(str25), Integer.valueOf(i3), e11);
                                    } catch (SQLiteException e12) {
                                        e = e12;
                                        try {
                                            zzi3.zzs.zzay().zzd().zzc("Database error querying filter results. appId", zzei.zzn(str25), e);
                                            Map emptyMap3 = Collections.emptyMap();
                                            if (cursor != null) {
                                                cursor.close();
                                            }
                                            arrayMap2 = emptyMap3;
                                            if (!arrayMap2.isEmpty()) {
                                            }
                                            if (!list.isEmpty()) {
                                            }
                                            if (!list2.isEmpty()) {
                                            }
                                            ArrayList arrayList = new ArrayList();
                                            Set<Integer> keySet = this.zzc.keySet();
                                            keySet.removeAll(this.zzb);
                                            while (r2.hasNext()) {
                                            }
                                            return arrayList;
                                        } catch (Throwable th4) {
                                            th = th4;
                                            if (cursor != null) {
                                            }
                                            throw th;
                                        }
                                    }
                                } catch (SQLiteException e13) {
                                    e = e13;
                                    str2 = str22;
                                    zzi3.zzs.zzay().zzd().zzc("Database error querying filter results. appId", zzei.zzn(str25), e);
                                    Map emptyMap32 = Collections.emptyMap();
                                    if (cursor != null) {
                                    }
                                    arrayMap2 = emptyMap32;
                                    if (!arrayMap2.isEmpty()) {
                                    }
                                    if (!list.isEmpty()) {
                                    }
                                    if (!list2.isEmpty()) {
                                    }
                                    ArrayList arrayList2 = new ArrayList();
                                    Set<Integer> keySet2 = this.zzc.keySet();
                                    keySet2.removeAll(this.zzb);
                                    while (r2.hasNext()) {
                                    }
                                    return arrayList2;
                                }
                            }
                            if (!cursor.moveToNext()) {
                                break;
                            }
                            arrayMap7 = arrayMap5;
                            str23 = str3;
                            str22 = str2;
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                        arrayMap2 = arrayMap5;
                    }
                    if (!arrayMap2.isEmpty()) {
                        str7 = str21;
                        str4 = str20;
                        str6 = str3;
                        str5 = str2;
                        zzu = null;
                    } else {
                        HashSet<Integer> hashSet = new HashSet(arrayMap2.keySet());
                        if (z) {
                            String str26 = this.zza;
                            Preconditions.checkNotEmpty(str26);
                            Preconditions.checkNotNull(arrayMap2);
                            ArrayMap arrayMap8 = new ArrayMap();
                            if (!arrayMap2.isEmpty()) {
                                zzaj zzi4 = this.zzf.zzi();
                                zzi4.zzY();
                                zzi4.zzg();
                                Preconditions.checkNotEmpty(str26);
                                Map arrayMap9 = new ArrayMap();
                                try {
                                    cursor4 = zzi4.zzh().rawQuery("select audience_id, filter_id from event_filters where app_id = ? and session_scoped = 1 UNION select audience_id, filter_id from property_filters where app_id = ? and session_scoped = 1;", new String[]{str26, str26});
                                    try {
                                        if (cursor4.moveToFirst()) {
                                            do {
                                                Integer valueOf2 = Integer.valueOf(cursor4.getInt(0));
                                                List list8 = (List) arrayMap9.get(valueOf2);
                                                if (list8 == null) {
                                                    list8 = new ArrayList();
                                                    arrayMap9.put(valueOf2, list8);
                                                }
                                                list8.add(Integer.valueOf(cursor4.getInt(1)));
                                            } while (cursor4.moveToNext());
                                        } else {
                                            arrayMap9 = Collections.emptyMap();
                                        }
                                    } catch (SQLiteException e14) {
                                        e5 = e14;
                                        try {
                                            zzi4.zzs.zzay().zzd().zzc("Database error querying scoped filters. appId", zzei.zzn(str26), e5);
                                            arrayMap9 = Collections.emptyMap();
                                        } catch (Throwable th5) {
                                            th = th5;
                                            if (cursor4 != null) {
                                                cursor4.close();
                                            }
                                            throw th;
                                        }
                                    } catch (Throwable th6) {
                                        th = th6;
                                        if (cursor4 != null) {
                                        }
                                        throw th;
                                    }
                                } catch (SQLiteException e15) {
                                    e5 = e15;
                                    cursor4 = null;
                                } catch (Throwable th7) {
                                    th = th7;
                                    cursor4 = null;
                                }
                            }
                            arrayMap4 = arrayMap8;
                        } else {
                            arrayMap4 = arrayMap2;
                        }
                        for (Integer num : hashSet) {
                            int intValue = num.intValue();
                            zzgd zzgd = (zzgd) arrayMap4.get(Integer.valueOf(intValue));
                            BitSet bitSet = new BitSet();
                            BitSet bitSet2 = new BitSet();
                            ArrayMap arrayMap10 = new ArrayMap();
                            if (!(zzgd == null || zzgd.zza() == 0)) {
                                for (zzfm zzfm : zzgd.zzj()) {
                                    if (zzfm.zzh()) {
                                        Integer valueOf3 = Integer.valueOf(zzfm.zza());
                                        if (zzfm.zzg()) {
                                            l3 = Long.valueOf(zzfm.zzb());
                                        } else {
                                            l3 = null;
                                        }
                                        arrayMap10.put(valueOf3, l3);
                                    }
                                }
                            }
                            ArrayMap arrayMap11 = new ArrayMap();
                            if (zzgd == null) {
                                map3 = arrayMap4;
                            } else if (zzgd.zzc() == 0) {
                                map3 = arrayMap4;
                            } else {
                                Iterator<zzgf> it5 = zzgd.zzm().iterator();
                                while (it5.hasNext()) {
                                    zzgf next = it5.next();
                                    if (next.zzi() && next.zza() > 0) {
                                        arrayMap11.put(Integer.valueOf(next.zzb()), Long.valueOf(next.zzc(next.zza() - 1)));
                                        arrayMap4 = arrayMap4;
                                        it5 = it5;
                                    }
                                }
                                map3 = arrayMap4;
                            }
                            if (zzgd != null) {
                                int i4 = 0;
                                while (i4 < zzgd.zzd() * 64) {
                                    if (zzkp.zzw(zzgd.zzn(), i4)) {
                                        str17 = str21;
                                        str16 = str20;
                                        this.zzs.zzay().zzj().zzc("Filter already evaluated. audience ID, filter ID", Integer.valueOf(intValue), Integer.valueOf(i4));
                                        bitSet2.set(i4);
                                        if (zzkp.zzw(zzgd.zzk(), i4)) {
                                            bitSet.set(i4);
                                            i4++;
                                            str21 = str17;
                                            str20 = str16;
                                        }
                                    } else {
                                        str17 = str21;
                                        str16 = str20;
                                    }
                                    arrayMap10.remove(Integer.valueOf(i4));
                                    i4++;
                                    str21 = str17;
                                    str20 = str16;
                                }
                                str15 = str21;
                                str14 = str20;
                            } else {
                                str15 = str21;
                                str14 = str20;
                            }
                            Integer valueOf4 = Integer.valueOf(intValue);
                            zzgd zzgd2 = (zzgd) arrayMap2.get(valueOf4);
                            if (!(!zzs2 || !zzs || (list5 = (List) arrayMap.get(valueOf4)) == null || this.zze == null || this.zzd == null)) {
                                for (zzej zzej : list5) {
                                    int zzb = zzej.zzb();
                                    long longValue = this.zze.longValue() / 1000;
                                    if (zzej.zzm()) {
                                        longValue = this.zzd.longValue() / 1000;
                                    }
                                    Integer valueOf5 = Integer.valueOf(zzb);
                                    if (arrayMap10.containsKey(valueOf5)) {
                                        arrayMap10.put(valueOf5, Long.valueOf(longValue));
                                    }
                                    if (arrayMap11.containsKey(valueOf5)) {
                                        arrayMap11.put(valueOf5, Long.valueOf(longValue));
                                    }
                                }
                            }
                            this.zzc.put(Integer.valueOf(intValue), new zzt(this, this.zza, zzgd2, bitSet, bitSet2, arrayMap10, arrayMap11, null));
                            arrayMap2 = arrayMap2;
                            str21 = str15;
                            arrayMap = arrayMap;
                            arrayMap4 = map3;
                            str20 = str14;
                        }
                        str7 = str21;
                        str4 = str20;
                        str6 = str3;
                        str5 = str2;
                        zzu = null;
                    }
                    if (!list.isEmpty()) {
                        str8 = str18;
                    } else {
                        zzv zzv2 = new zzv(this, zzu);
                        ArrayMap arrayMap12 = new ArrayMap();
                        Iterator<zzfo> it6 = list.iterator();
                        while (it6.hasNext()) {
                            zzfo next2 = it6.next();
                            zzfo zza = zzv2.zza(this.zza, next2);
                            if (zza != null) {
                                zzaj zzi5 = this.zzf.zzi();
                                String str27 = this.zza;
                                String zzh = zza.zzh();
                                zzap zzn = zzi5.zzn(str27, next2.zzh());
                                if (zzn == null) {
                                    zzi5.zzs.zzay().zzk().zzc("Event aggregate wasn't created during raw event logging. appId, event", zzei.zzn(str27), zzi5.zzs.zzj().zzc(zzh));
                                    zzap = new zzap(str27, next2.zzh(), 1, 1, 1, next2.zzd(), 0, null, null, null, null);
                                } else {
                                    zzap = new zzap(zzn.zza, zzn.zzb, zzn.zzc + 1, zzn.zzd + 1, zzn.zze + 1, zzn.zzf, zzn.zzg, zzn.zzh, zzn.zzi, zzn.zzj, zzn.zzk);
                                }
                                this.zzf.zzi().zzE(zzap);
                                long j = zzap.zzc;
                                String zzh2 = zza.zzh();
                                Map map4 = (Map) arrayMap12.get(zzh2);
                                if (map4 == null) {
                                    zzaj zzi6 = this.zzf.zzi();
                                    String str28 = this.zza;
                                    zzi6.zzY();
                                    zzi6.zzg();
                                    Preconditions.checkNotEmpty(str28);
                                    Preconditions.checkNotEmpty(zzh2);
                                    ArrayMap arrayMap13 = new ArrayMap();
                                    zzv = zzv2;
                                    it2 = it6;
                                    String str29 = str6;
                                    String str30 = str5;
                                    try {
                                        try {
                                            str12 = str18;
                                            try {
                                                cursor3 = zzi6.zzh().query("event_filters", new String[]{str29, str30}, "app_id=? AND event_name=?", new String[]{str28, zzh2}, null, null, null);
                                                try {
                                                    try {
                                                        if (cursor3.moveToFirst()) {
                                                            while (true) {
                                                                str5 = str30;
                                                                try {
                                                                    try {
                                                                        zzej zzaA2 = ((zzei) zzkp.zzl(zzej.zzc(), cursor3.getBlob(1))).zzaA();
                                                                        Integer valueOf6 = Integer.valueOf(cursor3.getInt(0));
                                                                        List list9 = (List) arrayMap13.get(valueOf6);
                                                                        if (list9 == null) {
                                                                            str6 = str29;
                                                                            try {
                                                                                list4 = new ArrayList();
                                                                                arrayMap13.put(valueOf6, list4);
                                                                            } catch (SQLiteException e16) {
                                                                                e4 = e16;
                                                                                str13 = str4;
                                                                                try {
                                                                                    zzi6.zzs.zzay().zzd().zzc(str7, zzei.zzn(str28), e4);
                                                                                    map4 = Collections.emptyMap();
                                                                                } catch (Throwable th8) {
                                                                                    th = th8;
                                                                                    if (cursor3 != null) {
                                                                                        cursor3.close();
                                                                                    }
                                                                                    throw th;
                                                                                }
                                                                            }
                                                                        } else {
                                                                            str6 = str29;
                                                                            list4 = list9;
                                                                        }
                                                                        list4.add(zzaA2);
                                                                        arrayMap3 = arrayMap13;
                                                                        str13 = str4;
                                                                    } catch (IOException e17) {
                                                                        str6 = str29;
                                                                        arrayMap3 = arrayMap13;
                                                                        str13 = str4;
                                                                        try {
                                                                            zzi6.zzs.zzay().zzd().zzc(str13, zzei.zzn(str28), e17);
                                                                        } catch (SQLiteException e18) {
                                                                            e4 = e18;
                                                                            zzi6.zzs.zzay().zzd().zzc(str7, zzei.zzn(str28), e4);
                                                                            map4 = Collections.emptyMap();
                                                                        }
                                                                    }
                                                                    if (!cursor3.moveToNext()) {
                                                                        break;
                                                                    }
                                                                    str4 = str13;
                                                                    arrayMap13 = arrayMap3;
                                                                    str29 = str6;
                                                                    str30 = str5;
                                                                } catch (SQLiteException e19) {
                                                                    e4 = e19;
                                                                    str6 = str29;
                                                                    str13 = str4;
                                                                    zzi6.zzs.zzay().zzd().zzc(str7, zzei.zzn(str28), e4);
                                                                    map4 = Collections.emptyMap();
                                                                }
                                                            }
                                                            if (cursor3 != null) {
                                                                cursor3.close();
                                                            }
                                                            map4 = arrayMap3;
                                                        } else {
                                                            str5 = str30;
                                                            str6 = str29;
                                                            str13 = str4;
                                                            map4 = Collections.emptyMap();
                                                        }
                                                    } catch (Throwable th9) {
                                                        th = th9;
                                                        if (cursor3 != null) {
                                                        }
                                                        throw th;
                                                    }
                                                } catch (SQLiteException e20) {
                                                    e4 = e20;
                                                    str5 = str30;
                                                }
                                            } catch (SQLiteException e21) {
                                                e4 = e21;
                                                str5 = str30;
                                                str6 = str29;
                                                str13 = str4;
                                                cursor3 = null;
                                                zzi6.zzs.zzay().zzd().zzc(str7, zzei.zzn(str28), e4);
                                                map4 = Collections.emptyMap();
                                            }
                                        } catch (Throwable th10) {
                                            th = th10;
                                            cursor3 = null;
                                        }
                                    } catch (SQLiteException e22) {
                                        e4 = e22;
                                        str5 = str30;
                                        str6 = str29;
                                        str12 = str18;
                                    }
                                    arrayMap12.put(zzh2, map4);
                                } else {
                                    zzv = zzv2;
                                    it2 = it6;
                                    str12 = str18;
                                    str13 = str4;
                                }
                                Iterator it7 = map4.keySet().iterator();
                                while (it7.hasNext()) {
                                    int intValue2 = ((Integer) it7.next()).intValue();
                                    Set<Integer> set = this.zzb;
                                    Integer valueOf7 = Integer.valueOf(intValue2);
                                    if (set.contains(valueOf7)) {
                                        this.zzs.zzay().zzj().zzb("Skipping failed audience ID", valueOf7);
                                    } else {
                                        Iterator it8 = ((List) map4.get(valueOf7)).iterator();
                                        boolean z2 = true;
                                        while (true) {
                                            if (!it8.hasNext()) {
                                                map2 = map4;
                                                it3 = it7;
                                                break;
                                            }
                                            zzej zzej2 = (zzej) it8.next();
                                            zzw zzw = new zzw(this, this.zza, intValue2, zzej2);
                                            map2 = map4;
                                            it3 = it7;
                                            z2 = zzw.zzd(this.zzd, this.zze, zza, j, zzap, zzf(intValue2, zzej2.zzb()));
                                            if (!z2) {
                                                this.zzb.add(Integer.valueOf(intValue2));
                                                break;
                                            }
                                            zzd(Integer.valueOf(intValue2)).zzc(zzw);
                                            map4 = map2;
                                            it7 = it3;
                                        }
                                        if (!z2) {
                                            this.zzb.add(Integer.valueOf(intValue2));
                                            map4 = map2;
                                            it7 = it3;
                                        } else {
                                            map4 = map2;
                                            it7 = it3;
                                        }
                                    }
                                }
                                it6 = it2;
                                str4 = str13;
                                zzv2 = zzv;
                                str18 = str12;
                            }
                        }
                        str8 = str18;
                    }
                    if (!list2.isEmpty()) {
                        str9 = str6;
                    } else {
                        ArrayMap arrayMap14 = new ArrayMap();
                        Iterator<zzgh> it9 = list2.iterator();
                        while (it9.hasNext()) {
                            zzgh next3 = it9.next();
                            String zzf = next3.zzf();
                            Map map5 = (Map) arrayMap14.get(zzf);
                            if (map5 == null) {
                                zzaj zzi7 = this.zzf.zzi();
                                String str31 = this.zza;
                                zzi7.zzY();
                                zzi7.zzg();
                                Preconditions.checkNotEmpty(str31);
                                Preconditions.checkNotEmpty(zzf);
                                ArrayMap arrayMap15 = new ArrayMap();
                                str10 = str6;
                                str11 = str5;
                                try {
                                    cursor2 = zzi7.zzh().query("property_filters", new String[]{str10, str11}, "app_id=? AND property_name=?", new String[]{str31, zzf}, null, null, null);
                                    try {
                                        try {
                                            if (cursor2.moveToFirst()) {
                                                while (true) {
                                                    try {
                                                        zzes zzaA3 = ((zzer) zzkp.zzl(zzes.zzc(), cursor2.getBlob(1))).zzaA();
                                                        Integer valueOf8 = Integer.valueOf(cursor2.getInt(0));
                                                        List list10 = (List) arrayMap15.get(valueOf8);
                                                        if (list10 == null) {
                                                            list3 = new ArrayList();
                                                            arrayMap15.put(valueOf8, list3);
                                                        } else {
                                                            list3 = list10;
                                                        }
                                                        list3.add(zzaA3);
                                                        it = it9;
                                                    } catch (IOException e23) {
                                                        it = it9;
                                                        try {
                                                            zzi7.zzs.zzay().zzd().zzc("Failed to merge filter", zzei.zzn(str31), e23);
                                                        } catch (SQLiteException e24) {
                                                            e3 = e24;
                                                            try {
                                                                zzi7.zzs.zzay().zzd().zzc(str7, zzei.zzn(str31), e3);
                                                                map5 = Collections.emptyMap();
                                                            } catch (Throwable th11) {
                                                                th = th11;
                                                                if (cursor2 != null) {
                                                                    cursor2.close();
                                                                }
                                                                throw th;
                                                            }
                                                        }
                                                    }
                                                    if (!cursor2.moveToNext()) {
                                                        break;
                                                    }
                                                    it9 = it;
                                                }
                                                if (cursor2 != null) {
                                                    cursor2.close();
                                                }
                                                map5 = arrayMap15;
                                            } else {
                                                it = it9;
                                                map5 = Collections.emptyMap();
                                            }
                                        } catch (SQLiteException e25) {
                                            e3 = e25;
                                            it = it9;
                                        }
                                    } catch (Throwable th12) {
                                        th = th12;
                                        if (cursor2 != null) {
                                        }
                                        throw th;
                                    }
                                } catch (SQLiteException e26) {
                                    e3 = e26;
                                    it = it9;
                                    cursor2 = null;
                                } catch (Throwable th13) {
                                    th = th13;
                                    cursor2 = null;
                                }
                                arrayMap14.put(zzf, map5);
                            } else {
                                it = it9;
                                str10 = str6;
                                str11 = str5;
                            }
                            Iterator it10 = map5.keySet().iterator();
                            while (true) {
                                if (!it10.hasNext()) {
                                    it9 = it;
                                    str5 = str11;
                                    str6 = str10;
                                    break;
                                }
                                int intValue3 = ((Integer) it10.next()).intValue();
                                Set<Integer> set2 = this.zzb;
                                Integer valueOf9 = Integer.valueOf(intValue3);
                                if (set2.contains(valueOf9)) {
                                    this.zzs.zzay().zzj().zzb("Skipping failed audience ID", valueOf9);
                                    it9 = it;
                                    str5 = str11;
                                    str6 = str10;
                                    break;
                                }
                                Iterator it11 = ((List) map5.get(valueOf9)).iterator();
                                boolean z3 = true;
                                while (true) {
                                    if (!it11.hasNext()) {
                                        map = map5;
                                        break;
                                    }
                                    zzes zzes = (zzes) it11.next();
                                    if (Log.isLoggable(this.zzs.zzay().zzq(), 2)) {
                                        map = map5;
                                        this.zzs.zzay().zzj().zzd("Evaluating filter. audience, filter, property", Integer.valueOf(intValue3), zzes.zzj() ? Integer.valueOf(zzes.zza()) : null, this.zzs.zzj().zze(zzes.zze()));
                                        this.zzs.zzay().zzj().zzb("Filter definition", this.zzf.zzu().zzp(zzes));
                                    } else {
                                        map = map5;
                                    }
                                    if (!zzes.zzj() || zzes.zza() > 256) {
                                        break;
                                    }
                                    zzy zzy = new zzy(this, this.zza, intValue3, zzes);
                                    z3 = zzy.zzd(this.zzd, this.zze, next3, zzf(intValue3, zzes.zza()));
                                    if (!z3) {
                                        this.zzb.add(Integer.valueOf(intValue3));
                                        break;
                                    }
                                    zzd(Integer.valueOf(intValue3)).zzc(zzy);
                                    map5 = map;
                                }
                                if (!z3) {
                                    this.zzb.add(Integer.valueOf(intValue3));
                                    map5 = map;
                                } else {
                                    map5 = map;
                                }
                            }
                        }
                        str9 = str6;
                    }
                    ArrayList arrayList22 = new ArrayList();
                    Set<Integer> keySet22 = this.zzc.keySet();
                    keySet22.removeAll(this.zzb);
                    for (Integer num2 : keySet22) {
                        int intValue4 = num2.intValue();
                        Map<Integer, zzt> map6 = this.zzc;
                        Integer valueOf10 = Integer.valueOf(intValue4);
                        zzt zzt = map6.get(valueOf10);
                        Preconditions.checkNotNull(zzt);
                        zzfk zza2 = zzt.zza(intValue4);
                        arrayList22.add(zza2);
                        zzaj zzi8 = this.zzf.zzi();
                        String str32 = this.zza;
                        zzgd zzd = zza2.zzd();
                        zzi8.zzY();
                        zzi8.zzg();
                        Preconditions.checkNotEmpty(str32);
                        Preconditions.checkNotNull(zzd);
                        byte[] zzbs = zzd.zzbs();
                        ContentValues contentValues2 = new ContentValues();
                        contentValues2.put("app_id", str32);
                        contentValues2.put(str9, valueOf10);
                        contentValues2.put(str8, zzbs);
                        try {
                            try {
                                if (zzi8.zzh().insertWithOnConflict("audience_filter_values", null, contentValues2, 5) == -1) {
                                    zzi8.zzs.zzay().zzd().zzb("Failed to insert filter results (got -1). appId", zzei.zzn(str32));
                                    str8 = str8;
                                } else {
                                    str8 = str8;
                                }
                            } catch (SQLiteException e27) {
                                e2 = e27;
                                zzi8.zzs.zzay().zzd().zzc("Error storing filter results. appId", zzei.zzn(str32), e2);
                                str8 = str8;
                            }
                        } catch (SQLiteException e28) {
                            e2 = e28;
                        }
                    }
                    return arrayList22;
                }
                emptyMap = Collections.emptyMap();
            }
            if (cursor.moveToFirst()) {
            }
            if (!arrayMap2.isEmpty()) {
            }
            if (!list.isEmpty()) {
            }
            if (!list2.isEmpty()) {
            }
            ArrayList arrayList222 = new ArrayList();
            Set<Integer> keySet222 = this.zzc.keySet();
            keySet222.removeAll(this.zzb);
            while (r2.hasNext()) {
            }
            return arrayList222;
        } catch (Throwable th14) {
            th = th14;
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        arrayMap = emptyMap;
        zzaj zzi32 = this.zzf.zzi();
        String str252 = this.zza;
        zzi32.zzY();
        zzi32.zzg();
        Preconditions.checkNotEmpty(str252);
        cursor = zzi32.zzh().query("audience_filter_values", new String[]{str23, str18}, "app_id=?", new String[]{str252}, null, null, null);
    }

    @Override // com.google.android.gms.measurement.internal.zzkd
    protected final boolean zzb() {
        return false;
    }
}
