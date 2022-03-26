package kotlin.collections;

import androidx.exifinterface.media.ExifInterface;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
/* compiled from: ArrayDeque.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0015\b\u0007\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u000f\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005B\u0007\b\u0016¢\u0006\u0002\u0010\u0006B\u0015\b\u0016\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\b¢\u0006\u0002\u0010\tJ\u0015\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0016J\u001d\u0010\u0013\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0019J\u001e\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u0018\u001a\u00020\u00042\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0016J\u0016\u0010\u001a\u001a\u00020\u00142\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0016J\u0013\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u0015\u001a\u00028\u0000¢\u0006\u0002\u0010\u001cJ\u0013\u0010\u001d\u001a\u00020\u00172\u0006\u0010\u0015\u001a\u00028\u0000¢\u0006\u0002\u0010\u001cJ\b\u0010\u001e\u001a\u00020\u0017H\u0016J\u0016\u0010\u001f\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u0016J\u001e\u0010 \u001a\u00020\u00172\u0006\u0010!\u001a\u00020\u00042\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0002J\u0010\u0010\"\u001a\u00020\u00172\u0006\u0010#\u001a\u00020\u0004H\u0002J\u0010\u0010$\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u0010\u0010%\u001a\u00020\u00172\u0006\u0010&\u001a\u00020\u0004H\u0002J\u001d\u0010'\u001a\u00020\u00142\u0012\u0010(\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00140)H\u0082\bJ\u000b\u0010*\u001a\u00028\u0000¢\u0006\u0002\u0010+J\r\u0010,\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u0010+J\u0016\u0010-\u001a\u00028\u00002\u0006\u0010\u0018\u001a\u00020\u0004H\u0096\u0002¢\u0006\u0002\u0010.J\u0010\u0010/\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u0015\u00100\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00028\u0000H\u0016¢\u0006\u0002\u00101J\u0016\u00102\u001a\u00028\u00002\u0006\u0010!\u001a\u00020\u0004H\u0083\b¢\u0006\u0002\u0010.J\u0011\u0010!\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0083\bJM\u00103\u001a\u00020\u00172>\u00104\u001a:\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b6\u0012\b\b7\u0012\u0004\b\b(\u000e\u0012\u001b\u0012\u0019\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000b¢\u0006\f\b6\u0012\b\b7\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\u001705H\u0000¢\u0006\u0002\b8J\b\u00109\u001a\u00020\u0014H\u0016J\u000b\u0010:\u001a\u00028\u0000¢\u0006\u0002\u0010+J\u0015\u0010;\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00028\u0000H\u0016¢\u0006\u0002\u00101J\r\u0010<\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u0010+J\u0010\u0010=\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u001d\u0010#\u001a\u00020\u00042\u0006\u0010>\u001a\u00020\u00042\u0006\u0010&\u001a\u00020\u0004H\u0000¢\u0006\u0002\b?J\u0010\u0010@\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u0015\u0010A\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0016J\u0016\u0010B\u001a\u00020\u00142\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0016J\u0015\u0010C\u001a\u00028\u00002\u0006\u0010\u0018\u001a\u00020\u0004H\u0016¢\u0006\u0002\u0010.J\u000b\u0010D\u001a\u00028\u0000¢\u0006\u0002\u0010+J\r\u0010E\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u0010+J\u000b\u0010F\u001a\u00028\u0000¢\u0006\u0002\u0010+J\r\u0010G\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u0010+J\u0016\u0010H\u001a\u00020\u00142\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0016J\u001e\u0010I\u001a\u00028\u00002\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010JR\u0018\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\rR\u000e\u0010\u000e\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u0004@RX\u0096\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012¨\u0006K"}, d2 = {"Lkotlin/collections/ArrayDeque;", ExifInterface.LONGITUDE_EAST, "Lkotlin/collections/AbstractMutableList;", "initialCapacity", "", "(I)V", "()V", "elements", "", "(Ljava/util/Collection;)V", "elementData", "", "", "[Ljava/lang/Object;", "head", "<set-?>", "size", "getSize", "()I", "add", "", "element", "(Ljava/lang/Object;)Z", "", FirebaseAnalytics.Param.INDEX, "(ILjava/lang/Object;)V", "addAll", "addFirst", "(Ljava/lang/Object;)V", "addLast", "clear", "contains", "copyCollectionElements", "internalIndex", "copyElements", "newCapacity", "decremented", "ensureCapacity", "minCapacity", "filterInPlace", "predicate", "Lkotlin/Function1;", "first", "()Ljava/lang/Object;", "firstOrNull", "get", "(I)Ljava/lang/Object;", "incremented", "indexOf", "(Ljava/lang/Object;)I", "internalGet", "internalStructure", "structure", "Lkotlin/Function2;", "Lkotlin/ParameterName;", AppMeasurementSdk.ConditionalUserProperty.NAME, "internalStructure$kotlin_stdlib", "isEmpty", "last", "lastIndexOf", "lastOrNull", "negativeMod", "oldCapacity", "newCapacity$kotlin_stdlib", "positiveMod", "remove", "removeAll", "removeAt", "removeFirst", "removeFirstOrNull", "removeLast", "removeLastOrNull", "retainAll", "set", "(ILjava/lang/Object;)Ljava/lang/Object;", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class ArrayDeque<E> extends AbstractMutableList<E> {
    private Object[] elementData;
    private int head;
    private int size;

    @Override // kotlin.collections.AbstractMutableList
    public int getSize() {
        return this.size;
    }

    public ArrayDeque(int initialCapacity) {
        Object[] objArr;
        if (initialCapacity == 0) {
            objArr = ArrayDequeKt.emptyElementData;
        } else if (initialCapacity > 0) {
            objArr = new Object[initialCapacity];
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        this.elementData = objArr;
    }

    public ArrayDeque() {
        this.elementData = ArrayDequeKt.emptyElementData;
    }

    public ArrayDeque(Collection<? extends E> collection) {
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        boolean z = false;
        Object[] array = collection.toArray(new Object[0]);
        if (array != null) {
            this.elementData = array;
            Object[] objArr = this.elementData;
            this.size = objArr.length;
            if (objArr.length == 0 ? true : z) {
                this.elementData = ArrayDequeKt.emptyElementData;
                return;
            }
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
    }

    private final void ensureCapacity(int minCapacity) {
        if (minCapacity >= 0) {
            Object[] objArr = this.elementData;
            if (minCapacity > objArr.length) {
                if (objArr == ArrayDequeKt.emptyElementData) {
                    this.elementData = new Object[RangesKt.coerceAtLeast(minCapacity, 10)];
                } else {
                    copyElements(newCapacity$kotlin_stdlib(this.elementData.length, minCapacity));
                }
            }
        } else {
            throw new IllegalStateException("Deque is too big.");
        }
    }

    public final int newCapacity$kotlin_stdlib(int oldCapacity, int minCapacity) {
        int newCapacity = (oldCapacity >> 1) + oldCapacity;
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        int newCapacity2 = 2147483639;
        if (newCapacity - 2147483639 <= 0) {
            return newCapacity;
        }
        if (minCapacity > 2147483639) {
            newCapacity2 = Integer.MAX_VALUE;
        }
        return newCapacity2;
    }

    private final void copyElements(int newCapacity) {
        Object[] newElements = new Object[newCapacity];
        Object[] objArr = this.elementData;
        ArraysKt.copyInto(objArr, newElements, 0, this.head, objArr.length);
        Object[] objArr2 = this.elementData;
        int length = objArr2.length;
        int i = this.head;
        ArraysKt.copyInto(objArr2, newElements, length - i, 0, i);
        this.head = 0;
        this.elementData = newElements;
    }

    private final E internalGet(int internalIndex) {
        return (E) this.elementData[internalIndex];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int positiveMod(int index) {
        Object[] objArr = this.elementData;
        return index >= objArr.length ? index - objArr.length : index;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int negativeMod(int index) {
        return index < 0 ? this.elementData.length + index : index;
    }

    private final int internalIndex(int index) {
        return positiveMod(this.head + index);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int incremented(int index) {
        if (index == ArraysKt.getLastIndex(this.elementData)) {
            return 0;
        }
        return index + 1;
    }

    private final int decremented(int index) {
        return index == 0 ? ArraysKt.getLastIndex(this.elementData) : index - 1;
    }

    @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
    public boolean isEmpty() {
        return size() == 0;
    }

    public final E first() {
        if (!isEmpty()) {
            return (E) this.elementData[this.head];
        }
        throw new NoSuchElementException("ArrayDeque is empty.");
    }

    public final E firstOrNull() {
        if (isEmpty()) {
            return null;
        }
        return (E) this.elementData[this.head];
    }

    public final E last() {
        if (!isEmpty()) {
            return (E) this.elementData[positiveMod(this.head + CollectionsKt.getLastIndex(this))];
        }
        throw new NoSuchElementException("ArrayDeque is empty.");
    }

    public final E lastOrNull() {
        if (isEmpty()) {
            return null;
        }
        return (E) this.elementData[positiveMod(this.head + CollectionsKt.getLastIndex(this))];
    }

    public final void addFirst(E e) {
        ensureCapacity(size() + 1);
        this.head = decremented(this.head);
        this.elementData[this.head] = e;
        this.size = size() + 1;
    }

    public final void addLast(E e) {
        ensureCapacity(size() + 1);
        this.elementData[positiveMod(this.head + size())] = e;
        this.size = size() + 1;
    }

    public final E removeFirst() {
        if (!isEmpty()) {
            E e = (E) this.elementData[this.head];
            Object[] objArr = this.elementData;
            int i = this.head;
            objArr[i] = null;
            this.head = incremented(i);
            this.size = size() - 1;
            return e;
        }
        throw new NoSuchElementException("ArrayDeque is empty.");
    }

    public final E removeFirstOrNull() {
        if (isEmpty()) {
            return null;
        }
        return removeFirst();
    }

    public final E removeLast() {
        if (!isEmpty()) {
            int internalLastIndex = positiveMod(this.head + CollectionsKt.getLastIndex(this));
            E e = (E) this.elementData[internalLastIndex];
            this.elementData[internalLastIndex] = null;
            this.size = size() - 1;
            return e;
        }
        throw new NoSuchElementException("ArrayDeque is empty.");
    }

    public final E removeLastOrNull() {
        if (isEmpty()) {
            return null;
        }
        return removeLast();
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.List, java.util.Collection
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override // kotlin.collections.AbstractMutableList, java.util.AbstractList, java.util.List
    public void add(int index, E e) {
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(index, size());
        if (index == size()) {
            addLast(e);
        } else if (index == 0) {
            addFirst(e);
        } else {
            ensureCapacity(size() + 1);
            int internalIndex = positiveMod(this.head + index);
            if (index < ((size() + 1) >> 1)) {
                int decrementedInternalIndex = decremented(internalIndex);
                int decrementedHead = decremented(this.head);
                int i = this.head;
                if (decrementedInternalIndex >= i) {
                    Object[] objArr = this.elementData;
                    objArr[decrementedHead] = objArr[i];
                    ArraysKt.copyInto(objArr, objArr, i, i + 1, decrementedInternalIndex + 1);
                } else {
                    Object[] objArr2 = this.elementData;
                    ArraysKt.copyInto(objArr2, objArr2, i - 1, i, objArr2.length);
                    Object[] objArr3 = this.elementData;
                    objArr3[objArr3.length - 1] = objArr3[0];
                    ArraysKt.copyInto(objArr3, objArr3, 0, 1, decrementedInternalIndex + 1);
                }
                this.elementData[decrementedInternalIndex] = e;
                this.head = decrementedHead;
            } else {
                int tail = positiveMod(this.head + size());
                if (internalIndex < tail) {
                    Object[] objArr4 = this.elementData;
                    ArraysKt.copyInto(objArr4, objArr4, internalIndex + 1, internalIndex, tail);
                } else {
                    Object[] objArr5 = this.elementData;
                    ArraysKt.copyInto(objArr5, objArr5, 1, 0, tail);
                    Object[] objArr6 = this.elementData;
                    objArr6[0] = objArr6[objArr6.length - 1];
                    ArraysKt.copyInto(objArr6, objArr6, internalIndex + 1, internalIndex, objArr6.length - 1);
                }
                this.elementData[internalIndex] = e;
            }
            this.size = size() + 1;
        }
    }

    private final void copyCollectionElements(int internalIndex, Collection<? extends E> collection) {
        Iterator iterator = collection.iterator();
        int length = this.elementData.length;
        for (int index = internalIndex; index < length && iterator.hasNext(); index++) {
            this.elementData[index] = iterator.next();
        }
        int i = this.head;
        for (int index2 = 0; index2 < i && iterator.hasNext(); index2++) {
            this.elementData[index2] = iterator.next();
        }
        this.size = size() + collection.size();
    }

    @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
    public boolean addAll(Collection<? extends E> collection) {
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        if (collection.isEmpty()) {
            return false;
        }
        ensureCapacity(size() + collection.size());
        copyCollectionElements(positiveMod(this.head + size()), collection);
        return true;
    }

    @Override // java.util.AbstractList, java.util.List
    public boolean addAll(int index, Collection<? extends E> collection) {
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(index, size());
        if (collection.isEmpty()) {
            return false;
        }
        if (index == size()) {
            return addAll(collection);
        }
        ensureCapacity(size() + collection.size());
        int tail = positiveMod(this.head + size());
        int internalIndex = positiveMod(this.head + index);
        int elementsSize = collection.size();
        if (index < ((size() + 1) >> 1)) {
            int i = this.head;
            int shiftedHead = i - elementsSize;
            if (internalIndex < i) {
                Object[] objArr = this.elementData;
                ArraysKt.copyInto(objArr, objArr, shiftedHead, i, objArr.length);
                if (elementsSize >= internalIndex) {
                    Object[] objArr2 = this.elementData;
                    ArraysKt.copyInto(objArr2, objArr2, objArr2.length - elementsSize, 0, internalIndex);
                } else {
                    Object[] objArr3 = this.elementData;
                    ArraysKt.copyInto(objArr3, objArr3, objArr3.length - elementsSize, 0, elementsSize);
                    Object[] objArr4 = this.elementData;
                    ArraysKt.copyInto(objArr4, objArr4, 0, elementsSize, internalIndex);
                }
            } else if (shiftedHead >= 0) {
                Object[] objArr5 = this.elementData;
                ArraysKt.copyInto(objArr5, objArr5, shiftedHead, i, internalIndex);
            } else {
                Object[] objArr6 = this.elementData;
                shiftedHead += objArr6.length;
                int elementsToShift = internalIndex - i;
                int shiftToBack = objArr6.length - shiftedHead;
                if (shiftToBack >= elementsToShift) {
                    ArraysKt.copyInto(objArr6, objArr6, shiftedHead, i, internalIndex);
                } else {
                    ArraysKt.copyInto(objArr6, objArr6, shiftedHead, i, i + shiftToBack);
                    Object[] objArr7 = this.elementData;
                    ArraysKt.copyInto(objArr7, objArr7, 0, this.head + shiftToBack, internalIndex);
                }
            }
            this.head = shiftedHead;
            copyCollectionElements(negativeMod(internalIndex - elementsSize), collection);
        } else {
            int shiftedInternalIndex = internalIndex + elementsSize;
            if (internalIndex < tail) {
                int i2 = tail + elementsSize;
                Object[] objArr8 = this.elementData;
                if (i2 <= objArr8.length) {
                    ArraysKt.copyInto(objArr8, objArr8, shiftedInternalIndex, internalIndex, tail);
                } else if (shiftedInternalIndex >= objArr8.length) {
                    ArraysKt.copyInto(objArr8, objArr8, shiftedInternalIndex - objArr8.length, internalIndex, tail);
                } else {
                    int shiftToFront = (tail + elementsSize) - objArr8.length;
                    ArraysKt.copyInto(objArr8, objArr8, 0, tail - shiftToFront, tail);
                    Object[] objArr9 = this.elementData;
                    ArraysKt.copyInto(objArr9, objArr9, shiftedInternalIndex, internalIndex, tail - shiftToFront);
                }
            } else {
                Object[] objArr10 = this.elementData;
                ArraysKt.copyInto(objArr10, objArr10, elementsSize, 0, tail);
                Object[] objArr11 = this.elementData;
                if (shiftedInternalIndex >= objArr11.length) {
                    ArraysKt.copyInto(objArr11, objArr11, shiftedInternalIndex - objArr11.length, internalIndex, objArr11.length);
                } else {
                    ArraysKt.copyInto(objArr11, objArr11, 0, objArr11.length - elementsSize, objArr11.length);
                    Object[] objArr12 = this.elementData;
                    ArraysKt.copyInto(objArr12, objArr12, shiftedInternalIndex, internalIndex, objArr12.length - elementsSize);
                }
            }
            copyCollectionElements(internalIndex, collection);
        }
        return true;
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int index) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(index, size());
        return (E) this.elementData[positiveMod(this.head + index)];
    }

    @Override // kotlin.collections.AbstractMutableList, java.util.AbstractList, java.util.List
    public E set(int index, E e) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(index, size());
        int internalIndex = positiveMod(this.head + index);
        E e2 = (E) this.elementData[internalIndex];
        this.elementData[internalIndex] = e;
        return e2;
    }

    @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
    public boolean contains(Object element) {
        return indexOf(element) != -1;
    }

    @Override // java.util.AbstractList, java.util.List
    public int indexOf(Object element) {
        int tail = positiveMod(this.head + size());
        int index = this.head;
        if (index < tail) {
            while (index < tail) {
                if (Intrinsics.areEqual(element, this.elementData[index])) {
                    return index - this.head;
                }
                index++;
            }
            return -1;
        } else if (index < tail) {
            return -1;
        } else {
            int length = this.elementData.length;
            while (index < length) {
                if (Intrinsics.areEqual(element, this.elementData[index])) {
                    return index - this.head;
                }
                index++;
            }
            for (int index2 = 0; index2 < tail; index2++) {
                if (Intrinsics.areEqual(element, this.elementData[index2])) {
                    return (this.elementData.length + index2) - this.head;
                }
            }
            return -1;
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public int lastIndexOf(Object element) {
        int tail = positiveMod(this.head + size());
        int i = this.head;
        if (i < tail) {
            int index = tail - 1;
            if (index < i) {
                return -1;
            }
            while (!Intrinsics.areEqual(element, this.elementData[index])) {
                if (index == i) {
                    return -1;
                }
                index--;
            }
            return index - this.head;
        } else if (i <= tail) {
            return -1;
        } else {
            for (int index2 = tail - 1; index2 >= 0; index2--) {
                if (Intrinsics.areEqual(element, this.elementData[index2])) {
                    return (this.elementData.length + index2) - this.head;
                }
            }
            int index3 = ArraysKt.getLastIndex(this.elementData);
            int i2 = this.head;
            if (index3 < i2) {
                return -1;
            }
            while (!Intrinsics.areEqual(element, this.elementData[index3])) {
                if (index3 == i2) {
                    return -1;
                }
                index3--;
            }
            return index3 - this.head;
        }
    }

    @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
    public boolean remove(Object element) {
        int index = indexOf(element);
        if (index == -1) {
            return false;
        }
        remove(index);
        return true;
    }

    @Override // kotlin.collections.AbstractMutableList
    public E removeAt(int index) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(index, size());
        if (index == CollectionsKt.getLastIndex(this)) {
            return removeLast();
        }
        if (index == 0) {
            return removeFirst();
        }
        int internalIndex = positiveMod(this.head + index);
        E e = (E) this.elementData[internalIndex];
        if (index < (size() >> 1)) {
            int i = this.head;
            if (internalIndex >= i) {
                Object[] objArr = this.elementData;
                ArraysKt.copyInto(objArr, objArr, i + 1, i, internalIndex);
            } else {
                Object[] objArr2 = this.elementData;
                ArraysKt.copyInto(objArr2, objArr2, 1, 0, internalIndex);
                Object[] objArr3 = this.elementData;
                objArr3[0] = objArr3[objArr3.length - 1];
                int i2 = this.head;
                ArraysKt.copyInto(objArr3, objArr3, i2 + 1, i2, objArr3.length - 1);
            }
            Object[] objArr4 = this.elementData;
            int i3 = this.head;
            objArr4[i3] = null;
            this.head = incremented(i3);
        } else {
            int internalLastIndex = positiveMod(this.head + CollectionsKt.getLastIndex(this));
            if (internalIndex <= internalLastIndex) {
                Object[] objArr5 = this.elementData;
                ArraysKt.copyInto(objArr5, objArr5, internalIndex, internalIndex + 1, internalLastIndex + 1);
            } else {
                Object[] objArr6 = this.elementData;
                ArraysKt.copyInto(objArr6, objArr6, internalIndex, internalIndex + 1, objArr6.length);
                Object[] objArr7 = this.elementData;
                objArr7[objArr7.length - 1] = objArr7[0];
                ArraysKt.copyInto(objArr7, objArr7, 0, 1, internalLastIndex + 1);
            }
            this.elementData[internalLastIndex] = null;
        }
        this.size = size() - 1;
        return e;
    }

    @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
    public boolean removeAll(Collection<? extends Object> collection) {
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        boolean modified$iv = false;
        modified$iv = false;
        if (!isEmpty()) {
            boolean z = true;
            if (this.elementData.length != 0) {
                z = false;
            }
            if (!z) {
                int tail$iv = positiveMod(this.head + size());
                int newTail$iv = this.head;
                boolean modified$iv2 = false;
                if (this.head < tail$iv) {
                    for (int index$iv = this.head; index$iv < tail$iv; index$iv++) {
                        Object element$iv = this.elementData[index$iv];
                        if (!collection.contains(element$iv)) {
                            this.elementData[newTail$iv] = element$iv;
                            newTail$iv++;
                        } else {
                            modified$iv2 = true;
                        }
                    }
                    ArraysKt.fill(this.elementData, (Object) null, newTail$iv, tail$iv);
                    modified$iv = modified$iv2;
                } else {
                    int length = this.elementData.length;
                    for (int index$iv2 = this.head; index$iv2 < length; index$iv2++) {
                        Object element$iv2 = this.elementData[index$iv2];
                        this.elementData[index$iv2] = null;
                        if (!collection.contains(element$iv2)) {
                            this.elementData[newTail$iv] = element$iv2;
                            newTail$iv++;
                        } else {
                            modified$iv2 = true;
                        }
                    }
                    newTail$iv = positiveMod(newTail$iv);
                    for (int index$iv3 = 0; index$iv3 < tail$iv; index$iv3++) {
                        Object element$iv3 = this.elementData[index$iv3];
                        this.elementData[index$iv3] = null;
                        if (!collection.contains(element$iv3)) {
                            this.elementData[newTail$iv] = element$iv3;
                            newTail$iv = incremented(newTail$iv);
                        } else {
                            modified$iv2 = true;
                        }
                    }
                    modified$iv = modified$iv2;
                }
                if (modified$iv) {
                    this.size = negativeMod(newTail$iv - this.head);
                }
            }
        }
        return modified$iv;
    }

    @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
    public boolean retainAll(Collection<? extends Object> collection) {
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        boolean modified$iv = false;
        modified$iv = false;
        if (!isEmpty()) {
            if (!(this.elementData.length == 0)) {
                int tail$iv = positiveMod(this.head + size());
                int newTail$iv = this.head;
                boolean modified$iv2 = false;
                if (this.head < tail$iv) {
                    for (int index$iv = this.head; index$iv < tail$iv; index$iv++) {
                        Object element$iv = this.elementData[index$iv];
                        if (collection.contains(element$iv)) {
                            this.elementData[newTail$iv] = element$iv;
                            newTail$iv++;
                        } else {
                            modified$iv2 = true;
                        }
                    }
                    ArraysKt.fill(this.elementData, (Object) null, newTail$iv, tail$iv);
                    modified$iv = modified$iv2;
                } else {
                    int length = this.elementData.length;
                    for (int index$iv2 = this.head; index$iv2 < length; index$iv2++) {
                        Object element$iv2 = this.elementData[index$iv2];
                        this.elementData[index$iv2] = null;
                        if (collection.contains(element$iv2)) {
                            this.elementData[newTail$iv] = element$iv2;
                            newTail$iv++;
                        } else {
                            modified$iv2 = true;
                        }
                    }
                    newTail$iv = positiveMod(newTail$iv);
                    for (int index$iv3 = 0; index$iv3 < tail$iv; index$iv3++) {
                        Object element$iv3 = this.elementData[index$iv3];
                        this.elementData[index$iv3] = null;
                        if (collection.contains(element$iv3)) {
                            this.elementData[newTail$iv] = element$iv3;
                            newTail$iv = incremented(newTail$iv);
                        } else {
                            modified$iv2 = true;
                        }
                    }
                    modified$iv = modified$iv2;
                }
                if (modified$iv) {
                    this.size = negativeMod(newTail$iv - this.head);
                }
            }
        }
        return modified$iv;
    }

    private final boolean filterInPlace(Function1<? super E, Boolean> function1) {
        if (!isEmpty()) {
            if (!(this.elementData.length == 0)) {
                int tail = positiveMod(this.head + size());
                int newTail = this.head;
                boolean modified = false;
                if (this.head < tail) {
                    for (int index = this.head; index < tail; index++) {
                        Object element = this.elementData[index];
                        if (function1.invoke(element).booleanValue()) {
                            this.elementData[newTail] = element;
                            newTail++;
                        } else {
                            modified = true;
                        }
                    }
                    ArraysKt.fill(this.elementData, (Object) null, newTail, tail);
                } else {
                    int length = this.elementData.length;
                    for (int index2 = this.head; index2 < length; index2++) {
                        Object element2 = this.elementData[index2];
                        this.elementData[index2] = null;
                        if (function1.invoke(element2).booleanValue()) {
                            this.elementData[newTail] = element2;
                            newTail++;
                        } else {
                            modified = true;
                        }
                    }
                    newTail = positiveMod(newTail);
                    for (int index3 = 0; index3 < tail; index3++) {
                        Object element3 = this.elementData[index3];
                        this.elementData[index3] = null;
                        if (function1.invoke(element3).booleanValue()) {
                            this.elementData[newTail] = element3;
                            newTail = incremented(newTail);
                        } else {
                            modified = true;
                        }
                    }
                }
                if (modified) {
                    this.size = negativeMod(newTail - this.head);
                }
                return modified;
            }
        }
        return false;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.List, java.util.Collection
    public void clear() {
        int tail = positiveMod(this.head + size());
        int i = this.head;
        if (i < tail) {
            ArraysKt.fill(this.elementData, (Object) null, i, tail);
        } else if (!isEmpty()) {
            Object[] objArr = this.elementData;
            ArraysKt.fill(objArr, (Object) null, this.head, objArr.length);
            ArraysKt.fill(this.elementData, (Object) null, 0, tail);
        }
        this.head = 0;
        this.size = 0;
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [java.lang.Object[], java.lang.Object] */
    /* JADX WARN: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final void internalStructure$kotlin_stdlib(Function2<? super Integer, ? super Object[], Unit> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "structure");
        int tail = positiveMod(this.head + size());
        if (isEmpty()) {
            function2.invoke(Integer.valueOf(this.head), new Object[0]);
            return;
        }
        ?? r1 = new Object[size()];
        int i = this.head;
        if (i < tail) {
            ArraysKt.copyInto$default(this.elementData, (Object[]) r1, 0, i, tail, 2, (Object) null);
            function2.invoke(Integer.valueOf(this.head), r1);
            return;
        }
        ArraysKt.copyInto$default(this.elementData, (Object[]) r1, 0, i, 0, 10, (Object) null);
        Object[] objArr = this.elementData;
        ArraysKt.copyInto(objArr, (Object[]) r1, objArr.length - this.head, 0, tail);
        function2.invoke(Integer.valueOf(this.head - this.elementData.length), r1);
    }
}
