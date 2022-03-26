package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
final class zzlm {
    private static final zzll zza;
    private static final zzll zzb;

    static {
        zzll zzll;
        try {
            zzll = (zzll) Class.forName("com.google.protobuf.NewInstanceSchemaFull").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception e) {
            zzll = null;
        }
        zza = zzll;
        zzb = new zzll();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzll zza() {
        return zza;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzll zzb() {
        return zzb;
    }
}
