package kotlin;

import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import kotlin.collections.ArraysKt;
import kotlin.collections.UIntIterator;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
/* compiled from: UIntArray.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0015\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0087@\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001-B\u0014\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006B\u0014\b\u0001\u0012\u0006\u0010\u0007\u001a\u00020\bø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\tJ\u001b\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\u0011\u0010\u0012J \u0010\u0013\u001a\u00020\u000f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u0016ø\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016J\u0013\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019HÖ\u0003J\u001b\u0010\u001a\u001a\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0004H\u0086\u0002ø\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u001dJ\t\u0010\u001e\u001a\u00020\u0004HÖ\u0001J\u000f\u0010\u001f\u001a\u00020\u000fH\u0016¢\u0006\u0004\b \u0010!J\u0010\u0010\"\u001a\u00020#H\u0096\u0002¢\u0006\u0004\b$\u0010%J#\u0010&\u001a\u00020'2\u0006\u0010\u001b\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H\u0086\u0002ø\u0001\u0000¢\u0006\u0004\b)\u0010*J\t\u0010+\u001a\u00020,HÖ\u0001R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\b8\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\f\u0010\rø\u0001\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006."}, d2 = {"Lkotlin/UIntArray;", "", "Lkotlin/UInt;", "size", "", "constructor-impl", "(I)[I", "storage", "", "([I)[I", "getSize-impl", "([I)I", "storage$annotations", "()V", "contains", "", "element", "contains-WZ4Q5Ns", "([II)Z", "containsAll", "elements", "containsAll-impl", "([ILjava/util/Collection;)Z", "equals", "other", "", "get", FirebaseAnalytics.Param.INDEX, "get-impl", "([II)I", "hashCode", "isEmpty", "isEmpty-impl", "([I)Z", "iterator", "Lkotlin/collections/UIntIterator;", "iterator-impl", "([I)Lkotlin/collections/UIntIterator;", "set", "", "value", "set-VXSXFK8", "([III)V", "toString", "", "Iterator", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class UIntArray implements Collection<UInt>, KMappedMarker {
    private final int[] storage;

    /* renamed from: equals-impl */
    public static boolean m147equalsimpl(int[] iArr, Object obj) {
        return (obj instanceof UIntArray) && Intrinsics.areEqual(iArr, ((UIntArray) obj).m158unboximpl());
    }

    /* renamed from: equals-impl0 */
    public static final boolean m148equalsimpl0(int[] iArr, int[] iArr2) {
        return Intrinsics.areEqual(iArr, iArr2);
    }

    /* renamed from: hashCode-impl */
    public static int m151hashCodeimpl(int[] iArr) {
        if (iArr != null) {
            return Arrays.hashCode(iArr);
        }
        return 0;
    }

    public static /* synthetic */ void storage$annotations() {
    }

    /* renamed from: toString-impl */
    public static String m155toStringimpl(int[] iArr) {
        return "UIntArray(storage=" + Arrays.toString(iArr) + ")";
    }

    @Override // java.util.Collection
    public /* synthetic */ boolean add(UInt uInt) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    /* renamed from: add-WZ4Q5Ns */
    public boolean m156addWZ4Q5Ns(int i) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends UInt> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    /* renamed from: contains-WZ4Q5Ns */
    public boolean m157containsWZ4Q5Ns(int i) {
        return m145containsWZ4Q5Ns(this.storage, i);
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<? extends Object> collection) {
        return m146containsAllimpl(this.storage, collection);
    }

    @Override // java.util.Collection, java.lang.Object
    public boolean equals(Object obj) {
        return m147equalsimpl(this.storage, obj);
    }

    public int getSize() {
        return m150getSizeimpl(this.storage);
    }

    @Override // java.util.Collection, java.lang.Object
    public int hashCode() {
        return m151hashCodeimpl(this.storage);
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return m152isEmptyimpl(this.storage);
    }

    @Override // java.util.Collection, java.lang.Iterable
    public UIntIterator iterator() {
        return m153iteratorimpl(this.storage);
    }

    @Override // java.util.Collection
    public boolean remove(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean removeAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean retainAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public Object[] toArray() {
        return CollectionToArray.toArray(this);
    }

    @Override // java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        return (T[]) CollectionToArray.toArray(this, tArr);
    }

    @Override // java.lang.Object
    public String toString() {
        return m155toStringimpl(this.storage);
    }

    /* renamed from: unbox-impl */
    public final /* synthetic */ int[] m158unboximpl() {
        return this.storage;
    }

    @Override // java.util.Collection
    public final /* bridge */ boolean contains(Object obj) {
        if (obj instanceof UInt) {
            return m157containsWZ4Q5Ns(((UInt) obj).m141unboximpl());
        }
        return false;
    }

    @Override // java.util.Collection
    public final /* bridge */ int size() {
        return getSize();
    }

    private /* synthetic */ UIntArray(int[] storage) {
        Intrinsics.checkParameterIsNotNull(storage, "storage");
        this.storage = storage;
    }

    /* renamed from: constructor-impl */
    public static int[] m144constructorimpl(int[] storage) {
        Intrinsics.checkParameterIsNotNull(storage, "storage");
        return storage;
    }

    /* renamed from: constructor-impl */
    public static int[] m143constructorimpl(int size) {
        return m144constructorimpl(new int[size]);
    }

    /* renamed from: get-impl */
    public static final int m149getimpl(int[] $this, int index) {
        return UInt.m98constructorimpl($this[index]);
    }

    /* renamed from: set-VXSXFK8 */
    public static final void m154setVXSXFK8(int[] $this, int index, int value) {
        $this[index] = value;
    }

    /* renamed from: getSize-impl */
    public static int m150getSizeimpl(int[] $this) {
        return $this.length;
    }

    /* renamed from: iterator-impl */
    public static UIntIterator m153iteratorimpl(int[] $this) {
        return new Iterator($this);
    }

    /* compiled from: UIntArray.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\bH\u0096\u0002J\u0010\u0010\t\u001a\u00020\nH\u0016ø\u0001\u0000¢\u0006\u0002\u0010\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\f"}, d2 = {"Lkotlin/UIntArray$Iterator;", "Lkotlin/collections/UIntIterator;", "array", "", "([I)V", FirebaseAnalytics.Param.INDEX, "", "hasNext", "", "nextUInt", "Lkotlin/UInt;", "()I", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Iterator extends UIntIterator {
        private final int[] array;
        private int index;

        public Iterator(int[] array) {
            Intrinsics.checkParameterIsNotNull(array, "array");
            this.array = array;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.index < this.array.length;
        }

        @Override // kotlin.collections.UIntIterator
        public int nextUInt() {
            int i = this.index;
            int[] iArr = this.array;
            if (i < iArr.length) {
                this.index = i + 1;
                return UInt.m98constructorimpl(iArr[i]);
            }
            throw new NoSuchElementException(String.valueOf(i));
        }
    }

    /* renamed from: contains-WZ4Q5Ns */
    public static boolean m145containsWZ4Q5Ns(int[] $this, int element) {
        return ArraysKt.contains($this, element);
    }

    /* renamed from: containsAll-impl */
    public static boolean m146containsAllimpl(int[] $this, Collection<UInt> collection) {
        Object it;
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        Collection<UInt> $this$all$iv = collection;
        if ($this$all$iv.isEmpty()) {
            return true;
        }
        for (Object element$iv : $this$all$iv) {
            if (!(element$iv instanceof UInt) || !ArraysKt.contains($this, ((UInt) element$iv).m141unboximpl())) {
                it = null;
                continue;
            } else {
                it = 1;
                continue;
            }
            if (it == null) {
                return false;
            }
        }
        return true;
    }

    /* renamed from: isEmpty-impl */
    public static boolean m152isEmptyimpl(int[] $this) {
        return $this.length == 0;
    }
}
