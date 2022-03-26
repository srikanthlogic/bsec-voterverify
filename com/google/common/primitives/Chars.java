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
import kotlin.jvm.internal.CharCompanionObject;
import okhttp3.internal.ws.WebSocketProtocol;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes3.dex */
public final class Chars {
    public static final int BYTES = 2;

    private Chars() {
    }

    public static int hashCode(char value) {
        return value;
    }

    public static char checkedCast(long value) {
        char result = (char) ((int) value);
        Preconditions.checkArgument(((long) result) == value, "Out of range: %s", value);
        return result;
    }

    public static char saturatedCast(long value) {
        if (value > WebSocketProtocol.PAYLOAD_SHORT_MAX) {
            return CharCompanionObject.MAX_VALUE;
        }
        if (value < 0) {
            return 0;
        }
        return (char) ((int) value);
    }

    public static int compare(char a2, char b) {
        return a2 - b;
    }

    public static boolean contains(char[] array, char target) {
        for (char value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(char[] array, char target) {
        return indexOf(array, target, 0, array.length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int indexOf(char[] array, char target, int start, int end) {
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
    public static int indexOf(char[] array, char[] target) {
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

    public static int lastIndexOf(char[] array, char target) {
        return lastIndexOf(array, target, 0, array.length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int lastIndexOf(char[] array, char target, int start, int end) {
        for (int i = end - 1; i >= start; i--) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static char min(char... array) {
        Preconditions.checkArgument(array.length > 0);
        char min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static char max(char... array) {
        Preconditions.checkArgument(array.length > 0);
        char max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static char constrainToRange(char value, char min, char max) {
        Preconditions.checkArgument(min <= max, "min (%s) must be less than or equal to max (%s)", min, max);
        if (value < min) {
            return min;
        }
        return value < max ? value : max;
    }

    public static char[] concat(char[]... arrays) {
        int length = 0;
        for (char[] array : arrays) {
            length += array.length;
        }
        char[] result = new char[length];
        int pos = 0;
        for (char[] array2 : arrays) {
            System.arraycopy(array2, 0, result, pos, array2.length);
            pos += array2.length;
        }
        return result;
    }

    public static byte[] toByteArray(char value) {
        return new byte[]{(byte) (value >> '\b'), (byte) value};
    }

    public static char fromByteArray(byte[] bytes) {
        Preconditions.checkArgument(bytes.length >= 2, "array too small: %s < %s", bytes.length, 2);
        return fromBytes(bytes[0], bytes[1]);
    }

    public static char fromBytes(byte b1, byte b2) {
        return (char) ((b1 << 8) | (b2 & 255));
    }

    public static char[] ensureCapacity(char[] array, int minLength, int padding) {
        boolean z = true;
        Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
        if (padding < 0) {
            z = false;
        }
        Preconditions.checkArgument(z, "Invalid padding: %s", padding);
        return array.length < minLength ? Arrays.copyOf(array, minLength + padding) : array;
    }

    public static String join(String separator, char... array) {
        Preconditions.checkNotNull(separator);
        int len = array.length;
        if (len == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder((separator.length() * (len - 1)) + len);
        builder.append(array[0]);
        for (int i = 1; i < len; i++) {
            builder.append(separator);
            builder.append(array[i]);
        }
        return builder.toString();
    }

    public static Comparator<char[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    /* loaded from: classes3.dex */
    private enum LexicographicalComparator implements Comparator<char[]> {
        INSTANCE;

        public int compare(char[] left, char[] right) {
            int minLength = Math.min(left.length, right.length);
            for (int i = 0; i < minLength; i++) {
                int result = Chars.compare(left[i], right[i]);
                if (result != 0) {
                    return result;
                }
            }
            return left.length - right.length;
        }

        @Override // java.lang.Enum, java.lang.Object
        public String toString() {
            return "Chars.lexicographicalComparator()";
        }
    }

    public static char[] toArray(Collection<Character> collection) {
        if (collection instanceof CharArrayAsList) {
            return ((CharArrayAsList) collection).toCharArray();
        }
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        char[] array = new char[len];
        for (int i = 0; i < len; i++) {
            array[i] = ((Character) Preconditions.checkNotNull(boxedArray[i])).charValue();
        }
        return array;
    }

    public static void sortDescending(char[] array) {
        Preconditions.checkNotNull(array);
        sortDescending(array, 0, array.length);
    }

    public static void sortDescending(char[] array, int fromIndex, int toIndex) {
        Preconditions.checkNotNull(array);
        Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
        Arrays.sort(array, fromIndex, toIndex);
        reverse(array, fromIndex, toIndex);
    }

    public static void reverse(char[] array) {
        Preconditions.checkNotNull(array);
        reverse(array, 0, array.length);
    }

    public static void reverse(char[] array, int fromIndex, int toIndex) {
        Preconditions.checkNotNull(array);
        Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
        int i = fromIndex;
        for (int j = toIndex - 1; i < j; j--) {
            char tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
            i++;
        }
    }

    public static List<Character> asList(char... backingArray) {
        if (backingArray.length == 0) {
            return Collections.emptyList();
        }
        return new CharArrayAsList(backingArray);
    }

    /* loaded from: classes3.dex */
    private static class CharArrayAsList extends AbstractList<Character> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;
        final char[] array;
        final int end;
        final int start;

        CharArrayAsList(char[] array) {
            this(array, 0, array.length);
        }

        CharArrayAsList(char[] array, int start, int end) {
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
        public Character get(int index) {
            Preconditions.checkElementIndex(index, size());
            return Character.valueOf(this.array[this.start + index]);
        }

        @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
        public boolean contains(Object target) {
            return (target instanceof Character) && Chars.indexOf(this.array, ((Character) target).charValue(), this.start, this.end) != -1;
        }

        @Override // java.util.AbstractList, java.util.List
        public int indexOf(Object target) {
            int i;
            if (!(target instanceof Character) || (i = Chars.indexOf(this.array, ((Character) target).charValue(), this.start, this.end)) < 0) {
                return -1;
            }
            return i - this.start;
        }

        @Override // java.util.AbstractList, java.util.List
        public int lastIndexOf(Object target) {
            int i;
            if (!(target instanceof Character) || (i = Chars.lastIndexOf(this.array, ((Character) target).charValue(), this.start, this.end)) < 0) {
                return -1;
            }
            return i - this.start;
        }

        public Character set(int index, Character element) {
            Preconditions.checkElementIndex(index, size());
            char[] cArr = this.array;
            int i = this.start;
            char oldValue = cArr[i + index];
            cArr[i + index] = ((Character) Preconditions.checkNotNull(element)).charValue();
            return Character.valueOf(oldValue);
        }

        @Override // java.util.AbstractList, java.util.List
        public List<Character> subList(int fromIndex, int toIndex) {
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
            if (fromIndex == toIndex) {
                return Collections.emptyList();
            }
            char[] cArr = this.array;
            int i = this.start;
            return new CharArrayAsList(cArr, i + fromIndex, i + toIndex);
        }

        @Override // java.util.AbstractList, java.util.List, java.util.Collection, java.lang.Object
        public boolean equals(@NullableDecl Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof CharArrayAsList)) {
                return super.equals(object);
            }
            CharArrayAsList that = (CharArrayAsList) object;
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
                result = (result * 31) + Chars.hashCode(this.array[i]);
            }
            return result;
        }

        @Override // java.util.AbstractCollection, java.lang.Object
        public String toString() {
            StringBuilder builder = new StringBuilder(size() * 3);
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

        char[] toCharArray() {
            return Arrays.copyOfRange(this.array, this.start, this.end);
        }
    }
}
