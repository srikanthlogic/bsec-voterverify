package com.google.common.base;

import java.io.Serializable;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public abstract class Equivalence<T> {
    protected abstract boolean doEquivalent(T t, T t2);

    protected abstract int doHash(T t);

    public final boolean equivalent(@NullableDecl T a2, @NullableDecl T b) {
        if (a2 == b) {
            return true;
        }
        if (a2 == null || b == null) {
            return false;
        }
        return doEquivalent(a2, b);
    }

    public final int hash(@NullableDecl T t) {
        if (t == null) {
            return 0;
        }
        return doHash(t);
    }

    public final <F> Equivalence<F> onResultOf(Function<F, ? extends T> function) {
        return new FunctionalEquivalence(function, this);
    }

    public final <S extends T> Wrapper<S> wrap(@NullableDecl S reference) {
        return new Wrapper<>(reference);
    }

    /* loaded from: classes.dex */
    public static final class Wrapper<T> implements Serializable {
        private static final long serialVersionUID;
        private final Equivalence<? super T> equivalence;
        @NullableDecl
        private final T reference;

        private Wrapper(Equivalence<? super T> equivalence, @NullableDecl T reference) {
            this.equivalence = (Equivalence) Preconditions.checkNotNull(equivalence);
            this.reference = reference;
        }

        @NullableDecl
        public T get() {
            return this.reference;
        }

        @Override // java.lang.Object
        public boolean equals(@NullableDecl Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Wrapper)) {
                return false;
            }
            Wrapper<?> that = (Wrapper) obj;
            if (this.equivalence.equals(that.equivalence)) {
                return this.equivalence.equivalent((T) this.reference, (T) that.reference);
            }
            return false;
        }

        @Override // java.lang.Object
        public int hashCode() {
            return this.equivalence.hash((T) this.reference);
        }

        @Override // java.lang.Object
        public String toString() {
            return this.equivalence + ".wrap(" + this.reference + ")";
        }
    }

    public final <S extends T> Equivalence<Iterable<S>> pairwise() {
        return new PairwiseEquivalence(this);
    }

    public final Predicate<T> equivalentTo(@NullableDecl T target) {
        return new EquivalentToPredicate(this, target);
    }

    /* loaded from: classes.dex */
    private static final class EquivalentToPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID;
        private final Equivalence<T> equivalence;
        @NullableDecl
        private final T target;

        EquivalentToPredicate(Equivalence<T> equivalence, @NullableDecl T target) {
            this.equivalence = (Equivalence) Preconditions.checkNotNull(equivalence);
            this.target = target;
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@NullableDecl T input) {
            return this.equivalence.equivalent(input, this.target);
        }

        @Override // com.google.common.base.Predicate, java.lang.Object
        public boolean equals(@NullableDecl Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof EquivalentToPredicate)) {
                return false;
            }
            EquivalentToPredicate<?> that = (EquivalentToPredicate) obj;
            if (!this.equivalence.equals(that.equivalence) || !Objects.equal(this.target, that.target)) {
                return false;
            }
            return true;
        }

        @Override // java.lang.Object
        public int hashCode() {
            return Objects.hashCode(this.equivalence, this.target);
        }

        @Override // java.lang.Object
        public String toString() {
            return this.equivalence + ".equivalentTo(" + this.target + ")";
        }
    }

    public static Equivalence<Object> equals() {
        return Equals.INSTANCE;
    }

    public static Equivalence<Object> identity() {
        return Identity.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class Equals extends Equivalence<Object> implements Serializable {
        static final Equals INSTANCE = new Equals();
        private static final long serialVersionUID;

        Equals() {
        }

        @Override // com.google.common.base.Equivalence
        protected boolean doEquivalent(Object a2, Object b) {
            return a2.equals(b);
        }

        @Override // com.google.common.base.Equivalence
        protected int doHash(Object o) {
            return o.hashCode();
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class Identity extends Equivalence<Object> implements Serializable {
        static final Identity INSTANCE = new Identity();
        private static final long serialVersionUID;

        Identity() {
        }

        @Override // com.google.common.base.Equivalence
        protected boolean doEquivalent(Object a2, Object b) {
            return false;
        }

        @Override // com.google.common.base.Equivalence
        protected int doHash(Object o) {
            return System.identityHashCode(o);
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }
}
