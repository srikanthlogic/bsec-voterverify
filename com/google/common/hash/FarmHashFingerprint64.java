package com.google.common.hash;

import com.google.common.base.Preconditions;
/* loaded from: classes.dex */
final class FarmHashFingerprint64 extends AbstractNonStreamingHashFunction {
    static final HashFunction FARMHASH_FINGERPRINT_64 = new FarmHashFingerprint64();
    private static final long K0 = -4348849565147123417L;
    private static final long K1 = -5435081209227447693L;
    private static final long K2 = -7286425919675154353L;

    FarmHashFingerprint64() {
    }

    @Override // com.google.common.hash.AbstractNonStreamingHashFunction, com.google.common.hash.AbstractHashFunction, com.google.common.hash.HashFunction
    public HashCode hashBytes(byte[] input, int off, int len) {
        Preconditions.checkPositionIndexes(off, off + len, input.length);
        return HashCode.fromLong(fingerprint(input, off, len));
    }

    @Override // com.google.common.hash.HashFunction
    public int bits() {
        return 64;
    }

    public String toString() {
        return "Hashing.farmHashFingerprint64()";
    }

    static long fingerprint(byte[] bytes, int offset, int length) {
        if (length <= 32) {
            if (length <= 16) {
                return hashLength0to16(bytes, offset, length);
            }
            return hashLength17to32(bytes, offset, length);
        } else if (length <= 64) {
            return hashLength33To64(bytes, offset, length);
        } else {
            return hashLength65Plus(bytes, offset, length);
        }
    }

    private static long shiftMix(long val) {
        return (val >>> 47) ^ val;
    }

    private static long hashLength16(long u, long v, long mul) {
        long a2 = (u ^ v) * mul;
        long b = (v ^ (a2 ^ (a2 >>> 47))) * mul;
        return (b ^ (b >>> 47)) * mul;
    }

    private static void weakHashLength32WithSeeds(byte[] bytes, int offset, long seedA, long seedB, long[] output) {
        long part1 = LittleEndianByteArray.load64(bytes, offset);
        long part2 = LittleEndianByteArray.load64(bytes, offset + 8);
        long part3 = LittleEndianByteArray.load64(bytes, offset + 16);
        long part4 = LittleEndianByteArray.load64(bytes, offset + 24);
        long seedA2 = seedA + part1;
        long seedA3 = seedA2 + part2 + part3;
        long seedB2 = Long.rotateRight(seedB + seedA2 + part4, 21) + Long.rotateRight(seedA3, 44);
        output[0] = seedA3 + part4;
        output[1] = seedB2 + seedA2;
    }

    private static long hashLength0to16(byte[] bytes, int offset, int length) {
        if (length >= 8) {
            long mul = ((long) (length * 2)) + K2;
            long a2 = K2 + LittleEndianByteArray.load64(bytes, offset);
            long b = LittleEndianByteArray.load64(bytes, (offset + length) - 8);
            return hashLength16((Long.rotateRight(b, 37) * mul) + a2, (Long.rotateRight(a2, 25) + b) * mul, mul);
        } else if (length >= 4) {
            return hashLength16(((long) length) + ((((long) LittleEndianByteArray.load32(bytes, offset)) & 4294967295L) << 3), ((long) LittleEndianByteArray.load32(bytes, (offset + length) - 4)) & 4294967295L, ((long) (length * 2)) + K2);
        } else if (length <= 0) {
            return K2;
        } else {
            return shiftMix((((long) ((bytes[offset] & 255) + ((bytes[offset + (length >> 1)] & 255) << 8))) * K2) ^ (((long) (((bytes[offset + (length - 1)] & 255) << 2) + length)) * K0)) * K2;
        }
    }

    private static long hashLength17to32(byte[] bytes, int offset, int length) {
        long mul = ((long) (length * 2)) + K2;
        long a2 = LittleEndianByteArray.load64(bytes, offset) * K1;
        long b = LittleEndianByteArray.load64(bytes, offset + 8);
        long c = LittleEndianByteArray.load64(bytes, (offset + length) - 8) * mul;
        return hashLength16(Long.rotateRight(a2 + b, 43) + Long.rotateRight(c, 30) + (LittleEndianByteArray.load64(bytes, (offset + length) - 16) * K2), Long.rotateRight(K2 + b, 18) + a2 + c, mul);
    }

