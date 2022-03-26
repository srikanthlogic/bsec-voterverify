package com.google.android.gms.internal.measurement;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.Unsafe;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public final class zzms {
    static final long zza;
    static final boolean zzb;
    private static final Unsafe zzc = zzg();
    private static final Class<?> zzd = zzij.zza();
    private static final boolean zze = zzv(Long.TYPE);
    private static final boolean zzf = zzv(Integer.TYPE);
    private static final zzmr zzg;
    private static final boolean zzh;
    private static final boolean zzi;

    static {
        boolean z;
        boolean z2;
        zzmr zzmr;
        Unsafe unsafe = zzc;
        zzmr zzmr2 = null;
        if (unsafe != null) {
            if (zze) {
                zzmr2 = new zzmq(unsafe);
            } else if (zzf) {
                zzmr2 = new zzmp(unsafe);
            }
        }
        zzg = zzmr2;
        zzmr zzmr3 = zzg;
        boolean z3 = true;
        if (zzmr3 == null) {
            z = false;
        } else {
            Unsafe unsafe2 = zzmr3.zza;
            if (unsafe2 == null) {
                z = false;
            } else {
                try {
                    Class<?> cls = unsafe2.getClass();
                    cls.getMethod("objectFieldOffset", Field.class);
                    cls.getMethod("getLong", Object.class, Long.TYPE);
                    z = zzB() != null;
                } catch (Throwable th) {
                    zzh(th);
                    z = false;
                }
            }
        }
        zzh = z;
        zzmr zzmr4 = zzg;
        if (zzmr4 == null) {
            z2 = false;
        } else {
            Unsafe unsafe3 = zzmr4.zza;
            if (unsafe3 == null) {
                z2 = false;
            } else {
                try {
                    Class<?> cls2 = unsafe3.getClass();
                    cls2.getMethod("objectFieldOffset", Field.class);
                    cls2.getMethod("arrayBaseOffset", Class.class);
                    cls2.getMethod("arrayIndexScale", Class.class);
                    cls2.getMethod("getInt", Object.class, Long.TYPE);
                    cls2.getMethod("putInt", Object.class, Long.TYPE, Integer.TYPE);
                    cls2.getMethod("getLong", Object.class, Long.TYPE);
                    cls2.getMethod("putLong", Object.class, Long.TYPE, Long.TYPE);
                    cls2.getMethod("getObject", Object.class, Long.TYPE);
                    cls2.getMethod("putObject", Object.class, Long.TYPE, Object.class);
                    z2 = true;
                } catch (Throwable th2) {
                    zzh(th2);
                    z2 = false;
                }
            }
        }
        zzi = z2;
        zza = (long) zzz(byte[].class);
        zzz(boolean[].class);
        zzA(boolean[].class);
        zzz(int[].class);
        zzA(int[].class);
        zzz(long[].class);
        zzA(long[].class);
        zzz(float[].class);
        zzA(float[].class);
        zzz(double[].class);
        zzA(double[].class);
        zzz(Object[].class);
        zzA(Object[].class);
        Field zzB = zzB();
        if (!(zzB == null || (zzmr = zzg) == null)) {
            zzmr.zzl(zzB);
        }
        if (ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN) {
            z3 = false;
        }
        zzb = z3;
    }

    private zzms() {
    }

    private static int zzA(Class<?> cls) {
        if (zzi) {
            return zzg.zzi(cls);
        }
        return -1;
    }

    private static Field zzB() {
        int i = zzij.zza;
        Field zzC = zzC(Buffer.class, "effectiveDirectAddress");
        if (zzC != null) {
            return zzC;
        }
        Field zzC2 = zzC(Buffer.class, "address");
        if (zzC2 == null || zzC2.getType() != Long.TYPE) {
            return null;
        }
        return zzC2;
    }

    private static Field zzC(Class<?> cls, String str) {
        try {
            return cls.getDeclaredField(str);
        } catch (Throwable th) {
            return null;
        }
    }

    public static void zzD(Object obj, long j, byte b) {
        long j2 = -4 & j;
        int zzj = zzg.zzj(obj, j2);
        int i = ((~((int) j)) & 3) << 3;
        zzg.zzn(obj, j2, ((b & 255) << i) | (zzj & (~(255 << i))));
    }

    public static void zzE(Object obj, long j, byte b) {
        long j2 = -4 & j;
        int i = (((int) j) & 3) << 3;
        int i2 = (b & 255) << i;
        zzg.zzn(obj, j2, i2 | (zzg.zzj(obj, j2) & (~(255 << i))));
    }

    public static double zza(Object obj, long j) {
        return zzg.zza(obj, j);
    }

    public static float zzb(Object obj, long j) {
        return zzg.zzb(obj, j);
    }

    public static int zzc(Object obj, long j) {
        return zzg.zzj(obj, j);
    }

    public static long zzd(Object obj, long j) {
        return zzg.zzk(obj, j);
    }

    public static <T> T zze(Class<T> cls) {
        try {
            return (T) zzc.allocateInstance(cls);
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Object zzf(Object obj, long j) {
        return zzg.zzm(obj, j);
    }

    public static Unsafe zzg() {
        try {
            return (Unsafe) AccessController.doPrivileged(new zzmo());
        } catch (Throwable th) {
            return null;
        }
    }

    static /* bridge */ /* synthetic */ void zzh(Throwable th) {
        Logger logger = Logger.getLogger(zzms.class.getName());
        Level level = Level.WARNING;
        String valueOf = String.valueOf(th);
        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 71);
        sb.append("platform method missing - proto runtime falling back to safer methods: ");
        sb.append(valueOf);
        logger.logp(level, "com.google.protobuf.UnsafeUtil", "logMissingMethod", sb.toString());
    }

    public static void zzm(Object obj, long j, boolean z) {
        zzg.zzc(obj, j, z);
    }

    public static void zzn(byte[] bArr, long j, byte b) {
        zzg.zzd(bArr, zza + j, b);
    }

    public static void zzo(Object obj, long j, double d) {
        zzg.zze(obj, j, d);
    }

    public static void zzp(Object obj, long j, float f) {
        zzg.zzf(obj, j, f);
    }

    public static void zzq(Object obj, long j, int i) {
        zzg.zzn(obj, j, i);
    }

    public static void zzr(Object obj, long j, long j2) {
        zzg.zzo(obj, j, j2);
    }

    public static void zzs(Object obj, long j, Object obj2) {
        zzg.zzp(obj, j, obj2);
    }

    public static /* bridge */ /* synthetic */ boolean zzt(Object obj, long j) {
        return ((byte) ((zzg.zzj(obj, -4 & j) >>> ((int) (((~j) & 3) << 3))) & 255)) != 0;
    }

    public static /* bridge */ /* synthetic */ boolean zzu(Object obj, long j) {
        return ((byte) ((zzg.zzj(obj, -4 & j) >>> ((int) ((j & 3) << 3))) & 255)) != 0;
    }

    static boolean zzv(Class<?> cls) {
        int i = zzij.zza;
        try {
            Class<?> cls2 = zzd;
            cls2.getMethod("peekLong", cls, Boolean.TYPE);
            cls2.getMethod("pokeLong", cls, Long.TYPE, Boolean.TYPE);
            cls2.getMethod("pokeInt", cls, Integer.TYPE, Boolean.TYPE);
            cls2.getMethod("peekInt", cls, Boolean.TYPE);
            cls2.getMethod("pokeByte", cls, Byte.TYPE);
            cls2.getMethod("peekByte", cls);
            cls2.getMethod("pokeByteArray", cls, byte[].class, Integer.TYPE, Integer.TYPE);
            cls2.getMethod("peekByteArray", cls, byte[].class, Integer.TYPE, Integer.TYPE);
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    public static boolean zzw(Object obj, long j) {
        return zzg.zzg(obj, j);
    }

    public static boolean zzx() {
        return zzi;
    }

    public static boolean zzy() {
        return zzh;
    }

    private static int zzz(Class<?> cls) {
        if (zzi) {
            return zzg.zzh(cls);
        }
        return -1;
    }
}
