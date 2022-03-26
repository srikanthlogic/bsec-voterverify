package com.google.common.primitives;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes3.dex */
public final class Booleans {
    private Booleans() {
    }

    /* loaded from: classes3.dex */
    private enum BooleanComparator implements Comparator<Boolean> {
        TRUE_FIRST(1, "Booleans.trueFirst()"),
        FALSE_FIRST(-1, "Booleans.falseFirst()");
        
        private final String toString;
        private final int trueValue;

        BooleanComparator(int trueValue, String toString) {
            this.trueValue = trueValue;
            this.toString = toString;
        }

        public int compare(Boolean a2, Boolean b) {
            int bVal = 0;
            int aVal = a2.booleanValue() ? this.trueValue : 0;
            if (b.booleanValue()) {
                bVal = this.trueValue;
            }
            return bVal - aVal;
        }

        @Override // java.lang.Enum, java.lang.Object
        public String toString() {
            return this.toString;
        }
    }

    public static Comparator<Boolean> trueFirst() {
        return BooleanComparator.TRUE_FIRST;
    }

    public static Comparator<Boolean> falseFirst() {
        return BooleanComparator.FALSE_FIRST;
    }

    public static int hashCode(boolean value) {
        return value ? 1231 : 1237;
    }

    public static int compare(boolean a2, boolean b) {
        if (a2 == b) {
            return 0;
        }
        return a2 ? 1 : -1;
    }

    public static boolean contains(boolean[] array, boolean target) {
        for (boolean value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(boolean[] array, boolean target) {
        return indexOf(array, target, 0, array.length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int indexOf(boolean[] array, boolean target, int start, int end) {
        for (int i = start; i < end; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0024, code lost:
        r0 = r0 + 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static int indexOf(boolean[] array, boolean[] target) {
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

    public static int lastIndexOf(boolean[] array, boolean target) {
        return lastIndexOf(array, target, 0, array.length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int lastIndexOf(boolean[] array, boolean target, int start, int end) {
        for (int i = end - 1; i >= start; i--) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static boolean[] concat(boolean[]... arrays) {
        int length = 0;
        for (boolean[] array : arrays) {
            length += array.length;
        }
        boolean[] result = new boolean[length];
        int pos = 0;
        for (boolean[] array2 : arrays) {
            System.arraycopy(array2, 0, result, pos, array2.length);
            pos += array2.length;
        }
        return result;
    }

    public static boolean[] ensureCapacity(boolean[] array, int minLength, int padding) {
        boolean z = true;
        Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
        if (padding < 0) {
            z = false;
        }
        Preconditions.checkArgument(z, "Invalid padding: %s", padding);
        return array.length < minLength ? Arrays.copyOf(array, minLength + padding) : array;
    }

    public static String join(String separator, boolean... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(array.length * 7);
        builder.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            builder.append(separator);
            builder.append(array[i]);
        }
        return builder.toString();
    }

    public static Comparator<boolean[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    /* loaded from: classes3.dex */
    private enum LexicographicalComparator implements Comparator<boolean[]> {
        INSTANCE;

        public int compare(boolean[] left, boolean[] right) {
            int minLength = Math.min(left.length, right.length);
            for (int i = 0; i < minLength; i++) {
                int result = Booleans.compare(left[i], right[i]);
                if (result != 0) {
                    return result;
                }
            }
            return left.length - right.length;
        }

        @Override // java.lang.Enum, java.lang.Object
        public String toString() {
            return "Booleans.lexicographicalComparator()";
        }
    }

    public static boolean[] toArray(Collection<Boolean> collection) {
        if (collection instanceof BooleanArrayAsList) {
            return ((BooleanArrayAsList) collection).toBooleanArray();
        }
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        boolean[] array = new boolean[len];
        for (int i = 0; i < len; i++) {
            array[i] = ((Boolean) Preconditions.checkNotNull(boxedArray[i])).booleanValue();
        }
        return array;
    }

    public static List<Boolean> asList(boolean... backingArray) {
        if (backingArray.length == 0) {
            return Collections.emptyList();
        }
        return new BooleanArrayAsList(backingArray);
    }

    /* loaded from: classes3.dex */
    private static class BooleanArrayAsList extends AbstractList<Boolean> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;
        final boolean[] array;
        final int end;
        final int start;

        BooleanArrayAsList(boolean[] array) {
            this(array, 0, array.length);
        }

        BooleanArrayAsList(boolean[] array, int start, int end) {
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
        public Boolean get(int index) {
            Preconditions.checkElementIndex(index, size());
            return Boolean.valueOf(this.array[this.start + index]);
        }

        @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
        public boolean contains(Object target) {
            return (target instanceof Boolean) && Booleans.indexOf(this.array, ((Boolean) target).booleanValue(), this.start, this.end) != -1;
        }

        @Override // java.util.AbstractList, java.util.List
        public int indexOf(Object target) {
            int i;
            if (!(target instanceof Boolean) || (i = Booleans.indexOf(this.array, ((Boolean) target).booleanValue(), this.start, this.end)) < 0) {
                return -1;
            }
            return i - this.start;
        }

        @Override // java.util.AbstractList, java.util.List
        public int lastIndexOf(Object target) {
            int i;
            if (!(target instanceof Boolean) || (i = Booleans.lastIndexOf(this.array, ((Boolean) target).booleanValue(), this.start, this.end)) < 0) {
                return -1;
            }
            return i - this.start;
        }

        public Boolean set(int index, Boolean element) {
            Preconditions.checkElementIndex(index, size());
            boolean[] zArr = this.array;
            int i = this.start;
            boolean oldValue = zArr[i + index];
            zArr[i + index] = ((Boolean) Preconditions.checkNotNull(element)).booleanValue();
            return Boolean.valueOf(oldValue);
        }

        @Override // java.util.AbstractList, java.util.List
        public List<Boolean> subList(int fromIndex, int toIndex) {
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
            if (fromIndex == toIndex) {
                return Collections.emptyList();
            }
            boolean[] zArr = this.array;
            int i = this.start;
            return new BooleanArrayAsList(zArr, i + fromIndex, i + toIndex);
        }

        @Override // java.util.AbstractList, java.util.List, java.util.Collection, java.lang.Object
        public boolean equals(@NullableDecl Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof BooleanArrayAsList)) {
                return super.equals(object);
            }
            BooleanArrayAsList that = (BooleanArrayAsList) object;
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
                result = (result * 31) + Booleans.hashCode(this.array[i]);
            }
            return result;
        }

        @Override // java.util.AbstractCollection, java.lang.Object
        public String toString() {
            StringBuilder builder = new StringBuilder(size() * 7);
            builder.append(this.array[this.start] ? "[true" : "[false");
            int i = this.start;
            while (true) {
                i++;
                if (i < this.end) {
                    builder.append(this.array[i] ? ", true" : ", false");
                } else {
                    builder.append(']');
                    return builder.toString();
                }
            }
        }

        boolean[] toBooleanArray() {
            return Arrays.copyOfRange(this.array, this.start, this.end);
        }
    }

    public static int countTrue(boolean... values) {
        int count = 0;
        for (boolean value : values) {
            if (value) {
                count++;
            }
        }
        return count;
    }

    public static void reverse(boolean[] array) {
        Preconditions.checkNotNull(array);
        reverse(array, 0, array.length);
    }

    public static void reverse(boolean[] array, int fromIndex, int toIndex) {
        Preconditions.checkNotNull(array);
        Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
        int i = fromIndex;
        for (int j = toIndex - 1; i < j; j--) {
            boolean tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
            i++;
        }
    }
}
