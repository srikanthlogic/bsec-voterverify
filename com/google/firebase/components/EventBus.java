package com.google.firebase.components;

import com.google.firebase.events.Event;
import com.google.firebase.events.EventHandler;
import com.google.firebase.events.Publisher;
import com.google.firebase.events.Subscriber;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class EventBus implements Subscriber, Publisher {
    private final Executor defaultExecutor;
    private final Map<Class<?>, ConcurrentHashMap<EventHandler<Object>, Executor>> handlerMap = new HashMap();
    private Queue<Event<?>> pendingEvents = new ArrayDeque();

    public EventBus(Executor defaultExecutor) {
        this.defaultExecutor = defaultExecutor;
    }

    @Override // com.google.firebase.events.Publisher
    public void publish(Event<?> event) {
        Preconditions.checkNotNull(event);
        synchronized (this) {
            if (this.pendingEvents != null) {
                this.pendingEvents.add(event);
                return;
            }
            for (Map.Entry<EventHandler<Object>, Executor> handlerData : getHandlers(event)) {
                handlerData.getValue().execute(new Runnable(handlerData, event) { // from class: com.google.firebase.components.-$$Lambda$EventBus$9vq445XHFrUb2PR7SHiZwffi9vs
                    private final /* synthetic */ Map.Entry f$0;
                    private final /* synthetic */ Event f$1;

                    {
                        this.f$0 = r1;
                        this.f$1 = r2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        ((EventHandler) this.f$0.getKey()).handle(this.f$1);
                    }
                });
            }
        }
    }

    private synchronized Set<Map.Entry<EventHandler<Object>, Executor>> getHandlers(Event<?> event) {
        Map<EventHandler<Object>, Executor> handlers;
        handlers = this.handlerMap.get(event.getType());
        return handlers == null ? Collections.emptySet() : handlers.entrySet();
    }

    @Override // com.google.firebase.events.Subscriber
    public synchronized <T> void subscribe(Class<T> type, Executor executor, EventHandler<? super T> handler) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(handler);
        Preconditions.checkNotNull(executor);
        if (!this.handlerMap.containsKey(type)) {
            this.handlerMap.put(type, new ConcurrentHashMap<>());
        }
        this.handlerMap.get(type).put(handler, executor);
    }

    @Override // com.google.firebase.events.Subscriber
    public <T> void subscribe(Class<T> type, EventHandler<? super T> handler) {
        subscribe(type, this.defaultExecutor, handler);
    }

    @Override // com.google.firebase.events.Subscriber
    public synchronized <T> void unsubscribe(Class<T> type, EventHandler<? super T> handler) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(handler);
        if (this.handlerMap.containsKey(type)) {
            ConcurrentHashMap<EventHandler<Object>, Executor> handlers = this.handlerMap.get(type);
            handlers.remove(handler);
            if (handlers.isEmpty()) {
                this.handlerMap.remove(type);
            }
        }
    }

    public void enablePublishingAndFlushPending() {
        Queue<Event<?>> pending = null;
        synchronized (this) {
            if (this.pendingEvents != null) {
                pending = this.pendingEvents;
                this.pendingEvents = null;
            }
        }
        if (pending != null) {
            for (Event<?> event : pending) {
                publish(event);
            }
        }
    }
}
