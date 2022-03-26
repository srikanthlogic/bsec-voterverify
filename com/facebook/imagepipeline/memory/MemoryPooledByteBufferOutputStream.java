package com.facebook.imagepipeline.memory;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.memory.PooledByteBufferOutputStream;
import com.facebook.common.references.CloseableReference;
import java.io.IOException;
/* loaded from: classes.dex */
public class MemoryPooledByteBufferOutputStream extends PooledByteBufferOutputStream {
    private CloseableReference<MemoryChunk> mBufRef;
    private int mCount;
    private final MemoryChunkPool mPool;

    public MemoryPooledByteBufferOutputStream(MemoryChunkPool pool) {
        this(pool, pool.getMinBufferSize());
    }

    public MemoryPooledByteBufferOutputStream(MemoryChunkPool pool, int initialCapacity) {
        Preconditions.checkArgument(initialCapacity > 0);
        this.mPool = (MemoryChunkPool) Preconditions.checkNotNull(pool);
        this.mCount = 0;
        this.mBufRef = CloseableReference.of(this.mPool.get(initialCapacity), this.mPool);
    }

    @Override // com.facebook.common.memory.PooledByteBufferOutputStream
    public MemoryPooledByteBuffer toByteBuffer() {
        ensureValid();
        return new MemoryPooledByteBuffer(this.mBufRef, this.mCount);
    }

    @Override // com.facebook.common.memory.PooledByteBufferOutputStream
    public int size() {
        return this.mCount;
    }

    @Override // java.io.OutputStream
    public void write(int oneByte) throws IOException {
        write(new byte[]{(byte) oneByte});
    }

    @Override // java.io.OutputStream
    public void write(byte[] buffer, int offset, int count) throws IOException {
        if (offset < 0 || count < 0 || offset + count > buffer.length) {
            throw new ArrayIndexOutOfBoundsException("length=" + buffer.length + "; regionStart=" + offset + "; regionLength=" + count);
        }
        ensureValid();
        realloc(this.mCount + count);
        this.mBufRef.get().write(this.mCount, buffer, offset, count);
        this.mCount += count;
    }

    @Override // com.facebook.common.memory.PooledByteBufferOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        CloseableReference.closeSafely(this.mBufRef);
        this.mBufRef = null;
        this.mCount = -1;
        super.close();
    }

    void realloc(int newLength) {
        ensureValid();
        if (newLength > this.mBufRef.get().getSize()) {
            MemoryChunk newbuf = this.mPool.get(newLength);
            this.mBufRef.get().copy(0, newbuf, 0, this.mCount);
            this.mBufRef.close();
            this.mBufRef = CloseableReference.of(newbuf, this.mPool);
        }
    }

    private void ensureValid() {
        if (!CloseableReference.isValid(this.mBufRef)) {
            throw new InvalidStreamException();
        }
    }

    /* loaded from: classes.dex */
    public static class InvalidStreamException extends RuntimeException {
        public InvalidStreamException() {
            super("OutputStream no longer valid");
        }
    }
}
