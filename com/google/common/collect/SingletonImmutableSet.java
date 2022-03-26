package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SingletonImmutableSet<E> extends ImmutableSet<E> {
    @LazyInit
    private transient int cachedHashCode;
    final transient E element;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SingletonImmutableSet(E element) {
        this.element = (E) Preconditions.checkNotNull(element);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SingletonImmutableSet(E element, int hashCode) {
        this.element = element;
        this.cachedHashCode = hashCode;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return 1;
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object target) {
        return this.element.equals(target);
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public UnmodifiableIterator<E> iterator() {
        return Iterators.singletonIterator(this.element);
    }

    @Override // com.google.common.collect.ImmutableSet
    ImmutableList<E> createAsList() {
        return ImmutableList.of((Object) this.element);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public boolean isPartialView() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public int copyIntoArray(Object[] dst, int offset) {
        dst[offset] = this.element;
        return offset + 1;
    }

    @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.lang.Object, java.util.Set
    public final int hashCode() {
        int code = this.cachedHashCode;
        if (code != 0) {
            return code;
        }
        int code2 = this.element.hashCode();
        this.cachedHashCode = code2;
        return code2;
    }

    @Override // com.google.common.collect.ImmutableSet
    boolean isHashCodeFast() {
        return this.cachedHashCode != 0;
    }

    @Override // java.util.AbstractCollection, java.lang.Object
    public String toString() {
        return '[' + this.element.toString() + ']';
    }
}
