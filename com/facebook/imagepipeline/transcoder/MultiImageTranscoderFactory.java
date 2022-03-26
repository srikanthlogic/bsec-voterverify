package com.facebook.imagepipeline.transcoder;

import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.nativecode.NativeImageTranscoderFactory;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class MultiImageTranscoderFactory implements ImageTranscoderFactory {
    @Nullable
    private final Integer mImageTranscoderType;
    private final int mMaxBitmapSize;
    @Nullable
    private final ImageTranscoderFactory mPrimaryImageTranscoderFactory;
    private final boolean mUseDownSamplingRatio;

    public MultiImageTranscoderFactory(int maxBitmapSize, boolean useDownSamplingRatio, @Nullable ImageTranscoderFactory primaryImageTranscoderFactory, @Nullable Integer imageTranscoderType) {
        this.mMaxBitmapSize = maxBitmapSize;
        this.mUseDownSamplingRatio = useDownSamplingRatio;
        this.mPrimaryImageTranscoderFactory = primaryImageTranscoderFactory;
        this.mImageTranscoderType = imageTranscoderType;
    }

    @Override // com.facebook.imagepipeline.transcoder.ImageTranscoderFactory
    public ImageTranscoder createImageTranscoder(ImageFormat imageFormat, boolean isResizingEnabled) {
        ImageTranscoder imageTranscoder = getCustomImageTranscoder(imageFormat, isResizingEnabled);
        if (imageTranscoder == null) {
            imageTranscoder = getImageTranscoderWithType(imageFormat, isResizingEnabled);
        }
        if (imageTranscoder == null) {
            imageTranscoder = getNativeImageTranscoder(imageFormat, isResizingEnabled);
        }
        return imageTranscoder == null ? getSimpleImageTranscoder(imageFormat, isResizingEnabled) : imageTranscoder;
    }

    @Nullable
    private ImageTranscoder getCustomImageTranscoder(ImageFormat imageFormat, boolean isResizingEnabled) {
        ImageTranscoderFactory imageTranscoderFactory = this.mPrimaryImageTranscoderFactory;
        if (imageTranscoderFactory == null) {
            return null;
        }
        return imageTranscoderFactory.createImageTranscoder(imageFormat, isResizingEnabled);
    }

    @Nullable
    private ImageTranscoder getNativeImageTranscoder(ImageFormat imageFormat, boolean isResizingEnabled) {
        return NativeImageTranscoderFactory.getNativeImageTranscoderFactory(this.mMaxBitmapSize, this.mUseDownSamplingRatio).createImageTranscoder(imageFormat, isResizingEnabled);
    }

    private ImageTranscoder getSimpleImageTranscoder(ImageFormat imageFormat, boolean isResizingEnabled) {
        return new SimpleImageTranscoderFactory(this.mMaxBitmapSize).createImageTranscoder(imageFormat, isResizingEnabled);
    }

    @Nullable
    private ImageTranscoder getImageTranscoderWithType(ImageFormat imageFormat, boolean isResizingEnabled) {
        Integer num = this.mImageTranscoderType;
        if (num == null) {
            return null;
        }
        int intValue = num.intValue();
        if (intValue == 0) {
            return getNativeImageTranscoder(imageFormat, isResizingEnabled);
        }
        if (intValue == 1) {
            return getSimpleImageTranscoder(imageFormat, isResizingEnabled);
        }
        throw new IllegalArgumentException("Invalid ImageTranscoderType");
    }
}
