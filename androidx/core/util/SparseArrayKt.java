package androidx.core.util;

import android.util.SparseArray;
import androidx.exifinterface.media.ExifInterface;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.IntIterator;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SparseArray.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010(\n\u0000\u001a!\u0010\u0006\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\b\u001a\u00020\u0001H\u0086\n\u001a!\u0010\t\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\b\u001a\u00020\u0001H\u0086\b\u001a&\u0010\n\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u000b\u001a\u0002H\u0002H\u0086\b¢\u0006\u0002\u0010\f\u001aQ\u0010\r\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000326\u0010\u000f\u001a2\u0012\u0013\u0012\u00110\u0001¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\b\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u000b\u0012\u0004\u0012\u00020\u000e0\u0010H\u0086\b\u001a.\u0010\u0013\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\b\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u0002H\u0002H\u0086\b¢\u0006\u0002\u0010\u0015\u001a4\u0010\u0016\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\b\u001a\u00020\u00012\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0017H\u0086\b¢\u0006\u0002\u0010\u0018\u001a\u0019\u0010\u0019\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0086\b\u001a\u0019\u0010\u001a\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0086\b\u001a\u0016\u0010\u001b\u001a\u00020\u001c\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003\u001a-\u0010\u001d\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0086\u0002\u001a$\u0010\u001f\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\u001a+\u0010 \u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\b\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u0002H\u0002¢\u0006\u0002\u0010!\u001a.\u0010\"\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\b\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u0002H\u0002H\u0086\n¢\u0006\u0002\u0010#\u001a\u001c\u0010$\u001a\b\u0012\u0004\u0012\u0002H\u00020%\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\"\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00038Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006&"}, d2 = {"size", "", ExifInterface.GPS_DIRECTION_TRUE, "Landroid/util/SparseArray;", "getSize", "(Landroid/util/SparseArray;)I", "contains", "", "key", "containsKey", "containsValue", "value", "(Landroid/util/SparseArray;Ljava/lang/Object;)Z", "forEach", "", "action", "Lkotlin/Function2;", "Lkotlin/ParameterName;", AppMeasurementSdk.ConditionalUserProperty.NAME, "getOrDefault", "defaultValue", "(Landroid/util/SparseArray;ILjava/lang/Object;)Ljava/lang/Object;", "getOrElse", "Lkotlin/Function0;", "(Landroid/util/SparseArray;ILkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "isEmpty", "isNotEmpty", "keyIterator", "Lkotlin/collections/IntIterator;", "plus", "other", "putAll", "remove", "(Landroid/util/SparseArray;ILjava/lang/Object;)Z", "set", "(Landroid/util/SparseArray;ILjava/lang/Object;)V", "valueIterator", "", "core-ktx_release"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class SparseArrayKt {
    public static final <T> int getSize(SparseArray<T> sparseArray) {
        Intrinsics.checkParameterIsNotNull(sparseArray, "$this$size");
        return sparseArray.size();
    }

    public static final <T> boolean contains(SparseArray<T> sparseArray, int key) {
        Intrinsics.checkParameterIsNotNull(sparseArray, "$this$contains");
        return sparseArray.indexOfKey(key) >= 0;
    }

    public static final <T> void set(SparseArray<T> sparseArray, int key, T t) {
        Intrinsics.checkParameterIsNotNull(sparseArray, "$this$set");
        sparseArray.put(key, t);
    }

    public static final <T> SparseArray<T> plus(SparseArray<T> sparseArray, SparseArray<T> sparseArray2) {
        Intrinsics.checkParameterIsNotNull(sparseArray, "$this$plus");
        Intrinsics.checkParameterIsNotNull(sparseArray2, "other");
        SparseArray sparseArray3 = new SparseArray(sparseArray.size() + sparseArray2.size());
        putAll(sparseArray3, sparseArray);
        putAll(sparseArray3, sparseArray2);
        return sparseArray3;
    }

    public static final <T> boolean containsKey(SparseArray<T> sparseArray, int key) {
        Intrinsics.checkParameterIsNotNull(sparseArray, "$this$containsKey");
        return sparseArray.indexOfKey(key) >= 0;
    }

    public static final <T> boolean containsValue(SparseArray<T> sparseArray, T t) {
        Intrinsics.checkParameterIsNotNull(sparseArray, "$this$containsValue");
        return sparseArray.indexOfValue(t) >= 0;
    }

    public static final <T> T getOrDefault(SparseArray<T> sparseArray, int key, T t) {
        Intrinsics.checkParameterIsNotNull(sparseArray, "$this$getOrDefault");
        T t2 = sparseArray.get(key);
        return t2 != null ? t2 : t;
    }

    public static final <T> T getOrElse(SparseArray<T> sparseArray, int key, Function0<? extends T> function0) {
        Intrinsics.checkParameterIsNotNull(sparseArray, "$this$getOrElse");
        Intrinsics.checkParameterIsNotNull(function0, "defaultValue");
        T t = sparseArray.get(key);
        return t != null ? t : (T) function0.invoke();
    }

    public static final <T> boolean isEmpty(SparseArray<T> sparseArray) {
        Intrinsics.checkParameterIsNotNull(sparseArray, "$this$isEmpty");
        return sparseArray.size() == 0;
    }

    public static final <T> boolean isNotEmpty(SparseArray<T> sparseArray) {
        Intrinsics.checkParameterIsNotNull(sparseArray, "$this$isNotEmpty");
        return sparseArray.size() != 0;
    }

    public static final <T> boolean remove(SparseArray<T> sparseArray, int key, T t) {
        Intrinsics.checkParameterIsNotNull(sparseArray, "$this$remove");
        int index = sparseArray.indexOfKey(key);
        if (index < 0 || !Intrinsics.areEqual(t, sparseArray.valueAt(index))) {
            return false;
        }
        sparseArray.removeAt(index);
        return true;
    }

    public static final <T> void putAll(SparseArray<T> sparseArray, SparseArray<T> sparseArray2) {
        Intrinsics.checkParameterIsNotNull(sparseArray, "$this$putAll");
        Intrinsics.checkParameterIsNotNull(sparseArray2, "other");
        int size = sparseArray2.size();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            sparseArray.put(sparseArray2.keyAt(index$iv), sparseArray2.valueAt(index$iv));
        }
    }

    public static final <T> void forEach(SparseArray<T> sparseArray, Function2<? super Integer, ? super T, Unit> function2) {
        Intrinsics.checkParameterIsNotNull(sparseArray, "$this$forEach");
        Intrinsics.checkParameterIsNotNull(function2, "action");
        int size = sparseArray.size();
        for (int index = 0; index < size; index++) {
            function2.invoke(Integer.valueOf(sparseArray.keyAt(index)), sparseArray.valueAt(index));
        }
    }

    public static final <T> IntIterator keyIterator(SparseArray<T> sparseArray) {
        Intrinsics.checkParameterIsNotNull(sparseArray, "$this$keyIterator");
        return new IntIterator(sparseArray) { // from class: androidx.core.util.SparseArrayKt$keyIterator$1
            final /* synthetic */ SparseArray $this_keyIterator;
            private int index;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$this_keyIterator = r1;
            }

            public final int getIndex() {
                return this.index;
            }

            public final void setIndex(int i) {
                this.index = i;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.index < this.$this_keyIterator.size();
            }

            @Override // kotlin.collections.IntIterator
            public int nextInt() {
                SparseArray sparseArray2 = this.$this_keyIterator;
                int i = this.index;
                this.index = i + 1;
                return sparseArray2.keyAt(i);
            }
        };
    }

    public static final <T> Iterator<T> valueIterator(SparseArray<T> sparseArray) {
        Intrinsics.checkParameterIsNotNull(sparseArray, "$this$valueIterator");
        return new Object(sparseArray) { // from class: androidx.core.util.SparseArrayKt$valueIterator$1
            final /* synthetic */ SparseArray $this_valueIterator;
            private int index;

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$this_valueIterator = r1;
            }

            public final int getIndex() {
                return this.index;
            }

            public final void setIndex(int i) {
                this.index = i;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.index < this.$this_valueIterator.size();
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [T, java.lang.Object] */
            @Override // java.util.Iterator
            public T next() {
                SparseArray sparseArray2 = this.$this_valueIterator;
                int i = this.index;
                this.index = i + 1;
                return sparseArray2.valueAt(i);
            }
        };
    }
}
