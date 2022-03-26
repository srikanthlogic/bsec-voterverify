package com.facebook.imagepipeline.producers;

import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.request.ImageRequest;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class BaseProducerContext implements ProducerContext {
    private final Object mCallerContext;
    private final String mId;
    private final ImageRequest mImageRequest;
    private boolean mIsIntermediateResultExpected;
    private boolean mIsPrefetch;
    private final ImageRequest.RequestLevel mLowestPermittedRequestLevel;
    private Priority mPriority;
    private final ProducerListener mProducerListener;
    private boolean mIsCancelled = false;
    private final List<ProducerContextCallbacks> mCallbacks = new ArrayList();

    public BaseProducerContext(ImageRequest imageRequest, String id, ProducerListener producerListener, Object callerContext, ImageRequest.RequestLevel lowestPermittedRequestLevel, boolean isPrefetch, boolean isIntermediateResultExpected, Priority priority) {
        this.mImageRequest = imageRequest;
        this.mId = id;
        this.mProducerListener = producerListener;
        this.mCallerContext = callerContext;
        this.mLowestPermittedRequestLevel = lowestPermittedRequestLevel;
        this.mIsPrefetch = isPrefetch;
        this.mPriority = priority;
        this.mIsIntermediateResultExpected = isIntermediateResultExpected;
    }

    @Override // com.facebook.imagepipeline.producers.ProducerContext
    public ImageRequest getImageRequest() {
        return this.mImageRequest;
    }

    @Override // com.facebook.imagepipeline.producers.ProducerContext
    public String getId() {
        return this.mId;
    }

    @Override // com.facebook.imagepipeline.producers.ProducerContext
    public ProducerListener getListener() {
        return this.mProducerListener;
    }

    @Override // com.facebook.imagepipeline.producers.ProducerContext
    public Object getCallerContext() {
        return this.mCallerContext;
    }

    @Override // com.facebook.imagepipeline.producers.ProducerContext
    public ImageRequest.RequestLevel getLowestPermittedRequestLevel() {
        return this.mLowestPermittedRequestLevel;
    }

    @Override // com.facebook.imagepipeline.producers.ProducerContext
    public synchronized boolean isPrefetch() {
        return this.mIsPrefetch;
    }

    @Override // com.facebook.imagepipeline.producers.ProducerContext
    public synchronized Priority getPriority() {
        return this.mPriority;
    }

    @Override // com.facebook.imagepipeline.producers.ProducerContext
    public synchronized boolean isIntermediateResultExpected() {
        return this.mIsIntermediateResultExpected;
    }

    public synchronized boolean isCancelled() {
        return this.mIsCancelled;
    }

    @Override // com.facebook.imagepipeline.producers.ProducerContext
    public void addCallbacks(ProducerContextCallbacks callbacks) {
        boolean cancelImmediately = false;
        synchronized (this) {
            this.mCallbacks.add(callbacks);
            if (this.mIsCancelled) {
                cancelImmediately = true;
            }
        }
        if (cancelImmediately) {
            callbacks.onCancellationRequested();
        }
    }

    public void cancel() {
        callOnCancellationRequested(cancelNoCallbacks());
    }

    @Nullable
    public synchronized List<ProducerContextCallbacks> setIsPrefetchNoCallbacks(boolean isPrefetch) {
        if (isPrefetch == this.mIsPrefetch) {
            return null;
        }
        this.mIsPrefetch = isPrefetch;
        return new ArrayList(this.mCallbacks);
    }

    @Nullable
    public synchronized List<ProducerContextCallbacks> setPriorityNoCallbacks(Priority priority) {
        if (priority == this.mPriority) {
            return null;
        }
        this.mPriority = priority;
        return new ArrayList(this.mCallbacks);
    }

    @Nullable
    public synchronized List<ProducerContextCallbacks> setIsIntermediateResultExpectedNoCallbacks(boolean isIntermediateResultExpected) {
        if (isIntermediateResultExpected == this.mIsIntermediateResultExpected) {
            return null;
        }
        this.mIsIntermediateResultExpected = isIntermediateResultExpected;
        return new ArrayList(this.mCallbacks);
    }

    @Nullable
    public synchronized List<ProducerContextCallbacks> cancelNoCallbacks() {
        if (this.mIsCancelled) {
            return null;
        }
        this.mIsCancelled = true;
        return new ArrayList(this.mCallbacks);
    }

    public static void callOnCancellationRequested(@Nullable List<ProducerContextCallbacks> callbacks) {
        if (callbacks != null) {
            for (ProducerContextCallbacks callback : callbacks) {
                callback.onCancellationRequested();
            }
        }
    }

    public static void callOnIsPrefetchChanged(@Nullable List<ProducerContextCallbacks> callbacks) {
        if (callbacks != null) {
            for (ProducerContextCallbacks callback : callbacks) {
                callback.onIsPrefetchChanged();
            }
        }
    }

    public static void callOnIsIntermediateResultExpectedChanged(@Nullable List<ProducerContextCallbacks> callbacks) {
        if (callbacks != null) {
            for (ProducerContextCallbacks callback : callbacks) {
                callback.onIsIntermediateResultExpectedChanged();
            }
        }
    }

    public static void callOnPriorityChanged(@Nullable List<ProducerContextCallbacks> callbacks) {
        if (callbacks != null) {
            for (ProducerContextCallbacks callback : callbacks) {
                callback.onPriorityChanged();
            }
        }
    }
}
