package com.facebook.imagepipeline.transcoder;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.logging.FLog;
import com.facebook.imageformat.DefaultImageFormats;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.EncodedImage;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class DownsampleUtil {
    public static final int DEFAULT_SAMPLE_SIZE = 1;
    private static final float INTERVAL_ROUNDING = 0.33333334f;

    private DownsampleUtil() {
    }

    public static int determineSampleSize(RotationOptions rotationOptions, @Nullable ResizeOptions resizeOptions, EncodedImage encodedImage, int maxBitmapSize) {
        int sampleSize;
        if (!EncodedImage.isMetaDataAvailable(encodedImage)) {
            return 1;
        }
        float ratio = determineDownsampleRatio(rotationOptions, resizeOptions, encodedImage);
        if (encodedImage.getImageFormat() == DefaultImageFormats.JPEG) {
            sampleSize = ratioToSampleSizeJPEG(ratio);
        } else {
            sampleSize = ratioToSampleSize(ratio);
        }
        int maxDimension = Math.max(encodedImage.getHeight(), encodedImage.getWidth());
        float computedMaxBitmapSize = resizeOptions != null ? resizeOptions.maxBitmapSize : (float) maxBitmapSize;
        while (((float) (maxDimension / sampleSize)) > computedMaxBitmapSize) {
            if (encodedImage.getImageFormat() == DefaultImageFormats.JPEG) {
                sampleSize *= 2;
            } else {
                sampleSize++;
            }
        }
        return sampleSize;
    }

    public static float determineDownsampleRatio(RotationOptions rotationOptions, @Nullable ResizeOptions resizeOptions, EncodedImage encodedImage) {
        Preconditions.checkArgument(EncodedImage.isMetaDataAvailable(encodedImage));
        if (resizeOptions == null || resizeOptions.height <= 0 || resizeOptions.width <= 0 || encodedImage.getWidth() == 0 || encodedImage.getHeight() == 0) {
            return 1.0f;
        }
        int rotationAngle = getRotationAngle(rotationOptions, encodedImage);
        boolean swapDimensions = rotationAngle == 90 || rotationAngle == 270;
        int widthAfterRotation = swapDimensions ? encodedImage.getHeight() : encodedImage.getWidth();
        int heightAfterRotation = swapDimensions ? encodedImage.getWidth() : encodedImage.getHeight();
        float widthRatio = ((float) resizeOptions.width) / ((float) widthAfterRotation);
        float heightRatio = ((float) resizeOptions.height) / ((float) heightAfterRotation);
        float ratio = Math.max(widthRatio, heightRatio);
        FLog.v("DownsampleUtil", "Downsample - Specified size: %dx%d, image size: %dx%d ratio: %.1f x %.1f, ratio: %.3f", Integer.valueOf(resizeOptions.width), Integer.valueOf(resizeOptions.height), Integer.valueOf(widthAfterRotation), Integer.valueOf(heightAfterRotation), Float.valueOf(widthRatio), Float.valueOf(heightRatio), Float.valueOf(ratio));
        return ratio;
    }

    public static int ratioToSampleSize(float ratio) {
        if (ratio > 0.6666667f) {
            return 1;
        }
        int sampleSize = 2;
        while ((1.0d / ((double) sampleSize)) + (0.3333333432674408d * (1.0d / (Math.pow((double) sampleSize, 2.0d) - ((double) sampleSize)))) > ((double) ratio)) {
            sampleSize++;
        }
        return sampleSize - 1;
    }

    public static int ratioToSampleSizeJPEG(float ratio) {
        if (ratio > 0.6666667f) {
            return 1;
        }
        int sampleSize = 2;
        while ((1.0d / ((double) (sampleSize * 2))) + (0.3333333432674408d * (1.0d / ((double) (sampleSize * 2)))) > ((double) ratio)) {
            sampleSize *= 2;
        }
        return sampleSize;
    }

    private static int getRotationAngle(RotationOptions rotationOptions, EncodedImage encodedImage) {
        boolean z = false;
        if (!rotationOptions.useImageMetadata()) {
            return 0;
        }
        int rotationAngle = encodedImage.getRotationAngle();
        if (rotationAngle == 0 || rotationAngle == 90 || rotationAngle == 180 || rotationAngle == 270) {
            z = true;
        }
        Preconditions.checkArgument(z);
        return rotationAngle;
    }

    public static int roundToPowerOfTwo(int sampleSize) {
        int compare = 1;
        while (compare < sampleSize) {
            compare *= 2;
        }
        return compare;
    }
}
