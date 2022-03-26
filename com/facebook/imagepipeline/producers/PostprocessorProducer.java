package com.facebook.imagepipeline.producers;

import android.graphics.Bitmap;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.request.Postprocessor;
import com.facebook.imagepipeline.request.RepeatedPostprocessor;
import com.facebook.imagepipeline.request.RepeatedPostprocessorRunner;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class PostprocessorProducer implements Producer<CloseableReference<CloseableImage>> {
    public static final String NAME = "PostprocessorProducer";
    static final String POSTPROCESSOR = "Postprocessor";
    private final PlatformBitmapFactory mBitmapFactory;
    private final Executor mExecutor;
    private final Producer<CloseableReference<CloseableImage>> mInputProducer;

    public PostprocessorProducer(Producer<CloseableReference<CloseableImage>> inputProducer, PlatformBitmapFactory platformBitmapFactory, Executor executor) {
        this.mInputProducer = (Producer) Preconditions.checkNotNull(inputProducer);
        this.mBitmapFactory = platformBitmapFactory;
        this.mExecutor = (Executor) Preconditions.checkNotNull(executor);
    }

    @Override // com.facebook.imagepipeline.producers.Producer
    public void produceResults(Consumer<CloseableReference<CloseableImage>> consumer, ProducerContext context) {
        Consumer<CloseableReference<CloseableImage>> postprocessorConsumer;
        ProducerListener listener = context.getListener();
        Postprocessor postprocessor = context.getImageRequest().getPostprocessor();
        PostprocessorConsumer basePostprocessorConsumer = new PostprocessorConsumer(consumer, listener, context.getId(), postprocessor, context);
        if (postprocessor instanceof RepeatedPostprocessor) {
            postprocessorConsumer = new RepeatedPostprocessorConsumer(basePostprocessorConsumer, (RepeatedPostprocessor) postprocessor, context);
        } else {
            postprocessorConsumer = new SingleUsePostprocessorConsumer(basePostprocessorConsumer);
        }
        this.mInputProducer.produceResults(postprocessorConsumer, context);
    }

    /* loaded from: classes.dex */
    private class PostprocessorConsumer extends DelegatingConsumer<CloseableReference<CloseableImage>, CloseableReference<CloseableImage>> {
        private boolean mIsClosed;
        private final ProducerListener mListener;
        private final Postprocessor mPostprocessor;
        private final String mRequestId;
        @Nullable
        private CloseableReference<CloseableImage> mSourceImageRef = null;
        private int mStatus = 0;
        private boolean mIsDirty = false;
        private boolean mIsPostProcessingRunning = false;

        public PostprocessorConsumer(Consumer<CloseableReference<CloseableImage>> consumer, ProducerListener listener, String requestId, Postprocessor postprocessor, ProducerContext producerContext) {
            super(consumer);
            this.mListener = listener;
            this.mRequestId = requestId;
            this.mPostprocessor = postprocessor;
            producerContext.addCallbacks(new BaseProducerContextCallbacks(PostprocessorProducer.this) { // from class: com.facebook.imagepipeline.producers.PostprocessorProducer.PostprocessorConsumer.1
                @Override // com.facebook.imagepipeline.producers.BaseProducerContextCallbacks, com.facebook.imagepipeline.producers.ProducerContextCallbacks
                public void onCancellationRequested() {
                    PostprocessorConsumer.this.maybeNotifyOnCancellation();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void onNewResultImpl(CloseableReference<CloseableImage> newResult, int status) {
            if (CloseableReference.isValid(newResult)) {
                updateSourceImageRef(newResult, status);
            } else if (isLast(status)) {
                maybeNotifyOnNewResult(null, status);
            }
        }

        @Override // com.facebook.imagepipeline.producers.DelegatingConsumer, com.facebook.imagepipeline.producers.BaseConsumer
        protected void onFailureImpl(Throwable t) {
            maybeNotifyOnFailure(t);
        }

        @Override // com.facebook.imagepipeline.producers.DelegatingConsumer, com.facebook.imagepipeline.producers.BaseConsumer
        protected void onCancellationImpl() {
            maybeNotifyOnCancellation();
        }

        private void updateSourceImageRef(@Nullable CloseableReference<CloseableImage> sourceImageRef, int status) {
            synchronized (this) {
                if (!this.mIsClosed) {
                    CloseableReference<CloseableImage> oldSourceImageRef = this.mSourceImageRef;
                    this.mSourceImageRef = CloseableReference.cloneOrNull(sourceImageRef);
                    this.mStatus = status;
                    this.mIsDirty = true;
                    boolean shouldSubmit = setRunningIfDirtyAndNotRunning();
                    CloseableReference.closeSafely(oldSourceImageRef);
                    if (shouldSubmit) {
                        submitPostprocessing();
                    }
                }
            }
        }

        private void submitPostprocessing() {
            PostprocessorProducer.this.mExecutor.execute(new Runnable() { // from class: com.facebook.imagepipeline.producers.PostprocessorProducer.PostprocessorConsumer.2
                @Override // java.lang.Runnable
                public void run() {
                    CloseableReference<CloseableImage> closeableImageRef;
                    int status;
                    synchronized (PostprocessorConsumer.this) {
                        closeableImageRef = PostprocessorConsumer.this.mSourceImageRef;
                        status = PostprocessorConsumer.this.mStatus;
                        PostprocessorConsumer.this.mSourceImageRef = null;
                        PostprocessorConsumer.this.mIsDirty = false;
                    }
                    if (CloseableReference.isValid(closeableImageRef)) {
                        try {
                            PostprocessorConsumer.this.doPostprocessing(closeableImageRef, status);
                        } finally {
                            CloseableReference.closeSafely(closeableImageRef);
                        }
                    }
                    PostprocessorConsumer.this.clearRunningAndStartIfDirty();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearRunningAndStartIfDirty() {
            boolean shouldExecuteAgain;
            synchronized (this) {
                this.mIsPostProcessingRunning = false;
                shouldExecuteAgain = setRunningIfDirtyAndNotRunning();
            }
            if (shouldExecuteAgain) {
                submitPostprocessing();
            }
        }

        private synchronized boolean setRunningIfDirtyAndNotRunning() {
            if (this.mIsClosed || !this.mIsDirty || this.mIsPostProcessingRunning || !CloseableReference.isValid(this.mSourceImageRef)) {
                return false;
            }
            this.mIsPostProcessingRunning = true;
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v3, types: [boolean] */
        /* JADX WARN: Type inference failed for: r0v6, types: [com.facebook.common.references.CloseableReference] */
        /* JADX WARN: Type inference failed for: r0v7 */
        public void doPostprocessing(CloseableReference<CloseableImage> sourceImageRef, int status) {
            Preconditions.checkArgument(CloseableReference.isValid(sourceImageRef));
            CloseableReference shouldPostprocess = shouldPostprocess(sourceImageRef.get());
            if (shouldPostprocess == 0) {
                maybeNotifyOnNewResult(sourceImageRef, status);
                return;
            }
            try {
                this.mListener.onProducerStart(this.mRequestId, PostprocessorProducer.NAME);
                shouldPostprocess = 0;
                shouldPostprocess = 0;
                CloseableReference<CloseableImage> destImageRef = postprocessInternal(sourceImageRef.get());
                this.mListener.onProducerFinishWithSuccess(this.mRequestId, PostprocessorProducer.NAME, getExtraMap(this.mListener, this.mRequestId, this.mPostprocessor));
                maybeNotifyOnNewResult(destImageRef, status);
            } catch (Exception e) {
                this.mListener.onProducerFinishWithFailure(this.mRequestId, PostprocessorProducer.NAME, e, getExtraMap(this.mListener, this.mRequestId, this.mPostprocessor));
                maybeNotifyOnFailure(e);
            } finally {
                CloseableReference.closeSafely(shouldPostprocess);
            }
        }

        @Nullable
        private Map<String, String> getExtraMap(ProducerListener listener, String requestId, Postprocessor postprocessor) {
            if (!listener.requiresExtraMap(requestId)) {
                return null;
            }
            return ImmutableMap.of(PostprocessorProducer.POSTPROCESSOR, postprocessor.getName());
        }

        private boolean shouldPostprocess(CloseableImage sourceImage) {
            return sourceImage instanceof CloseableStaticBitmap;
        }

        private CloseableReference<CloseableImage> postprocessInternal(CloseableImage sourceImage) {
            CloseableStaticBitmap staticBitmap = (CloseableStaticBitmap) sourceImage;
            CloseableReference<Bitmap> bitmapRef = this.mPostprocessor.process(staticBitmap.getUnderlyingBitmap(), PostprocessorProducer.this.mBitmapFactory);
            try {
                return CloseableReference.of(new CloseableStaticBitmap(bitmapRef, sourceImage.getQualityInfo(), staticBitmap.getRotationAngle(), staticBitmap.getExifOrientation()));
            } finally {
                CloseableReference.closeSafely(bitmapRef);
            }
        }

        private void maybeNotifyOnNewResult(CloseableReference<CloseableImage> newRef, int status) {
            boolean isLast = isLast(status);
            if ((!isLast && !isClosed()) || (isLast && close())) {
                getConsumer().onNewResult(newRef, status);
            }
        }

        private void maybeNotifyOnFailure(Throwable throwable) {
            if (close()) {
                getConsumer().onFailure(throwable);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void maybeNotifyOnCancellation() {
            if (close()) {
                getConsumer().onCancellation();
            }
        }

        private synchronized boolean isClosed() {
            return this.mIsClosed;
        }

        private boolean close() {
            synchronized (this) {
                if (this.mIsClosed) {
                    return false;
                }
                CloseableReference<CloseableImage> oldSourceImageRef = this.mSourceImageRef;
                this.mSourceImageRef = null;
                this.mIsClosed = true;
                CloseableReference.closeSafely(oldSourceImageRef);
                return true;
            }
        }
    }

    /* loaded from: classes.dex */
    class SingleUsePostprocessorConsumer extends DelegatingConsumer<CloseableReference<CloseableImage>, CloseableReference<CloseableImage>> {
        private SingleUsePostprocessorConsumer(PostprocessorConsumer postprocessorConsumer) {
            super(postprocessorConsumer);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void onNewResultImpl(CloseableReference<CloseableImage> newResult, int status) {
            if (!isNotLast(status)) {
                getConsumer().onNewResult(newResult, status);
            }
        }
    }

    /* loaded from: classes.dex */
    class RepeatedPostprocessorConsumer extends DelegatingConsumer<CloseableReference<CloseableImage>, CloseableReference<CloseableImage>> implements RepeatedPostprocessorRunner {
        private boolean mIsClosed;
        @Nullable
        private CloseableReference<CloseableImage> mSourceImageRef;

        private RepeatedPostprocessorConsumer(PostprocessorConsumer postprocessorConsumer, RepeatedPostprocessor repeatedPostprocessor, ProducerContext context) {
            super(postprocessorConsumer);
            this.mIsClosed = false;
            this.mSourceImageRef = null;
            repeatedPostprocessor.setCallback(this);
            context.addCallbacks(new BaseProducerContextCallbacks(PostprocessorProducer.this) { // from class: com.facebook.imagepipeline.producers.PostprocessorProducer.RepeatedPostprocessorConsumer.1
                @Override // com.facebook.imagepipeline.producers.BaseProducerContextCallbacks, com.facebook.imagepipeline.producers.ProducerContextCallbacks
                public void onCancellationRequested() {
                    if (RepeatedPostprocessorConsumer.this.close()) {
                        RepeatedPostprocessorConsumer.this.getConsumer().onCancellation();
                    }
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void onNewResultImpl(CloseableReference<CloseableImage> newResult, int status) {
            if (!isNotLast(status)) {
                setSourceImageRef(newResult);
                updateInternal();
            }
        }

        @Override // com.facebook.imagepipeline.producers.DelegatingConsumer, com.facebook.imagepipeline.producers.BaseConsumer
        protected void onFailureImpl(Throwable throwable) {
            if (close()) {
                getConsumer().onFailure(throwable);
            }
        }

        @Override // com.facebook.imagepipeline.producers.DelegatingConsumer, com.facebook.imagepipeline.producers.BaseConsumer
        protected void onCancellationImpl() {
            if (close()) {
                getConsumer().onCancellation();
            }
        }

        @Override // com.facebook.imagepipeline.request.RepeatedPostprocessorRunner
        public synchronized void update() {
            updateInternal();
        }

        private void updateInternal() {
            synchronized (this) {
                if (!this.mIsClosed) {
                    CloseableReference<CloseableImage> sourceImageRef = CloseableReference.cloneOrNull(this.mSourceImageRef);
                    try {
                        getConsumer().onNewResult(sourceImageRef, 0);
                    } finally {
                        CloseableReference.closeSafely(sourceImageRef);
                    }
                }
            }
        }

        private void setSourceImageRef(CloseableReference<CloseableImage> sourceImageRef) {
            synchronized (this) {
                if (!this.mIsClosed) {
                    CloseableReference<CloseableImage> oldSourceImageRef = this.mSourceImageRef;
                    this.mSourceImageRef = CloseableReference.cloneOrNull(sourceImageRef);
                    CloseableReference.closeSafely(oldSourceImageRef);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean close() {
            synchronized (this) {
                if (this.mIsClosed) {
                    return false;
                }
                CloseableReference<CloseableImage> oldSourceImageRef = this.mSourceImageRef;
                this.mSourceImageRef = null;
                this.mIsClosed = true;
                CloseableReference.closeSafely(oldSourceImageRef);
                return true;
            }
        }
    }
}
