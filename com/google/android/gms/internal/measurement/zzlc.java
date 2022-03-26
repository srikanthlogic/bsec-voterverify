package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
final class zzlc {
    private static final zzlb zza;
    private static final zzlb zzb;

    static {
        zzlb zzlb;
        try {
            zzlb = (zzlb) Class.forName("com.google.protobuf.MapFieldSchemaFull").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception e) {
            zzlb = null;
        }
        zza = zzlb;
        zzb = new zzlb();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzlb zza() {
        return zza;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzlb zzb() {
        return zzb;
    }
}
