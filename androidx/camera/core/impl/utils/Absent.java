package androidx.camera.core.impl.utils;

import androidx.core.util.Preconditions;
import androidx.core.util.Supplier;
/* loaded from: classes.dex */
public final class Absent<T> extends Optional<T> {
    static final Absent<Object> sInstance = new Absent<>();
    private static final long serialVersionUID;

    public static <T> Optional<T> withType() {
        return sInstance;
    }

    private Absent() {
    }

    @Override // androidx.camera.core.impl.utils.Optional
    public boolean isPresent() {
        return false;
    }

    @Override // androidx.camera.core.impl.utils.Optional
    public T get() {
        throw new IllegalStateException("Optional.get() cannot be called on an absent value");
    }

    @Override // androidx.camera.core.impl.utils.Optional
    public T or(T defaultValue) {
        return (T) Preconditions.checkNotNull(defaultValue, "use Optional.orNull() instead of Optional.or(null)");
    }

    @Override // androidx.camera.core.impl.utils.Optional
    public Optional<T> or(Optional<? extends T> secondChoice) {
        return (Optional) Preconditions.checkNotNull(secondChoice);
    }

    @Override // androidx.camera.core.impl.utils.Optional
    public T or(Supplier<? extends T> supplier) {
        return (T) Preconditions.checkNotNull(supplier.get(), "use Optional.orNull() instead of a Supplier that returns null");
    }

    @Override // androidx.camera.core.impl.utils.Optional
    public T orNull() {
        return null;
    }

    @Override // androidx.camera.core.impl.utils.Optional, java.lang.Object
    public boolean equals(Object object) {
        return object == this;
    }

    @Override // androidx.camera.core.impl.utils.Optional, java.lang.Object
    public int hashCode() {
        return 2040732332;
    }

    @Override // androidx.camera.core.impl.utils.Optional, java.lang.Object
    public String toString() {
        return "Optional.absent()";
    }

    private Object readResolve() {
        return sInstance;
    }
}
