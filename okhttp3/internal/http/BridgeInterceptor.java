package okhttp3.internal.http;

import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okhttp3.internal.Version;
import okio.GzipSource;
import okio.Okio;
/* compiled from: BridgeInterceptor.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0002J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lokhttp3/internal/http/BridgeInterceptor;", "Lokhttp3/Interceptor;", "cookieJar", "Lokhttp3/CookieJar;", "(Lokhttp3/CookieJar;)V", "cookieHeader", "", "cookies", "", "Lokhttp3/Cookie;", "intercept", "Lokhttp3/Response;", "chain", "Lokhttp3/Interceptor$Chain;", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class BridgeInterceptor implements Interceptor {
    private final CookieJar cookieJar;

    public BridgeInterceptor(CookieJar cookieJar) {
        Intrinsics.checkParameterIsNotNull(cookieJar, "cookieJar");
        this.cookieJar = cookieJar;
    }

    @Override // okhttp3.Interceptor
    public Response intercept(Interceptor.Chain chain) throws IOException {
        ResponseBody responseBody;
        Intrinsics.checkParameterIsNotNull(chain, "chain");
        Request userRequest = chain.request();
        Request.Builder requestBuilder = userRequest.newBuilder();
        RequestBody body = userRequest.body();
        if (body != null) {
            MediaType contentType = body.contentType();
            if (contentType != null) {
                requestBuilder.header(HttpHeaders.CONTENT_TYPE, contentType.toString());
            }
            long contentLength = body.contentLength();
            if (contentLength != -1) {
                requestBuilder.header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
                requestBuilder.removeHeader(HttpHeaders.TRANSFER_ENCODING);
            } else {
                requestBuilder.header(HttpHeaders.TRANSFER_ENCODING, "chunked");
                requestBuilder.removeHeader(HttpHeaders.CONTENT_LENGTH);
            }
        }
        if (userRequest.header(HttpHeaders.HOST) == null) {
            requestBuilder.header(HttpHeaders.HOST, Util.toHostHeader$default(userRequest.url(), false, 1, null));
        }
        if (userRequest.header(HttpHeaders.CONNECTION) == null) {
            requestBuilder.header(HttpHeaders.CONNECTION, "Keep-Alive");
        }
        boolean transparentGzip = false;
        if (userRequest.header(HttpHeaders.ACCEPT_ENCODING) == null && userRequest.header(HttpHeaders.RANGE) == null) {
            transparentGzip = true;
            requestBuilder.header(HttpHeaders.ACCEPT_ENCODING, "gzip");
        }
        List cookies = this.cookieJar.loadForRequest(userRequest.url());
        if (!cookies.isEmpty()) {
            requestBuilder.header(HttpHeaders.COOKIE, cookieHeader(cookies));
        }
        if (userRequest.header(HttpHeaders.USER_AGENT) == null) {
            requestBuilder.header(HttpHeaders.USER_AGENT, Version.userAgent);
        }
        Response networkResponse = chain.proceed(requestBuilder.build());
        HttpHeaders.receiveHeaders(this.cookieJar, userRequest.url(), networkResponse.headers());
        Response.Builder responseBuilder = networkResponse.newBuilder().request(userRequest);
        if (transparentGzip && StringsKt.equals("gzip", Response.header$default(networkResponse, HttpHeaders.CONTENT_ENCODING, null, 2, null), true) && HttpHeaders.promisesBody(networkResponse) && (responseBody = networkResponse.body()) != null) {
            GzipSource gzipSource = new GzipSource(responseBody.source());
            responseBuilder.headers(networkResponse.headers().newBuilder().removeAll(HttpHeaders.CONTENT_ENCODING).removeAll(HttpHeaders.CONTENT_LENGTH).build());
            responseBuilder.body(new RealResponseBody(Response.header$default(networkResponse, HttpHeaders.CONTENT_TYPE, null, 2, null), -1, Okio.buffer(gzipSource)));
        }
        return responseBuilder.build();
    }

    /* JADX INFO: Multiple debug info for r5v1 'index$iv'  int: [D('index' int), D('index$iv' int)] */
    private final String cookieHeader(List<Cookie> $this$forEachIndexed$iv) {
        StringBuilder $this$buildString = new StringBuilder();
        int index = 0;
        for (Object item$iv : $this$forEachIndexed$iv) {
            int index$iv = index + 1;
            if (index < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            Cookie cookie = (Cookie) item$iv;
            if (index > 0) {
                $this$buildString.append("; ");
            }
            $this$buildString.append(cookie.name());
            $this$buildString.append('=');
            $this$buildString.append(cookie.value());
            index = index$iv;
        }
        String sb = $this$buildString.toString();
        Intrinsics.checkExpressionValueIsNotNull(sb, "StringBuilder().apply(builderAction).toString()");
        return sb;
    }
}
