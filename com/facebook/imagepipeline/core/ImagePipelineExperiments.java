package com.facebook.imagepipeline.core;

import android.content.Context;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.ByteArrayPool;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.memory.PooledByteBufferFactory;
import com.facebook.common.webp.WebpBitmapFactory;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.cache.BufferedDiskCache;
import com.facebook.imagepipeline.cache.CacheKeyFactory;
import com.facebook.imagepipeline.cache.MemoryCache;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ImageDecoder;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.image.CloseableImage;
/* loaded from: classes.dex */
public class ImagePipelineExperiments {
    private boolean mBitmapPrepareToDrawForPrefetch;
    private final int mBitmapPrepareToDrawMaxSizeBytes;
    private final int mBitmapPrepareToDrawMinSizeBytes;
    private final boolean mDecodeCancellationEnabled;
    private final boolean mDownscaleFrameToDrawableDimensions;
    private final boolean mGingerbreadDecoderEnabled;
    private final Supplier<Boolean> mLazyDataSource;
    private final int mMaxBitmapSize;
    private final boolean mNativeCodeDisabled;
    private final boolean mPartialImageCachingEnabled;
    private final ProducerFactoryMethod mProducerFactoryMethod;
    private final boolean mUseBitmapPrepareToDraw;
    private final boolean mUseDownsamplingRatioForResizing;
    private final WebpBitmapFactory mWebpBitmapFactory;
    private final WebpBitmapFactory.WebpErrorLogger mWebpErrorLogger;
    private final boolean mWebpSupportEnabled;

    /* loaded from: classes.dex */
    public interface ProducerFactoryMethod {
        ProducerFactory createProducerFactory(Context context, ByteArrayPool byteArrayPool, ImageDecoder imageDecoder, ProgressiveJpegConfig progressiveJpegConfig, boolean z, boolean z2, boolean z3, ExecutorSupplier executorSupplier, PooledByteBufferFactory pooledByteBufferFactory, MemoryCache<CacheKey, CloseableImage> memoryCache, MemoryCache<CacheKey, PooledByteBuffer> memoryCache2, BufferedDiskCache bufferedDiskCache, BufferedDiskCache bufferedDiskCache2, CacheKeyFactory cacheKeyFactory, PlatformBitmapFactory platformBitmapFactory, int i, int i2, boolean z4, int i3);
    }

    private ImagePipelineExperiments(Builder builder) {
        this.mWebpSupportEnabled = builder.mWebpSupportEnabled;
        this.mWebpErrorLogger = builder.mWebpErrorLogger;
        this.mDecodeCancellationEnabled = builder.mDecodeCancellationEnabled;
        this.mWebpBitmapFactory = builder.mWebpBitmapFactory;
        this.mUseDownsamplingRatioForResizing = builder.mUseDownsamplingRatioForResizing;
        this.mUseBitmapPrepareToDraw = builder.mUseBitmapPrepareToDraw;
        this.mBitmapPrepareToDrawMinSizeBytes = builder.mBitmapPrepareToDrawMinSizeBytes;
        this.mBitmapPrepareToDrawMaxSizeBytes = builder.mBitmapPrepareToDrawMaxSizeBytes;
        this.mBitmapPrepareToDrawForPrefetch = builder.mBitmapPrepareToDrawForPrefetch;
        this.mMaxBitmapSize = builder.mMaxBitmapSize;
        this.mNativeCodeDisabled = builder.mNativeCodeDisabled;
        this.mPartialImageCachingEnabled = builder.mPartialImageCachingEnabled;
        if (builder.mProducerFactoryMethod == null) {
            this.mProducerFactoryMethod = new DefaultProducerFactoryMethod();
        } else {
            this.mProducerFactoryMethod = builder.mProducerFactoryMethod;
        }
        this.mLazyDataSource = builder.mLazyDataSource;
        this.mGingerbreadDecoderEnabled = builder.mGingerbreadDecoderEnabled;
        this.mDownscaleFrameToDrawableDimensions = builder.mDownscaleFrameToDrawableDimensions;
    }

    public boolean getUseDownsamplingRatioForResizing() {
        return this.mUseDownsamplingRatioForResizing;
    }

    public boolean isWebpSupportEnabled() {
        return this.mWebpSupportEnabled;
    }

