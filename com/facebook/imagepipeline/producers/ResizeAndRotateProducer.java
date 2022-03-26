package com.facebook.imagepipeline.producers;

import com.facebook.common.internal.ImmutableMap;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.memory.PooledByteBufferFactory;
import com.facebook.common.memory.PooledByteBufferOutputStream;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.TriState;
import com.facebook.imageformat.DefaultImageFormats;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.producers.JobScheduler;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.transcoder.ImageTranscodeResult;
import com.facebook.imagepipeline.transcoder.ImageTranscoder;
import com.facebook.imagepipeline.transcoder.ImageTranscoderFactory;
import com.facebook.imagepipeline.transcoder.JpegTranscoderUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class ResizeAndRotateProducer implements Producer<EncodedImage> {
    private static final String INPUT_IMAGE_FORMAT = "Image format";
    static final int MIN_TRANSFORM_INTERVAL_MS = 100;
    private static final String ORIGINAL_SIZE_KEY = "Original size";
    private static final String PRODUCER_NAME = "ResizeAndRotateProducer";
    private static final String REQUESTED_SIZE_KEY = "Requested size";
    private static final String TRANSCODER_ID = "Transcoder id";
    private static final String TRANSCODING_RESULT = "Transcoding result";
    private final Executor mExecutor;
    private final ImageTranscoderFactory mImageTranscoderFactory;
    private final Producer<EncodedImage> mInputProducer;
    private final boolean mIsResizingEnabled;
    private final PooledByteBufferFactory mPooledByteBufferFactory;

    public ResizeAndRotateProducer(Executor executor, PooledByteBufferFactory pooledByteBufferFactory, Producer<EncodedImage> inputProducer, boolean isResizingEnabled, ImageTranscoderFactory imageTranscoderFactory) {
        this.mExecutor = (Executor) Preconditions.checkNotNull(executor);
        this.mPooledByteBufferFactory = (PooledByteBufferFactory) Preconditions.checkNotNull(pooledByteBufferFactory);
        this.mInputProducer = (Producer) Preconditions.checkNotNull(inputProducer);
        this.mImageTranscoderFactory = (ImageTranscoderFactory) Preconditions.checkNotNull(imageTranscoderFactory);
        this.mIsResizingEnabled = isResizingEnabled;
    }

    @Override // com.facebook.imagepipeline.producers.Producer
    public void produceResults(Consumer<EncodedImage> consumer, ProducerContext context) {
        this.mInputProducer.produceResults(new TransformingConsumer(consumer, context, this.mIsResizingEnabled, this.mImageTranscoderFactory), context);
    }

    /* loaded from: classes.dex */
    private class TransformingConsumer extends DelegatingConsumer<EncodedImage, EncodedImage> {
        private final ImageTranscoderFactory mImageTranscoderFactory;
        private boolean mIsCancelled = false;
        private final boolean mIsResizingEnabled;
        private final JobScheduler mJobScheduler;
        private final ProducerContext mProducerContext;

        TransformingConsumer(final Consumer<EncodedImage> consumer, ProducerContext producerContext, boolean isResizingEnabled, ImageTranscoderFactory imageTranscoderFactory) {
            super(consumer);
            this.mProducerContext = producerContext;
            Boolean resizingAllowedOverride = this.mProducerContext.getImageRequest().getResizingAllowedOverride();
            this.mIsResizingEnabled = resizingAllowedOverride != null ? resizingAllowedOverride.booleanValue() : isResizingEnabled;
            this.mImageTranscoderFactory = imageTranscoderFactory;
            this.mJobScheduler = new JobScheduler(ResizeAndRotateProducer.this.mExecutor, new JobScheduler.JobRunnable(ResizeAndRotateProducer.this) { // from class: com.facebook.imagepipeline.producers.ResizeAndRotateProducer.TransformingConsumer.1
                @Override // com.facebook.imagepipeline.producers.JobScheduler.JobRunnable
                public void run(EncodedImage encodedImage, int status) {
                    TransformingConsumer transformingConsumer = TransformingConsumer.this;
                    transformingConsumer.doTransform(encodedImage, status, (ImageTranscoder) Preconditions.checkNotNull(transformingConsumer.mImageTranscoderFactory.createImageTranscoder(encodedImage.getImageFormat(), TransformingConsumer.this.mIsResizingEnabled)));
                }
            }, 100);
            this.mProducerContext.addCallbacks(new BaseProducerContextCallbacks(ResizeAndRotateProducer.this) { // from class: com.facebook.imagepipeline.producers.ResizeAndRotateProducer.TransformingConsumer.2
                @Override // com.facebook.imagepipeline.producers.BaseProducerContextCallbacks, com.facebook.imagepipeline.producers.ProducerContextCallbacks
                public void onIsIntermediateResultExpectedChanged() {
                    if (TransformingConsumer.this.mProducerContext.isIntermediateResultExpected()) {
                        TransformingConsumer.this.mJobScheduler.scheduleJob();
                    }
                }

                @Override // com.facebook.imagepipeline.producers.BaseProducerContextCallbacks, com.facebook.imagepipeline.producers.ProducerContextCallbacks
                public void onCancellationRequested() {
                    TransformingConsumer.this.mJobScheduler.clearJob();
                    TransformingConsumer.this.mIsCancelled = true;
                    consumer.onCancellation();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void onNewResultImpl(@Nullable EncodedImage newResult, int status) {
            if (!this.mIsCancelled) {
                boolean isLast = isLast(status);
                if (newResult != null) {
                    ImageFormat imageFormat = newResult.getImageFormat();
                    TriState shouldTransform = ResizeAndRotateProducer.shouldTransform(this.mProducerContext.getImageRequest(), newResult, (ImageTranscoder) Preconditions.checkNotNull(this.mImageTranscoderFactory.createImageTranscoder(imageFormat, this.mIsResizingEnabled)));
                    if (!isLast && shouldTransform == TriState.UNSET) {
                        return;
                    }
                    if (shouldTransform != TriState.YES) {
                        forwardNewResult(newResult, status, imageFormat);
                    } else if (this.mJobScheduler.updateJob(newResult, status)) {
                        if (isLast || this.mProducerContext.isIntermediateResultExpected()) {
                            this.mJobScheduler.scheduleJob();
                        }
                    }
                } else if (isLast) {
                    getConsumer().onNewResult(null, 1);
                }
            }
        }

        private void forwardNewResult(EncodedImage newResult, int status, ImageFormat imageFormat) {
            EncodedImage newResult2;
            if (imageFormat == DefaultImageFormats.JPEG || imageFormat == DefaultImageFormats.HEIF) {
                newResult2 = getNewResultsForJpegOrHeif(newResult);
            } else {
                newResult2 = getNewResultForImagesWithoutExifData(newResult);
            }
            getConsumer().onNewResult(newResult2, status);
        }

        @Nullable
        private EncodedImage getNewResultForImagesWithoutExifData(EncodedImage encodedImage) {
            RotationOptions options = this.mProducerContext.getImageRequest().getRotationOptions();
            if (options.useImageMetadata() || !options.rotationEnabled()) {
                return encodedImage;
            }
            return getCloneWithRotationApplied(encodedImage, options.getForcedAngle());
        }

        @Nullable
        private EncodedImage getNewResultsForJpegOrHeif(EncodedImage encodedImage) {
            if (this.mProducerContext.getImageRequest().getRotationOptions().canDeferUntilRendered() || encodedImage.getRotationAngle() == 0 || encodedImage.getRotationAngle() == -1) {
                return encodedImage;
            }
            return getCloneWithRotationApplied(encodedImage, 0);
        }

        @Nullable
        private EncodedImage getCloneWithRotationApplied(EncodedImage encodedImage, int angle) {
            EncodedImage newResult = EncodedImage.cloneOrNull(encodedImage);
            encodedImage.close();
            if (newResult != null) {
                newResult.setRotationAngle(angle);
            }
            return newResult;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void doTransform(EncodedImage encodedImage, int status, ImageTranscoder imageTranscoder) {
            PooledByteBufferOutputStream outputStream;
            try {
                this.mProducerContext.getListener().onProducerStart(this.mProducerContext.getId(), ResizeAndRotateProducer.PRODUCER_NAME);
                ImageRequest imageRequest = this.mProducerContext.getImageRequest();
                outputStream = ResizeAndRotateProducer.this.mPooledByteBufferFactory.newOutputStream();
                ImageTranscodeResult result = imageTranscoder.transcode(encodedImage, outputStream, imageRequest.getRotationOptions(), imageRequest.getResizeOptions(), null, 85);
                if (result.getTranscodeStatus() != 2) {
                    Map<String, String> extraMap = getExtraMap(encodedImage, imageRequest.getResizeOptions(), result, imageTranscoder.getIdentifier());
                    CloseableReference<PooledByteBuffer> ref = CloseableReference.of(outputStream.toByteBuffer());
                    try {
                        EncodedImage ret = new EncodedImage(ref);
                        ret.setImageFormat(DefaultImageFormats.JPEG);
                        ret.parseMetaData();
                        this.mProducerContext.getListener().onProducerFinishWithSuccess(this.mProducerContext.getId(), ResizeAndRotateProducer.PRODUCER_NAME, extraMap);
                        if (result.getTranscodeStatus() != 1) {
                            status |= 16;
                        }
                        getConsumer().onNewResult(ret, status);
                        EncodedImage.closeSafely(ret);
                    } finally {
                        CloseableReference.closeSafely(ref);
                    }
                } else {
                    throw new RuntimeException("Error while transcoding the image");
                }
            } catch (Exception e) {
                this.mProducerContext.getListener().onProducerFinishWithFailure(this.mProducerContext.getId(), ResizeAndRotateProducer.PRODUCER_NAME, e, null);
                if (isLast(status)) {
                    getConsumer().onFailure(e);
                }
            } finally {
                outputStream.close();
            }
        }

        @Nullable
        private Map<String, String> getExtraMap(EncodedImage encodedImage, @Nullable ResizeOptions resizeOptions, @Nullable ImageTranscodeResult transcodeResult, @Nullable String transcoderId) {
            String requestedSize;
            if (!this.mProducerContext.getListener().requiresExtraMap(this.mProducerContext.getId())) {
                return null;
            }
            String originalSize = encodedImage.getWidth() + "x" + encodedImage.getHeight();
            if (resizeOptions != null) {
                requestedSize = resizeOptions.width + "x" + resizeOptions.height;
            } else {
                requestedSize = "Unspecified";
            }
            Map<String, String> map = new HashMap<>();
            map.put(ResizeAndRotateProducer.INPUT_IMAGE_FORMAT, String.valueOf(encodedImage.getImageFormat()));
            map.put(ResizeAndRotateProducer.ORIGINAL_SIZE_KEY, originalSize);
            map.put(ResizeAndRotateProducer.REQUESTED_SIZE_KEY, requestedSize);
            map.put("queueTime", String.valueOf(this.mJobScheduler.getQueuedTime()));
            map.put(ResizeAndRotateProducer.TRANSCODER_ID, transcoderId);
            map.put(ResizeAndRotateProducer.TRANSCODING_RESULT, String.valueOf(transcodeResult));
            return ImmutableMap.copyOf(map);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static TriState shouldTransform(ImageRequest request, EncodedImage encodedImage, ImageTranscoder imageTranscoder) {
        if (encodedImage == null || encodedImage.getImageFormat() == ImageFormat.UNKNOWN) {
            return TriState.UNSET;
        }
        if (!imageTranscoder.canTranscode(encodedImage.getImageFormat())) {
            return TriState.NO;
        }
        return TriState.valueOf(shouldRotate(request.getRotationOptions(), encodedImage) || imageTranscoder.canResize(encodedImage, request.getRotationOptions(), request.getResizeOptions()));
    }

    private static boolean shouldRotate(RotationOptions rotationOptions, EncodedImage encodedImage) {
        return !rotationOptions.canDeferUntilRendered() && (JpegTranscoderUtils.getRotationAngle(rotationOptions, encodedImage) != 0 || shouldRotateUsingExifOrientation(rotationOptions, encodedImage));
    }

    private static boolean shouldRotateUsingExifOrientation(RotationOptions rotationOptions, EncodedImage encodedImage) {
        if (rotationOptions.rotationEnabled() && !rotationOptions.canDeferUntilRendered()) {
            return JpegTranscoderUtils.INVERTED_EXIF_ORIENTATIONS.contains(Integer.valueOf(encodedImage.getExifOrientation()));
        }
        encodedImage.setExifOrientation(0);
        return false;
    }
}
