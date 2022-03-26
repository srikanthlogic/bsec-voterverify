package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
final class zzkx implements zzle {
    private final zzle[] zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzkx(zzle... zzleArr) {
        this.zza = zzleArr;
    }

    @Override // com.google.android.gms.internal.measurement.zzle
    public final zzld zzb(Class<?> cls) {
        zzle[] zzleArr = this.zza;
        for (int i = 0; i < 2; i++) {
            zzle zzle = zzleArr[i];
            if (zzle.zzc(cls)) {
                return zzle.zzb(cls);
            }
        }
        String valueOf = String.valueOf(cls.getName());
        throw new UnsupportedOperationException(valueOf.length() != 0 ? "No factory is available for message type: ".concat(valueOf) : new String("No factory is available for message type: "));
    }

    @Override // com.google.android.gms.internal.measurement.zzle
    public final boolean zzc(Class<?> cls) {
        zzle[] zzleArr = this.zza;
        for (int i = 0; i < 2; i++) {
            if (zzleArr[i].zzc(cls)) {
                return true;
            }
        }
        return false;
    }
}
