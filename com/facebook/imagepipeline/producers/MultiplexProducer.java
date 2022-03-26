package com.facebook.imagepipeline.producers;

import android.util.Pair;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Sets;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.systrace.FrescoSystrace;
import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public abstract class MultiplexProducer<K, T extends Closeable> implements Producer<T> {
    private final Producer<T> mInputProducer;
    final Map<K, MultiplexProducer<K, T>.Multiplexer> mMultiplexers = new HashMap();

    protected abstract T cloneOrNull(T t);

    protected abstract K getKey(ProducerContext producerContext);

    public MultiplexProducer(Producer<T> inputProducer) {
        this.mInputProducer = inputProducer;
    }

    @Override // com.facebook.imagepipeline.producers.Producer
    public void produceResults(Consumer<T> consumer, ProducerContext context) {
        boolean createdNewMultiplexer;
        MultiplexProducer<K, T>.Multiplexer multiplexer;
        try {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.beginSection("MultiplexProducer#produceResults");
            }
            K key = getKey(context);
            do {
                createdNewMultiplexer = false;
                synchronized (this) {
                    multiplexer = getExistingMultiplexer(key);
                    if (multiplexer == null) {
                        multiplexer = createAndPutNewMultiplexer(key);
                        createdNewMultiplexer = true;
                    }
                }
            } while (!multiplexer.addNewConsumer(consumer, context));
            if (createdNewMultiplexer) {
                multiplexer.startInputProducerIfHasAttachedConsumers();
            }
        } finally {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.endSection();
            }
        }
    }

    public synchronized MultiplexProducer<K, T>.Multiplexer getExistingMultiplexer(K key) {
        return this.mMultiplexers.get(key);
    }

    private synchronized MultiplexProducer<K, T>.Multiplexer createAndPutNewMultiplexer(K key) {
        MultiplexProducer<K, T>.Multiplexer multiplexer;
        multiplexer = new Multiplexer(key);
        this.mMultiplexers.put(key, multiplexer);
        return multiplexer;
    }

    public synchronized void removeMultiplexer(K key, MultiplexProducer<K, T>.Multiplexer multiplexer) {
        if (this.mMultiplexers.get(key) == multiplexer) {
            this.mMultiplexers.remove(key);
        }
    }

    /* loaded from: classes.dex */
    public class Multiplexer {
        private final CopyOnWriteArraySet<Pair<Consumer<T>, ProducerContext>> mConsumerContextPairs = Sets.newCopyOnWriteArraySet();
        @Nullable
        private MultiplexProducer<K, T>.Multiplexer.ForwardingConsumer mForwardingConsumer;
        private final K mKey;
        @Nullable
        private Closeable mLastIntermediateResult;
        private float mLastProgress;
        private int mLastStatus;
        @Nullable
        private BaseProducerContext mMultiplexProducerContext;

        public Multiplexer(K key) {
            MultiplexProducer.this = this$0;
            this.mKey = key;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public boolean addNewConsumer(Consumer<T> consumer, ProducerContext producerContext) {
            Pair<Consumer<T>, ProducerContext> consumerContextPair = Pair.create(consumer, producerContext);
            synchronized (this) {
                if (MultiplexProducer.this.getExistingMultiplexer(this.mKey) != this) {
                    return false;
                }
                this.mConsumerContextPairs.add(consumerContextPair);
                List<ProducerContextCallbacks> prefetchCallbacks = updateIsPrefetch();
                List<ProducerContextCallbacks> priorityCallbacks = updatePriority();
                List<ProducerContextCallbacks> intermediateResultsCallbacks = updateIsIntermediateResultExpected();
                Closeable closeable = this.mLastIntermediateResult;
                float lastProgress = this.mLastProgress;
                int lastStatus = this.mLastStatus;
                BaseProducerContext.callOnIsPrefetchChanged(prefetchCallbacks);
                BaseProducerContext.callOnPriorityChanged(priorityCallbacks);
                BaseProducerContext.callOnIsIntermediateResultExpectedChanged(intermediateResultsCallbacks);
                synchronized (consumerContextPair) {
                    synchronized (this) {
                        if (closeable != this.mLastIntermediateResult) {
                            closeable = null;
                        } else if (closeable != null) {
                            closeable = MultiplexProducer.this.cloneOrNull(closeable);
                        }
                    }
                    if (closeable != null) {
                        if (lastProgress > 0.0f) {
                            consumer.onProgressUpdate(lastProgress);
                        }
                        consumer.onNewResult(closeable, lastStatus);
                        closeSafely(closeable);
                    }
                }
                addCallbacks(consumerContextPair, producerContext);
                return true;
            }
        }

        private void addCallbacks(final Pair<Consumer<T>, ProducerContext> consumerContextPair, ProducerContext producerContext) {
            producerContext.addCallbacks(new BaseProducerContextCallbacks() { // from class: com.facebook.imagepipeline.producers.MultiplexProducer.Multiplexer.1
                @Override // com.facebook.imagepipeline.producers.BaseProducerContextCallbacks, com.facebook.imagepipeline.producers.ProducerContextCallbacks
                public void onCancellationRequested() {
                    boolean pairWasRemoved;
                    BaseProducerContext contextToCancel = null;
                    List<ProducerContextCallbacks> isPrefetchCallbacks = null;
                    List<ProducerContextCallbacks> priorityCallbacks = null;
                    List<ProducerContextCallbacks> isIntermediateResultExpectedCallbacks = null;
                    synchronized (Multiplexer.this) {
                        pairWasRemoved = Multiplexer.this.mConsumerContextPairs.remove(consumerContextPair);
                        if (pairWasRemoved) {
                            if (Multiplexer.this.mConsumerContextPairs.isEmpty()) {
                                contextToCancel = Multiplexer.this.mMultiplexProducerContext;
                            } else {
                                isPrefetchCallbacks = Multiplexer.this.updateIsPrefetch();
                                priorityCallbacks = Multiplexer.this.updatePriority();
                                isIntermediateResultExpectedCallbacks = Multiplexer.this.updateIsIntermediateResultExpected();
                            }
                        }
                    }
                    BaseProducerContext.callOnIsPrefetchChanged(isPrefetchCallbacks);
                    BaseProducerContext.callOnPriorityChanged(priorityCallbacks);
                    BaseProducerContext.callOnIsIntermediateResultExpectedChanged(isIntermediateResultExpectedCallbacks);
                    if (contextToCancel != null) {
                        contextToCancel.cancel();
                    }
                    if (pairWasRemoved) {
                        ((Consumer) consumerContextPair.first).onCancellation();
                    }
                }

                @Override // com.facebook.imagepipeline.producers.BaseProducerContextCallbacks, com.facebook.imagepipeline.producers.ProducerContextCallbacks
                public void onIsPrefetchChanged() {
                    BaseProducerContext.callOnIsPrefetchChanged(Multiplexer.this.updateIsPrefetch());
                }

                @Override // com.facebook.imagepipeline.producers.BaseProducerContextCallbacks, com.facebook.imagepipeline.producers.ProducerContextCallbacks
                public void onIsIntermediateResultExpectedChanged() {
                    BaseProducerContext.callOnIsIntermediateResultExpectedChanged(Multiplexer.this.updateIsIntermediateResultExpected());
                }

                @Override // com.facebook.imagepipeline.producers.BaseProducerContextCallbacks, com.facebook.imagepipeline.producers.ProducerContextCallbacks
                public void onPriorityChanged() {
                    BaseProducerContext.callOnPriorityChanged(Multiplexer.this.updatePriority());
                }
            });
        }

        public void startInputProducerIfHasAttachedConsumers() {
            synchronized (this) {
                boolean z = true;
                Preconditions.checkArgument(this.mMultiplexProducerContext == null);
                if (this.mForwardingConsumer != null) {
                    z = false;
                }
                Preconditions.checkArgument(z);
                if (this.mConsumerContextPairs.isEmpty()) {
                    MultiplexProducer.this.removeMultiplexer(this.mKey, this);
                    return;
                }
                ProducerContext producerContext = (ProducerContext) ((Pair) this.mConsumerContextPairs.iterator().next()).second;
                this.mMultiplexProducerContext = new BaseProducerContext(producerContext.getImageRequest(), producerContext.getId(), producerContext.getListener(), producerContext.getCallerContext(), producerContext.getLowestPermittedRequestLevel(), computeIsPrefetch(), computeIsIntermediateResultExpected(), computePriority());
                this.mForwardingConsumer = new ForwardingConsumer();
                MultiplexProducer.this.mInputProducer.produceResults(this.mForwardingConsumer, this.mMultiplexProducerContext);
            }
        }

        @Nullable
        public synchronized List<ProducerContextCallbacks> updateIsPrefetch() {
            if (this.mMultiplexProducerContext == null) {
                return null;
            }
            return this.mMultiplexProducerContext.setIsPrefetchNoCallbacks(computeIsPrefetch());
        }

        private synchronized boolean computeIsPrefetch() {
            Iterator it = this.mConsumerContextPairs.iterator();
            while (it.hasNext()) {
                if (!((ProducerContext) ((Pair) it.next()).second).isPrefetch()) {
                    return false;
                }
            }
            return true;
        }

        @Nullable
        public synchronized List<ProducerContextCallbacks> updateIsIntermediateResultExpected() {
            if (this.mMultiplexProducerContext == null) {
                return null;
            }
            return this.mMultiplexProducerContext.setIsIntermediateResultExpectedNoCallbacks(computeIsIntermediateResultExpected());
        }

        private synchronized boolean computeIsIntermediateResultExpected() {
            Iterator it = this.mConsumerContextPairs.iterator();
            while (it.hasNext()) {
                if (((ProducerContext) ((Pair) it.next()).second).isIntermediateResultExpected()) {
                    return true;
                }
            }
            return false;
        }

        @Nullable
        public synchronized List<ProducerContextCallbacks> updatePriority() {
            if (this.mMultiplexProducerContext == null) {
                return null;
            }
            return this.mMultiplexProducerContext.setPriorityNoCallbacks(computePriority());
        }

        private synchronized Priority computePriority() {
            Priority priority;
            priority = Priority.LOW;
            Iterator it = this.mConsumerContextPairs.iterator();
            while (it.hasNext()) {
                priority = Priority.getHigherPriority(priority, ((ProducerContext) ((Pair) it.next()).second).getPriority());
            }
            return priority;
        }

        public void onFailure(MultiplexProducer<K, T>.Multiplexer.ForwardingConsumer consumer, Throwable t) {
            synchronized (this) {
                if (this.mForwardingConsumer == consumer) {
                    Iterator it = this.mConsumerContextPairs.iterator();
                    this.mConsumerContextPairs.clear();
                    MultiplexProducer.this.removeMultiplexer(this.mKey, this);
                    closeSafely(this.mLastIntermediateResult);
                    this.mLastIntermediateResult = null;
                    while (it.hasNext()) {
                        Pair<Consumer<T>, ProducerContext> pair = (Pair) it.next();
                        synchronized (pair) {
                            ((Consumer) pair.first).onFailure(t);
                        }
                    }
                }
            }
        }

        public void onNextResult(MultiplexProducer<K, T>.Multiplexer.ForwardingConsumer consumer, T closeableObject, int status) {
            synchronized (this) {
                if (this.mForwardingConsumer == consumer) {
                    closeSafely(this.mLastIntermediateResult);
                    this.mLastIntermediateResult = null;
                    Iterator it = this.mConsumerContextPairs.iterator();
                    if (BaseConsumer.isNotLast(status)) {
                        this.mLastIntermediateResult = MultiplexProducer.this.cloneOrNull(closeableObject);
                        this.mLastStatus = status;
                    } else {
                        this.mConsumerContextPairs.clear();
                        MultiplexProducer.this.removeMultiplexer(this.mKey, this);
                    }
                    while (it.hasNext()) {
                        Pair<Consumer<T>, ProducerContext> pair = (Pair) it.next();
                        synchronized (pair) {
                            ((Consumer) pair.first).onNewResult(closeableObject, status);
                        }
                    }
                }
            }
        }

        public void onCancelled(MultiplexProducer<K, T>.Multiplexer.ForwardingConsumer forwardingConsumer) {
            synchronized (this) {
                if (this.mForwardingConsumer == forwardingConsumer) {
                    this.mForwardingConsumer = null;
                    this.mMultiplexProducerContext = null;
                    closeSafely(this.mLastIntermediateResult);
                    this.mLastIntermediateResult = null;
                    startInputProducerIfHasAttachedConsumers();
                }
            }
        }

        public void onProgressUpdate(MultiplexProducer<K, T>.Multiplexer.ForwardingConsumer forwardingConsumer, float progress) {
            synchronized (this) {
                if (this.mForwardingConsumer == forwardingConsumer) {
                    this.mLastProgress = progress;
                    Iterator it = this.mConsumerContextPairs.iterator();
                    while (it.hasNext()) {
                        Pair<Consumer<T>, ProducerContext> pair = (Pair) it.next();
                        synchronized (pair) {
                            ((Consumer) pair.first).onProgressUpdate(progress);
                        }
                    }
                }
            }
        }

        private void closeSafely(Closeable obj) {
            if (obj != null) {
                try {
                    obj.close();
                } catch (IOException ioe) {
                    throw new RuntimeException(ioe);
                }
            }
        }

        /* loaded from: classes.dex */
        public class ForwardingConsumer extends BaseConsumer<T> {
            private ForwardingConsumer() {
                Multiplexer.this = r1;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.facebook.imagepipeline.producers.BaseConsumer
            protected /* bridge */ /* synthetic */ void onNewResultImpl(Object obj, int i) {
                onNewResultImpl((ForwardingConsumer) ((Closeable) obj), i);
            }

            protected void onNewResultImpl(T newResult, int status) {
                try {
                    if (FrescoSystrace.isTracing()) {
                        FrescoSystrace.beginSection("MultiplexProducer#onNewResult");
                    }
                    Multiplexer.this.onNextResult(this, newResult, status);
                } finally {
                    if (FrescoSystrace.isTracing()) {
                        FrescoSystrace.endSection();
                    }
                }
            }

            @Override // com.facebook.imagepipeline.producers.BaseConsumer
            protected void onFailureImpl(Throwable t) {
                try {
                    if (FrescoSystrace.isTracing()) {
                        FrescoSystrace.beginSection("MultiplexProducer#onFailure");
                    }
                    Multiplexer.this.onFailure(this, t);
                } finally {
                    if (FrescoSystrace.isTracing()) {
                        FrescoSystrace.endSection();
                    }
                }
            }

            @Override // com.facebook.imagepipeline.producers.BaseConsumer
            protected void onCancellationImpl() {
                try {
                    if (FrescoSystrace.isTracing()) {
                        FrescoSystrace.beginSection("MultiplexProducer#onCancellation");
                    }
                    Multiplexer.this.onCancelled(this);
                } finally {
                    if (FrescoSystrace.isTracing()) {
                        FrescoSystrace.endSection();
                    }
                }
            }

            @Override // com.facebook.imagepipeline.producers.BaseConsumer
            protected void onProgressUpdateImpl(float progress) {
                try {
                    if (FrescoSystrace.isTracing()) {
                        FrescoSystrace.beginSection("MultiplexProducer#onProgressUpdate");
                    }
                    Multiplexer.this.onProgressUpdate(this, progress);
                } finally {
                    if (FrescoSystrace.isTracing()) {
                        FrescoSystrace.endSection();
                    }
                }
            }
        }
    }
}
