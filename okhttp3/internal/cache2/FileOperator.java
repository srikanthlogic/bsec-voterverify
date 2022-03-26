package okhttp3.internal.cache2;

import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.IOException;
import java.nio.channels.FileChannel;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okio.Buffer;
/* compiled from: FileOperator.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\bJ\u001e\u0010\f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lokhttp3/internal/cache2/FileOperator;", "", "fileChannel", "Ljava/nio/channels/FileChannel;", "(Ljava/nio/channels/FileChannel;)V", "read", "", "pos", "", "sink", "Lokio/Buffer;", "byteCount", "write", FirebaseAnalytics.Param.SOURCE, "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class FileOperator {
    private final FileChannel fileChannel;

    public FileOperator(FileChannel fileChannel) {
        Intrinsics.checkParameterIsNotNull(fileChannel, "fileChannel");
        this.fileChannel = fileChannel;
    }

    public final void write(long pos, Buffer source, long byteCount) throws IOException {
        Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
        if (byteCount < 0 || byteCount > source.size()) {
            throw new IndexOutOfBoundsException();
        }
        long mutablePos = pos;
        long mutableByteCount = byteCount;
        while (mutableByteCount > 0) {
            long bytesWritten = this.fileChannel.transferFrom(source, mutablePos, mutableByteCount);
            mutablePos += bytesWritten;
            mutableByteCount -= bytesWritten;
        }
    }

    public final void read(long pos, Buffer sink, long byteCount) {
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        if (byteCount >= 0) {
            long mutablePos = pos;
            long mutableByteCount = byteCount;
            while (mutableByteCount > 0) {
                long bytesRead = this.fileChannel.transferTo(mutablePos, mutableByteCount, sink);
                mutablePos += bytesRead;
                mutableByteCount -= bytesRead;
            }
            return;
        }
        throw new IndexOutOfBoundsException();
    }
}
