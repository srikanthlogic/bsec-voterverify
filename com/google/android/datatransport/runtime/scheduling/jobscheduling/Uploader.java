package com.google.android.datatransport.runtime.scheduling.jobscheduling;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.google.android.datatransport.runtime.EventInternal;
import com.google.android.datatransport.runtime.TransportContext;
import com.google.android.datatransport.runtime.backends.BackendRegistry;
import com.google.android.datatransport.runtime.backends.BackendRequest;
import com.google.android.datatransport.runtime.backends.BackendResponse;
import com.google.android.datatransport.runtime.backends.TransportBackend;
import com.google.android.datatransport.runtime.logging.Logging;
import com.google.android.datatransport.runtime.scheduling.persistence.EventStore;
import com.google.android.datatransport.runtime.scheduling.persistence.PersistedEvent;
import com.google.android.datatransport.runtime.synchronization.SynchronizationException;
import com.google.android.datatransport.runtime.synchronization.SynchronizationGuard;
import com.google.android.datatransport.runtime.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class Uploader {
    private static final String LOG_TAG;
    private final BackendRegistry backendRegistry;
    private final Clock clock;
    private final Context context;
    private final EventStore eventStore;
    private final Executor executor;
    private final SynchronizationGuard guard;
    private final WorkScheduler workScheduler;

    @Inject
    public Uploader(Context context, BackendRegistry backendRegistry, EventStore eventStore, WorkScheduler workScheduler, Executor executor, SynchronizationGuard guard, Clock clock) {
        this.context = context;
        this.backendRegistry = backendRegistry;
        this.eventStore = eventStore;
        this.workScheduler = workScheduler;
        this.executor = executor;
        this.guard = guard;
        this.clock = clock;
    }

    boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void upload(TransportContext transportContext, int attemptNumber, Runnable callback) {
        this.executor.execute(new Runnable(transportContext, attemptNumber, callback) { // from class: com.google.android.datatransport.runtime.scheduling.jobscheduling.-$$Lambda$Uploader$gD2ng4DJI1wrsPbN_hxDpvworD8
            private final /* synthetic */ TransportContext f$1;
            private final /* synthetic */ int f$2;
            private final /* synthetic */ Runnable f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Uploader.this.lambda$upload$1$Uploader(this.f$1, this.f$2, this.f$3);
            }
        });
    }

    public /* synthetic */ void lambda$upload$1$Uploader(TransportContext transportContext, int attemptNumber, Runnable callback) {
        try {
            try {
                SynchronizationGuard synchronizationGuard = this.guard;
                EventStore eventStore = this.eventStore;
                Objects.requireNonNull(eventStore);
                synchronizationGuard.runCriticalSection(new SynchronizationGuard.CriticalSection() { // from class: com.google.android.datatransport.runtime.scheduling.jobscheduling.-$$Lambda$7iIGXG4rziTDaCv7wibWFWjAdgo
                    @Override // com.google.android.datatransport.runtime.synchronization.SynchronizationGuard.CriticalSection
                    public final Object execute() {
                        return Integer.valueOf(EventStore.this.cleanUp());
                    }
                });
                if (!isNetworkAvailable()) {
                    this.guard.runCriticalSection(new SynchronizationGuard.CriticalSection(transportContext, attemptNumber) { // from class: com.google.android.datatransport.runtime.scheduling.jobscheduling.-$$Lambda$Uploader$-PizdFkrUS80CHQoeatutNhEQNk
                        private final /* synthetic */ TransportContext f$1;
                        private final /* synthetic */ int f$2;

                        {
                            this.f$1 = r2;
                            this.f$2 = r3;
                        }

                        @Override // com.google.android.datatransport.runtime.synchronization.SynchronizationGuard.CriticalSection
                        public final Object execute() {
                            return Uploader.this.lambda$upload$0$Uploader(this.f$1, this.f$2);
                        }
                    });
                } else {
                    logAndUpdateState(transportContext, attemptNumber);
                }
            } catch (SynchronizationException e) {
                this.workScheduler.schedule(transportContext, attemptNumber + 1);
            }
        } finally {
            callback.run();
        }
    }

    public /* synthetic */ Object lambda$upload$0$Uploader(TransportContext transportContext, int attemptNumber) {
        this.workScheduler.schedule(transportContext, attemptNumber + 1);
        return null;
    }

    void logAndUpdateState(TransportContext transportContext, int attemptNumber) {
        BackendResponse response;
        TransportBackend backend = this.backendRegistry.get(transportContext.getBackendName());
        Iterable<PersistedEvent> persistedEvents = (Iterable) this.guard.runCriticalSection(new SynchronizationGuard.CriticalSection(transportContext) { // from class: com.google.android.datatransport.runtime.scheduling.jobscheduling.-$$Lambda$Uploader$YOQT1rGzgM7xDFyc87MybXuGXpM
            private final /* synthetic */ TransportContext f$1;

            {
                this.f$1 = r2;
            }

            @Override // com.google.android.datatransport.runtime.synchronization.SynchronizationGuard.CriticalSection
            public final Object execute() {
                return Uploader.this.lambda$logAndUpdateState$2$Uploader(this.f$1);
            }
        });
        if (persistedEvents.iterator().hasNext()) {
            if (backend == null) {
                Logging.d(LOG_TAG, "Unknown backend for %s, deleting event batch for it...", transportContext);
                response = BackendResponse.fatalError();
            } else {
                List<EventInternal> eventInternals = new ArrayList<>();
                for (PersistedEvent persistedEvent : persistedEvents) {
                    eventInternals.add(persistedEvent.getEvent());
                }
                response = backend.send(BackendRequest.builder().setEvents(eventInternals).setExtras(transportContext.getExtras()).build());
            }
            this.guard.runCriticalSection(new SynchronizationGuard.CriticalSection(response, persistedEvents, transportContext, attemptNumber) { // from class: com.google.android.datatransport.runtime.scheduling.jobscheduling.-$$Lambda$Uploader$uhccXyrB8dzthtbSkZDlHOlHlU8
                private final /* synthetic */ BackendResponse f$1;
                private final /* synthetic */ Iterable f$2;
                private final /* synthetic */ TransportContext f$3;
                private final /* synthetic */ int f$4;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                    this.f$4 = r5;
                }

                @Override // com.google.android.datatransport.runtime.synchronization.SynchronizationGuard.CriticalSection
                public final Object execute() {
                    return Uploader.this.lambda$logAndUpdateState$3$Uploader(this.f$1, this.f$2, this.f$3, this.f$4);
                }
            });
        }
    }

    public /* synthetic */ Iterable lambda$logAndUpdateState$2$Uploader(TransportContext transportContext) {
        return this.eventStore.loadBatch(transportContext);
    }

    public /* synthetic */ Object lambda$logAndUpdateState$3$Uploader(BackendResponse response, Iterable persistedEvents, TransportContext transportContext, int attemptNumber) {
        if (response.getStatus() == BackendResponse.Status.TRANSIENT_ERROR) {
            this.eventStore.recordFailure(persistedEvents);
            this.workScheduler.schedule(transportContext, attemptNumber + 1);
            return null;
        }
        this.eventStore.recordSuccess(persistedEvents);
        if (response.getStatus() == BackendResponse.Status.OK) {
            this.eventStore.recordNextCallTime(transportContext, this.clock.getTime() + response.getNextRequestWaitMillis());
        }
        if (!this.eventStore.hasPendingEventsFor(transportContext)) {
            return null;
        }
        this.workScheduler.schedule(transportContext, 1, true);
        return null;
    }
}
