package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ListenerCallQueue;
import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.Service;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes3.dex */
public abstract class AbstractService implements Service {
    private static final ListenerCallQueue.Event<Service.Listener> STARTING_EVENT = new ListenerCallQueue.Event<Service.Listener>() { // from class: com.google.common.util.concurrent.AbstractService.1
        public void call(Service.Listener listener) {
            listener.starting();
        }

        public String toString() {
            return "starting()";
        }
    };
    private static final ListenerCallQueue.Event<Service.Listener> RUNNING_EVENT = new ListenerCallQueue.Event<Service.Listener>() { // from class: com.google.common.util.concurrent.AbstractService.2
        public void call(Service.Listener listener) {
            listener.running();
        }

        public String toString() {
            return "running()";
        }
    };
    private static final ListenerCallQueue.Event<Service.Listener> STOPPING_FROM_STARTING_EVENT = stoppingEvent(Service.State.STARTING);
    private static final ListenerCallQueue.Event<Service.Listener> STOPPING_FROM_RUNNING_EVENT = stoppingEvent(Service.State.RUNNING);
    private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_NEW_EVENT = terminatedEvent(Service.State.NEW);
    private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_STARTING_EVENT = terminatedEvent(Service.State.STARTING);
    private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_RUNNING_EVENT = terminatedEvent(Service.State.RUNNING);
    private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_STOPPING_EVENT = terminatedEvent(Service.State.STOPPING);
    private final Monitor monitor = new Monitor();
    private final Monitor.Guard isStartable = new IsStartableGuard();
    private final Monitor.Guard isStoppable = new IsStoppableGuard();
    private final Monitor.Guard hasReachedRunning = new HasReachedRunningGuard();
    private final Monitor.Guard isStopped = new IsStoppedGuard();
    private final ListenerCallQueue<Service.Listener> listeners = new ListenerCallQueue<>();
    private volatile StateSnapshot snapshot = new StateSnapshot(Service.State.NEW);

    protected abstract void doStart();

    protected abstract void doStop();

    private static ListenerCallQueue.Event<Service.Listener> terminatedEvent(final Service.State from) {
        return new ListenerCallQueue.Event<Service.Listener>() { // from class: com.google.common.util.concurrent.AbstractService.3
            public void call(Service.Listener listener) {
                listener.terminated(Service.State.this);
            }

            public String toString() {
                return "terminated({from = " + Service.State.this + "})";
            }
        };
    }

    private static ListenerCallQueue.Event<Service.Listener> stoppingEvent(final Service.State from) {
        return new ListenerCallQueue.Event<Service.Listener>() { // from class: com.google.common.util.concurrent.AbstractService.4
            public void call(Service.Listener listener) {
                listener.stopping(Service.State.this);
            }

            public String toString() {
                return "stopping({from = " + Service.State.this + "})";
            }
        };
    }

    /* loaded from: classes3.dex */
    private final class IsStartableGuard extends Monitor.Guard {
        IsStartableGuard() {
            super(AbstractService.this.monitor);
        }

        @Override // com.google.common.util.concurrent.Monitor.Guard
        public boolean isSatisfied() {
            return AbstractService.this.state() == Service.State.NEW;
        }
    }

    /* loaded from: classes3.dex */
    private final class IsStoppableGuard extends Monitor.Guard {
        IsStoppableGuard() {
            super(AbstractService.this.monitor);
        }

        @Override // com.google.common.util.concurrent.Monitor.Guard
        public boolean isSatisfied() {
            return AbstractService.this.state().compareTo(Service.State.RUNNING) <= 0;
        }
    }

    /* loaded from: classes3.dex */
    private final class HasReachedRunningGuard extends Monitor.Guard {
        HasReachedRunningGuard() {
            super(AbstractService.this.monitor);
        }

        @Override // com.google.common.util.concurrent.Monitor.Guard
        public boolean isSatisfied() {
            return AbstractService.this.state().compareTo(Service.State.RUNNING) >= 0;
        }
    }

    /* loaded from: classes3.dex */
    private final class IsStoppedGuard extends Monitor.Guard {
        IsStoppedGuard() {
            super(AbstractService.this.monitor);
        }

        @Override // com.google.common.util.concurrent.Monitor.Guard
        public boolean isSatisfied() {
            return AbstractService.this.state().isTerminal();
        }
    }

