package com.facebook.imagepipeline.producers;

import android.graphics.Bitmap;
import androidx.core.os.EnvironmentCompat;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.logging.FLog;
import com.facebook.common.memory.ByteArrayPool;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.ExceptionWithNoStacktrace;
import com.facebook.common.util.UriUtil;
import com.facebook.imageformat.DefaultImageFormats;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.decoder.DecodeException;
import com.facebook.imagepipeline.decoder.ImageDecoder;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegParser;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.producers.JobScheduler;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.systrace.FrescoSystrace;
import com.facebook.imagepipeline.transcoder.DownsampleUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class DecodeProducer implements Producer<CloseableReference<CloseableImage>> {
    public static final String ENCODED_IMAGE_SIZE;
    public static final String EXTRA_BITMAP_SIZE;
    public static final String EXTRA_HAS_GOOD_QUALITY;
    public static final String EXTRA_IMAGE_FORMAT_NAME;
    public static final String EXTRA_IS_FINAL;
    public static final String PRODUCER_NAME;
    public static final String REQUESTED_IMAGE_SIZE;
    public static final String SAMPLE_SIZE;
    private final ByteArrayPool mByteArrayPool;
    private final boolean mDecodeCancellationEnabled;
    private final boolean mDownsampleEnabled;
    private final boolean mDownsampleEnabledForNetwork;
    private final Executor mExecutor;
    private final ImageDecoder mImageDecoder;
    private final Producer<EncodedImage> mInputProducer;
    private final int mMaxBitmapSize;
    private final ProgressiveJpegConfig mProgressiveJpegConfig;

    public DecodeProducer(ByteArrayPool byteArrayPool, Executor executor, ImageDecoder imageDecoder, ProgressiveJpegConfig progressiveJpegConfig, boolean downsampleEnabled, boolean downsampleEnabledForNetwork, boolean decodeCancellationEnabled, Producer<EncodedImage> inputProducer, int maxBitmapSize) {
        this.mByteArrayPool = (ByteArrayPool) Preconditions.checkNotNull(byteArrayPool);
        this.mExecutor = (Executor) Preconditions.checkNotNull(executor);
        this.mImageDecoder = (ImageDecoder) Preconditions.checkNotNull(imageDecoder);
        this.mProgressiveJpegConfig = (ProgressiveJpegConfig) Preconditions.checkNotNull(progressiveJpegConfig);
        this.mDownsampleEnabled = downsampleEnabled;
        this.mDownsampleEnabledForNetwork = downsampleEnabledForNetwork;
        this.mInputProducer = (Producer) Preconditions.checkNotNull(inputProducer);
        this.mDecodeCancellationEnabled = decodeCancellationEnabled;
        this.mMaxBitmapSize = maxBitmapSize;
    }

    @Override // com.facebook.imagepipeline.producers.Producer
    public void produceResults(Consumer<CloseableReference<CloseableImage>> consumer, ProducerContext producerContext) {
        ProgressiveDecoder progressiveDecoder;
        try {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.beginSection("DecodeProducer#produceResults");
            }
            if (!UriUtil.isNetworkUri(producerContext.getImageRequest().getSourceUri())) {
                progressiveDecoder = new LocalImagesProgressiveDecoder(consumer, producerContext, this.mDecodeCancellationEnabled, this.mMaxBitmapSize);
            } else {
                progressiveDecoder = new NetworkImagesProgressiveDecoder(consumer, producerContext, new ProgressiveJpegParser(this.mByteArrayPool), this.mProgressiveJpegConfig, this.mDecodeCancellationEnabled, this.mMaxBitmapSize);
            }
            this.mInputProducer.produceResults(progressiveDecoder, producerContext);
        } finally {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.endSection();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public abstract class ProgressiveDecoder extends DelegatingConsumer<EncodedImage, CloseableReference<CloseableImage>> {
        private static final int DECODE_EXCEPTION_MESSAGE_NUM_HEADER_BYTES;
        private final ImageDecodeOptions mImageDecodeOptions;
        private final JobScheduler mJobScheduler;
        private final ProducerContext mProducerContext;
        private final ProducerListener mProducerListener;
        private final String TAG = "ProgressiveDecoder";
        private boolean mIsFinished = false;

        protected abstract int getIntermediateImageEndOffset(EncodedImage encodedImage);

        protected abstract QualityInfo getQualityInfo();

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ProgressiveDecoder(Consumer<CloseableReference<CloseableImage>> consumer, final ProducerContext producerContext, final boolean decodeCancellationEnabled, final int maxBitmapSize) {
            super(consumer);
            DecodeProducer.this = r5;
            this.mProducerContext = producerContext;
            this.mProducerListener = producerContext.getListener();
            this.mImageDecodeOptions = producerContext.getImageRequest().getImageDecodeOptions();
            this.mJobScheduler = new JobScheduler(r5.mExecutor, new JobScheduler.JobRunnable() { // from class: com.facebook.imagepipeline.producers.DecodeProducer.ProgressiveDecoder.1
                @Override // com.facebook.imagepipeline.producers.JobScheduler.JobRunnable
                public void run(EncodedImage encodedImage, int status) {
                    if (encodedImage != null) {
                        if (DecodeProducer.this.mDownsampleEnabled || !BaseConsumer.statusHasFlag(status, 16)) {
                            ImageRequest request = producerContext.getImageRequest();
                            if (DecodeProducer.this.mDownsampleEnabledForNetwork || !UriUtil.isNetworkUri(request.getSourceUri())) {
                                encodedImage.setSampleSize(DownsampleUtil.determineSampleSize(request.getRotationOptions(), request.getResizeOptions(), encodedImage, maxBitmapSize));
                            }
                        }
                        ProgressiveDecoder.this.doDecode(encodedImage, status);
                    }
                }
            }, this.mImageDecodeOptions.minDecodeIntervalMs);
            this.mProducerContext.addCallbacks(new BaseProducerContextCallbacks() { // from class: com.facebook.imagepipeline.producers.DecodeProducer.ProgressiveDecoder.2
                @Override // com.facebook.imagepipeline.producers.BaseProducerContextCallbacks, com.facebook.imagepipeline.producers.ProducerContextCallbacks
                public void onIsIntermediateResultExpectedChanged() {
                    if (ProgressiveDecoder.this.mProducerContext.isIntermediateResultExpected()) {
                        ProgressiveDecoder.this.mJobScheduler.scheduleJob();
                    }
                }

                @Override // com.facebook.imagepipeline.producers.BaseProducerContextCallbacks, com.facebook.imagepipeline.producers.ProducerContextCallbacks
                public void onCancellationRequested() {
                    if (decodeCancellationEnabled) {
                        ProgressiveDecoder.this.handleCancellation();
                    }
                }
            });
        }

        public void onNewResultImpl(EncodedImage newResult, int status) {
            try {
                if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.beginSection("DecodeProducer#onNewResultImpl");
                }
                boolean isLast = isLast(status);
                if (isLast && !EncodedImage.isValid(newResult)) {
                    handleError(new ExceptionWithNoStacktrace("Encoded image is not valid."));
                } else if (updateDecodeJob(newResult, status)) {
                    boolean isPlaceholder = statusHasFlag(status, 4);
                    if (isLast || isPlaceholder || this.mProducerContext.isIntermediateResultExpected()) {
                        this.mJobScheduler.scheduleJob();
                    }
                    if (FrescoSystrace.isTracing()) {
                        FrescoSystrace.endSection();
                    }
                } else if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.endSection();
                }
            } finally {
                if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.endSection();
                }
            }
        }

        @Override // com.facebook.imagepipeline.producers.DelegatingConsumer, com.facebook.imagepipeline.producers.BaseConsumer
        public void onProgressUpdateImpl(float progress) {
            super.onProgressUpdateImpl(0.99f * progress);
        }

        @Override // com.facebook.imagepipeline.producers.DelegatingConsumer, com.facebook.imagepipeline.producers.BaseConsumer
        public void onFailureImpl(Throwable t) {
            handleError(t);
        }

        @Override // com.facebook.imagepipeline.producers.DelegatingConsumer, com.facebook.imagepipeline.producers.BaseConsumer
        public void onCancellationImpl() {
            handleCancellation();
        }

        protected boolean updateDecodeJob(EncodedImage ref, int status) {
            return this.mJobScheduler.updateJob(ref, status);
        }

        public void doDecode(EncodedImage encodedImage, int status) {
            String imageFormatStr;
            String requestedSizeStr;
            DecodeException e;
            int length;
            QualityInfo quality;
            Exception e2;
            int status2;
            if ((encodedImage.getImageFormat() == DefaultImageFormats.JPEG || !isNotLast(status)) && !isFinished() && EncodedImage.isValid(encodedImage)) {
                ImageFormat imageFormat = encodedImage.getImageFormat();
                if (imageFormat != null) {
                    imageFormatStr = imageFormat.getName();
                } else {
                    imageFormatStr = EnvironmentCompat.MEDIA_UNKNOWN;
                }
                String encodedImageSize = encodedImage.getWidth() + "x" + encodedImage.getHeight();
                String sampleSize = String.valueOf(encodedImage.getSampleSize());
                boolean isLast = isLast(status);
                boolean isLastAndComplete = isLast && !statusHasFlag(status, 8);
                boolean isPlaceholder = statusHasFlag(status, 4);
                ResizeOptions resizeOptions = this.mProducerContext.getImageRequest().getResizeOptions();
                if (resizeOptions != null) {
                    requestedSizeStr = resizeOptions.width + "x" + resizeOptions.height;
                } else {
                    requestedSizeStr = EnvironmentCompat.MEDIA_UNKNOWN;
                }
                try {
                    long queueTime = this.mJobScheduler.getQueuedTime();
                    String requestUri = String.valueOf(this.mProducerContext.getImageRequest().getSourceUri());
                    if (isLastAndComplete || isPlaceholder) {
                        length = encodedImage.getSize();
                    } else {
                        try {
                            length = getIntermediateImageEndOffset(encodedImage);
                        } catch (Throwable th) {
                            e = th;
                            EncodedImage.closeSafely(encodedImage);
                            throw e;
                        }
                    }
                    if (isLastAndComplete || isPlaceholder) {
                        quality = ImmutableQualityInfo.FULL_QUALITY;
                    } else {
                        quality = getQualityInfo();
                    }
                    this.mProducerListener.onProducerStart(this.mProducerContext.getId(), DecodeProducer.PRODUCER_NAME);
                    CloseableImage image = null;
                    try {
                        try {
                            CloseableImage image2 = DecodeProducer.this.mImageDecoder.decode(encodedImage, length, quality, this.mImageDecodeOptions);
                            try {
                                if (encodedImage.getSampleSize() != 1) {
                                    status2 = status | 16;
                                } else {
                                    status2 = status;
                                }
                                try {
                                    this.mProducerListener.onProducerFinishWithSuccess(this.mProducerContext.getId(), DecodeProducer.PRODUCER_NAME, getExtraMap(image2, queueTime, quality, isLast, imageFormatStr, encodedImageSize, requestedSizeStr, sampleSize));
                                    handleResult(image2, status2);
                                    EncodedImage.closeSafely(encodedImage);
                                } catch (Throwable th2) {
                                    e = th2;
                                    EncodedImage.closeSafely(encodedImage);
                                    throw e;
                                }
                            } catch (Exception e3) {
                                e2 = e3;
                                image = image2;
                                try {
                                    this.mProducerListener.onProducerFinishWithFailure(this.mProducerContext.getId(), DecodeProducer.PRODUCER_NAME, e2, getExtraMap(image, queueTime, quality, isLast, imageFormatStr, encodedImageSize, requestedSizeStr, sampleSize));
                                    handleError(e2);
                                    EncodedImage.closeSafely(encodedImage);
                                } catch (Throwable th3) {
                                    e = th3;
                                    EncodedImage.closeSafely(encodedImage);
                                    throw e;
                                }
                            }
                        } catch (Throwable th4) {
                            e = th4;
                        }
                    } catch (DecodeException e4) {
                        try {
                            EncodedImage failedEncodedImage = e4.getEncodedImage();
                            FLog.w("ProgressiveDecoder", "%s, {uri: %s, firstEncodedBytes: %s, length: %d}", e4.getMessage(), requestUri, failedEncodedImage.getFirstBytesAsHexString(10), Integer.valueOf(failedEncodedImage.getSize()));
                            throw e4;
                        } catch (Exception e5) {
                            e2 = e5;
                            this.mProducerListener.onProducerFinishWithFailure(this.mProducerContext.getId(), DecodeProducer.PRODUCER_NAME, e2, getExtraMap(image, queueTime, quality, isLast, imageFormatStr, encodedImageSize, requestedSizeStr, sampleSize));
                            handleError(e2);
                            EncodedImage.closeSafely(encodedImage);
                        } catch (Throwable th5) {
                            e = th5;
                            EncodedImage.closeSafely(encodedImage);
                            throw e;
                        }
                    } catch (Exception e6) {
                        e2 = e6;
                    }
                } catch (Throwable th6) {
                    e = th6;
                }
            }
        }

        @Nullable
        private Map<String, String> getExtraMap(@Nullable CloseableImage image, long queueTime, QualityInfo quality, boolean isFinal, String imageFormatName, String encodedImageSize, String requestImageSize, String sampleSize) {
            if (!this.mProducerListener.requiresExtraMap(this.mProducerContext.getId())) {
                return null;
            }
            String queueStr = String.valueOf(queueTime);
            String qualityStr = String.valueOf(quality.isOfGoodEnoughQuality());
            String finalStr = String.valueOf(isFinal);
            if (image instanceof CloseableStaticBitmap) {
                Bitmap bitmap = ((CloseableStaticBitmap) image).getUnderlyingBitmap();
                Map<String, String> tmpMap = new HashMap<>(8);
                tmpMap.put(DecodeProducer.EXTRA_BITMAP_SIZE, bitmap.getWidth() + "x" + bitmap.getHeight());
                tmpMap.put("queueTime", queueStr);
                tmpMap.put(DecodeProducer.EXTRA_HAS_GOOD_QUALITY, qualityStr);
                tmpMap.put(DecodeProducer.EXTRA_IS_FINAL, finalStr);
                tmpMap.put("encodedImageSize", encodedImageSize);
                tmpMap.put(DecodeProducer.EXTRA_IMAGE_FORMAT_NAME, imageFormatName);
                tmpMap.put(DecodeProducer.REQUESTED_IMAGE_SIZE, requestImageSize);
                tmpMap.put(DecodeProducer.SAMPLE_SIZE, sampleSize);
                return ImmutableMap.copyOf(tmpMap);
            }
            Map<String, String> tmpMap2 = new HashMap<>(7);
            tmpMap2.put("queueTime", queueStr);
            tmpMap2.put(DecodeProducer.EXTRA_HAS_GOOD_QUALITY, qualityStr);
            tmpMap2.put(DecodeProducer.EXTRA_IS_FINAL, finalStr);
            tmpMap2.put("encodedImageSize", encodedImageSize);
            tmpMap2.put(DecodeProducer.EXTRA_IMAGE_FORMAT_NAME, imageFormatName);
            tmpMap2.put(DecodeProducer.REQUESTED_IMAGE_SIZE, requestImageSize);
            tmpMap2.put(DecodeProducer.SAMPLE_SIZE, sampleSize);
            return ImmutableMap.copyOf(tmpMap2);
        }

        private synchronized boolean isFinished() {
            return this.mIsFinished;
        }

        private void maybeFinish(boolean shouldFinish) {
            synchronized (this) {
                if (shouldFinish) {
                    if (!this.mIsFinished) {
                        getConsumer().onProgressUpdate(1.0f);
                        this.mIsFinished = true;
                        this.mJobScheduler.clearJob();
                    }
                }
            }
        }

        private void handleResult(CloseableImage decodedImage, int status) {
            CloseableReference<CloseableImage> decodedImageRef = CloseableReference.of(decodedImage);
            try {
                maybeFinish(isLast(status));
                getConsumer().onNewResult(decodedImageRef, status);
            } finally {
                CloseableReference.closeSafely(decodedImageRef);
            }
        }

        private void handleError(Throwable t) {
            maybeFinish(true);
            getConsumer().onFailure(t);
        }

        public void handleCancellation() {
            maybeFinish(true);
            getConsumer().onCancellation();
        }
    }

    /* loaded from: classes.dex */
    private class LocalImagesProgressiveDecoder extends ProgressiveDecoder {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public LocalImagesProgressiveDecoder(Consumer<CloseableReference<CloseableImage>> consumer, ProducerContext producerContext, boolean decodeCancellationEnabled, int maxBitmapSize) {
            super(consumer, producerContext, decodeCancellationEnabled, maxBitmapSize);
            DecodeProducer.this = r1;
        }

        @Override // com.facebook.imagepipeline.producers.DecodeProducer.ProgressiveDecoder
        protected synchronized boolean updateDecodeJob(EncodedImage encodedImage, int status) {
            if (isNotLast(status)) {
                return false;
            }
            return super.updateDecodeJob(encodedImage, status);
        }

        @Override // com.facebook.imagepipeline.producers.DecodeProducer.ProgressiveDecoder
        protected int getIntermediateImageEndOffset(EncodedImage encodedImage) {
            return encodedImage.getSize();
        }

        @Override // com.facebook.imagepipeline.producers.DecodeProducer.ProgressiveDecoder
        protected QualityInfo getQualityInfo() {
            return ImmutableQualityInfo.of(0, false, false);
        }
    }

    /* loaded from: classes.dex */
    private class NetworkImagesProgressiveDecoder extends ProgressiveDecoder {
        private int mLastScheduledScanNumber = 0;
        private final ProgressiveJpegConfig mProgressiveJpegConfig;
        private final ProgressiveJpegParser mProgressiveJpegParser;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public NetworkImagesProgressiveDecoder(Consumer<CloseableReference<CloseableImage>> consumer, ProducerContext producerContext, ProgressiveJpegParser progressiveJpegParser, ProgressiveJpegConfig progressiveJpegConfig, boolean decodeCancellationEnabled, int maxBitmapSize) {
            super(consumer, producerContext, decodeCancellationEnabled, maxBitmapSize);
            DecodeProducer.this = r7;
            this.mProgressiveJpegParser = (ProgressiveJpegParser) Preconditions.checkNotNull(progressiveJpegParser);
            this.mProgressiveJpegConfig = (ProgressiveJpegConfig) Preconditions.checkNotNull(progressiveJpegConfig);
        }

        @Override // com.facebook.imagepipeline.producers.DecodeProducer.ProgressiveDecoder
        protected synchronized boolean updateDecodeJob(EncodedImage encodedImage, int status) {
            boolean ret = super.updateDecodeJob(encodedImage, status);
            if ((isNotLast(status) || statusHasFlag(status, 8)) && !statusHasFlag(status, 4) && EncodedImage.isValid(encodedImage) && encodedImage.getImageFormat() == DefaultImageFormats.JPEG) {
                if (!this.mProgressiveJpegParser.parseMoreData(encodedImage)) {
                    return false;
                }
                int scanNum = this.mProgressiveJpegParser.getBestScanNumber();
                if (scanNum <= this.mLastScheduledScanNumber) {
                    return false;
                }
                if (scanNum < this.mProgressiveJpegConfig.getNextScanNumberToDecode(this.mLastScheduledScanNumber) && !this.mProgressiveJpegParser.isEndMarkerRead()) {
                    return false;
                }
                this.mLastScheduledScanNumber = scanNum;
            }
            return ret;
        }

        @Override // com.facebook.imagepipeline.producers.DecodeProducer.ProgressiveDecoder
        protected int getIntermediateImageEndOffset(EncodedImage encodedImage) {
            return this.mProgressiveJpegParser.getBestScanEndOffset();
        }

        @Override // com.facebook.imagepipeline.producers.DecodeProducer.ProgressiveDecoder
        protected QualityInfo getQualityInfo() {
            return this.mProgressiveJpegConfig.getQualityInfo(this.mProgressiveJpegParser.getBestScanNumber());
        }
    }
}
