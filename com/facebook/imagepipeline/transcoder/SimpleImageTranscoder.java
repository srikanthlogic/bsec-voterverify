package com.facebook.imagepipeline.transcoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import com.facebook.common.logging.FLog;
import com.facebook.imageformat.DefaultImageFormats;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.EncodedImage;
import java.io.OutputStream;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class SimpleImageTranscoder implements ImageTranscoder {
    private static final String TAG = "SimpleImageTranscoder";
    private final int mMaxBitmapSize;
    private final boolean mResizingEnabled;

    public SimpleImageTranscoder(boolean resizingEnabled, int maxBitmapSize) {
        this.mResizingEnabled = resizingEnabled;
        this.mMaxBitmapSize = maxBitmapSize;
    }

    @Override // com.facebook.imagepipeline.transcoder.ImageTranscoder
    public ImageTranscodeResult transcode(EncodedImage encodedImage, OutputStream outputStream, @Nullable RotationOptions rotationOptions, @Nullable ResizeOptions resizeOptions, @Nullable ImageFormat outputFormat, @Nullable Integer quality) {
        Integer quality2;
        RotationOptions rotationOptions2;
        Bitmap srcBitmap;
        OutOfMemoryError oom;
        OutOfMemoryError oom2;
        if (quality == null) {
            quality2 = 85;
        } else {
            quality2 = quality;
        }
        if (rotationOptions == null) {
            rotationOptions2 = RotationOptions.autoRotate();
        } else {
            rotationOptions2 = rotationOptions;
        }
        int sampleSize = getSampleSize(encodedImage, rotationOptions2, resizeOptions);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        try {
            Bitmap resizedBitmap = BitmapFactory.decodeStream(encodedImage.getInputStream(), null, options);
            if (resizedBitmap == null) {
                FLog.e(TAG, "Couldn't decode the EncodedImage InputStream ! ");
                return new ImageTranscodeResult(2);
            }
            Matrix transformationMatrix = JpegTranscoderUtils.getTransformationMatrix(encodedImage, rotationOptions2);
            Bitmap srcBitmap2 = resizedBitmap;
            if (transformationMatrix != null) {
                try {
                    srcBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight(), transformationMatrix, false);
                } catch (OutOfMemoryError e) {
                    oom = e;
                    try {
                        FLog.e(TAG, "Out-Of-Memory during transcode", oom);
                        ImageTranscodeResult imageTranscodeResult = new ImageTranscodeResult(2);
                        srcBitmap2.recycle();
                        resizedBitmap.recycle();
                        return imageTranscodeResult;
                    } catch (Throwable th) {
                        oom2 = th;
                        srcBitmap2.recycle();
                        resizedBitmap.recycle();
                        throw oom2;
                    }
                } catch (Throwable th2) {
                    oom2 = th2;
                    srcBitmap2.recycle();
                    resizedBitmap.recycle();
                    throw oom2;
                }
            } else {
                srcBitmap = srcBitmap2;
            }
            try {
            } catch (OutOfMemoryError e2) {
                oom = e2;
            } catch (Throwable th3) {
                oom2 = th3;
            }
            try {
                srcBitmap.compress(getOutputFormat(outputFormat), quality2.intValue(), outputStream);
                int i = 1;
                if (sampleSize > 1) {
                    i = 0;
                }
                ImageTranscodeResult imageTranscodeResult2 = new ImageTranscodeResult(i);
                srcBitmap.recycle();
                resizedBitmap.recycle();
                return imageTranscodeResult2;
            } catch (OutOfMemoryError e3) {
                oom = e3;
                srcBitmap2 = srcBitmap;
                FLog.e(TAG, "Out-Of-Memory during transcode", oom);
                ImageTranscodeResult imageTranscodeResult3 = new ImageTranscodeResult(2);
                srcBitmap2.recycle();
                resizedBitmap.recycle();
                return imageTranscodeResult3;
            } catch (Throwable th4) {
                oom2 = th4;
                srcBitmap2 = srcBitmap;
                srcBitmap2.recycle();
                resizedBitmap.recycle();
                throw oom2;
            }
        } catch (OutOfMemoryError oom3) {
            FLog.e(TAG, "Out-Of-Memory during transcode", oom3);
            return new ImageTranscodeResult(2);
        }
    }

    @Override // com.facebook.imagepipeline.transcoder.ImageTranscoder
    public boolean canResize(EncodedImage encodedImage, @Nullable RotationOptions rotationOptions, @Nullable ResizeOptions resizeOptions) {
        if (rotationOptions == null) {
            rotationOptions = RotationOptions.autoRotate();
        }
        if (!this.mResizingEnabled || DownsampleUtil.determineSampleSize(rotationOptions, resizeOptions, encodedImage, this.mMaxBitmapSize) <= 1) {
            return false;
        }
        return true;
    }

    @Override // com.facebook.imagepipeline.transcoder.ImageTranscoder
    public boolean canTranscode(ImageFormat imageFormat) {
        return imageFormat == DefaultImageFormats.HEIF || imageFormat == DefaultImageFormats.JPEG;
    }

    @Override // com.facebook.imagepipeline.transcoder.ImageTranscoder
    public String getIdentifier() {
        return TAG;
    }

    private int getSampleSize(EncodedImage encodedImage, RotationOptions rotationOptions, @Nullable ResizeOptions resizeOptions) {
        if (!this.mResizingEnabled) {
            return 1;
        }
        return DownsampleUtil.determineSampleSize(rotationOptions, resizeOptions, encodedImage, this.mMaxBitmapSize);
    }

    private static Bitmap.CompressFormat getOutputFormat(@Nullable ImageFormat format) {
        if (format == null) {
            return Bitmap.CompressFormat.JPEG;
        }
        if (format == DefaultImageFormats.JPEG) {
            return Bitmap.CompressFormat.JPEG;
        }
        if (format == DefaultImageFormats.PNG) {
            return Bitmap.CompressFormat.PNG;
        }
        if (Build.VERSION.SDK_INT < 14 || !DefaultImageFormats.isStaticWebpFormat(format)) {
            return Bitmap.CompressFormat.JPEG;
        }
        return Bitmap.CompressFormat.WEBP;
    }
}
