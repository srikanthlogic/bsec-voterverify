package com.google.android.gms.measurement.internal;

import android.util.Log;
import androidx.exifinterface.media.ExifInterface;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzef implements Runnable {
    final /* synthetic */ int zza;
    final /* synthetic */ String zzb;
    final /* synthetic */ Object zzc;
    final /* synthetic */ Object zzd;
    final /* synthetic */ Object zze;
    final /* synthetic */ zzei zzf;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzef(zzei zzei, int i, String str, Object obj, Object obj2, Object obj3) {
        this.zzf = zzei;
        this.zza = i;
        this.zzb = str;
        this.zzc = obj;
        this.zzd = obj2;
        this.zze = obj3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzex zzm = this.zzf.zzs.zzm();
        if (zzm.zzx()) {
            if (this.zzf.zza == 0) {
                if (this.zzf.zzs.zzf().zzy()) {
                    zzei zzei = this.zzf;
                    zzei.zzs.zzaw();
                    zzei.zza = 'C';
                } else {
                    zzei zzei2 = this.zzf;
                    zzei2.zzs.zzaw();
                    zzei2.zza = 'c';
                }
            }
            if (this.zzf.zzb < 0) {
                zzei zzei3 = this.zzf;
                zzei3.zzs.zzf().zzh();
                zzei3.zzb = 42097;
            }
            char charAt = "01VDIWEA?".charAt(this.zza);
            char c = this.zzf.zza;
            long j = this.zzf.zzb;
            String zzo = zzei.zzo(true, this.zzb, this.zzc, this.zzd, this.zze);
            StringBuilder sb = new StringBuilder(String.valueOf(zzo).length() + 24);
            sb.append(ExifInterface.GPS_MEASUREMENT_2D);
            sb.append(charAt);
            sb.append(c);
            sb.append(j);
            sb.append(":");
            sb.append(zzo);
            String sb2 = sb.toString();
            if (sb2.length() > 1024) {
                sb2 = this.zzb.substring(0, 1024);
            }
            zzev zzev = zzm.zzb;
            if (zzev != null) {
                zzev.zzb(sb2, 1);
                return;
            }
            return;
        }
        Log.println(6, this.zzf.zzq(), "Persisted config not initialized. Not logging error/warn");
    }
}
