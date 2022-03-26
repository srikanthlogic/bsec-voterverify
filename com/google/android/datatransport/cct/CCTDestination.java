package com.google.android.datatransport.cct;

import com.google.android.datatransport.Encoding;
import com.google.android.datatransport.runtime.EncodedDestination;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public final class CCTDestination implements EncodedDestination {
    static final String DESTINATION_NAME = "cct";
    private static final String EXTRAS_DELIMITER = "\\";
    private static final String EXTRAS_VERSION_MARKER = "1$";
    private final String apiKey;
    private final String endPoint;
    static final String DEFAULT_END_POINT = StringMerger.mergeStrings("hts/frbslgiggolai.o/0clgbthfra=snpoo", "tp:/ieaeogn.ogepscmvc/o/ac?omtjo_rt3");
    static final String LEGACY_END_POINT = StringMerger.mergeStrings("hts/frbslgigp.ogepscmv/ieo/eaybtho", "tp:/ieaeogn-agolai.o/1frlglgc/aclg");
    private static final String DEFAULT_API_KEY = StringMerger.mergeStrings("AzSCki82AwsLzKd5O8zo", "IayckHiZRO1EFl1aGoK");
    private static final Set<Encoding> SUPPORTED_ENCODINGS = Collections.unmodifiableSet(new HashSet(Arrays.asList(Encoding.of("proto"), Encoding.of("json"))));
    public static final CCTDestination INSTANCE = new CCTDestination(DEFAULT_END_POINT, null);
    public static final CCTDestination LEGACY_INSTANCE = new CCTDestination(LEGACY_END_POINT, DEFAULT_API_KEY);

    public CCTDestination(String endPoint, String apiKey) {
        this.endPoint = endPoint;
        this.apiKey = apiKey;
    }

    @Override // com.google.android.datatransport.runtime.Destination
    public String getName() {
        return DESTINATION_NAME;
    }

    @Override // com.google.android.datatransport.runtime.Destination
    public byte[] getExtras() {
        return asByteArray();
    }

    @Override // com.google.android.datatransport.runtime.EncodedDestination
    public Set<Encoding> getSupportedEncodings() {
        return SUPPORTED_ENCODINGS;
    }

    public String getAPIKey() {
        return this.apiKey;
    }

    public String getEndPoint() {
        return this.endPoint;
    }

    public byte[] asByteArray() {
        if (this.apiKey == null && this.endPoint == null) {
            return null;
        }
        Object[] objArr = new Object[4];
        objArr[0] = EXTRAS_VERSION_MARKER;
        objArr[1] = this.endPoint;
        objArr[2] = EXTRAS_DELIMITER;
        String str = this.apiKey;
        if (str == null) {
            str = "";
        }
        objArr[3] = str;
        return String.format("%s%s%s%s", objArr).getBytes(Charset.forName("UTF-8"));
    }

    public static CCTDestination fromByteArray(byte[] a2) {
        String buffer = new String(a2, Charset.forName("UTF-8"));
        if (buffer.startsWith(EXTRAS_VERSION_MARKER)) {
            String[] fields = buffer.substring(EXTRAS_VERSION_MARKER.length()).split(Pattern.quote(EXTRAS_DELIMITER), 2);
            if (fields.length == 2) {
                String endPoint = fields[0];
                if (!endPoint.isEmpty()) {
                    String apiKey = fields[1];
                    return new CCTDestination(endPoint, apiKey.isEmpty() ? null : apiKey);
                }
                throw new IllegalArgumentException("Missing endpoint in CCTDestination extras");
            }
            throw new IllegalArgumentException("Extra is not a valid encoded LegacyFlgDestination");
        }
        throw new IllegalArgumentException("Version marker missing from extras");
    }

    static byte[] encodeString(String s) {
        return s.getBytes(Charset.forName("UTF-8"));
    }

    static String decodeExtras(byte[] a2) {
        return new String(a2, Charset.forName("UTF-8"));
    }
}
