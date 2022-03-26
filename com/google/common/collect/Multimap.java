package com.google.common.collect;

import com.google.errorprone.annotations.DoNotMock;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@DoNotMock("Use ImmutableMultimap, HashMultimap, or another implementation")
/* loaded from: classes.dex */
public interface Multimap<K, V> {
    @Override // com.google.common.collect.ListMultimap
    Map<K, Collection<V>> asMap();

    void clear();

    boolean containsEntry(@NullableDecl Object obj, @NullableDecl Object obj2);

    boolean containsKey(@NullableDecl Object obj);

    boolean containsValue(@NullableDecl Object obj);

    Collection<Map.Entry<K, V>> entries();

    @Override // java.lang.Object, com.google.common.collect.ListMultimap
    boolean equals(@NullableDecl Object obj);

    @Override // com.google.common.collect.ListMultimap
    Collection<V> get(@NullableDecl K k);

    int hashCode();

    boolean isEmpty();

    Set<K> keySet();

    Multiset<K> keys();

    boolean put(@NullableDecl K k, @NullableDecl V v);

    boolean putAll(Multimap<? extends K, ? extends V> multimap);

    boolean putAll(@NullableDecl K k, Iterable<? extends V> iterable);

    boolean remove(@NullableDecl Object obj, @NullableDecl Object obj2);

    @Override // com.google.common.collect.ListMultimap
    Collection<V> removeAll(@NullableDecl Object obj);

    @Override // com.google.common.collect.ListMultimap
    Collection<V> replaceValues(@NullableDecl K k, Iterable<? extends V> iterable);

    int size();

    Collection<V> values();
}
