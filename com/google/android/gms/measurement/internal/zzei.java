package com.google.android.gms.measurement.internal;

import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzei extends zzgm {
    private String zzc;
    private char zza = 0;
    private long zzb = -1;
    private final zzeg zzd = new zzeg(this, 6, false, false);
    private final zzeg zze = new zzeg(this, 6, true, false);
    private final zzeg zzf = new zzeg(this, 6, false, true);
    private final zzeg zzg = new zzeg(this, 5, false, false);
    private final zzeg zzh = new zzeg(this, 5, true, false);
    private final zzeg zzi = new zzeg(this, 5, false, true);
    private final zzeg zzj = new zzeg(this, 4, false, false);
    private final zzeg zzk = new zzeg(this, 3, false, false);
    private final zzeg zzl = new zzeg(this, 2, false, false);

    public zzei(zzfs zzfs) {
        super(zzfs);
    }

    public static Object zzn(String str) {
        if (str == null) {
            return null;
        }
        return new zzeh(str);
    }

    public static String zzo(boolean z, String str, Object obj, Object obj2, Object obj3) {
        String str2 = "";
        if (str == null) {
            str = str2;
        }
        String zzp = zzp(z, obj);
        String zzp2 = zzp(z, obj2);
        String zzp3 = zzp(z, obj3);
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(str)) {
            sb.append(str);
            str2 = ": ";
        }
        if (!TextUtils.isEmpty(zzp)) {
            sb.append(str2);
            sb.append(zzp);
            str2 = ", ";
        }
        if (!TextUtils.isEmpty(zzp2)) {
            sb.append(str2);
            sb.append(zzp2);
            str2 = ", ";
        }
        if (!TextUtils.isEmpty(zzp3)) {
            sb.append(str2);
            sb.append(zzp3);
        }
        return sb.toString();
    }

    static String zzp(boolean z, Object obj) {
        String className;
        String str = "";
        if (obj == null) {
            return str;
        }
        if (obj instanceof Integer) {
            obj = Long.valueOf((long) ((Integer) obj).intValue());
        }
        int i = 0;
        if (obj instanceof Long) {
            if (!z) {
                return String.valueOf(obj);
            }
            Long l = (Long) obj;
            if (Math.abs(l.longValue()) < 100) {
                return String.valueOf(obj);
            }
            if (String.valueOf(obj).charAt(0) == '-') {
                str = "-";
            }
            String valueOf = String.valueOf(Math.abs(l.longValue()));
            long round = Math.round(Math.pow(10.0d, (double) (valueOf.length() - 1)));
            long round2 = Math.round(Math.pow(10.0d, (double) valueOf.length()) - 4.0d);
            StringBuilder sb = new StringBuilder(str.length() + 43 + str.length());
            sb.append(str);
            sb.append(round);
            sb.append("...");
            sb.append(str);
            sb.append(round2);
            return sb.toString();
        } else if (obj instanceof Boolean) {
            return String.valueOf(obj);
        } else {
            if (obj instanceof Throwable) {
                Throwable th = (Throwable) obj;
                StringBuilder sb2 = new StringBuilder(z ? th.getClass().getName() : th.toString());
                String zzy = zzy(zzfs.class.getCanonicalName());
                StackTraceElement[] stackTrace = th.getStackTrace();
                int length = stackTrace.length;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    StackTraceElement stackTraceElement = stackTrace[i];
                    if (!stackTraceElement.isNativeMethod() && (className = stackTraceElement.getClassName()) != null && zzy(className).equals(zzy)) {
                        sb2.append(": ");
                        sb2.append(stackTraceElement);
                        break;
                    }
                    i++;
                }
                return sb2.toString();
            } else if (obj instanceof zzeh) {
                return ((zzeh) obj).zza;
            } else {
                if (z) {
                    return "-";
                }
                return String.valueOf(obj);
            }
        }
    }

    private static String zzy(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int lastIndexOf = str.lastIndexOf(46);
        if (lastIndexOf == -1) {
            return str;
        }
        return str.substring(0, lastIndexOf);
    }

    public final zzeg zzc() {
        return this.zzk;
    }

    public final zzeg zzd() {
        return this.zzd;
    }

    public final zzeg zze() {
        return this.zzf;
    }

    @Override // com.google.android.gms.measurement.internal.zzgm
    protected final boolean zzf() {
        return false;
    }

    public final zzeg zzh() {
        return this.zze;
    }

    public final zzeg zzi() {
        return this.zzj;
    }

    public final zzeg zzj() {
        return this.zzl;
    }

    public final zzeg zzk() {
        return this.zzg;
    }

    public final zzeg zzl() {
        return this.zzi;
    }

    public final zzeg zzm() {
        return this.zzh;
    }

    @EnsuresNonNull({"logTagDoNotUseDirectly"})
    public final String zzq() {
        String str;
        synchronized (this) {
            if (this.zzc == null) {
                if (this.zzs.zzy() != null) {
                    this.zzc = this.zzs.zzy();
                } else {
                    this.zzc = this.zzs.zzf().zzn();
                }
            }
            Preconditions.checkNotNull(this.zzc);
            str = this.zzc;
        }
        return str;
    }

    public final void zzt(int i, boolean z, boolean z2, String str, Object obj, Object obj2, Object obj3) {
        if (!z && Log.isLoggable(zzq(), i)) {
            Log.println(i, zzq(), zzo(false, str, obj, obj2, obj3));
        }
        if (!z2 && i >= 5) {
            Preconditions.checkNotNull(str);
            zzfp zzo = this.zzs.zzo();
            if (zzo == null) {
                Log.println(6, zzq(), "Scheduler not set. Not logging error/warn");
            } else if (!zzo.zzx()) {
                Log.println(6, zzq(), "Scheduler not initialized. Not logging error/warn");
            } else {
                if (i >= 9) {
                    i = 8;
                }
                zzo.zzp(new zzef(this, i, str, obj, obj2, obj3));
            }
        }
    }
}
