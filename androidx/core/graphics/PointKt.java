package androidx.core.graphics;

import android.graphics.Point;
import android.graphics.PointF;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Point.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\u0018\u0002\n\u0002\b\t\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0086\n\u001a\r\u0010\u0000\u001a\u00020\u0003*\u00020\u0004H\u0086\n\u001a\r\u0010\u0005\u001a\u00020\u0001*\u00020\u0002H\u0086\n\u001a\r\u0010\u0005\u001a\u00020\u0003*\u00020\u0004H\u0086\n\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0086\n\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\b\u001a\u00020\u0001H\u0086\n\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004H\u0086\n\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\b\u001a\u00020\u0003H\u0086\n\u001a\u0015\u0010\t\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0086\n\u001a\u0015\u0010\t\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\b\u001a\u00020\u0001H\u0086\n\u001a\u0015\u0010\t\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004H\u0086\n\u001a\u0015\u0010\t\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\b\u001a\u00020\u0003H\u0086\n\u001a\r\u0010\n\u001a\u00020\u0002*\u00020\u0004H\u0086\b\u001a\r\u0010\u000b\u001a\u00020\u0004*\u00020\u0002H\u0086\b\u001a\r\u0010\f\u001a\u00020\u0002*\u00020\u0002H\u0086\n\u001a\r\u0010\f\u001a\u00020\u0004*\u00020\u0004H\u0086\n¨\u0006\r"}, d2 = {"component1", "", "Landroid/graphics/Point;", "", "Landroid/graphics/PointF;", "component2", "minus", "p", "xy", "plus", "toPoint", "toPointF", "unaryMinus", "core-ktx_release"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class PointKt {
    public static final int component1(Point $this$component1) {
        Intrinsics.checkParameterIsNotNull($this$component1, "$this$component1");
        return $this$component1.x;
    }

    public static final int component2(Point $this$component2) {
        Intrinsics.checkParameterIsNotNull($this$component2, "$this$component2");
        return $this$component2.y;
    }

    public static final float component1(PointF $this$component1) {
        Intrinsics.checkParameterIsNotNull($this$component1, "$this$component1");
        return $this$component1.x;
    }

    public static final float component2(PointF $this$component2) {
        Intrinsics.checkParameterIsNotNull($this$component2, "$this$component2");
        return $this$component2.y;
    }

    public static final Point plus(Point $this$plus, Point p) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(p, "p");
        Point $this$apply = new Point($this$plus.x, $this$plus.y);
        $this$apply.offset(p.x, p.y);
        return $this$apply;
    }

    public static final PointF plus(PointF $this$plus, PointF p) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(p, "p");
        PointF $this$apply = new PointF($this$plus.x, $this$plus.y);
        $this$apply.offset(p.x, p.y);
        return $this$apply;
    }

    public static final Point plus(Point $this$plus, int xy) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Point $this$apply = new Point($this$plus.x, $this$plus.y);
        $this$apply.offset(xy, xy);
        return $this$apply;
    }

    public static final PointF plus(PointF $this$plus, float xy) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        PointF $this$apply = new PointF($this$plus.x, $this$plus.y);
        $this$apply.offset(xy, xy);
        return $this$apply;
    }

    public static final Point minus(Point $this$minus, Point p) {
        Intrinsics.checkParameterIsNotNull($this$minus, "$this$minus");
        Intrinsics.checkParameterIsNotNull(p, "p");
        Point $this$apply = new Point($this$minus.x, $this$minus.y);
        $this$apply.offset(-p.x, -p.y);
        return $this$apply;
    }

    public static final PointF minus(PointF $this$minus, PointF p) {
        Intrinsics.checkParameterIsNotNull($this$minus, "$this$minus");
        Intrinsics.checkParameterIsNotNull(p, "p");
        PointF $this$apply = new PointF($this$minus.x, $this$minus.y);
        $this$apply.offset(-p.x, -p.y);
        return $this$apply;
    }

    public static final Point minus(Point $this$minus, int xy) {
        Intrinsics.checkParameterIsNotNull($this$minus, "$this$minus");
        Point $this$apply = new Point($this$minus.x, $this$minus.y);
        $this$apply.offset(-xy, -xy);
        return $this$apply;
    }

    public static final PointF minus(PointF $this$minus, float xy) {
        Intrinsics.checkParameterIsNotNull($this$minus, "$this$minus");
        PointF $this$apply = new PointF($this$minus.x, $this$minus.y);
        $this$apply.offset(-xy, -xy);
        return $this$apply;
    }

    public static final Point unaryMinus(Point $this$unaryMinus) {
        Intrinsics.checkParameterIsNotNull($this$unaryMinus, "$this$unaryMinus");
        return new Point(-$this$unaryMinus.x, -$this$unaryMinus.y);
    }

    public static final PointF unaryMinus(PointF $this$unaryMinus) {
        Intrinsics.checkParameterIsNotNull($this$unaryMinus, "$this$unaryMinus");
        return new PointF(-$this$unaryMinus.x, -$this$unaryMinus.y);
    }

    public static final PointF toPointF(Point $this$toPointF) {
        Intrinsics.checkParameterIsNotNull($this$toPointF, "$this$toPointF");
        return new PointF($this$toPointF);
    }

    public static final Point toPoint(PointF $this$toPoint) {
        Intrinsics.checkParameterIsNotNull($this$toPoint, "$this$toPoint");
        return new Point((int) $this$toPoint.x, (int) $this$toPoint.y);
    }
}
