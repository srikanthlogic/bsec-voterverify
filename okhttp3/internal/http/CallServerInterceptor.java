package okhttp3.internal.http;

import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.net.ProtocolException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okhttp3.internal.connection.Exchange;
import okio.BufferedSink;
import okio.Okio;
/* compiled from: CallServerInterceptor.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lokhttp3/internal/http/CallServerInterceptor;", "Lokhttp3/Interceptor;", "forWebSocket", "", "(Z)V", "intercept", "Lokhttp3/Response;", "chain", "Lokhttp3/Interceptor$Chain;", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class CallServerInterceptor implements Interceptor {
    private final boolean forWebSocket;

    public CallServerInterceptor(boolean forWebSocket) {
        this.forWebSocket = forWebSocket;
    }

    /* JADX WARN: Code restructure failed: missing block: B:49:0x016b, code lost:
        if (kotlin.text.StringsKt.equals("close", okhttp3.Response.header$default(r14, com.google.common.net.HttpHeaders.CONNECTION, null, 2, null), true) != false) goto L_0x016f;
     */
    @Override // okhttp3.Interceptor
    /* Code decompiled incorrectly, please refer to instructions dump */
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Response response;
        Long l;
        Intrinsics.checkParameterIsNotNull(chain, "chain");
        RealInterceptorChain realChain = (RealInterceptorChain) chain;
        Exchange exchange = realChain.getExchange$okhttp();
        if (exchange == null) {
            Intrinsics.throwNpe();
        }
        Request request = realChain.getRequest$okhttp();
        RequestBody requestBody = request.body();
        long sentRequestMillis = System.currentTimeMillis();
        exchange.writeRequestHeaders(request);
        boolean invokeStartEvent = true;
        Response.Builder responseBuilder = null;
        if (!HttpMethod.permitsRequestBody(request.method()) || requestBody == null) {
            exchange.noRequestBody();
        } else {
            if (StringsKt.equals("100-continue", request.header(HttpHeaders.EXPECT), true)) {
                exchange.flushRequest();
                responseBuilder = exchange.readResponseHeaders(true);
                exchange.responseHeadersStart();
                invokeStartEvent = false;
            }
            if (responseBuilder != null) {
                exchange.noRequestBody();
                if (!exchange.getConnection$okhttp().isMultiplexed()) {
                    exchange.noNewExchangesOnConnection();
                }
            } else if (requestBody.isDuplex()) {
                exchange.flushRequest();
                requestBody.writeTo(Okio.buffer(exchange.createRequestBody(request, true)));
            } else {
                BufferedSink bufferedRequestBody = Okio.buffer(exchange.createRequestBody(request, false));
                requestBody.writeTo(bufferedRequestBody);
                bufferedRequestBody.close();
            }
        }
        if (requestBody == null || !requestBody.isDuplex()) {
            exchange.finishRequest();
        }
        if (responseBuilder == null) {
            Response.Builder readResponseHeaders = exchange.readResponseHeaders(false);
            if (readResponseHeaders == null) {
                Intrinsics.throwNpe();
            }
            responseBuilder = readResponseHeaders;
            if (invokeStartEvent) {
                exchange.responseHeadersStart();
                invokeStartEvent = false;
            }
        }
        Response response2 = responseBuilder.request(request).handshake(exchange.getConnection$okhttp().handshake()).sentRequestAtMillis(sentRequestMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
        int code = response2.code();
        if (code == 100) {
            Response.Builder responseBuilder2 = exchange.readResponseHeaders(false);
            if (responseBuilder2 == null) {
                Intrinsics.throwNpe();
            }
            if (invokeStartEvent) {
                exchange.responseHeadersStart();
            }
            response2 = responseBuilder2.request(request).handshake(exchange.getConnection$okhttp().handshake()).sentRequestAtMillis(sentRequestMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
            code = response2.code();
        }
        exchange.responseHeadersEnd(response2);
        if (!this.forWebSocket || code != 101) {
            response = response2.newBuilder().body(exchange.openResponseBody(response2)).build();
        } else {
            response = response2.newBuilder().body(Util.EMPTY_RESPONSE).build();
        }
        if (!StringsKt.equals("close", response.request().header(HttpHeaders.CONNECTION), true)) {
            l = null;
        } else {
            l = null;
        }
        exchange.noNewExchangesOnConnection();
        if (code == 204 || code == 205) {
            ResponseBody body = response.body();
            if ((body != null ? body.contentLength() : -1) > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("HTTP ");
                sb.append(code);
                sb.append(" had non-zero Content-Length: ");
                ResponseBody body2 = response.body();
                if (body2 != null) {
                    l = Long.valueOf(body2.contentLength());
                }
                sb.append(l);
                throw new ProtocolException(sb.toString());
            }
        }
        return response;
    }
}