    private static long hashLength33To64(byte[] bytes, int offset, int length) {
        long mul = ((long) (length * 2)) + K2;
        long a2 = LittleEndianByteArray.load64(bytes, offset) * K2;
        long b = LittleEndianByteArray.load64(bytes, offset + 8);
        long c = LittleEndianByteArray.load64(bytes, (offset + length) - 8) * mul;
        long y = Long.rotateRight(a2 + b, 43) + Long.rotateRight(c, 30) + (LittleEndianByteArray.load64(bytes, (offset + length) - 16) * K2);
        long z = hashLength16(y, Long.rotateRight(K2 + b, 18) + a2 + c, mul);
        long e = LittleEndianByteArray.load64(bytes, offset + 16) * mul;
        long f = LittleEndianByteArray.load64(bytes, offset + 24);
        long g = (y + LittleEndianByteArray.load64(bytes, (offset + length) - 32)) * mul;
        return hashLength16(Long.rotateRight(e + f, 43) + Long.rotateRight(g, 30) + ((z + LittleEndianByteArray.load64(bytes, (offset + length) - 24)) * mul), e + Long.rotateRight(f + a2, 18) + g, mul);
    }

    private static long hashLength65Plus(byte[] bytes, int offset, int length) {
        long z = 2480279821605975764L;
        long z2 = shiftMix((2480279821605975764L * K2) + 113) * K2;
        long[] v = new long[2];
        long[] w = new long[2];
        long x = (K2 * 81) + LittleEndianByteArray.load64(bytes, offset);
        int end = offset + (((length - 1) / 64) * 64);
        int last64offset = (((length - 1) & 63) + end) - 63;
        long z3 = z2;
        int offset2 = offset;
        while (true) {
            long x2 = Long.rotateRight(x + z + v[0] + LittleEndianByteArray.load64(bytes, offset2 + 8), 37) * K1;
            long y = Long.rotateRight(v[1] + z + LittleEndianByteArray.load64(bytes, offset2 + 48), 42) * K1;
            long x3 = x2 ^ w[1];
            long y2 = y + v[0] + LittleEndianByteArray.load64(bytes, offset2 + 40);
            long z4 = Long.rotateRight(w[0] + z3, 33) * K1;
            weakHashLength32WithSeeds(bytes, offset2, v[1] * K1, x3 + w[0], v);
            weakHashLength32WithSeeds(bytes, offset2 + 32, z4 + w[1], y2 + LittleEndianByteArray.load64(bytes, offset2 + 16), w);
            x = z4;
            offset2 += 64;
            if (offset2 == end) {
                long mul = ((255 & x3) << 1) + K1;
                w[0] = w[0] + ((long) ((length - 1) & 63));
                v[0] = v[0] + w[0];
                w[0] = w[0] + v[0];
                long x4 = (w[1] * 9) ^ (Long.rotateRight(((x + y2) + v[0]) + LittleEndianByteArray.load64(bytes, last64offset + 8), 37) * mul);
                long y3 = (v[0] * 9) + LittleEndianByteArray.load64(bytes, last64offset + 40) + (Long.rotateRight(y2 + v[1] + LittleEndianByteArray.load64(bytes, last64offset + 48), 42) * mul);
                long z5 = Long.rotateRight(w[0] + x3, 33) * mul;
                weakHashLength32WithSeeds(bytes, last64offset, v[1] * mul, w[0] + x4, v);
                weakHashLength32WithSeeds(bytes, last64offset + 32, z5 + w[1], LittleEndianByteArray.load64(bytes, last64offset + 16) + y3, w);
                return hashLength16(hashLength16(v[0], w[0], mul) + (shiftMix(y3) * K0) + x4, hashLength16(v[1], w[1], mul) + z5, mul);
            }
            z3 = x3;
            v = v;
            z = y2;
        }
    }
}
