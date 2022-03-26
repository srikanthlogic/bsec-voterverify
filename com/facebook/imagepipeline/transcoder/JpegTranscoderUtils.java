package com.facebook.imagepipeline.transcoder;

import android.graphics.Matrix;
import com.facebook.common.internal.ImmutableList;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.EncodedImage;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class JpegTranscoderUtils {
    public static final int DEFAULT_JPEG_QUALITY = 85;
    private static final int FULL_ROUND = 360;
    public static final ImmutableList<Integer> INVERTED_EXIF_ORIENTATIONS = ImmutableList.of((Object[]) new Integer[]{2, 7, 4, 5});
    public static final int MAX_QUALITY = 100;
    public static final int MAX_SCALE_NUMERATOR = 16;
    public static final int MIN_QUALITY = 0;
    public static final int MIN_SCALE_NUMERATOR = 1;
    public static final int SCALE_DENOMINATOR = 8;

    public static boolean isRotationAngleAllowed(int degrees) {
        return degrees >= 0 && degrees <= 270 && degrees % 90 == 0;
    }

    public static boolean isExifOrientationAllowed(int exifOrientation) {
        switch (exifOrientation) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return true;
            default:
                return false;
        }
    }

    public static int getSoftwareNumerator(RotationOptions rotationOptions, @Nullable ResizeOptions resizeOptions, EncodedImage encodedImage, boolean resizingEnabled) {
        if (!resizingEnabled || resizeOptions == null) {
            return 8;
        }
        int rotationAngle = getRotationAngle(rotationOptions, encodedImage);
        int exifOrientation = 0;
        if (INVERTED_EXIF_ORIENTATIONS.contains(Integer.valueOf(encodedImage.getExifOrientation()))) {
            exifOrientation = getForceRotatedInvertedExifOrientation(rotationOptions, encodedImage);
        }
        boolean swapDimensions = rotationAngle == 90 || rotationAngle == 270 || exifOrientation == 5 || exifOrientation == 7;
        int numerator = roundNumerator(determineResizeRatio(resizeOptions, swapDimensions ? encodedImage.getHeight() : encodedImage.getWidth(), swapDimensions ? encodedImage.getWidth() : encodedImage.getHeight()), resizeOptions.roundUpFraction);
        if (numerator > 8) {
            return 8;
        }
        if (numerator < 1) {
            return 1;
        }
        return numerator;
    }

    public static int getRotationAngle(RotationOptions rotationOptions, EncodedImage encodedImage) {
        if (!rotationOptions.rotationEnabled()) {
            return 0;
        }
        int rotationFromMetadata = extractOrientationFromMetadata(encodedImage);
        if (rotationOptions.useImageMetadata()) {
            return rotationFromMetadata;
        }
        return (rotationOptions.getForcedAngle() + rotationFromMetadata) % FULL_ROUND;
    }

    public static int getForceRotatedInvertedExifOrientation(RotationOptions rotationOptions, EncodedImage encodedImage) {
        int index = INVERTED_EXIF_ORIENTATIONS.indexOf(Integer.valueOf(encodedImage.getExifOrientation()));
        if (index >= 0) {
            int forcedAngle = 0;
            if (!rotationOptions.useImageMetadata()) {
                forcedAngle = rotationOptions.getForcedAngle();
            }
            ImmutableList<Integer> immutableList = INVERTED_EXIF_ORIENTATIONS;
            return immutableList.get((index + (forcedAngle / 90)) % immutableList.size()).intValue();
        }
        throw new IllegalArgumentException("Only accepts inverted exif orientations");
    }

    public static float determineResizeRatio(ResizeOptions resizeOptions, int width, int height) {
        if (resizeOptions == null) {
            return 1.0f;
        }
        float ratio = Math.max(((float) resizeOptions.width) / ((float) width), ((float) resizeOptions.height) / ((float) height));
        if (((float) width) * ratio > resizeOptions.maxBitmapSize) {
            ratio = resizeOptions.maxBitmapSize / ((float) width);
        }
        if (((float) height) * ratio > resizeOptions.maxBitmapSize) {
            return resizeOptions.maxBitmapSize / ((float) height);
        }
        return ratio;
    }

    public static int roundNumerator(float maxRatio, float roundUpFraction) {
        return (int) ((8.0f * maxRatio) + roundUpFraction);
    }

    public static int calculateDownsampleNumerator(int downsampleRatio) {
        return Math.max(1, 8 / downsampleRatio);
    }

    @Nullable
    public static Matrix getTransformationMatrix(EncodedImage encodedImage, RotationOptions rotationOptions) {
        if (INVERTED_EXIF_ORIENTATIONS.contains(Integer.valueOf(encodedImage.getExifOrientation()))) {
            return getTransformationMatrixFromInvertedExif(getForceRotatedInvertedExifOrientation(rotationOptions, encodedImage));
        }
        int rotationAngle = getRotationAngle(rotationOptions, encodedImage);
        if (rotationAngle == 0) {
            return null;
        }
        Matrix transformationMatrix = new Matrix();
        transformationMatrix.setRotate((float) rotationAngle);
        return transformationMatrix;
    }

    @Nullable
    private static Matrix getTransformationMatrixFromInvertedExif(int orientation) {
        Matrix matrix = new Matrix();
        if (orientation == 2) {
            matrix.setScale(-1.0f, 1.0f);
        } else if (orientation == 7) {
            matrix.setRotate(-90.0f);
            matrix.postScale(-1.0f, 1.0f);
        } else if (orientation == 4) {
            matrix.setRotate(180.0f);
            matrix.postScale(-1.0f, 1.0f);
        } else if (orientation != 5) {
            return null;
        } else {
            matrix.setRotate(90.0f);
            matrix.postScale(-1.0f, 1.0f);
        }
        return matrix;
    }

    private static int extractOrientationFromMetadata(EncodedImage encodedImage) {
        int rotationAngle = encodedImage.getRotationAngle();
        if (rotationAngle == 90 || rotationAngle == 180 || rotationAngle == 270) {
            return encodedImage.getRotationAngle();
        }
        return 0;
    }
}
