package androidx.core.graphics.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Drawable.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\u001a*\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0003\u0010\u0003\u001a\u00020\u00042\b\b\u0003\u0010\u0005\u001a\u00020\u00042\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u001a2\u0010\b\u001a\u00020\t*\u00020\u00022\b\b\u0003\u0010\n\u001a\u00020\u00042\b\b\u0003\u0010\u000b\u001a\u00020\u00042\b\b\u0003\u0010\f\u001a\u00020\u00042\b\b\u0003\u0010\r\u001a\u00020\u0004¨\u0006\u000e"}, d2 = {"toBitmap", "Landroid/graphics/Bitmap;", "Landroid/graphics/drawable/Drawable;", "width", "", "height", "config", "Landroid/graphics/Bitmap$Config;", "updateBounds", "", "left", "top", "right", "bottom", "core-ktx_release"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class DrawableKt {
    public static /* synthetic */ Bitmap toBitmap$default(Drawable drawable, int i, int i2, Bitmap.Config config, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = drawable.getIntrinsicWidth();
        }
        if ((i3 & 2) != 0) {
            i2 = drawable.getIntrinsicHeight();
        }
        if ((i3 & 4) != 0) {
            config = null;
        }
        return toBitmap(drawable, i, i2, config);
    }

    /* JADX INFO: Multiple debug info for r0v2 android.graphics.Rect: [D('$this$component2$iv' android.graphics.Rect), D('$this$component4$iv' android.graphics.Rect), D('$this$component3$iv' android.graphics.Rect), D('$this$component1$iv' android.graphics.Rect)] */
    /* JADX INFO: Multiple debug info for r0v3 int: [D('$this$component4$iv' android.graphics.Rect), D('oldBottom' int)] */
    /* JADX INFO: Multiple debug info for r2v1 int: [D('oldLeft' int), D('$this$component1$iv' android.graphics.Rect)] */
    /* JADX INFO: Multiple debug info for r3v2 int: [D('$this$component2$iv' android.graphics.Rect), D('oldTop' int)] */
    /* JADX INFO: Multiple debug info for r4v2 int: [D('$this$component3$iv' android.graphics.Rect), D('oldRight' int)] */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x001b, code lost:
        if (r0.getConfig() == r10) goto L_0x001d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static final Bitmap toBitmap(Drawable $this$toBitmap, int width, int height, Bitmap.Config config) {
        Intrinsics.checkParameterIsNotNull($this$toBitmap, "$this$toBitmap");
        if ($this$toBitmap instanceof BitmapDrawable) {
            if (config != null) {
                Bitmap bitmap = ((BitmapDrawable) $this$toBitmap).getBitmap();
                Intrinsics.checkExpressionValueIsNotNull(bitmap, "bitmap");
            }
            if (width == ((BitmapDrawable) $this$toBitmap).getIntrinsicWidth() && height == ((BitmapDrawable) $this$toBitmap).getIntrinsicHeight()) {
                Bitmap bitmap2 = ((BitmapDrawable) $this$toBitmap).getBitmap();
                Intrinsics.checkExpressionValueIsNotNull(bitmap2, "bitmap");
                return bitmap2;
            }
            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(((BitmapDrawable) $this$toBitmap).getBitmap(), width, height, true);
            Intrinsics.checkExpressionValueIsNotNull(createScaledBitmap, "Bitmap.createScaledBitma…map, width, height, true)");
            return createScaledBitmap;
        }
        Rect $this$component1$iv = $this$toBitmap.getBounds();
        int oldLeft = $this$component1$iv.left;
        int oldTop = $this$component1$iv.top;
        int oldRight = $this$component1$iv.right;
        int oldBottom = $this$component1$iv.bottom;
        Bitmap bitmap3 = Bitmap.createBitmap(width, height, config != null ? config : Bitmap.Config.ARGB_8888);
        $this$toBitmap.setBounds(0, 0, width, height);
        $this$toBitmap.draw(new Canvas(bitmap3));
        $this$toBitmap.setBounds(oldLeft, oldTop, oldRight, oldBottom);
        Intrinsics.checkExpressionValueIsNotNull(bitmap3, "bitmap");
        return bitmap3;
    }

    public static /* synthetic */ void updateBounds$default(Drawable drawable, int i, int i2, int i3, int i4, int i5, Object obj) {
        if ((i5 & 1) != 0) {
            i = drawable.getBounds().left;
        }
        if ((i5 & 2) != 0) {
            i2 = drawable.getBounds().top;
        }
        if ((i5 & 4) != 0) {
            i3 = drawable.getBounds().right;
        }
        if ((i5 & 8) != 0) {
            i4 = drawable.getBounds().bottom;
        }
        updateBounds(drawable, i, i2, i3, i4);
    }

    public static final void updateBounds(Drawable $this$updateBounds, int left, int top, int right, int bottom) {
        Intrinsics.checkParameterIsNotNull($this$updateBounds, "$this$updateBounds");
        $this$updateBounds.setBounds(left, top, right, bottom);
    }
}
