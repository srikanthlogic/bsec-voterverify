package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
final class zzju implements zzle {
    private static final zzju zza = new zzju();

    private zzju() {
    }

    public static zzju zza() {
        return zza;
    }

    @Override // com.google.android.gms.internal.measurement.zzle
    public final zzld zzb(Class<?> cls) {
        if (!zzjz.class.isAssignableFrom(cls)) {
            String valueOf = String.valueOf(cls.getName());
            throw new IllegalArgumentException(valueOf.length() != 0 ? "Unsupported message type: ".concat(valueOf) : new String("Unsupported message type: "));
        }
        try {
            return (zzld) zzjz.zzbw(cls.asSubclass(zzjz.class)).zzl(3, null, null);
        } catch (Exception e) {
            String valueOf2 = String.valueOf(cls.getName());
            throw new RuntimeException(valueOf2.length() != 0 ? "Unable to get message info for ".concat(valueOf2) : new String("Unable to get message info for "), e);
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzle
    public final boolean zzc(Class<?> cls) {
        return zzjz.class.isAssignableFrom(cls);
    }
}
