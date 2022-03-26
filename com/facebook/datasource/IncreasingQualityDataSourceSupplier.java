package com.facebook.datasource;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.internal.Objects;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Supplier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class IncreasingQualityDataSourceSupplier<T> implements Supplier<DataSource<T>> {
    private final boolean mDataSourceLazy;
    private final List<Supplier<DataSource<T>>> mDataSourceSuppliers;

    private IncreasingQualityDataSourceSupplier(List<Supplier<DataSource<T>>> dataSourceSuppliers, boolean dataSourceLazy) {
        Preconditions.checkArgument(!dataSourceSuppliers.isEmpty(), "List of suppliers is empty!");
        this.mDataSourceSuppliers = dataSourceSuppliers;
        this.mDataSourceLazy = dataSourceLazy;
    }

    public static <T> IncreasingQualityDataSourceSupplier<T> create(List<Supplier<DataSource<T>>> dataSourceSuppliers) {
        return create(dataSourceSuppliers, false);
    }

    public static <T> IncreasingQualityDataSourceSupplier<T> create(List<Supplier<DataSource<T>>> dataSourceSuppliers, boolean dataSourceLazy) {
        return new IncreasingQualityDataSourceSupplier<>(dataSourceSuppliers, dataSourceLazy);
    }

    @Override // com.facebook.common.internal.Supplier
    public DataSource<T> get() {
        return new IncreasingQualityDataSource();
    }

    public int hashCode() {
        return this.mDataSourceSuppliers.hashCode();
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof IncreasingQualityDataSourceSupplier)) {
            return false;
        }
        return Objects.equal(this.mDataSourceSuppliers, ((IncreasingQualityDataSourceSupplier) other).mDataSourceSuppliers);
    }

    public String toString() {
        return Objects.toStringHelper(this).add("list", this.mDataSourceSuppliers).toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class IncreasingQualityDataSource extends AbstractDataSource<T> {
        @Nullable
        private ArrayList<DataSource<T>> mDataSources;
        @Nullable
        private Throwable mDelayedError;
        private AtomicInteger mFinishedDataSources;
        private int mIndexOfDataSourceWithResult;
        private int mNumberOfDataSources;

        public IncreasingQualityDataSource() {
            if (!IncreasingQualityDataSourceSupplier.this.mDataSourceLazy) {
                ensureDataSourceInitialized();
            }
        }

        private void ensureDataSourceInitialized() {
            if (this.mFinishedDataSources == null) {
                synchronized (this) {
                    if (this.mFinishedDataSources == null) {
                        this.mFinishedDataSources = new AtomicInteger(0);
                        int n = IncreasingQualityDataSourceSupplier.this.mDataSourceSuppliers.size();
                        this.mNumberOfDataSources = n;
                        this.mIndexOfDataSourceWithResult = n;
                        this.mDataSources = new ArrayList<>(n);
                        for (int i = 0; i < n; i++) {
                            DataSource<T> dataSource = (DataSource) ((Supplier) IncreasingQualityDataSourceSupplier.this.mDataSourceSuppliers.get(i)).get();
                            this.mDataSources.add(dataSource);
                            dataSource.subscribe(new InternalDataSubscriber(i), CallerThreadExecutor.getInstance());
                            if (dataSource.hasResult()) {
                                break;
                            }
                        }
                    }
                }
            }
        }

        @Nullable
        private synchronized DataSource<T> getDataSource(int i) {
            return (this.mDataSources == null || i >= this.mDataSources.size()) ? null : this.mDataSources.get(i);
        }

        @Nullable
        private synchronized DataSource<T> getAndClearDataSource(int i) {
            DataSource<T> dataSource;
            dataSource = null;
            if (this.mDataSources != null && i < this.mDataSources.size()) {
                dataSource = this.mDataSources.set(i, null);
            }
            return dataSource;
        }

        @Nullable
        private synchronized DataSource<T> getDataSourceWithResult() {
            return getDataSource(this.mIndexOfDataSourceWithResult);
        }

        @Override // com.facebook.datasource.AbstractDataSource, com.facebook.datasource.DataSource
        @Nullable
        public synchronized T getResult() {
            DataSource<T> dataSourceWithResult;
            if (IncreasingQualityDataSourceSupplier.this.mDataSourceLazy) {
                ensureDataSourceInitialized();
            }
            dataSourceWithResult = getDataSourceWithResult();
            return dataSourceWithResult != null ? dataSourceWithResult.getResult() : null;
        }

        @Override // com.facebook.datasource.AbstractDataSource, com.facebook.datasource.DataSource
        public synchronized boolean hasResult() {
            boolean z;
            if (IncreasingQualityDataSourceSupplier.this.mDataSourceLazy) {
                ensureDataSourceInitialized();
            }
            DataSource<T> dataSourceWithResult = getDataSourceWithResult();
            if (dataSourceWithResult != null) {
                if (dataSourceWithResult.hasResult()) {
                    z = true;
                }
            }
            z = false;
            return z;
        }

        @Override // com.facebook.datasource.AbstractDataSource, com.facebook.datasource.DataSource
        public boolean close() {
            if (IncreasingQualityDataSourceSupplier.this.mDataSourceLazy) {
                ensureDataSourceInitialized();
            }
            synchronized (this) {
                if (!super.close()) {
                    return false;
                }
                ArrayList<DataSource<T>> dataSources = this.mDataSources;
                this.mDataSources = null;
                if (dataSources == null) {
                    return true;
                }
                for (int i = 0; i < dataSources.size(); i++) {
                    closeSafely(dataSources.get(i));
                }
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onDataSourceNewResult(int index, DataSource<T> dataSource) {
            maybeSetIndexOfDataSourceWithResult(index, dataSource, dataSource.isFinished());
            if (dataSource == getDataSourceWithResult()) {
                setResult(null, index == 0 && dataSource.isFinished());
            }
            maybeSetFailure();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onDataSourceFailed(int index, DataSource<T> dataSource) {
            closeSafely(tryGetAndClearDataSource(index, dataSource));
            if (index == 0) {
                this.mDelayedError = dataSource.getFailureCause();
            }
            maybeSetFailure();
        }

        private void maybeSetFailure() {
            Throwable th;
            if (this.mFinishedDataSources.incrementAndGet() == this.mNumberOfDataSources && (th = this.mDelayedError) != null) {
                setFailure(th);
            }
        }

        private void maybeSetIndexOfDataSourceWithResult(int index, DataSource<T> dataSource, boolean isFinished) {
            synchronized (this) {
                int oldIndexOfDataSourceWithResult = this.mIndexOfDataSourceWithResult;
                int newIndexOfDataSourceWithResult = this.mIndexOfDataSourceWithResult;
                if (dataSource == getDataSource(index) && index != this.mIndexOfDataSourceWithResult) {
                    if (getDataSourceWithResult() == null || (isFinished && index < this.mIndexOfDataSourceWithResult)) {
                        newIndexOfDataSourceWithResult = index;
                        this.mIndexOfDataSourceWithResult = index;
                    }
                    for (int i = oldIndexOfDataSourceWithResult; i > newIndexOfDataSourceWithResult; i--) {
                        closeSafely(getAndClearDataSource(i));
                    }
                }
            }
        }

        @Nullable
        private synchronized DataSource<T> tryGetAndClearDataSource(int i, DataSource<T> dataSource) {
            if (dataSource == getDataSourceWithResult()) {
                return null;
            }
            if (dataSource != getDataSource(i)) {
                return dataSource;
            }
            return getAndClearDataSource(i);
        }

        private void closeSafely(DataSource<T> dataSource) {
            if (dataSource != null) {
                dataSource.close();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public class InternalDataSubscriber implements DataSubscriber<T> {
            private int mIndex;

            public InternalDataSubscriber(int index) {
                this.mIndex = index;
            }

            @Override // com.facebook.datasource.DataSubscriber
            public void onNewResult(DataSource<T> dataSource) {
                if (dataSource.hasResult()) {
                    IncreasingQualityDataSource.this.onDataSourceNewResult(this.mIndex, dataSource);
                } else if (dataSource.isFinished()) {
                    IncreasingQualityDataSource.this.onDataSourceFailed(this.mIndex, dataSource);
                }
            }

            @Override // com.facebook.datasource.DataSubscriber
            public void onFailure(DataSource<T> dataSource) {
                IncreasingQualityDataSource.this.onDataSourceFailed(this.mIndex, dataSource);
            }

            @Override // com.facebook.datasource.DataSubscriber
            public void onCancellation(DataSource<T> dataSource) {
            }

            @Override // com.facebook.datasource.DataSubscriber
            public void onProgressUpdate(DataSource<T> dataSource) {
                if (this.mIndex == 0) {
                    IncreasingQualityDataSource.this.setProgress(dataSource.getProgress());
                }
            }
        }
    }
}
