package androidx.camera.core.impl.utils;

import androidx.core.util.Preconditions;
import androidx.core.util.Supplier;
import java.io.Serializable;
/* loaded from: classes.dex */
public abstract class Optional<T> implements Serializable {
    private static final long serialVersionUID;

    @Override // java.lang.Object
    public abstract boolean equals(Object obj);

    public abstract T get();

    @Override // java.lang.Object
    public abstract int hashCode();

    public abstract boolean isPresent();

    public abstract Optional<T> or(Optional<? extends T> optional);

    public abstract T or(Supplier<? extends T> supplier);

    public abstract T or(T t);

    public abstract T orNull();

    @Override // java.lang.Object
    public abstract String toString();

    public static <T> Optional<T> absent() {
        return Absent.withType();
    }

    public static <T> Optional<T> of(T reference) {
        return new Present(Preconditions.checkNotNull(reference));
    }

    public static <T> Optional<T> fromNullable(T nullableReference) {
        return nullableReference == null ? absent() : new Present(nullableReference);
    }
}
