package com.facebook.drawee.drawable;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import com.facebook.common.internal.Preconditions;
import com.facebook.imagepipeline.common.RotationOptions;
/* loaded from: classes.dex */
public class OrientedDrawable extends ForwardingDrawable {
    private int mExifOrientation;
    private int mRotationAngle;
    final Matrix mRotationMatrix;
    private final Matrix mTempMatrix;
    private final RectF mTempRectF;

    public OrientedDrawable(Drawable drawable, int rotationAngle) {
        this(drawable, rotationAngle, 0);
    }

    public OrientedDrawable(Drawable drawable, int rotationAngle, int exifOrientation) {
        super(drawable);
        this.mTempMatrix = new Matrix();
        this.mTempRectF = new RectF();
        boolean z = true;
        Preconditions.checkArgument(rotationAngle % 90 == 0);
        Preconditions.checkArgument((exifOrientation < 0 || exifOrientation > 8) ? false : z);
        this.mRotationMatrix = new Matrix();
        this.mRotationAngle = rotationAngle;
        this.mExifOrientation = exifOrientation;
    }

    @Override // com.facebook.drawee.drawable.ForwardingDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        int i;
        if (this.mRotationAngle > 0 || !((i = this.mExifOrientation) == 0 || i == 1)) {
            int saveCount = canvas.save();
            canvas.concat(this.mRotationMatrix);
            super.draw(canvas);
            canvas.restoreToCount(saveCount);
            return;
        }
        super.draw(canvas);
    }

    @Override // com.facebook.drawee.drawable.ForwardingDrawable, android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        int i = this.mExifOrientation;
        if (i == 5 || i == 7 || this.mRotationAngle % RotationOptions.ROTATE_180 != 0) {
            return super.getIntrinsicHeight();
        }
        return super.getIntrinsicWidth();
    }

    @Override // com.facebook.drawee.drawable.ForwardingDrawable, android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        int i = this.mExifOrientation;
        if (i == 5 || i == 7 || this.mRotationAngle % RotationOptions.ROTATE_180 != 0) {
            return super.getIntrinsicWidth();
        }
        return super.getIntrinsicHeight();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.drawee.drawable.ForwardingDrawable, android.graphics.drawable.Drawable
    public void onBoundsChange(Rect bounds) {
        int i;
        Drawable underlyingDrawable = getCurrent();
        if (this.mRotationAngle > 0 || !((i = this.mExifOrientation) == 0 || i == 1)) {
            int i2 = this.mExifOrientation;
            if (i2 == 2) {
                this.mRotationMatrix.setScale(-1.0f, 1.0f);
            } else if (i2 == 7) {
                this.mRotationMatrix.setRotate(270.0f, (float) bounds.centerX(), (float) bounds.centerY());
                this.mRotationMatrix.postScale(-1.0f, 1.0f);
            } else if (i2 == 4) {
                this.mRotationMatrix.setScale(1.0f, -1.0f);
            } else if (i2 != 5) {
                this.mRotationMatrix.setRotate((float) this.mRotationAngle, (float) bounds.centerX(), (float) bounds.centerY());
            } else {
                this.mRotationMatrix.setRotate(270.0f, (float) bounds.centerX(), (float) bounds.centerY());
                this.mRotationMatrix.postScale(1.0f, -1.0f);
            }
            this.mTempMatrix.reset();
            this.mRotationMatrix.invert(this.mTempMatrix);
            this.mTempRectF.set(bounds);
            this.mTempMatrix.mapRect(this.mTempRectF);
            underlyingDrawable.setBounds((int) this.mTempRectF.left, (int) this.mTempRectF.top, (int) this.mTempRectF.right, (int) this.mTempRectF.bottom);
            return;
        }
        underlyingDrawable.setBounds(bounds);
    }

    @Override // com.facebook.drawee.drawable.ForwardingDrawable, com.facebook.drawee.drawable.TransformCallback
    public void getTransform(Matrix transform) {
        getParentTransform(transform);
        if (!this.mRotationMatrix.isIdentity()) {
            transform.preConcat(this.mRotationMatrix);
        }
    }
}
