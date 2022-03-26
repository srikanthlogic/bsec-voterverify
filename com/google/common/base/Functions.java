package com.google.common.base;

import java.io.Serializable;
import java.util.Map;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public final class Functions {
    private Functions() {
    }

    public static Function<Object, String> toStringFunction() {
        return ToStringFunction.INSTANCE;
    }

    /* loaded from: classes.dex */
    private enum ToStringFunction implements Function<Object, String> {
        INSTANCE;

        @Override // com.google.common.base.Function
        public String apply(Object o) {
            Preconditions.checkNotNull(o);
            return o.toString();
        }

        @Override // java.lang.Enum, java.lang.Object
        public String toString() {
            return "Functions.toStringFunction()";
        }
    }

    public static <E> Function<E, E> identity() {
        return IdentityFunction.INSTANCE;
    }

    /* loaded from: classes.dex */
    private enum IdentityFunction implements Function<Object, Object> {
        INSTANCE;

        @Override // com.google.common.base.Function
        @NullableDecl
        public Object apply(@NullableDecl Object o) {
            return o;
        }

        @Override // java.lang.Enum, java.lang.Object
        public String toString() {
            return "Functions.identity()";
        }
    }

    public static <K, V> Function<K, V> forMap(Map<K, V> map) {
        return new FunctionForMapNoDefault(map);
    }

    public static <K, V> Function<K, V> forMap(Map<K, ? extends V> map, @NullableDecl V defaultValue) {
        return new ForMapWithDefault(map, defaultValue);
    }

    /* loaded from: classes.dex */
    private static class FunctionForMapNoDefault<K, V> implements Function<K, V>, Serializable {
        private static final long serialVersionUID = 0;
        final Map<K, V> map;

        FunctionForMapNoDefault(Map<K, V> map) {
            this.map = (Map) Preconditions.checkNotNull(map);
        }

        @Override // com.google.common.base.Function
        public V apply(@NullableDecl K key) {
            V result = this.map.get(key);
            Preconditions.checkArgument(result != null || this.map.containsKey(key), "Key '%s' not present in map", key);
            return result;
        }

        @Override // com.google.common.base.Function, java.lang.Object
        public boolean equals(@NullableDecl Object o) {
            if (o instanceof FunctionForMapNoDefault) {
                return this.map.equals(((FunctionForMapNoDefault) o).map);
            }
            return false;
        }

        @Override // java.lang.Object
        public int hashCode() {
            return this.map.hashCode();
        }

        @Override // java.lang.Object
        public String toString() {
            return "Functions.forMap(" + this.map + ")";
        }
    }

    /* loaded from: classes.dex */
    private static class ForMapWithDefault<K, V> implements Function<K, V>, Serializable {
        private static final long serialVersionUID = 0;
        @NullableDecl
        final V defaultValue;
        final Map<K, ? extends V> map;

        ForMapWithDefault(Map<K, ? extends V> map, @NullableDecl V defaultValue) {
            this.map = (Map) Preconditions.checkNotNull(map);
            this.defaultValue = defaultValue;
        }

        @Override // com.google.common.base.Function
        public V apply(@NullableDecl K key) {
            V result = (V) this.map.get(key);
            return (result != null || this.map.containsKey(key)) ? result : this.defaultValue;
        }

        @Override // com.google.common.base.Function, java.lang.Object
        public boolean equals(@NullableDecl Object o) {
            if (!(o instanceof ForMapWithDefault)) {
                return false;
            }
            ForMapWithDefault<?, ?> that = (ForMapWithDefault) o;
            if (!this.map.equals(that.map) || !Objects.equal(this.defaultValue, that.defaultValue)) {
                return false;
            }
            return true;
        }

        @Override // java.lang.Object
        public int hashCode() {
            return Objects.hashCode(this.map, this.defaultValue);
        }

        @Override // java.lang.Object
        public String toString() {
            return "Functions.forMap(" + this.map + ", defaultValue=" + this.defaultValue + ")";
        }
    }

    public static <A, B, C> Function<A, C> compose(Function<B, C> g, Function<A, ? extends B> f) {
        return new FunctionComposition(g, f);
    }

    /* loaded from: classes.dex */
    private static class FunctionComposition<A, B, C> implements Function<A, C>, Serializable {
        private static final long serialVersionUID = 0;
        private final Function<A, ? extends B> f;
        private final Function<B, C> g;

        public FunctionComposition(Function<B, C> g, Function<A, ? extends B> f) {
            this.g = (Function) Preconditions.checkNotNull(g);
            this.f = (Function) Preconditions.checkNotNull(f);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.base.Function
        public C apply(@NullableDecl A a2) {
            return (C) this.g.apply(this.f.apply(a2));
        }

        @Override // com.google.common.base.Function, java.lang.Object
        public boolean equals(@NullableDecl Object obj) {
            if (!(obj instanceof FunctionComposition)) {
                return false;
            }
            FunctionComposition<?, ?, ?> that = (FunctionComposition) obj;
            if (!this.f.equals(that.f) || !this.g.equals(that.g)) {
                return false;
            }
            return true;
        }

        @Override // java.lang.Object
        public int hashCode() {
            return this.f.hashCode() ^ this.g.hashCode();
        }

        @Override // java.lang.Object
        public String toString() {
            return this.g + "(" + this.f + ")";
        }
    }

    public static <T> Function<T, Boolean> forPredicate(Predicate<T> predicate) {
        return new PredicateFunction(predicate);
    }

    /* loaded from: classes.dex */
    private static class PredicateFunction<T> implements Function<T, Boolean>, Serializable {
        private static final long serialVersionUID = 0;
        private final Predicate<T> predicate;

        private PredicateFunction(Predicate<T> predicate) {
            this.predicate = (Predicate) Preconditions.checkNotNull(predicate);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.common.base.Function
        public Boolean apply(@NullableDecl T t) {
            return Boolean.valueOf(this.predicate.apply(t));
        }

        @Override // com.google.common.base.Function, java.lang.Object
        public boolean equals(@NullableDecl Object obj) {
            if (obj instanceof PredicateFunction) {
                return this.predicate.equals(((PredicateFunction) obj).predicate);
            }
            return false;
        }

        @Override // java.lang.Object
        public int hashCode() {
            return this.predicate.hashCode();
        }

        @Override // java.lang.Object
        public String toString() {
            return "Functions.forPredicate(" + this.predicate + ")";
        }
    }

    public static <E> Function<Object, E> constant(@NullableDecl E value) {
        return new ConstantFunction(value);
    }

    /* loaded from: classes.dex */
    private static class ConstantFunction<E> implements Function<Object, E>, Serializable {
        private static final long serialVersionUID = 0;
        @NullableDecl
        private final E value;

        public ConstantFunction(@NullableDecl E value) {
            this.value = value;
        }

        @Override // com.google.common.base.Function
        public E apply(@NullableDecl Object from) {
            return this.value;
        }

        @Override // com.google.common.base.Function, java.lang.Object
        public boolean equals(@NullableDecl Object obj) {
            if (obj instanceof ConstantFunction) {
                return Objects.equal(this.value, ((ConstantFunction) obj).value);
            }
            return false;
        }

        @Override // java.lang.Object
        public int hashCode() {
            E e = this.value;
            if (e == null) {
                return 0;
            }
            return e.hashCode();
        }

        @Override // java.lang.Object
        public String toString() {
            return "Functions.constant(" + this.value + ")";
        }
    }

    public static <T> Function<Object, T> forSupplier(Supplier<T> supplier) {
        return new SupplierFunction(supplier);
    }

    /* loaded from: classes.dex */
    private static class SupplierFunction<T> implements Function<Object, T>, Serializable {
        private static final long serialVersionUID = 0;
        private final Supplier<T> supplier;

        private SupplierFunction(Supplier<T> supplier) {
            this.supplier = (Supplier) Preconditions.checkNotNull(supplier);
        }

        @Override // com.google.common.base.Function
        public T apply(@NullableDecl Object input) {
            return this.supplier.get();
        }

        @Override // com.google.common.base.Function, java.lang.Object
        public boolean equals(@NullableDecl Object obj) {
            if (obj instanceof SupplierFunction) {
                return this.supplier.equals(((SupplierFunction) obj).supplier);
            }
            return false;
        }

        @Override // java.lang.Object
        public int hashCode() {
            return this.supplier.hashCode();
        }

        @Override // java.lang.Object
        public String toString() {
            return "Functions.forSupplier(" + this.supplier + ")";
        }
    }
}
