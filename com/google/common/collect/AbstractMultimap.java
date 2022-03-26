package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.Multimaps;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class AbstractMultimap<K, V> implements Multimap<K, V> {
    @NullableDecl
    private transient Map<K, Collection<V>> asMap;
    @NullableDecl
    private transient Collection<Map.Entry<K, V>> entries;
    @NullableDecl
    private transient Set<K> keySet;
    @NullableDecl
    private transient Multiset<K> keys;
    @NullableDecl
    private transient Collection<V> values;

    abstract Map<K, Collection<V>> createAsMap();

    abstract Collection<Map.Entry<K, V>> createEntries();

    abstract Set<K> createKeySet();

    abstract Multiset<K> createKeys();

    abstract Collection<V> createValues();

    abstract Iterator<Map.Entry<K, V>> entryIterator();

    @Override // com.google.common.collect.Multimap
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsValue(@NullableDecl Object value) {
        for (Collection<V> collection : asMap().values()) {
            if (collection.contains(value)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsEntry(@NullableDecl Object key, @NullableDecl Object value) {
        Collection<V> collection = asMap().get(key);
        return collection != null && collection.contains(value);
    }

    @Override // com.google.common.collect.Multimap
    public boolean remove(@NullableDecl Object key, @NullableDecl Object value) {
        Collection<V> collection = asMap().get(key);
        return collection != null && collection.remove(value);
    }

    @Override // com.google.common.collect.Multimap
    public boolean put(@NullableDecl K key, @NullableDecl V value) {
        return get(key).add(value);
    }

    @Override // com.google.common.collect.Multimap
    public boolean putAll(@NullableDecl K key, Iterable<? extends V> values) {
        Preconditions.checkNotNull(values);
        if (values instanceof Collection) {
            Collection<? extends V> valueCollection = (Collection) values;
            return !valueCollection.isEmpty() && get(key).addAll(valueCollection);
        }
        Iterator<? extends V> valueItr = values.iterator();
        return valueItr.hasNext() && Iterators.addAll(get(key), valueItr);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.Multimap
    public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
        boolean changed = false;
        for (Map.Entry<? extends K, ? extends V> entry : multimap.entries()) {
            changed |= put(entry.getKey(), entry.getValue());
        }
        return changed;
    }

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public Collection<V> replaceValues(@NullableDecl K key, Iterable<? extends V> values) {
        Preconditions.checkNotNull(values);
        Collection<V> result = removeAll(key);
        putAll(key, values);
        return result;
    }

    @Override // com.google.common.collect.Multimap
    public Collection<Map.Entry<K, V>> entries() {
        Collection<Map.Entry<K, V>> result = this.entries;
        if (result != null) {
            return result;
        }
        Collection<Map.Entry<K, V>> createEntries = createEntries();
        this.entries = createEntries;
        return createEntries;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class Entries extends Multimaps.Entries<K, V> {
        public Entries() {
            AbstractMultimap.this = this$0;
        }

        @Override // com.google.common.collect.Multimaps.Entries
        Multimap<K, V> multimap() {
            return AbstractMultimap.this;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<Map.Entry<K, V>> iterator() {
            return AbstractMultimap.this.entryIterator();
        }
    }

    /* loaded from: classes.dex */
    class EntrySet extends AbstractMultimap<K, V>.Entries implements Set<Map.Entry<K, V>> {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public EntrySet() {
            super();
            AbstractMultimap.this = this$0;
        }

        @Override // java.util.Collection, java.lang.Object, java.util.Set
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }

        @Override // java.util.Collection, java.lang.Object, java.util.Set
        public boolean equals(@NullableDecl Object obj) {
            return Sets.equalsImpl(this, obj);
        }
    }

    @Override // com.google.common.collect.Multimap
    public Set<K> keySet() {
        Set<K> result = this.keySet;
        if (result != null) {
            return result;
        }
        Set<K> createKeySet = createKeySet();
        this.keySet = createKeySet;
        return createKeySet;
    }

    @Override // com.google.common.collect.Multimap
    public Multiset<K> keys() {
        Multiset<K> result = this.keys;
        if (result != null) {
            return result;
        }
        Multiset<K> createKeys = createKeys();
        this.keys = createKeys;
        return createKeys;
    }

    @Override // com.google.common.collect.Multimap
    public Collection<V> values() {
        Collection<V> result = this.values;
        if (result != null) {
            return result;
        }
        Collection<V> createValues = createValues();
        this.values = createValues;
        return createValues;
    }

    /* loaded from: classes.dex */
    class Values extends AbstractCollection<V> {
        public Values() {
            AbstractMultimap.this = this$0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return AbstractMultimap.this.valueIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return AbstractMultimap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(@NullableDecl Object o) {
            return AbstractMultimap.this.containsValue(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            AbstractMultimap.this.clear();
        }
    }

    Iterator<V> valueIterator() {
        return Maps.valueIterator(entries().iterator());
    }

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public Map<K, Collection<V>> asMap() {
        Map<K, Collection<V>> result = this.asMap;
        if (result != null) {
            return result;
        }
        Map<K, Collection<V>> createAsMap = createAsMap();
        this.asMap = createAsMap;
        return createAsMap;
    }

    @Override // com.google.common.collect.Multimap, java.lang.Object, com.google.common.collect.ListMultimap
    public boolean equals(@NullableDecl Object object) {
        return Multimaps.equalsImpl(this, object);
    }

    @Override // com.google.common.collect.Multimap
    public int hashCode() {
        return asMap().hashCode();
    }

    @Override // java.lang.Object
    public String toString() {
        return asMap().toString();
    }
}
