package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public abstract class ForwardingMap<K, V> extends ForwardingObject implements Map<K, V> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.collect.ForwardingObject
    public abstract Map<K, V> delegate();

    @Override // java.util.Map
    public int size() {
        return delegate().size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return delegate().isEmpty();
    }

    @Override // java.util.Map
    public V remove(Object object) {
        return delegate().remove(object);
    }

    @Override // java.util.Map
    public void clear() {
        delegate().clear();
    }

    @Override // java.util.Map
    public boolean containsKey(@NullableDecl Object key) {
        return delegate().containsKey(key);
    }

    @Override // java.util.Map
    public boolean containsValue(@NullableDecl Object value) {
        return delegate().containsValue(value);
    }

    @Override // java.util.Map
    public V get(@NullableDecl Object key) {
        return delegate().get(key);
    }

    @Override // java.util.Map, com.google.common.collect.BiMap
    public V put(K key, V value) {
        return delegate().put(key, value);
    }

    @Override // java.util.Map, com.google.common.collect.BiMap
    public void putAll(Map<? extends K, ? extends V> map) {
        delegate().putAll(map);
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        return delegate().keySet();
    }

    @Override // java.util.Map, com.google.common.collect.BiMap
    public Collection<V> values() {
        return delegate().values();
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        return delegate().entrySet();
    }

    @Override // java.util.Map, java.lang.Object
    public boolean equals(@NullableDecl Object object) {
        return object == this || delegate().equals(object);
    }

    @Override // java.util.Map, java.lang.Object
    public int hashCode() {
        return delegate().hashCode();
    }

    protected void standardPutAll(Map<? extends K, ? extends V> map) {
        Maps.putAllImpl(this, map);
    }

    protected V standardRemove(@NullableDecl Object key) {
        Iterator<Map.Entry<K, V>> entryIterator = entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<K, V> entry = entryIterator.next();
            if (Objects.equal(entry.getKey(), key)) {
                V value = entry.getValue();
                entryIterator.remove();
                return value;
            }
        }
        return null;
    }

    protected void standardClear() {
        Iterators.clear(entrySet().iterator());
    }

    /* loaded from: classes.dex */
    protected class StandardKeySet extends Maps.KeySet<K, V> {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public StandardKeySet() {
            super(this$0);
            ForwardingMap.this = this$0;
        }
    }

    protected boolean standardContainsKey(@NullableDecl Object key) {
        return Maps.containsKeyImpl(this, key);
    }

    /* loaded from: classes.dex */
    protected class StandardValues extends Maps.Values<K, V> {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public StandardValues() {
            super(this$0);
            ForwardingMap.this = this$0;
        }
    }

    protected boolean standardContainsValue(@NullableDecl Object value) {
        return Maps.containsValueImpl(this, value);
    }

    /* loaded from: classes.dex */
    protected abstract class StandardEntrySet extends Maps.EntrySet<K, V> {
        public StandardEntrySet() {
            ForwardingMap.this = this$0;
        }

        @Override // com.google.common.collect.Maps.EntrySet
        Map<K, V> map() {
            return ForwardingMap.this;
        }
    }

    protected boolean standardIsEmpty() {
        return !entrySet().iterator().hasNext();
    }

    protected boolean standardEquals(@NullableDecl Object object) {
        return Maps.equalsImpl(this, object);
    }

    protected int standardHashCode() {
        return Sets.hashCodeImpl(entrySet());
    }

    protected String standardToString() {
        return Maps.toStringImpl(this);
    }
}
