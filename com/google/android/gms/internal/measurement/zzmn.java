package com.google.android.gms.internal.measurement;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public final class zzmn extends AbstractList<String> implements RandomAccess, zzko {
    private final zzko zza;

    public zzmn(zzko zzko) {
        this.zza = zzko;
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ Object get(int i) {
        return ((zzkn) this.zza).get(i);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.List, java.util.Collection, java.lang.Iterable
    public final Iterator<String> iterator() {
        return new zzmm(this);
    }

    @Override // java.util.AbstractList, java.util.List
    public final ListIterator<String> listIterator(int i) {
        return new zzml(this, i);
    }

    @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
    public final int size() {
        return this.zza.size();
    }

    @Override // com.google.android.gms.internal.measurement.zzko
    public final zzko zze() {
        return this;
    }

    @Override // com.google.android.gms.internal.measurement.zzko
    public final Object zzf(int i) {
        return this.zza.zzf(i);
    }

    @Override // com.google.android.gms.internal.measurement.zzko
    public final List<?> zzh() {
        return this.zza.zzh();
    }

    @Override // com.google.android.gms.internal.measurement.zzko
    public final void zzi(zziy zziy) {
        throw new UnsupportedOperationException();
    }
}
