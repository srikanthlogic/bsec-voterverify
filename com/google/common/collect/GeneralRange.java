package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Comparator;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class GeneralRange<T> implements Serializable {
    private final Comparator<? super T> comparator;
    private final boolean hasLowerBound;
    private final boolean hasUpperBound;
    private final BoundType lowerBoundType;
    @NullableDecl
    private final T lowerEndpoint;
    @NullableDecl
    private transient GeneralRange<T> reverse;
    private final BoundType upperBoundType;
    @NullableDecl
    private final T upperEndpoint;

    static <T extends Comparable> GeneralRange<T> from(Range<T> range) {
        T upperEndpoint = null;
        T lowerEndpoint = range.hasLowerBound() ? range.lowerEndpoint() : null;
        BoundType lowerBoundType = range.hasLowerBound() ? range.lowerBoundType() : BoundType.OPEN;
        if (range.hasUpperBound()) {
            upperEndpoint = range.upperEndpoint();
        }
        return new GeneralRange<>(Ordering.natural(), range.hasLowerBound(), lowerEndpoint, lowerBoundType, range.hasUpperBound(), upperEndpoint, range.hasUpperBound() ? range.upperBoundType() : BoundType.OPEN);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> GeneralRange<T> all(Comparator<? super T> comparator) {
        return new GeneralRange<>(comparator, false, null, BoundType.OPEN, false, null, BoundType.OPEN);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> GeneralRange<T> downTo(Comparator<? super T> comparator, @NullableDecl T endpoint, BoundType boundType) {
        return new GeneralRange<>(comparator, true, endpoint, boundType, false, null, BoundType.OPEN);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> GeneralRange<T> upTo(Comparator<? super T> comparator, @NullableDecl T endpoint, BoundType boundType) {
        return new GeneralRange<>(comparator, false, null, BoundType.OPEN, true, endpoint, boundType);
    }

    static <T> GeneralRange<T> range(Comparator<? super T> comparator, @NullableDecl T lower, BoundType lowerType, @NullableDecl T upper, BoundType upperType) {
        return new GeneralRange<>(comparator, true, lower, lowerType, true, upper, upperType);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private GeneralRange(Comparator<? super T> comparator, boolean hasLowerBound, @NullableDecl T lowerEndpoint, BoundType lowerBoundType, boolean hasUpperBound, @NullableDecl T upperEndpoint, BoundType upperBoundType) {
        this.comparator = (Comparator) Preconditions.checkNotNull(comparator);
        this.hasLowerBound = hasLowerBound;
        this.hasUpperBound = hasUpperBound;
        this.lowerEndpoint = lowerEndpoint;
        this.lowerBoundType = (BoundType) Preconditions.checkNotNull(lowerBoundType);
        this.upperEndpoint = upperEndpoint;
        this.upperBoundType = (BoundType) Preconditions.checkNotNull(upperBoundType);
        if (hasLowerBound) {
            comparator.compare(lowerEndpoint, lowerEndpoint);
        }
        if (hasUpperBound) {
            comparator.compare(upperEndpoint, upperEndpoint);
        }
        if (hasLowerBound && hasUpperBound) {
            int cmp = comparator.compare(lowerEndpoint, upperEndpoint);
            boolean z = true;
            Preconditions.checkArgument(cmp <= 0, "lowerEndpoint (%s) > upperEndpoint (%s)", lowerEndpoint, upperEndpoint);
            if (cmp == 0) {
                Preconditions.checkArgument((upperBoundType == BoundType.OPEN ? false : z) | (lowerBoundType != BoundType.OPEN));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Comparator<? super T> comparator() {
        return this.comparator;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasLowerBound() {
        return this.hasLowerBound;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasUpperBound() {
        return this.hasUpperBound;
    }

    boolean isEmpty() {
        return (hasUpperBound() && tooLow(getUpperEndpoint())) || (hasLowerBound() && tooHigh(getLowerEndpoint()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean tooLow(@NullableDecl T t) {
        boolean z = false;
        if (!hasLowerBound()) {
            return false;
        }
        int cmp = this.comparator.compare(t, getLowerEndpoint());
        boolean z2 = cmp < 0;
        boolean z3 = cmp == 0;
        if (getLowerBoundType() == BoundType.OPEN) {
            z = true;
        }
        return (z & z3) | z2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean tooHigh(@NullableDecl T t) {
        boolean z = false;
        if (!hasUpperBound()) {
            return false;
        }
        int cmp = this.comparator.compare(t, getUpperEndpoint());
        boolean z2 = cmp > 0;
        boolean z3 = cmp == 0;
        if (getUpperBoundType() == BoundType.OPEN) {
            z = true;
        }
        return (z & z3) | z2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean contains(@NullableDecl T t) {
        return !tooLow(t) && !tooHigh(t);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Multiple debug info for r5v1 boolean: [D('hasUpBound' boolean), D('cmp' int)] */
    public GeneralRange<T> intersect(GeneralRange<T> other) {
        T upEnd;
        boolean hasUpBound;
        BoundType upType;
        BoundType lowType;
        int cmp;
        int cmp2;
        int cmp3;
        Preconditions.checkNotNull(other);
        Preconditions.checkArgument(this.comparator.equals(other.comparator));
        boolean hasLowBound = this.hasLowerBound;
        T lowEnd = getLowerEndpoint();
        BoundType lowType2 = getLowerBoundType();
        if (!hasLowerBound()) {
            hasLowBound = other.hasLowerBound;
            lowEnd = other.getLowerEndpoint();
            lowType2 = other.getLowerBoundType();
        } else if (other.hasLowerBound() && ((cmp3 = this.comparator.compare(getLowerEndpoint(), other.getLowerEndpoint())) < 0 || (cmp3 == 0 && other.getLowerBoundType() == BoundType.OPEN))) {
            lowEnd = other.getLowerEndpoint();
            lowType2 = other.getLowerBoundType();
        }
        boolean hasUpBound2 = this.hasUpperBound;
        T upEnd2 = getUpperEndpoint();
        BoundType upType2 = getUpperBoundType();
        if (!hasUpperBound()) {
            boolean hasUpBound3 = other.hasUpperBound;
            T upEnd3 = other.getUpperEndpoint();
            upType2 = other.getUpperBoundType();
            hasUpBound = hasUpBound3;
            upEnd = upEnd3;
        } else if (!other.hasUpperBound() || ((cmp2 = this.comparator.compare(getUpperEndpoint(), other.getUpperEndpoint())) <= 0 && !(cmp2 == 0 && other.getUpperBoundType() == BoundType.OPEN))) {
            hasUpBound = hasUpBound2;
            upEnd = upEnd2;
        } else {
            T upEnd4 = other.getUpperEndpoint();
            upType2 = other.getUpperBoundType();
            hasUpBound = hasUpBound2;
            upEnd = upEnd4;
        }
        if (!hasLowBound || !hasUpBound || ((cmp = this.comparator.compare(lowEnd, upEnd)) <= 0 && !(cmp == 0 && lowType2 == BoundType.OPEN && upType2 == BoundType.OPEN))) {
            lowType = lowType2;
            upType = upType2;
        } else {
            lowEnd = upEnd;
            BoundType lowType3 = BoundType.OPEN;
            upType = BoundType.CLOSED;
            lowType = lowType3;
        }
        return new GeneralRange<>(this.comparator, hasLowBound, lowEnd, lowType, hasUpBound, upEnd, upType);
    }

    @Override // java.lang.Object
    public boolean equals(@NullableDecl Object obj) {
        if (!(obj instanceof GeneralRange)) {
            return false;
        }
        GeneralRange<?> r = (GeneralRange) obj;
        if (!this.comparator.equals(r.comparator) || this.hasLowerBound != r.hasLowerBound || this.hasUpperBound != r.hasUpperBound || !getLowerBoundType().equals(r.getLowerBoundType()) || !getUpperBoundType().equals(r.getUpperBoundType()) || !Objects.equal(getLowerEndpoint(), r.getLowerEndpoint()) || !Objects.equal(getUpperEndpoint(), r.getUpperEndpoint())) {
            return false;
        }
        return true;
    }

    @Override // java.lang.Object
    public int hashCode() {
        return Objects.hashCode(this.comparator, getLowerEndpoint(), getLowerBoundType(), getUpperEndpoint(), getUpperBoundType());
    }

    GeneralRange<T> reverse() {
        GeneralRange<T> result = this.reverse;
        if (result != null) {
            return result;
        }
        GeneralRange<T> result2 = new GeneralRange<>(Ordering.from(this.comparator).reverse(), this.hasUpperBound, getUpperEndpoint(), getUpperBoundType(), this.hasLowerBound, getLowerEndpoint(), getLowerBoundType());
        result2.reverse = this;
        this.reverse = result2;
        return result2;
    }

    @Override // java.lang.Object
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.comparator);
        sb.append(":");
        sb.append(this.lowerBoundType == BoundType.CLOSED ? '[' : '(');
        sb.append(this.hasLowerBound ? this.lowerEndpoint : "-∞");
        sb.append(',');
        sb.append(this.hasUpperBound ? this.upperEndpoint : "∞");
        sb.append(this.upperBoundType == BoundType.CLOSED ? ']' : ')');
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public T getLowerEndpoint() {
        return this.lowerEndpoint;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BoundType getLowerBoundType() {
        return this.lowerBoundType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public T getUpperEndpoint() {
        return this.upperEndpoint;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BoundType getUpperBoundType() {
        return this.upperBoundType;
    }
}