    public boolean isDecodeCancellationEnabled() {
        return this.mDecodeCancellationEnabled;
    }

    public WebpBitmapFactory.WebpErrorLogger getWebpErrorLogger() {
        return this.mWebpErrorLogger;
    }

    public WebpBitmapFactory getWebpBitmapFactory() {
        return this.mWebpBitmapFactory;
    }

    public boolean getUseBitmapPrepareToDraw() {
        return this.mUseBitmapPrepareToDraw;
    }

    public int getBitmapPrepareToDrawMinSizeBytes() {
        return this.mBitmapPrepareToDrawMinSizeBytes;
    }

    public int getBitmapPrepareToDrawMaxSizeBytes() {
        return this.mBitmapPrepareToDrawMaxSizeBytes;
    }

    public boolean isNativeCodeDisabled() {
        return this.mNativeCodeDisabled;
    }

    public boolean isPartialImageCachingEnabled() {
        return this.mPartialImageCachingEnabled;
    }

    public ProducerFactoryMethod getProducerFactoryMethod() {
        return this.mProducerFactoryMethod;
    }

    public static Builder newBuilder(ImagePipelineConfig.Builder configBuilder) {
        return new Builder(configBuilder);
    }

    public boolean getBitmapPrepareToDrawForPrefetch() {
        return this.mBitmapPrepareToDrawForPrefetch;
    }

    public int getMaxBitmapSize() {
        return this.mMaxBitmapSize;
    }

    public Supplier<Boolean> isLazyDataSource() {
        return this.mLazyDataSource;
    }

    public boolean isGingerbreadDecoderEnabled() {
        return this.mGingerbreadDecoderEnabled;
    }

    public boolean shouldDownscaleFrameToDrawableDimensions() {
        return this.mDownscaleFrameToDrawableDimensions;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private final ImagePipelineConfig.Builder mConfigBuilder;
        public boolean mDownscaleFrameToDrawableDimensions;
        public boolean mGingerbreadDecoderEnabled;
        public Supplier<Boolean> mLazyDataSource;
        private ProducerFactoryMethod mProducerFactoryMethod;
        private WebpBitmapFactory mWebpBitmapFactory;
        private WebpBitmapFactory.WebpErrorLogger mWebpErrorLogger;
        private boolean mWebpSupportEnabled = false;
        private boolean mDecodeCancellationEnabled = false;
        private boolean mUseDownsamplingRatioForResizing = false;
        private boolean mUseBitmapPrepareToDraw = false;
        private int mBitmapPrepareToDrawMinSizeBytes = 0;
        private int mBitmapPrepareToDrawMaxSizeBytes = 0;
        public boolean mBitmapPrepareToDrawForPrefetch = false;
        private int mMaxBitmapSize = 2048;
        private boolean mNativeCodeDisabled = false;
        private boolean mPartialImageCachingEnabled = false;

        public Builder(ImagePipelineConfig.Builder configBuilder) {
            this.mConfigBuilder = configBuilder;
        }

        public ImagePipelineConfig.Builder setWebpSupportEnabled(boolean webpSupportEnabled) {
            this.mWebpSupportEnabled = webpSupportEnabled;
            return this.mConfigBuilder;
        }

        public ImagePipelineConfig.Builder setUseDownsampligRatioForResizing(boolean useDownsamplingRatioForResizing) {
            this.mUseDownsamplingRatioForResizing = useDownsamplingRatioForResizing;
            return this.mConfigBuilder;
        }

        public ImagePipelineConfig.Builder setPartialImageCachingEnabled(boolean partialImageCachingEnabled) {
            this.mPartialImageCachingEnabled = partialImageCachingEnabled;
            return this.mConfigBuilder;
        }

        public boolean isPartialImageCachingEnabled() {
            return this.mPartialImageCachingEnabled;
        }

        public ImagePipelineConfig.Builder setDecodeCancellationEnabled(boolean decodeCancellationEnabled) {
            this.mDecodeCancellationEnabled = decodeCancellationEnabled;
            return this.mConfigBuilder;
        }

        public ImagePipelineConfig.Builder setWebpErrorLogger(WebpBitmapFactory.WebpErrorLogger webpErrorLogger) {
            this.mWebpErrorLogger = webpErrorLogger;
            return this.mConfigBuilder;
        }

        public ImagePipelineConfig.Builder setWebpBitmapFactory(WebpBitmapFactory webpBitmapFactory) {
            this.mWebpBitmapFactory = webpBitmapFactory;
            return this.mConfigBuilder;
        }

        public ImagePipelineConfig.Builder setBitmapPrepareToDraw(boolean useBitmapPrepareToDraw, int minBitmapSizeBytes, int maxBitmapSizeBytes, boolean preparePrefetch) {
            this.mUseBitmapPrepareToDraw = useBitmapPrepareToDraw;
            this.mBitmapPrepareToDrawMinSizeBytes = minBitmapSizeBytes;
            this.mBitmapPrepareToDrawMaxSizeBytes = maxBitmapSizeBytes;
            this.mBitmapPrepareToDrawForPrefetch = preparePrefetch;
            return this.mConfigBuilder;
        }

        public ImagePipelineConfig.Builder setMaxBitmapSize(int maxBitmapSize) {
            this.mMaxBitmapSize = maxBitmapSize;
            return this.mConfigBuilder;
        }

        public ImagePipelineConfig.Builder setNativeCodeDisabled(boolean nativeCodeDisabled) {
            this.mNativeCodeDisabled = nativeCodeDisabled;
            return this.mConfigBuilder;
        }

        public ImagePipelineConfig.Builder setProducerFactoryMethod(ProducerFactoryMethod producerFactoryMethod) {
            this.mProducerFactoryMethod = producerFactoryMethod;
            return this.mConfigBuilder;
        }

        public ImagePipelineConfig.Builder setLazyDataSource(Supplier<Boolean> lazyDataSource) {
            this.mLazyDataSource = lazyDataSource;
            return this.mConfigBuilder;
        }

        public ImagePipelineConfig.Builder setGingerbreadDecoderEnabled(boolean gingerbreadDecoderEnabled) {
            this.mGingerbreadDecoderEnabled = gingerbreadDecoderEnabled;
            return this.mConfigBuilder;
        }

        public ImagePipelineConfig.Builder setShouldDownscaleFrameToDrawableDimensions(boolean downscaleFrameToDrawableDimensions) {
            this.mDownscaleFrameToDrawableDimensions = downscaleFrameToDrawableDimensions;
            return this.mConfigBuilder;
        }

        public ImagePipelineExperiments build() {
            return new ImagePipelineExperiments(this);
        }
    }

