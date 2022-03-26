package com.facebook.imagepipeline.producers;
/* loaded from: classes.dex */
public class SwallowResultProducer<T> implements Producer<Void> {
    private final Producer<T> mInputProducer;

    public SwallowResultProducer(Producer<T> inputProducer) {
        this.mInputProducer = inputProducer;
    }

    @Override // com.facebook.imagepipeline.producers.Producer
    public void produceResults(Consumer<Void> consumer, ProducerContext producerContext) {
        this.mInputProducer.produceResults(new DelegatingConsumer<T, Void>(consumer) { // from class: com.facebook.imagepipeline.producers.SwallowResultProducer.1
            @Override // com.facebook.imagepipeline.producers.BaseConsumer
            protected void onNewResultImpl(T newResult, int status) {
                if (isLast(status)) {
                    getConsumer().onNewResult(null, status);
                }
            }
        }, producerContext);
    }
}
