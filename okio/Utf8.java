package okio;

import com.google.common.base.Ascii;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Utf8.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000D\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\u001a\u0011\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0001H\u0080\b\u001a\u0011\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0007H\u0080\b\u001a1\u0010\u0010\u001a\u00020\u0001*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u0015H\u0080\b\u001a1\u0010\u0017\u001a\u00020\u0001*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u0015H\u0080\b\u001a1\u0010\u0018\u001a\u00020\u0001*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u0015H\u0080\b\u001a1\u0010\u0019\u001a\u00020\u0016*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00160\u0015H\u0080\b\u001a1\u0010\u001a\u001a\u00020\u0016*\u00020\u001b2\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00160\u0015H\u0080\b\u001a1\u0010\u001c\u001a\u00020\u0016*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u0015H\u0080\b\u001a%\u0010\u001d\u001a\u00020\u001e*\u00020\u001b2\b\b\u0002\u0010\u0012\u001a\u00020\u00012\b\b\u0002\u0010\u0013\u001a\u00020\u0001H\u0007¢\u0006\u0002\b\u001f\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0007X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\tX\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\n\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000¨\u0006 "}, d2 = {"HIGH_SURROGATE_HEADER", "", "LOG_SURROGATE_HEADER", "MASK_2BYTES", "MASK_3BYTES", "MASK_4BYTES", "REPLACEMENT_BYTE", "", "REPLACEMENT_CHARACTER", "", "REPLACEMENT_CODE_POINT", "isIsoControl", "", "codePoint", "isUtf8Continuation", "byte", "process2Utf8Bytes", "", "beginIndex", "endIndex", "yield", "Lkotlin/Function1;", "", "process3Utf8Bytes", "process4Utf8Bytes", "processUtf16Chars", "processUtf8Bytes", "", "processUtf8CodePoints", "utf8Size", "", "size", "okio"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class Utf8 {
    public static final int HIGH_SURROGATE_HEADER = 55232;
    public static final int LOG_SURROGATE_HEADER = 56320;
    public static final int MASK_2BYTES = 3968;
    public static final int MASK_3BYTES = -123008;
    public static final int MASK_4BYTES = 3678080;
    public static final byte REPLACEMENT_BYTE = 63;
    public static final char REPLACEMENT_CHARACTER = 65533;
    public static final int REPLACEMENT_CODE_POINT = 65533;

    public static final long size(String str) {
        return size$default(str, 0, 0, 3, null);
    }

    public static final long size(String str, int i) {
        return size$default(str, i, 0, 2, null);
    }

    public static /* synthetic */ long size$default(String str, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = str.length();
        }
        return size(str, i, i2);
    }

    public static final long size(String $this$utf8Size, int beginIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull($this$utf8Size, "$this$utf8Size");
        boolean z = true;
        if (beginIndex >= 0) {
            if (endIndex >= beginIndex) {
                if (endIndex > $this$utf8Size.length()) {
                    z = false;
                }
                if (z) {
                    long result = 0;
                    int i = beginIndex;
                    while (i < endIndex) {
                        int c = $this$utf8Size.charAt(i);
                        if (c < 128) {
                            result++;
                            i++;
                        } else if (c < 2048) {
                            result += (long) 2;
                            i++;
                        } else if (c < 55296 || c > 57343) {
                            result += (long) 3;
                            i++;
                        } else {
                            int low = i + 1 < endIndex ? $this$utf8Size.charAt(i + 1) : 0;
                            if (c > 56319 || low < 56320 || low > 57343) {
                                result++;
                                i++;
                            } else {
                                result += (long) 4;
                                i += 2;
                            }
                        }
                    }
                    return result;
                }
                throw new IllegalArgumentException(("endIndex > string.length: " + endIndex + " > " + $this$utf8Size.length()).toString());
            }
            throw new IllegalArgumentException(("endIndex < beginIndex: " + endIndex + " < " + beginIndex).toString());
        }
        throw new IllegalArgumentException(("beginIndex < 0: " + beginIndex).toString());
    }

    public static final boolean isIsoControl(int codePoint) {
        return (codePoint >= 0 && 31 >= codePoint) || (127 <= codePoint && 159 >= codePoint);
    }

    public static final boolean isUtf8Continuation(byte b) {
        return (192 & b) == 128;
    }

    public static final void processUtf8Bytes(String $this$processUtf8Bytes, int beginIndex, int endIndex, Function1<? super Byte, Unit> function1) {
        char charAt;
        Intrinsics.checkParameterIsNotNull($this$processUtf8Bytes, "$this$processUtf8Bytes");
        Intrinsics.checkParameterIsNotNull(function1, "yield");
        int index = beginIndex;
        while (index < endIndex) {
            char c = $this$processUtf8Bytes.charAt(index);
            if (c < 128) {
                function1.invoke(Byte.valueOf((byte) c));
                index++;
                while (index < endIndex && $this$processUtf8Bytes.charAt(index) < 128) {
                    function1.invoke(Byte.valueOf((byte) $this$processUtf8Bytes.charAt(index)));
                    index++;
                }
            } else if (c < 2048) {
                function1.invoke(Byte.valueOf((byte) ((c >> 6) | 192)));
                function1.invoke(Byte.valueOf((byte) (128 | (c & '?'))));
                index++;
            } else if (55296 > c || 57343 < c) {
                function1.invoke(Byte.valueOf((byte) ((c >> '\f') | 224)));
                function1.invoke(Byte.valueOf((byte) (((c >> 6) & 63) | 128)));
                function1.invoke(Byte.valueOf((byte) (128 | (c & '?'))));
                index++;
            } else if (c > 56319 || endIndex <= index + 1 || 56320 > (charAt = $this$processUtf8Bytes.charAt(index + 1)) || 57343 < charAt) {
                function1.invoke(Byte.valueOf((byte) REPLACEMENT_BYTE));
                index++;
            } else {
                int codePoint = ((c << '\n') + $this$processUtf8Bytes.charAt(index + 1)) - 56613888;
                function1.invoke(Byte.valueOf((byte) ((codePoint >> 18) | 240)));
                function1.invoke(Byte.valueOf((byte) (((codePoint >> 12) & 63) | 128)));
                function1.invoke(Byte.valueOf((byte) ((63 & (codePoint >> 6)) | 128)));
                function1.invoke(Byte.valueOf((byte) (128 | (codePoint & 63))));
                index += 2;
            }
        }
    }

    public static final void processUtf8CodePoints(byte[] $this$processUtf8CodePoints, int beginIndex, int endIndex, Function1<? super Integer, Unit> function1) {
        int i;
        int it;
        int i2;
        int it2;
        int i3;
        int it3;
        Intrinsics.checkParameterIsNotNull($this$processUtf8CodePoints, "$this$processUtf8CodePoints");
        Intrinsics.checkParameterIsNotNull(function1, "yield");
        int index = beginIndex;
        while (index < endIndex) {
            byte b0 = $this$processUtf8CodePoints[index];
            if (b0 >= 0) {
                function1.invoke(Integer.valueOf(b0));
                index++;
                while (index < endIndex && $this$processUtf8CodePoints[index] >= 0) {
                    function1.invoke(Integer.valueOf($this$processUtf8CodePoints[index]));
                    index++;
                }
            } else if ((b0 >> 5) == -2) {
                if (endIndex <= index + 1) {
                    function1.invoke(Integer.valueOf((int) REPLACEMENT_CODE_POINT));
                    i = 1;
                } else {
                    byte b0$iv = $this$processUtf8CodePoints[index];
                    byte b1$iv = $this$processUtf8CodePoints[index + 1];
                    if (!((b1$iv & 192) == 128)) {
                        function1.invoke(Integer.valueOf((int) REPLACEMENT_CODE_POINT));
                        i = 1;
                    } else {
                        int codePoint$iv = (b1$iv ^ 3968) ^ (b0$iv << 6);
                        if (codePoint$iv < 128) {
                            it = REPLACEMENT_CODE_POINT;
                        } else {
                            it = codePoint$iv;
                        }
                        function1.invoke(Integer.valueOf(it));
                        i = 2;
                    }
                }
                index += i;
            } else if ((b0 >> 4) == -2) {
                if (endIndex <= index + 2) {
                    function1.invoke(Integer.valueOf((int) REPLACEMENT_CODE_POINT));
                    if (endIndex > index + 1) {
                        if ((192 & $this$processUtf8CodePoints[index + 1]) == 128) {
                            i2 = 2;
                        }
                    }
                    i2 = 1;
                } else {
                    byte b0$iv2 = $this$processUtf8CodePoints[index];
                    byte b1$iv2 = $this$processUtf8CodePoints[index + 1];
                    if (!((b1$iv2 & 192) == 128)) {
                        function1.invoke(Integer.valueOf((int) REPLACEMENT_CODE_POINT));
                        i2 = 1;
                    } else {
                        byte b2$iv = $this$processUtf8CodePoints[index + 2];
                        if (!((b2$iv & 192) == 128)) {
                            function1.invoke(Integer.valueOf((int) REPLACEMENT_CODE_POINT));
                            i2 = 2;
                        } else {
                            int codePoint$iv2 = ((-123008 ^ b2$iv) ^ (b1$iv2 << 6)) ^ (b0$iv2 << Ascii.FF);
                            if (codePoint$iv2 < 2048) {
                                it2 = REPLACEMENT_CODE_POINT;
                            } else if (55296 <= codePoint$iv2 && 57343 >= codePoint$iv2) {
                                it2 = REPLACEMENT_CODE_POINT;
                            } else {
                                it2 = codePoint$iv2;
                            }
                            function1.invoke(Integer.valueOf(it2));
                            i2 = 3;
                        }
                    }
                }
                index += i2;
            } else if ((b0 >> 3) == -2) {
                if (endIndex <= index + 3) {
                    function1.invoke(Integer.valueOf((int) REPLACEMENT_CODE_POINT));
                    if (endIndex > index + 1) {
                        if (((192 & $this$processUtf8CodePoints[index + 1]) == 128 ? (byte) 1 : 0) != 0) {
                            if (endIndex > index + 2) {
                                if ((192 & $this$processUtf8CodePoints[index + 2]) == 128) {
                                    i3 = 3;
                                }
                            }
                            i3 = 2;
                        }
                    }
                    i3 = 1;
                } else {
                    byte b0$iv3 = $this$processUtf8CodePoints[index];
                    byte b1$iv3 = $this$processUtf8CodePoints[index + 1];
                    if (!((b1$iv3 & 192) == 128)) {
                        function1.invoke(Integer.valueOf((int) REPLACEMENT_CODE_POINT));
                        i3 = 1;
                    } else {
                        byte b2$iv2 = $this$processUtf8CodePoints[index + 2];
                        if (!((b2$iv2 & 192) == 128)) {
                            function1.invoke(Integer.valueOf((int) REPLACEMENT_CODE_POINT));
                            i3 = 2;
                        } else {
                            byte b3$iv = $this$processUtf8CodePoints[index + 3];
                            if (!((b3$iv & 192) == 128)) {
                                function1.invoke(Integer.valueOf((int) REPLACEMENT_CODE_POINT));
                                i3 = 3;
                            } else {
                                int codePoint$iv3 = (((3678080 ^ b3$iv) ^ (b2$iv2 << 6)) ^ (b1$iv3 << Ascii.FF)) ^ (b0$iv3 << Ascii.DC2);
                                if (codePoint$iv3 > 1114111) {
                                    it3 = REPLACEMENT_CODE_POINT;
                                } else if (55296 <= codePoint$iv3 && 57343 >= codePoint$iv3) {
                                    it3 = REPLACEMENT_CODE_POINT;
                                } else if (codePoint$iv3 < 65536) {
                                    it3 = REPLACEMENT_CODE_POINT;
                                } else {
                                    it3 = codePoint$iv3;
                                }
                                function1.invoke(Integer.valueOf(it3));
                                i3 = 4;
                            }
                        }
                    }
                }
                index += i3;
            } else {
                function1.invoke(Integer.valueOf((int) REPLACEMENT_CODE_POINT));
                index++;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:121:0x02a5, code lost:
        if (65533(0xfffd, float:9.1831E-41) != 65533(0xfffd, float:9.1831E-41)) goto L_0x02a7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x02a7, code lost:
        r27.invoke(java.lang.Character.valueOf((char) ((r12 >>> 10) + okio.Utf8.HIGH_SURROGATE_HEADER)));
        r27.invoke(java.lang.Character.valueOf((char) ((r12 & 1023) + okio.Utf8.LOG_SURROGATE_HEADER)));
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x02bf, code lost:
        r27.invoke(java.lang.Character.valueOf(okio.Utf8.REPLACEMENT_CHARACTER));
     */
    /* JADX WARN: Code restructure failed: missing block: B:128:0x02d4, code lost:
        if (65533(0xfffd, float:9.1831E-41) != 65533(0xfffd, float:9.1831E-41)) goto L_0x02a7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:132:0x02df, code lost:
        if (65533(0xfffd, float:9.1831E-41) != 65533(0xfffd, float:9.1831E-41)) goto L_0x02a7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x02e4, code lost:
        if (r12 != 65533) goto L_0x02a7;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static final void processUtf16Chars(byte[] $this$processUtf16Chars, int beginIndex, int endIndex, Function1<? super Character, Unit> function1) {
        int i;
        int it;
        int i2;
        int it2;
        int i3;
        int codePoint;
        Intrinsics.checkParameterIsNotNull($this$processUtf16Chars, "$this$processUtf16Chars");
        Intrinsics.checkParameterIsNotNull(function1, "yield");
        int index = beginIndex;
        while (index < endIndex) {
            byte b0 = $this$processUtf16Chars[index];
            if (b0 >= 0) {
                function1.invoke(Character.valueOf((char) b0));
                index++;
                while (index < endIndex && $this$processUtf16Chars[index] >= 0) {
                    function1.invoke(Character.valueOf((char) $this$processUtf16Chars[index]));
                    index++;
                }
            } else if ((b0 >> 5) == -2) {
                if (endIndex <= index + 1) {
                    function1.invoke(Character.valueOf((char) REPLACEMENT_CODE_POINT));
                    i = 1;
                } else {
                    byte b0$iv = $this$processUtf16Chars[index];
                    byte b1$iv = $this$processUtf16Chars[index + 1];
                    if (!((b1$iv & 192) == 128)) {
                        function1.invoke(Character.valueOf((char) REPLACEMENT_CODE_POINT));
                        i = 1;
                    } else {
                        int codePoint$iv = (b1$iv ^ 3968) ^ (b0$iv << 6);
                        if (codePoint$iv < 128) {
                            it = REPLACEMENT_CODE_POINT;
                        } else {
                            it = codePoint$iv;
                        }
                        function1.invoke(Character.valueOf((char) it));
                        i = 2;
                    }
                }
                index += i;
            } else if ((b0 >> 4) == -2) {
                if (endIndex <= index + 2) {
                    function1.invoke(Character.valueOf((char) REPLACEMENT_CODE_POINT));
                    if (endIndex > index + 1) {
                        if ((192 & $this$processUtf16Chars[index + 1]) == 128) {
                            i2 = 2;
                        }
                    }
                    i2 = 1;
                } else {
                    byte b0$iv2 = $this$processUtf16Chars[index];
                    byte b1$iv2 = $this$processUtf16Chars[index + 1];
                    if (!((b1$iv2 & 192) == 128)) {
                        function1.invoke(Character.valueOf((char) REPLACEMENT_CODE_POINT));
                        i2 = 1;
                    } else {
                        byte b2$iv = $this$processUtf16Chars[index + 2];
                        if (!((b2$iv & 192) == 128)) {
                            function1.invoke(Character.valueOf((char) REPLACEMENT_CODE_POINT));
                            i2 = 2;
                        } else {
                            int codePoint$iv2 = ((-123008 ^ b2$iv) ^ (b1$iv2 << 6)) ^ (b0$iv2 << Ascii.FF);
                            if (codePoint$iv2 < 2048) {
                                it2 = REPLACEMENT_CODE_POINT;
                            } else if (55296 <= codePoint$iv2 && 57343 >= codePoint$iv2) {
                                it2 = REPLACEMENT_CODE_POINT;
                            } else {
                                it2 = codePoint$iv2;
                            }
                            function1.invoke(Character.valueOf((char) it2));
                            i2 = 3;
                        }
                    }
                }
                index += i2;
            } else if ((b0 >> 3) == -2) {
                if (endIndex <= index + 3) {
                    if (65533 != 65533) {
                        function1.invoke(Character.valueOf((char) ((REPLACEMENT_CODE_POINT >>> 10) + HIGH_SURROGATE_HEADER)));
                        function1.invoke(Character.valueOf((char) ((65533 & 1023) + LOG_SURROGATE_HEADER)));
                    } else {
                        function1.invoke(Character.valueOf(REPLACEMENT_CHARACTER));
                    }
                    if (endIndex > index + 1) {
                        if (((192 & $this$processUtf16Chars[index + 1]) == 128 ? (byte) 1 : 0) != 0) {
                            if (endIndex > index + 2) {
                                if ((192 & $this$processUtf16Chars[index + 2]) == 128) {
                                    i3 = 3;
                                }
                            }
                            i3 = 2;
                        }
                    }
                    i3 = 1;
                } else {
                    byte b0$iv3 = $this$processUtf16Chars[index];
                    byte b1$iv3 = $this$processUtf16Chars[index + 1];
                    if (!((b1$iv3 & 192) == 128)) {
                        if (65533 != 65533) {
                            function1.invoke(Character.valueOf((char) ((REPLACEMENT_CODE_POINT >>> 10) + HIGH_SURROGATE_HEADER)));
                            function1.invoke(Character.valueOf((char) ((65533 & 1023) + LOG_SURROGATE_HEADER)));
                        } else {
                            function1.invoke(Character.valueOf(REPLACEMENT_CHARACTER));
                        }
                        i3 = 1;
                    } else {
                        byte b2$iv2 = $this$processUtf16Chars[index + 2];
                        if (!((b2$iv2 & 192) == 128)) {
                            if (65533 != 65533) {
                                function1.invoke(Character.valueOf((char) ((REPLACEMENT_CODE_POINT >>> 10) + HIGH_SURROGATE_HEADER)));
                                function1.invoke(Character.valueOf((char) ((65533 & 1023) + LOG_SURROGATE_HEADER)));
                            } else {
                                function1.invoke(Character.valueOf(REPLACEMENT_CHARACTER));
                            }
                            i3 = 2;
                        } else {
                            byte b3$iv = $this$processUtf16Chars[index + 3];
                            if (!((b3$iv & 192) == 128)) {
                                if (65533 != 65533) {
                                    function1.invoke(Character.valueOf((char) ((REPLACEMENT_CODE_POINT >>> 10) + HIGH_SURROGATE_HEADER)));
                                    function1.invoke(Character.valueOf((char) ((65533 & 1023) + LOG_SURROGATE_HEADER)));
                                } else {
                                    function1.invoke(Character.valueOf(REPLACEMENT_CHARACTER));
                                }
                                i3 = 3;
                            } else {
                                int codePoint$iv3 = (((3678080 ^ b3$iv) ^ (b2$iv2 << 6)) ^ (b1$iv3 << Ascii.FF)) ^ (b0$iv3 << Ascii.DC2);
                                if (codePoint$iv3 > 1114111) {
                                    codePoint = REPLACEMENT_CODE_POINT;
                                } else if (55296 <= codePoint$iv3 && 57343 >= codePoint$iv3) {
                                    codePoint = REPLACEMENT_CODE_POINT;
                                } else if (codePoint$iv3 < 65536) {
                                    codePoint = REPLACEMENT_CODE_POINT;
                                } else {
                                    codePoint = codePoint$iv3;
                                }
                                i3 = 4;
                            }
                        }
                    }
                }
                index += i3;
            } else {
                function1.invoke(Character.valueOf(REPLACEMENT_CHARACTER));
                index++;
            }
        }
    }

    public static final int process2Utf8Bytes(byte[] $this$process2Utf8Bytes, int beginIndex, int endIndex, Function1<? super Integer, Unit> function1) {
        Intrinsics.checkParameterIsNotNull($this$process2Utf8Bytes, "$this$process2Utf8Bytes");
        Intrinsics.checkParameterIsNotNull(function1, "yield");
        int i = beginIndex + 1;
        Integer valueOf = Integer.valueOf((int) REPLACEMENT_CODE_POINT);
        if (endIndex <= i) {
            function1.invoke(valueOf);
            return 1;
        }
        byte b0 = $this$process2Utf8Bytes[beginIndex];
        byte b1 = $this$process2Utf8Bytes[beginIndex + 1];
        if (!((192 & b1) == 128)) {
            function1.invoke(valueOf);
            return 1;
        }
        int codePoint = (b1 ^ 3968) ^ (b0 << 6);
        if (codePoint < 128) {
            function1.invoke(valueOf);
            return 2;
        }
        function1.invoke(Integer.valueOf(codePoint));
        return 2;
    }

    public static final int process3Utf8Bytes(byte[] $this$process3Utf8Bytes, int beginIndex, int endIndex, Function1<? super Integer, Unit> function1) {
        Intrinsics.checkParameterIsNotNull($this$process3Utf8Bytes, "$this$process3Utf8Bytes");
        Intrinsics.checkParameterIsNotNull(function1, "yield");
        int i = beginIndex + 2;
        boolean z = false;
        Integer valueOf = Integer.valueOf((int) REPLACEMENT_CODE_POINT);
        if (endIndex <= i) {
            function1.invoke(valueOf);
            if (endIndex > beginIndex + 1) {
                if ((192 & $this$process3Utf8Bytes[beginIndex + 1]) == 128) {
                    z = true;
                }
                if (z) {
                    return 2;
                }
            }
            return 1;
        }
        byte b0 = $this$process3Utf8Bytes[beginIndex];
        byte b1 = $this$process3Utf8Bytes[beginIndex + 1];
        if (((192 & b1) == 128 ? 1 : 0) == 0) {
            function1.invoke(valueOf);
            return 1;
        }
        byte b2 = $this$process3Utf8Bytes[beginIndex + 2];
        if ((192 & b2) == 128) {
            z = true;
        }
        if (!z) {
            function1.invoke(valueOf);
            return 2;
        }
        int codePoint = ((-123008 ^ b2) ^ (b1 << 6)) ^ (b0 << Ascii.FF);
        if (codePoint < 2048) {
            function1.invoke(valueOf);
            return 3;
        } else if (55296 <= codePoint && 57343 >= codePoint) {
            function1.invoke(valueOf);
            return 3;
        } else {
            function1.invoke(Integer.valueOf(codePoint));
            return 3;
        }
    }

    public static final int process4Utf8Bytes(byte[] $this$process4Utf8Bytes, int beginIndex, int endIndex, Function1<? super Integer, Unit> function1) {
        Intrinsics.checkParameterIsNotNull($this$process4Utf8Bytes, "$this$process4Utf8Bytes");
        Intrinsics.checkParameterIsNotNull(function1, "yield");
        int i = beginIndex + 3;
        boolean z = false;
        Integer valueOf = Integer.valueOf((int) REPLACEMENT_CODE_POINT);
        if (endIndex <= i) {
            function1.invoke(valueOf);
            if (endIndex > beginIndex + 1) {
                if (((192 & $this$process4Utf8Bytes[beginIndex + 1]) == 128 ? (byte) 1 : 0) != 0) {
                    if (endIndex > beginIndex + 2) {
                        if ((192 & $this$process4Utf8Bytes[beginIndex + 2]) == 128) {
                            z = true;
                        }
                        if (z) {
                            return 3;
                        }
                    }
                    return 2;
                }
            }
            return 1;
        }
        byte b0 = $this$process4Utf8Bytes[beginIndex];
        byte b1 = $this$process4Utf8Bytes[beginIndex + 1];
        if (((192 & b1) == 128 ? 1 : 0) == 0) {
            function1.invoke(valueOf);
            return 1;
        }
        byte b2 = $this$process4Utf8Bytes[beginIndex + 2];
        if (((192 & b2) == 128 ? 1 : 0) == 0) {
            function1.invoke(valueOf);
            return 2;
        }
        byte b3 = $this$process4Utf8Bytes[beginIndex + 3];
        if ((192 & b3) == 128) {
            z = true;
        }
        if (!z) {
            function1.invoke(valueOf);
            return 3;
        }
        int codePoint = (((3678080 ^ b3) ^ (b2 << 6)) ^ (b1 << Ascii.FF)) ^ (b0 << Ascii.DC2);
        if (codePoint > 1114111) {
            function1.invoke(valueOf);
            return 4;
        } else if (55296 <= codePoint && 57343 >= codePoint) {
            function1.invoke(valueOf);
            return 4;
        } else if (codePoint < 65536) {
            function1.invoke(valueOf);
            return 4;
        } else {
            function1.invoke(Integer.valueOf(codePoint));
            return 4;
        }
    }
}
