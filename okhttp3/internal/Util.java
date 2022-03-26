package okhttp3.internal;

import android.support.v4.media.session.PlaybackStateCompat;
import androidx.core.app.NotificationCompat;
import androidx.exifinterface.media.ExifInterface;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.common.net.HttpHeaders;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.ranges.RangesKt;
import kotlin.text.Charsets;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http2.Header;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Options;
import okio.Source;
/* compiled from: Util.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0086\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010!\n\u0002\b\u0003\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\n\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\f\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010$\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a \u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018\u001a\u001e\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u00162\u0006\u0010\u001c\u001a\u00020\u00162\u0006\u0010\u001d\u001a\u00020\u0016\u001a'\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u001e\u001a\u00020\u00112\u0012\u0010\u001f\u001a\n\u0012\u0006\b\u0001\u0012\u00020!0 \"\u00020!¢\u0006\u0002\u0010\"\u001a\u0017\u0010#\u001a\u00020\u001a2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u001a0%H\u0086\b\u001a-\u0010&\u001a\b\u0012\u0004\u0012\u0002H(0'\"\u0004\b\u0000\u0010(2\u0012\u0010)\u001a\n\u0012\u0006\b\u0001\u0012\u0002H(0 \"\u0002H(H\u0007¢\u0006\u0002\u0010*\u001a1\u0010+\u001a\u0004\u0018\u0001H(\"\u0004\b\u0000\u0010(2\u0006\u0010,\u001a\u00020!2\f\u0010-\u001a\b\u0012\u0004\u0012\u0002H(0.2\u0006\u0010/\u001a\u00020\u0011¢\u0006\u0002\u00100\u001a\u0016\u00101\u001a\u0002022\u0006\u0010\u0014\u001a\u00020\u00112\u0006\u00103\u001a\u00020\u000f\u001a\u001f\u00104\u001a\u00020\u001a2\u0006\u0010\u0014\u001a\u00020\u00112\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u001a0%H\u0086\b\u001a%\u00105\u001a\u00020\u001a\"\u0004\b\u0000\u00106*\b\u0012\u0004\u0012\u0002H6072\u0006\u00108\u001a\u0002H6H\u0000¢\u0006\u0002\u00109\u001a\u0015\u0010:\u001a\u00020\u0013*\u00020;2\u0006\u0010<\u001a\u00020\u0013H\u0086\u0004\u001a\u0015\u0010:\u001a\u00020\u0016*\u00020\u00132\u0006\u0010<\u001a\u00020\u0016H\u0086\u0004\u001a\u0015\u0010:\u001a\u00020\u0013*\u00020=2\u0006\u0010<\u001a\u00020\u0013H\u0086\u0004\u001a\n\u0010>\u001a\u00020?*\u00020@\u001a\r\u0010A\u001a\u00020\u001a*\u00020!H\u0080\b\u001a\r\u0010B\u001a\u00020\u001a*\u00020!H\u0080\b\u001a\n\u0010C\u001a\u00020\u000f*\u00020\u0011\u001a\u0012\u0010D\u001a\u00020\u000f*\u00020E2\u0006\u0010F\u001a\u00020E\u001a\n\u0010G\u001a\u00020\u001a*\u00020H\u001a\n\u0010G\u001a\u00020\u001a*\u00020I\u001a\n\u0010G\u001a\u00020\u001a*\u00020J\u001a#\u0010K\u001a\b\u0012\u0004\u0012\u00020\u00110 *\b\u0012\u0004\u0012\u00020\u00110 2\u0006\u0010L\u001a\u00020\u0011¢\u0006\u0002\u0010M\u001a&\u0010N\u001a\u00020\u0013*\u00020\u00112\u0006\u0010O\u001a\u00020P2\b\b\u0002\u0010Q\u001a\u00020\u00132\b\b\u0002\u0010R\u001a\u00020\u0013\u001a&\u0010N\u001a\u00020\u0013*\u00020\u00112\u0006\u0010S\u001a\u00020\u00112\b\b\u0002\u0010Q\u001a\u00020\u00132\b\b\u0002\u0010R\u001a\u00020\u0013\u001a\u001a\u0010T\u001a\u00020\u000f*\u00020U2\u0006\u0010V\u001a\u00020\u00132\u0006\u0010W\u001a\u00020\u0018\u001a5\u0010X\u001a\u00020\u000f*\b\u0012\u0004\u0012\u00020\u00110 2\u000e\u0010F\u001a\n\u0012\u0004\u0012\u00020\u0011\u0018\u00010 2\u000e\u0010Y\u001a\n\u0012\u0006\b\u0000\u0012\u00020\u00110Z¢\u0006\u0002\u0010[\u001a\n\u0010\\\u001a\u00020\u0016*\u00020]\u001a+\u0010^\u001a\u00020\u0013*\b\u0012\u0004\u0012\u00020\u00110 2\u0006\u0010L\u001a\u00020\u00112\f\u0010Y\u001a\b\u0012\u0004\u0012\u00020\u00110Z¢\u0006\u0002\u0010_\u001a\n\u0010`\u001a\u00020\u0013*\u00020\u0011\u001a\u001e\u0010a\u001a\u00020\u0013*\u00020\u00112\b\b\u0002\u0010Q\u001a\u00020\u00132\b\b\u0002\u0010R\u001a\u00020\u0013\u001a\u001e\u0010b\u001a\u00020\u0013*\u00020\u00112\b\b\u0002\u0010Q\u001a\u00020\u00132\b\b\u0002\u0010R\u001a\u00020\u0013\u001a\u0014\u0010c\u001a\u00020\u0013*\u00020\u00112\b\b\u0002\u0010Q\u001a\u00020\u0013\u001a9\u0010d\u001a\b\u0012\u0004\u0012\u00020\u00110 *\b\u0012\u0004\u0012\u00020\u00110 2\f\u0010F\u001a\b\u0012\u0004\u0012\u00020\u00110 2\u000e\u0010Y\u001a\n\u0012\u0006\b\u0000\u0012\u00020\u00110Z¢\u0006\u0002\u0010e\u001a\u0012\u0010f\u001a\u00020\u000f*\u00020J2\u0006\u0010g\u001a\u00020h\u001a\r\u0010i\u001a\u00020\u001a*\u00020!H\u0086\b\u001a\r\u0010j\u001a\u00020\u001a*\u00020!H\u0086\b\u001a\n\u0010k\u001a\u00020\u0013*\u00020P\u001a\n\u0010l\u001a\u00020\u0011*\u00020J\u001a\u0012\u0010m\u001a\u00020n*\u00020h2\u0006\u0010o\u001a\u00020n\u001a\n\u0010p\u001a\u00020\u0013*\u00020h\u001a\u0012\u0010q\u001a\u00020\u0013*\u00020r2\u0006\u0010s\u001a\u00020;\u001a\u001a\u0010q\u001a\u00020\u000f*\u00020U2\u0006\u0010\u0015\u001a\u00020\u00132\u0006\u0010W\u001a\u00020\u0018\u001a\u0010\u0010t\u001a\b\u0012\u0004\u0012\u00020u0'*\u00020\u0003\u001a\u0010\u0010v\u001a\u00020\u0003*\b\u0012\u0004\u0012\u00020u0'\u001a\n\u0010w\u001a\u00020\u0011*\u00020\u0013\u001a\n\u0010w\u001a\u00020\u0011*\u00020\u0016\u001a\u0014\u0010x\u001a\u00020\u0011*\u00020E2\b\b\u0002\u0010y\u001a\u00020\u000f\u001a\u001c\u0010z\u001a\b\u0012\u0004\u0012\u0002H(0'\"\u0004\b\u0000\u0010(*\b\u0012\u0004\u0012\u0002H(0'\u001a.\u0010{\u001a\u000e\u0012\u0004\u0012\u0002H}\u0012\u0004\u0012\u0002H~0|\"\u0004\b\u0000\u0010}\"\u0004\b\u0001\u0010~*\u000e\u0012\u0004\u0012\u0002H}\u0012\u0004\u0012\u0002H~0|\u001a\u0013\u0010\u007f\u001a\u00020\u0016*\u00020\u00112\u0007\u0010\u0080\u0001\u001a\u00020\u0016\u001a\u0016\u0010\u0081\u0001\u001a\u00020\u0013*\u0004\u0018\u00010\u00112\u0007\u0010\u0080\u0001\u001a\u00020\u0013\u001a\u001f\u0010\u0082\u0001\u001a\u00020\u0011*\u00020\u00112\b\b\u0002\u0010Q\u001a\u00020\u00132\b\b\u0002\u0010R\u001a\u00020\u0013\u001a\u000e\u0010\u0083\u0001\u001a\u00020\u001a*\u00020!H\u0086\b\u001a\u0015\u0010\u0084\u0001\u001a\u00020\u001a*\u00030\u0085\u00012\u0007\u0010\u0086\u0001\u001a\u00020\u0013\"\u0010\u0010\u0000\u001a\u00020\u00018\u0006X\u0087\u0004¢\u0006\u0002\n\u0000\"\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000\"\u0010\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004¢\u0006\u0002\n\u0000\"\u0010\u0010\u0006\u001a\u00020\u00078\u0006X\u0087\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000\"\u0010\u0010\n\u001a\u00020\u000b8\u0006X\u0087\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000\"\u0010\u0010\u000e\u001a\u00020\u000f8\u0000X\u0081\u0004¢\u0006\u0002\n\u0000\"\u0010\u0010\u0010\u001a\u00020\u00118\u0000X\u0081\u0004¢\u0006\u0002\n\u0000¨\u0006\u0087\u0001"}, d2 = {"EMPTY_BYTE_ARRAY", "", "EMPTY_HEADERS", "Lokhttp3/Headers;", "EMPTY_REQUEST", "Lokhttp3/RequestBody;", "EMPTY_RESPONSE", "Lokhttp3/ResponseBody;", "UNICODE_BOMS", "Lokio/Options;", "UTC", "Ljava/util/TimeZone;", "VERIFY_AS_IP_ADDRESS", "Lkotlin/text/Regex;", "assertionsEnabled", "", "okHttpName", "", "checkDuration", "", AppMeasurementSdk.ConditionalUserProperty.NAME, "duration", "", "unit", "Ljava/util/concurrent/TimeUnit;", "checkOffsetAndCount", "", "arrayLength", "offset", "count", "format", "args", "", "", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "ignoreIoExceptions", "block", "Lkotlin/Function0;", "immutableListOf", "", ExifInterface.GPS_DIRECTION_TRUE, "elements", "([Ljava/lang/Object;)Ljava/util/List;", "readFieldOrNull", "instance", "fieldType", "Ljava/lang/Class;", "fieldName", "(Ljava/lang/Object;Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Object;", "threadFactory", "Ljava/util/concurrent/ThreadFactory;", "daemon", "threadName", "addIfAbsent", ExifInterface.LONGITUDE_EAST, "", "element", "(Ljava/util/List;Ljava/lang/Object;)V", "and", "", "mask", "", "asFactory", "Lokhttp3/EventListener$Factory;", "Lokhttp3/EventListener;", "assertThreadDoesntHoldLock", "assertThreadHoldsLock", "canParseAsIpAddress", "canReuseConnectionFor", "Lokhttp3/HttpUrl;", "other", "closeQuietly", "Ljava/io/Closeable;", "Ljava/net/ServerSocket;", "Ljava/net/Socket;", "concat", "value", "([Ljava/lang/String;Ljava/lang/String;)[Ljava/lang/String;", "delimiterOffset", "delimiter", "", "startIndex", "endIndex", "delimiters", "discard", "Lokio/Source;", "timeout", "timeUnit", "hasIntersection", "comparator", "Ljava/util/Comparator;", "([Ljava/lang/String;[Ljava/lang/String;Ljava/util/Comparator;)Z", "headersContentLength", "Lokhttp3/Response;", "indexOf", "([Ljava/lang/String;Ljava/lang/String;Ljava/util/Comparator;)I", "indexOfControlOrNonAscii", "indexOfFirstNonAsciiWhitespace", "indexOfLastNonAsciiWhitespace", "indexOfNonWhitespace", "intersect", "([Ljava/lang/String;[Ljava/lang/String;Ljava/util/Comparator;)[Ljava/lang/String;", "isHealthy", FirebaseAnalytics.Param.SOURCE, "Lokio/BufferedSource;", "notify", "notifyAll", "parseHexDigit", "peerName", "readBomAsCharset", "Ljava/nio/charset/Charset;", "default", "readMedium", "skipAll", "Lokio/Buffer;", "b", "toHeaderList", "Lokhttp3/internal/http2/Header;", "toHeaders", "toHexString", "toHostHeader", "includeDefaultPort", "toImmutableList", "toImmutableMap", "", "K", ExifInterface.GPS_MEASUREMENT_INTERRUPTED, "toLongOrDefault", "defaultValue", "toNonNegativeInt", "trimSubstring", "wait", "writeMedium", "Lokio/BufferedSink;", FirebaseAnalytics.Param.MEDIUM, "okhttp"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class Util {
    public static final TimeZone UTC;
    private static final Regex VERIFY_AS_IP_ADDRESS;
    public static final boolean assertionsEnabled;
    public static final String okHttpName;
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final Headers EMPTY_HEADERS = Headers.Companion.of(new String[0]);
    public static final ResponseBody EMPTY_RESPONSE = ResponseBody.Companion.create$default(ResponseBody.Companion, EMPTY_BYTE_ARRAY, (MediaType) null, 1, (Object) null);
    public static final RequestBody EMPTY_REQUEST = RequestBody.Companion.create$default(RequestBody.Companion, EMPTY_BYTE_ARRAY, (MediaType) null, 0, 0, 7, (Object) null);
    private static final Options UNICODE_BOMS = Options.Companion.of(ByteString.Companion.decodeHex("efbbbf"), ByteString.Companion.decodeHex("feff"), ByteString.Companion.decodeHex("fffe"), ByteString.Companion.decodeHex("0000ffff"), ByteString.Companion.decodeHex("ffff0000"));

    static {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        if (timeZone == null) {
            Intrinsics.throwNpe();
        }
        UTC = timeZone;
        VERIFY_AS_IP_ADDRESS = new Regex("([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)");
        assertionsEnabled = OkHttpClient.class.desiredAssertionStatus();
        String name = OkHttpClient.class.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, "OkHttpClient::class.java.name");
        okHttpName = StringsKt.removeSuffix(StringsKt.removePrefix(name, (CharSequence) "okhttp3."), (CharSequence) "Client");
    }

    public static final void checkOffsetAndCount(long arrayLength, long offset, long count) {
        if ((offset | count) < 0 || offset > arrayLength || arrayLength - offset < count) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public static final ThreadFactory threadFactory(String name, boolean daemon) {
        Intrinsics.checkParameterIsNotNull(name, AppMeasurementSdk.ConditionalUserProperty.NAME);
        return new ThreadFactory(name, daemon) { // from class: okhttp3.internal.Util$threadFactory$1
            final /* synthetic */ boolean $daemon;
            final /* synthetic */ String $name;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$name = r1;
                this.$daemon = r2;
            }

            @Override // java.util.concurrent.ThreadFactory
            public final Thread newThread(Runnable runnable) {
                Thread $this$apply = new Thread(runnable, this.$name);
                $this$apply.setDaemon(this.$daemon);
                return $this$apply;
            }
        };
    }

    public static final String[] intersect(String[] $this$intersect, String[] other, Comparator<? super String> comparator) {
        Intrinsics.checkParameterIsNotNull($this$intersect, "$this$intersect");
        Intrinsics.checkParameterIsNotNull(other, "other");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        List result = new ArrayList();
        for (String a2 : $this$intersect) {
            int length = other.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (comparator.compare(a2, other[i]) == 0) {
                    result.add(a2);
                    break;
                } else {
                    i++;
                }
            }
        }
        Object[] array = result.toArray(new String[0]);
        if (array != null) {
            return (String[]) array;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
    }

    public static final boolean hasIntersection(String[] $this$hasIntersection, String[] other, Comparator<? super String> comparator) {
        Intrinsics.checkParameterIsNotNull($this$hasIntersection, "$this$hasIntersection");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        if (!($this$hasIntersection.length == 0) && other != null) {
            if (!(other.length == 0)) {
                for (String a2 : $this$hasIntersection) {
                    for (String b : other) {
                        if (comparator.compare(a2, b) == 0) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }
        return false;
    }

    public static /* synthetic */ String toHostHeader$default(HttpUrl httpUrl, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        return toHostHeader(httpUrl, z);
    }

    public static final String toHostHeader(HttpUrl $this$toHostHeader, boolean includeDefaultPort) {
        String host;
        Intrinsics.checkParameterIsNotNull($this$toHostHeader, "$this$toHostHeader");
        if (StringsKt.contains$default((CharSequence) $this$toHostHeader.host(), (CharSequence) ":", false, 2, (Object) null)) {
            host = '[' + $this$toHostHeader.host() + ']';
        } else {
            host = $this$toHostHeader.host();
        }
        if (!includeDefaultPort && $this$toHostHeader.port() == HttpUrl.Companion.defaultPort($this$toHostHeader.scheme())) {
            return host;
        }
        return host + ':' + $this$toHostHeader.port();
    }

    public static final int indexOf(String[] $this$indexOf, String value, Comparator<String> comparator) {
        Intrinsics.checkParameterIsNotNull($this$indexOf, "$this$indexOf");
        Intrinsics.checkParameterIsNotNull(value, "value");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        int length = $this$indexOf.length;
        for (int index$iv = 0; index$iv < length; index$iv++) {
            if (comparator.compare($this$indexOf[index$iv], value) == 0) {
                return index$iv;
            }
        }
        return -1;
    }

    public static final String[] concat(String[] $this$concat, String value) {
        Intrinsics.checkParameterIsNotNull($this$concat, "$this$concat");
        Intrinsics.checkParameterIsNotNull(value, "value");
        Object[] copyOf = Arrays.copyOf($this$concat, $this$concat.length + 1);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        String[] result = (String[]) copyOf;
        result[ArraysKt.getLastIndex(result)] = value;
        if (result != null) {
            return result;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<kotlin.String>");
    }

    public static /* synthetic */ int indexOfFirstNonAsciiWhitespace$default(String str, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = str.length();
        }
        return indexOfFirstNonAsciiWhitespace(str, i, i2);
    }

    public static final int indexOfFirstNonAsciiWhitespace(String $this$indexOfFirstNonAsciiWhitespace, int startIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull($this$indexOfFirstNonAsciiWhitespace, "$this$indexOfFirstNonAsciiWhitespace");
        for (int i = startIndex; i < endIndex; i++) {
            char charAt = $this$indexOfFirstNonAsciiWhitespace.charAt(i);
            if (!(charAt == '\t' || charAt == '\n' || charAt == '\f' || charAt == '\r' || charAt == ' ')) {
                return i;
            }
        }
        return endIndex;
    }

    public static /* synthetic */ int indexOfLastNonAsciiWhitespace$default(String str, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = str.length();
        }
        return indexOfLastNonAsciiWhitespace(str, i, i2);
    }

    public static final int indexOfLastNonAsciiWhitespace(String $this$indexOfLastNonAsciiWhitespace, int startIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull($this$indexOfLastNonAsciiWhitespace, "$this$indexOfLastNonAsciiWhitespace");
        int i = endIndex - 1;
        if (i >= startIndex) {
            while (true) {
                char charAt = $this$indexOfLastNonAsciiWhitespace.charAt(i);
                if (charAt == '\t' || charAt == '\n' || charAt == '\f' || charAt == '\r' || charAt == ' ') {
                    if (i == startIndex) {
                        break;
                    }
                    i--;
                } else {
                    return i + 1;
                }
            }
        }
        return startIndex;
    }

    public static /* synthetic */ String trimSubstring$default(String str, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = str.length();
        }
        return trimSubstring(str, i, i2);
    }

    public static final String trimSubstring(String $this$trimSubstring, int startIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull($this$trimSubstring, "$this$trimSubstring");
        int start = indexOfFirstNonAsciiWhitespace($this$trimSubstring, startIndex, endIndex);
        String substring = $this$trimSubstring.substring(start, indexOfLastNonAsciiWhitespace($this$trimSubstring, start, endIndex));
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return substring;
    }

    public static /* synthetic */ int delimiterOffset$default(String str, String str2, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = str.length();
        }
        return delimiterOffset(str, str2, i, i2);
    }

    public static final int delimiterOffset(String $this$delimiterOffset, String delimiters, int startIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull($this$delimiterOffset, "$this$delimiterOffset");
        Intrinsics.checkParameterIsNotNull(delimiters, "delimiters");
        for (int i = startIndex; i < endIndex; i++) {
            if (StringsKt.contains$default((CharSequence) delimiters, $this$delimiterOffset.charAt(i), false, 2, (Object) null)) {
                return i;
            }
        }
        return endIndex;
    }

    public static /* synthetic */ int delimiterOffset$default(String str, char c, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = str.length();
        }
        return delimiterOffset(str, c, i, i2);
    }

    public static final int delimiterOffset(String $this$delimiterOffset, char delimiter, int startIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull($this$delimiterOffset, "$this$delimiterOffset");
        for (int i = startIndex; i < endIndex; i++) {
            if ($this$delimiterOffset.charAt(i) == delimiter) {
                return i;
            }
        }
        return endIndex;
    }

    public static final int indexOfControlOrNonAscii(String $this$indexOfControlOrNonAscii) {
        Intrinsics.checkParameterIsNotNull($this$indexOfControlOrNonAscii, "$this$indexOfControlOrNonAscii");
        int length = $this$indexOfControlOrNonAscii.length();
        for (int i = 0; i < length; i++) {
            char c = $this$indexOfControlOrNonAscii.charAt(i);
            if (c <= 31 || c >= 127) {
                return i;
            }
        }
        return -1;
    }

    public static final boolean canParseAsIpAddress(String $this$canParseAsIpAddress) {
        Intrinsics.checkParameterIsNotNull($this$canParseAsIpAddress, "$this$canParseAsIpAddress");
        return VERIFY_AS_IP_ADDRESS.matches($this$canParseAsIpAddress);
    }

    public static final String format(String format, Object... args) {
        Intrinsics.checkParameterIsNotNull(format, "format");
        Intrinsics.checkParameterIsNotNull(args, "args");
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        Locale locale = Locale.US;
        Intrinsics.checkExpressionValueIsNotNull(locale, "Locale.US");
        Object[] copyOf = Arrays.copyOf(args, args.length);
        String format2 = String.format(locale, format, Arrays.copyOf(copyOf, copyOf.length));
        Intrinsics.checkExpressionValueIsNotNull(format2, "java.lang.String.format(locale, format, *args)");
        return format2;
    }

    public static final Charset readBomAsCharset(BufferedSource $this$readBomAsCharset, Charset charset) throws IOException {
        Intrinsics.checkParameterIsNotNull($this$readBomAsCharset, "$this$readBomAsCharset");
        Intrinsics.checkParameterIsNotNull(charset, "default");
        int select = $this$readBomAsCharset.select(UNICODE_BOMS);
        if (select == -1) {
            return charset;
        }
        if (select == 0) {
            Charset charset2 = StandardCharsets.UTF_8;
            Intrinsics.checkExpressionValueIsNotNull(charset2, "UTF_8");
            return charset2;
        } else if (select == 1) {
            Charset charset3 = StandardCharsets.UTF_16BE;
            Intrinsics.checkExpressionValueIsNotNull(charset3, "UTF_16BE");
            return charset3;
        } else if (select == 2) {
            Charset charset4 = StandardCharsets.UTF_16LE;
            Intrinsics.checkExpressionValueIsNotNull(charset4, "UTF_16LE");
            return charset4;
        } else if (select == 3) {
            return Charsets.INSTANCE.UTF32_BE();
        } else {
            if (select == 4) {
                return Charsets.INSTANCE.UTF32_LE();
            }
            throw new AssertionError();
        }
    }

    public static final int checkDuration(String name, long duration, TimeUnit unit) {
        Intrinsics.checkParameterIsNotNull(name, AppMeasurementSdk.ConditionalUserProperty.NAME);
        boolean z = true;
        if (duration >= 0) {
            if (unit != null) {
                long millis = unit.toMillis(duration);
                if (millis <= ((long) Integer.MAX_VALUE)) {
                    if (millis == 0 && duration > 0) {
                        z = false;
                    }
                    if (z) {
                        return (int) millis;
                    }
                    throw new IllegalArgumentException((name + " too small.").toString());
                }
                throw new IllegalArgumentException((name + " too large.").toString());
            }
            throw new IllegalStateException("unit == null".toString());
        }
        throw new IllegalStateException((name + " < 0").toString());
    }

    public static final int parseHexDigit(char $this$parseHexDigit) {
        if ('0' <= $this$parseHexDigit && '9' >= $this$parseHexDigit) {
            return $this$parseHexDigit - '0';
        }
        if ('a' <= $this$parseHexDigit && 'f' >= $this$parseHexDigit) {
            return ($this$parseHexDigit - 'a') + 10;
        }
        if ('A' <= $this$parseHexDigit && 'F' >= $this$parseHexDigit) {
            return ($this$parseHexDigit - 'A') + 10;
        }
        return -1;
    }

    public static final Headers toHeaders(List<Header> list) {
        Intrinsics.checkParameterIsNotNull(list, "$this$toHeaders");
        Headers.Builder builder = new Headers.Builder();
        for (Header header : list) {
            builder.addLenient$okhttp(header.component1().utf8(), header.component2().utf8());
        }
        return builder.build();
    }

    public static final List<Header> toHeaderList(Headers $this$toHeaderList) {
        Intrinsics.checkParameterIsNotNull($this$toHeaderList, "$this$toHeaderList");
        Iterable $this$map$iv = RangesKt.until(0, $this$toHeaderList.size());
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        Iterator<Integer> it = $this$map$iv.iterator();
        while (it.hasNext()) {
            int item$iv$iv = ((IntIterator) it).nextInt();
            destination$iv$iv.add(new Header($this$toHeaderList.name(item$iv$iv), $this$toHeaderList.value(item$iv$iv)));
        }
        return (List) destination$iv$iv;
    }

    public static final boolean canReuseConnectionFor(HttpUrl $this$canReuseConnectionFor, HttpUrl other) {
        Intrinsics.checkParameterIsNotNull($this$canReuseConnectionFor, "$this$canReuseConnectionFor");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return Intrinsics.areEqual($this$canReuseConnectionFor.host(), other.host()) && $this$canReuseConnectionFor.port() == other.port() && Intrinsics.areEqual($this$canReuseConnectionFor.scheme(), other.scheme());
    }

    public static final EventListener.Factory asFactory(EventListener $this$asFactory) {
        Intrinsics.checkParameterIsNotNull($this$asFactory, "$this$asFactory");
        return new EventListener.Factory() { // from class: okhttp3.internal.Util$asFactory$1
            @Override // okhttp3.EventListener.Factory
            public EventListener create(Call call) {
                Intrinsics.checkParameterIsNotNull(call, NotificationCompat.CATEGORY_CALL);
                return EventListener.this;
            }
        };
    }

    public static final int and(byte $this$and, int mask) {
        return $this$and & mask;
    }

    public static final int and(short $this$and, int mask) {
        return $this$and & mask;
    }

    public static final long and(int $this$and, long mask) {
        return ((long) $this$and) & mask;
    }

    public static final void writeMedium(BufferedSink $this$writeMedium, int medium) throws IOException {
        Intrinsics.checkParameterIsNotNull($this$writeMedium, "$this$writeMedium");
        $this$writeMedium.writeByte((medium >>> 16) & 255);
        $this$writeMedium.writeByte((medium >>> 8) & 255);
        $this$writeMedium.writeByte(medium & 255);
    }

    public static final int readMedium(BufferedSource $this$readMedium) throws IOException {
        Intrinsics.checkParameterIsNotNull($this$readMedium, "$this$readMedium");
        return (and($this$readMedium.readByte(), 255) << 16) | (and($this$readMedium.readByte(), 255) << 8) | and($this$readMedium.readByte(), 255);
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0053, code lost:
        if (r5 == Long.MAX_VALUE) goto L_0x0073;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0071, code lost:
        if (r5 != Long.MAX_VALUE) goto L_0x007b;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0073, code lost:
        r11.timeout().clearDeadline();
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x007b, code lost:
        r11.timeout().deadlineNanoTime(r0 + r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0086, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static final boolean skipAll(Source $this$skipAll, int duration, TimeUnit timeUnit) throws IOException {
        long originalDurationNs;
        boolean z;
        Intrinsics.checkParameterIsNotNull($this$skipAll, "$this$skipAll");
        Intrinsics.checkParameterIsNotNull(timeUnit, "timeUnit");
        long nowNs = System.nanoTime();
        if ($this$skipAll.timeout().hasDeadline()) {
            originalDurationNs = $this$skipAll.timeout().deadlineNanoTime() - nowNs;
        } else {
            originalDurationNs = Long.MAX_VALUE;
        }
        $this$skipAll.timeout().deadlineNanoTime(Math.min(originalDurationNs, timeUnit.toNanos((long) duration)) + nowNs);
        try {
            Buffer skipBuffer = new Buffer();
            while ($this$skipAll.read(skipBuffer, PlaybackStateCompat.ACTION_PLAY_FROM_URI) != -1) {
                skipBuffer.clear();
            }
            z = true;
        } catch (InterruptedIOException e) {
            z = false;
        } catch (Throwable th) {
            if (originalDurationNs == Long.MAX_VALUE) {
                $this$skipAll.timeout().clearDeadline();
            } else {
                $this$skipAll.timeout().deadlineNanoTime(nowNs + originalDurationNs);
            }
            throw th;
        }
    }

    public static final boolean discard(Source $this$discard, int timeout, TimeUnit timeUnit) {
        Intrinsics.checkParameterIsNotNull($this$discard, "$this$discard");
        Intrinsics.checkParameterIsNotNull(timeUnit, "timeUnit");
        try {
            return skipAll($this$discard, timeout, timeUnit);
        } catch (IOException e) {
            return false;
        }
    }

    public static final String peerName(Socket $this$peerName) {
        Intrinsics.checkParameterIsNotNull($this$peerName, "$this$peerName");
        SocketAddress address = $this$peerName.getRemoteSocketAddress();
        if (!(address instanceof InetSocketAddress)) {
            return address.toString();
        }
        String hostName = ((InetSocketAddress) address).getHostName();
        Intrinsics.checkExpressionValueIsNotNull(hostName, "address.hostName");
        return hostName;
    }

    public static final boolean isHealthy(Socket $this$isHealthy, BufferedSource source) {
        Intrinsics.checkParameterIsNotNull($this$isHealthy, "$this$isHealthy");
        Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
        try {
            int readTimeout = $this$isHealthy.getSoTimeout();
            try {
                $this$isHealthy.setSoTimeout(1);
                return !source.exhausted();
            } finally {
                $this$isHealthy.setSoTimeout(readTimeout);
            }
        } catch (SocketTimeoutException e) {
            return true;
        } catch (IOException e2) {
            return false;
        }
    }

    public static final void ignoreIoExceptions(Function0<Unit> function0) {
        Intrinsics.checkParameterIsNotNull(function0, "block");
        try {
            function0.invoke();
        } catch (IOException e) {
        }
    }

    public static final void threadName(String name, Function0<Unit> function0) {
        Intrinsics.checkParameterIsNotNull(name, AppMeasurementSdk.ConditionalUserProperty.NAME);
        Intrinsics.checkParameterIsNotNull(function0, "block");
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(currentThread, "currentThread");
        String oldName = currentThread.getName();
        currentThread.setName(name);
        try {
            function0.invoke();
        } finally {
            InlineMarker.finallyStart(1);
            currentThread.setName(oldName);
            InlineMarker.finallyEnd(1);
        }
    }

    public static final int skipAll(Buffer $this$skipAll, byte b) {
        Intrinsics.checkParameterIsNotNull($this$skipAll, "$this$skipAll");
        int count = 0;
        while (!$this$skipAll.exhausted() && $this$skipAll.getByte(0) == b) {
            count++;
            $this$skipAll.readByte();
        }
        return count;
    }

    public static /* synthetic */ int indexOfNonWhitespace$default(String str, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 0;
        }
        return indexOfNonWhitespace(str, i);
    }

    public static final int indexOfNonWhitespace(String $this$indexOfNonWhitespace, int startIndex) {
        Intrinsics.checkParameterIsNotNull($this$indexOfNonWhitespace, "$this$indexOfNonWhitespace");
        int length = $this$indexOfNonWhitespace.length();
        for (int i = startIndex; i < length; i++) {
            char c = $this$indexOfNonWhitespace.charAt(i);
            if (!(c == ' ' || c == '\t')) {
                return i;
            }
        }
        return $this$indexOfNonWhitespace.length();
    }

    public static final long headersContentLength(Response $this$headersContentLength) {
        Intrinsics.checkParameterIsNotNull($this$headersContentLength, "$this$headersContentLength");
        String str = $this$headersContentLength.headers().get(HttpHeaders.CONTENT_LENGTH);
        if (str != null) {
            return toLongOrDefault(str, -1);
        }
        return -1;
    }

    public static final long toLongOrDefault(String $this$toLongOrDefault, long defaultValue) {
        Intrinsics.checkParameterIsNotNull($this$toLongOrDefault, "$this$toLongOrDefault");
        try {
            return Long.parseLong($this$toLongOrDefault);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static final int toNonNegativeInt(String $this$toNonNegativeInt, int defaultValue) {
        if ($this$toNonNegativeInt == null) {
            return defaultValue;
        }
        try {
            long value = Long.parseLong($this$toNonNegativeInt);
            if (value > ((long) Integer.MAX_VALUE)) {
                return Integer.MAX_VALUE;
            }
            if (value < 0) {
                return 0;
            }
            return (int) value;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static final <T> List<T> toImmutableList(List<? extends T> list) {
        Intrinsics.checkParameterIsNotNull(list, "$this$toImmutableList");
        List<T> unmodifiableList = Collections.unmodifiableList(CollectionsKt.toMutableList((Collection) list));
        Intrinsics.checkExpressionValueIsNotNull(unmodifiableList, "Collections.unmodifiableList(toMutableList())");
        return unmodifiableList;
    }

    @SafeVarargs
    public static final <T> List<T> immutableListOf(T... tArr) {
        Intrinsics.checkParameterIsNotNull(tArr, "elements");
        Object[] objArr = (Object[]) tArr.clone();
        List<T> unmodifiableList = Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(objArr, objArr.length)));
        Intrinsics.checkExpressionValueIsNotNull(unmodifiableList, "Collections.unmodifiable…sList(*elements.clone()))");
        return unmodifiableList;
    }

    public static final <K, V> Map<K, V> toImmutableMap(Map<K, ? extends V> map) {
        Intrinsics.checkParameterIsNotNull(map, "$this$toImmutableMap");
        if (map.isEmpty()) {
            return MapsKt.emptyMap();
        }
        Map<K, V> unmodifiableMap = Collections.unmodifiableMap(new LinkedHashMap(map));
        Intrinsics.checkExpressionValueIsNotNull(unmodifiableMap, "Collections.unmodifiableMap(LinkedHashMap(this))");
        return unmodifiableMap;
    }

    public static final void closeQuietly(Closeable $this$closeQuietly) {
        Intrinsics.checkParameterIsNotNull($this$closeQuietly, "$this$closeQuietly");
        try {
            $this$closeQuietly.close();
        } catch (RuntimeException rethrown) {
            throw rethrown;
        } catch (Exception e) {
        }
    }

    public static final void closeQuietly(Socket $this$closeQuietly) {
        Intrinsics.checkParameterIsNotNull($this$closeQuietly, "$this$closeQuietly");
        try {
            $this$closeQuietly.close();
        } catch (AssertionError e) {
            throw e;
        } catch (RuntimeException rethrown) {
            throw rethrown;
        } catch (Exception e2) {
        }
    }

    public static final void closeQuietly(ServerSocket $this$closeQuietly) {
        Intrinsics.checkParameterIsNotNull($this$closeQuietly, "$this$closeQuietly");
        try {
            $this$closeQuietly.close();
        } catch (RuntimeException rethrown) {
            throw rethrown;
        } catch (Exception e) {
        }
    }

    public static final String toHexString(long $this$toHexString) {
        String hexString = Long.toHexString($this$toHexString);
        Intrinsics.checkExpressionValueIsNotNull(hexString, "java.lang.Long.toHexString(this)");
        return hexString;
    }

    public static final String toHexString(int $this$toHexString) {
        String hexString = Integer.toHexString($this$toHexString);
        Intrinsics.checkExpressionValueIsNotNull(hexString, "Integer.toHexString(this)");
        return hexString;
    }

    public static final void wait(Object $this$wait) {
        Intrinsics.checkParameterIsNotNull($this$wait, "$this$wait");
        $this$wait.wait();
    }

    public static final void notify(Object $this$notify) {
        Intrinsics.checkParameterIsNotNull($this$notify, "$this$notify");
        $this$notify.notify();
    }

    public static final void notifyAll(Object $this$notifyAll) {
        Intrinsics.checkParameterIsNotNull($this$notifyAll, "$this$notifyAll");
        $this$notifyAll.notifyAll();
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x003a, code lost:
        return r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static final <T> T readFieldOrNull(Object instance, Class<T> cls, String fieldName) {
        Object delegate;
        Intrinsics.checkParameterIsNotNull(instance, "instance");
        Intrinsics.checkParameterIsNotNull(cls, "fieldType");
        Intrinsics.checkParameterIsNotNull(fieldName, "fieldName");
        Class c = instance.getClass();
        while (true) {
            T t = null;
            if (!Intrinsics.areEqual(c, Object.class)) {
                try {
                    Field field = c.getDeclaredField(fieldName);
                    Intrinsics.checkExpressionValueIsNotNull(field, "field");
                    field.setAccessible(true);
                    Object value = field.get(instance);
                    if (cls.isInstance(value)) {
                        t = cls.cast(value);
                        break;
                    }
                    break;
                } catch (NoSuchFieldException e) {
                    Class superclass = c.getSuperclass();
                    Intrinsics.checkExpressionValueIsNotNull(superclass, "c.superclass");
                    c = superclass;
                }
            } else if (!(true ^ Intrinsics.areEqual(fieldName, "delegate")) || (delegate = readFieldOrNull(instance, Object.class, "delegate")) == null) {
                return null;
            } else {
                return (T) readFieldOrNull(delegate, cls, fieldName);
            }
        }
    }

    public static final <E> void addIfAbsent(List<E> list, E e) {
        Intrinsics.checkParameterIsNotNull(list, "$this$addIfAbsent");
        if (!list.contains(e)) {
            list.add(e);
        }
    }

    public static final void assertThreadHoldsLock(Object $this$assertThreadHoldsLock) {
        Intrinsics.checkParameterIsNotNull($this$assertThreadHoldsLock, "$this$assertThreadHoldsLock");
        if (assertionsEnabled && !Thread.holdsLock($this$assertThreadHoldsLock)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Thread ");
            Thread currentThread = Thread.currentThread();
            Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
            sb.append(currentThread.getName());
            sb.append(" MUST hold lock on ");
            sb.append($this$assertThreadHoldsLock);
            throw new AssertionError(sb.toString());
        }
    }

    public static final void assertThreadDoesntHoldLock(Object $this$assertThreadDoesntHoldLock) {
        Intrinsics.checkParameterIsNotNull($this$assertThreadDoesntHoldLock, "$this$assertThreadDoesntHoldLock");
        if (assertionsEnabled && Thread.holdsLock($this$assertThreadDoesntHoldLock)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Thread ");
            Thread currentThread = Thread.currentThread();
            Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
            sb.append(currentThread.getName());
            sb.append(" MUST NOT hold lock on ");
            sb.append($this$assertThreadDoesntHoldLock);
            throw new AssertionError(sb.toString());
        }
    }
}
