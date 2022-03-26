package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public abstract class AbstractBiMap<K, V> extends ForwardingMap<K, V> implements BiMap<K, V>, Serializable {
    private static final long serialVersionUID;
    @NullableDecl
    private transient Map<K, V> delegate;
    @NullableDecl
    private transient Set<Map.Entry<K, V>> entrySet;
    @NullableDecl
    transient AbstractBiMap<V, K> inverse;
    @NullableDecl
    private transient Set<K> keySet;
    @NullableDecl
    private transient Set<V> valueSet;

    public AbstractBiMap(Map<K, V> forward, Map<V, K> backward) {
        setDelegates(forward, backward);
    }

    private AbstractBiMap(Map<K, V> backward, AbstractBiMap<V, K> forward) {
        this.delegate = backward;
        this.inverse = forward;
    }

    @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    public Map<K, V> delegate() {
        return this.delegate;
    }

    K checkKey(@NullableDecl K key) {
        return key;
    }

    V checkValue(@NullableDecl V value) {
        return value;
    }

    void setDelegates(Map<K, V> forward, Map<V, K> backward) {
        boolean z = true;
        Preconditions.checkState(this.delegate == null);
        Preconditions.checkState(this.inverse == null);
        Preconditions.checkArgument(forward.isEmpty());
        Preconditions.checkArgument(backward.isEmpty());
        if (forward == backward) {
            z = false;
        }
        Preconditions.checkArgument(z);
        this.delegate = forward;
        this.inverse = makeInverse(backward);
    }

    AbstractBiMap<V, K> makeInverse(Map<V, K> backward) {
        return new Inverse(backward, this);
    }

    void setInverse(AbstractBiMap<V, K> inverse) {
        this.inverse = inverse;
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map
    public boolean containsValue(@NullableDecl Object value) {
        return this.inverse.containsKey(value);
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
    public V put(@NullableDecl K key, @NullableDecl V value) {
        return putInBothMaps(key, value, false);
    }

    @Override // com.google.common.collect.BiMap
    public V forcePut(@NullableDecl K key, @NullableDecl V value) {
        return putInBothMaps(key, value, true);
    }

    private V putInBothMaps(@NullableDecl K key, @NullableDecl V value, boolean force) {
        checkKey(key);
        checkValue(value);
        boolean containedKey = containsKey(key);
        if (containedKey && Objects.equal(value, get(key))) {
            return value;
        }
        if (force) {
            inverse().remove(value);
        } else {
            Preconditions.checkArgument(!containsValue(value), "value already present: %s", value);
        }
        V oldValue = this.delegate.put(key, value);
        updateInverseMap(key, containedKey, oldValue, value);
        return oldValue;
    }

    public void updateInverseMap(K key, boolean containedKey, V oldValue, V newValue) {
        if (containedKey) {
            removeFromInverseMap(oldValue);
        }
        this.inverse.delegate.put(newValue, key);
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map
    public V remove(@NullableDecl Object key) {
        if (containsKey(key)) {
            return removeFromBothMaps(key);
        }
        return null;
    }

    public V removeFromBothMaps(Object key) {
        V oldValue = this.delegate.remove(key);
        removeFromInverseMap(oldValue);
        return oldValue;
    }

    public void removeFromInverseMap(V oldValue) {
        this.inverse.delegate.remove(oldValue);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map
    public void clear() {
        this.delegate.clear();
        this.inverse.delegate.clear();
    }

    @Override // com.google.common.collect.BiMap
    public BiMap<V, K> inverse() {
        return this.inverse;
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map
    public Set<K> keySet() {
        Set<K> result = this.keySet;
        if (result != null) {
            return result;
        }
        KeySet keySet = new KeySet();
        this.keySet = keySet;
        return keySet;
    }

    /* loaded from: classes.dex */
    public class KeySet extends ForwardingSet<K> {
        private KeySet() {
            AbstractBiMap.this = r1;
        }

        @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        public Set<K> delegate() {
            return AbstractBiMap.this.delegate.keySet();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public void clear() {
            AbstractBiMap.this.clear();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean remove(Object key) {
            if (!contains(key)) {
                return false;
            }
            AbstractBiMap.this.removeFromBothMaps(key);
            return true;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> keysToRemove) {
            return standardRemoveAll(keysToRemove);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> keysToRetain) {
            return standardRetainAll(keysToRetain);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return Maps.keyIterator(AbstractBiMap.this.entrySet().iterator());
        }
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
    public Set<V> values() {
        Set<V> result = this.valueSet;
        if (result != null) {
            return result;
        }
        ValueSet valueSet = new ValueSet();
        this.valueSet = valueSet;
        return valueSet;
    }

    /* loaded from: classes.dex */
    public class ValueSet extends ForwardingSet<V> {
        final Set<V> valuesDelegate;

        private ValueSet() {
            AbstractBiMap.this = r1;
            this.valuesDelegate = AbstractBiMap.this.inverse.keySet();
        }

        @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        public Set<V> delegate() {
            return this.valuesDelegate;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<V> iterator() {
            return Maps.valueIterator(AbstractBiMap.this.entrySet().iterator());
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            return standardToArray();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] array) {
            return (T[]) standardToArray(array);
        }

        @Override // com.google.common.collect.ForwardingObject, java.lang.Object
        public String toString() {
            return standardToString();
        }
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> result = this.entrySet;
        if (result != null) {
            return result;
        }
        EntrySet entrySet = new EntrySet();
        this.entrySet = entrySet;
        return entrySet;
    }

    /* loaded from: classes.dex */
    public class BiMapEntry extends ForwardingMapEntry<K, V> {
        private final Map.Entry<K, V> delegate;

        BiMapEntry(Map.Entry<K, V> delegate) {
            AbstractBiMap.this = this$0;
            this.delegate = delegate;
        }

        @Override // com.google.common.collect.ForwardingMapEntry, com.google.common.collect.ForwardingObject
        public Map.Entry<K, V> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.collect.ForwardingMapEntry, java.util.Map.Entry
        public V setValue(V value) {
            AbstractBiMap.this.checkValue(value);
            Preconditions.checkState(AbstractBiMap.this.entrySet().contains(this), "entry no longer in map");
            if (Objects.equal(value, getValue())) {
                return value;
            }
            Preconditions.checkArgument(!AbstractBiMap.this.containsValue(value), "value already present: %s", value);
            V oldValue = this.delegate.setValue(value);
            Preconditions.checkState(Objects.equal(value, AbstractBiMap.this.get(getKey())), "entry no longer in map");
            AbstractBiMap.this.updateInverseMap(getKey(), true, oldValue, value);
            return oldValue;
        }
    }

    Iterator<Map.Entry<K, V>> entrySetIterator() {
        final Iterator<Map.Entry<K, V>> iterator = this.delegate.entrySet().iterator();
        return new Iterator<Map.Entry<K, V>>() { // from class: com.google.common.collect.AbstractBiMap.1
            @NullableDecl
            Map.Entry<K, V> entry;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override // java.util.Iterator
            public Map.Entry<K, V> next() {
                this.entry = (Map.Entry) iterator.next();
                return new BiMapEntry(this.entry);
            }

            @Override // java.util.Iterator
            public void remove() {
                CollectPreconditions.checkRemove(this.entry != null);
                V value = this.entry.getValue();
                iterator.remove();
                AbstractBiMap.this.removeFromInverseMap(value);
                this.entry = null;
            }
        };
    }

    /* loaded from: classes.dex */
    public class EntrySet extends ForwardingSet<Map.Entry<K, V>> {
        final Set<Map.Entry<K, V>> esDelegate;

        private EntrySet() {
            AbstractBiMap.this = r1;
            this.esDelegate = AbstractBiMap.this.delegate.entrySet();
        }

        @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        public Set<Map.Entry<K, V>> delegate() {
            return this.esDelegate;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public void clear() {
            AbstractBiMap.this.clear();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean remove(Object object) {
            if (!this.esDelegate.contains(object)) {
                return false;
            }
            Map.Entry<?, ?> entry = (Map.Entry) object;
            ((AbstractBiMap) AbstractBiMap.this.inverse).delegate.remove(entry.getValue());
            this.esDelegate.remove(entry);
            return true;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return AbstractBiMap.this.entrySetIterator();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            return standardToArray();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] array) {
            return (T[]) standardToArray(array);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            return Maps.containsEntryImpl(delegate(), o);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean containsAll(Collection<?> c) {
            return standardContainsAll(c);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> c) {
            return standardRemoveAll(c);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> c) {
            return standardRetainAll(c);
        }
    }

    /* loaded from: classes.dex */
    public static class Inverse<K, V> extends AbstractBiMap<K, V> {
        private static final long serialVersionUID;

        @Override // com.google.common.collect.AbstractBiMap, com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return AbstractBiMap.super.delegate();
        }

        @Override // com.google.common.collect.AbstractBiMap, com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
        public /* bridge */ /* synthetic */ Collection values() {
            return AbstractBiMap.super.values();
        }

        Inverse(Map<K, V> backward, AbstractBiMap<V, K> forward) {
            super(backward, forward);
        }

        @Override // com.google.common.collect.AbstractBiMap
        K checkKey(K key) {
            return (K) this.inverse.checkValue(key);
        }

        @Override // com.google.common.collect.AbstractBiMap
        V checkValue(V value) {
            return (V) this.inverse.checkKey(value);
        }

        private void writeObject(ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();
            stream.writeObject(inverse());
        }

        private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            setInverse((AbstractBiMap) stream.readObject());
        }

        Object readResolve() {
            return inverse().inverse();
        }
    }
}
