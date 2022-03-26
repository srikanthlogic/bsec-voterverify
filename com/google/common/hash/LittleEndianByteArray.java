package com.google.common.hash;

import com.google.common.primitives.Longs;
import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import sun.misc.Unsafe;
/* loaded from: classes.dex */
final class LittleEndianByteArray {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final LittleEndianBytes byteArray;

    /* loaded from: classes.dex */
    private enum JavaLittleEndianBytes implements LittleEndianBytes {
        INSTANCE {
            @Override // com.google.common.hash.LittleEndianByteArray.LittleEndianBytes
            public long getLongLittleEndian(byte[] source, int offset) {
                return Longs.fromBytes(source[offset + 7], source[offset + 6], source[offset + 5], source[offset + 4], source[offset + 3], source[offset + 2], source[offset + 1], source[offset]);
            }

            @Override // com.google.common.hash.LittleEndianByteArray.LittleEndianBytes
            public void putLongLittleEndian(byte[] sink, int offset, long value) {
                long mask = 255;
                for (int i = 0; i < 8; i++) {
                    sink[offset + i] = (byte) ((int) ((value & mask) >> (i * 8)));
                    mask <<= 8;
                }
            }
        }
    }

    /* loaded from: classes.dex */
    private interface LittleEndianBytes {
        long getLongLittleEndian(byte[] bArr, int i);

        void putLongLittleEndian(byte[] bArr, int i, long j);
    }

    static {
        LittleEndianBytes theGetter = JavaLittleEndianBytes.INSTANCE;
        try {
            if ("amd64".equals(System.getProperty("os.arch"))) {
                theGetter = ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN) ? UnsafeByteArray.UNSAFE_LITTLE_ENDIAN : UnsafeByteArray.UNSAFE_BIG_ENDIAN;
            }
        } catch (Throwable th) {
        }
        byteArray = theGetter;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long load64(byte[] input, int offset) {
        return byteArray.getLongLittleEndian(input, offset);
    }

    static long load64Safely(byte[] input, int offset, int length) {
        long result = 0;
        int limit = Math.min(length, 8);
        for (int i = 0; i < limit; i++) {
            result |= (((long) input[offset + i]) & 255) << (i * 8);
        }
        return result;
    }

    static void store64(byte[] sink, int offset, long value) {
        byteArray.putLongLittleEndian(sink, offset, value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int load32(byte[] source, int offset) {
        return (source[offset] & 255) | ((source[offset + 1] & 255) << 8) | ((source[offset + 2] & 255) << 16) | ((source[offset + 3] & 255) << 24);
    }

    static boolean usingUnsafe() {
        return byteArray instanceof UnsafeByteArray;
    }

    /* loaded from: classes.dex */
    private enum UnsafeByteArray implements LittleEndianBytes {
        UNSAFE_LITTLE_ENDIAN {
            @Override // com.google.common.hash.LittleEndianByteArray.LittleEndianBytes
            public long getLongLittleEndian(byte[] array, int offset) {
                return UnsafeByteArray.theUnsafe.getLong(array, ((long) offset) + ((long) UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET));
            }

            @Override // com.google.common.hash.LittleEndianByteArray.LittleEndianBytes
            public void putLongLittleEndian(byte[] array, int offset, long value) {
                UnsafeByteArray.theUnsafe.putLong(array, ((long) offset) + ((long) UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET), value);
            }
        },
        UNSAFE_BIG_ENDIAN {
            @Override // com.google.common.hash.LittleEndianByteArray.LittleEndianBytes
            public long getLongLittleEndian(byte[] array, int offset) {
                return Long.reverseBytes(UnsafeByteArray.theUnsafe.getLong(array, ((long) offset) + ((long) UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET)));
            }

            @Override // com.google.common.hash.LittleEndianByteArray.LittleEndianBytes
            public void putLongLittleEndian(byte[] array, int offset, long value) {
                UnsafeByteArray.theUnsafe.putLong(array, ((long) offset) + ((long) UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET), Long.reverseBytes(value));
            }
        };
        
        private static final Unsafe theUnsafe = getUnsafe();
        private static final int BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class);

        static {
            if (theUnsafe.arrayIndexScale(byte[].class) != 1) {
                throw new AssertionError();
            }
        }

        private static Unsafe getUnsafe() {
            try {
                return Unsafe.getUnsafe();
            } catch (SecurityException e) {
                try {
                    return (Unsafe) AccessController.doPrivileged(new PrivilegedExceptionAction<Unsafe>() { // from class: com.google.common.hash.LittleEndianByteArray.UnsafeByteArray.3
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

    private LittleEndianByteArray() {
    }
}
