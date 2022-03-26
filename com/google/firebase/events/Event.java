package com.google.firebase.events;

import com.google.firebase.components.Preconditions;
/* loaded from: classes3.dex */
public class Event<T> {
    private final T payload;
    private final Class<T> type;

    public Event(Class<T> type, T payload) {
        this.type = (Class) Preconditions.checkNotNull(type);
        this.payload = (T) Preconditions.checkNotNull(payload);
    }

    public Class<T> getType() {
        return this.type;
    }

    public T getPayload() {
        return this.payload;
    }

    public String toString() {
        return String.format("Event{type: %s, payload: %s}", this.type, this.payload);
    }
}
