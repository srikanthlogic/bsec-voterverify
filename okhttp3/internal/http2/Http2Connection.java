package okhttp3.internal.http2;

import com.facebook.common.util.UriUtil;
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import okhttp3.internal.Util;
import okhttp3.internal.concurrent.Task;
import okhttp3.internal.concurrent.TaskQueue;
import okhttp3.internal.concurrent.TaskRunner;
import okhttp3.internal.http2.Http2Reader;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;
/* compiled from: Http2Connection.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000´\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010#\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u001d\n\u0002\u0018\u0002\n\u0002\b\u0014\u0018\u0000 \u0099\u00012\u00020\u0001:\b\u0098\u0001\u0099\u0001\u009a\u0001\u009b\u0001B\u000f\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010P\u001a\u00020QJ\b\u0010R\u001a\u00020QH\u0016J'\u0010R\u001a\u00020Q2\u0006\u0010S\u001a\u00020T2\u0006\u0010U\u001a\u00020T2\b\u0010V\u001a\u0004\u0018\u00010WH\u0000¢\u0006\u0002\bXJ\u0012\u0010Y\u001a\u00020Q2\b\u0010Z\u001a\u0004\u0018\u00010WH\u0002J\u0006\u0010[\u001a\u00020QJ\u0010\u0010\\\u001a\u0004\u0018\u00010B2\u0006\u0010]\u001a\u00020\u0012J\u000e\u0010^\u001a\u00020\t2\u0006\u0010_\u001a\u00020\u0006J&\u0010`\u001a\u00020B2\u0006\u0010a\u001a\u00020\u00122\f\u0010b\u001a\b\u0012\u0004\u0012\u00020d0c2\u0006\u0010e\u001a\u00020\tH\u0002J\u001c\u0010`\u001a\u00020B2\f\u0010b\u001a\b\u0012\u0004\u0012\u00020d0c2\u0006\u0010e\u001a\u00020\tJ\u0006\u0010f\u001a\u00020\u0012J-\u0010g\u001a\u00020Q2\u0006\u0010h\u001a\u00020\u00122\u0006\u0010i\u001a\u00020j2\u0006\u0010k\u001a\u00020\u00122\u0006\u0010l\u001a\u00020\tH\u0000¢\u0006\u0002\bmJ+\u0010n\u001a\u00020Q2\u0006\u0010h\u001a\u00020\u00122\f\u0010b\u001a\b\u0012\u0004\u0012\u00020d0c2\u0006\u0010l\u001a\u00020\tH\u0000¢\u0006\u0002\boJ#\u0010p\u001a\u00020Q2\u0006\u0010h\u001a\u00020\u00122\f\u0010b\u001a\b\u0012\u0004\u0012\u00020d0cH\u0000¢\u0006\u0002\bqJ\u001d\u0010r\u001a\u00020Q2\u0006\u0010h\u001a\u00020\u00122\u0006\u0010s\u001a\u00020TH\u0000¢\u0006\u0002\btJ$\u0010u\u001a\u00020B2\u0006\u0010a\u001a\u00020\u00122\f\u0010b\u001a\b\u0012\u0004\u0012\u00020d0c2\u0006\u0010e\u001a\u00020\tJ\u0015\u0010v\u001a\u00020\t2\u0006\u0010h\u001a\u00020\u0012H\u0000¢\u0006\u0002\bwJ\u0017\u0010x\u001a\u0004\u0018\u00010B2\u0006\u0010h\u001a\u00020\u0012H\u0000¢\u0006\u0002\byJ\r\u0010z\u001a\u00020QH\u0000¢\u0006\u0002\b{J\u000e\u0010|\u001a\u00020Q2\u0006\u0010}\u001a\u00020&J\u000e\u0010~\u001a\u00020Q2\u0006\u0010\u007f\u001a\u00020TJ\u0014\u0010\u0080\u0001\u001a\u00020Q2\t\b\u0002\u0010\u0081\u0001\u001a\u00020\tH\u0007J\u0018\u0010\u0082\u0001\u001a\u00020Q2\u0007\u0010\u0083\u0001\u001a\u00020\u0006H\u0000¢\u0006\u0003\b\u0084\u0001J,\u0010\u0085\u0001\u001a\u00020Q2\u0006\u0010h\u001a\u00020\u00122\u0007\u0010\u0086\u0001\u001a\u00020\t2\n\u0010\u0087\u0001\u001a\u0005\u0018\u00010\u0088\u00012\u0006\u0010k\u001a\u00020\u0006J/\u0010\u0089\u0001\u001a\u00020Q2\u0006\u0010h\u001a\u00020\u00122\u0007\u0010\u0086\u0001\u001a\u00020\t2\r\u0010\u008a\u0001\u001a\b\u0012\u0004\u0012\u00020d0cH\u0000¢\u0006\u0003\b\u008b\u0001J\u0007\u0010\u008c\u0001\u001a\u00020QJ\"\u0010\u008c\u0001\u001a\u00020Q2\u0007\u0010\u008d\u0001\u001a\u00020\t2\u0007\u0010\u008e\u0001\u001a\u00020\u00122\u0007\u0010\u008f\u0001\u001a\u00020\u0012J\u0007\u0010\u0090\u0001\u001a\u00020QJ\u001f\u0010\u0091\u0001\u001a\u00020Q2\u0006\u0010h\u001a\u00020\u00122\u0006\u0010\u007f\u001a\u00020TH\u0000¢\u0006\u0003\b\u0092\u0001J\u001f\u0010\u0093\u0001\u001a\u00020Q2\u0006\u0010h\u001a\u00020\u00122\u0006\u0010s\u001a\u00020TH\u0000¢\u0006\u0003\b\u0094\u0001J \u0010\u0095\u0001\u001a\u00020Q2\u0006\u0010h\u001a\u00020\u00122\u0007\u0010\u0096\u0001\u001a\u00020\u0006H\u0000¢\u0006\u0003\b\u0097\u0001R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\u00020\tX\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\u00020\rX\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0019\u001a\u00020\u0012X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u0014\u0010\u001e\u001a\u00020\u001fX\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u001a\u0010\"\u001a\u00020\u0012X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010\u001b\"\u0004\b$\u0010\u001dR\u0011\u0010%\u001a\u00020&¢\u0006\b\n\u0000\u001a\u0004\b'\u0010(R\u001a\u0010)\u001a\u00020&X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b*\u0010(\"\u0004\b+\u0010,R\u000e\u0010-\u001a\u00020.X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u000200X\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u00102\u001a\u00020\u00062\u0006\u00101\u001a\u00020\u0006@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b3\u00104R\u001e\u00105\u001a\u00020\u00062\u0006\u00101\u001a\u00020\u0006@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b6\u00104R\u0015\u00107\u001a\u000608R\u00020\u0000¢\u0006\b\n\u0000\u001a\u0004\b9\u0010:R\u000e\u0010;\u001a\u000200X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010<\u001a\u00020=X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b>\u0010?R \u0010@\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020B0AX\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\bC\u0010DR\u000e\u0010E\u001a\u00020FX\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010G\u001a\u00020\u00062\u0006\u00101\u001a\u00020\u0006@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\bH\u00104R\u001e\u0010I\u001a\u00020\u00062\u0006\u00101\u001a\u00020\u0006@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\bJ\u00104R\u0011\u0010K\u001a\u00020L¢\u0006\b\n\u0000\u001a\u0004\bM\u0010NR\u000e\u0010O\u001a\u000200X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u009c\u0001"}, d2 = {"Lokhttp3/internal/http2/Http2Connection;", "Ljava/io/Closeable;", "builder", "Lokhttp3/internal/http2/Http2Connection$Builder;", "(Lokhttp3/internal/http2/Http2Connection$Builder;)V", "awaitPingsSent", "", "awaitPongsReceived", "client", "", "getClient$okhttp", "()Z", "connectionName", "", "getConnectionName$okhttp", "()Ljava/lang/String;", "currentPushRequests", "", "", "degradedPingsSent", "degradedPongDeadlineNs", "degradedPongsReceived", "intervalPingsSent", "intervalPongsReceived", "isShutdown", "lastGoodStreamId", "getLastGoodStreamId$okhttp", "()I", "setLastGoodStreamId$okhttp", "(I)V", ServiceSpecificExtraArgs.CastExtraArgs.LISTENER, "Lokhttp3/internal/http2/Http2Connection$Listener;", "getListener$okhttp", "()Lokhttp3/internal/http2/Http2Connection$Listener;", "nextStreamId", "getNextStreamId$okhttp", "setNextStreamId$okhttp", "okHttpSettings", "Lokhttp3/internal/http2/Settings;", "getOkHttpSettings", "()Lokhttp3/internal/http2/Settings;", "peerSettings", "getPeerSettings", "setPeerSettings", "(Lokhttp3/internal/http2/Settings;)V", "pushObserver", "Lokhttp3/internal/http2/PushObserver;", "pushQueue", "Lokhttp3/internal/concurrent/TaskQueue;", "<set-?>", "readBytesAcknowledged", "getReadBytesAcknowledged", "()J", "readBytesTotal", "getReadBytesTotal", "readerRunnable", "Lokhttp3/internal/http2/Http2Connection$ReaderRunnable;", "getReaderRunnable", "()Lokhttp3/internal/http2/Http2Connection$ReaderRunnable;", "settingsListenerQueue", "socket", "Ljava/net/Socket;", "getSocket$okhttp", "()Ljava/net/Socket;", "streams", "", "Lokhttp3/internal/http2/Http2Stream;", "getStreams$okhttp", "()Ljava/util/Map;", "taskRunner", "Lokhttp3/internal/concurrent/TaskRunner;", "writeBytesMaximum", "getWriteBytesMaximum", "writeBytesTotal", "getWriteBytesTotal", "writer", "Lokhttp3/internal/http2/Http2Writer;", "getWriter", "()Lokhttp3/internal/http2/Http2Writer;", "writerQueue", "awaitPong", "", "close", "connectionCode", "Lokhttp3/internal/http2/ErrorCode;", "streamCode", "cause", "Ljava/io/IOException;", "close$okhttp", "failConnection", "e", "flush", "getStream", "id", "isHealthy", "nowNs", "newStream", "associatedStreamId", "requestHeaders", "", "Lokhttp3/internal/http2/Header;", "out", "openStreamCount", "pushDataLater", "streamId", FirebaseAnalytics.Param.SOURCE, "Lokio/BufferedSource;", "byteCount", "inFinished", "pushDataLater$okhttp", "pushHeadersLater", "pushHeadersLater$okhttp", "pushRequestLater", "pushRequestLater$okhttp", "pushResetLater", "errorCode", "pushResetLater$okhttp", "pushStream", "pushedStream", "pushedStream$okhttp", "removeStream", "removeStream$okhttp", "sendDegradedPingLater", "sendDegradedPingLater$okhttp", "setSettings", "settings", "shutdown", "statusCode", "start", "sendConnectionPreface", "updateConnectionFlowControl", "read", "updateConnectionFlowControl$okhttp", "writeData", "outFinished", "buffer", "Lokio/Buffer;", "writeHeaders", "alternating", "writeHeaders$okhttp", "writePing", "reply", "payload1", "payload2", "writePingAndAwaitPong", "writeSynReset", "writeSynReset$okhttp", "writeSynResetLater", "writeSynResetLater$okhttp", "writeWindowUpdateLater", "unacknowledgedBytesRead", "writeWindowUpdateLater$okhttp", "Builder", "Companion", "Listener", "ReaderRunnable", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class Http2Connection implements Closeable {
    public static final int AWAIT_PING;
    public static final Companion Companion = new Companion(null);
    private static final Settings DEFAULT_SETTINGS;
    public static final int DEGRADED_PING;
    public static final int DEGRADED_PONG_TIMEOUT_NS;
    public static final int INTERVAL_PING;
    public static final int OKHTTP_CLIENT_WINDOW_SIZE;
    private long awaitPingsSent;
    private long awaitPongsReceived;
    private final boolean client;
    private final String connectionName;
    private final Set<Integer> currentPushRequests;
    private long degradedPingsSent;
    private long degradedPongDeadlineNs;
    private long degradedPongsReceived;
    private long intervalPingsSent;
    private long intervalPongsReceived;
    private boolean isShutdown;
    private int lastGoodStreamId;
    private final Listener listener;
    private int nextStreamId;
    private final Settings okHttpSettings;
    private Settings peerSettings;
    private final PushObserver pushObserver;
    private final TaskQueue pushQueue;
    private long readBytesAcknowledged;
    private long readBytesTotal;
    private final ReaderRunnable readerRunnable;
    private final TaskQueue settingsListenerQueue;
    private final Socket socket;
    private final Map<Integer, Http2Stream> streams = new LinkedHashMap();
    private final TaskRunner taskRunner;
    private long writeBytesMaximum;
    private long writeBytesTotal;
    private final Http2Writer writer;
    private final TaskQueue writerQueue;

    public final void start() throws IOException {
        start$default(this, false, 1, null);
    }

    public Http2Connection(Builder builder) {
        Intrinsics.checkParameterIsNotNull(builder, "builder");
        this.client = builder.getClient$okhttp();
        this.listener = builder.getListener$okhttp();
        this.connectionName = builder.getConnectionName$okhttp();
        this.nextStreamId = builder.getClient$okhttp() ? 3 : 2;
        this.taskRunner = builder.getTaskRunner$okhttp();
        this.writerQueue = this.taskRunner.newQueue();
        this.pushQueue = this.taskRunner.newQueue();
        this.settingsListenerQueue = this.taskRunner.newQueue();
        this.pushObserver = builder.getPushObserver$okhttp();
        Settings $this$apply = new Settings();
        if (builder.getClient$okhttp()) {
            $this$apply.set(7, 16777216);
        }
        this.okHttpSettings = $this$apply;
        this.peerSettings = DEFAULT_SETTINGS;
        this.writeBytesMaximum = (long) this.peerSettings.getInitialWindowSize();
        this.socket = builder.getSocket$okhttp();
        this.writer = new Http2Writer(builder.getSink$okhttp(), this.client);
        this.readerRunnable = new ReaderRunnable(this, new Http2Reader(builder.getSource$okhttp(), this.client));
        this.currentPushRequests = new LinkedHashSet();
        if (builder.getPingIntervalMillis$okhttp() != 0) {
            long pingIntervalNanos = TimeUnit.MILLISECONDS.toNanos((long) builder.getPingIntervalMillis$okhttp());
            String name$iv = this.connectionName + " ping";
            this.writerQueue.schedule(new Task(name$iv, name$iv, this, pingIntervalNanos) { // from class: okhttp3.internal.http2.Http2Connection$$special$$inlined$schedule$1
                final /* synthetic */ String $name;
                final /* synthetic */ long $pingIntervalNanos$inlined;
                final /* synthetic */ Http2Connection this$0;

                {
                    this.$name = $captured_local_variable$1;
                    this.this$0 = r3;
                    this.$pingIntervalNanos$inlined = r4;
                }

                @Override // okhttp3.internal.concurrent.Task
                public long runOnce() {
                    int failDueToMissingPong;
                    synchronized (this.this$0) {
                        if (this.this$0.intervalPongsReceived < this.this$0.intervalPingsSent) {
                            failDueToMissingPong = 1;
                        } else {
                            Http2Connection http2Connection = this.this$0;
                            http2Connection.intervalPingsSent = http2Connection.intervalPingsSent + 1;
                            failDueToMissingPong = 0;
                        }
                    }
                    if (failDueToMissingPong != 0) {
                        this.this$0.failConnection(null);
                        return -1;
                    }
                    this.this$0.writePing(false, 1, 0);
                    return this.$pingIntervalNanos$inlined;
                }
            }, pingIntervalNanos);
        }
    }

    public final boolean getClient$okhttp() {
        return this.client;
    }

    public final Listener getListener$okhttp() {
        return this.listener;
    }

    public final Map<Integer, Http2Stream> getStreams$okhttp() {
        return this.streams;
    }

    public final String getConnectionName$okhttp() {
        return this.connectionName;
    }

    public final int getLastGoodStreamId$okhttp() {
        return this.lastGoodStreamId;
    }

    public final void setLastGoodStreamId$okhttp(int i) {
        this.lastGoodStreamId = i;
    }

    public final int getNextStreamId$okhttp() {
        return this.nextStreamId;
    }

    public final void setNextStreamId$okhttp(int i) {
        this.nextStreamId = i;
    }

    public final Settings getOkHttpSettings() {
        return this.okHttpSettings;
    }

    public final Settings getPeerSettings() {
        return this.peerSettings;
    }

    public final void setPeerSettings(Settings settings) {
        Intrinsics.checkParameterIsNotNull(settings, "<set-?>");
        this.peerSettings = settings;
    }

    public final long getReadBytesTotal() {
        return this.readBytesTotal;
    }

    public final long getReadBytesAcknowledged() {
        return this.readBytesAcknowledged;
    }

    public final long getWriteBytesTotal() {
        return this.writeBytesTotal;
    }

    public final long getWriteBytesMaximum() {
        return this.writeBytesMaximum;
    }

    public final Socket getSocket$okhttp() {
        return this.socket;
    }

    public final Http2Writer getWriter() {
        return this.writer;
    }

    public final ReaderRunnable getReaderRunnable() {
        return this.readerRunnable;
    }

    public final synchronized int openStreamCount() {
        return this.streams.size();
    }

    public final synchronized Http2Stream getStream(int id) {
        return this.streams.get(Integer.valueOf(id));
    }

    public final synchronized Http2Stream removeStream$okhttp(int streamId) {
        Http2Stream stream;
        stream = this.streams.remove(Integer.valueOf(streamId));
        notifyAll();
        return stream;
    }

    public final synchronized void updateConnectionFlowControl$okhttp(long read) {
        this.readBytesTotal += read;
        long readBytesToAcknowledge = this.readBytesTotal - this.readBytesAcknowledged;
        if (readBytesToAcknowledge >= ((long) (this.okHttpSettings.getInitialWindowSize() / 2))) {
            writeWindowUpdateLater$okhttp(0, readBytesToAcknowledge);
            this.readBytesAcknowledged += readBytesToAcknowledge;
        }
    }

    public final Http2Stream pushStream(int associatedStreamId, List<Header> list, boolean out) throws IOException {
        Intrinsics.checkParameterIsNotNull(list, "requestHeaders");
        if (!this.client) {
            return newStream(associatedStreamId, list, out);
        }
        throw new IllegalStateException("Client cannot push requests.".toString());
    }

    public final Http2Stream newStream(List<Header> list, boolean out) throws IOException {
        Intrinsics.checkParameterIsNotNull(list, "requestHeaders");
        return newStream(0, list, out);
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x0068 A[Catch: all -> 0x005c, TRY_ENTER, TRY_LEAVE, TryCatch #4 {all -> 0x005c, blocks: (B:19:0x0045, B:21:0x004d, B:31:0x0068), top: B:72:0x0045 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0076 A[Catch: all -> 0x00a7, TRY_LEAVE, TryCatch #2 {all -> 0x00a7, blocks: (B:33:0x0073, B:35:0x0076), top: B:68:0x0073 }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x007e A[Catch: all -> 0x00a5, TryCatch #1 {all -> 0x00a5, blocks: (B:37:0x007a, B:38:0x007e, B:40:0x0085, B:41:0x008a, B:47:0x0097, B:48:0x00a4), top: B:67:0x0074 }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0090  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private final Http2Stream newStream(int associatedStreamId, List<Header> list, boolean out) throws IOException {
        Throwable th;
        Throwable th2;
        boolean flushHeaders;
        boolean outFinished = !out;
        int streamId = 0;
        synchronized (this.writer) {
            try {
                synchronized (this) {
                    try {
                        if (this.nextStreamId > 1073741823) {
                            shutdown(ErrorCode.REFUSED_STREAM);
                        }
                        if (!this.isShutdown) {
                            int streamId2 = this.nextStreamId;
                            try {
                                this.nextStreamId += 2;
                                try {
                                    Http2Stream stream = new Http2Stream(streamId2, this, outFinished, false, null);
                                    try {
                                        try {
                                            if (out) {
                                                try {
                                                    if (this.writeBytesTotal < this.writeBytesMaximum && stream.getWriteBytesTotal() < stream.getWriteBytesMaximum()) {
                                                        flushHeaders = false;
                                                        if (stream.isOpen()) {
                                                            this.streams.put(Integer.valueOf(streamId2), stream);
                                                        }
                                                        Unit unit = Unit.INSTANCE;
                                                        if (associatedStreamId != 0) {
                                                            this.writer.headers(outFinished, streamId2, list);
                                                        } else if (!this.client) {
                                                            this.writer.pushPromise(associatedStreamId, streamId2, list);
                                                        } else {
                                                            throw new IllegalArgumentException("client streams shouldn't have associated stream IDs".toString());
                                                        }
                                                        Unit unit2 = Unit.INSTANCE;
                                                        if (flushHeaders) {
                                                            this.writer.flush();
                                                        }
                                                        return stream;
                                                    }
                                                } catch (Throwable th3) {
                                                    th2 = th3;
                                                    streamId = streamId2;
                                                    throw th2;
                                                }
                                            }
                                            if (associatedStreamId != 0) {
                                            }
                                            Unit unit22 = Unit.INSTANCE;
                                            if (flushHeaders) {
                                            }
                                            return stream;
                                        } catch (Throwable th4) {
                                            th = th4;
                                            throw th;
                                        }
                                    } catch (Throwable th5) {
                                        th = th5;
                                    }
                                    flushHeaders = true;
                                    if (stream.isOpen()) {
                                    }
                                    Unit unit3 = Unit.INSTANCE;
                                } catch (Throwable th6) {
                                    th2 = th6;
                                    streamId = streamId2;
                                }
                            } catch (Throwable th7) {
                                th2 = th7;
                                streamId = streamId2;
                            }
                        } else {
                            throw new ConnectionShutdownException();
                        }
                    } catch (Throwable th8) {
                        th2 = th8;
                    }
                }
            } catch (Throwable th9) {
                th = th9;
            }
        }
    }

    public final void writeHeaders$okhttp(int streamId, boolean outFinished, List<Header> list) throws IOException {
        Intrinsics.checkParameterIsNotNull(list, "alternating");
        this.writer.headers(outFinished, streamId, list);
    }

    public final void writeData(int streamId, boolean outFinished, Buffer buffer, long byteCount) throws IOException {
        if (byteCount == 0) {
            this.writer.data(outFinished, streamId, buffer, 0);
            return;
        }
        long byteCount2 = byteCount;
        while (byteCount2 > 0) {
            Ref.IntRef toWrite = new Ref.IntRef();
            synchronized (this) {
                while (this.writeBytesTotal >= this.writeBytesMaximum) {
                    try {
                        if (this.streams.containsKey(Integer.valueOf(streamId))) {
                            wait();
                        } else {
                            throw new IOException("stream closed");
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new InterruptedIOException();
                    }
                }
                toWrite.element = (int) Math.min(byteCount2, this.writeBytesMaximum - this.writeBytesTotal);
                toWrite.element = Math.min(toWrite.element, this.writer.maxDataLength());
                this.writeBytesTotal += (long) toWrite.element;
                Unit unit = Unit.INSTANCE;
            }
            byteCount2 -= (long) toWrite.element;
            this.writer.data(outFinished && byteCount2 == 0, streamId, buffer, toWrite.element);
        }
    }

    public final void writeSynResetLater$okhttp(int streamId, ErrorCode errorCode) {
        Intrinsics.checkParameterIsNotNull(errorCode, "errorCode");
        String name$iv = this.connectionName + '[' + streamId + "] writeSynReset";
        this.writerQueue.schedule(new Task(name$iv, true, name$iv, true, this, streamId, errorCode) { // from class: okhttp3.internal.http2.Http2Connection$writeSynResetLater$$inlined$execute$1
            final /* synthetic */ boolean $cancelable;
            final /* synthetic */ ErrorCode $errorCode$inlined;
            final /* synthetic */ String $name;
            final /* synthetic */ int $streamId$inlined;
            final /* synthetic */ Http2Connection this$0;

            {
                this.$name = $captured_local_variable$1;
                this.$cancelable = $captured_local_variable$2;
                this.this$0 = r5;
                this.$streamId$inlined = r6;
                this.$errorCode$inlined = r7;
            }

            @Override // okhttp3.internal.concurrent.Task
            public long runOnce() {
                try {
                    this.this$0.writeSynReset$okhttp(this.$streamId$inlined, this.$errorCode$inlined);
                    return -1;
                } catch (IOException e) {
                    this.this$0.failConnection(e);
                    return -1;
                }
            }
        }, 0);
    }

    public final void writeSynReset$okhttp(int streamId, ErrorCode statusCode) throws IOException {
        Intrinsics.checkParameterIsNotNull(statusCode, "statusCode");
        this.writer.rstStream(streamId, statusCode);
    }

    public final void writeWindowUpdateLater$okhttp(int streamId, long unacknowledgedBytesRead) {
        String name$iv = this.connectionName + '[' + streamId + "] windowUpdate";
        this.writerQueue.schedule(new Task(name$iv, true, name$iv, true, this, streamId, unacknowledgedBytesRead) { // from class: okhttp3.internal.http2.Http2Connection$writeWindowUpdateLater$$inlined$execute$1
            final /* synthetic */ boolean $cancelable;
            final /* synthetic */ String $name;
            final /* synthetic */ int $streamId$inlined;
            final /* synthetic */ long $unacknowledgedBytesRead$inlined;
            final /* synthetic */ Http2Connection this$0;

            {
                this.$name = $captured_local_variable$1;
                this.$cancelable = $captured_local_variable$2;
                this.this$0 = r5;
                this.$streamId$inlined = r6;
                this.$unacknowledgedBytesRead$inlined = r7;
            }

            @Override // okhttp3.internal.concurrent.Task
            public long runOnce() {
                try {
                    this.this$0.getWriter().windowUpdate(this.$streamId$inlined, this.$unacknowledgedBytesRead$inlined);
                    return -1;
                } catch (IOException e) {
                    this.this$0.failConnection(e);
                    return -1;
                }
            }
        }, 0);
    }

    public final void writePing(boolean reply, int payload1, int payload2) {
        try {
            this.writer.ping(reply, payload1, payload2);
        } catch (IOException e) {
            failConnection(e);
        }
    }

    public final void writePingAndAwaitPong() throws InterruptedException {
        writePing();
        awaitPong();
    }

    public final void writePing() throws InterruptedException {
        synchronized (this) {
            this.awaitPingsSent++;
        }
        writePing(false, 3, 1330343787);
    }

    public final synchronized void awaitPong() throws InterruptedException {
        while (this.awaitPongsReceived < this.awaitPingsSent) {
            wait();
        }
    }

    public final void flush() throws IOException {
        this.writer.flush();
    }

    public final void shutdown(ErrorCode statusCode) throws IOException {
        Intrinsics.checkParameterIsNotNull(statusCode, "statusCode");
        synchronized (this.writer) {
            synchronized (this) {
                if (!this.isShutdown) {
                    this.isShutdown = true;
                    int lastGoodStreamId = this.lastGoodStreamId;
                    Unit unit = Unit.INSTANCE;
                    this.writer.goAway(lastGoodStreamId, statusCode, Util.EMPTY_BYTE_ARRAY);
                    Unit unit2 = Unit.INSTANCE;
                }
            }
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        close$okhttp(ErrorCode.NO_ERROR, ErrorCode.CANCEL, null);
    }

    public final void close$okhttp(ErrorCode connectionCode, ErrorCode streamCode, IOException cause) {
        int i;
        Intrinsics.checkParameterIsNotNull(connectionCode, "connectionCode");
        Intrinsics.checkParameterIsNotNull(streamCode, "streamCode");
        if (!Util.assertionsEnabled || !Thread.holdsLock(this)) {
            try {
                shutdown(connectionCode);
            } catch (IOException e) {
            }
            Http2Stream[] http2StreamArr = null;
            synchronized (this) {
                if (!this.streams.isEmpty()) {
                    Object[] array = this.streams.values().toArray(new Http2Stream[0]);
                    if (array != null) {
                        http2StreamArr = (Http2Stream[]) array;
                        this.streams.clear();
                    } else {
                        throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                    }
                }
                Unit unit = Unit.INSTANCE;
            }
            if (http2StreamArr != null) {
                for (Http2Stream http2Stream : http2StreamArr) {
                    try {
                        http2Stream.close(streamCode, cause);
                    } catch (IOException e2) {
                    }
                }
            }
            try {
                this.writer.close();
            } catch (IOException e3) {
            }
            try {
                this.socket.close();
            } catch (IOException e4) {
            }
            this.writerQueue.shutdown();
            this.pushQueue.shutdown();
            this.settingsListenerQueue.shutdown();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Thread ");
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
        sb.append(currentThread.getName());
        sb.append(" MUST NOT hold lock on ");
        sb.append(this);
        throw new AssertionError(sb.toString());
    }

    public final void failConnection(IOException e) {
        close$okhttp(ErrorCode.PROTOCOL_ERROR, ErrorCode.PROTOCOL_ERROR, e);
    }

    public static /* synthetic */ void start$default(Http2Connection http2Connection, boolean z, int i, Object obj) throws IOException {
        if ((i & 1) != 0) {
            z = true;
        }
        http2Connection.start(z);
    }

    public final void start(boolean sendConnectionPreface) throws IOException {
        if (sendConnectionPreface) {
            this.writer.connectionPreface();
            this.writer.settings(this.okHttpSettings);
            int windowSize = this.okHttpSettings.getInitialWindowSize();
            if (windowSize != 65535) {
                this.writer.windowUpdate(0, (long) (windowSize - 65535));
            }
        }
        new Thread(this.readerRunnable, this.connectionName).start();
    }

    public final void setSettings(Settings settings) throws IOException {
        Intrinsics.checkParameterIsNotNull(settings, "settings");
        synchronized (this.writer) {
            synchronized (this) {
                if (!this.isShutdown) {
                    this.okHttpSettings.merge(settings);
                    Unit unit = Unit.INSTANCE;
                } else {
                    throw new ConnectionShutdownException();
                }
            }
            this.writer.settings(settings);
            Unit unit2 = Unit.INSTANCE;
        }
    }

    public final synchronized boolean isHealthy(long nowNs) {
        if (this.isShutdown) {
            return false;
        }
        if (this.degradedPongsReceived < this.degradedPingsSent) {
            if (nowNs >= this.degradedPongDeadlineNs) {
                return false;
            }
        }
        return true;
    }

    public final void sendDegradedPingLater$okhttp() {
        synchronized (this) {
            if (this.degradedPongsReceived >= this.degradedPingsSent) {
                this.degradedPingsSent++;
                this.degradedPongDeadlineNs = System.nanoTime() + ((long) DEGRADED_PONG_TIMEOUT_NS);
                Unit unit = Unit.INSTANCE;
                String name$iv = this.connectionName + " ping";
                this.writerQueue.schedule(new Task(name$iv, true, name$iv, true, this) { // from class: okhttp3.internal.http2.Http2Connection$sendDegradedPingLater$$inlined$execute$1
                    final /* synthetic */ boolean $cancelable;
                    final /* synthetic */ String $name;
                    final /* synthetic */ Http2Connection this$0;

                    {
                        this.$name = $captured_local_variable$1;
                        this.$cancelable = $captured_local_variable$2;
                        this.this$0 = r5;
                    }

                    @Override // okhttp3.internal.concurrent.Task
                    public long runOnce() {
                        this.this$0.writePing(false, 2, 0);
                        return -1;
                    }
                }, 0);
            }
        }
    }

    /* compiled from: Http2Connection.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0006\u00107\u001a\u000208J\u000e\u0010\u0011\u001a\u00020\u00002\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0017\u001a\u00020\u0018J\u000e\u0010\u001d\u001a\u00020\u00002\u0006\u0010\u001d\u001a\u00020\u001eJ.\u0010)\u001a\u00020\u00002\u0006\u0010)\u001a\u00020*2\b\b\u0002\u00109\u001a\u00020\f2\b\b\u0002\u0010/\u001a\u0002002\b\b\u0002\u0010#\u001a\u00020$H\u0007R\u001a\u0010\u0002\u001a\u00020\u0003X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\fX\u0080.¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u0012X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001a\u0010\u0017\u001a\u00020\u0018X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u001a\u0010\u001d\u001a\u00020\u001eX\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u001a\u0010#\u001a\u00020$X\u0080.¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010&\"\u0004\b'\u0010(R\u001a\u0010)\u001a\u00020*X\u0080.¢\u0006\u000e\n\u0000\u001a\u0004\b+\u0010,\"\u0004\b-\u0010.R\u001a\u0010/\u001a\u000200X\u0080.¢\u0006\u000e\n\u0000\u001a\u0004\b1\u00102\"\u0004\b3\u00104R\u0014\u0010\u0004\u001a\u00020\u0005X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b5\u00106¨\u0006:"}, d2 = {"Lokhttp3/internal/http2/Http2Connection$Builder;", "", "client", "", "taskRunner", "Lokhttp3/internal/concurrent/TaskRunner;", "(ZLokhttp3/internal/concurrent/TaskRunner;)V", "getClient$okhttp", "()Z", "setClient$okhttp", "(Z)V", "connectionName", "", "getConnectionName$okhttp", "()Ljava/lang/String;", "setConnectionName$okhttp", "(Ljava/lang/String;)V", ServiceSpecificExtraArgs.CastExtraArgs.LISTENER, "Lokhttp3/internal/http2/Http2Connection$Listener;", "getListener$okhttp", "()Lokhttp3/internal/http2/Http2Connection$Listener;", "setListener$okhttp", "(Lokhttp3/internal/http2/Http2Connection$Listener;)V", "pingIntervalMillis", "", "getPingIntervalMillis$okhttp", "()I", "setPingIntervalMillis$okhttp", "(I)V", "pushObserver", "Lokhttp3/internal/http2/PushObserver;", "getPushObserver$okhttp", "()Lokhttp3/internal/http2/PushObserver;", "setPushObserver$okhttp", "(Lokhttp3/internal/http2/PushObserver;)V", "sink", "Lokio/BufferedSink;", "getSink$okhttp", "()Lokio/BufferedSink;", "setSink$okhttp", "(Lokio/BufferedSink;)V", "socket", "Ljava/net/Socket;", "getSocket$okhttp", "()Ljava/net/Socket;", "setSocket$okhttp", "(Ljava/net/Socket;)V", FirebaseAnalytics.Param.SOURCE, "Lokio/BufferedSource;", "getSource$okhttp", "()Lokio/BufferedSource;", "setSource$okhttp", "(Lokio/BufferedSource;)V", "getTaskRunner$okhttp", "()Lokhttp3/internal/concurrent/TaskRunner;", "build", "Lokhttp3/internal/http2/Http2Connection;", "peerName", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Builder {
        private boolean client;
        public String connectionName;
        private int pingIntervalMillis;
        public BufferedSink sink;
        public Socket socket;
        public BufferedSource source;
        private final TaskRunner taskRunner;
        private Listener listener = Listener.REFUSE_INCOMING_STREAMS;
        private PushObserver pushObserver = PushObserver.CANCEL;

        public final Builder socket(Socket socket) throws IOException {
            return socket$default(this, socket, null, null, null, 14, null);
        }

        public final Builder socket(Socket socket, String str) throws IOException {
            return socket$default(this, socket, str, null, null, 12, null);
        }

        public final Builder socket(Socket socket, String str, BufferedSource bufferedSource) throws IOException {
            return socket$default(this, socket, str, bufferedSource, null, 8, null);
        }

        public Builder(boolean client, TaskRunner taskRunner) {
            Intrinsics.checkParameterIsNotNull(taskRunner, "taskRunner");
            this.client = client;
            this.taskRunner = taskRunner;
        }

        public final boolean getClient$okhttp() {
            return this.client;
        }

        public final void setClient$okhttp(boolean z) {
            this.client = z;
        }

        public final TaskRunner getTaskRunner$okhttp() {
            return this.taskRunner;
        }

        public final Socket getSocket$okhttp() {
            Socket socket = this.socket;
            if (socket == null) {
                Intrinsics.throwUninitializedPropertyAccessException("socket");
            }
            return socket;
        }

        public final void setSocket$okhttp(Socket socket) {
            Intrinsics.checkParameterIsNotNull(socket, "<set-?>");
            this.socket = socket;
        }

        public final String getConnectionName$okhttp() {
            String str = this.connectionName;
            if (str == null) {
                Intrinsics.throwUninitializedPropertyAccessException("connectionName");
            }
            return str;
        }

        public final void setConnectionName$okhttp(String str) {
            Intrinsics.checkParameterIsNotNull(str, "<set-?>");
            this.connectionName = str;
        }

        public final BufferedSource getSource$okhttp() {
            BufferedSource bufferedSource = this.source;
            if (bufferedSource == null) {
                Intrinsics.throwUninitializedPropertyAccessException(FirebaseAnalytics.Param.SOURCE);
            }
            return bufferedSource;
        }

        public final void setSource$okhttp(BufferedSource bufferedSource) {
            Intrinsics.checkParameterIsNotNull(bufferedSource, "<set-?>");
            this.source = bufferedSource;
        }

        public final BufferedSink getSink$okhttp() {
            BufferedSink bufferedSink = this.sink;
            if (bufferedSink == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sink");
            }
            return bufferedSink;
        }

        public final void setSink$okhttp(BufferedSink bufferedSink) {
            Intrinsics.checkParameterIsNotNull(bufferedSink, "<set-?>");
            this.sink = bufferedSink;
        }

        public final Listener getListener$okhttp() {
            return this.listener;
        }

        public final void setListener$okhttp(Listener listener) {
            Intrinsics.checkParameterIsNotNull(listener, "<set-?>");
            this.listener = listener;
        }

        public final PushObserver getPushObserver$okhttp() {
            return this.pushObserver;
        }

        public final void setPushObserver$okhttp(PushObserver pushObserver) {
            Intrinsics.checkParameterIsNotNull(pushObserver, "<set-?>");
            this.pushObserver = pushObserver;
        }

        public final int getPingIntervalMillis$okhttp() {
            return this.pingIntervalMillis;
        }

        public final void setPingIntervalMillis$okhttp(int i) {
            this.pingIntervalMillis = i;
        }

        public static /* synthetic */ Builder socket$default(Builder builder, Socket socket, String str, BufferedSource bufferedSource, BufferedSink bufferedSink, int i, Object obj) throws IOException {
            if ((i & 2) != 0) {
                str = Util.peerName(socket);
            }
            if ((i & 4) != 0) {
                bufferedSource = Okio.buffer(Okio.source(socket));
            }
            if ((i & 8) != 0) {
                bufferedSink = Okio.buffer(Okio.sink(socket));
            }
            return builder.socket(socket, str, bufferedSource, bufferedSink);
        }

        public final Builder socket(Socket socket, String peerName, BufferedSource source, BufferedSink sink) throws IOException {
            String str;
            Intrinsics.checkParameterIsNotNull(socket, "socket");
            Intrinsics.checkParameterIsNotNull(peerName, "peerName");
            Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
            Intrinsics.checkParameterIsNotNull(sink, "sink");
            Builder $this$apply = this;
            $this$apply.socket = socket;
            if ($this$apply.client) {
                str = Util.okHttpName + ' ' + peerName;
            } else {
                str = "MockWebServer " + peerName;
            }
            $this$apply.connectionName = str;
            $this$apply.source = source;
            $this$apply.sink = sink;
            return this;
        }

        public final Builder listener(Listener listener) {
            Intrinsics.checkParameterIsNotNull(listener, ServiceSpecificExtraArgs.CastExtraArgs.LISTENER);
            this.listener = listener;
            return this;
        }

        public final Builder pushObserver(PushObserver pushObserver) {
            Intrinsics.checkParameterIsNotNull(pushObserver, "pushObserver");
            this.pushObserver = pushObserver;
            return this;
        }

        public final Builder pingIntervalMillis(int pingIntervalMillis) {
            this.pingIntervalMillis = pingIntervalMillis;
            return this;
        }

        public final Http2Connection build() {
            return new Http2Connection(this);
        }
    }

    /* compiled from: Http2Connection.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0010\b\u0086\u0004\u0018\u00002\u00020\u00012\u00020\u0002B\u000f\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\b\u001a\u00020\tH\u0016J8\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\u0016\u0010\u0015\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019J(\u0010\u001a\u001a\u00020\t2\u0006\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\fH\u0016J \u0010\u001f\u001a\u00020\t2\u0006\u0010 \u001a\u00020\f2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u0010H\u0016J.\u0010$\u001a\u00020\t2\u0006\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010%\u001a\u00020\f2\f\u0010&\u001a\b\u0012\u0004\u0012\u00020(0'H\u0016J \u0010)\u001a\u00020\t2\u0006\u0010*\u001a\u00020\u00172\u0006\u0010+\u001a\u00020\f2\u0006\u0010,\u001a\u00020\fH\u0016J(\u0010-\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010.\u001a\u00020\f2\u0006\u0010/\u001a\u00020\f2\u0006\u00100\u001a\u00020\u0017H\u0016J&\u00101\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u00102\u001a\u00020\f2\f\u00103\u001a\b\u0012\u0004\u0012\u00020(0'H\u0016J\u0018\u00104\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010!\u001a\u00020\"H\u0016J\b\u00105\u001a\u00020\tH\u0016J\u0018\u0010\u0018\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\u0018\u00106\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u00107\u001a\u00020\u0014H\u0016R\u0014\u0010\u0003\u001a\u00020\u0004X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u00068"}, d2 = {"Lokhttp3/internal/http2/Http2Connection$ReaderRunnable;", "Ljava/lang/Runnable;", "Lokhttp3/internal/http2/Http2Reader$Handler;", "reader", "Lokhttp3/internal/http2/Http2Reader;", "(Lokhttp3/internal/http2/Http2Connection;Lokhttp3/internal/http2/Http2Reader;)V", "getReader$okhttp", "()Lokhttp3/internal/http2/Http2Reader;", "ackSettings", "", "alternateService", "streamId", "", "origin", "", "protocol", "Lokio/ByteString;", "host", "port", "maxAge", "", "applyAndAckSettings", "clearPrevious", "", "settings", "Lokhttp3/internal/http2/Settings;", UriUtil.DATA_SCHEME, "inFinished", FirebaseAnalytics.Param.SOURCE, "Lokio/BufferedSource;", "length", "goAway", "lastGoodStreamId", "errorCode", "Lokhttp3/internal/http2/ErrorCode;", "debugData", "headers", "associatedStreamId", "headerBlock", "", "Lokhttp3/internal/http2/Header;", "ping", "ack", "payload1", "payload2", "priority", "streamDependency", "weight", "exclusive", "pushPromise", "promisedStreamId", "requestHeaders", "rstStream", "run", "windowUpdate", "windowSizeIncrement", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public final class ReaderRunnable implements Runnable, Http2Reader.Handler {
        private final Http2Reader reader;
        final /* synthetic */ Http2Connection this$0;

        public ReaderRunnable(Http2Connection $outer, Http2Reader reader) {
            Intrinsics.checkParameterIsNotNull(reader, "reader");
            this.this$0 = $outer;
            this.reader = reader;
        }

        public final Http2Reader getReader$okhttp() {
            return this.reader;
        }

        @Override // java.lang.Runnable
        public void run() {
            IOException errorException;
            ErrorCode streamErrorCode;
            ErrorCode connectionErrorCode;
            try {
                connectionErrorCode = ErrorCode.INTERNAL_ERROR;
                streamErrorCode = ErrorCode.INTERNAL_ERROR;
                errorException = null;
                try {
                    this.reader.readConnectionPreface(this);
                    while (this.reader.nextFrame(false, this)) {
                    }
                    connectionErrorCode = ErrorCode.NO_ERROR;
                    streamErrorCode = ErrorCode.CANCEL;
                } catch (IOException e) {
                    errorException = e;
                    connectionErrorCode = ErrorCode.PROTOCOL_ERROR;
                    streamErrorCode = ErrorCode.PROTOCOL_ERROR;
                }
            } finally {
                this.this$0.close$okhttp(connectionErrorCode, streamErrorCode, errorException);
                Util.closeQuietly(this.reader);
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void data(boolean inFinished, int streamId, BufferedSource source, int length) throws IOException {
            Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
            if (this.this$0.pushedStream$okhttp(streamId)) {
                this.this$0.pushDataLater$okhttp(streamId, source, length, inFinished);
                return;
            }
            Http2Stream dataStream = this.this$0.getStream(streamId);
            if (dataStream == null) {
                this.this$0.writeSynResetLater$okhttp(streamId, ErrorCode.PROTOCOL_ERROR);
                this.this$0.updateConnectionFlowControl$okhttp((long) length);
                source.skip((long) length);
                return;
            }
            dataStream.receiveData(source, length);
            if (inFinished) {
                dataStream.receiveHeaders(Util.EMPTY_HEADERS, true);
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void headers(boolean inFinished, int streamId, int associatedStreamId, List<Header> list) {
            Http2Connection http2Connection;
            Throwable th;
            Intrinsics.checkParameterIsNotNull(list, "headerBlock");
            if (this.this$0.pushedStream$okhttp(streamId)) {
                this.this$0.pushHeadersLater$okhttp(streamId, list, inFinished);
                return;
            }
            Http2Connection http2Connection2 = this.this$0;
            synchronized (http2Connection2) {
                try {
                    Http2Stream stream = this.this$0.getStream(streamId);
                    if (stream == null) {
                        try {
                            if (!this.this$0.isShutdown) {
                                if (streamId > this.this$0.getLastGoodStreamId$okhttp()) {
                                    if (streamId % 2 != this.this$0.getNextStreamId$okhttp() % 2) {
                                        Http2Stream newStream = new Http2Stream(streamId, this.this$0, false, inFinished, Util.toHeaders(list));
                                        this.this$0.setLastGoodStreamId$okhttp(streamId);
                                        this.this$0.getStreams$okhttp().put(Integer.valueOf(streamId), newStream);
                                        String name$iv = this.this$0.getConnectionName$okhttp() + '[' + streamId + "] onStream";
                                        http2Connection = http2Connection2;
                                        try {
                                            this.this$0.taskRunner.newQueue().schedule(new Http2Connection$ReaderRunnable$headers$$inlined$synchronized$lambda$1(name$iv, true, name$iv, true, newStream, this, stream, streamId, list, inFinished), 0);
                                            return;
                                        } catch (Throwable th2) {
                                            th = th2;
                                        }
                                    } else {
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            } else {
                                return;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            http2Connection = http2Connection2;
                        }
                    } else {
                        http2Connection = http2Connection2;
                        try {
                            Unit unit = Unit.INSTANCE;
                            stream.receiveHeaders(Util.toHeaders(list), inFinished);
                            return;
                        } catch (Throwable th4) {
                            th = th4;
                        }
                    }
                } catch (Throwable th5) {
                    th = th5;
                    http2Connection = http2Connection2;
                }
                throw th;
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void rstStream(int streamId, ErrorCode errorCode) {
            Intrinsics.checkParameterIsNotNull(errorCode, "errorCode");
            if (this.this$0.pushedStream$okhttp(streamId)) {
                this.this$0.pushResetLater$okhttp(streamId, errorCode);
                return;
            }
            Http2Stream rstStream = this.this$0.removeStream$okhttp(streamId);
            if (rstStream != null) {
                rstStream.receiveRstStream(errorCode);
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void settings(boolean clearPrevious, Settings settings) {
            Intrinsics.checkParameterIsNotNull(settings, "settings");
            String name$iv = this.this$0.getConnectionName$okhttp() + " applyAndAckSettings";
            this.this$0.writerQueue.schedule(new Http2Connection$ReaderRunnable$settings$$inlined$execute$1(name$iv, true, name$iv, true, this, clearPrevious, settings), 0);
        }

        /* JADX WARN: Can't wrap try/catch for region: R(23:7|72|8|9|(2:74|11)(1:14)|15|16|(2:21|(15:23|27|28|83|29|77|30|75|31|87|32|37|38|39|(7:41|(1:43)|44|(3:46|12a|51)|89|54|90)(2:55|56))(2:24|25))|26|27|28|83|29|77|30|75|31|87|32|37|38|39|(0)(0)) */
        /* JADX WARN: Code restructure failed: missing block: B:33:0x0106, code lost:
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:35:0x010a, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:36:0x010b, code lost:
            r28.this$0.failConnection(r0);
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:41:0x011a  */
        /* JADX WARN: Removed duplicated region for block: B:55:0x0142  */
        /* JADX WARN: Type inference failed for: r1v28, types: [T, okhttp3.internal.http2.Settings] */
        /* JADX WARN: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump */
        public final void applyAndAckSettings(boolean clearPrevious, Settings settings) {
            Throwable th;
            Http2Connection http2Connection;
            Throwable th2;
            T t;
            Intrinsics.checkParameterIsNotNull(settings, "settings");
            Ref.LongRef delta = new Ref.LongRef();
            Ref.ObjectRef streamsToNotify = new Ref.ObjectRef();
            Ref.ObjectRef newPeerSettings = new Ref.ObjectRef();
            synchronized (this.this$0.getWriter()) {
                try {
                    Http2Connection http2Connection2 = this.this$0;
                    synchronized (http2Connection2) {
                        try {
                            Settings previousPeerSettings = this.this$0.getPeerSettings();
                            if (clearPrevious) {
                                try {
                                    newPeerSettings.element = settings;
                                } catch (Throwable th3) {
                                    th2 = th3;
                                    http2Connection = http2Connection2;
                                    try {
                                        throw th2;
                                    } catch (Throwable th4) {
                                        th = th4;
                                        throw th;
                                    }
                                }
                            } else {
                                ?? settings2 = new Settings();
                                settings2.merge(previousPeerSettings);
                                settings2.merge(settings);
                                newPeerSettings.element = settings2;
                            }
                            delta.element = ((long) ((Settings) newPeerSettings.element).getInitialWindowSize()) - ((long) previousPeerSettings.getInitialWindowSize());
                        } catch (Throwable th5) {
                            th2 = th5;
                            http2Connection = http2Connection2;
                        }
                        try {
                            try {
                                try {
                                    if (delta.element != 0 && !this.this$0.getStreams$okhttp().isEmpty()) {
                                        Object[] array = this.this$0.getStreams$okhttp().values().toArray(new Http2Stream[0]);
                                        if (array != null) {
                                            t = (Http2Stream[]) array;
                                            streamsToNotify.element = t;
                                            this.this$0.setPeerSettings((Settings) newPeerSettings.element);
                                            String name$iv = this.this$0.getConnectionName$okhttp() + " onSettings";
                                            http2Connection = http2Connection2;
                                            Ref.LongRef delta2 = delta;
                                            this.this$0.settingsListenerQueue.schedule(new Http2Connection$ReaderRunnable$applyAndAckSettings$$inlined$synchronized$lambda$1(name$iv, true, name$iv, true, this, clearPrevious, newPeerSettings, settings, delta, streamsToNotify), 0);
                                            Unit unit = Unit.INSTANCE;
                                            this.this$0.getWriter().applyAndAckSettings((Settings) newPeerSettings.element);
                                            Unit unit2 = Unit.INSTANCE;
                                            if (((Http2Stream[]) streamsToNotify.element) == null) {
                                                Http2Stream[] http2StreamArr = (Http2Stream[]) streamsToNotify.element;
                                                if (http2StreamArr == null) {
                                                    Intrinsics.throwNpe();
                                                }
                                                int length = http2StreamArr.length;
                                                int i = 0;
                                                while (i < length) {
                                                    Http2Stream stream = http2StreamArr[i];
                                                    synchronized (stream) {
                                                        stream.addBytesToWriteWindow(delta2.element);
                                                        Unit unit3 = Unit.INSTANCE;
                                                    }
                                                    i++;
                                                    delta2 = delta2;
                                                }
                                                return;
                                            }
                                            return;
                                        }
                                        throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                                    }
                                    this.this$0.getWriter().applyAndAckSettings((Settings) newPeerSettings.element);
                                    Unit unit22 = Unit.INSTANCE;
                                    if (((Http2Stream[]) streamsToNotify.element) == null) {
                                    }
                                } catch (Throwable th6) {
                                    th = th6;
                                    throw th;
                                }
                            } catch (Throwable th7) {
                                th = th7;
                            }
                            this.this$0.settingsListenerQueue.schedule(new Http2Connection$ReaderRunnable$applyAndAckSettings$$inlined$synchronized$lambda$1(name$iv, true, name$iv, true, this, clearPrevious, newPeerSettings, settings, delta, streamsToNotify), 0);
                            Unit unit4 = Unit.INSTANCE;
                        } catch (Throwable th8) {
                            th2 = th8;
                            throw th2;
                        }
                        t = 0;
                        streamsToNotify.element = t;
                        this.this$0.setPeerSettings((Settings) newPeerSettings.element);
                        String name$iv2 = this.this$0.getConnectionName$okhttp() + " onSettings";
                        http2Connection = http2Connection2;
                        Ref.LongRef delta22 = delta;
                    }
                } catch (Throwable th9) {
                    th = th9;
                }
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void ackSettings() {
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void ping(boolean ack, int payload1, int payload2) {
            if (ack) {
                synchronized (this.this$0) {
                    try {
                        if (payload1 == 1) {
                            Http2Connection http2Connection = this.this$0;
                            long j = http2Connection.intervalPongsReceived;
                            http2Connection.intervalPongsReceived = 1 + j;
                            Long.valueOf(j);
                        } else if (payload1 == 2) {
                            Http2Connection http2Connection2 = this.this$0;
                            long j2 = http2Connection2.degradedPongsReceived;
                            http2Connection2.degradedPongsReceived = 1 + j2;
                            Long.valueOf(j2);
                        } else if (payload1 != 3) {
                            Unit unit = Unit.INSTANCE;
                        } else {
                            this.this$0.awaitPongsReceived++;
                            Object $this$notifyAll$iv = this.this$0;
                            if ($this$notifyAll$iv != null) {
                                $this$notifyAll$iv.notifyAll();
                                Object $this$notifyAll$iv2 = Unit.INSTANCE;
                            } else {
                                throw new TypeCastException("null cannot be cast to non-null type java.lang.Object");
                            }
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                return;
            }
            String name$iv = this.this$0.getConnectionName$okhttp() + " ping";
            this.this$0.writerQueue.schedule(new Http2Connection$ReaderRunnable$ping$$inlined$execute$1(name$iv, true, name$iv, true, this, payload1, payload2), 0);
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void goAway(int lastGoodStreamId, ErrorCode errorCode, ByteString debugData) {
            int i;
            Http2Stream[] streamsCopy;
            Intrinsics.checkParameterIsNotNull(errorCode, "errorCode");
            Intrinsics.checkParameterIsNotNull(debugData, "debugData");
            debugData.size();
            synchronized (this.this$0) {
                Object[] array = this.this$0.getStreams$okhttp().values().toArray(new Http2Stream[0]);
                if (array != null) {
                    streamsCopy = (Http2Stream[]) array;
                    this.this$0.isShutdown = true;
                    Unit unit = Unit.INSTANCE;
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                }
            }
            for (Http2Stream http2Stream : streamsCopy) {
                if (http2Stream.getId() > lastGoodStreamId && http2Stream.isLocallyInitiated()) {
                    http2Stream.receiveRstStream(ErrorCode.REFUSED_STREAM);
                    this.this$0.removeStream$okhttp(http2Stream.getId());
                }
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void windowUpdate(int streamId, long windowSizeIncrement) {
            if (streamId == 0) {
                synchronized (this.this$0) {
                    Http2Connection http2Connection = this.this$0;
                    http2Connection.writeBytesMaximum = http2Connection.getWriteBytesMaximum() + windowSizeIncrement;
                    Object $this$notifyAll$iv = this.this$0;
                    if ($this$notifyAll$iv != null) {
                        $this$notifyAll$iv.notifyAll();
                        Unit unit = Unit.INSTANCE;
                    } else {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.Object");
                    }
                }
                return;
            }
            Http2Stream stream = this.this$0.getStream(streamId);
            if (stream != null) {
                synchronized (stream) {
                    stream.addBytesToWriteWindow(windowSizeIncrement);
                    Unit unit2 = Unit.INSTANCE;
                }
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void priority(int streamId, int streamDependency, int weight, boolean exclusive) {
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void pushPromise(int streamId, int promisedStreamId, List<Header> list) {
            Intrinsics.checkParameterIsNotNull(list, "requestHeaders");
            this.this$0.pushRequestLater$okhttp(promisedStreamId, list);
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void alternateService(int streamId, String origin, ByteString protocol, String host, int port, long maxAge) {
            Intrinsics.checkParameterIsNotNull(origin, "origin");
            Intrinsics.checkParameterIsNotNull(protocol, "protocol");
            Intrinsics.checkParameterIsNotNull(host, "host");
        }
    }

    public final boolean pushedStream$okhttp(int streamId) {
        return streamId != 0 && (streamId & 1) == 0;
    }

    public final void pushRequestLater$okhttp(int streamId, List<Header> list) {
        Intrinsics.checkParameterIsNotNull(list, "requestHeaders");
        synchronized (this) {
            if (this.currentPushRequests.contains(Integer.valueOf(streamId))) {
                writeSynResetLater$okhttp(streamId, ErrorCode.PROTOCOL_ERROR);
                return;
            }
            this.currentPushRequests.add(Integer.valueOf(streamId));
            String name$iv = this.connectionName + '[' + streamId + "] onRequest";
            this.pushQueue.schedule(new Task(name$iv, true, name$iv, true, this, streamId, list) { // from class: okhttp3.internal.http2.Http2Connection$pushRequestLater$$inlined$execute$1
                final /* synthetic */ boolean $cancelable;
                final /* synthetic */ String $name;
                final /* synthetic */ List $requestHeaders$inlined;
                final /* synthetic */ int $streamId$inlined;
                final /* synthetic */ Http2Connection this$0;

                {
                    this.$name = $captured_local_variable$1;
                    this.$cancelable = $captured_local_variable$2;
                    this.this$0 = r5;
                    this.$streamId$inlined = r6;
                    this.$requestHeaders$inlined = r7;
                }

                @Override // okhttp3.internal.concurrent.Task
                public long runOnce() {
                    if (!this.this$0.pushObserver.onRequest(this.$streamId$inlined, this.$requestHeaders$inlined)) {
                        return -1;
                    }
                    try {
                        this.this$0.getWriter().rstStream(this.$streamId$inlined, ErrorCode.CANCEL);
                        synchronized (this.this$0) {
                            this.this$0.currentPushRequests.remove(Integer.valueOf(this.$streamId$inlined));
                        }
                        return -1;
                    } catch (IOException e) {
                        return -1;
                    }
                }
            }, 0);
        }
    }

    public final void pushHeadersLater$okhttp(int streamId, List<Header> list, boolean inFinished) {
        Intrinsics.checkParameterIsNotNull(list, "requestHeaders");
        String name$iv = this.connectionName + '[' + streamId + "] onHeaders";
        this.pushQueue.schedule(new Task(name$iv, true, name$iv, true, this, streamId, list, inFinished) { // from class: okhttp3.internal.http2.Http2Connection$pushHeadersLater$$inlined$execute$1
            final /* synthetic */ boolean $cancelable;
            final /* synthetic */ boolean $inFinished$inlined;
            final /* synthetic */ String $name;
            final /* synthetic */ List $requestHeaders$inlined;
            final /* synthetic */ int $streamId$inlined;
            final /* synthetic */ Http2Connection this$0;

            {
                this.$name = $captured_local_variable$1;
                this.$cancelable = $captured_local_variable$2;
                this.this$0 = r5;
                this.$streamId$inlined = r6;
                this.$requestHeaders$inlined = r7;
                this.$inFinished$inlined = r8;
            }

            @Override // okhttp3.internal.concurrent.Task
            public long runOnce() {
                boolean cancel = this.this$0.pushObserver.onHeaders(this.$streamId$inlined, this.$requestHeaders$inlined, this.$inFinished$inlined);
                if (cancel) {
                    try {
                        this.this$0.getWriter().rstStream(this.$streamId$inlined, ErrorCode.CANCEL);
                    } catch (IOException e) {
                        return -1;
                    }
                }
                if (cancel || this.$inFinished$inlined) {
                    synchronized (this.this$0) {
                        this.this$0.currentPushRequests.remove(Integer.valueOf(this.$streamId$inlined));
                    }
                }
                return -1;
            }
        }, 0);
    }

    public final void pushDataLater$okhttp(int streamId, BufferedSource source, int byteCount, boolean inFinished) throws IOException {
        Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
        Buffer buffer = new Buffer();
        source.require((long) byteCount);
        source.read(buffer, (long) byteCount);
        String name$iv = this.connectionName + '[' + streamId + "] onData";
        this.pushQueue.schedule(new Task(name$iv, true, name$iv, true, this, streamId, buffer, byteCount, inFinished) { // from class: okhttp3.internal.http2.Http2Connection$pushDataLater$$inlined$execute$1
            final /* synthetic */ Buffer $buffer$inlined;
            final /* synthetic */ int $byteCount$inlined;
            final /* synthetic */ boolean $cancelable;
            final /* synthetic */ boolean $inFinished$inlined;
            final /* synthetic */ String $name;
            final /* synthetic */ int $streamId$inlined;
            final /* synthetic */ Http2Connection this$0;

            {
                this.$name = $captured_local_variable$1;
                this.$cancelable = $captured_local_variable$2;
                this.this$0 = r5;
                this.$streamId$inlined = r6;
                this.$buffer$inlined = r7;
                this.$byteCount$inlined = r8;
                this.$inFinished$inlined = r9;
            }

            @Override // okhttp3.internal.concurrent.Task
            public long runOnce() {
                try {
                    boolean cancel = this.this$0.pushObserver.onData(this.$streamId$inlined, this.$buffer$inlined, this.$byteCount$inlined, this.$inFinished$inlined);
                    if (cancel) {
                        this.this$0.getWriter().rstStream(this.$streamId$inlined, ErrorCode.CANCEL);
                    }
                    if (cancel || this.$inFinished$inlined) {
                        synchronized (this.this$0) {
                            this.this$0.currentPushRequests.remove(Integer.valueOf(this.$streamId$inlined));
                        }
                    }
                    return -1;
                } catch (IOException e) {
                    return -1;
                }
            }
        }, 0);
    }

    public final void pushResetLater$okhttp(int streamId, ErrorCode errorCode) {
        Intrinsics.checkParameterIsNotNull(errorCode, "errorCode");
        String name$iv = this.connectionName + '[' + streamId + "] onReset";
        this.pushQueue.schedule(new Task(name$iv, true, name$iv, true, this, streamId, errorCode) { // from class: okhttp3.internal.http2.Http2Connection$pushResetLater$$inlined$execute$1
            final /* synthetic */ boolean $cancelable;
            final /* synthetic */ ErrorCode $errorCode$inlined;
            final /* synthetic */ String $name;
            final /* synthetic */ int $streamId$inlined;
            final /* synthetic */ Http2Connection this$0;

            {
                this.$name = $captured_local_variable$1;
                this.$cancelable = $captured_local_variable$2;
                this.this$0 = r5;
                this.$streamId$inlined = r6;
                this.$errorCode$inlined = r7;
            }

            @Override // okhttp3.internal.concurrent.Task
            public long runOnce() {
                this.this$0.pushObserver.onReset(this.$streamId$inlined, this.$errorCode$inlined);
                synchronized (this.this$0) {
                    this.this$0.currentPushRequests.remove(Integer.valueOf(this.$streamId$inlined));
                }
                return -1;
            }
        }, 0);
    }

    /* compiled from: Http2Connection.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018\u0000 \f2\u00020\u0001:\u0001\fB\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bH&¨\u0006\r"}, d2 = {"Lokhttp3/internal/http2/Http2Connection$Listener;", "", "()V", "onSettings", "", "connection", "Lokhttp3/internal/http2/Http2Connection;", "settings", "Lokhttp3/internal/http2/Settings;", "onStream", "stream", "Lokhttp3/internal/http2/Http2Stream;", "Companion", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static abstract class Listener {
        public static final Companion Companion = new Companion(null);
        public static final Listener REFUSE_INCOMING_STREAMS = new Http2Connection$Listener$Companion$REFUSE_INCOMING_STREAMS$1();

        public abstract void onStream(Http2Stream http2Stream) throws IOException;

        public void onSettings(Http2Connection connection, Settings settings) {
            Intrinsics.checkParameterIsNotNull(connection, "connection");
            Intrinsics.checkParameterIsNotNull(settings, "settings");
        }

        /* compiled from: Http2Connection.kt */
        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lokhttp3/internal/http2/Http2Connection$Listener$Companion;", "", "()V", "REFUSE_INCOMING_STREAMS", "Lokhttp3/internal/http2/Http2Connection$Listener;", "okhttp"}, k = 1, mv = {1, 1, 16})
        /* loaded from: classes3.dex */
        public static final class Companion {
            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
                this();
            }
        }
    }

    /* compiled from: Http2Connection.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lokhttp3/internal/http2/Http2Connection$Companion;", "", "()V", "AWAIT_PING", "", "DEFAULT_SETTINGS", "Lokhttp3/internal/http2/Settings;", "getDEFAULT_SETTINGS", "()Lokhttp3/internal/http2/Settings;", "DEGRADED_PING", "DEGRADED_PONG_TIMEOUT_NS", "INTERVAL_PING", "OKHTTP_CLIENT_WINDOW_SIZE", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        public final Settings getDEFAULT_SETTINGS() {
            return Http2Connection.DEFAULT_SETTINGS;
        }
    }

    static {
        Settings $this$apply = new Settings();
        $this$apply.set(7, 65535);
        $this$apply.set(5, 16384);
        DEFAULT_SETTINGS = $this$apply;
    }
}
