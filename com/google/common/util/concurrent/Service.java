package com.google.common.util.concurrent;

import com.google.errorprone.annotations.DoNotMock;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
@DoNotMock("Create an AbstractIdleService")
/* loaded from: classes3.dex */
public interface Service {

    /* loaded from: classes3.dex */
    public enum State {
        NEW {
            @Override // com.google.common.util.concurrent.Service.State
            boolean isTerminal() {
                return false;
            }
        },
        STARTING {
            @Override // com.google.common.util.concurrent.Service.State
            boolean isTerminal() {
                return false;
            }
        },
        RUNNING {
            @Override // com.google.common.util.concurrent.Service.State
            boolean isTerminal() {
                return false;
            }
        },
        STOPPING {
            @Override // com.google.common.util.concurrent.Service.State
            boolean isTerminal() {
                return false;
            }
        },
        TERMINATED {
            @Override // com.google.common.util.concurrent.Service.State
            boolean isTerminal() {
                return true;
            }
        },
        FAILED {
            @Override // com.google.common.util.concurrent.Service.State
            boolean isTerminal() {
                return true;
            }
        };

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract boolean isTerminal();
    }

    void addListener(Listener listener, Executor executor);

    void awaitRunning();

    void awaitRunning(long j, TimeUnit timeUnit) throws TimeoutException;

    void awaitTerminated();

    void awaitTerminated(long j, TimeUnit timeUnit) throws TimeoutException;

    Throwable failureCause();

    boolean isRunning();

    Service startAsync();

    State state();

    Service stopAsync();

    /* loaded from: classes3.dex */
    public static abstract class Listener {
        public void starting() {
        }

        public void running() {
        }

        public void stopping(State from) {
        }

        public void terminated(State from) {
        }

        public void failed(State from, Throwable failure) {
        }
    }
}
