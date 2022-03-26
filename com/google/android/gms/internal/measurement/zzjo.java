package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
final class zzjo {
    private static final zzjm<?> zza = new zzjn();
    private static final zzjm<?> zzb;

    static {
        zzjm<?> zzjm;
        try {
            zzjm = (zzjm) Class.forName("com.google.protobuf.ExtensionSchemaFull").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception e) {
            zzjm = null;
        }
        zzb = zzjm;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzjm<?> zza() {
        zzjm<?> zzjm = zzb;
        if (zzjm != null) {
            return zzjm;
        }
        throw new IllegalStateException("Protobuf runtime is not correctly loaded.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzjm<?> zzb() {
        return zza;
    }
}
