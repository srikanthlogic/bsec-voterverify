package com.google.android.gms.internal.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public final class zzla<K, V> extends LinkedHashMap<K, V> {
    private static final zzla zza = new zzla();
    private boolean zzb = true;

    static {
        zza.zzb = false;
    }

    private zzla() {
    }

    public static <K, V> zzla<K, V> zza() {
        return zza;
    }

    private static int zzf(Object obj) {
        if (obj instanceof byte[]) {
            return zzkh.zzb((byte[]) obj);
        }
        if (!(obj instanceof zzkb)) {
            return obj.hashCode();
        }
        throw new UnsupportedOperationException();
    }

    private final void zzg() {
        if (!this.zzb) {
            throw new UnsupportedOperationException();
        }
    }

    @Override // java.util.LinkedHashMap, java.util.AbstractMap, java.util.Map, java.util.HashMap
    public final void clear() {
        zzg();
        super.clear();
    }

    @Override // java.util.LinkedHashMap, java.util.AbstractMap, java.util.Map, java.util.HashMap
    public final Set<Map.Entry<K, V>> entrySet() {
        return isEmpty() ? Collections.emptySet() : super.entrySet();
    }

    @Override // java.util.AbstractMap, java.util.Map, java.lang.Object
    public final boolean equals(Object obj) {
        boolean z;
        if (!(obj instanceof Map)) {
            return false;
        }
        Map map = (Map) obj;
        if (this == map) {
            return true;
        }
        if (size() != map.size()) {
            return false;
        }
        for (Map.Entry<K, V> entry : entrySet()) {
            if (!map.containsKey(entry.getKey())) {
                return false;
            }
            V value = entry.getValue();
            Object obj2 = map.get(entry.getKey());
            if (!(value instanceof byte[]) || !(obj2 instanceof byte[])) {
                z = value.equals(obj2);
                continue;
            } else {
                z = Arrays.equals((byte[]) value, (byte[]) obj2);
                continue;
            }
            if (!z) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.AbstractMap, java.util.Map, java.lang.Object
    public final int hashCode() {
        int i = 0;
        for (Map.Entry<K, V> entry : entrySet()) {
            i += zzf(entry.getValue()) ^ zzf(entry.getKey());
        }
        return i;
    }

    @Override // java.util.AbstractMap, java.util.Map, java.util.HashMap
    public final V put(K k, V v) {
        zzg();
        zzkh.zze(k);
        zzkh.zze(v);
        return (V) super.put(k, v);
    }

    @Override // java.util.AbstractMap, java.util.Map, java.util.HashMap
    public final void putAll(Map<? extends K, ? extends V> map) {
        zzg();
        for (Object obj : map.keySet()) {
            zzkh.zze(obj);
            zzkh.zze(map.get(obj));
        }
        super.putAll(map);
    }

    @Override // java.util.AbstractMap, java.util.Map, java.util.HashMap
    public final V remove(Object obj) {
        zzg();
        return (V) super.remove(obj);
    }

    public final zzla<K, V> zzb() {
        return isEmpty() ? new zzla<>() : new zzla<>(this);
    }

    public final void zzc() {
        this.zzb = false;
    }

    public final void zzd(zzla<K, V> zzla) {
        zzg();
        if (!zzla.isEmpty()) {
            putAll(zzla);
        }
    }

    public final boolean zze() {
        return this.zzb;
    }

    private zzla(Map<K, V> map) {
        super(map);
    }
}
