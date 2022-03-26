package com.facebook.imagepipeline.core;

import android.os.Process;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public class PriorityThreadFactory implements ThreadFactory {
    private final boolean mAddThreadNumber;
    private final String mPrefix;
    private final AtomicInteger mThreadNumber;
    private final int mThreadPriority;

    public PriorityThreadFactory(int threadPriority) {
        this(threadPriority, "PriorityThreadFactory", true);
    }

    public PriorityThreadFactory(int threadPriority, String prefix, boolean addThreadNumber) {
        this.mThreadNumber = new AtomicInteger(1);
        this.mThreadPriority = threadPriority;
        this.mPrefix = prefix;
        this.mAddThreadNumber = addThreadNumber;
    }

    @Override // java.util.concurrent.ThreadFactory
    public Thread newThread(final Runnable runnable) {
        String name;
        Runnable wrapperRunnable = new Runnable() { // from class: com.facebook.imagepipeline.core.PriorityThreadFactory.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    Process.setThreadPriority(PriorityThreadFactory.this.mThreadPriority);
                } catch (Throwable th) {
                }
                runnable.run();
            }
        };
        if (this.mAddThreadNumber) {
            name = this.mPrefix + "-" + this.mThreadNumber.getAndIncrement();
        } else {
            name = this.mPrefix;
        }
        return new Thread(wrapperRunnable, name);
    }
}
