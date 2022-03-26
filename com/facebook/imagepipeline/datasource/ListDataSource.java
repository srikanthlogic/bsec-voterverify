package com.facebook.imagepipeline.datasource;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.AbstractDataSource;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class ListDataSource<T> extends AbstractDataSource<List<CloseableReference<T>>> {
    private final DataSource<CloseableReference<T>>[] mDataSources;
    private int mFinishedDataSources = 0;

    protected ListDataSource(DataSource<CloseableReference<T>>[] dataSources) {
        this.mDataSources = dataSources;
    }

    public static <T> ListDataSource<T> create(DataSource<CloseableReference<T>>... dataSources) {
        Preconditions.checkNotNull(dataSources);
        Preconditions.checkState(dataSources.length > 0);
        ListDataSource<T> listDataSource = new ListDataSource<>(dataSources);
        for (DataSource<CloseableReference<T>> dataSource : dataSources) {
            if (dataSource != null) {
                listDataSource.getClass();
                dataSource.subscribe(new InternalDataSubscriber(), CallerThreadExecutor.getInstance());
            }
        }
        return listDataSource;
    }

    @Override // com.facebook.datasource.AbstractDataSource, com.facebook.datasource.DataSource
    @Nullable
    public synchronized List<CloseableReference<T>> getResult() {
        if (!hasResult()) {
            return null;
        }
        List<CloseableReference<T>> results = new ArrayList<>(this.mDataSources.length);
        for (DataSource<CloseableReference<T>> dataSource : this.mDataSources) {
            results.add(dataSource.getResult());
        }
        return results;
    }

    @Override // com.facebook.datasource.AbstractDataSource, com.facebook.datasource.DataSource
    public synchronized boolean hasResult() {
        boolean z;
        if (!isClosed()) {
            if (this.mFinishedDataSources == this.mDataSources.length) {
                z = true;
            }
        }
        z = false;
        return z;
    }

    @Override // com.facebook.datasource.AbstractDataSource, com.facebook.datasource.DataSource
    public boolean close() {
        if (!super.close()) {
            return false;
        }
        for (DataSource<?> dataSource : this.mDataSources) {
            dataSource.close();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDataSourceFinished() {
        if (increaseAndCheckIfLast()) {
            setResult(null, true);
        }
    }

    private synchronized boolean increaseAndCheckIfLast() {
        boolean z;
        z = true;
        int i = this.mFinishedDataSources + 1;
        this.mFinishedDataSources = i;
        if (i != this.mDataSources.length) {
            z = false;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDataSourceFailed(DataSource<CloseableReference<T>> dataSource) {
        setFailure(dataSource.getFailureCause());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDataSourceCancelled() {
        setFailure(new CancellationException());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDataSourceProgress() {
        float progress = 0.0f;
        for (DataSource<?> dataSource : this.mDataSources) {
            progress += dataSource.getProgress();
        }
        setProgress(progress / ((float) this.mDataSources.length));
    }

    /* loaded from: classes.dex */
    private class InternalDataSubscriber implements DataSubscriber<CloseableReference<T>> {
        boolean mFinished;

        private InternalDataSubscriber() {
            this.mFinished = false;
        }

        private synchronized boolean tryFinish() {
            if (this.mFinished) {
                return false;
            }
            this.mFinished = true;
            return true;
        }

        @Override // com.facebook.datasource.DataSubscriber
        public void onFailure(DataSource<CloseableReference<T>> dataSource) {
            ListDataSource.this.onDataSourceFailed(dataSource);
        }

        @Override // com.facebook.datasource.DataSubscriber
        public void onCancellation(DataSource<CloseableReference<T>> dataSource) {
            ListDataSource.this.onDataSourceCancelled();
        }

        @Override // com.facebook.datasource.DataSubscriber
        public void onNewResult(DataSource<CloseableReference<T>> dataSource) {
            if (dataSource.isFinished() && tryFinish()) {
                ListDataSource.this.onDataSourceFinished();
            }
        }

        @Override // com.facebook.datasource.DataSubscriber
        public void onProgressUpdate(DataSource<CloseableReference<T>> dataSource) {
            ListDataSource.this.onDataSourceProgress();
        }
    }
}
