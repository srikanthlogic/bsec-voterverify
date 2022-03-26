package com.google.common.math;

import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedLongs;
import com.nitgen.SDK.AndroidBSP.NBioBSPJNI;
import java.math.RoundingMode;
import okhttp3.internal.connection.RealConnection;
import org.zz.protocol.MXErrCode;
/* loaded from: classes3.dex */
public final class LongMath {
    static final long FLOOR_SQRT_MAX_LONG = 3037000499L;
    static final long MAX_POWER_OF_SQRT2_UNSIGNED = -5402926248376769404L;
    static final long MAX_SIGNED_POWER_OF_TWO = 4611686018427387904L;
    private static final int SIEVE_30 = -545925251;
    static final byte[] maxLog10ForLeadingZeros = {19, Ascii.DC2, Ascii.DC2, Ascii.DC2, Ascii.DC2, 17, 17, 17, Ascii.DLE, Ascii.DLE, Ascii.DLE, Ascii.SI, Ascii.SI, Ascii.SI, Ascii.SI, Ascii.SO, Ascii.SO, Ascii.SO, Ascii.CR, Ascii.CR, Ascii.CR, Ascii.FF, Ascii.FF, Ascii.FF, Ascii.FF, Ascii.VT, Ascii.VT, Ascii.VT, 10, 10, 10, 9, 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0};
    static final long[] powersOf10 = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000, RealConnection.IDLE_CONNECTION_HEALTHY_NS, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L};
    static final long[] halfPowersOf10 = {3, 31, 316, 3162, 31622, 316227, 3162277, 31622776, 316227766, 3162277660L, 31622776601L, 316227766016L, 3162277660168L, 31622776601683L, 316227766016837L, 3162277660168379L, 31622776601683793L, 316227766016837933L, 3162277660168379331L};
    static final long[] factorials = {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L};
    static final int[] biggestBinomials = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 3810779, 121977, 16175, 4337, 1733, 887, 534, 361, NBioBSPJNI.ERROR.NBioAPIERROR_LOWVERSION_DRIVER, 206, MXErrCode.ERR_INVALID_PARAMETER_TEE, 143, 125, 111, 101, 94, 88, 83, 79, 76, 74, 72, 70, 69, 68, 67, 67, 66, 66, 66, 66};
    static final int[] biggestSimpleBinomials = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 2642246, 86251, 11724, 3218, 1313, 684, 419, 287, 214, MXErrCode.ERR_INVALID_PARAMETER_TEE, 139, 119, 105, 95, 87, 81, 76, 73, 70, 68, 66, 64, 63, 62, 62, 61, 61, 61};
    private static final long[][] millerRabinBaseSets = {new long[]{291830, 126401071349994536L}, new long[]{885594168, 725270293939359937L, 3569819667048198375L}, new long[]{273919523040L, 15, 7363882082L, 992620450144556L}, new long[]{47636622961200L, 2, 2570940, 211991001, 3749873356L}, new long[]{7999252175582850L, 2, 4130806001517L, 149795463772692060L, 186635894390467037L, 3967304179347715805L}, new long[]{585226005592931976L, 2, 123635709730000L, 9233062284813009L, 43835965440333360L, 761179012939631437L, 1263739024124850375L}, new long[]{Long.MAX_VALUE, 2, 325, 9375, 28178, 450775, 9780504, 1795265022}};

    public static long ceilingPowerOfTwo(long x) {
        MathPreconditions.checkPositive("x", x);
        if (x <= 4611686018427387904L) {
            return 1 << (-Long.numberOfLeadingZeros(x - 1));
        }
        throw new ArithmeticException("ceilingPowerOfTwo(" + x + ") is not representable as a long");
    }

    public static long floorPowerOfTwo(long x) {
        MathPreconditions.checkPositive("x", x);
        return 1 << (63 - Long.numberOfLeadingZeros(x));
    }

    public static boolean isPowerOfTwo(long x) {
        boolean z = true;
        boolean z2 = x > 0;
        if (((x - 1) & x) != 0) {
            z = false;
        }
        return z2 & z;
    }

    static int lessThanBranchFree(long x, long y) {
        return (int) ((~(~(x - y))) >>> 63);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.common.math.LongMath$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode = new int[RoundingMode.values().length];

        static {
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UNNECESSARY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.DOWN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.FLOOR.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UP.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.CEILING.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_DOWN.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_UP.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_EVEN.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static int log2(long x, RoundingMode mode) {
        MathPreconditions.checkPositive("x", x);
        switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
                break;
            case 2:
            case 3:
                break;
            case 4:
            case 5:
                return 64 - Long.numberOfLeadingZeros(x - 1);
            case 6:
            case 7:
            case 8:
                int leadingZeros = Long.numberOfLeadingZeros(x);
                return lessThanBranchFree(MAX_POWER_OF_SQRT2_UNSIGNED >>> leadingZeros, x) + (63 - leadingZeros);
            default:
                throw new AssertionError("impossible");
        }
        return 63 - Long.numberOfLeadingZeros(x);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static int log10(long x, RoundingMode mode) {
        MathPreconditions.checkPositive("x", x);
        int logFloor = log10Floor(x);
        long floorPow = powersOf10[logFloor];
        switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(x == floorPow);
                break;
            case 2:
            case 3:
                break;
            case 4:
            case 5:
                return lessThanBranchFree(floorPow, x) + logFloor;
            case 6:
            case 7:
            case 8:
                return lessThanBranchFree(halfPowersOf10[logFloor], x) + logFloor;
            default:
                throw new AssertionError();
        }
        return logFloor;
    }

    static int log10Floor(long x) {
        byte b = maxLog10ForLeadingZeros[Long.numberOfLeadingZeros(x)];
        return b - lessThanBranchFree(x, powersOf10[b]);
    }

    public static long pow(long b, int k) {
        MathPreconditions.checkNonNegative("exponent", k);
        if (-2 > b || b > 2) {
            long accum = 1;
            while (k != 0) {
                if (k == 1) {
                    return accum * b;
                }
                accum *= (k & 1) == 0 ? 1 : b;
                b *= b;
                k >>= 1;
            }
            return accum;
        }
        int i = (int) b;
        if (i != -2) {
            if (i != -1) {
                if (i != 0) {
                    if (i == 1) {
                        return 1;
                    }
                    if (i != 2) {
                        throw new AssertionError();
                    } else if (k < 64) {
                        return 1 << k;
                    } else {
                        return 0;
                    }
                } else if (k == 0) {
                    return 1;
                } else {
                    return 0;
                }
            } else if ((k & 1) == 0) {
                return 1;
            } else {
                return -1;
            }
        } else if (k < 64) {
            return (k & 1) == 0 ? 1 << k : -(1 << k);
        } else {
            return 0;
        }
    }

    public static long sqrt(long x, RoundingMode mode) {
        MathPreconditions.checkNonNegative("x", x);
        if (fitsInInt(x)) {
            return (long) IntMath.sqrt((int) x, mode);
        }
        long guess = (long) Math.sqrt((double) x);
        long guessSquared = guess * guess;
        boolean z = true;
        int i = 1;
        switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                if (guessSquared != x) {
                    z = false;
                }
                MathPreconditions.checkRoundingUnnecessary(z);
                return guess;
            case 2:
            case 3:
                if (x < guessSquared) {
                    return guess - 1;
                }
                return guess;
            case 4:
            case 5:
                if (x > guessSquared) {
                    return 1 + guess;
                }
                return guess;
            case 6:
            case 7:
            case 8:
                if (x >= guessSquared) {
                    i = 0;
                }
                long sqrtFloor = guess - ((long) i);
                return ((long) lessThanBranchFree((sqrtFloor * sqrtFloor) + sqrtFloor, x)) + sqrtFloor;
            default:
                throw new AssertionError();
        }
    }

    public static long divide(long p, long q, RoundingMode mode) {
        boolean increment;
        Preconditions.checkNotNull(mode);
        long div = p / q;
        long rem = p - (q * div);
        if (rem == 0) {
            return div;
        }
        boolean z = true;
        int signum = ((int) ((p ^ q) >> 63)) | 1;
        switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                if (rem != 0) {
                    z = false;
                }
                MathPreconditions.checkRoundingUnnecessary(z);
            case 2:
                increment = false;
                break;
            case 3:
                if (signum >= 0) {
                    z = false;
                }
                increment = z;
                break;
            case 4:
                increment = true;
                break;
            case 5:
                if (signum <= 0) {
                    z = false;
                }
                increment = z;
                break;
            case 6:
            case 7:
            case 8:
                long absRem = Math.abs(rem);
                long cmpRemToHalfDivisor = absRem - (Math.abs(q) - absRem);
                if (cmpRemToHalfDivisor != 0) {
                    if (cmpRemToHalfDivisor <= 0) {
                        z = false;
                    }
                    increment = z;
                    break;
                } else {
                    boolean z2 = mode == RoundingMode.HALF_UP;
                    boolean z3 = mode == RoundingMode.HALF_EVEN;
                    if ((div & 1) == 0) {
                        z = false;
                    }
                    increment = (z3 & z) | z2;
                    break;
                }
            default:
                throw new AssertionError();
        }
        return increment ? ((long) signum) + div : div;
    }

    public static int mod(long x, int m) {
        return (int) mod(x, (long) m);
    }

    public static long mod(long x, long m) {
        if (m > 0) {
            long result = x % m;
            return result >= 0 ? result : result + m;
        }
        throw new ArithmeticException("Modulus must be positive");
    }

    public static long gcd(long a2, long b) {
        MathPreconditions.checkNonNegative("a", a2);
        MathPreconditions.checkNonNegative("b", b);
        if (a2 == 0) {
            return b;
        }
        if (b == 0) {
            return a2;
        }
        int aTwos = Long.numberOfTrailingZeros(a2);
        long a3 = a2 >> aTwos;
        int bTwos = Long.numberOfTrailingZeros(b);
        long b2 = b >> bTwos;
        while (a3 != b2) {
            long delta = a3 - b2;
            long minDeltaOrZero = (delta >> 63) & delta;
            long a4 = (delta - minDeltaOrZero) - minDeltaOrZero;
            b2 += minDeltaOrZero;
            a3 = a4 >> Long.numberOfTrailingZeros(a4);
        }
        return a3 << Math.min(aTwos, bTwos);
    }

    public static long checkedAdd(long a2, long b) {
        long result = a2 + b;
        boolean z = true;
        boolean z2 = (a2 ^ b) < 0;
        if ((a2 ^ result) < 0) {
            z = false;
        }
        MathPreconditions.checkNoOverflow(z2 | z, "checkedAdd", a2, b);
        return result;
    }

    public static long checkedSubtract(long a2, long b) {
        long result = a2 - b;
        boolean z = true;
        boolean z2 = (a2 ^ b) >= 0;
        if ((a2 ^ result) < 0) {
            z = false;
        }
        MathPreconditions.checkNoOverflow(z2 | z, "checkedSubtract", a2, b);
        return result;
    }

    public static long checkedMultiply(long a2, long b) {
        int leadingZeros = Long.numberOfLeadingZeros(a2) + Long.numberOfLeadingZeros(~a2) + Long.numberOfLeadingZeros(b) + Long.numberOfLeadingZeros(~b);
        if (leadingZeros > 65) {
            return a2 * b;
        }
        MathPreconditions.checkNoOverflow(leadingZeros >= 64, "checkedMultiply", a2, b);
        MathPreconditions.checkNoOverflow((a2 >= 0) | (b != Long.MIN_VALUE), "checkedMultiply", a2, b);
        long result = a2 * b;
        MathPreconditions.checkNoOverflow(a2 == 0 || result / a2 == b, "checkedMultiply", a2, b);
        return result;
    }

    public static long checkedPow(long b, int k) {
        long b2 = b;
        int k2 = k;
        MathPreconditions.checkNonNegative("exponent", k2);
        if ((b2 >= -2) && (b2 <= 2)) {
            int i = (int) b2;
            if (i == -2) {
                MathPreconditions.checkNoOverflow(k2 < 64, "checkedPow", b, (long) k2);
                return (k2 & 1) == 0 ? 1 << k2 : -1 << k2;
            } else if (i == -1) {
                return (k2 & 1) == 0 ? 1 : -1;
            } else {
                if (i != 0) {
                    if (i == 1) {
                        return 1;
                    }
                    if (i == 2) {
                        MathPreconditions.checkNoOverflow(k2 < 63, "checkedPow", b, (long) k2);
                        return 1 << k2;
                    }
                    throw new AssertionError();
                } else if (k2 == 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        } else {
            long accum = 1;
            while (k2 != 0) {
                if (k2 == 1) {
                    return checkedMultiply(accum, b2);
                }
                if ((k2 & 1) != 0) {
                    accum = checkedMultiply(accum, b2);
                }
                k2 >>= 1;
                if (k2 > 0) {
                    MathPreconditions.checkNoOverflow(-3037000499L <= b2 && b2 <= FLOOR_SQRT_MAX_LONG, "checkedPow", b2, (long) k2);
                    b2 *= b2;
                }
            }
            return accum;
        }
    }

    public static long saturatedAdd(long a2, long b) {
        long naiveSum = a2 + b;
        boolean z = true;
        boolean z2 = (a2 ^ b) < 0;
        if ((a2 ^ naiveSum) < 0) {
            z = false;
        }
        if (z2 || z) {
            return naiveSum;
        }
        return ((naiveSum >>> 63) ^ 1) + Long.MAX_VALUE;
    }

    public static long saturatedSubtract(long a2, long b) {
        long naiveDifference = a2 - b;
        boolean z = true;
        boolean z2 = (a2 ^ b) >= 0;
        if ((a2 ^ naiveDifference) < 0) {
            z = false;
        }
        if (z2 || z) {
            return naiveDifference;
        }
        return ((naiveDifference >>> 63) ^ 1) + Long.MAX_VALUE;
    }

    public static long saturatedMultiply(long a2, long b) {
        int leadingZeros = Long.numberOfLeadingZeros(a2) + Long.numberOfLeadingZeros(~a2) + Long.numberOfLeadingZeros(b) + Long.numberOfLeadingZeros(~b);
        if (leadingZeros > 65) {
            return a2 * b;
        }
        long limit = ((a2 ^ b) >>> 63) + Long.MAX_VALUE;
        boolean z = true;
        boolean z2 = leadingZeros < 64;
        boolean z3 = a2 < 0;
        if (b != Long.MIN_VALUE) {
            z = false;
        }
        if (z2 || (z & z3)) {
            return limit;
        }
        long result = a2 * b;
        if (a2 == 0 || result / a2 == b) {
            return result;
        }
        return limit;
    }

    public static long saturatedPow(long b, int k) {
        MathPreconditions.checkNonNegative("exponent", k);
        if ((b >= -2) && (b <= 2)) {
            int i = (int) b;
            if (i != -2) {
                if (i == -1) {
                    return (k & 1) == 0 ? 1 : -1;
                }
                if (i != 0) {
                    if (i == 1) {
                        return 1;
                    }
                    if (i != 2) {
                        throw new AssertionError();
                    } else if (k >= 63) {
                        return Long.MAX_VALUE;
                    } else {
                        return 1 << k;
                    }
                } else if (k == 0) {
                    return 1;
                } else {
                    return 0;
                }
            } else if (k >= 64) {
                return ((long) (k & 1)) + Long.MAX_VALUE;
            } else {
                return (k & 1) == 0 ? 1 << k : -1 << k;
            }
        } else {
            long accum = 1;
            long limit = ((b >>> 63) & ((long) (k & 1))) + Long.MAX_VALUE;
            while (k != 0) {
                if (k == 1) {
                    return saturatedMultiply(accum, b);
                }
                if ((k & 1) != 0) {
                    accum = saturatedMultiply(accum, b);
                }
                k >>= 1;
                if (k > 0) {
                    if ((-3037000499L > b) || (b > FLOOR_SQRT_MAX_LONG)) {
                        return limit;
                    }
                    b *= b;
                }
            }
            return accum;
        }
    }

    public static long factorial(int n) {
        MathPreconditions.checkNonNegative("n", n);
        long[] jArr = factorials;
        if (n < jArr.length) {
            return jArr[n];
        }
        return Long.MAX_VALUE;
    }

    public static long binomial(int n, int k) {
        int k2 = k;
        MathPreconditions.checkNonNegative("n", n);
        MathPreconditions.checkNonNegative("k", k2);
        Preconditions.checkArgument(k2 <= n, "k (%s) > n (%s)", k2, n);
        if (k2 > (n >> 1)) {
            k2 = n - k2;
        }
        if (k2 == 0) {
            return 1;
        }
        if (k2 == 1) {
            return (long) n;
        }
        long[] jArr = factorials;
        if (n < jArr.length) {
            return jArr[n] / (jArr[k2] * jArr[n - k2]);
        }
        int[] iArr = biggestBinomials;
        if (k2 >= iArr.length || n > iArr[k2]) {
            return Long.MAX_VALUE;
        }
        int[] iArr2 = biggestSimpleBinomials;
        if (k2 >= iArr2.length || n > iArr2[k2]) {
            int nBits = log2((long) n, RoundingMode.CEILING);
            int i = 2;
            long result = 1;
            long numerator = (long) n;
            long denominator = 1;
            int numeratorBits = nBits;
            int n2 = n - 1;
            while (i <= k2) {
                if (numeratorBits + nBits < 63) {
                    numerator *= (long) n2;
                    denominator *= (long) i;
                    numeratorBits += nBits;
                } else {
                    numeratorBits = nBits;
                    result = multiplyFraction(result, numerator, denominator);
                    numerator = (long) n2;
                    denominator = (long) i;
                }
                i++;
                n2--;
            }
            return multiplyFraction(result, numerator, denominator);
        }
        int n3 = n - 1;
        long result2 = (long) n;
        for (int i2 = 2; i2 <= k2; i2++) {
            result2 = (result2 * ((long) n3)) / ((long) i2);
            n3--;
        }
        return result2;
    }

    static long multiplyFraction(long x, long numerator, long denominator) {
        if (x == 1) {
            return numerator / denominator;
        }
        long commonDivisor = gcd(x, denominator);
        return (numerator / (denominator / commonDivisor)) * (x / commonDivisor);
    }

    static boolean fitsInInt(long x) {
        return ((long) ((int) x)) == x;
    }

    public static long mean(long x, long y) {
        return (x & y) + ((x ^ y) >> 1);
    }

    public static boolean isPrime(long n) {
        if (n < 2) {
            MathPreconditions.checkNonNegative("n", n);
            return false;
        } else if (n == 2 || n == 3 || n == 5 || n == 7 || n == 11 || n == 13) {
            return true;
        } else {
            if ((SIEVE_30 & (1 << ((int) (n % 30)))) != 0 || n % 7 == 0 || n % 11 == 0 || n % 13 == 0) {
                return false;
            }
            if (n < 289) {
                return true;
            }
            long[][] jArr = millerRabinBaseSets;
            for (long[] baseSet : jArr) {
                if (n <= baseSet[0]) {
                    for (int i = 1; i < baseSet.length; i++) {
                        if (!MillerRabinTester.test(baseSet[i], n)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public enum MillerRabinTester {
        SMALL {
            @Override // com.google.common.math.LongMath.MillerRabinTester
            long mulMod(long a2, long b, long m) {
                return (a2 * b) % m;
            }

            @Override // com.google.common.math.LongMath.MillerRabinTester
            long squareMod(long a2, long m) {
                return (a2 * a2) % m;
            }
        },
        LARGE {
            private long plusMod(long a2, long b, long m) {
                return a2 >= m - b ? (a2 + b) - m : a2 + b;
            }

            private long times2ToThe32Mod(long a2, long m) {
                int remainingPowersOf2 = 32;
                do {
                    int shift = Math.min(remainingPowersOf2, Long.numberOfLeadingZeros(a2));
                    a2 = UnsignedLongs.remainder(a2 << shift, m);
                    remainingPowersOf2 -= shift;
                } while (remainingPowersOf2 > 0);
                return a2;
            }

            @Override // com.google.common.math.LongMath.MillerRabinTester
            long mulMod(long a2, long b, long m) {
                long aHi = a2 >>> 32;
                long bHi = b >>> 32;
                long aLo = a2 & 4294967295L;
                long bLo = b & 4294967295L;
                long result = times2ToThe32Mod(aHi * bHi, m) + (aHi * bLo);
                if (result < 0) {
                    result = UnsignedLongs.remainder(result, m);
                }
                return plusMod(times2ToThe32Mod(result + (aLo * bHi), m), UnsignedLongs.remainder(aLo * bLo, m), m);
            }

            @Override // com.google.common.math.LongMath.MillerRabinTester
            long squareMod(long a2, long m) {
                long hiLo;
                long aHi = a2 >>> 32;
                long aLo = a2 & 4294967295L;
                long result = times2ToThe32Mod(aHi * aHi, m);
                long hiLo2 = aHi * aLo * 2;
                if (hiLo2 < 0) {
                    hiLo = UnsignedLongs.remainder(hiLo2, m);
                } else {
                    hiLo = hiLo2;
                }
                return plusMod(times2ToThe32Mod(result + hiLo, m), UnsignedLongs.remainder(aLo * aLo, m), m);
            }
        };

        abstract long mulMod(long j, long j2, long j3);

        abstract long squareMod(long j, long j2);

        /* synthetic */ MillerRabinTester(AnonymousClass1 x2) {
            this();
        }

        static boolean test(long base, long n) {
            return (n <= LongMath.FLOOR_SQRT_MAX_LONG ? SMALL : LARGE).testWitness(base, n);
        }

        private long powMod(long a2, long p, long m) {
            long res = 1;
            long a3 = a2;
            while (p != 0) {
                if ((1 & p) != 0) {
                    res = mulMod(res, a3, m);
                }
                a3 = squareMod(a3, m);
                p >>= 1;
            }
            return res;
        }

        private boolean testWitness(long base, long n) {
            int r = Long.numberOfTrailingZeros(n - 1);
            long d = (n - 1) >> r;
            long base2 = base % n;
            if (base2 == 0) {
                return true;
            }
            long a2 = powMod(base2, d, n);
            if (a2 == 1) {
                return true;
            }
            int j = 0;
            while (a2 != n - 1) {
                j++;
                if (j == r) {
                    return false;
                }
                a2 = squareMod(a2, n);
            }
            return true;
        }
    }

    private LongMath() {
    }
}
