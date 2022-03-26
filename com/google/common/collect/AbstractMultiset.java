package com.google.common.collect;

import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class AbstractMultiset<E> extends AbstractCollection<E> implements Multiset<E> {
    @NullableDecl
    @LazyInit
    private transient Set<E> elementSet;
    @NullableDecl
    @LazyInit
    private transient Set<Multiset.Entry<E>> entrySet;

    @Override // java.util.AbstractCollection, java.util.Collection
    public abstract void clear();

    abstract int distinctElements();

    abstract Iterator<E> elementIterator();

    abstract Iterator<Multiset.Entry<E>> entryIterator();

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return entrySet().isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public boolean contains(@NullableDecl Object element) {
        return count(element) > 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public final boolean add(@NullableDecl E element) {
        add(element, 1);
        return true;
    }

    @Override // com.google.common.collect.Multiset
    public int add(@NullableDecl E element, int occurrences) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public final boolean remove(@NullableDecl Object element) {
        return remove(element, 1) > 0;
    }

    @Override // com.google.common.collect.Multiset
    public int remove(@NullableDecl Object element, int occurrences) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.Multiset
    public int setCount(@NullableDecl E element, int count) {
        return Multisets.setCountImpl(this, element, count);
    }

    @Override // com.google.common.collect.Multiset
    public boolean setCount(@NullableDecl E element, int oldCount, int newCount) {
        return Multisets.setCountImpl(this, element, oldCount, newCount);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean addAll(Collection<? extends E> elementsToAdd) {
        return Multisets.addAllImpl(this, elementsToAdd);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public final boolean removeAll(Collection<?> elementsToRemove) {
        return Multisets.removeAllImpl(this, elementsToRemove);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public final boolean retainAll(Collection<?> elementsToRetain) {
        return Multisets.retainAllImpl(this, elementsToRetain);
    }

    @Override // com.google.common.collect.Multiset
    public Set<E> elementSet() {
        Set<E> result = this.elementSet;
        if (result != null) {
            return result;
        }
        Set<E> result2 = createElementSet();
        this.elementSet = result2;
        return result2;
    }

    Set<E> createElementSet() {
        return new ElementSet();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class ElementSet extends Multisets.ElementSet<E> {
        ElementSet() {
        }

        @Override // com.google.common.collect.Multisets.ElementSet
        Multiset<E> multiset() {
            return AbstractMultiset.this;
        }

        @Override // com.google.common.collect.Multisets.ElementSet, java.util.AbstractCollection, java.util.Collection, java.util.Set, java.lang.Iterable
        public Iterator<E> iterator() {
            return AbstractMultiset.this.elementIterator();
        }
    }

    @Override // com.google.common.collect.Multiset
    public Set<Multiset.Entry<E>> entrySet() {
        Set<Multiset.Entry<E>> result = this.entrySet;
        if (result != null) {
            return result;
        }
        Set<Multiset.Entry<E>> result2 = createEntrySet();
        this.entrySet = result2;
        return result2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class EntrySet extends Multisets.EntrySet<E> {
        /* JADX INFO: Access modifiers changed from: package-private */
        public EntrySet() {
        }

        @Override // com.google.common.collect.Multisets.EntrySet
        Multiset<E> multiset() {
            return AbstractMultiset.this;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set, java.lang.Iterable
        public Iterator<Multiset.Entry<E>> iterator() {
            return AbstractMultiset.this.entryIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return AbstractMultiset.this.distinctElements();
        }
    }

    Set<Multiset.Entry<E>> createEntrySet() {
        return new EntrySet();
    }

    @Override // java.util.Collection, java.lang.Object, com.google.common.collect.Multiset
    public final boolean equals(@NullableDecl Object object) {
        return Multisets.equalsImpl(this, object);
    }

    @Override // java.util.Collection, java.lang.Object, com.google.common.collect.Multiset
    public final int hashCode() {
        return entrySet().hashCode();
    }

    @Override // java.util.AbstractCollection, java.lang.Object, com.google.common.collect.Multiset
    public final String toString() {
        return entrySet().toString();
    }
}
