package com.facebook.imagepipeline.producers;

import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.request.ImageRequest;
/* loaded from: classes.dex */
public class SettableProducerContext extends BaseProducerContext {
    public SettableProducerContext(ProducerContext context) {
        this(context.getImageRequest(), context.getId(), context.getListener(), context.getCallerContext(), context.getLowestPermittedRequestLevel(), context.isPrefetch(), context.isIntermediateResultExpected(), context.getPriority());
    }

    public SettableProducerContext(ImageRequest overrideRequest, ProducerContext context) {
        this(overrideRequest, context.getId(), context.getListener(), context.getCallerContext(), context.getLowestPermittedRequestLevel(), context.isPrefetch(), context.isIntermediateResultExpected(), context.getPriority());
    }

    public SettableProducerContext(ImageRequest imageRequest, String id, ProducerListener producerListener, Object callerContext, ImageRequest.RequestLevel lowestPermittedRequestLevel, boolean isPrefetch, boolean isIntermediateResultExpected, Priority priority) {
        super(imageRequest, id, producerListener, callerContext, lowestPermittedRequestLevel, isPrefetch, isIntermediateResultExpected, priority);
    }

    public void setIsPrefetch(boolean isPrefetch) {
        BaseProducerContext.callOnIsPrefetchChanged(setIsPrefetchNoCallbacks(isPrefetch));
    }

    public void setIsIntermediateResultExpected(boolean isIntermediateResultExpected) {
        BaseProducerContext.callOnIsIntermediateResultExpectedChanged(setIsIntermediateResultExpectedNoCallbacks(isIntermediateResultExpected));
    }

    public void setPriority(Priority priority) {
        BaseProducerContext.callOnPriorityChanged(setPriorityNoCallbacks(priority));
    }
}
