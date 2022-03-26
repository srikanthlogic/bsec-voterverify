package com.google.android.gms.measurement.internal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzak {
    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0029, code lost:
        if (r0 == false) goto L_0x0045;
     */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00fe  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static void zza(zzei zzei, SQLiteDatabase sQLiteDatabase, String str, String str2, String str3, String[] strArr) throws SQLiteException {
        Cursor cursor;
        SQLiteException e;
        if (zzei != null) {
            try {
                cursor = sQLiteDatabase.query("SQLITE_MASTER", new String[]{AppMeasurementSdk.ConditionalUserProperty.NAME}, "name=?", new String[]{str}, null, null, null);
            } catch (SQLiteException e2) {
                e = e2;
                cursor = null;
            } catch (Throwable th) {
                th = th;
                cursor = null;
            }
            try {
                boolean moveToFirst = cursor.moveToFirst();
                if (cursor != null) {
                    cursor.close();
                }
            } catch (SQLiteException e3) {
                e = e3;
                try {
                    zzei.zzk().zzc("Error querying for table", str, e);
                    if (cursor != null) {
                        cursor.close();
                    }
                    sQLiteDatabase.execSQL(str2);
                    try {
                        HashSet hashSet = new HashSet();
                        StringBuilder sb = new StringBuilder(str.length() + 22);
                        sb.append("SELECT * FROM ");
                        sb.append(str);
                        sb.append(" LIMIT 0");
                        Cursor rawQuery = sQLiteDatabase.rawQuery(sb.toString(), null);
                        Collections.addAll(hashSet, rawQuery.getColumnNames());
                        rawQuery.close();
                        String[] split = str3.split(",");
                        for (String str4 : split) {
                            if (!hashSet.remove(str4)) {
                                StringBuilder sb2 = new StringBuilder(str.length() + 35 + String.valueOf(str4).length());
                                sb2.append("Table ");
                                sb2.append(str);
                                sb2.append(" is missing required column: ");
                                sb2.append(str4);
                                throw new SQLiteException(sb2.toString());
                            }
                        }
                        if (strArr != null) {
                            for (int i = 0; i < strArr.length; i += 2) {
                                if (!hashSet.remove(strArr[i])) {
                                    sQLiteDatabase.execSQL(strArr[i + 1]);
                                }
                            }
                        }
                        if (!hashSet.isEmpty()) {
                            zzei.zzk().zzc("Table has extra columns. table, columns", str, TextUtils.join(", ", hashSet));
                        }
                    } catch (SQLiteException e4) {
                        zzei.zzd().zzb("Failed to verify columns on table that was just created", str);
                        throw e4;
                    }
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
        } else {
            throw new IllegalArgumentException("Monitor must not be null");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void zzb(zzei zzei, SQLiteDatabase sQLiteDatabase) {
        if (zzei != null) {
            File file = new File(sQLiteDatabase.getPath());
            if (!file.setReadable(false, false)) {
                zzei.zzk().zza("Failed to turn off database read permission");
            }
            if (!file.setWritable(false, false)) {
                zzei.zzk().zza("Failed to turn off database write permission");
            }
            if (!file.setReadable(true, true)) {
                zzei.zzk().zza("Failed to turn on database read permission for owner");
            }
            if (!file.setWritable(true, true)) {
                zzei.zzk().zza("Failed to turn on database write permission for owner");
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Monitor must not be null");
    }
}
