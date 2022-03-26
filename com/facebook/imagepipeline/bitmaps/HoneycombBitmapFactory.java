package com.facebook.imagepipeline.bitmaps;

import android.graphics.Bitmap;
import com.facebook.common.logging.FLog;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.references.CloseableReference;
import com.facebook.imageformat.DefaultImageFormats;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.platform.PlatformDecoder;
/* loaded from: classes.dex */
public class HoneycombBitmapFactory extends PlatformBitmapFactory {
    private static final String TAG = HoneycombBitmapFactory.class.getSimpleName();
    private boolean mImmutableBitmapFallback;
    private final EmptyJpegGenerator mJpegGenerator;
    private final PlatformDecoder mPurgeableDecoder;

    public HoneycombBitmapFactory(EmptyJpegGenerator jpegGenerator, PlatformDecoder purgeableDecoder) {
        this.mJpegGenerator = jpegGenerator;
        this.mPurgeableDecoder = purgeableDecoder;
    }

    @Override // com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory
    public CloseableReference<Bitmap> createBitmapInternal(int width, int height, Bitmap.Config bitmapConfig) {
        if (this.mImmutableBitmapFallback) {
            return createFallbackBitmap(width, height, bitmapConfig);
        }
        CloseableReference<PooledByteBuffer> jpgRef = this.mJpegGenerator.generate((short) width, (short) height);
        try {
            EncodedImage encodedImage = new EncodedImage(jpgRef);
            encodedImage.setImageFormat(DefaultImageFormats.JPEG);
            CloseableReference<Bitmap> bitmapRef = this.mPurgeableDecoder.decodeJPEGFromEncodedImage(encodedImage, bitmapConfig, null, jpgRef.get().size());
            if (!bitmapRef.get().isMutable()) {
                CloseableReference.closeSafely(bitmapRef);
                this.mImmutableBitmapFallback = true;
                FLog.wtf(TAG, "Immutable bitmap returned by decoder");
                CloseableReference<Bitmap> createFallbackBitmap = createFallbackBitmap(width, height, bitmapConfig);
                EncodedImage.closeSafely(encodedImage);
                return createFallbackBitmap;
            }
            bitmapRef.get().setHasAlpha(true);
            bitmapRef.get().eraseColor(0);
            EncodedImage.closeSafely(encodedImage);
            return bitmapRef;
        } finally {
            jpgRef.close();
        }
    }

    private static CloseableReference<Bitmap> createFallbackBitmap(int width, int height, Bitmap.Config bitmapConfig) {
        return CloseableReference.of(Bitmap.createBitmap(width, height, bitmapConfig), SimpleBitmapReleaser.getInstance());
    }
}
