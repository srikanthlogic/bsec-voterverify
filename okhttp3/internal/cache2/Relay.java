package okhttp3.internal.cache2;

import com.facebook.common.util.UriUtil;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.ByteString;
import okio.Source;
import okio.Timeout;
/* compiled from: Relay.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\t\u0018\u0000 :2\u00020\u0001:\u0002:;B3\b\u0002\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u0007¢\u0006\u0002\u0010\u000bJ\u000e\u00102\u001a\u0002032\u0006\u00104\u001a\u00020\u0007J\u0006\u0010\b\u001a\u00020\tJ\b\u00105\u001a\u0004\u0018\u00010\u0005J \u00106\u001a\u0002032\u0006\u00107\u001a\u00020\t2\u0006\u00104\u001a\u00020\u00072\u0006\u00108\u001a\u00020\u0007H\u0002J\u0010\u00109\u001a\u0002032\u0006\u00104\u001a\u00020\u0007H\u0002R\u0011\u0010\f\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\n\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u00020\u0013X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u0011\u0010\u001c\u001a\u00020\u00138F¢\u0006\u0006\u001a\u0004\b\u001c\u0010\u0015R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u001d\u001a\u00020\u001eX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u0011\u0010'\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b(\u0010\u000fR\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u0011\"\u0004\b*\u0010+R\u001c\u0010,\u001a\u0004\u0018\u00010-X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b.\u0010/\"\u0004\b0\u00101¨\u0006<"}, d2 = {"Lokhttp3/internal/cache2/Relay;", "", UriUtil.LOCAL_FILE_SCHEME, "Ljava/io/RandomAccessFile;", "upstream", "Lokio/Source;", "upstreamPos", "", "metadata", "Lokio/ByteString;", "bufferMaxSize", "(Ljava/io/RandomAccessFile;Lokio/Source;JLokio/ByteString;J)V", "buffer", "Lokio/Buffer;", "getBuffer", "()Lokio/Buffer;", "getBufferMaxSize", "()J", "complete", "", "getComplete", "()Z", "setComplete", "(Z)V", "getFile", "()Ljava/io/RandomAccessFile;", "setFile", "(Ljava/io/RandomAccessFile;)V", "isClosed", "sourceCount", "", "getSourceCount", "()I", "setSourceCount", "(I)V", "getUpstream", "()Lokio/Source;", "setUpstream", "(Lokio/Source;)V", "upstreamBuffer", "getUpstreamBuffer", "getUpstreamPos", "setUpstreamPos", "(J)V", "upstreamReader", "Ljava/lang/Thread;", "getUpstreamReader", "()Ljava/lang/Thread;", "setUpstreamReader", "(Ljava/lang/Thread;)V", "commit", "", "upstreamSize", "newSource", "writeHeader", "prefix", "metadataSize", "writeMetadata", "Companion", "RelaySource", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class Relay {
    private static final long FILE_HEADER_SIZE = 32;
    private static final int SOURCE_FILE = 2;
    private static final int SOURCE_UPSTREAM = 1;
    private final Buffer buffer;
    private final long bufferMaxSize;
    private boolean complete;
    private RandomAccessFile file;
    private final ByteString metadata;
    private int sourceCount;
    private Source upstream;
    private final Buffer upstreamBuffer;
    private long upstreamPos;
    private Thread upstreamReader;
    public static final Companion Companion = new Companion(null);
    public static final ByteString PREFIX_CLEAN = ByteString.Companion.encodeUtf8("OkHttp cache v1\n");
    public static final ByteString PREFIX_DIRTY = ByteString.Companion.encodeUtf8("OkHttp DIRTY :(\n");

    private Relay(RandomAccessFile file, Source upstream, long upstreamPos, ByteString metadata, long bufferMaxSize) {
        this.file = file;
        this.upstream = upstream;
        this.upstreamPos = upstreamPos;
        this.metadata = metadata;
        this.bufferMaxSize = bufferMaxSize;
        this.upstreamBuffer = new Buffer();
        this.complete = this.upstream == null;
        this.buffer = new Buffer();
    }

    public /* synthetic */ Relay(RandomAccessFile file, Source upstream, long upstreamPos, ByteString metadata, long bufferMaxSize, DefaultConstructorMarker $constructor_marker) {
        this(file, upstream, upstreamPos, metadata, bufferMaxSize);
    }

    public final RandomAccessFile getFile() {
        return this.file;
    }

    public final void setFile(RandomAccessFile randomAccessFile) {
        this.file = randomAccessFile;
    }

    public final Source getUpstream() {
        return this.upstream;
    }

    public final void setUpstream(Source source) {
        this.upstream = source;
    }

    public final long getUpstreamPos() {
        return this.upstreamPos;
    }

    public final void setUpstreamPos(long j) {
        this.upstreamPos = j;
    }

    public final long getBufferMaxSize() {
        return this.bufferMaxSize;
    }

    public final Thread getUpstreamReader() {
        return this.upstreamReader;
    }

    public final void setUpstreamReader(Thread thread) {
        this.upstreamReader = thread;
    }

    public final Buffer getUpstreamBuffer() {
        return this.upstreamBuffer;
    }

    public final boolean getComplete() {
        return this.complete;
    }

    public final void setComplete(boolean z) {
        this.complete = z;
    }

    public final Buffer getBuffer() {
        return this.buffer;
    }

    public final int getSourceCount() {
        return this.sourceCount;
    }

    public final void setSourceCount(int i) {
        this.sourceCount = i;
    }

    public final boolean isClosed() {
        return this.file == null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: Multiple debug info for r3v0 okio.Buffer: [D('header' okio.Buffer), D('$this$apply' okio.Buffer)] */
    public final void writeHeader(ByteString prefix, long upstreamSize, long metadataSize) throws IOException {
        Buffer $this$apply = new Buffer();
        $this$apply.write(prefix);
        $this$apply.writeLong(upstreamSize);
        $this$apply.writeLong(metadataSize);
        if ($this$apply.size() == 32) {
            RandomAccessFile randomAccessFile = this.file;
            if (randomAccessFile == null) {
                Intrinsics.throwNpe();
            }
            FileChannel channel = randomAccessFile.getChannel();
            Intrinsics.checkExpressionValueIsNotNull(channel, "file!!.channel");
            new FileOperator(channel).write(0, $this$apply, 32);
            return;
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }

    private final void writeMetadata(long upstreamSize) throws IOException {
        Buffer metadataBuffer = new Buffer();
        metadataBuffer.write(this.metadata);
        RandomAccessFile randomAccessFile = this.file;
        if (randomAccessFile == null) {
            Intrinsics.throwNpe();
        }
        FileChannel channel = randomAccessFile.getChannel();
        Intrinsics.checkExpressionValueIsNotNull(channel, "file!!.channel");
        new FileOperator(channel).write(32 + upstreamSize, metadataBuffer, (long) this.metadata.size());
    }

    public final void commit(long upstreamSize) throws IOException {
        writeMetadata(upstreamSize);
        RandomAccessFile randomAccessFile = this.file;
        if (randomAccessFile == null) {
            Intrinsics.throwNpe();
        }
        randomAccessFile.getChannel().force(false);
        writeHeader(PREFIX_CLEAN, upstreamSize, (long) this.metadata.size());
        RandomAccessFile randomAccessFile2 = this.file;
        if (randomAccessFile2 == null) {
            Intrinsics.throwNpe();
        }
        randomAccessFile2.getChannel().force(false);
        synchronized (this) {
            this.complete = true;
            Unit unit = Unit.INSTANCE;
        }
        Source source = this.upstream;
        if (source != null) {
            Util.closeQuietly(source);
        }
        this.upstream = null;
    }

    public final ByteString metadata() {
        return this.metadata;
    }

    public final Source newSource() {
        synchronized (this) {
            if (this.file == null) {
                return null;
            }
            this.sourceCount++;
            return new RelaySource();
        }
    }

    /* compiled from: Relay.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0080\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0006H\u0016J\b\u0010\u0007\u001a\u00020\bH\u0016R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lokhttp3/internal/cache2/Relay$RelaySource;", "Lokio/Source;", "(Lokhttp3/internal/cache2/Relay;)V", "fileOperator", "Lokhttp3/internal/cache2/FileOperator;", "sourcePos", "", "timeout", "Lokio/Timeout;", "close", "", "read", "sink", "Lokio/Buffer;", "byteCount", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public final class RelaySource implements Source {
        private FileOperator fileOperator;
        private long sourcePos;
        private final Timeout timeout = new Timeout();

        /* JADX WARN: Incorrect args count in method signature: ()V */
        public RelaySource() {
            RandomAccessFile file = Relay.this.getFile();
            if (file == null) {
                Intrinsics.throwNpe();
            }
            FileChannel channel = file.getChannel();
            Intrinsics.checkExpressionValueIsNotNull(channel, "file!!.channel");
            this.fileOperator = new FileOperator(channel);
        }

        /* JADX WARN: Code restructure failed: missing block: B:29:0x0091, code lost:
            if (r4 != 2) goto L_0x00b6;
         */
        /* JADX WARN: Code restructure failed: missing block: B:30:0x0093, code lost:
            r12 = java.lang.Math.min(r27, r25.this$0.getUpstreamPos() - r25.sourcePos);
            r4 = r25.fileOperator;
         */
        /* JADX WARN: Code restructure failed: missing block: B:31:0x00a2, code lost:
            if (r4 != null) goto L_0x00a7;
         */
        /* JADX WARN: Code restructure failed: missing block: B:32:0x00a4, code lost:
            kotlin.jvm.internal.Intrinsics.throwNpe();
         */
        /* JADX WARN: Code restructure failed: missing block: B:33:0x00a7, code lost:
            r4.read(r25.sourcePos + 32, r26, r12);
            r25.sourcePos += r12;
         */
        /* JADX WARN: Code restructure failed: missing block: B:34:0x00b5, code lost:
            return r12;
         */
        /* JADX WARN: Code restructure failed: missing block: B:36:0x00b9, code lost:
            r0 = r25.this$0.getUpstream();
         */
        /* JADX WARN: Code restructure failed: missing block: B:37:0x00bf, code lost:
            if (r0 != null) goto L_0x00c4;
         */
        /* JADX WARN: Code restructure failed: missing block: B:38:0x00c1, code lost:
            kotlin.jvm.internal.Intrinsics.throwNpe();
         */
        /* JADX WARN: Code restructure failed: missing block: B:39:0x00c4, code lost:
            r4 = r0.read(r25.this$0.getUpstreamBuffer(), r25.this$0.getBufferMaxSize());
         */
        /* JADX WARN: Code restructure failed: missing block: B:40:0x00d7, code lost:
            if (r4 != -1) goto L_0x010c;
         */
        /* JADX WARN: Code restructure failed: missing block: B:41:0x00d9, code lost:
            r25.this$0.commit(r25.this$0.getUpstreamPos());
         */
        /* JADX WARN: Code restructure failed: missing block: B:42:0x00e5, code lost:
            r4 = r25.this$0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:43:0x00e7, code lost:
            monitor-enter(r4);
         */
        /* JADX WARN: Code restructure failed: missing block: B:45:0x00e9, code lost:
            r25.this$0.setUpstreamReader(null);
            r5 = r25.this$0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:46:0x00f5, code lost:
            if (r5 == null) goto L_0x0101;
         */
        /* JADX WARN: Code restructure failed: missing block: B:47:0x00f7, code lost:
            r5.notifyAll();
            r0 = kotlin.Unit.INSTANCE;
         */
        /* JADX WARN: Code restructure failed: missing block: B:48:0x00ff, code lost:
            monitor-exit(r4);
         */
        /* JADX WARN: Code restructure failed: missing block: B:49:0x0100, code lost:
            return -1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:51:0x0108, code lost:
            throw new kotlin.TypeCastException("null cannot be cast to non-null type java.lang.Object");
         */
        /* JADX WARN: Code restructure failed: missing block: B:52:0x0109, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:53:0x010b, code lost:
            throw r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:54:0x010c, code lost:
            r4 = java.lang.Math.min(r4, r27);
            r25.this$0.getUpstreamBuffer().copyTo(r26, 0, r4);
            r25.sourcePos += r4;
            r0 = r25.fileOperator;
         */
        /* JADX WARN: Code restructure failed: missing block: B:55:0x0128, code lost:
            if (r0 != null) goto L_0x012d;
         */
        /* JADX WARN: Code restructure failed: missing block: B:56:0x012a, code lost:
            kotlin.jvm.internal.Intrinsics.throwNpe();
         */
        /* JADX WARN: Code restructure failed: missing block: B:57:0x012d, code lost:
            r0.write(r25.this$0.getUpstreamPos() + 32, r25.this$0.getUpstreamBuffer().clone(), r4);
            r4 = r25.this$0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:58:0x0148, code lost:
            monitor-enter(r4);
         */
        /* JADX WARN: Code restructure failed: missing block: B:62:0x0158, code lost:
            r25.this$0.getBuffer().write(r25.this$0.getUpstreamBuffer(), r4);
         */
        /* JADX WARN: Code restructure failed: missing block: B:63:0x016d, code lost:
            if (r25.this$0.getBuffer().size() <= r25.this$0.getBufferMaxSize()) goto L_0x018a;
         */
        /* JADX WARN: Code restructure failed: missing block: B:64:0x016f, code lost:
            r25.this$0.getBuffer().skip(r25.this$0.getBuffer().size() - r25.this$0.getBufferMaxSize());
         */
        /* JADX WARN: Code restructure failed: missing block: B:65:0x018a, code lost:
            r5 = r25.this$0;
            r5.setUpstreamPos(r5.getUpstreamPos() + r4);
            r0 = kotlin.Unit.INSTANCE;
         */
        /* JADX WARN: Code restructure failed: missing block: B:66:0x0196, code lost:
            monitor-exit(r4);
         */
        /* JADX WARN: Code restructure failed: missing block: B:67:0x0198, code lost:
            r4 = r25.this$0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:68:0x019a, code lost:
            monitor-enter(r4);
         */
        /* JADX WARN: Code restructure failed: missing block: B:70:0x019c, code lost:
            r25.this$0.setUpstreamReader(null);
            r5 = r25.this$0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:71:0x01a8, code lost:
            if (r5 == null) goto L_0x01b4;
         */
        /* JADX WARN: Code restructure failed: missing block: B:72:0x01aa, code lost:
            r5.notifyAll();
            r0 = kotlin.Unit.INSTANCE;
         */
        /* JADX WARN: Code restructure failed: missing block: B:73:0x01b2, code lost:
            monitor-exit(r4);
         */
        /* JADX WARN: Code restructure failed: missing block: B:74:0x01b3, code lost:
            return r4;
         */
        /* JADX WARN: Code restructure failed: missing block: B:76:0x01bb, code lost:
            throw new kotlin.TypeCastException("null cannot be cast to non-null type java.lang.Object");
         */
        /* JADX WARN: Code restructure failed: missing block: B:77:0x01bc, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:78:0x01be, code lost:
            throw r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:79:0x01bf, code lost:
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:80:0x01c1, code lost:
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:82:0x01c4, code lost:
            monitor-exit(r4);
         */
        /* JADX WARN: Code restructure failed: missing block: B:83:0x01c5, code lost:
            throw r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:84:0x01c6, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:86:0x01c9, code lost:
            monitor-enter(r25.this$0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:88:0x01cb, code lost:
            r25.this$0.setUpstreamReader(null);
            r6 = r25.this$0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:89:0x01d7, code lost:
            if (r6 == null) goto L_0x01d9;
         */
        /* JADX WARN: Code restructure failed: missing block: B:91:0x01e0, code lost:
            throw new kotlin.TypeCastException("null cannot be cast to non-null type java.lang.Object");
         */
        /* JADX WARN: Code restructure failed: missing block: B:92:0x01e1, code lost:
            r6.notifyAll();
            r5 = kotlin.Unit.INSTANCE;
         */
        /* JADX WARN: Code restructure failed: missing block: B:93:0x01ea, code lost:
            throw r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:94:0x01eb, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:95:0x01ed, code lost:
            throw r0;
         */
        @Override // okio.Source
        /* Code decompiled incorrectly, please refer to instructions dump */
        public long read(Buffer sink, long byteCount) throws IOException {
            Intrinsics.checkParameterIsNotNull(sink, "sink");
            int source = 1;
            boolean z = false;
            if (this.fileOperator != null) {
                z = true;
            }
            if (z) {
                synchronized (Relay.this) {
                    while (true) {
                        try {
                            if (this.sourcePos == Relay.this.getUpstreamPos()) {
                                if (!Relay.this.getComplete()) {
                                    if (Relay.this.getUpstreamReader() == null) {
                                        Relay.this.setUpstreamReader(Thread.currentThread());
                                        break;
                                    }
                                    this.timeout.waitUntilNotified(Relay.this);
                                } else {
                                    return -1;
                                }
                            } else {
                                long bufferPos = Relay.this.getUpstreamPos() - Relay.this.getBuffer().size();
                                if (this.sourcePos < bufferPos) {
                                    source = 2;
                                } else {
                                    long bytesToRead = Math.min(byteCount, Relay.this.getUpstreamPos() - this.sourcePos);
                                    Relay.this.getBuffer().copyTo(sink, this.sourcePos - bufferPos, bytesToRead);
                                    this.sourcePos += bytesToRead;
                                    return bytesToRead;
                                }
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                }
            } else {
                throw new IllegalStateException("Check failed.".toString());
            }
        }

        @Override // okio.Source
        public Timeout timeout() {
            return this.timeout;
        }

        @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.fileOperator != null) {
                this.fileOperator = null;
                Object fileToClose = (RandomAccessFile) null;
                synchronized (Relay.this) {
                    Relay relay = Relay.this;
                    relay.setSourceCount(relay.getSourceCount() - 1);
                    if (Relay.this.getSourceCount() == 0) {
                        fileToClose = Relay.this.getFile();
                        Relay.this.setFile(null);
                    }
                    Unit unit = Unit.INSTANCE;
                }
                if (fileToClose != null) {
                    Util.closeQuietly((Closeable) fileToClose);
                }
            }
        }
    }

    /* compiled from: Relay.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J&\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004J\u000e\u0010\u0013\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u00020\u00068\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lokhttp3/internal/cache2/Relay$Companion;", "", "()V", "FILE_HEADER_SIZE", "", "PREFIX_CLEAN", "Lokio/ByteString;", "PREFIX_DIRTY", "SOURCE_FILE", "", "SOURCE_UPSTREAM", "edit", "Lokhttp3/internal/cache2/Relay;", UriUtil.LOCAL_FILE_SCHEME, "Ljava/io/File;", "upstream", "Lokio/Source;", "metadata", "bufferMaxSize", "read", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        public final Relay edit(File file, Source upstream, ByteString metadata, long bufferMaxSize) throws IOException {
            Intrinsics.checkParameterIsNotNull(file, UriUtil.LOCAL_FILE_SCHEME);
            Intrinsics.checkParameterIsNotNull(upstream, "upstream");
            Intrinsics.checkParameterIsNotNull(metadata, "metadata");
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            Relay result = new Relay(randomAccessFile, upstream, 0, metadata, bufferMaxSize, null);
            randomAccessFile.setLength(0);
            result.writeHeader(Relay.PREFIX_DIRTY, -1, -1);
            return result;
        }

        public final Relay read(File file) throws IOException {
            Intrinsics.checkParameterIsNotNull(file, UriUtil.LOCAL_FILE_SCHEME);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            FileChannel channel = randomAccessFile.getChannel();
            Intrinsics.checkExpressionValueIsNotNull(channel, "randomAccessFile.channel");
            FileOperator fileOperator = new FileOperator(channel);
            Buffer header = new Buffer();
            fileOperator.read(0, header, 32);
            if (!(!Intrinsics.areEqual(header.readByteString((long) Relay.PREFIX_CLEAN.size()), Relay.PREFIX_CLEAN))) {
                long upstreamSize = header.readLong();
                long metadataSize = header.readLong();
                Buffer metadataBuffer = new Buffer();
                fileOperator.read(upstreamSize + 32, metadataBuffer, metadataSize);
                return new Relay(randomAccessFile, null, upstreamSize, metadataBuffer.readByteString(), 0, null);
            }
            throw new IOException("unreadable cache file");
        }
    }
}
