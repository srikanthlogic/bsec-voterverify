package androidx.lifecycle;

import androidx.arch.core.util.Function;
/* loaded from: classes.dex */
public class Transformations {
    private Transformations() {
    }

    public static <X, Y> LiveData<Y> map(LiveData<X> source, final Function<X, Y> mapFunction) {
        final MediatorLiveData<Y> result = new MediatorLiveData<>();
        result.addSource(source, new Observer<X>() { // from class: androidx.lifecycle.Transformations.1
            @Override // androidx.lifecycle.Observer
            public void onChanged(X x) {
                MediatorLiveData.this.setValue(mapFunction.apply(x));
            }
        });
        return result;
    }

    public static <X, Y> LiveData<Y> switchMap(LiveData<X> source, final Function<X, LiveData<Y>> switchMapFunction) {
        final MediatorLiveData<Y> result = new MediatorLiveData<>();
        result.addSource(source, new Observer<X>() { // from class: androidx.lifecycle.Transformations.2
            LiveData<Y> mSource;

            @Override // androidx.lifecycle.Observer
            public void onChanged(X x) {
                LiveData<Y> newLiveData = (LiveData) Function.this.apply(x);
                Object obj = this.mSource;
                if (obj != newLiveData) {
                    if (obj != null) {
                        result.removeSource(obj);
                    }
                    this.mSource = newLiveData;
                    Object obj2 = this.mSource;
                    if (obj2 != null) {
                        result.addSource(obj2, new Observer<Y>() { // from class: androidx.lifecycle.Transformations.2.1
                            @Override // androidx.lifecycle.Observer
                            public void onChanged(Y y) {
                                result.setValue(y);
                            }
                        });
                    }
                }
            }
        });
        return result;
    }

    public static <X> LiveData<X> distinctUntilChanged(LiveData<X> source) {
        final MediatorLiveData<X> outputLiveData = new MediatorLiveData<>();
        outputLiveData.addSource(source, new Observer<X>() { // from class: androidx.lifecycle.Transformations.3
            boolean mFirstTime = true;

            @Override // androidx.lifecycle.Observer
            public void onChanged(X currentValue) {
                T value = MediatorLiveData.this.getValue();
                if (this.mFirstTime || ((value == 0 && currentValue != 0) || (value != 0 && !value.equals(currentValue)))) {
                    this.mFirstTime = false;
                    MediatorLiveData.this.setValue(currentValue);
                }
            }
        });
        return outputLiveData;
    }
}