    /* loaded from: classes.dex */
    public static class DefaultProducerFactoryMethod implements ProducerFactoryMethod {
        @Override // com.facebook.imagepipeline.core.ImagePipelineExperiments.ProducerFactoryMethod
        public ProducerFactory createProducerFactory(Context context, ByteArrayPool byteArrayPool, ImageDecoder imageDecoder, ProgressiveJpegConfig progressiveJpegConfig, boolean downsampleEnabled, boolean resizeAndRotateEnabledForNetwork, boolean decodeCancellationEnabled, ExecutorSupplier executorSupplier, PooledByteBufferFactory pooledByteBufferFactory, MemoryCache<CacheKey, CloseableImage> bitmapMemoryCache, MemoryCache<CacheKey, PooledByteBuffer> encodedMemoryCache, BufferedDiskCache defaultBufferedDiskCache, BufferedDiskCache smallImageBufferedDiskCache, CacheKeyFactory cacheKeyFactory, PlatformBitmapFactory platformBitmapFactory, int bitmapPrepareToDrawMinSizeBytes, int bitmapPrepareToDrawMaxSizeBytes, boolean bitmapPrepareToDrawForPrefetch, int maxBitmapSize) {
            return new ProducerFactory(context, byteArrayPool, imageDecoder, progressiveJpegConfig, downsampleEnabled, resizeAndRotateEnabledForNetwork, decodeCancellationEnabled, executorSupplier, pooledByteBufferFactory, bitmapMemoryCache, encodedMemoryCache, defaultBufferedDiskCache, smallImageBufferedDiskCache, cacheKeyFactory, platformBitmapFactory, bitmapPrepareToDrawMinSizeBytes, bitmapPrepareToDrawMaxSizeBytes, bitmapPrepareToDrawForPrefetch, maxBitmapSize);
        }
    }
}
