package com.facebook.imagepipeline.common;

import android.graphics.Bitmap;
import com.facebook.imagepipeline.decoder.ImageDecoder;
import com.facebook.imagepipeline.transformation.BitmapTransformation;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class ImageDecodeOptionsBuilder {
    @Nullable
    private BitmapTransformation mBitmapTransformation;
    @Nullable
    private ImageDecoder mCustomImageDecoder;
    private boolean mDecodeAllFrames;
    private boolean mDecodePreviewFrame;
    private boolean mForceStaticImage;
    private boolean mTransformToSRGB;
    private boolean mUseLastFrameForPreview;
    private int mMinDecodeIntervalMs = 100;
    private Bitmap.Config mBitmapConfig = Bitmap.Config.ARGB_8888;

    public ImageDecodeOptionsBuilder setFrom(ImageDecodeOptions options) {
        this.mDecodePreviewFrame = options.decodePreviewFrame;
        this.mUseLastFrameForPreview = options.useLastFrameForPreview;
        this.mDecodeAllFrames = options.decodeAllFrames;
        this.mForceStaticImage = options.forceStaticImage;
        this.mBitmapConfig = options.bitmapConfig;
        this.mCustomImageDecoder = options.customImageDecoder;
        this.mTransformToSRGB = options.transformToSRGB;
        this.mBitmapTransformation = options.bitmapTransformation;
        return this;
    }

    public ImageDecodeOptionsBuilder setMinDecodeIntervalMs(int intervalMs) {
        this.mMinDecodeIntervalMs = intervalMs;
        return this;
    }

    public int getMinDecodeIntervalMs() {
        return this.mMinDecodeIntervalMs;
    }

    public ImageDecodeOptionsBuilder setDecodePreviewFrame(boolean decodePreviewFrame) {
        this.mDecodePreviewFrame = decodePreviewFrame;
        return this;
    }

    public boolean getDecodePreviewFrame() {
        return this.mDecodePreviewFrame;
    }

    public boolean getUseLastFrameForPreview() {
        return this.mUseLastFrameForPreview;
    }

    public ImageDecodeOptionsBuilder setUseLastFrameForPreview(boolean useLastFrameForPreview) {
        this.mUseLastFrameForPreview = useLastFrameForPreview;
        return this;
    }

    public boolean getDecodeAllFrames() {
        return this.mDecodeAllFrames;
    }

    public ImageDecodeOptionsBuilder setDecodeAllFrames(boolean decodeAllFrames) {
        this.mDecodeAllFrames = decodeAllFrames;
        return this;
    }

    public ImageDecodeOptionsBuilder setForceStaticImage(boolean forceStaticImage) {
        this.mForceStaticImage = forceStaticImage;
        return this;
    }

    public ImageDecodeOptionsBuilder setCustomImageDecoder(@Nullable ImageDecoder customImageDecoder) {
        this.mCustomImageDecoder = customImageDecoder;
        return this;
    }

    @Nullable
    public ImageDecoder getCustomImageDecoder() {
        return this.mCustomImageDecoder;
    }

    public boolean getForceStaticImage() {
        return this.mForceStaticImage;
    }

    public Bitmap.Config getBitmapConfig() {
        return this.mBitmapConfig;
    }

    public ImageDecodeOptionsBuilder setBitmapConfig(Bitmap.Config bitmapConfig) {
        this.mBitmapConfig = bitmapConfig;
        return this;
    }

    public boolean getTransformToSRGB() {
        return this.mTransformToSRGB;
    }

    public ImageDecodeOptionsBuilder setTransformToSRGB(boolean transformToSRGB) {
        this.mTransformToSRGB = transformToSRGB;
        return this;
    }

    public ImageDecodeOptionsBuilder setBitmapTransformation(@Nullable BitmapTransformation bitmapTransformation) {
        this.mBitmapTransformation = bitmapTransformation;
        return this;
    }

    @Nullable
    public BitmapTransformation getBitmapTransformation() {
        return this.mBitmapTransformation;
    }

    public ImageDecodeOptions build() {
        return new ImageDecodeOptions(this);
    }
}
