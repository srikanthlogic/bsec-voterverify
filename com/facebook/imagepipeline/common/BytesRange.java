package com.facebook.imagepipeline.common;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.util.HashCodeUtil;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class BytesRange {
    public static final int TO_END_OF_CONTENT = Integer.MAX_VALUE;
    @Nullable
    private static Pattern sHeaderParsingRegEx;
    public final int from;
    public final int to;

    public BytesRange(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public String toHttpRangeHeaderValue() {
        return String.format(null, "bytes=%s-%s", valueOrEmpty(this.from), valueOrEmpty(this.to));
    }

    public boolean contains(@Nullable BytesRange compare) {
        if (compare != null && this.from <= compare.from && this.to >= compare.to) {
            return true;
        }
        return false;
    }

    public String toString() {
        return String.format(null, "%s-%s", valueOrEmpty(this.from), valueOrEmpty(this.to));
    }

    private static String valueOrEmpty(int n) {
        if (n == Integer.MAX_VALUE) {
            return "";
        }
        return Integer.toString(n);
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof BytesRange)) {
            return false;
        }
        BytesRange that = (BytesRange) other;
        if (this.from == that.from && this.to == that.to) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return HashCodeUtil.hashCode(this.from, this.to);
    }

    public static BytesRange from(int from) {
        Preconditions.checkArgument(from >= 0);
        return new BytesRange(from, Integer.MAX_VALUE);
    }

    public static BytesRange toMax(int to) {
        Preconditions.checkArgument(to > 0);
        return new BytesRange(0, to);
    }

    @Nullable
    public static BytesRange fromContentRangeHeader(@Nullable String header) throws IllegalArgumentException {
        if (header == null) {
            return null;
        }
        if (sHeaderParsingRegEx == null) {
            sHeaderParsingRegEx = Pattern.compile("[-/ ]");
        }
        try {
            String[] headerParts = sHeaderParsingRegEx.split(header);
            Preconditions.checkArgument(headerParts.length == 4);
            Preconditions.checkArgument(headerParts[0].equals("bytes"));
            int from = Integer.parseInt(headerParts[1]);
            int to = Integer.parseInt(headerParts[2]);
            int length = Integer.parseInt(headerParts[3]);
            Preconditions.checkArgument(to > from);
            Preconditions.checkArgument(length > to);
            if (to < length - 1) {
                return new BytesRange(from, to);
            }
            return new BytesRange(from, Integer.MAX_VALUE);
        } catch (IllegalArgumentException x) {
            throw new IllegalArgumentException(String.format(null, "Invalid Content-Range header value: \"%s\"", header), x);
        }
    }
}
