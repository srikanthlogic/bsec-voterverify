package okio.internal;

import com.google.common.base.Ascii;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okio.Utf8;
/* compiled from: -Utf8.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0012\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0002\u001a\u001e\u0010\u0003\u001a\u00020\u0002*\u00020\u00012\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0005¨\u0006\u0007"}, d2 = {"commonAsUtf8ToByteArray", "", "", "commonToUtf8String", "beginIndex", "", "endIndex", "okio"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class _Utf8Kt {
    public static /* synthetic */ String commonToUtf8String$default(byte[] bArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = bArr.length;
        }
        return commonToUtf8String(bArr, i, i2);
    }

    /* JADX INFO: Multiple debug info for r6v12 char: [D('$i$f$processUtf16Chars' int), D('c' char)] */
    public static final String commonToUtf8String(byte[] $this$commonToUtf8String, int beginIndex, int endIndex) {
        int $i$f$processUtf16Chars;
        int length;
        int length2;
        int length3;
        int length4;
        int length5;
        int length6;
        int i;
        int length7;
        int length8;
        int i2;
        int length9;
        Intrinsics.checkParameterIsNotNull($this$commonToUtf8String, "$this$commonToUtf8String");
        if (beginIndex < 0 || endIndex > $this$commonToUtf8String.length || beginIndex > endIndex) {
            throw new ArrayIndexOutOfBoundsException("size=" + $this$commonToUtf8String.length + " beginIndex=" + beginIndex + " endIndex=" + endIndex);
        }
        char[] chars = new char[endIndex - beginIndex];
        int length10 = 0;
        int $i$f$processUtf16Chars2 = 0;
        int index$iv = beginIndex;
        while (index$iv < endIndex) {
            byte b0$iv = $this$commonToUtf8String[index$iv];
            if (b0$iv >= 0) {
                int length11 = length10 + 1;
                chars[length10] = (char) b0$iv;
                index$iv++;
                while (index$iv < endIndex && $this$commonToUtf8String[index$iv] >= 0) {
                    chars[length11] = (char) $this$commonToUtf8String[index$iv];
                    index$iv++;
                    length11++;
                }
                $i$f$processUtf16Chars = $i$f$processUtf16Chars2;
                length10 = length11;
            } else if ((b0$iv >> 5) == -2) {
                if (endIndex <= index$iv + 1) {
                    chars[length10] = (char) Utf8.REPLACEMENT_CODE_POINT;
                    length8 = length10 + 1;
                    i2 = 1;
                    $i$f$processUtf16Chars = $i$f$processUtf16Chars2;
                } else {
                    byte b0$iv$iv = $this$commonToUtf8String[index$iv];
                    byte b1$iv$iv = $this$commonToUtf8String[index$iv + 1];
                    if (!((b1$iv$iv & 192) == 128)) {
                        length8 = length10 + 1;
                        chars[length10] = (char) Utf8.REPLACEMENT_CODE_POINT;
                        $i$f$processUtf16Chars = $i$f$processUtf16Chars2;
                        i2 = 1;
                    } else {
                        int codePoint$iv$iv = (b1$iv$iv ^ 3968) ^ (b0$iv$iv << 6);
                        if (codePoint$iv$iv < 128) {
                            $i$f$processUtf16Chars = $i$f$processUtf16Chars2;
                            length9 = length10 + 1;
                            chars[length10] = (char) Utf8.REPLACEMENT_CODE_POINT;
                        } else {
                            $i$f$processUtf16Chars = $i$f$processUtf16Chars2;
                            length9 = length10 + 1;
                            chars[length10] = (char) codePoint$iv$iv;
                        }
                        length8 = length9;
                        i2 = 2;
                    }
                }
                index$iv += i2;
                length10 = length8;
            } else {
                $i$f$processUtf16Chars = $i$f$processUtf16Chars2;
                if ((b0$iv >> 4) == -2) {
                    if (endIndex <= index$iv + 2) {
                        length6 = length10 + 1;
                        chars[length10] = (char) Utf8.REPLACEMENT_CODE_POINT;
                        if (endIndex > index$iv + 1) {
                            if ((192 & $this$commonToUtf8String[index$iv + 1]) == 128) {
                                i = 2;
                            }
                        }
                        i = 1;
                    } else {
                        byte b0$iv$iv2 = $this$commonToUtf8String[index$iv];
                        byte b1$iv$iv2 = $this$commonToUtf8String[index$iv + 1];
                        if (!((b1$iv$iv2 & 192) == 128)) {
                            chars[length10] = (char) Utf8.REPLACEMENT_CODE_POINT;
                            length6 = length10 + 1;
                            i = 1;
                        } else {
                            byte b2$iv$iv = $this$commonToUtf8String[index$iv + 2];
                            if (!((b2$iv$iv & 192) == 128)) {
                                chars[length10] = (char) Utf8.REPLACEMENT_CODE_POINT;
                                length6 = length10 + 1;
                                i = 2;
                            } else {
                                int codePoint$iv$iv2 = ((-123008 ^ b2$iv$iv) ^ (b1$iv$iv2 << 6)) ^ (b0$iv$iv2 << Ascii.FF);
                                if (codePoint$iv$iv2 < 2048) {
                                    length7 = length10 + 1;
                                    chars[length10] = (char) Utf8.REPLACEMENT_CODE_POINT;
                                } else if (55296 <= codePoint$iv$iv2 && 57343 >= codePoint$iv$iv2) {
                                    length7 = length10 + 1;
                                    chars[length10] = (char) Utf8.REPLACEMENT_CODE_POINT;
                                } else {
                                    length7 = length10 + 1;
                                    chars[length10] = (char) codePoint$iv$iv2;
                                }
                                length6 = length7;
                                i = 3;
                            }
                        }
                    }
                    index$iv += i;
                    length10 = length6;
                } else if ((b0$iv >> 3) == -2) {
                    if (endIndex <= index$iv + 3) {
                        if (65533 != 65533) {
                            int length12 = length10 + 1;
                            chars[length10] = (char) ((Utf8.REPLACEMENT_CODE_POINT >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                            length2 = length12 + 1;
                            chars[length12] = (char) ((65533 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                        } else {
                            chars[length10] = Utf8.REPLACEMENT_CHARACTER;
                            length2 = length10 + 1;
                        }
                        if (endIndex > index$iv + 1) {
                            if (((192 & $this$commonToUtf8String[index$iv + 1]) == 128 ? (byte) 1 : 0) != 0) {
                                if (endIndex > index$iv + 2) {
                                    if ((192 & $this$commonToUtf8String[index$iv + 2]) == 128) {
                                        length = 3;
                                    }
                                }
                                length = 2;
                            }
                        }
                        length = 1;
                    } else {
                        byte b0$iv$iv3 = $this$commonToUtf8String[index$iv];
                        byte b1$iv$iv3 = $this$commonToUtf8String[index$iv + 1];
                        if (!((b1$iv$iv3 & 192) == 128)) {
                            if (65533 != 65533) {
                                int length13 = length10 + 1;
                                chars[length10] = (char) ((Utf8.REPLACEMENT_CODE_POINT >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                length2 = length13 + 1;
                                chars[length13] = (char) ((65533 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                            } else {
                                chars[length10] = Utf8.REPLACEMENT_CHARACTER;
                                length2 = length10 + 1;
                            }
                            length = 1;
                        } else {
                            byte b2$iv$iv2 = $this$commonToUtf8String[index$iv + 2];
                            if (!((b2$iv$iv2 & 192) == 128)) {
                                if (65533 != 65533) {
                                    int length14 = length10 + 1;
                                    chars[length10] = (char) ((Utf8.REPLACEMENT_CODE_POINT >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                    chars[length14] = (char) ((65533 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                                    length2 = length14 + 1;
                                } else {
                                    chars[length10] = Utf8.REPLACEMENT_CHARACTER;
                                    length2 = length10 + 1;
                                }
                                length = 2;
                            } else {
                                byte b3$iv$iv = $this$commonToUtf8String[index$iv + 3];
                                if (!((b3$iv$iv & 192) == 128)) {
                                    if (65533 != 65533) {
                                        int length15 = length10 + 1;
                                        chars[length10] = (char) ((Utf8.REPLACEMENT_CODE_POINT >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                        length5 = length15 + 1;
                                        chars[length15] = (char) ((65533 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                                    } else {
                                        chars[length10] = Utf8.REPLACEMENT_CHARACTER;
                                        length5 = length10 + 1;
                                    }
                                    length2 = length5;
                                    length = 3;
                                } else {
                                    int codePoint$iv$iv3 = (((3678080 ^ b3$iv$iv) ^ (b2$iv$iv2 << 6)) ^ (b1$iv$iv3 << Ascii.FF)) ^ (b0$iv$iv3 << Ascii.DC2);
                                    if (codePoint$iv$iv3 > 1114111) {
                                        if (65533 != 65533) {
                                            int length16 = length10 + 1;
                                            chars[length10] = (char) ((Utf8.REPLACEMENT_CODE_POINT >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                            length3 = length16 + 1;
                                            chars[length16] = (char) ((65533 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                                            length = 4;
                                            length2 = length3;
                                        } else {
                                            length4 = length10 + 1;
                                            chars[length10] = Utf8.REPLACEMENT_CHARACTER;
                                            length3 = length4;
                                            length = 4;
                                            length2 = length3;
                                        }
                                    } else if (55296 <= codePoint$iv$iv3 && 57343 >= codePoint$iv$iv3) {
                                        if (65533 != 65533) {
                                            int length17 = length10 + 1;
                                            chars[length10] = (char) ((Utf8.REPLACEMENT_CODE_POINT >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                            length3 = length17 + 1;
                                            chars[length17] = (char) ((65533 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                                            length = 4;
                                            length2 = length3;
                                        } else {
                                            length4 = length10 + 1;
                                            chars[length10] = Utf8.REPLACEMENT_CHARACTER;
                                            length3 = length4;
                                            length = 4;
                                            length2 = length3;
                                        }
                                    } else if (codePoint$iv$iv3 < 65536) {
                                        if (65533 != 65533) {
                                            int length18 = length10 + 1;
                                            chars[length10] = (char) ((Utf8.REPLACEMENT_CODE_POINT >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                            length3 = length18 + 1;
                                            chars[length18] = (char) ((65533 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                                            length = 4;
                                            length2 = length3;
                                        } else {
                                            length4 = length10 + 1;
                                            chars[length10] = Utf8.REPLACEMENT_CHARACTER;
                                            length3 = length4;
                                            length = 4;
                                            length2 = length3;
                                        }
                                    } else if (codePoint$iv$iv3 != 65533) {
                                        int length19 = length10 + 1;
                                        chars[length10] = (char) ((codePoint$iv$iv3 >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                        length3 = length19 + 1;
                                        chars[length19] = (char) ((codePoint$iv$iv3 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                                        length = 4;
                                        length2 = length3;
                                    } else {
                                        length4 = length10 + 1;
                                        chars[length10] = Utf8.REPLACEMENT_CHARACTER;
                                        length3 = length4;
                                        length = 4;
                                        length2 = length3;
                                    }
                                }
                            }
                        }
                    }
                    index$iv += length;
                    length10 = length2;
                } else {
                    chars[length10] = Utf8.REPLACEMENT_CHARACTER;
                    index$iv++;
                    length10++;
                }
            }
            $i$f$processUtf16Chars2 = $i$f$processUtf16Chars;
        }
        return new String(chars, 0, length10);
    }

    public static final byte[] commonAsUtf8ToByteArray(String $this$commonAsUtf8ToByteArray) {
        char charAt;
        Intrinsics.checkParameterIsNotNull($this$commonAsUtf8ToByteArray, "$this$commonAsUtf8ToByteArray");
        byte[] bytes = new byte[$this$commonAsUtf8ToByteArray.length() * 4];
        int length = $this$commonAsUtf8ToByteArray.length();
        for (int index = 0; index < length; index++) {
            char b0 = $this$commonAsUtf8ToByteArray.charAt(index);
            if (b0 >= 128) {
                int size = index;
                int endIndex$iv = $this$commonAsUtf8ToByteArray.length();
                int index$iv = index;
                while (index$iv < endIndex$iv) {
                    char c$iv = $this$commonAsUtf8ToByteArray.charAt(index$iv);
                    if (c$iv < 128) {
                        int size2 = size + 1;
                        bytes[size] = (byte) c$iv;
                        index$iv++;
                        while (index$iv < endIndex$iv && $this$commonAsUtf8ToByteArray.charAt(index$iv) < 128) {
                            bytes[size2] = (byte) $this$commonAsUtf8ToByteArray.charAt(index$iv);
                            index$iv++;
                            size2++;
                        }
                        size = size2;
                    } else if (c$iv < 2048) {
                        int size3 = size + 1;
                        bytes[size] = (byte) ((c$iv >> 6) | 192);
                        bytes[size3] = (byte) ((c$iv & '?') | 128);
                        index$iv++;
                        size = size3 + 1;
                    } else if (55296 > c$iv || 57343 < c$iv) {
                        int size4 = size + 1;
                        bytes[size] = (byte) ((c$iv >> '\f') | 224);
                        int size5 = size4 + 1;
                        bytes[size4] = (byte) (((c$iv >> 6) & 63) | 128);
                        bytes[size5] = (byte) ((c$iv & '?') | 128);
                        index$iv++;
                        size = size5 + 1;
                    } else if (c$iv > 56319 || endIndex$iv <= index$iv + 1 || 56320 > (charAt = $this$commonAsUtf8ToByteArray.charAt(index$iv + 1)) || 57343 < charAt) {
                        bytes[size] = Utf8.REPLACEMENT_BYTE;
                        index$iv++;
                        size++;
                    } else {
                        int codePoint$iv = ((c$iv << '\n') + $this$commonAsUtf8ToByteArray.charAt(index$iv + 1)) - 56613888;
                        int size6 = size + 1;
                        bytes[size] = (byte) ((codePoint$iv >> 18) | 240);
                        int size7 = size6 + 1;
                        bytes[size6] = (byte) (((codePoint$iv >> 12) & 63) | 128);
                        int size8 = size7 + 1;
                        bytes[size7] = (byte) (((codePoint$iv >> 6) & 63) | 128);
                        bytes[size8] = (byte) ((codePoint$iv & 63) | 128);
                        index$iv += 2;
                        size = size8 + 1;
                    }
                }
                byte[] copyOf = Arrays.copyOf(bytes, size);
                Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
                return copyOf;
            }
            bytes[index] = (byte) b0;
        }
        byte[] copyOf2 = Arrays.copyOf(bytes, $this$commonAsUtf8ToByteArray.length());
        Intrinsics.checkExpressionValueIsNotNull(copyOf2, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf2;
    }
}
