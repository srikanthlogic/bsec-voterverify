package okio.internal;

import com.facebook.common.util.UriUtil;
import com.google.common.base.Ascii;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okio.Base64;
import okio.Buffer;
import okio.ByteString;
import okio.Platform;
import okio.Util;
import org.apache.commons.io.IOUtils;
/* compiled from: ByteString.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000P\n\u0000\n\u0002\u0010\u0019\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\f\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0018\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0005H\u0002\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0007H\u0080\b\u001a\u0010\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u000eH\u0002\u001a\r\u0010\u000f\u001a\u00020\u0010*\u00020\nH\u0080\b\u001a\r\u0010\u0011\u001a\u00020\u0010*\u00020\nH\u0080\b\u001a\u0015\u0010\u0012\u001a\u00020\u0005*\u00020\n2\u0006\u0010\u0013\u001a\u00020\nH\u0080\b\u001a\u000f\u0010\u0014\u001a\u0004\u0018\u00010\n*\u00020\u0010H\u0080\b\u001a\r\u0010\u0015\u001a\u00020\n*\u00020\u0010H\u0080\b\u001a\r\u0010\u0016\u001a\u00020\n*\u00020\u0010H\u0080\b\u001a\u0015\u0010\u0017\u001a\u00020\u0018*\u00020\n2\u0006\u0010\u0019\u001a\u00020\u0007H\u0080\b\u001a\u0015\u0010\u0017\u001a\u00020\u0018*\u00020\n2\u0006\u0010\u0019\u001a\u00020\nH\u0080\b\u001a\u0017\u0010\u001a\u001a\u00020\u0018*\u00020\n2\b\u0010\u0013\u001a\u0004\u0018\u00010\u001bH\u0080\b\u001a\u0015\u0010\u001c\u001a\u00020\u001d*\u00020\n2\u0006\u0010\u001e\u001a\u00020\u0005H\u0080\b\u001a\r\u0010\u001f\u001a\u00020\u0005*\u00020\nH\u0080\b\u001a\r\u0010 \u001a\u00020\u0005*\u00020\nH\u0080\b\u001a\r\u0010!\u001a\u00020\u0010*\u00020\nH\u0080\b\u001a\u001d\u0010\"\u001a\u00020\u0005*\u00020\n2\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010#\u001a\u00020\u0005H\u0080\b\u001a\r\u0010$\u001a\u00020\u0007*\u00020\nH\u0080\b\u001a\u001d\u0010%\u001a\u00020\u0005*\u00020\n2\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010#\u001a\u00020\u0005H\u0080\b\u001a\u001d\u0010%\u001a\u00020\u0005*\u00020\n2\u0006\u0010\u0013\u001a\u00020\n2\u0006\u0010#\u001a\u00020\u0005H\u0080\b\u001a-\u0010&\u001a\u00020\u0018*\u00020\n2\u0006\u0010'\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010(\u001a\u00020\u00052\u0006\u0010)\u001a\u00020\u0005H\u0080\b\u001a-\u0010&\u001a\u00020\u0018*\u00020\n2\u0006\u0010'\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\n2\u0006\u0010(\u001a\u00020\u00052\u0006\u0010)\u001a\u00020\u0005H\u0080\b\u001a\u0015\u0010*\u001a\u00020\u0018*\u00020\n2\u0006\u0010+\u001a\u00020\u0007H\u0080\b\u001a\u0015\u0010*\u001a\u00020\u0018*\u00020\n2\u0006\u0010+\u001a\u00020\nH\u0080\b\u001a\u001d\u0010,\u001a\u00020\n*\u00020\n2\u0006\u0010-\u001a\u00020\u00052\u0006\u0010.\u001a\u00020\u0005H\u0080\b\u001a\r\u0010/\u001a\u00020\n*\u00020\nH\u0080\b\u001a\r\u00100\u001a\u00020\n*\u00020\nH\u0080\b\u001a\r\u00101\u001a\u00020\u0007*\u00020\nH\u0080\b\u001a\u001d\u00102\u001a\u00020\n*\u00020\u00072\u0006\u0010'\u001a\u00020\u00052\u0006\u0010)\u001a\u00020\u0005H\u0080\b\u001a\r\u00103\u001a\u00020\u0010*\u00020\nH\u0080\b\u001a\r\u00104\u001a\u00020\u0010*\u00020\nH\u0080\b\u001a$\u00105\u001a\u000206*\u00020\n2\u0006\u00107\u001a\u0002082\u0006\u0010'\u001a\u00020\u00052\u0006\u0010)\u001a\u00020\u0005H\u0000\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003¨\u00069"}, d2 = {"HEX_DIGIT_CHARS", "", "getHEX_DIGIT_CHARS", "()[C", "codePointIndexToCharIndex", "", "s", "", "codePointCount", "commonOf", "Lokio/ByteString;", UriUtil.DATA_SCHEME, "decodeHexDigit", "c", "", "commonBase64", "", "commonBase64Url", "commonCompareTo", "other", "commonDecodeBase64", "commonDecodeHex", "commonEncodeUtf8", "commonEndsWith", "", "suffix", "commonEquals", "", "commonGetByte", "", "pos", "commonGetSize", "commonHashCode", "commonHex", "commonIndexOf", "fromIndex", "commonInternalArray", "commonLastIndexOf", "commonRangeEquals", "offset", "otherOffset", "byteCount", "commonStartsWith", "prefix", "commonSubstring", "beginIndex", "endIndex", "commonToAsciiLowercase", "commonToAsciiUppercase", "commonToByteArray", "commonToByteString", "commonToString", "commonUtf8", "commonWrite", "", "buffer", "Lokio/Buffer;", "okio"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class ByteStringKt {
    private static final char[] HEX_DIGIT_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static final /* synthetic */ int access$codePointIndexToCharIndex(byte[] s, int codePointCount) {
        return codePointIndexToCharIndex(s, codePointCount);
    }

    public static final /* synthetic */ int access$decodeHexDigit(char c) {
        return decodeHexDigit(c);
    }

    public static final String commonUtf8(ByteString $this$commonUtf8) {
        Intrinsics.checkParameterIsNotNull($this$commonUtf8, "$this$commonUtf8");
        String result = $this$commonUtf8.getUtf8$okio();
        if (result != null) {
            return result;
        }
        String result2 = Platform.toUtf8String($this$commonUtf8.internalArray$okio());
        $this$commonUtf8.setUtf8$okio(result2);
        return result2;
    }

    public static final String commonBase64(ByteString $this$commonBase64) {
        Intrinsics.checkParameterIsNotNull($this$commonBase64, "$this$commonBase64");
        return Base64.encodeBase64$default($this$commonBase64.getData$okio(), null, 1, null);
    }

    public static final String commonBase64Url(ByteString $this$commonBase64Url) {
        Intrinsics.checkParameterIsNotNull($this$commonBase64Url, "$this$commonBase64Url");
        return Base64.encodeBase64($this$commonBase64Url.getData$okio(), Base64.getBASE64_URL_SAFE());
    }

    public static final char[] getHEX_DIGIT_CHARS() {
        return HEX_DIGIT_CHARS;
    }

    public static final String commonHex(ByteString $this$commonHex) {
        Intrinsics.checkParameterIsNotNull($this$commonHex, "$this$commonHex");
        char[] result = new char[$this$commonHex.getData$okio().length * 2];
        int c = 0;
        byte[] data$okio = $this$commonHex.getData$okio();
        for (byte b : data$okio) {
            int c2 = c + 1;
            result[c] = getHEX_DIGIT_CHARS()[(b >> 4) & 15];
            c = c2 + 1;
            result[c2] = getHEX_DIGIT_CHARS()[15 & b];
        }
        return new String(result);
    }

    public static final ByteString commonToAsciiLowercase(ByteString $this$commonToAsciiLowercase) {
        byte b;
        Intrinsics.checkParameterIsNotNull($this$commonToAsciiLowercase, "$this$commonToAsciiLowercase");
        for (int i = 0; i < $this$commonToAsciiLowercase.getData$okio().length; i++) {
            byte c = $this$commonToAsciiLowercase.getData$okio()[i];
            byte b2 = (byte) 65;
            if (c >= b2 && c <= (b = (byte) 90)) {
                byte[] data$okio = $this$commonToAsciiLowercase.getData$okio();
                byte[] lowercase = Arrays.copyOf(data$okio, data$okio.length);
                Intrinsics.checkExpressionValueIsNotNull(lowercase, "java.util.Arrays.copyOf(this, size)");
                int i2 = i + 1;
                lowercase[i] = (byte) (c + 32);
                while (i2 < lowercase.length) {
                    byte c2 = lowercase[i2];
                    if (c2 < b2 || c2 > b) {
                        i2++;
                    } else {
                        lowercase[i2] = (byte) (c2 + 32);
                        i2++;
                    }
                }
                return new ByteString(lowercase);
            }
        }
        return $this$commonToAsciiLowercase;
    }

    public static final ByteString commonToAsciiUppercase(ByteString $this$commonToAsciiUppercase) {
        byte b;
        Intrinsics.checkParameterIsNotNull($this$commonToAsciiUppercase, "$this$commonToAsciiUppercase");
        for (int i = 0; i < $this$commonToAsciiUppercase.getData$okio().length; i++) {
            byte c = $this$commonToAsciiUppercase.getData$okio()[i];
            byte b2 = (byte) 97;
            if (c >= b2 && c <= (b = (byte) 122)) {
                byte[] data$okio = $this$commonToAsciiUppercase.getData$okio();
                byte[] lowercase = Arrays.copyOf(data$okio, data$okio.length);
                Intrinsics.checkExpressionValueIsNotNull(lowercase, "java.util.Arrays.copyOf(this, size)");
                int i2 = i + 1;
                lowercase[i] = (byte) (c - 32);
                while (i2 < lowercase.length) {
                    byte c2 = lowercase[i2];
                    if (c2 < b2 || c2 > b) {
                        i2++;
                    } else {
                        lowercase[i2] = (byte) (c2 - 32);
                        i2++;
                    }
                }
                return new ByteString(lowercase);
            }
        }
        return $this$commonToAsciiUppercase;
    }

    public static final ByteString commonSubstring(ByteString $this$commonSubstring, int beginIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull($this$commonSubstring, "$this$commonSubstring");
        boolean z = true;
        if (beginIndex >= 0) {
            if (endIndex <= $this$commonSubstring.getData$okio().length) {
                if (endIndex - beginIndex < 0) {
                    z = false;
                }
                if (!z) {
                    throw new IllegalArgumentException("endIndex < beginIndex".toString());
                } else if (beginIndex == 0 && endIndex == $this$commonSubstring.getData$okio().length) {
                    return $this$commonSubstring;
                } else {
                    return new ByteString(ArraysKt.copyOfRange($this$commonSubstring.getData$okio(), beginIndex, endIndex));
                }
            } else {
                throw new IllegalArgumentException(("endIndex > length(" + $this$commonSubstring.getData$okio().length + ')').toString());
            }
        } else {
            throw new IllegalArgumentException("beginIndex < 0".toString());
        }
    }

    public static final byte commonGetByte(ByteString $this$commonGetByte, int pos) {
        Intrinsics.checkParameterIsNotNull($this$commonGetByte, "$this$commonGetByte");
        return $this$commonGetByte.getData$okio()[pos];
    }

    public static final int commonGetSize(ByteString $this$commonGetSize) {
        Intrinsics.checkParameterIsNotNull($this$commonGetSize, "$this$commonGetSize");
        return $this$commonGetSize.getData$okio().length;
    }

    public static final byte[] commonToByteArray(ByteString $this$commonToByteArray) {
        Intrinsics.checkParameterIsNotNull($this$commonToByteArray, "$this$commonToByteArray");
        byte[] data$okio = $this$commonToByteArray.getData$okio();
        byte[] copyOf = Arrays.copyOf(data$okio, data$okio.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return copyOf;
    }

    public static final byte[] commonInternalArray(ByteString $this$commonInternalArray) {
        Intrinsics.checkParameterIsNotNull($this$commonInternalArray, "$this$commonInternalArray");
        return $this$commonInternalArray.getData$okio();
    }

    public static final boolean commonRangeEquals(ByteString $this$commonRangeEquals, int offset, ByteString other, int otherOffset, int byteCount) {
        Intrinsics.checkParameterIsNotNull($this$commonRangeEquals, "$this$commonRangeEquals");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return other.rangeEquals(otherOffset, $this$commonRangeEquals.getData$okio(), offset, byteCount);
    }

    public static final boolean commonRangeEquals(ByteString $this$commonRangeEquals, int offset, byte[] other, int otherOffset, int byteCount) {
        Intrinsics.checkParameterIsNotNull($this$commonRangeEquals, "$this$commonRangeEquals");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return offset >= 0 && offset <= $this$commonRangeEquals.getData$okio().length - byteCount && otherOffset >= 0 && otherOffset <= other.length - byteCount && Util.arrayRangeEquals($this$commonRangeEquals.getData$okio(), offset, other, otherOffset, byteCount);
    }

    public static final boolean commonStartsWith(ByteString $this$commonStartsWith, ByteString prefix) {
        Intrinsics.checkParameterIsNotNull($this$commonStartsWith, "$this$commonStartsWith");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        return $this$commonStartsWith.rangeEquals(0, prefix, 0, prefix.size());
    }

    public static final boolean commonStartsWith(ByteString $this$commonStartsWith, byte[] prefix) {
        Intrinsics.checkParameterIsNotNull($this$commonStartsWith, "$this$commonStartsWith");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        return $this$commonStartsWith.rangeEquals(0, prefix, 0, prefix.length);
    }

    public static final boolean commonEndsWith(ByteString $this$commonEndsWith, ByteString suffix) {
        Intrinsics.checkParameterIsNotNull($this$commonEndsWith, "$this$commonEndsWith");
        Intrinsics.checkParameterIsNotNull(suffix, "suffix");
        return $this$commonEndsWith.rangeEquals($this$commonEndsWith.size() - suffix.size(), suffix, 0, suffix.size());
    }

    public static final boolean commonEndsWith(ByteString $this$commonEndsWith, byte[] suffix) {
        Intrinsics.checkParameterIsNotNull($this$commonEndsWith, "$this$commonEndsWith");
        Intrinsics.checkParameterIsNotNull(suffix, "suffix");
        return $this$commonEndsWith.rangeEquals($this$commonEndsWith.size() - suffix.length, suffix, 0, suffix.length);
    }

    public static final int commonIndexOf(ByteString $this$commonIndexOf, byte[] other, int fromIndex) {
        Intrinsics.checkParameterIsNotNull($this$commonIndexOf, "$this$commonIndexOf");
        Intrinsics.checkParameterIsNotNull(other, "other");
        int limit = $this$commonIndexOf.getData$okio().length - other.length;
        int i = Math.max(fromIndex, 0);
        if (i > limit) {
            return -1;
        }
        while (!Util.arrayRangeEquals($this$commonIndexOf.getData$okio(), i, other, 0, other.length)) {
            if (i == limit) {
                return -1;
            }
            i++;
        }
        return i;
    }

    public static final int commonLastIndexOf(ByteString $this$commonLastIndexOf, ByteString other, int fromIndex) {
        Intrinsics.checkParameterIsNotNull($this$commonLastIndexOf, "$this$commonLastIndexOf");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return $this$commonLastIndexOf.lastIndexOf(other.internalArray$okio(), fromIndex);
    }

    public static final int commonLastIndexOf(ByteString $this$commonLastIndexOf, byte[] other, int fromIndex) {
        Intrinsics.checkParameterIsNotNull($this$commonLastIndexOf, "$this$commonLastIndexOf");
        Intrinsics.checkParameterIsNotNull(other, "other");
        for (int i = Math.min(fromIndex, $this$commonLastIndexOf.getData$okio().length - other.length); i >= 0; i--) {
            if (Util.arrayRangeEquals($this$commonLastIndexOf.getData$okio(), i, other, 0, other.length)) {
                return i;
            }
        }
        return -1;
    }

    public static final boolean commonEquals(ByteString $this$commonEquals, Object other) {
        Intrinsics.checkParameterIsNotNull($this$commonEquals, "$this$commonEquals");
        if (other == $this$commonEquals) {
            return true;
        }
        if (other instanceof ByteString) {
            return ((ByteString) other).size() == $this$commonEquals.getData$okio().length && ((ByteString) other).rangeEquals(0, $this$commonEquals.getData$okio(), 0, $this$commonEquals.getData$okio().length);
        }
        return false;
    }

    public static final int commonHashCode(ByteString $this$commonHashCode) {
        Intrinsics.checkParameterIsNotNull($this$commonHashCode, "$this$commonHashCode");
        int result = $this$commonHashCode.getHashCode$okio();
        if (result != 0) {
            return result;
        }
        int it = Arrays.hashCode($this$commonHashCode.getData$okio());
        $this$commonHashCode.setHashCode$okio(it);
        return it;
    }

    /* JADX INFO: Multiple debug info for r7v1 int: [D('$this$and$iv' byte), D('byteA' int)] */
    /* JADX INFO: Multiple debug info for r8v2 int: [D('$this$and$iv' byte), D('byteB' int)] */
    public static final int commonCompareTo(ByteString $this$commonCompareTo, ByteString other) {
        Intrinsics.checkParameterIsNotNull($this$commonCompareTo, "$this$commonCompareTo");
        Intrinsics.checkParameterIsNotNull(other, "other");
        int sizeA = $this$commonCompareTo.size();
        int sizeB = other.size();
        int size = Math.min(sizeA, sizeB);
        for (int i = 0; i < size; i++) {
            int byteA = $this$commonCompareTo.getByte(i) & 255;
            int byteB = other.getByte(i) & 255;
            if (byteA != byteB) {
                return byteA < byteB ? -1 : 1;
            }
        }
        if (sizeA == sizeB) {
            return 0;
        }
        return sizeA < sizeB ? -1 : 1;
    }

    public static final ByteString commonOf(byte[] data) {
        Intrinsics.checkParameterIsNotNull(data, UriUtil.DATA_SCHEME);
        byte[] copyOf = Arrays.copyOf(data, data.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return new ByteString(copyOf);
    }

    public static final ByteString commonToByteString(byte[] $this$commonToByteString, int offset, int byteCount) {
        Intrinsics.checkParameterIsNotNull($this$commonToByteString, "$this$commonToByteString");
        Util.checkOffsetAndCount((long) $this$commonToByteString.length, (long) offset, (long) byteCount);
        return new ByteString(ArraysKt.copyOfRange($this$commonToByteString, offset, offset + byteCount));
    }

    public static final ByteString commonEncodeUtf8(String $this$commonEncodeUtf8) {
        Intrinsics.checkParameterIsNotNull($this$commonEncodeUtf8, "$this$commonEncodeUtf8");
        ByteString byteString = new ByteString(Platform.asUtf8ToByteArray($this$commonEncodeUtf8));
        byteString.setUtf8$okio($this$commonEncodeUtf8);
        return byteString;
    }

    public static final ByteString commonDecodeBase64(String $this$commonDecodeBase64) {
        Intrinsics.checkParameterIsNotNull($this$commonDecodeBase64, "$this$commonDecodeBase64");
        byte[] decoded = Base64.decodeBase64ToArray($this$commonDecodeBase64);
        if (decoded != null) {
            return new ByteString(decoded);
        }
        return null;
    }

    public static final ByteString commonDecodeHex(String $this$commonDecodeHex) {
        Intrinsics.checkParameterIsNotNull($this$commonDecodeHex, "$this$commonDecodeHex");
        if ($this$commonDecodeHex.length() % 2 == 0) {
            byte[] result = new byte[$this$commonDecodeHex.length() / 2];
            int length = result.length;
            for (int i = 0; i < length; i++) {
                result[i] = (byte) ((decodeHexDigit($this$commonDecodeHex.charAt(i * 2)) << 4) + decodeHexDigit($this$commonDecodeHex.charAt((i * 2) + 1)));
            }
            return new ByteString(result);
        }
        throw new IllegalArgumentException(("Unexpected hex string: " + $this$commonDecodeHex).toString());
    }

    public static final void commonWrite(ByteString $this$commonWrite, Buffer buffer, int offset, int byteCount) {
        Intrinsics.checkParameterIsNotNull($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkParameterIsNotNull(buffer, "buffer");
        buffer.write($this$commonWrite.getData$okio(), offset, byteCount);
    }

    public static final int decodeHexDigit(char c) {
        if ('0' <= c && '9' >= c) {
            return c - '0';
        }
        if ('a' <= c && 'f' >= c) {
            return (c - 'a') + 10;
        }
        if ('A' <= c && 'F' >= c) {
            return (c - 'A') + 10;
        }
        throw new IllegalArgumentException("Unexpected hex digit: " + c);
    }

    public static final String commonToString(ByteString $this$commonToString) {
        ByteString byteString;
        Intrinsics.checkParameterIsNotNull($this$commonToString, "$this$commonToString");
        boolean z = true;
        if ($this$commonToString.getData$okio().length == 0) {
            return "[size=0]";
        }
        int i = codePointIndexToCharIndex($this$commonToString.getData$okio(), 64);
        if (i != -1) {
            String text = $this$commonToString.utf8();
            if (text != null) {
                String substring = text.substring(0, i);
                Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                String safeText = StringsKt.replace$default(StringsKt.replace$default(StringsKt.replace$default(substring, "\\", "\\\\", false, 4, (Object) null), IOUtils.LINE_SEPARATOR_UNIX, "\\n", false, 4, (Object) null), "\r", "\\r", false, 4, (Object) null);
                if (i < text.length()) {
                    return "[size=" + $this$commonToString.getData$okio().length + " text=" + safeText + "…]";
                }
                return "[text=" + safeText + ']';
            }
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        } else if ($this$commonToString.getData$okio().length <= 64) {
            return "[hex=" + $this$commonToString.hex() + ']';
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("[size=");
            sb.append($this$commonToString.getData$okio().length);
            sb.append(" hex=");
            if (64 <= $this$commonToString.getData$okio().length) {
                if (64 - 0 < 0) {
                    z = false;
                }
                if (z) {
                    if (64 == $this$commonToString.getData$okio().length) {
                        byteString = $this$commonToString;
                    } else {
                        byteString = new ByteString(ArraysKt.copyOfRange($this$commonToString.getData$okio(), 0, 64));
                    }
                    sb.append(byteString.hex());
                    sb.append("…]");
                    return sb.toString();
                }
                throw new IllegalArgumentException("endIndex < beginIndex".toString());
            }
            throw new IllegalArgumentException(("endIndex > length(" + $this$commonToString.getData$okio().length + ')').toString());
        }
    }

    /* JADX INFO: Multiple debug info for r8v9 byte: [D('c' int), D('index$iv' int)] */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x0182, code lost:
        if (31 >= 65533(0xfffd, float:9.1831E-41)) goto L_0x0190;
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x018e, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x0192;
     */
    /* JADX WARN: Code restructure failed: missing block: B:126:0x0190, code lost:
        r16 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x01c2, code lost:
        if (31 >= r3) goto L_0x01d0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x01ce, code lost:
        if (159 < r3) goto L_0x01d2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:151:0x01d0, code lost:
        r16 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:196:0x0250, code lost:
        if (r16 == false) goto L_0x0255;
     */
    /* JADX WARN: Code restructure failed: missing block: B:213:0x028d, code lost:
        if (31 >= 65533(0xfffd, float:9.1831E-41)) goto L_0x029b;
     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x0299, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x029d;
     */
    /* JADX WARN: Code restructure failed: missing block: B:219:0x029b, code lost:
        r16 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:243:0x02ea, code lost:
        if (31 >= 65533(0xfffd, float:9.1831E-41)) goto L_0x02f8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:248:0x02f6, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x02fa;
     */
    /* JADX WARN: Code restructure failed: missing block: B:249:0x02f8, code lost:
        r16 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:270:0x0344, code lost:
        if (31 >= 65533(0xfffd, float:9.1831E-41)) goto L_0x0352;
     */
    /* JADX WARN: Code restructure failed: missing block: B:275:0x0350, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x0354;
     */
    /* JADX WARN: Code restructure failed: missing block: B:276:0x0352, code lost:
        r16 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x0393, code lost:
        if (31 >= 65533(0xfffd, float:9.1831E-41)) goto L_0x03a1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x039f, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x03a3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x03a1, code lost:
        r16 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x03ae, code lost:
        if (65533(0xfffd, float:9.1831E-41) < 65536(0x10000, float:9.18355E-41)) goto L_0x03e9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x03cc, code lost:
        if (31 >= r4) goto L_0x03da;
     */
    /* JADX WARN: Code restructure failed: missing block: B:326:0x03d8, code lost:
        if (159 < r4) goto L_0x03dc;
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x03da, code lost:
        r16 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x03e7, code lost:
        if (r4 < 65536) goto L_0x03e9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:334:0x03e9, code lost:
        r17 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x03eb, code lost:
        r1 = r1 + r17;
        r14 = r27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:397:0x04b0, code lost:
        if (31 >= 65533(0xfffd, float:9.1831E-41)) goto L_0x04be;
     */
    /* JADX WARN: Code restructure failed: missing block: B:402:0x04bc, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x04c0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:403:0x04be, code lost:
        r16 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:427:0x050b, code lost:
        if (31 >= 65533(0xfffd, float:9.1831E-41)) goto L_0x0519;
     */
    /* JADX WARN: Code restructure failed: missing block: B:432:0x0517, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x051b;
     */
    /* JADX WARN: Code restructure failed: missing block: B:433:0x0519, code lost:
        r16 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:457:0x0569, code lost:
        if (31 >= 65533(0xfffd, float:9.1831E-41)) goto L_0x0577;
     */
    /* JADX WARN: Code restructure failed: missing block: B:462:0x0575, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x0579;
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x0577, code lost:
        r16 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:484:0x05c8, code lost:
        if (31 >= 65533(0xfffd, float:9.1831E-41)) goto L_0x05d6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:489:0x05d4, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x05d8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:490:0x05d6, code lost:
        r16 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:512:0x0617, code lost:
        if (31 >= 65533(0xfffd, float:9.1831E-41)) goto L_0x0625;
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x0623, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x0627;
     */
    /* JADX WARN: Code restructure failed: missing block: B:518:0x0625, code lost:
        r16 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:524:0x0632, code lost:
        if (65533(0xfffd, float:9.1831E-41) < 65536(0x10000, float:9.18355E-41)) goto L_0x0634;
     */
    /* JADX WARN: Code restructure failed: missing block: B:525:0x0634, code lost:
        r17 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:526:0x0636, code lost:
        r1 = r1 + r17;
        r14 = r27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:539:0x065d, code lost:
        if (31 >= 65533(0xfffd, float:9.1831E-41)) goto L_0x066b;
     */
    /* JADX WARN: Code restructure failed: missing block: B:544:0x0669, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x066d;
     */
    /* JADX WARN: Code restructure failed: missing block: B:545:0x066b, code lost:
        r16 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:551:0x0678, code lost:
        if (65533(0xfffd, float:9.1831E-41) < 65536(0x10000, float:9.18355E-41)) goto L_0x0634;
     */
    /* JADX WARN: Code restructure failed: missing block: B:562:0x0696, code lost:
        if (31 >= r3) goto L_0x06a4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:567:0x06a2, code lost:
        if (159 < r3) goto L_0x06a6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:568:0x06a4, code lost:
        r16 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:574:0x06b1, code lost:
        if (r3 < 65536) goto L_0x0634;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x012e, code lost:
        if (31 >= 65533(0xfffd, float:9.1831E-41)) goto L_0x013c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x013a, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x013e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x013c, code lost:
        r16 = true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static final int codePointIndexToCharIndex(byte[] s, int codePointCount) {
        int beginIndex$iv;
        int j;
        int j2;
        int j3;
        int j4;
        int j5;
        int j6 = 0;
        int charCount = 0;
        int beginIndex$iv2 = 0;
        int endIndex$iv = s.length;
        int index$iv = 0;
        while (index$iv < endIndex$iv) {
            byte b0$iv = s[index$iv];
            byte b = Byte.MAX_VALUE;
            boolean z = false;
            int i = 2;
            int i2 = 1;
            if (b0$iv >= 0) {
                int j7 = charCount + 1;
                if (charCount == codePointCount) {
                    return j6;
                }
                if (!(b0$iv == 10 || b0$iv == 13)) {
                    if ((((b0$iv < 0 || 31 < b0$iv) && (Byte.MAX_VALUE > b0$iv || 159 < b0$iv)) ? 0 : 1) != 0) {
                        return -1;
                    }
                }
                if (b0$iv == 65533) {
                    return -1;
                }
                index$iv++;
                int charCount2 = j6 + (b0$iv < 65536 ? 1 : 2);
                int charCount3 = j7;
                while (index$iv < endIndex$iv && s[index$iv] >= 0) {
                    int index$iv2 = index$iv + 1;
                    byte b2 = s[index$iv];
                    int j8 = charCount3 + 1;
                    if (charCount3 == codePointCount) {
                        return charCount2;
                    }
                    if (!(b2 == 10 || b2 == 13)) {
                        if ((((b2 < 0 || 31 < b2) && (b > b2 || 159 < b2)) ? 0 : 1) != 0) {
                            return -1;
                        }
                    }
                    if (b2 == 65533) {
                        return -1;
                    }
                    charCount2 += b2 < 65536 ? 1 : 2;
                    index$iv = index$iv2;
                    charCount3 = j8;
                    b = Byte.MAX_VALUE;
                }
                beginIndex$iv = beginIndex$iv2;
                charCount = charCount3;
                j6 = charCount2;
            } else if ((b0$iv >> 5) == -2) {
                if (endIndex$iv <= index$iv + 1) {
                    j5 = charCount + 1;
                    if (charCount == codePointCount) {
                        return j6;
                    }
                    if (!(65533 == 10 || 65533 == 13)) {
                        if ((65533 >= 0 && 31 >= 65533) || (127 <= 65533 && 159 >= 65533)) {
                            z = true;
                        }
                        if (z) {
                            return -1;
                        }
                    }
                    if (65533 == 65533) {
                        return -1;
                    }
                    if (65533 < 65536) {
                        i = 1;
                    }
                    j6 += i;
                    beginIndex$iv = beginIndex$iv2;
                } else {
                    byte b0$iv$iv = s[index$iv];
                    byte b1$iv$iv = s[index$iv + 1];
                    if (!((b1$iv$iv & 192) == 128)) {
                        int j9 = charCount + 1;
                        if (charCount == codePointCount) {
                            return j6;
                        }
                        beginIndex$iv = beginIndex$iv2;
                        if (!(65533 == 10 || 65533 == 13)) {
                            if (65533 < 0) {
                            }
                            if (127 <= 65533) {
                            }
                            if (z) {
                                return -1;
                            }
                        }
                        if (65533 == 65533) {
                            return -1;
                        }
                        if (65533 < 65536) {
                            i = 1;
                        }
                        j6 += i;
                        j5 = j9;
                    } else {
                        beginIndex$iv = beginIndex$iv2;
                        int codePoint$iv$iv = (b1$iv$iv ^ 3968) ^ (b0$iv$iv << 6);
                        if (codePoint$iv$iv < 128) {
                            int j10 = charCount + 1;
                            if (charCount == codePointCount) {
                                return j6;
                            }
                            if (!(65533 == 10 || 65533 == 13)) {
                                if (65533 < 0) {
                                }
                                if (127 <= 65533) {
                                }
                                if (z) {
                                    return -1;
                                }
                            }
                            if (65533 == 65533) {
                                return -1;
                            }
                            if (65533 >= 65536) {
                                i2 = 2;
                            }
                            j6 += i2;
                            j5 = j10;
                        } else {
                            int j11 = charCount + 1;
                            if (charCount == codePointCount) {
                                return j6;
                            }
                            if (!(codePoint$iv$iv == 10 || codePoint$iv$iv == 13)) {
                                if (codePoint$iv$iv < 0) {
                                }
                                if (127 <= codePoint$iv$iv) {
                                }
                                if (z) {
                                    return -1;
                                }
                            }
                            if (codePoint$iv$iv == 65533) {
                                return -1;
                            }
                            if (codePoint$iv$iv >= 65536) {
                                i2 = 2;
                            }
                            j6 += i2;
                            j5 = j11;
                        }
                        i2 = 2;
                    }
                }
                index$iv += i2;
                charCount = j5;
            } else {
                beginIndex$iv = beginIndex$iv2;
                if ((b0$iv >> 4) == -2) {
                    if (endIndex$iv <= index$iv + 2) {
                        j3 = charCount + 1;
                        if (charCount == codePointCount) {
                            return j6;
                        }
                        if (!(65533 == 10 || 65533 == 13)) {
                            if ((((65533 < 0 || 31 < 65533) && (127 > 65533 || 159 < 65533)) ? 0 : 1) != 0) {
                                return -1;
                            }
                        }
                        if (65533 == 65533) {
                            return -1;
                        }
                        j6 += 65533 < 65536 ? 1 : 2;
                        if (endIndex$iv > index$iv + 1) {
                            if ((192 & s[index$iv + 1]) == 128) {
                                z = true;
                            }
                        }
                        i = 1;
                    } else {
                        byte b0$iv$iv2 = s[index$iv];
                        byte b1$iv$iv2 = s[index$iv + 1];
                        if (((192 & b1$iv$iv2) == 128 ? 1 : 0) == 0) {
                            int j12 = charCount + 1;
                            if (charCount == codePointCount) {
                                return j6;
                            }
                            if (!(65533 == 10 || 65533 == 13)) {
                                if (65533 < 0) {
                                }
                                if (127 <= 65533) {
                                }
                                if (z) {
                                    return -1;
                                }
                            }
                            if (65533 == 65533) {
                                return -1;
                            }
                            if (65533 < 65536) {
                                i = 1;
                            }
                            j6 += i;
                            i = 1;
                            j3 = j12;
                        } else {
                            byte b2$iv$iv = s[index$iv + 2];
                            if (!((b2$iv$iv & 192) == 128)) {
                                int j13 = charCount + 1;
                                if (charCount == codePointCount) {
                                    return j6;
                                }
                                if (!(65533 == 10 || 65533 == 13)) {
                                    if (65533 < 0) {
                                    }
                                    if (127 <= 65533) {
                                    }
                                    if (z) {
                                        return -1;
                                    }
                                }
                                if (65533 == 65533) {
                                    return -1;
                                }
                                if (65533 >= 65536) {
                                    i2 = 2;
                                }
                                j6 += i2;
                                j3 = j13;
                            } else {
                                int codePoint$iv$iv2 = ((-123008 ^ b2$iv$iv) ^ (b1$iv$iv2 << 6)) ^ (b0$iv$iv2 << Ascii.FF);
                                if (codePoint$iv$iv2 < 2048) {
                                    int j14 = charCount + 1;
                                    if (charCount == codePointCount) {
                                        return j6;
                                    }
                                    if (!(65533 == 10 || 65533 == 13)) {
                                        if (65533 < 0) {
                                        }
                                        if (127 <= 65533) {
                                        }
                                        if (z) {
                                            return -1;
                                        }
                                    }
                                    if (65533 == 65533) {
                                        return -1;
                                    }
                                    if (65533 < 65536) {
                                        i = 1;
                                    }
                                    j6 += i;
                                    j3 = j14;
                                } else if (55296 <= codePoint$iv$iv2 && 57343 >= codePoint$iv$iv2) {
                                    j4 = charCount + 1;
                                    if (charCount == codePointCount) {
                                        return j6;
                                    }
                                    if (!(65533 == 10 || 65533 == 13)) {
                                        if (65533 < 0) {
                                        }
                                        if (127 <= 65533) {
                                        }
                                        if (z) {
                                            return -1;
                                        }
                                    }
                                    if (65533 == 65533) {
                                        return -1;
                                    }
                                } else {
                                    j4 = charCount + 1;
                                    if (charCount == codePointCount) {
                                        return j6;
                                    }
                                    if (!(codePoint$iv$iv2 == 10 || codePoint$iv$iv2 == 13)) {
                                        if (codePoint$iv$iv2 < 0) {
                                        }
                                        if (127 <= codePoint$iv$iv2) {
                                        }
                                        if (z) {
                                            return -1;
                                        }
                                    }
                                    if (codePoint$iv$iv2 == 65533) {
                                        return -1;
                                    }
                                }
                                i = 3;
                            }
                        }
                    }
                    index$iv += i;
                    charCount = j3;
                } else if ((b0$iv >> 3) == -2) {
                    if (endIndex$iv <= index$iv + 3) {
                        j = charCount + 1;
                        if (charCount == codePointCount) {
                            return j6;
                        }
                        if (!(65533 == 10 || 65533 == 13)) {
                            if ((((65533 < 0 || 31 < 65533) && (127 > 65533 || 159 < 65533)) ? 0 : 1) != 0) {
                                return -1;
                            }
                        }
                        if (65533 == 65533) {
                            return -1;
                        }
                        j6 += 65533 < 65536 ? 1 : 2;
                        if (endIndex$iv > index$iv + 1) {
                            if (((192 & s[index$iv + 1]) == 128 ? (byte) 1 : 0) != 0) {
                                if (endIndex$iv > index$iv + 2) {
                                    if ((192 & s[index$iv + 2]) == 128) {
                                        z = true;
                                    }
                                    if (z) {
                                        i = 3;
                                    }
                                }
                            }
                        }
                        i = 1;
                    } else {
                        byte b0$iv$iv3 = s[index$iv];
                        byte b1$iv$iv3 = s[index$iv + 1];
                        if (((192 & b1$iv$iv3) == 128 ? 1 : 0) == 0) {
                            int j15 = charCount + 1;
                            if (charCount == codePointCount) {
                                return j6;
                            }
                            if (!(65533 == 10 || 65533 == 13)) {
                                if (65533 < 0) {
                                }
                                if (127 <= 65533) {
                                }
                                if (z) {
                                    return -1;
                                }
                            }
                            if (65533 == 65533) {
                                return -1;
                            }
                            if (65533 < 65536) {
                                i = 1;
                            }
                            j6 += i;
                            i = 1;
                            j = j15;
                        } else {
                            byte b2$iv$iv2 = s[index$iv + 2];
                            if (((192 & b2$iv$iv2) == 128 ? 1 : 0) == 0) {
                                int j16 = charCount + 1;
                                if (charCount == codePointCount) {
                                    return j6;
                                }
                                if (!(65533 == 10 || 65533 == 13)) {
                                    if (65533 < 0) {
                                    }
                                    if (127 <= 65533) {
                                    }
                                    if (z) {
                                        return -1;
                                    }
                                }
                                if (65533 == 65533) {
                                    return -1;
                                }
                                if (65533 >= 65536) {
                                    i2 = 2;
                                }
                                j6 += i2;
                                j = j16;
                            } else {
                                byte b3$iv$iv = s[index$iv + 3];
                                if (!((b3$iv$iv & 192) == 128)) {
                                    int j17 = charCount + 1;
                                    if (charCount == codePointCount) {
                                        return j6;
                                    }
                                    if (!(65533 == 10 || 65533 == 13)) {
                                        if (65533 < 0) {
                                        }
                                        if (127 <= 65533) {
                                        }
                                        if (z) {
                                            return -1;
                                        }
                                    }
                                    if (65533 == 65533) {
                                        return -1;
                                    }
                                    if (65533 < 65536) {
                                        i = 1;
                                    }
                                    j6 += i;
                                    j = j17;
                                    i = 3;
                                } else {
                                    int codePoint$iv$iv3 = (((3678080 ^ b3$iv$iv) ^ (b2$iv$iv2 << 6)) ^ (b1$iv$iv3 << Ascii.FF)) ^ (b0$iv$iv3 << Ascii.DC2);
                                    if (codePoint$iv$iv3 > 1114111) {
                                        int j18 = charCount + 1;
                                        if (charCount == codePointCount) {
                                            return j6;
                                        }
                                        if (!(65533 == 10 || 65533 == 13)) {
                                            if (65533 < 0) {
                                            }
                                            if (127 <= 65533) {
                                            }
                                            if (z) {
                                                return -1;
                                            }
                                        }
                                        if (65533 == 65533) {
                                            return -1;
                                        }
                                        if (65533 < 65536) {
                                            i = 1;
                                        }
                                        j6 += i;
                                        j = j18;
                                    } else if (55296 <= codePoint$iv$iv3 && 57343 >= codePoint$iv$iv3) {
                                        j2 = charCount + 1;
                                        if (charCount == codePointCount) {
                                            return j6;
                                        }
                                        if (!(65533 == 10 || 65533 == 13)) {
                                            if (65533 < 0) {
                                            }
                                            if (127 <= 65533) {
                                            }
                                            if (z) {
                                                return -1;
                                            }
                                        }
                                        if (65533 == 65533) {
                                            return -1;
                                        }
                                    } else if (codePoint$iv$iv3 < 65536) {
                                        j2 = charCount + 1;
                                        if (charCount == codePointCount) {
                                            return j6;
                                        }
                                        if (!(65533 == 10 || 65533 == 13)) {
                                            if (65533 < 0) {
                                            }
                                            if (127 <= 65533) {
                                            }
                                            if (z) {
                                                return -1;
                                            }
                                        }
                                        if (65533 == 65533) {
                                            return -1;
                                        }
                                    } else {
                                        j2 = charCount + 1;
                                        if (charCount == codePointCount) {
                                            return j6;
                                        }
                                        if (!(codePoint$iv$iv3 == 10 || codePoint$iv$iv3 == 13)) {
                                            if (codePoint$iv$iv3 < 0) {
                                            }
                                            if (127 <= codePoint$iv$iv3) {
                                            }
                                            if (z) {
                                                return -1;
                                            }
                                        }
                                        if (codePoint$iv$iv3 == 65533) {
                                            return -1;
                                        }
                                    }
                                    i = 4;
                                }
                            }
                        }
                    }
                    index$iv += i;
                    charCount = j;
                } else {
                    int j19 = charCount + 1;
                    if (charCount == codePointCount) {
                        return j6;
                    }
                    if (!(65533 == 10 || 65533 == 13)) {
                        if ((65533 >= 0 && 31 >= 65533) || (127 <= 65533 && 159 >= 65533)) {
                            z = true;
                        }
                        if (z) {
                            return -1;
                        }
                    }
                    if (65533 == 65533) {
                        return -1;
                    }
                    if (65533 < 65536) {
                        i = 1;
                    }
                    j6 += i;
                    index$iv++;
                    charCount = j19;
                }
            }
            beginIndex$iv2 = beginIndex$iv;
        }
        return j6;
    }
}
