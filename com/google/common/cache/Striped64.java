package com.google.common.cache;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Random;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import sun.misc.Unsafe;
/* loaded from: classes.dex */
abstract class Striped64 extends Number {
    private static final Unsafe UNSAFE;
    private static final long baseOffset;
    private static final long busyOffset;
    volatile transient long base;
    volatile transient int busy;
    @NullableDecl
    volatile transient Cell[] cells;
    static final ThreadLocal<int[]> threadHashCode = new ThreadLocal<>();
    static final Random rng = new Random();
    static final int NCPU = Runtime.getRuntime().availableProcessors();

    abstract long fn(long j, long j2);

    /* loaded from: classes.dex */
    static final class Cell {
        private static final Unsafe UNSAFE;
        private static final long valueOffset;
        volatile long p0;
        volatile long p1;
        volatile long p2;
        volatile long p3;
        volatile long p4;
        volatile long p5;
        volatile long p6;
        volatile long q0;
        volatile long q1;
        volatile long q2;
        volatile long q3;
        volatile long q4;
        volatile long q5;
        volatile long q6;
        volatile long value;

        Cell(long x) {
            this.value = x;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final boolean cas(long cmp, long val) {
            return UNSAFE.compareAndSwapLong(this, valueOffset, cmp, val);
        }

        static {
            try {
                UNSAFE = Striped64.getUnsafe();
                valueOffset = UNSAFE.objectFieldOffset(Cell.class.getDeclaredField("value"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    static {
        try {
            UNSAFE = getUnsafe();
            baseOffset = UNSAFE.objectFieldOffset(Striped64.class.getDeclaredField("base"));
            busyOffset = UNSAFE.objectFieldOffset(Striped64.class.getDeclaredField("busy"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    final boolean casBase(long cmp, long val) {
        return UNSAFE.compareAndSwapLong(this, baseOffset, cmp, val);
    }

    final boolean casBusy() {
        return UNSAFE.compareAndSwapInt(this, busyOffset, 0, 1);
    }

    /* JADX INFO: Multiple debug info for r0v1 com.google.common.cache.Striped64$Cell[]: [D('h' int), D('as' com.google.common.cache.Striped64$Cell[])] */
    /* JADX WARN: Finally extract failed */
    final void retryUpdate(long x, int[] hc, boolean wasUncontended) {
        int[] hc2;
        int r;
        int h;
        int i;
        int h2;
        int h3;
        int m;
        int i2 = 0;
        if (hc == null) {
            int i3 = 1;
            int[] iArr = new int[1];
            hc2 = iArr;
            threadHashCode.set(iArr);
            int r2 = rng.nextInt();
            if (r2 != 0) {
                i3 = r2;
            }
            hc2[0] = i3;
            r = i3;
        } else {
            r = hc[0];
            hc2 = hc;
        }
        boolean collide = false;
        boolean wasUncontended2 = wasUncontended;
        int h4 = r;
        while (true) {
            Cell[] as = this.cells;
            if (as != null) {
                int n = as.length;
                if (n > 0) {
                    Cell a2 = as[(n - 1) & h4];
                    if (a2 == null) {
                        if (this.busy == 0) {
                            Cell r3 = new Cell(x);
                            if (this.busy == 0 && casBusy()) {
                                boolean created = false;
                                try {
                                    Cell[] rs = this.cells;
                                    if (rs != null && (m = rs.length) > 0) {
                                        int j = (m - 1) & h4;
                                        if (rs[j] == null) {
                                            rs[j] = r3;
                                            created = true;
                                        }
                                    }
                                    if (created) {
                                        return;
                                    }
                                } finally {
                                    this.busy = i2;
                                }
                            }
                        }
                        collide = false;
                        h3 = h4;
                        int h5 = h3 ^ (h3 << 13);
                        int h6 = h5 ^ (h5 >>> 17);
                        h2 = h6 ^ (h6 << 5);
                        hc2[0] = h2;
                        i = 0;
                    } else {
                        if (!wasUncontended2) {
                            wasUncontended2 = true;
                            h3 = h4;
                        } else {
                            long v = a2.value;
                            h3 = h4;
                            if (a2.cas(v, fn(v, x))) {
                                return;
                            }
                            if (n >= NCPU || this.cells != as) {
                                collide = false;
                            } else if (!collide) {
                                collide = true;
                            } else if (this.busy == 0 && casBusy()) {
                                try {
                                    if (this.cells == as) {
                                        Cell[] rs2 = new Cell[n << 1];
                                        for (int i4 = 0; i4 < n; i4++) {
                                            rs2[i4] = as[i4];
                                        }
                                        this.cells = rs2;
                                    }
                                    i2 = 0;
                                    this.busy = 0;
                                    collide = false;
                                    h4 = h3;
                                } catch (Throwable th) {
                                    i2 = 0;
                                    throw th;
                                }
                            }
                        }
                        int h52 = h3 ^ (h3 << 13);
                        int h62 = h52 ^ (h52 >>> 17);
                        h2 = h62 ^ (h62 << 5);
                        hc2[0] = h2;
                        i = 0;
                    }
                    i2 = i;
                    h4 = h2;
                } else {
                    h = h4;
                }
            } else {
                h = h4;
            }
            if (this.busy == 0 && this.cells == as && casBusy()) {
                boolean init = false;
                try {
                    if (this.cells == as) {
                        Cell[] rs3 = new Cell[2];
                        rs3[h & 1] = new Cell(x);
                        this.cells = rs3;
                        init = true;
                    }
                    i2 = 0;
                    if (!init) {
                        i = 0;
                    } else {
                        return;
                    }
                } catch (Throwable th2) {
                    i2 = 0;
                    throw th2;
                }
            } else {
                i = 0;
                long v2 = this.base;
                if (casBase(v2, fn(v2, x))) {
                    return;
                }
            }
            h2 = h;
            i2 = i;
            h4 = h2;
        }
    }

    final void internalReset(long initialValue) {
        Cell[] as = this.cells;
        this.base = initialValue;
        if (as != null) {
            for (Cell a2 : as) {
                if (a2 != null) {
                    a2.value = initialValue;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Unsafe getUnsafe() {
        try {
            return Unsafe.getUnsafe();
        } catch (SecurityException e) {
            try {
                return (Unsafe) AccessController.doPrivileged(new PrivilegedExceptionAction<Unsafe>() { // from class: com.google.common.cache.Striped64.1
                    @Override // java.security.PrivilegedExceptionAction
                    public Unsafe run() throws Exception {
                        Field[] declaredFields = Unsafe.class.getDeclaredFields();
                        for (Field f : declaredFields) {
                            f.setAccessible(true);
                            Object x = f.get(null);
                            if (Unsafe.class.isInstance(x)) {
                                return (Unsafe) Unsafe.class.cast(x);
                            }
                        }
                        throw new NoSuchFieldError("the Unsafe");
                    }
                });
            } catch (PrivilegedActionException e2) {
                throw new RuntimeException("Could not initialize intrinsics", e2.getCause());
            }
        }
    }
}
