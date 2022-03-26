package com.facebook.imagepipeline.producers;

import com.facebook.common.executors.StatefulRunnable;
import java.util.Map;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public abstract class StatefulProducerRunnable<T> extends StatefulRunnable<T> {
    private final Consumer<T> mConsumer;
    private final ProducerListener mProducerListener;
    private final String mProducerName;
    private final String mRequestId;

    @Override // com.facebook.common.executors.StatefulRunnable
    protected abstract void disposeResult(T t);

    public StatefulProducerRunnable(Consumer<T> consumer, ProducerListener producerListener, String producerName, String requestId) {
        this.mConsumer = consumer;
        this.mProducerListener = producerListener;
        this.mProducerName = producerName;
        this.mRequestId = requestId;
        this.mProducerListener.onProducerStart(this.mRequestId, this.mProducerName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.common.executors.StatefulRunnable
    public void onSuccess(T result) {
        ProducerListener producerListener = this.mProducerListener;
        String str = this.mRequestId;
        producerListener.onProducerFinishWithSuccess(str, this.mProducerName, producerListener.requiresExtraMap(str) ? getExtraMapOnSuccess(result) : null);
        this.mConsumer.onNewResult(result, 1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.common.executors.StatefulRunnable
    public void onFailure(Exception e) {
        ProducerListener producerListener = this.mProducerListener;
        String str = this.mRequestId;
        producerListener.onProducerFinishWithFailure(str, this.mProducerName, e, producerListener.requiresExtraMap(str) ? getExtraMapOnFailure(e) : null);
        this.mConsumer.onFailure(e);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.common.executors.StatefulRunnable
    public void onCancellation() {
        ProducerListener producerListener = this.mProducerListener;
        String str = this.mRequestId;
        producerListener.onProducerFinishWithCancellation(str, this.mProducerName, producerListener.requiresExtraMap(str) ? getExtraMapOnCancellation() : null);
        this.mConsumer.onCancellation();
    }

    @Nullable
    protected Map<String, String> getExtraMapOnSuccess(T result) {
        return null;
    }

    @Nullable
    protected Map<String, String> getExtraMapOnFailure(Exception exception) {
        return null;
    }

    @Nullable
    protected Map<String, String> getExtraMapOnCancellation() {
        return null;
    }
}
