package com.facebook.imagepipeline.producers;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.memory.PooledByteBufferFactory;
import com.facebook.common.memory.PooledByteBufferOutputStream;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.TriState;
import com.facebook.imageformat.DefaultImageFormats;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imageformat.ImageFormatChecker;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.nativecode.WebpTranscoder;
import com.facebook.imagepipeline.nativecode.WebpTranscoderFactory;
import java.io.InputStream;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class WebpTranscodeProducer implements Producer<EncodedImage> {
    private static final int DEFAULT_JPEG_QUALITY = 80;
    public static final String PRODUCER_NAME = "WebpTranscodeProducer";
    private final Executor mExecutor;
    private final Producer<EncodedImage> mInputProducer;
    private final PooledByteBufferFactory mPooledByteBufferFactory;

    public WebpTranscodeProducer(Executor executor, PooledByteBufferFactory pooledByteBufferFactory, Producer<EncodedImage> inputProducer) {
        this.mExecutor = (Executor) Preconditions.checkNotNull(executor);
        this.mPooledByteBufferFactory = (PooledByteBufferFactory) Preconditions.checkNotNull(pooledByteBufferFactory);
        this.mInputProducer = (Producer) Preconditions.checkNotNull(inputProducer);
    }

    @Override // com.facebook.imagepipeline.producers.Producer
    public void produceResults(Consumer<EncodedImage> consumer, ProducerContext context) {
        this.mInputProducer.produceResults(new WebpTranscodeConsumer(consumer, context), context);
    }

    /* loaded from: classes.dex */
    private class WebpTranscodeConsumer extends DelegatingConsumer<EncodedImage, EncodedImage> {
        private final ProducerContext mContext;
        private TriState mShouldTranscodeWhenFinished = TriState.UNSET;

        public WebpTranscodeConsumer(Consumer<EncodedImage> consumer, ProducerContext context) {
            super(consumer);
            this.mContext = context;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void onNewResultImpl(@Nullable EncodedImage newResult, int status) {
            if (this.mShouldTranscodeWhenFinished == TriState.UNSET && newResult != null) {
                this.mShouldTranscodeWhenFinished = WebpTranscodeProducer.shouldTranscode(newResult);
            }
            if (this.mShouldTranscodeWhenFinished == TriState.NO) {
                getConsumer().onNewResult(newResult, status);
            } else if (!isLast(status)) {
            } else {
                if (this.mShouldTranscodeWhenFinished != TriState.YES || newResult == null) {
                    getConsumer().onNewResult(newResult, status);
                } else {
                    WebpTranscodeProducer.this.transcodeLastResult(newResult, getConsumer(), this.mContext);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void transcodeLastResult(EncodedImage originalResult, Consumer<EncodedImage> consumer, ProducerContext producerContext) {
        Preconditions.checkNotNull(originalResult);
        final EncodedImage encodedImageCopy = EncodedImage.cloneOrNull(originalResult);
        this.mExecutor.execute(new StatefulProducerRunnable<EncodedImage>(consumer, producerContext.getListener(), PRODUCER_NAME, producerContext.getId()) { // from class: com.facebook.imagepipeline.producers.WebpTranscodeProducer.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.facebook.common.executors.StatefulRunnable
            public EncodedImage getResult() throws Exception {
                PooledByteBufferOutputStream outputStream = WebpTranscodeProducer.this.mPooledByteBufferFactory.newOutputStream();
                try {
                    WebpTranscodeProducer.doTranscode(encodedImageCopy, outputStream);
                    CloseableReference<PooledByteBuffer> ref = CloseableReference.of(outputStream.toByteBuffer());
                    EncodedImage encodedImage = new EncodedImage(ref);
                    encodedImage.copyMetaDataFrom(encodedImageCopy);
                    CloseableReference.closeSafely(ref);
                    return encodedImage;
                } finally {
                    outputStream.close();
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            public void disposeResult(EncodedImage result) {
                EncodedImage.closeSafely(result);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            public void onSuccess(EncodedImage result) {
                EncodedImage.closeSafely(encodedImageCopy);
                super.onSuccess((AnonymousClass1) result);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.facebook.imagepipeline.producers.StatefulProducerRunnable, com.facebook.common.executors.StatefulRunnable
            public void onFailure(Exception e) {
                EncodedImage.closeSafely(encodedImageCopy);
                super.onFailure(e);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.facebook.imagepipeline.producers.StatefulProducerRunnable, com.facebook.common.executors.StatefulRunnable
            public void onCancellation() {
                EncodedImage.closeSafely(encodedImageCopy);
                super.onCancellation();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static TriState shouldTranscode(EncodedImage encodedImage) {
        Preconditions.checkNotNull(encodedImage);
        ImageFormat imageFormat = ImageFormatChecker.getImageFormat_WrapIOException(encodedImage.getInputStream());
        if (DefaultImageFormats.isStaticWebpFormat(imageFormat)) {
            WebpTranscoder webpTranscoder = WebpTranscoderFactory.getWebpTranscoder();
            if (webpTranscoder == null) {
                return TriState.NO;
            }
            return TriState.valueOf(!webpTranscoder.isWebpNativelySupported(imageFormat));
        } else if (imageFormat == ImageFormat.UNKNOWN) {
            return TriState.UNSET;
        } else {
            return TriState.NO;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void doTranscode(EncodedImage encodedImage, PooledByteBufferOutputStream outputStream) throws Exception {
        InputStream imageInputStream = encodedImage.getInputStream();
        ImageFormat imageFormat = ImageFormatChecker.getImageFormat_WrapIOException(imageInputStream);
        if (imageFormat == DefaultImageFormats.WEBP_SIMPLE || imageFormat == DefaultImageFormats.WEBP_EXTENDED) {
            WebpTranscoderFactory.getWebpTranscoder().transcodeWebpToJpeg(imageInputStream, outputStream, 80);
            encodedImage.setImageFormat(DefaultImageFormats.JPEG);
        } else if (imageFormat == DefaultImageFormats.WEBP_LOSSLESS || imageFormat == DefaultImageFormats.WEBP_EXTENDED_WITH_ALPHA) {
            WebpTranscoderFactory.getWebpTranscoder().transcodeWebpToPng(imageInputStream, outputStream);
            encodedImage.setImageFormat(DefaultImageFormats.PNG);
        } else {
            throw new IllegalArgumentException("Wrong image format");
        }
    }
}
