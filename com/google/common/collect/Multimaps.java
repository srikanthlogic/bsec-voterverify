package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.collect.AbstractMapBasedMultimap;
import com.google.common.collect.AbstractMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public final class Multimaps {
    private Multimaps() {
    }

    public static <K, V> Multimap<K, V> newMultimap(Map<K, Collection<V>> map, Supplier<? extends Collection<V>> factory) {
        return new CustomMultimap(map, factory);
    }

    /* loaded from: classes.dex */
    private static class CustomMultimap<K, V> extends AbstractMapBasedMultimap<K, V> {
        private static final long serialVersionUID;
        transient Supplier<? extends Collection<V>> factory;

        CustomMultimap(Map<K, Collection<V>> map, Supplier<? extends Collection<V>> factory) {
            super(map);
            this.factory = (Supplier) Preconditions.checkNotNull(factory);
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap
        Set<K> createKeySet() {
            return createMaybeNavigableKeySet();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap
        Map<K, Collection<V>> createAsMap() {
            return createMaybeNavigableAsMap();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap
        protected Collection<V> createCollection() {
            return (Collection) this.factory.get();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap
        <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> collection) {
            if (collection instanceof NavigableSet) {
                return Sets.unmodifiableNavigableSet((NavigableSet) collection);
            }
            if (collection instanceof SortedSet) {
                return Collections.unmodifiableSortedSet((SortedSet) collection);
            }
            if (collection instanceof Set) {
                return Collections.unmodifiableSet((Set) collection);
            }
            if (collection instanceof List) {
                return Collections.unmodifiableList((List) collection);
            }
            return Collections.unmodifiableCollection(collection);
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap
        Collection<V> wrapCollection(K key, Collection<V> collection) {
            if (collection instanceof List) {
                return wrapList(key, (List) collection, null);
            }
            if (collection instanceof NavigableSet) {
                return new AbstractMapBasedMultimap.WrappedNavigableSet(key, (NavigableSet) collection, null);
            }
            if (collection instanceof SortedSet) {
                return new AbstractMapBasedMultimap.WrappedSortedSet(key, (SortedSet) collection, null);
            }
            if (collection instanceof Set) {
                return new AbstractMapBasedMultimap.WrappedSet(key, (Set) collection);
            }
            return new AbstractMapBasedMultimap.WrappedCollection(key, collection, null);
        }

        private void writeObject(ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();
            stream.writeObject(this.factory);
            stream.writeObject(backingMap());
        }

        private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            this.factory = (Supplier) stream.readObject();
            setMap((Map) stream.readObject());
        }
    }

    public static <K, V> ListMultimap<K, V> newListMultimap(Map<K, Collection<V>> map, Supplier<? extends List<V>> factory) {
        return new CustomListMultimap(map, factory);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CustomListMultimap<K, V> extends AbstractListMultimap<K, V> {
        private static final long serialVersionUID;
        transient Supplier<? extends List<V>> factory;

        CustomListMultimap(Map<K, Collection<V>> map, Supplier<? extends List<V>> factory) {
            super(map);
            this.factory = (Supplier) Preconditions.checkNotNull(factory);
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap
        Set<K> createKeySet() {
            return createMaybeNavigableKeySet();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap
        Map<K, Collection<V>> createAsMap() {
            return createMaybeNavigableAsMap();
        }

        @Override // com.google.common.collect.AbstractListMultimap, com.google.common.collect.AbstractMapBasedMultimap
        public List<V> createCollection() {
            return (List) this.factory.get();
        }

        private void writeObject(ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();
            stream.writeObject(this.factory);
            stream.writeObject(backingMap());
        }

        private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            this.factory = (Supplier) stream.readObject();
            setMap((Map) stream.readObject());
        }
    }

    public static <K, V> SetMultimap<K, V> newSetMultimap(Map<K, Collection<V>> map, Supplier<? extends Set<V>> factory) {
        return new CustomSetMultimap(map, factory);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CustomSetMultimap<K, V> extends AbstractSetMultimap<K, V> {
        private static final long serialVersionUID;
        transient Supplier<? extends Set<V>> factory;

        CustomSetMultimap(Map<K, Collection<V>> map, Supplier<? extends Set<V>> factory) {
            super(map);
            this.factory = (Supplier) Preconditions.checkNotNull(factory);
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap
        Set<K> createKeySet() {
            return createMaybeNavigableKeySet();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap
        Map<K, Collection<V>> createAsMap() {
            return createMaybeNavigableAsMap();
        }

        @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap
        public Set<V> createCollection() {
            return (Set) this.factory.get();
        }

        @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap
        <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> collection) {
            if (collection instanceof NavigableSet) {
                return Sets.unmodifiableNavigableSet((NavigableSet) collection);
            }
            if (collection instanceof SortedSet) {
                return Collections.unmodifiableSortedSet((SortedSet) collection);
            }
            return Collections.unmodifiableSet((Set) collection);
        }

        @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap
        Collection<V> wrapCollection(K key, Collection<V> collection) {
            if (collection instanceof NavigableSet) {
                return new AbstractMapBasedMultimap.WrappedNavigableSet(key, (NavigableSet) collection, null);
            }
            if (collection instanceof SortedSet) {
                return new AbstractMapBasedMultimap.WrappedSortedSet(key, (SortedSet) collection, null);
            }
            return new AbstractMapBasedMultimap.WrappedSet(key, (Set) collection);
        }

        private void writeObject(ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();
            stream.writeObject(this.factory);
            stream.writeObject(backingMap());
        }

        private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            this.factory = (Supplier) stream.readObject();
            setMap((Map) stream.readObject());
        }
    }

    public static <K, V> SortedSetMultimap<K, V> newSortedSetMultimap(Map<K, Collection<V>> map, Supplier<? extends SortedSet<V>> factory) {
        return new CustomSortedSetMultimap(map, factory);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CustomSortedSetMultimap<K, V> extends AbstractSortedSetMultimap<K, V> {
        private static final long serialVersionUID;
        transient Supplier<? extends SortedSet<V>> factory;
        transient Comparator<? super V> valueComparator;

        CustomSortedSetMultimap(Map<K, Collection<V>> map, Supplier<? extends SortedSet<V>> factory) {
            super(map);
            this.factory = (Supplier) Preconditions.checkNotNull(factory);
            this.valueComparator = ((SortedSet) factory.get()).comparator();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap
        Set<K> createKeySet() {
            return createMaybeNavigableKeySet();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap
        Map<K, Collection<V>> createAsMap() {
            return createMaybeNavigableAsMap();
        }

        @Override // com.google.common.collect.AbstractSortedSetMultimap, com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap
        public SortedSet<V> createCollection() {
            return (SortedSet) this.factory.get();
        }

        @Override // com.google.common.collect.SortedSetMultimap
        public Comparator<? super V> valueComparator() {
            return this.valueComparator;
        }

        private void writeObject(ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();
            stream.writeObject(this.factory);
            stream.writeObject(backingMap());
        }

        private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            this.factory = (Supplier) stream.readObject();
            this.valueComparator = ((SortedSet) this.factory.get()).comparator();
            setMap((Map) stream.readObject());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <K, V, M extends Multimap<K, V>> M invertFrom(Multimap<? extends V, ? extends K> source, M dest) {
        Preconditions.checkNotNull(dest);
        for (Map.Entry<? extends V, ? extends K> entry : source.entries()) {
            dest.put(entry.getValue(), entry.getKey());
        }
        return dest;
    }

    public static <K, V> Multimap<K, V> synchronizedMultimap(Multimap<K, V> multimap) {
        return Synchronized.multimap(multimap, null);
    }

    public static <K, V> Multimap<K, V> unmodifiableMultimap(Multimap<K, V> delegate) {
        if ((delegate instanceof UnmodifiableMultimap) || (delegate instanceof ImmutableMultimap)) {
            return delegate;
        }
        return new UnmodifiableMultimap(delegate);
    }

    @Deprecated
    public static <K, V> Multimap<K, V> unmodifiableMultimap(ImmutableMultimap<K, V> delegate) {
        return (Multimap) Preconditions.checkNotNull(delegate);
    }

    /* loaded from: classes.dex */
    public static class UnmodifiableMultimap<K, V> extends ForwardingMultimap<K, V> implements Serializable {
        private static final long serialVersionUID;
        final Multimap<K, V> delegate;
        @NullableDecl
        transient Collection<Map.Entry<K, V>> entries;
        @NullableDecl
        transient Set<K> keySet;
        @NullableDecl
        transient Multiset<K> keys;
        @NullableDecl
        transient Map<K, Collection<V>> map;
        @NullableDecl
        transient Collection<V> values;

        UnmodifiableMultimap(Multimap<K, V> delegate) {
            this.delegate = (Multimap) Preconditions.checkNotNull(delegate);
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.ForwardingObject
        public Multimap<K, V> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public Map<K, Collection<V>> asMap() {
            Map<K, Collection<V>> result = this.map;
            if (result != null) {
                return result;
            }
            Map<K, Collection<V>> result2 = Collections.unmodifiableMap(Maps.transformValues(this.delegate.asMap(), new Function<Collection<V>, Collection<V>>() { // from class: com.google.common.collect.Multimaps.UnmodifiableMultimap.1
                @Override // com.google.common.base.Function
                public /* bridge */ /* synthetic */ Object apply(Object obj) {
                    return apply((Collection) ((Collection) obj));
                }

                public Collection<V> apply(Collection<V> collection) {
                    return Multimaps.unmodifiableValueCollection(collection);
                }
            }));
            this.map = result2;
            return result2;
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public Collection<Map.Entry<K, V>> entries() {
            Collection<Map.Entry<K, V>> result = this.entries;
            if (result != null) {
                return result;
            }
            Collection<Map.Entry<K, V>> result2 = Multimaps.unmodifiableEntries(this.delegate.entries());
            this.entries = result2;
            return result2;
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public Collection<V> get(K key) {
            return Multimaps.unmodifiableValueCollection(this.delegate.get(key));
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public Multiset<K> keys() {
            Multiset<K> result = this.keys;
            if (result != null) {
                return result;
            }
            Multiset<K> result2 = Multisets.unmodifiableMultiset(this.delegate.keys());
            this.keys = result2;
            return result2;
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public Set<K> keySet() {
            Set<K> result = this.keySet;
            if (result != null) {
                return result;
            }
            Set<K> result2 = Collections.unmodifiableSet(this.delegate.keySet());
            this.keySet = result2;
            return result2;
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public boolean put(K key, V value) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public boolean putAll(K key, Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public boolean remove(Object key, Object value) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public Collection<V> removeAll(Object key) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public Collection<V> values() {
            Collection<V> result = this.values;
            if (result != null) {
                return result;
            }
            Collection<V> result2 = Collections.unmodifiableCollection(this.delegate.values());
            this.values = result2;
            return result2;
        }
    }

    /* loaded from: classes.dex */
    private static class UnmodifiableListMultimap<K, V> extends UnmodifiableMultimap<K, V> implements ListMultimap<K, V> {
        private static final long serialVersionUID;

        UnmodifiableListMultimap(ListMultimap<K, V> delegate) {
            super(delegate);
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.ForwardingObject
        public ListMultimap<K, V> delegate() {
            return (ListMultimap) super.delegate();
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public List<V> get(K key) {
            return Collections.unmodifiableList(delegate().get((ListMultimap<K, V>) key));
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public List<V> removeAll(Object key) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public List<V> replaceValues(K key, Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: classes.dex */
    public static class UnmodifiableSetMultimap<K, V> extends UnmodifiableMultimap<K, V> implements SetMultimap<K, V> {
        private static final long serialVersionUID;

        UnmodifiableSetMultimap(SetMultimap<K, V> delegate) {
            super(delegate);
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.ForwardingObject
        public SetMultimap<K, V> delegate() {
            return (SetMultimap) super.delegate();
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public Set<V> get(K key) {
            return Collections.unmodifiableSet(delegate().get((SetMultimap<K, V>) key));
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public Set<Map.Entry<K, V>> entries() {
            return Maps.unmodifiableEntrySet(delegate().entries());
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public Set<V> removeAll(Object key) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public Set<V> replaceValues(K key, Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: classes.dex */
    private static class UnmodifiableSortedSetMultimap<K, V> extends UnmodifiableSetMultimap<K, V> implements SortedSetMultimap<K, V> {
        private static final long serialVersionUID;

        UnmodifiableSortedSetMultimap(SortedSetMultimap<K, V> delegate) {
            super(delegate);
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableSetMultimap, com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.ForwardingObject
        public SortedSetMultimap<K, V> delegate() {
            return (SortedSetMultimap) super.delegate();
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableSetMultimap, com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public SortedSet<V> get(K key) {
            return Collections.unmodifiableSortedSet(delegate().get((SortedSetMultimap<K, V>) key));
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableSetMultimap, com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public SortedSet<V> removeAll(Object key) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableSetMultimap, com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.SortedSetMultimap
        public Comparator<? super V> valueComparator() {
            return delegate().valueComparator();
        }
    }

    public static <K, V> SetMultimap<K, V> synchronizedSetMultimap(SetMultimap<K, V> multimap) {
        return Synchronized.setMultimap(multimap, null);
    }

    public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(SetMultimap<K, V> delegate) {
        if ((delegate instanceof UnmodifiableSetMultimap) || (delegate instanceof ImmutableSetMultimap)) {
            return delegate;
        }
        return new UnmodifiableSetMultimap(delegate);
    }

    @Deprecated
    public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(ImmutableSetMultimap<K, V> delegate) {
        return (SetMultimap) Preconditions.checkNotNull(delegate);
    }

    public static <K, V> SortedSetMultimap<K, V> synchronizedSortedSetMultimap(SortedSetMultimap<K, V> multimap) {
        return Synchronized.sortedSetMultimap(multimap, null);
    }

    public static <K, V> SortedSetMultimap<K, V> unmodifiableSortedSetMultimap(SortedSetMultimap<K, V> delegate) {
        if (delegate instanceof UnmodifiableSortedSetMultimap) {
            return delegate;
        }
        return new UnmodifiableSortedSetMultimap(delegate);
    }

    public static <K, V> ListMultimap<K, V> synchronizedListMultimap(ListMultimap<K, V> multimap) {
        return Synchronized.listMultimap(multimap, null);
    }

    public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ListMultimap<K, V> delegate) {
        if ((delegate instanceof UnmodifiableListMultimap) || (delegate instanceof ImmutableListMultimap)) {
            return delegate;
        }
        return new UnmodifiableListMultimap(delegate);
    }

    @Deprecated
    public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ImmutableListMultimap<K, V> delegate) {
        return (ListMultimap) Preconditions.checkNotNull(delegate);
    }

    public static <V> Collection<V> unmodifiableValueCollection(Collection<V> collection) {
        if (collection instanceof SortedSet) {
            return Collections.unmodifiableSortedSet((SortedSet) collection);
        }
        if (collection instanceof Set) {
            return Collections.unmodifiableSet((Set) collection);
        }
        if (collection instanceof List) {
            return Collections.unmodifiableList((List) collection);
        }
        return Collections.unmodifiableCollection(collection);
    }

    public static <K, V> Collection<Map.Entry<K, V>> unmodifiableEntries(Collection<Map.Entry<K, V>> entries) {
        if (entries instanceof Set) {
            return Maps.unmodifiableEntrySet((Set) entries);
        }
        return new Maps.UnmodifiableEntries(Collections.unmodifiableCollection(entries));
    }

    public static <K, V> Map<K, List<V>> asMap(ListMultimap<K, V> multimap) {
        return (Map<K, Collection<V>>) multimap.asMap();
    }

    public static <K, V> Map<K, Set<V>> asMap(SetMultimap<K, V> multimap) {
        return (Map<K, Collection<V>>) multimap.asMap();
    }

    public static <K, V> Map<K, SortedSet<V>> asMap(SortedSetMultimap<K, V> multimap) {
        return (Map<K, Collection<V>>) multimap.asMap();
    }

    public static <K, V> Map<K, Collection<V>> asMap(Multimap<K, V> multimap) {
        return multimap.asMap();
    }

    public static <K, V> SetMultimap<K, V> forMap(Map<K, V> map) {
        return new MapMultimap(map);
    }

    /* loaded from: classes.dex */
    public static class MapMultimap<K, V> extends AbstractMultimap<K, V> implements SetMultimap<K, V>, Serializable {
        private static final long serialVersionUID;
        final Map<K, V> map;

        MapMultimap(Map<K, V> map) {
            this.map = (Map) Preconditions.checkNotNull(map);
        }

        @Override // com.google.common.collect.Multimap
        public int size() {
            return this.map.size();
        }

        @Override // com.google.common.collect.Multimap
        public boolean containsKey(Object key) {
            return this.map.containsKey(key);
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean containsValue(Object value) {
            return this.map.containsValue(value);
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean containsEntry(Object key, Object value) {
            return this.map.entrySet().contains(Maps.immutableEntry(key, value));
        }

        @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public Set<V> get(final K key) {
            return new Sets.ImprovedAbstractSet<V>() { // from class: com.google.common.collect.Multimaps.MapMultimap.1
                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set, java.lang.Iterable
                public Iterator<V> iterator() {
                    return new Iterator<V>() { // from class: com.google.common.collect.Multimaps.MapMultimap.1.1
                        int i;

                        @Override // java.util.Iterator
                        public boolean hasNext() {
                            return this.i == 0 && MapMultimap.this.map.containsKey(key);
                        }

                        @Override // java.util.Iterator
                        public V next() {
                            if (hasNext()) {
                                this.i++;
                                return MapMultimap.this.map.get(key);
                            }
                            throw new NoSuchElementException();
                        }

                        @Override // java.util.Iterator
                        public void remove() {
                            boolean z = true;
                            if (this.i != 1) {
                                z = false;
                            }
                            CollectPreconditions.checkRemove(z);
                            this.i = -1;
                            MapMultimap.this.map.remove(key);
                        }
                    };
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public int size() {
                    return MapMultimap.this.map.containsKey(key) ? 1 : 0;
                }
            };
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean put(K key, V value) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean putAll(K key, Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public Set<V> replaceValues(K key, Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean remove(Object key, Object value) {
            return this.map.entrySet().remove(Maps.immutableEntry(key, value));
        }

        @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public Set<V> removeAll(Object key) {
            Set<V> values = new HashSet<>(2);
            if (!this.map.containsKey(key)) {
                return values;
            }
            values.add(this.map.remove(key));
            return values;
        }

        @Override // com.google.common.collect.Multimap
        public void clear() {
            this.map.clear();
        }

        @Override // com.google.common.collect.AbstractMultimap
        Set<K> createKeySet() {
            return this.map.keySet();
        }

        @Override // com.google.common.collect.AbstractMultimap
        Collection<V> createValues() {
            return this.map.values();
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public Set<Map.Entry<K, V>> entries() {
            return this.map.entrySet();
        }

        @Override // com.google.common.collect.AbstractMultimap
        Collection<Map.Entry<K, V>> createEntries() {
            throw new AssertionError("unreachable");
        }

        @Override // com.google.common.collect.AbstractMultimap
        Multiset<K> createKeys() {
            return new Keys(this);
        }

        @Override // com.google.common.collect.AbstractMultimap
        Iterator<Map.Entry<K, V>> entryIterator() {
            return this.map.entrySet().iterator();
        }

        @Override // com.google.common.collect.AbstractMultimap
        Map<K, Collection<V>> createAsMap() {
            return new AsMap(this);
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public int hashCode() {
            return this.map.hashCode();
        }
    }

    public static <K, V1, V2> Multimap<K, V2> transformValues(Multimap<K, V1> fromMultimap, Function<? super V1, V2> function) {
        Preconditions.checkNotNull(function);
        return transformEntries(fromMultimap, Maps.asEntryTransformer(function));
    }

    public static <K, V1, V2> ListMultimap<K, V2> transformValues(ListMultimap<K, V1> fromMultimap, Function<? super V1, V2> function) {
        Preconditions.checkNotNull(function);
        return transformEntries((ListMultimap) fromMultimap, (Maps.EntryTransformer) Maps.asEntryTransformer(function));
    }

    public static <K, V1, V2> Multimap<K, V2> transformEntries(Multimap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
        return new TransformedEntriesMultimap(fromMap, transformer);
    }

    public static <K, V1, V2> ListMultimap<K, V2> transformEntries(ListMultimap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
        return new TransformedEntriesListMultimap(fromMap, transformer);
    }

    /* loaded from: classes.dex */
    public static class TransformedEntriesMultimap<K, V1, V2> extends AbstractMultimap<K, V2> {
        final Multimap<K, V1> fromMultimap;
        final Maps.EntryTransformer<? super K, ? super V1, V2> transformer;

        TransformedEntriesMultimap(Multimap<K, V1> fromMultimap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
            this.fromMultimap = (Multimap) Preconditions.checkNotNull(fromMultimap);
            this.transformer = (Maps.EntryTransformer) Preconditions.checkNotNull(transformer);
        }

        Collection<V2> transform(K key, Collection<V1> values) {
            Function<? super V1, V2> function = Maps.asValueToValueFunction(this.transformer, key);
            if (values instanceof List) {
                return Lists.transform((List) values, function);
            }
            return Collections2.transform(values, function);
        }

        @Override // com.google.common.collect.AbstractMultimap
        Map<K, Collection<V2>> createAsMap() {
            return Maps.transformEntries(this.fromMultimap.asMap(), new Maps.EntryTransformer<K, Collection<V1>, Collection<V2>>() { // from class: com.google.common.collect.Multimaps.TransformedEntriesMultimap.1
                @Override // com.google.common.collect.Maps.EntryTransformer
                public /* bridge */ /* synthetic */ Object transformEntry(Object obj, Object obj2) {
                    return transformEntry((AnonymousClass1) obj, (Collection) ((Collection) obj2));
                }

                public Collection<V2> transformEntry(K key, Collection<V1> value) {
                    return TransformedEntriesMultimap.this.transform(key, value);
                }
            });
        }

        @Override // com.google.common.collect.Multimap
        public void clear() {
            this.fromMultimap.clear();
        }

        @Override // com.google.common.collect.Multimap
        public boolean containsKey(Object key) {
            return this.fromMultimap.containsKey(key);
        }

        @Override // com.google.common.collect.AbstractMultimap
        Collection<Map.Entry<K, V2>> createEntries() {
            return new AbstractMultimap.Entries();
        }

        @Override // com.google.common.collect.AbstractMultimap
        Iterator<Map.Entry<K, V2>> entryIterator() {
            return Iterators.transform(this.fromMultimap.entries().iterator(), Maps.asEntryToEntryFunction(this.transformer));
        }

        @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public Collection<V2> get(K key) {
            return transform(key, this.fromMultimap.get(key));
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean isEmpty() {
            return this.fromMultimap.isEmpty();
        }

        @Override // com.google.common.collect.AbstractMultimap
        Set<K> createKeySet() {
            return this.fromMultimap.keySet();
        }

        @Override // com.google.common.collect.AbstractMultimap
        Multiset<K> createKeys() {
            return this.fromMultimap.keys();
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean put(K key, V2 value) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean putAll(K key, Iterable<? extends V2> values) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean putAll(Multimap<? extends K, ? extends V2> multimap) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean remove(Object key, Object value) {
            return get(key).remove(value);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public Collection<V2> removeAll(Object key) {
            return transform(key, this.fromMultimap.removeAll(key));
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public Collection<V2> replaceValues(K key, Iterable<? extends V2> values) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.Multimap
        public int size() {
            return this.fromMultimap.size();
        }

        @Override // com.google.common.collect.AbstractMultimap
        Collection<V2> createValues() {
            return Collections2.transform(this.fromMultimap.entries(), Maps.asEntryToValueFunction(this.transformer));
        }
    }

    /* loaded from: classes.dex */
    public static final class TransformedEntriesListMultimap<K, V1, V2> extends TransformedEntriesMultimap<K, V1, V2> implements ListMultimap<K, V2> {
        TransformedEntriesListMultimap(ListMultimap<K, V1> fromMultimap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
            super(fromMultimap, transformer);
        }

        @Override // com.google.common.collect.Multimaps.TransformedEntriesMultimap
        List<V2> transform(K key, Collection<V1> values) {
            return Lists.transform((List) values, Maps.asValueToValueFunction(this.transformer, key));
        }

        @Override // com.google.common.collect.Multimaps.TransformedEntriesMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public List<V2> get(K key) {
            return transform((TransformedEntriesListMultimap<K, V1, V2>) key, (Collection) this.fromMultimap.get(key));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Multimaps.TransformedEntriesMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public List<V2> removeAll(Object key) {
            return transform((TransformedEntriesListMultimap<K, V1, V2>) key, (Collection) this.fromMultimap.removeAll(key));
        }

        @Override // com.google.common.collect.Multimaps.TransformedEntriesMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public List<V2> replaceValues(K key, Iterable<? extends V2> values) {
            throw new UnsupportedOperationException();
        }
    }

    public static <K, V> ImmutableListMultimap<K, V> index(Iterable<V> values, Function<? super V, K> keyFunction) {
        return index(values.iterator(), keyFunction);
    }

    public static <K, V> ImmutableListMultimap<K, V> index(Iterator<V> values, Function<? super V, K> keyFunction) {
        Preconditions.checkNotNull(keyFunction);
        ImmutableListMultimap.Builder<K, V> builder = ImmutableListMultimap.builder();
        while (values.hasNext()) {
            V value = values.next();
            Preconditions.checkNotNull(value, values);
            builder.put((ImmutableListMultimap.Builder<K, V>) keyFunction.apply(value), (K) value);
        }
        return builder.build();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Keys<K, V> extends AbstractMultiset<K> {
        final Multimap<K, V> multimap;

        public Keys(Multimap<K, V> multimap) {
            this.multimap = multimap;
        }

        @Override // com.google.common.collect.AbstractMultiset
        Iterator<Multiset.Entry<K>> entryIterator() {
            return new TransformedIterator<Map.Entry<K, Collection<V>>, Multiset.Entry<K>>(this.multimap.asMap().entrySet().iterator()) { // from class: com.google.common.collect.Multimaps.Keys.1
                @Override // com.google.common.collect.TransformedIterator
                /* bridge */ /* synthetic */ Object transform(Object obj) {
                    return transform((Map.Entry) ((Map.Entry) obj));
                }

                Multiset.Entry<K> transform(final Map.Entry<K, Collection<V>> backingEntry) {
                    return new Multisets.AbstractEntry<K>() { // from class: com.google.common.collect.Multimaps.Keys.1.1
                        @Override // com.google.common.collect.Multiset.Entry
                        public K getElement() {
                            return (K) backingEntry.getKey();
                        }

                        @Override // com.google.common.collect.Multiset.Entry
                        public int getCount() {
                            return ((Collection) backingEntry.getValue()).size();
                        }
                    };
                }
            };
        }

        @Override // com.google.common.collect.AbstractMultiset
        int distinctElements() {
            return this.multimap.asMap().size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
        public int size() {
            return this.multimap.size();
        }

        @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
        public boolean contains(@NullableDecl Object element) {
            return this.multimap.containsKey(element);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, com.google.common.collect.Multiset
        public Iterator<K> iterator() {
            return Maps.keyIterator(this.multimap.entries().iterator());
        }

        @Override // com.google.common.collect.Multiset
        public int count(@NullableDecl Object element) {
            Collection<V> values = (Collection) Maps.safeGet(this.multimap.asMap(), element);
            if (values == null) {
                return 0;
            }
            return values.size();
        }

        @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
        public int remove(@NullableDecl Object element, int occurrences) {
            CollectPreconditions.checkNonnegative(occurrences, "occurrences");
            if (occurrences == 0) {
                return count(element);
            }
            Collection<V> values = (Collection) Maps.safeGet(this.multimap.asMap(), element);
            if (values == null) {
                return 0;
            }
            int oldCount = values.size();
            if (occurrences >= oldCount) {
                values.clear();
            } else {
                Iterator<V> iterator = values.iterator();
                for (int i = 0; i < occurrences; i++) {
                    iterator.next();
                    iterator.remove();
                }
            }
            return oldCount;
        }

        @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
        public void clear() {
            this.multimap.clear();
        }

        @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
        public Set<K> elementSet() {
            return this.multimap.keySet();
        }

        @Override // com.google.common.collect.AbstractMultiset
        Iterator<K> elementIterator() {
            throw new AssertionError("should never be called");
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Entries<K, V> extends AbstractCollection<Map.Entry<K, V>> {
        abstract Multimap<K, V> multimap();

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return multimap().size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(@NullableDecl Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> entry = (Map.Entry) o;
            return multimap().containsEntry(entry.getKey(), entry.getValue());
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean remove(@NullableDecl Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> entry = (Map.Entry) o;
            return multimap().remove(entry.getKey(), entry.getValue());
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            multimap().clear();
        }
    }

    /* loaded from: classes.dex */
    public static final class AsMap<K, V> extends Maps.ViewCachingAbstractMap<K, Collection<V>> {
        private final Multimap<K, V> multimap;

        public AsMap(Multimap<K, V> multimap) {
            this.multimap = (Multimap) Preconditions.checkNotNull(multimap);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            return this.multimap.keySet().size();
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
            return new EntrySet();
        }

        void removeValuesForKey(Object key) {
            this.multimap.keySet().remove(key);
        }

        /* loaded from: classes.dex */
        public class EntrySet extends Maps.EntrySet<K, Collection<V>> {
            EntrySet() {
                AsMap.this = this$0;
            }

            @Override // com.google.common.collect.Maps.EntrySet
            Map<K, Collection<V>> map() {
                return AsMap.this;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set, java.lang.Iterable
            public Iterator<Map.Entry<K, Collection<V>>> iterator() {
                return Maps.asMapEntryIterator(AsMap.this.multimap.keySet(), new Function<K, Collection<V>>() { // from class: com.google.common.collect.Multimaps.AsMap.EntrySet.1
                    @Override // com.google.common.base.Function
                    public Collection<V> apply(K key) {
                        return AsMap.this.multimap.get(key);
                    }
                });
            }

            @Override // com.google.common.collect.Maps.EntrySet, java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(Object o) {
                if (!contains(o)) {
                    return false;
                }
                AsMap.this.removeValuesForKey(((Map.Entry) o).getKey());
                return true;
            }
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Collection<V> get(Object key) {
            if (containsKey(key)) {
                return this.multimap.get(key);
            }
            return null;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Collection<V> remove(Object key) {
            if (containsKey(key)) {
                return this.multimap.removeAll(key);
            }
            return null;
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap, java.util.AbstractMap, java.util.Map
        public Set<K> keySet() {
            return this.multimap.keySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean isEmpty() {
            return this.multimap.isEmpty();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(Object key) {
            return this.multimap.containsKey(key);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            this.multimap.clear();
        }
    }

    public static <K, V> Multimap<K, V> filterKeys(Multimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
        if (unfiltered instanceof SetMultimap) {
            return filterKeys((SetMultimap) unfiltered, (Predicate) keyPredicate);
        }
        if (unfiltered instanceof ListMultimap) {
            return filterKeys((ListMultimap) unfiltered, (Predicate) keyPredicate);
        }
        if (unfiltered instanceof FilteredKeyMultimap) {
            FilteredKeyMultimap<K, V> prev = (FilteredKeyMultimap) unfiltered;
            return new FilteredKeyMultimap(prev.unfiltered, Predicates.and(prev.keyPredicate, keyPredicate));
        } else if (unfiltered instanceof FilteredMultimap) {
            return filterFiltered((FilteredMultimap) unfiltered, Maps.keyPredicateOnEntries(keyPredicate));
        } else {
            return new FilteredKeyMultimap(unfiltered, keyPredicate);
        }
    }

    public static <K, V> SetMultimap<K, V> filterKeys(SetMultimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
        if (unfiltered instanceof FilteredKeySetMultimap) {
            FilteredKeySetMultimap<K, V> prev = (FilteredKeySetMultimap) unfiltered;
            return new FilteredKeySetMultimap(prev.unfiltered(), Predicates.and(prev.keyPredicate, keyPredicate));
        } else if (unfiltered instanceof FilteredSetMultimap) {
            return filterFiltered((FilteredSetMultimap) ((FilteredSetMultimap) unfiltered), Maps.keyPredicateOnEntries(keyPredicate));
        } else {
            return new FilteredKeySetMultimap(unfiltered, keyPredicate);
        }
    }

    public static <K, V> ListMultimap<K, V> filterKeys(ListMultimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
        if (!(unfiltered instanceof FilteredKeyListMultimap)) {
            return new FilteredKeyListMultimap(unfiltered, keyPredicate);
        }
        FilteredKeyListMultimap<K, V> prev = (FilteredKeyListMultimap) unfiltered;
        return new FilteredKeyListMultimap(prev.unfiltered(), Predicates.and(prev.keyPredicate, keyPredicate));
    }

    public static <K, V> Multimap<K, V> filterValues(Multimap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
        return filterEntries(unfiltered, Maps.valuePredicateOnEntries(valuePredicate));
    }

    public static <K, V> SetMultimap<K, V> filterValues(SetMultimap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
        return filterEntries((SetMultimap) unfiltered, Maps.valuePredicateOnEntries(valuePredicate));
    }

    public static <K, V> Multimap<K, V> filterEntries(Multimap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
        Preconditions.checkNotNull(entryPredicate);
        if (unfiltered instanceof SetMultimap) {
            return filterEntries((SetMultimap) unfiltered, (Predicate) entryPredicate);
        }
        if (unfiltered instanceof FilteredMultimap) {
            return filterFiltered((FilteredMultimap) unfiltered, entryPredicate);
        }
        return new FilteredEntryMultimap((Multimap) Preconditions.checkNotNull(unfiltered), entryPredicate);
    }

    public static <K, V> SetMultimap<K, V> filterEntries(SetMultimap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
        Preconditions.checkNotNull(entryPredicate);
        if (unfiltered instanceof FilteredSetMultimap) {
            return filterFiltered((FilteredSetMultimap) unfiltered, (Predicate) entryPredicate);
        }
        return new FilteredEntrySetMultimap((SetMultimap) Preconditions.checkNotNull(unfiltered), entryPredicate);
    }

    private static <K, V> Multimap<K, V> filterFiltered(FilteredMultimap<K, V> multimap, Predicate<? super Map.Entry<K, V>> entryPredicate) {
        return new FilteredEntryMultimap(multimap.unfiltered(), Predicates.and(multimap.entryPredicate(), entryPredicate));
    }

    private static <K, V> SetMultimap<K, V> filterFiltered(FilteredSetMultimap<K, V> multimap, Predicate<? super Map.Entry<K, V>> entryPredicate) {
        return new FilteredEntrySetMultimap(multimap.unfiltered(), Predicates.and(multimap.entryPredicate(), entryPredicate));
    }

    public static boolean equalsImpl(Multimap<?, ?> multimap, @NullableDecl Object object) {
        if (object == multimap) {
            return true;
        }
        if (object instanceof Multimap) {
            return multimap.asMap().equals(((Multimap) object).asMap());
        }
        return false;
    }
}
