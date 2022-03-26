package okhttp3.internal.platform;

import android.os.Build;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.Protocol;
import okhttp3.internal.platform.android.AndroidCertificateChainCleaner;
import okhttp3.internal.platform.android.CloseGuard;
import okhttp3.internal.platform.android.ConscryptSocketAdapter;
import okhttp3.internal.platform.android.DeferredSocketAdapter;
import okhttp3.internal.platform.android.SocketAdapter;
import okhttp3.internal.platform.android.StandardAndroidSocketAdapter;
import okhttp3.internal.platform.android.UtilKt;
import okhttp3.internal.tls.CertificateChainCleaner;
import okhttp3.internal.tls.TrustRootIndex;
/* compiled from: AndroidPlatform.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0084\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 22\u00020\u0001:\u000223B\u0005¢\u0006\u0002\u0010\u0002J$\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\n\u0010\f\u001a\u0006\u0012\u0002\b\u00030\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J$\u0010\u0010\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\n\u0010\f\u001a\u0006\u0012\u0002\b\u00030\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J-\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\b\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0011\u0010\u001b\u001a\r\u0012\t\u0012\u00070\u001c¢\u0006\u0002\b\u001d0\u0006H\u0016J \u0010\u001e\u001a\u00020\u00182\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$H\u0016J\u0012\u0010%\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J\u0012\u0010&\u001a\u0004\u0018\u00010\u000f2\u0006\u0010'\u001a\u00020\u000bH\u0016J\u0010\u0010(\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\"\u0010)\u001a\u00020\u00182\u0006\u0010*\u001a\u00020\u000b2\u0006\u0010+\u001a\u00020$2\b\u0010,\u001a\u0004\u0018\u00010-H\u0016J\u001a\u0010.\u001a\u00020\u00182\u0006\u0010*\u001a\u00020\u000b2\b\u0010/\u001a\u0004\u0018\u00010\u000fH\u0016J\u0012\u0010\u0013\u001a\u0004\u0018\u00010\u00142\u0006\u00100\u001a\u000201H\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u00064"}, d2 = {"Lokhttp3/internal/platform/AndroidPlatform;", "Lokhttp3/internal/platform/Platform;", "()V", "closeGuard", "Lokhttp3/internal/platform/android/CloseGuard;", "socketAdapters", "", "Lokhttp3/internal/platform/android/SocketAdapter;", "api23IsCleartextTrafficPermitted", "", "hostname", "", "networkPolicyClass", "Ljava/lang/Class;", "networkSecurityPolicy", "", "api24IsCleartextTrafficPermitted", "buildCertificateChainCleaner", "Lokhttp3/internal/tls/CertificateChainCleaner;", "trustManager", "Ljavax/net/ssl/X509TrustManager;", "buildTrustRootIndex", "Lokhttp3/internal/tls/TrustRootIndex;", "configureTlsExtensions", "", "sslSocket", "Ljavax/net/ssl/SSLSocket;", "protocols", "Lokhttp3/Protocol;", "Lkotlin/jvm/JvmSuppressWildcards;", "connectSocket", "socket", "Ljava/net/Socket;", "address", "Ljava/net/InetSocketAddress;", "connectTimeout", "", "getSelectedProtocol", "getStackTraceForCloseable", "closer", "isCleartextTrafficPermitted", "log", "message", FirebaseAnalytics.Param.LEVEL, "t", "", "logCloseableLeak", "stackTrace", "sslSocketFactory", "Ljavax/net/ssl/SSLSocketFactory;", "Companion", "CustomTrustRootIndex", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class AndroidPlatform extends Platform {
    public static final Companion Companion = new Companion(null);
    private static final boolean isAndroid;
    private static final boolean isSupported;
    private final CloseGuard closeGuard;
    private final List<SocketAdapter> socketAdapters;

    public AndroidPlatform() {
        SocketAdapter[] socketAdapterArr = {StandardAndroidSocketAdapter.Companion.buildIfSupported$default(StandardAndroidSocketAdapter.Companion, null, 1, null), ConscryptSocketAdapter.Companion.buildIfSupported(), new DeferredSocketAdapter("com.google.android.gms.org.conscrypt")};
        Collection destination$iv$iv = new ArrayList();
        for (Object element$iv$iv : CollectionsKt.listOfNotNull((Object[]) socketAdapterArr)) {
            if (((SocketAdapter) element$iv$iv).isSupported()) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        this.socketAdapters = (List) destination$iv$iv;
        this.closeGuard = CloseGuard.Companion.get();
    }

    @Override // okhttp3.internal.platform.Platform
    public void connectSocket(Socket socket, InetSocketAddress address, int connectTimeout) throws IOException {
        Intrinsics.checkParameterIsNotNull(socket, "socket");
        Intrinsics.checkParameterIsNotNull(address, "address");
        try {
            socket.connect(address, connectTimeout);
        } catch (ClassCastException e) {
            if (Build.VERSION.SDK_INT == 26) {
                throw new IOException("Exception in connect", e);
            }
            throw e;
        }
    }

    @Override // okhttp3.internal.platform.Platform
    public X509TrustManager trustManager(SSLSocketFactory sslSocketFactory) {
        Object obj;
        Intrinsics.checkParameterIsNotNull(sslSocketFactory, "sslSocketFactory");
        Iterator<T> it = this.socketAdapters.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (((SocketAdapter) obj).matchesSocketFactory(sslSocketFactory)) {
                break;
            }
        }
        SocketAdapter socketAdapter = (SocketAdapter) obj;
        if (socketAdapter != null) {
            return socketAdapter.trustManager(sslSocketFactory);
        }
        return null;
    }

    @Override // okhttp3.internal.platform.Platform
    public void configureTlsExtensions(SSLSocket sslSocket, String hostname, List<Protocol> list) {
        Object obj;
        Intrinsics.checkParameterIsNotNull(sslSocket, "sslSocket");
        Intrinsics.checkParameterIsNotNull(list, "protocols");
        Iterator<T> it = this.socketAdapters.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (((SocketAdapter) obj).matchesSocket(sslSocket)) {
                break;
            }
        }
        SocketAdapter socketAdapter = (SocketAdapter) obj;
        if (socketAdapter != null) {
            socketAdapter.configureTlsExtensions(sslSocket, hostname, list);
        }
    }

    @Override // okhttp3.internal.platform.Platform
    public String getSelectedProtocol(SSLSocket sslSocket) {
        Object obj;
        Intrinsics.checkParameterIsNotNull(sslSocket, "sslSocket");
        Iterator<T> it = this.socketAdapters.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (((SocketAdapter) obj).matchesSocket(sslSocket)) {
                break;
            }
        }
        SocketAdapter socketAdapter = (SocketAdapter) obj;
        if (socketAdapter != null) {
            return socketAdapter.getSelectedProtocol(sslSocket);
        }
        return null;
    }

    @Override // okhttp3.internal.platform.Platform
    public void log(String message, int level, Throwable t) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        UtilKt.androidLog(level, message, t);
    }

    @Override // okhttp3.internal.platform.Platform
    public Object getStackTraceForCloseable(String closer) {
        Intrinsics.checkParameterIsNotNull(closer, "closer");
        return this.closeGuard.createAndOpen(closer);
    }

    @Override // okhttp3.internal.platform.Platform
    public void logCloseableLeak(String message, Object stackTrace) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        if (!this.closeGuard.warnIfOpen(stackTrace)) {
            Platform.log$default(this, message, 5, null, 4, null);
        }
    }

    @Override // okhttp3.internal.platform.Platform
    public boolean isCleartextTrafficPermitted(String hostname) {
        Intrinsics.checkParameterIsNotNull(hostname, "hostname");
        try {
            Class networkPolicyClass = Class.forName("android.security.NetworkSecurityPolicy");
            Object networkSecurityPolicy = networkPolicyClass.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
            Intrinsics.checkExpressionValueIsNotNull(networkPolicyClass, "networkPolicyClass");
            Intrinsics.checkExpressionValueIsNotNull(networkSecurityPolicy, "networkSecurityPolicy");
            return api24IsCleartextTrafficPermitted(hostname, networkPolicyClass, networkSecurityPolicy);
        } catch (ClassNotFoundException e) {
            return super.isCleartextTrafficPermitted(hostname);
        } catch (IllegalAccessException e2) {
            throw new AssertionError("unable to determine cleartext support", e2);
        } catch (IllegalArgumentException e3) {
            throw new AssertionError("unable to determine cleartext support", e3);
        } catch (NoSuchMethodException e4) {
            return super.isCleartextTrafficPermitted(hostname);
        } catch (InvocationTargetException e5) {
            throw new AssertionError("unable to determine cleartext support", e5);
        }
    }

    private final boolean api24IsCleartextTrafficPermitted(String hostname, Class<?> cls, Object networkSecurityPolicy) throws InvocationTargetException, IllegalAccessException {
        try {
            Object invoke = cls.getMethod("isCleartextTrafficPermitted", String.class).invoke(networkSecurityPolicy, hostname);
            if (invoke != null) {
                return ((Boolean) invoke).booleanValue();
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Boolean");
        } catch (NoSuchMethodException e) {
            return api23IsCleartextTrafficPermitted(hostname, cls, networkSecurityPolicy);
        }
    }

    private final boolean api23IsCleartextTrafficPermitted(String hostname, Class<?> cls, Object networkSecurityPolicy) throws InvocationTargetException, IllegalAccessException {
        try {
            Object invoke = cls.getMethod("isCleartextTrafficPermitted", new Class[0]).invoke(networkSecurityPolicy, new Object[0]);
            if (invoke != null) {
                return ((Boolean) invoke).booleanValue();
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Boolean");
        } catch (NoSuchMethodException e) {
            return super.isCleartextTrafficPermitted(hostname);
        }
    }

    @Override // okhttp3.internal.platform.Platform
    public CertificateChainCleaner buildCertificateChainCleaner(X509TrustManager trustManager) {
        Intrinsics.checkParameterIsNotNull(trustManager, "trustManager");
        AndroidCertificateChainCleaner build = AndroidCertificateChainCleaner.Companion.build(trustManager);
        return build != null ? build : super.buildCertificateChainCleaner(trustManager);
    }

    @Override // okhttp3.internal.platform.Platform
    public TrustRootIndex buildTrustRootIndex(X509TrustManager trustManager) {
        Intrinsics.checkParameterIsNotNull(trustManager, "trustManager");
        try {
            Method method = trustManager.getClass().getDeclaredMethod("findTrustAnchorByIssuerAndSignature", X509Certificate.class);
            Intrinsics.checkExpressionValueIsNotNull(method, FirebaseAnalytics.Param.METHOD);
            method.setAccessible(true);
            return new CustomTrustRootIndex(trustManager, method);
        } catch (NoSuchMethodException e) {
            return super.buildTrustRootIndex(trustManager);
        }
    }

    /* compiled from: AndroidPlatform.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0080\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u0007\u001a\u00020\u0003HÂ\u0003J\t\u0010\b\u001a\u00020\u0005HÂ\u0003J\u001d\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rHÖ\u0003J\u0012\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0016J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lokhttp3/internal/platform/AndroidPlatform$CustomTrustRootIndex;", "Lokhttp3/internal/tls/TrustRootIndex;", "trustManager", "Ljavax/net/ssl/X509TrustManager;", "findByIssuerAndSignatureMethod", "Ljava/lang/reflect/Method;", "(Ljavax/net/ssl/X509TrustManager;Ljava/lang/reflect/Method;)V", "component1", "component2", "copy", "equals", "", "other", "", "findByIssuerAndSignature", "Ljava/security/cert/X509Certificate;", "cert", "hashCode", "", "toString", "", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class CustomTrustRootIndex implements TrustRootIndex {
        private final Method findByIssuerAndSignatureMethod;
        private final X509TrustManager trustManager;

        private final X509TrustManager component1() {
            return this.trustManager;
        }

        private final Method component2() {
            return this.findByIssuerAndSignatureMethod;
        }

        public static /* synthetic */ CustomTrustRootIndex copy$default(CustomTrustRootIndex customTrustRootIndex, X509TrustManager x509TrustManager, Method method, int i, Object obj) {
            if ((i & 1) != 0) {
                x509TrustManager = customTrustRootIndex.trustManager;
            }
            if ((i & 2) != 0) {
                method = customTrustRootIndex.findByIssuerAndSignatureMethod;
            }
            return customTrustRootIndex.copy(x509TrustManager, method);
        }

        public final CustomTrustRootIndex copy(X509TrustManager x509TrustManager, Method method) {
            Intrinsics.checkParameterIsNotNull(x509TrustManager, "trustManager");
            Intrinsics.checkParameterIsNotNull(method, "findByIssuerAndSignatureMethod");
            return new CustomTrustRootIndex(x509TrustManager, method);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof CustomTrustRootIndex)) {
                return false;
            }
            CustomTrustRootIndex customTrustRootIndex = (CustomTrustRootIndex) obj;
            return Intrinsics.areEqual(this.trustManager, customTrustRootIndex.trustManager) && Intrinsics.areEqual(this.findByIssuerAndSignatureMethod, customTrustRootIndex.findByIssuerAndSignatureMethod);
        }

        public int hashCode() {
            X509TrustManager x509TrustManager = this.trustManager;
            int i = 0;
            int hashCode = (x509TrustManager != null ? x509TrustManager.hashCode() : 0) * 31;
            Method method = this.findByIssuerAndSignatureMethod;
            if (method != null) {
                i = method.hashCode();
            }
            return hashCode + i;
        }

        public String toString() {
            return "CustomTrustRootIndex(trustManager=" + this.trustManager + ", findByIssuerAndSignatureMethod=" + this.findByIssuerAndSignatureMethod + ")";
        }

        public CustomTrustRootIndex(X509TrustManager trustManager, Method findByIssuerAndSignatureMethod) {
            Intrinsics.checkParameterIsNotNull(trustManager, "trustManager");
            Intrinsics.checkParameterIsNotNull(findByIssuerAndSignatureMethod, "findByIssuerAndSignatureMethod");
            this.trustManager = trustManager;
            this.findByIssuerAndSignatureMethod = findByIssuerAndSignatureMethod;
        }

        @Override // okhttp3.internal.tls.TrustRootIndex
        public X509Certificate findByIssuerAndSignature(X509Certificate cert) {
            Intrinsics.checkParameterIsNotNull(cert, "cert");
            try {
                Object invoke = this.findByIssuerAndSignatureMethod.invoke(this.trustManager, cert);
                if (invoke != null) {
                    return ((TrustAnchor) invoke).getTrustedCert();
                }
                throw new TypeCastException("null cannot be cast to non-null type java.security.cert.TrustAnchor");
            } catch (IllegalAccessException e) {
                throw new AssertionError("unable to get issues and signature", e);
            } catch (InvocationTargetException e2) {
                return null;
            }
        }
    }

    /* compiled from: AndroidPlatform.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u0004\u0018\u00010\bR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0003\u0010\u0005R\u0011\u0010\u0006\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0005¨\u0006\t"}, d2 = {"Lokhttp3/internal/platform/AndroidPlatform$Companion;", "", "()V", "isAndroid", "", "()Z", "isSupported", "buildIfSupported", "Lokhttp3/internal/platform/Platform;", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        public final boolean isAndroid() {
            return AndroidPlatform.isAndroid;
        }

        public final boolean isSupported() {
            return AndroidPlatform.isSupported;
        }

        public final Platform buildIfSupported() {
            if (isSupported()) {
                return new AndroidPlatform();
            }
            return null;
        }
    }

    static {
        boolean z;
        boolean z2 = true;
        boolean z3 = false;
        try {
            Class.forName("com.android.org.conscrypt.OpenSSLSocketImpl");
        } catch (ClassNotFoundException e) {
            z = false;
        } catch (UnsatisfiedLinkError e2) {
            z = false;
        }
        if (Build.VERSION.SDK_INT > 0) {
            z = true;
            isAndroid = z;
            if (!isAndroid) {
                z2 = false;
            } else {
                if (Build.VERSION.SDK_INT >= 21) {
                    z3 = true;
                }
                if (!z3) {
                    throw new IllegalStateException(("Expected Android API level 21+ but was " + Build.VERSION.SDK_INT).toString());
                }
            }
            isSupported = z2;
            return;
        }
        throw new IllegalStateException("Check failed.".toString());
    }
}
