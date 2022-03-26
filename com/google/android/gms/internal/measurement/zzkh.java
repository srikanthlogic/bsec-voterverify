package com.google.android.gms.internal.measurement;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import org.apache.commons.codec.CharEncoding;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public final class zzkh {
    static final Charset zza = Charset.forName("UTF-8");
    static final Charset zzb = Charset.forName(CharEncoding.ISO_8859_1);
    public static final byte[] zzc = new byte[0];
    public static final ByteBuffer zzd = ByteBuffer.wrap(zzc);
    public static final zzjc zze;

    static {
        byte[] bArr = zzc;
        int i = zzjc.zza;
        int length = bArr.length;
        zzja zzja = new zzja(bArr, 0, 0, false, null);
        try {
            zzja.zza(0);
            zze = zzja;
        } catch (zzkj e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static int zza(boolean z) {
        return z ? 1231 : 1237;
    }

    public static int zzb(byte[] bArr) {
        int length = bArr.length;
        int zzd2 = zzd(length, bArr, 0, length);
        if (zzd2 == 0) {
            return 1;
        }
        return zzd2;
    }

    public static int zzc(long j) {
        return (int) (j ^ (j >>> 32));
    }

    public static int zzd(int i, byte[] bArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            i = (i * 31) + bArr[i4];
        }
        return i;
    }

    public static <T> T zze(T t) {
        if (t != null) {
            return t;
        }
        throw null;
    }

    public static <T> T zzf(T t, String str) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(str);
    }

    public static Object zzg(Object obj, Object obj2) {
        return ((zzlg) obj).zzbD().zzau((zzlg) obj2).zzaC();
    }

    public static String zzh(byte[] bArr) {
        return new String(bArr, zza);
    }

    public static boolean zzi(byte[] bArr) {
        return zzmx.zze(bArr);
    }
}
