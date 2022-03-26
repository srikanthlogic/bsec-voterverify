package com.facebook.imagepipeline.producers;

import android.os.SystemClock;
import com.facebook.imagepipeline.image.EncodedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public class JobScheduler {
    static final String QUEUE_TIME_KEY = "queueTime";
    private final Executor mExecutor;
    private final JobRunnable mJobRunnable;
    private final int mMinimumJobIntervalMs;
    private final Runnable mDoJobRunnable = new Runnable() { // from class: com.facebook.imagepipeline.producers.JobScheduler.1
        @Override // java.lang.Runnable
        public void run() {
            JobScheduler.this.doJob();
        }
    };
    private final Runnable mSubmitJobRunnable = new Runnable() { // from class: com.facebook.imagepipeline.producers.JobScheduler.2
        @Override // java.lang.Runnable
        public void run() {
            JobScheduler.this.submitJob();
        }
    };
    EncodedImage mEncodedImage = null;
    int mStatus = 0;
    JobState mJobState = JobState.IDLE;
    long mJobSubmitTime = 0;
    long mJobStartTime = 0;

    /* loaded from: classes.dex */
    public interface JobRunnable {
        void run(EncodedImage encodedImage, int i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum JobState {
        IDLE,
        QUEUED,
        RUNNING,
        RUNNING_AND_PENDING
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class JobStartExecutorSupplier {
        private static ScheduledExecutorService sJobStarterExecutor;

        JobStartExecutorSupplier() {
        }

        static ScheduledExecutorService get() {
            if (sJobStarterExecutor == null) {
                sJobStarterExecutor = Executors.newSingleThreadScheduledExecutor();
            }
            return sJobStarterExecutor;
        }
    }

    public JobScheduler(Executor executor, JobRunnable jobRunnable, int minimumJobIntervalMs) {
        this.mExecutor = executor;
        this.mJobRunnable = jobRunnable;
        this.mMinimumJobIntervalMs = minimumJobIntervalMs;
    }

    public void clearJob() {
        EncodedImage oldEncodedImage;
        synchronized (this) {
            oldEncodedImage = this.mEncodedImage;
            this.mEncodedImage = null;
            this.mStatus = 0;
        }
        EncodedImage.closeSafely(oldEncodedImage);
    }

    public boolean updateJob(EncodedImage encodedImage, int status) {
        EncodedImage oldEncodedImage;
        if (!shouldProcess(encodedImage, status)) {
            return false;
        }
        synchronized (this) {
            oldEncodedImage = this.mEncodedImage;
            this.mEncodedImage = EncodedImage.cloneOrNull(encodedImage);
            this.mStatus = status;
        }
        EncodedImage.closeSafely(oldEncodedImage);
        return true;
    }

    public boolean scheduleJob() {
        long now = SystemClock.uptimeMillis();
        long when = 0;
        boolean shouldEnqueue = false;
        synchronized (this) {
            if (!shouldProcess(this.mEncodedImage, this.mStatus)) {
                return false;
            }
            int i = AnonymousClass3.$SwitchMap$com$facebook$imagepipeline$producers$JobScheduler$JobState[this.mJobState.ordinal()];
            if (i == 1) {
                when = Math.max(this.mJobStartTime + ((long) this.mMinimumJobIntervalMs), now);
                shouldEnqueue = true;
                this.mJobSubmitTime = now;
                this.mJobState = JobState.QUEUED;
            } else if (i != 2 && i == 3) {
                this.mJobState = JobState.RUNNING_AND_PENDING;
            }
            if (shouldEnqueue) {
                enqueueJob(when - now);
            }
            return true;
        }
    }

    /* renamed from: com.facebook.imagepipeline.producers.JobScheduler$3  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$facebook$imagepipeline$producers$JobScheduler$JobState = new int[JobState.values().length];

        static {
            try {
                $SwitchMap$com$facebook$imagepipeline$producers$JobScheduler$JobState[JobState.IDLE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$facebook$imagepipeline$producers$JobScheduler$JobState[JobState.QUEUED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$facebook$imagepipeline$producers$JobScheduler$JobState[JobState.RUNNING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$facebook$imagepipeline$producers$JobScheduler$JobState[JobState.RUNNING_AND_PENDING.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    private void enqueueJob(long delay) {
        if (delay > 0) {
            JobStartExecutorSupplier.get().schedule(this.mSubmitJobRunnable, delay, TimeUnit.MILLISECONDS);
        } else {
            this.mSubmitJobRunnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void submitJob() {
        this.mExecutor.execute(this.mDoJobRunnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doJob() {
        EncodedImage input;
        int status;
        long now = SystemClock.uptimeMillis();
        synchronized (this) {
            input = this.mEncodedImage;
            status = this.mStatus;
            this.mEncodedImage = null;
            this.mStatus = 0;
            this.mJobState = JobState.RUNNING;
            this.mJobStartTime = now;
        }
        try {
            if (shouldProcess(input, status)) {
                this.mJobRunnable.run(input, status);
            }
        } finally {
            EncodedImage.closeSafely(input);
            onJobFinished();
        }
    }

    private void onJobFinished() {
        long now = SystemClock.uptimeMillis();
        long when = 0;
        boolean shouldEnqueue = false;
        synchronized (this) {
            if (this.mJobState == JobState.RUNNING_AND_PENDING) {
                when = Math.max(this.mJobStartTime + ((long) this.mMinimumJobIntervalMs), now);
                shouldEnqueue = true;
                this.mJobSubmitTime = now;
                this.mJobState = JobState.QUEUED;
            } else {
                this.mJobState = JobState.IDLE;
            }
        }
        if (shouldEnqueue) {
            enqueueJob(when - now);
        }
    }

    private static boolean shouldProcess(EncodedImage encodedImage, int status) {
        return BaseConsumer.isLast(status) || BaseConsumer.statusHasFlag(status, 4) || EncodedImage.isValid(encodedImage);
    }

    public synchronized long getQueuedTime() {
        return this.mJobStartTime - this.mJobSubmitTime;
    }
}
