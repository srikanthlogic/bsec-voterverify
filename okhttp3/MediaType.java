package okhttp3;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.TypeCastException;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kotlin.text.Typography;
/* compiled from: MediaType.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u0000 \u00132\u00020\u0001:\u0001\u0013B)\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0007J\u0016\u0010\u0006\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\tH\u0007J\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\r\u0010\u0005\u001a\u00020\u0003H\u0007¢\u0006\u0002\b\u0010J\b\u0010\u0011\u001a\u00020\u0003H\u0016J\r\u0010\u0004\u001a\u00020\u0003H\u0007¢\u0006\u0002\b\u0012R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0013\u0010\u0005\u001a\u00020\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\bR\u0013\u0010\u0004\u001a\u00020\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\b¨\u0006\u0014"}, d2 = {"Lokhttp3/MediaType;", "", "mediaType", "", "type", "subtype", "charset", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "()Ljava/lang/String;", "Ljava/nio/charset/Charset;", "defaultValue", "equals", "", "other", "hashCode", "", "-deprecated_subtype", "toString", "-deprecated_type", "Companion", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class MediaType {
    private static final String QUOTED;
    private static final String TOKEN;
    private final String charset;
    private final String mediaType;
    private final String subtype;
    private final String type;
    public static final Companion Companion = new Companion(null);
    private static final Pattern TYPE_SUBTYPE = Pattern.compile("([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)/([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)");
    private static final Pattern PARAMETER = Pattern.compile(";\\s*(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)=(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)|\"([^\"]*)\"))?");

    @JvmStatic
    public static final MediaType get(String str) {
        return Companion.get(str);
    }

    @JvmStatic
    public static final MediaType parse(String str) {
        return Companion.parse(str);
    }

    public final Charset charset() {
        return charset$default(this, null, 1, null);
    }

    private MediaType(String mediaType, String type, String subtype, String charset) {
        this.mediaType = mediaType;
        this.type = type;
        this.subtype = subtype;
        this.charset = charset;
    }

    public /* synthetic */ MediaType(String mediaType, String type, String subtype, String charset, DefaultConstructorMarker $constructor_marker) {
        this(mediaType, type, subtype, charset);
    }

    public final String type() {
        return this.type;
    }

    public final String subtype() {
        return this.subtype;
    }

    public static /* synthetic */ Charset charset$default(MediaType mediaType, Charset charset, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = null;
        }
        return mediaType.charset(charset);
    }

    public final Charset charset(Charset defaultValue) {
        try {
            return this.charset != null ? Charset.forName(this.charset) : defaultValue;
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "type", imports = {}))
    /* renamed from: -deprecated_type */
    public final String m1093deprecated_type() {
        return this.type;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "subtype", imports = {}))
    /* renamed from: -deprecated_subtype */
    public final String m1092deprecated_subtype() {
        return this.subtype;
    }

    public String toString() {
        return this.mediaType;
    }

    public boolean equals(Object other) {
        return (other instanceof MediaType) && Intrinsics.areEqual(((MediaType) other).mediaType, this.mediaType);
    }

    public int hashCode() {
        return this.mediaType.hashCode();
    }

    /* compiled from: MediaType.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0015\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0007H\u0007¢\u0006\u0002\b\rJ\u0017\u0010\u000e\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\u0007H\u0007¢\u0006\u0002\b\u000fJ\u0011\u0010\u0010\u001a\u00020\u000b*\u00020\u0007H\u0007¢\u0006\u0002\b\nJ\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u000b*\u00020\u0007H\u0007¢\u0006\u0002\b\u000eR\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lokhttp3/MediaType$Companion;", "", "()V", "PARAMETER", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "QUOTED", "", "TOKEN", "TYPE_SUBTYPE", "get", "Lokhttp3/MediaType;", "mediaType", "-deprecated_get", "parse", "-deprecated_parse", "toMediaType", "toMediaTypeOrNull", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        /* JADX WARN: Code restructure failed: missing block: B:30:0x00cb, code lost:
            if (kotlin.text.StringsKt.equals(r0, r11, true) != false) goto L_0x00cf;
         */
        @JvmStatic
        /* Code decompiled incorrectly, please refer to instructions dump */
        public final MediaType get(String $this$toMediaType) {
            String charsetParameter;
            boolean z;
            Intrinsics.checkParameterIsNotNull($this$toMediaType, "$this$toMediaType");
            Matcher typeSubtype = MediaType.TYPE_SUBTYPE.matcher($this$toMediaType);
            if (typeSubtype.lookingAt()) {
                boolean z2 = true;
                String group = typeSubtype.group(1);
                Intrinsics.checkExpressionValueIsNotNull(group, "typeSubtype.group(1)");
                Locale locale = Locale.US;
                Intrinsics.checkExpressionValueIsNotNull(locale, "Locale.US");
                if (group != null) {
                    String type = group.toLowerCase(locale);
                    Intrinsics.checkExpressionValueIsNotNull(type, "(this as java.lang.String).toLowerCase(locale)");
                    String group2 = typeSubtype.group(2);
                    Intrinsics.checkExpressionValueIsNotNull(group2, "typeSubtype.group(2)");
                    Locale locale2 = Locale.US;
                    Intrinsics.checkExpressionValueIsNotNull(locale2, "Locale.US");
                    if (group2 != null) {
                        String subtype = group2.toLowerCase(locale2);
                        Intrinsics.checkExpressionValueIsNotNull(subtype, "(this as java.lang.String).toLowerCase(locale)");
                        Matcher parameter = MediaType.PARAMETER.matcher($this$toMediaType);
                        int s = typeSubtype.end();
                        String str = null;
                        while (s < $this$toMediaType.length()) {
                            parameter.region(s, $this$toMediaType.length());
                            if (parameter.lookingAt()) {
                                int i = z2 ? 1 : 0;
                                int i2 = z2 ? 1 : 0;
                                int i3 = z2 ? 1 : 0;
                                int i4 = z2 ? 1 : 0;
                                int i5 = z2 ? 1 : 0;
                                String name = parameter.group(i);
                                if (name == null || !StringsKt.equals(name, "charset", z2)) {
                                    s = parameter.end();
                                    boolean z3 = z2 ? 1 : 0;
                                    String charsetParameter2 = z2 ? 1 : 0;
                                    String charsetParameter3 = z2 ? 1 : 0;
                                    String charsetParameter4 = z2 ? 1 : 0;
                                    String charsetParameter5 = z2 ? 1 : 0;
                                    z2 = z3;
                                } else {
                                    String token = parameter.group(2);
                                    boolean z4 = false;
                                    if (token == null) {
                                        String group3 = parameter.group(3);
                                        Intrinsics.checkExpressionValueIsNotNull(group3, "parameter.group(3)");
                                        charsetParameter = group3;
                                    } else if (!StringsKt.startsWith$default(token, "'", false, 2, (Object) null) || !StringsKt.endsWith$default(token, "'", false, 2, (Object) null) || token.length() <= 2) {
                                        charsetParameter = token;
                                    } else {
                                        charsetParameter = token.substring(1, token.length() - 1);
                                        Intrinsics.checkExpressionValueIsNotNull(charsetParameter, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                                    }
                                    if (str != null) {
                                        z = true;
                                    } else {
                                        z = true;
                                    }
                                    z4 = z;
                                    if (z4) {
                                        str = charsetParameter;
                                        s = parameter.end();
                                        z2 = z;
                                    } else {
                                        throw new IllegalArgumentException(("Multiple charsets defined: \"" + str + "\" and: \"" + charsetParameter + "\" for: \"" + $this$toMediaType + Typography.quote).toString());
                                    }
                                }
                            } else {
                                StringBuilder sb = new StringBuilder();
                                sb.append("Parameter is not formatted correctly: \"");
                                String substring = $this$toMediaType.substring(s);
                                Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
                                sb.append(substring);
                                sb.append("\" for: \"");
                                sb.append($this$toMediaType);
                                sb.append(Typography.quote);
                                throw new IllegalArgumentException(sb.toString().toString());
                            }
                        }
                        return new MediaType($this$toMediaType, type, subtype, str, null);
                    }
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            throw new IllegalArgumentException(("No subtype found for: \"" + $this$toMediaType + Typography.quote).toString());
        }

        @JvmStatic
        public final MediaType parse(String $this$toMediaTypeOrNull) {
            Intrinsics.checkParameterIsNotNull($this$toMediaTypeOrNull, "$this$toMediaTypeOrNull");
            try {
                return get($this$toMediaTypeOrNull);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        @Deprecated(level = DeprecationLevel.ERROR, message = "moved to extension function", replaceWith = @ReplaceWith(expression = "mediaType.toMediaType()", imports = {"okhttp3.MediaType.Companion.toMediaType"}))
        /* renamed from: -deprecated_get */
        public final MediaType m1094deprecated_get(String mediaType) {
            Intrinsics.checkParameterIsNotNull(mediaType, "mediaType");
            return get(mediaType);
        }

        @Deprecated(level = DeprecationLevel.ERROR, message = "moved to extension function", replaceWith = @ReplaceWith(expression = "mediaType.toMediaTypeOrNull()", imports = {"okhttp3.MediaType.Companion.toMediaTypeOrNull"}))
        /* renamed from: -deprecated_parse */
        public final MediaType m1095deprecated_parse(String mediaType) {
            Intrinsics.checkParameterIsNotNull(mediaType, "mediaType");
            return parse(mediaType);
        }
    }
}
