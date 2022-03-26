package com.google.android.material.shadow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import androidx.appcompat.graphics.drawable.DrawableWrapper;
import androidx.core.content.ContextCompat;
import com.google.android.material.R;
/* loaded from: classes.dex */
public class ShadowDrawableWrapper extends DrawableWrapper {
    static final double COS_45 = Math.cos(Math.toRadians(45.0d));
    static final float SHADOW_BOTTOM_SCALE = 1.0f;
    static final float SHADOW_HORIZ_SCALE = 0.5f;
    static final float SHADOW_MULTIPLIER = 1.5f;
    static final float SHADOW_TOP_SCALE = 0.25f;
    float cornerRadius;
    Path cornerShadowPath;
    float maxShadowSize;
    float rawMaxShadowSize;
    float rawShadowSize;
    private float rotation;
    private final int shadowEndColor;
    private final int shadowMiddleColor;
    float shadowSize;
    private final int shadowStartColor;
    private boolean dirty = true;
    private boolean addPaddingForCorners = true;
    private boolean printedShadowClipWarning = false;
    final Paint cornerShadowPaint = new Paint(5);
    final RectF contentBounds = new RectF();
    final Paint edgeShadowPaint = new Paint(this.cornerShadowPaint);

    public ShadowDrawableWrapper(Context context, Drawable content, float radius, float shadowSize, float maxShadowSize) {
        super(content);
        this.shadowStartColor = ContextCompat.getColor(context, R.color.design_fab_shadow_start_color);
        this.shadowMiddleColor = ContextCompat.getColor(context, R.color.design_fab_shadow_mid_color);
        this.shadowEndColor = ContextCompat.getColor(context, R.color.design_fab_shadow_end_color);
        this.cornerShadowPaint.setStyle(Paint.Style.FILL);
        this.cornerRadius = (float) Math.round(radius);
        this.edgeShadowPaint.setAntiAlias(false);
        setShadowSize(shadowSize, maxShadowSize);
    }

    private static int toEven(float value) {
        int i = Math.round(value);
        return i % 2 == 1 ? i - 1 : i;
    }

    public void setAddPaddingForCorners(boolean addPaddingForCorners) {
        this.addPaddingForCorners = addPaddingForCorners;
        invalidateSelf();
    }

