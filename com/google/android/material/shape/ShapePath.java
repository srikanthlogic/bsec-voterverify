package com.google.android.material.shape;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class ShapePath {
    public float endX;
    public float endY;
    private final List<PathOperation> operations = new ArrayList();
    public float startX;
    public float startY;

    /* loaded from: classes.dex */
    public static abstract class PathOperation {
        protected final Matrix matrix = new Matrix();

        public abstract void applyToPath(Matrix matrix, Path path);
    }

    public ShapePath() {
        reset(0.0f, 0.0f);
    }

    public ShapePath(float startX, float startY) {
        reset(startX, startY);
    }

    public void reset(float startX, float startY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = startX;
        this.endY = startY;
        this.operations.clear();
    }

    public void lineTo(float x, float y) {
        PathLineOperation operation = new PathLineOperation();
        operation.x = x;
        operation.y = y;
        this.operations.add(operation);
        this.endX = x;
        this.endY = y;
    }

    public void quadToPoint(float controlX, float controlY, float toX, float toY) {
        PathQuadOperation operation = new PathQuadOperation();
        operation.controlX = controlX;
        operation.controlY = controlY;
        operation.endX = toX;
        operation.endY = toY;
        this.operations.add(operation);
        this.endX = toX;
        this.endY = toY;
    }

    public void addArc(float left, float top, float right, float bottom, float startAngle, float sweepAngle) {
        PathArcOperation operation = new PathArcOperation(left, top, right, bottom);
        operation.startAngle = startAngle;
        operation.sweepAngle = sweepAngle;
        this.operations.add(operation);
        this.endX = ((left + right) * 0.5f) + (((right - left) / 2.0f) * ((float) Math.cos(Math.toRadians((double) (startAngle + sweepAngle)))));
        this.endY = ((top + bottom) * 0.5f) + (((bottom - top) / 2.0f) * ((float) Math.sin(Math.toRadians((double) (startAngle + sweepAngle)))));
    }

    public void applyToPath(Matrix transform, Path path) {
        int size = this.operations.size();
        for (int i = 0; i < size; i++) {
            this.operations.get(i).applyToPath(transform, path);
        }
    }

    /* loaded from: classes.dex */
    public static class PathLineOperation extends PathOperation {
        private float x;
        private float y;

        @Override // com.google.android.material.shape.ShapePath.PathOperation
        public void applyToPath(Matrix transform, Path path) {
            Matrix inverse = this.matrix;
            transform.invert(inverse);
            path.transform(inverse);
            path.lineTo(this.x, this.y);
            path.transform(transform);
        }
    }

    /* loaded from: classes.dex */
    public static class PathQuadOperation extends PathOperation {
        public float controlX;
        public float controlY;
        public float endX;
        public float endY;

        @Override // com.google.android.material.shape.ShapePath.PathOperation
        public void applyToPath(Matrix transform, Path path) {
            Matrix inverse = this.matrix;
            transform.invert(inverse);
            path.transform(inverse);
            path.quadTo(this.controlX, this.controlY, this.endX, this.endY);
            path.transform(transform);
        }
    }

    /* loaded from: classes.dex */
    public static class PathArcOperation extends PathOperation {
        private static final RectF rectF = new RectF();
        public float bottom;
        public float left;
        public float right;
        public float startAngle;
        public float sweepAngle;
        public float top;

        public PathArcOperation(float left, float top, float right, float bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        @Override // com.google.android.material.shape.ShapePath.PathOperation
        public void applyToPath(Matrix transform, Path path) {
            Matrix inverse = this.matrix;
            transform.invert(inverse);
            path.transform(inverse);
            rectF.set(this.left, this.top, this.right, this.bottom);
            path.arcTo(rectF, this.startAngle, this.sweepAngle, false);
            path.transform(transform);
        }
    }
}
