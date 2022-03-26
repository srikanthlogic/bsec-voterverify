package com.facebook.imagepipeline.memory;

import android.util.Log;
import com.facebook.common.internal.Preconditions;
import java.io.Closeable;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class BufferMemoryChunk implements MemoryChunk, Closeable {
    private static final String TAG = "BufferMemoryChunk";
    private ByteBuffer mBuffer;
    private final long mId = (long) System.identityHashCode(this);
    private final int mSize;

    public BufferMemoryChunk(int size) {
        this.mBuffer = ByteBuffer.allocateDirect(size);
        this.mSize = size;
    }

    @Override // com.facebook.imagepipeline.memory.MemoryChunk, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() {
        this.mBuffer = null;
    }

    @Override // com.facebook.imagepipeline.memory.MemoryChunk
    public synchronized boolean isClosed() {
        return this.mBuffer == null;
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
        this.mBuffer.position(memoryOffset);
        this.mBuffer.put(byteArray, byteArrayOffset, actualCount);
        return actualCount;
    }

    @Override // com.facebook.imagepipeline.memory.MemoryChunk
    public synchronized int read(int memoryOffset, byte[] byteArray, int byteArrayOffset, int count) {
        int actualCount;
        Preconditions.checkNotNull(byteArray);
        Preconditions.checkState(!isClosed());
        actualCount = MemoryChunkUtil.adjustByteCount(memoryOffset, count, this.mSize);
        MemoryChunkUtil.checkBounds(memoryOffset, byteArray.length, byteArrayOffset, actualCount, this.mSize);
        this.mBuffer.position(memoryOffset);
        this.mBuffer.get(byteArray, byteArrayOffset, actualCount);
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
        return this.mBuffer.get(offset);
    }

    @Override // com.facebook.imagepipeline.memory.MemoryChunk
    public void copy(int offset, MemoryChunk other, int otherOffset, int count) {
        Preconditions.checkNotNull(other);
        if (other.getUniqueId() == getUniqueId()) {
            Log.w(TAG, "Copying from BufferMemoryChunk " + Long.toHexString(getUniqueId()) + " to BufferMemoryChunk " + Long.toHexString(other.getUniqueId()) + " which are the same ");
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

    @Override // com.facebook.imagepipeline.memory.MemoryChunk
    public long getNativePtr() {
        throw new UnsupportedOperationException("Cannot get the pointer of a BufferMemoryChunk");
    }

    @Override // com.facebook.imagepipeline.memory.MemoryChunk
    @Nullable
    public synchronized ByteBuffer getByteBuffer() {
        return this.mBuffer;
    }

    @Override // com.facebook.imagepipeline.memory.MemoryChunk
    public long getUniqueId() {
        return this.mId;
    }

    private void doCopy(int offset, MemoryChunk other, int otherOffset, int count) {
        if (other instanceof BufferMemoryChunk) {
            Preconditions.checkState(!isClosed());
            Preconditions.checkState(!other.isClosed());
            MemoryChunkUtil.checkBounds(offset, other.getSize(), otherOffset, count, this.mSize);
            this.mBuffer.position(offset);
            other.getByteBuffer().position(otherOffset);
            byte[] b = new byte[count];
            this.mBuffer.get(b, 0, count);
            other.getByteBuffer().put(b, 0, count);
            return;
        }
        throw new IllegalArgumentException("Cannot copy two incompatible MemoryChunks");
    }
}
