package okhttp3.internal.connection;

import androidx.core.app.NotificationCompat;
import androidx.exifinterface.media.ExifInterface;
import com.google.android.gms.common.internal.ImagesContract;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import okhttp3.Address;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CertificatePinner;
import okhttp3.Connection;
import okhttp3.Dispatcher;
import okhttp3.EventListener;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.cache.CacheInterceptor;
import okhttp3.internal.http.BridgeInterceptor;
import okhttp3.internal.http.CallServerInterceptor;
import okhttp3.internal.http.ExchangeCodec;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.http.RetryAndFollowUpInterceptor;
import okhttp3.internal.platform.Platform;
import okio.AsyncTimeout;
/* compiled from: RealCall.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u009d\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007*\u0001)\u0018\u00002\u00020\u0001:\u0002`aB\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u000e\u0010,\u001a\u00020-2\u0006\u0010\u000e\u001a\u00020\u000fJ\b\u0010.\u001a\u00020-H\u0002J\b\u0010/\u001a\u00020-H\u0016J\b\u00100\u001a\u00020\u0000H\u0016J\u0010\u00101\u001a\u0002022\u0006\u00103\u001a\u000204H\u0002J\u0010\u00105\u001a\u00020-2\u0006\u00106\u001a\u000207H\u0016J\u0016\u00108\u001a\u00020-2\u0006\u00109\u001a\u00020\u00052\u0006\u0010:\u001a\u00020\u0007J\b\u0010;\u001a\u00020<H\u0016J\u0015\u0010=\u001a\u00020-2\u0006\u0010>\u001a\u00020\u0007H\u0000¢\u0006\u0002\b?J\r\u0010@\u001a\u00020<H\u0000¢\u0006\u0002\bAJ\u0015\u0010B\u001a\u00020\u00192\u0006\u0010C\u001a\u00020DH\u0000¢\u0006\u0002\bEJ\b\u0010F\u001a\u00020\u0007H\u0016J\b\u0010G\u001a\u00020\u0007H\u0016J)\u0010H\u001a\u0002HI\"\n\b\u0000\u0010I*\u0004\u0018\u00010J2\u0006\u0010K\u001a\u0002HI2\u0006\u0010L\u001a\u00020\u0007H\u0002¢\u0006\u0002\u0010MJ;\u0010N\u001a\u0002HI\"\n\b\u0000\u0010I*\u0004\u0018\u00010J2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010O\u001a\u00020\u00072\u0006\u0010P\u001a\u00020\u00072\u0006\u0010K\u001a\u0002HIH\u0000¢\u0006\u0004\bQ\u0010RJ\u0019\u0010%\u001a\u0004\u0018\u00010J2\b\u0010K\u001a\u0004\u0018\u00010JH\u0000¢\u0006\u0002\bSJ\r\u0010T\u001a\u00020UH\u0000¢\u0006\u0002\bVJ\u000f\u0010W\u001a\u0004\u0018\u00010XH\u0000¢\u0006\u0002\bYJ\b\u00109\u001a\u00020\u0005H\u0016J\u0006\u0010Z\u001a\u00020\u0007J\b\u0010(\u001a\u00020[H\u0016J\u0006\u0010+\u001a\u00020-J!\u0010\\\u001a\u0002HI\"\n\b\u0000\u0010I*\u0004\u0018\u00010J2\u0006\u0010]\u001a\u0002HIH\u0002¢\u0006\u0002\u0010^J\b\u0010_\u001a\u00020UH\u0002R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u001c\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\"\u0010\"\u001a\u0004\u0018\u00010\u00192\b\u0010!\u001a\u0004\u0018\u00010\u0019@BX\u0080\u000e¢\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u000e\u0010%\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b&\u0010'R\u0010\u0010(\u001a\u00020)X\u0082\u0004¢\u0006\u0004\n\u0002\u0010*R\u000e\u0010+\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006b"}, d2 = {"Lokhttp3/internal/connection/RealCall;", "Lokhttp3/Call;", "client", "Lokhttp3/OkHttpClient;", "originalRequest", "Lokhttp3/Request;", "forWebSocket", "", "(Lokhttp3/OkHttpClient;Lokhttp3/Request;Z)V", "callStackTrace", "", "canceled", "getClient", "()Lokhttp3/OkHttpClient;", "connection", "Lokhttp3/internal/connection/RealConnection;", "getConnection", "()Lokhttp3/internal/connection/RealConnection;", "setConnection", "(Lokhttp3/internal/connection/RealConnection;)V", "connectionPool", "Lokhttp3/internal/connection/RealConnectionPool;", "eventListener", "Lokhttp3/EventListener;", "exchange", "Lokhttp3/internal/connection/Exchange;", "exchangeFinder", "Lokhttp3/internal/connection/ExchangeFinder;", "exchangeRequestDone", "exchangeResponseDone", "executed", "getForWebSocket", "()Z", "<set-?>", "interceptorScopedExchange", "getInterceptorScopedExchange$okhttp", "()Lokhttp3/internal/connection/Exchange;", "noMoreExchanges", "getOriginalRequest", "()Lokhttp3/Request;", "timeout", "okhttp3/internal/connection/RealCall$timeout$1", "Lokhttp3/internal/connection/RealCall$timeout$1;", "timeoutEarlyExit", "acquireConnectionNoEvents", "", "callStart", "cancel", "clone", "createAddress", "Lokhttp3/Address;", ImagesContract.URL, "Lokhttp3/HttpUrl;", "enqueue", "responseCallback", "Lokhttp3/Callback;", "enterNetworkInterceptorExchange", "request", "newExchangeFinder", "execute", "Lokhttp3/Response;", "exitNetworkInterceptorExchange", "closeExchange", "exitNetworkInterceptorExchange$okhttp", "getResponseWithInterceptorChain", "getResponseWithInterceptorChain$okhttp", "initExchange", "chain", "Lokhttp3/internal/http/RealInterceptorChain;", "initExchange$okhttp", "isCanceled", "isExecuted", "maybeReleaseConnection", ExifInterface.LONGITUDE_EAST, "Ljava/io/IOException;", "e", "force", "(Ljava/io/IOException;Z)Ljava/io/IOException;", "messageDone", "requestDone", "responseDone", "messageDone$okhttp", "(Lokhttp3/internal/connection/Exchange;ZZLjava/io/IOException;)Ljava/io/IOException;", "noMoreExchanges$okhttp", "redactedUrl", "", "redactedUrl$okhttp", "releaseConnectionNoEvents", "Ljava/net/Socket;", "releaseConnectionNoEvents$okhttp", "retryAfterFailure", "Lokio/AsyncTimeout;", "timeoutExit", "cause", "(Ljava/io/IOException;)Ljava/io/IOException;", "toLoggableString", "AsyncCall", "CallReference", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class RealCall implements Call {
    private Object callStackTrace;
    private boolean canceled;
    private final OkHttpClient client;
    private RealConnection connection;
    private final RealConnectionPool connectionPool;
    private final EventListener eventListener;
    private Exchange exchange;
    private ExchangeFinder exchangeFinder;
    private boolean exchangeRequestDone;
    private boolean exchangeResponseDone;
    private boolean executed;
    private final boolean forWebSocket;
    private Exchange interceptorScopedExchange;
    private boolean noMoreExchanges;
    private final Request originalRequest;
    private final RealCall$timeout$1 timeout;
    private boolean timeoutEarlyExit;

    public RealCall(OkHttpClient client, Request originalRequest, boolean forWebSocket) {
        Intrinsics.checkParameterIsNotNull(client, "client");
        Intrinsics.checkParameterIsNotNull(originalRequest, "originalRequest");
        this.client = client;
        this.originalRequest = originalRequest;
        this.forWebSocket = forWebSocket;
        this.connectionPool = this.client.connectionPool().getDelegate$okhttp();
        this.eventListener = this.client.eventListenerFactory().create(this);
        RealCall$timeout$1 $this$apply = new RealCall$timeout$1(this);
        $this$apply.timeout((long) this.client.callTimeoutMillis(), TimeUnit.MILLISECONDS);
        this.timeout = $this$apply;
    }

    public final OkHttpClient getClient() {
        return this.client;
    }

    public final Request getOriginalRequest() {
        return this.originalRequest;
    }

    public final boolean getForWebSocket() {
        return this.forWebSocket;
    }

    public final RealConnection getConnection() {
        return this.connection;
    }

    public final void setConnection(RealConnection realConnection) {
        this.connection = realConnection;
    }

    public final Exchange getInterceptorScopedExchange$okhttp() {
        return this.interceptorScopedExchange;
    }

    @Override // okhttp3.Call
    public AsyncTimeout timeout() {
        return this.timeout;
    }

    @Override // okhttp3.Call, java.lang.Object
    public RealCall clone() {
        return new RealCall(this.client, this.originalRequest, this.forWebSocket);
    }

    @Override // okhttp3.Call
    public Request request() {
        return this.originalRequest;
    }

    @Override // okhttp3.Call
    public void cancel() {
        RealConnection connectionToCancel;
        synchronized (this.connectionPool) {
            if (!this.canceled) {
                this.canceled = true;
                Exchange exchangeToCancel = this.exchange;
                ExchangeFinder exchangeFinder = this.exchangeFinder;
                if (exchangeFinder == null || (connectionToCancel = exchangeFinder.connectingConnection()) == null) {
                    connectionToCancel = this.connection;
                }
                Unit unit = Unit.INSTANCE;
                if (exchangeToCancel != null) {
                    exchangeToCancel.cancel();
                } else if (connectionToCancel != null) {
                    connectionToCancel.cancel();
                }
                this.eventListener.canceled(this);
            }
        }
    }

    @Override // okhttp3.Call
    public boolean isCanceled() {
        boolean z;
        synchronized (this.connectionPool) {
            z = this.canceled;
        }
        return z;
    }

    @Override // okhttp3.Call
    public Response execute() {
        synchronized (this) {
            if (!this.executed) {
                this.executed = true;
                Unit unit = Unit.INSTANCE;
            } else {
                throw new IllegalStateException("Already Executed".toString());
            }
        }
        this.timeout.enter();
        callStart();
        try {
            this.client.dispatcher().executed$okhttp(this);
            return getResponseWithInterceptorChain$okhttp();
        } finally {
            this.client.dispatcher().finished$okhttp(this);
        }
    }

    @Override // okhttp3.Call
    public void enqueue(Callback responseCallback) {
        Intrinsics.checkParameterIsNotNull(responseCallback, "responseCallback");
        synchronized (this) {
            if (!this.executed) {
                this.executed = true;
                Unit unit = Unit.INSTANCE;
            } else {
                throw new IllegalStateException("Already Executed".toString());
            }
        }
        callStart();
        this.client.dispatcher().enqueue$okhttp(new AsyncCall(this, responseCallback));
    }

    @Override // okhttp3.Call
    public synchronized boolean isExecuted() {
        return this.executed;
    }

    private final void callStart() {
        this.callStackTrace = Platform.Companion.get().getStackTraceForCloseable("response.body().close()");
        this.eventListener.callStart(this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r11v0, types: [okhttp3.internal.connection.RealCall] */
    /* JADX WARN: Type inference failed for: r2v15, types: [java.lang.Iterable] */
    /* JADX WARN: Type inference failed for: r3v4, types: [okhttp3.Cache] */
    /* JADX WARN: Type inference failed for: r3v5, types: [java.io.IOException] */
    /* JADX WARN: Type inference failed for: r3v8, types: [java.io.IOException] */
    /* JADX WARN: Type inference failed for: r3v9 */
    /* JADX WARN: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Response getResponseWithInterceptorChain$okhttp() throws IOException {
        List interceptors = new ArrayList();
        CollectionsKt.addAll(interceptors, this.client.interceptors());
        interceptors.add(new RetryAndFollowUpInterceptor(this.client));
        interceptors.add(new BridgeInterceptor(this.client.cookieJar()));
        ?? cache = this.client.cache();
        interceptors.add(new CacheInterceptor(cache));
        ConnectInterceptor connectInterceptor = ConnectInterceptor.INSTANCE;
        interceptors.add(connectInterceptor);
        ConnectInterceptor connectInterceptor2 = connectInterceptor;
        if (!this.forWebSocket) {
            List<Interceptor> networkInterceptors = this.client.networkInterceptors();
            CollectionsKt.addAll(interceptors, networkInterceptors);
            connectInterceptor2 = networkInterceptors;
        }
        try {
            interceptors.add(new CallServerInterceptor(this.forWebSocket));
            connectInterceptor2 = null;
            connectInterceptor2 = null;
            cache = 0;
            cache = 0;
            try {
                Response response = new RealInterceptorChain(this, interceptors, 0, null, this.originalRequest, this.client.connectTimeoutMillis(), this.client.readTimeoutMillis(), this.client.writeTimeoutMillis()).proceed(this.originalRequest);
                boolean isCanceled = isCanceled();
                if (!isCanceled) {
                    return response;
                }
                Util.closeQuietly(response);
                throw new IOException("Canceled");
            } catch (IOException e) {
                IOException noMoreExchanges$okhttp = noMoreExchanges$okhttp(e);
                if (noMoreExchanges$okhttp != null) {
                    throw noMoreExchanges$okhttp;
                }
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Throwable");
            }
        } finally {
            if (connectInterceptor2 == null) {
                noMoreExchanges$okhttp(cache);
            }
        }
    }

    public final void enterNetworkInterceptorExchange(Request request, boolean newExchangeFinder) {
        Intrinsics.checkParameterIsNotNull(request, "request");
        boolean z = true;
        if (this.interceptorScopedExchange == null) {
            if (this.exchange != null) {
                z = false;
            }
            if (!z) {
                throw new IllegalStateException("cannot make a new request because the previous response is still open: please call response.close()".toString());
            } else if (newExchangeFinder) {
                this.exchangeFinder = new ExchangeFinder(this.connectionPool, createAddress(request.url()), this, this.eventListener);
            }
        } else {
            throw new IllegalStateException("Check failed.".toString());
        }
    }

    public final Exchange initExchange$okhttp(RealInterceptorChain chain) {
        Intrinsics.checkParameterIsNotNull(chain, "chain");
        synchronized (this.connectionPool) {
            boolean z = true;
            if (!this.noMoreExchanges) {
                if (this.exchange != null) {
                    z = false;
                }
                if (z) {
                    Unit unit = Unit.INSTANCE;
                } else {
                    throw new IllegalStateException("Check failed.".toString());
                }
            } else {
                throw new IllegalStateException("released".toString());
            }
        }
        ExchangeFinder exchangeFinder = this.exchangeFinder;
        if (exchangeFinder == null) {
            Intrinsics.throwNpe();
        }
        ExchangeCodec codec = exchangeFinder.find(this.client, chain);
        EventListener eventListener = this.eventListener;
        ExchangeFinder exchangeFinder2 = this.exchangeFinder;
        if (exchangeFinder2 == null) {
            Intrinsics.throwNpe();
        }
        Exchange result = new Exchange(this, eventListener, exchangeFinder2, codec);
        this.interceptorScopedExchange = result;
        synchronized (this.connectionPool) {
            this.exchange = result;
            this.exchangeRequestDone = false;
            this.exchangeResponseDone = false;
        }
        return result;
    }

    public final void acquireConnectionNoEvents(RealConnection connection) {
        Intrinsics.checkParameterIsNotNull(connection, "connection");
        Object $this$assertThreadHoldsLock$iv = this.connectionPool;
        if (!Util.assertionsEnabled || Thread.holdsLock($this$assertThreadHoldsLock$iv)) {
            if (this.connection == null) {
                this.connection = connection;
                connection.getCalls().add(new CallReference(this, this.callStackTrace));
                return;
            }
            throw new IllegalStateException("Check failed.".toString());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Thread ");
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
        sb.append(currentThread.getName());
        sb.append(" MUST hold lock on ");
        sb.append($this$assertThreadHoldsLock$iv);
        throw new AssertionError(sb.toString());
    }

    public final <E extends IOException> E messageDone$okhttp(Exchange exchange, boolean requestDone, boolean responseDone, E e) {
        Intrinsics.checkParameterIsNotNull(exchange, "exchange");
        boolean exchangeDone = false;
        synchronized (this.connectionPool) {
            if (!Intrinsics.areEqual(exchange, this.exchange)) {
                return e;
            }
            boolean changed = false;
            if (requestDone) {
                if (!this.exchangeRequestDone) {
                    changed = true;
                }
                this.exchangeRequestDone = true;
            }
            if (responseDone) {
                if (!this.exchangeResponseDone) {
                    changed = true;
                }
                this.exchangeResponseDone = true;
            }
            if (this.exchangeRequestDone && this.exchangeResponseDone && changed) {
                exchangeDone = true;
                Exchange exchange2 = this.exchange;
                if (exchange2 == null) {
                    Intrinsics.throwNpe();
                }
                RealConnection connection$okhttp = exchange2.getConnection$okhttp();
                connection$okhttp.setSuccessCount$okhttp(connection$okhttp.getSuccessCount$okhttp() + 1);
                this.exchange = null;
            }
            Unit unit = Unit.INSTANCE;
            if (exchangeDone) {
                return (E) maybeReleaseConnection(e, false);
            }
            return e;
        }
    }

    public final IOException noMoreExchanges$okhttp(IOException e) {
        synchronized (this.connectionPool) {
            this.noMoreExchanges = true;
            Unit unit = Unit.INSTANCE;
        }
        return maybeReleaseConnection(e, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x001d A[Catch: all -> 0x0017, TryCatch #0 {all -> 0x0017, blocks: (B:6:0x0010, B:14:0x001d, B:16:0x0028, B:19:0x002e, B:21:0x0032, B:23:0x0038, B:25:0x003d, B:26:0x0041, B:28:0x0045, B:32:0x004c, B:54:0x0091, B:55:0x009e), top: B:57:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0090  */
    /* JADX WARN: Type inference failed for: r8v4, types: [T, okhttp3.Connection] */
    /* JADX WARN: Type inference failed for: r9v1, types: [T, okhttp3.Connection] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private final <E extends IOException> E maybeReleaseConnection(E e, boolean force) {
        boolean callFailed;
        boolean z;
        Socket socket;
        boolean callEnd;
        E e2 = e;
        Ref.ObjectRef releasedConnection = new Ref.ObjectRef();
        synchronized (this.connectionPool) {
            callFailed = false;
            if (force) {
                try {
                    if (this.exchange != null) {
                        z = false;
                        if (!z) {
                            releasedConnection.element = this.connection;
                            if (this.connection == null || this.exchange != null || (!force && !this.noMoreExchanges)) {
                                socket = null;
                            } else {
                                socket = releaseConnectionNoEvents$okhttp();
                            }
                            if (this.connection != null) {
                                releasedConnection.element = (Connection) 0;
                            }
                            callEnd = this.noMoreExchanges && this.exchange == null;
                            Unit unit = Unit.INSTANCE;
                        } else {
                            throw new IllegalStateException("cannot release connection while it is in use".toString());
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            z = true;
            if (!z) {
            }
        }
        if (socket != null) {
            Util.closeQuietly(socket);
        }
        if (((Connection) releasedConnection.element) != null) {
            EventListener eventListener = this.eventListener;
            RealCall realCall = this;
            Connection connection = (Connection) releasedConnection.element;
            if (connection == null) {
                Intrinsics.throwNpe();
            }
            eventListener.connectionReleased(realCall, connection);
        }
        if (callEnd) {
            if (e2 != null) {
                callFailed = true;
            }
            e2 = (E) timeoutExit(e2);
            if (callFailed) {
                EventListener eventListener2 = this.eventListener;
                RealCall realCall2 = this;
                if (e2 == null) {
                    Intrinsics.throwNpe();
                }
                eventListener2.callFailed(realCall2, e2);
            } else {
                this.eventListener.callEnd(this);
            }
        }
        return e2;
    }

    public final Socket releaseConnectionNoEvents$okhttp() {
        Object $this$assertThreadHoldsLock$iv = this.connectionPool;
        if (!Util.assertionsEnabled || Thread.holdsLock($this$assertThreadHoldsLock$iv)) {
            RealConnection realConnection = this.connection;
            if (realConnection == null) {
                Intrinsics.throwNpe();
            }
            int index$iv = 0;
            Iterator<Reference<RealCall>> it = realConnection.getCalls().iterator();
            while (true) {
                if (!it.hasNext()) {
                    index$iv = -1;
                    break;
                } else if (Intrinsics.areEqual(it.next().get(), this)) {
                    break;
                } else {
                    index$iv++;
                }
            }
            if (index$iv != -1) {
                RealConnection released = this.connection;
                if (released == null) {
                    Intrinsics.throwNpe();
                }
                released.getCalls().remove(index$iv);
                this.connection = null;
                if (released.getCalls().isEmpty()) {
                    released.setIdleAtNs$okhttp(System.nanoTime());
                    if (this.connectionPool.connectionBecameIdle(released)) {
                        return released.socket();
                    }
                }
                return null;
            }
            throw new IllegalStateException("Check failed.".toString());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Thread ");
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
        sb.append(currentThread.getName());
        sb.append(" MUST hold lock on ");
        sb.append($this$assertThreadHoldsLock$iv);
        throw new AssertionError(sb.toString());
    }

    private final <E extends IOException> E timeoutExit(E e) {
        if (this.timeoutEarlyExit) {
            return e;
        }
        if (!this.timeout.exit()) {
            return e;
        }
        InterruptedIOException e2 = new InterruptedIOException("timeout");
        if (e != null) {
            e2.initCause(e);
        }
        return e2;
    }

    public final void timeoutEarlyExit() {
        if (!this.timeoutEarlyExit) {
            this.timeoutEarlyExit = true;
            this.timeout.exit();
            return;
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    public final void exitNetworkInterceptorExchange$okhttp(boolean closeExchange) {
        boolean z = true;
        if (!this.noMoreExchanges) {
            if (closeExchange) {
                Exchange exchange = this.exchange;
                if (exchange != null) {
                    exchange.detachWithViolence();
                }
                if (this.exchange != null) {
                    z = false;
                }
                if (!z) {
                    throw new IllegalStateException("Check failed.".toString());
                }
            }
            this.interceptorScopedExchange = null;
            return;
        }
        throw new IllegalStateException("released".toString());
    }

    private final Address createAddress(HttpUrl url) {
        SSLSocketFactory sslSocketFactory = null;
        HostnameVerifier hostnameVerifier = null;
        CertificatePinner certificatePinner = null;
        if (url.isHttps()) {
            sslSocketFactory = this.client.sslSocketFactory();
            hostnameVerifier = this.client.hostnameVerifier();
            certificatePinner = this.client.certificatePinner();
        }
        return new Address(url.host(), url.port(), this.client.dns(), this.client.socketFactory(), sslSocketFactory, hostnameVerifier, certificatePinner, this.client.proxyAuthenticator(), this.client.proxy(), this.client.protocols(), this.client.connectionSpecs(), this.client.proxySelector());
    }

    public final boolean retryAfterFailure() {
        ExchangeFinder exchangeFinder = this.exchangeFinder;
        if (exchangeFinder == null) {
            Intrinsics.throwNpe();
        }
        return exchangeFinder.retryAfterFailure();
    }

    public final String toLoggableString() {
        StringBuilder sb = new StringBuilder();
        sb.append(isCanceled() ? "canceled " : "");
        sb.append(this.forWebSocket ? "web socket" : NotificationCompat.CATEGORY_CALL);
        sb.append(" to ");
        sb.append(redactedUrl$okhttp());
        return sb.toString();
    }

    public final String redactedUrl$okhttp() {
        return this.originalRequest.url().redact();
    }

    /* compiled from: RealCall.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0080\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019J\u0012\u0010\u001a\u001a\u00020\u00172\n\u0010\u001b\u001a\u00060\u0000R\u00020\u0006J\b\u0010\u001c\u001a\u00020\u0017H\u0016R\u0011\u0010\u0005\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u001e\u0010\u000b\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\n@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u000f8F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0012\u001a\u00020\u00138F¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lokhttp3/internal/connection/RealCall$AsyncCall;", "Ljava/lang/Runnable;", "responseCallback", "Lokhttp3/Callback;", "(Lokhttp3/internal/connection/RealCall;Lokhttp3/Callback;)V", NotificationCompat.CATEGORY_CALL, "Lokhttp3/internal/connection/RealCall;", "getCall", "()Lokhttp3/internal/connection/RealCall;", "<set-?>", "Ljava/util/concurrent/atomic/AtomicInteger;", "callsPerHost", "getCallsPerHost", "()Ljava/util/concurrent/atomic/AtomicInteger;", "host", "", "getHost", "()Ljava/lang/String;", "request", "Lokhttp3/Request;", "getRequest", "()Lokhttp3/Request;", "executeOn", "", "executorService", "Ljava/util/concurrent/ExecutorService;", "reuseCallsPerHostFrom", "other", "run", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public final class AsyncCall implements Runnable {
        private volatile AtomicInteger callsPerHost = new AtomicInteger(0);
        private final Callback responseCallback;
        final /* synthetic */ RealCall this$0;

        public AsyncCall(RealCall $outer, Callback responseCallback) {
            Intrinsics.checkParameterIsNotNull(responseCallback, "responseCallback");
            this.this$0 = $outer;
            this.responseCallback = responseCallback;
        }

        public final AtomicInteger getCallsPerHost() {
            return this.callsPerHost;
        }

        public final void reuseCallsPerHostFrom(AsyncCall other) {
            Intrinsics.checkParameterIsNotNull(other, "other");
            this.callsPerHost = other.callsPerHost;
        }

        public final String getHost() {
            return this.this$0.getOriginalRequest().url().host();
        }

        public final Request getRequest() {
            return this.this$0.getOriginalRequest();
        }

        public final RealCall getCall() {
            return this.this$0;
        }

        public final void executeOn(ExecutorService executorService) {
            Intrinsics.checkParameterIsNotNull(executorService, "executorService");
            Object $this$assertThreadDoesntHoldLock$iv = this.this$0.getClient().dispatcher();
            if (!Util.assertionsEnabled || !Thread.holdsLock($this$assertThreadDoesntHoldLock$iv)) {
                try {
                    try {
                        executorService.execute(this);
                    } catch (RejectedExecutionException e) {
                        InterruptedIOException ioException = new InterruptedIOException("executor rejected");
                        ioException.initCause(e);
                        this.this$0.noMoreExchanges$okhttp(ioException);
                        this.responseCallback.onFailure(this.this$0, ioException);
                        this.this$0.getClient().dispatcher().finished$okhttp(this);
                    }
                } catch (Throwable th) {
                    this.this$0.getClient().dispatcher().finished$okhttp(this);
                    throw th;
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Thread ");
                Thread currentThread = Thread.currentThread();
                Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
                sb.append(currentThread.getName());
                sb.append(" MUST NOT hold lock on ");
                sb.append($this$assertThreadDoesntHoldLock$iv);
                throw new AssertionError(sb.toString());
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            Dispatcher dispatcher;
            Thread currentThread$iv = Thread.currentThread();
            Intrinsics.checkExpressionValueIsNotNull(currentThread$iv, "currentThread");
            String oldName$iv = currentThread$iv.getName();
            currentThread$iv.setName("OkHttp " + this.this$0.redactedUrl$okhttp());
            boolean signalledCallback = false;
            try {
                this.this$0.timeout.enter();
                try {
                    signalledCallback = true;
                    this.responseCallback.onResponse(this.this$0, this.this$0.getResponseWithInterceptorChain$okhttp());
                    dispatcher = this.this$0.getClient().dispatcher();
                } catch (IOException e) {
                    if (signalledCallback) {
                        Platform.Companion.get().log("Callback failure for " + this.this$0.toLoggableString(), 4, e);
                    } else {
                        this.responseCallback.onFailure(this.this$0, e);
                    }
                    dispatcher = this.this$0.getClient().dispatcher();
                } catch (Throwable t) {
                    this.this$0.cancel();
                    if (!signalledCallback) {
                        IOException canceledException = new IOException("canceled due to " + t);
                        canceledException.addSuppressed(t);
                        this.responseCallback.onFailure(this.this$0, canceledException);
                    }
                    throw t;
                }
                dispatcher.finished$okhttp(this);
            } finally {
                currentThread$iv.setName(oldName$iv);
            }
        }
    }

    /* compiled from: RealCall.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\b\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0017\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\t"}, d2 = {"Lokhttp3/internal/connection/RealCall$CallReference;", "Ljava/lang/ref/WeakReference;", "Lokhttp3/internal/connection/RealCall;", "referent", "callStackTrace", "", "(Lokhttp3/internal/connection/RealCall;Ljava/lang/Object;)V", "getCallStackTrace", "()Ljava/lang/Object;", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class CallReference extends WeakReference<RealCall> {
        private final Object callStackTrace;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public CallReference(RealCall referent, Object callStackTrace) {
            super(referent);
            Intrinsics.checkParameterIsNotNull(referent, "referent");
            this.callStackTrace = callStackTrace;
        }

        public final Object getCallStackTrace() {
            return this.callStackTrace;
        }
    }
}
