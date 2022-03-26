package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nullable;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public abstract class zzhu<T> {
    public static final /* synthetic */ int zzc;
    private static final Object zzd = new Object();
    @Nullable
    private static volatile zzhs zze = null;
    private static volatile boolean zzf = false;
    private static final AtomicReference<Collection<zzhu<?>>> zzg = new AtomicReference<>();
    private static final zzhw zzh = new zzhw(zzhl.zza, null);
    private static final AtomicInteger zzi = new AtomicInteger();
    final zzhr zza;
    final String zzb;
    private final T zzj;
    private volatile int zzk = -1;
    private volatile T zzl;
    private final boolean zzm;

    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ zzhu(zzhr zzhr, String str, Object obj, boolean z, zzht zzht) {
        if (zzhr.zzb != null) {
            this.zza = zzhr;
            this.zzb = str;
            this.zzj = obj;
            this.zzm = true;
            return;
        }
        throw new IllegalArgumentException("Must pass a valid SharedPreferences file name or ContentProvider URI");
    }

    @Deprecated
    public static void zzd(Context context) {
        boolean z = zzf;
        synchronized (zzd) {
            zzhs zzhs = zze;
            Context applicationContext = context.getApplicationContext();
            if (applicationContext != null) {
                context = applicationContext;
            }
            if (zzhs == null || zzhs.zza() != context) {
                zzha.zze();
                zzhv.zzc();
                zzhh.zze();
                zze = new zzgx(context, zzif.zza(new zzib(context) { // from class: com.google.android.gms.internal.measurement.zzhm
                    public final /* synthetic */ Context zza;

                    {
                        this.zza = r1;
                    }

                    @Override // com.google.android.gms.internal.measurement.zzib
                    public final Object zza() {
                        zzhz zzhz;
                        zzhz zzhz2;
                        Context context2 = this.zza;
                        int i = zzhu.zzc;
                        String str = Build.TYPE;
                        String str2 = Build.TAGS;
                        if ((!str.equals("eng") && !str.equals("userdebug")) || (!str2.contains("dev-keys") && !str2.contains("test-keys"))) {
                            return zzhz.zzc();
                        }
                        if (zzgw.zza() && !context2.isDeviceProtectedStorage()) {
                            context2 = context2.createDeviceProtectedStorageContext();
                        }
                        StrictMode.ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
                        try {
                            StrictMode.allowThreadDiskWrites();
                            try {
                                File file = new File(context2.getDir("phenotype_hermetic", 0), "overrides.txt");
                                zzhz = file.exists() ? zzhz.zzd(file) : zzhz.zzc();
                            } catch (RuntimeException e) {
                                Log.e("HermeticFileOverrides", "no data dir", e);
                                zzhz = zzhz.zzc();
                            }
                            if (zzhz.zzb()) {
                                File file2 = (File) zzhz.zza();
                                try {
                                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file2)));
                                    try {
                                        HashMap hashMap = new HashMap();
                                        HashMap hashMap2 = new HashMap();
                                        while (true) {
                                            String readLine = bufferedReader.readLine();
                                            if (readLine == null) {
                                                break;
                                            }
                                            String[] split = readLine.split(" ", 3);
                                            if (split.length != 3) {
                                                Log.e("HermeticFileOverrides", readLine.length() != 0 ? "Invalid: ".concat(readLine) : new String("Invalid: "));
                                            } else {
                                                String str3 = new String(split[0]);
                                                String decode = Uri.decode(new String(split[1]));
                                                String str4 = (String) hashMap2.get(split[2]);
                                                if (str4 == null) {
                                                    String str5 = new String(split[2]);
                                                    str4 = Uri.decode(str5);
                                                    if (str4.length() < 1024 || str4 == str5) {
                                                        hashMap2.put(str5, str4);
                                                    }
                                                }
                                                if (!hashMap.containsKey(str3)) {
                                                    hashMap.put(str3, new HashMap());
                                                }
                                                ((Map) hashMap.get(str3)).put(decode, str4);
                                            }
                                        }
                                        String valueOf = String.valueOf(file2);
                                        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 7);
                                        sb.append("Parsed ");
                                        sb.append(valueOf);
                                        Log.i("HermeticFileOverrides", sb.toString());
                                        zzhi zzhi = new zzhi(hashMap);
                                        bufferedReader.close();
                                        zzhz2 = zzhz.zzd(zzhi);
                                    } catch (Throwable th) {
                                        try {
                                            bufferedReader.close();
                                        } catch (Throwable th2) {
                                        }
                                        throw th;
                                    }
                                } catch (IOException e2) {
                                    throw new RuntimeException(e2);
                                }
                            } else {
                                zzhz2 = zzhz.zzc();
                            }
                            return zzhz2;
                        } finally {
                            StrictMode.setThreadPolicy(allowThreadDiskReads);
                        }
                    }
                }));
                zzi.incrementAndGet();
            }
        }
    }

    public static void zze() {
        zzi.incrementAndGet();
    }

    abstract T zza(Object obj);

    public final String zzc() {
        String str = this.zza.zzd;
        return this.zzb;
    }

    public final T zzb() {
        T t;
        String str;
        zzhe zzhe;
        Object zzb;
        if (this.zzm || this.zzb != null) {
            int i = zzi.get();
            if (this.zzk < i) {
                synchronized (this) {
                    if (this.zzk < i) {
                        zzhs zzhs = zze;
                        boolean z = zzf;
                        if (zzhs != null) {
                            zzhr zzhr = this.zza;
                            boolean z2 = zzhr.zzf;
                            boolean z3 = zzhr.zzg;
                            String zzc2 = zzhh.zza(zzhs.zza()).zzb("gms:phenotype:phenotype_flag:debug_bypass_phenotype");
                            if (zzc2 == null || !zzgv.zzc.matcher(zzc2).matches()) {
                                if (this.zza.zzb == null) {
                                    Context zza = zzhs.zza();
                                    String str2 = this.zza.zza;
                                    zzhe = zzhv.zza(zza, null);
                                } else if (zzhj.zza(zzhs.zza(), this.zza.zzb)) {
                                    boolean z4 = this.zza.zzh;
                                    zzhe = zzha.zza(zzhs.zza().getContentResolver(), this.zza.zzb);
                                } else {
                                    zzhe = null;
                                }
                                t = (zzhe == null || (zzb = zzhe.zzb(zzc())) == null) ? null : zza(zzb);
                            } else {
                                if (Log.isLoggable("PhenotypeFlag", 3)) {
                                    String valueOf = String.valueOf(zzc());
                                    Log.d("PhenotypeFlag", valueOf.length() != 0 ? "Bypass reading Phenotype values for flag: ".concat(valueOf) : new String("Bypass reading Phenotype values for flag: "));
                                }
                                t = null;
                            }
                            if (t == null) {
                                zzhr zzhr2 = this.zza;
                                if (!zzhr2.zze) {
                                    zzhy<Context, Boolean> zzhy = zzhr2.zzi;
                                    zzhh zza2 = zzhh.zza(zzhs.zza());
                                    zzhr zzhr3 = this.zza;
                                    if (zzhr3.zze) {
                                        str = null;
                                    } else {
                                        String str3 = zzhr3.zzc;
                                        str = this.zzb;
                                    }
                                    String zzc3 = zza2.zzb(str);
                                    t = zzc3 != null ? zza(zzc3) : null;
                                } else {
                                    t = null;
                                }
                                if (t == null) {
                                    t = this.zzj;
                                }
                            }
                            zzhz<zzhi> zza3 = zzhs.zzb().zza();
                            if (zza3.zzb()) {
                                zzhr zzhr4 = this.zza;
                                Uri uri = zzhr4.zzb;
                                String str4 = zzhr4.zza;
                                String zza4 = zza3.zza().zza(uri, null, zzhr4.zzd, this.zzb);
                                if (zza4 == null) {
                                    t = this.zzj;
                                } else {
                                    t = zza(zza4);
                                }
                            }
                            this.zzl = t;
                            this.zzk = i;
                        } else {
                            throw new IllegalStateException("Must call PhenotypeFlag.init() first");
                        }
                    }
                }
            }
            return this.zzl;
        }
        throw new NullPointerException("flagName must not be null");
    }
}
