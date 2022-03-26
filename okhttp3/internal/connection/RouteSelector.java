package okhttp3.internal.connection;

import androidx.core.app.NotificationCompat;
import com.google.android.gms.common.internal.ImagesContract;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.Address;
import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.HttpUrl;
import okhttp3.Route;
import okhttp3.internal.Util;
/* compiled from: RouteSelector.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 !2\u00020\u0001:\u0002!\"B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\t\u0010\u0015\u001a\u00020\u0016H\u0086\u0002J\b\u0010\u0017\u001a\u00020\u0016H\u0002J\t\u0010\u0018\u001a\u00020\u0019H\u0086\u0002J\b\u0010\u001a\u001a\u00020\u0014H\u0002J\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u0014H\u0002J\u001a\u0010\u001e\u001a\u00020\u001c2\u0006\u0010\u001f\u001a\u00020 2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0014H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006#"}, d2 = {"Lokhttp3/internal/connection/RouteSelector;", "", "address", "Lokhttp3/Address;", "routeDatabase", "Lokhttp3/internal/connection/RouteDatabase;", NotificationCompat.CATEGORY_CALL, "Lokhttp3/Call;", "eventListener", "Lokhttp3/EventListener;", "(Lokhttp3/Address;Lokhttp3/internal/connection/RouteDatabase;Lokhttp3/Call;Lokhttp3/EventListener;)V", "inetSocketAddresses", "", "Ljava/net/InetSocketAddress;", "nextProxyIndex", "", "postponedRoutes", "", "Lokhttp3/Route;", "proxies", "Ljava/net/Proxy;", "hasNext", "", "hasNextProxy", "next", "Lokhttp3/internal/connection/RouteSelector$Selection;", "nextProxy", "resetNextInetSocketAddress", "", "proxy", "resetNextProxy", ImagesContract.URL, "Lokhttp3/HttpUrl;", "Companion", "Selection", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class RouteSelector {
    public static final Companion Companion = new Companion(null);
    private final Address address;
    private final Call call;
    private final EventListener eventListener;
    private int nextProxyIndex;
    private final RouteDatabase routeDatabase;
    private List<? extends Proxy> proxies = CollectionsKt.emptyList();
    private List<? extends InetSocketAddress> inetSocketAddresses = CollectionsKt.emptyList();
    private final List<Route> postponedRoutes = new ArrayList();

    public RouteSelector(Address address, RouteDatabase routeDatabase, Call call, EventListener eventListener) {
        Intrinsics.checkParameterIsNotNull(address, "address");
        Intrinsics.checkParameterIsNotNull(routeDatabase, "routeDatabase");
        Intrinsics.checkParameterIsNotNull(call, NotificationCompat.CATEGORY_CALL);
        Intrinsics.checkParameterIsNotNull(eventListener, "eventListener");
        this.address = address;
        this.routeDatabase = routeDatabase;
        this.call = call;
        this.eventListener = eventListener;
        resetNextProxy(this.address.url(), this.address.proxy());
    }

    public final boolean hasNext() {
        return hasNextProxy() || (this.postponedRoutes.isEmpty() ^ true);
    }

    public final Selection next() throws IOException {
        if (hasNext()) {
            List routes = new ArrayList();
            while (hasNextProxy()) {
                Proxy proxy = nextProxy();
                for (InetSocketAddress inetSocketAddress : this.inetSocketAddresses) {
                    Route route = new Route(this.address, proxy, inetSocketAddress);
                    if (this.routeDatabase.shouldPostpone(route)) {
                        this.postponedRoutes.add(route);
                    } else {
                        routes.add(route);
                    }
                }
                if (!routes.isEmpty()) {
                    break;
                }
            }
            if (routes.isEmpty()) {
                CollectionsKt.addAll(routes, this.postponedRoutes);
                this.postponedRoutes.clear();
            }
            return new Selection(routes);
        }
        throw new NoSuchElementException();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void resetNextProxy(HttpUrl url, Proxy proxy) {
        RouteSelector$resetNextProxy$1 $fun$selectProxies$1 = new Function0<List<? extends Proxy>>(proxy, url) { // from class: okhttp3.internal.connection.RouteSelector$resetNextProxy$1
            final /* synthetic */ Proxy $proxy;
            final /* synthetic */ HttpUrl $url;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$proxy = r2;
                this.$url = r3;
            }

            @Override // kotlin.jvm.functions.Function0
            public final List<? extends Proxy> invoke() {
                Proxy proxy2 = this.$proxy;
                if (proxy2 != null) {
                    return CollectionsKt.listOf(proxy2);
                }
                URI uri = this.$url.uri();
                if (uri.getHost() == null) {
                    return Util.immutableListOf(Proxy.NO_PROXY);
                }
                List proxiesOrNull = RouteSelector.this.address.proxySelector().select(uri);
                List list = proxiesOrNull;
                if (list == null || list.isEmpty()) {
                    return Util.immutableListOf(Proxy.NO_PROXY);
                }
                return Util.toImmutableList(proxiesOrNull);
            }
        };
        this.eventListener.proxySelectStart(this.call, url);
        this.proxies = $fun$selectProxies$1.invoke();
        this.nextProxyIndex = 0;
        this.eventListener.proxySelectEnd(this.call, url, this.proxies);
    }

    private final boolean hasNextProxy() {
        return this.nextProxyIndex < this.proxies.size();
    }

    private final Proxy nextProxy() throws IOException {
        if (hasNextProxy()) {
            List<? extends Proxy> list = this.proxies;
            int i = this.nextProxyIndex;
            this.nextProxyIndex = i + 1;
            Proxy result = (Proxy) list.get(i);
            resetNextInetSocketAddress(result);
            return result;
        }
        throw new SocketException("No route to " + this.address.url().host() + "; exhausted proxy configurations: " + this.proxies);
    }

    private final void resetNextInetSocketAddress(Proxy proxy) throws IOException {
        int socketPort;
        String socketHost;
        List mutableInetSocketAddresses = new ArrayList();
        this.inetSocketAddresses = mutableInetSocketAddresses;
        if (proxy.type() == Proxy.Type.DIRECT || proxy.type() == Proxy.Type.SOCKS) {
            socketHost = this.address.url().host();
            socketPort = this.address.url().port();
        } else {
            SocketAddress proxyAddress = proxy.address();
            if (proxyAddress instanceof InetSocketAddress) {
                socketHost = Companion.getSocketHost((InetSocketAddress) proxyAddress);
                socketPort = ((InetSocketAddress) proxyAddress).getPort();
            } else {
                throw new IllegalArgumentException(("Proxy.address() is not an InetSocketAddress: " + proxyAddress.getClass()).toString());
            }
        }
        if (1 > socketPort || 65535 < socketPort) {
            throw new SocketException("No route to " + socketHost + ':' + socketPort + "; port is out of range");
        } else if (proxy.type() == Proxy.Type.SOCKS) {
            mutableInetSocketAddresses.add(InetSocketAddress.createUnresolved(socketHost, socketPort));
        } else {
            this.eventListener.dnsStart(this.call, socketHost);
            List<InetAddress> addresses = this.address.dns().lookup(socketHost);
            if (!addresses.isEmpty()) {
                this.eventListener.dnsEnd(this.call, socketHost, addresses);
                for (InetAddress inetAddress : addresses) {
                    mutableInetSocketAddresses.add(new InetSocketAddress(inetAddress, socketPort));
                }
                return;
            }
            throw new UnknownHostException(this.address.dns() + " returned no addresses for " + socketHost);
        }
    }

    /* compiled from: RouteSelector.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\u0002\u0010\u0005J\t\u0010\n\u001a\u00020\u000bH\u0086\u0002J\t\u0010\f\u001a\u00020\u0004H\u0086\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t¨\u0006\r"}, d2 = {"Lokhttp3/internal/connection/RouteSelector$Selection;", "", "routes", "", "Lokhttp3/Route;", "(Ljava/util/List;)V", "nextRouteIndex", "", "getRoutes", "()Ljava/util/List;", "hasNext", "", "next", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Selection {
        private int nextRouteIndex;
        private final List<Route> routes;

        public Selection(List<Route> list) {
            Intrinsics.checkParameterIsNotNull(list, "routes");
            this.routes = list;
        }

        public final List<Route> getRoutes() {
            return this.routes;
        }

        public final boolean hasNext() {
            return this.nextRouteIndex < this.routes.size();
        }

        public final Route next() {
            if (hasNext()) {
                List<Route> list = this.routes;
                int i = this.nextRouteIndex;
                this.nextRouteIndex = i + 1;
                return list.get(i);
            }
            throw new NoSuchElementException();
        }
    }

    /* compiled from: RouteSelector.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0015\u0010\u0003\u001a\u00020\u0004*\u00020\u00058F¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"Lokhttp3/internal/connection/RouteSelector$Companion;", "", "()V", "socketHost", "", "Ljava/net/InetSocketAddress;", "getSocketHost", "(Ljava/net/InetSocketAddress;)Ljava/lang/String;", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        public final String getSocketHost(InetSocketAddress $this$socketHost) {
            Intrinsics.checkParameterIsNotNull($this$socketHost, "$this$socketHost");
            InetAddress address = $this$socketHost.getAddress();
            if (address != null) {
                String hostAddress = address.getHostAddress();
                Intrinsics.checkExpressionValueIsNotNull(hostAddress, "address.hostAddress");
                return hostAddress;
            }
            String hostName = $this$socketHost.getHostName();
            Intrinsics.checkExpressionValueIsNotNull(hostName, "hostName");
            return hostName;
        }
    }
}