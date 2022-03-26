package com.google.common.hash;

import com.google.common.hash.Striped64;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
/* loaded from: classes.dex */
final class LongAdder extends Striped64 implements Serializable, LongAddable {
    private static final long serialVersionUID = 7249069246863182397L;

    @Override // com.google.common.hash.Striped64
    final long fn(long v, long x) {
        return v + x;
    }

    @Override // com.google.common.hash.LongAddable
    public void add(long x) {
        int n;
        Striped64.Cell a2;
        Striped64.Cell[] as = this.cells;
        if (as == null) {
            long b = this.base;
            if (casBase(b, b + x)) {
                return;
            }
        }
        boolean uncontended = true;
        int[] hc = (int[]) threadHashCode.get();
        if (!(hc == null || as == null || (n = as.length) < 1 || (a2 = as[(n - 1) & hc[0]]) == null)) {
            long v = a2.value;
            boolean cas = a2.cas(v, v + x);
            uncontended = cas;
            if (cas) {
                return;
            }
        }
        retryUpdate(x, hc, uncontended);
    }

    @Override // com.google.common.hash.LongAddable
    public void increment() {
        add(1);
    }

    public void decrement() {
        add(-1);
    }

    @Override // com.google.common.hash.LongAddable
    public long sum() {
        long sum = this.base;
        Striped64.Cell[] as = this.cells;
        if (as != null) {
            for (Striped64.Cell a2 : as) {
                if (a2 != null) {
                    sum += a2.value;
                }
            }
        }
        return sum;
    }

    public void reset() {
        internalReset(0);
    }

    public long sumThenReset() {
        long sum = this.base;
        Striped64.Cell[] as = this.cells;
        this.base = 0;
        if (as != null) {
            for (Striped64.Cell a2 : as) {
                if (a2 != null) {
                    sum += a2.value;
                    a2.value = 0;
                }
            }
        }
        return sum;
    }

    @Override // java.lang.Object
    public String toString() {
        return Long.toString(sum());
    }

    @Override // java.lang.Number
    public long longValue() {
        return sum();
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) sum();
    }

    @Override // java.lang.Number
    public float floatValue() {
        return (float) sum();
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return (double) sum();
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeLong(sum());
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.busy = 0;
        this.cells = null;
        this.base = s.readLong();
    }
}
