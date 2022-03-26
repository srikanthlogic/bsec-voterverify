package com.google.android.gms.measurement.internal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteFullException;
import android.os.Parcel;
import android.os.SystemClock;
import androidx.exifinterface.media.ExifInterface;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import java.util.ArrayList;
import java.util.List;
import okhttp3.internal.cache.DiskLruCache;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzec extends zzf {
    private final zzeb zza;
    private boolean zzb;

    public zzec(zzfs zzfs) {
        super(zzfs);
        Context zzau = this.zzs.zzau();
        this.zzs.zzf();
        this.zza = new zzeb(this, zzau, "google_app_measurement_local.db");
    }

    /* JADX WARN: Removed duplicated region for block: B:74:0x012c  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0131  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private final boolean zzq(int i, byte[] bArr) {
        SQLiteDatabase sQLiteDatabase;
        Throwable th;
        Cursor cursor;
        SQLiteFullException e;
        SQLiteException e2;
        zzg();
        boolean z = false;
        if (this.zzb) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("type", Integer.valueOf(i));
        contentValues.put("entry", bArr);
        this.zzs.zzf();
        int i2 = 0;
        int i3 = 5;
        for (int i4 = 5; i2 < i4; i4 = 5) {
            SQLiteDatabase sQLiteDatabase2 = null;
            r8 = null;
            r8 = null;
            sQLiteDatabase2 = null;
            Cursor cursor2 = null;
            try {
                sQLiteDatabase = zzh();
                try {
                    if (sQLiteDatabase == null) {
                        this.zzb = true;
                        return z;
                    }
                    sQLiteDatabase.beginTransaction();
                    cursor = sQLiteDatabase.rawQuery("select count(1) from messages", null);
                    long j = 0;
                    if (cursor != null) {
                        try {
                            if (cursor.moveToFirst()) {
                                int i5 = z ? 1 : 0;
                                int i6 = z ? 1 : 0;
                                int i7 = z ? 1 : 0;
                                j = cursor.getLong(i5);
                            }
                        } catch (SQLiteDatabaseLockedException e3) {
                            cursor2 = cursor;
                            try {
                                SystemClock.sleep((long) i3);
                                i3 += 20;
                                if (cursor2 != null) {
                                    cursor2.close();
                                }
                                if (sQLiteDatabase != null) {
                                    sQLiteDatabase.close();
                                }
                                i2++;
                                z = false;
                            } catch (Throwable th2) {
                                th = th2;
                                if (cursor2 != null) {
                                }
                                if (sQLiteDatabase != null) {
                                }
                                throw th;
                            }
                        } catch (SQLiteFullException e4) {
                            e = e4;
                            sQLiteDatabase2 = sQLiteDatabase;
                            this.zzs.zzay().zzd().zzb("Error writing entry; local database full", e);
                            this.zzb = true;
                            if (cursor != null) {
                                cursor.close();
                            }
                            if (sQLiteDatabase2 == null) {
                                i2++;
                                z = false;
                            }
                            sQLiteDatabase2.close();
                            i2++;
                            z = false;
                        } catch (SQLiteException e5) {
                            e2 = e5;
                            sQLiteDatabase2 = sQLiteDatabase;
                            if (sQLiteDatabase2 != null) {
                                try {
                                    if (sQLiteDatabase2.inTransaction()) {
                                        sQLiteDatabase2.endTransaction();
                                    }
                                } catch (Throwable th3) {
                                    th = th3;
                                    sQLiteDatabase = sQLiteDatabase2;
                                    cursor2 = cursor;
                                    if (cursor2 != null) {
                                        cursor2.close();
                                    }
                                    if (sQLiteDatabase != null) {
                                        sQLiteDatabase.close();
                                    }
                                    throw th;
                                }
                            }
                            this.zzs.zzay().zzd().zzb("Error writing entry to local database", e2);
                            this.zzb = true;
                            if (cursor != null) {
                                cursor.close();
                            }
                            if (sQLiteDatabase2 == null) {
                                i2++;
                                z = false;
                            }
                            sQLiteDatabase2.close();
                            i2++;
                            z = false;
                        } catch (Throwable th4) {
                            th = th4;
                            cursor2 = cursor;
                            if (cursor2 != null) {
                            }
                            if (sQLiteDatabase != null) {
                            }
                            throw th;
                        }
                    }
                    if (j >= 100000) {
                        this.zzs.zzay().zzd().zza("Data loss, local db full");
                        long j2 = (100000 - j) + 1;
                        String[] strArr = new String[1];
                        String l = Long.toString(j2);
                        char c = z ? 1 : 0;
                        char c2 = z ? 1 : 0;
                        char c3 = z ? 1 : 0;
                        strArr[c] = l;
                        long delete = (long) sQLiteDatabase.delete("messages", "rowid in (select rowid from messages order by rowid asc limit ?)", strArr);
                        if (delete != j2) {
                            this.zzs.zzay().zzd().zzd("Different delete count than expected in local db. expected, received, difference", Long.valueOf(j2), Long.valueOf(delete), Long.valueOf(j2 - delete));
                        }
                    }
                    sQLiteDatabase.insertOrThrow("messages", null, contentValues);
                    sQLiteDatabase.setTransactionSuccessful();
                    sQLiteDatabase.endTransaction();
                    if (cursor != null) {
                        cursor.close();
                    }
                    sQLiteDatabase.close();
                    return true;
                } catch (SQLiteDatabaseLockedException e6) {
                } catch (SQLiteFullException e7) {
                    e = e7;
                    cursor = null;
                } catch (SQLiteException e8) {
                    e2 = e8;
                    cursor = null;
                }
            } catch (SQLiteDatabaseLockedException e9) {
                sQLiteDatabase = null;
            } catch (SQLiteFullException e10) {
                e = e10;
                cursor = null;
            } catch (SQLiteException e11) {
                e2 = e11;
                cursor = null;
            } catch (Throwable th5) {
                th = th5;
                sQLiteDatabase = null;
                if (cursor2 != null) {
                }
                if (sQLiteDatabase != null) {
                }
                throw th;
            }
        }
        this.zzs.zzay().zzj().zza("Failed to write entry to local database");
        return false;
    }

    @Override // com.google.android.gms.measurement.internal.zzf
    protected final boolean zzf() {
        return false;
    }

    final SQLiteDatabase zzh() throws SQLiteException {
        if (this.zzb) {
            return null;
        }
        SQLiteDatabase writableDatabase = this.zza.getWritableDatabase();
        if (writableDatabase != null) {
            return writableDatabase;
        }
        this.zzb = true;
        return null;
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:143:0x022e  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x023f  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x025c  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x0269  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x026e  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x0214 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:193:0x01ee A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:199:0x0262 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:200:0x0262 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:202:0x0262 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final List<AbstractSafeParcelable> zzi(int i) {
        SQLiteDatabase sQLiteDatabase;
        Cursor cursor;
        Throwable th;
        SQLiteFullException e;
        SQLiteException e2;
        Cursor cursor2;
        Throwable th2;
        long j;
        String[] strArr;
        String str;
        zzkq zzkq;
        zzab zzab;
        zzg();
        if (this.zzb) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        if (!zzl()) {
            return arrayList;
        }
        int i2 = 5;
        for (int i3 = 0; i3 < 5; i3++) {
            try {
                SQLiteDatabase zzh = zzh();
                if (zzh == null) {
                    this.zzb = true;
                    return null;
                }
                try {
                    zzh.beginTransaction();
                    try {
                        try {
                            cursor2 = zzh.query("messages", new String[]{"rowid"}, "type=?", new String[]{ExifInterface.GPS_MEASUREMENT_3D}, null, null, "rowid desc", DiskLruCache.VERSION_1);
                            try {
                                long j2 = -1;
                                try {
                                    if (cursor2.moveToFirst()) {
                                        j = cursor2.getLong(0);
                                        if (cursor2 != null) {
                                            cursor2.close();
                                        }
                                    } else {
                                        if (cursor2 != null) {
                                            cursor2.close();
                                        }
                                        j = -1;
                                    }
                                    if (j != -1) {
                                        str = "rowid<?";
                                        strArr = new String[]{String.valueOf(j)};
                                    } else {
                                        str = null;
                                        strArr = null;
                                    }
                                    cursor = zzh.query("messages", new String[]{"rowid", "type", "entry"}, str, strArr, null, null, "rowid asc", Integer.toString(100));
                                    while (cursor.moveToNext()) {
                                        try {
                                            j2 = cursor.getLong(0);
                                            int i4 = cursor.getInt(1);
                                            byte[] blob = cursor.getBlob(2);
                                            if (i4 == 0) {
                                                Parcel obtain = Parcel.obtain();
                                                try {
                                                    try {
                                                        obtain.unmarshall(blob, 0, blob.length);
                                                        obtain.setDataPosition(0);
                                                        zzat createFromParcel = zzat.CREATOR.createFromParcel(obtain);
                                                        obtain.recycle();
                                                        if (createFromParcel != null) {
                                                            arrayList.add(createFromParcel);
                                                        }
                                                    } catch (SafeParcelReader.ParseException e3) {
                                                        this.zzs.zzay().zzd().zza("Failed to load event from local database");
                                                        obtain.recycle();
                                                    }
                                                } catch (Throwable th3) {
                                                    obtain.recycle();
                                                    throw th3;
                                                }
                                            } else if (i4 == 1) {
                                                Parcel obtain2 = Parcel.obtain();
                                                try {
                                                    try {
                                                        obtain2.unmarshall(blob, 0, blob.length);
                                                        obtain2.setDataPosition(0);
                                                        zzkq = zzkq.CREATOR.createFromParcel(obtain2);
                                                        obtain2.recycle();
                                                    } catch (SafeParcelReader.ParseException e4) {
                                                        this.zzs.zzay().zzd().zza("Failed to load user property from local database");
                                                        obtain2.recycle();
                                                        zzkq = null;
                                                    }
                                                    if (zzkq != null) {
                                                        arrayList.add(zzkq);
                                                    }
                                                } catch (Throwable th4) {
                                                    obtain2.recycle();
                                                    throw th4;
                                                }
                                            } else if (i4 == 2) {
                                                Parcel obtain3 = Parcel.obtain();
                                                try {
                                                    try {
                                                        obtain3.unmarshall(blob, 0, blob.length);
                                                        obtain3.setDataPosition(0);
                                                        zzab = zzab.CREATOR.createFromParcel(obtain3);
                                                        obtain3.recycle();
                                                    } catch (SafeParcelReader.ParseException e5) {
                                                        this.zzs.zzay().zzd().zza("Failed to load conditional user property from local database");
                                                        obtain3.recycle();
                                                        zzab = null;
                                                    }
                                                    if (zzab != null) {
                                                        arrayList.add(zzab);
                                                    }
                                                } catch (Throwable th5) {
                                                    obtain3.recycle();
                                                    throw th5;
                                                }
                                            } else if (i4 == 3) {
                                                this.zzs.zzay().zzk().zza("Skipping app launch break");
                                            } else {
                                                this.zzs.zzay().zzd().zza("Unknown record type in local database");
                                            }
                                        } catch (SQLiteDatabaseLockedException e6) {
                                            sQLiteDatabase = zzh;
                                        } catch (SQLiteFullException e7) {
                                            e = e7;
                                            sQLiteDatabase = zzh;
                                        } catch (SQLiteException e8) {
                                            e2 = e8;
                                            sQLiteDatabase = zzh;
                                        } catch (Throwable th6) {
                                            th = th6;
                                            sQLiteDatabase = zzh;
                                        }
                                    }
                                    sQLiteDatabase = zzh;
                                } catch (SQLiteDatabaseLockedException e9) {
                                    sQLiteDatabase = zzh;
                                    cursor = null;
                                    SystemClock.sleep((long) i2);
                                    i2 += 20;
                                    if (cursor != null) {
                                        cursor.close();
                                    }
                                    if (sQLiteDatabase == null) {
                                    }
                                    sQLiteDatabase.close();
                                } catch (SQLiteFullException e10) {
                                    e = e10;
                                    sQLiteDatabase = zzh;
                                    cursor = null;
                                    this.zzs.zzay().zzd().zzb("Error reading entries from local database", e);
                                    this.zzb = true;
                                    if (cursor != null) {
                                        cursor.close();
                                    }
                                    if (sQLiteDatabase == null) {
                                    }
                                    sQLiteDatabase.close();
                                } catch (SQLiteException e11) {
                                    e2 = e11;
                                    sQLiteDatabase = zzh;
                                    cursor = null;
                                    if (sQLiteDatabase != null) {
                                        try {
                                            if (sQLiteDatabase.inTransaction()) {
                                                sQLiteDatabase.endTransaction();
                                            }
                                        } catch (Throwable th7) {
                                            th = th7;
                                            if (cursor != null) {
                                                cursor.close();
                                            }
                                            if (sQLiteDatabase != null) {
                                                sQLiteDatabase.close();
                                            }
                                            throw th;
                                        }
                                    }
                                    this.zzs.zzay().zzd().zzb("Error reading entries from local database", e2);
                                    this.zzb = true;
                                    if (cursor != null) {
                                        cursor.close();
                                    }
                                    if (sQLiteDatabase == null) {
                                    }
                                    sQLiteDatabase.close();
                                } catch (Throwable th8) {
                                    th = th8;
                                    sQLiteDatabase = zzh;
                                    cursor = null;
                                    if (cursor != null) {
                                    }
                                    if (sQLiteDatabase != null) {
                                    }
                                    throw th;
                                }
                                try {
                                    if (sQLiteDatabase.delete("messages", "rowid <= ?", new String[]{Long.toString(j2)}) < arrayList.size()) {
                                        this.zzs.zzay().zzd().zza("Fewer entries removed from local database than expected");
                                    }
                                    sQLiteDatabase.setTransactionSuccessful();
                                    sQLiteDatabase.endTransaction();
                                    if (cursor != null) {
                                        cursor.close();
                                    }
                                    sQLiteDatabase.close();
                                    return arrayList;
                                } catch (SQLiteDatabaseLockedException e12) {
                                    SystemClock.sleep((long) i2);
                                    i2 += 20;
                                    if (cursor != null) {
                                    }
                                    if (sQLiteDatabase == null) {
                                    }
                                    sQLiteDatabase.close();
                                } catch (SQLiteFullException e13) {
                                    e = e13;
                                    this.zzs.zzay().zzd().zzb("Error reading entries from local database", e);
                                    this.zzb = true;
                                    if (cursor != null) {
                                    }
                                    if (sQLiteDatabase == null) {
                                    }
                                    sQLiteDatabase.close();
                                } catch (SQLiteException e14) {
                                    e2 = e14;
                                    if (sQLiteDatabase != null) {
                                    }
                                    this.zzs.zzay().zzd().zzb("Error reading entries from local database", e2);
                                    this.zzb = true;
                                    if (cursor != null) {
                                    }
                                    if (sQLiteDatabase == null) {
                                    }
                                    sQLiteDatabase.close();
                                } catch (Throwable th9) {
                                    th = th9;
                                    if (cursor != null) {
                                    }
                                    if (sQLiteDatabase != null) {
                                    }
                                    throw th;
                                }
                            } catch (Throwable th10) {
                                th2 = th10;
                                sQLiteDatabase = zzh;
                                if (cursor2 != null) {
                                    try {
                                        cursor2.close();
                                    } catch (SQLiteDatabaseLockedException e15) {
                                        cursor = null;
                                        SystemClock.sleep((long) i2);
                                        i2 += 20;
                                        if (cursor != null) {
                                        }
                                        if (sQLiteDatabase == null) {
                                        }
                                        sQLiteDatabase.close();
                                    } catch (SQLiteFullException e16) {
                                        e = e16;
                                        cursor = null;
                                        this.zzs.zzay().zzd().zzb("Error reading entries from local database", e);
                                        this.zzb = true;
                                        if (cursor != null) {
                                        }
                                        if (sQLiteDatabase == null) {
                                        }
                                        sQLiteDatabase.close();
                                    } catch (SQLiteException e17) {
                                        e2 = e17;
                                        cursor = null;
                                        if (sQLiteDatabase != null) {
                                        }
                                        this.zzs.zzay().zzd().zzb("Error reading entries from local database", e2);
                                        this.zzb = true;
                                        if (cursor != null) {
                                        }
                                        if (sQLiteDatabase == null) {
                                        }
                                        sQLiteDatabase.close();
                                    } catch (Throwable th11) {
                                        th = th11;
                                        cursor = null;
                                        if (cursor != null) {
                                        }
                                        if (sQLiteDatabase != null) {
                                        }
                                        throw th;
                                    }
                                }
                                throw th2;
                                break;
                            }
                        } catch (Throwable th12) {
                            th2 = th12;
                            sQLiteDatabase = zzh;
                            cursor2 = null;
                            if (cursor2 != null) {
                            }
                            throw th2;
                            break;
                            break;
                        }
                    } catch (Throwable th13) {
                        th2 = th13;
                        sQLiteDatabase = zzh;
                    }
                } catch (SQLiteDatabaseLockedException e18) {
                    sQLiteDatabase = zzh;
                } catch (SQLiteFullException e19) {
                    e = e19;
                    sQLiteDatabase = zzh;
                } catch (SQLiteException e20) {
                    e2 = e20;
                    sQLiteDatabase = zzh;
                } catch (Throwable th14) {
                    th = th14;
                    sQLiteDatabase = zzh;
                }
            } catch (SQLiteDatabaseLockedException e21) {
                cursor = null;
                sQLiteDatabase = null;
            } catch (SQLiteFullException e22) {
                e = e22;
                cursor = null;
                sQLiteDatabase = null;
            } catch (SQLiteException e23) {
                e2 = e23;
                cursor = null;
                sQLiteDatabase = null;
            } catch (Throwable th15) {
                th = th15;
                cursor = null;
                sQLiteDatabase = null;
            }
        }
        this.zzs.zzay().zzk().zza("Failed to read events from database in reasonable time");
        return null;
    }

    public final void zzj() {
        int delete;
        zzg();
        try {
            SQLiteDatabase zzh = zzh();
            if (zzh != null && (delete = zzh.delete("messages", null, null)) > 0) {
                this.zzs.zzay().zzj().zzb("Reset local analytics data. records", Integer.valueOf(delete));
            }
        } catch (SQLiteException e) {
            this.zzs.zzay().zzd().zzb("Error resetting local analytics data. error", e);
        }
    }

    public final boolean zzk() {
        return zzq(3, new byte[0]);
    }

    final boolean zzl() {
        Context zzau = this.zzs.zzau();
        this.zzs.zzf();
        return zzau.getDatabasePath("google_app_measurement_local.db").exists();
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x008b  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final boolean zzm() {
        Throwable th;
        SQLiteFullException e;
        SQLiteException e2;
        zzg();
        if (!this.zzb && zzl()) {
            int i = 5;
            for (int i2 = 0; i2 < 5; i2++) {
                SQLiteDatabase sQLiteDatabase = null;
                try {
                    sQLiteDatabase = zzh();
                    try {
                        if (sQLiteDatabase == null) {
                            this.zzb = true;
                            return false;
                        }
                        sQLiteDatabase.beginTransaction();
                        sQLiteDatabase.delete("messages", "type == ?", new String[]{Integer.toString(3)});
                        sQLiteDatabase.setTransactionSuccessful();
                        sQLiteDatabase.endTransaction();
                        sQLiteDatabase.close();
                        return true;
                    } catch (SQLiteDatabaseLockedException e3) {
                        SystemClock.sleep((long) i);
                        i += 20;
                        if (sQLiteDatabase == null) {
                        }
                        sQLiteDatabase.close();
                    } catch (SQLiteFullException e4) {
                        e = e4;
                        this.zzs.zzay().zzd().zzb("Error deleting app launch break from local database", e);
                        this.zzb = true;
                        if (sQLiteDatabase == null) {
                        }
                        sQLiteDatabase.close();
                    } catch (SQLiteException e5) {
                        e2 = e5;
                        if (sQLiteDatabase != null) {
                            try {
                                if (sQLiteDatabase.inTransaction()) {
                                    sQLiteDatabase.endTransaction();
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                if (sQLiteDatabase != null) {
                                    sQLiteDatabase.close();
                                }
                                throw th;
                            }
                        }
                        this.zzs.zzay().zzd().zzb("Error deleting app launch break from local database", e2);
                        this.zzb = true;
                        if (sQLiteDatabase != null) {
                            sQLiteDatabase.close();
                        }
                    }
                } catch (SQLiteDatabaseLockedException e6) {
                } catch (SQLiteFullException e7) {
                    e = e7;
                } catch (SQLiteException e8) {
                    e2 = e8;
                } catch (Throwable th3) {
                    th = th3;
                    if (sQLiteDatabase != null) {
                    }
                    throw th;
                }
            }
            this.zzs.zzay().zzk().zza("Error deleting app launch break from local database in reasonable time");
        }
        return false;
    }

    public final boolean zzn(zzab zzab) {
        byte[] zzan = this.zzs.zzv().zzan(zzab);
        if (zzan.length <= 131072) {
            return zzq(2, zzan);
        }
        this.zzs.zzay().zzh().zza("Conditional user property too long for local database. Sending directly to service");
        return false;
    }

    public final boolean zzo(zzat zzat) {
        Parcel obtain = Parcel.obtain();
        zzau.zza(zzat, obtain, 0);
        byte[] marshall = obtain.marshall();
        obtain.recycle();
        if (marshall.length <= 131072) {
            return zzq(0, marshall);
        }
        this.zzs.zzay().zzh().zza("Event is too long for local database. Sending event directly to service");
        return false;
    }

    public final boolean zzp(zzkq zzkq) {
        Parcel obtain = Parcel.obtain();
        zzkr.zza(zzkq, obtain, 0);
        byte[] marshall = obtain.marshall();
        obtain.recycle();
        if (marshall.length <= 131072) {
            return zzq(1, marshall);
        }
        this.zzs.zzay().zzh().zza("User property too long for local database. Sending directly to service");
        return false;
    }
}
