package com.google.common.collect;

import com.google.errorprone.annotations.DoNotMock;
import java.lang.Comparable;
import java.util.Map;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@DoNotMock("Use ImmutableRangeMap or TreeRangeMap")
/* loaded from: classes.dex */
public interface RangeMap<K extends Comparable, V> {
    Map<Range<K>, V> asDescendingMapOfRanges();

    Map<Range<K>, V> asMapOfRanges();

    void clear();

    @Override // java.lang.Object
    boolean equals(@NullableDecl Object obj);

    @NullableDecl
    V get(K k);

    @NullableDecl
    Map.Entry<Range<K>, V> getEntry(K k);

    @Override // java.lang.Object
    int hashCode();

    void put(Range<K> range, V v);

    void putAll(RangeMap<K, V> rangeMap);

    void putCoalescing(Range<K> range, V v);

    void remove(Range<K> range);

    Range<K> span();

    RangeMap<K, V> subRangeMap(Range<K> range);

    @Override // java.lang.Object
    String toString();
}