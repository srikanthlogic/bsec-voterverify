package com.facebook.imagepipeline.producers;

import com.facebook.common.internal.Preconditions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.EncodedImage;
/* loaded from: classes.dex */
public class ThumbnailBranchProducer implements Producer<EncodedImage> {
    private final ThumbnailProducer<EncodedImage>[] mThumbnailProducers;

    public ThumbnailBranchProducer(ThumbnailProducer<EncodedImage>... thumbnailProducers) {
        this.mThumbnailProducers = (ThumbnailProducer[]) Preconditions.checkNotNull(thumbnailProducers);
        Preconditions.checkElementIndex(0, this.mThumbnailProducers.length);
    }

    @Override // com.facebook.imagepipeline.producers.Producer
    public void produceResults(Consumer<EncodedImage> consumer, ProducerContext context) {
        if (context.getImageRequest().getResizeOptions() == null) {
            consumer.onNewResult(null, 1);
        } else if (!produceResultsFromThumbnailProducer(0, consumer, context)) {
            consumer.onNewResult(null, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ThumbnailConsumer extends DelegatingConsumer<EncodedImage, EncodedImage> {
        private final ProducerContext mProducerContext;
        private final int mProducerIndex;
        private final ResizeOptions mResizeOptions;

        public ThumbnailConsumer(Consumer<EncodedImage> consumer, ProducerContext producerContext, int producerIndex) {
            super(consumer);
            this.mProducerContext = producerContext;
            this.mProducerIndex = producerIndex;
            this.mResizeOptions = this.mProducerContext.getImageRequest().getResizeOptions();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void onNewResultImpl(EncodedImage newResult, int status) {
            if (newResult != null && (isNotLast(status) || ThumbnailSizeChecker.isImageBigEnough(newResult, this.mResizeOptions))) {
                getConsumer().onNewResult(newResult, status);
            } else if (isLast(status)) {
                EncodedImage.closeSafely(newResult);
                if (!ThumbnailBranchProducer.this.produceResultsFromThumbnailProducer(this.mProducerIndex + 1, getConsumer(), this.mProducerContext)) {
                    getConsumer().onNewResult(null, 1);
                }
            }
        }

        @Override // com.facebook.imagepipeline.producers.DelegatingConsumer, com.facebook.imagepipeline.producers.BaseConsumer
        protected void onFailureImpl(Throwable t) {
            if (!ThumbnailBranchProducer.this.produceResultsFromThumbnailProducer(this.mProducerIndex + 1, getConsumer(), this.mProducerContext)) {
                getConsumer().onFailure(t);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean produceResultsFromThumbnailProducer(int startIndex, Consumer<EncodedImage> consumer, ProducerContext context) {
        int producerIndex = findFirstProducerForSize(startIndex, context.getImageRequest().getResizeOptions());
        if (producerIndex == -1) {
            return false;
        }
        this.mThumbnailProducers[producerIndex].produceResults(new ThumbnailConsumer(consumer, context, producerIndex), context);
        return true;
    }

    private int findFirstProducerForSize(int startIndex, ResizeOptions resizeOptions) {
        int i = startIndex;
        while (true) {
            ThumbnailProducer<EncodedImage>[] thumbnailProducerArr = this.mThumbnailProducers;
            if (i >= thumbnailProducerArr.length) {
                return -1;
            }
            if (thumbnailProducerArr[i].canProvideImageForSize(resizeOptions)) {
                return i;
            }
            i++;
        }
    }
}
