package androidx.constraintlayout.motion.widget;

import android.view.View;
import androidx.constraintlayout.motion.utils.Easing;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintSet;
import java.util.Arrays;
import java.util.LinkedHashMap;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class MotionPaths implements Comparable<MotionPaths> {
    static final int CARTESIAN = 2;
    public static final boolean DEBUG = false;
    static final int OFF_HEIGHT = 4;
    static final int OFF_PATH_ROTATE = 5;
    static final int OFF_POSITION = 0;
    static final int OFF_WIDTH = 3;
    static final int OFF_X = 1;
    static final int OFF_Y = 2;
    public static final boolean OLD_WAY = false;
    static final int PERPENDICULAR = 1;
    static final int SCREEN = 3;
    public static final String TAG = "MotionPaths";
    static String[] names = {"position", "x", "y", "width", "height", "pathRotate"};
    LinkedHashMap<String, ConstraintAttribute> attributes;
    float height;
    int mDrawPath;
    Easing mKeyFrameEasing;
    int mMode;
    int mPathMotionArc;
    float mPathRotate;
    float mProgress;
    double[] mTempDelta;
    double[] mTempValue;
    float position;
    float time;
    float width;
    float x;
    float y;

    public MotionPaths() {
        this.mDrawPath = 0;
        this.mPathRotate = Float.NaN;
        this.mProgress = Float.NaN;
        this.mPathMotionArc = Key.UNSET;
        this.attributes = new LinkedHashMap<>();
        this.mMode = 0;
        this.mTempValue = new double[18];
        this.mTempDelta = new double[18];
    }

    void initCartesian(KeyPosition c, MotionPaths startTimePoint, MotionPaths endTimePoint) {
        float position = ((float) c.mFramePosition) / 100.0f;
        this.time = position;
        this.mDrawPath = c.mDrawPath;
        float scaleWidth = Float.isNaN(c.mPercentWidth) ? position : c.mPercentWidth;
        float scaleHeight = Float.isNaN(c.mPercentHeight) ? position : c.mPercentHeight;
        float f = endTimePoint.width;
        float f2 = startTimePoint.width;
        float scaleX = f - f2;
        float f3 = endTimePoint.height;
        float f4 = startTimePoint.height;
        float scaleY = f3 - f4;
        this.position = this.time;
        float f5 = startTimePoint.x;
        float position2 = startTimePoint.y;
        float endCenterX = endTimePoint.x + (f / 2.0f);
        float endCenterY = endTimePoint.y + (f3 / 2.0f);
        float pathVectorX = endCenterX - (f5 + (f2 / 2.0f));
        float pathVectorY = endCenterY - (position2 + (f4 / 2.0f));
        this.x = (float) ((int) ((f5 + (pathVectorX * position)) - ((scaleX * scaleWidth) / 2.0f)));
        this.y = (float) ((int) ((position2 + (pathVectorY * position)) - ((scaleY * scaleHeight) / 2.0f)));
        this.width = (float) ((int) (f2 + (scaleX * scaleWidth)));
        this.height = (float) ((int) (f4 + (scaleY * scaleHeight)));
        float dxdx = Float.isNaN(c.mPercentX) ? position : c.mPercentX;
        float dydx = Float.isNaN(c.mAltPercentY) ? 0.0f : c.mAltPercentY;
        float dydy = Float.isNaN(c.mPercentY) ? position : c.mPercentY;
        float dxdy = Float.isNaN(c.mAltPercentX) ? 0.0f : c.mAltPercentX;
        this.mMode = 2;
        this.x = (float) ((int) (((startTimePoint.x + (pathVectorX * dxdx)) + (pathVectorY * dxdy)) - ((scaleX * scaleWidth) / 2.0f)));
        this.y = (float) ((int) (((startTimePoint.y + (pathVectorX * dydx)) + (pathVectorY * dydy)) - ((scaleY * scaleHeight) / 2.0f)));
        this.mKeyFrameEasing = Easing.getInterpolator(c.mTransitionEasing);
        this.mPathMotionArc = c.mPathMotionArc;
    }

    public MotionPaths(int parentWidth, int parentHeight, KeyPosition c, MotionPaths startTimePoint, MotionPaths endTimePoint) {
        this.mDrawPath = 0;
        this.mPathRotate = Float.NaN;
        this.mProgress = Float.NaN;
        this.mPathMotionArc = Key.UNSET;
        this.attributes = new LinkedHashMap<>();
        this.mMode = 0;
        this.mTempValue = new double[18];
        this.mTempDelta = new double[18];
        int i = c.mPositionType;
        if (i == 1) {
            initPath(c, startTimePoint, endTimePoint);
        } else if (i != 2) {
            initCartesian(c, startTimePoint, endTimePoint);
        } else {
            initScreen(parentWidth, parentHeight, c, startTimePoint, endTimePoint);
        }
    }

    void initScreen(int parentWidth, int parentHeight, KeyPosition c, MotionPaths startTimePoint, MotionPaths endTimePoint) {
        float position = ((float) c.mFramePosition) / 100.0f;
        this.time = position;
        this.mDrawPath = c.mDrawPath;
        float scaleWidth = Float.isNaN(c.mPercentWidth) ? position : c.mPercentWidth;
        float scaleHeight = Float.isNaN(c.mPercentHeight) ? position : c.mPercentHeight;
        float f = endTimePoint.width;
        float f2 = startTimePoint.width;
        float scaleX = f - f2;
        float f3 = endTimePoint.height;
        float f4 = startTimePoint.height;
        float scaleY = f3 - f4;
        this.position = this.time;
        float f5 = startTimePoint.x;
        float position2 = startTimePoint.y;
        float endCenterX = endTimePoint.x + (f / 2.0f);
        float endCenterY = endTimePoint.y + (f3 / 2.0f);
        this.x = (float) ((int) ((f5 + ((endCenterX - (f5 + (f2 / 2.0f))) * position)) - ((scaleX * scaleWidth) / 2.0f)));
        this.y = (float) ((int) ((position2 + ((endCenterY - (position2 + (f4 / 2.0f))) * position)) - ((scaleY * scaleHeight) / 2.0f)));
        this.width = (float) ((int) (f2 + (scaleX * scaleWidth)));
        this.height = (float) ((int) (f4 + (scaleY * scaleHeight)));
        this.mMode = 3;
        if (!Float.isNaN(c.mPercentX)) {
            this.x = (float) ((int) (c.mPercentX * ((float) ((int) (((float) parentWidth) - this.width)))));
        }
        if (!Float.isNaN(c.mPercentY)) {
            this.y = (float) ((int) (c.mPercentY * ((float) ((int) (((float) parentHeight) - this.height)))));
        }
        this.mKeyFrameEasing = Easing.getInterpolator(c.mTransitionEasing);
        this.mPathMotionArc = c.mPathMotionArc;
    }

    void initPath(KeyPosition c, MotionPaths startTimePoint, MotionPaths endTimePoint) {
        float position = ((float) c.mFramePosition) / 100.0f;
        this.time = position;
        this.mDrawPath = c.mDrawPath;
        float scaleWidth = Float.isNaN(c.mPercentWidth) ? position : c.mPercentWidth;
        float scaleHeight = Float.isNaN(c.mPercentHeight) ? position : c.mPercentHeight;
        float scaleX = endTimePoint.width - startTimePoint.width;
        float scaleY = endTimePoint.height - startTimePoint.height;
        this.position = this.time;
        float path = Float.isNaN(c.mPercentX) ? position : c.mPercentX;
        float f = startTimePoint.x;
        float f2 = startTimePoint.width;
        float f3 = startTimePoint.y;
        float position2 = startTimePoint.height;
        float pathVectorX = (endTimePoint.x + (endTimePoint.width / 2.0f)) - ((f2 / 2.0f) + f);
        float pathVectorY = (endTimePoint.y + (endTimePoint.height / 2.0f)) - (f3 + (position2 / 2.0f));
        this.x = (float) ((int) ((f + (pathVectorX * path)) - ((scaleX * scaleWidth) / 2.0f)));
        this.y = (float) ((int) ((f3 + (pathVectorY * path)) - ((scaleY * scaleHeight) / 2.0f)));
        this.width = (float) ((int) (f2 + (scaleX * scaleWidth)));
        this.height = (float) ((int) (position2 + (scaleY * scaleHeight)));
        float perpendicular = Float.isNaN(c.mPercentY) ? 0.0f : c.mPercentY;
        this.mMode = 1;
        this.x = (float) ((int) ((startTimePoint.x + (pathVectorX * path)) - ((scaleX * scaleWidth) / 2.0f)));
        this.y = (float) ((int) ((startTimePoint.y + (pathVectorY * path)) - ((scaleY * scaleHeight) / 2.0f)));
        this.x += (-pathVectorY) * perpendicular;
        this.y += pathVectorX * perpendicular;
        this.mKeyFrameEasing = Easing.getInterpolator(c.mTransitionEasing);
        this.mPathMotionArc = c.mPathMotionArc;
    }

    private static final float xRotate(float sin, float cos, float cx, float cy, float x, float y) {
        return (((x - cx) * cos) - ((y - cy) * sin)) + cx;
    }

    private static final float yRotate(float sin, float cos, float cx, float cy, float x, float y) {
        return ((x - cx) * sin) + ((y - cy) * cos) + cy;
    }

    private boolean diff(float a2, float b) {
        return (Float.isNaN(a2) || Float.isNaN(b)) ? Float.isNaN(a2) != Float.isNaN(b) : Math.abs(a2 - b) > 1.0E-6f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void different(MotionPaths points, boolean[] mask, String[] custom, boolean arcMode) {
        int c = 0 + 1;
        mask[0] = mask[0] | diff(this.position, points.position);
        int c2 = c + 1;
        mask[c] = mask[c] | diff(this.x, points.x) | arcMode;
        int c3 = c2 + 1;
        mask[c2] = mask[c2] | diff(this.y, points.y) | arcMode;
        int c4 = c3 + 1;
        mask[c3] = mask[c3] | diff(this.width, points.width);
        int i = c4 + 1;
        mask[c4] = mask[c4] | diff(this.height, points.height);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getCenter(int[] toUse, double[] data, float[] point, int offset) {
        float v_x = this.x;
        float v_y = this.y;
        float v_width = this.width;
        float v_height = this.height;
        for (int i = 0; i < toUse.length; i++) {
            float value = (float) data[i];
            int i2 = toUse[i];
            if (i2 == 1) {
                v_x = value;
            } else if (i2 == 2) {
                v_y = value;
            } else if (i2 == 3) {
                v_width = value;
            } else if (i2 == 4) {
                v_height = value;
            }
        }
        point[offset] = (v_width / 2.0f) + v_x + 0.0f;
        point[offset + 1] = (v_height / 2.0f) + v_y + 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getBounds(int[] toUse, double[] data, float[] point, int offset) {
        float f = this.x;
        float f2 = this.y;
        float v_width = this.width;
        float v_height = this.height;
        for (int i = 0; i < toUse.length; i++) {
            float value = (float) data[i];
            int i2 = toUse[i];
            if (!(i2 == 1 || i2 == 2)) {
                if (i2 == 3) {
                    v_width = value;
                } else if (i2 == 4) {
                    v_height = value;
                }
            }
        }
        point[offset] = v_width;
        point[offset + 1] = v_height;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Multiple debug info for r4v11 float: [D('value' float), D('deltaCycle' double)] */
    public void setView(View view, int[] toUse, double[] data, double[] slope, double[] cycle) {
        float v_x;
        float v_y;
        float v_y2;
        float v_x2 = this.x;
        float v_y3 = this.y;
        float v_width = this.width;
        float v_height = this.height;
        float dv_x = 0.0f;
        float dv_y = 0.0f;
        float dv_width = 0.0f;
        float dv_height = 0.0f;
        float path_rotate = Float.NaN;
        float f = Float.MIN_VALUE;
        if (toUse.length != 0) {
            v_x = v_x2;
            if (this.mTempValue.length <= toUse[toUse.length - 1]) {
                int scratch_data_length = toUse[toUse.length - 1] + 1;
                this.mTempValue = new double[scratch_data_length];
                this.mTempDelta = new double[scratch_data_length];
            }
        } else {
            v_x = v_x2;
        }
        float v_y4 = v_y3;
        float v_width2 = v_width;
        Arrays.fill(this.mTempValue, Double.NaN);
        for (int i = 0; i < toUse.length; i++) {
            this.mTempValue[toUse[i]] = data[i];
            this.mTempDelta[toUse[i]] = slope[i];
        }
        int i2 = 0;
        while (true) {
            double[] dArr = this.mTempValue;
            if (i2 >= dArr.length) {
                break;
            }
            double d = 0.0d;
            if (Double.isNaN(dArr[i2])) {
                if (cycle == null) {
                    v_y = v_y4;
                    v_y2 = f;
                } else if (cycle[i2] == 0.0d) {
                    v_y = v_y4;
                    v_y2 = f;
                }
                i2++;
                f = v_y2;
                v_y4 = v_y;
            }
            if (cycle != null) {
                d = cycle[i2];
            }
            double deltaCycle = d;
            if (!Double.isNaN(this.mTempValue[i2])) {
                deltaCycle = this.mTempValue[i2] + deltaCycle;
            }
            float value = (float) deltaCycle;
            v_y = v_y4;
            float dvalue = (float) this.mTempDelta[i2];
            if (i2 != 0) {
                v_y2 = Float.MIN_VALUE;
                if (i2 == 1) {
                    dv_x = dvalue;
                    v_x = value;
                } else if (i2 == 2) {
                    dv_y = dvalue;
                    v_y = value;
                } else if (i2 == 3) {
                    dv_width = dvalue;
                    v_width2 = value;
                } else if (i2 == 4) {
                    v_height = value;
                    dv_height = dvalue;
                } else if (i2 == 5) {
                    path_rotate = value;
                }
            } else {
                v_y2 = Float.MIN_VALUE;
            }
            i2++;
            f = v_y2;
            v_y4 = v_y;
        }
        if (!Float.isNaN(path_rotate)) {
            view.setRotation((float) (((double) (Float.isNaN(Float.NaN) ? 0.0f : Float.NaN)) + ((double) path_rotate) + Math.toDegrees(Math.atan2((double) ((dv_height / 2.0f) + dv_y), (double) ((dv_width / 2.0f) + dv_x)))));
        } else if (!Float.isNaN(Float.NaN)) {
            view.setRotation(Float.NaN);
        }
        int l = (int) (v_x + 0.5f);
        int t = (int) (v_y4 + 0.5f);
        int r = (int) (v_x + 0.5f + v_width2);
        int b = (int) (v_y4 + 0.5f + v_height);
        int i_width = r - l;
        int i_height = b - t;
        if ((i_width == view.getMeasuredWidth() && i_height == view.getMeasuredHeight()) ? false : true) {
            view.measure(View.MeasureSpec.makeMeasureSpec(i_width, 1073741824), View.MeasureSpec.makeMeasureSpec(i_height, 1073741824));
        }
        view.layout(l, t, r, b);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getRect(int[] toUse, double[] data, float[] path, int offset) {
        float alpha;
        float v_x = this.x;
        float v_y = this.y;
        float v_width = this.width;
        float alpha2 = 0.0f;
        float rotationX = 0.0f;
        int i = 0;
        float v_x2 = v_x;
        float v_height = this.height;
        float v_width2 = v_width;
        float v_y2 = v_y;
        while (i < toUse.length) {
            float value = (float) data[i];
            int i2 = toUse[i];
            if (i2 != 0) {
                alpha = alpha2;
                if (i2 == 1) {
                    v_x2 = value;
                } else if (i2 == 2) {
                    v_y2 = value;
                } else if (i2 == 3) {
                    v_width2 = value;
                } else if (i2 == 4) {
                    v_height = value;
                }
            } else {
                alpha = alpha2;
            }
            i++;
            rotationX = rotationX;
            alpha2 = alpha;
        }
        float x1 = v_x2;
        float y1 = v_y2;
        float x2 = v_x2 + v_width2;
        float y2 = y1;
        float x3 = x2;
        float y3 = v_y2 + v_height;
        float x4 = x1;
        float y4 = y3;
        float cx = x1 + (v_width2 / 2.0f);
        float cy = y1 + (v_height / 2.0f);
        if (!Float.isNaN(Float.NaN)) {
            cx = x1 + ((x2 - x1) * Float.NaN);
        }
        if (!Float.isNaN(Float.NaN)) {
            cy = y1 + ((y3 - y1) * Float.NaN);
        }
        if (1.0f != 1.0f) {
            float midx = (x1 + x2) / 2.0f;
            x1 = ((x1 - midx) * 1.0f) + midx;
            x2 = ((x2 - midx) * 1.0f) + midx;
            x3 = ((x3 - midx) * 1.0f) + midx;
            x4 = ((x4 - midx) * 1.0f) + midx;
        }
        if (1.0f != 1.0f) {
            float midy = (y1 + y3) / 2.0f;
            y1 = ((y1 - midy) * 1.0f) + midy;
            y2 = ((y2 - midy) * 1.0f) + midy;
            y3 = ((y3 - midy) * 1.0f) + midy;
            y4 = ((y4 - midy) * 1.0f) + midy;
        }
        if (0.0f != 0.0f) {
            float sin = (float) Math.sin(Math.toRadians((double) 0.0f));
            float cos = (float) Math.cos(Math.toRadians((double) 0.0f));
            float tx1 = xRotate(sin, cos, cx, cy, x1, y1);
            float ty1 = yRotate(sin, cos, cx, cy, x1, y1);
            float tx2 = xRotate(sin, cos, cx, cy, x2, y2);
            float ty2 = yRotate(sin, cos, cx, cy, x2, y2);
            float tx3 = xRotate(sin, cos, cx, cy, x3, y3);
            float ty3 = yRotate(sin, cos, cx, cy, x3, y3);
            float tx4 = xRotate(sin, cos, cx, cy, x4, y4);
            float ty4 = yRotate(sin, cos, cx, cy, x4, y4);
            x1 = tx1;
            y1 = ty1;
            x2 = tx2;
            y2 = ty2;
            x3 = tx3;
            y3 = ty3;
            x4 = tx4;
            y4 = ty4;
        }
        int offset2 = offset + 1;
        path[offset] = x1 + 0.0f;
        int offset3 = offset2 + 1;
        path[offset2] = y1 + 0.0f;
        int offset4 = offset3 + 1;
        path[offset3] = x2 + 0.0f;
        int offset5 = offset4 + 1;
        path[offset4] = y2 + 0.0f;
        int offset6 = offset5 + 1;
        path[offset5] = x3 + 0.0f;
        int offset7 = offset6 + 1;
        path[offset6] = y3 + 0.0f;
        int offset8 = offset7 + 1;
        path[offset7] = x4 + 0.0f;
        int i3 = offset8 + 1;
        path[offset8] = y4 + 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDpDt(float locationX, float locationY, float[] mAnchorDpDt, int[] toUse, double[] deltaData, double[] data) {
        float d_x = 0.0f;
        float d_y = 0.0f;
        float d_width = 0.0f;
        float d_height = 0.0f;
        for (int i = 0; i < toUse.length; i++) {
            float deltaV = (float) deltaData[i];
            float f = (float) data[i];
            int i2 = toUse[i];
            if (i2 != 0) {
                if (i2 == 1) {
                    d_x = deltaV;
                } else if (i2 == 2) {
                    d_y = deltaV;
                } else if (i2 == 3) {
                    d_width = deltaV;
                } else if (i2 == 4) {
                    d_height = deltaV;
                }
            }
        }
        float deltaX = d_x - ((0.0f * d_width) / 2.0f);
        float deltaY = d_y - ((0.0f * d_height) / 2.0f);
        mAnchorDpDt[0] = ((1.0f - locationX) * deltaX) + ((deltaX + ((0.0f + 1.0f) * d_width)) * locationX) + 0.0f;
        mAnchorDpDt[1] = ((1.0f - locationY) * deltaY) + ((deltaY + ((0.0f + 1.0f) * d_height)) * locationY) + 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void fillStandard(double[] data, int[] toUse) {
        float[] set = {this.position, this.x, this.y, this.width, this.height, this.mPathRotate};
        int c = 0;
        for (int i = 0; i < toUse.length; i++) {
            if (toUse[i] < set.length) {
                data[c] = (double) set[toUse[i]];
                c++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasCustomData(String name) {
        return this.attributes.containsKey(name);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCustomDataCount(String name) {
        return this.attributes.get(name).noOfInterpValues();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCustomData(String name, double[] value, int offset) {
        ConstraintAttribute a2 = this.attributes.get(name);
        if (a2.noOfInterpValues() == 1) {
            value[offset] = (double) a2.getValueToInterpolate();
            return 1;
        }
        int N = a2.noOfInterpValues();
        float[] f = new float[N];
        a2.getValuesToInterpolate(f);
        int i = 0;
        while (i < N) {
            value[offset] = (double) f[i];
            i++;
            offset++;
        }
        return N;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBounds(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    public int compareTo(MotionPaths o) {
        return Float.compare(this.position, o.position);
    }

    public void applyParameters(ConstraintSet.Constraint c) {
        this.mKeyFrameEasing = Easing.getInterpolator(c.motion.mTransitionEasing);
        this.mPathMotionArc = c.motion.mPathMotionArc;
        this.mPathRotate = c.motion.mPathRotate;
        this.mDrawPath = c.motion.mDrawPath;
        this.mProgress = c.propertySet.mProgress;
        for (String s : c.mCustomConstraints.keySet()) {
            ConstraintAttribute attr = c.mCustomConstraints.get(s);
            if (attr.getType() != ConstraintAttribute.AttributeType.STRING_TYPE) {
                this.attributes.put(s, attr);
            }
        }
    }
}
