package androidx.core.view;

import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
/* compiled from: Menu.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000D\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010)\n\u0002\b\u0002\u001a\u0015\u0010\n\u001a\u00020\u000b*\u00020\u00032\u0006\u0010\f\u001a\u00020\u0002H\u0086\u0002\u001a0\u0010\r\u001a\u00020\u000e*\u00020\u00032!\u0010\u000f\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\f\u0012\u0004\u0012\u00020\u000e0\u0010H\u0086\b\u001aE\u0010\u0013\u001a\u00020\u000e*\u00020\u000326\u0010\u000f\u001a2\u0012\u0013\u0012\u00110\u0007¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\f\u0012\u0004\u0012\u00020\u000e0\u0014H\u0086\b\u001a\u0015\u0010\u0016\u001a\u00020\u0002*\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0007H\u0086\n\u001a\r\u0010\u0017\u001a\u00020\u000b*\u00020\u0003H\u0086\b\u001a\r\u0010\u0018\u001a\u00020\u000b*\u00020\u0003H\u0086\b\u001a\u0013\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00020\u001a*\u00020\u0003H\u0086\u0002\u001a\u0015\u0010\u001b\u001a\u00020\u000e*\u00020\u00032\u0006\u0010\f\u001a\u00020\u0002H\u0086\n\"\u001b\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\"\u0016\u0010\u0006\u001a\u00020\u0007*\u00020\u00038Æ\u0002¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\u001c"}, d2 = {"children", "Lkotlin/sequences/Sequence;", "Landroid/view/MenuItem;", "Landroid/view/Menu;", "getChildren", "(Landroid/view/Menu;)Lkotlin/sequences/Sequence;", "size", "", "getSize", "(Landroid/view/Menu;)I", "contains", "", "item", "forEach", "", "action", "Lkotlin/Function1;", "Lkotlin/ParameterName;", AppMeasurementSdk.ConditionalUserProperty.NAME, "forEachIndexed", "Lkotlin/Function2;", FirebaseAnalytics.Param.INDEX, "get", "isEmpty", "isNotEmpty", "iterator", "", "minusAssign", "core-ktx_release"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class MenuKt {
    public static final MenuItem get(Menu $this$get, int index) {
        Intrinsics.checkParameterIsNotNull($this$get, "$this$get");
        MenuItem item = $this$get.getItem(index);
        Intrinsics.checkExpressionValueIsNotNull(item, "getItem(index)");
        return item;
    }

    public static final boolean contains(Menu $this$contains, MenuItem item) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        Intrinsics.checkParameterIsNotNull(item, "item");
        int size = $this$contains.size();
        for (int index = 0; index < size; index++) {
            if (Intrinsics.areEqual($this$contains.getItem(index), item)) {
                return true;
            }
        }
        return false;
    }

    public static final void minusAssign(Menu $this$minusAssign, MenuItem item) {
        Intrinsics.checkParameterIsNotNull($this$minusAssign, "$this$minusAssign");
        Intrinsics.checkParameterIsNotNull(item, "item");
        $this$minusAssign.removeItem(item.getItemId());
    }

    public static final int getSize(Menu $this$size) {
        Intrinsics.checkParameterIsNotNull($this$size, "$this$size");
        return $this$size.size();
    }

    public static final boolean isEmpty(Menu $this$isEmpty) {
        Intrinsics.checkParameterIsNotNull($this$isEmpty, "$this$isEmpty");
        return $this$isEmpty.size() == 0;
    }

    public static final boolean isNotEmpty(Menu $this$isNotEmpty) {
        Intrinsics.checkParameterIsNotNull($this$isNotEmpty, "$this$isNotEmpty");
        return $this$isNotEmpty.size() != 0;
    }

    public static final void forEach(Menu $this$forEach, Function1<? super MenuItem, Unit> function1) {
        Intrinsics.checkParameterIsNotNull($this$forEach, "$this$forEach");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        int size = $this$forEach.size();
        for (int index = 0; index < size; index++) {
            MenuItem item = $this$forEach.getItem(index);
            Intrinsics.checkExpressionValueIsNotNull(item, "getItem(index)");
            function1.invoke(item);
        }
    }

    public static final void forEachIndexed(Menu $this$forEachIndexed, Function2<? super Integer, ? super MenuItem, Unit> function2) {
        Intrinsics.checkParameterIsNotNull($this$forEachIndexed, "$this$forEachIndexed");
        Intrinsics.checkParameterIsNotNull(function2, "action");
        int size = $this$forEachIndexed.size();
        for (int index = 0; index < size; index++) {
            Integer valueOf = Integer.valueOf(index);
            MenuItem item = $this$forEachIndexed.getItem(index);
            Intrinsics.checkExpressionValueIsNotNull(item, "getItem(index)");
            function2.invoke(valueOf, item);
        }
    }

    public static final Iterator<MenuItem> iterator(Menu $this$iterator) {
        Intrinsics.checkParameterIsNotNull($this$iterator, "$this$iterator");
        return new Object($this$iterator) { // from class: androidx.core.view.MenuKt$iterator$1
            final /* synthetic */ Menu $this_iterator;
            private int index;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$this_iterator = $receiver;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.index < this.$this_iterator.size();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public MenuItem next() {
                Menu menu = this.$this_iterator;
                int i = this.index;
                this.index = i + 1;
                MenuItem item = menu.getItem(i);
                if (item != null) {
                    return item;
                }
                throw new IndexOutOfBoundsException();
            }

            @Override // java.util.Iterator
            public void remove() {
                this.index--;
                this.$this_iterator.removeItem(this.index);
            }
        };
    }

    public static final Sequence<MenuItem> getChildren(Menu $this$children) {
        Intrinsics.checkParameterIsNotNull($this$children, "$this$children");
        return new Sequence<MenuItem>($this$children) { // from class: androidx.core.view.MenuKt$children$1
            final /* synthetic */ Menu $this_children;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$this_children = $receiver;
            }

            @Override // kotlin.sequences.Sequence
            public Iterator<MenuItem> iterator() {
                return MenuKt.iterator(this.$this_children);
            }
        };
    }
}