    @Override // androidx.appcompat.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        super.setAlpha(alpha);
        this.cornerShadowPaint.setAlpha(alpha);
        this.edgeShadowPaint.setAlpha(alpha);
    }

    @Override // androidx.appcompat.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect bounds) {
        this.dirty = true;
    }

    public void setShadowSize(float shadowSize, float maxShadowSize) {
        if (shadowSize < 0.0f || maxShadowSize < 0.0f) {
            throw new IllegalArgumentException("invalid shadow size");
        }
        float shadowSize2 = (float) toEven(shadowSize);
        float maxShadowSize2 = (float) toEven(maxShadowSize);
        if (shadowSize2 > maxShadowSize2) {
            shadowSize2 = maxShadowSize2;
            if (!this.printedShadowClipWarning) {
                this.printedShadowClipWarning = true;
            }
        }
        if (this.rawShadowSize != shadowSize2 || this.rawMaxShadowSize != maxShadowSize2) {
            this.rawShadowSize = shadowSize2;
            this.rawMaxShadowSize = maxShadowSize2;
            this.shadowSize = (float) Math.round(SHADOW_MULTIPLIER * shadowSize2);
            this.maxShadowSize = maxShadowSize2;
            this.dirty = true;
            invalidateSelf();
        }
    }

    public void setShadowSize(float size) {
        setShadowSize(size, this.rawMaxShadowSize);
    }

    public float getShadowSize() {
        return this.rawShadowSize;
    }

    @Override // androidx.appcompat.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public boolean getPadding(Rect padding) {
        int vOffset = (int) Math.ceil((double) calculateVerticalPadding(this.rawMaxShadowSize, this.cornerRadius, this.addPaddingForCorners));
        int hOffset = (int) Math.ceil((double) calculateHorizontalPadding(this.rawMaxShadowSize, this.cornerRadius, this.addPaddingForCorners));
        padding.set(hOffset, vOffset, hOffset, vOffset);
        return true;
    }

    public static float calculateVerticalPadding(float maxShadowSize, float cornerRadius, boolean addPaddingForCorners) {
        if (addPaddingForCorners) {
            return (float) (((double) (SHADOW_MULTIPLIER * maxShadowSize)) + ((1.0d - COS_45) * ((double) cornerRadius)));
        }
        return SHADOW_MULTIPLIER * maxShadowSize;
    }

    public static float calculateHorizontalPadding(float maxShadowSize, float cornerRadius, boolean addPaddingForCorners) {
        if (addPaddingForCorners) {
            return (float) (((double) maxShadowSize) + ((1.0d - COS_45) * ((double) cornerRadius)));
        }
        return maxShadowSize;
    }

    @Override // androidx.appcompat.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    public void setCornerRadius(float radius) {
        float radius2 = (float) Math.round(radius);
        if (this.cornerRadius != radius2) {
            this.cornerRadius = radius2;
            this.dirty = true;
            invalidateSelf();
        }
    }

    @Override // androidx.appcompat.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (this.dirty) {
            buildComponents(getBounds());
            this.dirty = false;
        }
        drawShadow(canvas);
        super.draw(canvas);
    }

    public final void setRotation(float rotation) {
        if (this.rotation != rotation) {
            this.rotation = rotation;
            invalidateSelf();
        }
    }

    private void drawShadow(Canvas canvas) {
        float shadowScaleHorizontal;
        float shadowScaleTop;
        float shadowOffsetHorizontal;
        int saved;
        float shadowScaleBottom;
        float shadowScaleHorizontal2;
        int rotateSaved = canvas.save();
        canvas.rotate(this.rotation, this.contentBounds.centerX(), this.contentBounds.centerY());
        float edgeShadowTop = (-this.cornerRadius) - this.shadowSize;
        float shadowOffset = this.cornerRadius;
        boolean drawVerticalEdges = true;
        boolean drawHorizontalEdges = this.contentBounds.width() - (shadowOffset * 2.0f) > 0.0f;
        if (this.contentBounds.height() - (shadowOffset * 2.0f) <= 0.0f) {
            drawVerticalEdges = false;
        }
        float f = this.rawShadowSize;
        float shadowOffsetTop = f - (SHADOW_TOP_SCALE * f);
        float shadowScaleHorizontal3 = shadowOffset / (shadowOffset + (f - (SHADOW_HORIZ_SCALE * f)));
        float shadowScaleTop2 = shadowOffset / (shadowOffset + shadowOffsetTop);
        float shadowScaleBottom2 = shadowOffset / (shadowOffset + (f - (f * 1.0f)));
        int saved2 = canvas.save();
        canvas.translate(this.contentBounds.left + shadowOffset, this.contentBounds.top + shadowOffset);
        canvas.scale(shadowScaleHorizontal3, shadowScaleTop2);
        canvas.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (drawHorizontalEdges) {
            canvas.scale(1.0f / shadowScaleHorizontal3, 1.0f);
            saved = saved2;
            shadowScaleBottom = shadowScaleBottom2;
            shadowScaleTop = shadowScaleTop2;
            shadowScaleHorizontal = shadowScaleHorizontal3;
            shadowOffsetHorizontal = 1.0f;
            canvas.drawRect(0.0f, edgeShadowTop, this.contentBounds.width() - (shadowOffset * 2.0f), -this.cornerRadius, this.edgeShadowPaint);
        } else {
            shadowScaleBottom = shadowScaleBottom2;
            shadowScaleTop = shadowScaleTop2;
            shadowScaleHorizontal = shadowScaleHorizontal3;
            saved = saved2;
            shadowOffsetHorizontal = 1.0f;
        }
        canvas.restoreToCount(saved);
        int saved3 = canvas.save();
        canvas.translate(this.contentBounds.right - shadowOffset, this.contentBounds.bottom - shadowOffset);
        canvas.scale(shadowScaleHorizontal, shadowScaleBottom);
        canvas.rotate(180.0f);
        canvas.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (drawHorizontalEdges) {
            canvas.scale(shadowOffsetHorizontal / shadowScaleHorizontal, shadowOffsetHorizontal);
            shadowScaleHorizontal2 = shadowScaleHorizontal;
            canvas.drawRect(0.0f, edgeShadowTop, this.contentBounds.width() - (shadowOffset * 2.0f), (-this.cornerRadius) + this.shadowSize, this.edgeShadowPaint);
        } else {
            shadowScaleHorizontal2 = shadowScaleHorizontal;
        }
        canvas.restoreToCount(saved3);
        int saved4 = canvas.save();
        canvas.translate(this.contentBounds.left + shadowOffset, this.contentBounds.bottom - shadowOffset);
        canvas.scale(shadowScaleHorizontal2, shadowScaleBottom);
        canvas.rotate(270.0f);
        canvas.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (drawVerticalEdges) {
            canvas.scale(1.0f / shadowScaleBottom, 1.0f);
            canvas.drawRect(0.0f, edgeShadowTop, this.contentBounds.height() - (shadowOffset * 2.0f), -this.cornerRadius, this.edgeShadowPaint);
        }
        canvas.restoreToCount(saved4);
        int saved5 = canvas.save();
        canvas.translate(this.contentBounds.right - shadowOffset, this.contentBounds.top + shadowOffset);
        canvas.scale(shadowScaleHorizontal2, shadowScaleTop);
        canvas.rotate(90.0f);
        canvas.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (drawVerticalEdges) {
            canvas.scale(1.0f / shadowScaleTop, 1.0f);
            canvas.drawRect(0.0f, edgeShadowTop, this.contentBounds.height() - (2.0f * shadowOffset), -this.cornerRadius, this.edgeShadowPaint);
        }
        canvas.restoreToCount(saved5);
        canvas.restoreToCount(rotateSaved);
    }

    private void buildShadowCorners() {
        int i;
        float f = this.cornerRadius;
        RectF innerBounds = new RectF(-f, -f, f, f);
        RectF outerBounds = new RectF(innerBounds);
        float f2 = this.shadowSize;
        outerBounds.inset(-f2, -f2);
        Path path = this.cornerShadowPath;
        if (path == null) {
            this.cornerShadowPath = new Path();
        } else {
            path.reset();
        }
        this.cornerShadowPath.setFillType(Path.FillType.EVEN_ODD);
        this.cornerShadowPath.moveTo(-this.cornerRadius, 0.0f);
        this.cornerShadowPath.rLineTo(-this.shadowSize, 0.0f);
        this.cornerShadowPath.arcTo(outerBounds, 180.0f, 90.0f, false);
        this.cornerShadowPath.arcTo(innerBounds, 270.0f, -90.0f, false);
        this.cornerShadowPath.close();
        float shadowRadius = -outerBounds.top;
        if (shadowRadius > 0.0f) {
            float startRatio = this.cornerRadius / shadowRadius;
            i = 3;
            this.cornerShadowPaint.setShader(new RadialGradient(0.0f, 0.0f, shadowRadius, new int[]{0, this.shadowStartColor, this.shadowMiddleColor, this.shadowEndColor}, new float[]{0.0f, startRatio, startRatio + ((1.0f - startRatio) / 2.0f), 1.0f}, Shader.TileMode.CLAMP));
        } else {
            i = 3;
        }
        Paint paint = this.edgeShadowPaint;
        float f3 = innerBounds.top;
        float f4 = outerBounds.top;
        int[] iArr = new int[i];
        iArr[0] = this.shadowStartColor;
        iArr[1] = this.shadowMiddleColor;
        iArr[2] = this.shadowEndColor;
        float[] fArr = new float[i];
        // fill-array-data instruction
        fArr[0] = 0.0f;
        fArr[1] = 0.5f;
        fArr[2] = 1.0f;
        paint.setShader(new LinearGradient(0.0f, f3, 0.0f, f4, iArr, fArr, Shader.TileMode.CLAMP));
        this.edgeShadowPaint.setAntiAlias(false);
    }

    private void buildComponents(Rect bounds) {
        float verticalOffset = this.rawMaxShadowSize * SHADOW_MULTIPLIER;
        this.contentBounds.set(((float) bounds.left) + this.rawMaxShadowSize, ((float) bounds.top) + verticalOffset, ((float) bounds.right) - this.rawMaxShadowSize, ((float) bounds.bottom) - verticalOffset);
        getWrappedDrawable().setBounds((int) this.contentBounds.left, (int) this.contentBounds.top, (int) this.contentBounds.right, (int) this.contentBounds.bottom);
        buildShadowCorners();
    }

    public float getCornerRadius() {
        return this.cornerRadius;
    }

    public void setMaxShadowSize(float size) {
        setShadowSize(this.rawShadowSize, size);
    }

    public float getMaxShadowSize() {
        return this.rawMaxShadowSize;
    }

    public float getMinWidth() {
        float f = this.rawMaxShadowSize;
        return (this.rawMaxShadowSize * 2.0f) + (Math.max(f, this.cornerRadius + (f / 2.0f)) * 2.0f);
    }

    public float getMinHeight() {
        float f = this.rawMaxShadowSize;
        return (this.rawMaxShadowSize * SHADOW_MULTIPLIER * 2.0f) + (Math.max(f, this.cornerRadius + ((f * SHADOW_MULTIPLIER) / 2.0f)) * 2.0f);
    }
}
