package okhttp3.internal.cache;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.Cache;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okhttp3.internal.cache.CacheStrategy;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.RealResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;
/* compiled from: CacheInterceptor.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fB\u000f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0004J\u001a\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\bH\u0002J\u0010\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000eH\u0016R\u0016\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0010"}, d2 = {"Lokhttp3/internal/cache/CacheInterceptor;", "Lokhttp3/Interceptor;", "cache", "Lokhttp3/Cache;", "(Lokhttp3/Cache;)V", "getCache$okhttp", "()Lokhttp3/Cache;", "cacheWritingResponse", "Lokhttp3/Response;", "cacheRequest", "Lokhttp3/internal/cache/CacheRequest;", "response", "intercept", "chain", "Lokhttp3/Interceptor$Chain;", "Companion", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class CacheInterceptor implements Interceptor {
    public static final Companion Companion = new Companion(null);
    private final Cache cache;

    public CacheInterceptor(Cache cache) {
        this.cache = cache;
    }

    public final Cache getCache$okhttp() {
        return this.cache;
    }

    @Override // okhttp3.Interceptor
    public Response intercept(Interceptor.Chain chain) throws IOException {
        ResponseBody body;
        ResponseBody body2;
        Intrinsics.checkParameterIsNotNull(chain, "chain");
        Cache cache = this.cache;
        Response cacheCandidate = cache != null ? cache.get$okhttp(chain.request()) : null;
        CacheStrategy strategy = new CacheStrategy.Factory(System.currentTimeMillis(), chain.request(), cacheCandidate).compute();
        Request networkRequest = strategy.getNetworkRequest();
        Response cacheResponse = strategy.getCacheResponse();
        Cache cache2 = this.cache;
        if (cache2 != null) {
            cache2.trackResponse$okhttp(strategy);
        }
        if (!(cacheCandidate == null || cacheResponse != null || (body2 = cacheCandidate.body()) == null)) {
            Util.closeQuietly(body2);
        }
        if (networkRequest == null && cacheResponse == null) {
            return new Response.Builder().request(chain.request()).protocol(Protocol.HTTP_1_1).code(504).message("Unsatisfiable Request (only-if-cached)").body(Util.EMPTY_RESPONSE).sentRequestAtMillis(-1).receivedResponseAtMillis(System.currentTimeMillis()).build();
        }
        if (networkRequest == null) {
            if (cacheResponse == null) {
                Intrinsics.throwNpe();
            }
            return cacheResponse.newBuilder().cacheResponse(Companion.stripBody(cacheResponse)).build();
        }
        try {
            Response networkResponse = chain.proceed(networkRequest);
            if (!(networkResponse != null || cacheCandidate == null || body == null)) {
            }
            if (cacheResponse != null) {
                if (networkResponse == null || networkResponse.code() != 304) {
                    ResponseBody body3 = cacheResponse.body();
                    if (body3 != null) {
                        Util.closeQuietly(body3);
                    }
                } else {
                    Response response = cacheResponse.newBuilder().headers(Companion.combine(cacheResponse.headers(), networkResponse.headers())).sentRequestAtMillis(networkResponse.sentRequestAtMillis()).receivedResponseAtMillis(networkResponse.receivedResponseAtMillis()).cacheResponse(Companion.stripBody(cacheResponse)).networkResponse(Companion.stripBody(networkResponse)).build();
                    ResponseBody body4 = networkResponse.body();
                    if (body4 == null) {
                        Intrinsics.throwNpe();
                    }
                    body4.close();
                    Cache cache3 = this.cache;
                    if (cache3 == null) {
                        Intrinsics.throwNpe();
                    }
                    cache3.trackConditionalCacheHit$okhttp();
                    this.cache.update$okhttp(cacheResponse, response);
                    return response;
                }
            }
            if (networkResponse == null) {
                Intrinsics.throwNpe();
            }
            Response response2 = networkResponse.newBuilder().cacheResponse(Companion.stripBody(cacheResponse)).networkResponse(Companion.stripBody(networkResponse)).build();
            if (this.cache != null) {
                if (HttpHeaders.promisesBody(response2) && CacheStrategy.Companion.isCacheable(response2, networkRequest)) {
                    return cacheWritingResponse(this.cache.put$okhttp(response2), response2);
                }
                if (HttpMethod.INSTANCE.invalidatesCache(networkRequest.method())) {
                    try {
                        this.cache.remove$okhttp(networkRequest);
                    } catch (IOException e) {
                    }
                }
            }
            return response2;
        } finally {
            if (!(cacheCandidate == null || (body = cacheCandidate.body()) == null)) {
                Util.closeQuietly(body);
            }
        }
    }

    private final Response cacheWritingResponse(CacheRequest cacheRequest, Response response) throws IOException {
        if (cacheRequest == null) {
            return response;
        }
        Sink cacheBodyUnbuffered = cacheRequest.body();
        ResponseBody body = response.body();
        if (body == null) {
            Intrinsics.throwNpe();
        }
        CacheInterceptor$cacheWritingResponse$cacheWritingSource$1 cacheWritingSource = new Source(cacheRequest, Okio.buffer(cacheBodyUnbuffered)) { // from class: okhttp3.internal.cache.CacheInterceptor$cacheWritingResponse$cacheWritingSource$1
            final /* synthetic */ BufferedSink $cacheBody;
            final /* synthetic */ CacheRequest $cacheRequest;
            private boolean cacheRequestClosed;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$cacheRequest = $captured_local_variable$1;
                this.$cacheBody = $captured_local_variable$2;
            }

            public final boolean getCacheRequestClosed() {
                return this.cacheRequestClosed;
            }

            public final void setCacheRequestClosed(boolean z) {
                this.cacheRequestClosed = z;
            }

            @Override // okio.Source
            public long read(Buffer sink, long byteCount) throws IOException {
                Intrinsics.checkParameterIsNotNull(sink, "sink");
                try {
                    long bytesRead = BufferedSource.this.read(sink, byteCount);
                    if (bytesRead == -1) {
                        if (!this.cacheRequestClosed) {
                            this.cacheRequestClosed = true;
                            this.$cacheBody.close();
                        }
                        return -1;
                    }
                    sink.copyTo(this.$cacheBody.getBuffer(), sink.size() - bytesRead, bytesRead);
                    this.$cacheBody.emitCompleteSegments();
                    return bytesRead;
                } catch (IOException e) {
                    if (!this.cacheRequestClosed) {
                        this.cacheRequestClosed = true;
                        this.$cacheRequest.abort();
                    }
                    throw e;
                }
            }

            @Override // okio.Source
            public Timeout timeout() {
                return BufferedSource.this.timeout();
            }

            @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                if (!this.cacheRequestClosed && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                    this.cacheRequestClosed = true;
                    this.$cacheRequest.abort();
                }
                BufferedSource.this.close();
            }
        };
        return response.newBuilder().body(new RealResponseBody(Response.header$default(response, com.google.common.net.HttpHeaders.CONTENT_TYPE, null, 2, null), response.body().contentLength(), Okio.buffer(cacheWritingSource))).build();
    }

    /* compiled from: CacheInterceptor.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0004H\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002J\u0010\u0010\u000b\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002J\u0014\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\rH\u0002¨\u0006\u000f"}, d2 = {"Lokhttp3/internal/cache/CacheInterceptor$Companion;", "", "()V", "combine", "Lokhttp3/Headers;", "cachedHeaders", "networkHeaders", "isContentSpecificHeader", "", "fieldName", "", "isEndToEnd", "stripBody", "Lokhttp3/Response;", "response", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final Response stripBody(Response response) {
            if ((response != null ? response.body() : null) != null) {
                return response.newBuilder().body(null).build();
            }
            return response;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final Headers combine(Headers cachedHeaders, Headers networkHeaders) {
            Headers.Builder result = new Headers.Builder();
            int size = cachedHeaders.size();
            for (int index = 0; index < size; index++) {
                String fieldName = cachedHeaders.name(index);
                String value = cachedHeaders.value(index);
                if ((!StringsKt.equals(com.google.common.net.HttpHeaders.WARNING, fieldName, true) || !StringsKt.startsWith$default(value, DiskLruCache.VERSION_1, false, 2, (Object) null)) && (isContentSpecificHeader(fieldName) || !isEndToEnd(fieldName) || networkHeaders.get(fieldName) == null)) {
                    result.addLenient$okhttp(fieldName, value);
                }
            }
            int size2 = networkHeaders.size();
            for (int index2 = 0; index2 < size2; index2++) {
                String fieldName2 = networkHeaders.name(index2);
                if (!isContentSpecificHeader(fieldName2) && isEndToEnd(fieldName2)) {
                    result.addLenient$okhttp(fieldName2, networkHeaders.value(index2));
                }
            }
            return result.build();
        }

        private final boolean isEndToEnd(String fieldName) {
            if (StringsKt.equals(com.google.common.net.HttpHeaders.CONNECTION, fieldName, true) || StringsKt.equals("Keep-Alive", fieldName, true) || StringsKt.equals(com.google.common.net.HttpHeaders.PROXY_AUTHENTICATE, fieldName, true) || StringsKt.equals(com.google.common.net.HttpHeaders.PROXY_AUTHORIZATION, fieldName, true) || StringsKt.equals(com.google.common.net.HttpHeaders.TE, fieldName, true) || StringsKt.equals("Trailers", fieldName, true) || StringsKt.equals(com.google.common.net.HttpHeaders.TRANSFER_ENCODING, fieldName, true) || StringsKt.equals(com.google.common.net.HttpHeaders.UPGRADE, fieldName, true)) {
                return false;
            }
            return true;
        }

        private final boolean isContentSpecificHeader(String fieldName) {
            if (StringsKt.equals(com.google.common.net.HttpHeaders.CONTENT_LENGTH, fieldName, true) || StringsKt.equals(com.google.common.net.HttpHeaders.CONTENT_ENCODING, fieldName, true) || StringsKt.equals(com.google.common.net.HttpHeaders.CONTENT_TYPE, fieldName, true)) {
                return true;
            }
            return false;
        }
    }
}
