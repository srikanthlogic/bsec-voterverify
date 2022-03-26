package okhttp3.logging;

import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.common.net.HttpHeaders;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.text.StringsKt;
import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
/* compiled from: HttpLoggingInterceptor.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\u0018\u00002\u00020\u0001:\u0002\u001e\u001fB\u0011\b\u0007\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\r\u0010\u000b\u001a\u00020\tH\u0007¢\u0006\u0002\b\u0012J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0018\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0019\u001a\u00020\u001aH\u0002J\u000e\u0010\u001b\u001a\u00020\u00182\u0006\u0010\u001c\u001a\u00020\u0007J\u000e\u0010\u001d\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\tR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R$\u0010\n\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t@GX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\n\u0010\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u0091F0\u0001¨\u0006 "}, d2 = {"Lokhttp3/logging/HttpLoggingInterceptor;", "Lokhttp3/Interceptor;", "logger", "Lokhttp3/logging/HttpLoggingInterceptor$Logger;", "(Lokhttp3/logging/HttpLoggingInterceptor$Logger;)V", "headersToRedact", "", "", "<set-?>", "Lokhttp3/logging/HttpLoggingInterceptor$Level;", FirebaseAnalytics.Param.LEVEL, "getLevel", "()Lokhttp3/logging/HttpLoggingInterceptor$Level;", "(Lokhttp3/logging/HttpLoggingInterceptor$Level;)V", "bodyHasUnknownEncoding", "", "headers", "Lokhttp3/Headers;", "-deprecated_level", "intercept", "Lokhttp3/Response;", "chain", "Lokhttp3/Interceptor$Chain;", "logHeader", "", "i", "", "redactHeader", AppMeasurementSdk.ConditionalUserProperty.NAME, "setLevel", "Level", "Logger", "okhttp-logging-interceptor"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class HttpLoggingInterceptor implements Interceptor {
    private volatile Set<String> headersToRedact;
    private volatile Level level;
    private final Logger logger;

    /* compiled from: HttpLoggingInterceptor.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006¨\u0006\u0007"}, d2 = {"Lokhttp3/logging/HttpLoggingInterceptor$Level;", "", "(Ljava/lang/String;I)V", "NONE", "BASIC", "HEADERS", "BODY", "okhttp-logging-interceptor"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public enum Level {
        NONE,
        BASIC,
        HEADERS,
        BODY
    }

    public HttpLoggingInterceptor() {
        this(null, 1, null);
    }

    public HttpLoggingInterceptor(Logger logger) {
        Intrinsics.checkParameterIsNotNull(logger, "logger");
        this.logger = logger;
        this.headersToRedact = SetsKt.emptySet();
        this.level = Level.NONE;
    }

    public /* synthetic */ HttpLoggingInterceptor(Logger logger, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? Logger.DEFAULT : logger);
    }

    public final Level getLevel() {
        return this.level;
    }

    public final void level(Level level) {
        Intrinsics.checkParameterIsNotNull(level, "<set-?>");
        this.level = level;
    }

    /* compiled from: HttpLoggingInterceptor.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\bf\u0018\u0000 \u00062\u00020\u0001:\u0001\u0006J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u0082\u0002\u0007\n\u0005\b\u0091F0\u0001¨\u0006\u0007"}, d2 = {"Lokhttp3/logging/HttpLoggingInterceptor$Logger;", "", "log", "", "message", "", "Companion", "okhttp-logging-interceptor"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public interface Logger {
        public static final Companion Companion = new Companion(null);
        public static final Logger DEFAULT = new HttpLoggingInterceptor$Logger$Companion$DEFAULT$1();

        void log(String str);

        /* compiled from: HttpLoggingInterceptor.kt */
        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004ø\u0001\u0000¢\u0006\u0002\n\u0000¨\u0006\u0001\u0082\u0002\u0007\n\u0005\b\u0091F0\u0001¨\u0006\u0005"}, d2 = {"Lokhttp3/logging/HttpLoggingInterceptor$Logger$Companion;", "", "()V", "DEFAULT", "Lokhttp3/logging/HttpLoggingInterceptor$Logger;", "okhttp-logging-interceptor"}, k = 1, mv = {1, 1, 16})
        /* loaded from: classes3.dex */
        public static final class Companion {
            static final /* synthetic */ Companion $$INSTANCE;

            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
                this();
            }
        }
    }

    public final void redactHeader(String name) {
        Intrinsics.checkParameterIsNotNull(name, AppMeasurementSdk.ConditionalUserProperty.NAME);
        TreeSet newHeadersToRedact = new TreeSet(StringsKt.getCASE_INSENSITIVE_ORDER(StringCompanionObject.INSTANCE));
        CollectionsKt.addAll(newHeadersToRedact, this.headersToRedact);
        newHeadersToRedact.add(name);
        this.headersToRedact = newHeadersToRedact;
    }

    public final HttpLoggingInterceptor setLevel(Level level) {
        Intrinsics.checkParameterIsNotNull(level, FirebaseAnalytics.Param.LEVEL);
        this.level = level;
        return this;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to var", replaceWith = @ReplaceWith(expression = "level", imports = {}))
    /* renamed from: -deprecated_level */
    public final Level m1152deprecated_level() {
        return this.level;
    }

    @Override // okhttp3.Interceptor
    public Response intercept(Interceptor.Chain chain) throws IOException {
        String str;
        String requestStartMessage;
        String str2;
        String str3;
        long contentLength;
        char c;
        String str4;
        String str5;
        Charset charset;
        Connection connection;
        Charset charset2;
        Intrinsics.checkParameterIsNotNull(chain, "chain");
        Level level = this.level;
        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }
        boolean logBody = level == Level.BODY;
        boolean logHeaders = logBody || level == Level.HEADERS;
        RequestBody requestBody = request.body();
        Connection connection2 = chain.connection();
        StringBuilder sb = new StringBuilder();
        sb.append("--> ");
        sb.append(request.method());
        sb.append(' ');
        sb.append(request.url());
        sb.append(connection2 != null ? " " + connection2.protocol() : "");
        String requestStartMessage2 = sb.toString();
        if (logHeaders || requestBody == null) {
            str = "";
            requestStartMessage = requestStartMessage2;
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(requestStartMessage2);
            sb2.append(" (");
            str = "";
            sb2.append(requestBody.contentLength());
            sb2.append("-byte body)");
            requestStartMessage = sb2.toString();
        }
        this.logger.log(requestStartMessage);
        if (logHeaders) {
            Headers headers = request.headers();
            if (requestBody != null) {
                MediaType it = requestBody.contentType();
                if (it != null && headers.get(HttpHeaders.CONTENT_TYPE) == null) {
                    this.logger.log("Content-Type: " + it);
                }
                if (requestBody.contentLength() == -1) {
                    connection = connection2;
                } else if (headers.get(HttpHeaders.CONTENT_LENGTH) == null) {
                    Logger logger = this.logger;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Content-Length: ");
                    connection = connection2;
                    sb3.append(requestBody.contentLength());
                    logger.log(sb3.toString());
                } else {
                    connection = connection2;
                }
            } else {
                connection = connection2;
            }
            int size = headers.size();
            for (int i = 0; i < size; i++) {
                logHeader(headers, i);
            }
            if (!logBody) {
                str2 = str;
                str3 = "UTF_8";
            } else if (requestBody == null) {
                str2 = str;
                str3 = "UTF_8";
            } else if (bodyHasUnknownEncoding(request.headers())) {
                this.logger.log("--> END " + request.method() + " (encoded body omitted)");
                str3 = "UTF_8";
                str2 = str;
            } else if (requestBody.isDuplex()) {
                this.logger.log("--> END " + request.method() + " (duplex request body omitted)");
                str3 = "UTF_8";
                str2 = str;
            } else if (requestBody.isOneShot()) {
                this.logger.log("--> END " + request.method() + " (one-shot body omitted)");
                str3 = "UTF_8";
                str2 = str;
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                MediaType contentType = requestBody.contentType();
                if (contentType == null || (charset2 = contentType.charset(StandardCharsets.UTF_8)) == null) {
                    charset2 = StandardCharsets.UTF_8;
                    Intrinsics.checkExpressionValueIsNotNull(charset2, "UTF_8");
                }
                this.logger.log(str);
                if (Utf8Kt.isProbablyUtf8(buffer)) {
                    this.logger.log(buffer.readString(charset2));
                    Logger logger2 = this.logger;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("--> END ");
                    sb4.append(request.method());
                    sb4.append(" (");
                    str2 = str;
                    sb4.append(requestBody.contentLength());
                    sb4.append("-byte body)");
                    logger2.log(sb4.toString());
                    str3 = "UTF_8";
                } else {
                    str2 = str;
                    Logger logger3 = this.logger;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("--> END ");
                    sb5.append(request.method());
                    sb5.append(" (binary ");
                    str3 = "UTF_8";
                    sb5.append(requestBody.contentLength());
                    sb5.append("-byte body omitted)");
                    logger3.log(sb5.toString());
                }
            }
            this.logger.log("--> END " + request.method());
        } else {
            str3 = "UTF_8";
            str2 = str;
        }
        long startNs = System.nanoTime();
        try {
            Response response = chain.proceed(request);
            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                Intrinsics.throwNpe();
            }
            long contentLength2 = responseBody.contentLength();
            String bodySize = contentLength2 != -1 ? contentLength2 + "-byte" : "unknown-length";
            Logger logger4 = this.logger;
            StringBuilder sb6 = new StringBuilder();
            sb6.append("<-- ");
            sb6.append(response.code());
            if (response.message().length() == 0) {
                contentLength = contentLength2;
                str4 = "-byte body)";
                str5 = str2;
                c = ' ';
            } else {
                String message = response.message();
                str4 = "-byte body)";
                StringBuilder sb7 = new StringBuilder();
                contentLength = contentLength2;
                c = ' ';
                sb7.append(String.valueOf(' '));
                sb7.append(message);
                str5 = sb7.toString();
            }
            sb6.append(str5);
            sb6.append(c);
            sb6.append(response.request().url());
            sb6.append(" (");
            sb6.append(tookMs);
            sb6.append("ms");
            sb6.append(!logHeaders ? ", " + bodySize + " body" : str2);
            sb6.append(')');
            logger4.log(sb6.toString());
            if (logHeaders) {
                Headers headers2 = response.headers();
                int size2 = headers2.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    logHeader(headers2, i2);
                }
                if (logBody && okhttp3.internal.http.HttpHeaders.promisesBody(response)) {
                    if (bodyHasUnknownEncoding(response.headers())) {
                        this.logger.log("<-- END HTTP (encoded body omitted)");
                    } else {
                        BufferedSource source = responseBody.source();
                        source.request(Long.MAX_VALUE);
                        Buffer buffer2 = source.getBuffer();
                        Long gzippedLength = null;
                        if (StringsKt.equals("gzip", headers2.get(HttpHeaders.CONTENT_ENCODING), true)) {
                            gzippedLength = Long.valueOf(buffer2.size());
                            GzipSource gzipSource = new GzipSource(buffer2.clone());
                            Throwable th = null;
                            try {
                                GzipSource gzippedResponseBody = gzipSource;
                                buffer2 = new Buffer();
                                try {
                                    buffer2.writeAll(gzippedResponseBody);
                                } catch (Throwable th2) {
                                    th = th2;
                                    try {
                                        throw th;
                                    } finally {
                                        CloseableKt.closeFinally(gzipSource, th);
                                    }
                                }
                            } catch (Throwable th3) {
                                th = th3;
                            }
                        }
                        MediaType contentType2 = responseBody.contentType();
                        if (contentType2 == null || (charset = contentType2.charset(StandardCharsets.UTF_8)) == null) {
                            charset = StandardCharsets.UTF_8;
                            Intrinsics.checkExpressionValueIsNotNull(charset, str3);
                        }
                        if (!Utf8Kt.isProbablyUtf8(buffer2)) {
                            this.logger.log(str2);
                            this.logger.log("<-- END HTTP (binary " + buffer2.size() + "-byte body omitted)");
                            return response;
                        }
                        if (contentLength != 0) {
                            this.logger.log(str2);
                            this.logger.log(buffer2.clone().readString(charset));
                        }
                        if (gzippedLength != null) {
                            this.logger.log("<-- END HTTP (" + buffer2.size() + "-byte, " + gzippedLength + "-gzipped-byte body)");
                        } else {
                            this.logger.log("<-- END HTTP (" + buffer2.size() + str4);
                        }
                    }
                }
                this.logger.log("<-- END HTTP");
            }
            return response;
        } catch (Exception e) {
            this.logger.log("<-- HTTP FAILED: " + e);
            throw e;
        }
    }

    private final void logHeader(Headers headers, int i) {
        String value = this.headersToRedact.contains(headers.name(i)) ? "██" : headers.value(i);
        Logger logger = this.logger;
        logger.log(headers.name(i) + ": " + value);
    }

    private final boolean bodyHasUnknownEncoding(Headers headers) {
        String contentEncoding = headers.get(HttpHeaders.CONTENT_ENCODING);
        if (contentEncoding == null || StringsKt.equals(contentEncoding, "identity", true) || StringsKt.equals(contentEncoding, "gzip", true)) {
            return false;
        }
        return true;
    }
}
