package com.facebook.datasource;

import android.util.Pair;
import com.facebook.common.internal.Preconditions;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public abstract class AbstractDataSource<T> implements DataSource<T> {
    @Nullable
    private T mResult = null;
    private Throwable mFailureThrowable = null;
    private float mProgress = 0.0f;
    private boolean mIsClosed = false;
    private DataSourceStatus mDataSourceStatus = DataSourceStatus.IN_PROGRESS;
    private final ConcurrentLinkedQueue<Pair<DataSubscriber<T>, Executor>> mSubscribers = new ConcurrentLinkedQueue<>();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum DataSourceStatus {
        IN_PROGRESS,
        SUCCESS,
        FAILURE
    }

    @Override // com.facebook.datasource.DataSource
    public synchronized boolean isClosed() {
        return this.mIsClosed;
    }

    @Override // com.facebook.datasource.DataSource
    public synchronized boolean isFinished() {
        return this.mDataSourceStatus != DataSourceStatus.IN_PROGRESS;
    }

    @Override // com.facebook.datasource.DataSource
    public synchronized boolean hasResult() {
        return this.mResult != null;
    }

    @Override // com.facebook.datasource.DataSource
    @Nullable
    public synchronized T getResult() {
        return this.mResult;
    }

    @Override // com.facebook.datasource.DataSource
    public synchronized boolean hasFailed() {
        return this.mDataSourceStatus == DataSourceStatus.FAILURE;
    }

    @Override // com.facebook.datasource.DataSource
    @Nullable
    public synchronized Throwable getFailureCause() {
        return this.mFailureThrowable;
    }

    @Override // com.facebook.datasource.DataSource
    public synchronized float getProgress() {
        return this.mProgress;
    }

    @Override // com.facebook.datasource.DataSource
    public boolean close() {
        synchronized (this) {
            if (this.mIsClosed) {
                return false;
            }
            this.mIsClosed = true;
            T resultToClose = this.mResult;
            this.mResult = null;
            if (resultToClose != null) {
                closeResult(resultToClose);
            }
            if (!isFinished()) {
                notifyDataSubscribers();
            }
            synchronized (this) {
                this.mSubscribers.clear();
            }
            return true;
        }
    }

    protected void closeResult(@Nullable T result) {
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0035  */
    /* JADX WARN: Removed duplicated region for block: B:28:? A[RETURN, SYNTHETIC] */
    @Override // com.facebook.datasource.DataSource
    /* Code decompiled incorrectly, please refer to instructions dump */
    public void subscribe(DataSubscriber<T> dataSubscriber, Executor executor) {
        boolean shouldNotify;
        Preconditions.checkNotNull(dataSubscriber);
        Preconditions.checkNotNull(executor);
        synchronized (this) {
            if (!this.mIsClosed) {
                if (this.mDataSourceStatus == DataSourceStatus.IN_PROGRESS) {
                    this.mSubscribers.add(Pair.create(dataSubscriber, executor));
                }
                if (!hasResult() && !isFinished() && !wasCancelled()) {
                    shouldNotify = false;
                    if (!shouldNotify) {
                        notifyDataSubscriber(dataSubscriber, executor, hasFailed(), wasCancelled());
                        return;
                    }
                    return;
                }
                shouldNotify = true;
                if (!shouldNotify) {
                }
            }
        }
    }

    private void notifyDataSubscribers() {
        boolean isFailure = hasFailed();
        boolean isCancellation = wasCancelled();
        Iterator<Pair<DataSubscriber<T>, Executor>> it = this.mSubscribers.iterator();
        while (it.hasNext()) {
            Pair<DataSubscriber<T>, Executor> pair = it.next();
            notifyDataSubscriber((DataSubscriber) pair.first, (Executor) pair.second, isFailure, isCancellation);
        }
    }

    private void notifyDataSubscriber(final DataSubscriber<T> dataSubscriber, Executor executor, final boolean isFailure, final boolean isCancellation) {
        executor.execute(new Runnable() { // from class: com.facebook.datasource.AbstractDataSource.1
            @Override // java.lang.Runnable
            public void run() {
                if (isFailure) {
                    dataSubscriber.onFailure(AbstractDataSource.this);
                } else if (isCancellation) {
                    dataSubscriber.onCancellation(AbstractDataSource.this);
                } else {
                    dataSubscriber.onNewResult(AbstractDataSource.this);
                }
            }
        });
    }

    private synchronized boolean wasCancelled() {
        boolean z;
        if (isClosed()) {
            if (!isFinished()) {
                z = true;
            }
        }
        z = false;
        return z;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean setResult(@Nullable T value, boolean isLast) {
        boolean result = setResultInternal(value, isLast);
        if (result) {
            notifyDataSubscribers();
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean setFailure(Throwable throwable) {
        boolean result = setFailureInternal(throwable);
        if (result) {
            notifyDataSubscribers();
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean setProgress(float progress) {
        boolean result = setProgressInternal(progress);
        if (result) {
            notifyProgressUpdate();
        }
        return result;
    }

    private boolean setResultInternal(@Nullable T value, boolean isLast) {
        T resultToClose = null;
        try {
            synchronized (this) {
                if (!this.mIsClosed && this.mDataSourceStatus == DataSourceStatus.IN_PROGRESS) {
                    if (isLast) {
                        this.mDataSourceStatus = DataSourceStatus.SUCCESS;
                        this.mProgress = 1.0f;
                    }
                    if (this.mResult != value) {
                        resultToClose = this.mResult;
                        this.mResult = value;
                    }
                    return true;
                }
                if (value != null) {
                    closeResult(value);
                }
                return false;
            }
        } finally {
            if (0 != 0) {
                closeResult(null);
            }
        }
    }

    private synchronized boolean setFailureInternal(Throwable throwable) {
        if (!this.mIsClosed && this.mDataSourceStatus == DataSourceStatus.IN_PROGRESS) {
            this.mDataSourceStatus = DataSourceStatus.FAILURE;
            this.mFailureThrowable = throwable;
            return true;
        }
        return false;
    }

    private synchronized boolean setProgressInternal(float progress) {
        if (!this.mIsClosed && this.mDataSourceStatus == DataSourceStatus.IN_PROGRESS) {
            if (progress < this.mProgress) {
                return false;
            }
            this.mProgress = progress;
            return true;
        }
        return false;
    }

    protected void notifyProgressUpdate() {
        Iterator<Pair<DataSubscriber<T>, Executor>> it = this.mSubscribers.iterator();
        while (it.hasNext()) {
            Pair<DataSubscriber<T>, Executor> pair = it.next();
            final DataSubscriber<T> subscriber = (DataSubscriber) pair.first;
            ((Executor) pair.second).execute(new Runnable() { // from class: com.facebook.datasource.AbstractDataSource.2
                @Override // java.lang.Runnable
                public void run() {
                    subscriber.onProgressUpdate(AbstractDataSource.this);
                }
            });
        }
    }

    @Override // com.facebook.datasource.DataSource
    public boolean hasMultipleResults() {
        return false;
    }
}
