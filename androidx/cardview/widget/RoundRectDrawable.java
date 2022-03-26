package androidx.cardview.widget;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
/* loaded from: classes.dex */
class RoundRectDrawable extends Drawable {
    private ColorStateList mBackground;
    private float mPadding;
    private float mRadius;
    private ColorStateList mTint;
    private PorterDuffColorFilter mTintFilter;
    private boolean mInsetForPadding = false;
    private boolean mInsetForRadius = true;
    private PorterDuff.Mode mTintMode = PorterDuff.Mode.SRC_IN;
    private final Paint mPaint = new Paint(5);
    private final RectF mBoundsF = new RectF();
    private final Rect mBoundsI = new Rect();

    /* JADX INFO: Access modifiers changed from: package-private */
    public RoundRectDrawable(ColorStateList backgroundColor, float radius) {
        this.mRadius = radius;
        setBackground(backgroundColor);
    }

    private void setBackground(ColorStateList color) {
        this.mBackground = color == null ? ColorStateList.valueOf(0) : color;
        this.mPaint.setColor(this.mBackground.getColorForState(getState(), this.mBackground.getDefaultColor()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPadding(float padding, boolean insetForPadding, boolean insetForRadius) {
        if (padding != this.mPadding || this.mInsetForPadding != insetForPadding || this.mInsetForRadius != insetForRadius) {
            this.mPadding = padding;
            this.mInsetForPadding = insetForPadding;
            this.mInsetForRadius = insetForRadius;
            updateBounds(null);
            invalidateSelf();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getPadding() {
        return this.mPadding;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        boolean clearColorFilter;
        Paint paint = this.mPaint;
        if (this.mTintFilter == null || paint.getColorFilter() != null) {
            clearColorFilter = false;
        } else {
            paint.setColorFilter(this.mTintFilter);
            clearColorFilter = true;
        }
        RectF rectF = this.mBoundsF;
        float f = this.mRadius;
        canvas.drawRoundRect(rectF, f, f, paint);
        if (clearColorFilter) {
            paint.setColorFilter(null);
        }
    }

    private void updateBounds(Rect bounds) {
        if (bounds == null) {
            bounds = getBounds();
        }
        this.mBoundsF.set((float) bounds.left, (float) bounds.top, (float) bounds.right, (float) bounds.bottom);
        this.mBoundsI.set(bounds);
        if (this.mInsetForPadding) {
            float vInset = RoundRectDrawableWithShadow.calculateVerticalPadding(this.mPadding, this.mRadius, this.mInsetForRadius);
            this.mBoundsI.inset((int) Math.ceil((double) RoundRectDrawableWithShadow.calculateHorizontalPadding(this.mPadding, this.mRadius, this.mInsetForRadius)), (int) Math.ceil((double) vInset));
            this.mBoundsF.set(this.mBoundsI);
        }
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        updateBounds(bounds);
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        outline.setRoundRect(this.mBoundsI, this.mRadius);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRadius(float radius) {
        if (radius != this.mRadius) {
            this.mRadius = radius;
            updateBounds(null);
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    public float getRadius() {
        return this.mRadius;
    }

    public void setColor(ColorStateList color) {
        setBackground(color);
        invalidateSelf();
    }

    public ColorStateList getColor() {
        return this.mBackground;
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintList(ColorStateList tint) {
        this.mTint = tint;
        this.mTintFilter = createTintFilter(this.mTint, this.mTintMode);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintMode(PorterDuff.Mode tintMode) {
        this.mTintMode = tintMode;
        this.mTintFilter = createTintFilter(this.mTint, this.mTintMode);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] stateSet) {
        PorterDuff.Mode mode;
        ColorStateList colorStateList = this.mBackground;
        int newColor = colorStateList.getColorForState(stateSet, colorStateList.getDefaultColor());
        boolean colorChanged = newColor != this.mPaint.getColor();
        if (colorChanged) {
            this.mPaint.setColor(newColor);
        }
        ColorStateList colorStateList2 = this.mTint;
        if (colorStateList2 == null || (mode = this.mTintMode) == null) {
            return colorChanged;
        }
        this.mTintFilter = createTintFilter(colorStateList2, mode);
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        ColorStateList colorStateList;
        ColorStateList colorStateList2 = this.mTint;
        return (colorStateList2 != null && colorStateList2.isStateful()) || ((colorStateList = this.mBackground) != null && colorStateList.isStateful()) || super.isStateful();
    }

    private PorterDuffColorFilter createTintFilter(ColorStateList tint, PorterDuff.Mode tintMode) {
        if (tint == null || tintMode == null) {
            return null;
        }
        return new PorterDuffColorFilter(tint.getColorForState(getState(), 0), tintMode);
    }
}
