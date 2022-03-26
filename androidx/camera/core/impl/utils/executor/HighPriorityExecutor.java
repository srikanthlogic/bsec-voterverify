package androidx.camera.core.impl.utils.executor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
/* loaded from: classes.dex */
final class HighPriorityExecutor implements Executor {
    private static volatile Executor sExecutor;
    private final ExecutorService mHighPriorityService = Executors.newSingleThreadExecutor(new ThreadFactory() { // from class: androidx.camera.core.impl.utils.executor.HighPriorityExecutor.1
        private static final String THREAD_NAME = "CameraX-camerax_high_priority";

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setPriority(10);
            t.setName(THREAD_NAME);
            return t;
        }
    });

    HighPriorityExecutor() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Executor getInstance() {
        if (sExecutor != null) {
            return sExecutor;
        }
        synchronized (HighPriorityExecutor.class) {
            if (sExecutor == null) {
                sExecutor = new HighPriorityExecutor();
            }
        }
        return sExecutor;
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable command) {
        this.mHighPriorityService.execute(command);
    }
}
