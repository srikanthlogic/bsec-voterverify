package okhttp3;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.TypeCastException;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Challenge.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005B#\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0014\u0010\u0006\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0003\u0012\u0004\u0012\u00020\u00030\u0007¢\u0006\u0002\u0010\bJ\u001b\u0010\u0006\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0003\u0012\u0004\u0012\u00020\u00030\u0007H\u0007¢\u0006\u0002\b\u000eJ\r\u0010\n\u001a\u00020\u000bH\u0007¢\u0006\u0002\b\u000fJ\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\u000f\u0010\u0004\u001a\u0004\u0018\u00010\u0003H\u0007¢\u0006\u0002\b\u0015J\r\u0010\u0002\u001a\u00020\u0003H\u0007¢\u0006\u0002\b\u0016J\b\u0010\u0017\u001a\u00020\u0003H\u0016J\u000e\u0010\u0018\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u000bR!\u0010\u0006\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0003\u0012\u0004\u0012\u00020\u00030\u00078G¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\tR\u0011\u0010\n\u001a\u00020\u000b8G¢\u0006\u0006\u001a\u0004\b\n\u0010\fR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u00038G¢\u0006\u0006\u001a\u0004\b\u0004\u0010\rR\u0013\u0010\u0002\u001a\u00020\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\r¨\u0006\u0019"}, d2 = {"Lokhttp3/Challenge;", "", "scheme", "", "realm", "(Ljava/lang/String;Ljava/lang/String;)V", "authParams", "", "(Ljava/lang/String;Ljava/util/Map;)V", "()Ljava/util/Map;", "charset", "Ljava/nio/charset/Charset;", "()Ljava/nio/charset/Charset;", "()Ljava/lang/String;", "-deprecated_authParams", "-deprecated_charset", "equals", "", "other", "hashCode", "", "-deprecated_realm", "-deprecated_scheme", "toString", "withCharset", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class Challenge {
    private final Map<String, String> authParams;
    private final String scheme;

    public Challenge(String scheme, Map<String, String> map) {
        String newKey;
        Intrinsics.checkParameterIsNotNull(scheme, "scheme");
        Intrinsics.checkParameterIsNotNull(map, "authParams");
        this.scheme = scheme;
        Map newAuthParams = new LinkedHashMap();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key != null) {
                Locale locale = Locale.US;
                Intrinsics.checkExpressionValueIsNotNull(locale, "US");
                if (key != null) {
                    newKey = key.toLowerCase(locale);
                    Intrinsics.checkExpressionValueIsNotNull(newKey, "(this as java.lang.String).toLowerCase(locale)");
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
            } else {
                newKey = null;
            }
            newAuthParams.put(newKey, value);
        }
        Map<String, String> unmodifiableMap = Collections.unmodifiableMap(newAuthParams);
        Intrinsics.checkExpressionValueIsNotNull(unmodifiableMap, "unmodifiableMap<String?, String>(newAuthParams)");
        this.authParams = unmodifiableMap;
    }

    public final String scheme() {
        return this.scheme;
    }

    public final Map<String, String> authParams() {
        return this.authParams;
    }

    public final String realm() {
        return this.authParams.get("realm");
    }

    public final Charset charset() {
        String charset = this.authParams.get("charset");
        if (charset != null) {
            try {
                Charset forName = Charset.forName(charset);
                Intrinsics.checkExpressionValueIsNotNull(forName, "Charset.forName(charset)");
                return forName;
            } catch (Exception e) {
            }
        }
        Charset charset2 = StandardCharsets.ISO_8859_1;
        Intrinsics.checkExpressionValueIsNotNull(charset2, "ISO_8859_1");
        return charset2;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public Challenge(String scheme, String realm) {
        this(scheme, r0);
        Intrinsics.checkParameterIsNotNull(scheme, "scheme");
        Intrinsics.checkParameterIsNotNull(realm, "realm");
        Map singletonMap = Collections.singletonMap("realm", realm);
        Intrinsics.checkExpressionValueIsNotNull(singletonMap, "singletonMap(\"realm\", realm)");
    }

    public final Challenge withCharset(Charset charset) {
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        Map authParams = MapsKt.toMutableMap(this.authParams);
        String name = charset.name();
        Intrinsics.checkExpressionValueIsNotNull(name, "charset.name()");
        authParams.put("charset", name);
        return new Challenge(this.scheme, authParams);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "scheme", imports = {}))
    /* renamed from: -deprecated_scheme  reason: not valid java name */
    public final String m1043deprecated_scheme() {
        return this.scheme;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "authParams", imports = {}))
    /* renamed from: -deprecated_authParams  reason: not valid java name */
    public final Map<String, String> m1040deprecated_authParams() {
        return this.authParams;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "realm", imports = {}))
    /* renamed from: -deprecated_realm  reason: not valid java name */
    public final String m1042deprecated_realm() {
        return realm();
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "charset", imports = {}))
    /* renamed from: -deprecated_charset  reason: not valid java name */
    public final Charset m1041deprecated_charset() {
        return charset();
    }

    public boolean equals(Object other) {
        return (other instanceof Challenge) && Intrinsics.areEqual(((Challenge) other).scheme, this.scheme) && Intrinsics.areEqual(((Challenge) other).authParams, this.authParams);
    }

    public int hashCode() {
        return (((29 * 31) + this.scheme.hashCode()) * 31) + this.authParams.hashCode();
    }

    public String toString() {
        return this.scheme + " authParams=" + this.authParams;
    }
}
