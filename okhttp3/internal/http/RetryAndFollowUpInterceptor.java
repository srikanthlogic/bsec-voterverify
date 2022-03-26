package okhttp3.internal.http;

import androidx.core.app.NotificationCompat;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.google.common.net.HttpHeaders;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.sec.biometric.license.SecBiometricLicenseManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.connection.Exchange;
import okhttp3.internal.connection.RealCall;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.RouteException;
import okhttp3.internal.http2.ConnectionShutdownException;
/* compiled from: RetryAndFollowUpInterceptor.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001a\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002J\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0002J\u0010\u0010\u000e\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0012H\u0002J(\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u0015\u001a\u00020\u0012H\u0002J\u0018\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u0006H\u0002J\u0018\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u001d\u001a\u00020\u001cH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Lokhttp3/internal/http/RetryAndFollowUpInterceptor;", "Lokhttp3/Interceptor;", "client", "Lokhttp3/OkHttpClient;", "(Lokhttp3/OkHttpClient;)V", "buildRedirectRequest", "Lokhttp3/Request;", "userResponse", "Lokhttp3/Response;", FirebaseAnalytics.Param.METHOD, "", "followUpRequest", "exchange", "Lokhttp3/internal/connection/Exchange;", "intercept", "chain", "Lokhttp3/Interceptor$Chain;", "isRecoverable", "", "e", "Ljava/io/IOException;", "requestSendStarted", "recover", NotificationCompat.CATEGORY_CALL, "Lokhttp3/internal/connection/RealCall;", "userRequest", "requestIsOneShot", "retryAfter", "", "defaultDelay", "Companion", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class RetryAndFollowUpInterceptor implements Interceptor {
    public static final Companion Companion = new Companion(null);
    private static final int MAX_FOLLOW_UPS = 20;
    private final OkHttpClient client;

    public RetryAndFollowUpInterceptor(OkHttpClient client) {
        Intrinsics.checkParameterIsNotNull(client, "client");
        this.client = client;
    }

    @Override // okhttp3.Interceptor
    public Response intercept(Interceptor.Chain chain) throws IOException {
        boolean z;
        Intrinsics.checkParameterIsNotNull(chain, "chain");
        RealInterceptorChain realChain = (RealInterceptorChain) chain;
        Request request = ((RealInterceptorChain) chain).getRequest$okhttp();
        RealCall call = realChain.getCall$okhttp();
        int followUpCount = 0;
        Response priorResponse = null;
        boolean newExchangeFinder = true;
        while (true) {
            call.enterNetworkInterceptorExchange(request, newExchangeFinder);
            boolean closeActiveExchange = true;
            try {
            } catch (IOException e) {
                if (!(e instanceof ConnectionShutdownException)) {
                    z = true;
                }
                if (recover(e, call, request, z)) {
                    newExchangeFinder = false;
                } else {
                    throw e;
                }
            } catch (RouteException e2) {
                if (recover(e2.getLastConnectException(), call, request, false)) {
                    newExchangeFinder = false;
                } else {
                    throw e2.getFirstConnectException();
                }
            } finally {
                call.exitNetworkInterceptorExchange$okhttp(closeActiveExchange);
            }
            if (!call.isCanceled()) {
                z = false;
                Response response = realChain.proceed(request);
                newExchangeFinder = true;
                if (priorResponse != null) {
                    response = response.newBuilder().priorResponse(priorResponse.newBuilder().body(null).build()).build();
                }
                Exchange exchange = call.getInterceptorScopedExchange$okhttp();
                Request followUp = followUpRequest(response, exchange);
                if (followUp == null) {
                    if (exchange != null && exchange.isDuplex$okhttp()) {
                        call.timeoutEarlyExit();
                    }
                    closeActiveExchange = false;
                    return response;
                }
                RequestBody followUpBody = followUp.body();
                if (followUpBody == null || !followUpBody.isOneShot()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        Util.closeQuietly(body);
                    }
                    followUpCount++;
                    if (followUpCount <= 20) {
                        request = followUp;
                        priorResponse = response;
                        call.exitNetworkInterceptorExchange$okhttp(closeActiveExchange);
                    } else {
                        throw new ProtocolException("Too many follow-up requests: " + followUpCount);
                    }
                } else {
                    closeActiveExchange = false;
                    return response;
                }
            } else {
                throw new IOException("Canceled");
            }
        }
    }

    private final boolean recover(IOException e, RealCall call, Request userRequest, boolean requestSendStarted) {
        if (!this.client.retryOnConnectionFailure()) {
            return false;
        }
        if ((!requestSendStarted || !requestIsOneShot(e, userRequest)) && isRecoverable(e, requestSendStarted) && call.retryAfterFailure()) {
            return true;
        }
        return false;
    }

    private final boolean requestIsOneShot(IOException e, Request userRequest) {
        RequestBody requestBody = userRequest.body();
        return (requestBody != null && requestBody.isOneShot()) || (e instanceof FileNotFoundException);
    }

    private final boolean isRecoverable(IOException e, boolean requestSendStarted) {
        if (e instanceof ProtocolException) {
            return false;
        }
        if (e instanceof InterruptedIOException) {
            if (!(e instanceof SocketTimeoutException) || requestSendStarted) {
                return false;
            }
            return true;
        } else if ((!(e instanceof SSLHandshakeException) || !(e.getCause() instanceof CertificateException)) && !(e instanceof SSLPeerUnverifiedException)) {
            return true;
        } else {
            return false;
        }
    }

    private final Request followUpRequest(Response userResponse, Exchange exchange) throws IOException {
        RealConnection connection$okhttp;
        Route route = (exchange == null || (connection$okhttp = exchange.getConnection$okhttp()) == null) ? null : connection$okhttp.route();
        int responseCode = userResponse.code();
        String method = userResponse.request().method();
        if (responseCode == 307 || responseCode == 308) {
            if (!(!Intrinsics.areEqual(method, "GET")) || !(!Intrinsics.areEqual(method, "HEAD"))) {
                return buildRedirectRequest(userResponse, method);
            }
            return null;
        } else if (responseCode == 401) {
            return this.client.authenticator().authenticate(route, userResponse);
        } else {
            if (responseCode == 421) {
                RequestBody requestBody = userResponse.request().body();
                if ((requestBody != null && requestBody.isOneShot()) || exchange == null || !exchange.isCoalescedConnection$okhttp()) {
                    return null;
                }
                exchange.getConnection$okhttp().noCoalescedConnections();
                return userResponse.request();
            } else if (responseCode == 503) {
                Response priorResponse = userResponse.priorResponse();
                if ((priorResponse == null || priorResponse.code() != 503) && retryAfter(userResponse, Integer.MAX_VALUE) == 0) {
                    return userResponse.request();
                }
                return null;
            } else if (responseCode == 407) {
                if (route == null) {
                    Intrinsics.throwNpe();
                }
                if (route.proxy().type() == Proxy.Type.HTTP) {
                    return this.client.proxyAuthenticator().authenticate(route, userResponse);
                }
                throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
            } else if (responseCode != 408) {
                switch (responseCode) {
                    case GenericDraweeHierarchyBuilder.DEFAULT_FADE_DURATION:
                    case SecBiometricLicenseManager.ERROR_INTERNAL:
                    case 302:
                    case 303:
                        return buildRedirectRequest(userResponse, method);
                    default:
                        return null;
                }
            } else if (!this.client.retryOnConnectionFailure()) {
                return null;
            } else {
                RequestBody requestBody2 = userResponse.request().body();
                if (requestBody2 != null && requestBody2.isOneShot()) {
                    return null;
                }
                Response priorResponse2 = userResponse.priorResponse();
                if ((priorResponse2 == null || priorResponse2.code() != 408) && retryAfter(userResponse, 0) <= 0) {
                    return userResponse.request();
                }
                return null;
            }
        }
    }

    private final Request buildRedirectRequest(Response userResponse, String method) {
        String location;
        HttpUrl url;
        RequestBody requestBody = null;
        if (!this.client.followRedirects() || (location = Response.header$default(userResponse, HttpHeaders.LOCATION, null, 2, null)) == null || (url = userResponse.request().url().resolve(location)) == null) {
            return null;
        }
        if (!Intrinsics.areEqual(url.scheme(), userResponse.request().url().scheme()) && !this.client.followSslRedirects()) {
            return null;
        }
        Request.Builder requestBuilder = userResponse.request().newBuilder();
        if (HttpMethod.permitsRequestBody(method)) {
            boolean maintainBody = HttpMethod.INSTANCE.redirectsWithBody(method);
            if (HttpMethod.INSTANCE.redirectsToGet(method)) {
                requestBuilder.method("GET", null);
            } else {
                if (maintainBody) {
                    requestBody = userResponse.request().body();
                }
                requestBuilder.method(method, requestBody);
            }
            if (!maintainBody) {
                requestBuilder.removeHeader(HttpHeaders.TRANSFER_ENCODING);
                requestBuilder.removeHeader(HttpHeaders.CONTENT_LENGTH);
                requestBuilder.removeHeader(HttpHeaders.CONTENT_TYPE);
            }
        }
        if (!Util.canReuseConnectionFor(userResponse.request().url(), url)) {
            requestBuilder.removeHeader(HttpHeaders.AUTHORIZATION);
        }
        return requestBuilder.url(url).build();
    }

    private final int retryAfter(Response userResponse, int defaultDelay) {
        String header = Response.header$default(userResponse, HttpHeaders.RETRY_AFTER, null, 2, null);
        if (header == null) {
            return defaultDelay;
        }
        if (!new Regex("\\d+").matches(header)) {
            return Integer.MAX_VALUE;
        }
        Integer valueOf = Integer.valueOf(header);
        Intrinsics.checkExpressionValueIsNotNull(valueOf, "Integer.valueOf(header)");
        return valueOf.intValue();
    }

    /* compiled from: RetryAndFollowUpInterceptor.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lokhttp3/internal/http/RetryAndFollowUpInterceptor$Companion;", "", "()V", "MAX_FOLLOW_UPS", "", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}
