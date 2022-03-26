package com.google.common.base;

import com.google.errorprone.annotations.DoNotMock;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@DoNotMock("Use Optional.of(value) or Optional.absent()")
/* loaded from: classes.dex */
public abstract class Optional<T> implements Serializable {
    private static final long serialVersionUID;

    public abstract Set<T> asSet();

    @Override // java.lang.Object
    public abstract boolean equals(@NullableDecl Object obj);

    public abstract T get();

    @Override // java.lang.Object
    public abstract int hashCode();

    public abstract boolean isPresent();

    public abstract Optional<T> or(Optional<? extends T> optional);

    public abstract T or(Supplier<? extends T> supplier);

    public abstract T or(T t);

    @NullableDecl
    public abstract T orNull();

    @Override // java.lang.Object
    public abstract String toString();

    public abstract <V> Optional<V> transform(Function<? super T, V> function);

    public static <T> Optional<T> absent() {
        return Absent.withType();
    }

    public static <T> Optional<T> of(T reference) {
        return new Present(Preconditions.checkNotNull(reference));
    }

    public static <T> Optional<T> fromNullable(@NullableDecl T nullableReference) {
        return nullableReference == null ? absent() : new Present(nullableReference);
    }

    public static <T> Iterable<T> presentInstances(final Iterable<? extends Optional<? extends T>> optionals) {
        Preconditions.checkNotNull(optionals);
        return new Iterable<T>() { // from class: com.google.common.base.Optional.1
            @Override // java.lang.Iterable
            public Iterator<T> iterator() {
                return new AbstractIterator<T>() { // from class: com.google.common.base.Optional.1.1
                    private final Iterator<? extends Optional<? extends T>> iterator;

                    {
                        this.iterator = (Iterator) Preconditions.checkNotNull(optionals.iterator());
                    }

                    @Override // com.google.common.base.AbstractIterator
                    protected T computeNext() {
                        while (this.iterator.hasNext()) {
                            Optional<? extends T> optional = (Optional) this.iterator.next();
                            if (optional.isPresent()) {
                                return (T) optional.get();
                            }
                        }
                        return endOfData();
                    }
                };
            }
        };
    }
}
