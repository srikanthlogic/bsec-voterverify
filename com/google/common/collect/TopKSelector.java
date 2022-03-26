package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class TopKSelector<T> {
    private final T[] buffer;
    private int bufferSize;
    private final Comparator<? super T> comparator;
    private final int k;
    @NullableDecl
    private T threshold;

    public static <T extends Comparable<? super T>> TopKSelector<T> least(int k) {
        return least(k, Ordering.natural());
    }

    public static <T> TopKSelector<T> least(int k, Comparator<? super T> comparator) {
        return new TopKSelector<>(comparator, k);
    }

    public static <T extends Comparable<? super T>> TopKSelector<T> greatest(int k) {
        return greatest(k, Ordering.natural());
    }

    public static <T> TopKSelector<T> greatest(int k, Comparator<? super T> comparator) {
        return new TopKSelector<>(Ordering.from(comparator).reverse(), k);
    }

    private TopKSelector(Comparator<? super T> comparator, int k) {
        this.comparator = (Comparator) Preconditions.checkNotNull(comparator, "comparator");
        this.k = k;
        boolean z = true;
        Preconditions.checkArgument(k >= 0, "k (%s) must be >= 0", k);
        Preconditions.checkArgument(k > 1073741823 ? false : z, "k (%s) must be <= Integer.MAX_VALUE / 2", k);
        this.buffer = (T[]) new Object[IntMath.checkedMultiply(k, 2)];
        this.bufferSize = 0;
        this.threshold = null;
    }

    public void offer(@NullableDecl T elem) {
        int i = this.k;
        if (i != 0) {
            int i2 = this.bufferSize;
            if (i2 == 0) {
                this.buffer[0] = elem;
                this.threshold = elem;
                this.bufferSize = 1;
            } else if (i2 < i) {
                T[] tArr = this.buffer;
                this.bufferSize = i2 + 1;
                tArr[i2] = elem;
                if (this.comparator.compare(elem, (T) this.threshold) > 0) {
                    this.threshold = elem;
                }
            } else if (this.comparator.compare(elem, (T) this.threshold) < 0) {
                T[] tArr2 = this.buffer;
                int i3 = this.bufferSize;
                this.bufferSize = i3 + 1;
                tArr2[i3] = elem;
                if (this.bufferSize == this.k * 2) {
                    trim();
                }
            }
        }
    }

    private void trim() {
        int left = 0;
        int right = (this.k * 2) - 1;
        int minThresholdPosition = 0;
        int iterations = 0;
        int maxIterations = IntMath.log2(right - 0, RoundingMode.CEILING) * 3;
        while (true) {
            if (left >= right) {
                break;
            }
            int pivotNewIndex = partition(left, right, ((left + right) + 1) >>> 1);
            int i = this.k;
            if (pivotNewIndex <= i) {
                if (pivotNewIndex >= i) {
                    break;
                }
                left = Math.max(pivotNewIndex, left + 1);
                minThresholdPosition = pivotNewIndex;
            } else {
                right = pivotNewIndex - 1;
            }
            iterations++;
            if (iterations >= maxIterations) {
                Arrays.sort(this.buffer, left, right, this.comparator);
                break;
            }
        }
        this.bufferSize = this.k;
        this.threshold = this.buffer[minThresholdPosition];
        for (int i2 = minThresholdPosition + 1; i2 < this.k; i2++) {
            if (this.comparator.compare((Object) this.buffer[i2], (T) this.threshold) > 0) {
                this.threshold = this.buffer[i2];
            }
        }
    }

    private int partition(int left, int right, int pivotIndex) {
        T[] tArr = this.buffer;
        T pivotValue = tArr[pivotIndex];
        tArr[pivotIndex] = tArr[right];
        int pivotNewIndex = left;
        for (int i = left; i < right; i++) {
            if (this.comparator.compare((Object) this.buffer[i], pivotValue) < 0) {
                swap(pivotNewIndex, i);
                pivotNewIndex++;
            }
        }
        T[] tArr2 = this.buffer;
        tArr2[right] = tArr2[pivotNewIndex];
        tArr2[pivotNewIndex] = pivotValue;
        return pivotNewIndex;
    }

    private void swap(int i, int j) {
        T[] tArr = this.buffer;
        T tmp = tArr[i];
        tArr[i] = tArr[j];
        tArr[j] = tmp;
    }

    public void offerAll(Iterable<? extends T> elements) {
        offerAll(elements.iterator());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void offerAll(Iterator<? extends T> elements) {
        while (elements.hasNext()) {
            offer(elements.next());
        }
    }

    public List<T> topK() {
        Arrays.sort(this.buffer, 0, this.bufferSize, this.comparator);
        int i = this.bufferSize;
        int i2 = this.k;
        if (i > i2) {
            T[] tArr = this.buffer;
            Arrays.fill(tArr, i2, tArr.length, (Object) null);
            int i3 = this.k;
            this.bufferSize = i3;
            this.threshold = this.buffer[i3 - 1];
        }
        return Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(this.buffer, this.bufferSize)));
    }
}
