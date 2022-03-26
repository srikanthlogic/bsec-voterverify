package okhttp3.internal.connection;

import androidx.core.app.NotificationCompat;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.common.net.HttpHeaders;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.IOException;
import java.lang.ref.Reference;
import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownServiceException;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.Address;
import okhttp3.Call;
import okhttp3.CertificatePinner;
import okhttp3.Connection;
import okhttp3.ConnectionSpec;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.Version;
import okhttp3.internal.concurrent.TaskRunner;
import okhttp3.internal.http.ExchangeCodec;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.http1.Http1ExchangeCodec;
import okhttp3.internal.http2.ConnectionShutdownException;
import okhttp3.internal.http2.ErrorCode;
import okhttp3.internal.http2.Http2Connection;
import okhttp3.internal.http2.Http2ExchangeCodec;
import okhttp3.internal.http2.Http2Stream;
import okhttp3.internal.http2.Settings;
import okhttp3.internal.http2.StreamResetException;
import okhttp3.internal.platform.Platform;
import okhttp3.internal.tls.CertificateChainCleaner;
import okhttp3.internal.tls.OkHostnameVerifier;
import okhttp3.internal.ws.RealWebSocket;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
/* compiled from: RealConnection.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000ì\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\u0018\u0000 w2\u00020\u00012\u00020\u0002:\u0001wB\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0006\u00106\u001a\u000207J>\u00108\u001a\u0002072\u0006\u00109\u001a\u00020\t2\u0006\u0010:\u001a\u00020\t2\u0006\u0010;\u001a\u00020\t2\u0006\u0010<\u001a\u00020\t2\u0006\u0010=\u001a\u00020\u001d2\u0006\u0010>\u001a\u00020?2\u0006\u0010@\u001a\u00020AJ%\u0010B\u001a\u0002072\u0006\u0010C\u001a\u00020D2\u0006\u0010E\u001a\u00020\u00062\u0006\u0010F\u001a\u00020GH\u0000¢\u0006\u0002\bHJ(\u0010I\u001a\u0002072\u0006\u00109\u001a\u00020\t2\u0006\u0010:\u001a\u00020\t2\u0006\u0010>\u001a\u00020?2\u0006\u0010@\u001a\u00020AH\u0002J\u0010\u0010J\u001a\u0002072\u0006\u0010K\u001a\u00020LH\u0002J0\u0010M\u001a\u0002072\u0006\u00109\u001a\u00020\t2\u0006\u0010:\u001a\u00020\t2\u0006\u0010;\u001a\u00020\t2\u0006\u0010>\u001a\u00020?2\u0006\u0010@\u001a\u00020AH\u0002J*\u0010N\u001a\u0004\u0018\u00010O2\u0006\u0010:\u001a\u00020\t2\u0006\u0010;\u001a\u00020\t2\u0006\u0010P\u001a\u00020O2\u0006\u0010Q\u001a\u00020RH\u0002J\b\u0010S\u001a\u00020OH\u0002J(\u0010T\u001a\u0002072\u0006\u0010K\u001a\u00020L2\u0006\u0010<\u001a\u00020\t2\u0006\u0010>\u001a\u00020?2\u0006\u0010@\u001a\u00020AH\u0002J\n\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0016J%\u0010U\u001a\u00020\u001d2\u0006\u0010V\u001a\u00020W2\u000e\u0010X\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010YH\u0000¢\u0006\u0002\bZJ\u000e\u0010[\u001a\u00020\u001d2\u0006\u0010\\\u001a\u00020\u001dJ\u001d\u0010]\u001a\u00020^2\u0006\u0010C\u001a\u00020D2\u0006\u0010_\u001a\u00020`H\u0000¢\u0006\u0002\baJ\u0015\u0010b\u001a\u00020c2\u0006\u0010d\u001a\u00020eH\u0000¢\u0006\u0002\bfJ\u0006\u0010\u001f\u001a\u000207J\u0006\u0010 \u001a\u000207J\u0018\u0010g\u001a\u0002072\u0006\u0010h\u001a\u00020\u00152\u0006\u0010i\u001a\u00020jH\u0016J\u0010\u0010k\u001a\u0002072\u0006\u0010l\u001a\u00020mH\u0016J\b\u0010$\u001a\u00020%H\u0016J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\u0016\u0010n\u001a\u00020\u001d2\f\u0010o\u001a\b\u0012\u0004\u0012\u00020\u00060YH\u0002J\b\u00100\u001a\u00020'H\u0016J\u0010\u0010p\u001a\u0002072\u0006\u0010<\u001a\u00020\tH\u0002J\u000e\u0010q\u001a\u00020\u001d2\u0006\u0010Q\u001a\u00020RJ\b\u0010r\u001a\u00020sH\u0016J\u001f\u0010t\u001a\u0002072\u0006\u0010>\u001a\u00020\r2\b\u0010u\u001a\u0004\u0018\u00010GH\u0000¢\u0006\u0002\bvR\u000e\u0010\b\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u001d\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0016\u001a\u00020\u0017X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u0011\u0010\u001c\u001a\u00020\u001d8F¢\u0006\u0006\u001a\u0004\b\u001c\u0010\u001eR\u000e\u0010\u001f\u001a\u00020\u001dX\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010 \u001a\u00020\u001dX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\u001e\"\u0004\b\"\u0010#R\u0010\u0010$\u001a\u0004\u0018\u00010%X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010&\u001a\u0004\u0018\u00010'X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010)\u001a\u00020\tX\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b*\u0010+\"\u0004\b,\u0010-R\u0010\u0010.\u001a\u0004\u0018\u00010/X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u00100\u001a\u0004\u0018\u00010'X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u00101\u001a\u0004\u0018\u000102X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u00103\u001a\u00020\tX\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b4\u0010+\"\u0004\b5\u0010-¨\u0006x"}, d2 = {"Lokhttp3/internal/connection/RealConnection;", "Lokhttp3/internal/http2/Http2Connection$Listener;", "Lokhttp3/Connection;", "connectionPool", "Lokhttp3/internal/connection/RealConnectionPool;", "route", "Lokhttp3/Route;", "(Lokhttp3/internal/connection/RealConnectionPool;Lokhttp3/Route;)V", "allocationLimit", "", "calls", "", "Ljava/lang/ref/Reference;", "Lokhttp3/internal/connection/RealCall;", "getCalls", "()Ljava/util/List;", "getConnectionPool", "()Lokhttp3/internal/connection/RealConnectionPool;", "handshake", "Lokhttp3/Handshake;", "http2Connection", "Lokhttp3/internal/http2/Http2Connection;", "idleAtNs", "", "getIdleAtNs$okhttp", "()J", "setIdleAtNs$okhttp", "(J)V", "isMultiplexed", "", "()Z", "noCoalescedConnections", "noNewExchanges", "getNoNewExchanges", "setNoNewExchanges", "(Z)V", "protocol", "Lokhttp3/Protocol;", "rawSocket", "Ljava/net/Socket;", "refusedStreamCount", "routeFailureCount", "getRouteFailureCount$okhttp", "()I", "setRouteFailureCount$okhttp", "(I)V", "sink", "Lokio/BufferedSink;", "socket", FirebaseAnalytics.Param.SOURCE, "Lokio/BufferedSource;", "successCount", "getSuccessCount$okhttp", "setSuccessCount$okhttp", "cancel", "", "connect", "connectTimeout", "readTimeout", "writeTimeout", "pingIntervalMillis", "connectionRetryEnabled", NotificationCompat.CATEGORY_CALL, "Lokhttp3/Call;", "eventListener", "Lokhttp3/EventListener;", "connectFailed", "client", "Lokhttp3/OkHttpClient;", "failedRoute", "failure", "Ljava/io/IOException;", "connectFailed$okhttp", "connectSocket", "connectTls", "connectionSpecSelector", "Lokhttp3/internal/connection/ConnectionSpecSelector;", "connectTunnel", "createTunnel", "Lokhttp3/Request;", "tunnelRequest", ImagesContract.URL, "Lokhttp3/HttpUrl;", "createTunnelRequest", "establishProtocol", "isEligible", "address", "Lokhttp3/Address;", "routes", "", "isEligible$okhttp", "isHealthy", "doExtensiveChecks", "newCodec", "Lokhttp3/internal/http/ExchangeCodec;", "chain", "Lokhttp3/internal/http/RealInterceptorChain;", "newCodec$okhttp", "newWebSocketStreams", "Lokhttp3/internal/ws/RealWebSocket$Streams;", "exchange", "Lokhttp3/internal/connection/Exchange;", "newWebSocketStreams$okhttp", "onSettings", "connection", "settings", "Lokhttp3/internal/http2/Settings;", "onStream", "stream", "Lokhttp3/internal/http2/Http2Stream;", "routeMatchesAny", "candidates", "startHttp2", "supportsUrl", "toString", "", "trackFailure", "e", "trackFailure$okhttp", "Companion", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class RealConnection extends Http2Connection.Listener implements Connection {
    public static final Companion Companion = new Companion(null);
    public static final long IDLE_CONNECTION_HEALTHY_NS;
    private static final int MAX_TUNNEL_ATTEMPTS;
    private static final String NPE_THROW_WITH_NULL;
    private final RealConnectionPool connectionPool;
    private Handshake handshake;
    private Http2Connection http2Connection;
    private boolean noCoalescedConnections;
    private boolean noNewExchanges;
    private Protocol protocol;
    private Socket rawSocket;
    private int refusedStreamCount;
    private final Route route;
    private int routeFailureCount;
    private BufferedSink sink;
    private Socket socket;
    private BufferedSource source;
    private int successCount;
    private int allocationLimit = 1;
    private final List<Reference<RealCall>> calls = new ArrayList();
    private long idleAtNs = Long.MAX_VALUE;

    @Metadata(bv = {1, 0, 3}, k = 3, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0 = new int[Proxy.Type.values().length];

        static {
            $EnumSwitchMapping$0[Proxy.Type.DIRECT.ordinal()] = 1;
            $EnumSwitchMapping$0[Proxy.Type.HTTP.ordinal()] = 2;
        }
    }

    public RealConnection(RealConnectionPool connectionPool, Route route) {
        Intrinsics.checkParameterIsNotNull(connectionPool, "connectionPool");
        Intrinsics.checkParameterIsNotNull(route, "route");
        this.connectionPool = connectionPool;
        this.route = route;
    }

    public final RealConnectionPool getConnectionPool() {
        return this.connectionPool;
    }

    public final boolean getNoNewExchanges() {
        return this.noNewExchanges;
    }

    public final void setNoNewExchanges(boolean z) {
        this.noNewExchanges = z;
    }

    public final int getRouteFailureCount$okhttp() {
        return this.routeFailureCount;
    }

    public final void setRouteFailureCount$okhttp(int i) {
        this.routeFailureCount = i;
    }

    public final int getSuccessCount$okhttp() {
        return this.successCount;
    }

    public final void setSuccessCount$okhttp(int i) {
        this.successCount = i;
    }

    public final List<Reference<RealCall>> getCalls() {
        return this.calls;
    }

    public final long getIdleAtNs$okhttp() {
        return this.idleAtNs;
    }

    public final void setIdleAtNs$okhttp(long j) {
        this.idleAtNs = j;
    }

    public final boolean isMultiplexed() {
        return this.http2Connection != null;
    }

    public final void noNewExchanges() {
        Object $this$assertThreadDoesntHoldLock$iv = this.connectionPool;
        if (!Util.assertionsEnabled || !Thread.holdsLock($this$assertThreadDoesntHoldLock$iv)) {
            synchronized (this.connectionPool) {
                this.noNewExchanges = true;
                Unit unit = Unit.INSTANCE;
            }
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Thread ");
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
        sb.append(currentThread.getName());
        sb.append(" MUST NOT hold lock on ");
        sb.append($this$assertThreadDoesntHoldLock$iv);
        throw new AssertionError(sb.toString());
    }

    public final void noCoalescedConnections() {
        Object $this$assertThreadDoesntHoldLock$iv = this.connectionPool;
        if (!Util.assertionsEnabled || !Thread.holdsLock($this$assertThreadDoesntHoldLock$iv)) {
            synchronized (this.connectionPool) {
                this.noCoalescedConnections = true;
                Unit unit = Unit.INSTANCE;
            }
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Thread ");
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
        sb.append(currentThread.getName());
        sb.append(" MUST NOT hold lock on ");
        sb.append($this$assertThreadDoesntHoldLock$iv);
        throw new AssertionError(sb.toString());
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x00b2 A[Catch: IOException -> 0x011a, TRY_LEAVE, TryCatch #0 {IOException -> 0x011a, blocks: (B:21:0x00aa, B:23:0x00b2), top: B:63:0x00aa }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00d1  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00f8  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0125  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x012c  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x016b  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0172  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0181 A[EDGE_INSN: B:71:0x0181->B:57:0x0181 ?: BREAK  ] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final void connect(int connectTimeout, int readTimeout, int writeTimeout, int pingIntervalMillis, boolean connectionRetryEnabled, Call call, EventListener eventListener) {
        IOException e;
        Socket socket;
        Socket socket2;
        Intrinsics.checkParameterIsNotNull(call, NotificationCompat.CATEGORY_CALL);
        Intrinsics.checkParameterIsNotNull(eventListener, "eventListener");
        if (this.protocol == null) {
            RouteException routeException = null;
            List connectionSpecs = this.route.address().connectionSpecs();
            ConnectionSpecSelector connectionSpecSelector = new ConnectionSpecSelector(connectionSpecs);
            if (this.route.address().sslSocketFactory() == null) {
                if (connectionSpecs.contains(ConnectionSpec.CLEARTEXT)) {
                    String host = this.route.address().url().host();
                    if (!Platform.Companion.get().isCleartextTrafficPermitted(host)) {
                        throw new RouteException(new UnknownServiceException("CLEARTEXT communication to " + host + " not permitted by network security policy"));
                    }
                } else {
                    throw new RouteException(new UnknownServiceException("CLEARTEXT communication not enabled for client"));
                }
            } else if (this.route.address().protocols().contains(Protocol.H2_PRIOR_KNOWLEDGE)) {
                throw new RouteException(new UnknownServiceException("H2_PRIOR_KNOWLEDGE cannot be used with HTTPS"));
            }
            RouteException routeException2 = routeException;
            do {
                try {
                } catch (IOException e2) {
                    e = e2;
                }
                try {
                    if (this.route.requiresTunnel()) {
                        connectTunnel(connectTimeout, readTimeout, writeTimeout, call, eventListener);
                        if (this.rawSocket == null) {
                            if (this.route.requiresTunnel() || this.rawSocket != null) {
                                this.idleAtNs = System.nanoTime();
                                return;
                            }
                            throw new RouteException(new ProtocolException("Too many tunnel connections attempted: 21"));
                        }
                    } else {
                        try {
                            connectSocket(connectTimeout, readTimeout, call, eventListener);
                        } catch (IOException e3) {
                            e = e3;
                            socket = this.socket;
                            if (socket != null) {
                                Util.closeQuietly(socket);
                            }
                            socket2 = this.rawSocket;
                            if (socket2 != null) {
                                Util.closeQuietly(socket2);
                            }
                            Socket socket3 = null;
                            this.socket = socket3;
                            this.rawSocket = socket3;
                            this.source = null;
                            this.sink = null;
                            this.handshake = null;
                            this.protocol = null;
                            this.http2Connection = null;
                            this.allocationLimit = 1;
                            eventListener.connectFailed(call, this.route.socketAddress(), this.route.proxy(), null, e);
                            if (routeException2 != null) {
                                routeException2 = new RouteException(e);
                            } else {
                                routeException2.addConnectException(e);
                            }
                            if (!connectionRetryEnabled || connectionSpecSelector.connectionFailed(e)) {
                                throw routeException2;
                            }
                            do {
                                if (this.route.requiresTunnel()) {
                                }
                                establishProtocol(connectionSpecSelector, pingIntervalMillis, call, eventListener);
                                eventListener.connectEnd(call, this.route.socketAddress(), this.route.proxy(), this.protocol);
                                if (this.route.requiresTunnel()) {
                                }
                                this.idleAtNs = System.nanoTime();
                                return;
                            } while (connectionSpecSelector.connectionFailed(e));
                            throw routeException2;
                        }
                    }
                    establishProtocol(connectionSpecSelector, pingIntervalMillis, call, eventListener);
                    eventListener.connectEnd(call, this.route.socketAddress(), this.route.proxy(), this.protocol);
                    if (this.route.requiresTunnel()) {
                    }
                    this.idleAtNs = System.nanoTime();
                    return;
                } catch (IOException e4) {
                    e = e4;
                    socket = this.socket;
                    if (socket != null) {
                    }
                    socket2 = this.rawSocket;
                    if (socket2 != null) {
                    }
                    Socket socket32 = null;
                    this.socket = socket32;
                    this.rawSocket = socket32;
                    this.source = null;
                    this.sink = null;
                    this.handshake = null;
                    this.protocol = null;
                    this.http2Connection = null;
                    this.allocationLimit = 1;
                    eventListener.connectFailed(call, this.route.socketAddress(), this.route.proxy(), null, e);
                    if (routeException2 != null) {
                    }
                    if (!connectionRetryEnabled || connectionSpecSelector.connectionFailed(e)) {
                    }
                    do {
                        if (this.route.requiresTunnel()) {
                        }
                        establishProtocol(connectionSpecSelector, pingIntervalMillis, call, eventListener);
                        eventListener.connectEnd(call, this.route.socketAddress(), this.route.proxy(), this.protocol);
                        if (this.route.requiresTunnel()) {
                        }
                        this.idleAtNs = System.nanoTime();
                        return;
                    } while (connectionSpecSelector.connectionFailed(e));
                    throw routeException2;
                }
            } while (connectionSpecSelector.connectionFailed(e));
            throw routeException2;
        }
        throw new IllegalStateException("already connected".toString());
    }

    private final void connectTunnel(int connectTimeout, int readTimeout, int writeTimeout, Call call, EventListener eventListener) throws IOException {
        Request tunnelRequest = createTunnelRequest();
        HttpUrl url = tunnelRequest.url();
        for (int i = 0; i < 21; i++) {
            connectSocket(connectTimeout, readTimeout, call, eventListener);
            Request createTunnel = createTunnel(readTimeout, writeTimeout, tunnelRequest, url);
            if (createTunnel != null) {
                tunnelRequest = createTunnel;
                Socket socket = this.rawSocket;
                if (socket != null) {
                    Util.closeQuietly(socket);
                }
                this.rawSocket = null;
                this.sink = null;
                this.source = null;
                eventListener.connectEnd(call, this.route.socketAddress(), this.route.proxy(), null);
            } else {
                return;
            }
        }
    }

    private final void connectSocket(int connectTimeout, int readTimeout, Call call, EventListener eventListener) throws IOException {
        Socket rawSocket;
        int i;
        Proxy proxy = this.route.proxy();
        Address address = this.route.address();
        Proxy.Type type = proxy.type();
        if (type != null && ((i = WhenMappings.$EnumSwitchMapping$0[type.ordinal()]) == 1 || i == 2)) {
            rawSocket = address.socketFactory().createSocket();
            if (rawSocket == null) {
                Intrinsics.throwNpe();
            }
        } else {
            rawSocket = new Socket(proxy);
        }
        this.rawSocket = rawSocket;
        eventListener.connectStart(call, this.route.socketAddress(), proxy);
        rawSocket.setSoTimeout(readTimeout);
        try {
            Platform.Companion.get().connectSocket(rawSocket, this.route.socketAddress(), connectTimeout);
            try {
                this.source = Okio.buffer(Okio.source(rawSocket));
                this.sink = Okio.buffer(Okio.sink(rawSocket));
            } catch (NullPointerException npe) {
                if (Intrinsics.areEqual(npe.getMessage(), NPE_THROW_WITH_NULL)) {
                    throw new IOException(npe);
                }
            }
        } catch (ConnectException e) {
            ConnectException $this$apply = new ConnectException("Failed to connect to " + this.route.socketAddress());
            $this$apply.initCause(e);
            throw $this$apply;
        }
    }

    private final void establishProtocol(ConnectionSpecSelector connectionSpecSelector, int pingIntervalMillis, Call call, EventListener eventListener) throws IOException {
        if (this.route.address().sslSocketFactory() != null) {
            eventListener.secureConnectStart(call);
            connectTls(connectionSpecSelector);
            eventListener.secureConnectEnd(call, this.handshake);
            if (this.protocol == Protocol.HTTP_2) {
                startHttp2(pingIntervalMillis);
            }
        } else if (this.route.address().protocols().contains(Protocol.H2_PRIOR_KNOWLEDGE)) {
            this.socket = this.rawSocket;
            this.protocol = Protocol.H2_PRIOR_KNOWLEDGE;
            startHttp2(pingIntervalMillis);
        } else {
            this.socket = this.rawSocket;
            this.protocol = Protocol.HTTP_1_1;
        }
    }

    private final void startHttp2(int pingIntervalMillis) throws IOException {
        Socket socket = this.socket;
        if (socket == null) {
            Intrinsics.throwNpe();
        }
        BufferedSource source = this.source;
        if (source == null) {
            Intrinsics.throwNpe();
        }
        BufferedSink sink = this.sink;
        if (sink == null) {
            Intrinsics.throwNpe();
        }
        socket.setSoTimeout(0);
        Http2Connection http2Connection = new Http2Connection.Builder(true, TaskRunner.INSTANCE).socket(socket, this.route.address().url().host(), source, sink).listener(this).pingIntervalMillis(pingIntervalMillis).build();
        this.http2Connection = http2Connection;
        this.allocationLimit = Http2Connection.Companion.getDEFAULT_SETTINGS().getMaxConcurrentStreams();
        Http2Connection.start$default(http2Connection, false, 1, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x01b8  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x01c4  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private final void connectTls(ConnectionSpecSelector connectionSpecSelector) throws IOException {
        Throwable th;
        Address address = this.route.address();
        SSLSocketFactory sslSocketFactory = address.sslSocketFactory();
        SSLSocket sslSocket = null;
        if (sslSocketFactory == null) {
            try {
                Intrinsics.throwNpe();
            } catch (Throwable th2) {
                th = th2;
                if (sslSocket != null) {
                }
                if (sslSocket != null) {
                }
                throw th;
            }
        }
        Socket createSocket = sslSocketFactory.createSocket(this.rawSocket, address.url().host(), address.url().port(), true);
        try {
            if (createSocket != null) {
                SSLSocket sslSocket2 = (SSLSocket) createSocket;
                ConnectionSpec connectionSpec = connectionSpecSelector.configureSecureSocket(sslSocket2);
                if (connectionSpec.supportsTlsExtensions()) {
                    Platform.Companion.get().configureTlsExtensions(sslSocket2, address.url().host(), address.protocols());
                }
                sslSocket2.startHandshake();
                SSLSession sslSocketSession = sslSocket2.getSession();
                Handshake.Companion companion = Handshake.Companion;
                Intrinsics.checkExpressionValueIsNotNull(sslSocketSession, "sslSocketSession");
                Handshake unverifiedHandshake = companion.get(sslSocketSession);
                HostnameVerifier hostnameVerifier = address.hostnameVerifier();
                if (hostnameVerifier == null) {
                    Intrinsics.throwNpe();
                }
                if (!hostnameVerifier.verify(address.url().host(), sslSocketSession)) {
                    List peerCertificates = unverifiedHandshake.peerCertificates();
                    if (!peerCertificates.isEmpty()) {
                        Certificate certificate = peerCertificates.get(0);
                        if (certificate == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.security.cert.X509Certificate");
                        }
                        X509Certificate cert = (X509Certificate) certificate;
                        StringBuilder sb = new StringBuilder();
                        sb.append("\n              |Hostname ");
                        sb.append(address.url().host());
                        sb.append(" not verified:\n              |    certificate: ");
                        sb.append(CertificatePinner.Companion.pin(cert));
                        sb.append("\n              |    DN: ");
                        Principal subjectDN = cert.getSubjectDN();
                        Intrinsics.checkExpressionValueIsNotNull(subjectDN, "cert.subjectDN");
                        sb.append(subjectDN.getName());
                        sb.append("\n              |    subjectAltNames: ");
                        sb.append(OkHostnameVerifier.INSTANCE.allSubjectAltNames(cert));
                        sb.append("\n              ");
                        throw new SSLPeerUnverifiedException(StringsKt.trimMargin$default(sb.toString(), null, 1, null));
                    }
                    throw new SSLPeerUnverifiedException("Hostname " + address.url().host() + " not verified (no certificates)");
                }
                String maybeProtocol = null;
                CertificatePinner certificatePinner = address.certificatePinner();
                if (certificatePinner == null) {
                    Intrinsics.throwNpe();
                }
                this.handshake = new Handshake(unverifiedHandshake.tlsVersion(), unverifiedHandshake.cipherSuite(), unverifiedHandshake.localCertificates(), new Function0<List<? extends Certificate>>(unverifiedHandshake, address) { // from class: okhttp3.internal.connection.RealConnection$connectTls$1
                    final /* synthetic */ Address $address;
                    final /* synthetic */ Handshake $unverifiedHandshake;

                    /* JADX INFO: Access modifiers changed from: package-private */
                    {
                        this.$unverifiedHandshake = r2;
                        this.$address = r3;
                    }

                    @Override // kotlin.jvm.functions.Function0
                    public final List<? extends Certificate> invoke() {
                        CertificateChainCleaner certificateChainCleaner$okhttp = CertificatePinner.this.getCertificateChainCleaner$okhttp();
                        if (certificateChainCleaner$okhttp == null) {
                            Intrinsics.throwNpe();
                        }
                        return certificateChainCleaner$okhttp.clean(this.$unverifiedHandshake.peerCertificates(), this.$address.url().host());
                    }
                });
                certificatePinner.check$okhttp(address.url().host(), new Function0<List<? extends X509Certificate>>() { // from class: okhttp3.internal.connection.RealConnection$connectTls$2
                    @Override // kotlin.jvm.functions.Function0
                    public final List<? extends X509Certificate> invoke() {
                        Handshake handshake = RealConnection.this.handshake;
                        if (handshake == null) {
                            Intrinsics.throwNpe();
                        }
                        Iterable<Certificate> $this$map$iv = handshake.peerCertificates();
                        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                        for (Certificate it : $this$map$iv) {
                            if (it != null) {
                                destination$iv$iv.add((X509Certificate) it);
                            } else {
                                throw new TypeCastException("null cannot be cast to non-null type java.security.cert.X509Certificate");
                            }
                        }
                        return (List) destination$iv$iv;
                    }
                });
                if (connectionSpec.supportsTlsExtensions()) {
                    maybeProtocol = Platform.Companion.get().getSelectedProtocol(sslSocket2);
                }
                this.socket = sslSocket2;
                this.source = Okio.buffer(Okio.source(sslSocket2));
                this.sink = Okio.buffer(Okio.sink(sslSocket2));
                this.protocol = maybeProtocol != null ? Protocol.Companion.get(maybeProtocol) : Protocol.HTTP_1_1;
                if (sslSocket2 != null) {
                    Platform.Companion.get().afterHandshake(sslSocket2);
                    return;
                }
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type javax.net.ssl.SSLSocket");
        } catch (Throwable th3) {
            th = th3;
            if (sslSocket != null) {
                Platform.Companion.get().afterHandshake(sslSocket);
            }
            if (sslSocket != null) {
                Util.closeQuietly((Socket) sslSocket);
            }
            throw th;
        }
    }

    private final Request createTunnel(int readTimeout, int writeTimeout, Request tunnelRequest, HttpUrl url) throws IOException {
        Response response;
        Request nextRequest = tunnelRequest;
        String requestLine = "CONNECT " + Util.toHostHeader(url, true) + " HTTP/1.1";
        do {
            BufferedSource source = this.source;
            if (source == null) {
                Intrinsics.throwNpe();
            }
            BufferedSink sink = this.sink;
            if (sink == null) {
                Intrinsics.throwNpe();
            }
            Http1ExchangeCodec tunnelCodec = new Http1ExchangeCodec(null, this, source, sink);
            source.timeout().timeout((long) readTimeout, TimeUnit.MILLISECONDS);
            sink.timeout().timeout((long) writeTimeout, TimeUnit.MILLISECONDS);
            tunnelCodec.writeRequest(nextRequest.headers(), requestLine);
            tunnelCodec.finishRequest();
            Response.Builder readResponseHeaders = tunnelCodec.readResponseHeaders(false);
            if (readResponseHeaders == null) {
                Intrinsics.throwNpe();
            }
            response = readResponseHeaders.request(nextRequest).build();
            tunnelCodec.skipConnectBody(response);
            int code = response.code();
            if (code != 200) {
                if (code == 407) {
                    Request authenticate = this.route.address().proxyAuthenticator().authenticate(this.route, response);
                    if (authenticate != null) {
                        nextRequest = authenticate;
                    } else {
                        throw new IOException("Failed to authenticate with proxy");
                    }
                } else {
                    throw new IOException("Unexpected response code for CONNECT: " + response.code());
                }
            } else if (source.getBuffer().exhausted() && sink.getBuffer().exhausted()) {
                return null;
            } else {
                throw new IOException("TLS tunnel buffered too many bytes!");
            }
        } while (!StringsKt.equals("close", Response.header$default(response, HttpHeaders.CONNECTION, null, 2, null), true));
        return nextRequest;
    }

    private final Request createTunnelRequest() throws IOException {
        Request proxyConnectRequest = new Request.Builder().url(this.route.address().url()).method("CONNECT", null).header(HttpHeaders.HOST, Util.toHostHeader(this.route.address().url(), true)).header("Proxy-Connection", "Keep-Alive").header(HttpHeaders.USER_AGENT, Version.userAgent).build();
        Request authenticatedRequest = this.route.address().proxyAuthenticator().authenticate(this.route, new Response.Builder().request(proxyConnectRequest).protocol(Protocol.HTTP_1_1).code(407).message("Preemptive Authenticate").body(Util.EMPTY_RESPONSE).sentRequestAtMillis(-1).receivedResponseAtMillis(-1).header(HttpHeaders.PROXY_AUTHENTICATE, "OkHttp-Preemptive").build());
        return authenticatedRequest != null ? authenticatedRequest : proxyConnectRequest;
    }

    public final boolean isEligible$okhttp(Address address, List<Route> list) {
        Intrinsics.checkParameterIsNotNull(address, "address");
        if (this.calls.size() >= this.allocationLimit || this.noNewExchanges || !this.route.address().equalsNonHost$okhttp(address)) {
            return false;
        }
        if (Intrinsics.areEqual(address.url().host(), route().address().url().host())) {
            return true;
        }
        if (this.http2Connection == null || list == null || !routeMatchesAny(list) || address.hostnameVerifier() != OkHostnameVerifier.INSTANCE || !supportsUrl(address.url())) {
            return false;
        }
        try {
            CertificatePinner certificatePinner = address.certificatePinner();
            if (certificatePinner == null) {
                Intrinsics.throwNpe();
            }
            String host = address.url().host();
            Handshake handshake = handshake();
            if (handshake == null) {
                Intrinsics.throwNpe();
            }
            certificatePinner.check(host, handshake.peerCertificates());
            return true;
        } catch (SSLPeerUnverifiedException e) {
            return false;
        }
    }

    private final boolean routeMatchesAny(List<Route> list) {
        Route it;
        List<Route> $this$any$iv = list;
        if (($this$any$iv instanceof Collection) && $this$any$iv.isEmpty()) {
            return false;
        }
        for (Route it2 : $this$any$iv) {
            if (it2.proxy().type() == Proxy.Type.DIRECT && this.route.proxy().type() == Proxy.Type.DIRECT && Intrinsics.areEqual(this.route.socketAddress(), it2.socketAddress())) {
                it = 1;
                continue;
            } else {
                it = null;
                continue;
            }
            if (it != null) {
                return true;
            }
        }
        return false;
    }

    public final boolean supportsUrl(HttpUrl url) {
        Intrinsics.checkParameterIsNotNull(url, ImagesContract.URL);
        HttpUrl routeUrl = this.route.address().url();
        if (url.port() != routeUrl.port()) {
            return false;
        }
        if (Intrinsics.areEqual(url.host(), routeUrl.host())) {
            return true;
        }
        if (!this.noCoalescedConnections && this.handshake != null) {
            OkHostnameVerifier okHostnameVerifier = OkHostnameVerifier.INSTANCE;
            String host = url.host();
            Handshake handshake = this.handshake;
            if (handshake == null) {
                Intrinsics.throwNpe();
            }
            Certificate certificate = handshake.peerCertificates().get(0);
            if (certificate == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.security.cert.X509Certificate");
            } else if (okHostnameVerifier.verify(host, (X509Certificate) certificate)) {
                return true;
            }
        }
        return false;
    }

    public final ExchangeCodec newCodec$okhttp(OkHttpClient client, RealInterceptorChain chain) throws SocketException {
        Intrinsics.checkParameterIsNotNull(client, "client");
        Intrinsics.checkParameterIsNotNull(chain, "chain");
        Socket socket = this.socket;
        if (socket == null) {
            Intrinsics.throwNpe();
        }
        BufferedSource source = this.source;
        if (source == null) {
            Intrinsics.throwNpe();
        }
        BufferedSink sink = this.sink;
        if (sink == null) {
            Intrinsics.throwNpe();
        }
        Http2Connection http2Connection = this.http2Connection;
        if (http2Connection != null) {
            return new Http2ExchangeCodec(client, this, chain, http2Connection);
        }
        socket.setSoTimeout(chain.readTimeoutMillis());
        source.timeout().timeout((long) chain.getReadTimeoutMillis$okhttp(), TimeUnit.MILLISECONDS);
        sink.timeout().timeout((long) chain.getWriteTimeoutMillis$okhttp(), TimeUnit.MILLISECONDS);
        return new Http1ExchangeCodec(client, this, source, sink);
    }

    public final RealWebSocket.Streams newWebSocketStreams$okhttp(Exchange exchange) throws SocketException {
        Intrinsics.checkParameterIsNotNull(exchange, "exchange");
        Socket socket = this.socket;
        if (socket == null) {
            Intrinsics.throwNpe();
        }
        BufferedSource source = this.source;
        if (source == null) {
            Intrinsics.throwNpe();
        }
        BufferedSink sink = this.sink;
        if (sink == null) {
            Intrinsics.throwNpe();
        }
        socket.setSoTimeout(0);
        noNewExchanges();
        return new RealWebSocket.Streams(source, sink, true, source, sink) { // from class: okhttp3.internal.connection.RealConnection$newWebSocketStreams$1
            final /* synthetic */ BufferedSink $sink;
            final /* synthetic */ BufferedSource $source;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$source = $captured_local_variable$1;
                this.$sink = $captured_local_variable$2;
            }

            @Override // java.io.Closeable, java.lang.AutoCloseable
            public void close() {
                Exchange.this.bodyComplete(-1, true, true, null);
            }
        };
    }

    @Override // okhttp3.Connection
    public Route route() {
        return this.route;
    }

    public final void cancel() {
        Socket socket = this.rawSocket;
        if (socket != null) {
            Util.closeQuietly(socket);
        }
    }

    @Override // okhttp3.Connection
    public Socket socket() {
        Socket socket = this.socket;
        if (socket == null) {
            Intrinsics.throwNpe();
        }
        return socket;
    }

    public final boolean isHealthy(boolean doExtensiveChecks) {
        long nowNs = System.nanoTime();
        Socket socket = this.socket;
        if (socket == null) {
            Intrinsics.throwNpe();
        }
        BufferedSource source = this.source;
        if (source == null) {
            Intrinsics.throwNpe();
        }
        if (socket.isClosed() || socket.isInputShutdown() || socket.isOutputShutdown()) {
            return false;
        }
        Http2Connection http2Connection = this.http2Connection;
        if (http2Connection != null) {
            return http2Connection.isHealthy(nowNs);
        }
        if (nowNs - this.idleAtNs < IDLE_CONNECTION_HEALTHY_NS || !doExtensiveChecks) {
            return true;
        }
        return Util.isHealthy(socket, source);
    }

    @Override // okhttp3.internal.http2.Http2Connection.Listener
    public void onStream(Http2Stream stream) throws IOException {
        Intrinsics.checkParameterIsNotNull(stream, "stream");
        stream.close(ErrorCode.REFUSED_STREAM, null);
    }

    @Override // okhttp3.internal.http2.Http2Connection.Listener
    public void onSettings(Http2Connection connection, Settings settings) {
        Intrinsics.checkParameterIsNotNull(connection, "connection");
        Intrinsics.checkParameterIsNotNull(settings, "settings");
        synchronized (this.connectionPool) {
            this.allocationLimit = settings.getMaxConcurrentStreams();
            Unit unit = Unit.INSTANCE;
        }
    }

    @Override // okhttp3.Connection
    public Handshake handshake() {
        return this.handshake;
    }

    public final void connectFailed$okhttp(OkHttpClient client, Route failedRoute, IOException failure) {
        Intrinsics.checkParameterIsNotNull(client, "client");
        Intrinsics.checkParameterIsNotNull(failedRoute, "failedRoute");
        Intrinsics.checkParameterIsNotNull(failure, "failure");
        if (failedRoute.proxy().type() != Proxy.Type.DIRECT) {
            Address address = failedRoute.address();
            address.proxySelector().connectFailed(address.url().uri(), failedRoute.proxy().address(), failure);
        }
        client.getRouteDatabase().failed(failedRoute);
    }

    public final void trackFailure$okhttp(RealCall call, IOException e) {
        Intrinsics.checkParameterIsNotNull(call, NotificationCompat.CATEGORY_CALL);
        Object $this$assertThreadDoesntHoldLock$iv = this.connectionPool;
        if (!Util.assertionsEnabled || !Thread.holdsLock($this$assertThreadDoesntHoldLock$iv)) {
            synchronized (this.connectionPool) {
                if (e instanceof StreamResetException) {
                    if (((StreamResetException) e).errorCode == ErrorCode.REFUSED_STREAM) {
                        this.refusedStreamCount++;
                        if (this.refusedStreamCount > 1) {
                            this.noNewExchanges = true;
                            this.routeFailureCount++;
                        }
                    } else if (((StreamResetException) e).errorCode != ErrorCode.CANCEL || !call.isCanceled()) {
                        this.noNewExchanges = true;
                        this.routeFailureCount++;
                    }
                } else if (!isMultiplexed() || (e instanceof ConnectionShutdownException)) {
                    this.noNewExchanges = true;
                    if (this.successCount == 0) {
                        if (e != null) {
                            connectFailed$okhttp(call.getClient(), this.route, e);
                        }
                        this.routeFailureCount++;
                    }
                }
                Unit unit = Unit.INSTANCE;
            }
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Thread ");
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
        sb.append(currentThread.getName());
        sb.append(" MUST NOT hold lock on ");
        sb.append($this$assertThreadDoesntHoldLock$iv);
        throw new AssertionError(sb.toString());
    }

    @Override // okhttp3.Connection
    public Protocol protocol() {
        Protocol protocol = this.protocol;
        if (protocol == null) {
            Intrinsics.throwNpe();
        }
        return protocol;
    }

    public String toString() {
        Object obj;
        StringBuilder sb = new StringBuilder();
        sb.append("Connection{");
        sb.append(this.route.address().url().host());
        sb.append(':');
        sb.append(this.route.address().url().port());
        sb.append(',');
        sb.append(" proxy=");
        sb.append(this.route.proxy());
        sb.append(" hostAddress=");
        sb.append(this.route.socketAddress());
        sb.append(" cipherSuite=");
        Handshake handshake = this.handshake;
        if (handshake == null || (obj = handshake.cipherSuite()) == null) {
            obj = "none";
        }
        sb.append(obj);
        sb.append(" protocol=");
        sb.append(this.protocol);
        sb.append('}');
        return sb.toString();
    }

    /* compiled from: RealConnection.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J&\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0080T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lokhttp3/internal/connection/RealConnection$Companion;", "", "()V", "IDLE_CONNECTION_HEALTHY_NS", "", "MAX_TUNNEL_ATTEMPTS", "", "NPE_THROW_WITH_NULL", "", "newTestConnection", "Lokhttp3/internal/connection/RealConnection;", "connectionPool", "Lokhttp3/internal/connection/RealConnectionPool;", "route", "Lokhttp3/Route;", "socket", "Ljava/net/Socket;", "idleAtNanos", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        public final RealConnection newTestConnection(RealConnectionPool connectionPool, Route route, Socket socket, long idleAtNanos) {
            Intrinsics.checkParameterIsNotNull(connectionPool, "connectionPool");
            Intrinsics.checkParameterIsNotNull(route, "route");
            Intrinsics.checkParameterIsNotNull(socket, "socket");
            RealConnection result = new RealConnection(connectionPool, route);
            result.socket = socket;
            result.setIdleAtNs$okhttp(idleAtNanos);
            return result;
        }
    }
}
