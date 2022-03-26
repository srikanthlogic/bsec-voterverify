package com.google.common.util.concurrent;

import java.util.concurrent.Executor;
/* loaded from: classes3.dex */
enum DirectExecutor implements Executor {
    INSTANCE;

    @Override // java.util.concurrent.Executor
    public void execute(Runnable command) {
        command.run();
    }

    @Override // java.lang.Enum, java.lang.Object
    public String toString() {
        return "MoreExecutors.directExecutor()";
    }
}
