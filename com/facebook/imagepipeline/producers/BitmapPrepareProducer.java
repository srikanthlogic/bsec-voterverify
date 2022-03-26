package com.facebook.imagepipeline.producers;

import android.graphics.Bitmap;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
/* loaded from: classes.dex */
public class BitmapPrepareProducer implements Producer<CloseableReference<CloseableImage>> {
    public static final String PRODUCER_NAME = "BitmapPrepareProducer";
    private final Producer<CloseableReference<CloseableImage>> mInputProducer;
    private final int mMaxBitmapSizeBytes;
    private final int mMinBitmapSizeBytes;
    private final boolean mPreparePrefetch;

    public BitmapPrepareProducer(Producer<CloseableReference<CloseableImage>> inputProducer, int minBitmapSizeBytes, int maxBitmapSizeBytes, boolean preparePrefetch) {
        Preconditions.checkArgument(minBitmapSizeBytes <= maxBitmapSizeBytes);
        this.mInputProducer = (Producer) Preconditions.checkNotNull(inputProducer);
        this.mMinBitmapSizeBytes = minBitmapSizeBytes;
        this.mMaxBitmapSizeBytes = maxBitmapSizeBytes;
        this.mPreparePrefetch = preparePrefetch;
    }

    @Override // com.facebook.imagepipeline.producers.Producer
    public void produceResults(Consumer<CloseableReference<CloseableImage>> consumer, ProducerContext producerContext) {
        if (!producerContext.isPrefetch() || this.mPreparePrefetch) {
            this.mInputProducer.produceResults(new BitmapPrepareConsumer(consumer, this.mMinBitmapSizeBytes, this.mMaxBitmapSizeBytes), producerContext);
        } else {
            this.mInputProducer.produceResults(consumer, producerContext);
        }
    }

    /* loaded from: classes.dex */
    private static class BitmapPrepareConsumer extends DelegatingConsumer<CloseableReference<CloseableImage>, CloseableReference<CloseableImage>> {
        private final int mMaxBitmapSizeBytes;
        private final int mMinBitmapSizeBytes;

        BitmapPrepareConsumer(Consumer<CloseableReference<CloseableImage>> consumer, int minBitmapSizeBytes, int maxBitmapSizeBytes) {
            super(consumer);
            this.mMinBitmapSizeBytes = minBitmapSizeBytes;
            this.mMaxBitmapSizeBytes = maxBitmapSizeBytes;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void onNewResultImpl(CloseableReference<CloseableImage> newResult, int status) {
            internalPrepareBitmap(newResult);
            getConsumer().onNewResult(newResult, status);
        }

        private void internalPrepareBitmap(CloseableReference<CloseableImage> newResult) {
            CloseableImage closeableImage;
            Bitmap bitmap;
            int bitmapByteCount;
            if (newResult != null && newResult.isValid() && (closeableImage = newResult.get()) != null && !closeableImage.isClosed() && (closeableImage instanceof CloseableStaticBitmap) && (bitmap = ((CloseableStaticBitmap) closeableImage).getUnderlyingBitmap()) != null && (bitmapByteCount = bitmap.getRowBytes() * bitmap.getHeight()) >= this.mMinBitmapSizeBytes && bitmapByteCount <= this.mMaxBitmapSizeBytes) {
                bitmap.prepareToDraw();
            }
        }
    }
}
