package com.facebook.drawee.drawable;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import com.facebook.common.internal.Preconditions;
import java.util.Arrays;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class RoundedCornersDrawable extends ForwardingDrawable implements Rounded {
    @Nullable
    private RectF mInsideBorderBounds;
    @Nullable
    private Matrix mInsideBorderTransform;
    Type mType = Type.OVERLAY_COLOR;
    private final RectF mBounds = new RectF();
    private final float[] mRadii = new float[8];
    final float[] mBorderRadii = new float[8];
    final Paint mPaint = new Paint(1);
    private boolean mIsCircle = false;
    private float mBorderWidth = 0.0f;
    private int mBorderColor = 0;
    private int mOverlayColor = 0;
    private float mPadding = 0.0f;
    private boolean mScaleDownInsideBorders = false;
    private final Path mPath = new Path();
    private final Path mBorderPath = new Path();
    private final RectF mTempRectangle = new RectF();

    /* loaded from: classes.dex */
    public enum Type {
        OVERLAY_COLOR,
        CLIPPING
    }

    public RoundedCornersDrawable(Drawable drawable) {
        super((Drawable) Preconditions.checkNotNull(drawable));
    }

    public void setType(Type type) {
        this.mType = type;
        invalidateSelf();
    }

    @Override // com.facebook.drawee.drawable.Rounded
    public void setCircle(boolean isCircle) {
        this.mIsCircle = isCircle;
        updatePath();
        invalidateSelf();
    }

    @Override // com.facebook.drawee.drawable.Rounded
    public boolean isCircle() {
        return this.mIsCircle;
    }

    @Override // com.facebook.drawee.drawable.Rounded
    public void setRadius(float radius) {
        Arrays.fill(this.mRadii, radius);
        updatePath();
        invalidateSelf();
    }

    @Override // com.facebook.drawee.drawable.Rounded
    public void setRadii(float[] radii) {
        if (radii == null) {
            Arrays.fill(this.mRadii, 0.0f);
        } else {
            Preconditions.checkArgument(radii.length == 8, "radii should have exactly 8 values");
            System.arraycopy(radii, 0, this.mRadii, 0, 8);
        }
        updatePath();
        invalidateSelf();
    }

    @Override // com.facebook.drawee.drawable.Rounded
    public float[] getRadii() {
        return this.mRadii;
    }

    public void setOverlayColor(int overlayColor) {
        this.mOverlayColor = overlayColor;
        invalidateSelf();
    }

    public int getOverlayColor() {
        return this.mOverlayColor;
    }

    @Override // com.facebook.drawee.drawable.Rounded
    public void setBorder(int color, float width) {
        this.mBorderColor = color;
        this.mBorderWidth = width;
        updatePath();
        invalidateSelf();
    }

    @Override // com.facebook.drawee.drawable.Rounded
    public int getBorderColor() {
        return this.mBorderColor;
    }

    @Override // com.facebook.drawee.drawable.Rounded
    public float getBorderWidth() {
        return this.mBorderWidth;
    }

    @Override // com.facebook.drawee.drawable.Rounded
    public void setPadding(float padding) {
        this.mPadding = padding;
        updatePath();
        invalidateSelf();
    }

    @Override // com.facebook.drawee.drawable.Rounded
    public float getPadding() {
        return this.mPadding;
    }

    @Override // com.facebook.drawee.drawable.Rounded
    public void setScaleDownInsideBorders(boolean scaleDownInsideBorders) {
        this.mScaleDownInsideBorders = scaleDownInsideBorders;
        updatePath();
        invalidateSelf();
    }

    @Override // com.facebook.drawee.drawable.Rounded
    public boolean getScaleDownInsideBorders() {
        return this.mScaleDownInsideBorders;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.drawee.drawable.ForwardingDrawable, android.graphics.drawable.Drawable
    public void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        updatePath();
    }

    private void updatePath() {
        float[] fArr;
        this.mPath.reset();
        this.mBorderPath.reset();
        this.mTempRectangle.set(getBounds());
        RectF rectF = this.mTempRectangle;
        float f = this.mPadding;
        rectF.inset(f, f);
        this.mPath.addRect(this.mTempRectangle, Path.Direction.CW);
        if (this.mIsCircle) {
            this.mPath.addCircle(this.mTempRectangle.centerX(), this.mTempRectangle.centerY(), Math.min(this.mTempRectangle.width(), this.mTempRectangle.height()) / 2.0f, Path.Direction.CW);
        } else {
            this.mPath.addRoundRect(this.mTempRectangle, this.mRadii, Path.Direction.CW);
        }
        RectF rectF2 = this.mTempRectangle;
        float f2 = this.mPadding;
        rectF2.inset(-f2, -f2);
        RectF rectF3 = this.mTempRectangle;
        float f3 = this.mBorderWidth;
        rectF3.inset(f3 / 2.0f, f3 / 2.0f);
        if (this.mIsCircle) {
            this.mBorderPath.addCircle(this.mTempRectangle.centerX(), this.mTempRectangle.centerY(), Math.min(this.mTempRectangle.width(), this.mTempRectangle.height()) / 2.0f, Path.Direction.CW);
        } else {
            int i = 0;
            while (true) {
                fArr = this.mBorderRadii;
                if (i >= fArr.length) {
                    break;
                }
                fArr[i] = (this.mRadii[i] + this.mPadding) - (this.mBorderWidth / 2.0f);
                i++;
            }
            this.mBorderPath.addRoundRect(this.mTempRectangle, fArr, Path.Direction.CW);
        }
        RectF rectF4 = this.mTempRectangle;
        float f4 = this.mBorderWidth;
        rectF4.inset((-f4) / 2.0f, (-f4) / 2.0f);
    }

    /* renamed from: com.facebook.drawee.drawable.RoundedCornersDrawable$1  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$facebook$drawee$drawable$RoundedCornersDrawable$Type = new int[Type.values().length];

        static {
            try {
                $SwitchMap$com$facebook$drawee$drawable$RoundedCornersDrawable$Type[Type.CLIPPING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$facebook$drawee$drawable$RoundedCornersDrawable$Type[Type.OVERLAY_COLOR.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    @Override // com.facebook.drawee.drawable.ForwardingDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        this.mBounds.set(getBounds());
        int i = AnonymousClass1.$SwitchMap$com$facebook$drawee$drawable$RoundedCornersDrawable$Type[this.mType.ordinal()];
        if (i == 1) {
            int saveCount = canvas.save();
            this.mPath.setFillType(Path.FillType.EVEN_ODD);
            canvas.clipPath(this.mPath);
            super.draw(canvas);
            canvas.restoreToCount(saveCount);
        } else if (i == 2) {
            if (this.mScaleDownInsideBorders) {
                RectF rectF = this.mInsideBorderBounds;
                if (rectF == null) {
                    this.mInsideBorderBounds = new RectF(this.mBounds);
                    this.mInsideBorderTransform = new Matrix();
                } else {
                    rectF.set(this.mBounds);
                }
                RectF rectF2 = this.mInsideBorderBounds;
                float f = this.mBorderWidth;
                rectF2.inset(f, f);
                this.mInsideBorderTransform.setRectToRect(this.mBounds, this.mInsideBorderBounds, Matrix.ScaleToFit.FILL);
                int saveCount2 = canvas.save();
                canvas.clipRect(this.mBounds);
                canvas.concat(this.mInsideBorderTransform);
                super.draw(canvas);
                canvas.restoreToCount(saveCount2);
            } else {
                super.draw(canvas);
            }
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setColor(this.mOverlayColor);
            this.mPaint.setStrokeWidth(0.0f);
            this.mPath.setFillType(Path.FillType.EVEN_ODD);
            canvas.drawPath(this.mPath, this.mPaint);
            if (this.mIsCircle) {
                float paddingH = ((this.mBounds.width() - this.mBounds.height()) + this.mBorderWidth) / 2.0f;
                float paddingV = ((this.mBounds.height() - this.mBounds.width()) + this.mBorderWidth) / 2.0f;
                if (paddingH > 0.0f) {
                    canvas.drawRect(this.mBounds.left, this.mBounds.top, this.mBounds.left + paddingH, this.mBounds.bottom, this.mPaint);
                    canvas.drawRect(this.mBounds.right - paddingH, this.mBounds.top, this.mBounds.right, this.mBounds.bottom, this.mPaint);
                }
                if (paddingV > 0.0f) {
                    canvas.drawRect(this.mBounds.left, this.mBounds.top, this.mBounds.right, this.mBounds.top + paddingV, this.mPaint);
                    canvas.drawRect(this.mBounds.left, this.mBounds.bottom - paddingV, this.mBounds.right, this.mBounds.bottom, this.mPaint);
                }
            }
        }
        if (this.mBorderColor != 0) {
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setColor(this.mBorderColor);
            this.mPaint.setStrokeWidth(this.mBorderWidth);
            this.mPath.setFillType(Path.FillType.EVEN_ODD);
            canvas.drawPath(this.mBorderPath, this.mPaint);
        }
    }
}
