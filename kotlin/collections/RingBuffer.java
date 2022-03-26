package kotlin.collections;

import androidx.exifinterface.media.ExifInterface;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.Arrays;
import java.util.Iterator;
import java.util.RandomAccess;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
/* compiled from: SlidingWindow.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010(\n\u0002\b\b\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u0004B\u000f\b\u0016\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007B\u001d\u0012\u000e\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\t\u0012\u0006\u0010\u000b\u001a\u00020\u0006¢\u0006\u0002\u0010\fJ\u0013\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000¢\u0006\u0002\u0010\u0016J\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u0006\u0010\u0018\u001a\u00020\u0006J\u0016\u0010\u0019\u001a\u00028\u00002\u0006\u0010\u001a\u001a\u00020\u0006H\u0096\u0002¢\u0006\u0002\u0010\u001bJ\u0006\u0010\u001c\u001a\u00020\u001dJ\u000f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00028\u00000\u001fH\u0096\u0002J\u000e\u0010 \u001a\u00020\u00142\u0006\u0010!\u001a\u00020\u0006J\u0015\u0010\"\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tH\u0014¢\u0006\u0002\u0010#J'\u0010\"\u001a\b\u0012\u0004\u0012\u0002H\u00010\t\"\u0004\b\u0001\u0010\u00012\f\u0010$\u001a\b\u0012\u0004\u0012\u0002H\u00010\tH\u0014¢\u0006\u0002\u0010%J\u0015\u0010&\u001a\u00020\u0006*\u00020\u00062\u0006\u0010!\u001a\u00020\u0006H\u0082\bR\u0018\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\rR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u0006@RX\u0096\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006'"}, d2 = {"Lkotlin/collections/RingBuffer;", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/collections/AbstractList;", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "capacity", "", "(I)V", "buffer", "", "", "filledSize", "([Ljava/lang/Object;I)V", "[Ljava/lang/Object;", "<set-?>", "size", "getSize", "()I", "startIndex", "add", "", "element", "(Ljava/lang/Object;)V", "expanded", "maxCapacity", "get", FirebaseAnalytics.Param.INDEX, "(I)Ljava/lang/Object;", "isFull", "", "iterator", "", "removeFirst", "n", "toArray", "()[Ljava/lang/Object;", "array", "([Ljava/lang/Object;)[Ljava/lang/Object;", "forward", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class RingBuffer<T> extends AbstractList<T> implements RandomAccess {
    private final Object[] buffer;
    private final int capacity;
    private int size;
    private int startIndex;

    public RingBuffer(Object[] buffer, int filledSize) {
        Intrinsics.checkParameterIsNotNull(buffer, "buffer");
        this.buffer = buffer;
        boolean z = true;
        if (filledSize >= 0) {
            if (filledSize > this.buffer.length ? false : z) {
                this.capacity = this.buffer.length;
                this.size = filledSize;
                return;
            }
            throw new IllegalArgumentException(("ring buffer filled size: " + filledSize + " cannot be larger than the buffer size: " + this.buffer.length).toString());
        }
        throw new IllegalArgumentException(("ring buffer filled size should not be negative but it is " + filledSize).toString());
    }

    public RingBuffer(int capacity) {
        this(new Object[capacity], 0);
    }

    @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection
    public int getSize() {
        return this.size;
    }

    @Override // kotlin.collections.AbstractList, java.util.List
    public T get(int index) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(index, size());
        return (T) this.buffer[(this.startIndex + index) % this.capacity];
    }

    public final boolean isFull() {
        return size() == this.capacity;
    }

    @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<T> iterator() {
        return new AbstractIterator<T>() { // from class: kotlin.collections.RingBuffer$iterator$1
            private int count;
            private int index;

            /* JADX INFO: Access modifiers changed from: package-private */
            /* JADX WARN: Incorrect args count in method signature: ()V */
            {
                this.count = RingBuffer.this.size();
                this.index = RingBuffer.this.startIndex;
            }

            @Override // kotlin.collections.AbstractIterator
            protected void computeNext() {
                if (this.count == 0) {
                    done();
                    return;
                }
                setNext(RingBuffer.this.buffer[this.index]);
                this.index = (this.index + 1) % RingBuffer.this.capacity;
                this.count--;
            }
        };
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlin.collections.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        Object[] result;
        Intrinsics.checkParameterIsNotNull(tArr, "array");
        if (tArr.length < size()) {
            result = (T[]) Arrays.copyOf(tArr, size());
            Intrinsics.checkExpressionValueIsNotNull(result, "java.util.Arrays.copyOf(this, newSize)");
        } else {
            result = tArr;
        }
        int size = size();
        int widx = 0;
        int idx = this.startIndex;
        while (widx < size && idx < this.capacity) {
            result[widx] = this.buffer[idx];
            widx++;
            idx++;
        }
        int idx2 = 0;
        while (widx < size) {
            result[widx] = this.buffer[idx2];
            widx++;
            idx2++;
        }
        if (result.length > size()) {
            result[size()] = null;
        }
        if (result != null) {
            return (T[]) result;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlin.collections.AbstractCollection, java.util.Collection
    public Object[] toArray() {
        return toArray(new Object[size()]);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final RingBuffer<T> expanded(int maxCapacity) {
        Object[] newBuffer;
        int i = this.capacity;
        int newCapacity = RangesKt.coerceAtMost(i + (i >> 1) + 1, maxCapacity);
        if (this.startIndex == 0) {
            newBuffer = Arrays.copyOf(this.buffer, newCapacity);
            Intrinsics.checkExpressionValueIsNotNull(newBuffer, "java.util.Arrays.copyOf(this, newSize)");
        } else {
            newBuffer = toArray(new Object[newCapacity]);
        }
        return new RingBuffer<>(newBuffer, size());
    }

    @Override // kotlin.collections.AbstractCollection, java.util.Collection
    public final void add(T t) {
        if (!isFull()) {
            this.buffer[(this.startIndex + size()) % this.capacity] = t;
            this.size = size() + 1;
            return;
        }
        throw new IllegalStateException("ring buffer is full");
    }

    public final void removeFirst(int n) {
        boolean z = true;
        if (n >= 0) {
            if (n > size()) {
                z = false;
            }
            if (!z) {
                throw new IllegalArgumentException(("n shouldn't be greater than the buffer size: n = " + n + ", size = " + size()).toString());
            } else if (n > 0) {
                int start = this.startIndex;
                int end = (start + n) % this.capacity;
                if (start > end) {
                    ArraysKt.fill(this.buffer, (Object) null, start, this.capacity);
                    ArraysKt.fill(this.buffer, (Object) null, 0, end);
                } else {
                    ArraysKt.fill(this.buffer, (Object) null, start, end);
                }
                this.startIndex = end;
                this.size = size() - n;
            }
        } else {
            throw new IllegalArgumentException(("n shouldn't be negative but it is " + n).toString());
        }
    }

    public final int forward(int $this$forward, int n) {
        return ($this$forward + n) % this.capacity;
    }
}
