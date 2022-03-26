package com.google.common.collect;

import com.google.common.base.Objects;
import java.util.Arrays;
import kotlin.UShort;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
final class CompactHashing {
    private static final int BYTE_MASK = 255;
    private static final int BYTE_MAX_SIZE = 256;
    static final int DEFAULT_SIZE = 3;
    static final int HASH_TABLE_BITS_MASK = 31;
    private static final int HASH_TABLE_BITS_MAX_BITS = 5;
    static final int MAX_SIZE = 1073741823;
    private static final int MIN_HASH_TABLE_SIZE = 4;
    static final int MODIFICATION_COUNT_INCREMENT = 32;
    private static final int SHORT_MASK = 65535;
    private static final int SHORT_MAX_SIZE = 65536;
    static final byte UNSET = 0;

    private CompactHashing() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int tableSize(int expectedSize) {
        return Math.max(4, Hashing.closedTableSize(expectedSize + 1, 1.0d));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object createTable(int buckets) {
        if (buckets < 2 || buckets > 1073741824 || Integer.highestOneBit(buckets) != buckets) {
            throw new IllegalArgumentException("must be power of 2 between 2^1 and 2^30: " + buckets);
        } else if (buckets <= 256) {
            return new byte[buckets];
        } else {
            if (buckets <= 65536) {
                return new short[buckets];
            }
            return new int[buckets];
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void tableClear(Object table) {
        if (table instanceof byte[]) {
            Arrays.fill((byte[]) table, (byte) 0);
        } else if (table instanceof short[]) {
            Arrays.fill((short[]) table, (short) 0);
        } else {
            Arrays.fill((int[]) table, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int tableGet(Object table, int index) {
        if (table instanceof byte[]) {
            return ((byte[]) table)[index] & 255;
        }
        if (table instanceof short[]) {
            return ((short[]) table)[index] & UShort.MAX_VALUE;
        }
        return ((int[]) table)[index];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void tableSet(Object table, int index, int entry) {
        if (table instanceof byte[]) {
            ((byte[]) table)[index] = (byte) entry;
        } else if (table instanceof short[]) {
            ((short[]) table)[index] = (short) entry;
        } else {
            ((int[]) table)[index] = entry;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int newCapacity(int mask) {
        return (mask < 32 ? 4 : 2) * (mask + 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getHashPrefix(int value, int mask) {
        return (~mask) & value;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getNext(int entry, int mask) {
        return entry & mask;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int maskCombine(int prefix, int suffix, int mask) {
        return ((~mask) & prefix) | (suffix & mask);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int remove(@NullableDecl Object key, @NullableDecl Object value, int mask, Object table, int[] entries, Object[] keys, @NullableDecl Object[] values) {
        int hash = Hashing.smearedHash(key);
        int tableIndex = hash & mask;
        int next = tableGet(table, tableIndex);
        if (next == 0) {
            return -1;
        }
        int hashPrefix = getHashPrefix(hash, mask);
        int lastEntryIndex = -1;
        do {
            int entryIndex = next - 1;
            int entry = entries[entryIndex];
            if (getHashPrefix(entry, mask) != hashPrefix || !Objects.equal(key, keys[entryIndex]) || (values != null && !Objects.equal(value, values[entryIndex]))) {
                lastEntryIndex = entryIndex;
                next = getNext(entry, mask);
            } else {
                int newNext = getNext(entry, mask);
                if (lastEntryIndex == -1) {
                    tableSet(table, tableIndex, newNext);
                } else {
                    entries[lastEntryIndex] = maskCombine(entries[lastEntryIndex], newNext, mask);
                }
                return entryIndex;
            }
        } while (next != 0);
        return -1;
    }
}
