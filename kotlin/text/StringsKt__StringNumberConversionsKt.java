package kotlin.text;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: StringNumberConversions.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0000\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\n\n\u0002\b\u0003\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0000\u001a\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u0006\u001a\u001b\u0010\u0004\u001a\u0004\u0018\u00010\u0005*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\t\u001a\u0013\u0010\n\u001a\u0004\u0018\u00010\b*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u000b\u001a\u001b\u0010\n\u001a\u0004\u0018\u00010\b*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\f\u001a\u0013\u0010\r\u001a\u0004\u0018\u00010\u000e*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u000f\u001a\u001b\u0010\r\u001a\u0004\u0018\u00010\u000e*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\u0010\u001a\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u0012*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u0013\u001a\u001b\u0010\u0011\u001a\u0004\u0018\u00010\u0012*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\u0014¨\u0006\u0015"}, d2 = {"numberFormatError", "", "input", "", "toByteOrNull", "", "(Ljava/lang/String;)Ljava/lang/Byte;", "radix", "", "(Ljava/lang/String;I)Ljava/lang/Byte;", "toIntOrNull", "(Ljava/lang/String;)Ljava/lang/Integer;", "(Ljava/lang/String;I)Ljava/lang/Integer;", "toLongOrNull", "", "(Ljava/lang/String;)Ljava/lang/Long;", "(Ljava/lang/String;I)Ljava/lang/Long;", "toShortOrNull", "", "(Ljava/lang/String;)Ljava/lang/Short;", "(Ljava/lang/String;I)Ljava/lang/Short;", "kotlin-stdlib"}, k = 5, mv = {1, 1, 16}, xi = 1, xs = "kotlin/text/StringsKt")
/* loaded from: classes3.dex */
public class StringsKt__StringNumberConversionsKt extends StringsKt__StringNumberConversionsJVMKt {
    public static final Byte toByteOrNull(String $this$toByteOrNull) {
        Intrinsics.checkParameterIsNotNull($this$toByteOrNull, "$this$toByteOrNull");
        return StringsKt.toByteOrNull($this$toByteOrNull, 10);
    }

    public static final Byte toByteOrNull(String $this$toByteOrNull, int radix) {
        int intValue;
        Intrinsics.checkParameterIsNotNull($this$toByteOrNull, "$this$toByteOrNull");
        Integer intOrNull = StringsKt.toIntOrNull($this$toByteOrNull, radix);
        if (intOrNull == null || (intValue = intOrNull.intValue()) < -128 || intValue > 127) {
            return null;
        }
        return Byte.valueOf((byte) intValue);
    }

    public static final Short toShortOrNull(String $this$toShortOrNull) {
        Intrinsics.checkParameterIsNotNull($this$toShortOrNull, "$this$toShortOrNull");
        return StringsKt.toShortOrNull($this$toShortOrNull, 10);
    }

    public static final Short toShortOrNull(String $this$toShortOrNull, int radix) {
        int intValue;
        Intrinsics.checkParameterIsNotNull($this$toShortOrNull, "$this$toShortOrNull");
        Integer intOrNull = StringsKt.toIntOrNull($this$toShortOrNull, radix);
        if (intOrNull == null || (intValue = intOrNull.intValue()) < -32768 || intValue > 32767) {
            return null;
        }
        return Short.valueOf((short) intValue);
    }

    public static final Integer toIntOrNull(String $this$toIntOrNull) {
        Intrinsics.checkParameterIsNotNull($this$toIntOrNull, "$this$toIntOrNull");
        return StringsKt.toIntOrNull($this$toIntOrNull, 10);
    }

    /* JADX INFO: Multiple debug info for r7v2 'limitBeforeMul'  int: [D('limitBeforeMul' int), D('i' int)] */
    public static final Integer toIntOrNull(String $this$toIntOrNull, int radix) {
        int limit;
        boolean isNegative;
        int start;
        int result;
        Intrinsics.checkParameterIsNotNull($this$toIntOrNull, "$this$toIntOrNull");
        CharsKt.checkRadix(radix);
        int length = $this$toIntOrNull.length();
        if (length == 0) {
            return null;
        }
        char firstChar = $this$toIntOrNull.charAt(0);
        if (firstChar >= '0') {
            start = 0;
            isNegative = false;
            limit = -2147483647;
        } else if (length == 1) {
            return null;
        } else {
            start = 1;
            if (firstChar == '-') {
                isNegative = true;
                limit = Integer.MIN_VALUE;
            } else if (firstChar != '+') {
                return null;
            } else {
                isNegative = false;
                limit = -2147483647;
            }
        }
        int result2 = 0;
        int limitBeforeMul = -59652323;
        for (int i = start; i < length; i++) {
            int digit = CharsKt.digitOf($this$toIntOrNull.charAt(i), radix);
            if (digit < 0) {
                return null;
            }
            if ((result2 < limitBeforeMul && (limitBeforeMul != -59652323 || result2 < (limitBeforeMul = limit / radix))) || (result = result2 * radix) < limit + digit) {
                return null;
            }
            result2 = result - digit;
        }
        return isNegative ? Integer.valueOf(result2) : Integer.valueOf(-result2);
    }

    public static final Long toLongOrNull(String $this$toLongOrNull) {
        Intrinsics.checkParameterIsNotNull($this$toLongOrNull, "$this$toLongOrNull");
        return StringsKt.toLongOrNull($this$toLongOrNull, 10);
    }

    /* JADX INFO: Multiple debug info for r11v2 int: [D('i' int), D('limitBeforeMul' long)] */
    public static final Long toLongOrNull(String $this$toLongOrNull, int radix) {
        long limit;
        boolean isNegative;
        int start;
        int start2;
        Intrinsics.checkParameterIsNotNull($this$toLongOrNull, "$this$toLongOrNull");
        CharsKt.checkRadix(radix);
        int length = $this$toLongOrNull.length();
        Long l = null;
        if (length == 0) {
            return null;
        }
        char firstChar = $this$toLongOrNull.charAt(0);
        if (firstChar >= '0') {
            start = 0;
            isNegative = false;
            limit = -9223372036854775807L;
        } else if (length == 1) {
            return null;
        } else {
            start = 1;
            if (firstChar == '-') {
                isNegative = true;
                limit = Long.MIN_VALUE;
            } else if (firstChar != '+') {
                return null;
            } else {
                isNegative = false;
                limit = -9223372036854775807L;
            }
        }
        long result = 0;
        long limitBeforeMul = -256204778801521550L;
        int i = start;
        while (i < length) {
            int digit = CharsKt.digitOf($this$toLongOrNull.charAt(i), radix);
            if (digit < 0) {
                return l;
            }
            if (result >= limitBeforeMul) {
                start2 = start;
            } else if (limitBeforeMul != -256204778801521550L) {
                return l;
            } else {
                start2 = start;
                long limitBeforeMul2 = limit / ((long) radix);
                if (result < limitBeforeMul2) {
                    return null;
                }
                limitBeforeMul = limitBeforeMul2;
            }
            long result2 = result * ((long) radix);
            if (result2 < ((long) digit) + limit) {
                return null;
            }
            result = result2 - ((long) digit);
            i++;
            start = start2;
            l = null;
        }
        return isNegative ? Long.valueOf(result) : Long.valueOf(-result);
    }

    public static final Void numberFormatError(String input) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        throw new NumberFormatException("Invalid number format: '" + input + '\'');
    }
}
