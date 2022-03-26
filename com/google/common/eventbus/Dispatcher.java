package com.google.common.eventbus;

import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
/* loaded from: classes.dex */
public abstract class Dispatcher {
    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void dispatch(Object obj, Iterator<Subscriber> it);

    Dispatcher() {
    }

    public static Dispatcher perThreadDispatchQueue() {
        return new PerThreadQueuedDispatcher();
    }

    public static Dispatcher legacyAsync() {
        return new LegacyAsyncDispatcher();
    }

    static Dispatcher immediate() {
        return ImmediateDispatcher.INSTANCE;
    }

    /* loaded from: classes.dex */
    public static final class PerThreadQueuedDispatcher extends Dispatcher {
        private final ThreadLocal<Boolean> dispatching;
        private final ThreadLocal<Queue<Event>> queue;

        private PerThreadQueuedDispatcher() {
            this.queue = new ThreadLocal<Queue<Event>>() { // from class: com.google.common.eventbus.Dispatcher.PerThreadQueuedDispatcher.1
                @Override // java.lang.ThreadLocal
                public Queue<Event> initialValue() {
                    return Queues.newArrayDeque();
                }
            };
            this.dispatching = new ThreadLocal<Boolean>() { // from class: com.google.common.eventbus.Dispatcher.PerThreadQueuedDispatcher.2
                @Override // java.lang.ThreadLocal
                public Boolean initialValue() {
                    return false;
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX WARN: Removed duplicated region for block: B:17:0x0054 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:7:0x0038 A[Catch: all -> 0x005f, LOOP:0: B:7:0x0038->B:9:0x0042, LOOP_START, TryCatch #0 {all -> 0x005f, blocks: (B:5:0x002f, B:7:0x0038, B:9:0x0042), top: B:15:0x002f }] */
        @Override // com.google.common.eventbus.Dispatcher
        /* Code decompiled incorrectly, please refer to instructions dump */
        public void dispatch(Object event, Iterator<Subscriber> subscribers) {
            Preconditions.checkNotNull(event);
            Preconditions.checkNotNull(subscribers);
            Queue<Event> queueForThread = this.queue.get();
            queueForThread.offer(new Event(event, subscribers));
            if (!this.dispatching.get().booleanValue()) {
                this.dispatching.set(true);
                while (true) {
                    try {
                        Event nextEvent = queueForThread.poll();
                        if (nextEvent == null) {
                            while (true) {
                                if (nextEvent.subscribers.hasNext()) {
                                    ((Subscriber) nextEvent.subscribers.next()).dispatchEvent(nextEvent.event);
                                }
                            }
                            Event nextEvent2 = queueForThread.poll();
                            if (nextEvent2 == null) {
                                return;
                            }
                        }
                    } finally {
                        this.dispatching.remove();
                        this.queue.remove();
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static final class Event {
            private final Object event;
            private final Iterator<Subscriber> subscribers;

            private Event(Object event, Iterator<Subscriber> subscribers) {
                this.event = event;
                this.subscribers = subscribers;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class LegacyAsyncDispatcher extends Dispatcher {
        private final ConcurrentLinkedQueue<EventWithSubscriber> queue;

        private LegacyAsyncDispatcher() {
            this.queue = Queues.newConcurrentLinkedQueue();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.eventbus.Dispatcher
        public void dispatch(Object event, Iterator<Subscriber> subscribers) {
            Preconditions.checkNotNull(event);
            while (subscribers.hasNext()) {
                this.queue.add(new EventWithSubscriber(event, subscribers.next()));
            }
            while (true) {
                EventWithSubscriber e = this.queue.poll();
                if (e != null) {
                    e.subscriber.dispatchEvent(e.event);
                } else {
                    return;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static final class EventWithSubscriber {
            private final Object event;
            private final Subscriber subscriber;

            private EventWithSubscriber(Object event, Subscriber subscriber) {
                this.event = event;
                this.subscriber = subscriber;
            }
        }
    }

    /* loaded from: classes.dex */
    private static final class ImmediateDispatcher extends Dispatcher {
        private static final ImmediateDispatcher INSTANCE = new ImmediateDispatcher();

        private ImmediateDispatcher() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.eventbus.Dispatcher
        public void dispatch(Object event, Iterator<Subscriber> subscribers) {
            Preconditions.checkNotNull(event);
            while (subscribers.hasNext()) {
                subscribers.next().dispatchEvent(event);
            }
        }
    }
}
