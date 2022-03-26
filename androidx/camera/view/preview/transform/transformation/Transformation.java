package androidx.camera.view.preview.transform.transformation;

import android.view.View;
/* loaded from: classes.dex */
public class Transformation {
    private final float mRotation;
    private final float mScaleX;
    private final float mScaleY;
    private final float mTransX;
    private final float mTransY;

    public Transformation() {
        this(1.0f, 1.0f, 0.0f, 0.0f, 0.0f);
    }

    public Transformation(float scaleX, float scaleY, float transX, float transY, float rotation) {
        this.mScaleX = scaleX;
        this.mScaleY = scaleY;
        this.mTransX = transX;
        this.mTransY = transY;
        this.mRotation = rotation;
    }

    public float getScaleX() {
        return this.mScaleX;
    }

    public float getScaleY() {
        return this.mScaleY;
    }

    public float getTransX() {
        return this.mTransX;
    }

    public float getTransY() {
        return this.mTransY;
    }

    public float getRotation() {
        return this.mRotation;
    }

    public Transformation add(Transformation other) {
        return new Transformation(other.mScaleX * this.mScaleX, other.mScaleY * this.mScaleY, other.mTransX + this.mTransX, other.mTransY + this.mTransY, other.mRotation + this.mRotation);
    }

    public Transformation subtract(Transformation other) {
        return new Transformation(this.mScaleX / other.mScaleX, this.mScaleY / other.mScaleY, this.mTransX - other.mTransX, this.mTransY - other.mTransY, this.mRotation - other.mRotation);
    }

    public static Transformation getTransformation(View view) {
        return new Transformation(view.getScaleX(), view.getScaleY(), view.getTranslationX(), view.getTranslationY(), view.getRotation());
    }
}
