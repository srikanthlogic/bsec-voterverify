package okio;

import com.google.common.base.Ascii;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: -Base64.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0003\u001a\u000e\u0010\u0006\u001a\u0004\u0018\u00010\u0001*\u00020\u0007H\u0000\u001a\u0016\u0010\b\u001a\u00020\u0007*\u00020\u00012\b\b\u0002\u0010\t\u001a\u00020\u0001H\u0000\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003\"\u0014\u0010\u0004\u001a\u00020\u0001X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0003¨\u0006\n"}, d2 = {"BASE64", "", "getBASE64", "()[B", "BASE64_URL_SAFE", "getBASE64_URL_SAFE", "decodeBase64ToArray", "", "encodeBase64", "map", "okio"}, k = 2, mv = {1, 1, 16})
/* renamed from: okio.-Base64 */
/* loaded from: classes3.dex */
public final class Base64 {
    private static final byte[] BASE64 = ByteString.Companion.encodeUtf8("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/").getData$okio();
    private static final byte[] BASE64_URL_SAFE = ByteString.Companion.encodeUtf8("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_").getData$okio();

    public static final byte[] getBASE64() {
        return BASE64;
    }

    public static final byte[] getBASE64_URL_SAFE() {
        return BASE64_URL_SAFE;
    }

    public static final byte[] decodeBase64ToArray(String $this$decodeBase64ToArray) {
        int bits;
        Intrinsics.checkParameterIsNotNull($this$decodeBase64ToArray, "$this$decodeBase64ToArray");
        int limit = $this$decodeBase64ToArray.length();
        while (limit > 0 && ((c = $this$decodeBase64ToArray.charAt(limit - 1)) == '=' || c == '\n' || c == '\r' || c == ' ' || c == '\t')) {
            limit--;
        }
        byte[] out = new byte[(int) ((((long) limit) * 6) / 8)];
        int outCount = 0;
        int inCount = 0;
        int word = 0;
        for (int pos = 0; pos < limit; pos++) {
            char c = $this$decodeBase64ToArray.charAt(pos);
            if ('A' <= c && 'Z' >= c) {
                bits = c - 'A';
            } else if ('a' <= c && 'z' >= c) {
                bits = c - 'G';
            } else if ('0' <= c && '9' >= c) {
                bits = c + 4;
            } else if (c == '+' || c == '-') {
                bits = 62;
            } else if (c == '/' || c == '_') {
                bits = 63;
            } else {
                if (!(c == '\n' || c == '\r' || c == ' ' || c == '\t')) {
                    return null;
                }
            }
            word = (word << 6) | bits;
            inCount++;
            if (inCount % 4 == 0) {
                int outCount2 = outCount + 1;
                out[outCount] = (byte) (word >> 16);
                int outCount3 = outCount2 + 1;
                out[outCount2] = (byte) (word >> 8);
                out[outCount3] = (byte) word;
                outCount = outCount3 + 1;
            }
        }
        int lastWordChars = inCount % 4;
        if (lastWordChars == 1) {
            return null;
        }
        if (lastWordChars == 2) {
            out[outCount] = (byte) ((word << 12) >> 16);
            outCount++;
        } else if (lastWordChars == 3) {
            int word2 = word << 6;
            int outCount4 = outCount + 1;
            out[outCount] = (byte) (word2 >> 16);
            outCount = outCount4 + 1;
            out[outCount4] = (byte) (word2 >> 8);
        }
        if (outCount == out.length) {
            return out;
        }
        byte[] copyOf = Arrays.copyOf(out, outCount);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf;
    }

    public static /* synthetic */ String encodeBase64$default(byte[] bArr, byte[] bArr2, int i, Object obj) {
        if ((i & 1) != 0) {
            bArr2 = BASE64;
        }
        return encodeBase64(bArr, bArr2);
    }

    /* JADX INFO: Multiple debug info for r5v4 byte: [D('b0' int), D('i' int)] */
    /* JADX INFO: Multiple debug info for r5v6 byte: [D('b0' int), D('i' int)] */
    /* JADX INFO: Multiple debug info for r6v2 byte: [D('b0' int), D('i' int)] */
    /* JADX INFO: Multiple debug info for r6v5 byte: [D('b1' int), D('i' int)] */
    /* JADX INFO: Multiple debug info for r7v3 byte: [D('i' int), D('b2' int)] */
    public static final String encodeBase64(byte[] $this$encodeBase64, byte[] map) {
        Intrinsics.checkParameterIsNotNull($this$encodeBase64, "$this$encodeBase64");
        Intrinsics.checkParameterIsNotNull(map, "map");
        byte[] out = new byte[(($this$encodeBase64.length + 2) / 3) * 4];
        int index = 0;
        int end = $this$encodeBase64.length - ($this$encodeBase64.length % 3);
        int b0 = 0;
        while (b0 < end) {
            int i = b0 + 1;
            byte b = $this$encodeBase64[b0];
            int i2 = i + 1;
            byte b2 = $this$encodeBase64[i];
            int i3 = i2 + 1;
            byte b3 = $this$encodeBase64[i2];
            int index2 = index + 1;
            out[index] = map[(b & 255) >> 2];
            int index3 = index2 + 1;
            out[index2] = map[((b & 3) << 4) | ((b2 & 255) >> 4)];
            int index4 = index3 + 1;
            out[index3] = map[((b2 & Ascii.SI) << 2) | ((b3 & 255) >> 6)];
            index = index4 + 1;
            out[index4] = map[b3 & Utf8.REPLACEMENT_BYTE];
            b0 = i3;
        }
        int length = $this$encodeBase64.length - end;
        if (length == 1) {
            byte b4 = $this$encodeBase64[b0];
            int index5 = index + 1;
            out[index] = map[(b4 & 255) >> 2];
            int index6 = index5 + 1;
            out[index5] = map[(b4 & 3) << 4];
            byte b5 = (byte) 61;
            out[index6] = b5;
            out[index6 + 1] = b5;
        } else if (length == 2) {
            int i4 = b0 + 1;
            byte b6 = $this$encodeBase64[b0];
            byte b7 = $this$encodeBase64[i4];
            int index7 = index + 1;
            out[index] = map[(b6 & 255) >> 2];
            int index8 = index7 + 1;
            out[index7] = map[((b6 & 3) << 4) | ((b7 & 255) >> 4)];
            out[index8] = map[(b7 & Ascii.SI) << 2];
            out[index8 + 1] = (byte) 61;
        }
        return Platform.toUtf8String(out);
    }
}
