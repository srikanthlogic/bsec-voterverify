package com.google.android.datatransport.runtime.scheduling;

import com.google.android.datatransport.TransportScheduleCallback;
import com.google.android.datatransport.runtime.EventInternal;
import com.google.android.datatransport.runtime.TransportContext;
import com.google.android.datatransport.runtime.TransportRuntime;
import com.google.android.datatransport.runtime.backends.BackendRegistry;
import com.google.android.datatransport.runtime.backends.TransportBackend;
import com.google.android.datatransport.runtime.scheduling.jobscheduling.WorkScheduler;
import com.google.android.datatransport.runtime.scheduling.persistence.EventStore;
import com.google.android.datatransport.runtime.synchronization.SynchronizationGuard;
import java.util.concurrent.Executor;
import java.util.logging.Logger;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class DefaultScheduler implements Scheduler {
    private static final Logger LOGGER = Logger.getLogger(TransportRuntime.class.getName());
    private final BackendRegistry backendRegistry;
    private final EventStore eventStore;
    private final Executor executor;
    private final SynchronizationGuard guard;
    private final WorkScheduler workScheduler;

    @Inject
    public DefaultScheduler(Executor executor, BackendRegistry backendRegistry, WorkScheduler workScheduler, EventStore eventStore, SynchronizationGuard guard) {
        this.executor = executor;
        this.backendRegistry = backendRegistry;
        this.workScheduler = workScheduler;
        this.eventStore = eventStore;
        this.guard = guard;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.Scheduler
    public void schedule(TransportContext transportContext, EventInternal event, TransportScheduleCallback callback) {
        this.executor.execute(new Runnable(transportContext, callback, event) { // from class: com.google.android.datatransport.runtime.scheduling.-$$Lambda$DefaultScheduler$TUAu_XOWDlEdt54QDWOOZf2JNr8
            private final /* synthetic */ TransportContext f$1;
            private final /* synthetic */ TransportScheduleCallback f$2;
            private final /* synthetic */ EventInternal f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // java.lang.Runnable
            public final void run() {
                DefaultScheduler.this.lambda$schedule$1$DefaultScheduler(this.f$1, this.f$2, this.f$3);
            }
        });
    }

    public /* synthetic */ void lambda$schedule$1$DefaultScheduler(TransportContext transportContext, TransportScheduleCallback callback, EventInternal event) {
        try {
            TransportBackend transportBackend = this.backendRegistry.get(transportContext.getBackendName());
            if (transportBackend == null) {
                String errorMsg = String.format("Transport backend '%s' is not registered", transportContext.getBackendName());
                LOGGER.warning(errorMsg);
                callback.onSchedule(new IllegalArgumentException(errorMsg));
                return;
            }
            this.guard.runCriticalSection(new SynchronizationGuard.CriticalSection(transportContext, transportBackend.decorate(event)) { // from class: com.google.android.datatransport.runtime.scheduling.-$$Lambda$DefaultScheduler$n3OBz42ObbCIYysuPGXmoHB28aA
                private final /* synthetic */ TransportContext f$1;
                private final /* synthetic */ EventInternal f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // com.google.android.datatransport.runtime.synchronization.SynchronizationGuard.CriticalSection
                public final Object execute() {
                    return DefaultScheduler.this.lambda$schedule$0$DefaultScheduler(this.f$1, this.f$2);
                }
            });
            callback.onSchedule(null);
        } catch (Exception e) {
            Logger logger = LOGGER;
            logger.warning("Error scheduling event " + e.getMessage());
            callback.onSchedule(e);
        }
    }

    public /* synthetic */ Object lambda$schedule$0$DefaultScheduler(TransportContext transportContext, EventInternal decoratedEvent) {
        this.eventStore.persist(transportContext, decoratedEvent);
        this.workScheduler.schedule(transportContext, 1);
        return null;
    }
}
