package com.google.common.primitives;
/* loaded from: classes3.dex */
final class ParseRequest {
    final int radix;
    final String rawValue;

    private ParseRequest(String rawValue, int radix) {
        this.rawValue = rawValue;
        this.radix = radix;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ParseRequest fromString(String stringValue) {
        int radix;
        String rawValue;
        if (stringValue.length() != 0) {
            char firstChar = stringValue.charAt(0);
            if (stringValue.startsWith("0x") || stringValue.startsWith("0X")) {
                rawValue = stringValue.substring(2);
                radix = 16;
            } else if (firstChar == '#') {
                rawValue = stringValue.substring(1);
                radix = 16;
            } else if (firstChar != '0' || stringValue.length() <= 1) {
                rawValue = stringValue;
                radix = 10;
            } else {
                rawValue = stringValue.substring(1);
                radix = 8;
            }
            return new ParseRequest(rawValue, radix);
        }
        throw new NumberFormatException("empty string");
    }
}
