package com.google.common.collect;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public final class Platform {
    public static <K, V> Map<K, V> newHashMapWithExpectedSize(int expectedSize) {
        return CompactHashMap.createWithExpectedSize(expectedSize);
    }

    public static <K, V> Map<K, V> newLinkedHashMapWithExpectedSize(int expectedSize) {
        return CompactLinkedHashMap.createWithExpectedSize(expectedSize);
    }

    public static <E> Set<E> newHashSetWithExpectedSize(int expectedSize) {
        return CompactHashSet.createWithExpectedSize(expectedSize);
    }

    public static <E> Set<E> newLinkedHashSetWithExpectedSize(int expectedSize) {
        return CompactLinkedHashSet.createWithExpectedSize(expectedSize);
    }

    public static <K, V> Map<K, V> preservesInsertionOrderOnPutsMap() {
        return CompactHashMap.create();
    }

    public static <E> Set<E> preservesInsertionOrderOnAddsSet() {
        return CompactHashSet.create();
    }

    public static <T> T[] newArray(T[] reference, int length) {
        return (T[]) ((Object[]) Array.newInstance(reference.getClass().getComponentType(), length));
    }

    public static <T> T[] copy(Object[] source, int from, int to, T[] arrayOfType) {
        return (T[]) Arrays.copyOfRange(source, from, to, arrayOfType.getClass());
    }

    public static MapMaker tryWeakKeys(MapMaker mapMaker) {
        return mapMaker.weakKeys();
    }

    static int reduceIterationsIfGwt(int iterations) {
        return iterations;
    }

    static int reduceExponentIfGwt(int exponent) {
        return exponent;
    }

    static void checkGwtRpcEnabled() {
    }

    private Platform() {
    }
}
