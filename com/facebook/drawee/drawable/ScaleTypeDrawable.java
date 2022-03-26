package com.facebook.drawee.drawable;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.facebook.common.internal.Objects;
import com.facebook.common.internal.Preconditions;
import com.facebook.drawee.drawable.ScalingUtils;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class ScaleTypeDrawable extends ForwardingDrawable {
    Matrix mDrawMatrix;
    @Nullable
    PointF mFocusPoint;
    ScalingUtils.ScaleType mScaleType;
    Object mScaleTypeState;
    private Matrix mTempMatrix;
    int mUnderlyingHeight;
    int mUnderlyingWidth;

    public ScaleTypeDrawable(Drawable drawable, ScalingUtils.ScaleType scaleType) {
        super((Drawable) Preconditions.checkNotNull(drawable));
        this.mFocusPoint = null;
        this.mUnderlyingWidth = 0;
        this.mUnderlyingHeight = 0;
        this.mTempMatrix = new Matrix();
        this.mScaleType = scaleType;
    }

    public ScaleTypeDrawable(Drawable drawable, ScalingUtils.ScaleType scaleType, @Nullable PointF focusPoint) {
        super((Drawable) Preconditions.checkNotNull(drawable));
        this.mFocusPoint = null;
        this.mUnderlyingWidth = 0;
        this.mUnderlyingHeight = 0;
        this.mTempMatrix = new Matrix();
        this.mScaleType = scaleType;
        this.mFocusPoint = focusPoint;
    }

    @Override // com.facebook.drawee.drawable.ForwardingDrawable
    public Drawable setCurrent(Drawable newDelegate) {
        Drawable previousDelegate = super.setCurrent(newDelegate);
        configureBounds();
        return previousDelegate;
    }

    public ScalingUtils.ScaleType getScaleType() {
        return this.mScaleType;
    }

    public void setScaleType(ScalingUtils.ScaleType scaleType) {
        if (!Objects.equal(this.mScaleType, scaleType)) {
            this.mScaleType = scaleType;
            this.mScaleTypeState = null;
            configureBounds();
            invalidateSelf();
        }
    }

    @Nullable
    public PointF getFocusPoint() {
        return this.mFocusPoint;
    }

    public void setFocusPoint(PointF focusPoint) {
        if (!Objects.equal(this.mFocusPoint, focusPoint)) {
            if (this.mFocusPoint == null) {
                this.mFocusPoint = new PointF();
            }
            this.mFocusPoint.set(focusPoint);
            configureBounds();
            invalidateSelf();
        }
    }

    @Override // com.facebook.drawee.drawable.ForwardingDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        configureBoundsIfUnderlyingChanged();
        if (this.mDrawMatrix != null) {
            int saveCount = canvas.save();
            canvas.clipRect(getBounds());
            canvas.concat(this.mDrawMatrix);
            super.draw(canvas);
            canvas.restoreToCount(saveCount);
            return;
        }
        super.draw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.drawee.drawable.ForwardingDrawable, android.graphics.drawable.Drawable
    public void onBoundsChange(Rect bounds) {
        configureBounds();
    }

    private void configureBoundsIfUnderlyingChanged() {
        boolean scaleTypeChanged = false;
        ScalingUtils.ScaleType scaleType = this.mScaleType;
        boolean underlyingChanged = false;
        if (scaleType instanceof ScalingUtils.StatefulScaleType) {
            Object state = ((ScalingUtils.StatefulScaleType) scaleType).getState();
            scaleTypeChanged = state == null || !state.equals(this.mScaleTypeState);
            this.mScaleTypeState = state;
        }
        if (!(this.mUnderlyingWidth == getCurrent().getIntrinsicWidth() && this.mUnderlyingHeight == getCurrent().getIntrinsicHeight())) {
            underlyingChanged = true;
        }
        if (underlyingChanged || scaleTypeChanged) {
            configureBounds();
        }
    }

    void configureBounds() {
        Drawable underlyingDrawable = getCurrent();
        Rect bounds = getBounds();
        int viewWidth = bounds.width();
        int viewHeight = bounds.height();
        int underlyingWidth = underlyingDrawable.getIntrinsicWidth();
        this.mUnderlyingWidth = underlyingWidth;
        int underlyingHeight = underlyingDrawable.getIntrinsicHeight();
        this.mUnderlyingHeight = underlyingHeight;
        if (underlyingWidth <= 0 || underlyingHeight <= 0) {
            underlyingDrawable.setBounds(bounds);
            this.mDrawMatrix = null;
        } else if (underlyingWidth == viewWidth && underlyingHeight == viewHeight) {
            underlyingDrawable.setBounds(bounds);
            this.mDrawMatrix = null;
        } else if (this.mScaleType == ScalingUtils.ScaleType.FIT_XY) {
            underlyingDrawable.setBounds(bounds);
            this.mDrawMatrix = null;
        } else {
            underlyingDrawable.setBounds(0, 0, underlyingWidth, underlyingHeight);
            ScalingUtils.ScaleType scaleType = this.mScaleType;
            Matrix matrix = this.mTempMatrix;
            PointF pointF = this.mFocusPoint;
            float f = pointF != null ? pointF.x : 0.5f;
            PointF pointF2 = this.mFocusPoint;
            scaleType.getTransform(matrix, bounds, underlyingWidth, underlyingHeight, f, pointF2 != null ? pointF2.y : 0.5f);
            this.mDrawMatrix = this.mTempMatrix;
        }
    }

    @Override // com.facebook.drawee.drawable.ForwardingDrawable, com.facebook.drawee.drawable.TransformCallback
    public void getTransform(Matrix transform) {
        getParentTransform(transform);
        configureBoundsIfUnderlyingChanged();
        Matrix matrix = this.mDrawMatrix;
        if (matrix != null) {
            transform.preConcat(matrix);
        }
    }
}
