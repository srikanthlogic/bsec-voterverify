package com.facebook.imagepipeline.listener;

import com.facebook.common.logging.FLog;
import com.facebook.imagepipeline.request.ImageRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class ForwardingRequestListener implements RequestListener {
    private static final String TAG = "ForwardingRequestListener";
    private final List<RequestListener> mRequestListeners;

    public ForwardingRequestListener(Set<RequestListener> requestListeners) {
        this.mRequestListeners = new ArrayList(requestListeners.size());
        for (RequestListener requestListener : requestListeners) {
            if (requestListener != null) {
                this.mRequestListeners.add(requestListener);
            }
        }
    }

    public ForwardingRequestListener(RequestListener... requestListeners) {
        this.mRequestListeners = new ArrayList(requestListeners.length);
        for (RequestListener requestListener : requestListeners) {
            if (requestListener != null) {
                this.mRequestListeners.add(requestListener);
            }
        }
    }

    public void addRequestListener(RequestListener requestListener) {
        this.mRequestListeners.add(requestListener);
    }

    @Override // com.facebook.imagepipeline.listener.RequestListener
    public void onRequestStart(ImageRequest request, Object callerContext, String requestId, boolean isPrefetch) {
        int numberOfListeners = this.mRequestListeners.size();
        for (int i = 0; i < numberOfListeners; i++) {
            try {
                this.mRequestListeners.get(i).onRequestStart(request, callerContext, requestId, isPrefetch);
            } catch (Exception exception) {
                onException("InternalListener exception in onRequestStart", exception);
            }
        }
    }

    @Override // com.facebook.imagepipeline.producers.ProducerListener
    public void onProducerStart(String requestId, String producerName) {
        int numberOfListeners = this.mRequestListeners.size();
        for (int i = 0; i < numberOfListeners; i++) {
            try {
                this.mRequestListeners.get(i).onProducerStart(requestId, producerName);
            } catch (Exception exception) {
                onException("InternalListener exception in onProducerStart", exception);
            }
        }
    }

    @Override // com.facebook.imagepipeline.producers.ProducerListener
    public void onProducerFinishWithSuccess(String requestId, String producerName, @Nullable Map<String, String> extraMap) {
        int numberOfListeners = this.mRequestListeners.size();
        for (int i = 0; i < numberOfListeners; i++) {
            try {
                this.mRequestListeners.get(i).onProducerFinishWithSuccess(requestId, producerName, extraMap);
            } catch (Exception exception) {
                onException("InternalListener exception in onProducerFinishWithSuccess", exception);
            }
        }
    }

    @Override // com.facebook.imagepipeline.producers.ProducerListener
    public void onProducerFinishWithFailure(String requestId, String producerName, Throwable t, @Nullable Map<String, String> extraMap) {
        int numberOfListeners = this.mRequestListeners.size();
        for (int i = 0; i < numberOfListeners; i++) {
            try {
                this.mRequestListeners.get(i).onProducerFinishWithFailure(requestId, producerName, t, extraMap);
            } catch (Exception exception) {
                onException("InternalListener exception in onProducerFinishWithFailure", exception);
            }
        }
    }

    @Override // com.facebook.imagepipeline.producers.ProducerListener
    public void onProducerFinishWithCancellation(String requestId, String producerName, @Nullable Map<String, String> extraMap) {
        int numberOfListeners = this.mRequestListeners.size();
        for (int i = 0; i < numberOfListeners; i++) {
            try {
                this.mRequestListeners.get(i).onProducerFinishWithCancellation(requestId, producerName, extraMap);
            } catch (Exception exception) {
                onException("InternalListener exception in onProducerFinishWithCancellation", exception);
            }
        }
    }

    @Override // com.facebook.imagepipeline.producers.ProducerListener
    public void onProducerEvent(String requestId, String producerName, String producerEventName) {
        int numberOfListeners = this.mRequestListeners.size();
        for (int i = 0; i < numberOfListeners; i++) {
            try {
                this.mRequestListeners.get(i).onProducerEvent(requestId, producerName, producerEventName);
            } catch (Exception exception) {
                onException("InternalListener exception in onIntermediateChunkStart", exception);
            }
        }
    }

    @Override // com.facebook.imagepipeline.producers.ProducerListener
    public void onUltimateProducerReached(String requestId, String producerName, boolean successful) {
        int numberOfListeners = this.mRequestListeners.size();
        for (int i = 0; i < numberOfListeners; i++) {
            try {
                this.mRequestListeners.get(i).onUltimateProducerReached(requestId, producerName, successful);
            } catch (Exception exception) {
                onException("InternalListener exception in onProducerFinishWithSuccess", exception);
            }
        }
    }

    @Override // com.facebook.imagepipeline.listener.RequestListener
    public void onRequestSuccess(ImageRequest request, String requestId, boolean isPrefetch) {
        int numberOfListeners = this.mRequestListeners.size();
        for (int i = 0; i < numberOfListeners; i++) {
            try {
                this.mRequestListeners.get(i).onRequestSuccess(request, requestId, isPrefetch);
            } catch (Exception exception) {
                onException("InternalListener exception in onRequestSuccess", exception);
            }
        }
    }

    @Override // com.facebook.imagepipeline.listener.RequestListener
    public void onRequestFailure(ImageRequest request, String requestId, Throwable throwable, boolean isPrefetch) {
        int numberOfListeners = this.mRequestListeners.size();
        for (int i = 0; i < numberOfListeners; i++) {
            try {
                this.mRequestListeners.get(i).onRequestFailure(request, requestId, throwable, isPrefetch);
            } catch (Exception exception) {
                onException("InternalListener exception in onRequestFailure", exception);
            }
        }
    }

    @Override // com.facebook.imagepipeline.listener.RequestListener
    public void onRequestCancellation(String requestId) {
        int numberOfListeners = this.mRequestListeners.size();
        for (int i = 0; i < numberOfListeners; i++) {
            try {
                this.mRequestListeners.get(i).onRequestCancellation(requestId);
            } catch (Exception exception) {
                onException("InternalListener exception in onRequestCancellation", exception);
            }
        }
    }

    @Override // com.facebook.imagepipeline.producers.ProducerListener
    public boolean requiresExtraMap(String id) {
        int numberOfListeners = this.mRequestListeners.size();
        for (int i = 0; i < numberOfListeners; i++) {
            if (this.mRequestListeners.get(i).requiresExtraMap(id)) {
                return true;
            }
        }
        return false;
    }

    private void onException(String message, Throwable t) {
        FLog.e(TAG, message, t);
    }
}
