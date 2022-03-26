package okhttp3.internal;

import java.net.IDN;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okio.Buffer;
/* compiled from: hostnames.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a0\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0005H\u0002\u001a\"\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0002\u001a\u0010\u0010\f\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0002\u001a\f\u0010\r\u001a\u00020\u0001*\u00020\u0003H\u0002\u001a\f\u0010\u000e\u001a\u0004\u0018\u00010\u0003*\u00020\u0003¨\u0006\u000f"}, d2 = {"decodeIpv4Suffix", "", "input", "", "pos", "", "limit", "address", "", "addressOffset", "decodeIpv6", "Ljava/net/InetAddress;", "inet6AddressToAscii", "containsInvalidHostnameAsciiCodes", "toCanonicalHost", "okhttp"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class HostnamesKt {
    public static final String toCanonicalHost(String $this$toCanonicalHost) {
        InetAddress inetAddress;
        Intrinsics.checkParameterIsNotNull($this$toCanonicalHost, "$this$toCanonicalHost");
        boolean z = true;
        if (StringsKt.contains$default((CharSequence) $this$toCanonicalHost, (CharSequence) ":", false, 2, (Object) null)) {
            if (!StringsKt.startsWith$default($this$toCanonicalHost, "[", false, 2, (Object) null) || !StringsKt.endsWith$default($this$toCanonicalHost, "]", false, 2, (Object) null)) {
                inetAddress = decodeIpv6($this$toCanonicalHost, 0, $this$toCanonicalHost.length());
            } else {
                inetAddress = decodeIpv6($this$toCanonicalHost, 1, $this$toCanonicalHost.length() - 1);
            }
            if (inetAddress == null) {
                return null;
            }
            byte[] address = inetAddress.getAddress();
            if (address.length == 16) {
                Intrinsics.checkExpressionValueIsNotNull(address, "address");
                return inet6AddressToAscii(address);
            } else if (address.length == 4) {
                return inetAddress.getHostAddress();
            } else {
                throw new AssertionError("Invalid IPv6 address: '" + $this$toCanonicalHost + '\'');
            }
        } else {
            try {
                String ascii = IDN.toASCII($this$toCanonicalHost);
                Intrinsics.checkExpressionValueIsNotNull(ascii, "IDN.toASCII(host)");
                Locale locale = Locale.US;
                Intrinsics.checkExpressionValueIsNotNull(locale, "Locale.US");
                if (ascii != null) {
                    String result = ascii.toLowerCase(locale);
                    Intrinsics.checkExpressionValueIsNotNull(result, "(this as java.lang.String).toLowerCase(locale)");
                    if (result.length() != 0) {
                        z = false;
                    }
                    if (!z && !containsInvalidHostnameAsciiCodes(result)) {
                        return result;
                    }
                    return null;
                }
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    private static final boolean containsInvalidHostnameAsciiCodes(String $this$containsInvalidHostnameAsciiCodes) {
        int length = $this$containsInvalidHostnameAsciiCodes.length();
        for (int i = 0; i < length; i++) {
            char c = $this$containsInvalidHostnameAsciiCodes.charAt(i);
            if (c <= 31 || c >= 127 || StringsKt.indexOf$default((CharSequence) " #%/:?@[\\]", c, 0, false, 6, (Object) null) != -1) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:37:0x008c, code lost:
        return null;
     */
    /* JADX WARN: Removed duplicated region for block: B:29:0x005f  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private static final InetAddress decodeIpv6(String input, int pos, int limit) {
        int groupLength;
        byte[] address = new byte[16];
        int b = 0;
        int compress = -1;
        int groupOffset = -1;
        int i = pos;
        while (true) {
            if (i < limit) {
                if (b != address.length) {
                    if (i + 2 <= limit && StringsKt.startsWith$default(input, "::", i, false, 4, (Object) null)) {
                        if (compress == -1) {
                            i += 2;
                            b += 2;
                            compress = b;
                            if (i == limit) {
                                break;
                            }
                            int value = 0;
                            groupOffset = i;
                            while (i < limit) {
                            }
                            groupLength = i - groupOffset;
                            if (groupLength == 0) {
                                break;
                            }
                            break;
                        }
                        return null;
                    }
                    if (b != 0) {
                        if (StringsKt.startsWith$default(input, ":", i, false, 4, (Object) null)) {
                            i++;
                        } else if (!StringsKt.startsWith$default(input, ".", i, false, 4, (Object) null) || !decodeIpv4Suffix(input, groupOffset, limit, address, b - 2)) {
                            return null;
                        } else {
                            b += 2;
                        }
                    }
                    int value2 = 0;
                    groupOffset = i;
                    while (i < limit) {
                        int hexDigit = Util.parseHexDigit(input.charAt(i));
                        if (hexDigit == -1) {
                            break;
                        }
                        value2 = (value2 << 4) + hexDigit;
                        i++;
                    }
                    groupLength = i - groupOffset;
                    if (groupLength == 0 || groupLength > 4) {
                        break;
                    }
                    int b2 = b + 1;
                    address[b] = (byte) ((value2 >>> 8) & 255);
                    b = b2 + 1;
                    address[b2] = (byte) (value2 & 255);
                } else {
                    return null;
                }
            } else {
                break;
            }
        }
        if (b != address.length) {
            if (compress == -1) {
                return null;
            }
            System.arraycopy(address, compress, address, address.length - (b - compress), b - compress);
            Arrays.fill(address, compress, (address.length - b) + compress, (byte) 0);
        }
        return InetAddress.getByAddress(address);
    }

    private static final boolean decodeIpv4Suffix(String input, int pos, int limit, byte[] address, int addressOffset) {
        int b = addressOffset;
        int i = pos;
        while (i < limit) {
            if (b == address.length) {
                return false;
            }
            if (b != addressOffset) {
                if (input.charAt(i) != '.') {
                    return false;
                }
                i++;
            }
            int value = 0;
            while (i < limit) {
                char c = input.charAt(i);
                if (c < '0' || c > '9') {
                    break;
                } else if ((value == 0 && i != i) || ((value * 10) + c) - 48 > 255) {
                    return false;
                } else {
                    i++;
                }
            }
            if (i - i == 0) {
                return false;
            }
            address[b] = (byte) value;
            b++;
        }
        if (b == addressOffset + 4) {
            return true;
        }
        return false;
    }

    private static final String inet6AddressToAscii(byte[] address) {
        int longestRunOffset = -1;
        int longestRunLength = 0;
        int i = 0;
        while (i < address.length) {
            while (i < 16 && address[i] == 0 && address[i + 1] == 0) {
                i += 2;
            }
            int currentRunLength = i - i;
            if (currentRunLength > longestRunLength && currentRunLength >= 4) {
                longestRunOffset = i;
                longestRunLength = currentRunLength;
            }
            i += 2;
        }
        Buffer result = new Buffer();
        int i2 = 0;
        while (i2 < address.length) {
            if (i2 == longestRunOffset) {
                result.writeByte(58);
                i2 += longestRunLength;
                if (i2 == 16) {
                    result.writeByte(58);
                }
            } else {
                if (i2 > 0) {
                    result.writeByte(58);
                }
                result.writeHexadecimalUnsignedLong((long) ((Util.and(address[i2], 255) << 8) | Util.and(address[i2 + 1], 255)));
                i2 += 2;
            }
        }
        return result.readUtf8();
    }
}
