package androidx.core.view;

import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
/* compiled from: ViewGroup.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000L\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010)\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\u001a\u0015\u0010\n\u001a\u00020\u000b*\u00020\u00032\u0006\u0010\f\u001a\u00020\u0002H\u0086\n\u001a0\u0010\r\u001a\u00020\u000e*\u00020\u00032!\u0010\u000f\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\f\u0012\u0004\u0012\u00020\u000e0\u0010H\u0086\b\u001aE\u0010\u0013\u001a\u00020\u000e*\u00020\u000326\u0010\u000f\u001a2\u0012\u0013\u0012\u00110\u0007¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\f\u0012\u0004\u0012\u00020\u000e0\u0014H\u0086\b\u001a\u0015\u0010\u0016\u001a\u00020\u0002*\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0007H\u0086\u0002\u001a\r\u0010\u0017\u001a\u00020\u000b*\u00020\u0003H\u0086\b\u001a\r\u0010\u0018\u001a\u00020\u000b*\u00020\u0003H\u0086\b\u001a\u0013\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00020\u001a*\u00020\u0003H\u0086\u0002\u001a\u0015\u0010\u001b\u001a\u00020\u000e*\u00020\u00032\u0006\u0010\f\u001a\u00020\u0002H\u0086\n\u001a\u0015\u0010\u001c\u001a\u00020\u000e*\u00020\u00032\u0006\u0010\f\u001a\u00020\u0002H\u0086\n\u001a\u0017\u0010\u001d\u001a\u00020\u000e*\u00020\u001e2\b\b\u0001\u0010\u0006\u001a\u00020\u0007H\u0086\b\u001a5\u0010\u001f\u001a\u00020\u000e*\u00020\u001e2\b\b\u0003\u0010 \u001a\u00020\u00072\b\b\u0003\u0010!\u001a\u00020\u00072\b\b\u0003\u0010\"\u001a\u00020\u00072\b\b\u0003\u0010#\u001a\u00020\u0007H\u0086\b\u001a5\u0010$\u001a\u00020\u000e*\u00020\u001e2\b\b\u0003\u0010%\u001a\u00020\u00072\b\b\u0003\u0010!\u001a\u00020\u00072\b\b\u0003\u0010&\u001a\u00020\u00072\b\b\u0003\u0010#\u001a\u00020\u0007H\u0087\b\"\u001b\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\"\u0016\u0010\u0006\u001a\u00020\u0007*\u00020\u00038Æ\u0002¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006'"}, d2 = {"children", "Lkotlin/sequences/Sequence;", "Landroid/view/View;", "Landroid/view/ViewGroup;", "getChildren", "(Landroid/view/ViewGroup;)Lkotlin/sequences/Sequence;", "size", "", "getSize", "(Landroid/view/ViewGroup;)I", "contains", "", "view", "forEach", "", "action", "Lkotlin/Function1;", "Lkotlin/ParameterName;", AppMeasurementSdk.ConditionalUserProperty.NAME, "forEachIndexed", "Lkotlin/Function2;", FirebaseAnalytics.Param.INDEX, "get", "isEmpty", "isNotEmpty", "iterator", "", "minusAssign", "plusAssign", "setMargins", "Landroid/view/ViewGroup$MarginLayoutParams;", "updateMargins", "left", "top", "right", "bottom", "updateMarginsRelative", "start", "end", "core-ktx_release"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class ViewGroupKt {
    public static final View get(ViewGroup $this$get, int index) {
        Intrinsics.checkParameterIsNotNull($this$get, "$this$get");
        View childAt = $this$get.getChildAt(index);
        if (childAt != null) {
            return childAt;
        }
        throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + $this$get.getChildCount());
    }

    public static final boolean contains(ViewGroup $this$contains, View view) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        Intrinsics.checkParameterIsNotNull(view, "view");
        return $this$contains.indexOfChild(view) != -1;
    }

    public static final void plusAssign(ViewGroup $this$plusAssign, View view) {
        Intrinsics.checkParameterIsNotNull($this$plusAssign, "$this$plusAssign");
        Intrinsics.checkParameterIsNotNull(view, "view");
        $this$plusAssign.addView(view);
    }

    public static final void minusAssign(ViewGroup $this$minusAssign, View view) {
        Intrinsics.checkParameterIsNotNull($this$minusAssign, "$this$minusAssign");
        Intrinsics.checkParameterIsNotNull(view, "view");
        $this$minusAssign.removeView(view);
    }

    public static final int getSize(ViewGroup $this$size) {
        Intrinsics.checkParameterIsNotNull($this$size, "$this$size");
        return $this$size.getChildCount();
    }

    public static final boolean isEmpty(ViewGroup $this$isEmpty) {
        Intrinsics.checkParameterIsNotNull($this$isEmpty, "$this$isEmpty");
        return $this$isEmpty.getChildCount() == 0;
    }

    public static final boolean isNotEmpty(ViewGroup $this$isNotEmpty) {
        Intrinsics.checkParameterIsNotNull($this$isNotEmpty, "$this$isNotEmpty");
        return $this$isNotEmpty.getChildCount() != 0;
    }

    public static final void forEach(ViewGroup $this$forEach, Function1<? super View, Unit> function1) {
        Intrinsics.checkParameterIsNotNull($this$forEach, "$this$forEach");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        int childCount = $this$forEach.getChildCount();
        for (int index = 0; index < childCount; index++) {
            View childAt = $this$forEach.getChildAt(index);
            Intrinsics.checkExpressionValueIsNotNull(childAt, "getChildAt(index)");
            function1.invoke(childAt);
        }
    }

    public static final void forEachIndexed(ViewGroup $this$forEachIndexed, Function2<? super Integer, ? super View, Unit> function2) {
        Intrinsics.checkParameterIsNotNull($this$forEachIndexed, "$this$forEachIndexed");
        Intrinsics.checkParameterIsNotNull(function2, "action");
        int childCount = $this$forEachIndexed.getChildCount();
        for (int index = 0; index < childCount; index++) {
            Integer valueOf = Integer.valueOf(index);
            View childAt = $this$forEachIndexed.getChildAt(index);
            Intrinsics.checkExpressionValueIsNotNull(childAt, "getChildAt(index)");
            function2.invoke(valueOf, childAt);
        }
    }

    public static final Iterator<View> iterator(ViewGroup $this$iterator) {
        Intrinsics.checkParameterIsNotNull($this$iterator, "$this$iterator");
        return new Object($this$iterator) { // from class: androidx.core.view.ViewGroupKt$iterator$1
            final /* synthetic */ ViewGroup $this_iterator;
            private int index;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$this_iterator = $receiver;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.index < this.$this_iterator.getChildCount();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public View next() {
                ViewGroup viewGroup = this.$this_iterator;
                int i = this.index;
                this.index = i + 1;
                View childAt = viewGroup.getChildAt(i);
                if (childAt != null) {
                    return childAt;
                }
                throw new IndexOutOfBoundsException();
            }

            @Override // java.util.Iterator
            public void remove() {
                this.index--;
                this.$this_iterator.removeViewAt(this.index);
            }
        };
    }

    public static final Sequence<View> getChildren(ViewGroup $this$children) {
        Intrinsics.checkParameterIsNotNull($this$children, "$this$children");
        return new Sequence<View>($this$children) { // from class: androidx.core.view.ViewGroupKt$children$1
            final /* synthetic */ ViewGroup $this_children;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$this_children = $receiver;
            }

            @Override // kotlin.sequences.Sequence
            public Iterator<View> iterator() {
                return ViewGroupKt.iterator(this.$this_children);
            }
        };
    }

    public static final void setMargins(ViewGroup.MarginLayoutParams $this$setMargins, int size) {
        Intrinsics.checkParameterIsNotNull($this$setMargins, "$this$setMargins");
        $this$setMargins.setMargins(size, size, size, size);
    }

    public static /* synthetic */ void updateMargins$default(ViewGroup.MarginLayoutParams $this$updateMargins, int left, int top, int right, int bottom, int i, Object obj) {
        if ((i & 1) != 0) {
            left = $this$updateMargins.leftMargin;
        }
        if ((i & 2) != 0) {
            top = $this$updateMargins.topMargin;
        }
        if ((i & 4) != 0) {
            right = $this$updateMargins.rightMargin;
        }
        if ((i & 8) != 0) {
            bottom = $this$updateMargins.bottomMargin;
        }
        Intrinsics.checkParameterIsNotNull($this$updateMargins, "$this$updateMargins");
        $this$updateMargins.setMargins(left, top, right, bottom);
    }

    public static final void updateMargins(ViewGroup.MarginLayoutParams $this$updateMargins, int left, int top, int right, int bottom) {
        Intrinsics.checkParameterIsNotNull($this$updateMargins, "$this$updateMargins");
        $this$updateMargins.setMargins(left, top, right, bottom);
    }

    public static /* synthetic */ void updateMarginsRelative$default(ViewGroup.MarginLayoutParams $this$updateMarginsRelative, int start, int top, int end, int bottom, int i, Object obj) {
        if ((i & 1) != 0) {
            start = $this$updateMarginsRelative.getMarginStart();
        }
        if ((i & 2) != 0) {
            top = $this$updateMarginsRelative.topMargin;
        }
        if ((i & 4) != 0) {
            end = $this$updateMarginsRelative.getMarginEnd();
        }
        if ((i & 8) != 0) {
            bottom = $this$updateMarginsRelative.bottomMargin;
        }
        Intrinsics.checkParameterIsNotNull($this$updateMarginsRelative, "$this$updateMarginsRelative");
        $this$updateMarginsRelative.setMarginStart(start);
        $this$updateMarginsRelative.topMargin = top;
        $this$updateMarginsRelative.setMarginEnd(end);
        $this$updateMarginsRelative.bottomMargin = bottom;
    }

    public static final void updateMarginsRelative(ViewGroup.MarginLayoutParams $this$updateMarginsRelative, int start, int top, int end, int bottom) {
        Intrinsics.checkParameterIsNotNull($this$updateMarginsRelative, "$this$updateMarginsRelative");
        $this$updateMarginsRelative.setMarginStart(start);
        $this$updateMarginsRelative.topMargin = top;
        $this$updateMarginsRelative.setMarginEnd(end);
        $this$updateMarginsRelative.bottomMargin = bottom;
    }
}
