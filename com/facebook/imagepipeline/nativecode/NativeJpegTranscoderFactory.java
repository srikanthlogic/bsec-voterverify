package com.facebook.imagepipeline.nativecode;

import com.facebook.imageformat.DefaultImageFormats;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.transcoder.ImageTranscoder;
import com.facebook.imagepipeline.transcoder.ImageTranscoderFactory;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class NativeJpegTranscoderFactory implements ImageTranscoderFactory {
    private final int mMaxBitmapSize;
    private final boolean mUseDownSamplingRatio;

    public NativeJpegTranscoderFactory(int maxBitmapSize, boolean useDownSamplingRatio) {
        this.mMaxBitmapSize = maxBitmapSize;
        this.mUseDownSamplingRatio = useDownSamplingRatio;
    }

    @Override // com.facebook.imagepipeline.transcoder.ImageTranscoderFactory
    @Nullable
    public ImageTranscoder createImageTranscoder(ImageFormat imageFormat, boolean isResizingEnabled) {
        if (imageFormat != DefaultImageFormats.JPEG) {
            return null;
        }
        return new NativeJpegTranscoder(isResizingEnabled, this.mMaxBitmapSize, this.mUseDownSamplingRatio);
    }
}
