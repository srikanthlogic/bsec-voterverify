package com.google.common.primitives;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CheckReturnValue;
import com.google.errorprone.annotations.Immutable;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;
import okhttp3.HttpUrl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@Immutable
/* loaded from: classes3.dex */
public final class ImmutableDoubleArray implements Serializable {
    private static final ImmutableDoubleArray EMPTY = new ImmutableDoubleArray(new double[0]);
    private final double[] array;
    private final int end;
    private final transient int start;

    public static ImmutableDoubleArray of() {
        return EMPTY;
    }

    public static ImmutableDoubleArray of(double e0) {
        return new ImmutableDoubleArray(new double[]{e0});
    }

    public static ImmutableDoubleArray of(double e0, double e1) {
        return new ImmutableDoubleArray(new double[]{e0, e1});
    }

    public static ImmutableDoubleArray of(double e0, double e1, double e2) {
        return new ImmutableDoubleArray(new double[]{e0, e1, e2});
    }

    public static ImmutableDoubleArray of(double e0, double e1, double e2, double e3) {
        return new ImmutableDoubleArray(new double[]{e0, e1, e2, e3});
    }

    public static ImmutableDoubleArray of(double e0, double e1, double e2, double e3, double e4) {
        return new ImmutableDoubleArray(new double[]{e0, e1, e2, e3, e4});
    }

    public static ImmutableDoubleArray of(double e0, double e1, double e2, double e3, double e4, double e5) {
        return new ImmutableDoubleArray(new double[]{e0, e1, e2, e3, e4, e5});
    }

    public static ImmutableDoubleArray of(double first, double... rest) {
        Preconditions.checkArgument(rest.length <= 2147483646, "the total number of elements must fit in an int");
        double[] array = new double[rest.length + 1];
        array[0] = first;
        System.arraycopy(rest, 0, array, 1, rest.length);
        return new ImmutableDoubleArray(array);
    }

    public static ImmutableDoubleArray copyOf(double[] values) {
        if (values.length == 0) {
            return EMPTY;
        }
        return new ImmutableDoubleArray(Arrays.copyOf(values, values.length));
    }

    public static ImmutableDoubleArray copyOf(Collection<Double> values) {
        return values.isEmpty() ? EMPTY : new ImmutableDoubleArray(Doubles.toArray(values));
    }

    public static ImmutableDoubleArray copyOf(Iterable<Double> values) {
        if (values instanceof Collection) {
            return copyOf((Collection<Double>) ((Collection) values));
        }
        return builder().addAll(values).build();
    }

    public static Builder builder(int initialCapacity) {
        Preconditions.checkArgument(initialCapacity >= 0, "Invalid initialCapacity: %s", initialCapacity);
        return new Builder(initialCapacity);
    }

    public static Builder builder() {
        return new Builder(10);
    }

    /* loaded from: classes3.dex */
    public static final class Builder {
        private double[] array;
        private int count = 0;

        Builder(int initialCapacity) {
            this.array = new double[initialCapacity];
        }

        public Builder add(double value) {
            ensureRoomFor(1);
            double[] dArr = this.array;
            int i = this.count;
            dArr[i] = value;
            this.count = i + 1;
            return this;
        }

        public Builder addAll(double[] values) {
            ensureRoomFor(values.length);
            System.arraycopy(values, 0, this.array, this.count, values.length);
            this.count += values.length;
            return this;
        }

        public Builder addAll(Iterable<Double> values) {
            if (values instanceof Collection) {
                return addAll((Collection) values);
            }
            for (Double value : values) {
                add(value.doubleValue());
            }
            return this;
        }

        public Builder addAll(Collection<Double> values) {
            ensureRoomFor(values.size());
            for (Double value : values) {
                double[] dArr = this.array;
                int i = this.count;
                this.count = i + 1;
                dArr[i] = value.doubleValue();
            }
            return this;
        }

        public Builder addAll(ImmutableDoubleArray values) {
            ensureRoomFor(values.length());
            System.arraycopy(values.array, values.start, this.array, this.count, values.length());
            this.count += values.length();
            return this;
        }

        private void ensureRoomFor(int numberToAdd) {
            int newCount = this.count + numberToAdd;
            double[] dArr = this.array;
            if (newCount > dArr.length) {
                double[] newArray = new double[expandedCapacity(dArr.length, newCount)];
                System.arraycopy(this.array, 0, newArray, 0, this.count);
                this.array = newArray;
            }
        }

        private static int expandedCapacity(int oldCapacity, int minCapacity) {
            if (minCapacity >= 0) {
                int newCapacity = (oldCapacity >> 1) + oldCapacity + 1;
                if (newCapacity < minCapacity) {
                    newCapacity = Integer.highestOneBit(minCapacity - 1) << 1;
                }
                if (newCapacity < 0) {
                    return Integer.MAX_VALUE;
                }
                return newCapacity;
            }
            throw new AssertionError("cannot store more than MAX_VALUE elements");
        }

        @CheckReturnValue
        public ImmutableDoubleArray build() {
            int i = this.count;
            if (i == 0) {
                return ImmutableDoubleArray.EMPTY;
            }
            return new ImmutableDoubleArray(this.array, 0, i);
        }
    }

