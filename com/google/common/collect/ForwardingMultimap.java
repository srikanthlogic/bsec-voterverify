package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public abstract class ForwardingMultimap<K, V> extends ForwardingObject implements Multimap<K, V> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.collect.ForwardingObject
    public abstract Multimap<K, V> delegate();

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public Map<K, Collection<V>> asMap() {
        return delegate().asMap();
    }

    @Override // com.google.common.collect.Multimap
    public void clear() {
        delegate().clear();
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsEntry(@NullableDecl Object key, @NullableDecl Object value) {
        return delegate().containsEntry(key, value);
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsKey(@NullableDecl Object key) {
        return delegate().containsKey(key);
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsValue(@NullableDecl Object value) {
        return delegate().containsValue(value);
    }

    @Override // com.google.common.collect.Multimap
    public Collection<Map.Entry<K, V>> entries() {
        return delegate().entries();
    }

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public Collection<V> get(@NullableDecl K key) {
        return delegate().get(key);
    }

    @Override // com.google.common.collect.Multimap
    public boolean isEmpty() {
        return delegate().isEmpty();
    }

    @Override // com.google.common.collect.Multimap
    public Multiset<K> keys() {
        return delegate().keys();
    }

    @Override // com.google.common.collect.Multimap
    public Set<K> keySet() {
        return delegate().keySet();
    }

    @Override // com.google.common.collect.Multimap
    public boolean put(K key, V value) {
        return delegate().put(key, value);
    }

    @Override // com.google.common.collect.Multimap
    public boolean putAll(K key, Iterable<? extends V> values) {
        return delegate().putAll(key, values);
    }

    @Override // com.google.common.collect.Multimap
    public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
        return delegate().putAll(multimap);
    }

    @Override // com.google.common.collect.Multimap
    public boolean remove(@NullableDecl Object key, @NullableDecl Object value) {
        return delegate().remove(key, value);
    }

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public Collection<V> removeAll(@NullableDecl Object key) {
        return delegate().removeAll(key);
    }

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
        return delegate().replaceValues(key, values);
    }

    @Override // com.google.common.collect.Multimap
    public int size() {
        return delegate().size();
    }

    @Override // com.google.common.collect.Multimap
    public Collection<V> values() {
        return delegate().values();
    }

    @Override // com.google.common.collect.Multimap, java.lang.Object, com.google.common.collect.ListMultimap
    public boolean equals(@NullableDecl Object object) {
        return object == this || delegate().equals(object);
    }

    @Override // com.google.common.collect.Multimap
    public int hashCode() {
        return delegate().hashCode();
    }
}
