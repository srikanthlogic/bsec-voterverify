package okio;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okio.internal.ByteStringKt;
/* compiled from: -Util.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0005\n\u0002\b\u0002\n\u0002\u0010\n\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\u001a0\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0000\u001a \u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\b\u001a\u00020\fH\u0000\u001a\u0019\u0010\u000e\u001a\u00020\f2\u0006\u0010\u0002\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\fH\u0080\b\u001a\u0019\u0010\u000e\u001a\u00020\f2\u0006\u0010\u0002\u001a\u00020\f2\u0006\u0010\u0006\u001a\u00020\u0005H\u0080\b\u001a\u0015\u0010\u000f\u001a\u00020\u0005*\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0005H\u0080\f\u001a\u0015\u0010\u000f\u001a\u00020\f*\u00020\u00102\u0006\u0010\u0011\u001a\u00020\fH\u0080\f\u001a\u0015\u0010\u000f\u001a\u00020\f*\u00020\u00052\u0006\u0010\u0011\u001a\u00020\fH\u0080\f\u001a\f\u0010\u0012\u001a\u00020\u0005*\u00020\u0005H\u0000\u001a\f\u0010\u0012\u001a\u00020\f*\u00020\fH\u0000\u001a\f\u0010\u0012\u001a\u00020\u0013*\u00020\u0013H\u0000\u001a\u0015\u0010\u0014\u001a\u00020\u0005*\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0005H\u0080\f\u001a\u0015\u0010\u0015\u001a\u00020\u0005*\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0005H\u0080\f\u001a\f\u0010\u0016\u001a\u00020\u0017*\u00020\u0010H\u0000\u001a\f\u0010\u0016\u001a\u00020\u0017*\u00020\u0005H\u0000\u001a\f\u0010\u0016\u001a\u00020\u0017*\u00020\fH\u0000¨\u0006\u0018"}, d2 = {"arrayRangeEquals", "", "a", "", "aOffset", "", "b", "bOffset", "byteCount", "checkOffsetAndCount", "", "size", "", "offset", "minOf", "and", "", "other", "reverseBytes", "", "shl", "shr", "toHexString", "", "okio"}, k = 2, mv = {1, 1, 16})
/* renamed from: okio.-Util */
/* loaded from: classes3.dex */
public final class Util {
    public static final void checkOffsetAndCount(long size, long offset, long byteCount) {
        if ((offset | byteCount) < 0 || offset > size || size - offset < byteCount) {
            throw new ArrayIndexOutOfBoundsException("size=" + size + " offset=" + offset + " byteCount=" + byteCount);
        }
    }

    public static final short reverseBytes(short $this$reverseBytes) {
        int i = 65535 & $this$reverseBytes;
        return (short) (((65280 & i) >>> 8) | ((i & 255) << 8));
    }

    public static final int reverseBytes(int $this$reverseBytes) {
        return ((-16777216 & $this$reverseBytes) >>> 24) | ((16711680 & $this$reverseBytes) >>> 8) | ((65280 & $this$reverseBytes) << 8) | (($this$reverseBytes & 255) << 24);
    }

    public static final long reverseBytes(long $this$reverseBytes) {
        return ((-72057594037927936L & $this$reverseBytes) >>> 56) | ((71776119061217280L & $this$reverseBytes) >>> 40) | ((280375465082880L & $this$reverseBytes) >>> 24) | ((1095216660480L & $this$reverseBytes) >>> 8) | ((4278190080L & $this$reverseBytes) << 8) | ((16711680 & $this$reverseBytes) << 24) | ((65280 & $this$reverseBytes) << 40) | ((255 & $this$reverseBytes) << 56);
    }

    public static final int shr(byte $this$shr, int other) {
        return $this$shr >> other;
    }

    public static final int shl(byte $this$shl, int other) {
        return $this$shl << other;
    }

    public static final int and(byte $this$and, int other) {
        return $this$and & other;
    }

    public static final long and(byte $this$and, long other) {
        return ((long) $this$and) & other;
    }

    public static final long and(int $this$and, long other) {
        return ((long) $this$and) & other;
    }

