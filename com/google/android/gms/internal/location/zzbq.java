package com.google.android.gms.internal.location;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-location@@18.0.0 */
/* loaded from: classes.dex */
public final class zzbq<E> extends zzbo<E> {
    private final zzbs<E> zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzbq(zzbs<E> zzbs, int i) {
        super(zzbs.size(), i);
        this.zza = zzbs;
    }

    @Override // com.google.android.gms.internal.location.zzbo
    protected final E zza(int i) {
        return this.zza.get(i);
    }
}
