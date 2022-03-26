package com.facebook.imagepipeline.nativecode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Throwables;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.common.TooManyBitmapsException;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.BitmapCounter;
import com.facebook.imagepipeline.memory.BitmapCounterProvider;
import com.facebook.imagepipeline.platform.PlatformDecoder;
import com.facebook.imageutils.BitmapUtil;
import java.util.Locale;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public abstract class DalvikPurgeableDecoder implements PlatformDecoder {
    protected static final byte[] EOI = {-1, -39};
    private final BitmapCounter mUnpooledBitmapsCounter = BitmapCounterProvider.get();

    private static native void nativePinBitmap(Bitmap bitmap);

    protected abstract Bitmap decodeByteArrayAsPurgeable(CloseableReference<PooledByteBuffer> closeableReference, BitmapFactory.Options options);

    protected abstract Bitmap decodeJPEGByteArrayAsPurgeable(CloseableReference<PooledByteBuffer> closeableReference, int i, BitmapFactory.Options options);

    static {
        ImagePipelineNativeLoader.load();
    }

    @Override // com.facebook.imagepipeline.platform.PlatformDecoder
    public CloseableReference<Bitmap> decodeFromEncodedImage(EncodedImage encodedImage, Bitmap.Config bitmapConfig, @Nullable Rect regionToDecode) {
        return decodeFromEncodedImageWithColorSpace(encodedImage, bitmapConfig, regionToDecode, false);
    }

    @Override // com.facebook.imagepipeline.platform.PlatformDecoder
    public CloseableReference<Bitmap> decodeJPEGFromEncodedImage(EncodedImage encodedImage, Bitmap.Config bitmapConfig, @Nullable Rect regionToDecode, int length) {
        return decodeJPEGFromEncodedImageWithColorSpace(encodedImage, bitmapConfig, regionToDecode, length, false);
    }

    @Override // com.facebook.imagepipeline.platform.PlatformDecoder
    public CloseableReference<Bitmap> decodeFromEncodedImageWithColorSpace(EncodedImage encodedImage, Bitmap.Config bitmapConfig, @Nullable Rect regionToDecode, boolean transformToSRGB) {
        BitmapFactory.Options options = getBitmapFactoryOptions(encodedImage.getSampleSize(), bitmapConfig);
        CloseableReference<PooledByteBuffer> bytesRef = encodedImage.getByteBufferRef();
        Preconditions.checkNotNull(bytesRef);
        try {
            return pinBitmap(decodeByteArrayAsPurgeable(bytesRef, options));
        } finally {
            CloseableReference.closeSafely(bytesRef);
        }
    }

    @Override // com.facebook.imagepipeline.platform.PlatformDecoder
    public CloseableReference<Bitmap> decodeJPEGFromEncodedImageWithColorSpace(EncodedImage encodedImage, Bitmap.Config bitmapConfig, @Nullable Rect regionToDecode, int length, boolean transformToSRGB) {
        BitmapFactory.Options options = getBitmapFactoryOptions(encodedImage.getSampleSize(), bitmapConfig);
        CloseableReference<PooledByteBuffer> bytesRef = encodedImage.getByteBufferRef();
        Preconditions.checkNotNull(bytesRef);
        try {
            return pinBitmap(decodeJPEGByteArrayAsPurgeable(bytesRef, length, options));
        } finally {
            CloseableReference.closeSafely(bytesRef);
        }
    }

    public static BitmapFactory.Options getBitmapFactoryOptions(int sampleSize, Bitmap.Config bitmapConfig) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = true;
        options.inPreferredConfig = bitmapConfig;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = sampleSize;
        if (Build.VERSION.SDK_INT >= 11) {
            options.inMutable = true;
        }
        return options;
    }

    public static boolean endsWithEOI(CloseableReference<PooledByteBuffer> bytesRef, int length) {
        PooledByteBuffer buffer = bytesRef.get();
        return length >= 2 && buffer.read(length + -2) == -1 && buffer.read(length + -1) == -39;
    }

    public CloseableReference<Bitmap> pinBitmap(Bitmap bitmap) {
        Preconditions.checkNotNull(bitmap);
        try {
            nativePinBitmap(bitmap);
            if (this.mUnpooledBitmapsCounter.increase(bitmap)) {
                return CloseableReference.of(bitmap, this.mUnpooledBitmapsCounter.getReleaser());
            }
            int bitmapSize = BitmapUtil.getSizeInBytes(bitmap);
            bitmap.recycle();
            throw new TooManyBitmapsException(String.format(Locale.US, "Attempted to pin a bitmap of size %d bytes. The current pool count is %d, the current pool size is %d bytes. The current pool max count is %d, the current pool max size is %d bytes.", Integer.valueOf(bitmapSize), Integer.valueOf(this.mUnpooledBitmapsCounter.getCount()), Long.valueOf(this.mUnpooledBitmapsCounter.getSize()), Integer.valueOf(this.mUnpooledBitmapsCounter.getMaxCount()), Integer.valueOf(this.mUnpooledBitmapsCounter.getMaxSize())));
        } catch (Exception e) {
            bitmap.recycle();
            throw Throwables.propagate(e);
        }
    }
}
