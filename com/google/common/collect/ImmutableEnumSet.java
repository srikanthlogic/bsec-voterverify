package com.google.common.collect;

import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.lang.Enum;
import java.util.Collection;
import java.util.EnumSet;
/* loaded from: classes.dex */
final class ImmutableEnumSet<E extends Enum<E>> extends ImmutableSet<E> {
    private final transient EnumSet<E> delegate;
    @LazyInit
    private transient int hashCode;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ImmutableSet asImmutable(EnumSet set) {
        int size = set.size();
        if (size == 0) {
            return ImmutableSet.of();
        }
        if (size != 1) {
            return new ImmutableEnumSet(set);
        }
        return ImmutableSet.of(Iterables.getOnlyElement(set));
    }

    private ImmutableEnumSet(EnumSet<E> delegate) {
        this.delegate = delegate;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public boolean isPartialView() {
        return false;
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public UnmodifiableIterator<E> iterator() {
        return Iterators.unmodifiableIterator(this.delegate.iterator());
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.delegate.size();
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object object) {
        return this.delegate.contains(object);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean containsAll(Collection<?> collection) {
        if (collection instanceof ImmutableEnumSet) {
            collection = ((ImmutableEnumSet) collection).delegate;
        }
        return this.delegate.containsAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.lang.Object, java.util.Set
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof ImmutableEnumSet) {
            object = ((ImmutableEnumSet) object).delegate;
        }
        return this.delegate.equals(object);
    }

    @Override // com.google.common.collect.ImmutableSet
    boolean isHashCodeFast() {
        return true;
    }

    @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.lang.Object, java.util.Set
    public int hashCode() {
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int hashCode = this.delegate.hashCode();
        this.hashCode = hashCode;
        return hashCode;
    }

    @Override // java.util.AbstractCollection, java.lang.Object
    public String toString() {
        return this.delegate.toString();
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new EnumSerializedForm(this.delegate);
    }

    /* loaded from: classes.dex */
    private static class EnumSerializedForm<E extends Enum<E>> implements Serializable {
        private static final long serialVersionUID = 0;
        final EnumSet<E> delegate;

        EnumSerializedForm(EnumSet<E> delegate) {
            this.delegate = delegate;
        }

        Object readResolve() {
            return new ImmutableEnumSet(this.delegate.clone());
        }
    }
}
