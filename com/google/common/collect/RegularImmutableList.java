package com.google.common.collect;

import com.google.common.base.Preconditions;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class RegularImmutableList<E> extends ImmutableList<E> {
    static final ImmutableList<Object> EMPTY = new RegularImmutableList(new Object[0], 0);
    final transient Object[] array;
    private final transient int size;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RegularImmutableList(Object[] array, int size) {
        this.array = array;
        this.size = size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.size;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public boolean isPartialView() {
        return false;
    }

    @Override // com.google.common.collect.ImmutableCollection
    Object[] internalArray() {
        return this.array;
    }

    @Override // com.google.common.collect.ImmutableCollection
    int internalArrayStart() {
        return 0;
    }

    @Override // com.google.common.collect.ImmutableCollection
    int internalArrayEnd() {
        return this.size;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection
    public int copyIntoArray(Object[] dst, int dstOff) {
        System.arraycopy(this.array, 0, dst, dstOff, this.size);
        return this.size + dstOff;
    }

    @Override // java.util.List
    public E get(int index) {
        Preconditions.checkElementIndex(index, this.size);
        return (E) this.array[index];
    }
}
