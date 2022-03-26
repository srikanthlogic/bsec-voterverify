package androidx.camera.core;

import android.util.Rational;
import androidx.core.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes.dex */
public final class ViewPort {
    public static final int FILL_CENTER = 1;
    public static final int FILL_END = 2;
    public static final int FILL_START = 0;
    public static final int FIT = 3;
    private Rational mAspectRatio;
    private int mLayoutDirection;
    private int mRotation;
    private int mScaleType;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface LayoutDirection {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface ScaleType {
    }

    ViewPort(int scaleType, Rational aspectRatio, int rotation, int layoutDirection) {
        this.mScaleType = scaleType;
        this.mAspectRatio = aspectRatio;
        this.mRotation = rotation;
        this.mLayoutDirection = layoutDirection;
    }

    public Rational getAspectRatio() {
        return this.mAspectRatio;
    }

    public int getRotation() {
        return this.mRotation;
    }

    public int getScaleType() {
        return this.mScaleType;
    }

    public int getLayoutDirection() {
        return this.mLayoutDirection;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private static final int DEFAULT_LAYOUT_DIRECTION = 0;
        private static final int DEFAULT_SCALE_TYPE = 1;
        private final Rational mAspectRatio;
        private final int mRotation;
        private int mScaleType = 1;
        private int mLayoutDirection = 0;

        public Builder(Rational aspectRatio, int rotation) {
            this.mAspectRatio = aspectRatio;
            this.mRotation = rotation;
        }

        public Builder setScaleType(int scaleType) {
            this.mScaleType = scaleType;
            return this;
        }

        public Builder setLayoutDirection(int layoutDirection) {
            this.mLayoutDirection = layoutDirection;
            return this;
        }

        public ViewPort build() {
            Preconditions.checkNotNull(this.mAspectRatio, "The crop aspect ratio must be set.");
            return new ViewPort(this.mScaleType, this.mAspectRatio, this.mRotation, this.mLayoutDirection);
        }
    }
}
