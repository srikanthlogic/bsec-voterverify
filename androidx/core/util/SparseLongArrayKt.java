package androidx.core.util;

import android.util.SparseLongArray;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.IntIterator;
import kotlin.collections.LongIterator;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SparseLongArray.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000D\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\u001a\u0015\u0010\u0005\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\b\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\t\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\n\u001a\u00020\u000bH\u0087\b\u001aE\u0010\f\u001a\u00020\r*\u00020\u000226\u0010\u000e\u001a2\u0012\u0013\u0012\u00110\u0001¢\u0006\f\b\u0010\u0012\b\b\u0011\u0012\u0004\b\b(\u0007\u0012\u0013\u0012\u00110\u000b¢\u0006\f\b\u0010\u0012\b\b\u0011\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00020\r0\u000fH\u0087\b\u001a\u001d\u0010\u0012\u001a\u00020\u000b*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u000bH\u0087\b\u001a#\u0010\u0014\u001a\u00020\u000b*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u00012\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0015H\u0087\b\u001a\r\u0010\u0016\u001a\u00020\u0006*\u00020\u0002H\u0087\b\u001a\r\u0010\u0017\u001a\u00020\u0006*\u00020\u0002H\u0087\b\u001a\f\u0010\u0018\u001a\u00020\u0019*\u00020\u0002H\u0007\u001a\u0015\u0010\u001a\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002H\u0087\u0002\u001a\u0014\u0010\u001c\u001a\u00020\r*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002H\u0007\u001a\u001c\u0010\u001d\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u000bH\u0007\u001a\u001d\u0010\u001e\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u000bH\u0087\n\u001a\f\u0010\u001f\u001a\u00020 *\u00020\u0002H\u0007\"\u0016\u0010\u0000\u001a\u00020\u0001*\u00020\u00028Ç\u0002¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004¨\u0006!"}, d2 = {"size", "", "Landroid/util/SparseLongArray;", "getSize", "(Landroid/util/SparseLongArray;)I", "contains", "", "key", "containsKey", "containsValue", "value", "", "forEach", "", "action", "Lkotlin/Function2;", "Lkotlin/ParameterName;", AppMeasurementSdk.ConditionalUserProperty.NAME, "getOrDefault", "defaultValue", "getOrElse", "Lkotlin/Function0;", "isEmpty", "isNotEmpty", "keyIterator", "Lkotlin/collections/IntIterator;", "plus", "other", "putAll", "remove", "set", "valueIterator", "Lkotlin/collections/LongIterator;", "core-ktx_release"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class SparseLongArrayKt {
    public static final int getSize(SparseLongArray $this$size) {
        Intrinsics.checkParameterIsNotNull($this$size, "$this$size");
        return $this$size.size();
    }

    public static final boolean contains(SparseLongArray $this$contains, int key) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        return $this$contains.indexOfKey(key) >= 0;
    }

    public static final void set(SparseLongArray $this$set, int key, long value) {
        Intrinsics.checkParameterIsNotNull($this$set, "$this$set");
        $this$set.put(key, value);
    }

    public static final SparseLongArray plus(SparseLongArray $this$plus, SparseLongArray other) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(other, "other");
        SparseLongArray sparseLongArray = new SparseLongArray($this$plus.size() + other.size());
        putAll(sparseLongArray, $this$plus);
        putAll(sparseLongArray, other);
        return sparseLongArray;
    }

    public static final boolean containsKey(SparseLongArray $this$containsKey, int key) {
        Intrinsics.checkParameterIsNotNull($this$containsKey, "$this$containsKey");
        return $this$containsKey.indexOfKey(key) >= 0;
    }

    public static final boolean containsValue(SparseLongArray $this$containsValue, long value) {
        Intrinsics.checkParameterIsNotNull($this$containsValue, "$this$containsValue");
        return $this$containsValue.indexOfValue(value) >= 0;
    }

    public static final long getOrDefault(SparseLongArray $this$getOrDefault, int key, long defaultValue) {
        Intrinsics.checkParameterIsNotNull($this$getOrDefault, "$this$getOrDefault");
        return $this$getOrDefault.get(key, defaultValue);
    }

    public static final long getOrElse(SparseLongArray $this$getOrElse, int key, Function0<Long> function0) {
        Intrinsics.checkParameterIsNotNull($this$getOrElse, "$this$getOrElse");
        Intrinsics.checkParameterIsNotNull(function0, "defaultValue");
        int it = $this$getOrElse.indexOfKey(key);
        return it >= 0 ? $this$getOrElse.valueAt(it) : function0.invoke().longValue();
    }

    public static final boolean isEmpty(SparseLongArray $this$isEmpty) {
        Intrinsics.checkParameterIsNotNull($this$isEmpty, "$this$isEmpty");
        return $this$isEmpty.size() == 0;
    }

    public static final boolean isNotEmpty(SparseLongArray $this$isNotEmpty) {
        Intrinsics.checkParameterIsNotNull($this$isNotEmpty, "$this$isNotEmpty");
        return $this$isNotEmpty.size() != 0;
    }

    public static final boolean remove(SparseLongArray $this$remove, int key, long value) {
        Intrinsics.checkParameterIsNotNull($this$remove, "$this$remove");
        int index = $this$remove.indexOfKey(key);
        if (index < 0 || value != $this$remove.valueAt(index)) {
            return false;
        }
        $this$remove.removeAt(index);
        return true;
    }

    public static final void putAll(SparseLongArray $this$putAll, SparseLongArray other) {
        Intrinsics.checkParameterIsNotNull($this$putAll, "$this$putAll");
        Intrinsics.checkParameterIsNotNull(other, "other");
        int size = other.size();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            $this$putAll.put(other.keyAt(index$iv), other.valueAt(index$iv));
        }
    }

    public static final void forEach(SparseLongArray $this$forEach, Function2<? super Integer, ? super Long, Unit> function2) {
        Intrinsics.checkParameterIsNotNull($this$forEach, "$this$forEach");
        Intrinsics.checkParameterIsNotNull(function2, "action");
        int size = $this$forEach.size();
        for (int index = 0; index < size; index++) {
            function2.invoke(Integer.valueOf($this$forEach.keyAt(index)), Long.valueOf($this$forEach.valueAt(index)));
        }
    }

    public static final IntIterator keyIterator(SparseLongArray $this$keyIterator) {
        Intrinsics.checkParameterIsNotNull($this$keyIterator, "$this$keyIterator");
        return new IntIterator($this$keyIterator) { // from class: androidx.core.util.SparseLongArrayKt$keyIterator$1
            final /* synthetic */ SparseLongArray $this_keyIterator;
            private int index;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$this_keyIterator = $receiver;
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
                SparseLongArray sparseLongArray = this.$this_keyIterator;
                int i = this.index;
                this.index = i + 1;
                return sparseLongArray.keyAt(i);
            }
        };
    }

    public static final LongIterator valueIterator(SparseLongArray $this$valueIterator) {
        Intrinsics.checkParameterIsNotNull($this$valueIterator, "$this$valueIterator");
        return new LongIterator($this$valueIterator) { // from class: androidx.core.util.SparseLongArrayKt$valueIterator$1
            final /* synthetic */ SparseLongArray $this_valueIterator;
            private int index;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$this_valueIterator = $receiver;
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

            @Override // kotlin.collections.LongIterator
            public long nextLong() {
                SparseLongArray sparseLongArray = this.$this_valueIterator;
                int i = this.index;
                this.index = i + 1;
                return sparseLongArray.valueAt(i);
            }
        };
    }
}
