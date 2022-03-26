package com.facebook.imagepipeline.platform;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.MemoryFile;
import com.facebook.common.internal.ByteStreams;
import com.facebook.common.internal.Closeables;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Throwables;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.memory.PooledByteBufferInputStream;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.streams.LimitedInputStream;
import com.facebook.common.webp.WebpBitmapFactory;
import com.facebook.common.webp.WebpSupportStatus;
import com.facebook.imagepipeline.nativecode.DalvikPurgeableDecoder;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class GingerbreadPurgeableDecoder extends DalvikPurgeableDecoder {
    private static Method sGetFileDescriptorMethod;
    @Nullable
    private final WebpBitmapFactory mWebpBitmapFactory = WebpSupportStatus.loadWebpBitmapFactoryIfExists();

    @Override // com.facebook.imagepipeline.nativecode.DalvikPurgeableDecoder
    protected Bitmap decodeByteArrayAsPurgeable(CloseableReference<PooledByteBuffer> bytesRef, BitmapFactory.Options options) {
        return decodeFileDescriptorAsPurgeable(bytesRef, bytesRef.get().size(), null, options);
    }

    @Override // com.facebook.imagepipeline.nativecode.DalvikPurgeableDecoder
    protected Bitmap decodeJPEGByteArrayAsPurgeable(CloseableReference<PooledByteBuffer> bytesRef, int length, BitmapFactory.Options options) {
        return decodeFileDescriptorAsPurgeable(bytesRef, length, endsWithEOI(bytesRef, length) ? null : EOI, options);
    }

    private static MemoryFile copyToMemoryFile(CloseableReference<PooledByteBuffer> bytesRef, int inputLength, @Nullable byte[] suffix) throws IOException {
        MemoryFile memoryFile = new MemoryFile(null, (suffix == null ? 0 : suffix.length) + inputLength);
        memoryFile.allowPurging(false);
        PooledByteBufferInputStream pbbIs = null;
        LimitedInputStream is = null;
        OutputStream os = null;
        try {
            pbbIs = new PooledByteBufferInputStream(bytesRef.get());
            is = new LimitedInputStream(pbbIs, inputLength);
            os = memoryFile.getOutputStream();
            ByteStreams.copy(is, os);
            if (suffix != null) {
                memoryFile.writeBytes(suffix, 0, inputLength, suffix.length);
            }
            return memoryFile;
        } finally {
            CloseableReference.closeSafely(bytesRef);
            Closeables.closeQuietly(pbbIs);
            Closeables.closeQuietly(is);
            Closeables.close(os, true);
        }
    }

    private synchronized Method getFileDescriptorMethod() {
        if (sGetFileDescriptorMethod == null) {
            try {
                sGetFileDescriptorMethod = MemoryFile.class.getDeclaredMethod("getFileDescriptor", new Class[0]);
            } catch (Exception e) {
                throw Throwables.propagate(e);
            }
        }
        return sGetFileDescriptorMethod;
    }

    private FileDescriptor getMemoryFileDescriptor(MemoryFile memoryFile) {
        try {
            return (FileDescriptor) getFileDescriptorMethod().invoke(memoryFile, new Object[0]);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    private Bitmap decodeFileDescriptorAsPurgeable(CloseableReference<PooledByteBuffer> bytesRef, int inputLength, byte[] suffix, BitmapFactory.Options options) {
        MemoryFile memoryFile;
        try {
            memoryFile = null;
            try {
                memoryFile = copyToMemoryFile(bytesRef, inputLength, suffix);
                FileDescriptor fd = getMemoryFileDescriptor(memoryFile);
                if (this.mWebpBitmapFactory != null) {
                    return (Bitmap) Preconditions.checkNotNull(this.mWebpBitmapFactory.decodeFileDescriptor(fd, null, options), "BitmapFactory returned null");
                }
                throw new IllegalStateException("WebpBitmapFactory is null");
            } catch (IOException e) {
                throw Throwables.propagate(e);
            }
        } finally {
            if (memoryFile != null) {
                memoryFile.close();
            }
        }
    }
}
