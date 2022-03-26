package com.google.common.base;

import com.google.common.base.CharMatcher;
import java.util.BitSet;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SmallCharMatcher extends CharMatcher.NamedFastMatcher {
    private static final int C1 = -862048943;
    private static final int C2 = 461845907;
    private static final double DESIRED_LOAD_FACTOR = 0.5d;
    static final int MAX_SIZE = 1023;
    private final boolean containsZero;
    private final long filter;
    private final char[] table;

    private SmallCharMatcher(char[] table, long filter, boolean containsZero, String description) {
        super(description);
        this.table = table;
        this.filter = filter;
        this.containsZero = containsZero;
    }

    static int smear(int hashCode) {
        return Integer.rotateLeft(C1 * hashCode, 15) * C2;
    }

    private boolean checkFilter(int c) {
        return 1 == ((this.filter >> c) & 1);
    }

    static int chooseTableSize(int setSize) {
        if (setSize == 1) {
            return 2;
        }
        int tableSize = Integer.highestOneBit(setSize - 1) << 1;
        while (((double) tableSize) * 0.5d < ((double) setSize)) {
            tableSize <<= 1;
        }
        return tableSize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CharMatcher from(BitSet chars, String description) {
        long filter = 0;
        int size = chars.cardinality();
        boolean containsZero = chars.get(0);
        char[] table = new char[chooseTableSize(size)];
        int mask = table.length - 1;
        int c = chars.nextSetBit(0);
        while (c != -1) {
            long filter2 = (1 << c) | filter;
            int index = smear(c) & mask;
            while (table[index] != 0) {
                index = (index + 1) & mask;
            }
            table[index] = (char) c;
            c = chars.nextSetBit(c + 1);
            filter = filter2;
        }
        return new SmallCharMatcher(table, filter, containsZero, description);
    }

    @Override // com.google.common.base.CharMatcher
    public boolean matches(char c) {
        if (c == 0) {
            return this.containsZero;
        }
        if (!checkFilter(c)) {
            return false;
        }
        int mask = this.table.length - 1;
        int startingIndex = smear(c) & mask;
        int index = startingIndex;
        do {
            char[] cArr = this.table;
            if (cArr[index] == 0) {
                return false;
            }
            if (cArr[index] == c) {
                return true;
            }
            index = (index + 1) & mask;
        } while (index != startingIndex);
        return false;
    }

    @Override // com.google.common.base.CharMatcher
    void setBits(BitSet table) {
        if (this.containsZero) {
            table.set(0);
        }
        char[] cArr = this.table;
        for (char c : cArr) {
            if (c != 0) {
                table.set(c);
            }
        }
    }
}
