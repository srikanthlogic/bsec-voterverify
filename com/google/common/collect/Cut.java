package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Booleans;
import java.io.Serializable;
import java.lang.Comparable;
import java.util.NoSuchElementException;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class Cut<C extends Comparable> implements Comparable<Cut<C>>, Serializable {
    private static final long serialVersionUID = 0;
    @NullableDecl
    final C endpoint;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void describeAsLowerBound(StringBuilder sb);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void describeAsUpperBound(StringBuilder sb);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract C greatestValueBelow(DiscreteDomain<C> discreteDomain);

    @Override // java.lang.Object
    public abstract int hashCode();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean isLessThan(C c);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract C leastValueAbove(DiscreteDomain<C> discreteDomain);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract BoundType typeAsLowerBound();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract BoundType typeAsUpperBound();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract Cut<C> withLowerBoundType(BoundType boundType, DiscreteDomain<C> discreteDomain);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract Cut<C> withUpperBoundType(BoundType boundType, DiscreteDomain<C> discreteDomain);

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(Object obj) {
        return compareTo((Cut) ((Cut) obj));
    }

    Cut(@NullableDecl C endpoint) {
        this.endpoint = endpoint;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Cut<C> canonical(DiscreteDomain<C> domain) {
        return this;
    }

    public int compareTo(Cut<C> that) {
        if (that == belowAll()) {
            return 1;
        }
        if (that == aboveAll()) {
            return -1;
        }
        int result = Range.compareOrThrow(this.endpoint, that.endpoint);
        if (result != 0) {
            return result;
        }
        return Booleans.compare(this instanceof AboveValue, that instanceof AboveValue);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public C endpoint() {
        return this.endpoint;
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        if (obj instanceof Cut) {
            try {
                if (compareTo((Cut) ((Cut) obj)) == 0) {
                    return true;
                }
                return false;
            } catch (ClassCastException e) {
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <C extends Comparable> Cut<C> belowAll() {
        return BelowAll.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class BelowAll extends Cut<Comparable<?>> {
        private static final BelowAll INSTANCE = new BelowAll();
        private static final long serialVersionUID = 0;

        private BelowAll() {
            super(null);
        }

        @Override // com.google.common.collect.Cut
        Comparable<?> endpoint() {
            throw new IllegalStateException("range unbounded on this side");
        }

        @Override // com.google.common.collect.Cut
        boolean isLessThan(Comparable<?> value) {
            return true;
        }

        @Override // com.google.common.collect.Cut
        BoundType typeAsLowerBound() {
            throw new IllegalStateException();
        }

        @Override // com.google.common.collect.Cut
        BoundType typeAsUpperBound() {
            throw new AssertionError("this statement should be unreachable");
        }

        @Override // com.google.common.collect.Cut
        Cut<Comparable<?>> withLowerBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain) {
            throw new IllegalStateException();
        }

        @Override // com.google.common.collect.Cut
        Cut<Comparable<?>> withUpperBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain) {
            throw new AssertionError("this statement should be unreachable");
        }

        @Override // com.google.common.collect.Cut
        void describeAsLowerBound(StringBuilder sb) {
            sb.append("(-∞");
        }

        @Override // com.google.common.collect.Cut
        void describeAsUpperBound(StringBuilder sb) {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.Cut
        Comparable<?> leastValueAbove(DiscreteDomain<Comparable<?>> domain) {
            return domain.minValue();
        }

        @Override // com.google.common.collect.Cut
        Comparable<?> greatestValueBelow(DiscreteDomain<Comparable<?>> domain) {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.Cut
        Cut<Comparable<?>> canonical(DiscreteDomain<Comparable<?>> domain) {
            try {
                return Cut.belowValue(domain.minValue());
            } catch (NoSuchElementException e) {
                return this;
            }
        }

        @Override // com.google.common.collect.Cut
        public int compareTo(Cut<Comparable<?>> o) {
            return o == this ? 0 : -1;
        }

        @Override // com.google.common.collect.Cut, java.lang.Object
        public int hashCode() {
            return System.identityHashCode(this);
        }

        @Override // java.lang.Object
        public String toString() {
            return "-∞";
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <C extends Comparable> Cut<C> aboveAll() {
        return AboveAll.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class AboveAll extends Cut<Comparable<?>> {
        private static final AboveAll INSTANCE = new AboveAll();
        private static final long serialVersionUID = 0;

        private AboveAll() {
            super(null);
        }

        @Override // com.google.common.collect.Cut
        Comparable<?> endpoint() {
            throw new IllegalStateException("range unbounded on this side");
        }

        @Override // com.google.common.collect.Cut
        boolean isLessThan(Comparable<?> value) {
            return false;
        }

        @Override // com.google.common.collect.Cut
        BoundType typeAsLowerBound() {
            throw new AssertionError("this statement should be unreachable");
        }

        @Override // com.google.common.collect.Cut
        BoundType typeAsUpperBound() {
            throw new IllegalStateException();
        }

        @Override // com.google.common.collect.Cut
        Cut<Comparable<?>> withLowerBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain) {
            throw new AssertionError("this statement should be unreachable");
        }

        @Override // com.google.common.collect.Cut
        Cut<Comparable<?>> withUpperBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain) {
            throw new IllegalStateException();
        }

        @Override // com.google.common.collect.Cut
        void describeAsLowerBound(StringBuilder sb) {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.Cut
        void describeAsUpperBound(StringBuilder sb) {
            sb.append("+∞)");
        }

        @Override // com.google.common.collect.Cut
        Comparable<?> leastValueAbove(DiscreteDomain<Comparable<?>> domain) {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.Cut
        Comparable<?> greatestValueBelow(DiscreteDomain<Comparable<?>> domain) {
            return domain.maxValue();
        }

        @Override // com.google.common.collect.Cut
        public int compareTo(Cut<Comparable<?>> o) {
            return o == this ? 0 : 1;
        }

        @Override // com.google.common.collect.Cut, java.lang.Object
        public int hashCode() {
            return System.identityHashCode(this);
        }

        @Override // java.lang.Object
        public String toString() {
            return "+∞";
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <C extends Comparable> Cut<C> belowValue(C endpoint) {
        return new BelowValue(endpoint);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class BelowValue<C extends Comparable> extends Cut<C> {
        private static final long serialVersionUID = 0;

        @Override // com.google.common.collect.Cut, java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(Object obj) {
            return Cut.super.compareTo((Cut) ((Cut) obj));
        }

        BelowValue(C endpoint) {
            super((Comparable) Preconditions.checkNotNull(endpoint));
        }

        @Override // com.google.common.collect.Cut
        boolean isLessThan(C value) {
            return Range.compareOrThrow(this.endpoint, value) <= 0;
        }

        @Override // com.google.common.collect.Cut
        BoundType typeAsLowerBound() {
            return BoundType.CLOSED;
        }

        @Override // com.google.common.collect.Cut
        BoundType typeAsUpperBound() {
            return BoundType.OPEN;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Cut
        Cut<C> withLowerBoundType(BoundType boundType, DiscreteDomain<C> domain) {
            int i = AnonymousClass1.$SwitchMap$com$google$common$collect$BoundType[boundType.ordinal()];
            if (i == 1) {
                return this;
            }
            if (i == 2) {
                C previous = domain.previous(this.endpoint);
                return previous == null ? Cut.belowAll() : new AboveValue(previous);
            }
            throw new AssertionError();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Cut
        Cut<C> withUpperBoundType(BoundType boundType, DiscreteDomain<C> domain) {
            int i = AnonymousClass1.$SwitchMap$com$google$common$collect$BoundType[boundType.ordinal()];
            if (i == 1) {
                C previous = domain.previous(this.endpoint);
                return previous == null ? Cut.aboveAll() : new AboveValue(previous);
            } else if (i == 2) {
                return this;
            } else {
                throw new AssertionError();
            }
        }

        @Override // com.google.common.collect.Cut
        void describeAsLowerBound(StringBuilder sb) {
            sb.append('[');
            sb.append(this.endpoint);
        }

        @Override // com.google.common.collect.Cut
        void describeAsUpperBound(StringBuilder sb) {
            sb.append(this.endpoint);
            sb.append(')');
        }

        @Override // com.google.common.collect.Cut
        C leastValueAbove(DiscreteDomain<C> domain) {
            return (C) this.endpoint;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Cut
        C greatestValueBelow(DiscreteDomain<C> domain) {
            return (C) domain.previous(this.endpoint);
        }

        @Override // com.google.common.collect.Cut, java.lang.Object
        public int hashCode() {
            return this.endpoint.hashCode();
        }

        @Override // java.lang.Object
        public String toString() {
            return "\\" + this.endpoint + "/";
        }
    }

    /* renamed from: com.google.common.collect.Cut$1  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$google$common$collect$BoundType = new int[BoundType.values().length];

        static {
            try {
                $SwitchMap$com$google$common$collect$BoundType[BoundType.CLOSED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$google$common$collect$BoundType[BoundType.OPEN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <C extends Comparable> Cut<C> aboveValue(C endpoint) {
        return new AboveValue(endpoint);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class AboveValue<C extends Comparable> extends Cut<C> {
        private static final long serialVersionUID = 0;

        @Override // com.google.common.collect.Cut, java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(Object obj) {
            return Cut.super.compareTo((Cut) ((Cut) obj));
        }

        AboveValue(C endpoint) {
            super((Comparable) Preconditions.checkNotNull(endpoint));
        }

        @Override // com.google.common.collect.Cut
        boolean isLessThan(C value) {
            return Range.compareOrThrow(this.endpoint, value) < 0;
        }

        @Override // com.google.common.collect.Cut
        BoundType typeAsLowerBound() {
            return BoundType.OPEN;
        }

        @Override // com.google.common.collect.Cut
        BoundType typeAsUpperBound() {
            return BoundType.CLOSED;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Cut
        Cut<C> withLowerBoundType(BoundType boundType, DiscreteDomain<C> domain) {
            int i = AnonymousClass1.$SwitchMap$com$google$common$collect$BoundType[boundType.ordinal()];
            if (i == 1) {
                C next = domain.next(this.endpoint);
                return next == null ? Cut.belowAll() : belowValue(next);
            } else if (i == 2) {
                return this;
            } else {
                throw new AssertionError();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Cut
        Cut<C> withUpperBoundType(BoundType boundType, DiscreteDomain<C> domain) {
            int i = AnonymousClass1.$SwitchMap$com$google$common$collect$BoundType[boundType.ordinal()];
            if (i == 1) {
                return this;
            }
            if (i == 2) {
                C next = domain.next(this.endpoint);
                return next == null ? Cut.aboveAll() : belowValue(next);
            }
            throw new AssertionError();
        }

        @Override // com.google.common.collect.Cut
        void describeAsLowerBound(StringBuilder sb) {
            sb.append('(');
            sb.append(this.endpoint);
        }

        @Override // com.google.common.collect.Cut
        void describeAsUpperBound(StringBuilder sb) {
            sb.append(this.endpoint);
            sb.append(']');
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Cut
        C leastValueAbove(DiscreteDomain<C> domain) {
            return (C) domain.next(this.endpoint);
        }

        @Override // com.google.common.collect.Cut
        C greatestValueBelow(DiscreteDomain<C> domain) {
            return (C) this.endpoint;
        }

        @Override // com.google.common.collect.Cut
        Cut<C> canonical(DiscreteDomain<C> domain) {
            C next = leastValueAbove(domain);
            return next != null ? belowValue(next) : Cut.aboveAll();
        }

        @Override // com.google.common.collect.Cut, java.lang.Object
        public int hashCode() {
            return ~this.endpoint.hashCode();
        }

        @Override // java.lang.Object
        public String toString() {
            return "/" + this.endpoint + "\\";
        }
    }
}
