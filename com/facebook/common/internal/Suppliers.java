package com.facebook.common.internal;
/* loaded from: classes.dex */
public class Suppliers {
    public static final Supplier<Boolean> BOOLEAN_TRUE = new Supplier<Boolean>() { // from class: com.facebook.common.internal.Suppliers.2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.facebook.common.internal.Supplier
        public Boolean get() {
            return true;
        }
    };
    public static final Supplier<Boolean> BOOLEAN_FALSE = new Supplier<Boolean>() { // from class: com.facebook.common.internal.Suppliers.3
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.facebook.common.internal.Supplier
        public Boolean get() {
            return false;
        }
    };

    public static <T> Supplier<T> of(final T instance) {
        return new Supplier<T>() { // from class: com.facebook.common.internal.Suppliers.1
            /* JADX WARN: Type inference failed for: r0v0, types: [T, java.lang.Object] */
            @Override // com.facebook.common.internal.Supplier
            public T get() {
                return instance;
            }
        };
    }
}
