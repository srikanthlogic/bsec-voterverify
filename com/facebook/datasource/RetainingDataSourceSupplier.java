package com.facebook.datasource;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.internal.Supplier;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class RetainingDataSourceSupplier<T> implements Supplier<DataSource<T>> {
    private final Set<RetainingDataSource> mDataSources = Collections.newSetFromMap(new WeakHashMap());
    @Nullable
    private Supplier<DataSource<T>> mCurrentDataSourceSupplier = null;

    @Override // com.facebook.common.internal.Supplier
    public DataSource<T> get() {
        RetainingDataSource dataSource = new RetainingDataSource();
        dataSource.setSupplier(this.mCurrentDataSourceSupplier);
        this.mDataSources.add(dataSource);
        return dataSource;
    }

    public void replaceSupplier(Supplier<DataSource<T>> supplier) {
        this.mCurrentDataSourceSupplier = supplier;
        for (RetainingDataSource dataSource : this.mDataSources) {
            if (!dataSource.isClosed()) {
                dataSource.setSupplier(supplier);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class RetainingDataSource<T> extends AbstractDataSource<T> {
        @Nullable
        private DataSource<T> mDataSource;

        private RetainingDataSource() {
            this.mDataSource = null;
        }

        public void setSupplier(@Nullable Supplier<DataSource<T>> supplier) {
            if (!isClosed()) {
                DataSource<T> newDataSource = supplier != null ? supplier.get() : null;
                synchronized (this) {
                    if (isClosed()) {
                        closeSafely(newDataSource);
                        return;
                    }
                    DataSource<T> oldDataSource = this.mDataSource;
                    this.mDataSource = newDataSource;
                    if (newDataSource != null) {
                        newDataSource.subscribe(new InternalDataSubscriber(), CallerThreadExecutor.getInstance());
                    }
                    closeSafely(oldDataSource);
                }
            }
        }

        @Override // com.facebook.datasource.AbstractDataSource, com.facebook.datasource.DataSource
        @Nullable
        public synchronized T getResult() {
            return this.mDataSource != null ? this.mDataSource.getResult() : null;
        }

        @Override // com.facebook.datasource.AbstractDataSource, com.facebook.datasource.DataSource
        public synchronized boolean hasResult() {
            boolean z;
            if (this.mDataSource != null) {
                if (this.mDataSource.hasResult()) {
                    z = true;
                }
            }
            z = false;
            return z;
        }

        @Override // com.facebook.datasource.AbstractDataSource, com.facebook.datasource.DataSource
        public boolean close() {
            synchronized (this) {
                if (!super.close()) {
                    return false;
                }
                DataSource<T> dataSource = this.mDataSource;
                this.mDataSource = null;
                closeSafely(dataSource);
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onDataSourceNewResult(DataSource<T> dataSource) {
            if (dataSource == this.mDataSource) {
                setResult(null, false);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onDataSourceFailed(DataSource<T> dataSource) {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onDatasourceProgress(DataSource<T> dataSource) {
            if (dataSource == this.mDataSource) {
                setProgress(dataSource.getProgress());
            }
        }

        private static <T> void closeSafely(DataSource<T> dataSource) {
            if (dataSource != null) {
                dataSource.close();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public class InternalDataSubscriber implements DataSubscriber<T> {
            private InternalDataSubscriber() {
            }

            @Override // com.facebook.datasource.DataSubscriber
            public void onNewResult(DataSource<T> dataSource) {
                if (dataSource.hasResult()) {
                    RetainingDataSource.this.onDataSourceNewResult(dataSource);
                } else if (dataSource.isFinished()) {
                    RetainingDataSource.this.onDataSourceFailed(dataSource);
                }
            }

            @Override // com.facebook.datasource.DataSubscriber
            public void onFailure(DataSource<T> dataSource) {
                RetainingDataSource.this.onDataSourceFailed(dataSource);
            }

            @Override // com.facebook.datasource.DataSubscriber
            public void onCancellation(DataSource<T> dataSource) {
            }

            @Override // com.facebook.datasource.DataSubscriber
            public void onProgressUpdate(DataSource<T> dataSource) {
                RetainingDataSource.this.onDatasourceProgress(dataSource);
            }
        }

        @Override // com.facebook.datasource.AbstractDataSource, com.facebook.datasource.DataSource
        public boolean hasMultipleResults() {
            return true;
        }
    }
}