    public static final long minOf(long a2, int b) {
        return Math.min(a2, (long) b);
    }

    public static final long minOf(int a2, long b) {
        return Math.min((long) a2, b);
    }

    public static final boolean arrayRangeEquals(byte[] a2, int aOffset, byte[] b, int bOffset, int byteCount) {
        Intrinsics.checkParameterIsNotNull(a2, "a");
        Intrinsics.checkParameterIsNotNull(b, "b");
        for (int i = 0; i < byteCount; i++) {
            if (a2[i + aOffset] != b[i + bOffset]) {
                return false;
            }
        }
        return true;
    }

    public static final String toHexString(byte $this$toHexString) {
        return new String(new char[]{ByteStringKt.getHEX_DIGIT_CHARS()[($this$toHexString >> 4) & 15], ByteStringKt.getHEX_DIGIT_CHARS()[15 & $this$toHexString]});
    }

    public static final String toHexString(int $this$toHexString) {
        if ($this$toHexString == 0) {
            return "0";
        }
        char[] result = {ByteStringKt.getHEX_DIGIT_CHARS()[($this$toHexString >> 28) & 15], ByteStringKt.getHEX_DIGIT_CHARS()[($this$toHexString >> 24) & 15], ByteStringKt.getHEX_DIGIT_CHARS()[($this$toHexString >> 20) & 15], ByteStringKt.getHEX_DIGIT_CHARS()[($this$toHexString >> 16) & 15], ByteStringKt.getHEX_DIGIT_CHARS()[($this$toHexString >> 12) & 15], ByteStringKt.getHEX_DIGIT_CHARS()[($this$toHexString >> 8) & 15], ByteStringKt.getHEX_DIGIT_CHARS()[($this$toHexString >> 4) & 15], ByteStringKt.getHEX_DIGIT_CHARS()[$this$toHexString & 15]};
        int i = 0;
        while (i < result.length && result[i] == '0') {
            i++;
        }
        return new String(result, i, result.length - i);
    }

    public static final String toHexString(long $this$toHexString) {
        if ($this$toHexString == 0) {
            return "0";
        }
        char[] result = {ByteStringKt.getHEX_DIGIT_CHARS()[(int) (($this$toHexString >> 60) & 15)], ByteStringKt.getHEX_DIGIT_CHARS()[(int) (($this$toHexString >> 56) & 15)], ByteStringKt.getHEX_DIGIT_CHARS()[(int) (($this$toHexString >> 52) & 15)], ByteStringKt.getHEX_DIGIT_CHARS()[(int) (($this$toHexString >> 48) & 15)], ByteStringKt.getHEX_DIGIT_CHARS()[(int) (($this$toHexString >> 44) & 15)], ByteStringKt.getHEX_DIGIT_CHARS()[(int) (($this$toHexString >> 40) & 15)], ByteStringKt.getHEX_DIGIT_CHARS()[(int) (($this$toHexString >> 36) & 15)], ByteStringKt.getHEX_DIGIT_CHARS()[(int) (($this$toHexString >> 32) & 15)], ByteStringKt.getHEX_DIGIT_CHARS()[(int) (($this$toHexString >> 28) & 15)], ByteStringKt.getHEX_DIGIT_CHARS()[(int) (($this$toHexString >> 24) & 15)], ByteStringKt.getHEX_DIGIT_CHARS()[(int) (($this$toHexString >> 20) & 15)], ByteStringKt.getHEX_DIGIT_CHARS()[(int) (($this$toHexString >> 16) & 15)], ByteStringKt.getHEX_DIGIT_CHARS()[(int) (($this$toHexString >> 12) & 15)], ByteStringKt.getHEX_DIGIT_CHARS()[(int) (($this$toHexString >> 8) & 15)], ByteStringKt.getHEX_DIGIT_CHARS()[(int) (($this$toHexString >> 4) & 15)], ByteStringKt.getHEX_DIGIT_CHARS()[(int) ($this$toHexString & 15)]};
        int i = 0;
        while (i < result.length && result[i] == '0') {
            i++;
        }
        return new String(result, i, result.length - i);
    }
}
