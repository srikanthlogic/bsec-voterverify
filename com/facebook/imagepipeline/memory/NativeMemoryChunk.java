package com.facebook.imagepipeline.memory;

import android.util.Log;
import com.facebook.common.internal.Preconditions;
import com.facebook.imagepipeline.nativecode.ImagePipelineNativeLoader;
import java.io.Closeable;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class NativeMemoryChunk implements MemoryChunk, Closeable {
    private static final String TAG = "NativeMemoryChunk";
    private boolean mIsClosed;
    private final long mNativePtr;
    private final int mSize;

    private static native long nativeAllocate(int i);

    private static native void nativeCopyFromByteArray(long j, byte[] bArr, int i, int i2);

    private static native void nativeCopyToByteArray(long j, byte[] bArr, int i, int i2);

    private static native void nativeFree(long j);

    private static native void nativeMemcpy(long j, long j2, int i);

    private static native byte nativeReadByte(long j);

    static {
        ImagePipelineNativeLoader.load();
    }

    public NativeMemoryChunk(int size) {
        Preconditions.checkArgument(size > 0);
        this.mSize = size;
        this.mNativePtr = nativeAllocate(this.mSize);
        this.mIsClosed = false;
    }

    public NativeMemoryChunk() {
        this.mSize = 0;
        this.mNativePtr = 0;
        this.mIsClosed = true;
    }

    @Override // com.facebook.imagepipeline.memory.MemoryChunk, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() {
        if (!this.mIsClosed) {
            this.mIsClosed = true;
            nativeFree(this.mNativePtr);
        }
    }

    @Override // com.facebook.imagepipeline.memory.MemoryChunk
    public synchronized boolean isClosed() {
        return this.mIsClosed;
    }

    @Override // com.facebook.imagepipeline.memory.MemoryChunk
    public int getSize() {
        return this.mSize;
    }

    @Override // com.facebook.imagepipeline.memory.MemoryChunk
    public synchronized int write(int memoryOffset, byte[] byteArray, int byteArrayOffset, int count) {
        int actualCount;
        Preconditions.checkNotNull(byteArray);
        Preconditions.checkState(!isClosed());
        actualCount = MemoryChunkUtil.adjustByteCount(memoryOffset, count, this.mSize);
        MemoryChunkUtil.checkBounds(memoryOffset, byteArray.length, byteArrayOffset, actualCount, this.mSize);
        nativeCopyFromByteArray(this.mNativePtr + ((long) memoryOffset), byteArray, byteArrayOffset, actualCount);
        return actualCount;
    }

    @Override // com.facebook.imagepipeline.memory.MemoryChunk
    public synchronized int read(int memoryOffset, byte[] byteArray, int byteArrayOffset, int count) {
        int actualCount;
        Preconditions.checkNotNull(byteArray);
        Preconditions.checkState(!isClosed());
        actualCount = MemoryChunkUtil.adjustByteCount(memoryOffset, count, this.mSize);
        MemoryChunkUtil.checkBounds(memoryOffset, byteArray.length, byteArrayOffset, actualCount, this.mSize);
        nativeCopyToByteArray(this.mNativePtr + ((long) memoryOffset), byteArray, byteArrayOffset, actualCount);
        return actualCount;
    }

    @Override // com.facebook.imagepipeline.memory.MemoryChunk
    public synchronized byte read(int offset) {
        boolean z = true;
        Preconditions.checkState(!isClosed());
        Preconditions.checkArgument(offset >= 0);
        if (offset >= this.mSize) {
            z = false;
        }
        Preconditions.checkArgument(z);
        return nativeReadByte(this.mNativePtr + ((long) offset));
    }

    @Override // com.facebook.imagepipeline.memory.MemoryChunk
    public long getNativePtr() {
        return this.mNativePtr;
    }

    @Override // com.facebook.imagepipeline.memory.MemoryChunk
    @Nullable
    public ByteBuffer getByteBuffer() {
        return null;
    }

    @Override // com.facebook.imagepipeline.memory.MemoryChunk
    public long getUniqueId() {
        return this.mNativePtr;
    }

    @Override // com.facebook.imagepipeline.memory.MemoryChunk
    public void copy(int offset, MemoryChunk other, int otherOffset, int count) {
        Preconditions.checkNotNull(other);
        if (other.getUniqueId() == getUniqueId()) {
            Log.w(TAG, "Copying from NativeMemoryChunk " + Integer.toHexString(System.identityHashCode(this)) + " to NativeMemoryChunk " + Integer.toHexString(System.identityHashCode(other)) + " which share the same address " + Long.toHexString(this.mNativePtr));
            Preconditions.checkArgument(false);
        }
        if (other.getUniqueId() < getUniqueId()) {
            synchronized (other) {
                synchronized (this) {
                    doCopy(offset, other, otherOffset, count);
                }
            }
            return;
        }
        synchronized (this) {
            synchronized (other) {
                doCopy(offset, other, otherOffset, count);
            }
        }
    }

    private void doCopy(int offset, MemoryChunk other, int otherOffset, int count) {
        if (other instanceof NativeMemoryChunk) {
            Preconditions.checkState(!isClosed());
            Preconditions.checkState(!other.isClosed());
            MemoryChunkUtil.checkBounds(offset, other.getSize(), otherOffset, count, this.mSize);
            nativeMemcpy(other.getNativePtr() + ((long) otherOffset), this.mNativePtr + ((long) offset), count);
            return;
        }
        throw new IllegalArgumentException("Cannot copy two incompatible MemoryChunks");
    }

    @Override // java.lang.Object
    protected void finalize() throws Throwable {
        if (!isClosed()) {
            Log.w(TAG, "finalize: Chunk " + Integer.toHexString(System.identityHashCode(this)) + " still active. ");
            try {
                close();
            } finally {
                super.finalize();
            }
        }
    }
}