    protected void doCancelStart() {
    }

    @Override // com.google.common.util.concurrent.Service
    public final Service startAsync() {
        if (this.monitor.enterIf(this.isStartable)) {
            try {
                this.snapshot = new StateSnapshot(Service.State.STARTING);
                enqueueStartingEvent();
                doStart();
            } finally {
                try {
                    return this;
                } finally {
                }
            }
            return this;
        }
        throw new IllegalStateException("Service " + this + " has already been started");
    }

    @Override // com.google.common.util.concurrent.Service
    public final Service stopAsync() {
        if (this.monitor.enterIf(this.isStoppable)) {
            try {
                Service.State previous = state();
                switch (previous) {
                    case NEW:
                        this.snapshot = new StateSnapshot(Service.State.TERMINATED);
                        enqueueTerminatedEvent(Service.State.NEW);
                        break;
                    case STARTING:
                        this.snapshot = new StateSnapshot(Service.State.STARTING, true, null);
                        enqueueStoppingEvent(Service.State.STARTING);
                        doCancelStart();
                        break;
                    case RUNNING:
                        this.snapshot = new StateSnapshot(Service.State.STOPPING);
                        enqueueStoppingEvent(Service.State.RUNNING);
                        doStop();
                        break;
                    case STOPPING:
                    case TERMINATED:
                    case FAILED:
                        throw new AssertionError("isStoppable is incorrectly implemented, saw: " + previous);
                }
            } finally {
                try {
                } finally {
                }
            }
        }
        return this;
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitRunning() {
        this.monitor.enterWhenUninterruptibly(this.hasReachedRunning);
        try {
            checkCurrentState(Service.State.RUNNING);
        } finally {
            this.monitor.leave();
        }
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
        if (this.monitor.enterWhenUninterruptibly(this.hasReachedRunning, timeout, unit)) {
            try {
                checkCurrentState(Service.State.RUNNING);
            } finally {
                this.monitor.leave();
            }
        } else {
            throw new TimeoutException("Timed out waiting for " + this + " to reach the RUNNING state.");
        }
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitTerminated() {
        this.monitor.enterWhenUninterruptibly(this.isStopped);
        try {
            checkCurrentState(Service.State.TERMINATED);
        } finally {
            this.monitor.leave();
        }
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
        if (this.monitor.enterWhenUninterruptibly(this.isStopped, timeout, unit)) {
            try {
                checkCurrentState(Service.State.TERMINATED);
            } finally {
                this.monitor.leave();
            }
        } else {
            throw new TimeoutException("Timed out waiting for " + this + " to reach a terminal state. Current state: " + state());
        }
    }

    private void checkCurrentState(Service.State expected) {
        Service.State actual = state();
        if (actual == expected) {
            return;
        }
        if (actual == Service.State.FAILED) {
            throw new IllegalStateException("Expected the service " + this + " to be " + expected + ", but the service has FAILED", failureCause());
        }
        throw new IllegalStateException("Expected the service " + this + " to be " + expected + ", but was " + actual);
    }

    protected final void notifyStarted() {
        this.monitor.enter();
        try {
            if (this.snapshot.state == Service.State.STARTING) {
                if (this.snapshot.shutdownWhenStartupFinishes) {
                    this.snapshot = new StateSnapshot(Service.State.STOPPING);
                    doStop();
                } else {
                    this.snapshot = new StateSnapshot(Service.State.RUNNING);
                    enqueueRunningEvent();
                }
                return;
            }
            IllegalStateException failure = new IllegalStateException("Cannot notifyStarted() when the service is " + this.snapshot.state);
            notifyFailed(failure);
            throw failure;
        } finally {
            this.monitor.leave();
            dispatchListenerEvents();
        }
    }

    protected final void notifyStopped() {
        this.monitor.enter();
        try {
            Service.State previous = state();
            switch (previous) {
                case NEW:
                case TERMINATED:
                case FAILED:
                    throw new IllegalStateException("Cannot notifyStopped() when the service is " + previous);
                case STARTING:
                case RUNNING:
                case STOPPING:
                    this.snapshot = new StateSnapshot(Service.State.TERMINATED);
                    enqueueTerminatedEvent(previous);
                    break;
            }
        } finally {
            this.monitor.leave();
            dispatchListenerEvents();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void notifyFailed(Throwable cause) {
        Preconditions.checkNotNull(cause);
        this.monitor.enter();
        try {
            Service.State previous = state();
            int i = AnonymousClass6.$SwitchMap$com$google$common$util$concurrent$Service$State[previous.ordinal()];
            if (i != 1) {
                if (i == 2 || i == 3 || i == 4) {
                    this.snapshot = new StateSnapshot(Service.State.FAILED, false, cause);
                    enqueueFailedEvent(previous, cause);
                } else if (i != 5) {
                }
                return;
            }
            throw new IllegalStateException("Failed while in state:" + previous, cause);
        } finally {
            this.monitor.leave();
            dispatchListenerEvents();
        }
    }

    @Override // com.google.common.util.concurrent.Service
    public final boolean isRunning() {
        return state() == Service.State.RUNNING;
    }

    @Override // com.google.common.util.concurrent.Service
    public final Service.State state() {
        return this.snapshot.externalState();
    }

    @Override // com.google.common.util.concurrent.Service
    public final Throwable failureCause() {
        return this.snapshot.failureCause();
    }

    @Override // com.google.common.util.concurrent.Service
    public final void addListener(Service.Listener listener, Executor executor) {
        this.listeners.addListener(listener, executor);
    }

    public String toString() {
        return getClass().getSimpleName() + " [" + state() + "]";
    }

    private void dispatchListenerEvents() {
        if (!this.monitor.isOccupiedByCurrentThread()) {
            this.listeners.dispatch();
        }
    }

    private void enqueueStartingEvent() {
        this.listeners.enqueue(STARTING_EVENT);
    }

    private void enqueueRunningEvent() {
        this.listeners.enqueue(RUNNING_EVENT);
    }

    private void enqueueStoppingEvent(Service.State from) {
        if (from == Service.State.STARTING) {
            this.listeners.enqueue(STOPPING_FROM_STARTING_EVENT);
        } else if (from == Service.State.RUNNING) {
            this.listeners.enqueue(STOPPING_FROM_RUNNING_EVENT);
        } else {
            throw new AssertionError();
        }
    }

    private void enqueueTerminatedEvent(Service.State from) {
        switch (from) {
            case NEW:
                this.listeners.enqueue(TERMINATED_FROM_NEW_EVENT);
                return;
            case STARTING:
                this.listeners.enqueue(TERMINATED_FROM_STARTING_EVENT);
                return;
            case RUNNING:
                this.listeners.enqueue(TERMINATED_FROM_RUNNING_EVENT);
                return;
            case STOPPING:
                this.listeners.enqueue(TERMINATED_FROM_STOPPING_EVENT);
                return;
            case TERMINATED:
            case FAILED:
                throw new AssertionError();
            default:
                return;
        }
    }

    private void enqueueFailedEvent(final Service.State from, final Throwable cause) {
        this.listeners.enqueue(new ListenerCallQueue.Event<Service.Listener>() { // from class: com.google.common.util.concurrent.AbstractService.5
            public void call(Service.Listener listener) {
                listener.failed(from, cause);
            }

            public String toString() {
                return "failed({from = " + from + ", cause = " + cause + "})";
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class StateSnapshot {
        @NullableDecl
        final Throwable failure;
        final boolean shutdownWhenStartupFinishes;
        final Service.State state;

        StateSnapshot(Service.State internalState) {
            this(internalState, false, null);
        }

        StateSnapshot(Service.State internalState, boolean shutdownWhenStartupFinishes, @NullableDecl Throwable failure) {
            boolean z = false;
            Preconditions.checkArgument(!shutdownWhenStartupFinishes || internalState == Service.State.STARTING, "shutdownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", internalState);
            Preconditions.checkArgument(!((internalState == Service.State.FAILED ? true : z) ^ (failure != null)), "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", internalState, failure);
            this.state = internalState;
            this.shutdownWhenStartupFinishes = shutdownWhenStartupFinishes;
            this.failure = failure;
        }

        Service.State externalState() {
            if (!this.shutdownWhenStartupFinishes || this.state != Service.State.STARTING) {
                return this.state;
            }
            return Service.State.STOPPING;
        }

        Throwable failureCause() {
            Preconditions.checkState(this.state == Service.State.FAILED, "failureCause() is only valid if the service has failed, service is %s", this.state);
            return this.failure;
        }
    }
}
