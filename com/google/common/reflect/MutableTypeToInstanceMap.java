package com.google.common.reflect;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ForwardingMapEntry;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes3.dex */
public final class MutableTypeToInstanceMap<B> extends ForwardingMap<TypeToken<? extends B>, B> implements TypeToInstanceMap<B> {
    private final Map<TypeToken<? extends B>, B> backingMap = Maps.newHashMap();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
    @Deprecated
    public /* bridge */ /* synthetic */ Object put(Object obj, Object obj2) {
        return put((TypeToken<? extends TypeToken<? extends B>>) ((TypeToken) obj), (TypeToken<? extends B>) obj2);
    }

    @Override // com.google.common.reflect.TypeToInstanceMap
    @NullableDecl
    public <T extends B> T getInstance(Class<T> type) {
        return (T) trustedGet(TypeToken.of((Class) type));
    }

    @Override // com.google.common.reflect.TypeToInstanceMap
    @NullableDecl
    public <T extends B> T getInstance(TypeToken<T> type) {
        return (T) trustedGet(type.rejectTypeVariables());
    }

    @Override // com.google.common.reflect.TypeToInstanceMap
    @NullableDecl
    public <T extends B> T putInstance(Class<T> type, @NullableDecl T value) {
        return (T) trustedPut(TypeToken.of((Class) type), value);
    }

    @Override // com.google.common.reflect.TypeToInstanceMap
    @NullableDecl
    public <T extends B> T putInstance(TypeToken<T> type, @NullableDecl T value) {
        return (T) trustedPut(type.rejectTypeVariables(), value);
    }

    @Deprecated
    public B put(TypeToken<? extends B> key, B value) {
        throw new UnsupportedOperationException("Please use putInstance() instead.");
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
    @Deprecated
    public void putAll(Map<? extends TypeToken<? extends B>, ? extends B> map) {
        throw new UnsupportedOperationException("Please use putInstance() instead.");
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map
    public Set<Map.Entry<TypeToken<? extends B>, B>> entrySet() {
        return UnmodifiableEntry.transformEntries(super.entrySet());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    public Map<TypeToken<? extends B>, B> delegate() {
        return this.backingMap;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [T extends B, java.lang.Object] */
    @NullableDecl
    private <T extends B> T trustedPut(TypeToken<T> type, @NullableDecl T value) {
        return this.backingMap.put(type, value);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [T extends B, java.lang.Object] */
    @NullableDecl
    private <T extends B> T trustedGet(TypeToken<T> type) {
        return this.backingMap.get(type);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class UnmodifiableEntry<K, V> extends ForwardingMapEntry<K, V> {
        private final Map.Entry<K, V> delegate;

        static <K, V> Set<Map.Entry<K, V>> transformEntries(final Set<Map.Entry<K, V>> entries) {
            return new ForwardingSet<Map.Entry<K, V>>() { // from class: com.google.common.reflect.MutableTypeToInstanceMap.UnmodifiableEntry.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
                public Set<Map.Entry<K, V>> delegate() {
                    return entries;
                }

                @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.lang.Iterable, java.util.Set
                public Iterator<Map.Entry<K, V>> iterator() {
                    return UnmodifiableEntry.transformEntries(super.iterator());
                }

                @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
                public Object[] toArray() {
                    return standardToArray();
                }

                @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
                public <T> T[] toArray(T[] array) {
                    return (T[]) standardToArray(array);
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static <K, V> Iterator<Map.Entry<K, V>> transformEntries(Iterator<Map.Entry<K, V>> entries) {
            return Iterators.transform(entries, new Function<Map.Entry<K, V>, Map.Entry<K, V>>() { // from class: com.google.common.reflect.MutableTypeToInstanceMap.UnmodifiableEntry.2
                @Override // com.google.common.base.Function
                public /* bridge */ /* synthetic */ Object apply(Object obj) {
                    return apply((Map.Entry) ((Map.Entry) obj));
                }

                public Map.Entry<K, V> apply(Map.Entry<K, V> entry) {
                    return new UnmodifiableEntry(entry);
                }
            });
        }

        private UnmodifiableEntry(Map.Entry<K, V> delegate) {
            this.delegate = (Map.Entry) Preconditions.checkNotNull(delegate);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.ForwardingMapEntry, com.google.common.collect.ForwardingObject
        public Map.Entry<K, V> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.collect.ForwardingMapEntry, java.util.Map.Entry
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }
    }
}
