package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
/* loaded from: classes3.dex */
public class Metaphone implements StringEncoder {
    private static final String FRONTV = "EIY";
    private static final String VARSON = "CSPTG";
    private static final String VOWELS = "AEIOU";
    private int maxCodeLen = 4;

    private boolean isLastChar(int i, int i2) {
        return i2 + 1 == i;
    }

    private boolean isNextChar(StringBuilder sb, int i, char c) {
        return i >= 0 && i < sb.length() - 1 && sb.charAt(i + 1) == c;
    }

    private boolean isPreviousChar(StringBuilder sb, int i, char c) {
        return i > 0 && i < sb.length() && sb.charAt(i - 1) == c;
    }

    private boolean isVowel(StringBuilder sb, int i) {
        return VOWELS.indexOf(sb.charAt(i)) >= 0;
    }

    private boolean regionMatch(StringBuilder sb, int i, String str) {
        if (i < 0 || (str.length() + i) - 1 >= sb.length()) {
            return false;
        }
        return sb.substring(i, str.length() + i).equals(str);
    }

    @Override // org.apache.commons.codec.Encoder
    public Object encode(Object obj) throws EncoderException {
        if (obj instanceof String) {
            return metaphone((String) obj);
        }
        throw new EncoderException("Parameter supplied to Metaphone encode is not of type java.lang.String");
    }

    @Override // org.apache.commons.codec.StringEncoder
    public String encode(String str) {
        return metaphone(str);
    }

    public int getMaxCodeLen() {
        return this.maxCodeLen;
    }

    public boolean isMetaphoneEqual(String str, String str2) {
        return metaphone(str).equals(metaphone(str2));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x0210, code lost:
        if (isNextChar(r2, r5, 'H') != false) goto L_0x0113;
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x0224, code lost:
        if (isVowel(r2, 2) != false) goto L_0x0113;
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x0234, code lost:
        if (isLastChar(r1, r5) != false) goto L_0x023b;
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x0237, code lost:
        if (r5 == 0) goto L_0x01a5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0072, code lost:
        if (r1[1] == 'N') goto L_0x007b;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0079, code lost:
        if (r1[1] == 'E') goto L_0x007b;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00c3, code lost:
        if (isVowel(r2, r5 + 1) != false) goto L_0x01a5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x010c, code lost:
        if (regionMatch(r2, r5, "SIA") == false) goto L_0x00b2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x011c, code lost:
        if (isNextChar(r2, r5, 'H') != false) goto L_0x00c7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0125, code lost:
        if (isPreviousChar(r2, r5, 'C') != false) goto L_0x023b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public String metaphone(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        if (str.length() == 1) {
            return str.toUpperCase(Locale.ENGLISH);
        }
        char[] charArray = str.toUpperCase(Locale.ENGLISH).toCharArray();
        StringBuilder sb = new StringBuilder(40);
        StringBuilder sb2 = new StringBuilder(10);
        int i = 0;
        char c = charArray[0];
        if (c != 'A') {
            if (c != 'G' && c != 'K' && c != 'P') {
                if (c == 'W') {
                    if (charArray[1] != 'R') {
                        if (charArray[1] == 'H') {
                            sb.append(charArray, 1, charArray.length - 1);
                            sb.setCharAt(0, 'W');
                        }
                    }
                    sb.append(charArray, 1, charArray.length - 1);
                } else if (c == 'X') {
                    charArray[0] = 'S';
                }
                sb.append(charArray);
            }
        }
        int length = sb.length();
        while (sb2.length() < getMaxCodeLen() && i < length) {
            char charAt = sb.charAt(i);
            if (charAt == 'C' || !isPreviousChar(sb, i, charAt)) {
                switch (charAt) {
                    case 'B':
                        if (isPreviousChar(sb, i, 'M')) {
                            break;
                        }
                        sb2.append(charAt);
                        break;
                    case 'C':
                        if (!isPreviousChar(sb, i, 'S') || isLastChar(length, i) || FRONTV.indexOf(sb.charAt(i + 1)) < 0) {
                            if (!regionMatch(sb, i, "CIA")) {
                                if (isLastChar(length, i) || FRONTV.indexOf(sb.charAt(i + 1)) < 0) {
                                    if (isPreviousChar(sb, i, 'S')) {
                                        break;
                                    }
                                    if (isNextChar(sb, i, 'H')) {
                                        if (i == 0) {
                                            if (length >= 3) {
                                                break;
                                            }
                                        }
                                    }
                                    sb2.append('K');
                                    break;
                                }
                                sb2.append('S');
                                break;
                            }
                            sb2.append('X');
                            break;
                        }
                        break;
                    case 'D':
                        if (!isLastChar(length, i + 1) && isNextChar(sb, i, 'G')) {
                            int i2 = i + 2;
                            if (FRONTV.indexOf(sb.charAt(i2)) >= 0) {
                                sb2.append('J');
                                i = i2;
                                break;
                            }
                        }
                        sb2.append('T');
                        break;
                    case 'F':
                    case 'J':
                    case 'L':
                    case 'M':
                    case 'N':
                    case 'R':
                        sb2.append(charAt);
                        break;
                    case 'G':
                        int i3 = i + 1;
                        if ((!isLastChar(length, i3) || !isNextChar(sb, i, 'H')) && ((isLastChar(length, i3) || !isNextChar(sb, i, 'H') || isVowel(sb, i + 2)) && (i <= 0 || (!regionMatch(sb, i, "GN") && !regionMatch(sb, i, "GNED"))))) {
                            boolean isPreviousChar = isPreviousChar(sb, i, 'G');
                            if (!isLastChar(length, i) && FRONTV.indexOf(sb.charAt(i3)) >= 0 && !isPreviousChar) {
                                sb2.append('J');
                                break;
                            }
                            sb2.append('K');
                            break;
                        }
                        break;
                    case 'H':
                        if (!isLastChar(length, i) && ((i <= 0 || VARSON.indexOf(sb.charAt(i - 1)) < 0) && isVowel(sb, i + 1))) {
                            sb2.append('H');
                            break;
                        }
                        break;
                    case 'K':
                        if (i > 0) {
                            break;
                        }
                        sb2.append(charAt);
                        break;
                    case 'Q':
                        sb2.append('K');
                        break;
                    case 'S':
                        if (!regionMatch(sb, i, "SH")) {
                            if (!regionMatch(sb, i, "SIO")) {
                                break;
                            }
                        }
                        sb2.append('X');
                        break;
                    case 'T':
                        if (!regionMatch(sb, i, "TIA") && !regionMatch(sb, i, "TIO")) {
                            if (!regionMatch(sb, i, "TCH")) {
                                if (regionMatch(sb, i, "TH")) {
                                    sb2.append('0');
                                    break;
                                }
                                sb2.append('T');
                                break;
                            }
                        }
                        sb2.append('X');
                        break;
                    case 'V':
                        sb2.append('F');
                        break;
                    case 'W':
                    case 'Y':
                        if (!isLastChar(length, i)) {
                            break;
                        }
                        break;
                    case 'X':
                        sb2.append('K');
                        sb2.append('S');
                        break;
                    case 'Z':
                        sb2.append('S');
                        break;
                }
                i++;
            } else {
                i++;
            }
            if (sb2.length() > getMaxCodeLen()) {
                sb2.setLength(getMaxCodeLen());
            }
        }
        return sb2.toString();
    }

    public void setMaxCodeLen(int i) {
        this.maxCodeLen = i;
    }
}
