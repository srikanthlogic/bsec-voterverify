package com.google.common.math;

import java.math.BigInteger;
import java.math.RoundingMode;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes3.dex */
final class MathPreconditions {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int checkPositive(@NullableDecl String role, int x) {
        if (x > 0) {
            return x;
        }
        throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long checkPositive(@NullableDecl String role, long x) {
        if (x > 0) {
            return x;
        }
        throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BigInteger checkPositive(@NullableDecl String role, BigInteger x) {
        if (x.signum() > 0) {
            return x;
        }
        throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int checkNonNegative(@NullableDecl String role, int x) {
        if (x >= 0) {
            return x;
        }
        throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long checkNonNegative(@NullableDecl String role, long x) {
        if (x >= 0) {
            return x;
        }
        throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BigInteger checkNonNegative(@NullableDecl String role, BigInteger x) {
        if (x.signum() >= 0) {
            return x;
        }
        throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static double checkNonNegative(@NullableDecl String role, double x) {
        if (x >= 0.0d) {
            return x;
        }
        throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkRoundingUnnecessary(boolean condition) {
        if (!condition) {
            throw new ArithmeticException("mode was UNNECESSARY, but rounding was necessary");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkInRangeForRoundingInputs(boolean condition, double input, RoundingMode mode) {
        if (!condition) {
            throw new ArithmeticException("rounded value is out of range for input " + input + " and rounding mode " + mode);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkNoOverflow(boolean condition, String methodName, int a2, int b) {
        if (!condition) {
            throw new ArithmeticException("overflow: " + methodName + "(" + a2 + ", " + b + ")");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkNoOverflow(boolean condition, String methodName, long a2, long b) {
        if (!condition) {
            throw new ArithmeticException("overflow: " + methodName + "(" + a2 + ", " + b + ")");
        }
    }

    private MathPreconditions() {
    }
}
