package com.google.android.datatransport.runtime.scheduling.persistence;

import com.google.android.datatransport.runtime.EventInternal;
import com.google.android.datatransport.runtime.TransportContext;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AutoValue_PersistedEvent extends PersistedEvent {
    private final EventInternal event;
    private final long id;
    private final TransportContext transportContext;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoValue_PersistedEvent(long id, TransportContext transportContext, EventInternal event) {
        this.id = id;
        if (transportContext != null) {
            this.transportContext = transportContext;
            if (event != null) {
                this.event = event;
                return;
            }
            throw new NullPointerException("Null event");
        }
        throw new NullPointerException("Null transportContext");
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.PersistedEvent
    public long getId() {
        return this.id;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.PersistedEvent
    public TransportContext getTransportContext() {
        return this.transportContext;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.PersistedEvent
    public EventInternal getEvent() {
        return this.event;
    }

    public String toString() {
        return "PersistedEvent{id=" + this.id + ", transportContext=" + this.transportContext + ", event=" + this.event + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PersistedEvent)) {
            return false;
        }
        PersistedEvent that = (PersistedEvent) o;
        if (this.id != that.getId() || !this.transportContext.equals(that.getTransportContext()) || !this.event.equals(that.getEvent())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        long j = this.id;
        return (((((1 * 1000003) ^ ((int) (j ^ (j >>> 32)))) * 1000003) ^ this.transportContext.hashCode()) * 1000003) ^ this.event.hashCode();
    }
}
