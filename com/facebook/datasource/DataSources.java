package com.facebook.datasource;

import com.facebook.common.internal.Supplier;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class DataSources {
    private DataSources() {
    }

    public static <T> DataSource<T> immediateFailedDataSource(Throwable failure) {
        SimpleDataSource<T> simpleDataSource = SimpleDataSource.create();
        simpleDataSource.setFailure(failure);
        return simpleDataSource;
    }

    public static <T> DataSource<T> immediateDataSource(T result) {
        SimpleDataSource<T> simpleDataSource = SimpleDataSource.create();
        simpleDataSource.setResult(result);
        return simpleDataSource;
    }

    public static <T> Supplier<DataSource<T>> getFailedDataSourceSupplier(final Throwable failure) {
        return new Supplier<DataSource<T>>() { // from class: com.facebook.datasource.DataSources.1
            @Override // com.facebook.common.internal.Supplier
            public DataSource<T> get() {
                return DataSources.immediateFailedDataSource(failure);
            }
        };
    }

    @Nullable
    public static <T> T waitForFinalResult(DataSource<T> dataSource) throws Throwable {
        final CountDownLatch latch = new CountDownLatch(1);
        final ValueHolder<T> resultHolder = new ValueHolder<>();
        final ValueHolder<Throwable> pendingException = new ValueHolder<>();
        dataSource.subscribe(new DataSubscriber<T>() { // from class: com.facebook.datasource.DataSources.2
            /* JADX WARN: Type inference failed for: r1v1, types: [T, java.lang.Object] */
            @Override // com.facebook.datasource.DataSubscriber
            public void onNewResult(DataSource<T> dataSource2) {
                if (dataSource2.isFinished()) {
                    try {
                        ValueHolder.this.value = dataSource2.getResult();
                    } finally {
                        latch.countDown();
                    }
                }
            }

            /* JADX WARN: Type inference failed for: r1v1, types: [java.lang.Throwable, T] */
            @Override // com.facebook.datasource.DataSubscriber
            public void onFailure(DataSource<T> dataSource2) {
                try {
                    pendingException.value = dataSource2.getFailureCause();
                } finally {
                    latch.countDown();
                }
            }

            @Override // com.facebook.datasource.DataSubscriber
            public void onCancellation(DataSource<T> dataSource2) {
                latch.countDown();
            }

            @Override // com.facebook.datasource.DataSubscriber
            public void onProgressUpdate(DataSource<T> dataSource2) {
            }
        }, new Executor() { // from class: com.facebook.datasource.DataSources.3
            @Override // java.util.concurrent.Executor
            public void execute(Runnable command) {
                command.run();
            }
        });
        latch.await();
        if (pendingException.value == null) {
            return resultHolder.value;
        }
        throw ((Throwable) pendingException.value);
    }

    /* loaded from: classes.dex */
    private static class ValueHolder<T> {
        @Nullable
        public T value;

        private ValueHolder() {
            this.value = null;
        }
    }
}
