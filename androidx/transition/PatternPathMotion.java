package androidx.transition;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.graphics.PathParser;
import org.xmlpull.v1.XmlPullParser;
/* loaded from: classes.dex */
public class PatternPathMotion extends PathMotion {
    private Path mOriginalPatternPath;
    private final Path mPatternPath;
    private final Matrix mTempMatrix;

    public PatternPathMotion() {
        this.mPatternPath = new Path();
        this.mTempMatrix = new Matrix();
        this.mPatternPath.lineTo(1.0f, 0.0f);
        this.mOriginalPatternPath = this.mPatternPath;
    }

    public PatternPathMotion(Context context, AttributeSet attrs) {
        this.mPatternPath = new Path();
        this.mTempMatrix = new Matrix();
        TypedArray a2 = context.obtainStyledAttributes(attrs, Styleable.PATTERN_PATH_MOTION);
        try {
            String pathData = TypedArrayUtils.getNamedString(a2, (XmlPullParser) attrs, "patternPathData", 0);
            if (pathData != null) {
                setPatternPath(PathParser.createPathFromPathData(pathData));
                return;
            }
            throw new RuntimeException("pathData must be supplied for patternPathMotion");
        } finally {
            a2.recycle();
        }
    }

    public PatternPathMotion(Path patternPath) {
        this.mPatternPath = new Path();
        this.mTempMatrix = new Matrix();
        setPatternPath(patternPath);
    }

    public Path getPatternPath() {
        return this.mOriginalPatternPath;
    }

    public void setPatternPath(Path patternPath) {
        PathMeasure pathMeasure = new PathMeasure(patternPath, false);
        float[] pos = new float[2];
        pathMeasure.getPosTan(pathMeasure.getLength(), pos, null);
        float endX = pos[0];
        float endY = pos[1];
        pathMeasure.getPosTan(0.0f, pos, null);
        float startX = pos[0];
        float startY = pos[1];
        if (startX == endX && startY == endY) {
            throw new IllegalArgumentException("pattern must not end at the starting point");
        }
        this.mTempMatrix.setTranslate(-startX, -startY);
        float dx = endX - startX;
        float dy = endY - startY;
        float scale = 1.0f / distance(dx, dy);
        this.mTempMatrix.postScale(scale, scale);
        this.mTempMatrix.postRotate((float) Math.toDegrees(-Math.atan2((double) dy, (double) dx)));
        patternPath.transform(this.mTempMatrix, this.mPatternPath);
        this.mOriginalPatternPath = patternPath;
    }

    @Override // androidx.transition.PathMotion
    public Path getPath(float startX, float startY, float endX, float endY) {
        float dx = endX - startX;
        float dy = endY - startY;
        float length = distance(dx, dy);
        double angle = Math.atan2((double) dy, (double) dx);
        this.mTempMatrix.setScale(length, length);
        this.mTempMatrix.postRotate((float) Math.toDegrees(angle));
        this.mTempMatrix.postTranslate(startX, startY);
        Path path = new Path();
        this.mPatternPath.transform(this.mTempMatrix, path);
        return path;
    }

    private static float distance(float x, float y) {
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }
}
