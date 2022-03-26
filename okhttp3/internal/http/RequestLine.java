package okhttp3.internal.http;

import com.google.android.gms.common.internal.ImagesContract;
import java.net.Proxy;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.HttpUrl;
import okhttp3.Request;
/* compiled from: RequestLine.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u000e\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\r¨\u0006\u000e"}, d2 = {"Lokhttp3/internal/http/RequestLine;", "", "()V", "get", "", "request", "Lokhttp3/Request;", "proxyType", "Ljava/net/Proxy$Type;", "includeAuthorityInRequestLine", "", "requestPath", ImagesContract.URL, "Lokhttp3/HttpUrl;", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class RequestLine {
    public static final RequestLine INSTANCE = new RequestLine();

    private RequestLine() {
    }

    public final String get(Request request, Proxy.Type proxyType) {
        Intrinsics.checkParameterIsNotNull(request, "request");
        Intrinsics.checkParameterIsNotNull(proxyType, "proxyType");
        StringBuilder $this$buildString = new StringBuilder();
        $this$buildString.append(request.method());
        $this$buildString.append(' ');
        if (INSTANCE.includeAuthorityInRequestLine(request, proxyType)) {
            $this$buildString.append(request.url());
        } else {
            $this$buildString.append(INSTANCE.requestPath(request.url()));
        }
        $this$buildString.append(" HTTP/1.1");
        String sb = $this$buildString.toString();
        Intrinsics.checkExpressionValueIsNotNull(sb, "StringBuilder().apply(builderAction).toString()");
        return sb;
    }

    private final boolean includeAuthorityInRequestLine(Request request, Proxy.Type proxyType) {
        return !request.isHttps() && proxyType == Proxy.Type.HTTP;
    }

    public final String requestPath(HttpUrl url) {
        Intrinsics.checkParameterIsNotNull(url, ImagesContract.URL);
        String path = url.encodedPath();
        String query = url.encodedQuery();
        if (query == null) {
            return path;
        }
        return path + '?' + query;
    }
}
