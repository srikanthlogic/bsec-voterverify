package com.google.common.base;

import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Iterator;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public abstract class Converter<A, B> implements Function<A, B> {
    private final boolean handleNullAutomatically;
    @NullableDecl
    @LazyInit
    private transient Converter<B, A> reverse;

    protected abstract A doBackward(B b);

    protected abstract B doForward(A a2);

    /* JADX INFO: Access modifiers changed from: protected */
    public Converter() {
        this(true);
    }

    Converter(boolean handleNullAutomatically) {
        this.handleNullAutomatically = handleNullAutomatically;
    }

    @NullableDecl
    public final B convert(@NullableDecl A a2) {
        return correctedDoForward(a2);
    }

    @NullableDecl
    B correctedDoForward(@NullableDecl A a2) {
        if (!this.handleNullAutomatically) {
            return doForward(a2);
        }
        if (a2 == null) {
            return null;
        }
        return (B) Preconditions.checkNotNull(doForward(a2));
    }

    @NullableDecl
    A correctedDoBackward(@NullableDecl B b) {
        if (!this.handleNullAutomatically) {
            return doBackward(b);
        }
        if (b == null) {
            return null;
        }
        return (A) Preconditions.checkNotNull(doBackward(b));
    }

    public Iterable<B> convertAll(final Iterable<? extends A> fromIterable) {
        Preconditions.checkNotNull(fromIterable, "fromIterable");
        return new Iterable<B>() { // from class: com.google.common.base.Converter.1
            @Override // java.lang.Iterable
            public Iterator<B> iterator() {
                return new Iterator<B>() { // from class: com.google.common.base.Converter.1.1
                    private final Iterator<? extends A> fromIterator;

                    {
                        this.fromIterator = fromIterable.iterator();
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.fromIterator.hasNext();
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.util.Iterator
                    public B next() {
                        return (B) Converter.this.convert(this.fromIterator.next());
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        this.fromIterator.remove();
                    }
                };
            }
        };
    }

    public Converter<B, A> reverse() {
        Converter<B, A> result = this.reverse;
        if (result != null) {
            return result;
        }
        ReverseConverter reverseConverter = new ReverseConverter(this);
        this.reverse = reverseConverter;
        return reverseConverter;
    }

    /* loaded from: classes.dex */
    private static final class ReverseConverter<A, B> extends Converter<B, A> implements Serializable {
        private static final long serialVersionUID = 0;
        final Converter<A, B> original;

        ReverseConverter(Converter<A, B> original) {
            this.original = original;
        }

        @Override // com.google.common.base.Converter
        protected A doForward(B b) {
            throw new AssertionError();
        }

        @Override // com.google.common.base.Converter
        protected B doBackward(A a2) {
            throw new AssertionError();
        }

        @Override // com.google.common.base.Converter
        @NullableDecl
        A correctedDoForward(@NullableDecl B b) {
            return this.original.correctedDoBackward(b);
        }

        @Override // com.google.common.base.Converter
        @NullableDecl
        B correctedDoBackward(@NullableDecl A a2) {
            return this.original.correctedDoForward(a2);
        }

        @Override // com.google.common.base.Converter
        public Converter<A, B> reverse() {
            return this.original;
        }

        @Override // com.google.common.base.Converter, com.google.common.base.Function, java.lang.Object
        public boolean equals(@NullableDecl Object object) {
            if (object instanceof ReverseConverter) {
                return this.original.equals(((ReverseConverter) object).original);
            }
            return false;
        }

        @Override // java.lang.Object
        public int hashCode() {
            return ~this.original.hashCode();
        }

        @Override // java.lang.Object
        public String toString() {
            return this.original + ".reverse()";
        }
    }

    public final <C> Converter<A, C> andThen(Converter<B, C> secondConverter) {
        return doAndThen(secondConverter);
    }

    <C> Converter<A, C> doAndThen(Converter<B, C> secondConverter) {
        return new ConverterComposition(this, (Converter) Preconditions.checkNotNull(secondConverter));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ConverterComposition<A, B, C> extends Converter<A, C> implements Serializable {
        private static final long serialVersionUID = 0;
        final Converter<A, B> first;
        final Converter<B, C> second;

        ConverterComposition(Converter<A, B> first, Converter<B, C> second) {
            this.first = first;
            this.second = second;
        }

        @Override // com.google.common.base.Converter
        protected C doForward(A a2) {
            throw new AssertionError();
        }

        @Override // com.google.common.base.Converter
        protected A doBackward(C c) {
            throw new AssertionError();
        }

        @Override // com.google.common.base.Converter
        @NullableDecl
        C correctedDoForward(@NullableDecl A a2) {
            return this.second.correctedDoForward(this.first.correctedDoForward(a2));
        }

        @Override // com.google.common.base.Converter
        @NullableDecl
        A correctedDoBackward(@NullableDecl C c) {
            return this.first.correctedDoBackward(this.second.correctedDoBackward(c));
        }

        @Override // com.google.common.base.Converter, com.google.common.base.Function, java.lang.Object
        public boolean equals(@NullableDecl Object object) {
            if (!(object instanceof ConverterComposition)) {
                return false;
            }
            ConverterComposition<?, ?, ?> that = (ConverterComposition) object;
            if (!this.first.equals(that.first) || !this.second.equals(that.second)) {
                return false;
            }
            return true;
        }

        @Override // java.lang.Object
        public int hashCode() {
            return (this.first.hashCode() * 31) + this.second.hashCode();
        }

        @Override // java.lang.Object
        public String toString() {
            return this.first + ".andThen(" + this.second + ")";
        }
    }

    @Override // com.google.common.base.Function
    @NullableDecl
    @Deprecated
    public final B apply(@NullableDecl A a2) {
        return convert(a2);
    }

    @Override // com.google.common.base.Function, java.lang.Object
    public boolean equals(@NullableDecl Object object) {
        return super.equals(object);
    }

    public static <A, B> Converter<A, B> from(Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction) {
        return new FunctionBasedConverter(forwardFunction, backwardFunction);
    }

    /* loaded from: classes.dex */
    private static final class FunctionBasedConverter<A, B> extends Converter<A, B> implements Serializable {
        private final Function<? super B, ? extends A> backwardFunction;
        private final Function<? super A, ? extends B> forwardFunction;

        private FunctionBasedConverter(Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction) {
            this.forwardFunction = (Function) Preconditions.checkNotNull(forwardFunction);
            this.backwardFunction = (Function) Preconditions.checkNotNull(backwardFunction);
        }

        @Override // com.google.common.base.Converter
        protected B doForward(A a2) {
            return (B) this.forwardFunction.apply(a2);
        }

        @Override // com.google.common.base.Converter
        protected A doBackward(B b) {
            return (A) this.backwardFunction.apply(b);
        }

        @Override // com.google.common.base.Converter, com.google.common.base.Function, java.lang.Object
        public boolean equals(@NullableDecl Object object) {
            if (!(object instanceof FunctionBasedConverter)) {
                return false;
            }
            FunctionBasedConverter<?, ?> that = (FunctionBasedConverter) object;
            if (!this.forwardFunction.equals(that.forwardFunction) || !this.backwardFunction.equals(that.backwardFunction)) {
                return false;
            }
            return true;
        }

        @Override // java.lang.Object
        public int hashCode() {
            return (this.forwardFunction.hashCode() * 31) + this.backwardFunction.hashCode();
        }

        @Override // java.lang.Object
        public String toString() {
            return "Converter.from(" + this.forwardFunction + ", " + this.backwardFunction + ")";
        }
    }

    public static <T> Converter<T, T> identity() {
        return IdentityConverter.INSTANCE;
    }

    /* loaded from: classes.dex */
    private static final class IdentityConverter<T> extends Converter<T, T> implements Serializable {
        static final IdentityConverter<?> INSTANCE = new IdentityConverter<>();
        private static final long serialVersionUID = 0;

        private IdentityConverter() {
        }

        @Override // com.google.common.base.Converter
        protected T doForward(T t) {
            return t;
        }

        @Override // com.google.common.base.Converter
        protected T doBackward(T t) {
            return t;
        }

        @Override // com.google.common.base.Converter
        public IdentityConverter<T> reverse() {
            return this;
        }

        @Override // com.google.common.base.Converter
        <S> Converter<T, S> doAndThen(Converter<T, S> otherConverter) {
            return (Converter) Preconditions.checkNotNull(otherConverter, "otherConverter");
        }

        @Override // java.lang.Object
        public String toString() {
            return "Converter.identity()";
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }
}
