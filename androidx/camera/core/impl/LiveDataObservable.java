package androidx.camera.core.impl;

import android.os.SystemClock;
import androidx.camera.core.impl.Observable;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.core.util.Preconditions;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public final class LiveDataObservable<T> implements Observable<T> {
    final MutableLiveData<Result<T>> mLiveData = new MutableLiveData<>();
    private final Map<Observable.Observer<T>, LiveDataObserverAdapter<T>> mObservers = new HashMap();

    public void postValue(T value) {
        this.mLiveData.postValue(Result.fromValue(value));
    }

    public void postError(Throwable error) {
        this.mLiveData.postValue(Result.fromError(error));
    }

    public LiveData<Result<T>> getLiveData() {
        return this.mLiveData;
    }

    @Override // androidx.camera.core.impl.Observable
    public ListenableFuture<T> fetchData() {
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver<T>() { // from class: androidx.camera.core.impl.LiveDataObservable.1
            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public Object attachCompleter(final CallbackToFutureAdapter.Completer<T> completer) {
                CameraXExecutors.mainThreadExecutor().execute(new Runnable() { // from class: androidx.camera.core.impl.LiveDataObservable.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Result<T> result = LiveDataObservable.this.mLiveData.getValue();
                        if (result == null) {
                            completer.setException(new IllegalStateException("Observable has not yet been initialized with a value."));
                        } else if (result.completedSuccessfully()) {
                            completer.set(result.getValue());
                        } else {
                            Preconditions.checkNotNull(result.getError());
                            completer.setException(result.getError());
                        }
                    }
                });
                return LiveDataObservable.this + " [fetch@" + SystemClock.uptimeMillis() + "]";
            }
        });
    }

    @Override // androidx.camera.core.impl.Observable
    public void addObserver(Executor executor, Observable.Observer<T> observer) {
        synchronized (this.mObservers) {
            final LiveDataObserverAdapter<T> oldAdapter = this.mObservers.get(observer);
            if (oldAdapter != null) {
                oldAdapter.disable();
            }
            final LiveDataObserverAdapter<T> newAdapter = new LiveDataObserverAdapter<>(executor, observer);
            this.mObservers.put(observer, newAdapter);
            CameraXExecutors.mainThreadExecutor().execute(new Runnable() { // from class: androidx.camera.core.impl.LiveDataObservable.2
                @Override // java.lang.Runnable
                public void run() {
                    LiveDataObservable.this.mLiveData.removeObserver(oldAdapter);
                    LiveDataObservable.this.mLiveData.observeForever(newAdapter);
                }
            });
        }
    }

    @Override // androidx.camera.core.impl.Observable
    public void removeObserver(Observable.Observer<T> observer) {
        synchronized (this.mObservers) {
            final LiveDataObserverAdapter<T> adapter = this.mObservers.remove(observer);
            if (adapter != null) {
                adapter.disable();
                CameraXExecutors.mainThreadExecutor().execute(new Runnable() { // from class: androidx.camera.core.impl.LiveDataObservable.3
                    @Override // java.lang.Runnable
                    public void run() {
                        LiveDataObservable.this.mLiveData.removeObserver(adapter);
                    }
                });
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class Result<T> {
        private Throwable mError;
        private T mValue;

        private Result(T value, Throwable error) {
            this.mValue = value;
            this.mError = error;
        }

        static <T> Result<T> fromValue(T value) {
            return new Result<>(value, null);
        }

        static <T> Result<T> fromError(Throwable error) {
            return new Result<>(null, (Throwable) Preconditions.checkNotNull(error));
        }

        public boolean completedSuccessfully() {
            return this.mError == null;
        }

        public T getValue() {
            if (completedSuccessfully()) {
                return this.mValue;
            }
            throw new IllegalStateException("Result contains an error. Does not contain a value.");
        }

        public Throwable getError() {
            return this.mError;
        }

        public String toString() {
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append("[Result: <");
            if (completedSuccessfully()) {
                str = "Value: " + this.mValue;
            } else {
                str = "Error: " + this.mError;
            }
            sb.append(str);
            sb.append(">]");
            return sb.toString();
        }
    }

    /* loaded from: classes.dex */
    private static final class LiveDataObserverAdapter<T> implements Observer<Result<T>> {
        final AtomicBoolean mActive = new AtomicBoolean(true);
        final Executor mExecutor;
        final Observable.Observer<T> mObserver;

        @Override // androidx.lifecycle.Observer
        public /* bridge */ /* synthetic */ void onChanged(Object obj) {
            onChanged((Result) ((Result) obj));
        }

        LiveDataObserverAdapter(Executor executor, Observable.Observer<T> observer) {
            this.mExecutor = executor;
            this.mObserver = observer;
        }

        void disable() {
            this.mActive.set(false);
        }

        public void onChanged(final Result<T> result) {
            this.mExecutor.execute(new Runnable() { // from class: androidx.camera.core.impl.LiveDataObservable.LiveDataObserverAdapter.1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.lang.Runnable
                public void run() {
                    if (LiveDataObserverAdapter.this.mActive.get()) {
                        if (result.completedSuccessfully()) {
                            LiveDataObserverAdapter.this.mObserver.onNewData(result.getValue());
                            return;
                        }
                        Preconditions.checkNotNull(result.getError());
                        LiveDataObserverAdapter.this.mObserver.onError(result.getError());
                    }
                }
            });
        }
    }
}
