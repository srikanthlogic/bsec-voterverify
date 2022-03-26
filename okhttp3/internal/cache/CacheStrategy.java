package okhttp3.internal.cache;

import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.google.common.net.HttpHeaders;
import com.sec.biometric.license.SecBiometricLicenseManager;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.http.DatesKt;
/* compiled from: CacheStrategy.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 \u000b2\u00020\u0001:\u0002\u000b\fB\u001b\b\u0000\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\r"}, d2 = {"Lokhttp3/internal/cache/CacheStrategy;", "", "networkRequest", "Lokhttp3/Request;", "cacheResponse", "Lokhttp3/Response;", "(Lokhttp3/Request;Lokhttp3/Response;)V", "getCacheResponse", "()Lokhttp3/Response;", "getNetworkRequest", "()Lokhttp3/Request;", "Companion", "Factory", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class CacheStrategy {
    public static final Companion Companion = new Companion(null);
    private final Response cacheResponse;
    private final Request networkRequest;

    public CacheStrategy(Request networkRequest, Response cacheResponse) {
        this.networkRequest = networkRequest;
        this.cacheResponse = cacheResponse;
    }

    public final Request getNetworkRequest() {
        return this.networkRequest;
    }

    public final Response getCacheResponse() {
        return this.cacheResponse;
    }

    /* compiled from: CacheStrategy.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bJ\b\u0010\u0017\u001a\u00020\u0003H\u0002J\u0006\u0010\u0018\u001a\u00020\u0019J\b\u0010\u001a\u001a\u00020\u0019H\u0002J\b\u0010\u001b\u001a\u00020\u0003H\u0002J\u0010\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u0004\u001a\u00020\u0005H\u0002J\b\u0010\u001e\u001a\u00020\u001dH\u0002R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\u00020\u0005X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0014\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Lokhttp3/internal/cache/CacheStrategy$Factory;", "", "nowMillis", "", "request", "Lokhttp3/Request;", "cacheResponse", "Lokhttp3/Response;", "(JLokhttp3/Request;Lokhttp3/Response;)V", "ageSeconds", "", "etag", "", "expires", "Ljava/util/Date;", "lastModified", "lastModifiedString", "receivedResponseMillis", "getRequest$okhttp", "()Lokhttp3/Request;", "sentRequestMillis", "servedDate", "servedDateString", "cacheResponseAge", "compute", "Lokhttp3/internal/cache/CacheStrategy;", "computeCandidate", "computeFreshnessLifetime", "hasConditions", "", "isFreshnessLifetimeHeuristic", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Factory {
        private int ageSeconds;
        private final Response cacheResponse;
        private String etag;
        private Date expires;
        private Date lastModified;
        private String lastModifiedString;
        private final long nowMillis;
        private long receivedResponseMillis;
        private final Request request;
        private long sentRequestMillis;
        private Date servedDate;
        private String servedDateString;

        public Factory(long nowMillis, Request request, Response cacheResponse) {
            Intrinsics.checkParameterIsNotNull(request, "request");
            this.nowMillis = nowMillis;
            this.request = request;
            this.cacheResponse = cacheResponse;
            this.ageSeconds = -1;
            Response response = this.cacheResponse;
            if (response != null) {
                this.sentRequestMillis = response.sentRequestAtMillis();
                this.receivedResponseMillis = this.cacheResponse.receivedResponseAtMillis();
                Headers headers = this.cacheResponse.headers();
                int size = headers.size();
                for (int i = 0; i < size; i++) {
                    String fieldName = headers.name(i);
                    String value = headers.value(i);
                    if (StringsKt.equals(fieldName, HttpHeaders.DATE, true)) {
                        this.servedDate = DatesKt.toHttpDateOrNull(value);
                        this.servedDateString = value;
                    } else if (StringsKt.equals(fieldName, HttpHeaders.EXPIRES, true)) {
                        this.expires = DatesKt.toHttpDateOrNull(value);
                    } else if (StringsKt.equals(fieldName, HttpHeaders.LAST_MODIFIED, true)) {
                        this.lastModified = DatesKt.toHttpDateOrNull(value);
                        this.lastModifiedString = value;
                    } else if (StringsKt.equals(fieldName, HttpHeaders.ETAG, true)) {
                        this.etag = value;
                    } else if (StringsKt.equals(fieldName, HttpHeaders.AGE, true)) {
                        this.ageSeconds = Util.toNonNegativeInt(value, -1);
                    }
                }
            }
        }

        public final Request getRequest$okhttp() {
            return this.request;
        }

        private final boolean isFreshnessLifetimeHeuristic() {
            Response response = this.cacheResponse;
            if (response == null) {
                Intrinsics.throwNpe();
            }
            return response.cacheControl().maxAgeSeconds() == -1 && this.expires == null;
        }

        public final CacheStrategy compute() {
            CacheStrategy candidate = computeCandidate();
            if (candidate.getNetworkRequest() == null || !this.request.cacheControl().onlyIfCached()) {
                return candidate;
            }
            return new CacheStrategy(null, null);
        }

        private final CacheStrategy computeCandidate() {
            String conditionName;
            String conditionName2;
            if (this.cacheResponse == null) {
                return new CacheStrategy(this.request, null);
            }
            if (this.request.isHttps() && this.cacheResponse.handshake() == null) {
                return new CacheStrategy(this.request, null);
            }
            if (!CacheStrategy.Companion.isCacheable(this.cacheResponse, this.request)) {
                return new CacheStrategy(this.request, null);
            }
            CacheControl requestCaching = this.request.cacheControl();
            if (!requestCaching.noCache() && !hasConditions(this.request)) {
                CacheControl responseCaching = this.cacheResponse.cacheControl();
                long ageMillis = cacheResponseAge();
                long freshMillis = computeFreshnessLifetime();
                if (requestCaching.maxAgeSeconds() != -1) {
                    freshMillis = Math.min(freshMillis, TimeUnit.SECONDS.toMillis((long) requestCaching.maxAgeSeconds()));
                }
                long minFreshMillis = 0;
                if (requestCaching.minFreshSeconds() != -1) {
                    minFreshMillis = TimeUnit.SECONDS.toMillis((long) requestCaching.minFreshSeconds());
                }
                long maxStaleMillis = 0;
                if (!responseCaching.mustRevalidate() && requestCaching.maxStaleSeconds() != -1) {
                    maxStaleMillis = TimeUnit.SECONDS.toMillis((long) requestCaching.maxStaleSeconds());
                }
                if (responseCaching.noCache() || ageMillis + minFreshMillis >= freshMillis + maxStaleMillis) {
                    if (this.etag != null) {
                        conditionName2 = HttpHeaders.IF_NONE_MATCH;
                        conditionName = this.etag;
                    } else if (this.lastModified != null) {
                        conditionName2 = HttpHeaders.IF_MODIFIED_SINCE;
                        conditionName = this.lastModifiedString;
                    } else if (this.servedDate == null) {
                        return new CacheStrategy(this.request, null);
                    } else {
                        conditionName2 = HttpHeaders.IF_MODIFIED_SINCE;
                        conditionName = this.servedDateString;
                    }
                    Headers.Builder conditionalRequestHeaders = this.request.headers().newBuilder();
                    if (conditionName == null) {
                        Intrinsics.throwNpe();
                    }
                    conditionalRequestHeaders.addLenient$okhttp(conditionName2, conditionName);
                    return new CacheStrategy(this.request.newBuilder().headers(conditionalRequestHeaders.build()).build(), this.cacheResponse);
                }
                Response.Builder builder = this.cacheResponse.newBuilder();
                if (ageMillis + minFreshMillis >= freshMillis) {
                    builder.addHeader(HttpHeaders.WARNING, "110 HttpURLConnection \"Response is stale\"");
                }
                if (ageMillis > 86400000 && isFreshnessLifetimeHeuristic()) {
                    builder.addHeader(HttpHeaders.WARNING, "113 HttpURLConnection \"Heuristic expiration\"");
                }
                return new CacheStrategy(null, builder.build());
            }
            return new CacheStrategy(this.request, null);
        }

        private final long computeFreshnessLifetime() {
            Response response = this.cacheResponse;
            if (response == null) {
                Intrinsics.throwNpe();
            }
            CacheControl responseCaching = response.cacheControl();
            if (responseCaching.maxAgeSeconds() != -1) {
                return TimeUnit.SECONDS.toMillis((long) responseCaching.maxAgeSeconds());
            }
            Date expires = this.expires;
            if (expires != null) {
                Date date = this.servedDate;
                long delta = expires.getTime() - (date != null ? date.getTime() : this.receivedResponseMillis);
                if (delta > 0) {
                    return delta;
                }
                return 0;
            } else if (this.lastModified == null || this.cacheResponse.request().url().query() != null) {
                return 0;
            } else {
                Date date2 = this.servedDate;
                long servedMillis = date2 != null ? date2.getTime() : this.sentRequestMillis;
                Date date3 = this.lastModified;
                if (date3 == null) {
                    Intrinsics.throwNpe();
                }
                long delta2 = servedMillis - date3.getTime();
                if (delta2 > 0) {
                    return delta2 / ((long) 10);
                }
                return 0;
            }
        }

        private final long cacheResponseAge() {
            long receivedAge;
            Date servedDate = this.servedDate;
            long apparentReceivedAge = 0;
            if (servedDate != null) {
                apparentReceivedAge = Math.max(0L, this.receivedResponseMillis - servedDate.getTime());
            }
            if (this.ageSeconds != -1) {
                receivedAge = Math.max(apparentReceivedAge, TimeUnit.SECONDS.toMillis((long) this.ageSeconds));
            } else {
                receivedAge = apparentReceivedAge;
            }
            long j = this.receivedResponseMillis;
            return receivedAge + (j - this.sentRequestMillis) + (this.nowMillis - j);
        }

        private final boolean hasConditions(Request request) {
            return (request.header(HttpHeaders.IF_MODIFIED_SINCE) == null && request.header(HttpHeaders.IF_NONE_MATCH) == null) ? false : true;
        }
    }

    /* compiled from: CacheStrategy.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b¨\u0006\t"}, d2 = {"Lokhttp3/internal/cache/CacheStrategy$Companion;", "", "()V", "isCacheable", "", "response", "Lokhttp3/Response;", "request", "Lokhttp3/Request;", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        public final boolean isCacheable(Response response, Request request) {
            Intrinsics.checkParameterIsNotNull(response, "response");
            Intrinsics.checkParameterIsNotNull(request, "request");
            int code = response.code();
            if (!(code == 200 || code == 410 || code == 414 || code == 501 || code == 203 || code == 204)) {
                if (code != 307) {
                    if (!(code == 308 || code == 404 || code == 405)) {
                        switch (code) {
                            case GenericDraweeHierarchyBuilder.DEFAULT_FADE_DURATION:
                            case SecBiometricLicenseManager.ERROR_INTERNAL:
                                break;
                            case 302:
                                break;
                            default:
                                return false;
                        }
                    }
                }
                if (Response.header$default(response, HttpHeaders.EXPIRES, null, 2, null) == null && response.cacheControl().maxAgeSeconds() == -1 && !response.cacheControl().isPublic() && !response.cacheControl().isPrivate()) {
                    return false;
                }
            }
            if (response.cacheControl().noStore() || request.cacheControl().noStore()) {
                return false;
            }
            return true;
        }
    }
}
