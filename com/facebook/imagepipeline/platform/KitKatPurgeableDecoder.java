package com.facebook.imagepipeline.platform;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.memory.FlexByteArrayPool;
import com.facebook.imagepipeline.nativecode.DalvikPurgeableDecoder;
/* loaded from: classes.dex */
public class KitKatPurgeableDecoder extends DalvikPurgeableDecoder {
    private final FlexByteArrayPool mFlexByteArrayPool;

    public KitKatPurgeableDecoder(FlexByteArrayPool flexByteArrayPool) {
        this.mFlexByteArrayPool = flexByteArrayPool;
    }

    @Override // com.facebook.imagepipeline.nativecode.DalvikPurgeableDecoder
    protected Bitmap decodeByteArrayAsPurgeable(CloseableReference<PooledByteBuffer> bytesRef, BitmapFactory.Options options) {
        PooledByteBuffer pooledByteBuffer = bytesRef.get();
        int length = pooledByteBuffer.size();
        CloseableReference<byte[]> encodedBytesArrayRef = this.mFlexByteArrayPool.get(length);
        try {
            byte[] encodedBytesArray = encodedBytesArrayRef.get();
            pooledByteBuffer.read(0, encodedBytesArray, 0, length);
            return (Bitmap) Preconditions.checkNotNull(BitmapFactory.decodeByteArray(encodedBytesArray, 0, length, options), "BitmapFactory returned null");
        } finally {
            CloseableReference.closeSafely((CloseableReference<?>) encodedBytesArrayRef);
        }
    }

    @Override // com.facebook.imagepipeline.nativecode.DalvikPurgeableDecoder
    protected Bitmap decodeJPEGByteArrayAsPurgeable(CloseableReference<PooledByteBuffer> bytesRef, int length, BitmapFactory.Options options) {
        byte[] suffix = endsWithEOI(bytesRef, length) ? null : EOI;
        PooledByteBuffer pooledByteBuffer = bytesRef.get();
        Preconditions.checkArgument(length <= pooledByteBuffer.size());
        CloseableReference<byte[]> encodedBytesArrayRef = this.mFlexByteArrayPool.get(length + 2);
        try {
            byte[] encodedBytesArray = encodedBytesArrayRef.get();
            pooledByteBuffer.read(0, encodedBytesArray, 0, length);
            if (suffix != null) {
                putEOI(encodedBytesArray, length);
                length += 2;
            }
            return (Bitmap) Preconditions.checkNotNull(BitmapFactory.decodeByteArray(encodedBytesArray, 0, length, options), "BitmapFactory returned null");
        } finally {
            CloseableReference.closeSafely((CloseableReference<?>) encodedBytesArrayRef);
        }
    }

    private static void putEOI(byte[] imageBytes, int offset) {
        imageBytes[offset] = -1;
        imageBytes[offset + 1] = -39;
    }
}
