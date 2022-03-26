package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
final class zzks extends zzku {
    private zzks() {
        super(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzks(zzkr zzkr) {
        super(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.measurement.zzku
    public final void zza(Object obj, long j) {
        ((zzkg) zzms.zzf(obj, j)).zzb();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [com.google.android.gms.internal.measurement.zzkg] */
    @Override // com.google.android.gms.internal.measurement.zzku
    public final <E> void zzb(Object obj, Object obj2, long j) {
        zzkg<E> zzkg = (zzkg) zzms.zzf(obj, j);
        zzkg<E> zzkg2 = (zzkg) zzms.zzf(obj2, j);
        int size = zzkg.size();
        int size2 = zzkg2.size();
        zzkg<E> zzkg3 = zzkg;
        zzkg3 = zzkg;
        if (size > 0 && size2 > 0) {
            boolean zzc = zzkg.zzc();
            zzkg<E> zzkg4 = zzkg;
            if (!zzc) {
                zzkg4 = zzkg.zzd(size2 + size);
            }
            zzkg4.addAll(zzkg2);
            zzkg3 = zzkg4;
        }
        if (size > 0) {
            zzkg2 = zzkg3;
        }
        zzms.zzs(obj, j, zzkg2);
    }
}