    private ImmutableDoubleArray(double[] array) {
        this(array, 0, array.length);
    }

    private ImmutableDoubleArray(double[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    public int length() {
        return this.end - this.start;
    }

    public boolean isEmpty() {
        return this.end == this.start;
    }

    public double get(int index) {
        Preconditions.checkElementIndex(index, length());
        return this.array[this.start + index];
    }

    public int indexOf(double target) {
        for (int i = this.start; i < this.end; i++) {
            if (areEqual(this.array[i], target)) {
                return i - this.start;
            }
        }
        return -1;
    }

    public int lastIndexOf(double target) {
        int i = this.end;
        while (true) {
            i--;
            if (i < this.start) {
                return -1;
            }
            if (areEqual(this.array[i], target)) {
                return i - this.start;
            }
        }
    }

    public boolean contains(double target) {
        return indexOf(target) >= 0;
    }

    public double[] toArray() {
        return Arrays.copyOfRange(this.array, this.start, this.end);
    }

    public ImmutableDoubleArray subArray(int startIndex, int endIndex) {
        Preconditions.checkPositionIndexes(startIndex, endIndex, length());
        if (startIndex == endIndex) {
            return EMPTY;
        }
        double[] dArr = this.array;
        int i = this.start;
        return new ImmutableDoubleArray(dArr, i + startIndex, i + endIndex);
    }

    public List<Double> asList() {
        return new AsList();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class AsList extends AbstractList<Double> implements RandomAccess, Serializable {
        private final ImmutableDoubleArray parent;

        private AsList(ImmutableDoubleArray parent) {
            this.parent = parent;
        }

        @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
        public int size() {
            return this.parent.length();
        }

        @Override // java.util.AbstractList, java.util.List
        public Double get(int index) {
            return Double.valueOf(this.parent.get(index));
        }

        @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
        public boolean contains(Object target) {
            return indexOf(target) >= 0;
        }

        @Override // java.util.AbstractList, java.util.List
        public int indexOf(Object target) {
            if (target instanceof Double) {
                return this.parent.indexOf(((Double) target).doubleValue());
            }
            return -1;
        }

        @Override // java.util.AbstractList, java.util.List
        public int lastIndexOf(Object target) {
            if (target instanceof Double) {
                return this.parent.lastIndexOf(((Double) target).doubleValue());
            }
            return -1;
        }

        @Override // java.util.AbstractList, java.util.List
        public List<Double> subList(int fromIndex, int toIndex) {
            return this.parent.subArray(fromIndex, toIndex).asList();
        }

        @Override // java.util.AbstractList, java.util.List, java.util.Collection, java.lang.Object
        public boolean equals(@NullableDecl Object object) {
            if (object instanceof AsList) {
                return this.parent.equals(((AsList) object).parent);
            }
            if (!(object instanceof List)) {
                return false;
            }
            List<?> that = (List) object;
            if (size() != that.size()) {
                return false;
            }
            int i = this.parent.start;
            for (Object element : that) {
                if (element instanceof Double) {
                    int i2 = i + 1;
                    if (ImmutableDoubleArray.areEqual(this.parent.array[i], ((Double) element).doubleValue())) {
                        i = i2;
                    }
                }
                return false;
            }
            return true;
        }

        @Override // java.util.AbstractList, java.util.List, java.util.Collection, java.lang.Object
        public int hashCode() {
            return this.parent.hashCode();
        }

        @Override // java.util.AbstractCollection, java.lang.Object
        public String toString() {
            return this.parent.toString();
        }
    }

    @Override // java.lang.Object
    public boolean equals(@NullableDecl Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof ImmutableDoubleArray)) {
            return false;
        }
        ImmutableDoubleArray that = (ImmutableDoubleArray) object;
        if (length() != that.length()) {
            return false;
        }
        for (int i = 0; i < length(); i++) {
            if (!areEqual(get(i), that.get(i))) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean areEqual(double a2, double b) {
        return Double.doubleToLongBits(a2) == Double.doubleToLongBits(b);
    }

    @Override // java.lang.Object
    public int hashCode() {
        int hash = 1;
        for (int i = this.start; i < this.end; i++) {
            hash = (hash * 31) + Doubles.hashCode(this.array[i]);
        }
        return hash;
    }

    @Override // java.lang.Object
    public String toString() {
        if (isEmpty()) {
            return HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }
        StringBuilder builder = new StringBuilder(length() * 5);
        builder.append('[');
        builder.append(this.array[this.start]);
        int i = this.start;
        while (true) {
            i++;
            if (i < this.end) {
                builder.append(", ");
                builder.append(this.array[i]);
            } else {
                builder.append(']');
                return builder.toString();
            }
        }
    }

    public ImmutableDoubleArray trimmed() {
        return isPartialView() ? new ImmutableDoubleArray(toArray()) : this;
    }

    private boolean isPartialView() {
        return this.start > 0 || this.end < this.array.length;
    }

    Object writeReplace() {
        return trimmed();
    }

    Object readResolve() {
        return isEmpty() ? EMPTY : this;
    }
}
