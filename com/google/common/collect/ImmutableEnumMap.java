package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.io.Serializable;
import java.lang.Enum;
import java.util.EnumMap;
import java.util.Map;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
final class ImmutableEnumMap<K extends Enum<K>, V> extends ImmutableMap.IteratorBasedImmutableMap<K, V> {
    private final transient EnumMap<K, V> delegate;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K extends Enum<K>, V> ImmutableMap<K, V> asImmutable(EnumMap<K, V> map) {
        int size = map.size();
        if (size == 0) {
            return ImmutableMap.of();
        }
        if (size != 1) {
            return new ImmutableEnumMap(map);
        }
        Map.Entry<K, V> entry = (Map.Entry) Iterables.getOnlyElement(map.entrySet());
        return ImmutableMap.of(entry.getKey(), (Object) entry.getValue());
    }

    private ImmutableEnumMap(EnumMap<K, V> delegate) {
        this.delegate = delegate;
        Preconditions.checkArgument(!delegate.isEmpty());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableMap
    public UnmodifiableIterator<K> keyIterator() {
        return Iterators.unmodifiableIterator(this.delegate.keySet().iterator());
    }

    @Override // java.util.Map
    public int size() {
        return this.delegate.size();
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public boolean containsKey(@NullableDecl Object key) {
        return this.delegate.containsKey(key);
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public V get(Object key) {
        return this.delegate.get(key);
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map, java.lang.Object
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof ImmutableEnumMap) {
            object = ((ImmutableEnumMap) object).delegate;
        }
        return this.delegate.equals(object);
    }

    @Override // com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap
    UnmodifiableIterator<Map.Entry<K, V>> entryIterator() {
        return Maps.unmodifiableEntryIterator(this.delegate.entrySet().iterator());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableMap
    public boolean isPartialView() {
        return false;
    }

    @Override // com.google.common.collect.ImmutableMap
    Object writeReplace() {
        return new EnumSerializedForm(this.delegate);
    }

    /* loaded from: classes.dex */
    private static class EnumSerializedForm<K extends Enum<K>, V> implements Serializable {
        private static final long serialVersionUID = 0;
        final EnumMap<K, V> delegate;

        EnumSerializedForm(EnumMap<K, V> delegate) {
            this.delegate = delegate;
        }

        Object readResolve() {
            return new ImmutableEnumMap(this.delegate);
        }
    }
}
