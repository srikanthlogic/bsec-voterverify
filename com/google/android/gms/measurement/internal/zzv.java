package com.google.android.gms.measurement.internal;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzfn;
import com.google.android.gms.internal.measurement.zzfo;
import com.google.android.gms.internal.measurement.zzfs;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzv {
    final /* synthetic */ zzz zza;
    private zzfo zzb;
    private Long zzc;
    private long zzd;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzv(zzz zzz, zzu zzu) {
        this.zza = zzz;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x00a5, code lost:
        if (r14 != null) goto L_0x00a7;
     */
    /* JADX WARN: Removed duplicated region for block: B:44:0x010f  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x01f1  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final zzfo zza(String str, zzfo zzfo) {
        Cursor cursor;
        SQLiteException e;
        Cursor cursor2;
        String zzh = zzfo.zzh();
        List<zzfs> zzi = zzfo.zzi();
        this.zza.zzf.zzu();
        Long l = (Long) zzkp.zzD(zzfo, "_eid");
        if (l != null) {
            if (zzh.equals("_ep")) {
                Preconditions.checkNotNull(l);
                this.zza.zzf.zzu();
                String str2 = (String) zzkp.zzD(zzfo, "_en");
                Pair pair = null;
                if (TextUtils.isEmpty(str2)) {
                    this.zza.zzs.zzay().zzh().zzb("Extra parameter without an event name. eventId", l);
                    return null;
                }
                if (this.zzb == null || this.zzc == null || l.longValue() != this.zzc.longValue()) {
                    zzaj zzi2 = this.zza.zzf.zzi();
                    zzi2.zzg();
                    zzi2.zzY();
                    try {
                        cursor2 = zzi2.zzh().rawQuery("select main_event, children_to_process from main_event_params where app_id=? and event_id=?", new String[]{str, String.valueOf(l)});
                    } catch (SQLiteException e2) {
                        e = e2;
                        cursor = null;
                    } catch (Throwable th) {
                        th = th;
                        cursor2 = null;
                    }
                    try {
                    } catch (SQLiteException e3) {
                        e = e3;
                        cursor = cursor2;
                        try {
                            zzi2.zzs.zzay().zzd().zzb("Error selecting main event", e);
                            if (cursor != null) {
                                cursor.close();
                            }
                            pair = null;
                            if (pair != null) {
                            }
                            this.zza.zzs.zzay().zzh().zzc("Extra parameter without existing main event. eventName, eventId", str2, l);
                            return null;
                        } catch (Throwable th2) {
                            th = th2;
                            cursor2 = cursor;
                            if (cursor2 != null) {
                                cursor2.close();
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        if (cursor2 != null) {
                        }
                        throw th;
                    }
                    if (!cursor2.moveToFirst()) {
                        zzi2.zzs.zzay().zzj().zza("Main event not found");
                    } else {
                        try {
                            pair = Pair.create(((zzfn) zzkp.zzl(zzfo.zze(), cursor2.getBlob(0))).zzaA(), Long.valueOf(cursor2.getLong(1)));
                            if (cursor2 != null) {
                                cursor2.close();
                            }
                        } catch (IOException e4) {
                            zzi2.zzs.zzay().zzd().zzd("Failed to merge main event. appId, eventId", zzei.zzn(str), l, e4);
                            if (cursor2 != null) {
                                cursor2.close();
                            }
                            pair = null;
                            if (pair != null) {
                            }
                            this.zza.zzs.zzay().zzh().zzc("Extra parameter without existing main event. eventName, eventId", str2, l);
                            return null;
                        }
                        if (pair != null || pair.first == null) {
                            this.zza.zzs.zzay().zzh().zzc("Extra parameter without existing main event. eventName, eventId", str2, l);
                            return null;
                        }
                        this.zzb = (zzfo) pair.first;
                        this.zzd = ((Long) pair.second).longValue();
                        this.zza.zzf.zzu();
                        this.zzc = (Long) zzkp.zzD(this.zzb, "_eid");
                    }
                }
                long j = this.zzd - 1;
                this.zzd = j;
                if (j <= 0) {
                    zzaj zzi3 = this.zza.zzf.zzi();
                    zzi3.zzg();
                    zzi3.zzs.zzay().zzj().zzb("Clearing complex main event info. appId", str);
                    try {
                        zzi3.zzh().execSQL("delete from main_event_params where app_id=?", new String[]{str});
                    } catch (SQLiteException e5) {
                        zzi3.zzs.zzay().zzd().zzb("Error clearing complex main event", e5);
                    }
                } else {
                    this.zza.zzf.zzi().zzL(str, l, this.zzd, this.zzb);
                }
                ArrayList arrayList = new ArrayList();
                for (zzfs zzfs : this.zzb.zzi()) {
                    this.zza.zzf.zzu();
                    if (zzkp.zzC(zzfo, zzfs.zzg()) == null) {
                        arrayList.add(zzfs);
                    }
                }
                if (!arrayList.isEmpty()) {
                    arrayList.addAll(zzi);
                    zzi = arrayList;
                } else {
                    this.zza.zzs.zzay().zzh().zzb("No unique parameters in main event. eventName", str2);
                }
                zzh = str2;
            } else {
                this.zzc = l;
                this.zzb = zzfo;
                this.zza.zzf.zzu();
                long j2 = 0L;
                Object zzD = zzkp.zzD(zzfo, "_epc");
                if (zzD != null) {
                    j2 = zzD;
                }
                long longValue = ((Long) j2).longValue();
                this.zzd = longValue;
                if (longValue <= 0) {
                    this.zza.zzs.zzay().zzh().zzb("Complex event with zero extra param count. eventName", zzh);
                } else {
                    this.zza.zzf.zzi().zzL(str, (Long) Preconditions.checkNotNull(l), this.zzd, zzfo);
                }
            }
        }
        zzfn zzbv = zzfo.zzbv();
        zzbv.zzi(zzh);
        zzbv.zzg();
        zzbv.zzd(zzi);
        return zzbv.zzaA();
    }
}
