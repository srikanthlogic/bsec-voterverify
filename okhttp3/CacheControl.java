package okhttp3;

import com.google.common.net.HttpHeaders;
import java.util.concurrent.TimeUnit;
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
import okhttp3.internal.Util;
/* compiled from: CacheControl.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010\u000e\n\u0002\b\u0011\u0018\u0000 !2\u00020\u0001:\u0002 !Bq\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\u0006\u0010\u000b\u001a\u00020\u0006\u0012\u0006\u0010\f\u001a\u00020\u0006\u0012\u0006\u0010\r\u001a\u00020\u0003\u0012\u0006\u0010\u000e\u001a\u00020\u0003\u0012\u0006\u0010\u000f\u001a\u00020\u0003\u0012\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011¢\u0006\u0002\u0010\u0012J\r\u0010\u000f\u001a\u00020\u0003H\u0007¢\u0006\u0002\b\u0015J\r\u0010\u0005\u001a\u00020\u0006H\u0007¢\u0006\u0002\b\u0016J\r\u0010\u000b\u001a\u00020\u0006H\u0007¢\u0006\u0002\b\u0017J\r\u0010\f\u001a\u00020\u0006H\u0007¢\u0006\u0002\b\u0018J\r\u0010\n\u001a\u00020\u0003H\u0007¢\u0006\u0002\b\u0019J\r\u0010\u0002\u001a\u00020\u0003H\u0007¢\u0006\u0002\b\u001aJ\r\u0010\u0004\u001a\u00020\u0003H\u0007¢\u0006\u0002\b\u001bJ\r\u0010\u000e\u001a\u00020\u0003H\u0007¢\u0006\u0002\b\u001cJ\r\u0010\r\u001a\u00020\u0003H\u0007¢\u0006\u0002\b\u001dJ\r\u0010\u0007\u001a\u00020\u0006H\u0007¢\u0006\u0002\b\u001eJ\b\u0010\u001f\u001a\u00020\u0011H\u0016R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u0013\u0010\u000f\u001a\u00020\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0013R\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0013R\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0013R\u0013\u0010\u0005\u001a\u00020\u00068\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0014R\u0013\u0010\u000b\u001a\u00020\u00068\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u0014R\u0013\u0010\f\u001a\u00020\u00068\u0007¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0014R\u0013\u0010\n\u001a\u00020\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0013R\u0013\u0010\u0002\u001a\u00020\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0013R\u0013\u0010\u0004\u001a\u00020\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0013R\u0013\u0010\u000e\u001a\u00020\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0013R\u0013\u0010\r\u001a\u00020\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u0013R\u0013\u0010\u0007\u001a\u00020\u00068\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u0014¨\u0006\""}, d2 = {"Lokhttp3/CacheControl;", "", "noCache", "", "noStore", "maxAgeSeconds", "", "sMaxAgeSeconds", "isPrivate", "isPublic", "mustRevalidate", "maxStaleSeconds", "minFreshSeconds", "onlyIfCached", "noTransform", "immutable", "headerValue", "", "(ZZIIZZZIIZZZLjava/lang/String;)V", "()Z", "()I", "-deprecated_immutable", "-deprecated_maxAgeSeconds", "-deprecated_maxStaleSeconds", "-deprecated_minFreshSeconds", "-deprecated_mustRevalidate", "-deprecated_noCache", "-deprecated_noStore", "-deprecated_noTransform", "-deprecated_onlyIfCached", "-deprecated_sMaxAgeSeconds", "toString", "Builder", "Companion", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class CacheControl {
    private String headerValue;
    private final boolean immutable;
    private final boolean isPrivate;
    private final boolean isPublic;
    private final int maxAgeSeconds;
    private final int maxStaleSeconds;
    private final int minFreshSeconds;
    private final boolean mustRevalidate;
    private final boolean noCache;
    private final boolean noStore;
    private final boolean noTransform;
    private final boolean onlyIfCached;
    private final int sMaxAgeSeconds;
    public static final Companion Companion = new Companion(null);
    public static final CacheControl FORCE_NETWORK = new Builder().noCache().build();
    public static final CacheControl FORCE_CACHE = new Builder().onlyIfCached().maxStale(Integer.MAX_VALUE, TimeUnit.SECONDS).build();

    @JvmStatic
    public static final CacheControl parse(Headers headers) {
        return Companion.parse(headers);
    }

    private CacheControl(boolean noCache, boolean noStore, int maxAgeSeconds, int sMaxAgeSeconds, boolean isPrivate, boolean isPublic, boolean mustRevalidate, int maxStaleSeconds, int minFreshSeconds, boolean onlyIfCached, boolean noTransform, boolean immutable, String headerValue) {
        this.noCache = noCache;
        this.noStore = noStore;
        this.maxAgeSeconds = maxAgeSeconds;
        this.sMaxAgeSeconds = sMaxAgeSeconds;
        this.isPrivate = isPrivate;
        this.isPublic = isPublic;
        this.mustRevalidate = mustRevalidate;
        this.maxStaleSeconds = maxStaleSeconds;
        this.minFreshSeconds = minFreshSeconds;
        this.onlyIfCached = onlyIfCached;
        this.noTransform = noTransform;
        this.immutable = immutable;
        this.headerValue = headerValue;
    }

    public /* synthetic */ CacheControl(boolean noCache, boolean noStore, int maxAgeSeconds, int sMaxAgeSeconds, boolean isPrivate, boolean isPublic, boolean mustRevalidate, int maxStaleSeconds, int minFreshSeconds, boolean onlyIfCached, boolean noTransform, boolean immutable, String headerValue, DefaultConstructorMarker $constructor_marker) {
        this(noCache, noStore, maxAgeSeconds, sMaxAgeSeconds, isPrivate, isPublic, mustRevalidate, maxStaleSeconds, minFreshSeconds, onlyIfCached, noTransform, immutable, headerValue);
    }

    public final boolean noCache() {
        return this.noCache;
    }

    public final boolean noStore() {
        return this.noStore;
    }

    public final int maxAgeSeconds() {
        return this.maxAgeSeconds;
    }

    public final int sMaxAgeSeconds() {
        return this.sMaxAgeSeconds;
    }

    public final boolean isPrivate() {
        return this.isPrivate;
    }

    public final boolean isPublic() {
        return this.isPublic;
    }

    public final boolean mustRevalidate() {
        return this.mustRevalidate;
    }

    public final int maxStaleSeconds() {
        return this.maxStaleSeconds;
    }

    public final int minFreshSeconds() {
        return this.minFreshSeconds;
    }

    public final boolean onlyIfCached() {
        return this.onlyIfCached;
    }

    public final boolean noTransform() {
        return this.noTransform;
    }

    public final boolean immutable() {
        return this.immutable;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "noCache", imports = {}))
    /* renamed from: -deprecated_noCache  reason: not valid java name */
    public final boolean m1035deprecated_noCache() {
        return this.noCache;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "noStore", imports = {}))
    /* renamed from: -deprecated_noStore  reason: not valid java name */
    public final boolean m1036deprecated_noStore() {
        return this.noStore;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "maxAgeSeconds", imports = {}))
    /* renamed from: -deprecated_maxAgeSeconds  reason: not valid java name */
    public final int m1031deprecated_maxAgeSeconds() {
        return this.maxAgeSeconds;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "sMaxAgeSeconds", imports = {}))
    /* renamed from: -deprecated_sMaxAgeSeconds  reason: not valid java name */
    public final int m1039deprecated_sMaxAgeSeconds() {
        return this.sMaxAgeSeconds;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "mustRevalidate", imports = {}))
    /* renamed from: -deprecated_mustRevalidate  reason: not valid java name */
    public final boolean m1034deprecated_mustRevalidate() {
        return this.mustRevalidate;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "maxStaleSeconds", imports = {}))
    /* renamed from: -deprecated_maxStaleSeconds  reason: not valid java name */
    public final int m1032deprecated_maxStaleSeconds() {
        return this.maxStaleSeconds;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "minFreshSeconds", imports = {}))
    /* renamed from: -deprecated_minFreshSeconds  reason: not valid java name */
    public final int m1033deprecated_minFreshSeconds() {
        return this.minFreshSeconds;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "onlyIfCached", imports = {}))
    /* renamed from: -deprecated_onlyIfCached  reason: not valid java name */
    public final boolean m1038deprecated_onlyIfCached() {
        return this.onlyIfCached;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "noTransform", imports = {}))
    /* renamed from: -deprecated_noTransform  reason: not valid java name */
    public final boolean m1037deprecated_noTransform() {
        return this.noTransform;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "immutable", imports = {}))
    /* renamed from: -deprecated_immutable  reason: not valid java name */
    public final boolean m1030deprecated_immutable() {
        return this.immutable;
    }

    public String toString() {
        String result = this.headerValue;
        if (result != null) {
            return result;
        }
        StringBuilder $this$buildString = new StringBuilder();
        if (this.noCache) {
            $this$buildString.append("no-cache, ");
        }
        if (this.noStore) {
            $this$buildString.append("no-store, ");
        }
        if (this.maxAgeSeconds != -1) {
            $this$buildString.append("max-age=");
            $this$buildString.append(this.maxAgeSeconds);
            $this$buildString.append(", ");
        }
        if (this.sMaxAgeSeconds != -1) {
            $this$buildString.append("s-maxage=");
            $this$buildString.append(this.sMaxAgeSeconds);
            $this$buildString.append(", ");
        }
        if (this.isPrivate) {
            $this$buildString.append("private, ");
        }
        if (this.isPublic) {
            $this$buildString.append("public, ");
        }
        if (this.mustRevalidate) {
            $this$buildString.append("must-revalidate, ");
        }
        if (this.maxStaleSeconds != -1) {
            $this$buildString.append("max-stale=");
            $this$buildString.append(this.maxStaleSeconds);
            $this$buildString.append(", ");
        }
        if (this.minFreshSeconds != -1) {
            $this$buildString.append("min-fresh=");
            $this$buildString.append(this.minFreshSeconds);
            $this$buildString.append(", ");
        }
        if (this.onlyIfCached) {
            $this$buildString.append("only-if-cached, ");
        }
        if (this.noTransform) {
            $this$buildString.append("no-transform, ");
        }
        if (this.immutable) {
            $this$buildString.append("immutable, ");
        }
        if ($this$buildString.length() == 0) {
            return "";
        }
        $this$buildString.delete($this$buildString.length() - 2, $this$buildString.length());
        String result2 = $this$buildString.toString();
        Intrinsics.checkExpressionValueIsNotNull(result2, "StringBuilder().apply(builderAction).toString()");
        this.headerValue = result2;
        return result2;
    }

    /* compiled from: CacheControl.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\r\u001a\u00020\u000eJ\u0006\u0010\u0003\u001a\u00020\u0000J\u0016\u0010\u000f\u001a\u00020\u00002\u0006\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0011J\u0016\u0010\u0012\u001a\u00020\u00002\u0006\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0011J\u0016\u0010\u0013\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\t\u001a\u00020\u0000J\u0006\u0010\n\u001a\u00020\u0000J\u0006\u0010\u000b\u001a\u00020\u0000J\u0006\u0010\f\u001a\u00020\u0000J\f\u0010\u0014\u001a\u00020\u0006*\u00020\u0015H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0016"}, d2 = {"Lokhttp3/CacheControl$Builder;", "", "()V", "immutable", "", "maxAgeSeconds", "", "maxStaleSeconds", "minFreshSeconds", "noCache", "noStore", "noTransform", "onlyIfCached", "build", "Lokhttp3/CacheControl;", "maxAge", "timeUnit", "Ljava/util/concurrent/TimeUnit;", "maxStale", "minFresh", "clampToInt", "", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Builder {
        private boolean immutable;
        private int maxAgeSeconds = -1;
        private int maxStaleSeconds = -1;
        private int minFreshSeconds = -1;
        private boolean noCache;
        private boolean noStore;
        private boolean noTransform;
        private boolean onlyIfCached;

        public final Builder noCache() {
            this.noCache = true;
            return this;
        }

        public final Builder noStore() {
            this.noStore = true;
            return this;
        }

        public final Builder maxAge(int maxAge, TimeUnit timeUnit) {
            Intrinsics.checkParameterIsNotNull(timeUnit, "timeUnit");
            Builder $this$apply = this;
            if (maxAge >= 0) {
                $this$apply.maxAgeSeconds = $this$apply.clampToInt(timeUnit.toSeconds((long) maxAge));
                return this;
            }
            throw new IllegalArgumentException(("maxAge < 0: " + maxAge).toString());
        }

        public final Builder maxStale(int maxStale, TimeUnit timeUnit) {
            Intrinsics.checkParameterIsNotNull(timeUnit, "timeUnit");
            Builder $this$apply = this;
            if (maxStale >= 0) {
                $this$apply.maxStaleSeconds = $this$apply.clampToInt(timeUnit.toSeconds((long) maxStale));
                return this;
            }
            throw new IllegalArgumentException(("maxStale < 0: " + maxStale).toString());
        }

        public final Builder minFresh(int minFresh, TimeUnit timeUnit) {
            Intrinsics.checkParameterIsNotNull(timeUnit, "timeUnit");
            Builder $this$apply = this;
            if (minFresh >= 0) {
                $this$apply.minFreshSeconds = $this$apply.clampToInt(timeUnit.toSeconds((long) minFresh));
                return this;
            }
            throw new IllegalArgumentException(("minFresh < 0: " + minFresh).toString());
        }

        public final Builder onlyIfCached() {
            this.onlyIfCached = true;
            return this;
        }

        public final Builder noTransform() {
            this.noTransform = true;
            return this;
        }

        public final Builder immutable() {
            this.immutable = true;
            return this;
        }

        private final int clampToInt(long $this$clampToInt) {
            if ($this$clampToInt > ((long) Integer.MAX_VALUE)) {
                return Integer.MAX_VALUE;
            }
            return (int) $this$clampToInt;
        }

        public final CacheControl build() {
            return new CacheControl(this.noCache, this.noStore, this.maxAgeSeconds, -1, false, false, false, this.maxStaleSeconds, this.minFreshSeconds, this.onlyIfCached, this.noTransform, this.immutable, null, null);
        }
    }

    /* compiled from: CacheControl.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0007J\u001e\u0010\t\u001a\u00020\n*\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\nH\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lokhttp3/CacheControl$Companion;", "", "()V", "FORCE_CACHE", "Lokhttp3/CacheControl;", "FORCE_NETWORK", "parse", "headers", "Lokhttp3/Headers;", "indexOfElement", "", "", "characters", "startIndex", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        /* JADX INFO: Multiple debug info for r1v3 int: [D('noCache' boolean), D('i' int)] */
        /* JADX WARN: Removed duplicated region for block: B:49:0x012b  */
        /* JADX WARN: Removed duplicated region for block: B:50:0x0138  */
        @JvmStatic
        /* Code decompiled incorrectly, please refer to instructions dump */
        public final CacheControl parse(Headers headers) {
            boolean mustRevalidate;
            boolean isPublic;
            int pos;
            String parameter;
            Headers headers2 = headers;
            Intrinsics.checkParameterIsNotNull(headers2, "headers");
            int sMaxAgeSeconds = -1;
            boolean isPrivate = false;
            boolean isPublic2 = false;
            boolean mustRevalidate2 = false;
            int maxStaleSeconds = -1;
            int minFreshSeconds = -1;
            boolean onlyIfCached = false;
            boolean noTransform = false;
            boolean immutable = false;
            boolean canUseHeaderValue = true;
            String headerValue = null;
            int i = 0;
            int maxAgeSeconds = -1;
            boolean noStore = false;
            boolean noCache = false;
            for (int size = headers.size(); i < size; size = size) {
                String name = headers2.name(i);
                boolean immutable2 = immutable;
                String value = headers2.value(i);
                boolean noTransform2 = noTransform;
                if (StringsKt.equals(name, HttpHeaders.CACHE_CONTROL, true)) {
                    if (headerValue != null) {
                        canUseHeaderValue = false;
                    } else {
                        headerValue = value;
                    }
                } else if (StringsKt.equals(name, HttpHeaders.PRAGMA, true)) {
                    canUseHeaderValue = false;
                } else {
                    continue;
                    immutable = immutable2;
                    noTransform = noTransform2;
                    i++;
                    headers2 = headers;
                }
                int pos2 = 0;
                while (pos2 < value.length()) {
                    boolean noCache2 = noCache;
                    int pos3 = indexOfElement(value, "=,;", pos2);
                    if (value != null) {
                        String substring = value.substring(pos2, pos3);
                        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                        if (substring != null) {
                            String directive = StringsKt.trim((CharSequence) substring).toString();
                            boolean isPrivate2 = isPrivate;
                            if (pos3 != value.length()) {
                                isPublic = isPublic2;
                                if (value.charAt(pos3) == ',') {
                                    mustRevalidate = mustRevalidate2;
                                } else if (value.charAt(pos3) == ';') {
                                    mustRevalidate = mustRevalidate2;
                                } else {
                                    int pos4 = Util.indexOfNonWhitespace(value, pos3 + 1);
                                    if (pos4 >= value.length() || value.charAt(pos4) != '\"') {
                                        mustRevalidate = mustRevalidate2;
                                        pos = indexOfElement(value, ",;", pos4);
                                        if (value != null) {
                                            String substring2 = value.substring(pos4, pos);
                                            Intrinsics.checkExpressionValueIsNotNull(substring2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                                            if (substring2 != null) {
                                                parameter = StringsKt.trim((CharSequence) substring2).toString();
                                            } else {
                                                throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                                            }
                                        } else {
                                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                                        }
                                    } else {
                                        int pos5 = pos4 + 1;
                                        int pos6 = StringsKt.indexOf$default((CharSequence) value, (char) Typography.quote, pos5, false, 4, (Object) null);
                                        if (value != null) {
                                            String substring3 = value.substring(pos5, pos6);
                                            Intrinsics.checkExpressionValueIsNotNull(substring3, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                                            parameter = substring3;
                                            pos = pos6 + 1;
                                            mustRevalidate = mustRevalidate2;
                                        } else {
                                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                                        }
                                    }
                                    if (!StringsKt.equals("no-cache", directive, true)) {
                                        noCache2 = true;
                                        noStore = noStore;
                                        sMaxAgeSeconds = sMaxAgeSeconds;
                                        isPublic2 = isPublic;
                                        mustRevalidate2 = mustRevalidate;
                                    } else if (StringsKt.equals("no-store", directive, true)) {
                                        noStore = true;
                                        sMaxAgeSeconds = sMaxAgeSeconds;
                                        isPublic2 = isPublic;
                                        mustRevalidate2 = mustRevalidate;
                                    } else if (StringsKt.equals("max-age", directive, true)) {
                                        maxAgeSeconds = Util.toNonNegativeInt(parameter, -1);
                                        noStore = noStore;
                                        sMaxAgeSeconds = sMaxAgeSeconds;
                                        isPublic2 = isPublic;
                                        mustRevalidate2 = mustRevalidate;
                                    } else if (StringsKt.equals("s-maxage", directive, true)) {
                                        sMaxAgeSeconds = Util.toNonNegativeInt(parameter, -1);
                                        noStore = noStore;
                                        isPublic2 = isPublic;
                                        mustRevalidate2 = mustRevalidate;
                                    } else if (StringsKt.equals("private", directive, true)) {
                                        isPrivate2 = true;
                                        noStore = noStore;
                                        sMaxAgeSeconds = sMaxAgeSeconds;
                                        isPublic2 = isPublic;
                                        mustRevalidate2 = mustRevalidate;
                                    } else if (StringsKt.equals("public", directive, true)) {
                                        isPublic2 = true;
                                        noStore = noStore;
                                        sMaxAgeSeconds = sMaxAgeSeconds;
                                        mustRevalidate2 = mustRevalidate;
                                    } else if (StringsKt.equals("must-revalidate", directive, true)) {
                                        mustRevalidate2 = true;
                                        noStore = noStore;
                                        sMaxAgeSeconds = sMaxAgeSeconds;
                                        isPublic2 = isPublic;
                                    } else if (StringsKt.equals("max-stale", directive, true)) {
                                        maxStaleSeconds = Util.toNonNegativeInt(parameter, Integer.MAX_VALUE);
                                        noStore = noStore;
                                        sMaxAgeSeconds = sMaxAgeSeconds;
                                        isPublic2 = isPublic;
                                        mustRevalidate2 = mustRevalidate;
                                    } else if (StringsKt.equals("min-fresh", directive, true)) {
                                        minFreshSeconds = Util.toNonNegativeInt(parameter, -1);
                                        noStore = noStore;
                                        sMaxAgeSeconds = sMaxAgeSeconds;
                                        isPublic2 = isPublic;
                                        mustRevalidate2 = mustRevalidate;
                                    } else if (StringsKt.equals("only-if-cached", directive, true)) {
                                        onlyIfCached = true;
                                        noStore = noStore;
                                        sMaxAgeSeconds = sMaxAgeSeconds;
                                        isPublic2 = isPublic;
                                        mustRevalidate2 = mustRevalidate;
                                    } else if (StringsKt.equals("no-transform", directive, true)) {
                                        noTransform2 = true;
                                        noStore = noStore;
                                        sMaxAgeSeconds = sMaxAgeSeconds;
                                        isPublic2 = isPublic;
                                        mustRevalidate2 = mustRevalidate;
                                    } else if (StringsKt.equals("immutable", directive, true)) {
                                        immutable2 = true;
                                        noStore = noStore;
                                        sMaxAgeSeconds = sMaxAgeSeconds;
                                        isPublic2 = isPublic;
                                        mustRevalidate2 = mustRevalidate;
                                    } else {
                                        noStore = noStore;
                                        sMaxAgeSeconds = sMaxAgeSeconds;
                                        isPublic2 = isPublic;
                                        mustRevalidate2 = mustRevalidate;
                                    }
                                    pos2 = pos;
                                    noCache = noCache2;
                                    isPrivate = isPrivate2;
                                }
                            } else {
                                isPublic = isPublic2;
                                mustRevalidate = mustRevalidate2;
                            }
                            pos = pos3 + 1;
                            parameter = null;
                            if (!StringsKt.equals("no-cache", directive, true)) {
                            }
                            pos2 = pos;
                            noCache = noCache2;
                            isPrivate = isPrivate2;
                        } else {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                        }
                    } else {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                }
                immutable = immutable2;
                noTransform = noTransform2;
                i++;
                headers2 = headers;
            }
            if (!canUseHeaderValue) {
                headerValue = null;
            }
            return new CacheControl(noCache, noStore, maxAgeSeconds, sMaxAgeSeconds, isPrivate, isPublic2, mustRevalidate2, maxStaleSeconds, minFreshSeconds, onlyIfCached, noTransform, immutable, headerValue, null);
        }

        static /* synthetic */ int indexOfElement$default(Companion companion, String str, String str2, int i, int i2, Object obj) {
            if ((i2 & 2) != 0) {
                i = 0;
            }
            return companion.indexOfElement(str, str2, i);
        }

        private final int indexOfElement(String $this$indexOfElement, String characters, int startIndex) {
            int length = $this$indexOfElement.length();
            for (int i = startIndex; i < length; i++) {
                if (StringsKt.contains$default((CharSequence) characters, $this$indexOfElement.charAt(i), false, 2, (Object) null)) {
                    return i;
                }
            }
            return $this$indexOfElement.length();
        }
    }
}
