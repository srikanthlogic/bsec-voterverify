package com.facebook.imagepipeline.platform;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.ColorSpace;
import android.graphics.Rect;
import android.os.Build;
import androidx.core.util.Pools;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.logging.FLog;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.streams.LimitedInputStream;
import com.facebook.common.streams.TailAppendingInputStream;
import com.facebook.imagepipeline.bitmaps.SimpleBitmapReleaser;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.BitmapPool;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public abstract class DefaultDecoder implements PlatformDecoder {
    private static final int DECODE_BUFFER_SIZE;
    private final BitmapPool mBitmapPool;
    final Pools.SynchronizedPool<ByteBuffer> mDecodeBuffers;
    @Nullable
    private final PreverificationHelper mPreverificationHelper;
    private static final Class<?> TAG = DefaultDecoder.class;
    private static final byte[] EOI_TAIL = {-1, -39};

    public abstract int getBitmapSize(int i, int i2, BitmapFactory.Options options);

    public DefaultDecoder(BitmapPool bitmapPool, int maxNumThreads, Pools.SynchronizedPool decodeBuffers) {
        this.mPreverificationHelper = Build.VERSION.SDK_INT >= 26 ? new PreverificationHelper() : null;
        this.mBitmapPool = bitmapPool;
        this.mDecodeBuffers = decodeBuffers;
        for (int i = 0; i < maxNumThreads; i++) {
            this.mDecodeBuffers.release(ByteBuffer.allocate(16384));
        }
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
        BitmapFactory.Options options = getDecodeOptionsForStream(encodedImage, bitmapConfig);
        boolean retryOnFail = options.inPreferredConfig != Bitmap.Config.ARGB_8888;
        try {
            return decodeFromStream(encodedImage.getInputStream(), options, regionToDecode, transformToSRGB);
        } catch (RuntimeException re) {
            if (retryOnFail) {
                return decodeFromEncodedImageWithColorSpace(encodedImage, Bitmap.Config.ARGB_8888, regionToDecode, transformToSRGB);
            }
            throw re;
        }
    }

    @Override // com.facebook.imagepipeline.platform.PlatformDecoder
    public CloseableReference<Bitmap> decodeJPEGFromEncodedImageWithColorSpace(EncodedImage encodedImage, Bitmap.Config bitmapConfig, @Nullable Rect regionToDecode, int length, boolean transformToSRGB) {
        InputStream jpegDataStream;
        boolean isJpegComplete = encodedImage.isCompleteAt(length);
        BitmapFactory.Options options = getDecodeOptionsForStream(encodedImage, bitmapConfig);
        InputStream jpegDataStream2 = encodedImage.getInputStream();
        Preconditions.checkNotNull(jpegDataStream2);
        if (encodedImage.getSize() > length) {
            jpegDataStream2 = new LimitedInputStream(jpegDataStream2, length);
        }
        if (!isJpegComplete) {
            jpegDataStream = new TailAppendingInputStream(jpegDataStream2, EOI_TAIL);
        } else {
            jpegDataStream = jpegDataStream2;
        }
        boolean retryOnFail = options.inPreferredConfig != Bitmap.Config.ARGB_8888;
        try {
            return decodeFromStream(jpegDataStream, options, regionToDecode, transformToSRGB);
        } catch (RuntimeException re) {
            if (retryOnFail) {
                return decodeJPEGFromEncodedImageWithColorSpace(encodedImage, Bitmap.Config.ARGB_8888, regionToDecode, length, transformToSRGB);
            }
            throw re;
        }
    }

    protected CloseableReference<Bitmap> decodeStaticImageFromStream(InputStream inputStream, BitmapFactory.Options options, @Nullable Rect regionToDecode) {
        return decodeFromStream(inputStream, options, regionToDecode, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:43:0x00b2, code lost:
        if (r13 == null) goto L_0x00bc;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private CloseableReference<Bitmap> decodeFromStream(InputStream inputStream, BitmapFactory.Options options, @Nullable Rect regionToDecode, boolean transformToSRGB) {
        int targetHeight;
        int targetHeight2;
        Bitmap bitmapToReuse;
        ByteBuffer byteBuffer;
        BitmapRegionDecoder bitmapRegionDecoder;
        Preconditions.checkNotNull(inputStream);
        int targetWidth = options.outWidth;
        int targetHeight3 = options.outHeight;
        if (regionToDecode != null) {
            int targetWidth2 = regionToDecode.width() / options.inSampleSize;
            targetHeight = regionToDecode.height() / options.inSampleSize;
            targetHeight2 = targetWidth2;
        } else {
            targetHeight = targetHeight3;
            targetHeight2 = targetWidth;
        }
        boolean shouldUseHardwareBitmapConfig = false;
        if (Build.VERSION.SDK_INT >= 26) {
            PreverificationHelper preverificationHelper = this.mPreverificationHelper;
            shouldUseHardwareBitmapConfig = preverificationHelper != null && preverificationHelper.shouldUseHardwareBitmapConfig(options.inPreferredConfig);
        }
        if (regionToDecode != null || !shouldUseHardwareBitmapConfig) {
            if (regionToDecode != null && shouldUseHardwareBitmapConfig) {
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            }
            Bitmap bitmapToReuse2 = this.mBitmapPool.get(getBitmapSize(targetHeight2, targetHeight, options));
            if (bitmapToReuse2 != null) {
                bitmapToReuse = bitmapToReuse2;
            } else {
                throw new NullPointerException("BitmapPool.get returned null");
            }
        } else {
            options.inMutable = false;
            bitmapToReuse = null;
        }
        options.inBitmap = bitmapToReuse;
        if (Build.VERSION.SDK_INT >= 26 && transformToSRGB) {
            options.inPreferredColorSpace = ColorSpace.get(ColorSpace.Named.SRGB);
        }
        Bitmap decodedBitmap = null;
        ByteBuffer byteBuffer2 = this.mDecodeBuffers.acquire();
        if (byteBuffer2 == null) {
            byteBuffer = ByteBuffer.allocate(16384);
        } else {
            byteBuffer = byteBuffer2;
        }
        try {
            try {
                options.inTempStorage = byteBuffer.array();
                if (!(regionToDecode == null || bitmapToReuse == null)) {
                    try {
                        bitmapRegionDecoder = null;
                        try {
                            bitmapToReuse.reconfigure(targetHeight2, targetHeight, options.inPreferredConfig);
                            bitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, true);
                            decodedBitmap = bitmapRegionDecoder.decodeRegion(regionToDecode, options);
                        } catch (IOException e) {
                            FLog.e(TAG, "Could not decode region %s, decoding full bitmap instead.", regionToDecode);
                        }
                    } finally {
                        if (bitmapRegionDecoder != null) {
                            bitmapRegionDecoder.recycle();
                        }
                    }
                }
                if (decodedBitmap == null) {
                    decodedBitmap = BitmapFactory.decodeStream(inputStream, null, options);
                }
                this.mDecodeBuffers.release(byteBuffer);
                if (bitmapToReuse == null || bitmapToReuse == decodedBitmap) {
                    return CloseableReference.of(decodedBitmap, this.mBitmapPool);
                }
                this.mBitmapPool.release(bitmapToReuse);
                decodedBitmap.recycle();
                throw new IllegalStateException();
            } catch (Throwable th) {
                this.mDecodeBuffers.release(byteBuffer);
                throw th;
            }
        } catch (IllegalArgumentException e2) {
            if (bitmapToReuse != null) {
                this.mBitmapPool.release(bitmapToReuse);
            }
            try {
                inputStream.reset();
                Bitmap naiveDecodedBitmap = BitmapFactory.decodeStream(inputStream);
                if (naiveDecodedBitmap != null) {
                    CloseableReference<Bitmap> of = CloseableReference.of(naiveDecodedBitmap, SimpleBitmapReleaser.getInstance());
                    this.mDecodeBuffers.release(byteBuffer);
                    return of;
                }
                throw e2;
            } catch (IOException e3) {
                throw e2;
            }
        } catch (RuntimeException re) {
            if (bitmapToReuse != null) {
                this.mBitmapPool.release(bitmapToReuse);
            }
            throw re;
        }
    }

    private static BitmapFactory.Options getDecodeOptionsForStream(EncodedImage encodedImage, Bitmap.Config bitmapConfig) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = encodedImage.getSampleSize();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(encodedImage.getInputStream(), null, options);
        if (options.outWidth == -1 || options.outHeight == -1) {
            throw new IllegalArgumentException();
        }
        options.inJustDecodeBounds = false;
        options.inDither = true;
        options.inPreferredConfig = bitmapConfig;
        options.inMutable = true;
        return options;
    }
}
