package com.google.android.gms.measurement.internal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.exifinterface.media.ExifInterface;
import com.alcorlink.camera.AlErrorCode;
import com.facebook.common.util.UriUtil;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzeg;
import com.google.android.gms.internal.measurement.zzeh;
import com.google.android.gms.internal.measurement.zzei;
import com.google.android.gms.internal.measurement.zzej;
import com.google.android.gms.internal.measurement.zzek;
import com.google.android.gms.internal.measurement.zzel;
import com.google.android.gms.internal.measurement.zzer;
import com.google.android.gms.internal.measurement.zzes;
import com.google.android.gms.internal.measurement.zzfn;
import com.google.android.gms.internal.measurement.zzfo;
import com.google.android.gms.internal.measurement.zzfs;
import com.google.android.gms.internal.measurement.zzfx;
import com.google.android.gms.internal.measurement.zzfy;
import com.google.android.gms.internal.measurement.zzoq;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzaj extends zzkd {
    private static final String[] zza = {"last_bundled_timestamp", "ALTER TABLE events ADD COLUMN last_bundled_timestamp INTEGER;", "last_bundled_day", "ALTER TABLE events ADD COLUMN last_bundled_day INTEGER;", "last_sampled_complex_event_id", "ALTER TABLE events ADD COLUMN last_sampled_complex_event_id INTEGER;", "last_sampling_rate", "ALTER TABLE events ADD COLUMN last_sampling_rate INTEGER;", "last_exempt_from_sampling", "ALTER TABLE events ADD COLUMN last_exempt_from_sampling INTEGER;", "current_session_count", "ALTER TABLE events ADD COLUMN current_session_count INTEGER;"};
    private static final String[] zzb = {"origin", "ALTER TABLE user_attributes ADD COLUMN origin TEXT;"};
    private static final String[] zzc = {"app_version", "ALTER TABLE apps ADD COLUMN app_version TEXT;", "app_store", "ALTER TABLE apps ADD COLUMN app_store TEXT;", "gmp_version", "ALTER TABLE apps ADD COLUMN gmp_version INTEGER;", "dev_cert_hash", "ALTER TABLE apps ADD COLUMN dev_cert_hash INTEGER;", "measurement_enabled", "ALTER TABLE apps ADD COLUMN measurement_enabled INTEGER;", "last_bundle_start_timestamp", "ALTER TABLE apps ADD COLUMN last_bundle_start_timestamp INTEGER;", "day", "ALTER TABLE apps ADD COLUMN day INTEGER;", "daily_public_events_count", "ALTER TABLE apps ADD COLUMN daily_public_events_count INTEGER;", "daily_events_count", "ALTER TABLE apps ADD COLUMN daily_events_count INTEGER;", "daily_conversions_count", "ALTER TABLE apps ADD COLUMN daily_conversions_count INTEGER;", "remote_config", "ALTER TABLE apps ADD COLUMN remote_config BLOB;", "config_fetched_time", "ALTER TABLE apps ADD COLUMN config_fetched_time INTEGER;", "failed_config_fetch_time", "ALTER TABLE apps ADD COLUMN failed_config_fetch_time INTEGER;", "app_version_int", "ALTER TABLE apps ADD COLUMN app_version_int INTEGER;", "firebase_instance_id", "ALTER TABLE apps ADD COLUMN firebase_instance_id TEXT;", "daily_error_events_count", "ALTER TABLE apps ADD COLUMN daily_error_events_count INTEGER;", "daily_realtime_events_count", "ALTER TABLE apps ADD COLUMN daily_realtime_events_count INTEGER;", "health_monitor_sample", "ALTER TABLE apps ADD COLUMN health_monitor_sample TEXT;", "android_id", "ALTER TABLE apps ADD COLUMN android_id INTEGER;", "adid_reporting_enabled", "ALTER TABLE apps ADD COLUMN adid_reporting_enabled INTEGER;", "ssaid_reporting_enabled", "ALTER TABLE apps ADD COLUMN ssaid_reporting_enabled INTEGER;", "admob_app_id", "ALTER TABLE apps ADD COLUMN admob_app_id TEXT;", "linked_admob_app_id", "ALTER TABLE apps ADD COLUMN linked_admob_app_id TEXT;", "dynamite_version", "ALTER TABLE apps ADD COLUMN dynamite_version INTEGER;", "safelisted_events", "ALTER TABLE apps ADD COLUMN safelisted_events TEXT;", "ga_app_id", "ALTER TABLE apps ADD COLUMN ga_app_id TEXT;", "config_last_modified_time", "ALTER TABLE apps ADD COLUMN config_last_modified_time TEXT;"};
    private static final String[] zzd = {"realtime", "ALTER TABLE raw_events ADD COLUMN realtime INTEGER;"};
    private static final String[] zze = {"has_realtime", "ALTER TABLE queue ADD COLUMN has_realtime INTEGER;", "retry_count", "ALTER TABLE queue ADD COLUMN retry_count INTEGER;"};
    private static final String[] zzg = {"session_scoped", "ALTER TABLE event_filters ADD COLUMN session_scoped BOOLEAN;"};
    private static final String[] zzh = {"session_scoped", "ALTER TABLE property_filters ADD COLUMN session_scoped BOOLEAN;"};
    private static final String[] zzi = {"previous_install_count", "ALTER TABLE app2 ADD COLUMN previous_install_count INTEGER;"};
    private final zzjz zzk = new zzjz(this.zzs.zzav());
    private final zzai zzj = new zzai(this, this.zzs.zzau(), "google_app_measurement.db");

    public zzaj(zzkn zzkn) {
        super(zzkn);
        this.zzs.zzf();
    }

    static final void zzX(ContentValues contentValues, String str, Object obj) {
        Preconditions.checkNotEmpty("value");
        Preconditions.checkNotNull(obj);
        if (obj instanceof String) {
            contentValues.put("value", (String) obj);
        } else if (obj instanceof Long) {
            contentValues.put("value", (Long) obj);
        } else if (obj instanceof Double) {
            contentValues.put("value", (Double) obj);
        } else {
            throw new IllegalArgumentException("Invalid value type");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x003c  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private final long zzab(String str, String[] strArr) {
        SQLiteException e;
        Cursor cursor = null;
        try {
            cursor = zzh().rawQuery(str, strArr);
        } catch (SQLiteException e2) {
            e = e2;
        } catch (Throwable th) {
            th = th;
        }
        try {
            if (cursor.moveToFirst()) {
                long j = cursor.getLong(0);
                if (cursor != null) {
                    cursor.close();
                }
                return j;
            }
            throw new SQLiteException("Database returned empty set");
        } catch (SQLiteException e3) {
            e = e3;
            try {
                this.zzs.zzay().zzd().zzc("Database error", str, e);
                throw e;
            } catch (Throwable th2) {
                th = th2;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            if (cursor != null) {
            }
            throw th;
        }
    }

    private final long zzac(String str, String[] strArr, long j) {
        Throwable th;
        Cursor cursor;
        SQLiteException e;
        try {
            cursor = null;
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            Cursor rawQuery = zzh().rawQuery(str, strArr);
            try {
                if (rawQuery.moveToFirst()) {
                    long j2 = rawQuery.getLong(0);
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    return j2;
                }
                if (rawQuery != null) {
                    rawQuery.close();
                }
                return j;
            } catch (SQLiteException e2) {
                e = e2;
                this.zzs.zzay().zzd().zzc("Database error", str, e);
                throw e;
            }
        } catch (SQLiteException e3) {
            e = e3;
        } catch (Throwable th3) {
            th = th3;
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    public final void zzA(String str, String str2) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzg();
        zzY();
        try {
            zzh().delete("user_attributes", "app_id=? and name=?", new String[]{str, str2});
        } catch (SQLiteException e) {
            this.zzs.zzay().zzd().zzd("Error deleting user property. appId", zzei.zzn(str), this.zzs.zzj().zze(str2), e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x0347, code lost:
        r0 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:101:0x0348, code lost:
        r11.put("session_scoped", r0);
        r11.put(com.facebook.common.util.UriUtil.DATA_SCHEME, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x035c, code lost:
        if (zzh().insertWithOnConflict("property_filters", null, r11, 5) != -1) goto L_0x0372;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x035e, code lost:
        r23.zzs.zzay().zzd().zzb("Failed to insert property filter (got -1). appId", com.google.android.gms.measurement.internal.zzei.zzn(r24));
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x0372, code lost:
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x0376, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x0377, code lost:
        r23.zzs.zzay().zzd().zzc("Error storing property filter. appId", com.google.android.gms.measurement.internal.zzei.zzn(r24), r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x038a, code lost:
        zzY();
        zzg();
        com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r24);
        r0 = zzh();
        r0.delete("property_filters", r18, new java.lang.String[]{r24, java.lang.String.valueOf(r10)});
        r0.delete("event_filters", r18, new java.lang.String[]{r24, java.lang.String.valueOf(r10)});
        r18 = r18;
        r7 = r21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x03c1, code lost:
        r7 = r21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0186, code lost:
        r11 = r0.zzh().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0192, code lost:
        if (r11.hasNext() == false) goto L_0x01b9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x019e, code lost:
        if (r11.next().zzj() != false) goto L_0x018e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x01a0, code lost:
        r23.zzs.zzay().zzk().zzc("Property filter with no ID. Audience definition ignored. appId, audienceId", com.google.android.gms.measurement.internal.zzei.zzn(r24), java.lang.Integer.valueOf(r10));
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x01b9, code lost:
        r11 = r0.zzg().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x01cf, code lost:
        if (r11.hasNext() == false) goto L_0x02ab;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x01d1, code lost:
        r12 = r11.next();
        zzY();
        zzg();
        com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r24);
        com.google.android.gms.common.internal.Preconditions.checkNotNull(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x01eb, code lost:
        if (android.text.TextUtils.isEmpty(r12.zzg()) == false) goto L_0x021f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x01ed, code lost:
        r0 = r23.zzs.zzay().zzk();
        r9 = com.google.android.gms.measurement.internal.zzei.zzn(r24);
        r11 = java.lang.Integer.valueOf(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0205, code lost:
        if (r12.zzp() == false) goto L_0x0212;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0207, code lost:
        r17 = java.lang.Integer.valueOf(r12.zzb());
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0212, code lost:
        r17 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0214, code lost:
        r0.zzd("Event filter had no event name. Audience definition ignored. appId, audienceId, filterId", r9, r11, java.lang.String.valueOf(r17));
        r21 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x021f, code lost:
        r3 = r12.zzbs();
        r21 = r7;
        r7 = new android.content.ContentValues();
        r7.put("app_id", r24);
        r7.put("audience_id", java.lang.Integer.valueOf(r10));
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0238, code lost:
        if (r12.zzp() == false) goto L_0x0243;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x023a, code lost:
        r9 = java.lang.Integer.valueOf(r12.zzb());
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0243, code lost:
        r9 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0244, code lost:
        r7.put("filter_id", r9);
        r7.put("event_name", r12.zzg());
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0254, code lost:
        if (r12.zzq() == false) goto L_0x025f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0256, code lost:
        r9 = java.lang.Boolean.valueOf(r12.zzn());
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x025f, code lost:
        r9 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0260, code lost:
        r7.put("session_scoped", r9);
        r7.put(com.facebook.common.util.UriUtil.DATA_SCHEME, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x0274, code lost:
        if (zzh().insertWithOnConflict("event_filters", null, r7, 5) != -1) goto L_0x028f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0276, code lost:
        r23.zzs.zzay().zzd().zzb("Failed to insert event filter (got -1). appId", com.google.android.gms.measurement.internal.zzei.zzn(r24));
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0289, code lost:
        r7 = r21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x028f, code lost:
        r7 = r21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0295, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x0296, code lost:
        r23.zzs.zzay().zzd().zzc("Error storing event filter. appId", com.google.android.gms.measurement.internal.zzei.zzn(r24), r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x02ab, code lost:
        r21 = r7;
        r0 = r0.zzh().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x02b9, code lost:
        if (r0.hasNext() == false) goto L_0x03c1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x02bb, code lost:
        r3 = r0.next();
        zzY();
        zzg();
        com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r24);
        com.google.android.gms.common.internal.Preconditions.checkNotNull(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x02d5, code lost:
        if (android.text.TextUtils.isEmpty(r3.zze()) == false) goto L_0x0307;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x02d7, code lost:
        r0 = r23.zzs.zzay().zzk();
        r8 = com.google.android.gms.measurement.internal.zzei.zzn(r24);
        r9 = java.lang.Integer.valueOf(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x02ef, code lost:
        if (r3.zzj() == false) goto L_0x02fc;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x02f1, code lost:
        r17 = java.lang.Integer.valueOf(r3.zza());
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x02fc, code lost:
        r17 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x02fe, code lost:
        r0.zzd("Property filter had no property name. Audience definition ignored. appId, audienceId, filterId", r8, r9, java.lang.String.valueOf(r17));
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x0307, code lost:
        r7 = r3.zzbs();
        r11 = new android.content.ContentValues();
        r11.put("app_id", r24);
        r11.put("audience_id", java.lang.Integer.valueOf(r10));
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x031e, code lost:
        if (r3.zzj() == false) goto L_0x0329;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x0320, code lost:
        r12 = java.lang.Integer.valueOf(r3.zza());
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0329, code lost:
        r12 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x032a, code lost:
        r11.put("filter_id", r12);
        r11.put("property_name", r3.zze());
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x033c, code lost:
        if (r3.zzk() == false) goto L_0x0347;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x033e, code lost:
        r0 = java.lang.Boolean.valueOf(r3.zzi());
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final void zzB(String str, List<zzeh> list) {
        String str2;
        zzeg zzeg;
        boolean z;
        String str3 = "app_id=? and audience_id=?";
        Preconditions.checkNotNull(list);
        int i = 0;
        while (i < list.size()) {
            zzeg zzbv = list.get(i).zzbv();
            if (zzbv.zza() != 0) {
                zzeg = zzbv;
                int i2 = 0;
                while (i2 < zzeg.zza()) {
                    zzei zzbv2 = zzeg.zze(i2).zzbv();
                    zzei zzax = zzbv2.zzaq();
                    String zzb2 = zzgp.zzb(zzbv2.zze());
                    if (zzb2 != null) {
                        zzax.zzb(zzb2);
                        z = true;
                    } else {
                        z = false;
                    }
                    boolean z2 = z;
                    int i3 = 0;
                    while (i3 < zzbv2.zza()) {
                        zzel zzd2 = zzbv2.zzd(i3);
                        String zzb3 = zzib.zzb(zzd2.zze(), zzgq.zza, zzgq.zzb);
                        if (zzb3 != null) {
                            zzek zzbv3 = zzd2.zzbv();
                            zzbv3.zza(zzb3);
                            zzax.zzc(i3, zzbv3.zzaA());
                            z2 = true;
                        }
                        i3++;
                        zzbv2 = zzbv2;
                        str3 = str3;
                    }
                    if (z2) {
                        zzeg.zzc(i2, zzax);
                        list.set(i, zzbv.zzaA());
                        zzeg = zzbv;
                    }
                    i2++;
                    str3 = str3;
                }
                str2 = str3;
            } else {
                str2 = str3;
                zzeg = zzbv;
            }
            if (zzeg.zzb() != 0) {
                for (int i4 = 0; i4 < zzeg.zzb(); i4++) {
                    zzes zzf = zzeg.zzf(i4);
                    String zzb4 = zzib.zzb(zzf.zze(), zzgr.zza, zzgr.zzb);
                    if (zzb4 != null) {
                        zzer zzbv4 = zzf.zzbv();
                        zzbv4.zza(zzb4);
                        zzeg.zzd(i4, zzbv4);
                        list.set(i, zzbv.zzaA());
                        zzeg = zzbv;
                    }
                }
            }
            i++;
            str3 = str2;
        }
        String str4 = str3;
        zzY();
        zzg();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(list);
        SQLiteDatabase zzh2 = zzh();
        zzh2.beginTransaction();
        try {
            zzY();
            zzg();
            Preconditions.checkNotEmpty(str);
            SQLiteDatabase zzh3 = zzh();
            zzh3.delete("property_filters", "app_id=?", new String[]{str});
            zzh3.delete("event_filters", "app_id=?", new String[]{str});
            Iterator<zzeh> it = list.iterator();
            while (it.hasNext()) {
                zzeh next = it.next();
                zzY();
                zzg();
                Preconditions.checkNotEmpty(str);
                Preconditions.checkNotNull(next);
                if (next.zzk()) {
                    int zza2 = next.zza();
                    Iterator<zzej> it2 = next.zzg().iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            if (!it2.next().zzp()) {
                                this.zzs.zzay().zzk().zzc("Event filter with no ID. Audience definition ignored. appId, audienceId", zzei.zzn(str), Integer.valueOf(zza2));
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                } else {
                    this.zzs.zzay().zzk().zzb("Audience with no ID. appId", zzei.zzn(str));
                }
            }
            ArrayList arrayList = new ArrayList();
            for (zzeh zzeh : list) {
                arrayList.add(zzeh.zzk() ? Integer.valueOf(zzeh.zza()) : null);
            }
            Preconditions.checkNotEmpty(str);
            zzY();
            zzg();
            SQLiteDatabase zzh4 = zzh();
            try {
                long zzab = zzab("select count(1) from audience_filter_values where app_id=?", new String[]{str});
                int max = Math.max(0, Math.min(2000, this.zzs.zzf().zze(str, zzdw.zzE)));
                if (zzab > ((long) max)) {
                    ArrayList arrayList2 = new ArrayList();
                    int i5 = 0;
                    while (true) {
                        if (i5 >= arrayList.size()) {
                            String join = TextUtils.join(",", arrayList2);
                            StringBuilder sb = new StringBuilder(String.valueOf(join).length() + 2);
                            sb.append("(");
                            sb.append(join);
                            sb.append(")");
                            String sb2 = sb.toString();
                            StringBuilder sb3 = new StringBuilder(String.valueOf(sb2).length() + 140);
                            sb3.append("audience_id in (select audience_id from audience_filter_values where app_id=? and audience_id not in ");
                            sb3.append(sb2);
                            sb3.append(" order by rowid desc limit -1 offset ?)");
                            zzh4.delete("audience_filter_values", sb3.toString(), new String[]{str, Integer.toString(max)});
                            break;
                        }
                        Integer num = (Integer) arrayList.get(i5);
                        if (num == null) {
                            break;
                        }
                        arrayList2.add(Integer.toString(num.intValue()));
                        i5++;
                    }
                }
            } catch (SQLiteException e) {
                this.zzs.zzay().zzd().zzc("Database error querying filters. appId", zzei.zzn(str), e);
            }
            zzh2.setTransactionSuccessful();
        } finally {
            zzh2.endTransaction();
        }
    }

    public final void zzC() {
        zzY();
        zzh().setTransactionSuccessful();
    }

    public final void zzD(zzg zzg2) {
        Preconditions.checkNotNull(zzg2);
        zzg();
        zzY();
        String zzt = zzg2.zzt();
        Preconditions.checkNotNull(zzt);
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzt);
        contentValues.put("app_instance_id", zzg2.zzu());
        contentValues.put("gmp_app_id", zzg2.zzz());
        contentValues.put("resettable_device_id_hash", zzg2.zzB());
        contentValues.put("last_bundle_index", Long.valueOf(zzg2.zzo()));
        contentValues.put("last_bundle_start_timestamp", Long.valueOf(zzg2.zzp()));
        contentValues.put("last_bundle_end_timestamp", Long.valueOf(zzg2.zzn()));
        contentValues.put("app_version", zzg2.zzw());
        contentValues.put("app_store", zzg2.zzv());
        contentValues.put("gmp_version", Long.valueOf(zzg2.zzm()));
        contentValues.put("dev_cert_hash", Long.valueOf(zzg2.zzj()));
        contentValues.put("measurement_enabled", Boolean.valueOf(zzg2.zzaj()));
        contentValues.put("day", Long.valueOf(zzg2.zzi()));
        contentValues.put("daily_public_events_count", Long.valueOf(zzg2.zzg()));
        contentValues.put("daily_events_count", Long.valueOf(zzg2.zzf()));
        contentValues.put("daily_conversions_count", Long.valueOf(zzg2.zzd()));
        contentValues.put("config_fetched_time", Long.valueOf(zzg2.zzc()));
        contentValues.put("failed_config_fetch_time", Long.valueOf(zzg2.zzl()));
        contentValues.put("app_version_int", Long.valueOf(zzg2.zzb()));
        contentValues.put("firebase_instance_id", zzg2.zzx());
        contentValues.put("daily_error_events_count", Long.valueOf(zzg2.zze()));
        contentValues.put("daily_realtime_events_count", Long.valueOf(zzg2.zzh()));
        contentValues.put("health_monitor_sample", zzg2.zzA());
        contentValues.put("android_id", Long.valueOf(zzg2.zza()));
        contentValues.put("adid_reporting_enabled", Boolean.valueOf(zzg2.zzai()));
        contentValues.put("admob_app_id", zzg2.zzr());
        contentValues.put("dynamite_version", Long.valueOf(zzg2.zzk()));
        List<String> zzC = zzg2.zzC();
        if (zzC != null) {
            if (zzC.size() == 0) {
                this.zzs.zzay().zzk().zzb("Safelisted events should not be an empty list. appId", zzt);
            } else {
                contentValues.put("safelisted_events", TextUtils.join(",", zzC));
            }
        }
        zzoq.zzc();
        if (this.zzs.zzf().zzs(zzt, zzdw.zzad)) {
            contentValues.put("ga_app_id", zzg2.zzy());
        }
        try {
            SQLiteDatabase zzh2 = zzh();
            if (((long) zzh2.update("apps", contentValues, "app_id = ?", new String[]{zzt})) == 0 && zzh2.insertWithOnConflict("apps", null, contentValues, 5) == -1) {
                this.zzs.zzay().zzd().zzb("Failed to insert/update app (got -1). appId", zzei.zzn(zzt));
            }
        } catch (SQLiteException e) {
            this.zzs.zzay().zzd().zzc("Error storing app. appId", zzei.zzn(zzt), e);
        }
    }

    public final void zzE(zzap zzap) {
        long j;
        Preconditions.checkNotNull(zzap);
        zzg();
        zzY();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzap.zza);
        contentValues.put(AppMeasurementSdk.ConditionalUserProperty.NAME, zzap.zzb);
        contentValues.put("lifetime_count", Long.valueOf(zzap.zzc));
        contentValues.put("current_bundle_count", Long.valueOf(zzap.zzd));
        contentValues.put("last_fire_timestamp", Long.valueOf(zzap.zzf));
        contentValues.put("last_bundled_timestamp", Long.valueOf(zzap.zzg));
        contentValues.put("last_bundled_day", zzap.zzh);
        contentValues.put("last_sampled_complex_event_id", zzap.zzi);
        contentValues.put("last_sampling_rate", zzap.zzj);
        contentValues.put("current_session_count", Long.valueOf(zzap.zze));
        Boolean bool = zzap.zzk;
        if (bool == null || !bool.booleanValue()) {
            j = null;
        } else {
            j = 1L;
        }
        contentValues.put("last_exempt_from_sampling", j);
        try {
            if (zzh().insertWithOnConflict("events", null, contentValues, 5) == -1) {
                this.zzs.zzay().zzd().zzb("Failed to insert/update event aggregates (got -1). appId", zzei.zzn(zzap.zza));
            }
        } catch (SQLiteException e) {
            this.zzs.zzay().zzd().zzc("Error storing event aggregates. appId", zzei.zzn(zzap.zza), e);
        }
    }

    public final void zzF(String str, byte[] bArr, String str2) {
        Preconditions.checkNotEmpty(str);
        zzg();
        zzY();
        ContentValues contentValues = new ContentValues();
        contentValues.put("remote_config", bArr);
        contentValues.put("config_last_modified_time", str2);
        try {
            if (((long) zzh().update("apps", contentValues, "app_id = ?", new String[]{str})) == 0) {
                this.zzs.zzay().zzd().zzb("Failed to update remote config (got 0). appId", zzei.zzn(str));
            }
        } catch (SQLiteException e) {
            this.zzs.zzay().zzd().zzc("Error storing remote config. appId", zzei.zzn(str), e);
        }
    }

    public final boolean zzG() {
        return zzab("select count(1) > 0 from raw_events", null) != 0;
    }

    public final boolean zzH() {
        return zzab("select count(1) > 0 from queue where has_realtime = 1", null) != 0;
    }

    public final boolean zzI() {
        return zzab("select count(1) > 0 from raw_events where realtime = 1", null) != 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:5:0x0047, code lost:
        if (r2 > (com.google.android.gms.measurement.internal.zzaf.zzA() + r0)) goto L_0x0049;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final boolean zzJ(zzfy zzfy, boolean z) {
        zzg();
        zzY();
        Preconditions.checkNotNull(zzfy);
        Preconditions.checkNotEmpty(zzfy.zzy());
        Preconditions.checkState(zzfy.zzbc());
        zzz();
        long currentTimeMillis = this.zzs.zzav().currentTimeMillis();
        long zzm = zzfy.zzm();
        this.zzs.zzf();
        if (zzm >= currentTimeMillis - zzaf.zzA()) {
            long zzm2 = zzfy.zzm();
            this.zzs.zzf();
        }
        this.zzs.zzay().zzk().zzd("Storing bundle outside of the max uploading time span. appId, now, timestamp", zzei.zzn(zzfy.zzy()), Long.valueOf(currentTimeMillis), Long.valueOf(zzfy.zzm()));
        try {
            byte[] zzz = this.zzf.zzu().zzz(zzfy.zzbs());
            this.zzs.zzay().zzj().zzb("Saving bundle, size", Integer.valueOf(zzz.length));
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", zzfy.zzy());
            contentValues.put("bundle_end_timestamp", Long.valueOf(zzfy.zzm()));
            contentValues.put(UriUtil.DATA_SCHEME, zzz);
            contentValues.put("has_realtime", Integer.valueOf(z ? 1 : 0));
            if (zzfy.zzbi()) {
                contentValues.put("retry_count", Integer.valueOf(zzfy.zze()));
            }
            try {
                if (zzh().insert("queue", null, contentValues) != -1) {
                    return true;
                }
                this.zzs.zzay().zzd().zzb("Failed to insert bundle (got -1). appId", zzei.zzn(zzfy.zzy()));
                return false;
            } catch (SQLiteException e) {
                this.zzs.zzay().zzd().zzc("Error storing bundle. appId", zzei.zzn(zzfy.zzy()), e);
                return false;
            }
        } catch (IOException e2) {
            this.zzs.zzay().zzd().zzc("Data loss. Failed to serialize bundle. appId", zzei.zzn(zzfy.zzy()), e2);
            return false;
        }
    }

    protected final boolean zzK() {
        Context zzau = this.zzs.zzau();
        this.zzs.zzf();
        return zzau.getDatabasePath("google_app_measurement.db").exists();
    }

    public final boolean zzL(String str, Long l, long j, zzfo zzfo) {
        zzg();
        zzY();
        Preconditions.checkNotNull(zzfo);
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(l);
        byte[] zzbs = zzfo.zzbs();
        this.zzs.zzay().zzj().zzc("Saving complex main event, appId, data size", this.zzs.zzj().zzc(str), Integer.valueOf(zzbs.length));
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", str);
        contentValues.put("event_id", l);
        contentValues.put("children_to_process", Long.valueOf(j));
        contentValues.put("main_event", zzbs);
        try {
            if (zzh().insertWithOnConflict("main_event_params", null, contentValues, 5) != -1) {
                return true;
            }
            this.zzs.zzay().zzd().zzb("Failed to insert complex main event (got -1). appId", zzei.zzn(str));
            return false;
        } catch (SQLiteException e) {
            this.zzs.zzay().zzd().zzc("Error storing complex main event. appId", zzei.zzn(str), e);
            return false;
        }
    }

    public final boolean zzM(zzab zzab) {
        Preconditions.checkNotNull(zzab);
        zzg();
        zzY();
        String str = zzab.zza;
        Preconditions.checkNotNull(str);
        if (zzp(str, zzab.zzc.zzb) == null) {
            long zzab2 = zzab("SELECT COUNT(1) FROM conditional_properties WHERE app_id=?", new String[]{str});
            this.zzs.zzf();
            if (zzab2 >= 1000) {
                return false;
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", str);
        contentValues.put("origin", zzab.zzb);
        contentValues.put(AppMeasurementSdk.ConditionalUserProperty.NAME, zzab.zzc.zzb);
        zzX(contentValues, "value", Preconditions.checkNotNull(zzab.zzc.zza()));
        contentValues.put(AppMeasurementSdk.ConditionalUserProperty.ACTIVE, Boolean.valueOf(zzab.zze));
        contentValues.put(AppMeasurementSdk.ConditionalUserProperty.TRIGGER_EVENT_NAME, zzab.zzf);
        contentValues.put(AppMeasurementSdk.ConditionalUserProperty.TRIGGER_TIMEOUT, Long.valueOf(zzab.zzh));
        contentValues.put("timed_out_event", this.zzs.zzv().zzan(zzab.zzg));
        contentValues.put(AppMeasurementSdk.ConditionalUserProperty.CREATION_TIMESTAMP, Long.valueOf(zzab.zzd));
        contentValues.put("triggered_event", this.zzs.zzv().zzan(zzab.zzi));
        contentValues.put(AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_TIMESTAMP, Long.valueOf(zzab.zzc.zzc));
        contentValues.put(AppMeasurementSdk.ConditionalUserProperty.TIME_TO_LIVE, Long.valueOf(zzab.zzj));
        contentValues.put("expired_event", this.zzs.zzv().zzan(zzab.zzk));
        try {
            if (zzh().insertWithOnConflict("conditional_properties", null, contentValues, 5) == -1) {
                this.zzs.zzay().zzd().zzb("Failed to insert/update conditional user property (got -1)", zzei.zzn(str));
            }
        } catch (SQLiteException e) {
            this.zzs.zzay().zzd().zzc("Error storing conditional user property", zzei.zzn(str), e);
        }
        return true;
    }

    public final boolean zzN(zzks zzks) {
        Preconditions.checkNotNull(zzks);
        zzg();
        zzY();
        if (zzp(zzks.zza, zzks.zzc) == null) {
            if (zzku.zzah(zzks.zzc)) {
                if (zzab("select count(1) from user_attributes where app_id=? and name not like '!_%' escape '!'", new String[]{zzks.zza}) >= ((long) this.zzs.zzf().zzf(zzks.zza, zzdw.zzF, 25, 100))) {
                    return false;
                }
            } else if (!"_npa".equals(zzks.zzc)) {
                long zzab = zzab("select count(1) from user_attributes where app_id=? and origin=? AND name like '!_%' escape '!'", new String[]{zzks.zza, zzks.zzb});
                this.zzs.zzf();
                if (zzab >= 25) {
                    return false;
                }
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzks.zza);
        contentValues.put("origin", zzks.zzb);
        contentValues.put(AppMeasurementSdk.ConditionalUserProperty.NAME, zzks.zzc);
        contentValues.put("set_timestamp", Long.valueOf(zzks.zzd));
        zzX(contentValues, "value", zzks.zze);
        try {
            if (zzh().insertWithOnConflict("user_attributes", null, contentValues, 5) == -1) {
                this.zzs.zzay().zzd().zzb("Failed to insert/update user property (got -1). appId", zzei.zzn(zzks.zza));
            }
        } catch (SQLiteException e) {
            this.zzs.zzay().zzd().zzc("Error storing user property. appId", zzei.zzn(zzks.zza), e);
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x024b  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0252  */
    /* JADX WARN: Removed duplicated region for block: B:127:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final void zzW(String str, long j, long j2, zzkm zzkm) {
        Cursor cursor;
        Throwable th;
        String str2;
        SQLiteException e;
        SQLiteDatabase zzh2;
        Cursor cursor2;
        String str3;
        Cursor query;
        int i;
        String[] strArr;
        String str4;
        try {
            Preconditions.checkNotNull(zzkm);
            zzg();
            zzY();
            str2 = null;
            try {
                zzh2 = zzh();
                String str5 = "";
                if (TextUtils.isEmpty(null)) {
                    int i2 = (j2 > -1 ? 1 : (j2 == -1 ? 0 : -1));
                    String[] strArr2 = i2 != 0 ? new String[]{String.valueOf(j2), String.valueOf(j)} : new String[]{String.valueOf(j)};
                    if (i2 != 0) {
                        str5 = "rowid <= ? and ";
                    }
                    StringBuilder sb = new StringBuilder(str5.length() + AlErrorCode.ERR_VERIFY);
                    sb.append("select app_id, metadata_fingerprint from raw_events where ");
                    sb.append(str5);
                    sb.append("app_id in (select app_id from apps where config_fetched_time >= ?) order by rowid limit 1;");
                    cursor = zzh2.rawQuery(sb.toString(), strArr2);
                    try {
                        if (cursor.moveToFirst()) {
                            str2 = cursor.getString(0);
                            String string = cursor.getString(1);
                            cursor.close();
                            cursor2 = cursor;
                            str3 = string;
                        } else if (cursor != null) {
                            cursor.close();
                            return;
                        } else {
                            return;
                        }
                    } catch (SQLiteException e2) {
                        e = e2;
                        this.zzs.zzay().zzd().zzc("Data loss. Error selecting raw event. appId", zzei.zzn(str2), e);
                        if (cursor == null) {
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (cursor != null) {
                        }
                        throw th;
                    }
                } else {
                    int i3 = (j2 > -1 ? 1 : (j2 == -1 ? 0 : -1));
                    String[] strArr3 = i3 != 0 ? new String[]{null, String.valueOf(j2)} : new String[]{null};
                    if (i3 != 0) {
                        str5 = " and rowid <= ?";
                    }
                    StringBuilder sb2 = new StringBuilder(str5.length() + 84);
                    sb2.append("select metadata_fingerprint from raw_events where app_id = ?");
                    sb2.append(str5);
                    sb2.append(" order by rowid limit 1;");
                    cursor = zzh2.rawQuery(sb2.toString(), strArr3);
                    try {
                        if (cursor.moveToFirst()) {
                            String string2 = cursor.getString(0);
                            cursor.close();
                            cursor2 = cursor;
                            str3 = string2;
                        } else if (cursor != null) {
                            cursor.close();
                            return;
                        } else {
                            return;
                        }
                    } catch (SQLiteException e3) {
                        e = e3;
                        this.zzs.zzay().zzd().zzc("Data loss. Error selecting raw event. appId", zzei.zzn(str2), e);
                        if (cursor == null) {
                            cursor.close();
                            return;
                        }
                        return;
                    }
                }
                try {
                    query = zzh2.query("raw_events_metadata", new String[]{"metadata"}, "app_id = ? and metadata_fingerprint = ?", new String[]{str2, str3}, null, null, "rowid", ExifInterface.GPS_MEASUREMENT_2D);
                } catch (SQLiteException e4) {
                    e = e4;
                } catch (Throwable th3) {
                    th = th3;
                }
            } catch (SQLiteException e5) {
                e = e5;
                cursor = null;
            } catch (Throwable th4) {
                th = th4;
                cursor = null;
            }
        } catch (Throwable th5) {
            th = th5;
        }
        try {
            if (!query.moveToFirst()) {
                this.zzs.zzay().zzd().zzb("Raw event metadata record is missing. appId", zzei.zzn(str2));
                if (query != null) {
                    query.close();
                    return;
                }
                return;
            }
            try {
                zzfy zzaA = ((zzfx) zzkp.zzl(zzfy.zzu(), query.getBlob(0))).zzaA();
                if (query.moveToNext()) {
                    this.zzs.zzay().zzk().zzb("Get multiple raw event metadata records, expected one. appId", zzei.zzn(str2));
                }
                query.close();
                Preconditions.checkNotNull(zzaA);
                zzkm.zza = zzaA;
                if (j2 != -1) {
                    i = 1;
                    str4 = "app_id = ? and metadata_fingerprint = ? and rowid <= ?";
                    strArr = new String[]{str2, str3, String.valueOf(j2)};
                } else {
                    i = 1;
                    str4 = "app_id = ? and metadata_fingerprint = ?";
                    strArr = new String[]{str2, str3};
                }
                cursor = zzh2.query("raw_events", new String[]{"rowid", AppMeasurementSdk.ConditionalUserProperty.NAME, "timestamp", UriUtil.DATA_SCHEME}, str4, strArr, null, null, "rowid", null);
                try {
                    if (cursor.moveToFirst()) {
                        do {
                            long j3 = cursor.getLong(0);
                            try {
                                zzfn zzfn = (zzfn) zzkp.zzl(zzfo.zze(), cursor.getBlob(3));
                                zzfn.zzi(cursor.getString(i));
                                zzfn.zzm(cursor.getLong(2));
                                if (!zzkm.zza(j3, zzfn.zzaA())) {
                                    if (cursor != null) {
                                        cursor.close();
                                        return;
                                    }
                                    return;
                                }
                            } catch (IOException e6) {
                                this.zzs.zzay().zzd().zzc("Data loss. Failed to merge raw event. appId", zzei.zzn(str2), e6);
                            }
                        } while (cursor.moveToNext());
                        if (cursor != null) {
                            cursor.close();
                            return;
                        }
                        return;
                    }
                    this.zzs.zzay().zzk().zzb("Raw event data disappeared while in transaction. appId", zzei.zzn(str2));
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (SQLiteException e7) {
                    e = e7;
                    this.zzs.zzay().zzd().zzc("Data loss. Error selecting raw event. appId", zzei.zzn(str2), e);
                    if (cursor == null) {
                    }
                } catch (Throwable th6) {
                    th = th6;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            } catch (IOException e8) {
                this.zzs.zzay().zzd().zzc("Data loss. Failed to merge raw event metadata. appId", zzei.zzn(str2), e8);
                if (query != null) {
                    query.close();
                }
            }
        } catch (SQLiteException e9) {
            e = e9;
            cursor2 = query;
            cursor = cursor2;
            this.zzs.zzay().zzd().zzc("Data loss. Error selecting raw event. appId", zzei.zzn(str2), e);
            if (cursor == null) {
            }
        } catch (Throwable th7) {
            th = th7;
            cursor2 = query;
            cursor = cursor2;
            if (cursor != null) {
            }
            throw th;
        }
    }

    public final int zza(String str, String str2) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzg();
        zzY();
        try {
            return zzh().delete("conditional_properties", "app_id=? and name=?", new String[]{str, str2});
        } catch (SQLiteException e) {
            this.zzs.zzay().zzd().zzd("Error deleting conditional property", zzei.zzn(str), this.zzs.zzj().zze(str2), e);
            return 0;
        }
    }

    @Override // com.google.android.gms.measurement.internal.zzkd
    protected final boolean zzb() {
        return false;
    }

    public final long zzc(String str, String str2) {
        long j;
        SQLiteException e;
        ContentValues contentValues;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty("first_open_count");
        zzg();
        zzY();
        SQLiteDatabase zzh2 = zzh();
        zzh2.beginTransaction();
        try {
            try {
                StringBuilder sb = new StringBuilder(48);
                sb.append("select ");
                sb.append("first_open_count");
                sb.append(" from app2 where app_id=?");
                j = zzac(sb.toString(), new String[]{str}, -1);
            } catch (SQLiteException e2) {
                e = e2;
                j = 0;
            }
            try {
                if (j == -1) {
                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put("app_id", str);
                    contentValues2.put("first_open_count", (Integer) 0);
                    contentValues2.put("previous_install_count", (Integer) 0);
                    if (zzh2.insertWithOnConflict("app2", null, contentValues2, 5) == -1) {
                        this.zzs.zzay().zzd().zzc("Failed to insert column (got -1). appId", zzei.zzn(str), "first_open_count");
                        return -1;
                    }
                    j = 0;
                }
                contentValues = new ContentValues();
                contentValues.put("app_id", str);
                contentValues.put("first_open_count", Long.valueOf(1 + j));
            } catch (SQLiteException e3) {
                e = e3;
                this.zzs.zzay().zzd().zzd("Error inserting column. appId", zzei.zzn(str), "first_open_count", e);
                return j;
            }
            if (((long) zzh2.update("app2", contentValues, "app_id = ?", new String[]{str})) == 0) {
                this.zzs.zzay().zzd().zzc("Failed to update column (got 0). appId", zzei.zzn(str), "first_open_count");
                return -1;
            }
            zzh2.setTransactionSuccessful();
            return j;
        } finally {
            zzh2.endTransaction();
        }
    }

    public final long zzd() {
        return zzac("select max(bundle_end_timestamp) from queue", null, 0);
    }

    public final long zze() {
        return zzac("select max(timestamp) from raw_events", null, 0);
    }

    public final long zzf(String str) {
        Preconditions.checkNotEmpty(str);
        return zzac("select count(1) from events where app_id=? and name not like '!_%' escape '!'", new String[]{str}, 0);
    }

    public final SQLiteDatabase zzh() {
        zzg();
        try {
            return this.zzj.getWritableDatabase();
        } catch (SQLiteException e) {
            this.zzs.zzay().zzk().zzb("Error opening database", e);
            throw e;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:47:0x00e2  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Bundle zzi(String str) {
        SQLiteException e;
        Cursor cursor;
        zzg();
        zzY();
        try {
            cursor = zzh().rawQuery("select parameters from default_event_params where app_id=?", new String[]{str});
        } catch (SQLiteException e2) {
            e = e2;
            cursor = null;
        } catch (Throwable th) {
            th = th;
            cursor = null;
        }
        try {
            if (!cursor.moveToFirst()) {
                this.zzs.zzay().zzj().zza("Default event parameters not found");
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            try {
                zzfo zzaA = ((zzfn) zzkp.zzl(zzfo.zze(), cursor.getBlob(0))).zzaA();
                this.zzf.zzu();
                List<zzfs> zzi2 = zzaA.zzi();
                Bundle bundle = new Bundle();
                for (zzfs zzfs : zzi2) {
                    String zzg2 = zzfs.zzg();
                    if (zzfs.zzu()) {
                        bundle.putDouble(zzg2, zzfs.zza());
                    } else if (zzfs.zzv()) {
                        bundle.putFloat(zzg2, zzfs.zzb());
                    } else if (zzfs.zzy()) {
                        bundle.putString(zzg2, zzfs.zzh());
                    } else if (zzfs.zzw()) {
                        bundle.putLong(zzg2, zzfs.zzd());
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
                return bundle;
            } catch (IOException e3) {
                this.zzs.zzay().zzd().zzc("Failed to retrieve default event parameters. appId", zzei.zzn(str), e3);
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
        } catch (SQLiteException e4) {
            e = e4;
            try {
                this.zzs.zzay().zzd().zzb("Error selecting default event parameters", e);
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            } catch (Throwable th2) {
                th = th2;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            if (cursor != null) {
            }
            throw th;
        }
    }

    public final zzg zzj(String str) {
        Throwable th;
        Cursor cursor;
        Cursor cursor2;
        SQLiteException e;
        try {
            Preconditions.checkNotEmpty(str);
            zzg();
            zzY();
            cursor = null;
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            boolean z = true;
            cursor2 = zzh().query("apps", new String[]{"app_instance_id", "gmp_app_id", "resettable_device_id_hash", "last_bundle_index", "last_bundle_start_timestamp", "last_bundle_end_timestamp", "app_version", "app_store", "gmp_version", "dev_cert_hash", "measurement_enabled", "day", "daily_public_events_count", "daily_events_count", "daily_conversions_count", "config_fetched_time", "failed_config_fetch_time", "app_version_int", "firebase_instance_id", "daily_error_events_count", "daily_realtime_events_count", "health_monitor_sample", "android_id", "adid_reporting_enabled", "admob_app_id", "dynamite_version", "safelisted_events", "ga_app_id"}, "app_id=?", new String[]{str}, null, null, null);
            try {
                if (!cursor2.moveToFirst()) {
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    return null;
                }
                zzg zzg2 = new zzg(this.zzf.zzq(), str);
                zzg2.zzI(cursor2.getString(0));
                zzg2.zzY(cursor2.getString(1));
                zzg2.zzag(cursor2.getString(2));
                zzg2.zzac(cursor2.getLong(3));
                zzg2.zzad(cursor2.getLong(4));
                zzg2.zzab(cursor2.getLong(5));
                zzg2.zzK(cursor2.getString(6));
                zzg2.zzJ(cursor2.getString(7));
                zzg2.zzZ(cursor2.getLong(8));
                zzg2.zzT(cursor2.getLong(9));
                zzg2.zzae(!cursor2.isNull(10) ? cursor2.getInt(10) != 0 : true);
                zzg2.zzS(cursor2.getLong(11));
                zzg2.zzQ(cursor2.getLong(12));
                zzg2.zzP(cursor2.getLong(13));
                zzg2.zzN(cursor2.getLong(14));
                zzg2.zzM(cursor2.getLong(15));
                zzg2.zzV(cursor2.getLong(16));
                zzg2.zzL(cursor2.isNull(17) ? -2147483648L : (long) cursor2.getInt(17));
                zzg2.zzW(cursor2.getString(18));
                zzg2.zzO(cursor2.getLong(19));
                zzg2.zzR(cursor2.getLong(20));
                zzg2.zzaa(cursor2.getString(21));
                long j = 0;
                if (!this.zzs.zzf().zzs(null, zzdw.zzan)) {
                    zzg2.zzH(cursor2.isNull(22) ? 0 : cursor2.getLong(22));
                }
                if (!cursor2.isNull(23) && cursor2.getInt(23) == 0) {
                    z = false;
                }
                zzg2.zzG(z);
                zzg2.zzF(cursor2.getString(24));
                if (!cursor2.isNull(25)) {
                    j = cursor2.getLong(25);
                }
                zzg2.zzU(j);
                if (!cursor2.isNull(26)) {
                    zzg2.zzah(Arrays.asList(cursor2.getString(26).split(",", -1)));
                }
                zzoq.zzc();
                if (this.zzs.zzf().zzs(str, zzdw.zzad)) {
                    zzg2.zzX(cursor2.getString(27));
                }
                zzg2.zzD();
                if (cursor2.moveToNext()) {
                    this.zzs.zzay().zzd().zzb("Got multiple records for app, expected one. appId", zzei.zzn(str));
                }
                if (cursor2 != null) {
                    cursor2.close();
                }
                return zzg2;
            } catch (SQLiteException e2) {
                e = e2;
                this.zzs.zzay().zzd().zzc("Error querying app. appId", zzei.zzn(str), e);
                if (cursor2 != null) {
                    cursor2.close();
                }
                return null;
            }
        } catch (SQLiteException e3) {
            e = e3;
            cursor2 = null;
        } catch (Throwable th3) {
            th = th3;
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    public final zzab zzk(String str, String str2) {
        Throwable th;
        Cursor cursor;
        Cursor cursor2;
        SQLiteException e;
        boolean z;
        try {
            Preconditions.checkNotEmpty(str);
            Preconditions.checkNotEmpty(str2);
            zzg();
            zzY();
            cursor = null;
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            cursor2 = zzh().query("conditional_properties", new String[]{"origin", "value", AppMeasurementSdk.ConditionalUserProperty.ACTIVE, AppMeasurementSdk.ConditionalUserProperty.TRIGGER_EVENT_NAME, AppMeasurementSdk.ConditionalUserProperty.TRIGGER_TIMEOUT, "timed_out_event", AppMeasurementSdk.ConditionalUserProperty.CREATION_TIMESTAMP, "triggered_event", AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_TIMESTAMP, AppMeasurementSdk.ConditionalUserProperty.TIME_TO_LIVE, "expired_event"}, "app_id=? and name=?", new String[]{str, str2}, null, null, null);
            try {
                if (!cursor2.moveToFirst()) {
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    return null;
                }
                String string = cursor2.getString(0);
                Object zzq = zzq(cursor2, 1);
                if (cursor2.getInt(2) != 0) {
                    z = true;
                } else {
                    z = false;
                }
                zzab zzab = new zzab(str, string, new zzkq(str2, cursor2.getLong(8), zzq, string), cursor2.getLong(6), z, cursor2.getString(3), (zzat) this.zzf.zzu().zzh(cursor2.getBlob(5), zzat.CREATOR), cursor2.getLong(4), (zzat) this.zzf.zzu().zzh(cursor2.getBlob(7), zzat.CREATOR), cursor2.getLong(9), (zzat) this.zzf.zzu().zzh(cursor2.getBlob(10), zzat.CREATOR));
                if (cursor2.moveToNext()) {
                    this.zzs.zzay().zzd().zzc("Got multiple records for conditional property, expected one", zzei.zzn(str), this.zzs.zzj().zze(str2));
                }
                if (cursor2 != null) {
                    cursor2.close();
                }
                return zzab;
            } catch (SQLiteException e2) {
                e = e2;
                this.zzs.zzay().zzd().zzd("Error querying conditional property", zzei.zzn(str), this.zzs.zzj().zze(str2), e);
                if (cursor2 != null) {
                    cursor2.close();
                }
                return null;
            }
        } catch (SQLiteException e3) {
            e = e3;
            cursor2 = null;
        } catch (Throwable th3) {
            th = th3;
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    public final zzah zzl(long j, String str, boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
        return zzm(j, str, 1, false, false, z3, false, z5);
    }

    public final zzah zzm(long j, String str, long j2, boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
        Throwable th;
        String[] strArr;
        zzah zzah;
        Cursor cursor;
        SQLiteException e;
        try {
            Preconditions.checkNotEmpty(str);
            zzg();
            zzY();
            strArr = new String[]{str};
            zzah = new zzah();
            cursor = null;
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            SQLiteDatabase zzh2 = zzh();
            cursor = zzh2.query("apps", new String[]{"day", "daily_events_count", "daily_public_events_count", "daily_conversions_count", "daily_error_events_count", "daily_realtime_events_count"}, "app_id=?", new String[]{str}, null, null, null);
            try {
                if (!cursor.moveToFirst()) {
                    this.zzs.zzay().zzk().zzb("Not updating daily counts, app is not known. appId", zzei.zzn(str));
                    if (cursor != null) {
                        cursor.close();
                    }
                    return zzah;
                }
                if (cursor.getLong(0) == j) {
                    zzah.zzb = cursor.getLong(1);
                    zzah.zza = cursor.getLong(2);
                    zzah.zzc = cursor.getLong(3);
                    zzah.zzd = cursor.getLong(4);
                    zzah.zze = cursor.getLong(5);
                }
                if (z) {
                    zzah.zzb += j2;
                }
                if (z2) {
                    zzah.zza += j2;
                }
                if (z3) {
                    zzah.zzc += j2;
                }
                if (z4) {
                    zzah.zzd += j2;
                }
                if (z5) {
                    zzah.zze += j2;
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("day", Long.valueOf(j));
                contentValues.put("daily_public_events_count", Long.valueOf(zzah.zza));
                contentValues.put("daily_events_count", Long.valueOf(zzah.zzb));
                contentValues.put("daily_conversions_count", Long.valueOf(zzah.zzc));
                contentValues.put("daily_error_events_count", Long.valueOf(zzah.zzd));
                contentValues.put("daily_realtime_events_count", Long.valueOf(zzah.zze));
                zzh2.update("apps", contentValues, "app_id=?", strArr);
                if (cursor != null) {
                    cursor.close();
                }
                return zzah;
            } catch (SQLiteException e2) {
                e = e2;
                this.zzs.zzay().zzd().zzc("Error updating daily counts. appId", zzei.zzn(str), e);
                if (cursor != null) {
                    cursor.close();
                }
                return zzah;
            }
        } catch (SQLiteException e3) {
            e = e3;
        } catch (Throwable th3) {
            th = th3;
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    public final zzap zzn(String str, String str2) {
        Cursor cursor;
        Throwable th;
        Cursor cursor2;
        SQLiteException e;
        Boolean bool;
        long j;
        try {
            Preconditions.checkNotEmpty(str);
            Preconditions.checkNotEmpty(str2);
            zzg();
            zzY();
            try {
                boolean z = false;
                Cursor query = zzh().query("events", (String[]) new ArrayList(Arrays.asList("lifetime_count", "current_bundle_count", "last_fire_timestamp", "last_bundled_timestamp", "last_bundled_day", "last_sampled_complex_event_id", "last_sampling_rate", "last_exempt_from_sampling", "current_session_count")).toArray(new String[0]), "app_id=? and name=?", new String[]{str, str2}, null, null, null);
                try {
                    if (!query.moveToFirst()) {
                        if (query != null) {
                            query.close();
                        }
                        return null;
                    }
                    long j2 = query.getLong(0);
                    long j3 = query.getLong(1);
                    long j4 = query.getLong(2);
                    long j5 = query.isNull(3) ? 0 : query.getLong(3);
                    Long valueOf = query.isNull(4) ? null : Long.valueOf(query.getLong(4));
                    Long valueOf2 = query.isNull(5) ? null : Long.valueOf(query.getLong(5));
                    Long valueOf3 = query.isNull(6) ? null : Long.valueOf(query.getLong(6));
                    if (!query.isNull(7)) {
                        if (query.getLong(7) == 1) {
                            z = true;
                        }
                        bool = Boolean.valueOf(z);
                    } else {
                        bool = null;
                    }
                    if (query.isNull(8)) {
                        j = 0;
                    } else {
                        j = query.getLong(8);
                    }
                    cursor2 = query;
                    try {
                        zzap zzap = new zzap(str, str2, j2, j3, j, j4, j5, valueOf, valueOf2, valueOf3, bool);
                        if (cursor2.moveToNext()) {
                            this.zzs.zzay().zzd().zzb("Got multiple records for event aggregates, expected one. appId", zzei.zzn(str));
                        }
                        if (cursor2 != null) {
                            cursor2.close();
                        }
                        return zzap;
                    } catch (SQLiteException e2) {
                        e = e2;
                        this.zzs.zzay().zzd().zzd("Error querying events. appId", zzei.zzn(str), this.zzs.zzj().zzc(str2), e);
                        if (cursor2 != null) {
                            cursor2.close();
                        }
                        return null;
                    }
                } catch (SQLiteException e3) {
                    e = e3;
                    cursor2 = query;
                } catch (Throwable th2) {
                    th = th2;
                    cursor = query;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            } catch (SQLiteException e4) {
                e = e4;
                cursor2 = null;
            } catch (Throwable th3) {
                th = th3;
                cursor = null;
            }
        } catch (Throwable th4) {
            th = th4;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00b0  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final zzks zzp(String str, String str2) {
        Cursor cursor;
        Throwable th;
        SQLiteException e;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzg();
        zzY();
        try {
            cursor = zzh().query("user_attributes", new String[]{"set_timestamp", "value", "origin"}, "app_id=? and name=?", new String[]{str, str2}, null, null, null);
            try {
                try {
                    if (!cursor.moveToFirst()) {
                        if (cursor != null) {
                            cursor.close();
                        }
                        return null;
                    }
                    long j = cursor.getLong(0);
                    Object zzq = zzq(cursor, 1);
                    if (zzq == null) {
                        if (cursor != null) {
                            cursor.close();
                        }
                        return null;
                    }
                    zzks zzks = new zzks(str, cursor.getString(2), str2, j, zzq);
                    if (cursor.moveToNext()) {
                        this.zzs.zzay().zzd().zzb("Got multiple records for user property, expected one. appId", zzei.zzn(str));
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                    return zzks;
                } catch (SQLiteException e2) {
                    e = e2;
                    this.zzs.zzay().zzd().zzd("Error querying user property. appId", zzei.zzn(str), this.zzs.zzj().zze(str2), e);
                    if (cursor != null) {
                        cursor.close();
                    }
                    return null;
                }
            } catch (Throwable th2) {
                th = th2;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } catch (SQLiteException e3) {
            e = e3;
            cursor = null;
        } catch (Throwable th3) {
            th = th3;
            cursor = null;
            if (cursor != null) {
            }
            throw th;
        }
    }

    final Object zzq(Cursor cursor, int i) {
        int type = cursor.getType(i);
        if (type == 0) {
            this.zzs.zzay().zzd().zza("Loaded invalid null value from database");
            return null;
        } else if (type == 1) {
            return Long.valueOf(cursor.getLong(i));
        } else {
            if (type == 2) {
                return Double.valueOf(cursor.getDouble(i));
            }
            if (type == 3) {
                return cursor.getString(i);
            }
            if (type != 4) {
                this.zzs.zzay().zzd().zzb("Loaded invalid unknown value type, ignoring it", Integer.valueOf(type));
                return null;
            }
            this.zzs.zzay().zzd().zza("Loaded invalid blob type value, ignoring it");
            return null;
        }
    }

    public final String zzr() {
        Throwable th;
        Cursor cursor;
        SQLiteException e;
        Cursor cursor2;
        try {
            cursor = null;
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            cursor2 = zzh().rawQuery("select app_id from queue order by has_realtime desc, rowid asc limit 1;", null);
            try {
                if (cursor2.moveToFirst()) {
                    String string = cursor2.getString(0);
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    return string;
                }
                if (cursor2 != null) {
                    cursor2.close();
                }
                return null;
            } catch (SQLiteException e2) {
                e = e2;
                this.zzs.zzay().zzd().zzb("Database error getting next bundle app id", e);
                if (cursor2 != null) {
                    cursor2.close();
                }
                return null;
            }
        } catch (SQLiteException e3) {
            e = e3;
            cursor2 = null;
        } catch (Throwable th3) {
            th = th3;
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    public final List<zzab> zzs(String str, String str2, String str3) {
        Preconditions.checkNotEmpty(str);
        zzg();
        zzY();
        ArrayList arrayList = new ArrayList(3);
        arrayList.add(str);
        StringBuilder sb = new StringBuilder("app_id=?");
        if (!TextUtils.isEmpty(str2)) {
            arrayList.add(str2);
            sb.append(" and origin=?");
        }
        if (!TextUtils.isEmpty(str3)) {
            arrayList.add(String.valueOf(str3).concat("*"));
            sb.append(" and name glob ?");
        }
        return zzt(sb.toString(), (String[]) arrayList.toArray(new String[arrayList.size()]));
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0059, code lost:
        r2 = r27.zzs.zzay().zzd();
        r27.zzs.zzf();
        r2.zzb("Read more than the max allowed conditional properties, ignoring extra", 1000);
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final List<zzab> zzt(String str, String[] strArr) {
        Throwable th;
        ArrayList arrayList;
        Cursor cursor;
        SQLiteException e;
        try {
            zzg();
            zzY();
            arrayList = new ArrayList();
            cursor = null;
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            SQLiteDatabase zzh2 = zzh();
            String[] strArr2 = {"app_id", "origin", AppMeasurementSdk.ConditionalUserProperty.NAME, "value", AppMeasurementSdk.ConditionalUserProperty.ACTIVE, AppMeasurementSdk.ConditionalUserProperty.TRIGGER_EVENT_NAME, AppMeasurementSdk.ConditionalUserProperty.TRIGGER_TIMEOUT, "timed_out_event", AppMeasurementSdk.ConditionalUserProperty.CREATION_TIMESTAMP, "triggered_event", AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_TIMESTAMP, AppMeasurementSdk.ConditionalUserProperty.TIME_TO_LIVE, "expired_event"};
            this.zzs.zzf();
            cursor = zzh2.query("conditional_properties", strArr2, str, strArr, null, null, "rowid", "1001");
            try {
                if (cursor.moveToFirst()) {
                    while (true) {
                        int size = arrayList.size();
                        this.zzs.zzf();
                        if (size < 1000) {
                            String string = cursor.getString(0);
                            String string2 = cursor.getString(1);
                            String string3 = cursor.getString(2);
                            Object zzq = zzq(cursor, 3);
                            boolean z = cursor.getInt(4) != 0;
                            String string4 = cursor.getString(5);
                            long j = cursor.getLong(6);
                            zzat zzat = (zzat) this.zzf.zzu().zzh(cursor.getBlob(7), zzat.CREATOR);
                            arrayList.add(new zzab(string, string2, new zzkq(string3, cursor.getLong(10), zzq, string2), cursor.getLong(8), z, string4, zzat, j, (zzat) this.zzf.zzu().zzh(cursor.getBlob(9), zzat.CREATOR), cursor.getLong(11), (zzat) this.zzf.zzu().zzh(cursor.getBlob(12), zzat.CREATOR)));
                            if (!cursor.moveToNext()) {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                    return arrayList;
                }
                if (cursor != null) {
                    cursor.close();
                }
                return arrayList;
            } catch (SQLiteException e2) {
                e = e2;
                this.zzs.zzay().zzd().zzb("Error querying conditional user property value", e);
                List<zzab> emptyList = Collections.emptyList();
                if (cursor != null) {
                    cursor.close();
                }
                return emptyList;
            }
        } catch (SQLiteException e3) {
            e = e3;
        } catch (Throwable th3) {
            th = th3;
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x00af  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final List<zzks> zzu(String str) {
        Throwable th;
        SQLiteException e;
        Preconditions.checkNotEmpty(str);
        zzg();
        zzY();
        ArrayList arrayList = new ArrayList();
        Cursor cursor = null;
        try {
            this.zzs.zzf();
            cursor = zzh().query("user_attributes", new String[]{AppMeasurementSdk.ConditionalUserProperty.NAME, "origin", "set_timestamp", "value"}, "app_id=?", new String[]{str}, null, null, "rowid", "1000");
            try {
                try {
                    if (cursor.moveToFirst()) {
                        do {
                            String string = cursor.getString(0);
                            String string2 = cursor.getString(1);
                            if (string2 == null) {
                                string2 = "";
                            }
                            long j = cursor.getLong(2);
                            Object zzq = zzq(cursor, 3);
                            if (zzq == null) {
                                this.zzs.zzay().zzd().zzb("Read invalid user property value, ignoring it. appId", zzei.zzn(str));
                            } else {
                                arrayList.add(new zzks(str, string2, string, j, zzq));
                            }
                        } while (cursor.moveToNext());
                        if (cursor != null) {
                            cursor.close();
                        }
                        return arrayList;
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                    return arrayList;
                } catch (SQLiteException e2) {
                    e = e2;
                    this.zzs.zzay().zzd().zzc("Error querying user properties. appId", zzei.zzn(str), e);
                    List<zzks> emptyList = Collections.emptyList();
                    if (cursor != null) {
                        cursor.close();
                    }
                    return emptyList;
                }
            } catch (Throwable th2) {
                th = th2;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } catch (SQLiteException e3) {
            e = e3;
        } catch (Throwable th3) {
            th = th3;
            if (cursor != null) {
            }
            throw th;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x00a0, code lost:
        r2 = r16.zzs.zzay().zzd();
        r16.zzs.zzf();
        r2.zzb("Read more than the max allowed user properties, ignoring excess", 1000);
     */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0125  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final List<zzks> zzv(String str, String str2, String str3) {
        Throwable th;
        String str4;
        SQLiteException e;
        ArrayList arrayList;
        try {
            Preconditions.checkNotEmpty(str);
            zzg();
            zzY();
            ArrayList arrayList2 = new ArrayList();
            Cursor cursor = null;
            try {
                try {
                    arrayList = new ArrayList(3);
                } catch (SQLiteException e2) {
                    e = e2;
                }
                try {
                    arrayList.add(str);
                    StringBuilder sb = new StringBuilder("app_id=?");
                    if (!TextUtils.isEmpty(str2)) {
                        str4 = str2;
                        try {
                            arrayList.add(str4);
                            sb.append(" and origin=?");
                        } catch (SQLiteException e3) {
                            e = e3;
                            this.zzs.zzay().zzd().zzd("(2)Error querying user properties", zzei.zzn(str), str4, e);
                            List<zzks> emptyList = Collections.emptyList();
                            if (cursor != null) {
                            }
                            return emptyList;
                        }
                    } else {
                        str4 = str2;
                    }
                    if (!TextUtils.isEmpty(str3)) {
                        arrayList.add(String.valueOf(str3).concat("*"));
                        sb.append(" and name glob ?");
                    }
                    String[] strArr = (String[]) arrayList.toArray(new String[arrayList.size()]);
                    SQLiteDatabase zzh2 = zzh();
                    String[] strArr2 = {AppMeasurementSdk.ConditionalUserProperty.NAME, "set_timestamp", "value", "origin"};
                    String sb2 = sb.toString();
                    this.zzs.zzf();
                    cursor = zzh2.query("user_attributes", strArr2, sb2, strArr, null, null, "rowid", "1001");
                } catch (SQLiteException e4) {
                    e = e4;
                    str4 = str2;
                    this.zzs.zzay().zzd().zzd("(2)Error querying user properties", zzei.zzn(str), str4, e);
                    List<zzks> emptyList2 = Collections.emptyList();
                    if (cursor != null) {
                        cursor.close();
                    }
                    return emptyList2;
                }
                try {
                    if (!cursor.moveToFirst()) {
                        if (cursor != null) {
                            cursor.close();
                        }
                        return arrayList2;
                    }
                    while (true) {
                        int size = arrayList2.size();
                        this.zzs.zzf();
                        if (size < 1000) {
                            String string = cursor.getString(0);
                            long j = cursor.getLong(1);
                            Object zzq = zzq(cursor, 2);
                            str4 = cursor.getString(3);
                            if (zzq == null) {
                                this.zzs.zzay().zzd().zzd("(2)Read invalid user property value, ignoring it", zzei.zzn(str), str4, str3);
                            } else {
                                arrayList2.add(new zzks(str, str4, string, j, zzq));
                            }
                            if (!cursor.moveToNext()) {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                    return arrayList2;
                } catch (SQLiteException e5) {
                    e = e5;
                    this.zzs.zzay().zzd().zzd("(2)Error querying user properties", zzei.zzn(str), str4, e);
                    List<zzks> emptyList22 = Collections.emptyList();
                    if (cursor != null) {
                    }
                    return emptyList22;
                }
            } catch (Throwable th2) {
                th = th2;
                if (0 != 0) {
                    cursor.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public final void zzw() {
        zzY();
        zzh().beginTransaction();
    }

    public final void zzx() {
        zzY();
        zzh().endTransaction();
    }

    public final void zzy(List<Long> list) {
        zzg();
        zzY();
        Preconditions.checkNotNull(list);
        Preconditions.checkNotZero(list.size());
        if (zzK()) {
            String join = TextUtils.join(",", list);
            StringBuilder sb = new StringBuilder(String.valueOf(join).length() + 2);
            sb.append("(");
            sb.append(join);
            sb.append(")");
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder(String.valueOf(sb2).length() + 80);
            sb3.append("SELECT COUNT(1) FROM queue WHERE rowid IN ");
            sb3.append(sb2);
            sb3.append(" AND retry_count =  2147483647 LIMIT 1");
            if (zzab(sb3.toString(), null) > 0) {
                this.zzs.zzay().zzk().zza("The number of upload retries exceeds the limit. Will remain unchanged.");
            }
            try {
                SQLiteDatabase zzh2 = zzh();
                StringBuilder sb4 = new StringBuilder(String.valueOf(sb2).length() + 127);
                sb4.append("UPDATE queue SET retry_count = IFNULL(retry_count, 0) + 1 WHERE rowid IN ");
                sb4.append(sb2);
                sb4.append(" AND (retry_count IS NULL OR retry_count < ");
                sb4.append(Integer.MAX_VALUE);
                sb4.append(")");
                zzh2.execSQL(sb4.toString());
            } catch (SQLiteException e) {
                this.zzs.zzay().zzd().zzb("Error incrementing retry count. error", e);
            }
        }
    }

    public final void zzz() {
        zzg();
        zzY();
        if (zzK()) {
            long zza2 = this.zzf.zzs().zza.zza();
            long elapsedRealtime = this.zzs.zzav().elapsedRealtime();
            long abs = Math.abs(elapsedRealtime - zza2);
            this.zzs.zzf();
            if (abs > zzdw.zzx.zza(null).longValue()) {
                this.zzf.zzs().zza.zzb(elapsedRealtime);
                zzg();
                zzY();
                if (zzK()) {
                    SQLiteDatabase zzh2 = zzh();
                    this.zzs.zzf();
                    int delete = zzh2.delete("queue", "abs(bundle_end_timestamp - ?) > cast(? as integer)", new String[]{String.valueOf(this.zzs.zzav().currentTimeMillis()), String.valueOf(zzaf.zzA())});
                    if (delete > 0) {
                        this.zzs.zzay().zzj().zzb("Deleted stale rows. rowsDeleted", Integer.valueOf(delete));
                    }
                }
            }
        }
    }
}
