package com.google.android.gms.measurement.internal;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Pair;
import androidx.collection.ArrayMap;
import androidx.collection.LruCache;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzc;
import com.google.android.gms.internal.measurement.zzd;
import com.google.android.gms.internal.measurement.zzez;
import com.google.android.gms.internal.measurement.zzfb;
import com.google.android.gms.internal.measurement.zzfc;
import com.google.android.gms.internal.measurement.zzfe;
import com.google.android.gms.internal.measurement.zzgm;
import com.google.android.gms.internal.measurement.zzgo;
import com.google.android.gms.internal.measurement.zzkj;
import com.google.android.gms.internal.measurement.zzn;
import com.google.android.gms.internal.measurement.zzpf;
import com.google.android.gms.internal.measurement.zzpl;
import com.google.android.gms.internal.measurement.zzr;
import com.google.android.gms.internal.measurement.zzt;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Callable;
import okhttp3.internal.cache.DiskLruCache;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzfj extends zzkd implements zzae {
    private final Map<String, Map<String, String>> zzc = new ArrayMap();
    private final Map<String, Map<String, Boolean>> zzd = new ArrayMap();
    private final Map<String, Map<String, Boolean>> zze = new ArrayMap();
    private final Map<String, zzfc> zzg = new ArrayMap();
    private final Map<String, String> zzi = new ArrayMap();
    private final Map<String, Map<String, Integer>> zzh = new ArrayMap();
    final LruCache<String, zzc> zza = new zzfg(this, 20);
    final zzr zzb = new zzfh(this);

    public zzfj(zzkn zzkn) {
        super(zzkn);
    }

    public static /* bridge */ /* synthetic */ zzc zzd(zzfj zzfj, String str) {
        zzfj.zzY();
        Preconditions.checkNotEmpty(str);
        zzpl.zzc();
        if (!zzfj.zzs.zzf().zzs(null, zzdw.zzav) || !zzfj.zzl(str)) {
            return null;
        }
        if (!zzfj.zzg.containsKey(str) || zzfj.zzg.get(str) == null) {
            zzfj.zzt(str);
        } else {
            zzfj.zzu(str, zzfj.zzg.get(str));
        }
        return zzfj.zza.snapshot().get(str);
    }

    private final zzfc zzr(String str, byte[] bArr) {
        Long l;
        if (bArr == null) {
            return zzfc.zzg();
        }
        try {
            zzfc zzaA = ((zzfb) zzkp.zzl(zzfc.zze(), bArr)).zzaA();
            zzeg zzj = this.zzs.zzay().zzj();
            String str2 = null;
            if (zzaA.zzq()) {
                l = Long.valueOf(zzaA.zzc());
            } else {
                l = null;
            }
            if (zzaA.zzp()) {
                str2 = zzaA.zzh();
            }
            zzj.zzc("Parsed config. version, gmp_app_id", l, str2);
            return zzaA;
        } catch (zzkj e) {
            this.zzs.zzay().zzk().zzc("Unable to merge remote config. appId", zzei.zzn(str), e);
            return zzfc.zzg();
        } catch (RuntimeException e2) {
            this.zzs.zzay().zzk().zzc("Unable to merge remote config. appId", zzei.zzn(str), e2);
            return zzfc.zzg();
        }
    }

    private final void zzs(String str, zzfb zzfb) {
        ArrayMap arrayMap = new ArrayMap();
        ArrayMap arrayMap2 = new ArrayMap();
        ArrayMap arrayMap3 = new ArrayMap();
        if (zzfb != null) {
            for (int i = 0; i < zzfb.zza(); i++) {
                zzez zzbv = zzfb.zzb(i).zzbv();
                if (TextUtils.isEmpty(zzbv.zzc())) {
                    this.zzs.zzay().zzk().zza("EventConfig contained null event name");
                } else {
                    String zzc = zzbv.zzc();
                    String zzb = zzgp.zzb(zzbv.zzc());
                    if (!TextUtils.isEmpty(zzb)) {
                        zzbv.zzb(zzb);
                        zzfb.zzd(i, zzbv);
                    }
                    arrayMap.put(zzc, Boolean.valueOf(zzbv.zzd()));
                    arrayMap2.put(zzbv.zzc(), Boolean.valueOf(zzbv.zze()));
                    if (zzbv.zzf()) {
                        if (zzbv.zza() < 2 || zzbv.zza() > 65535) {
                            this.zzs.zzay().zzk().zzc("Invalid sampling rate. Event name, sample rate", zzbv.zzc(), Integer.valueOf(zzbv.zza()));
                        } else {
                            arrayMap3.put(zzbv.zzc(), Integer.valueOf(zzbv.zza()));
                        }
                    }
                }
            }
        }
        this.zzd.put(str, arrayMap);
        this.zze.put(str, arrayMap2);
        this.zzh.put(str, arrayMap3);
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x00a3  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00c2  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private final void zzt(String str) {
        Throwable th;
        zzaj zzi;
        Cursor cursor;
        Pair pair;
        Cursor cursor2;
        SQLiteException e;
        zzY();
        zzg();
        Preconditions.checkNotEmpty(str);
        if (this.zzg.get(str) == null) {
            try {
                zzi = this.zzf.zzi();
                Preconditions.checkNotEmpty(str);
                zzi.zzg();
                zzi.zzY();
                cursor = null;
            } catch (Throwable th2) {
                th = th2;
            }
            try {
                cursor2 = zzi.zzh().query("apps", new String[]{"remote_config", "config_last_modified_time"}, "app_id=?", new String[]{str}, null, null, null);
                try {
                } catch (SQLiteException e2) {
                    e = e2;
                    zzi.zzs.zzay().zzd().zzc("Error querying remote config. appId", zzei.zzn(str), e);
                    if (cursor2 == null) {
                        pair = null;
                        if (pair == null) {
                        }
                    }
                    cursor2.close();
                    pair = null;
                    if (pair == null) {
                    }
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
            if (cursor2.moveToFirst()) {
                byte[] blob = cursor2.getBlob(0);
                String string = cursor2.getString(1);
                if (cursor2.moveToNext()) {
                    zzi.zzs.zzay().zzd().zzb("Got multiple records for app config, expected one. appId", zzei.zzn(str));
                }
                if (blob != null) {
                    pair = new Pair(blob, string);
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    if (pair == null) {
                    }
                } else if (cursor2 != null) {
                    cursor2.close();
                }
            } else if (cursor2 != null) {
                cursor2.close();
            } else {
                pair = null;
                if (pair == null) {
                    this.zzc.put(str, null);
                    this.zzd.put(str, null);
                    this.zze.put(str, null);
                    this.zzg.put(str, null);
                    this.zzi.put(str, null);
                    this.zzh.put(str, null);
                    return;
                }
                zzfb zzbv = zzr(str, (byte[]) pair.first).zzbv();
                zzs(str, zzbv);
                this.zzc.put(str, zzv(zzbv.zzaA()));
                this.zzg.put(str, zzbv.zzaA());
                zzpl.zzc();
                if (this.zzs.zzf().zzs(null, zzdw.zzav)) {
                    zzu(str, zzbv.zzaA());
                }
                zzpf.zzc();
                if (this.zzs.zzf().zzs(null, zzdw.zzat)) {
                    this.zzi.put(str, (String) pair.second);
                    return;
                } else {
                    this.zzi.put(str, null);
                    return;
                }
            }
            pair = null;
            if (pair == null) {
            }
        }
    }

    private final void zzu(String str, zzfc zzfc) {
        if (zzfc.zza() != 0) {
            this.zzs.zzay().zzj().zzb("EES programs found", Integer.valueOf(zzfc.zza()));
            zzgo zzgo = zzfc.zzj().get(0);
            try {
                zzc zzc = new zzc();
                zzc.zzd("internal.remoteConfig", new Callable(str) { // from class: com.google.android.gms.measurement.internal.zzfd
                    public final /* synthetic */ String zzb;

                    {
                        this.zzb = r2;
                    }

                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        return new zzn("internal.remoteConfig", new zzfi(zzfj.this, this.zzb));
                    }
                });
                zzc.zzd("internal.appMetadata", new Callable(str) { // from class: com.google.android.gms.measurement.internal.zzff
                    public final /* synthetic */ String zzb;

                    {
                        this.zzb = r2;
                    }

                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0010: RETURN  
                          (wrap: com.google.android.gms.internal.measurement.zzu : 0x000d: CONSTRUCTOR  (r2v0 com.google.android.gms.internal.measurement.zzu A[REMOVE]) = 
                          ("internal.appMetadata")
                          (wrap: com.google.android.gms.measurement.internal.zzfe : 0x0008: CONSTRUCTOR  (r3v0 com.google.android.gms.measurement.internal.zzfe A[REMOVE]) = 
                          (wrap: com.google.android.gms.measurement.internal.zzfj : 0x0000: IGET  (r0v0 com.google.android.gms.measurement.internal.zzfj A[REMOVE]) = (r4v0 'this' com.google.android.gms.measurement.internal.zzff A[IMMUTABLE_TYPE, THIS]) com.google.android.gms.measurement.internal.zzff.zza com.google.android.gms.measurement.internal.zzfj)
                          (wrap: java.lang.String : 0x0002: IGET  (r1v0 java.lang.String A[REMOVE]) = (r4v0 'this' com.google.android.gms.measurement.internal.zzff A[IMMUTABLE_TYPE, THIS]) com.google.android.gms.measurement.internal.zzff.zzb java.lang.String)
                         call: com.google.android.gms.measurement.internal.zzfe.<init>(com.google.android.gms.measurement.internal.zzfj, java.lang.String):void type: CONSTRUCTOR)
                         call: com.google.android.gms.internal.measurement.zzu.<init>(java.lang.String, java.util.concurrent.Callable):void type: CONSTRUCTOR)
                         in method: com.google.android.gms.measurement.internal.zzff.call():java.lang.Object, file: classes.dex
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:270)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:233)
                        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:90)
                        	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                        	at jadx.core.dex.regions.Region.generate(Region.java:35)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                        	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:255)
                        	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:248)
                        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:369)
                        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:304)
                        Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.google.android.gms.measurement.internal.zzfe, state: NOT_LOADED
                        	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:268)
                        	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:668)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:378)
                        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:132)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                        	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:974)
                        	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:709)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:378)
                        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:132)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:328)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:263)
                        	... 10 more
                        */
                    @Override // java.util.concurrent.Callable
                    public final java.lang.Object call() {
                        /*
                            r4 = this;
                            com.google.android.gms.measurement.internal.zzfj r0 = com.google.android.gms.measurement.internal.zzfj.this
                            java.lang.String r1 = r4.zzb
                            com.google.android.gms.internal.measurement.zzu r2 = new com.google.android.gms.internal.measurement.zzu
                            com.google.android.gms.measurement.internal.zzfe r3 = new com.google.android.gms.measurement.internal.zzfe
                            r3.<init>(r0, r1)
                            java.lang.String r0 = "internal.appMetadata"
                            r2.<init>(r0, r3)
                            return r2
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzff.call():java.lang.Object");
                    }
                });
                zzc.zzd("internal.logger", new Callable() { // from class: com.google.android.gms.measurement.internal.zzfc
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        return new zzt(zzfj.this.zzb);
                    }
                });
                zzc.zzc(zzgo);
                this.zza.put(str, zzc);
                this.zzs.zzay().zzj().zzc("EES program loaded for appId, activities", str, Integer.valueOf(zzgo.zza().zza()));
                for (zzgm zzgm : zzgo.zza().zzd()) {
                    this.zzs.zzay().zzj().zzb("EES program activity", zzgm.zzb());
                }
            } catch (zzd e) {
                this.zzs.zzay().zzd().zzb("Failed to load EES program. appId", str);
            }
        } else {
            this.zza.remove(str);
        }
    }

    private static final Map<String, String> zzv(zzfc zzfc) {
        ArrayMap arrayMap = new ArrayMap();
        if (zzfc != null) {
            for (zzfe zzfe : zzfc.zzk()) {
                arrayMap.put(zzfe.zzb(), zzfe.zzc());
            }
        }
        return arrayMap;
    }

    @Override // com.google.android.gms.measurement.internal.zzae
    public final String zza(String str, String str2) {
        zzg();
        zzt(str);
        Map<String, String> map = this.zzc.get(str);
        if (map != null) {
            return map.get(str2);
        }
        return null;
    }

    @Override // com.google.android.gms.measurement.internal.zzkd
    protected final boolean zzb() {
        return false;
    }

    public final int zzc(String str, String str2) {
        Integer num;
        zzg();
        zzt(str);
        Map<String, Integer> map = this.zzh.get(str);
        if (map == null || (num = map.get(str2)) == null) {
            return 1;
        }
        return num.intValue();
    }

    public final zzfc zze(String str) {
        zzY();
        zzg();
        Preconditions.checkNotEmpty(str);
        zzt(str);
        return this.zzg.get(str);
    }

    public final String zzf(String str) {
        zzg();
        return this.zzi.get(str);
    }

    public final void zzi(String str) {
        zzg();
        this.zzi.put(str, null);
    }

    public final void zzj(String str) {
        zzg();
        this.zzg.remove(str);
    }

    public final boolean zzk(String str) {
        zzg();
        zzfc zze = zze(str);
        if (zze == null) {
            return false;
        }
        return zze.zzo();
    }

    public final boolean zzl(String str) {
        zzfc zzfc;
        zzpl.zzc();
        if (!this.zzs.zzf().zzs(null, zzdw.zzav) || TextUtils.isEmpty(str) || (zzfc = this.zzg.get(str)) == null || zzfc.zza() == 0) {
            return false;
        }
        return true;
    }

    public final boolean zzm(String str) {
        return DiskLruCache.VERSION_1.equals(zza(str, "measurement.upload.blacklist_internal"));
    }

    public final boolean zzn(String str, String str2) {
        Boolean bool;
        zzg();
        zzt(str);
        if (FirebaseAnalytics.Event.ECOMMERCE_PURCHASE.equals(str2) || FirebaseAnalytics.Event.PURCHASE.equals(str2) || FirebaseAnalytics.Event.REFUND.equals(str2)) {
            return true;
        }
        Map<String, Boolean> map = this.zze.get(str);
        if (map == null || (bool = map.get(str2)) == null) {
            return false;
        }
        return bool.booleanValue();
    }

    public final boolean zzo(String str, String str2) {
        Boolean bool;
        zzg();
        zzt(str);
        if (zzm(str) && zzku.zzag(str2)) {
            return true;
        }
        if (zzp(str) && zzku.zzah(str2)) {
            return true;
        }
        Map<String, Boolean> map = this.zzd.get(str);
        if (map == null || (bool = map.get(str2)) == null) {
            return false;
        }
        return bool.booleanValue();
    }

    public final boolean zzp(String str) {
        return DiskLruCache.VERSION_1.equals(zza(str, "measurement.upload.blacklist_public"));
    }

    public final boolean zzq(String str, byte[] bArr, String str2) {
        zzY();
        zzg();
        Preconditions.checkNotEmpty(str);
        zzfb zzbv = zzr(str, bArr).zzbv();
        if (zzbv == null) {
            return false;
        }
        zzs(str, zzbv);
        zzpl.zzc();
        if (this.zzs.zzf().zzs(null, zzdw.zzav)) {
            zzu(str, zzbv.zzaA());
        }
        this.zzg.put(str, zzbv.zzaA());
        this.zzi.put(str, str2);
        this.zzc.put(str, zzv(zzbv.zzaA()));
        this.zzf.zzi().zzB(str, new ArrayList(zzbv.zze()));
        try {
            zzbv.zzc();
            bArr = zzbv.zzaA().zzbs();
        } catch (RuntimeException e) {
            this.zzs.zzay().zzk().zzc("Unable to serialize reduced-size config. Storing full config instead. appId", zzei.zzn(str), e);
        }
        zzpf.zzc();
        if (this.zzs.zzf().zzs(null, zzdw.zzat)) {
            this.zzf.zzi().zzF(str, bArr, str2);
        } else {
            this.zzf.zzi().zzF(str, bArr, null);
        }
        this.zzg.put(str, zzbv.zzaA());
        return true;
    }
}
