package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public class zzjc {
    public static final /* synthetic */ int zza = 0;
    private static volatile int zzb = 100;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzjc(zzjb zzjb) {
        int i = zzb;
    }

    public static int zzb(int i) {
        return (-(i & 1)) ^ (i >>> 1);
    }

    public static long zzc(long j) {
        return (-(j & 1)) ^ (j >>> 1);
    }
}
