package com.google.common.collect;

import com.google.common.base.Objects;
import java.util.Collection;
import java.util.Iterator;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public abstract class ForwardingCollection<E> extends ForwardingObject implements Collection<E> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.collect.ForwardingObject
    public abstract Collection<E> delegate();

    @Override // java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<E> iterator() {
        return delegate().iterator();
    }

    @Override // java.util.Collection
    public int size() {
        return delegate().size();
    }

    @Override // java.util.Collection, java.util.Set
    public boolean removeAll(Collection<?> collection) {
        return delegate().removeAll(collection);
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return delegate().isEmpty();
    }

    @Override // java.util.Collection, java.util.Set
    public boolean contains(Object object) {
        return delegate().contains(object);
    }

    @Override // java.util.Collection, java.util.Queue
    public boolean add(E element) {
        return delegate().add(element);
    }

    @Override // java.util.Collection, java.util.Set
    public boolean remove(Object object) {
        return delegate().remove(object);
    }

    @Override // java.util.Collection, java.util.Set
    public boolean containsAll(Collection<?> collection) {
        return delegate().containsAll(collection);
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends E> collection) {
        return delegate().addAll(collection);
    }

    @Override // java.util.Collection, java.util.Set
    public boolean retainAll(Collection<?> collection) {
        return delegate().retainAll(collection);
    }

    @Override // java.util.Collection, java.util.Set
    public void clear() {
        delegate().clear();
    }

    @Override // java.util.Collection, java.util.Set
    public Object[] toArray() {
        return delegate().toArray();
    }

    @Override // java.util.Collection, java.util.Set
    public <T> T[] toArray(T[] array) {
        return (T[]) delegate().toArray(array);
    }

    protected boolean standardContains(@NullableDecl Object object) {
        return Iterators.contains(iterator(), object);
    }

    protected boolean standardContainsAll(Collection<?> collection) {
        return Collections2.containsAllImpl(this, collection);
    }

    protected boolean standardAddAll(Collection<? extends E> collection) {
        return Iterators.addAll(this, collection.iterator());
    }

    protected boolean standardRemove(@NullableDecl Object object) {
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            if (Objects.equal(iterator.next(), object)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    protected boolean standardRemoveAll(Collection<?> collection) {
        return Iterators.removeAll(iterator(), collection);
    }

    protected boolean standardRetainAll(Collection<?> collection) {
        return Iterators.retainAll(iterator(), collection);
    }

    protected void standardClear() {
        Iterators.clear(iterator());
    }

    protected boolean standardIsEmpty() {
        return !iterator().hasNext();
    }

    protected String standardToString() {
        return Collections2.toStringImpl(this);
    }

    protected Object[] standardToArray() {
        return toArray(new Object[size()]);
    }

    protected <T> T[] standardToArray(T[] array) {
        return (T[]) ObjectArrays.toArrayImpl(this, array);
    }
}
