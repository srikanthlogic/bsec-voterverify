package com.facebook.imagepipeline.bitmaps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.webp.BitmapCreator;
import com.facebook.imageformat.DefaultImageFormats;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.FlexByteArrayPool;
import com.facebook.imagepipeline.memory.PoolFactory;
/* loaded from: classes.dex */
public class HoneycombBitmapCreator implements BitmapCreator {
    private final FlexByteArrayPool mFlexByteArrayPool;
    private final EmptyJpegGenerator mJpegGenerator;

    public HoneycombBitmapCreator(PoolFactory poolFactory) {
        this.mFlexByteArrayPool = poolFactory.getFlexByteArrayPool();
        this.mJpegGenerator = new EmptyJpegGenerator(poolFactory.getPooledByteBufferFactory());
    }

    @Override // com.facebook.common.webp.BitmapCreator
    public Bitmap createNakedBitmap(int width, int height, Bitmap.Config bitmapConfig) {
        CloseableReference<PooledByteBuffer> jpgRef = this.mJpegGenerator.generate((short) width, (short) height);
        EncodedImage encodedImage = null;
        CloseableReference<byte[]> encodedBytesArrayRef = null;
        try {
            encodedImage = new EncodedImage(jpgRef);
            encodedImage.setImageFormat(DefaultImageFormats.JPEG);
            BitmapFactory.Options options = getBitmapFactoryOptions(encodedImage.getSampleSize(), bitmapConfig);
            int length = jpgRef.get().size();
            encodedBytesArrayRef = this.mFlexByteArrayPool.get(length + 2);
            byte[] encodedBytesArray = encodedBytesArrayRef.get();
            jpgRef.get().read(0, encodedBytesArray, 0, length);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodedBytesArray, 0, length, options);
            bitmap.setHasAlpha(true);
            bitmap.eraseColor(0);
            return bitmap;
        } finally {
            CloseableReference.closeSafely((CloseableReference<?>) encodedBytesArrayRef);
            EncodedImage.closeSafely(encodedImage);
            CloseableReference.closeSafely(jpgRef);
        }
    }

    private static BitmapFactory.Options getBitmapFactoryOptions(int sampleSize, Bitmap.Config bitmapConfig) {
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
}
