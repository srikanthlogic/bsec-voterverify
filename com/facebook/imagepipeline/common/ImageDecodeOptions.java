package com.facebook.imagepipeline.common;

import android.graphics.Bitmap;
import com.facebook.imagepipeline.decoder.ImageDecoder;
import com.facebook.imagepipeline.transformation.BitmapTransformation;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class ImageDecodeOptions {
    private static final ImageDecodeOptions DEFAULTS = newBuilder().build();
    public final Bitmap.Config bitmapConfig;
    @Nullable
    public final BitmapTransformation bitmapTransformation;
    @Nullable
    public final ImageDecoder customImageDecoder;
    public final boolean decodeAllFrames;
    public final boolean decodePreviewFrame;
    public final boolean forceStaticImage;
    public final int minDecodeIntervalMs;
    public final boolean transformToSRGB;
    public final boolean useLastFrameForPreview;

    public ImageDecodeOptions(ImageDecodeOptionsBuilder b) {
        this.minDecodeIntervalMs = b.getMinDecodeIntervalMs();
        this.decodePreviewFrame = b.getDecodePreviewFrame();
        this.useLastFrameForPreview = b.getUseLastFrameForPreview();
        this.decodeAllFrames = b.getDecodeAllFrames();
        this.forceStaticImage = b.getForceStaticImage();
        this.bitmapConfig = b.getBitmapConfig();
        this.customImageDecoder = b.getCustomImageDecoder();
        this.transformToSRGB = b.getTransformToSRGB();
        this.bitmapTransformation = b.getBitmapTransformation();
    }

    public static ImageDecodeOptions defaults() {
        return DEFAULTS;
    }

    public static ImageDecodeOptionsBuilder newBuilder() {
        return new ImageDecodeOptionsBuilder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImageDecodeOptions that = (ImageDecodeOptions) o;
        if (this.decodePreviewFrame == that.decodePreviewFrame && this.useLastFrameForPreview == that.useLastFrameForPreview && this.decodeAllFrames == that.decodeAllFrames && this.forceStaticImage == that.forceStaticImage && this.transformToSRGB == that.transformToSRGB && this.bitmapConfig == that.bitmapConfig && this.customImageDecoder == that.customImageDecoder && this.bitmapTransformation == that.bitmapTransformation) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = ((((((((((((this.minDecodeIntervalMs * 31) + (this.decodePreviewFrame ? 1 : 0)) * 31) + (this.useLastFrameForPreview ? 1 : 0)) * 31) + (this.decodeAllFrames ? 1 : 0)) * 31) + (this.forceStaticImage ? 1 : 0)) * 31) + (this.transformToSRGB ? 1 : 0)) * 31) + this.bitmapConfig.ordinal()) * 31;
        ImageDecoder imageDecoder = this.customImageDecoder;
        int i = 0;
        int result2 = (result + (imageDecoder != null ? imageDecoder.hashCode() : 0)) * 31;
        BitmapTransformation bitmapTransformation = this.bitmapTransformation;
        if (bitmapTransformation != null) {
            i = bitmapTransformation.hashCode();
        }
        return result2 + i;
    }

    public String toString() {
        return String.format(null, "%d-%b-%b-%b-%b-%b-%s-%s-%s", Integer.valueOf(this.minDecodeIntervalMs), Boolean.valueOf(this.decodePreviewFrame), Boolean.valueOf(this.useLastFrameForPreview), Boolean.valueOf(this.decodeAllFrames), Boolean.valueOf(this.forceStaticImage), Boolean.valueOf(this.transformToSRGB), this.bitmapConfig.name(), this.customImageDecoder, this.bitmapTransformation);
    }
}
