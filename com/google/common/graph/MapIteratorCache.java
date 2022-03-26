package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public class MapIteratorCache<K, V> {
    private final Map<K, V> backingMap;
    @NullableDecl
    private volatile transient Map.Entry<K, V> cacheEntry;

    public MapIteratorCache(Map<K, V> backingMap) {
        this.backingMap = (Map) Preconditions.checkNotNull(backingMap);
    }

    public final V put(@NullableDecl K key, @NullableDecl V value) {
        clearCache();
        return this.backingMap.put(key, value);
    }

    public final V remove(@NullableDecl Object key) {
        clearCache();
        return this.backingMap.remove(key);
    }

    public final void clear() {
        clearCache();
        this.backingMap.clear();
    }

    public V get(@NullableDecl Object key) {
        V value = getIfCached(key);
        return value != null ? value : getWithoutCaching(key);
    }

    public final V getWithoutCaching(@NullableDecl Object key) {
        return this.backingMap.get(key);
    }

    public final boolean containsKey(@NullableDecl Object key) {
        return getIfCached(key) != null || this.backingMap.containsKey(key);
    }

    public final Set<K> unmodifiableKeySet() {
        return new AbstractSet<K>() { // from class: com.google.common.graph.MapIteratorCache.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set, java.lang.Iterable
            public UnmodifiableIterator<K> iterator() {
                final Iterator<Map.Entry<K, V>> entryIterator = MapIteratorCache.this.backingMap.entrySet().iterator();
                return new UnmodifiableIterator<K>() { // from class: com.google.common.graph.MapIteratorCache.1.1
                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return entryIterator.hasNext();
                    }

                    @Override // java.util.Iterator
                    public K next() {
                        Map.Entry<K, V> entry = (Map.Entry) entryIterator.next();
                        MapIteratorCache.this.cacheEntry = entry;
                        return entry.getKey();
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return MapIteratorCache.this.backingMap.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(@NullableDecl Object key) {
                return MapIteratorCache.this.containsKey(key);
            }
        };
    }

    public V getIfCached(@NullableDecl Object key) {
        Map.Entry<K, V> entry = this.cacheEntry;
        if (entry == null || entry.getKey() != key) {
            return null;
        }
        return entry.getValue();
    }

    public void clearCache() {
        this.cacheEntry = null;
    }
}
