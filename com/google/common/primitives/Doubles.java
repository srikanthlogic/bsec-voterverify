package com.google.common.primitives;

import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import java.util.regex.Pattern;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes3.dex */
public final class Doubles extends DoublesMethodsForWeb {
    public static final int BYTES = 8;
    static final Pattern FLOATING_POINT_PATTERN = fpPattern();

    private Doubles() {
    }

    public static int hashCode(double value) {
        return Double.valueOf(value).hashCode();
    }

    public static int compare(double a2, double b) {
        return Double.compare(a2, b);
    }

    public static boolean isFinite(double value) {
        return Double.NEGATIVE_INFINITY < value && value < Double.POSITIVE_INFINITY;
    }

    public static boolean contains(double[] array, double target) {
        for (double value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(double[] array, double target) {
        return indexOf(array, target, 0, array.length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int indexOf(double[] array, double target, int start, int end) {
        for (int i = start; i < end; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0026, code lost:
        r0 = r0 + 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static int indexOf(double[] array, double[] target) {
        Preconditions.checkNotNull(array, "array");
        Preconditions.checkNotNull(target, "target");
        if (target.length == 0) {
            return 0;
        }
        int i = 0;
        while (i < (array.length - target.length) + 1) {
            for (int j = 0; j < target.length; j++) {
                if (array[i + j] != target[j]) {
                    break;
                }
            }
            return i;
        }
        return -1;
    }

    public static int lastIndexOf(double[] array, double target) {
        return lastIndexOf(array, target, 0, array.length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int lastIndexOf(double[] array, double target, int start, int end) {
        for (int i = end - 1; i >= start; i--) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static double min(double... array) {
        Preconditions.checkArgument(array.length > 0);
        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            min = Math.min(min, array[i]);
        }
        return min;
    }

    public static double max(double... array) {
        Preconditions.checkArgument(array.length > 0);
        double max = array[0];
        for (int i = 1; i < array.length; i++) {
            max = Math.max(max, array[i]);
        }
        return max;
    }

    public static double constrainToRange(double value, double min, double max) {
        Preconditions.checkArgument(min <= max, "min (%s) must be less than or equal to max (%s)", Double.valueOf(min), Double.valueOf(max));
        return Math.min(Math.max(value, min), max);
    }

    public static double[] concat(double[]... arrays) {
        int length = 0;
        for (double[] array : arrays) {
            length += array.length;
        }
        double[] result = new double[length];
        int pos = 0;
        for (double[] array2 : arrays) {
            System.arraycopy(array2, 0, result, pos, array2.length);
            pos += array2.length;
        }
        return result;
    }

    /* loaded from: classes3.dex */
    private static final class DoubleConverter extends Converter<String, Double> implements Serializable {
        static final DoubleConverter INSTANCE = new DoubleConverter();
        private static final long serialVersionUID = 1;

        private DoubleConverter() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public Double doForward(String value) {
            return Double.valueOf(value);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public String doBackward(Double value) {
            return value.toString();
        }

        @Override // java.lang.Object
        public String toString() {
            return "Doubles.stringConverter()";
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }

    public static Converter<String, Double> stringConverter() {
        return DoubleConverter.INSTANCE;
    }

    public static double[] ensureCapacity(double[] array, int minLength, int padding) {
        boolean z = true;
        Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
        if (padding < 0) {
            z = false;
        }
        Preconditions.checkArgument(z, "Invalid padding: %s", padding);
        return array.length < minLength ? Arrays.copyOf(array, minLength + padding) : array;
    }

    public static String join(String separator, double... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(array.length * 12);
        builder.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            builder.append(separator);
            builder.append(array[i]);
        }
        return builder.toString();
    }

    public static Comparator<double[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    /* loaded from: classes3.dex */
    private enum LexicographicalComparator implements Comparator<double[]> {
        INSTANCE;

        public int compare(double[] left, double[] right) {
            int minLength = Math.min(left.length, right.length);
            for (int i = 0; i < minLength; i++) {
                int result = Double.compare(left[i], right[i]);
                if (result != 0) {
                    return result;
                }
            }
            return left.length - right.length;
        }

        @Override // java.lang.Enum, java.lang.Object
        public String toString() {
            return "Doubles.lexicographicalComparator()";
        }
    }

    public static void sortDescending(double[] array) {
        Preconditions.checkNotNull(array);
        sortDescending(array, 0, array.length);
    }

    public static void sortDescending(double[] array, int fromIndex, int toIndex) {
        Preconditions.checkNotNull(array);
        Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
        Arrays.sort(array, fromIndex, toIndex);
        reverse(array, fromIndex, toIndex);
    }

    public static void reverse(double[] array) {
        Preconditions.checkNotNull(array);
        reverse(array, 0, array.length);
    }

    public static void reverse(double[] array, int fromIndex, int toIndex) {
        Preconditions.checkNotNull(array);
        Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
        int i = fromIndex;
        for (int j = toIndex - 1; i < j; j--) {
            double tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
            i++;
        }
    }

    public static double[] toArray(Collection<? extends Number> collection) {
        if (collection instanceof DoubleArrayAsList) {
            return ((DoubleArrayAsList) collection).toDoubleArray();
        }
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        double[] array = new double[len];
        for (int i = 0; i < len; i++) {
            array[i] = ((Number) Preconditions.checkNotNull(boxedArray[i])).doubleValue();
        }
        return array;
    }

    public static List<Double> asList(double... backingArray) {
        if (backingArray.length == 0) {
            return Collections.emptyList();
        }
        return new DoubleArrayAsList(backingArray);
    }

    /* loaded from: classes3.dex */
    private static class DoubleArrayAsList extends AbstractList<Double> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;
        final double[] array;
        final int end;
        final int start;

        DoubleArrayAsList(double[] array) {
            this(array, 0, array.length);
        }

        DoubleArrayAsList(double[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
        public int size() {
            return this.end - this.start;
        }

        @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
        public boolean isEmpty() {
            return false;
        }

        @Override // java.util.AbstractList, java.util.List
        public Double get(int index) {
            Preconditions.checkElementIndex(index, size());
            return Double.valueOf(this.array[this.start + index]);
        }

        @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
        public boolean contains(Object target) {
            return (target instanceof Double) && Doubles.indexOf(this.array, ((Double) target).doubleValue(), this.start, this.end) != -1;
        }

        @Override // java.util.AbstractList, java.util.List
        public int indexOf(Object target) {
            int i;
            if (!(target instanceof Double) || (i = Doubles.indexOf(this.array, ((Double) target).doubleValue(), this.start, this.end)) < 0) {
                return -1;
            }
            return i - this.start;
        }

        @Override // java.util.AbstractList, java.util.List
        public int lastIndexOf(Object target) {
            int i;
            if (!(target instanceof Double) || (i = Doubles.lastIndexOf(this.array, ((Double) target).doubleValue(), this.start, this.end)) < 0) {
                return -1;
            }
            return i - this.start;
        }

        public Double set(int index, Double element) {
            Preconditions.checkElementIndex(index, size());
            double[] dArr = this.array;
            int i = this.start;
            double oldValue = dArr[i + index];
            dArr[i + index] = ((Double) Preconditions.checkNotNull(element)).doubleValue();
            return Double.valueOf(oldValue);
        }

        @Override // java.util.AbstractList, java.util.List
        public List<Double> subList(int fromIndex, int toIndex) {
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
            if (fromIndex == toIndex) {
                return Collections.emptyList();
            }
            double[] dArr = this.array;
            int i = this.start;
            return new DoubleArrayAsList(dArr, i + fromIndex, i + toIndex);
        }

        @Override // java.util.AbstractList, java.util.List, java.util.Collection, java.lang.Object
        public boolean equals(@NullableDecl Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof DoubleArrayAsList)) {
                return super.equals(object);
            }
            DoubleArrayAsList that = (DoubleArrayAsList) object;
            int size = size();
            if (that.size() != size) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (this.array[this.start + i] != that.array[that.start + i]) {
                    return false;
                }
            }
            return true;
        }

        @Override // java.util.AbstractList, java.util.List, java.util.Collection, java.lang.Object
        public int hashCode() {
            int result = 1;
            for (int i = this.start; i < this.end; i++) {
                result = (result * 31) + Doubles.hashCode(this.array[i]);
            }
            return result;
        }

        @Override // java.util.AbstractCollection, java.lang.Object
        public String toString() {
            StringBuilder builder = new StringBuilder(size() * 12);
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

        double[] toDoubleArray() {
            return Arrays.copyOfRange(this.array, this.start, this.end);
        }
    }

    private static Pattern fpPattern() {
        return Pattern.compile(("[+-]?(?:NaN|Infinity|" + ("(?:\\d+#(?:\\.\\d*#)?|\\.\\d+#)(?:[eE][+-]?\\d+#)?[fFdD]?") + "|" + ("0[xX](?:[0-9a-fA-F]+#(?:\\.[0-9a-fA-F]*#)?|\\.[0-9a-fA-F]+#)[pP][+-]?\\d+#[fFdD]?") + ")").replace("#", "+"));
    }

    @NullableDecl
    public static Double tryParse(String string) {
        if (!FLOATING_POINT_PATTERN.matcher(string).matches()) {
            return null;
        }
        try {
            return Double.valueOf(Double.parseDouble(string));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
