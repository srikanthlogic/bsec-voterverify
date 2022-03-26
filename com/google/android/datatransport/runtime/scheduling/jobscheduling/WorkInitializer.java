package com.google.android.datatransport.runtime.scheduling.jobscheduling;

import com.google.android.datatransport.runtime.TransportContext;
import com.google.android.datatransport.runtime.scheduling.persistence.EventStore;
import com.google.android.datatransport.runtime.synchronization.SynchronizationGuard;
import java.util.concurrent.Executor;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class WorkInitializer {
    private final Executor executor;
    private final SynchronizationGuard guard;
    private final WorkScheduler scheduler;
    private final EventStore store;

    @Inject
    public WorkInitializer(Executor executor, EventStore store, WorkScheduler scheduler, SynchronizationGuard guard) {
        this.executor = executor;
        this.store = store;
        this.scheduler = scheduler;
        this.guard = guard;
    }

    public void ensureContextsScheduled() {
        this.executor.execute(new Runnable() { // from class: com.google.android.datatransport.runtime.scheduling.jobscheduling.-$$Lambda$WorkInitializer$4euq1WvqLQFGz_1FrH7d8V-ONnE
            @Override // java.lang.Runnable
            public final void run() {
                WorkInitializer.this.lambda$ensureContextsScheduled$1$WorkInitializer();
            }
        });
    }

    public /* synthetic */ void lambda$ensureContextsScheduled$1$WorkInitializer() {
        this.guard.runCriticalSection(new SynchronizationGuard.CriticalSection() { // from class: com.google.android.datatransport.runtime.scheduling.jobscheduling.-$$Lambda$WorkInitializer$NRHUY4kiIAYkslmRyjlKfd6AS8I
            @Override // com.google.android.datatransport.runtime.synchronization.SynchronizationGuard.CriticalSection
            public final Object execute() {
                return WorkInitializer.this.lambda$ensureContextsScheduled$0$WorkInitializer();
            }
        });
    }

    public /* synthetic */ Object lambda$ensureContextsScheduled$0$WorkInitializer() {
        for (TransportContext context : this.store.loadActiveContexts()) {
            this.scheduler.schedule(context, 1);
        }
        return null;
    }
}
