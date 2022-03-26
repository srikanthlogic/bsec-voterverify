package androidx.core.graphics;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Rect.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000<\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u0015\u0010\u0000\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\f\u001a\r\u0010\u0004\u001a\u00020\u0005*\u00020\u0001H\u0086\n\u001a\r\u0010\u0004\u001a\u00020\u0006*\u00020\u0003H\u0086\n\u001a\r\u0010\u0007\u001a\u00020\u0005*\u00020\u0001H\u0086\n\u001a\r\u0010\u0007\u001a\u00020\u0006*\u00020\u0003H\u0086\n\u001a\r\u0010\b\u001a\u00020\u0005*\u00020\u0001H\u0086\n\u001a\r\u0010\b\u001a\u00020\u0006*\u00020\u0003H\u0086\n\u001a\r\u0010\t\u001a\u00020\u0005*\u00020\u0001H\u0086\n\u001a\r\u0010\t\u001a\u00020\u0006*\u00020\u0003H\u0086\n\u001a\u0015\u0010\n\u001a\u00020\u000b*\u00020\u00012\u0006\u0010\f\u001a\u00020\rH\u0086\n\u001a\u0015\u0010\n\u001a\u00020\u000b*\u00020\u00032\u0006\u0010\f\u001a\u00020\u000eH\u0086\n\u001a\u0015\u0010\u000f\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\rH\u0086\n\u001a\u0015\u0010\u000f\u001a\u00020\u0011*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0086\n\u001a\u0015\u0010\u000f\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0005H\u0086\n\u001a\u0015\u0010\u000f\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u000eH\u0086\n\u001a\u0015\u0010\u000f\u001a\u00020\u0011*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0086\n\u001a\u0015\u0010\u000f\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u0006H\u0086\n\u001a\u0015\u0010\u0012\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0086\f\u001a\u0015\u0010\u0012\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0086\f\u001a\u0015\u0010\u0013\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\rH\u0086\n\u001a\u0015\u0010\u0013\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0086\n\u001a\u0015\u0010\u0013\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0005H\u0086\n\u001a\u0015\u0010\u0013\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u000eH\u0086\n\u001a\u0015\u0010\u0013\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0086\n\u001a\u0015\u0010\u0013\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u0006H\u0086\n\u001a\u0015\u0010\u0014\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u0005H\u0086\n\u001a\u0015\u0010\u0014\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0006H\u0086\n\u001a\u0015\u0010\u0014\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0005H\u0086\n\u001a\r\u0010\u0016\u001a\u00020\u0001*\u00020\u0003H\u0086\b\u001a\r\u0010\u0017\u001a\u00020\u0003*\u00020\u0001H\u0086\b\u001a\r\u0010\u0018\u001a\u00020\u0011*\u00020\u0001H\u0086\b\u001a\r\u0010\u0018\u001a\u00020\u0011*\u00020\u0003H\u0086\b\u001a\u0015\u0010\u0019\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u001bH\u0086\b\u001a\u0015\u0010\u001c\u001a\u00020\u0011*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0086\f\u001a\u0015\u0010\u001c\u001a\u00020\u0011*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0086\f¨\u0006\u001d"}, d2 = {"and", "Landroid/graphics/Rect;", "r", "Landroid/graphics/RectF;", "component1", "", "", "component2", "component3", "component4", "contains", "", "p", "Landroid/graphics/Point;", "Landroid/graphics/PointF;", "minus", "xy", "Landroid/graphics/Region;", "or", "plus", "times", "factor", "toRect", "toRectF", "toRegion", "transform", "m", "Landroid/graphics/Matrix;", "xor", "core-ktx_release"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class RectKt {
    public static final int component1(Rect $this$component1) {
        Intrinsics.checkParameterIsNotNull($this$component1, "$this$component1");
        return $this$component1.left;
    }

    public static final int component2(Rect $this$component2) {
        Intrinsics.checkParameterIsNotNull($this$component2, "$this$component2");
        return $this$component2.top;
    }

    public static final int component3(Rect $this$component3) {
        Intrinsics.checkParameterIsNotNull($this$component3, "$this$component3");
        return $this$component3.right;
    }

    public static final int component4(Rect $this$component4) {
        Intrinsics.checkParameterIsNotNull($this$component4, "$this$component4");
        return $this$component4.bottom;
    }

    public static final float component1(RectF $this$component1) {
        Intrinsics.checkParameterIsNotNull($this$component1, "$this$component1");
        return $this$component1.left;
    }

    public static final float component2(RectF $this$component2) {
        Intrinsics.checkParameterIsNotNull($this$component2, "$this$component2");
        return $this$component2.top;
    }

    public static final float component3(RectF $this$component3) {
        Intrinsics.checkParameterIsNotNull($this$component3, "$this$component3");
        return $this$component3.right;
    }

    public static final float component4(RectF $this$component4) {
        Intrinsics.checkParameterIsNotNull($this$component4, "$this$component4");
        return $this$component4.bottom;
    }

    public static final Rect plus(Rect $this$plus, Rect r) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(r, "r");
        Rect $this$apply = new Rect($this$plus);
        $this$apply.union(r);
        return $this$apply;
    }

    public static final RectF plus(RectF $this$plus, RectF r) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(r, "r");
        RectF $this$apply = new RectF($this$plus);
        $this$apply.union(r);
        return $this$apply;
    }

    public static final Rect plus(Rect $this$plus, int xy) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Rect $this$apply = new Rect($this$plus);
        $this$apply.offset(xy, xy);
        return $this$apply;
    }

    public static final RectF plus(RectF $this$plus, float xy) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        RectF $this$apply = new RectF($this$plus);
        $this$apply.offset(xy, xy);
        return $this$apply;
    }

    public static final Rect plus(Rect $this$plus, Point xy) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(xy, "xy");
        Rect $this$apply = new Rect($this$plus);
        $this$apply.offset(xy.x, xy.y);
        return $this$apply;
    }

    public static final RectF plus(RectF $this$plus, PointF xy) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(xy, "xy");
        RectF $this$apply = new RectF($this$plus);
        $this$apply.offset(xy.x, xy.y);
        return $this$apply;
    }

    public static final Region minus(Rect $this$minus, Rect r) {
        Intrinsics.checkParameterIsNotNull($this$minus, "$this$minus");
        Intrinsics.checkParameterIsNotNull(r, "r");
        Region $this$apply = new Region($this$minus);
        $this$apply.op(r, Region.Op.DIFFERENCE);
        return $this$apply;
    }

    public static final Region minus(RectF $this$minus, RectF r) {
        Intrinsics.checkParameterIsNotNull($this$minus, "$this$minus");
        Intrinsics.checkParameterIsNotNull(r, "r");
        Rect r$iv = new Rect();
        $this$minus.roundOut(r$iv);
        Region $this$apply = new Region(r$iv);
        Rect r$iv2 = new Rect();
        r.roundOut(r$iv2);
        $this$apply.op(r$iv2, Region.Op.DIFFERENCE);
        return $this$apply;
    }

    public static final Rect minus(Rect $this$minus, int xy) {
        Intrinsics.checkParameterIsNotNull($this$minus, "$this$minus");
        Rect $this$apply = new Rect($this$minus);
        $this$apply.offset(-xy, -xy);
        return $this$apply;
    }

    public static final RectF minus(RectF $this$minus, float xy) {
        Intrinsics.checkParameterIsNotNull($this$minus, "$this$minus");
        RectF $this$apply = new RectF($this$minus);
        $this$apply.offset(-xy, -xy);
        return $this$apply;
    }

    public static final Rect minus(Rect $this$minus, Point xy) {
        Intrinsics.checkParameterIsNotNull($this$minus, "$this$minus");
        Intrinsics.checkParameterIsNotNull(xy, "xy");
        Rect $this$apply = new Rect($this$minus);
        $this$apply.offset(-xy.x, -xy.y);
        return $this$apply;
    }

    public static final RectF minus(RectF $this$minus, PointF xy) {
        Intrinsics.checkParameterIsNotNull($this$minus, "$this$minus");
        Intrinsics.checkParameterIsNotNull(xy, "xy");
        RectF $this$apply = new RectF($this$minus);
        $this$apply.offset(-xy.x, -xy.y);
        return $this$apply;
    }

    public static final Rect times(Rect $this$times, int factor) {
        Intrinsics.checkParameterIsNotNull($this$times, "$this$times");
        Rect $this$apply = new Rect($this$times);
        $this$apply.top *= factor;
        $this$apply.left *= factor;
        $this$apply.right *= factor;
        $this$apply.bottom *= factor;
        return $this$apply;
    }

    public static final RectF times(RectF $this$times, int factor) {
        Intrinsics.checkParameterIsNotNull($this$times, "$this$times");
        float factor$iv = (float) factor;
        RectF $this$apply$iv = new RectF($this$times);
        $this$apply$iv.top *= factor$iv;
        $this$apply$iv.left *= factor$iv;
        $this$apply$iv.right *= factor$iv;
        $this$apply$iv.bottom *= factor$iv;
        return $this$apply$iv;
    }

    public static final RectF times(RectF $this$times, float factor) {
        Intrinsics.checkParameterIsNotNull($this$times, "$this$times");
        RectF $this$apply = new RectF($this$times);
        $this$apply.top *= factor;
        $this$apply.left *= factor;
        $this$apply.right *= factor;
        $this$apply.bottom *= factor;
        return $this$apply;
    }

    public static final Rect or(Rect $this$or, Rect r) {
        Intrinsics.checkParameterIsNotNull($this$or, "$this$or");
        Intrinsics.checkParameterIsNotNull(r, "r");
        Rect $this$apply$iv = new Rect($this$or);
        $this$apply$iv.union(r);
        return $this$apply$iv;
    }

    public static final RectF or(RectF $this$or, RectF r) {
        Intrinsics.checkParameterIsNotNull($this$or, "$this$or");
        Intrinsics.checkParameterIsNotNull(r, "r");
        RectF $this$apply$iv = new RectF($this$or);
        $this$apply$iv.union(r);
        return $this$apply$iv;
    }

    public static final Rect and(Rect $this$and, Rect r) {
        Intrinsics.checkParameterIsNotNull($this$and, "$this$and");
        Intrinsics.checkParameterIsNotNull(r, "r");
        Rect $this$apply = new Rect($this$and);
        $this$apply.intersect(r);
        return $this$apply;
    }

    public static final RectF and(RectF $this$and, RectF r) {
        Intrinsics.checkParameterIsNotNull($this$and, "$this$and");
        Intrinsics.checkParameterIsNotNull(r, "r");
        RectF $this$apply = new RectF($this$and);
        $this$apply.intersect(r);
        return $this$apply;
    }

    public static final Region xor(Rect $this$xor, Rect r) {
        Intrinsics.checkParameterIsNotNull($this$xor, "$this$xor");
        Intrinsics.checkParameterIsNotNull(r, "r");
        Region $this$apply = new Region($this$xor);
        $this$apply.op(r, Region.Op.XOR);
        return $this$apply;
    }

    public static final Region xor(RectF $this$xor, RectF r) {
        Intrinsics.checkParameterIsNotNull($this$xor, "$this$xor");
        Intrinsics.checkParameterIsNotNull(r, "r");
        Rect r$iv = new Rect();
        $this$xor.roundOut(r$iv);
        Region $this$apply = new Region(r$iv);
        Rect r$iv2 = new Rect();
        r.roundOut(r$iv2);
        $this$apply.op(r$iv2, Region.Op.XOR);
        return $this$apply;
    }

    public static final boolean contains(Rect $this$contains, Point p) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        Intrinsics.checkParameterIsNotNull(p, "p");
        return $this$contains.contains(p.x, p.y);
    }

    public static final boolean contains(RectF $this$contains, PointF p) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        Intrinsics.checkParameterIsNotNull(p, "p");
        return $this$contains.contains(p.x, p.y);
    }

    public static final RectF toRectF(Rect $this$toRectF) {
        Intrinsics.checkParameterIsNotNull($this$toRectF, "$this$toRectF");
        return new RectF($this$toRectF);
    }

    public static final Rect toRect(RectF $this$toRect) {
        Intrinsics.checkParameterIsNotNull($this$toRect, "$this$toRect");
        Rect r = new Rect();
        $this$toRect.roundOut(r);
        return r;
    }

    public static final Region toRegion(Rect $this$toRegion) {
        Intrinsics.checkParameterIsNotNull($this$toRegion, "$this$toRegion");
        return new Region($this$toRegion);
    }

    public static final Region toRegion(RectF $this$toRegion) {
        Intrinsics.checkParameterIsNotNull($this$toRegion, "$this$toRegion");
        Rect r$iv = new Rect();
        $this$toRegion.roundOut(r$iv);
        return new Region(r$iv);
    }

    public static final RectF transform(RectF $this$transform, Matrix m) {
        Intrinsics.checkParameterIsNotNull($this$transform, "$this$transform");
        Intrinsics.checkParameterIsNotNull(m, "m");
        m.mapRect($this$transform);
        return $this$transform;
    }
}
