package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import androidx.collection.ArrayMap;
import com.example.aadhaarfpoffline.tatvik.database.DBHelper;
import com.facebook.common.util.UriUtil;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.stats.ConnectionTracker;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.wrappers.Wrappers;
import com.google.android.gms.internal.measurement.zzfc;
import com.google.android.gms.internal.measurement.zzfn;
import com.google.android.gms.internal.measurement.zzfo;
import com.google.android.gms.internal.measurement.zzfr;
import com.google.android.gms.internal.measurement.zzfs;
import com.google.android.gms.internal.measurement.zzfv;
import com.google.android.gms.internal.measurement.zzfw;
import com.google.android.gms.internal.measurement.zzfx;
import com.google.android.gms.internal.measurement.zzfy;
import com.google.android.gms.internal.measurement.zzgg;
import com.google.android.gms.internal.measurement.zzgh;
import com.google.android.gms.internal.measurement.zzna;
import com.google.android.gms.internal.measurement.zzoq;
import com.google.android.gms.internal.measurement.zzpl;
import com.google.android.gms.internal.measurement.zzpx;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.common.net.HttpHeaders;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.sec.biometric.license.SecBiometricLicenseManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.zip.GZIPInputStream;
import kotlinx.coroutines.DebugKt;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzkn implements zzgn {
    private static volatile zzkn zzb;
    long zza;
    private final zzfj zzc;
    private final zzeo zzd;
    private zzaj zze;
    private zzeq zzf;
    private zzkb zzg;
    private zzz zzh;
    private final zzkp zzi;
    private zzia zzj;
    private zzjk zzk;
    private zzfa zzm;
    private final zzfs zzn;
    private boolean zzp;
    private List<Runnable> zzq;
    private int zzr;
    private int zzs;
    private boolean zzt;
    private boolean zzu;
    private boolean zzv;
    private FileLock zzw;
    private FileChannel zzx;
    private List<Long> zzy;
    private List<Long> zzz;
    private boolean zzo = false;
    private final zzkt zzC = new zzkk(this);
    private long zzA = -1;
    private final zzke zzl = new zzke(this);
    private final Map<String, zzag> zzB = new HashMap();

    zzkn(zzko zzko, zzfs zzfs) {
        Preconditions.checkNotNull(zzko);
        this.zzn = zzfs.zzp(zzko.zza, null, null);
        zzkp zzkp = new zzkp(this);
        zzkp.zzZ();
        this.zzi = zzkp;
        zzeo zzeo = new zzeo(this);
        zzeo.zzZ();
        this.zzd = zzeo;
        zzfj zzfj = new zzfj(this);
        zzfj.zzZ();
        this.zzc = zzfj;
        zzaz().zzp(new zzkf(this, zzko));
    }

    static final void zzY(zzfn zzfn, int i, String str) {
        List<zzfs> zzp = zzfn.zzp();
        for (int i2 = 0; i2 < zzp.size(); i2++) {
            if ("_err".equals(zzp.get(i2).zzg())) {
                return;
            }
        }
        zzfr zze = zzfs.zze();
        zze.zzj("_err");
        zze.zzi(Long.valueOf((long) i).longValue());
        zzfr zze2 = zzfs.zze();
        zze2.zzj("_ev");
        zze2.zzk(str);
        zzfn.zzf(zze.zzaA());
        zzfn.zzf(zze2.zzaA());
    }

    static final void zzZ(zzfn zzfn, String str) {
        List<zzfs> zzp = zzfn.zzp();
        for (int i = 0; i < zzp.size(); i++) {
            if (str.equals(zzp.get(i).zzg())) {
                zzfn.zzh(i);
                return;
            }
        }
    }

    private final zzp zzaa(String str) {
        String str2;
        zzaj zzaj = this.zze;
        zzak(zzaj);
        zzg zzj = zzaj.zzj(str);
        if (zzj == null || TextUtils.isEmpty(zzj.zzw())) {
            zzay().zzc().zzb("No app data available; dropping", str);
            return null;
        }
        Boolean zzab = zzab(zzj);
        if (zzab == null || zzab.booleanValue()) {
            String zzz = zzj.zzz();
            String zzw = zzj.zzw();
            long zzb2 = zzj.zzb();
            String zzv = zzj.zzv();
            long zzm = zzj.zzm();
            long zzj2 = zzj.zzj();
            boolean zzaj2 = zzj.zzaj();
            String zzx = zzj.zzx();
            long zza = zzj.zza();
            boolean zzai = zzj.zzai();
            String zzr = zzj.zzr();
            Boolean zzq = zzj.zzq();
            long zzk = zzj.zzk();
            List<String> zzC = zzj.zzC();
            zzoq.zzc();
            if (zzg().zzs(str, zzdw.zzad)) {
                str2 = zzj.zzy();
            } else {
                str2 = null;
            }
            return new zzp(str, zzz, zzw, zzb2, zzv, zzm, zzj2, (String) null, zzaj2, false, zzx, zza, 0L, 0, zzai, false, zzr, zzq, zzk, zzC, str2, zzh(str).zzi());
        }
        zzay().zzd().zzb("App version does not match; dropping. appId", zzei.zzn(str));
        return null;
    }

    private final Boolean zzab(zzg zzg) {
        try {
            if (zzg.zzb() != -2147483648L) {
                if (zzg.zzb() == ((long) Wrappers.packageManager(this.zzn.zzau()).getPackageInfo(zzg.zzt(), 0).versionCode)) {
                    return true;
                }
            } else {
                String str = Wrappers.packageManager(this.zzn.zzau()).getPackageInfo(zzg.zzt(), 0).versionName;
                String zzw = zzg.zzw();
                if (zzw != null && zzw.equals(str)) {
                    return true;
                }
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    private final void zzac() {
        zzaz().zzg();
        if (this.zzt || this.zzu || this.zzv) {
            zzay().zzj().zzd("Not stopping services. fetch, network, upload", Boolean.valueOf(this.zzt), Boolean.valueOf(this.zzu), Boolean.valueOf(this.zzv));
            return;
        }
        zzay().zzj().zza("Stopping uploading service(s)");
        List<Runnable> list = this.zzq;
        if (list != null) {
            for (Runnable runnable : list) {
                runnable.run();
            }
            ((List) Preconditions.checkNotNull(this.zzq)).clear();
        }
    }

    private final void zzae(zzfn zzfn, zzfn zzfn2) {
        Preconditions.checkArgument("_e".equals(zzfn.zzo()));
        zzak(this.zzi);
        zzfs zzC = zzkp.zzC(zzfn.zzaA(), "_et");
        if (zzC != null && zzC.zzw() && zzC.zzd() > 0) {
            long zzd = zzC.zzd();
            zzak(this.zzi);
            zzfs zzC2 = zzkp.zzC(zzfn2.zzaA(), "_et");
            if (zzC2 != null && zzC2.zzd() > 0) {
                zzd += zzC2.zzd();
            }
            zzak(this.zzi);
            zzkp.zzA(zzfn2, "_et", Long.valueOf(zzd));
            zzak(this.zzi);
            zzkp.zzA(zzfn, "_fr", 1L);
        }
    }

    private final void zzaf() {
        long j;
        long j2;
        zzaz().zzg();
        zzB();
        if (this.zza > 0) {
            long abs = 3600000 - Math.abs(zzav().elapsedRealtime() - this.zza);
            if (abs > 0) {
                zzay().zzj().zzb("Upload has been suspended. Will update scheduling later in approximately ms", Long.valueOf(abs));
                zzm().zzc();
                zzkb zzkb = this.zzg;
                zzak(zzkb);
                zzkb.zza();
                return;
            }
            this.zza = 0;
        }
        if (!this.zzn.zzM() || !zzai()) {
            zzay().zzj().zza("Nothing to upload or uploading impossible");
            zzm().zzc();
            zzkb zzkb2 = this.zzg;
            zzak(zzkb2);
            zzkb2.zza();
            return;
        }
        long currentTimeMillis = zzav().currentTimeMillis();
        zzg();
        long max = Math.max(0L, zzdw.zzz.zza(null).longValue());
        zzaj zzaj = this.zze;
        zzak(zzaj);
        boolean z = true;
        if (!zzaj.zzI()) {
            zzaj zzaj2 = this.zze;
            zzak(zzaj2);
            if (!zzaj2.zzH()) {
                z = false;
            }
        }
        if (z) {
            String zzl = zzg().zzl();
            if (TextUtils.isEmpty(zzl) || ".none.".equals(zzl)) {
                zzg();
                j = Math.max(0L, zzdw.zzt.zza(null).longValue());
            } else {
                zzg();
                j = Math.max(0L, zzdw.zzu.zza(null).longValue());
            }
        } else {
            zzg();
            j = Math.max(0L, zzdw.zzs.zza(null).longValue());
        }
        long zza = this.zzk.zzc.zza();
        long zza2 = this.zzk.zzd.zza();
        zzaj zzaj3 = this.zze;
        zzak(zzaj3);
        long zzd = zzaj3.zzd();
        zzaj zzaj4 = this.zze;
        zzak(zzaj4);
        long max2 = Math.max(zzd, zzaj4.zze());
        if (max2 == 0) {
            j2 = 0;
        } else {
            long abs2 = currentTimeMillis - Math.abs(max2 - currentTimeMillis);
            long abs3 = Math.abs(zza - currentTimeMillis);
            long abs4 = currentTimeMillis - Math.abs(zza2 - currentTimeMillis);
            long max3 = Math.max(currentTimeMillis - abs3, abs4);
            long j3 = abs2 + max;
            if (z && max3 > 0) {
                j3 = Math.min(abs2, max3) + j;
            }
            zzkp zzkp = this.zzi;
            zzak(zzkp);
            if (!zzkp.zzx(max3, j)) {
                j2 = max3 + j;
            } else {
                j2 = j3;
            }
            if (abs4 != 0 && abs4 >= abs2) {
                long j4 = j2;
                int i = 0;
                while (true) {
                    zzg();
                    if (i >= Math.min(20, Math.max(0, zzdw.zzB.zza(null).intValue()))) {
                        j2 = 0;
                        break;
                    }
                    zzg();
                    j4 += Math.max(0L, zzdw.zzA.zza(null).longValue()) * (1 << i);
                    if (j4 > abs4) {
                        j2 = j4;
                        break;
                    }
                    i++;
                }
            }
        }
        if (j2 != 0) {
            zzeo zzeo = this.zzd;
            zzak(zzeo);
            if (zzeo.zzc()) {
                long zza3 = this.zzk.zzb.zza();
                zzg();
                long max4 = Math.max(0L, zzdw.zzq.zza(null).longValue());
                zzkp zzkp2 = this.zzi;
                zzak(zzkp2);
                if (!zzkp2.zzx(zza3, max4)) {
                    j2 = Math.max(j2, zza3 + max4);
                }
                zzm().zzc();
                long currentTimeMillis2 = j2 - zzav().currentTimeMillis();
                if (currentTimeMillis2 <= 0) {
                    zzg();
                    currentTimeMillis2 = Math.max(0L, zzdw.zzv.zza(null).longValue());
                    this.zzk.zzc.zzb(zzav().currentTimeMillis());
                }
                zzay().zzj().zzb("Upload scheduled in approximately ms", Long.valueOf(currentTimeMillis2));
                zzkb zzkb3 = this.zzg;
                zzak(zzkb3);
                zzkb3.zzd(currentTimeMillis2);
                return;
            }
            zzay().zzj().zza("No network");
            zzm().zzb();
            zzkb zzkb4 = this.zzg;
            zzak(zzkb4);
            zzkb4.zza();
            return;
        }
        zzay().zzj().zza("Next upload time is 0");
        zzm().zzc();
        zzkb zzkb5 = this.zzg;
        zzak(zzkb5);
        zzkb5.zza();
    }

    private final boolean zzag(zzp zzp) {
        zzoq.zzc();
        return zzg().zzs(zzp.zza, zzdw.zzad) ? !TextUtils.isEmpty(zzp.zzb) || !TextUtils.isEmpty(zzp.zzu) || !TextUtils.isEmpty(zzp.zzq) : !TextUtils.isEmpty(zzp.zzb) || !TextUtils.isEmpty(zzp.zzq);
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:114:0x03c2 A[Catch: all -> 0x0d84, TryCatch #0 {all -> 0x0d84, blocks: (B:3:0x000a, B:5:0x0022, B:8:0x002a, B:9:0x0050, B:12:0x0060, B:15:0x0087, B:17:0x00bd, B:20:0x00cf, B:22:0x00d9, B:24:0x0100, B:26:0x0110, B:29:0x0132, B:31:0x0138, B:33:0x014a, B:35:0x0158, B:37:0x0168, B:38:0x0175, B:42:0x0182, B:45:0x0199, B:71:0x01fc, B:74:0x0206, B:76:0x0214, B:77:0x022e, B:79:0x023d, B:80:0x0258, B:83:0x0263, B:86:0x0294, B:87:0x02be, B:89:0x02f5, B:91:0x02fb, B:94:0x0307, B:96:0x033d, B:97:0x0358, B:99:0x035e, B:101:0x036c, B:102:0x0374, B:105:0x0380, B:108:0x0387, B:111:0x0390, B:112:0x03a8, B:114:0x03c2, B:115:0x03ce, B:117:0x03d4, B:120:0x03e9, B:123:0x03fc, B:127:0x0405, B:129:0x0411, B:131:0x041d, B:135:0x043e, B:138:0x0450, B:140:0x0456, B:142:0x0460, B:143:0x0466, B:145:0x0482, B:147:0x048d, B:150:0x04a1, B:152:0x04b4, B:154:0x04c0, B:160:0x04e5, B:162:0x04f3, B:165:0x0508, B:167:0x051b, B:169:0x0527, B:174:0x054a, B:176:0x0560, B:178:0x056c, B:181:0x057f, B:183:0x0592, B:185:0x05db, B:187:0x05e2, B:189:0x05e8, B:191:0x05f4, B:193:0x05fb, B:195:0x0601, B:197:0x060d, B:198:0x061f, B:200:0x0628, B:202:0x0632, B:204:0x0638, B:205:0x0650, B:207:0x0663, B:208:0x067b, B:209:0x0685, B:210:0x069d, B:215:0x06b3, B:217:0x06c1, B:219:0x06cc, B:220:0x06d4, B:222:0x06df, B:224:0x06e5, B:227:0x06f1, B:229:0x06fb, B:230:0x0702, B:233:0x0708, B:234:0x0715, B:237:0x071d, B:239:0x072f, B:240:0x073b, B:242:0x0743, B:243:0x0748, B:245:0x074f, B:246:0x0769, B:248:0x078e, B:250:0x079f, B:252:0x07a5, B:254:0x07b1, B:255:0x07e2, B:257:0x07e8, B:259:0x07f8, B:260:0x07fc, B:261:0x07ff, B:262:0x0802, B:263:0x0810, B:265:0x0816, B:267:0x0826, B:268:0x082d, B:270:0x0839, B:271:0x0840, B:272:0x0843, B:274:0x0881, B:275:0x0894, B:277:0x089a, B:280:0x08b2, B:282:0x08cd, B:284:0x08e4, B:286:0x08eb, B:288:0x08ef, B:290:0x08f3, B:292:0x08fd, B:293:0x0907, B:295:0x090b, B:297:0x0911, B:298:0x0921, B:299:0x092a, B:300:0x0937, B:302:0x094e, B:305:0x0955, B:307:0x096d, B:309:0x098f, B:310:0x0997, B:312:0x099d, B:314:0x09af, B:319:0x09c3, B:321:0x09d8, B:322:0x0a03, B:324:0x0a0f, B:326:0x0a24, B:329:0x0a68, B:333:0x0a80, B:335:0x0a87, B:337:0x0a96, B:339:0x0a9a, B:341:0x0a9e, B:343:0x0aa2, B:344:0x0aae, B:345:0x0abb, B:347:0x0ac1, B:349:0x0add, B:350:0x0ae4, B:351:0x0b00, B:353:0x0b08, B:354:0x0b15, B:357:0x0b2f, B:359:0x0b5b, B:361:0x0b6b, B:362:0x0b7d, B:364:0x0b89, B:365:0x0b95, B:366:0x0b9a, B:367:0x0ba4, B:369:0x0bb2, B:370:0x0bb8, B:371:0x0bc0, B:373:0x0bc6, B:375:0x0bde, B:377:0x0bf1, B:378:0x0c09, B:380:0x0c0f, B:382:0x0c19, B:383:0x0c1d, B:384:0x0c20, B:390:0x0c30, B:391:0x0c34, B:392:0x0c37, B:394:0x0c56, B:395:0x0c5a, B:396:0x0c5d, B:397:0x0c65, B:399:0x0c6b, B:401:0x0c81, B:404:0x0c88, B:405:0x0c90, B:407:0x0c9c, B:408:0x0ca2, B:409:0x0cb9, B:410:0x0cc9, B:411:0x0ce1, B:414:0x0ce9, B:415:0x0cee, B:416:0x0cfe, B:418:0x0d18, B:419:0x0d33, B:421:0x0d3d, B:424:0x0d4d, B:425:0x0d60, B:428:0x0d72), top: B:434:0x000a, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0481  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x048d A[Catch: all -> 0x0d84, TryCatch #0 {all -> 0x0d84, blocks: (B:3:0x000a, B:5:0x0022, B:8:0x002a, B:9:0x0050, B:12:0x0060, B:15:0x0087, B:17:0x00bd, B:20:0x00cf, B:22:0x00d9, B:24:0x0100, B:26:0x0110, B:29:0x0132, B:31:0x0138, B:33:0x014a, B:35:0x0158, B:37:0x0168, B:38:0x0175, B:42:0x0182, B:45:0x0199, B:71:0x01fc, B:74:0x0206, B:76:0x0214, B:77:0x022e, B:79:0x023d, B:80:0x0258, B:83:0x0263, B:86:0x0294, B:87:0x02be, B:89:0x02f5, B:91:0x02fb, B:94:0x0307, B:96:0x033d, B:97:0x0358, B:99:0x035e, B:101:0x036c, B:102:0x0374, B:105:0x0380, B:108:0x0387, B:111:0x0390, B:112:0x03a8, B:114:0x03c2, B:115:0x03ce, B:117:0x03d4, B:120:0x03e9, B:123:0x03fc, B:127:0x0405, B:129:0x0411, B:131:0x041d, B:135:0x043e, B:138:0x0450, B:140:0x0456, B:142:0x0460, B:143:0x0466, B:145:0x0482, B:147:0x048d, B:150:0x04a1, B:152:0x04b4, B:154:0x04c0, B:160:0x04e5, B:162:0x04f3, B:165:0x0508, B:167:0x051b, B:169:0x0527, B:174:0x054a, B:176:0x0560, B:178:0x056c, B:181:0x057f, B:183:0x0592, B:185:0x05db, B:187:0x05e2, B:189:0x05e8, B:191:0x05f4, B:193:0x05fb, B:195:0x0601, B:197:0x060d, B:198:0x061f, B:200:0x0628, B:202:0x0632, B:204:0x0638, B:205:0x0650, B:207:0x0663, B:208:0x067b, B:209:0x0685, B:210:0x069d, B:215:0x06b3, B:217:0x06c1, B:219:0x06cc, B:220:0x06d4, B:222:0x06df, B:224:0x06e5, B:227:0x06f1, B:229:0x06fb, B:230:0x0702, B:233:0x0708, B:234:0x0715, B:237:0x071d, B:239:0x072f, B:240:0x073b, B:242:0x0743, B:243:0x0748, B:245:0x074f, B:246:0x0769, B:248:0x078e, B:250:0x079f, B:252:0x07a5, B:254:0x07b1, B:255:0x07e2, B:257:0x07e8, B:259:0x07f8, B:260:0x07fc, B:261:0x07ff, B:262:0x0802, B:263:0x0810, B:265:0x0816, B:267:0x0826, B:268:0x082d, B:270:0x0839, B:271:0x0840, B:272:0x0843, B:274:0x0881, B:275:0x0894, B:277:0x089a, B:280:0x08b2, B:282:0x08cd, B:284:0x08e4, B:286:0x08eb, B:288:0x08ef, B:290:0x08f3, B:292:0x08fd, B:293:0x0907, B:295:0x090b, B:297:0x0911, B:298:0x0921, B:299:0x092a, B:300:0x0937, B:302:0x094e, B:305:0x0955, B:307:0x096d, B:309:0x098f, B:310:0x0997, B:312:0x099d, B:314:0x09af, B:319:0x09c3, B:321:0x09d8, B:322:0x0a03, B:324:0x0a0f, B:326:0x0a24, B:329:0x0a68, B:333:0x0a80, B:335:0x0a87, B:337:0x0a96, B:339:0x0a9a, B:341:0x0a9e, B:343:0x0aa2, B:344:0x0aae, B:345:0x0abb, B:347:0x0ac1, B:349:0x0add, B:350:0x0ae4, B:351:0x0b00, B:353:0x0b08, B:354:0x0b15, B:357:0x0b2f, B:359:0x0b5b, B:361:0x0b6b, B:362:0x0b7d, B:364:0x0b89, B:365:0x0b95, B:366:0x0b9a, B:367:0x0ba4, B:369:0x0bb2, B:370:0x0bb8, B:371:0x0bc0, B:373:0x0bc6, B:375:0x0bde, B:377:0x0bf1, B:378:0x0c09, B:380:0x0c0f, B:382:0x0c19, B:383:0x0c1d, B:384:0x0c20, B:390:0x0c30, B:391:0x0c34, B:392:0x0c37, B:394:0x0c56, B:395:0x0c5a, B:396:0x0c5d, B:397:0x0c65, B:399:0x0c6b, B:401:0x0c81, B:404:0x0c88, B:405:0x0c90, B:407:0x0c9c, B:408:0x0ca2, B:409:0x0cb9, B:410:0x0cc9, B:411:0x0ce1, B:414:0x0ce9, B:415:0x0cee, B:416:0x0cfe, B:418:0x0d18, B:419:0x0d33, B:421:0x0d3d, B:424:0x0d4d, B:425:0x0d60, B:428:0x0d72), top: B:434:0x000a, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:160:0x04e5 A[Catch: all -> 0x0d84, TryCatch #0 {all -> 0x0d84, blocks: (B:3:0x000a, B:5:0x0022, B:8:0x002a, B:9:0x0050, B:12:0x0060, B:15:0x0087, B:17:0x00bd, B:20:0x00cf, B:22:0x00d9, B:24:0x0100, B:26:0x0110, B:29:0x0132, B:31:0x0138, B:33:0x014a, B:35:0x0158, B:37:0x0168, B:38:0x0175, B:42:0x0182, B:45:0x0199, B:71:0x01fc, B:74:0x0206, B:76:0x0214, B:77:0x022e, B:79:0x023d, B:80:0x0258, B:83:0x0263, B:86:0x0294, B:87:0x02be, B:89:0x02f5, B:91:0x02fb, B:94:0x0307, B:96:0x033d, B:97:0x0358, B:99:0x035e, B:101:0x036c, B:102:0x0374, B:105:0x0380, B:108:0x0387, B:111:0x0390, B:112:0x03a8, B:114:0x03c2, B:115:0x03ce, B:117:0x03d4, B:120:0x03e9, B:123:0x03fc, B:127:0x0405, B:129:0x0411, B:131:0x041d, B:135:0x043e, B:138:0x0450, B:140:0x0456, B:142:0x0460, B:143:0x0466, B:145:0x0482, B:147:0x048d, B:150:0x04a1, B:152:0x04b4, B:154:0x04c0, B:160:0x04e5, B:162:0x04f3, B:165:0x0508, B:167:0x051b, B:169:0x0527, B:174:0x054a, B:176:0x0560, B:178:0x056c, B:181:0x057f, B:183:0x0592, B:185:0x05db, B:187:0x05e2, B:189:0x05e8, B:191:0x05f4, B:193:0x05fb, B:195:0x0601, B:197:0x060d, B:198:0x061f, B:200:0x0628, B:202:0x0632, B:204:0x0638, B:205:0x0650, B:207:0x0663, B:208:0x067b, B:209:0x0685, B:210:0x069d, B:215:0x06b3, B:217:0x06c1, B:219:0x06cc, B:220:0x06d4, B:222:0x06df, B:224:0x06e5, B:227:0x06f1, B:229:0x06fb, B:230:0x0702, B:233:0x0708, B:234:0x0715, B:237:0x071d, B:239:0x072f, B:240:0x073b, B:242:0x0743, B:243:0x0748, B:245:0x074f, B:246:0x0769, B:248:0x078e, B:250:0x079f, B:252:0x07a5, B:254:0x07b1, B:255:0x07e2, B:257:0x07e8, B:259:0x07f8, B:260:0x07fc, B:261:0x07ff, B:262:0x0802, B:263:0x0810, B:265:0x0816, B:267:0x0826, B:268:0x082d, B:270:0x0839, B:271:0x0840, B:272:0x0843, B:274:0x0881, B:275:0x0894, B:277:0x089a, B:280:0x08b2, B:282:0x08cd, B:284:0x08e4, B:286:0x08eb, B:288:0x08ef, B:290:0x08f3, B:292:0x08fd, B:293:0x0907, B:295:0x090b, B:297:0x0911, B:298:0x0921, B:299:0x092a, B:300:0x0937, B:302:0x094e, B:305:0x0955, B:307:0x096d, B:309:0x098f, B:310:0x0997, B:312:0x099d, B:314:0x09af, B:319:0x09c3, B:321:0x09d8, B:322:0x0a03, B:324:0x0a0f, B:326:0x0a24, B:329:0x0a68, B:333:0x0a80, B:335:0x0a87, B:337:0x0a96, B:339:0x0a9a, B:341:0x0a9e, B:343:0x0aa2, B:344:0x0aae, B:345:0x0abb, B:347:0x0ac1, B:349:0x0add, B:350:0x0ae4, B:351:0x0b00, B:353:0x0b08, B:354:0x0b15, B:357:0x0b2f, B:359:0x0b5b, B:361:0x0b6b, B:362:0x0b7d, B:364:0x0b89, B:365:0x0b95, B:366:0x0b9a, B:367:0x0ba4, B:369:0x0bb2, B:370:0x0bb8, B:371:0x0bc0, B:373:0x0bc6, B:375:0x0bde, B:377:0x0bf1, B:378:0x0c09, B:380:0x0c0f, B:382:0x0c19, B:383:0x0c1d, B:384:0x0c20, B:390:0x0c30, B:391:0x0c34, B:392:0x0c37, B:394:0x0c56, B:395:0x0c5a, B:396:0x0c5d, B:397:0x0c65, B:399:0x0c6b, B:401:0x0c81, B:404:0x0c88, B:405:0x0c90, B:407:0x0c9c, B:408:0x0ca2, B:409:0x0cb9, B:410:0x0cc9, B:411:0x0ce1, B:414:0x0ce9, B:415:0x0cee, B:416:0x0cfe, B:418:0x0d18, B:419:0x0d33, B:421:0x0d3d, B:424:0x0d4d, B:425:0x0d60, B:428:0x0d72), top: B:434:0x000a, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:204:0x0638 A[Catch: all -> 0x0d84, TryCatch #0 {all -> 0x0d84, blocks: (B:3:0x000a, B:5:0x0022, B:8:0x002a, B:9:0x0050, B:12:0x0060, B:15:0x0087, B:17:0x00bd, B:20:0x00cf, B:22:0x00d9, B:24:0x0100, B:26:0x0110, B:29:0x0132, B:31:0x0138, B:33:0x014a, B:35:0x0158, B:37:0x0168, B:38:0x0175, B:42:0x0182, B:45:0x0199, B:71:0x01fc, B:74:0x0206, B:76:0x0214, B:77:0x022e, B:79:0x023d, B:80:0x0258, B:83:0x0263, B:86:0x0294, B:87:0x02be, B:89:0x02f5, B:91:0x02fb, B:94:0x0307, B:96:0x033d, B:97:0x0358, B:99:0x035e, B:101:0x036c, B:102:0x0374, B:105:0x0380, B:108:0x0387, B:111:0x0390, B:112:0x03a8, B:114:0x03c2, B:115:0x03ce, B:117:0x03d4, B:120:0x03e9, B:123:0x03fc, B:127:0x0405, B:129:0x0411, B:131:0x041d, B:135:0x043e, B:138:0x0450, B:140:0x0456, B:142:0x0460, B:143:0x0466, B:145:0x0482, B:147:0x048d, B:150:0x04a1, B:152:0x04b4, B:154:0x04c0, B:160:0x04e5, B:162:0x04f3, B:165:0x0508, B:167:0x051b, B:169:0x0527, B:174:0x054a, B:176:0x0560, B:178:0x056c, B:181:0x057f, B:183:0x0592, B:185:0x05db, B:187:0x05e2, B:189:0x05e8, B:191:0x05f4, B:193:0x05fb, B:195:0x0601, B:197:0x060d, B:198:0x061f, B:200:0x0628, B:202:0x0632, B:204:0x0638, B:205:0x0650, B:207:0x0663, B:208:0x067b, B:209:0x0685, B:210:0x069d, B:215:0x06b3, B:217:0x06c1, B:219:0x06cc, B:220:0x06d4, B:222:0x06df, B:224:0x06e5, B:227:0x06f1, B:229:0x06fb, B:230:0x0702, B:233:0x0708, B:234:0x0715, B:237:0x071d, B:239:0x072f, B:240:0x073b, B:242:0x0743, B:243:0x0748, B:245:0x074f, B:246:0x0769, B:248:0x078e, B:250:0x079f, B:252:0x07a5, B:254:0x07b1, B:255:0x07e2, B:257:0x07e8, B:259:0x07f8, B:260:0x07fc, B:261:0x07ff, B:262:0x0802, B:263:0x0810, B:265:0x0816, B:267:0x0826, B:268:0x082d, B:270:0x0839, B:271:0x0840, B:272:0x0843, B:274:0x0881, B:275:0x0894, B:277:0x089a, B:280:0x08b2, B:282:0x08cd, B:284:0x08e4, B:286:0x08eb, B:288:0x08ef, B:290:0x08f3, B:292:0x08fd, B:293:0x0907, B:295:0x090b, B:297:0x0911, B:298:0x0921, B:299:0x092a, B:300:0x0937, B:302:0x094e, B:305:0x0955, B:307:0x096d, B:309:0x098f, B:310:0x0997, B:312:0x099d, B:314:0x09af, B:319:0x09c3, B:321:0x09d8, B:322:0x0a03, B:324:0x0a0f, B:326:0x0a24, B:329:0x0a68, B:333:0x0a80, B:335:0x0a87, B:337:0x0a96, B:339:0x0a9a, B:341:0x0a9e, B:343:0x0aa2, B:344:0x0aae, B:345:0x0abb, B:347:0x0ac1, B:349:0x0add, B:350:0x0ae4, B:351:0x0b00, B:353:0x0b08, B:354:0x0b15, B:357:0x0b2f, B:359:0x0b5b, B:361:0x0b6b, B:362:0x0b7d, B:364:0x0b89, B:365:0x0b95, B:366:0x0b9a, B:367:0x0ba4, B:369:0x0bb2, B:370:0x0bb8, B:371:0x0bc0, B:373:0x0bc6, B:375:0x0bde, B:377:0x0bf1, B:378:0x0c09, B:380:0x0c0f, B:382:0x0c19, B:383:0x0c1d, B:384:0x0c20, B:390:0x0c30, B:391:0x0c34, B:392:0x0c37, B:394:0x0c56, B:395:0x0c5a, B:396:0x0c5d, B:397:0x0c65, B:399:0x0c6b, B:401:0x0c81, B:404:0x0c88, B:405:0x0c90, B:407:0x0c9c, B:408:0x0ca2, B:409:0x0cb9, B:410:0x0cc9, B:411:0x0ce1, B:414:0x0ce9, B:415:0x0cee, B:416:0x0cfe, B:418:0x0d18, B:419:0x0d33, B:421:0x0d3d, B:424:0x0d4d, B:425:0x0d60, B:428:0x0d72), top: B:434:0x000a, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:205:0x0650 A[Catch: all -> 0x0d84, TryCatch #0 {all -> 0x0d84, blocks: (B:3:0x000a, B:5:0x0022, B:8:0x002a, B:9:0x0050, B:12:0x0060, B:15:0x0087, B:17:0x00bd, B:20:0x00cf, B:22:0x00d9, B:24:0x0100, B:26:0x0110, B:29:0x0132, B:31:0x0138, B:33:0x014a, B:35:0x0158, B:37:0x0168, B:38:0x0175, B:42:0x0182, B:45:0x0199, B:71:0x01fc, B:74:0x0206, B:76:0x0214, B:77:0x022e, B:79:0x023d, B:80:0x0258, B:83:0x0263, B:86:0x0294, B:87:0x02be, B:89:0x02f5, B:91:0x02fb, B:94:0x0307, B:96:0x033d, B:97:0x0358, B:99:0x035e, B:101:0x036c, B:102:0x0374, B:105:0x0380, B:108:0x0387, B:111:0x0390, B:112:0x03a8, B:114:0x03c2, B:115:0x03ce, B:117:0x03d4, B:120:0x03e9, B:123:0x03fc, B:127:0x0405, B:129:0x0411, B:131:0x041d, B:135:0x043e, B:138:0x0450, B:140:0x0456, B:142:0x0460, B:143:0x0466, B:145:0x0482, B:147:0x048d, B:150:0x04a1, B:152:0x04b4, B:154:0x04c0, B:160:0x04e5, B:162:0x04f3, B:165:0x0508, B:167:0x051b, B:169:0x0527, B:174:0x054a, B:176:0x0560, B:178:0x056c, B:181:0x057f, B:183:0x0592, B:185:0x05db, B:187:0x05e2, B:189:0x05e8, B:191:0x05f4, B:193:0x05fb, B:195:0x0601, B:197:0x060d, B:198:0x061f, B:200:0x0628, B:202:0x0632, B:204:0x0638, B:205:0x0650, B:207:0x0663, B:208:0x067b, B:209:0x0685, B:210:0x069d, B:215:0x06b3, B:217:0x06c1, B:219:0x06cc, B:220:0x06d4, B:222:0x06df, B:224:0x06e5, B:227:0x06f1, B:229:0x06fb, B:230:0x0702, B:233:0x0708, B:234:0x0715, B:237:0x071d, B:239:0x072f, B:240:0x073b, B:242:0x0743, B:243:0x0748, B:245:0x074f, B:246:0x0769, B:248:0x078e, B:250:0x079f, B:252:0x07a5, B:254:0x07b1, B:255:0x07e2, B:257:0x07e8, B:259:0x07f8, B:260:0x07fc, B:261:0x07ff, B:262:0x0802, B:263:0x0810, B:265:0x0816, B:267:0x0826, B:268:0x082d, B:270:0x0839, B:271:0x0840, B:272:0x0843, B:274:0x0881, B:275:0x0894, B:277:0x089a, B:280:0x08b2, B:282:0x08cd, B:284:0x08e4, B:286:0x08eb, B:288:0x08ef, B:290:0x08f3, B:292:0x08fd, B:293:0x0907, B:295:0x090b, B:297:0x0911, B:298:0x0921, B:299:0x092a, B:300:0x0937, B:302:0x094e, B:305:0x0955, B:307:0x096d, B:309:0x098f, B:310:0x0997, B:312:0x099d, B:314:0x09af, B:319:0x09c3, B:321:0x09d8, B:322:0x0a03, B:324:0x0a0f, B:326:0x0a24, B:329:0x0a68, B:333:0x0a80, B:335:0x0a87, B:337:0x0a96, B:339:0x0a9a, B:341:0x0a9e, B:343:0x0aa2, B:344:0x0aae, B:345:0x0abb, B:347:0x0ac1, B:349:0x0add, B:350:0x0ae4, B:351:0x0b00, B:353:0x0b08, B:354:0x0b15, B:357:0x0b2f, B:359:0x0b5b, B:361:0x0b6b, B:362:0x0b7d, B:364:0x0b89, B:365:0x0b95, B:366:0x0b9a, B:367:0x0ba4, B:369:0x0bb2, B:370:0x0bb8, B:371:0x0bc0, B:373:0x0bc6, B:375:0x0bde, B:377:0x0bf1, B:378:0x0c09, B:380:0x0c0f, B:382:0x0c19, B:383:0x0c1d, B:384:0x0c20, B:390:0x0c30, B:391:0x0c34, B:392:0x0c37, B:394:0x0c56, B:395:0x0c5a, B:396:0x0c5d, B:397:0x0c65, B:399:0x0c6b, B:401:0x0c81, B:404:0x0c88, B:405:0x0c90, B:407:0x0c9c, B:408:0x0ca2, B:409:0x0cb9, B:410:0x0cc9, B:411:0x0ce1, B:414:0x0ce9, B:415:0x0cee, B:416:0x0cfe, B:418:0x0d18, B:419:0x0d33, B:421:0x0d3d, B:424:0x0d4d, B:425:0x0d60, B:428:0x0d72), top: B:434:0x000a, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:321:0x09d8 A[Catch: all -> 0x0d84, TryCatch #0 {all -> 0x0d84, blocks: (B:3:0x000a, B:5:0x0022, B:8:0x002a, B:9:0x0050, B:12:0x0060, B:15:0x0087, B:17:0x00bd, B:20:0x00cf, B:22:0x00d9, B:24:0x0100, B:26:0x0110, B:29:0x0132, B:31:0x0138, B:33:0x014a, B:35:0x0158, B:37:0x0168, B:38:0x0175, B:42:0x0182, B:45:0x0199, B:71:0x01fc, B:74:0x0206, B:76:0x0214, B:77:0x022e, B:79:0x023d, B:80:0x0258, B:83:0x0263, B:86:0x0294, B:87:0x02be, B:89:0x02f5, B:91:0x02fb, B:94:0x0307, B:96:0x033d, B:97:0x0358, B:99:0x035e, B:101:0x036c, B:102:0x0374, B:105:0x0380, B:108:0x0387, B:111:0x0390, B:112:0x03a8, B:114:0x03c2, B:115:0x03ce, B:117:0x03d4, B:120:0x03e9, B:123:0x03fc, B:127:0x0405, B:129:0x0411, B:131:0x041d, B:135:0x043e, B:138:0x0450, B:140:0x0456, B:142:0x0460, B:143:0x0466, B:145:0x0482, B:147:0x048d, B:150:0x04a1, B:152:0x04b4, B:154:0x04c0, B:160:0x04e5, B:162:0x04f3, B:165:0x0508, B:167:0x051b, B:169:0x0527, B:174:0x054a, B:176:0x0560, B:178:0x056c, B:181:0x057f, B:183:0x0592, B:185:0x05db, B:187:0x05e2, B:189:0x05e8, B:191:0x05f4, B:193:0x05fb, B:195:0x0601, B:197:0x060d, B:198:0x061f, B:200:0x0628, B:202:0x0632, B:204:0x0638, B:205:0x0650, B:207:0x0663, B:208:0x067b, B:209:0x0685, B:210:0x069d, B:215:0x06b3, B:217:0x06c1, B:219:0x06cc, B:220:0x06d4, B:222:0x06df, B:224:0x06e5, B:227:0x06f1, B:229:0x06fb, B:230:0x0702, B:233:0x0708, B:234:0x0715, B:237:0x071d, B:239:0x072f, B:240:0x073b, B:242:0x0743, B:243:0x0748, B:245:0x074f, B:246:0x0769, B:248:0x078e, B:250:0x079f, B:252:0x07a5, B:254:0x07b1, B:255:0x07e2, B:257:0x07e8, B:259:0x07f8, B:260:0x07fc, B:261:0x07ff, B:262:0x0802, B:263:0x0810, B:265:0x0816, B:267:0x0826, B:268:0x082d, B:270:0x0839, B:271:0x0840, B:272:0x0843, B:274:0x0881, B:275:0x0894, B:277:0x089a, B:280:0x08b2, B:282:0x08cd, B:284:0x08e4, B:286:0x08eb, B:288:0x08ef, B:290:0x08f3, B:292:0x08fd, B:293:0x0907, B:295:0x090b, B:297:0x0911, B:298:0x0921, B:299:0x092a, B:300:0x0937, B:302:0x094e, B:305:0x0955, B:307:0x096d, B:309:0x098f, B:310:0x0997, B:312:0x099d, B:314:0x09af, B:319:0x09c3, B:321:0x09d8, B:322:0x0a03, B:324:0x0a0f, B:326:0x0a24, B:329:0x0a68, B:333:0x0a80, B:335:0x0a87, B:337:0x0a96, B:339:0x0a9a, B:341:0x0a9e, B:343:0x0aa2, B:344:0x0aae, B:345:0x0abb, B:347:0x0ac1, B:349:0x0add, B:350:0x0ae4, B:351:0x0b00, B:353:0x0b08, B:354:0x0b15, B:357:0x0b2f, B:359:0x0b5b, B:361:0x0b6b, B:362:0x0b7d, B:364:0x0b89, B:365:0x0b95, B:366:0x0b9a, B:367:0x0ba4, B:369:0x0bb2, B:370:0x0bb8, B:371:0x0bc0, B:373:0x0bc6, B:375:0x0bde, B:377:0x0bf1, B:378:0x0c09, B:380:0x0c0f, B:382:0x0c19, B:383:0x0c1d, B:384:0x0c20, B:390:0x0c30, B:391:0x0c34, B:392:0x0c37, B:394:0x0c56, B:395:0x0c5a, B:396:0x0c5d, B:397:0x0c65, B:399:0x0c6b, B:401:0x0c81, B:404:0x0c88, B:405:0x0c90, B:407:0x0c9c, B:408:0x0ca2, B:409:0x0cb9, B:410:0x0cc9, B:411:0x0ce1, B:414:0x0ce9, B:415:0x0cee, B:416:0x0cfe, B:418:0x0d18, B:419:0x0d33, B:421:0x0d3d, B:424:0x0d4d, B:425:0x0d60, B:428:0x0d72), top: B:434:0x000a, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:322:0x0a03 A[Catch: all -> 0x0d84, TryCatch #0 {all -> 0x0d84, blocks: (B:3:0x000a, B:5:0x0022, B:8:0x002a, B:9:0x0050, B:12:0x0060, B:15:0x0087, B:17:0x00bd, B:20:0x00cf, B:22:0x00d9, B:24:0x0100, B:26:0x0110, B:29:0x0132, B:31:0x0138, B:33:0x014a, B:35:0x0158, B:37:0x0168, B:38:0x0175, B:42:0x0182, B:45:0x0199, B:71:0x01fc, B:74:0x0206, B:76:0x0214, B:77:0x022e, B:79:0x023d, B:80:0x0258, B:83:0x0263, B:86:0x0294, B:87:0x02be, B:89:0x02f5, B:91:0x02fb, B:94:0x0307, B:96:0x033d, B:97:0x0358, B:99:0x035e, B:101:0x036c, B:102:0x0374, B:105:0x0380, B:108:0x0387, B:111:0x0390, B:112:0x03a8, B:114:0x03c2, B:115:0x03ce, B:117:0x03d4, B:120:0x03e9, B:123:0x03fc, B:127:0x0405, B:129:0x0411, B:131:0x041d, B:135:0x043e, B:138:0x0450, B:140:0x0456, B:142:0x0460, B:143:0x0466, B:145:0x0482, B:147:0x048d, B:150:0x04a1, B:152:0x04b4, B:154:0x04c0, B:160:0x04e5, B:162:0x04f3, B:165:0x0508, B:167:0x051b, B:169:0x0527, B:174:0x054a, B:176:0x0560, B:178:0x056c, B:181:0x057f, B:183:0x0592, B:185:0x05db, B:187:0x05e2, B:189:0x05e8, B:191:0x05f4, B:193:0x05fb, B:195:0x0601, B:197:0x060d, B:198:0x061f, B:200:0x0628, B:202:0x0632, B:204:0x0638, B:205:0x0650, B:207:0x0663, B:208:0x067b, B:209:0x0685, B:210:0x069d, B:215:0x06b3, B:217:0x06c1, B:219:0x06cc, B:220:0x06d4, B:222:0x06df, B:224:0x06e5, B:227:0x06f1, B:229:0x06fb, B:230:0x0702, B:233:0x0708, B:234:0x0715, B:237:0x071d, B:239:0x072f, B:240:0x073b, B:242:0x0743, B:243:0x0748, B:245:0x074f, B:246:0x0769, B:248:0x078e, B:250:0x079f, B:252:0x07a5, B:254:0x07b1, B:255:0x07e2, B:257:0x07e8, B:259:0x07f8, B:260:0x07fc, B:261:0x07ff, B:262:0x0802, B:263:0x0810, B:265:0x0816, B:267:0x0826, B:268:0x082d, B:270:0x0839, B:271:0x0840, B:272:0x0843, B:274:0x0881, B:275:0x0894, B:277:0x089a, B:280:0x08b2, B:282:0x08cd, B:284:0x08e4, B:286:0x08eb, B:288:0x08ef, B:290:0x08f3, B:292:0x08fd, B:293:0x0907, B:295:0x090b, B:297:0x0911, B:298:0x0921, B:299:0x092a, B:300:0x0937, B:302:0x094e, B:305:0x0955, B:307:0x096d, B:309:0x098f, B:310:0x0997, B:312:0x099d, B:314:0x09af, B:319:0x09c3, B:321:0x09d8, B:322:0x0a03, B:324:0x0a0f, B:326:0x0a24, B:329:0x0a68, B:333:0x0a80, B:335:0x0a87, B:337:0x0a96, B:339:0x0a9a, B:341:0x0a9e, B:343:0x0aa2, B:344:0x0aae, B:345:0x0abb, B:347:0x0ac1, B:349:0x0add, B:350:0x0ae4, B:351:0x0b00, B:353:0x0b08, B:354:0x0b15, B:357:0x0b2f, B:359:0x0b5b, B:361:0x0b6b, B:362:0x0b7d, B:364:0x0b89, B:365:0x0b95, B:366:0x0b9a, B:367:0x0ba4, B:369:0x0bb2, B:370:0x0bb8, B:371:0x0bc0, B:373:0x0bc6, B:375:0x0bde, B:377:0x0bf1, B:378:0x0c09, B:380:0x0c0f, B:382:0x0c19, B:383:0x0c1d, B:384:0x0c20, B:390:0x0c30, B:391:0x0c34, B:392:0x0c37, B:394:0x0c56, B:395:0x0c5a, B:396:0x0c5d, B:397:0x0c65, B:399:0x0c6b, B:401:0x0c81, B:404:0x0c88, B:405:0x0c90, B:407:0x0c9c, B:408:0x0ca2, B:409:0x0cb9, B:410:0x0cc9, B:411:0x0ce1, B:414:0x0ce9, B:415:0x0cee, B:416:0x0cfe, B:418:0x0d18, B:419:0x0d33, B:421:0x0d3d, B:424:0x0d4d, B:425:0x0d60, B:428:0x0d72), top: B:434:0x000a, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x01df  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private final boolean zzah(String str, long j) {
        String str2;
        long j2;
        long j3;
        zzkm zzkm;
        zzfx zzfx;
        SecureRandom secureRandom;
        zzkm zzkm2;
        HashMap hashMap;
        zzfx zzfx2;
        long j4;
        int i;
        long j5;
        zzfx zzfx3;
        zzap zzap;
        HashMap hashMap2;
        long j6;
        zzap zzap2;
        Long l;
        boolean z;
        zzfx zzfx4;
        int i2;
        int i3;
        int i4;
        boolean z2;
        boolean z3;
        int i5;
        zzfn zzfn;
        String str3;
        zzfx zzfx5;
        String str4;
        int i6;
        String str5;
        String str6;
        String str7;
        zzfn zzfn2;
        int i7;
        zzfn zzfn3;
        char c;
        zzaj zzaj = this.zze;
        zzak(zzaj);
        zzaj.zzw();
        try {
            zzkm zzkm3 = new zzkm(this, null);
            zzaj zzaj2 = this.zze;
            zzak(zzaj2);
            zzaj2.zzW(null, j, this.zzA, zzkm3);
            List<zzfo> list = zzkm3.zzc;
            if (list != null && !list.isEmpty()) {
                zzfx zzbv = zzkm3.zza.zzbv();
                zzbv.zzp();
                boolean zzs = zzg().zzs(zzkm3.zza.zzy(), zzdw.zzT);
                zzfn zzfn4 = null;
                zzfn zzfn5 = null;
                int i8 = 0;
                int i9 = -1;
                long j7 = 0;
                int i10 = 0;
                int i11 = -1;
                boolean z4 = false;
                while (true) {
                    str2 = "_et";
                    j2 = j7;
                    if (i8 >= zzkm3.zzc.size()) {
                        break;
                    }
                    zzfn zzbv2 = zzkm3.zzc.get(i8).zzbv();
                    zzfj zzfj = this.zzc;
                    zzak(zzfj);
                    if (zzfj.zzo(zzkm3.zza.zzy(), zzbv2.zzo())) {
                        zzay().zzk().zzc("Dropping blocked raw event. appId", zzei.zzn(zzkm3.zza.zzy()), this.zzn.zzj().zzc(zzbv2.zzo()));
                        zzfj zzfj2 = this.zzc;
                        zzak(zzfj2);
                        if (!zzfj2.zzm(zzkm3.zza.zzy())) {
                            zzfj zzfj3 = this.zzc;
                            zzak(zzfj3);
                            if (!zzfj3.zzp(zzkm3.zza.zzy()) && !"_err".equals(zzbv2.zzo())) {
                                zzv().zzM(this.zzC, zzkm3.zza.zzy(), 11, "_ev", zzbv2.zzo(), 0);
                            }
                        }
                        zzfx4 = zzbv;
                        z = zzs;
                        i2 = i8;
                        j7 = j2;
                        i10 = i10;
                    } else {
                        if (zzbv2.zzo().equals(zzgp.zza("_ai"))) {
                            zzbv2.zzi("_ai");
                            zzay().zzj().zza("Renaming ad_impression to _ai");
                            if (Log.isLoggable(zzay().zzq(), 5)) {
                                int i12 = 0;
                                while (i12 < zzbv2.zza()) {
                                    if (FirebaseAnalytics.Param.AD_PLATFORM.equals(zzbv2.zzn(i12).zzg()) && !TextUtils.isEmpty(zzbv2.zzn(i12).zzh()) && "admob".equalsIgnoreCase(zzbv2.zzn(i12).zzh())) {
                                        zzay().zzl().zza("AdMob ad impression logged from app. Potentially duplicative.");
                                    }
                                    i12++;
                                    i8 = i8;
                                }
                                i3 = i8;
                            } else {
                                i3 = i8;
                            }
                        } else {
                            i3 = i8;
                        }
                        zzfj zzfj4 = this.zzc;
                        zzak(zzfj4);
                        boolean zzn = zzfj4.zzn(zzkm3.zza.zzy(), zzbv2.zzo());
                        if (!zzn) {
                            zzak(this.zzi);
                            String zzo = zzbv2.zzo();
                            Preconditions.checkNotEmpty(zzo);
                            z = zzs;
                            int hashCode = zzo.hashCode();
                            i4 = i11;
                            if (hashCode == 94660) {
                                if (zzo.equals("_in")) {
                                    c = 0;
                                    if (c != 0) {
                                    }
                                    i5 = 0;
                                    z3 = false;
                                    z2 = false;
                                }
                                c = 65535;
                                if (c != 0) {
                                }
                                i5 = 0;
                                z3 = false;
                                z2 = false;
                            } else if (hashCode != 95025) {
                                if (hashCode == 95027 && zzo.equals("_ui")) {
                                    c = 1;
                                    if (c != 0 || c == 1 || c == 2) {
                                        i5 = 0;
                                        z3 = false;
                                        z2 = false;
                                    } else {
                                        zzfx5 = zzbv;
                                        str4 = "_fr";
                                        str3 = str2;
                                        zzfn = zzfn4;
                                        zzn = false;
                                        if (!zzn) {
                                            ArrayList arrayList = new ArrayList(zzbv2.zzp());
                                            int i13 = -1;
                                            int i14 = -1;
                                            for (int i15 = 0; i15 < arrayList.size(); i15++) {
                                                if ("value".equals(((zzfs) arrayList.get(i15)).zzg())) {
                                                    i13 = i15;
                                                } else if (FirebaseAnalytics.Param.CURRENCY.equals(((zzfs) arrayList.get(i15)).zzg())) {
                                                    i14 = i15;
                                                }
                                            }
                                            if (i13 != -1) {
                                                if (((zzfs) arrayList.get(i13)).zzw() || ((zzfs) arrayList.get(i13)).zzu()) {
                                                    if (i14 != -1) {
                                                        String zzh = ((zzfs) arrayList.get(i14)).zzh();
                                                        if (zzh.length() == 3) {
                                                            int i16 = 0;
                                                            while (i16 < zzh.length()) {
                                                                int codePointAt = zzh.codePointAt(i16);
                                                                if (Character.isLetter(codePointAt)) {
                                                                    i16 += Character.charCount(codePointAt);
                                                                }
                                                            }
                                                        }
                                                    }
                                                    zzay().zzl().zza("Value parameter discarded. You must also supply a 3-letter ISO_4217 currency code in the currency parameter.");
                                                    zzbv2.zzh(i13);
                                                    zzZ(zzbv2, "_c");
                                                    zzY(zzbv2, 19, FirebaseAnalytics.Param.CURRENCY);
                                                    break;
                                                }
                                                zzay().zzl().zza("Value must be specified with a numeric type.");
                                                zzbv2.zzh(i13);
                                                zzZ(zzbv2, "_c");
                                                zzY(zzbv2, 18, "value");
                                            }
                                        }
                                        if (!"_e".equals(zzbv2.zzo())) {
                                            zzak(this.zzi);
                                            if (zzkp.zzC(zzbv2.zzaA(), str4) == null) {
                                                if (zzfn5 == null || Math.abs(zzfn5.zzc() - zzbv2.zzc()) > 1000) {
                                                    zzfx4 = zzfx5;
                                                    zzfn = zzbv2;
                                                    i6 = i10;
                                                } else {
                                                    zzfn zzax = zzfn5.zzaq();
                                                    if (zzaj(zzbv2, zzax)) {
                                                        zzfx4 = zzfx5;
                                                        zzfx4.zzO(i9, zzax);
                                                        i6 = i4;
                                                        zzfn3 = null;
                                                        zzfn5 = null;
                                                    } else {
                                                        zzfx4 = zzfx5;
                                                        zzfn3 = zzbv2;
                                                        i6 = i10;
                                                    }
                                                    zzfn = zzfn3;
                                                }
                                                str5 = str3;
                                            } else {
                                                zzfx4 = zzfx5;
                                                i6 = i4;
                                                str5 = str3;
                                            }
                                        } else {
                                            zzfx4 = zzfx5;
                                            if ("_vs".equals(zzbv2.zzo())) {
                                                zzak(this.zzi);
                                                str5 = str3;
                                                if (zzkp.zzC(zzbv2.zzaA(), str5) != null) {
                                                    i6 = i4;
                                                } else if (zzfn == null || Math.abs(zzfn.zzc() - zzbv2.zzc()) > 1000) {
                                                    i6 = i4;
                                                    zzfn5 = zzbv2;
                                                    i9 = i10;
                                                } else {
                                                    zzfn zzax2 = zzfn.zzaq();
                                                    if (zzaj(zzax2, zzbv2)) {
                                                        i6 = i4;
                                                        zzfx4.zzO(i6, zzax2);
                                                        i7 = i9;
                                                        zzfn2 = null;
                                                        zzfn5 = null;
                                                    } else {
                                                        i6 = i4;
                                                        zzfn5 = zzbv2;
                                                        zzfn2 = zzfn;
                                                        i7 = i10;
                                                    }
                                                    zzfn = zzfn2;
                                                    i9 = i7;
                                                }
                                            } else {
                                                i6 = i4;
                                                str5 = str3;
                                                if (zzg().zzs(zzkm3.zza.zzy(), zzdw.zzag) && "_ab".equals(zzbv2.zzo())) {
                                                    zzak(this.zzi);
                                                    if (zzkp.zzC(zzbv2.zzaA(), str5) == null && zzfn != null && Math.abs(zzfn.zzc() - zzbv2.zzc()) <= 4000) {
                                                        zzfn zzax3 = zzfn.zzaq();
                                                        zzae(zzax3, zzbv2);
                                                        Preconditions.checkArgument("_e".equals(zzax3.zzo()));
                                                        zzak(this.zzi);
                                                        zzfs zzC = zzkp.zzC(zzax3.zzaA(), "_sn");
                                                        zzak(this.zzi);
                                                        zzfs zzC2 = zzkp.zzC(zzax3.zzaA(), "_sc");
                                                        zzak(this.zzi);
                                                        zzfs zzC3 = zzkp.zzC(zzax3.zzaA(), "_si");
                                                        if (zzC != null) {
                                                            str6 = zzC.zzh();
                                                        } else {
                                                            str6 = "";
                                                        }
                                                        if (!TextUtils.isEmpty(str6)) {
                                                            zzak(this.zzi);
                                                            zzkp.zzA(zzbv2, "_sn", str6);
                                                        }
                                                        if (zzC2 != null) {
                                                            str7 = zzC2.zzh();
                                                        } else {
                                                            str7 = "";
                                                        }
                                                        if (!TextUtils.isEmpty(str7)) {
                                                            zzak(this.zzi);
                                                            zzkp.zzA(zzbv2, "_sc", str7);
                                                        }
                                                        if (zzC3 != null) {
                                                            zzak(this.zzi);
                                                            zzkp.zzA(zzbv2, "_si", Long.valueOf(zzC3.zzd()));
                                                        }
                                                        zzfx4.zzO(i6, zzax3);
                                                        zzfn = null;
                                                    }
                                                }
                                            }
                                        }
                                        if (!z && "_e".equals(zzbv2.zzo())) {
                                            if (zzbv2.zza() != 0) {
                                                zzay().zzk().zzb("Engagement event does not contain any parameters. appId", zzei.zzn(zzkm3.zza.zzy()));
                                            } else {
                                                zzak(this.zzi);
                                                Long l2 = (Long) zzkp.zzD(zzbv2.zzaA(), str5);
                                                if (l2 == null) {
                                                    zzay().zzk().zzb("Engagement event does not include duration. appId", zzei.zzn(zzkm3.zza.zzy()));
                                                } else {
                                                    j2 += l2.longValue();
                                                }
                                            }
                                        }
                                        i2 = i3;
                                        zzkm3.zzc.set(i2, zzbv2.zzaA());
                                        zzfx4.zzj(zzbv2);
                                        i11 = i6;
                                        zzfn4 = zzfn;
                                        j7 = j2;
                                        i10++;
                                    }
                                }
                                c = 65535;
                                if (c != 0) {
                                }
                                i5 = 0;
                                z3 = false;
                                z2 = false;
                            } else {
                                if (zzo.equals("_ug")) {
                                    c = 2;
                                    if (c != 0) {
                                    }
                                    i5 = 0;
                                    z3 = false;
                                    z2 = false;
                                }
                                c = 65535;
                                if (c != 0) {
                                }
                                i5 = 0;
                                z3 = false;
                                z2 = false;
                            }
                        } else {
                            z = zzs;
                            i4 = i11;
                            i5 = 0;
                            z3 = false;
                            z2 = false;
                        }
                        while (true) {
                            zzfn = zzfn4;
                            str3 = str2;
                            if (i5 >= zzbv2.zza()) {
                                break;
                            }
                            if ("_c".equals(zzbv2.zzn(i5).zzg())) {
                                zzfr zzbv3 = zzbv2.zzn(i5).zzbv();
                                zzbv3.zzi(1);
                                zzbv2.zzk(i5, zzbv3.zzaA());
                                z3 = true;
                            } else if ("_r".equals(zzbv2.zzn(i5).zzg())) {
                                zzfr zzbv4 = zzbv2.zzn(i5).zzbv();
                                zzbv4.zzi(1);
                                zzbv2.zzk(i5, zzbv4.zzaA());
                                z2 = true;
                            }
                            i5++;
                            zzfn4 = zzfn;
                            str2 = str3;
                        }
                        if (z3 || !zzn) {
                            zzfx5 = zzbv;
                        } else {
                            zzfx5 = zzbv;
                            zzay().zzj().zzb("Marking event as conversion", this.zzn.zzj().zzc(zzbv2.zzo()));
                            zzfr zze = zzfs.zze();
                            zze.zzj("_c");
                            zze.zzi(1);
                            zzbv2.zze(zze);
                        }
                        if (!z2) {
                            zzay().zzj().zzb("Marking event as real-time", this.zzn.zzj().zzc(zzbv2.zzo()));
                            zzfr zze2 = zzfs.zze();
                            zze2.zzj("_r");
                            zze2.zzi(1);
                            zzbv2.zze(zze2);
                        }
                        zzaj zzaj3 = this.zze;
                        zzak(zzaj3);
                        str4 = "_fr";
                        if (zzaj3.zzl(zza(), zzkm3.zza.zzy(), false, false, false, false, true).zze > ((long) zzg().zze(zzkm3.zza.zzy(), zzdw.zzn))) {
                            zzZ(zzbv2, "_r");
                        } else {
                            z4 = true;
                        }
                        if (zzku.zzah(zzbv2.zzo()) && zzn) {
                            zzaj zzaj4 = this.zze;
                            zzak(zzaj4);
                            if (zzaj4.zzl(zza(), zzkm3.zza.zzy(), false, false, true, false, false).zzc > ((long) zzg().zze(zzkm3.zza.zzy(), zzdw.zzm))) {
                                zzay().zzk().zzb("Too many conversions. Not logging as conversion. appId", zzei.zzn(zzkm3.zza.zzy()));
                                boolean z5 = false;
                                zzfr zzfr = null;
                                int i17 = -1;
                                for (int i18 = 0; i18 < zzbv2.zza(); i18++) {
                                    zzfs zzn2 = zzbv2.zzn(i18);
                                    if ("_c".equals(zzn2.zzg())) {
                                        zzfr = zzn2.zzbv();
                                        i17 = i18;
                                    } else if ("_err".equals(zzn2.zzg())) {
                                        z5 = true;
                                    }
                                }
                                if (z5) {
                                    if (zzfr != null) {
                                        zzbv2.zzh(i17);
                                    } else {
                                        zzfr = null;
                                    }
                                }
                                if (zzfr != null) {
                                    zzfr zzax4 = zzfr.zzaq();
                                    zzax4.zzj("_err");
                                    zzax4.zzi(10);
                                    zzbv2.zzk(i17, zzax4.zzaA());
                                } else {
                                    zzay().zzd().zzb("Did not find conversion parameter. appId", zzei.zzn(zzkm3.zza.zzy()));
                                }
                            }
                        }
                        if (!zzn) {
                        }
                        if (!"_e".equals(zzbv2.zzo())) {
                        }
                        if (!z) {
                            if (zzbv2.zza() != 0) {
                            }
                        }
                        i2 = i3;
                        zzkm3.zzc.set(i2, zzbv2.zzaA());
                        zzfx4.zzj(zzbv2);
                        i11 = i6;
                        zzfn4 = zzfn;
                        j7 = j2;
                        i10++;
                    }
                    i8 = i2 + 1;
                    zzbv = zzfx4;
                    zzs = z;
                }
                zzfx zzfx6 = zzbv;
                if (zzs) {
                    j3 = j2;
                    int i19 = i10;
                    int i20 = 0;
                    while (i20 < i19) {
                        zzfo zze3 = zzfx6.zze(i20);
                        if ("_e".equals(zze3.zzh())) {
                            zzak(this.zzi);
                            if (zzkp.zzC(zze3, "_fr") != null) {
                                zzfx6.zzw(i20);
                                i19--;
                                i20--;
                                i20++;
                            }
                        }
                        zzak(this.zzi);
                        zzfs zzC4 = zzkp.zzC(zze3, str2);
                        if (zzC4 != null) {
                            if (zzC4.zzw()) {
                                l = Long.valueOf(zzC4.zzd());
                            } else {
                                l = null;
                            }
                            if (l != null && l.longValue() > 0) {
                                j3 += l.longValue();
                            }
                        }
                        i20++;
                    }
                } else {
                    j3 = j2;
                }
                zzad(zzfx6, j3, false);
                Iterator<zzfo> it = zzfx6.zzao().iterator();
                while (true) {
                    if (it.hasNext()) {
                        if ("_s".equals(it.next().zzh())) {
                            zzaj zzaj5 = this.zze;
                            zzak(zzaj5);
                            zzaj5.zzA(zzfx6.zzal(), "_se");
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (zzkp.zza(zzfx6, "_sid") >= 0) {
                    zzad(zzfx6, j3, true);
                } else {
                    int zza = zzkp.zza(zzfx6, "_se");
                    if (zza >= 0) {
                        zzfx6.zzx(zza);
                        zzay().zzd().zzb("Session engagement user property is in the bundle without session ID. appId", zzei.zzn(zzkm3.zza.zzy()));
                    }
                }
                zzkp zzkp = this.zzi;
                zzak(zzkp);
                zzkp.zzs.zzay().zzj().zza("Checking account type status for ad personalization signals");
                zzfj zzfj5 = zzkp.zzf.zzc;
                zzak(zzfj5);
                if (zzfj5.zzk(zzfx6.zzal())) {
                    zzaj zzaj6 = zzkp.zzf.zze;
                    zzak(zzaj6);
                    zzg zzj = zzaj6.zzj(zzfx6.zzal());
                    if (zzj != null && zzj.zzai() && zzkp.zzs.zzg().zze()) {
                        zzkp.zzs.zzay().zzc().zza("Turning off ad personalization due to account type");
                        zzgg zzd = zzgh.zzd();
                        zzd.zzf("_npa");
                        zzd.zzg(zzkp.zzs.zzg().zza());
                        zzd.zze(1);
                        zzgh zzaA = zzd.zzaA();
                        int i21 = 0;
                        while (true) {
                            if (i21 >= zzfx6.zzb()) {
                                zzfx6.zzl(zzaA);
                                break;
                            } else if ("_npa".equals(zzfx6.zzak(i21).zzf())) {
                                zzfx6.zzai(i21, zzaA);
                                break;
                            } else {
                                i21++;
                            }
                        }
                    }
                }
                zzfx6.zzae(Long.MAX_VALUE);
                zzfx6.zzN(Long.MIN_VALUE);
                for (int i22 = 0; i22 < zzfx6.zza(); i22++) {
                    zzfo zze4 = zzfx6.zze(i22);
                    if (zze4.zzd() < zzfx6.zzd()) {
                        zzfx6.zzae(zze4.zzd());
                    }
                    if (zze4.zzd() > zzfx6.zzc()) {
                        zzfx6.zzN(zze4.zzd());
                    }
                }
                zzfx6.zzv();
                zzfx6.zzn();
                zzz zzz = this.zzh;
                zzak(zzz);
                zzfx6.zzf(zzz.zza(zzfx6.zzal(), zzfx6.zzao(), zzfx6.zzap(), Long.valueOf(zzfx6.zzd()), Long.valueOf(zzfx6.zzc())));
                if (zzg().zzw(zzkm3.zza.zzy())) {
                    HashMap hashMap3 = new HashMap();
                    ArrayList arrayList2 = new ArrayList();
                    SecureRandom zzF = zzv().zzF();
                    int i23 = 0;
                    while (i23 < zzfx6.zza()) {
                        zzfn zzbv5 = zzfx6.zze(i23).zzbv();
                        if (zzbv5.zzo().equals("_ep")) {
                            zzak(this.zzi);
                            String str8 = (String) zzkp.zzD(zzbv5.zzaA(), "_en");
                            zzap zzap3 = (zzap) hashMap3.get(str8);
                            if (zzap3 == null) {
                                zzaj zzaj7 = this.zze;
                                zzak(zzaj7);
                                zzap3 = zzaj7.zzn(zzkm3.zza.zzy(), (String) Preconditions.checkNotNull(str8));
                                if (zzap3 != null) {
                                    hashMap3.put(str8, zzap3);
                                }
                            }
                            if (zzap3 != null && zzap3.zzi == null) {
                                Long l3 = zzap3.zzj;
                                if (l3 != null && l3.longValue() > 1) {
                                    zzak(this.zzi);
                                    zzkp.zzA(zzbv5, "_sr", zzap3.zzj);
                                }
                                Boolean bool = zzap3.zzk;
                                if (bool != null && bool.booleanValue()) {
                                    zzak(this.zzi);
                                    zzkp.zzA(zzbv5, "_efs", 1L);
                                }
                                arrayList2.add(zzbv5.zzaA());
                            }
                            zzfx6.zzO(i23, zzbv5);
                            zzkm2 = zzkm3;
                            secureRandom = zzF;
                            hashMap = hashMap3;
                            zzfx2 = zzfx6;
                        } else {
                            zzfj zzfj6 = this.zzc;
                            zzak(zzfj6);
                            String zzy = zzkm3.zza.zzy();
                            String zza2 = zzfj6.zza(zzy, "measurement.account.time_zone_offset_minutes");
                            if (!TextUtils.isEmpty(zza2)) {
                                try {
                                    j4 = Long.parseLong(zza2);
                                } catch (NumberFormatException e) {
                                    zzfj6.zzs.zzay().zzk().zzc("Unable to parse timezone offset. appId", zzei.zzn(zzy), e);
                                    j4 = 0;
                                }
                            } else {
                                j4 = 0;
                            }
                            long zzr = zzv().zzr(zzbv5.zzc(), j4);
                            zzfo zzaA2 = zzbv5.zzaA();
                            Long l4 = 1L;
                            if (!TextUtils.isEmpty("_dbg")) {
                                Iterator<zzfs> it2 = zzaA2.zzi().iterator();
                                while (true) {
                                    if (!it2.hasNext()) {
                                        break;
                                    }
                                    zzfs next = it2.next();
                                    if (!"_dbg".equals(next.zzg())) {
                                        it2 = it2;
                                    } else if (l4.equals(Long.valueOf(next.zzd()))) {
                                        i = 1;
                                    }
                                }
                                if (i > 0) {
                                    zzay().zzk().zzc("Sample rate must be positive. event, rate", zzbv5.zzo(), Integer.valueOf(i));
                                    arrayList2.add(zzbv5.zzaA());
                                    zzfx6.zzO(i23, zzbv5);
                                    zzkm2 = zzkm3;
                                    secureRandom = zzF;
                                    hashMap = hashMap3;
                                    zzfx2 = zzfx6;
                                } else {
                                    zzap zzap4 = (zzap) hashMap3.get(zzbv5.zzo());
                                    if (zzap4 == null) {
                                        zzaj zzaj8 = this.zze;
                                        zzak(zzaj8);
                                        zzap4 = zzaj8.zzn(zzkm3.zza.zzy(), zzbv5.zzo());
                                        if (zzap4 == null) {
                                            j5 = zzr;
                                            zzay().zzk().zzc("Event being bundled has no eventAggregate. appId, eventName", zzkm3.zza.zzy(), zzbv5.zzo());
                                            zzap4 = new zzap(zzkm3.zza.zzy(), zzbv5.zzo(), 1, 1, 1, zzbv5.zzc(), 0, null, null, null, null);
                                        } else {
                                            j5 = zzr;
                                        }
                                    } else {
                                        j5 = zzr;
                                    }
                                    zzak(this.zzi);
                                    Long l5 = (Long) zzkp.zzD(zzbv5.zzaA(), "_eid");
                                    Boolean valueOf = Boolean.valueOf(l5 != null);
                                    if (i == 1) {
                                        arrayList2.add(zzbv5.zzaA());
                                        if (valueOf.booleanValue() && !(zzap4.zzi == null && zzap4.zzj == null && zzap4.zzk == null)) {
                                            hashMap3.put(zzbv5.zzo(), zzap4.zza(null, null, null));
                                        }
                                        zzfx6.zzO(i23, zzbv5);
                                        zzkm2 = zzkm3;
                                        secureRandom = zzF;
                                        hashMap = hashMap3;
                                        zzfx2 = zzfx6;
                                    } else {
                                        if (zzF.nextInt(i) == 0) {
                                            zzak(this.zzi);
                                            Long valueOf2 = Long.valueOf((long) i);
                                            zzkp.zzA(zzbv5, "_sr", valueOf2);
                                            arrayList2.add(zzbv5.zzaA());
                                            if (valueOf.booleanValue()) {
                                                zzap4 = zzap4.zza(null, valueOf2, null);
                                            }
                                            hashMap3.put(zzbv5.zzo(), zzap4.zzb(zzbv5.zzc(), j5));
                                            zzkm2 = zzkm3;
                                            secureRandom = zzF;
                                            zzfx3 = zzfx6;
                                            hashMap = hashMap3;
                                        } else {
                                            secureRandom = zzF;
                                            Long l6 = zzap4.zzh;
                                            if (l6 != null) {
                                                j6 = l6.longValue();
                                                zzkm2 = zzkm3;
                                                hashMap2 = hashMap3;
                                                zzfx3 = zzfx6;
                                                zzap = zzap4;
                                            } else {
                                                zzkm2 = zzkm3;
                                                hashMap2 = hashMap3;
                                                zzfx3 = zzfx6;
                                                zzap = zzap4;
                                                j6 = zzv().zzr(zzbv5.zzb(), j4);
                                            }
                                            if (j6 != j5) {
                                                zzak(this.zzi);
                                                zzkp.zzA(zzbv5, "_efs", 1L);
                                                zzak(this.zzi);
                                                Long valueOf3 = Long.valueOf((long) i);
                                                zzkp.zzA(zzbv5, "_sr", valueOf3);
                                                arrayList2.add(zzbv5.zzaA());
                                                if (valueOf.booleanValue()) {
                                                    zzap2 = zzap.zza(null, valueOf3, true);
                                                } else {
                                                    zzap2 = zzap;
                                                }
                                                hashMap = hashMap2;
                                                hashMap.put(zzbv5.zzo(), zzap2.zzb(zzbv5.zzc(), j5));
                                            } else {
                                                hashMap = hashMap2;
                                                if (valueOf.booleanValue()) {
                                                    hashMap.put(zzbv5.zzo(), zzap.zza(l5, null, null));
                                                }
                                            }
                                        }
                                        zzfx2 = zzfx3;
                                        zzfx2.zzO(i23, zzbv5);
                                    }
                                }
                            }
                            zzfj zzfj7 = this.zzc;
                            zzak(zzfj7);
                            i = zzfj7.zzc(zzkm3.zza.zzy(), zzbv5.zzo());
                            if (i > 0) {
                            }
                        }
                        i23++;
                        zzfx6 = zzfx2;
                        hashMap3 = hashMap;
                        zzkm3 = zzkm2;
                        zzF = secureRandom;
                    }
                    zzkm = zzkm3;
                    zzfx = zzfx6;
                    if (arrayList2.size() < zzfx.zza()) {
                        zzfx.zzp();
                        zzfx.zzg(arrayList2);
                    }
                    for (Map.Entry entry : hashMap3.entrySet()) {
                        zzaj zzaj9 = this.zze;
                        zzak(zzaj9);
                        zzaj9.zzE((zzap) entry.getValue());
                    }
                } else {
                    zzkm = zzkm3;
                    zzfx = zzfx6;
                }
                String zzy2 = zzkm.zza.zzy();
                zzaj zzaj10 = this.zze;
                zzak(zzaj10);
                zzg zzj2 = zzaj10.zzj(zzy2);
                if (zzj2 == null) {
                    zzay().zzd().zzb("Bundling raw events w/o app info. appId", zzei.zzn(zzkm.zza.zzy()));
                } else if (zzfx.zza() > 0) {
                    long zzn3 = zzj2.zzn();
                    if (zzn3 != 0) {
                        zzfx.zzY(zzn3);
                    } else {
                        zzfx.zzs();
                    }
                    long zzp = zzj2.zzp();
                    if (zzp != 0) {
                        zzn3 = zzp;
                    }
                    if (zzn3 != 0) {
                        zzfx.zzZ(zzn3);
                    } else {
                        zzfx.zzt();
                    }
                    zzj2.zzE();
                    zzfx.zzF((int) zzj2.zzo());
                    zzj2.zzad(zzfx.zzd());
                    zzj2.zzab(zzfx.zzc());
                    String zzs2 = zzj2.zzs();
                    if (zzs2 != null) {
                        zzfx.zzT(zzs2);
                    } else {
                        zzfx.zzq();
                    }
                    zzaj zzaj11 = this.zze;
                    zzak(zzaj11);
                    zzaj11.zzD(zzj2);
                }
                if (zzfx.zza() > 0) {
                    this.zzn.zzaw();
                    zzfj zzfj8 = this.zzc;
                    zzak(zzfj8);
                    zzfc zze5 = zzfj8.zze(zzkm.zza.zzy());
                    if (zze5 != null && zze5.zzq()) {
                        zzfx.zzH(zze5.zzc());
                        zzaj zzaj12 = this.zze;
                        zzak(zzaj12);
                        zzaj12.zzJ(zzfx.zzaA(), z4);
                    }
                    if (TextUtils.isEmpty(zzkm.zza.zzH())) {
                        zzfx.zzH(-1);
                    } else {
                        zzay().zzk().zzb("Did not find measurement config or missing version info. appId", zzei.zzn(zzkm.zza.zzy()));
                    }
                    zzaj zzaj122 = this.zze;
                    zzak(zzaj122);
                    zzaj122.zzJ(zzfx.zzaA(), z4);
                }
                zzaj zzaj13 = this.zze;
                zzak(zzaj13);
                List<Long> list2 = zzkm.zzb;
                Preconditions.checkNotNull(list2);
                zzaj13.zzg();
                zzaj13.zzY();
                StringBuilder sb = new StringBuilder("rowid in (");
                for (int i24 = 0; i24 < list2.size(); i24++) {
                    if (i24 != 0) {
                        sb.append(",");
                    }
                    sb.append(list2.get(i24).longValue());
                }
                sb.append(")");
                int delete = zzaj13.zzh().delete("raw_events", sb.toString(), null);
                if (delete != list2.size()) {
                    zzaj13.zzs.zzay().zzd().zzc("Deleted fewer rows from raw events table than expected", Integer.valueOf(delete), Integer.valueOf(list2.size()));
                }
                zzaj zzaj14 = this.zze;
                zzak(zzaj14);
                try {
                    zzaj14.zzh().execSQL("delete from raw_events_metadata where app_id=? and metadata_fingerprint not in (select distinct metadata_fingerprint from raw_events where app_id=?)", new String[]{zzy2, zzy2});
                } catch (SQLiteException e2) {
                    zzaj14.zzs.zzay().zzd().zzc("Failed to remove unused event metadata. appId", zzei.zzn(zzy2), e2);
                }
                zzaj zzaj15 = this.zze;
                zzak(zzaj15);
                zzaj15.zzC();
                zzaj zzaj16 = this.zze;
                zzak(zzaj16);
                zzaj16.zzx();
                return true;
            }
            zzaj zzaj17 = this.zze;
            zzak(zzaj17);
            zzaj17.zzC();
            zzaj zzaj18 = this.zze;
            zzak(zzaj18);
            zzaj18.zzx();
            return false;
        } catch (Throwable th) {
            zzaj zzaj19 = this.zze;
            zzak(zzaj19);
            zzaj19.zzx();
            throw th;
        }
    }

    private final boolean zzai() {
        zzaz().zzg();
        zzB();
        zzaj zzaj = this.zze;
        zzak(zzaj);
        if (zzaj.zzG()) {
            return true;
        }
        zzaj zzaj2 = this.zze;
        zzak(zzaj2);
        return !TextUtils.isEmpty(zzaj2.zzr());
    }

    private final boolean zzaj(zzfn zzfn, zzfn zzfn2) {
        String str;
        Preconditions.checkArgument("_e".equals(zzfn.zzo()));
        zzak(this.zzi);
        zzfs zzC = zzkp.zzC(zzfn.zzaA(), "_sc");
        String str2 = null;
        if (zzC == null) {
            str = null;
        } else {
            str = zzC.zzh();
        }
        zzak(this.zzi);
        zzfs zzC2 = zzkp.zzC(zzfn2.zzaA(), "_pc");
        if (zzC2 != null) {
            str2 = zzC2.zzh();
        }
        if (str2 == null || !str2.equals(str)) {
            return false;
        }
        zzae(zzfn, zzfn2);
        return true;
    }

    private static final zzkd zzak(zzkd zzkd) {
        if (zzkd == null) {
            throw new IllegalStateException("Upload Component not created");
        } else if (zzkd.zzaa()) {
            return zzkd;
        } else {
            String valueOf = String.valueOf(zzkd.getClass());
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 27);
            sb.append("Component not initialized: ");
            sb.append(valueOf);
            throw new IllegalStateException(sb.toString());
        }
    }

    public static zzkn zzt(Context context) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(context.getApplicationContext());
        if (zzb == null) {
            synchronized (zzkn.class) {
                if (zzb == null) {
                    zzb = new zzkn((zzko) Preconditions.checkNotNull(new zzko(context)), null);
                }
            }
        }
        return zzb;
    }

    public static /* bridge */ /* synthetic */ void zzy(zzkn zzkn, zzko zzko) {
        zzkn.zzaz().zzg();
        zzkn.zzm = new zzfa(zzkn);
        zzaj zzaj = new zzaj(zzkn);
        zzaj.zzZ();
        zzkn.zze = zzaj;
        zzkn.zzg().zzq((zzae) Preconditions.checkNotNull(zzkn.zzc));
        zzjk zzjk = new zzjk(zzkn);
        zzjk.zzZ();
        zzkn.zzk = zzjk;
        zzz zzz = new zzz(zzkn);
        zzz.zzZ();
        zzkn.zzh = zzz;
        zzia zzia = new zzia(zzkn);
        zzia.zzZ();
        zzkn.zzj = zzia;
        zzkb zzkb = new zzkb(zzkn);
        zzkb.zzZ();
        zzkn.zzg = zzkb;
        zzkn.zzf = new zzeq(zzkn);
        if (zzkn.zzr != zzkn.zzs) {
            zzkn.zzay().zzd().zzc("Not all upload components initialized", Integer.valueOf(zzkn.zzr), Integer.valueOf(zzkn.zzs));
        }
        zzkn.zzo = true;
    }

    public final void zzA() {
        zzaz().zzg();
        zzB();
        if (!this.zzp) {
            this.zzp = true;
            if (zzX()) {
                FileChannel fileChannel = this.zzx;
                zzaz().zzg();
                int i = 0;
                if (fileChannel == null || !fileChannel.isOpen()) {
                    zzay().zzd().zza("Bad channel to read from");
                } else {
                    ByteBuffer allocate = ByteBuffer.allocate(4);
                    try {
                        fileChannel.position(0L);
                        int read = fileChannel.read(allocate);
                        if (read == 4) {
                            allocate.flip();
                            i = allocate.getInt();
                        } else if (read != -1) {
                            zzay().zzk().zzb("Unexpected data length. Bytes read", Integer.valueOf(read));
                        }
                    } catch (IOException e) {
                        zzay().zzd().zzb("Failed to read from channel", e);
                    }
                }
                int zzi = this.zzn.zzh().zzi();
                zzaz().zzg();
                if (i > zzi) {
                    zzay().zzd().zzc("Panic: can't downgrade version. Previous, current version", Integer.valueOf(i), Integer.valueOf(zzi));
                } else if (i < zzi) {
                    FileChannel fileChannel2 = this.zzx;
                    zzaz().zzg();
                    if (fileChannel2 == null || !fileChannel2.isOpen()) {
                        zzay().zzd().zza("Bad channel to read from");
                    } else {
                        ByteBuffer allocate2 = ByteBuffer.allocate(4);
                        allocate2.putInt(zzi);
                        allocate2.flip();
                        try {
                            fileChannel2.truncate(0L);
                            if (zzg().zzs(null, zzdw.zzal) && Build.VERSION.SDK_INT <= 19) {
                                fileChannel2.position(0L);
                            }
                            fileChannel2.write(allocate2);
                            fileChannel2.force(true);
                            if (fileChannel2.size() != 4) {
                                zzay().zzd().zzb("Error writing to channel. Bytes written", Long.valueOf(fileChannel2.size()));
                            }
                            zzay().zzj().zzc("Storage version upgraded. Previous, current version", Integer.valueOf(i), Integer.valueOf(zzi));
                            return;
                        } catch (IOException e2) {
                            zzay().zzd().zzb("Failed to write to channel", e2);
                        }
                    }
                    zzay().zzd().zzc("Storage version upgrade failed. Previous, current version", Integer.valueOf(i), Integer.valueOf(zzi));
                }
            }
        }
    }

    public final void zzB() {
        if (!this.zzo) {
            throw new IllegalStateException("UploadController is not initialized");
        }
    }

    final void zzC(zzg zzg) {
        String str;
        ArrayMap arrayMap;
        zzaz().zzg();
        zzoq.zzc();
        if (zzg().zzs(zzg.zzt(), zzdw.zzad)) {
            if (TextUtils.isEmpty(zzg.zzz()) && TextUtils.isEmpty(zzg.zzy()) && TextUtils.isEmpty(zzg.zzr())) {
                zzH((String) Preconditions.checkNotNull(zzg.zzt()), SecBiometricLicenseManager.ERROR_INVALID_PACKAGE_NAME, null, null, null);
                return;
            }
        } else if (TextUtils.isEmpty(zzg.zzz()) && TextUtils.isEmpty(zzg.zzr())) {
            zzH((String) Preconditions.checkNotNull(zzg.zzt()), SecBiometricLicenseManager.ERROR_INVALID_PACKAGE_NAME, null, null, null);
            return;
        }
        zzke zzke = this.zzl;
        Uri.Builder builder = new Uri.Builder();
        String zzz = zzg.zzz();
        if (TextUtils.isEmpty(zzz)) {
            zzoq.zzc();
            if (zzke.zzs.zzf().zzs(zzg.zzt(), zzdw.zzad)) {
                zzz = zzg.zzy();
                if (TextUtils.isEmpty(zzz)) {
                    zzz = zzg.zzr();
                }
            } else {
                zzz = zzg.zzr();
            }
        }
        Uri.Builder encodedAuthority = builder.scheme(zzdw.zzd.zza(null)).encodedAuthority(zzdw.zze.zza(null));
        String valueOf = String.valueOf(zzz);
        if (valueOf.length() != 0) {
            str = "config/app/".concat(valueOf);
        } else {
            str = new String("config/app/");
        }
        Uri.Builder appendQueryParameter = encodedAuthority.path(str).appendQueryParameter("app_instance_id", zzg.zzu()).appendQueryParameter("platform", "android");
        zzke.zzs.zzf().zzh();
        appendQueryParameter.appendQueryParameter("gmp_version", String.valueOf(42097L));
        zzpl.zzc();
        if (zzke.zzs.zzf().zzs(zzg.zzt(), zzdw.zzav)) {
            builder.appendQueryParameter("runtime_version", "0");
        }
        String uri = builder.build().toString();
        try {
            String str2 = (String) Preconditions.checkNotNull(zzg.zzt());
            URL url = new URL(uri);
            zzay().zzj().zzb("Fetching remote configuration", str2);
            zzfj zzfj = this.zzc;
            zzak(zzfj);
            zzfc zze = zzfj.zze(str2);
            zzfj zzfj2 = this.zzc;
            zzak(zzfj2);
            String zzf = zzfj2.zzf(str2);
            if (zze == null || TextUtils.isEmpty(zzf)) {
                arrayMap = null;
            } else {
                ArrayMap arrayMap2 = new ArrayMap();
                arrayMap2.put(HttpHeaders.IF_MODIFIED_SINCE, zzf);
                arrayMap = arrayMap2;
            }
            this.zzt = true;
            zzeo zzeo = this.zzd;
            zzak(zzeo);
            zzkh zzkh = new zzkh(this);
            zzeo.zzg();
            zzeo.zzY();
            Preconditions.checkNotNull(url);
            Preconditions.checkNotNull(zzkh);
            zzeo.zzs.zzaz().zzo(new zzen(zzeo, str2, url, null, arrayMap, zzkh));
        } catch (MalformedURLException e) {
            zzay().zzd().zzc("Failed to parse config URL. Not fetching. appId", zzei.zzn(zzg.zzt()), uri);
        }
    }

    public final void zzD(zzat zzat, zzp zzp) {
        zzat zzat2;
        List<zzab> list;
        List<zzab> list2;
        List<zzab> list3;
        Preconditions.checkNotNull(zzp);
        Preconditions.checkNotEmpty(zzp.zza);
        zzaz().zzg();
        zzB();
        String str = zzp.zza;
        zzat zzat3 = zzat;
        long j = zzat3.zzd;
        zzpx.zzc();
        if (zzg().zzs(null, zzdw.zzaA)) {
            zzej zzb2 = zzej.zzb(zzat);
            zzaz().zzg();
            zzku.zzJ(null, zzb2.zzd, false);
            zzat3 = zzb2.zza();
        }
        zzak(this.zzi);
        if (zzkp.zzB(zzat3, zzp)) {
            if (!zzp.zzh) {
                zzd(zzp);
                return;
            }
            List<String> list4 = zzp.zzt;
            if (list4 == null) {
                zzat2 = zzat3;
            } else if (list4.contains(zzat3.zza)) {
                Bundle zzc = zzat3.zzb.zzc();
                zzc.putLong("ga_safelisted", 1);
                zzat2 = new zzat(zzat3.zza, new zzar(zzc), zzat3.zzc, zzat3.zzd);
            } else {
                zzay().zzc().zzd("Dropping non-safelisted event. appId, event name, origin", str, zzat3.zza, zzat3.zzc);
                return;
            }
            zzaj zzaj = this.zze;
            zzak(zzaj);
            zzaj.zzw();
            try {
                zzaj zzaj2 = this.zze;
                zzak(zzaj2);
                Preconditions.checkNotEmpty(str);
                zzaj2.zzg();
                zzaj2.zzY();
                int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
                if (i < 0) {
                    zzaj2.zzs.zzay().zzk().zzc("Invalid time querying timed out conditional properties", zzei.zzn(str), Long.valueOf(j));
                    list = Collections.emptyList();
                } else {
                    list = zzaj2.zzt("active=0 and app_id=? and abs(? - creation_timestamp) > trigger_timeout", new String[]{str, String.valueOf(j)});
                }
                for (zzab zzab : list) {
                    if (zzab != null) {
                        zzay().zzj().zzd("User property timed out", zzab.zza, this.zzn.zzj().zze(zzab.zzc.zzb), zzab.zzc.zza());
                        zzat zzat4 = zzab.zzg;
                        if (zzat4 != null) {
                            zzW(new zzat(zzat4, j), zzp);
                        }
                        zzaj zzaj3 = this.zze;
                        zzak(zzaj3);
                        zzaj3.zza(str, zzab.zzc.zzb);
                    }
                }
                zzaj zzaj4 = this.zze;
                zzak(zzaj4);
                Preconditions.checkNotEmpty(str);
                zzaj4.zzg();
                zzaj4.zzY();
                if (i < 0) {
                    zzaj4.zzs.zzay().zzk().zzc("Invalid time querying expired conditional properties", zzei.zzn(str), Long.valueOf(j));
                    list2 = Collections.emptyList();
                } else {
                    list2 = zzaj4.zzt("active<>0 and app_id=? and abs(? - triggered_timestamp) > time_to_live", new String[]{str, String.valueOf(j)});
                }
                ArrayList<zzat> arrayList = new ArrayList(list2.size());
                for (zzab zzab2 : list2) {
                    if (zzab2 != null) {
                        zzay().zzj().zzd("User property expired", zzab2.zza, this.zzn.zzj().zze(zzab2.zzc.zzb), zzab2.zzc.zza());
                        zzaj zzaj5 = this.zze;
                        zzak(zzaj5);
                        zzaj5.zzA(str, zzab2.zzc.zzb);
                        zzat zzat5 = zzab2.zzk;
                        if (zzat5 != null) {
                            arrayList.add(zzat5);
                        }
                        zzaj zzaj6 = this.zze;
                        zzak(zzaj6);
                        zzaj6.zza(str, zzab2.zzc.zzb);
                    }
                }
                for (zzat zzat6 : arrayList) {
                    zzW(new zzat(zzat6, j), zzp);
                }
                zzaj zzaj7 = this.zze;
                zzak(zzaj7);
                String str2 = zzat2.zza;
                Preconditions.checkNotEmpty(str);
                Preconditions.checkNotEmpty(str2);
                zzaj7.zzg();
                zzaj7.zzY();
                if (i < 0) {
                    zzaj7.zzs.zzay().zzk().zzd("Invalid time querying triggered conditional properties", zzei.zzn(str), zzaj7.zzs.zzj().zzc(str2), Long.valueOf(j));
                    list3 = Collections.emptyList();
                } else {
                    list3 = zzaj7.zzt("active=0 and app_id=? and trigger_event_name=? and abs(? - creation_timestamp) <= trigger_timeout", new String[]{str, str2, String.valueOf(j)});
                }
                ArrayList<zzat> arrayList2 = new ArrayList(list3.size());
                for (zzab zzab3 : list3) {
                    if (zzab3 != null) {
                        zzkq zzkq = zzab3.zzc;
                        zzks zzks = new zzks((String) Preconditions.checkNotNull(zzab3.zza), zzab3.zzb, zzkq.zzb, j, Preconditions.checkNotNull(zzkq.zza()));
                        zzaj zzaj8 = this.zze;
                        zzak(zzaj8);
                        if (zzaj8.zzN(zzks)) {
                            zzay().zzj().zzd("User property triggered", zzab3.zza, this.zzn.zzj().zze(zzks.zzc), zzks.zze);
                        } else {
                            zzay().zzd().zzd("Too many active user properties, ignoring", zzei.zzn(zzab3.zza), this.zzn.zzj().zze(zzks.zzc), zzks.zze);
                        }
                        zzat zzat7 = zzab3.zzi;
                        if (zzat7 != null) {
                            arrayList2.add(zzat7);
                        }
                        zzab3.zzc = new zzkq(zzks);
                        zzab3.zze = true;
                        zzaj zzaj9 = this.zze;
                        zzak(zzaj9);
                        zzaj9.zzM(zzab3);
                    }
                }
                zzW(zzat2, zzp);
                for (zzat zzat8 : arrayList2) {
                    zzW(new zzat(zzat8, j), zzp);
                }
                zzaj zzaj10 = this.zze;
                zzak(zzaj10);
                zzaj10.zzC();
            } finally {
                zzaj zzaj11 = this.zze;
                zzak(zzaj11);
                zzaj11.zzx();
            }
        }
    }

    public final void zzE(zzat zzat, String str) {
        String str2;
        zzaj zzaj = this.zze;
        zzak(zzaj);
        zzg zzj = zzaj.zzj(str);
        if (zzj == null || TextUtils.isEmpty(zzj.zzw())) {
            zzay().zzc().zzb("No app data available; dropping event", str);
            return;
        }
        Boolean zzab = zzab(zzj);
        if (zzab == null) {
            if (!"_ui".equals(zzat.zza)) {
                zzay().zzk().zzb("Could not find package. appId", zzei.zzn(str));
            }
        } else if (!zzab.booleanValue()) {
            zzay().zzd().zzb("App version does not match; dropping event. appId", zzei.zzn(str));
            return;
        }
        String zzz = zzj.zzz();
        String zzw = zzj.zzw();
        long zzb2 = zzj.zzb();
        String zzv = zzj.zzv();
        long zzm = zzj.zzm();
        long zzj2 = zzj.zzj();
        boolean zzaj2 = zzj.zzaj();
        String zzx = zzj.zzx();
        long zza = zzj.zza();
        boolean zzai = zzj.zzai();
        String zzr = zzj.zzr();
        Boolean zzq = zzj.zzq();
        long zzk = zzj.zzk();
        List<String> zzC = zzj.zzC();
        zzoq.zzc();
        if (zzg().zzs(zzj.zzt(), zzdw.zzad)) {
            str2 = zzj.zzy();
        } else {
            str2 = null;
        }
        zzF(zzat, new zzp(str, zzz, zzw, zzb2, zzv, zzm, zzj2, (String) null, zzaj2, false, zzx, zza, 0L, 0, zzai, false, zzr, zzq, zzk, zzC, str2, zzh(str).zzi()));
    }

    final void zzF(zzat zzat, zzp zzp) {
        Preconditions.checkNotEmpty(zzp.zza);
        zzej zzb2 = zzej.zzb(zzat);
        zzku zzv = zzv();
        Bundle bundle = zzb2.zzd;
        zzaj zzaj = this.zze;
        zzak(zzaj);
        zzv.zzK(bundle, zzaj.zzi(zzp.zza));
        zzv().zzL(zzb2, zzg().zzd(zzp.zza));
        zzat zza = zzb2.zza();
        if ("_cmp".equals(zza.zza) && "referrer API v2".equals(zza.zzb.zzg("_cis"))) {
            String zzg = zza.zzb.zzg("gclid");
            if (!TextUtils.isEmpty(zzg)) {
                zzU(new zzkq("_lgclid", zza.zzd, zzg, DebugKt.DEBUG_PROPERTY_VALUE_AUTO), zzp);
            }
        }
        zzD(zza, zzp);
    }

    public final void zzG() {
        this.zzs++;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0049 A[Catch: all -> 0x016b, TryCatch #2 {all -> 0x0175, blocks: (B:4:0x0010, B:5:0x0012, B:41:0x00e7, B:42:0x00ec, B:49:0x010b, B:61:0x0165, B:6:0x002c, B:16:0x0049, B:21:0x0063, B:25:0x00a6, B:26:0x00b5, B:29:0x00bd, B:32:0x00c9, B:34:0x00cf, B:39:0x00dc, B:45:0x00f5, B:47:0x0100, B:50:0x0111, B:52:0x0126, B:53:0x0134, B:54:0x0145, B:56:0x0150, B:58:0x0156, B:59:0x015a, B:60:0x015d), top: B:65:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0126 A[Catch: all -> 0x016b, TryCatch #2 {all -> 0x0175, blocks: (B:4:0x0010, B:5:0x0012, B:41:0x00e7, B:42:0x00ec, B:49:0x010b, B:61:0x0165, B:6:0x002c, B:16:0x0049, B:21:0x0063, B:25:0x00a6, B:26:0x00b5, B:29:0x00bd, B:32:0x00c9, B:34:0x00cf, B:39:0x00dc, B:45:0x00f5, B:47:0x0100, B:50:0x0111, B:52:0x0126, B:53:0x0134, B:54:0x0145, B:56:0x0150, B:58:0x0156, B:59:0x015a, B:60:0x015d), top: B:65:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0134 A[Catch: all -> 0x016b, TryCatch #2 {all -> 0x0175, blocks: (B:4:0x0010, B:5:0x0012, B:41:0x00e7, B:42:0x00ec, B:49:0x010b, B:61:0x0165, B:6:0x002c, B:16:0x0049, B:21:0x0063, B:25:0x00a6, B:26:0x00b5, B:29:0x00bd, B:32:0x00c9, B:34:0x00cf, B:39:0x00dc, B:45:0x00f5, B:47:0x0100, B:50:0x0111, B:52:0x0126, B:53:0x0134, B:54:0x0145, B:56:0x0150, B:58:0x0156, B:59:0x015a, B:60:0x015d), top: B:65:0x0010 }] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final void zzH(String str, int i, Throwable th, byte[] bArr, Map<String, List<String>> map) {
        boolean z;
        zzaj zzaj;
        List<String> list;
        String str2;
        zzeo zzeo;
        zzaz().zzg();
        zzB();
        Preconditions.checkNotEmpty(str);
        if (bArr == null) {
            try {
                bArr = new byte[0];
            } finally {
                this.zzt = false;
                zzac();
            }
        }
        zzeg zzj = zzay().zzj();
        Integer valueOf = Integer.valueOf(bArr.length);
        zzj.zzb("onConfigFetched. Response size", valueOf);
        zzaj zzaj2 = this.zze;
        zzak(zzaj2);
        zzaj2.zzw();
        zzaj zzaj3 = this.zze;
        zzak(zzaj3);
        zzg zzj2 = zzaj3.zzj(str);
        if (!(i == 200 || i == 204)) {
            if (i == 304) {
                i = 304;
            }
            z = false;
            if (zzj2 == null) {
                zzay().zzk().zzb("App does not exist in onConfigFetched. appId", zzei.zzn(str));
            } else {
                if (!z && i != 404) {
                    zzj2.zzV(zzav().currentTimeMillis());
                    zzaj zzaj4 = this.zze;
                    zzak(zzaj4);
                    zzaj4.zzD(zzj2);
                    zzay().zzj().zzc("Fetching config failed. code, error", Integer.valueOf(i), th);
                    zzfj zzfj = this.zzc;
                    zzak(zzfj);
                    zzfj.zzi(str);
                    this.zzk.zzd.zzb(zzav().currentTimeMillis());
                    if (i == 503 || i == 429) {
                        this.zzk.zzb.zzb(zzav().currentTimeMillis());
                    }
                    zzaf();
                }
                if (map != null) {
                    list = map.get(HttpHeaders.LAST_MODIFIED);
                } else {
                    list = null;
                }
                if (list == null || list.size() <= 0) {
                    str2 = null;
                } else {
                    str2 = list.get(0);
                }
                if (!(i == 404 || i == 304)) {
                    zzfj zzfj2 = this.zzc;
                    zzak(zzfj2);
                    if (!zzfj2.zzq(str, bArr, str2)) {
                        zzaj = this.zze;
                        zzak(zzaj);
                        zzaj.zzx();
                    }
                    zzj2.zzM(zzav().currentTimeMillis());
                    zzaj zzaj5 = this.zze;
                    zzak(zzaj5);
                    zzaj5.zzD(zzj2);
                    if (i != 404) {
                        zzay().zzl().zzb("Config not found. Using empty config. appId", str);
                    } else {
                        zzay().zzj().zzc("Successfully fetched config. Got network response. code, size", Integer.valueOf(i), valueOf);
                    }
                    zzeo = this.zzd;
                    zzak(zzeo);
                    if (zzeo.zzc() || !zzai()) {
                        zzaf();
                    } else {
                        zzV();
                    }
                }
                zzfj zzfj3 = this.zzc;
                zzak(zzfj3);
                if (zzfj3.zze(str) == null) {
                    zzfj zzfj4 = this.zzc;
                    zzak(zzfj4);
                    if (!zzfj4.zzq(str, null, null)) {
                        zzaj = this.zze;
                        zzak(zzaj);
                        zzaj.zzx();
                    }
                }
                zzj2.zzM(zzav().currentTimeMillis());
                zzaj zzaj52 = this.zze;
                zzak(zzaj52);
                zzaj52.zzD(zzj2);
                if (i != 404) {
                }
                zzeo = this.zzd;
                zzak(zzeo);
                if (zzeo.zzc()) {
                }
                zzaf();
            }
            zzaj zzaj6 = this.zze;
            zzak(zzaj6);
            zzaj6.zzC();
            zzaj = this.zze;
            zzak(zzaj);
            zzaj.zzx();
        }
        if (th == null) {
            z = true;
            if (zzj2 == null) {
            }
            zzaj zzaj62 = this.zze;
            zzak(zzaj62);
            zzaj62.zzC();
            zzaj = this.zze;
            zzak(zzaj);
            zzaj.zzx();
        }
        z = false;
        if (zzj2 == null) {
        }
        zzaj zzaj622 = this.zze;
        zzak(zzaj622);
        zzaj622.zzC();
        zzaj = this.zze;
        zzak(zzaj);
        zzaj.zzx();
    }

    public final void zzI(boolean z) {
        zzaf();
    }

    /* JADX WARN: Finally extract failed */
    public final void zzJ(int i, Throwable th, byte[] bArr, String str) {
        zzaj zzaj;
        long longValue;
        zzaz().zzg();
        zzB();
        if (bArr == null) {
            try {
                bArr = new byte[0];
            } finally {
                this.zzu = false;
                zzac();
            }
        }
        List<Long> list = (List) Preconditions.checkNotNull(this.zzy);
        this.zzy = null;
        if (i != 200) {
            if (i == 204) {
                i = 204;
            }
            zzay().zzj().zzc("Network upload failed. Will retry later. code, error", Integer.valueOf(i), th);
            this.zzk.zzd.zzb(zzav().currentTimeMillis());
            if (i != 503 || i == 429) {
                this.zzk.zzb.zzb(zzav().currentTimeMillis());
            }
            zzaj zzaj2 = this.zze;
            zzak(zzaj2);
            zzaj2.zzy(list);
            zzaf();
        }
        if (th == null) {
            try {
                this.zzk.zzc.zzb(zzav().currentTimeMillis());
                this.zzk.zzd.zzb(0);
                zzaf();
                zzay().zzj().zzc("Successful upload. Got network response. code, size", Integer.valueOf(i), Integer.valueOf(bArr.length));
                zzaj zzaj3 = this.zze;
                zzak(zzaj3);
                zzaj3.zzw();
            } catch (SQLiteException e) {
                zzay().zzd().zzb("Database error while trying to delete uploaded bundles", e);
                this.zza = zzav().elapsedRealtime();
                zzay().zzj().zzb("Disable upload, time", Long.valueOf(this.zza));
            }
            try {
                for (Long l : list) {
                    try {
                        zzaj = this.zze;
                        zzak(zzaj);
                        longValue = l.longValue();
                        zzaj.zzg();
                        zzaj.zzY();
                    } catch (SQLiteException e2) {
                        List<Long> list2 = this.zzz;
                        if (list2 == null || !list2.contains(l)) {
                            throw e2;
                        }
                    }
                    try {
                        if (zzaj.zzh().delete("queue", "rowid=?", new String[]{String.valueOf(longValue)}) != 1) {
                            throw new SQLiteException("Deleted fewer rows from queue than expected");
                            break;
                        }
                    } catch (SQLiteException e3) {
                        zzaj.zzs.zzay().zzd().zzb("Failed to delete a bundle in a queue table", e3);
                        throw e3;
                        break;
                    }
                }
                zzaj zzaj4 = this.zze;
                zzak(zzaj4);
                zzaj4.zzC();
                zzaj zzaj5 = this.zze;
                zzak(zzaj5);
                zzaj5.zzx();
                this.zzz = null;
                zzeo zzeo = this.zzd;
                zzak(zzeo);
                if (!zzeo.zzc() || !zzai()) {
                    this.zzA = -1;
                    zzaf();
                } else {
                    zzV();
                }
                this.zza = 0;
            } catch (Throwable th2) {
                zzaj zzaj6 = this.zze;
                zzak(zzaj6);
                zzaj6.zzx();
                throw th2;
            }
        }
        zzay().zzj().zzc("Network upload failed. Will retry later. code, error", Integer.valueOf(i), th);
        this.zzk.zzd.zzb(zzav().currentTimeMillis());
        if (i != 503) {
        }
        this.zzk.zzb.zzb(zzav().currentTimeMillis());
        zzaj zzaj22 = this.zze;
        zzak(zzaj22);
        zzaj22.zzy(list);
        zzaf();
    }

    /* JADX WARN: Removed duplicated region for block: B:119:0x03d9 A[Catch: all -> 0x05a7, TryCatch #0 {all -> 0x05a7, blocks: (B:23:0x00a5, B:25:0x00b4, B:29:0x00c3, B:31:0x00c7, B:35:0x00d6, B:37:0x00f1, B:39:0x00fb, B:42:0x0105, B:43:0x0114, B:45:0x0127, B:47:0x013d, B:48:0x0164, B:50:0x01b5, B:54:0x01cd, B:56:0x01e5, B:58:0x01f0, B:63:0x0201, B:66:0x020f, B:70:0x021a, B:72:0x021d, B:74:0x023d, B:76:0x0242, B:77:0x0251, B:79:0x0261, B:82:0x0274, B:84:0x02a0, B:87:0x02a8, B:89:0x02b7, B:90:0x02c8, B:92:0x02f3, B:93:0x0304, B:95:0x030c, B:97:0x0312, B:99:0x031c, B:101:0x0326, B:103:0x032c, B:105:0x0332, B:106:0x0337, B:111:0x035b, B:113:0x0360, B:114:0x0374, B:115:0x0384, B:116:0x0394, B:117:0x03a3, B:119:0x03d9, B:120:0x03dc, B:122:0x0405, B:124:0x041b, B:126:0x042c, B:128:0x0440, B:130:0x0448, B:132:0x0452, B:136:0x0465, B:138:0x046e, B:140:0x0478, B:144:0x0484, B:147:0x049c, B:149:0x04ad, B:151:0x04c1, B:153:0x04c7, B:154:0x04ce, B:156:0x04d4, B:159:0x04e1, B:160:0x04e4, B:161:0x04f9, B:163:0x052e, B:164:0x0531, B:165:0x0545, B:167:0x0553, B:168:0x0577, B:170:0x057d, B:171:0x0596), top: B:178:0x00a5, inners: #1, #2, #3, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0405 A[Catch: all -> 0x05a7, TRY_LEAVE, TryCatch #0 {all -> 0x05a7, blocks: (B:23:0x00a5, B:25:0x00b4, B:29:0x00c3, B:31:0x00c7, B:35:0x00d6, B:37:0x00f1, B:39:0x00fb, B:42:0x0105, B:43:0x0114, B:45:0x0127, B:47:0x013d, B:48:0x0164, B:50:0x01b5, B:54:0x01cd, B:56:0x01e5, B:58:0x01f0, B:63:0x0201, B:66:0x020f, B:70:0x021a, B:72:0x021d, B:74:0x023d, B:76:0x0242, B:77:0x0251, B:79:0x0261, B:82:0x0274, B:84:0x02a0, B:87:0x02a8, B:89:0x02b7, B:90:0x02c8, B:92:0x02f3, B:93:0x0304, B:95:0x030c, B:97:0x0312, B:99:0x031c, B:101:0x0326, B:103:0x032c, B:105:0x0332, B:106:0x0337, B:111:0x035b, B:113:0x0360, B:114:0x0374, B:115:0x0384, B:116:0x0394, B:117:0x03a3, B:119:0x03d9, B:120:0x03dc, B:122:0x0405, B:124:0x041b, B:126:0x042c, B:128:0x0440, B:130:0x0448, B:132:0x0452, B:136:0x0465, B:138:0x046e, B:140:0x0478, B:144:0x0484, B:147:0x049c, B:149:0x04ad, B:151:0x04c1, B:153:0x04c7, B:154:0x04ce, B:156:0x04d4, B:159:0x04e1, B:160:0x04e4, B:161:0x04f9, B:163:0x052e, B:164:0x0531, B:165:0x0545, B:167:0x0553, B:168:0x0577, B:170:0x057d, B:171:0x0596), top: B:178:0x00a5, inners: #1, #2, #3, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:159:0x04e1 A[Catch: all -> 0x05a7, TryCatch #0 {all -> 0x05a7, blocks: (B:23:0x00a5, B:25:0x00b4, B:29:0x00c3, B:31:0x00c7, B:35:0x00d6, B:37:0x00f1, B:39:0x00fb, B:42:0x0105, B:43:0x0114, B:45:0x0127, B:47:0x013d, B:48:0x0164, B:50:0x01b5, B:54:0x01cd, B:56:0x01e5, B:58:0x01f0, B:63:0x0201, B:66:0x020f, B:70:0x021a, B:72:0x021d, B:74:0x023d, B:76:0x0242, B:77:0x0251, B:79:0x0261, B:82:0x0274, B:84:0x02a0, B:87:0x02a8, B:89:0x02b7, B:90:0x02c8, B:92:0x02f3, B:93:0x0304, B:95:0x030c, B:97:0x0312, B:99:0x031c, B:101:0x0326, B:103:0x032c, B:105:0x0332, B:106:0x0337, B:111:0x035b, B:113:0x0360, B:114:0x0374, B:115:0x0384, B:116:0x0394, B:117:0x03a3, B:119:0x03d9, B:120:0x03dc, B:122:0x0405, B:124:0x041b, B:126:0x042c, B:128:0x0440, B:130:0x0448, B:132:0x0452, B:136:0x0465, B:138:0x046e, B:140:0x0478, B:144:0x0484, B:147:0x049c, B:149:0x04ad, B:151:0x04c1, B:153:0x04c7, B:154:0x04ce, B:156:0x04d4, B:159:0x04e1, B:160:0x04e4, B:161:0x04f9, B:163:0x052e, B:164:0x0531, B:165:0x0545, B:167:0x0553, B:168:0x0577, B:170:0x057d, B:171:0x0596), top: B:178:0x00a5, inners: #1, #2, #3, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:168:0x0577 A[Catch: all -> 0x05a7, TryCatch #0 {all -> 0x05a7, blocks: (B:23:0x00a5, B:25:0x00b4, B:29:0x00c3, B:31:0x00c7, B:35:0x00d6, B:37:0x00f1, B:39:0x00fb, B:42:0x0105, B:43:0x0114, B:45:0x0127, B:47:0x013d, B:48:0x0164, B:50:0x01b5, B:54:0x01cd, B:56:0x01e5, B:58:0x01f0, B:63:0x0201, B:66:0x020f, B:70:0x021a, B:72:0x021d, B:74:0x023d, B:76:0x0242, B:77:0x0251, B:79:0x0261, B:82:0x0274, B:84:0x02a0, B:87:0x02a8, B:89:0x02b7, B:90:0x02c8, B:92:0x02f3, B:93:0x0304, B:95:0x030c, B:97:0x0312, B:99:0x031c, B:101:0x0326, B:103:0x032c, B:105:0x0332, B:106:0x0337, B:111:0x035b, B:113:0x0360, B:114:0x0374, B:115:0x0384, B:116:0x0394, B:117:0x03a3, B:119:0x03d9, B:120:0x03dc, B:122:0x0405, B:124:0x041b, B:126:0x042c, B:128:0x0440, B:130:0x0448, B:132:0x0452, B:136:0x0465, B:138:0x046e, B:140:0x0478, B:144:0x0484, B:147:0x049c, B:149:0x04ad, B:151:0x04c1, B:153:0x04c7, B:154:0x04ce, B:156:0x04d4, B:159:0x04e1, B:160:0x04e4, B:161:0x04f9, B:163:0x052e, B:164:0x0531, B:165:0x0545, B:167:0x0553, B:168:0x0577, B:170:0x057d, B:171:0x0596), top: B:178:0x00a5, inners: #1, #2, #3, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:186:0x041b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x01b5 A[Catch: SQLiteException -> 0x01cc, all -> 0x05a7, TRY_LEAVE, TryCatch #2 {SQLiteException -> 0x01cc, blocks: (B:48:0x0164, B:50:0x01b5), top: B:182:0x0164, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x01ca  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x01e5 A[Catch: all -> 0x05a7, TryCatch #0 {all -> 0x05a7, blocks: (B:23:0x00a5, B:25:0x00b4, B:29:0x00c3, B:31:0x00c7, B:35:0x00d6, B:37:0x00f1, B:39:0x00fb, B:42:0x0105, B:43:0x0114, B:45:0x0127, B:47:0x013d, B:48:0x0164, B:50:0x01b5, B:54:0x01cd, B:56:0x01e5, B:58:0x01f0, B:63:0x0201, B:66:0x020f, B:70:0x021a, B:72:0x021d, B:74:0x023d, B:76:0x0242, B:77:0x0251, B:79:0x0261, B:82:0x0274, B:84:0x02a0, B:87:0x02a8, B:89:0x02b7, B:90:0x02c8, B:92:0x02f3, B:93:0x0304, B:95:0x030c, B:97:0x0312, B:99:0x031c, B:101:0x0326, B:103:0x032c, B:105:0x0332, B:106:0x0337, B:111:0x035b, B:113:0x0360, B:114:0x0374, B:115:0x0384, B:116:0x0394, B:117:0x03a3, B:119:0x03d9, B:120:0x03dc, B:122:0x0405, B:124:0x041b, B:126:0x042c, B:128:0x0440, B:130:0x0448, B:132:0x0452, B:136:0x0465, B:138:0x046e, B:140:0x0478, B:144:0x0484, B:147:0x049c, B:149:0x04ad, B:151:0x04c1, B:153:0x04c7, B:154:0x04ce, B:156:0x04d4, B:159:0x04e1, B:160:0x04e4, B:161:0x04f9, B:163:0x052e, B:164:0x0531, B:165:0x0545, B:167:0x0553, B:168:0x0577, B:170:0x057d, B:171:0x0596), top: B:178:0x00a5, inners: #1, #2, #3, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x021d A[Catch: all -> 0x05a7, TryCatch #0 {all -> 0x05a7, blocks: (B:23:0x00a5, B:25:0x00b4, B:29:0x00c3, B:31:0x00c7, B:35:0x00d6, B:37:0x00f1, B:39:0x00fb, B:42:0x0105, B:43:0x0114, B:45:0x0127, B:47:0x013d, B:48:0x0164, B:50:0x01b5, B:54:0x01cd, B:56:0x01e5, B:58:0x01f0, B:63:0x0201, B:66:0x020f, B:70:0x021a, B:72:0x021d, B:74:0x023d, B:76:0x0242, B:77:0x0251, B:79:0x0261, B:82:0x0274, B:84:0x02a0, B:87:0x02a8, B:89:0x02b7, B:90:0x02c8, B:92:0x02f3, B:93:0x0304, B:95:0x030c, B:97:0x0312, B:99:0x031c, B:101:0x0326, B:103:0x032c, B:105:0x0332, B:106:0x0337, B:111:0x035b, B:113:0x0360, B:114:0x0374, B:115:0x0384, B:116:0x0394, B:117:0x03a3, B:119:0x03d9, B:120:0x03dc, B:122:0x0405, B:124:0x041b, B:126:0x042c, B:128:0x0440, B:130:0x0448, B:132:0x0452, B:136:0x0465, B:138:0x046e, B:140:0x0478, B:144:0x0484, B:147:0x049c, B:149:0x04ad, B:151:0x04c1, B:153:0x04c7, B:154:0x04ce, B:156:0x04d4, B:159:0x04e1, B:160:0x04e4, B:161:0x04f9, B:163:0x052e, B:164:0x0531, B:165:0x0545, B:167:0x0553, B:168:0x0577, B:170:0x057d, B:171:0x0596), top: B:178:0x00a5, inners: #1, #2, #3, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x023b  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0242 A[Catch: all -> 0x05a7, TryCatch #0 {all -> 0x05a7, blocks: (B:23:0x00a5, B:25:0x00b4, B:29:0x00c3, B:31:0x00c7, B:35:0x00d6, B:37:0x00f1, B:39:0x00fb, B:42:0x0105, B:43:0x0114, B:45:0x0127, B:47:0x013d, B:48:0x0164, B:50:0x01b5, B:54:0x01cd, B:56:0x01e5, B:58:0x01f0, B:63:0x0201, B:66:0x020f, B:70:0x021a, B:72:0x021d, B:74:0x023d, B:76:0x0242, B:77:0x0251, B:79:0x0261, B:82:0x0274, B:84:0x02a0, B:87:0x02a8, B:89:0x02b7, B:90:0x02c8, B:92:0x02f3, B:93:0x0304, B:95:0x030c, B:97:0x0312, B:99:0x031c, B:101:0x0326, B:103:0x032c, B:105:0x0332, B:106:0x0337, B:111:0x035b, B:113:0x0360, B:114:0x0374, B:115:0x0384, B:116:0x0394, B:117:0x03a3, B:119:0x03d9, B:120:0x03dc, B:122:0x0405, B:124:0x041b, B:126:0x042c, B:128:0x0440, B:130:0x0448, B:132:0x0452, B:136:0x0465, B:138:0x046e, B:140:0x0478, B:144:0x0484, B:147:0x049c, B:149:0x04ad, B:151:0x04c1, B:153:0x04c7, B:154:0x04ce, B:156:0x04d4, B:159:0x04e1, B:160:0x04e4, B:161:0x04f9, B:163:0x052e, B:164:0x0531, B:165:0x0545, B:167:0x0553, B:168:0x0577, B:170:0x057d, B:171:0x0596), top: B:178:0x00a5, inners: #1, #2, #3, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0251 A[Catch: all -> 0x05a7, TryCatch #0 {all -> 0x05a7, blocks: (B:23:0x00a5, B:25:0x00b4, B:29:0x00c3, B:31:0x00c7, B:35:0x00d6, B:37:0x00f1, B:39:0x00fb, B:42:0x0105, B:43:0x0114, B:45:0x0127, B:47:0x013d, B:48:0x0164, B:50:0x01b5, B:54:0x01cd, B:56:0x01e5, B:58:0x01f0, B:63:0x0201, B:66:0x020f, B:70:0x021a, B:72:0x021d, B:74:0x023d, B:76:0x0242, B:77:0x0251, B:79:0x0261, B:82:0x0274, B:84:0x02a0, B:87:0x02a8, B:89:0x02b7, B:90:0x02c8, B:92:0x02f3, B:93:0x0304, B:95:0x030c, B:97:0x0312, B:99:0x031c, B:101:0x0326, B:103:0x032c, B:105:0x0332, B:106:0x0337, B:111:0x035b, B:113:0x0360, B:114:0x0374, B:115:0x0384, B:116:0x0394, B:117:0x03a3, B:119:0x03d9, B:120:0x03dc, B:122:0x0405, B:124:0x041b, B:126:0x042c, B:128:0x0440, B:130:0x0448, B:132:0x0452, B:136:0x0465, B:138:0x046e, B:140:0x0478, B:144:0x0484, B:147:0x049c, B:149:0x04ad, B:151:0x04c1, B:153:0x04c7, B:154:0x04ce, B:156:0x04d4, B:159:0x04e1, B:160:0x04e4, B:161:0x04f9, B:163:0x052e, B:164:0x0531, B:165:0x0545, B:167:0x0553, B:168:0x0577, B:170:0x057d, B:171:0x0596), top: B:178:0x00a5, inners: #1, #2, #3, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0261 A[Catch: all -> 0x05a7, TRY_LEAVE, TryCatch #0 {all -> 0x05a7, blocks: (B:23:0x00a5, B:25:0x00b4, B:29:0x00c3, B:31:0x00c7, B:35:0x00d6, B:37:0x00f1, B:39:0x00fb, B:42:0x0105, B:43:0x0114, B:45:0x0127, B:47:0x013d, B:48:0x0164, B:50:0x01b5, B:54:0x01cd, B:56:0x01e5, B:58:0x01f0, B:63:0x0201, B:66:0x020f, B:70:0x021a, B:72:0x021d, B:74:0x023d, B:76:0x0242, B:77:0x0251, B:79:0x0261, B:82:0x0274, B:84:0x02a0, B:87:0x02a8, B:89:0x02b7, B:90:0x02c8, B:92:0x02f3, B:93:0x0304, B:95:0x030c, B:97:0x0312, B:99:0x031c, B:101:0x0326, B:103:0x032c, B:105:0x0332, B:106:0x0337, B:111:0x035b, B:113:0x0360, B:114:0x0374, B:115:0x0384, B:116:0x0394, B:117:0x03a3, B:119:0x03d9, B:120:0x03dc, B:122:0x0405, B:124:0x041b, B:126:0x042c, B:128:0x0440, B:130:0x0448, B:132:0x0452, B:136:0x0465, B:138:0x046e, B:140:0x0478, B:144:0x0484, B:147:0x049c, B:149:0x04ad, B:151:0x04c1, B:153:0x04c7, B:154:0x04ce, B:156:0x04d4, B:159:0x04e1, B:160:0x04e4, B:161:0x04f9, B:163:0x052e, B:164:0x0531, B:165:0x0545, B:167:0x0553, B:168:0x0577, B:170:0x057d, B:171:0x0596), top: B:178:0x00a5, inners: #1, #2, #3, #4 }] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final void zzK(zzp zzp) {
        String str;
        int i;
        zzg zzj;
        String str2;
        boolean z;
        zzap zzap;
        long j;
        String str3;
        PackageInfo packageInfo;
        long j2;
        ApplicationInfo applicationInfo;
        boolean z2;
        boolean z3;
        String zzw;
        zzaj zzaj;
        String zzt;
        int delete;
        zzaz().zzg();
        zzB();
        Preconditions.checkNotNull(zzp);
        Preconditions.checkNotEmpty(zzp.zza);
        if (zzag(zzp)) {
            zzaj zzaj2 = this.zze;
            zzak(zzaj2);
            zzg zzj2 = zzaj2.zzj(zzp.zza);
            if (zzj2 != null && TextUtils.isEmpty(zzj2.zzz()) && !TextUtils.isEmpty(zzp.zzb)) {
                zzj2.zzM(0);
                zzaj zzaj3 = this.zze;
                zzak(zzaj3);
                zzaj3.zzD(zzj2);
                zzfj zzfj = this.zzc;
                zzak(zzfj);
                zzfj.zzj(zzp.zza);
            }
            if (!zzp.zzh) {
                zzd(zzp);
                return;
            }
            long j3 = zzp.zzm;
            if (j3 == 0) {
                j3 = zzav().currentTimeMillis();
            }
            this.zzn.zzg().zzd();
            int i2 = zzp.zzn;
            if (!(i2 == 0 || i2 == 1)) {
                zzay().zzk().zzc("Incorrect app type, assuming installed app. appId, appType", zzei.zzn(zzp.zza), Integer.valueOf(i2));
                i2 = 0;
            }
            zzaj zzaj4 = this.zze;
            zzak(zzaj4);
            zzaj4.zzw();
            try {
                zzaj zzaj5 = this.zze;
                zzak(zzaj5);
                zzks zzp2 = zzaj5.zzp(zzp.zza, "_npa");
                if (zzp2 != null && !DebugKt.DEBUG_PROPERTY_VALUE_AUTO.equals(zzp2.zzb)) {
                    str = "_pfo";
                    i = 1;
                    zzaj zzaj6 = this.zze;
                    zzak(zzaj6);
                    zzj = zzaj6.zzj((String) Preconditions.checkNotNull(zzp.zza));
                    if (zzj != null && zzv().zzam(zzp.zzb, zzj.zzz(), zzp.zzq, zzj.zzr())) {
                        zzay().zzk().zzb("New GMP App Id passed in. Removing cached database data. appId", zzei.zzn(zzj.zzt()));
                        zzaj = this.zze;
                        zzak(zzaj);
                        zzt = zzj.zzt();
                        zzaj.zzY();
                        zzaj.zzg();
                        Preconditions.checkNotEmpty(zzt);
                        try {
                            SQLiteDatabase zzh = zzaj.zzh();
                            String[] strArr = new String[i];
                            strArr[0] = zzt;
                            delete = zzh.delete("events", "app_id=?", strArr) + zzh.delete("user_attributes", "app_id=?", strArr) + zzh.delete("conditional_properties", "app_id=?", strArr) + zzh.delete("apps", "app_id=?", strArr) + zzh.delete("raw_events", "app_id=?", strArr) + zzh.delete("raw_events_metadata", "app_id=?", strArr) + zzh.delete("event_filters", "app_id=?", strArr) + zzh.delete("property_filters", "app_id=?", strArr) + zzh.delete("audience_filter_values", "app_id=?", strArr) + zzh.delete("consent_settings", "app_id=?", strArr);
                            if (delete <= 0) {
                                zzaj.zzs.zzay().zzj().zzc("Deleted application data. app, records", zzt, Integer.valueOf(delete));
                                zzj = null;
                            } else {
                                zzj = null;
                            }
                        } catch (SQLiteException e) {
                            zzaj.zzs.zzay().zzd().zzc("Error deleting application data. appId, error", zzei.zzn(zzt), e);
                            zzj = null;
                        }
                    }
                    if (zzj == null) {
                        if (zzj.zzb() != -2147483648L) {
                            str2 = "_uwa";
                            if (zzj.zzb() != zzp.zzj) {
                                z3 = true;
                                zzw = zzj.zzw();
                                if (z3 || ((zzj.zzb() == -2147483648L || zzw == null || zzw.equals(zzp.zzc)) ? false : true)) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("_pv", zzw);
                                    zzD(new zzat("_au", new zzar(bundle), DebugKt.DEBUG_PROPERTY_VALUE_AUTO, j3), zzp);
                                }
                            }
                        } else {
                            str2 = "_uwa";
                        }
                        z3 = false;
                        zzw = zzj.zzw();
                        if (z3 || ((zzj.zzb() == -2147483648L || zzw == null || zzw.equals(zzp.zzc)) ? false : true)) {
                        }
                    } else {
                        str2 = "_uwa";
                    }
                    zzd(zzp);
                    if (i2 != 0) {
                        zzaj zzaj7 = this.zze;
                        zzak(zzaj7);
                        zzap = zzaj7.zzn(zzp.zza, "_f");
                        z = false;
                    } else {
                        zzaj zzaj8 = this.zze;
                        zzak(zzaj8);
                        zzap = zzaj8.zzn(zzp.zza, "_v");
                        z = true;
                    }
                    if (zzap != null) {
                        long j4 = ((j3 / 3600000) + 1) * 3600000;
                        if (!z) {
                            str3 = "_et";
                            zzU(new zzkq("_fot", j3, Long.valueOf(j4), DebugKt.DEBUG_PROPERTY_VALUE_AUTO), zzp);
                            zzaz().zzg();
                            zzfa zzfa = (zzfa) Preconditions.checkNotNull(this.zzm);
                            String str4 = zzp.zza;
                            if (str4 != null && !str4.isEmpty()) {
                                zzfa.zza.zzaz().zzg();
                                if (!zzfa.zza()) {
                                    zzfa.zza.zzay().zzi().zza("Install Referrer Reporter is not available");
                                } else {
                                    zzez zzez = new zzez(zzfa, str4);
                                    zzfa.zza.zzaz().zzg();
                                    Intent intent = new Intent("com.google.android.finsky.BIND_GET_INSTALL_REFERRER_SERVICE");
                                    intent.setComponent(new ComponentName("com.android.vending", "com.google.android.finsky.externalreferrer.GetInstallReferrerService"));
                                    PackageManager packageManager = zzfa.zza.zzau().getPackageManager();
                                    if (packageManager == null) {
                                        zzfa.zza.zzay().zzm().zza("Failed to obtain Package Manager to verify binding conditions for Install Referrer");
                                    } else {
                                        List<ResolveInfo> queryIntentServices = packageManager.queryIntentServices(intent, 0);
                                        if (queryIntentServices == null || queryIntentServices.isEmpty()) {
                                            zzfa.zza.zzay().zzi().zza("Play Service for fetching Install Referrer is unavailable on device");
                                        } else {
                                            ResolveInfo resolveInfo = queryIntentServices.get(0);
                                            if (resolveInfo.serviceInfo != null) {
                                                String str5 = resolveInfo.serviceInfo.packageName;
                                                if (resolveInfo.serviceInfo.name == null || !"com.android.vending".equals(str5) || !zzfa.zza()) {
                                                    zzfa.zza.zzay().zzk().zza("Play Store version 8.3.73 or higher required for Install Referrer");
                                                } else {
                                                    try {
                                                        boolean bindService = ConnectionTracker.getInstance().bindService(zzfa.zza.zzau(), new Intent(intent), zzez, 1);
                                                        zzeg zzj3 = zzfa.zza.zzay().zzj();
                                                        String str6 = "not available";
                                                        if (true == bindService) {
                                                            str6 = "available";
                                                        }
                                                        zzj3.zzb("Install Referrer Service is", str6);
                                                    } catch (RuntimeException e2) {
                                                        zzfa.zza.zzay().zzd().zzb("Exception occurred while binding to Install Referrer Service", e2.getMessage());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                zzaz().zzg();
                                zzB();
                                Bundle bundle2 = new Bundle();
                                bundle2.putLong("_c", 1);
                                bundle2.putLong("_r", 1);
                                bundle2.putLong(str2, 0);
                                bundle2.putLong(str, 0);
                                bundle2.putLong("_sys", 0);
                                bundle2.putLong("_sysu", 0);
                                bundle2.putLong(str3, 1);
                                if (zzp.zzp) {
                                    bundle2.putLong("_dac", 1);
                                }
                                String str7 = (String) Preconditions.checkNotNull(zzp.zza);
                                zzaj zzaj9 = this.zze;
                                zzak(zzaj9);
                                Preconditions.checkNotEmpty(str7);
                                zzaj9.zzg();
                                zzaj9.zzY();
                                long zzc = zzaj9.zzc(str7, "first_open_count");
                                if (this.zzn.zzau().getPackageManager() != null) {
                                    zzay().zzd().zzb("PackageManager is null, first open report might be inaccurate. appId", zzei.zzn(str7));
                                    j = j3;
                                    j2 = zzc;
                                } else {
                                    try {
                                        packageInfo = Wrappers.packageManager(this.zzn.zzau()).getPackageInfo(str7, 0);
                                    } catch (PackageManager.NameNotFoundException e3) {
                                        zzay().zzd().zzc("Package info is null, first open report might be inaccurate. appId", zzei.zzn(str7), e3);
                                        packageInfo = null;
                                    }
                                    if (packageInfo == null) {
                                        j = j3;
                                        j2 = zzc;
                                    } else if (packageInfo.firstInstallTime != 0) {
                                        j = j3;
                                        if (packageInfo.firstInstallTime == packageInfo.lastUpdateTime) {
                                            z2 = true;
                                        } else if (!zzg().zzs(null, zzdw.zzah)) {
                                            bundle2.putLong(str2, 1);
                                            z2 = false;
                                        } else if (zzc == 0) {
                                            bundle2.putLong(str2, 1);
                                            z2 = false;
                                        } else {
                                            z2 = false;
                                        }
                                        j2 = zzc;
                                        zzU(new zzkq("_fi", j, Long.valueOf(true != z2 ? 0 : 1), DebugKt.DEBUG_PROPERTY_VALUE_AUTO), zzp);
                                    } else {
                                        j = j3;
                                        j2 = zzc;
                                    }
                                    try {
                                        applicationInfo = Wrappers.packageManager(this.zzn.zzau()).getApplicationInfo(str7, 0);
                                    } catch (PackageManager.NameNotFoundException e4) {
                                        zzay().zzd().zzc("Application info is null, first open report might be inaccurate. appId", zzei.zzn(str7), e4);
                                        applicationInfo = null;
                                    }
                                    if (applicationInfo != null) {
                                        if ((applicationInfo.flags & 1) != 0) {
                                            bundle2.putLong("_sys", 1);
                                        }
                                        if ((applicationInfo.flags & 128) != 0) {
                                            bundle2.putLong("_sysu", 1);
                                        }
                                    }
                                }
                                if (j2 >= 0) {
                                    bundle2.putLong(str, j2);
                                }
                                zzF(new zzat("_f", new zzar(bundle2), DebugKt.DEBUG_PROPERTY_VALUE_AUTO, j), zzp);
                            }
                            zzfa.zza.zzay().zzm().zza("Install Referrer Reporter was called with invalid app package name");
                            zzaz().zzg();
                            zzB();
                            Bundle bundle22 = new Bundle();
                            bundle22.putLong("_c", 1);
                            bundle22.putLong("_r", 1);
                            bundle22.putLong(str2, 0);
                            bundle22.putLong(str, 0);
                            bundle22.putLong("_sys", 0);
                            bundle22.putLong("_sysu", 0);
                            bundle22.putLong(str3, 1);
                            if (zzp.zzp) {
                            }
                            String str72 = (String) Preconditions.checkNotNull(zzp.zza);
                            zzaj zzaj92 = this.zze;
                            zzak(zzaj92);
                            Preconditions.checkNotEmpty(str72);
                            zzaj92.zzg();
                            zzaj92.zzY();
                            long zzc2 = zzaj92.zzc(str72, "first_open_count");
                            if (this.zzn.zzau().getPackageManager() != null) {
                            }
                            if (j2 >= 0) {
                            }
                            zzF(new zzat("_f", new zzar(bundle22), DebugKt.DEBUG_PROPERTY_VALUE_AUTO, j), zzp);
                        } else {
                            j = j3;
                            str3 = "_et";
                            zzU(new zzkq("_fvt", j, Long.valueOf(j4), DebugKt.DEBUG_PROPERTY_VALUE_AUTO), zzp);
                            zzaz().zzg();
                            zzB();
                            Bundle bundle3 = new Bundle();
                            bundle3.putLong("_c", 1);
                            bundle3.putLong("_r", 1);
                            bundle3.putLong(str3, 1);
                            if (zzp.zzp) {
                                bundle3.putLong("_dac", 1);
                            }
                            zzF(new zzat("_v", new zzar(bundle3), DebugKt.DEBUG_PROPERTY_VALUE_AUTO, j), zzp);
                        }
                        if (!zzg().zzs(zzp.zza, zzdw.zzT)) {
                            Bundle bundle4 = new Bundle();
                            bundle4.putLong(str3, 1);
                            bundle4.putLong("_fr", 1);
                            zzF(new zzat("_e", new zzar(bundle4), DebugKt.DEBUG_PROPERTY_VALUE_AUTO, j), zzp);
                        }
                    } else if (zzp.zzi) {
                        zzF(new zzat("_cd", new zzar(new Bundle()), DebugKt.DEBUG_PROPERTY_VALUE_AUTO, j3), zzp);
                    }
                    zzaj zzaj10 = this.zze;
                    zzak(zzaj10);
                    zzaj10.zzC();
                }
                Boolean bool = zzp.zzr;
                if (bool != null) {
                    str = "_pfo";
                    i = 1;
                    zzkq zzkq = new zzkq("_npa", j3, Long.valueOf(true != bool.booleanValue() ? 0 : 1), DebugKt.DEBUG_PROPERTY_VALUE_AUTO);
                    if (zzp2 == null || !zzp2.zze.equals(zzkq.zzd)) {
                        zzU(zzkq, zzp);
                    }
                } else {
                    str = "_pfo";
                    i = 1;
                    if (zzp2 != null) {
                        zzO(new zzkq("_npa", j3, null, DebugKt.DEBUG_PROPERTY_VALUE_AUTO), zzp);
                    }
                }
                zzaj zzaj62 = this.zze;
                zzak(zzaj62);
                zzj = zzaj62.zzj((String) Preconditions.checkNotNull(zzp.zza));
                if (zzj != null) {
                    zzay().zzk().zzb("New GMP App Id passed in. Removing cached database data. appId", zzei.zzn(zzj.zzt()));
                    zzaj = this.zze;
                    zzak(zzaj);
                    zzt = zzj.zzt();
                    zzaj.zzY();
                    zzaj.zzg();
                    Preconditions.checkNotEmpty(zzt);
                    SQLiteDatabase zzh2 = zzaj.zzh();
                    String[] strArr2 = new String[i];
                    strArr2[0] = zzt;
                    delete = zzh2.delete("events", "app_id=?", strArr2) + zzh2.delete("user_attributes", "app_id=?", strArr2) + zzh2.delete("conditional_properties", "app_id=?", strArr2) + zzh2.delete("apps", "app_id=?", strArr2) + zzh2.delete("raw_events", "app_id=?", strArr2) + zzh2.delete("raw_events_metadata", "app_id=?", strArr2) + zzh2.delete("event_filters", "app_id=?", strArr2) + zzh2.delete("property_filters", "app_id=?", strArr2) + zzh2.delete("audience_filter_values", "app_id=?", strArr2) + zzh2.delete("consent_settings", "app_id=?", strArr2);
                    if (delete <= 0) {
                    }
                }
                if (zzj == null) {
                }
                zzd(zzp);
                if (i2 != 0) {
                }
                if (zzap != null) {
                }
                zzaj zzaj102 = this.zze;
                zzak(zzaj102);
                zzaj102.zzC();
            } finally {
                zzaj zzaj11 = this.zze;
                zzak(zzaj11);
                zzaj11.zzx();
            }
        }
    }

    public final void zzL() {
        this.zzr++;
    }

    public final void zzM(zzab zzab) {
        zzp zzaa = zzaa((String) Preconditions.checkNotNull(zzab.zza));
        if (zzaa != null) {
            zzN(zzab, zzaa);
        }
    }

    public final void zzN(zzab zzab, zzp zzp) {
        Bundle bundle;
        Preconditions.checkNotNull(zzab);
        Preconditions.checkNotEmpty(zzab.zza);
        Preconditions.checkNotNull(zzab.zzc);
        Preconditions.checkNotEmpty(zzab.zzc.zzb);
        zzaz().zzg();
        zzB();
        if (zzag(zzp)) {
            if (zzp.zzh) {
                zzaj zzaj = this.zze;
                zzak(zzaj);
                zzaj.zzw();
                try {
                    zzd(zzp);
                    String str = (String) Preconditions.checkNotNull(zzab.zza);
                    zzaj zzaj2 = this.zze;
                    zzak(zzaj2);
                    zzab zzk = zzaj2.zzk(str, zzab.zzc.zzb);
                    if (zzk != null) {
                        zzay().zzc().zzc("Removing conditional user property", zzab.zza, this.zzn.zzj().zze(zzab.zzc.zzb));
                        zzaj zzaj3 = this.zze;
                        zzak(zzaj3);
                        zzaj3.zza(str, zzab.zzc.zzb);
                        if (zzk.zze) {
                            zzaj zzaj4 = this.zze;
                            zzak(zzaj4);
                            zzaj4.zzA(str, zzab.zzc.zzb);
                        }
                        zzat zzat = zzab.zzk;
                        if (zzat != null) {
                            zzar zzar = zzat.zzb;
                            if (zzar != null) {
                                bundle = zzar.zzc();
                            } else {
                                bundle = null;
                            }
                            zzW((zzat) Preconditions.checkNotNull(zzv().zzz(str, ((zzat) Preconditions.checkNotNull(zzab.zzk)).zza, bundle, zzk.zzb, zzab.zzk.zzd, true, true)), zzp);
                        }
                    } else {
                        zzay().zzk().zzc("Conditional user property doesn't exist", zzei.zzn(zzab.zza), this.zzn.zzj().zze(zzab.zzc.zzb));
                    }
                    zzaj zzaj5 = this.zze;
                    zzak(zzaj5);
                    zzaj5.zzC();
                } finally {
                    zzaj zzaj6 = this.zze;
                    zzak(zzaj6);
                    zzaj6.zzx();
                }
            } else {
                zzd(zzp);
            }
        }
    }

    public final void zzO(zzkq zzkq, zzp zzp) {
        long j;
        zzaz().zzg();
        zzB();
        if (zzag(zzp)) {
            if (!zzp.zzh) {
                zzd(zzp);
            } else if (!"_npa".equals(zzkq.zzb) || zzp.zzr == null) {
                zzay().zzc().zzb("Removing user property", this.zzn.zzj().zze(zzkq.zzb));
                zzaj zzaj = this.zze;
                zzak(zzaj);
                zzaj.zzw();
                try {
                    zzd(zzp);
                    zzaj zzaj2 = this.zze;
                    zzak(zzaj2);
                    zzaj2.zzA((String) Preconditions.checkNotNull(zzp.zza), zzkq.zzb);
                    zzaj zzaj3 = this.zze;
                    zzak(zzaj3);
                    zzaj3.zzC();
                    zzay().zzc().zzb("User property removed", this.zzn.zzj().zze(zzkq.zzb));
                } finally {
                    zzaj zzaj4 = this.zze;
                    zzak(zzaj4);
                    zzaj4.zzx();
                }
            } else {
                zzay().zzc().zza("Falling back to manifest metadata value for ad personalization");
                long currentTimeMillis = zzav().currentTimeMillis();
                if (true != zzp.zzr.booleanValue()) {
                    j = 0;
                } else {
                    j = 1;
                }
                zzU(new zzkq("_npa", currentTimeMillis, Long.valueOf(j), DebugKt.DEBUG_PROPERTY_VALUE_AUTO), zzp);
            }
        }
    }

    public final void zzP(zzp zzp) {
        if (this.zzy != null) {
            this.zzz = new ArrayList();
            this.zzz.addAll(this.zzy);
        }
        zzaj zzaj = this.zze;
        zzak(zzaj);
        String str = (String) Preconditions.checkNotNull(zzp.zza);
        Preconditions.checkNotEmpty(str);
        zzaj.zzg();
        zzaj.zzY();
        try {
            SQLiteDatabase zzh = zzaj.zzh();
            String[] strArr = {str};
            int delete = zzh.delete("apps", "app_id=?", strArr) + zzh.delete("events", "app_id=?", strArr) + zzh.delete("user_attributes", "app_id=?", strArr) + zzh.delete("conditional_properties", "app_id=?", strArr) + zzh.delete("raw_events", "app_id=?", strArr) + zzh.delete("raw_events_metadata", "app_id=?", strArr) + zzh.delete("queue", "app_id=?", strArr) + zzh.delete("audience_filter_values", "app_id=?", strArr) + zzh.delete("main_event_params", "app_id=?", strArr) + zzh.delete("default_event_params", "app_id=?", strArr);
            if (delete > 0) {
                zzaj.zzs.zzay().zzj().zzc("Reset analytics data. app, records", str, Integer.valueOf(delete));
            }
        } catch (SQLiteException e) {
            zzaj.zzs.zzay().zzd().zzc("Error resetting analytics data. appId, error", zzei.zzn(str), e);
        }
        if (zzp.zzh) {
            zzK(zzp);
        }
    }

    public final void zzQ() {
        zzaz().zzg();
        zzaj zzaj = this.zze;
        zzak(zzaj);
        zzaj.zzz();
        if (this.zzk.zzc.zza() == 0) {
            this.zzk.zzc.zzb(zzav().currentTimeMillis());
        }
        zzaf();
    }

    public final void zzR(zzab zzab) {
        zzp zzaa = zzaa((String) Preconditions.checkNotNull(zzab.zza));
        if (zzaa != null) {
            zzS(zzab, zzaa);
        }
    }

    public final void zzS(zzab zzab, zzp zzp) {
        zzat zzat;
        Preconditions.checkNotNull(zzab);
        Preconditions.checkNotEmpty(zzab.zza);
        Preconditions.checkNotNull(zzab.zzb);
        Preconditions.checkNotNull(zzab.zzc);
        Preconditions.checkNotEmpty(zzab.zzc.zzb);
        zzaz().zzg();
        zzB();
        if (zzag(zzp)) {
            if (!zzp.zzh) {
                zzd(zzp);
                return;
            }
            zzab zzab2 = new zzab(zzab);
            boolean z = false;
            zzab2.zze = false;
            zzaj zzaj = this.zze;
            zzak(zzaj);
            zzaj.zzw();
            try {
                zzaj zzaj2 = this.zze;
                zzak(zzaj2);
                zzab zzk = zzaj2.zzk((String) Preconditions.checkNotNull(zzab2.zza), zzab2.zzc.zzb);
                if (zzk != null && !zzk.zzb.equals(zzab2.zzb)) {
                    zzay().zzk().zzd("Updating a conditional user property with different origin. name, origin, origin (from DB)", this.zzn.zzj().zze(zzab2.zzc.zzb), zzab2.zzb, zzk.zzb);
                }
                if (zzk != null && zzk.zze) {
                    zzab2.zzb = zzk.zzb;
                    zzab2.zzd = zzk.zzd;
                    zzab2.zzh = zzk.zzh;
                    zzab2.zzf = zzk.zzf;
                    zzab2.zzi = zzk.zzi;
                    zzab2.zze = true;
                    zzkq zzkq = zzab2.zzc;
                    zzab2.zzc = new zzkq(zzkq.zzb, zzk.zzc.zzc, zzkq.zza(), zzk.zzc.zzf);
                } else if (TextUtils.isEmpty(zzab2.zzf)) {
                    zzkq zzkq2 = zzab2.zzc;
                    zzab2.zzc = new zzkq(zzkq2.zzb, zzab2.zzd, zzkq2.zza(), zzab2.zzc.zzf);
                    zzab2.zze = true;
                    z = true;
                }
                if (zzab2.zze) {
                    zzkq zzkq3 = zzab2.zzc;
                    zzks zzks = new zzks((String) Preconditions.checkNotNull(zzab2.zza), zzab2.zzb, zzkq3.zzb, zzkq3.zzc, Preconditions.checkNotNull(zzkq3.zza()));
                    zzaj zzaj3 = this.zze;
                    zzak(zzaj3);
                    if (zzaj3.zzN(zzks)) {
                        zzay().zzc().zzd("User property updated immediately", zzab2.zza, this.zzn.zzj().zze(zzks.zzc), zzks.zze);
                    } else {
                        zzay().zzd().zzd("(2)Too many active user properties, ignoring", zzei.zzn(zzab2.zza), this.zzn.zzj().zze(zzks.zzc), zzks.zze);
                    }
                    if (z && (zzat = zzab2.zzi) != null) {
                        zzW(new zzat(zzat, zzab2.zzd), zzp);
                    }
                }
                zzaj zzaj4 = this.zze;
                zzak(zzaj4);
                if (zzaj4.zzM(zzab2)) {
                    zzay().zzc().zzd("Conditional property added", zzab2.zza, this.zzn.zzj().zze(zzab2.zzc.zzb), zzab2.zzc.zza());
                } else {
                    zzay().zzd().zzd("Too many conditional properties, ignoring", zzei.zzn(zzab2.zza), this.zzn.zzj().zze(zzab2.zzc.zzb), zzab2.zzc.zza());
                }
                zzaj zzaj5 = this.zze;
                zzak(zzaj5);
                zzaj5.zzC();
            } finally {
                zzaj zzaj6 = this.zze;
                zzak(zzaj6);
                zzaj6.zzx();
            }
        }
    }

    public final void zzT(String str, zzag zzag) {
        zzaz().zzg();
        zzB();
        this.zzB.put(str, zzag);
        zzaj zzaj = this.zze;
        zzak(zzaj);
        Preconditions.checkNotNull(str);
        Preconditions.checkNotNull(zzag);
        zzaj.zzg();
        zzaj.zzY();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", str);
        contentValues.put("consent_state", zzag.zzi());
        try {
            if (zzaj.zzh().insertWithOnConflict("consent_settings", null, contentValues, 5) == -1) {
                zzaj.zzs.zzay().zzd().zzb("Failed to insert/update consent setting (got -1). appId", zzei.zzn(str));
            }
        } catch (SQLiteException e) {
            zzaj.zzs.zzay().zzd().zzc("Error storing consent setting. appId, error", zzei.zzn(str), e);
        }
    }

    public final void zzU(zzkq zzkq, zzp zzp) {
        long j;
        int i;
        int i2;
        zzaz().zzg();
        zzB();
        if (zzag(zzp)) {
            if (!zzp.zzh) {
                zzd(zzp);
                return;
            }
            int zzl = zzv().zzl(zzkq.zzb);
            if (zzl != 0) {
                zzku zzv = zzv();
                String str = zzkq.zzb;
                zzg();
                String zzC = zzv.zzC(str, 24, true);
                String str2 = zzkq.zzb;
                if (str2 != null) {
                    i2 = str2.length();
                } else {
                    i2 = 0;
                }
                zzv().zzM(this.zzC, zzp.zza, zzl, "_ev", zzC, i2);
                return;
            }
            int zzd = zzv().zzd(zzkq.zzb, zzkq.zza());
            if (zzd != 0) {
                zzku zzv2 = zzv();
                String str3 = zzkq.zzb;
                zzg();
                String zzC2 = zzv2.zzC(str3, 24, true);
                Object zza = zzkq.zza();
                if (zza == null || (!(zza instanceof String) && !(zza instanceof CharSequence))) {
                    i = 0;
                } else {
                    i = String.valueOf(zza).length();
                }
                zzv().zzM(this.zzC, zzp.zza, zzd, "_ev", zzC2, i);
                return;
            }
            Object zzB = zzv().zzB(zzkq.zzb, zzkq.zza());
            if (zzB != null) {
                if ("_sid".equals(zzkq.zzb)) {
                    long j2 = zzkq.zzc;
                    String str4 = zzkq.zzf;
                    String str5 = (String) Preconditions.checkNotNull(zzp.zza);
                    zzaj zzaj = this.zze;
                    zzak(zzaj);
                    zzks zzp2 = zzaj.zzp(str5, "_sno");
                    if (zzp2 != null) {
                        Object obj = zzp2.zze;
                        if (obj instanceof Long) {
                            j = ((Long) obj).longValue();
                            zzU(new zzkq("_sno", j2, Long.valueOf(j + 1), str4), zzp);
                        }
                    }
                    if (zzp2 != null) {
                        zzay().zzk().zzb("Retrieved last session number from database does not contain a valid (long) value", zzp2.zze);
                    }
                    zzaj zzaj2 = this.zze;
                    zzak(zzaj2);
                    zzap zzn = zzaj2.zzn(str5, "_s");
                    if (zzn != null) {
                        j = zzn.zzc;
                        zzay().zzj().zzb("Backfill the session number. Last used session number", Long.valueOf(j));
                    } else {
                        j = 0;
                    }
                    zzU(new zzkq("_sno", j2, Long.valueOf(j + 1), str4), zzp);
                }
                zzks zzks = new zzks((String) Preconditions.checkNotNull(zzp.zza), (String) Preconditions.checkNotNull(zzkq.zzf), zzkq.zzb, zzkq.zzc, zzB);
                zzay().zzj().zzc("Setting user property", this.zzn.zzj().zze(zzks.zzc), zzB);
                zzaj zzaj3 = this.zze;
                zzak(zzaj3);
                zzaj3.zzw();
                try {
                    zzna.zzc();
                    if (this.zzn.zzf().zzs(null, zzdw.zzay) && DBHelper.Key_ID.equals(zzks.zzc)) {
                        zzaj zzaj4 = this.zze;
                        zzak(zzaj4);
                        zzaj4.zzA(zzp.zza, "_lair");
                    }
                    zzd(zzp);
                    zzaj zzaj5 = this.zze;
                    zzak(zzaj5);
                    boolean zzN = zzaj5.zzN(zzks);
                    zzaj zzaj6 = this.zze;
                    zzak(zzaj6);
                    zzaj6.zzC();
                    if (!zzN) {
                        zzay().zzd().zzc("Too many unique user properties are set. Ignoring user property", this.zzn.zzj().zze(zzks.zzc), zzks.zze);
                        zzv().zzM(this.zzC, zzp.zza, 9, null, null, 0);
                    }
                } finally {
                    zzaj zzaj7 = this.zze;
                    zzak(zzaj7);
                    zzaj7.zzx();
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:206:0x0505, code lost:
        if (r9 != null) goto L_0x0507;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0108, code lost:
        if (r11 != null) goto L_0x010a;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x010a, code lost:
        r11.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0114, code lost:
        if (r11 != null) goto L_0x010a;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x012f, code lost:
        if (r11 == null) goto L_0x0133;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0133, code lost:
        r22.zzA = r7;
     */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0279 A[Catch: all -> 0x052c, TRY_ENTER, TRY_LEAVE, TryCatch #10 {all -> 0x052c, blocks: (B:3:0x0010, B:5:0x0021, B:9:0x0034, B:11:0x003a, B:13:0x004a, B:15:0x0052, B:17:0x0058, B:19:0x0063, B:21:0x0073, B:23:0x007e, B:25:0x0091, B:27:0x00b0, B:29:0x00b6, B:30:0x00b9, B:32:0x00c5, B:33:0x00dc, B:35:0x00ed, B:37:0x00f3, B:42:0x010a, B:53:0x0133, B:56:0x0139, B:57:0x013c, B:58:0x013d, B:62:0x0165, B:66:0x016d, B:72:0x01a3, B:112:0x0279, B:126:0x02ad, B:127:0x02b0, B:129:0x02b6, B:131:0x02c0, B:132:0x02c4, B:134:0x02ca, B:136:0x02de, B:140:0x02e7, B:142:0x02ed, B:145:0x0302, B:147:0x030c, B:148:0x0312, B:149:0x0315, B:151:0x0330, B:155:0x033d, B:157:0x0350, B:159:0x038a, B:161:0x038f, B:163:0x0397, B:164:0x039a, B:166:0x03a6, B:167:0x03bc, B:168:0x03c4, B:170:0x03d5, B:172:0x03e6, B:173:0x0402, B:175:0x0414, B:176:0x0422, B:177:0x0429, B:179:0x0434, B:180:0x043d, B:182:0x0481, B:186:0x0499, B:187:0x049c, B:188:0x049d, B:198:0x04ea, B:207:0x0507, B:208:0x050c, B:210:0x0512, B:212:0x051d, B:216:0x0528, B:217:0x052b), top: B:232:0x0010, inners: #5 }] */
    /* JADX WARN: Removed duplicated region for block: B:129:0x02b6 A[Catch: all -> 0x052c, TryCatch #10 {all -> 0x052c, blocks: (B:3:0x0010, B:5:0x0021, B:9:0x0034, B:11:0x003a, B:13:0x004a, B:15:0x0052, B:17:0x0058, B:19:0x0063, B:21:0x0073, B:23:0x007e, B:25:0x0091, B:27:0x00b0, B:29:0x00b6, B:30:0x00b9, B:32:0x00c5, B:33:0x00dc, B:35:0x00ed, B:37:0x00f3, B:42:0x010a, B:53:0x0133, B:56:0x0139, B:57:0x013c, B:58:0x013d, B:62:0x0165, B:66:0x016d, B:72:0x01a3, B:112:0x0279, B:126:0x02ad, B:127:0x02b0, B:129:0x02b6, B:131:0x02c0, B:132:0x02c4, B:134:0x02ca, B:136:0x02de, B:140:0x02e7, B:142:0x02ed, B:145:0x0302, B:147:0x030c, B:148:0x0312, B:149:0x0315, B:151:0x0330, B:155:0x033d, B:157:0x0350, B:159:0x038a, B:161:0x038f, B:163:0x0397, B:164:0x039a, B:166:0x03a6, B:167:0x03bc, B:168:0x03c4, B:170:0x03d5, B:172:0x03e6, B:173:0x0402, B:175:0x0414, B:176:0x0422, B:177:0x0429, B:179:0x0434, B:180:0x043d, B:182:0x0481, B:186:0x0499, B:187:0x049c, B:188:0x049d, B:198:0x04ea, B:207:0x0507, B:208:0x050c, B:210:0x0512, B:212:0x051d, B:216:0x0528, B:217:0x052b), top: B:232:0x0010, inners: #5 }] */
    /* JADX WARN: Removed duplicated region for block: B:186:0x0499 A[Catch: all -> 0x052c, TryCatch #10 {all -> 0x052c, blocks: (B:3:0x0010, B:5:0x0021, B:9:0x0034, B:11:0x003a, B:13:0x004a, B:15:0x0052, B:17:0x0058, B:19:0x0063, B:21:0x0073, B:23:0x007e, B:25:0x0091, B:27:0x00b0, B:29:0x00b6, B:30:0x00b9, B:32:0x00c5, B:33:0x00dc, B:35:0x00ed, B:37:0x00f3, B:42:0x010a, B:53:0x0133, B:56:0x0139, B:57:0x013c, B:58:0x013d, B:62:0x0165, B:66:0x016d, B:72:0x01a3, B:112:0x0279, B:126:0x02ad, B:127:0x02b0, B:129:0x02b6, B:131:0x02c0, B:132:0x02c4, B:134:0x02ca, B:136:0x02de, B:140:0x02e7, B:142:0x02ed, B:145:0x0302, B:147:0x030c, B:148:0x0312, B:149:0x0315, B:151:0x0330, B:155:0x033d, B:157:0x0350, B:159:0x038a, B:161:0x038f, B:163:0x0397, B:164:0x039a, B:166:0x03a6, B:167:0x03bc, B:168:0x03c4, B:170:0x03d5, B:172:0x03e6, B:173:0x0402, B:175:0x0414, B:176:0x0422, B:177:0x0429, B:179:0x0434, B:180:0x043d, B:182:0x0481, B:186:0x0499, B:187:0x049c, B:188:0x049d, B:198:0x04ea, B:207:0x0507, B:208:0x050c, B:210:0x0512, B:212:0x051d, B:216:0x0528, B:217:0x052b), top: B:232:0x0010, inners: #5 }] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0139 A[Catch: all -> 0x052c, TryCatch #10 {all -> 0x052c, blocks: (B:3:0x0010, B:5:0x0021, B:9:0x0034, B:11:0x003a, B:13:0x004a, B:15:0x0052, B:17:0x0058, B:19:0x0063, B:21:0x0073, B:23:0x007e, B:25:0x0091, B:27:0x00b0, B:29:0x00b6, B:30:0x00b9, B:32:0x00c5, B:33:0x00dc, B:35:0x00ed, B:37:0x00f3, B:42:0x010a, B:53:0x0133, B:56:0x0139, B:57:0x013c, B:58:0x013d, B:62:0x0165, B:66:0x016d, B:72:0x01a3, B:112:0x0279, B:126:0x02ad, B:127:0x02b0, B:129:0x02b6, B:131:0x02c0, B:132:0x02c4, B:134:0x02ca, B:136:0x02de, B:140:0x02e7, B:142:0x02ed, B:145:0x0302, B:147:0x030c, B:148:0x0312, B:149:0x0315, B:151:0x0330, B:155:0x033d, B:157:0x0350, B:159:0x038a, B:161:0x038f, B:163:0x0397, B:164:0x039a, B:166:0x03a6, B:167:0x03bc, B:168:0x03c4, B:170:0x03d5, B:172:0x03e6, B:173:0x0402, B:175:0x0414, B:176:0x0422, B:177:0x0429, B:179:0x0434, B:180:0x043d, B:182:0x0481, B:186:0x0499, B:187:0x049c, B:188:0x049d, B:198:0x04ea, B:207:0x0507, B:208:0x050c, B:210:0x0512, B:212:0x051d, B:216:0x0528, B:217:0x052b), top: B:232:0x0010, inners: #5 }] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final void zzV() {
        Throwable th;
        String str;
        zzaj zzaj;
        long zzz;
        zzeg zzeg;
        SQLiteException e;
        long j;
        Cursor cursor;
        SQLiteException e2;
        Cursor cursor2;
        List emptyList;
        String str2;
        String str3;
        IOException e3;
        byte[] blob;
        zzkp zzkp;
        IOException e4;
        Cursor cursor3;
        SQLiteException e5;
        zzaz().zzg();
        zzB();
        int i = 1;
        this.zzv = true;
        int i2 = 0;
        try {
            this.zzn.zzaw();
            Boolean zzj = this.zzn.zzt().zzj();
            if (zzj == null) {
                zzay().zzk().zza("Upload data called on the client side before use of service was decided");
                this.zzv = false;
            } else if (zzj.booleanValue()) {
                zzay().zzd().zza("Upload called in the client side when service should be used");
                this.zzv = false;
            } else if (this.zza > 0) {
                zzaf();
                this.zzv = false;
            } else {
                zzaz().zzg();
                if (this.zzy != null) {
                    zzay().zzj().zza("Uploading requested multiple times");
                    this.zzv = false;
                } else {
                    zzeo zzeo = this.zzd;
                    zzak(zzeo);
                    if (!zzeo.zzc()) {
                        zzay().zzj().zza("Network not connected, ignoring upload request");
                        zzaf();
                        this.zzv = false;
                    } else {
                        long currentTimeMillis = zzav().currentTimeMillis();
                        Cursor cursor4 = null;
                        int zze = zzg().zze(null, zzdw.zzP);
                        zzg();
                        long zzz2 = currentTimeMillis - zzaf.zzz();
                        for (int i3 = 0; i3 < zze && zzah(null, zzz2); i3++) {
                        }
                        long zza = this.zzk.zzc.zza();
                        if (zza != 0) {
                            zzay().zzc().zzb("Uploading events. Elapsed time since last upload attempt (ms)", Long.valueOf(Math.abs(currentTimeMillis - zza)));
                        }
                        zzaj zzaj2 = this.zze;
                        zzak(zzaj2);
                        String zzr = zzaj2.zzr();
                        long j2 = -1;
                        if (!TextUtils.isEmpty(zzr)) {
                            if (this.zzA == -1) {
                                zzaj zzaj3 = this.zze;
                                zzak(zzaj3);
                                try {
                                    cursor3 = zzaj3.zzh().rawQuery("select rowid from raw_events order by rowid desc limit 1;", null);
                                    try {
                                        if (cursor3.moveToFirst()) {
                                            j2 = cursor3.getLong(0);
                                        }
                                    } catch (SQLiteException e6) {
                                        e5 = e6;
                                        try {
                                            zzaj3.zzs.zzay().zzd().zzb("Error querying raw events", e5);
                                        } catch (Throwable th2) {
                                            th = th2;
                                            if (cursor3 != null) {
                                                cursor3.close();
                                            }
                                            throw th;
                                        }
                                    } catch (Throwable th3) {
                                        th = th3;
                                        if (cursor3 != null) {
                                        }
                                        throw th;
                                    }
                                } catch (SQLiteException e7) {
                                    e5 = e7;
                                    cursor3 = null;
                                } catch (Throwable th4) {
                                    th = th4;
                                    cursor3 = null;
                                }
                            }
                            int zze2 = zzg().zze(zzr, zzdw.zzf);
                            int max = Math.max(0, zzg().zze(zzr, zzdw.zzg));
                            zzaj zzaj4 = this.zze;
                            zzak(zzaj4);
                            zzaj4.zzg();
                            zzaj4.zzY();
                            Preconditions.checkArgument(zze2 > 0);
                            Preconditions.checkArgument(max > 0);
                            Preconditions.checkNotEmpty(zzr);
                            try {
                                Cursor query = zzaj4.zzh().query("queue", new String[]{"rowid", UriUtil.DATA_SCHEME, "retry_count"}, "app_id=?", new String[]{zzr}, null, null, "rowid", String.valueOf(zze2));
                                try {
                                    try {
                                        if (!query.moveToFirst()) {
                                            emptyList = Collections.emptyList();
                                            if (query != null) {
                                                query.close();
                                            }
                                            j = currentTimeMillis;
                                        } else {
                                            ArrayList arrayList = new ArrayList();
                                            int i4 = 0;
                                            while (true) {
                                                long j3 = query.getLong(i2);
                                                try {
                                                    blob = query.getBlob(i);
                                                    zzkp = zzaj4.zzf.zzi;
                                                    zzak(zzkp);
                                                } catch (IOException e8) {
                                                    e3 = e8;
                                                    j = currentTimeMillis;
                                                }
                                                try {
                                                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(blob);
                                                    GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
                                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                    byte[] bArr = new byte[1024];
                                                    while (true) {
                                                        j = currentTimeMillis;
                                                        try {
                                                            try {
                                                                int read = gZIPInputStream.read(bArr);
                                                                if (read <= 0) {
                                                                    break;
                                                                }
                                                                byteArrayOutputStream.write(bArr, 0, read);
                                                                currentTimeMillis = j;
                                                            } catch (IOException e9) {
                                                                e4 = e9;
                                                                try {
                                                                    zzkp.zzs.zzay().zzd().zzb("Failed to ungzip content", e4);
                                                                    throw e4;
                                                                    break;
                                                                } catch (IOException e10) {
                                                                    e3 = e10;
                                                                    zzaj4.zzs.zzay().zzd().zzc("Failed to unzip queued bundle. appId", zzei.zzn(zzr), e3);
                                                                    if (query.moveToNext()) {
                                                                        break;
                                                                    }
                                                                    currentTimeMillis = j;
                                                                    i = 1;
                                                                    i2 = 0;
                                                                    if (query != null) {
                                                                    }
                                                                    emptyList = arrayList;
                                                                    if (!emptyList.isEmpty()) {
                                                                    }
                                                                    this.zzv = false;
                                                                    zzac();
                                                                }
                                                            }
                                                        } catch (SQLiteException e11) {
                                                            e2 = e11;
                                                            cursor = query;
                                                            try {
                                                                zzaj4.zzs.zzay().zzd().zzc("Error querying bundles. appId", zzei.zzn(zzr), e2);
                                                                emptyList = Collections.emptyList();
                                                                if (cursor != null) {
                                                                    cursor.close();
                                                                }
                                                                if (!emptyList.isEmpty()) {
                                                                }
                                                                this.zzv = false;
                                                                zzac();
                                                            } catch (Throwable th5) {
                                                                th = th5;
                                                                cursor2 = cursor;
                                                                if (cursor2 != null) {
                                                                }
                                                                throw th;
                                                            }
                                                        }
                                                    }
                                                    gZIPInputStream.close();
                                                    byteArrayInputStream.close();
                                                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                                                    if (!arrayList.isEmpty() && byteArray.length + i4 > max) {
                                                        break;
                                                    }
                                                    try {
                                                        zzfx zzfx = (zzfx) zzkp.zzl(zzfy.zzu(), byteArray);
                                                        if (!query.isNull(2)) {
                                                            zzfx.zzac(query.getInt(2));
                                                        }
                                                        i4 += byteArray.length;
                                                        arrayList.add(Pair.create(zzfx.zzaA(), Long.valueOf(j3)));
                                                    } catch (IOException e12) {
                                                        zzaj4.zzs.zzay().zzd().zzc("Failed to merge queued bundle. appId", zzei.zzn(zzr), e12);
                                                    }
                                                    if (query.moveToNext() || i4 > max) {
                                                        break;
                                                        break;
                                                    }
                                                    currentTimeMillis = j;
                                                    i = 1;
                                                    i2 = 0;
                                                } catch (IOException e13) {
                                                    e4 = e13;
                                                    j = currentTimeMillis;
                                                }
                                            }
                                            if (query != null) {
                                                query.close();
                                            }
                                            emptyList = arrayList;
                                        }
                                    } catch (Throwable th6) {
                                        th = th6;
                                        cursor2 = query;
                                        if (cursor2 != null) {
                                            cursor2.close();
                                        }
                                        throw th;
                                    }
                                } catch (SQLiteException e14) {
                                    e2 = e14;
                                    j = currentTimeMillis;
                                }
                            } catch (SQLiteException e15) {
                                e2 = e15;
                                j = currentTimeMillis;
                                cursor = null;
                            } catch (Throwable th7) {
                                th = th7;
                                cursor2 = null;
                            }
                            if (!emptyList.isEmpty()) {
                                if (zzh(zzr).zzj()) {
                                    Iterator it = emptyList.iterator();
                                    while (true) {
                                        if (!it.hasNext()) {
                                            str3 = null;
                                            break;
                                        }
                                        zzfy zzfy = (zzfy) ((Pair) it.next()).first;
                                        if (!TextUtils.isEmpty(zzfy.zzL())) {
                                            str3 = zzfy.zzL();
                                            break;
                                        }
                                    }
                                    if (str3 != null) {
                                        int i5 = 0;
                                        while (true) {
                                            if (i5 >= emptyList.size()) {
                                                break;
                                            }
                                            zzfy zzfy2 = (zzfy) ((Pair) emptyList.get(i5)).first;
                                            if (!TextUtils.isEmpty(zzfy2.zzL()) && !zzfy2.zzL().equals(str3)) {
                                                emptyList = emptyList.subList(0, i5);
                                                break;
                                            }
                                            i5++;
                                        }
                                    }
                                }
                                zzfv zza2 = zzfw.zza();
                                int size = emptyList.size();
                                ArrayList arrayList2 = new ArrayList(emptyList.size());
                                boolean z = zzg().zzt(zzr) && zzh(zzr).zzj();
                                boolean zzj2 = zzh(zzr).zzj();
                                boolean zzk = zzh(zzr).zzk();
                                int i6 = 0;
                                while (i6 < size) {
                                    zzfx zzbv = ((zzfy) ((Pair) emptyList.get(i6)).first).zzbv();
                                    arrayList2.add((Long) ((Pair) emptyList.get(i6)).second);
                                    zzg().zzh();
                                    zzbv.zzah(42097);
                                    zzbv.zzag(j);
                                    this.zzn.zzaw();
                                    zzbv.zzad(false);
                                    if (!z) {
                                        zzbv.zzo();
                                    }
                                    if (!zzj2) {
                                        zzbv.zzu();
                                        zzbv.zzr();
                                    }
                                    if (!zzk) {
                                        zzbv.zzm();
                                    }
                                    if (zzg().zzs(zzr, zzdw.zzV)) {
                                        byte[] zzbs = zzbv.zzaA().zzbs();
                                        zzkp zzkp2 = this.zzi;
                                        zzak(zzkp2);
                                        zzbv.zzG(zzkp2.zzd(zzbs));
                                    }
                                    zza2.zza(zzbv);
                                    i6++;
                                    j = j;
                                }
                                if (Log.isLoggable(zzay().zzq(), 2)) {
                                    zzkp zzkp3 = this.zzi;
                                    zzak(zzkp3);
                                    str2 = zzkp3.zzm(zza2.zzaA());
                                } else {
                                    str2 = null;
                                }
                                zzak(this.zzi);
                                byte[] zzbs2 = zza2.zzaA().zzbs();
                                zzg();
                                String zza3 = zzdw.zzp.zza(null);
                                try {
                                    URL url = new URL(zza3);
                                    Preconditions.checkArgument(!arrayList2.isEmpty());
                                    if (this.zzy != null) {
                                        zzay().zzd().zza("Set uploading progress before finishing the previous upload");
                                    } else {
                                        this.zzy = new ArrayList(arrayList2);
                                    }
                                    this.zzk.zzd.zzb(j);
                                    String str4 = "?";
                                    if (size > 0) {
                                        str4 = zza2.zzb(0).zzy();
                                    }
                                    zzay().zzj().zzd("Uploading data. app, uncompressed size, data", str4, Integer.valueOf(zzbs2.length), str2);
                                    this.zzu = true;
                                    zzeo zzeo2 = this.zzd;
                                    zzak(zzeo2);
                                    zzkg zzkg = new zzkg(this, zzr);
                                    zzeo2.zzg();
                                    zzeo2.zzY();
                                    Preconditions.checkNotNull(url);
                                    Preconditions.checkNotNull(zzbs2);
                                    Preconditions.checkNotNull(zzkg);
                                    zzeo2.zzs.zzaz().zzo(new zzen(zzeo2, zzr, url, zzbs2, null, zzkg));
                                } catch (MalformedURLException e16) {
                                    zzay().zzd().zzc("Failed to parse upload URL. Not uploading. appId", zzei.zzn(zzr), zza3);
                                }
                            }
                        } else {
                            try {
                                str = null;
                                this.zzA = -1;
                                zzaj = this.zze;
                                zzak(zzaj);
                                zzg();
                                zzz = currentTimeMillis - zzaf.zzz();
                                zzaj.zzg();
                                zzaj.zzY();
                            } catch (Throwable th8) {
                                th = th8;
                            }
                            try {
                                cursor4 = zzaj.zzh().rawQuery("select app_id from apps where app_id in (select distinct app_id from raw_events) and config_fetched_time < ? order by failed_config_fetch_time limit 1;", new String[]{String.valueOf(zzz)});
                                try {
                                } catch (SQLiteException e17) {
                                    e = e17;
                                    zzeg zzd = zzaj.zzs.zzay().zzd();
                                    zzd.zzb("Error selecting expired configs", e);
                                    zzeg = zzd;
                                    zzaj = zzd;
                                }
                            } catch (SQLiteException e18) {
                                e = e18;
                                cursor4 = null;
                                zzeg zzd2 = zzaj.zzs.zzay().zzd();
                                zzd2.zzb("Error selecting expired configs", e);
                                zzeg = zzd2;
                                zzaj = zzd2;
                            } catch (Throwable th9) {
                                th = th9;
                                cursor4 = null;
                                if (cursor4 != null) {
                                    cursor4.close();
                                }
                                throw th;
                            }
                            if (!cursor4.moveToFirst()) {
                                zzaj.zzs.zzay().zzj().zza("No expired configs for apps with pending events");
                                if (cursor4 != null) {
                                    zzeg = zzaj;
                                    cursor4.close();
                                    zzaj = zzeg;
                                } else {
                                    zzaj = zzaj;
                                }
                            } else {
                                String string = cursor4.getString(0);
                                if (cursor4 != null) {
                                    cursor4.close();
                                }
                                str = string;
                                zzaj = zzaj;
                            }
                            if (!TextUtils.isEmpty(str)) {
                                zzaj zzaj5 = this.zze;
                                zzak(zzaj5);
                                zzg zzj3 = zzaj5.zzj(str);
                                if (zzj3 != null) {
                                    zzC(zzj3);
                                }
                            }
                        }
                        this.zzv = false;
                    }
                }
            }
            zzac();
        } catch (Throwable th10) {
            this.zzv = false;
            zzac();
            throw th10;
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(11:81|(1:83)(1:85)|84|86|(2:88|(1:90)(3:91|99|(1:101)))(1:92)|93|315|94|98|99|(0)) */
    /* JADX WARN: Can't wrap try/catch for region: R(51:(2:123|(4:125|(1:127)|128|129))|130|(2:132|(4:134|(1:136)|137|138))|139|140|(1:142)|143|(2:145|(1:149))|150|308|151|152|(3:313|153|154)|160|(1:162)|163|(2:165|(1:170)(2:168|169))(1:171)|172|(1:174)|175|(1:177)|178|(1:180)|181|(1:183)|184|(1:186)|187|(4:189|(1:193)|194|(1:200))(2:201|(1:205))|206|(1:208)|209|(12:(30:211|(1:213)(4:214|(4:217|(3:319|219|(3:329|221|(3:330|223|331)(1:335))(1:334))(1:333)|332|215)|328|227)|(1:231)|232|(2:234|(2:238|(1:240)))|241|(1:243)|244|(2:246|(1:248))|249|(5:251|(1:253)|254|(1:256)|257)|258|(1:262)|263|(1:265)|266|(3:269|270|267)|310|271|317|272|273|(2:274|(2:276|(2:337|278)(1:279))(3:338|280|(1:284)(0)))|285|311|286|(1:288)(2:289|290)|300|301|302)|317|272|273|(3:274|(0)(0)|279)|285|311|286|(0)(0)|300|301|302)|229|(0)|232|(0)|241|(0)|244|(0)|249|(0)|258|(2:260|262)|263|(0)|266|(1:267)|310|271) */
    /* JADX WARN: Code restructure failed: missing block: B:228:0x07b1, code lost:
        if (r13.size() == 0) goto L_0x07b3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x0ab5, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x0ab7, code lost:
        zzay().zzd().zzc("Data loss. Failed to insert raw event metadata. appId", com.google.android.gms.measurement.internal.zzei.zzn(r2.zzal()), r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x02f5, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x02f7, code lost:
        r9.zzs.zzay().zzd().zzc("Error pruning currencies. appId", com.google.android.gms.measurement.internal.zzei.zzn(r8), r0);
     */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0331 A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0382  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0385 A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:117:0x03e0 A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0408  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x0552 A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:165:0x0592 A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:171:0x0604 A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:174:0x064f A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:177:0x065c A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0669 A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:183:0x0677 A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:186:0x0688 A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:189:0x06bb A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:201:0x06f4 A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:208:0x0713 A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:211:0x072e A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:231:0x07b6 A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:234:0x07d5 A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:243:0x0844 A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:246:0x0851 A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:251:0x086a A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:260:0x08e3 A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:265:0x0904 A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:269:0x0924 A[Catch: all -> 0x0aff, TRY_LEAVE, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:276:0x09b6 A[Catch: all -> 0x0aff, TryCatch #7 {all -> 0x0aff, blocks: (B:38:0x0168, B:40:0x017a, B:42:0x0186, B:43:0x0192, B:46:0x019e, B:48:0x01a8, B:53:0x01b5, B:58:0x01cb, B:61:0x01d8, B:63:0x01ef, B:68:0x020a, B:72:0x021a, B:76:0x0240, B:77:0x024a, B:79:0x0250, B:81:0x025e, B:83:0x026a, B:85:0x0270, B:86:0x0276, B:88:0x0281, B:91:0x028e, B:93:0x02c2, B:94:0x02dc, B:97:0x02f7, B:98:0x030a, B:99:0x0326, B:101:0x0331, B:104:0x036d, B:107:0x0385, B:108:0x038c, B:110:0x0392, B:112:0x039e, B:115:0x03a7, B:117:0x03e0, B:119:0x03e5, B:120:0x03fc, B:123:0x040a, B:125:0x0421, B:127:0x0426, B:128:0x043d, B:132:0x045f, B:136:0x0480, B:137:0x0497, B:139:0x04a3, B:142:0x04c0, B:143:0x04d4, B:145:0x04de, B:147:0x04eb, B:149:0x04f1, B:150:0x04fa, B:151:0x0508, B:153:0x051d, B:159:0x053a, B:162:0x0552, B:163:0x0567, B:165:0x0592, B:168:0x05aa, B:170:0x05e6, B:171:0x0604, B:172:0x0612, B:174:0x064f, B:175:0x0654, B:177:0x065c, B:178:0x0661, B:180:0x0669, B:181:0x066e, B:183:0x0677, B:184:0x067b, B:186:0x0688, B:187:0x068d, B:189:0x06bb, B:191:0x06c5, B:193:0x06cd, B:194:0x06d2, B:196:0x06dc, B:198:0x06e6, B:200:0x06ee, B:201:0x06f4, B:203:0x06fe, B:205:0x0706, B:206:0x070b, B:208:0x0713, B:209:0x0716, B:211:0x072e, B:214:0x0737, B:215:0x0750, B:217:0x0756, B:219:0x076a, B:221:0x0776, B:223:0x0783, B:226:0x079d, B:227:0x07ad, B:231:0x07b6, B:232:0x07b9, B:234:0x07d5, B:236:0x07e7, B:238:0x07eb, B:240:0x07f6, B:241:0x0801, B:243:0x0844, B:244:0x0849, B:246:0x0851, B:248:0x085a, B:249:0x085d, B:251:0x086a, B:253:0x088a, B:254:0x0895, B:256:0x08cb, B:257:0x08d0, B:258:0x08dd, B:260:0x08e3, B:262:0x08ed, B:263:0x08fa, B:265:0x0904, B:266:0x0911, B:267:0x091e, B:269:0x0924, B:271:0x0954, B:272:0x099a, B:273:0x09a4, B:274:0x09b0, B:276:0x09b6, B:280:0x09c8, B:282:0x09ef, B:285:0x0a06, B:286:0x0a58, B:288:0x0a68, B:290:0x0a80, B:292:0x0a84, B:295:0x0a9d, B:296:0x0ab4, B:299:0x0ab7, B:300:0x0acc), top: B:321:0x0168, inners: #1, #2, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:288:0x0a68 A[Catch: SQLiteException -> 0x0a83, all -> 0x0aff, TRY_LEAVE, TryCatch #2 {SQLiteException -> 0x0a83, blocks: (B:286:0x0a58, B:288:0x0a68), top: B:311:0x0a58, outer: #7 }] */
    /* JADX WARN: Removed duplicated region for block: B:289:0x0a7e  */
    /* JADX WARN: Removed duplicated region for block: B:338:0x09c8 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    final void zzW(zzat zzat, zzp zzp) {
        boolean z;
        String str;
        long j;
        zzar zzar;
        long j2;
        long intValue;
        int i;
        long j3;
        zzao zzao;
        int i2;
        String str2;
        zzap zzn;
        zzap zzap;
        long j4;
        long j5;
        Map<String, String> zzc;
        ArrayList arrayList;
        zzag zzc2;
        zzg zzj;
        List<zzks> zzu;
        int i3;
        zzaj zzaj;
        zzfy zzfy;
        zzaj zzaj2;
        zzaq zzaq;
        ContentValues contentValues;
        zzaj zzaj3;
        SQLiteException e;
        long j6;
        String str3;
        zzks zzks;
        zzaj zzaj4;
        char c;
        char c2;
        String str4;
        String str5;
        Preconditions.checkNotNull(zzp);
        Preconditions.checkNotEmpty(zzp.zza);
        long nanoTime = System.nanoTime();
        zzaz().zzg();
        zzB();
        String str6 = zzp.zza;
        zzak(this.zzi);
        if (zzkp.zzB(zzat, zzp)) {
            if (zzp.zzh) {
                zzfj zzfj = this.zzc;
                zzak(zzfj);
                if (zzfj.zzo(str6, zzat.zza)) {
                    zzay().zzk().zzc("Dropping blocked event. appId", zzei.zzn(str6), this.zzn.zzj().zzc(zzat.zza));
                    zzfj zzfj2 = this.zzc;
                    zzak(zzfj2);
                    if (!zzfj2.zzm(str6)) {
                        zzfj zzfj3 = this.zzc;
                        zzak(zzfj3);
                        if (!zzfj3.zzp(str6)) {
                            if (!"_err".equals(zzat.zza)) {
                                zzv().zzM(this.zzC, str6, 11, "_ev", zzat.zza, 0);
                                return;
                            }
                            return;
                        }
                    }
                    zzaj zzaj5 = this.zze;
                    zzak(zzaj5);
                    zzg zzj2 = zzaj5.zzj(str6);
                    if (zzj2 != null) {
                        long abs = Math.abs(zzav().currentTimeMillis() - Math.max(zzj2.zzl(), zzj2.zzc()));
                        zzg();
                        if (abs > zzdw.zzy.zza(null).longValue()) {
                            zzay().zzc().zza("Fetching config for blocked app");
                            zzC(zzj2);
                            return;
                        }
                        return;
                    }
                    return;
                }
                zzej zzb2 = zzej.zzb(zzat);
                zzv().zzL(zzb2, zzg().zzd(str6));
                zzat zza = zzb2.zza();
                if (Log.isLoggable(zzay().zzq(), 2)) {
                    zzeg zzj3 = zzay().zzj();
                    zzed zzj4 = this.zzn.zzj();
                    if (!zzj4.zzh()) {
                        str4 = zza.toString();
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("origin=");
                        sb.append(zza.zzc);
                        sb.append(",name=");
                        sb.append(zzj4.zzc(zza.zza));
                        sb.append(",params=");
                        zzar zzar2 = zza.zzb;
                        if (zzar2 == null) {
                            str5 = null;
                        } else {
                            str5 = !zzj4.zzh() ? zzar2.toString() : zzj4.zzb(zzar2.zzc());
                        }
                        sb.append(str5);
                        str4 = sb.toString();
                    }
                    zzj3.zzb("Logging event", str4);
                }
                zzaj zzaj6 = this.zze;
                zzak(zzaj6);
                zzaj6.zzw();
                try {
                    zzd(zzp);
                    zzna.zzc();
                    if (!zzg().zzs(null, zzdw.zzay) && zzg().zzs(null, zzdw.zzaz)) {
                        zzaj zzaj7 = this.zze;
                        zzak(zzaj7);
                        zzaj7.zzA(zzp.zza, "_lair");
                    }
                    if (FirebaseAnalytics.Event.ECOMMERCE_PURCHASE.equals(zza.zza) || FirebaseAnalytics.Event.PURCHASE.equals(zza.zza)) {
                        z = true;
                    } else {
                        z = FirebaseAnalytics.Event.REFUND.equals(zza.zza);
                    }
                    if (!"_iap".equals(zza.zza)) {
                        if (z) {
                            z = true;
                        } else {
                            j = nanoTime;
                            str = "_err";
                            boolean zzah = zzku.zzah(zza.zza);
                            boolean equals = str.equals(zza.zza);
                            zzv();
                            zzar = zza.zzb;
                            if (zzar != null) {
                                j2 = 0;
                            } else {
                                zzaq zzaq2 = new zzaq(zzar);
                                long j7 = 0;
                                while (zzaq2.hasNext()) {
                                    Object zzf = zzar.zzf(zzaq2.next());
                                    if (zzf instanceof Parcelable[]) {
                                        j7 += (long) ((Parcelable[]) zzf).length;
                                    }
                                }
                                j2 = j7;
                            }
                            zzaj zzaj8 = this.zze;
                            zzak(zzaj8);
                            zzah zzm = zzaj8.zzm(zza(), str6, j2 + 1, true, zzah, false, equals, false);
                            long j8 = zzm.zzb;
                            zzg();
                            intValue = j8 - ((long) zzdw.zzj.zza(null).intValue());
                            if (intValue <= 0) {
                                if (intValue % 1000 == 1) {
                                    zzay().zzd().zzc("Data loss. Too many events logged. appId, count", zzei.zzn(str6), Long.valueOf(zzm.zzb));
                                }
                                zzaj zzaj9 = this.zze;
                                zzak(zzaj9);
                                zzaj9.zzC();
                                zzaj3 = this.zze;
                            } else {
                                if (zzah) {
                                    long j9 = zzm.zza;
                                    zzg();
                                    long intValue2 = j9 - ((long) zzdw.zzl.zza(null).intValue());
                                    if (intValue2 > 0) {
                                        if (intValue2 % 1000 == 1) {
                                            zzay().zzd().zzc("Data loss. Too many public events logged. appId, count", zzei.zzn(str6), Long.valueOf(zzm.zza));
                                        }
                                        zzv().zzM(this.zzC, str6, 16, "_ev", zza.zza, 0);
                                        zzaj zzaj10 = this.zze;
                                        zzak(zzaj10);
                                        zzaj10.zzC();
                                        zzaj3 = this.zze;
                                    }
                                }
                                if (equals) {
                                    long max = zzm.zzd - ((long) Math.max(0, Math.min(1000000, zzg().zze(zzp.zza, zzdw.zzk))));
                                    if (max > 0) {
                                        if (max == 1) {
                                            zzay().zzd().zzc("Too many error events logged. appId, count", zzei.zzn(str6), Long.valueOf(zzm.zzd));
                                        }
                                        zzaj zzaj11 = this.zze;
                                        zzak(zzaj11);
                                        zzaj11.zzC();
                                        zzaj3 = this.zze;
                                    }
                                }
                                Bundle zzc3 = zza.zzb.zzc();
                                zzv().zzN(zzc3, "_o", zza.zzc);
                                if (zzv().zzad(str6)) {
                                    zzv().zzN(zzc3, "_dbg", 1L);
                                    zzv().zzN(zzc3, "_r", 1L);
                                }
                                if ("_s".equals(zza.zza)) {
                                    zzaj zzaj12 = this.zze;
                                    zzak(zzaj12);
                                    zzks zzp2 = zzaj12.zzp(zzp.zza, "_sno");
                                    if (zzp2 != null && (zzp2.zze instanceof Long)) {
                                        zzv().zzN(zzc3, "_sno", zzp2.zze);
                                    }
                                }
                                zzaj zzaj13 = this.zze;
                                zzak(zzaj13);
                                Preconditions.checkNotEmpty(str6);
                                zzaj13.zzg();
                                zzaj13.zzY();
                                try {
                                    i = 0;
                                } catch (SQLiteException e2) {
                                    e = e2;
                                    i = 0;
                                }
                                try {
                                    j3 = (long) zzaj13.zzh().delete("raw_events", "rowid in (select rowid from raw_events where app_id=? order by rowid desc limit -1 offset ?)", new String[]{str6, String.valueOf(Math.max(0, Math.min(1000000, zzaj13.zzs.zzf().zze(str6, zzdw.zzo))))});
                                } catch (SQLiteException e3) {
                                    e = e3;
                                    zzaj13.zzs.zzay().zzd().zzc("Error deleting over the limit events. appId", zzei.zzn(str6), e);
                                    j3 = 0;
                                    if (j3 > 0) {
                                    }
                                    i2 = i;
                                    str2 = "_r";
                                    zzao = new zzao(this.zzn, zza.zzc, str6, zza.zza, zza.zzd, 0, zzc3);
                                    zzaj zzaj14 = this.zze;
                                    zzak(zzaj14);
                                    zzn = zzaj14.zzn(str6, zzao.zzb);
                                    if (zzn != null) {
                                    }
                                    zzaj zzaj15 = this.zze;
                                    zzak(zzaj15);
                                    zzaj15.zzE(zzap);
                                    zzaz().zzg();
                                    zzB();
                                    Preconditions.checkNotNull(zzao);
                                    Preconditions.checkNotNull(zzp);
                                    Preconditions.checkNotEmpty(zzao.zza);
                                    Preconditions.checkArgument(zzao.zza.equals(zzp.zza));
                                    zzfx zzu2 = zzfy.zzu();
                                    zzu2.zzaa(1);
                                    zzu2.zzW("android");
                                    if (!TextUtils.isEmpty(zzp.zza)) {
                                    }
                                    if (!TextUtils.isEmpty(zzp.zzd)) {
                                    }
                                    if (!TextUtils.isEmpty(zzp.zzc)) {
                                    }
                                    j4 = zzp.zzj;
                                    if (j4 != -2147483648L) {
                                    }
                                    zzu2.zzS(zzp.zze);
                                    if (!TextUtils.isEmpty(zzp.zzb)) {
                                    }
                                    zzu2.zzI(zzh((String) Preconditions.checkNotNull(zzp.zza)).zzc(zzag.zzb(zzp.zzv)).zzi());
                                    zzoq.zzc();
                                    if (!zzg().zzs(zzp.zza, zzdw.zzad)) {
                                    }
                                    j5 = zzp.zzf;
                                    if (j5 != 0) {
                                    }
                                    zzu2.zzM(zzp.zzs);
                                    zzkp zzkp = this.zzi;
                                    zzak(zzkp);
                                    zzc = zzdw.zzc(zzkp.zzf.zzn.zzau());
                                    if (zzc != null) {
                                    }
                                    arrayList = null;
                                    if (arrayList != null) {
                                    }
                                    zzc2 = zzh((String) Preconditions.checkNotNull(zzp.zza)).zzc(zzag.zzb(zzp.zzv));
                                    if (zzc2.zzj()) {
                                    }
                                    this.zzn.zzg().zzu();
                                    zzu2.zzK(Build.MODEL);
                                    this.zzn.zzg().zzu();
                                    zzu2.zzV(Build.VERSION.RELEASE);
                                    zzu2.zzaf((int) this.zzn.zzg().zzb());
                                    zzu2.zzaj(this.zzn.zzg().zzc());
                                    if (!zzg().zzs(null, zzdw.zzan)) {
                                    }
                                    if (this.zzn.zzJ()) {
                                    }
                                    zzaj zzaj16 = this.zze;
                                    zzak(zzaj16);
                                    zzj = zzaj16.zzj(zzp.zza);
                                    if (zzj == null) {
                                    }
                                    if (zzc2.zzk()) {
                                    }
                                    if (!TextUtils.isEmpty(zzj.zzx())) {
                                    }
                                    zzaj zzaj17 = this.zze;
                                    zzak(zzaj17);
                                    zzu = zzaj17.zzu(zzp.zza);
                                    while (i3 < zzu.size()) {
                                    }
                                    zzaj = this.zze;
                                    zzak(zzaj);
                                    zzfy = (zzfy) zzu2.zzaA();
                                    zzaj.zzg();
                                    zzaj.zzY();
                                    Preconditions.checkNotNull(zzfy);
                                    Preconditions.checkNotEmpty(zzfy.zzy());
                                    byte[] zzbs = zzfy.zzbs();
                                    zzkp zzkp2 = zzaj.zzf.zzi;
                                    zzak(zzkp2);
                                    long zzd = zzkp2.zzd(zzbs);
                                    ContentValues contentValues2 = new ContentValues();
                                    contentValues2.put("app_id", zzfy.zzy());
                                    contentValues2.put("metadata_fingerprint", Long.valueOf(zzd));
                                    contentValues2.put("metadata", zzbs);
                                    zzaj.zzh().insertWithOnConflict("raw_events_metadata", null, contentValues2, 4);
                                    zzaj2 = this.zze;
                                    zzak(zzaj2);
                                    zzaq = new zzaq(zzao.zzf);
                                    while (true) {
                                        if (!zzaq.hasNext()) {
                                        }
                                        str2 = str2;
                                    }
                                    zzaj2.zzg();
                                    zzaj2.zzY();
                                    Preconditions.checkNotNull(zzao);
                                    Preconditions.checkNotEmpty(zzao.zza);
                                    zzkp zzkp3 = zzaj2.zzf.zzi;
                                    zzak(zzkp3);
                                    byte[] zzbs2 = zzkp3.zzj(zzao).zzbs();
                                    contentValues = new ContentValues();
                                    contentValues.put("app_id", zzao.zza);
                                    contentValues.put(AppMeasurementSdk.ConditionalUserProperty.NAME, zzao.zzb);
                                    contentValues.put("timestamp", Long.valueOf(zzao.zzd));
                                    contentValues.put("metadata_fingerprint", Long.valueOf(zzd));
                                    contentValues.put(UriUtil.DATA_SCHEME, zzbs2);
                                    contentValues.put("realtime", Integer.valueOf(i2));
                                    try {
                                        if (zzaj2.zzh().insert("raw_events", null, contentValues) == -1) {
                                        }
                                    } catch (SQLiteException e4) {
                                        zzaj2.zzs.zzay().zzd().zzc("Error storing raw event. appId", zzei.zzn(zzao.zza), e4);
                                    }
                                    zzaj zzaj18 = this.zze;
                                    zzak(zzaj18);
                                    zzaj18.zzC();
                                    zzaj zzaj19 = this.zze;
                                    zzak(zzaj19);
                                    zzaj19.zzx();
                                    zzaf();
                                    zzay().zzj().zzb("Background event processing time, ms", Long.valueOf(((System.nanoTime() - j) + 500000) / 1000000));
                                    return;
                                }
                                if (j3 > 0) {
                                    zzay().zzk().zzc("Data lost. Too many events stored on disk, deleted. appId", zzei.zzn(str6), Long.valueOf(j3));
                                }
                                i2 = i;
                                str2 = "_r";
                                zzao = new zzao(this.zzn, zza.zzc, str6, zza.zza, zza.zzd, 0, zzc3);
                                zzaj zzaj142 = this.zze;
                                zzak(zzaj142);
                                zzn = zzaj142.zzn(str6, zzao.zzb);
                                if (zzn != null) {
                                    zzaj zzaj20 = this.zze;
                                    zzak(zzaj20);
                                    if (zzaj20.zzf(str6) < ((long) zzg().zzb(str6)) || !zzah) {
                                        zzap = new zzap(str6, zzao.zzb, 0, 0, 0, zzao.zzd, 0, null, null, null, null);
                                    } else {
                                        zzay().zzd().zzd("Too many event names used, ignoring event. appId, name, supported count", zzei.zzn(str6), this.zzn.zzj().zzc(zzao.zzb), Integer.valueOf(zzg().zzb(str6)));
                                        zzv().zzM(this.zzC, str6, 8, null, null, 0);
                                        zzaj3 = this.zze;
                                    }
                                } else {
                                    zzao = zzao.zza(this.zzn, zzn.zzf);
                                    zzap = zzn.zzc(zzao.zzd);
                                }
                                zzaj zzaj152 = this.zze;
                                zzak(zzaj152);
                                zzaj152.zzE(zzap);
                                zzaz().zzg();
                                zzB();
                                Preconditions.checkNotNull(zzao);
                                Preconditions.checkNotNull(zzp);
                                Preconditions.checkNotEmpty(zzao.zza);
                                Preconditions.checkArgument(zzao.zza.equals(zzp.zza));
                                zzfx zzu22 = zzfy.zzu();
                                zzu22.zzaa(1);
                                zzu22.zzW("android");
                                if (!TextUtils.isEmpty(zzp.zza)) {
                                    zzu22.zzA(zzp.zza);
                                }
                                if (!TextUtils.isEmpty(zzp.zzd)) {
                                    zzu22.zzC(zzp.zzd);
                                }
                                if (!TextUtils.isEmpty(zzp.zzc)) {
                                    zzu22.zzD(zzp.zzc);
                                }
                                j4 = zzp.zzj;
                                if (j4 != -2147483648L) {
                                    zzu22.zzE((int) j4);
                                }
                                zzu22.zzS(zzp.zze);
                                if (!TextUtils.isEmpty(zzp.zzb)) {
                                    zzu22.zzR(zzp.zzb);
                                }
                                zzu22.zzI(zzh((String) Preconditions.checkNotNull(zzp.zza)).zzc(zzag.zzb(zzp.zzv)).zzi());
                                zzoq.zzc();
                                if (!zzg().zzs(zzp.zza, zzdw.zzad)) {
                                    if (TextUtils.isEmpty(zzu22.zzan()) && !TextUtils.isEmpty(zzp.zzu)) {
                                        zzu22.zzQ(zzp.zzu);
                                    }
                                    if (TextUtils.isEmpty(zzu22.zzan()) && TextUtils.isEmpty(zzu22.zzam()) && !TextUtils.isEmpty(zzp.zzq)) {
                                        zzu22.zzy(zzp.zzq);
                                    }
                                } else if (TextUtils.isEmpty(zzu22.zzan()) && !TextUtils.isEmpty(zzp.zzq)) {
                                    zzu22.zzy(zzp.zzq);
                                }
                                j5 = zzp.zzf;
                                if (j5 != 0) {
                                    zzu22.zzJ(j5);
                                }
                                zzu22.zzM(zzp.zzs);
                                zzkp zzkp4 = this.zzi;
                                zzak(zzkp4);
                                zzc = zzdw.zzc(zzkp4.zzf.zzn.zzau());
                                try {
                                    if (zzc != null) {
                                        if (zzc.size() == 0) {
                                            arrayList = null;
                                        } else {
                                            arrayList = new ArrayList();
                                            int intValue3 = zzdw.zzO.zza(null).intValue();
                                            for (Map.Entry<String, String> entry : zzc.entrySet()) {
                                                if (entry.getKey().startsWith("measurement.id.")) {
                                                    try {
                                                        int parseInt = Integer.parseInt(entry.getValue());
                                                        if (parseInt != 0) {
                                                            arrayList.add(Integer.valueOf(parseInt));
                                                            if (arrayList.size() >= intValue3) {
                                                                zzkp4.zzs.zzay().zzk().zzb("Too many experiment IDs. Number of IDs", Integer.valueOf(arrayList.size()));
                                                                break;
                                                            }
                                                            continue;
                                                        } else {
                                                            continue;
                                                        }
                                                    } catch (NumberFormatException e5) {
                                                        zzkp4.zzs.zzay().zzk().zzb("Experiment ID NumberFormatException", e5);
                                                    }
                                                }
                                            }
                                        }
                                        if (arrayList != null) {
                                            zzu22.zzh(arrayList);
                                        }
                                        zzc2 = zzh((String) Preconditions.checkNotNull(zzp.zza)).zzc(zzag.zzb(zzp.zzv));
                                        if (zzc2.zzj()) {
                                            Pair<String, Boolean> zzd2 = this.zzk.zzd(zzp.zza, zzc2);
                                            if (!TextUtils.isEmpty((CharSequence) zzd2.first) && zzp.zzo) {
                                                zzu22.zzab((String) zzd2.first);
                                                if (zzd2.second != null) {
                                                    zzu22.zzU(((Boolean) zzd2.second).booleanValue());
                                                }
                                            }
                                        }
                                        this.zzn.zzg().zzu();
                                        zzu22.zzK(Build.MODEL);
                                        this.zzn.zzg().zzu();
                                        zzu22.zzV(Build.VERSION.RELEASE);
                                        zzu22.zzaf((int) this.zzn.zzg().zzb());
                                        zzu22.zzaj(this.zzn.zzg().zzc());
                                        if (!zzg().zzs(null, zzdw.zzan)) {
                                            zzu22.zzz(zzp.zzl);
                                        }
                                        if (this.zzn.zzJ()) {
                                            zzu22.zzal();
                                            if (!TextUtils.isEmpty(null)) {
                                                zzu22.zzL(null);
                                            }
                                        }
                                        zzaj zzaj162 = this.zze;
                                        zzak(zzaj162);
                                        zzj = zzaj162.zzj(zzp.zza);
                                        if (zzj == null) {
                                            zzj = new zzg(this.zzn, zzp.zza);
                                            zzj.zzI(zzw(zzc2));
                                            zzj.zzW(zzp.zzk);
                                            zzj.zzY(zzp.zzb);
                                            if (zzc2.zzj()) {
                                                zzj.zzag(this.zzk.zzf(zzp.zza));
                                            }
                                            zzj.zzac(0);
                                            zzj.zzad(0);
                                            zzj.zzab(0);
                                            zzj.zzK(zzp.zzc);
                                            zzj.zzL(zzp.zzj);
                                            zzj.zzJ(zzp.zzd);
                                            zzj.zzZ(zzp.zze);
                                            zzj.zzT(zzp.zzf);
                                            zzj.zzae(zzp.zzh);
                                            if (!zzg().zzs(null, zzdw.zzan)) {
                                                zzj.zzH(zzp.zzl);
                                            }
                                            zzj.zzU(zzp.zzs);
                                            zzaj zzaj21 = this.zze;
                                            zzak(zzaj21);
                                            zzaj21.zzD(zzj);
                                        }
                                        if (zzc2.zzk() && !TextUtils.isEmpty(zzj.zzu())) {
                                            zzu22.zzB((String) Preconditions.checkNotNull(zzj.zzu()));
                                        }
                                        if (!TextUtils.isEmpty(zzj.zzx())) {
                                            zzu22.zzP((String) Preconditions.checkNotNull(zzj.zzx()));
                                        }
                                        zzaj zzaj172 = this.zze;
                                        zzak(zzaj172);
                                        zzu = zzaj172.zzu(zzp.zza);
                                        for (i3 = i2; i3 < zzu.size(); i3++) {
                                            zzgg zzd3 = zzgh.zzd();
                                            zzd3.zzf(zzu.get(i3).zzc);
                                            zzd3.zzg(zzu.get(i3).zzd);
                                            zzkp zzkp5 = this.zzi;
                                            zzak(zzkp5);
                                            zzkp5.zzv(zzd3, zzu.get(i3).zze);
                                            zzu22.zzk(zzd3);
                                        }
                                        zzaj = this.zze;
                                        zzak(zzaj);
                                        zzfy = (zzfy) zzu22.zzaA();
                                        zzaj.zzg();
                                        zzaj.zzY();
                                        Preconditions.checkNotNull(zzfy);
                                        Preconditions.checkNotEmpty(zzfy.zzy());
                                        byte[] zzbs3 = zzfy.zzbs();
                                        zzkp zzkp22 = zzaj.zzf.zzi;
                                        zzak(zzkp22);
                                        long zzd4 = zzkp22.zzd(zzbs3);
                                        ContentValues contentValues22 = new ContentValues();
                                        contentValues22.put("app_id", zzfy.zzy());
                                        contentValues22.put("metadata_fingerprint", Long.valueOf(zzd4));
                                        contentValues22.put("metadata", zzbs3);
                                        zzaj.zzh().insertWithOnConflict("raw_events_metadata", null, contentValues22, 4);
                                        zzaj2 = this.zze;
                                        zzak(zzaj2);
                                        zzaq = new zzaq(zzao.zzf);
                                        while (true) {
                                            if (!zzaq.hasNext()) {
                                                zzfj zzfj4 = this.zzc;
                                                zzak(zzfj4);
                                                boolean zzn2 = zzfj4.zzn(zzao.zza, zzao.zzb);
                                                zzaj zzaj22 = this.zze;
                                                zzak(zzaj22);
                                                zzah zzl = zzaj22.zzl(zza(), zzao.zza, false, false, false, false, false);
                                                if (zzn2 && zzl.zze < ((long) zzg().zze(zzao.zza, zzdw.zzn))) {
                                                    i2 = 1;
                                                }
                                            } else if (str2.equals(zzaq.next())) {
                                                i2 = 1;
                                                break;
                                            } else {
                                                str2 = str2;
                                            }
                                        }
                                        zzaj2.zzg();
                                        zzaj2.zzY();
                                        Preconditions.checkNotNull(zzao);
                                        Preconditions.checkNotEmpty(zzao.zza);
                                        zzkp zzkp32 = zzaj2.zzf.zzi;
                                        zzak(zzkp32);
                                        byte[] zzbs22 = zzkp32.zzj(zzao).zzbs();
                                        contentValues = new ContentValues();
                                        contentValues.put("app_id", zzao.zza);
                                        contentValues.put(AppMeasurementSdk.ConditionalUserProperty.NAME, zzao.zzb);
                                        contentValues.put("timestamp", Long.valueOf(zzao.zzd));
                                        contentValues.put("metadata_fingerprint", Long.valueOf(zzd4));
                                        contentValues.put(UriUtil.DATA_SCHEME, zzbs22);
                                        contentValues.put("realtime", Integer.valueOf(i2));
                                        if (zzaj2.zzh().insert("raw_events", null, contentValues) == -1) {
                                            zzaj2.zzs.zzay().zzd().zzb("Failed to insert raw event (got -1). appId", zzei.zzn(zzao.zza));
                                        } else {
                                            this.zza = 0;
                                        }
                                        zzaj zzaj182 = this.zze;
                                        zzak(zzaj182);
                                        zzaj182.zzC();
                                        zzaj zzaj192 = this.zze;
                                        zzak(zzaj192);
                                        zzaj192.zzx();
                                        zzaf();
                                        zzay().zzj().zzb("Background event processing time, ms", Long.valueOf(((System.nanoTime() - j) + 500000) / 1000000));
                                        return;
                                    }
                                    zzaj.zzh().insertWithOnConflict("raw_events_metadata", null, contentValues22, 4);
                                    zzaj2 = this.zze;
                                    zzak(zzaj2);
                                    zzaq = new zzaq(zzao.zzf);
                                    while (true) {
                                        if (!zzaq.hasNext()) {
                                        }
                                        str2 = str2;
                                    }
                                    zzaj2.zzg();
                                    zzaj2.zzY();
                                    Preconditions.checkNotNull(zzao);
                                    Preconditions.checkNotEmpty(zzao.zza);
                                    zzkp zzkp322 = zzaj2.zzf.zzi;
                                    zzak(zzkp322);
                                    byte[] zzbs222 = zzkp322.zzj(zzao).zzbs();
                                    contentValues = new ContentValues();
                                    contentValues.put("app_id", zzao.zza);
                                    contentValues.put(AppMeasurementSdk.ConditionalUserProperty.NAME, zzao.zzb);
                                    contentValues.put("timestamp", Long.valueOf(zzao.zzd));
                                    contentValues.put("metadata_fingerprint", Long.valueOf(zzd4));
                                    contentValues.put(UriUtil.DATA_SCHEME, zzbs222);
                                    contentValues.put("realtime", Integer.valueOf(i2));
                                    if (zzaj2.zzh().insert("raw_events", null, contentValues) == -1) {
                                    }
                                    zzaj zzaj1822 = this.zze;
                                    zzak(zzaj1822);
                                    zzaj1822.zzC();
                                    zzaj zzaj1922 = this.zze;
                                    zzak(zzaj1922);
                                    zzaj1922.zzx();
                                    zzaf();
                                    zzay().zzj().zzb("Background event processing time, ms", Long.valueOf(((System.nanoTime() - j) + 500000) / 1000000));
                                    return;
                                } catch (SQLiteException e6) {
                                    zzaj.zzs.zzay().zzd().zzc("Error storing raw event metadata. appId", zzei.zzn(zzfy.zzy()), e6);
                                    throw e6;
                                }
                                arrayList = null;
                                if (arrayList != null) {
                                }
                                zzc2 = zzh((String) Preconditions.checkNotNull(zzp.zza)).zzc(zzag.zzb(zzp.zzv));
                                if (zzc2.zzj()) {
                                }
                                this.zzn.zzg().zzu();
                                zzu22.zzK(Build.MODEL);
                                this.zzn.zzg().zzu();
                                zzu22.zzV(Build.VERSION.RELEASE);
                                zzu22.zzaf((int) this.zzn.zzg().zzb());
                                zzu22.zzaj(this.zzn.zzg().zzc());
                                if (!zzg().zzs(null, zzdw.zzan)) {
                                }
                                if (this.zzn.zzJ()) {
                                }
                                zzaj zzaj1622 = this.zze;
                                zzak(zzaj1622);
                                zzj = zzaj1622.zzj(zzp.zza);
                                if (zzj == null) {
                                }
                                if (zzc2.zzk()) {
                                    zzu22.zzB((String) Preconditions.checkNotNull(zzj.zzu()));
                                }
                                if (!TextUtils.isEmpty(zzj.zzx())) {
                                }
                                zzaj zzaj1722 = this.zze;
                                zzak(zzaj1722);
                                zzu = zzaj1722.zzu(zzp.zza);
                                while (i3 < zzu.size()) {
                                }
                                zzaj = this.zze;
                                zzak(zzaj);
                                zzfy = (zzfy) zzu22.zzaA();
                                zzaj.zzg();
                                zzaj.zzY();
                                Preconditions.checkNotNull(zzfy);
                                Preconditions.checkNotEmpty(zzfy.zzy());
                                byte[] zzbs32 = zzfy.zzbs();
                                zzkp zzkp222 = zzaj.zzf.zzi;
                                zzak(zzkp222);
                                long zzd42 = zzkp222.zzd(zzbs32);
                                ContentValues contentValues222 = new ContentValues();
                                contentValues222.put("app_id", zzfy.zzy());
                                contentValues222.put("metadata_fingerprint", Long.valueOf(zzd42));
                                contentValues222.put("metadata", zzbs32);
                            }
                            zzak(zzaj3);
                            zzaj3.zzx();
                        }
                    }
                    String zzg = zza.zzb.zzg(FirebaseAnalytics.Param.CURRENCY);
                    if (z) {
                        double doubleValue = zza.zzb.zzd("value").doubleValue() * 1000000.0d;
                        if (doubleValue == 0.0d) {
                            doubleValue = ((double) zza.zzb.zze("value").longValue()) * 1000000.0d;
                        }
                        if (doubleValue > 9.223372036854776E18d || doubleValue < -9.223372036854776E18d) {
                            zzay().zzk().zzc("Data lost. Currency value is too big. appId", zzei.zzn(str6), Double.valueOf(doubleValue));
                            zzaj zzaj23 = this.zze;
                            zzak(zzaj23);
                            zzaj23.zzC();
                            zzaj3 = this.zze;
                            zzak(zzaj3);
                            zzaj3.zzx();
                        }
                        j6 = Math.round(doubleValue);
                        if (FirebaseAnalytics.Event.REFUND.equals(zza.zza)) {
                            j6 = -j6;
                        }
                    } else {
                        j6 = zza.zzb.zze("value").longValue();
                    }
                    if (!TextUtils.isEmpty(zzg)) {
                        String upperCase = zzg.toUpperCase(Locale.US);
                        if (upperCase.matches("[A-Z]{3}")) {
                            String valueOf = String.valueOf(upperCase);
                            if (valueOf.length() != 0) {
                                str3 = "_ltv_".concat(valueOf);
                            } else {
                                str3 = new String("_ltv_");
                            }
                            zzaj zzaj24 = this.zze;
                            zzak(zzaj24);
                            zzks zzp3 = zzaj24.zzp(str6, str3);
                            if (zzp3 != null) {
                                Object obj = zzp3.zze;
                                if (!(obj instanceof Long)) {
                                    j = nanoTime;
                                    str = "_err";
                                    c2 = 1;
                                    c = 0;
                                } else {
                                    j = nanoTime;
                                    str = "_err";
                                    zzks = new zzks(str6, zza.zzc, str3, zzav().currentTimeMillis(), Long.valueOf(((Long) obj).longValue() + j6));
                                    zzaj4 = this.zze;
                                    zzak(zzaj4);
                                    if (!zzaj4.zzN(zzks)) {
                                        zzay().zzd().zzd("Too many unique user properties are set. Ignoring user property. appId", zzei.zzn(str6), this.zzn.zzj().zze(zzks.zzc), zzks.zze);
                                        zzv().zzM(this.zzC, str6, 9, null, null, 0);
                                    }
                                }
                            } else {
                                j = nanoTime;
                                str = "_err";
                                c2 = 1;
                                c = 0;
                            }
                            zzaj zzaj25 = this.zze;
                            zzak(zzaj25);
                            int zze = zzg().zze(str6, zzdw.zzD) - 1;
                            Preconditions.checkNotEmpty(str6);
                            zzaj25.zzg();
                            zzaj25.zzY();
                            SQLiteDatabase zzh = zzaj25.zzh();
                            String[] strArr = new String[3];
                            strArr[c] = str6;
                            strArr[c2] = str6;
                            strArr[2] = String.valueOf(zze);
                            zzh.execSQL("delete from user_attributes where app_id=? and name in (select name from user_attributes where app_id=? and name like '_ltv_%' order by set_timestamp desc limit ?,10);", strArr);
                            zzks = new zzks(str6, zza.zzc, str3, zzav().currentTimeMillis(), Long.valueOf(j6));
                            zzaj4 = this.zze;
                            zzak(zzaj4);
                            if (!zzaj4.zzN(zzks)) {
                            }
                        } else {
                            j = nanoTime;
                            str = "_err";
                        }
                    } else {
                        j = nanoTime;
                        str = "_err";
                    }
                    boolean zzah2 = zzku.zzah(zza.zza);
                    boolean equals2 = str.equals(zza.zza);
                    zzv();
                    zzar = zza.zzb;
                    if (zzar != null) {
                    }
                    zzaj zzaj82 = this.zze;
                    zzak(zzaj82);
                    zzah zzm2 = zzaj82.zzm(zza(), str6, j2 + 1, true, zzah2, false, equals2, false);
                    long j82 = zzm2.zzb;
                    zzg();
                    intValue = j82 - ((long) zzdw.zzj.zza(null).intValue());
                    if (intValue <= 0) {
                    }
                    zzak(zzaj3);
                    zzaj3.zzx();
                } catch (Throwable th) {
                    zzaj zzaj26 = this.zze;
                    zzak(zzaj26);
                    zzaj26.zzx();
                    throw th;
                }
            } else {
                zzd(zzp);
            }
        }
    }

    final boolean zzX() {
        FileLock fileLock;
        zzaz().zzg();
        if (!zzg().zzs(null, zzdw.zzac) || (fileLock = this.zzw) == null || !fileLock.isValid()) {
            this.zze.zzs.zzf();
            try {
                this.zzx = new RandomAccessFile(new File(this.zzn.zzau().getFilesDir(), "google_app_measurement.db"), "rw").getChannel();
                this.zzw = this.zzx.tryLock();
                if (this.zzw != null) {
                    zzay().zzj().zza("Storage concurrent access okay");
                    return true;
                }
                zzay().zzd().zza("Storage concurrent data access panic");
                return false;
            } catch (FileNotFoundException e) {
                zzay().zzd().zzb("Failed to acquire storage lock", e);
                return false;
            } catch (IOException e2) {
                zzay().zzd().zzb("Failed to access storage lock file", e2);
                return false;
            } catch (OverlappingFileLockException e3) {
                zzay().zzk().zzb("Storage lock already acquired", e3);
                return false;
            }
        } else {
            zzay().zzj().zza("Storage concurrent access okay");
            return true;
        }
    }

    final long zza() {
        long currentTimeMillis = zzav().currentTimeMillis();
        zzjk zzjk = this.zzk;
        zzjk.zzY();
        zzjk.zzg();
        long zza = zzjk.zze.zza();
        if (zza == 0) {
            zza = ((long) zzjk.zzs.zzv().zzF().nextInt(86400000)) + 1;
            zzjk.zze.zzb(zza);
        }
        return ((((currentTimeMillis + zza) / 1000) / 60) / 60) / 24;
    }

    @Override // com.google.android.gms.measurement.internal.zzgn
    public final Context zzau() {
        return this.zzn.zzau();
    }

    @Override // com.google.android.gms.measurement.internal.zzgn
    public final Clock zzav() {
        return ((zzfs) Preconditions.checkNotNull(this.zzn)).zzav();
    }

    @Override // com.google.android.gms.measurement.internal.zzgn
    public final zzaa zzaw() {
        throw null;
    }

    @Override // com.google.android.gms.measurement.internal.zzgn
    public final zzei zzay() {
        return ((zzfs) Preconditions.checkNotNull(this.zzn)).zzay();
    }

    @Override // com.google.android.gms.measurement.internal.zzgn
    public final zzfp zzaz() {
        return ((zzfs) Preconditions.checkNotNull(this.zzn)).zzaz();
    }

    public final zzg zzd(zzp zzp) {
        String str;
        zzaz().zzg();
        zzB();
        Preconditions.checkNotNull(zzp);
        Preconditions.checkNotEmpty(zzp.zza);
        zzaj zzaj = this.zze;
        zzak(zzaj);
        zzg zzj = zzaj.zzj(zzp.zza);
        zzag zzc = zzh(zzp.zza).zzc(zzag.zzb(zzp.zzv));
        if (zzc.zzj()) {
            str = this.zzk.zzf(zzp.zza);
        } else {
            str = "";
        }
        if (zzj == null) {
            zzj = new zzg(this.zzn, zzp.zza);
            if (zzc.zzk()) {
                zzj.zzI(zzw(zzc));
            }
            if (zzc.zzj()) {
                zzj.zzag(str);
            }
        } else if (zzc.zzj() && str != null && !str.equals(zzj.zzB())) {
            zzj.zzag(str);
            zzj.zzI(zzw(zzc));
            zzna.zzc();
            if (zzg().zzs(null, zzdw.zzay) && !"00000000-0000-0000-0000-000000000000".equals(this.zzk.zzd(zzp.zza, zzc).first)) {
                zzaj zzaj2 = this.zze;
                zzak(zzaj2);
                if (zzaj2.zzp(zzp.zza, DBHelper.Key_ID) != null) {
                    zzaj zzaj3 = this.zze;
                    zzak(zzaj3);
                    if (zzaj3.zzp(zzp.zza, "_lair") == null) {
                        zzks zzks = new zzks(zzp.zza, DebugKt.DEBUG_PROPERTY_VALUE_AUTO, "_lair", zzav().currentTimeMillis(), 1L);
                        zzaj zzaj4 = this.zze;
                        zzak(zzaj4);
                        zzaj4.zzN(zzks);
                    }
                }
            }
        } else if (TextUtils.isEmpty(zzj.zzu()) && zzc.zzk()) {
            zzj.zzI(zzw(zzc));
        }
        zzj.zzY(zzp.zzb);
        zzj.zzF(zzp.zzq);
        zzoq.zzc();
        if (zzg().zzs(zzj.zzt(), zzdw.zzad)) {
            zzj.zzX(zzp.zzu);
        }
        if (!TextUtils.isEmpty(zzp.zzk)) {
            zzj.zzW(zzp.zzk);
        }
        long j = zzp.zze;
        if (j != 0) {
            zzj.zzZ(j);
        }
        if (!TextUtils.isEmpty(zzp.zzc)) {
            zzj.zzK(zzp.zzc);
        }
        zzj.zzL(zzp.zzj);
        String str2 = zzp.zzd;
        if (str2 != null) {
            zzj.zzJ(str2);
        }
        zzj.zzT(zzp.zzf);
        zzj.zzae(zzp.zzh);
        if (!TextUtils.isEmpty(zzp.zzg)) {
            zzj.zzaa(zzp.zzg);
        }
        if (!zzg().zzs(null, zzdw.zzan)) {
            zzj.zzH(zzp.zzl);
        }
        zzj.zzG(zzp.zzo);
        zzj.zzaf(zzp.zzr);
        zzj.zzU(zzp.zzs);
        if (zzj.zzak()) {
            zzaj zzaj5 = this.zze;
            zzak(zzaj5);
            zzaj5.zzD(zzj);
        }
        return zzj;
    }

    public final zzz zzf() {
        zzz zzz = this.zzh;
        zzak(zzz);
        return zzz;
    }

    public final zzaf zzg() {
        return ((zzfs) Preconditions.checkNotNull(this.zzn)).zzf();
    }

    public final zzag zzh(String str) {
        Throwable th;
        zzaj zzaj;
        Cursor cursor;
        SQLiteException e;
        String str2;
        zzaz().zzg();
        zzB();
        zzag zzag = this.zzB.get(str);
        if (zzag != null) {
            return zzag;
        }
        try {
            zzaj = this.zze;
            zzak(zzaj);
            Preconditions.checkNotNull(str);
            zzaj.zzg();
            zzaj.zzY();
            cursor = null;
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            Cursor rawQuery = zzaj.zzh().rawQuery("select consent_state from consent_settings where app_id=? limit 1;", new String[]{str});
            try {
                if (rawQuery.moveToFirst()) {
                    str2 = rawQuery.getString(0);
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                } else {
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    str2 = "G1";
                }
                zzag zzb2 = zzag.zzb(str2);
                zzT(str, zzb2);
                return zzb2;
            } catch (SQLiteException e2) {
                e = e2;
                zzaj.zzs.zzay().zzd().zzc("Database error", "select consent_state from consent_settings where app_id=? limit 1;", e);
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

    public final zzaj zzi() {
        zzaj zzaj = this.zze;
        zzak(zzaj);
        return zzaj;
    }

    public final zzed zzj() {
        return this.zzn.zzj();
    }

    public final zzeo zzl() {
        zzeo zzeo = this.zzd;
        zzak(zzeo);
        return zzeo;
    }

    public final zzeq zzm() {
        zzeq zzeq = this.zzf;
        if (zzeq != null) {
            return zzeq;
        }
        throw new IllegalStateException("Network broadcast receiver not created");
    }

    public final zzfj zzo() {
        zzfj zzfj = this.zzc;
        zzak(zzfj);
        return zzfj;
    }

    public final zzfs zzq() {
        return this.zzn;
    }

    public final zzia zzr() {
        zzia zzia = this.zzj;
        zzak(zzia);
        return zzia;
    }

    public final zzjk zzs() {
        return this.zzk;
    }

    public final zzkp zzu() {
        zzkp zzkp = this.zzi;
        zzak(zzkp);
        return zzkp;
    }

    public final zzku zzv() {
        return ((zzfs) Preconditions.checkNotNull(this.zzn)).zzv();
    }

    final String zzw(zzag zzag) {
        if (!zzag.zzk()) {
            return null;
        }
        byte[] bArr = new byte[16];
        zzv().zzF().nextBytes(bArr);
        return String.format(Locale.US, "%032x", new BigInteger(1, bArr));
    }

    public final String zzx(zzp zzp) {
        try {
            return (String) zzaz().zzh(new zzki(this, zzp)).get(30000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            zzay().zzd().zzc("Failed to get app instance id. appId", zzei.zzn(zzp.zza), e);
            return null;
        }
    }

    public final void zzz(Runnable runnable) {
        zzaz().zzg();
        if (this.zzq == null) {
            this.zzq = new ArrayList();
        }
        this.zzq.add(runnable);
    }

    private final void zzad(zzfx zzfx, long j, boolean z) {
        zzks zzks;
        String str;
        String str2 = true != z ? "_lte" : "_se";
        zzaj zzaj = this.zze;
        zzak(zzaj);
        zzks zzp = zzaj.zzp(zzfx.zzal(), str2);
        if (zzp == null || zzp.zze == null) {
            zzks = new zzks(zzfx.zzal(), DebugKt.DEBUG_PROPERTY_VALUE_AUTO, str2, zzav().currentTimeMillis(), Long.valueOf(j));
        } else {
            zzks = new zzks(zzfx.zzal(), DebugKt.DEBUG_PROPERTY_VALUE_AUTO, str2, zzav().currentTimeMillis(), Long.valueOf(((Long) zzp.zze).longValue() + j));
        }
        zzgg zzd = zzgh.zzd();
        zzd.zzf(str2);
        zzd.zzg(zzav().currentTimeMillis());
        zzd.zze(((Long) zzks.zze).longValue());
        zzgh zzaA = zzd.zzaA();
        int zza = zzkp.zza(zzfx, str2);
        if (zza >= 0) {
            zzfx.zzai(zza, zzaA);
        } else {
            zzfx.zzl(zzaA);
        }
        if (j > 0) {
            zzaj zzaj2 = this.zze;
            zzak(zzaj2);
            zzaj2.zzN(zzks);
            if (true != z) {
                str = "lifetime";
            } else {
                str = "session-scoped";
            }
            zzay().zzj().zzc("Updated engagement user property. scope, value", str, zzks.zze);
        }
    }
}
