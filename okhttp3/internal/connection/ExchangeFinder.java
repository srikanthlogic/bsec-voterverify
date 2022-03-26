package okhttp3.internal.connection;

import androidx.core.app.NotificationCompat;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import okhttp3.Address;
import okhttp3.EventListener;
import okhttp3.OkHttpClient;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RouteSelector;
import okhttp3.internal.http.ExchangeCodec;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.http2.ConnectionShutdownException;
import okhttp3.internal.http2.ErrorCode;
import okhttp3.internal.http2.StreamResetException;
/* compiled from: ExchangeFinder.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\b\u0010\r\u001a\u0004\u0018\u00010\u000eJ\u0016\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eJ0\u0010\u001f\u001a\u00020\u000e2\u0006\u0010 \u001a\u00020\u00102\u0006\u0010!\u001a\u00020\u00102\u0006\u0010\"\u001a\u00020\u00102\u0006\u0010#\u001a\u00020\u00102\u0006\u0010$\u001a\u00020%H\u0002J8\u0010&\u001a\u00020\u000e2\u0006\u0010 \u001a\u00020\u00102\u0006\u0010!\u001a\u00020\u00102\u0006\u0010\"\u001a\u00020\u00102\u0006\u0010#\u001a\u00020\u00102\u0006\u0010$\u001a\u00020%2\u0006\u0010'\u001a\u00020%H\u0002J\u0006\u0010(\u001a\u00020%J\b\u0010)\u001a\u00020%H\u0002J\u000e\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020-R\u0014\u0010\u0004\u001a\u00020\u0005X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006."}, d2 = {"Lokhttp3/internal/connection/ExchangeFinder;", "", "connectionPool", "Lokhttp3/internal/connection/RealConnectionPool;", "address", "Lokhttp3/Address;", NotificationCompat.CATEGORY_CALL, "Lokhttp3/internal/connection/RealCall;", "eventListener", "Lokhttp3/EventListener;", "(Lokhttp3/internal/connection/RealConnectionPool;Lokhttp3/Address;Lokhttp3/internal/connection/RealCall;Lokhttp3/EventListener;)V", "getAddress$okhttp", "()Lokhttp3/Address;", "connectingConnection", "Lokhttp3/internal/connection/RealConnection;", "connectionShutdownCount", "", "nextRouteToTry", "Lokhttp3/Route;", "otherFailureCount", "refusedStreamCount", "routeSelection", "Lokhttp3/internal/connection/RouteSelector$Selection;", "routeSelector", "Lokhttp3/internal/connection/RouteSelector;", "find", "Lokhttp3/internal/http/ExchangeCodec;", "client", "Lokhttp3/OkHttpClient;", "chain", "Lokhttp3/internal/http/RealInterceptorChain;", "findConnection", "connectTimeout", "readTimeout", "writeTimeout", "pingIntervalMillis", "connectionRetryEnabled", "", "findHealthyConnection", "doExtensiveHealthChecks", "retryAfterFailure", "retryCurrentRoute", "trackFailure", "", "e", "Ljava/io/IOException;", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class ExchangeFinder {
    private final Address address;
    private final RealCall call;
    private RealConnection connectingConnection;
    private final RealConnectionPool connectionPool;
    private int connectionShutdownCount;
    private final EventListener eventListener;
    private Route nextRouteToTry;
    private int otherFailureCount;
    private int refusedStreamCount;
    private RouteSelector.Selection routeSelection;
    private RouteSelector routeSelector;

    public ExchangeFinder(RealConnectionPool connectionPool, Address address, RealCall call, EventListener eventListener) {
        Intrinsics.checkParameterIsNotNull(connectionPool, "connectionPool");
        Intrinsics.checkParameterIsNotNull(address, "address");
        Intrinsics.checkParameterIsNotNull(call, NotificationCompat.CATEGORY_CALL);
        Intrinsics.checkParameterIsNotNull(eventListener, "eventListener");
        this.connectionPool = connectionPool;
        this.address = address;
        this.call = call;
        this.eventListener = eventListener;
    }

    public final Address getAddress$okhttp() {
        return this.address;
    }

    public final ExchangeCodec find(OkHttpClient client, RealInterceptorChain chain) {
        Intrinsics.checkParameterIsNotNull(client, "client");
        Intrinsics.checkParameterIsNotNull(chain, "chain");
        try {
            return findHealthyConnection(chain.getConnectTimeoutMillis$okhttp(), chain.getReadTimeoutMillis$okhttp(), chain.getWriteTimeoutMillis$okhttp(), client.pingIntervalMillis(), client.retryOnConnectionFailure(), !Intrinsics.areEqual(chain.getRequest$okhttp().method(), "GET")).newCodec$okhttp(client, chain);
        } catch (IOException e) {
            trackFailure(e);
            throw new RouteException(e);
        } catch (RouteException e2) {
            trackFailure(e2.getLastConnectException());
            throw e2;
        }
    }

    private final RealConnection findHealthyConnection(int connectTimeout, int readTimeout, int writeTimeout, int pingIntervalMillis, boolean connectionRetryEnabled, boolean doExtensiveHealthChecks) throws IOException {
        while (true) {
            RealConnection candidate = findConnection(connectTimeout, readTimeout, writeTimeout, pingIntervalMillis, connectionRetryEnabled);
            if (candidate.isHealthy(doExtensiveHealthChecks)) {
                return candidate;
            }
            candidate.noNewExchanges();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0053, code lost:
        if (r9.supportsUrl(r20.address.url()) == false) goto L_0x0055;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00ea, code lost:
        if (r8.hasNext() == false) goto L_0x00ec;
     */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0066 A[Catch: all -> 0x0237, TryCatch #0 {, blocks: (B:5:0x0014, B:7:0x001c, B:9:0x002d, B:11:0x0035, B:12:0x0038, B:14:0x003e, B:16:0x0046, B:17:0x0049, B:19:0x0055, B:21:0x005d, B:23:0x0066, B:26:0x0075, B:28:0x0087, B:29:0x0090, B:31:0x0094, B:32:0x009c, B:135:0x022d, B:136:0x0236), top: B:139:0x0014 }] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0075 A[Catch: all -> 0x0237, TryCatch #0 {, blocks: (B:5:0x0014, B:7:0x001c, B:9:0x002d, B:11:0x0035, B:12:0x0038, B:14:0x003e, B:16:0x0046, B:17:0x0049, B:19:0x0055, B:21:0x005d, B:23:0x0066, B:26:0x0075, B:28:0x0087, B:29:0x0090, B:31:0x0094, B:32:0x009c, B:135:0x022d, B:136:0x0236), top: B:139:0x0014 }] */
    /* JADX WARN: Type inference failed for: r9v33, types: [T, okhttp3.internal.connection.RealConnection] */
    /* JADX WARN: Type inference failed for: r9v4, types: [T, okhttp3.internal.connection.RealConnection] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private final RealConnection findConnection(int connectTimeout, int readTimeout, int writeTimeout, int pingIntervalMillis, boolean connectionRetryEnabled) throws IOException {
        Socket toClose;
        boolean foundPooledConnection = false;
        RealConnection realConnection = null;
        Route route = null;
        Ref.ObjectRef releasedConnection = new Ref.ObjectRef();
        synchronized (this.connectionPool) {
            if (!this.call.isCanceled()) {
                releasedConnection.element = this.call.getConnection();
                if (this.call.getConnection() != null) {
                    RealConnection connection = this.call.getConnection();
                    if (connection == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!connection.getNoNewExchanges()) {
                        RealConnection connection2 = this.call.getConnection();
                        if (connection2 == null) {
                            Intrinsics.throwNpe();
                        }
                    }
                    toClose = this.call.releaseConnectionNoEvents$okhttp();
                    if (this.call.getConnection() != null) {
                        realConnection = this.call.getConnection();
                        releasedConnection.element = (RealConnection) 0;
                    }
                    if (realConnection == null) {
                        this.refusedStreamCount = 0;
                        this.connectionShutdownCount = 0;
                        this.otherFailureCount = 0;
                        if (this.connectionPool.callAcquirePooledConnection(this.address, this.call, null, false)) {
                            foundPooledConnection = true;
                            realConnection = this.call.getConnection();
                        } else if (this.nextRouteToTry != null) {
                            route = this.nextRouteToTry;
                            this.nextRouteToTry = null;
                        }
                    }
                    Unit unit = Unit.INSTANCE;
                }
                toClose = null;
                if (this.call.getConnection() != null) {
                }
                if (realConnection == null) {
                }
                Unit unit2 = Unit.INSTANCE;
            } else {
                throw new IOException("Canceled");
            }
        }
        if (toClose != null) {
            Util.closeQuietly(toClose);
        }
        if (((RealConnection) releasedConnection.element) != null) {
            EventListener eventListener = this.eventListener;
            RealCall realCall = this.call;
            RealConnection realConnection2 = (RealConnection) releasedConnection.element;
            if (realConnection2 == null) {
                Intrinsics.throwNpe();
            }
            eventListener.connectionReleased(realCall, realConnection2);
        }
        if (foundPooledConnection) {
            EventListener eventListener2 = this.eventListener;
            RealCall realCall2 = this.call;
            if (realConnection == null) {
                Intrinsics.throwNpe();
            }
            eventListener2.connectionAcquired(realCall2, realConnection);
        }
        if (realConnection != null) {
            if (realConnection == null) {
                Intrinsics.throwNpe();
            }
            return realConnection;
        }
        boolean newRouteSelection = false;
        if (route == null) {
            RouteSelector.Selection selection = this.routeSelection;
            if (selection != null) {
                if (selection == null) {
                    Intrinsics.throwNpe();
                }
            }
            RouteSelector localRouteSelector = this.routeSelector;
            if (localRouteSelector == null) {
                localRouteSelector = new RouteSelector(this.address, this.call.getClient().getRouteDatabase(), this.call, this.eventListener);
                this.routeSelector = localRouteSelector;
            }
            newRouteSelection = true;
            this.routeSelection = localRouteSelector.next();
        }
        List<Route> list = null;
        synchronized (this.connectionPool) {
            if (!this.call.isCanceled()) {
                if (newRouteSelection) {
                    RouteSelector.Selection selection2 = this.routeSelection;
                    if (selection2 == null) {
                        Intrinsics.throwNpe();
                    }
                    list = selection2.getRoutes();
                    if (this.connectionPool.callAcquirePooledConnection(this.address, this.call, list, false)) {
                        foundPooledConnection = true;
                        realConnection = this.call.getConnection();
                    }
                }
                if (!foundPooledConnection) {
                    if (route == null) {
                        RouteSelector.Selection selection3 = this.routeSelection;
                        if (selection3 == null) {
                            Intrinsics.throwNpe();
                        }
                        route = selection3.next();
                    }
                    RealConnectionPool realConnectionPool = this.connectionPool;
                    if (route == null) {
                        Intrinsics.throwNpe();
                    }
                    realConnection = new RealConnection(realConnectionPool, route);
                    this.connectingConnection = realConnection;
                }
                Unit unit3 = Unit.INSTANCE;
            } else {
                throw new IOException("Canceled");
            }
        }
        if (foundPooledConnection) {
            EventListener eventListener3 = this.eventListener;
            RealCall realCall3 = this.call;
            if (realConnection == null) {
                Intrinsics.throwNpe();
            }
            eventListener3.connectionAcquired(realCall3, realConnection);
            if (realConnection == null) {
                Intrinsics.throwNpe();
            }
            return realConnection;
        }
        if (realConnection == null) {
            Intrinsics.throwNpe();
        }
        realConnection.connect(connectTimeout, readTimeout, writeTimeout, pingIntervalMillis, connectionRetryEnabled, this.call, this.eventListener);
        RouteDatabase routeDatabase = this.call.getClient().getRouteDatabase();
        if (realConnection == null) {
            Intrinsics.throwNpe();
        }
        routeDatabase.connected(realConnection.route());
        Socket socket = null;
        synchronized (this.connectionPool) {
            this.connectingConnection = null;
            if (this.connectionPool.callAcquirePooledConnection(this.address, this.call, list, true)) {
                if (realConnection == null) {
                    Intrinsics.throwNpe();
                }
                realConnection.setNoNewExchanges(true);
                if (realConnection == null) {
                    Intrinsics.throwNpe();
                }
                socket = realConnection.socket();
                realConnection = this.call.getConnection();
                this.nextRouteToTry = route;
            } else {
                RealConnectionPool realConnectionPool2 = this.connectionPool;
                if (realConnection == null) {
                    Intrinsics.throwNpe();
                }
                realConnectionPool2.put(realConnection);
                RealCall realCall4 = this.call;
                if (realConnection == null) {
                    Intrinsics.throwNpe();
                }
                realCall4.acquireConnectionNoEvents(realConnection);
            }
            Unit unit4 = Unit.INSTANCE;
        }
        if (socket != null) {
            Util.closeQuietly(socket);
        }
        EventListener eventListener4 = this.eventListener;
        RealCall realCall5 = this.call;
        if (realConnection == null) {
            Intrinsics.throwNpe();
        }
        eventListener4.connectionAcquired(realCall5, realConnection);
        if (realConnection == null) {
            Intrinsics.throwNpe();
        }
        return realConnection;
    }

    public final RealConnection connectingConnection() {
        Object $this$assertThreadHoldsLock$iv = this.connectionPool;
        if (!Util.assertionsEnabled || Thread.holdsLock($this$assertThreadHoldsLock$iv)) {
            return this.connectingConnection;
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

    public final void trackFailure(IOException e) {
        Intrinsics.checkParameterIsNotNull(e, "e");
        Object $this$assertThreadDoesntHoldLock$iv = this.connectionPool;
        if (!Util.assertionsEnabled || !Thread.holdsLock($this$assertThreadDoesntHoldLock$iv)) {
            synchronized (this.connectionPool) {
                this.nextRouteToTry = null;
                if ((e instanceof StreamResetException) && ((StreamResetException) e).errorCode == ErrorCode.REFUSED_STREAM) {
                    this.refusedStreamCount++;
                } else if (e instanceof ConnectionShutdownException) {
                    this.connectionShutdownCount++;
                } else {
                    this.otherFailureCount++;
                }
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

    public final boolean retryAfterFailure() {
        synchronized (this.connectionPool) {
            if (this.refusedStreamCount == 0 && this.connectionShutdownCount == 0 && this.otherFailureCount == 0) {
                return false;
            }
            if (this.nextRouteToTry != null) {
                return true;
            }
            if (retryCurrentRoute()) {
                RealConnection connection = this.call.getConnection();
                if (connection == null) {
                    Intrinsics.throwNpe();
                }
                this.nextRouteToTry = connection.route();
                return true;
            }
            RouteSelector.Selection selection = this.routeSelection;
            if (selection != null && selection.hasNext()) {
                return true;
            }
            RouteSelector localRouteSelector = this.routeSelector;
            if (localRouteSelector == null) {
                return true;
            }
            return localRouteSelector.hasNext();
        }
    }

    private final boolean retryCurrentRoute() {
        RealConnection connection;
        return this.refusedStreamCount <= 1 && this.connectionShutdownCount <= 1 && this.otherFailureCount <= 0 && (connection = this.call.getConnection()) != null && connection.getRouteFailureCount$okhttp() == 0 && Util.canReuseConnectionFor(connection.route().address().url(), this.address.url());
    }
}
