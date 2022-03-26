package okhttp3;

import androidx.exifinterface.media.ExifInterface;
import com.android.volley.toolbox.HttpClientStack;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.common.net.HttpHeaders;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ReplaceWith;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpMethod;
/* compiled from: Request.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001:\u0001*BA\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t\u0012\u0016\u0010\n\u001a\u0012\u0012\b\u0012\u0006\u0012\u0002\b\u00030\f\u0012\u0004\u0012\u00020\u00010\u000b¢\u0006\u0002\u0010\rJ\u000f\u0010\b\u001a\u0004\u0018\u00010\tH\u0007¢\u0006\u0002\b\u001bJ\r\u0010\u000f\u001a\u00020\u0010H\u0007¢\u0006\u0002\b\u001cJ\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u001e\u001a\u00020\u0005J\r\u0010\u0006\u001a\u00020\u0007H\u0007¢\u0006\u0002\b\u001fJ\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050 2\u0006\u0010\u001e\u001a\u00020\u0005J\r\u0010\u0004\u001a\u00020\u0005H\u0007¢\u0006\u0002\b!J\u0006\u0010\"\u001a\u00020#J\b\u0010$\u001a\u0004\u0018\u00010\u0001J#\u0010$\u001a\u0004\u0018\u0001H%\"\u0004\b\u0000\u0010%2\u000e\u0010&\u001a\n\u0012\u0006\b\u0001\u0012\u0002H%0\f¢\u0006\u0002\u0010'J\b\u0010(\u001a\u00020\u0005H\u0016J\r\u0010\u0002\u001a\u00020\u0003H\u0007¢\u0006\u0002\b)R\u0015\u0010\b\u001a\u0004\u0018\u00010\t8\u0007¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u00108G¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0011R\u0013\u0010\u0006\u001a\u00020\u00078\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0012R\u0011\u0010\u0013\u001a\u00020\u00148F¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0015R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u0013\u0010\u0004\u001a\u00020\u00058\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0017R$\u0010\n\u001a\u0012\u0012\b\u0012\u0006\u0012\u0002\b\u00030\f\u0012\u0004\u0012\u00020\u00010\u000bX\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0013\u0010\u0002\u001a\u00020\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u001a¨\u0006+"}, d2 = {"Lokhttp3/Request;", "", ImagesContract.URL, "Lokhttp3/HttpUrl;", FirebaseAnalytics.Param.METHOD, "", "headers", "Lokhttp3/Headers;", "body", "Lokhttp3/RequestBody;", "tags", "", "Ljava/lang/Class;", "(Lokhttp3/HttpUrl;Ljava/lang/String;Lokhttp3/Headers;Lokhttp3/RequestBody;Ljava/util/Map;)V", "()Lokhttp3/RequestBody;", "cacheControl", "Lokhttp3/CacheControl;", "()Lokhttp3/CacheControl;", "()Lokhttp3/Headers;", "isHttps", "", "()Z", "lazyCacheControl", "()Ljava/lang/String;", "getTags$okhttp", "()Ljava/util/Map;", "()Lokhttp3/HttpUrl;", "-deprecated_body", "-deprecated_cacheControl", "header", AppMeasurementSdk.ConditionalUserProperty.NAME, "-deprecated_headers", "", "-deprecated_method", "newBuilder", "Lokhttp3/Request$Builder;", "tag", ExifInterface.GPS_DIRECTION_TRUE, "type", "(Ljava/lang/Class;)Ljava/lang/Object;", "toString", "-deprecated_url", "Builder", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class Request {
    private final RequestBody body;
    private final Headers headers;
    private CacheControl lazyCacheControl;
    private final String method;
    private final Map<Class<?>, Object> tags;
    private final HttpUrl url;

    public Request(HttpUrl url, String method, Headers headers, RequestBody body, Map<Class<?>, ? extends Object> map) {
        Intrinsics.checkParameterIsNotNull(url, ImagesContract.URL);
        Intrinsics.checkParameterIsNotNull(method, FirebaseAnalytics.Param.METHOD);
        Intrinsics.checkParameterIsNotNull(headers, "headers");
        Intrinsics.checkParameterIsNotNull(map, "tags");
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.body = body;
        this.tags = map;
    }

    public final HttpUrl url() {
        return this.url;
    }

    public final String method() {
        return this.method;
    }

    public final Headers headers() {
        return this.headers;
    }

    public final RequestBody body() {
        return this.body;
    }

    public final Map<Class<?>, Object> getTags$okhttp() {
        return this.tags;
    }

    public final boolean isHttps() {
        return this.url.isHttps();
    }

    public final String header(String name) {
        Intrinsics.checkParameterIsNotNull(name, AppMeasurementSdk.ConditionalUserProperty.NAME);
        return this.headers.get(name);
    }

    public final List<String> headers(String name) {
        Intrinsics.checkParameterIsNotNull(name, AppMeasurementSdk.ConditionalUserProperty.NAME);
        return this.headers.values(name);
    }

    public final Object tag() {
        return tag(Object.class);
    }

    public final <T> T tag(Class<? extends T> cls) {
        Intrinsics.checkParameterIsNotNull(cls, "type");
        return (T) cls.cast(this.tags.get(cls));
    }

    public final Builder newBuilder() {
        return new Builder(this);
    }

    public final CacheControl cacheControl() {
        CacheControl result = this.lazyCacheControl;
        if (result != null) {
            return result;
        }
        CacheControl result2 = CacheControl.Companion.parse(this.headers);
        this.lazyCacheControl = result2;
        return result2;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "url", imports = {}))
    /* renamed from: -deprecated_url */
    public final HttpUrl m1134deprecated_url() {
        return this.url;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "method", imports = {}))
    /* renamed from: -deprecated_method */
    public final String m1133deprecated_method() {
        return this.method;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "headers", imports = {}))
    /* renamed from: -deprecated_headers */
    public final Headers m1132deprecated_headers() {
        return this.headers;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "body", imports = {}))
    /* renamed from: -deprecated_body */
    public final RequestBody m1130deprecated_body() {
        return this.body;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "cacheControl", imports = {}))
    /* renamed from: -deprecated_cacheControl */
    public final CacheControl m1131deprecated_cacheControl() {
        return cacheControl();
    }

    /* JADX INFO: Multiple debug info for r5v1 'index$iv'  int: [D('index' int), D('index$iv' int)] */
    public String toString() {
        StringBuilder $this$buildString = new StringBuilder();
        $this$buildString.append("Request{method=");
        $this$buildString.append(this.method);
        $this$buildString.append(", url=");
        $this$buildString.append(this.url);
        if (this.headers.size() != 0) {
            $this$buildString.append(", headers=[");
            int index = 0;
            for (Pair item$iv : this.headers) {
                int index$iv = index + 1;
                if (index < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                Pair $dstr$name$value = item$iv;
                String name = (String) $dstr$name$value.component1();
                String value = (String) $dstr$name$value.component2();
                if (index > 0) {
                    $this$buildString.append(", ");
                }
                $this$buildString.append(name);
                $this$buildString.append(':');
                $this$buildString.append(value);
                index = index$iv;
            }
            $this$buildString.append(']');
        }
        if (!this.tags.isEmpty()) {
            $this$buildString.append(", tags=");
            $this$buildString.append(this.tags);
        }
        $this$buildString.append('}');
        String sb = $this$buildString.toString();
        Intrinsics.checkExpressionValueIsNotNull(sb, "StringBuilder().apply(builderAction).toString()");
        return sb;
    }

    /* compiled from: Request.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\b\u0016\u0018\u00002\u00020\u0001B\u0007\b\u0016¢\u0006\u0002\u0010\u0002B\u000f\b\u0010\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0018\u0010%\u001a\u00020\u00002\u0006\u0010&\u001a\u00020\u00132\u0006\u0010'\u001a\u00020\u0013H\u0016J\b\u0010(\u001a\u00020\u0004H\u0016J\u0010\u0010)\u001a\u00020\u00002\u0006\u0010)\u001a\u00020*H\u0016J\u0014\u0010+\u001a\u00020\u00002\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0017J\b\u0010,\u001a\u00020\u0000H\u0016J\b\u0010-\u001a\u00020\u0000H\u0016J\u0018\u0010.\u001a\u00020\u00002\u0006\u0010&\u001a\u00020\u00132\u0006\u0010'\u001a\u00020\u0013H\u0016J\u0010\u0010\f\u001a\u00020\u00002\u0006\u0010\f\u001a\u00020/H\u0016J\u001a\u0010\u0012\u001a\u00020\u00002\u0006\u0010\u0012\u001a\u00020\u00132\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0016J\u0010\u00100\u001a\u00020\u00002\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u00101\u001a\u00020\u00002\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u00102\u001a\u00020\u00002\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u00103\u001a\u00020\u00002\u0006\u0010&\u001a\u00020\u0013H\u0016J-\u00104\u001a\u00020\u0000\"\u0004\b\u0000\u001052\u000e\u00106\u001a\n\u0012\u0006\b\u0000\u0012\u0002H50\u001a2\b\u00104\u001a\u0004\u0018\u0001H5H\u0016¢\u0006\u0002\u00107J\u0012\u00104\u001a\u00020\u00002\b\u00104\u001a\u0004\u0018\u00010\u0001H\u0016J\u0010\u0010\u001f\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u000208H\u0016J\u0010\u0010\u001f\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020\u0013H\u0016J\u0010\u0010\u001f\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020 H\u0016R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001a\u0010\f\u001a\u00020\rX\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u00020\u0013X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R*\u0010\u0018\u001a\u0012\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u001a\u0012\u0004\u0012\u00020\u00010\u0019X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001c\u0010\u001f\u001a\u0004\u0018\u00010 X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$¨\u00069"}, d2 = {"Lokhttp3/Request$Builder;", "", "()V", "request", "Lokhttp3/Request;", "(Lokhttp3/Request;)V", "body", "Lokhttp3/RequestBody;", "getBody$okhttp", "()Lokhttp3/RequestBody;", "setBody$okhttp", "(Lokhttp3/RequestBody;)V", "headers", "Lokhttp3/Headers$Builder;", "getHeaders$okhttp", "()Lokhttp3/Headers$Builder;", "setHeaders$okhttp", "(Lokhttp3/Headers$Builder;)V", FirebaseAnalytics.Param.METHOD, "", "getMethod$okhttp", "()Ljava/lang/String;", "setMethod$okhttp", "(Ljava/lang/String;)V", "tags", "", "Ljava/lang/Class;", "getTags$okhttp", "()Ljava/util/Map;", "setTags$okhttp", "(Ljava/util/Map;)V", ImagesContract.URL, "Lokhttp3/HttpUrl;", "getUrl$okhttp", "()Lokhttp3/HttpUrl;", "setUrl$okhttp", "(Lokhttp3/HttpUrl;)V", "addHeader", AppMeasurementSdk.ConditionalUserProperty.NAME, "value", "build", "cacheControl", "Lokhttp3/CacheControl;", "delete", "get", "head", "header", "Lokhttp3/Headers;", "patch", "post", "put", "removeHeader", "tag", ExifInterface.GPS_DIRECTION_TRUE, "type", "(Ljava/lang/Class;Ljava/lang/Object;)Lokhttp3/Request$Builder;", "Ljava/net/URL;", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static class Builder {
        private RequestBody body;
        private Headers.Builder headers;
        private String method;
        private Map<Class<?>, Object> tags;
        private HttpUrl url;

        public Builder delete() {
            return delete$default(this, null, 1, null);
        }

        public final HttpUrl getUrl$okhttp() {
            return this.url;
        }

        public final void setUrl$okhttp(HttpUrl httpUrl) {
            this.url = httpUrl;
        }

        public final String getMethod$okhttp() {
            return this.method;
        }

        public final void setMethod$okhttp(String str) {
            Intrinsics.checkParameterIsNotNull(str, "<set-?>");
            this.method = str;
        }

        public final Headers.Builder getHeaders$okhttp() {
            return this.headers;
        }

        public final void setHeaders$okhttp(Headers.Builder builder) {
            Intrinsics.checkParameterIsNotNull(builder, "<set-?>");
            this.headers = builder;
        }

        public final RequestBody getBody$okhttp() {
            return this.body;
        }

        public final void setBody$okhttp(RequestBody requestBody) {
            this.body = requestBody;
        }

        public final Map<Class<?>, Object> getTags$okhttp() {
            return this.tags;
        }

        public final void setTags$okhttp(Map<Class<?>, Object> map) {
            Intrinsics.checkParameterIsNotNull(map, "<set-?>");
            this.tags = map;
        }

        public Builder() {
            this.tags = new LinkedHashMap();
            this.method = "GET";
            this.headers = new Headers.Builder();
        }

        public Builder(Request request) {
            LinkedHashMap linkedHashMap;
            Intrinsics.checkParameterIsNotNull(request, "request");
            this.tags = new LinkedHashMap();
            this.url = request.url();
            this.method = request.method();
            this.body = request.body();
            if (request.getTags$okhttp().isEmpty()) {
                linkedHashMap = new LinkedHashMap();
            } else {
                linkedHashMap = MapsKt.toMutableMap(request.getTags$okhttp());
            }
            this.tags = linkedHashMap;
            this.headers = request.headers().newBuilder();
        }

        public Builder url(HttpUrl url) {
            Intrinsics.checkParameterIsNotNull(url, ImagesContract.URL);
            this.url = url;
            return this;
        }

        public Builder url(String url) {
            String finalUrl;
            Intrinsics.checkParameterIsNotNull(url, ImagesContract.URL);
            if (StringsKt.startsWith(url, "ws:", true)) {
                StringBuilder sb = new StringBuilder();
                sb.append("http:");
                String substring = url.substring(3);
                Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
                sb.append(substring);
                finalUrl = sb.toString();
            } else if (StringsKt.startsWith(url, "wss:", true)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("https:");
                String substring2 = url.substring(4);
                Intrinsics.checkExpressionValueIsNotNull(substring2, "(this as java.lang.String).substring(startIndex)");
                sb2.append(substring2);
                finalUrl = sb2.toString();
            } else {
                finalUrl = url;
            }
            return url(HttpUrl.Companion.get(finalUrl));
        }

        public Builder url(URL url) {
            Intrinsics.checkParameterIsNotNull(url, ImagesContract.URL);
            HttpUrl.Companion companion = HttpUrl.Companion;
            String url2 = url.toString();
            Intrinsics.checkExpressionValueIsNotNull(url2, "url.toString()");
            return url(companion.get(url2));
        }

        public Builder header(String name, String value) {
            Intrinsics.checkParameterIsNotNull(name, AppMeasurementSdk.ConditionalUserProperty.NAME);
            Intrinsics.checkParameterIsNotNull(value, "value");
            this.headers.set(name, value);
            return this;
        }

        public Builder addHeader(String name, String value) {
            Intrinsics.checkParameterIsNotNull(name, AppMeasurementSdk.ConditionalUserProperty.NAME);
            Intrinsics.checkParameterIsNotNull(value, "value");
            this.headers.add(name, value);
            return this;
        }

        public Builder removeHeader(String name) {
            Intrinsics.checkParameterIsNotNull(name, AppMeasurementSdk.ConditionalUserProperty.NAME);
            this.headers.removeAll(name);
            return this;
        }

        public Builder headers(Headers headers) {
            Intrinsics.checkParameterIsNotNull(headers, "headers");
            this.headers = headers.newBuilder();
            return this;
        }

        public Builder cacheControl(CacheControl cacheControl) {
            Intrinsics.checkParameterIsNotNull(cacheControl, "cacheControl");
            String value = cacheControl.toString();
            if (value.length() == 0) {
                return removeHeader(HttpHeaders.CACHE_CONTROL);
            }
            return header(HttpHeaders.CACHE_CONTROL, value);
        }

        public Builder get() {
            return method("GET", null);
        }

        public Builder head() {
            return method("HEAD", null);
        }

        public Builder post(RequestBody body) {
            Intrinsics.checkParameterIsNotNull(body, "body");
            return method("POST", body);
        }

        public static /* synthetic */ Builder delete$default(Builder builder, RequestBody requestBody, int i, Object obj) {
            if (obj == null) {
                if ((i & 1) != 0) {
                    requestBody = Util.EMPTY_REQUEST;
                }
                return builder.delete(requestBody);
            }
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: delete");
        }

        public Builder delete(RequestBody body) {
            return method("DELETE", body);
        }

        public Builder put(RequestBody body) {
            Intrinsics.checkParameterIsNotNull(body, "body");
            return method("PUT", body);
        }

        public Builder patch(RequestBody body) {
            Intrinsics.checkParameterIsNotNull(body, "body");
            return method(HttpClientStack.HttpPatch.METHOD_NAME, body);
        }

        public Builder method(String method, RequestBody body) {
            Intrinsics.checkParameterIsNotNull(method, FirebaseAnalytics.Param.METHOD);
            Builder $this$apply = this;
            if (method.length() > 0) {
                if (body == null) {
                    if (!(true ^ HttpMethod.requiresRequestBody(method))) {
                        throw new IllegalArgumentException(("method " + method + " must have a request body.").toString());
                    }
                } else if (!HttpMethod.permitsRequestBody(method)) {
                    throw new IllegalArgumentException(("method " + method + " must not have a request body.").toString());
                }
                $this$apply.method = method;
                $this$apply.body = body;
                return this;
            }
            throw new IllegalArgumentException("method.isEmpty() == true".toString());
        }

        public Builder tag(Object tag) {
            return tag(Object.class, tag);
        }

        public <T> Builder tag(Class<? super T> cls, T t) {
            Intrinsics.checkParameterIsNotNull(cls, "type");
            Builder $this$apply = this;
            if (t == null) {
                $this$apply.tags.remove(cls);
            } else {
                if ($this$apply.tags.isEmpty()) {
                    $this$apply.tags = new LinkedHashMap();
                }
                Map<Class<?>, Object> map = $this$apply.tags;
                Object cast = cls.cast(t);
                if (cast == null) {
                    Intrinsics.throwNpe();
                }
                map.put(cls, cast);
            }
            return this;
        }

        public Request build() {
            HttpUrl httpUrl = this.url;
            if (httpUrl != null) {
                return new Request(httpUrl, this.method, this.headers.build(), this.body, Util.toImmutableMap(this.tags));
            }
            throw new IllegalStateException("url == null".toString());
        }
    }
}
