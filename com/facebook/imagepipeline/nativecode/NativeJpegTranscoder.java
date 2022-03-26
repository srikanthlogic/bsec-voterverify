package com.facebook.imagepipeline.nativecode;

import com.facebook.common.internal.Closeables;
import com.facebook.common.internal.Preconditions;
import com.facebook.imageformat.DefaultImageFormats;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.transcoder.DownsampleUtil;
import com.facebook.imagepipeline.transcoder.ImageTranscodeResult;
import com.facebook.imagepipeline.transcoder.ImageTranscoder;
import com.facebook.imagepipeline.transcoder.JpegTranscoderUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class NativeJpegTranscoder implements ImageTranscoder {
    public static final String TAG = "NativeJpegTranscoder";
    private int mMaxBitmapSize;
    private boolean mResizingEnabled;
    private boolean mUseDownsamplingRatio;

    private static native void nativeTranscodeJpeg(InputStream inputStream, OutputStream outputStream, int i, int i2, int i3) throws IOException;

    private static native void nativeTranscodeJpegWithExifOrientation(InputStream inputStream, OutputStream outputStream, int i, int i2, int i3) throws IOException;

    static {
        NativeJpegTranscoderSoLoader.ensure();
    }

    public NativeJpegTranscoder(boolean resizingEnabled, int maxBitmapSize, boolean useDownsamplingRatio) {
        this.mResizingEnabled = resizingEnabled;
        this.mMaxBitmapSize = maxBitmapSize;
        this.mUseDownsamplingRatio = useDownsamplingRatio;
    }

    @Override // com.facebook.imagepipeline.transcoder.ImageTranscoder
    public boolean canResize(EncodedImage encodedImage, @Nullable RotationOptions rotationOptions, @Nullable ResizeOptions resizeOptions) {
        if (rotationOptions == null) {
            rotationOptions = RotationOptions.autoRotate();
        }
        return JpegTranscoderUtils.getSoftwareNumerator(rotationOptions, resizeOptions, encodedImage, this.mResizingEnabled) < 8;
    }

    @Override // com.facebook.imagepipeline.transcoder.ImageTranscoder
    public boolean canTranscode(ImageFormat imageFormat) {
        return imageFormat == DefaultImageFormats.JPEG;
    }

    @Override // com.facebook.imagepipeline.transcoder.ImageTranscoder
    public String getIdentifier() {
        return TAG;
    }

    /* JADX WARN: Finally extract failed */
    @Override // com.facebook.imagepipeline.transcoder.ImageTranscoder
    public ImageTranscodeResult transcode(EncodedImage encodedImage, OutputStream outputStream, @Nullable RotationOptions rotationOptions, @Nullable ResizeOptions resizeOptions, @Nullable ImageFormat outputFormat, @Nullable Integer quality) throws IOException {
        int numerator;
        if (quality == null) {
            quality = 85;
        }
        if (rotationOptions == null) {
            rotationOptions = RotationOptions.autoRotate();
        }
        int downsampleRatio = DownsampleUtil.determineSampleSize(rotationOptions, resizeOptions, encodedImage, this.mMaxBitmapSize);
        try {
            int softwareNumerator = JpegTranscoderUtils.getSoftwareNumerator(rotationOptions, resizeOptions, encodedImage, this.mResizingEnabled);
            int downsampleNumerator = JpegTranscoderUtils.calculateDownsampleNumerator(downsampleRatio);
            if (this.mUseDownsamplingRatio) {
                numerator = downsampleNumerator;
            } else {
                numerator = softwareNumerator;
            }
            InputStream is = encodedImage.getInputStream();
            if (JpegTranscoderUtils.INVERTED_EXIF_ORIENTATIONS.contains(Integer.valueOf(encodedImage.getExifOrientation()))) {
                transcodeJpegWithExifOrientation(is, outputStream, JpegTranscoderUtils.getForceRotatedInvertedExifOrientation(rotationOptions, encodedImage), numerator, quality.intValue());
            } else {
                transcodeJpeg(is, outputStream, JpegTranscoderUtils.getRotationAngle(rotationOptions, encodedImage), numerator, quality.intValue());
            }
            Closeables.closeQuietly(is);
            int i = 1;
            if (downsampleRatio != 1) {
                i = 0;
            }
            return new ImageTranscodeResult(i);
        } catch (Throwable th) {
            Closeables.closeQuietly((InputStream) null);
            throw th;
        }
    }

    public static void transcodeJpeg(InputStream inputStream, OutputStream outputStream, int rotationAngle, int scaleNumerator, int quality) throws IOException {
        NativeJpegTranscoderSoLoader.ensure();
        boolean z = false;
        Preconditions.checkArgument(scaleNumerator >= 1);
        Preconditions.checkArgument(scaleNumerator <= 16);
        Preconditions.checkArgument(quality >= 0);
        Preconditions.checkArgument(quality <= 100);
        Preconditions.checkArgument(JpegTranscoderUtils.isRotationAngleAllowed(rotationAngle));
        if (!(scaleNumerator == 8 && rotationAngle == 0)) {
            z = true;
        }
        Preconditions.checkArgument(z, "no transformation requested");
        nativeTranscodeJpeg((InputStream) Preconditions.checkNotNull(inputStream), (OutputStream) Preconditions.checkNotNull(outputStream), rotationAngle, scaleNumerator, quality);
    }

    public static void transcodeJpegWithExifOrientation(InputStream inputStream, OutputStream outputStream, int exifOrientation, int scaleNumerator, int quality) throws IOException {
        NativeJpegTranscoderSoLoader.ensure();
        boolean z = false;
        Preconditions.checkArgument(scaleNumerator >= 1);
        Preconditions.checkArgument(scaleNumerator <= 16);
        Preconditions.checkArgument(quality >= 0);
        Preconditions.checkArgument(quality <= 100);
        Preconditions.checkArgument(JpegTranscoderUtils.isExifOrientationAllowed(exifOrientation));
        if (!(scaleNumerator == 8 && exifOrientation == 1)) {
            z = true;
        }
        Preconditions.checkArgument(z, "no transformation requested");
        nativeTranscodeJpegWithExifOrientation((InputStream) Preconditions.checkNotNull(inputStream), (OutputStream) Preconditions.checkNotNull(outputStream), exifOrientation, scaleNumerator, quality);
    }
}
