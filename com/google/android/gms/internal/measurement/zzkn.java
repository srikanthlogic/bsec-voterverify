package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public final class zzkn extends zzii<String> implements RandomAccess, zzko {
    private final List<Object> zzc;
    private static final zzkn zzb = new zzkn(10);
    public static final zzko zza = zzb;

    static {
        zzb.zzb();
    }

    public zzkn() {
        this(10);
    }

    private static String zzj(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof zziy) {
            return ((zziy) obj).zzn(zzkh.zza);
        }
        return zzkh.zzh((byte[]) obj);
    }

    @Override // com.google.android.gms.internal.measurement.zzii, java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ void add(int i, Object obj) {
        zzbM();
        this.zzc.add(i, (String) obj);
        this.modCount++;
    }

    @Override // com.google.android.gms.internal.measurement.zzii, java.util.AbstractList, java.util.List
    public final boolean addAll(int i, Collection<? extends String> collection) {
        zzbM();
        if (collection instanceof zzko) {
            collection = ((zzko) collection).zzh();
        }
        boolean addAll = this.zzc.addAll(i, collection);
        this.modCount++;
        return addAll;
    }

    @Override // com.google.android.gms.internal.measurement.zzii, java.util.AbstractList, java.util.AbstractCollection, java.util.List, java.util.Collection
    public final void clear() {
        zzbM();
        this.zzc.clear();
        this.modCount++;
    }

    @Override // com.google.android.gms.internal.measurement.zzii, java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ Object remove(int i) {
        zzbM();
        Object remove = this.zzc.remove(i);
        this.modCount++;
        return zzj(remove);
    }

    @Override // com.google.android.gms.internal.measurement.zzii, java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ Object set(int i, Object obj) {
        zzbM();
        return zzj(this.zzc.set(i, (String) obj));
    }

    @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
    public final int size() {
        return this.zzc.size();
    }

    @Override // com.google.android.gms.internal.measurement.zzkg
    public final /* bridge */ /* synthetic */ zzkg zzd(int i) {
        if (i >= size()) {
            ArrayList arrayList = new ArrayList(i);
            arrayList.addAll(this.zzc);
            return new zzkn(arrayList);
        }
        throw new IllegalArgumentException();
    }

    @Override // com.google.android.gms.internal.measurement.zzko
    public final zzko zze() {
        return zzc() ? new zzmn(this) : this;
    }

    @Override // com.google.android.gms.internal.measurement.zzko
    public final Object zzf(int i) {
        return this.zzc.get(i);
    }

    /* renamed from: zzg */
    public final String get(int i) {
        Object obj = this.zzc.get(i);
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof zziy) {
            zziy zziy = (zziy) obj;
            String zzn = zziy.zzn(zzkh.zza);
            if (zziy.zzi()) {
                this.zzc.set(i, zzn);
            }
            return zzn;
        }
        byte[] bArr = (byte[]) obj;
        String zzh = zzkh.zzh(bArr);
        if (zzkh.zzi(bArr)) {
            this.zzc.set(i, zzh);
        }
        return zzh;
    }

    @Override // com.google.android.gms.internal.measurement.zzko
    public final List<?> zzh() {
        return Collections.unmodifiableList(this.zzc);
    }

    @Override // com.google.android.gms.internal.measurement.zzko
    public final void zzi(zziy zziy) {
        zzbM();
        this.zzc.add(zziy);
        this.modCount++;
    }

    public zzkn(int i) {
        this.zzc = new ArrayList(i);
    }

    private zzkn(ArrayList<Object> arrayList) {
        this.zzc = arrayList;
    }

    @Override // com.google.android.gms.internal.measurement.zzii, java.util.AbstractCollection, java.util.List, java.util.Collection
    public final boolean addAll(Collection<? extends String> collection) {
        return addAll(size(), collection);
    }
}
