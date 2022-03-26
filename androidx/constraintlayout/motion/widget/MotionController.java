package androidx.constraintlayout.motion.widget;

import android.graphics.RectF;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.motion.utils.CurveFit;
import androidx.constraintlayout.motion.utils.Easing;
import androidx.constraintlayout.motion.utils.VelocityMatrix;
import androidx.constraintlayout.motion.widget.KeyCycleOscillator;
import androidx.constraintlayout.motion.widget.SplineSet;
import androidx.constraintlayout.motion.widget.TimeCycleSplineSet;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
/* loaded from: classes.dex */
public class MotionController {
    private static final boolean DEBUG;
    public static final int DRAW_PATH_AS_CONFIGURED;
    public static final int DRAW_PATH_BASIC;
    public static final int DRAW_PATH_CARTESIAN;
    public static final int DRAW_PATH_NONE;
    public static final int DRAW_PATH_RECTANGLE;
    public static final int DRAW_PATH_RELATIVE;
    public static final int DRAW_PATH_SCREEN;
    private static final boolean FAVOR_FIXED_SIZE_VIEWS;
    public static final int HORIZONTAL_PATH_X;
    public static final int HORIZONTAL_PATH_Y;
    public static final int PATH_PERCENT;
    public static final int PATH_PERPENDICULAR;
    private static final String TAG;
    public static final int VERTICAL_PATH_X;
    public static final int VERTICAL_PATH_Y;
    String[] attributeTable;
    private CurveFit mArcSpline;
    private int[] mAttributeInterpCount;
    private String[] mAttributeNames;
    private HashMap<String, SplineSet> mAttributesMap;
    String mConstraintTag;
    private HashMap<String, KeyCycleOscillator> mCycleMap;
    int mId;
    private double[] mInterpolateData;
    private int[] mInterpolateVariables;
    private double[] mInterpolateVelocity;
    private KeyTrigger[] mKeyTriggers;
    private CurveFit[] mSpline;
    private HashMap<String, TimeCycleSplineSet> mTimeCycleAttributesMap;
    View mView;
    private int mCurveFitType = -1;
    private MotionPaths mStartMotionPath = new MotionPaths();
    private MotionPaths mEndMotionPath = new MotionPaths();
    private MotionConstrainedPoint mStartPoint = new MotionConstrainedPoint();
    private MotionConstrainedPoint mEndPoint = new MotionConstrainedPoint();
    float mMotionStagger = Float.NaN;
    float mStaggerOffset = 0.0f;
    float mStaggerScale = 1.0f;
    private int MAX_DIMENSION = 4;
    private float[] mValuesBuff = new float[this.MAX_DIMENSION];
    private ArrayList<MotionPaths> mMotionPaths = new ArrayList<>();
    private float[] mVelocity = new float[1];
    private ArrayList<Key> mKeyList = new ArrayList<>();
    private int mPathMotionArc = Key.UNSET;

    public MotionPaths getKeyFrame(int i) {
        return this.mMotionPaths.get(i);
    }

    public MotionController(View view) {
        setView(view);
    }

    float getStartX() {
        return this.mStartMotionPath.x;
    }

    float getStartY() {
        return this.mStartMotionPath.y;
    }

    public float getFinalX() {
        return this.mEndMotionPath.x;
    }

    public float getFinalY() {
        return this.mEndMotionPath.y;
    }

    public void buildPath(float[] points, int pointCount) {
        MotionController motionController = this;
        int i = pointCount;
        float f = 1.0f;
        float mils = 1.0f / ((float) (i - 1));
        HashMap<String, SplineSet> hashMap = motionController.mAttributesMap;
        KeyCycleOscillator osc_y = null;
        SplineSet trans_x = hashMap == null ? null : hashMap.get("translationX");
        HashMap<String, SplineSet> hashMap2 = motionController.mAttributesMap;
        SplineSet trans_y = hashMap2 == null ? null : hashMap2.get("translationY");
        HashMap<String, KeyCycleOscillator> hashMap3 = motionController.mCycleMap;
        KeyCycleOscillator osc_x = hashMap3 == null ? null : hashMap3.get("translationX");
        HashMap<String, KeyCycleOscillator> hashMap4 = motionController.mCycleMap;
        if (hashMap4 != null) {
            osc_y = hashMap4.get("translationY");
        }
        int i2 = 0;
        while (i2 < i) {
            float position = ((float) i2) * mils;
            if (motionController.mStaggerScale != f) {
                if (position < motionController.mStaggerOffset) {
                    position = 0.0f;
                }
                float f2 = motionController.mStaggerOffset;
                if (position > f2 && ((double) position) < 1.0d) {
                    position = (position - f2) * motionController.mStaggerScale;
                }
            }
            double p = (double) position;
            Easing easing = motionController.mStartMotionPath.mKeyFrameEasing;
            float start = 0.0f;
            float end = Float.NaN;
            Iterator<MotionPaths> it = motionController.mMotionPaths.iterator();
            while (it.hasNext()) {
                MotionPaths frame = it.next();
                if (frame.mKeyFrameEasing != null) {
                    if (frame.time < position) {
                        Easing easing2 = frame.mKeyFrameEasing;
                        start = frame.time;
                        easing = easing2;
                    } else if (Float.isNaN(end)) {
                        end = frame.time;
                    }
                }
                mils = mils;
            }
            if (easing != null) {
                if (Float.isNaN(end)) {
                    end = 1.0f;
                }
                p = (double) (((end - start) * ((float) easing.get((double) ((position - start) / (end - start))))) + start);
            }
            motionController.mSpline[0].getPos(p, motionController.mInterpolateData);
            CurveFit curveFit = motionController.mArcSpline;
            if (curveFit != null) {
                double[] dArr = motionController.mInterpolateData;
                if (dArr.length > 0) {
                    curveFit.getPos(p, dArr);
                }
            }
            motionController.mStartMotionPath.getCenter(motionController.mInterpolateVariables, motionController.mInterpolateData, points, i2 * 2);
            if (osc_x != null) {
                int i3 = i2 * 2;
                points[i3] = points[i3] + osc_x.get(position);
            } else if (trans_x != null) {
                int i4 = i2 * 2;
                points[i4] = points[i4] + trans_x.get(position);
            }
            if (osc_y != null) {
                int i5 = (i2 * 2) + 1;
                points[i5] = points[i5] + osc_y.get(position);
            } else if (trans_y != null) {
                int i6 = (i2 * 2) + 1;
                points[i6] = points[i6] + trans_y.get(position);
            }
            i2++;
            f = 1.0f;
            motionController = this;
            i = pointCount;
            mils = mils;
        }
    }

    void buildBounds(float[] bounds, int pointCount) {
        float mils;
        MotionController motionController = this;
        int i = pointCount;
        float f = 1.0f;
        float mils2 = 1.0f / ((float) (i - 1));
        HashMap<String, SplineSet> hashMap = motionController.mAttributesMap;
        SplineSet trans_x = hashMap == null ? null : hashMap.get("translationX");
        HashMap<String, SplineSet> hashMap2 = motionController.mAttributesMap;
        if (hashMap2 != null) {
            hashMap2.get("translationY");
        }
        HashMap<String, KeyCycleOscillator> hashMap3 = motionController.mCycleMap;
        if (hashMap3 != null) {
            hashMap3.get("translationX");
        }
        HashMap<String, KeyCycleOscillator> hashMap4 = motionController.mCycleMap;
        if (hashMap4 != null) {
            hashMap4.get("translationY");
        }
        int i2 = 0;
        while (i2 < i) {
            float position = ((float) i2) * mils2;
            if (motionController.mStaggerScale != f) {
                if (position < motionController.mStaggerOffset) {
                    position = 0.0f;
                }
                float f2 = motionController.mStaggerOffset;
                if (position > f2 && ((double) position) < 1.0d) {
                    position = (position - f2) * motionController.mStaggerScale;
                }
            }
            double p = (double) position;
            Easing easing = motionController.mStartMotionPath.mKeyFrameEasing;
            float start = 0.0f;
            float end = Float.NaN;
            Iterator<MotionPaths> it = motionController.mMotionPaths.iterator();
            while (it.hasNext()) {
                MotionPaths frame = it.next();
                if (frame.mKeyFrameEasing != null) {
                    if (frame.time < position) {
                        Easing easing2 = frame.mKeyFrameEasing;
                        start = frame.time;
                        easing = easing2;
                    } else if (Float.isNaN(end)) {
                        end = frame.time;
                    }
                }
            }
            if (easing != null) {
                if (Float.isNaN(end)) {
                    end = 1.0f;
                }
                mils = mils2;
                p = (double) (((end - start) * ((float) easing.get((double) ((position - start) / (end - start))))) + start);
            } else {
                mils = mils2;
            }
            motionController.mSpline[0].getPos(p, motionController.mInterpolateData);
            CurveFit curveFit = motionController.mArcSpline;
            if (curveFit != null) {
                double[] dArr = motionController.mInterpolateData;
                if (dArr.length > 0) {
                    curveFit.getPos(p, dArr);
                }
            }
            motionController.mStartMotionPath.getBounds(motionController.mInterpolateVariables, motionController.mInterpolateData, bounds, i2 * 2);
            i2++;
            motionController = this;
            i = pointCount;
            mils2 = mils;
            trans_x = trans_x;
            f = 1.0f;
        }
    }

    private float getPreCycleDistance() {
        float position;
        MotionController motionController = this;
        int pointCount = 100;
        float[] points = new float[2];
        float sum = 0.0f;
        float mils = 1.0f / ((float) (100 - 1));
        double x = 0.0d;
        double y = 0.0d;
        int i = 0;
        while (i < pointCount) {
            float position2 = ((float) i) * mils;
            double p = (double) position2;
            Easing easing = motionController.mStartMotionPath.mKeyFrameEasing;
            float start = 0.0f;
            float end = Float.NaN;
            for (Iterator<MotionPaths> it = motionController.mMotionPaths.iterator(); it.hasNext(); it = it) {
                MotionPaths frame = it.next();
                if (frame.mKeyFrameEasing != null) {
                    if (frame.time < position2) {
                        Easing easing2 = frame.mKeyFrameEasing;
                        start = frame.time;
                        easing = easing2;
                    } else if (Float.isNaN(end)) {
                        end = frame.time;
                    }
                }
                mils = mils;
            }
            if (easing != null) {
                if (Float.isNaN(end)) {
                    end = 1.0f;
                }
                position = position2;
                p = (double) (((end - start) * ((float) easing.get((double) ((position2 - start) / (end - start))))) + start);
            } else {
                position = position2;
            }
            motionController.mSpline[0].getPos(p, motionController.mInterpolateData);
            motionController.mStartMotionPath.getCenter(motionController.mInterpolateVariables, motionController.mInterpolateData, points, 0);
            if (i > 0) {
                sum = (float) (((double) sum) + Math.hypot(y - ((double) points[1]), x - ((double) points[0])));
            }
            x = (double) points[0];
            y = (double) points[1];
            i++;
            motionController = this;
            pointCount = pointCount;
            mils = mils;
        }
        return sum;
    }

    public KeyPositionBase getPositionKeyframe(int layoutWidth, int layoutHeight, float x, float y) {
        RectF start = new RectF();
        start.left = this.mStartMotionPath.x;
        start.top = this.mStartMotionPath.y;
        start.right = start.left + this.mStartMotionPath.width;
        start.bottom = start.top + this.mStartMotionPath.height;
        RectF end = new RectF();
        end.left = this.mEndMotionPath.x;
        end.top = this.mEndMotionPath.y;
        end.right = end.left + this.mEndMotionPath.width;
        end.bottom = end.top + this.mEndMotionPath.height;
        Iterator<Key> it = this.mKeyList.iterator();
        while (it.hasNext()) {
            Key key = it.next();
            if ((key instanceof KeyPositionBase) && ((KeyPositionBase) key).intersects(layoutWidth, layoutHeight, start, end, x, y)) {
                return (KeyPositionBase) key;
            }
        }
        return null;
    }

    public int buildKeyFrames(float[] keyFrames, int[] mode) {
        if (keyFrames == null) {
            return 0;
        }
        int count = 0;
        double[] time = this.mSpline[0].getTimePoints();
        if (mode != null) {
            Iterator<MotionPaths> it = this.mMotionPaths.iterator();
            while (it.hasNext()) {
                mode[count] = it.next().mMode;
                count++;
            }
            count = 0;
        }
        for (double d : time) {
            this.mSpline[0].getPos(d, this.mInterpolateData);
            this.mStartMotionPath.getCenter(this.mInterpolateVariables, this.mInterpolateData, keyFrames, count);
            count += 2;
        }
        return count / 2;
    }

    int buildKeyBounds(float[] keyBounds, int[] mode) {
        if (keyBounds == null) {
            return 0;
        }
        int count = 0;
        double[] time = this.mSpline[0].getTimePoints();
        if (mode != null) {
            Iterator<MotionPaths> it = this.mMotionPaths.iterator();
            while (it.hasNext()) {
                mode[count] = it.next().mMode;
                count++;
            }
            count = 0;
        }
        for (double d : time) {
            this.mSpline[0].getPos(d, this.mInterpolateData);
            this.mStartMotionPath.getBounds(this.mInterpolateVariables, this.mInterpolateData, keyBounds, count);
            count += 2;
        }
        return count / 2;
    }

    public int getAttributeValues(String attributeType, float[] points, int pointCount) {
        float f = 1.0f / ((float) (pointCount - 1));
        SplineSet spline = this.mAttributesMap.get(attributeType);
        if (spline == null) {
            return -1;
        }
        for (int j = 0; j < points.length; j++) {
            points[j] = spline.get((float) (j / (points.length - 1)));
        }
        return points.length;
    }

    public void buildRect(float p, float[] path, int offset) {
        this.mSpline[0].getPos((double) getAdjustedPosition(p, null), this.mInterpolateData);
        this.mStartMotionPath.getRect(this.mInterpolateVariables, this.mInterpolateData, path, offset);
    }

    public void buildRectangles(float[] path, int pointCount) {
        float mils = 1.0f / ((float) (pointCount - 1));
        for (int i = 0; i < pointCount; i++) {
            this.mSpline[0].getPos((double) getAdjustedPosition(((float) i) * mils, null), this.mInterpolateData);
            this.mStartMotionPath.getRect(this.mInterpolateVariables, this.mInterpolateData, path, i * 8);
        }
    }

    public float getKeyFrameParameter(int type, float x, float y) {
        float dx = this.mEndMotionPath.x - this.mStartMotionPath.x;
        float dy = this.mEndMotionPath.y - this.mStartMotionPath.y;
        float startCenterX = this.mStartMotionPath.x + (this.mStartMotionPath.width / 2.0f);
        float startCenterY = this.mStartMotionPath.y + (this.mStartMotionPath.height / 2.0f);
        float hypot = (float) Math.hypot((double) dx, (double) dy);
        if (((double) hypot) < 1.0E-7d) {
            return Float.NaN;
        }
        float vx = x - startCenterX;
        float vy = y - startCenterY;
        if (((float) Math.hypot((double) vx, (double) vy)) == 0.0f) {
            return 0.0f;
        }
        float pathDistance = (vx * dx) + (vy * dy);
        if (type == 0) {
            return pathDistance / hypot;
        }
        if (type == 1) {
            return (float) Math.sqrt((double) ((hypot * hypot) - (pathDistance * pathDistance)));
        }
        if (type == 2) {
            return vx / dx;
        }
        if (type == 3) {
            return vy / dx;
        }
        if (type == 4) {
            return vx / dy;
        }
        if (type != 5) {
            return 0.0f;
        }
        return vy / dy;
    }

    private void insertKey(MotionPaths point) {
        int pos = Collections.binarySearch(this.mMotionPaths, point);
        if (pos == 0) {
            Log.e(TAG, " KeyPath positon \"" + point.position + "\" outside of range");
        }
        this.mMotionPaths.add((-pos) - 1, point);
    }

    public void addKeys(ArrayList<Key> list) {
        this.mKeyList.addAll(list);
    }

    public void addKey(Key key) {
        this.mKeyList.add(key);
    }

    public void setPathMotionArc(int arc) {
        this.mPathMotionArc = arc;
    }

    public void setup(int parentWidth, int parentHeight, float transitionDuration, long currentTime) {
        HashSet<String> timeCycleAttributes;
        Iterator<String> it;
        TimeCycleSplineSet splineSets;
        Iterator<String> it2;
        ArrayList<KeyTrigger> triggerList;
        SplineSet splineSets2;
        ArrayList<KeyTrigger> triggerList2;
        HashSet<String> springAttributes;
        HashSet<String> springAttributes2 = new HashSet<>();
        HashSet<String> timeCycleAttributes2 = new HashSet<>();
        HashSet<String> splineAttributes = new HashSet<>();
        HashSet<String> cycleAttributes = new HashSet<>();
        HashMap<String, Integer> interpolation = new HashMap<>();
        ArrayList<KeyTrigger> triggerList3 = null;
        if (this.mPathMotionArc != Key.UNSET) {
            this.mStartMotionPath.mPathMotionArc = this.mPathMotionArc;
        }
        this.mStartPoint.different(this.mEndPoint, splineAttributes);
        ArrayList<Key> arrayList = this.mKeyList;
        if (arrayList != null) {
            Iterator<Key> it3 = arrayList.iterator();
            while (it3.hasNext()) {
                Key key = it3.next();
                if (key instanceof KeyPosition) {
                    KeyPosition keyPath = (KeyPosition) key;
                    springAttributes = springAttributes2;
                    insertKey(new MotionPaths(parentWidth, parentHeight, keyPath, this.mStartMotionPath, this.mEndMotionPath));
                    if (keyPath.mCurveFit != Key.UNSET) {
                        this.mCurveFitType = keyPath.mCurveFit;
                    }
                } else {
                    springAttributes = springAttributes2;
                    if (key instanceof KeyCycle) {
                        key.getAttributeNames(cycleAttributes);
                    } else if (key instanceof KeyTimeCycle) {
                        key.getAttributeNames(timeCycleAttributes2);
                    } else if (key instanceof KeyTrigger) {
                        if (triggerList3 == null) {
                            triggerList3 = new ArrayList<>();
                        }
                        triggerList3.add((KeyTrigger) key);
                    } else {
                        key.setInterpolation(interpolation);
                        key.getAttributeNames(splineAttributes);
                    }
                }
                springAttributes2 = springAttributes;
            }
        }
        if (triggerList3 != null) {
            this.mKeyTriggers = (KeyTrigger[]) triggerList3.toArray(new KeyTrigger[0]);
        }
        char c = 1;
        if (!splineAttributes.isEmpty()) {
            this.mAttributesMap = new HashMap<>();
            Iterator<String> it4 = splineAttributes.iterator();
            while (it4.hasNext()) {
                String attribute = it4.next();
                if (attribute.startsWith("CUSTOM,")) {
                    SparseArray<ConstraintAttribute> attrList = new SparseArray<>();
                    String customAttributeName = attribute.split(",")[c];
                    Iterator<Key> it5 = this.mKeyList.iterator();
                    while (it5.hasNext()) {
                        Key key2 = it5.next();
                        if (key2.mCustomConstraints != null) {
                            ConstraintAttribute customAttribute = key2.mCustomConstraints.get(customAttributeName);
                            if (customAttribute != null) {
                                triggerList2 = triggerList3;
                                attrList.append(key2.mFramePosition, customAttribute);
                            } else {
                                triggerList2 = triggerList3;
                            }
                            triggerList3 = triggerList2;
                        }
                    }
                    triggerList = triggerList3;
                    splineSets2 = SplineSet.makeCustomSpline(attribute, attrList);
                } else {
                    triggerList = triggerList3;
                    splineSets2 = SplineSet.makeSpline(attribute);
                }
                if (splineSets2 == null) {
                    triggerList3 = triggerList;
                    c = 1;
                } else {
                    splineSets2.setType(attribute);
                    this.mAttributesMap.put(attribute, splineSets2);
                    triggerList3 = triggerList;
                    c = 1;
                }
            }
            ArrayList<Key> arrayList2 = this.mKeyList;
            if (arrayList2 != null) {
                Iterator<Key> it6 = arrayList2.iterator();
                while (it6.hasNext()) {
                    Key key3 = it6.next();
                    if (key3 instanceof KeyAttributes) {
                        key3.addValues(this.mAttributesMap);
                    }
                }
            }
            this.mStartPoint.addValues(this.mAttributesMap, 0);
            this.mEndPoint.addValues(this.mAttributesMap, 100);
            for (String spline : this.mAttributesMap.keySet()) {
                int curve = 0;
                if (interpolation.containsKey(spline)) {
                    curve = interpolation.get(spline).intValue();
                }
                this.mAttributesMap.get(spline).setup(curve);
            }
        }
        if (!timeCycleAttributes2.isEmpty()) {
            if (this.mTimeCycleAttributesMap == null) {
                this.mTimeCycleAttributesMap = new HashMap<>();
            }
            Iterator<String> it7 = timeCycleAttributes2.iterator();
            while (it7.hasNext()) {
                String attribute2 = it7.next();
                if (!this.mTimeCycleAttributesMap.containsKey(attribute2)) {
                    if (attribute2.startsWith("CUSTOM,")) {
                        SparseArray<ConstraintAttribute> attrList2 = new SparseArray<>();
                        String customAttributeName2 = attribute2.split(",")[1];
                        Iterator<Key> it8 = this.mKeyList.iterator();
                        while (it8.hasNext()) {
                            Key key4 = it8.next();
                            if (key4.mCustomConstraints != null) {
                                ConstraintAttribute customAttribute2 = key4.mCustomConstraints.get(customAttributeName2);
                                if (customAttribute2 != null) {
                                    it2 = it7;
                                    attrList2.append(key4.mFramePosition, customAttribute2);
                                } else {
                                    it2 = it7;
                                }
                                it7 = it2;
                            }
                        }
                        it = it7;
                        splineSets = TimeCycleSplineSet.makeCustomSpline(attribute2, attrList2);
                    } else {
                        it = it7;
                        splineSets = TimeCycleSplineSet.makeSpline(attribute2, currentTime);
                    }
                    if (splineSets == null) {
                        it7 = it;
                    } else {
                        splineSets.setType(attribute2);
                        this.mTimeCycleAttributesMap.put(attribute2, splineSets);
                        it7 = it;
                    }
                }
            }
            ArrayList<Key> arrayList3 = this.mKeyList;
            if (arrayList3 != null) {
                Iterator<Key> it9 = arrayList3.iterator();
                while (it9.hasNext()) {
                    Key key5 = it9.next();
                    if (key5 instanceof KeyTimeCycle) {
                        ((KeyTimeCycle) key5).addTimeValues(this.mTimeCycleAttributesMap);
                    }
                }
            }
            for (String spline2 : this.mTimeCycleAttributesMap.keySet()) {
                int curve2 = 0;
                if (interpolation.containsKey(spline2)) {
                    curve2 = interpolation.get(spline2).intValue();
                }
                this.mTimeCycleAttributesMap.get(spline2).setup(curve2);
            }
        }
        MotionPaths[] points = new MotionPaths[this.mMotionPaths.size() + 2];
        int count = 1;
        points[0] = this.mStartMotionPath;
        points[points.length - 1] = this.mEndMotionPath;
        if (this.mMotionPaths.size() > 0 && this.mCurveFitType == -1) {
            this.mCurveFitType = 0;
        }
        Iterator<MotionPaths> it10 = this.mMotionPaths.iterator();
        while (it10.hasNext()) {
            points[count] = it10.next();
            count++;
        }
        int variables = 18;
        HashSet<String> attributeNameSet = new HashSet<>();
        for (String s : this.mEndMotionPath.attributes.keySet()) {
            if (this.mStartMotionPath.attributes.containsKey(s)) {
                if (!splineAttributes.contains("CUSTOM," + s)) {
                    attributeNameSet.add(s);
                }
            }
        }
        this.mAttributeNames = (String[]) attributeNameSet.toArray(new String[0]);
        this.mAttributeInterpCount = new int[this.mAttributeNames.length];
        int i = 0;
        while (true) {
            String[] strArr = this.mAttributeNames;
            if (i >= strArr.length) {
                break;
            }
            String attributeName = strArr[i];
            this.mAttributeInterpCount[i] = 0;
            int j = 0;
            while (true) {
                if (j >= points.length) {
                    timeCycleAttributes = timeCycleAttributes2;
                    break;
                } else if (points[j].attributes.containsKey(attributeName)) {
                    int[] iArr = this.mAttributeInterpCount;
                    timeCycleAttributes = timeCycleAttributes2;
                    iArr[i] = iArr[i] + points[j].attributes.get(attributeName).noOfInterpValues();
                    break;
                } else {
                    j++;
                }
            }
            i++;
            timeCycleAttributes2 = timeCycleAttributes;
        }
        boolean arcMode = points[0].mPathMotionArc != Key.UNSET;
        boolean[] mask = new boolean[this.mAttributeNames.length + 18];
        int i2 = 1;
        while (i2 < points.length) {
            points[i2].different(points[i2 - 1], mask, this.mAttributeNames, arcMode);
            i2++;
            splineAttributes = splineAttributes;
        }
        int count2 = 0;
        for (int i3 = 1; i3 < mask.length; i3++) {
            if (mask[i3]) {
                count2++;
            }
        }
        this.mInterpolateVariables = new int[count2];
        int[] iArr2 = this.mInterpolateVariables;
        this.mInterpolateData = new double[iArr2.length];
        this.mInterpolateVelocity = new double[iArr2.length];
        int count3 = 0;
        for (int i4 = 1; i4 < mask.length; i4++) {
            if (mask[i4]) {
                this.mInterpolateVariables[count3] = i4;
                count3++;
            }
        }
        double[][] splineData = (double[][]) Array.newInstance(double.class, points.length, this.mInterpolateVariables.length);
        double[] timePoint = new double[points.length];
        int i5 = 0;
        while (i5 < points.length) {
            points[i5].fillStandard(splineData[i5], this.mInterpolateVariables);
            timePoint[i5] = (double) points[i5].time;
            i5++;
            arcMode = arcMode;
            count3 = count3;
        }
        int j2 = 0;
        while (true) {
            int[] iArr3 = this.mInterpolateVariables;
            if (j2 >= iArr3.length) {
                break;
            }
            if (iArr3[j2] < MotionPaths.names.length) {
                String s2 = MotionPaths.names[this.mInterpolateVariables[j2]] + " [";
                int i6 = 0;
                while (i6 < points.length) {
                    s2 = s2 + splineData[i6][j2];
                    i6++;
                    interpolation = interpolation;
                    mask = mask;
                }
            }
            j2++;
            interpolation = interpolation;
            mask = mask;
        }
        this.mSpline = new CurveFit[this.mAttributeNames.length + 1];
        int i7 = 0;
        while (true) {
            String[] strArr2 = this.mAttributeNames;
            if (i7 >= strArr2.length) {
                break;
            }
            int pointCount = 0;
            double[][] splinePoints = null;
            double[] timePoints = null;
            String name = strArr2[i7];
            int j3 = 0;
            while (j3 < points.length) {
                if (points[j3].hasCustomData(name)) {
                    if (splinePoints == null) {
                        timePoints = new double[points.length];
                        splinePoints = (double[][]) Array.newInstance(double.class, points.length, points[j3].getCustomDataCount(name));
                    }
                    timePoints[pointCount] = (double) points[j3].time;
                    points[j3].getCustomData(name, splinePoints[pointCount], 0);
                    pointCount++;
                }
                j3++;
                variables = variables;
            }
            this.mSpline[i7 + 1] = CurveFit.get(this.mCurveFitType, Arrays.copyOf(timePoints, pointCount), (double[][]) Arrays.copyOf(splinePoints, pointCount));
            i7++;
            variables = variables;
        }
        this.mSpline[0] = CurveFit.get(this.mCurveFitType, timePoint, splineData);
        if (points[0].mPathMotionArc != Key.UNSET) {
            int size = points.length;
            int[] mode = new int[size];
            double[] time = new double[size];
            double[][] values = (double[][]) Array.newInstance(double.class, size, 2);
            for (int i8 = 0; i8 < size; i8++) {
                mode[i8] = points[i8].mPathMotionArc;
                time[i8] = (double) points[i8].time;
                values[i8][0] = (double) points[i8].x;
                values[i8][1] = (double) points[i8].y;
            }
            this.mArcSpline = CurveFit.getArc(mode, time, values);
        }
        float distance = Float.NaN;
        this.mCycleMap = new HashMap<>();
        if (this.mKeyList != null) {
            Iterator<String> it11 = cycleAttributes.iterator();
            while (it11.hasNext()) {
                String attribute3 = it11.next();
                KeyCycleOscillator cycle = KeyCycleOscillator.makeSpline(attribute3);
                if (cycle != null) {
                    if (cycle.variesByPath() && Float.isNaN(distance)) {
                        distance = getPreCycleDistance();
                    }
                    cycle.setType(attribute3);
                    this.mCycleMap.put(attribute3, cycle);
                }
            }
            Iterator<Key> it12 = this.mKeyList.iterator();
            while (it12.hasNext()) {
                Key key6 = it12.next();
                if (key6 instanceof KeyCycle) {
                    ((KeyCycle) key6).addCycleValues(this.mCycleMap);
                }
            }
            for (KeyCycleOscillator cycle2 : this.mCycleMap.values()) {
                cycle2.setup(distance);
            }
        }
    }

    public String toString() {
        return " start: x: " + this.mStartMotionPath.x + " y: " + this.mStartMotionPath.y + " end: x: " + this.mEndMotionPath.x + " y: " + this.mEndMotionPath.y;
    }

    private void readView(MotionPaths motionPaths) {
        motionPaths.setBounds((float) ((int) this.mView.getX()), (float) ((int) this.mView.getY()), (float) this.mView.getWidth(), (float) this.mView.getHeight());
    }

    public void setView(View view) {
        this.mView = view;
        this.mId = view.getId();
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp instanceof ConstraintLayout.LayoutParams) {
            this.mConstraintTag = ((ConstraintLayout.LayoutParams) lp).getConstraintTag();
        }
    }

    public void setStartCurrentState(View v) {
        MotionPaths motionPaths = this.mStartMotionPath;
        motionPaths.time = 0.0f;
        motionPaths.position = 0.0f;
        motionPaths.setBounds(v.getX(), v.getY(), (float) v.getWidth(), (float) v.getHeight());
        this.mStartPoint.setState(v);
    }

    public void setStartState(ConstraintWidget cw, ConstraintSet constraintSet) {
        MotionPaths motionPaths = this.mStartMotionPath;
        motionPaths.time = 0.0f;
        motionPaths.position = 0.0f;
        readView(motionPaths);
        this.mStartMotionPath.setBounds((float) cw.getX(), (float) cw.getY(), (float) cw.getWidth(), (float) cw.getHeight());
        ConstraintSet.Constraint constraint = constraintSet.getParameters(this.mId);
        this.mStartMotionPath.applyParameters(constraint);
        this.mMotionStagger = constraint.motion.mMotionStagger;
        this.mStartPoint.setState(cw, constraintSet, this.mId);
    }

    public void setEndState(ConstraintWidget cw, ConstraintSet constraintSet) {
        MotionPaths motionPaths = this.mEndMotionPath;
        motionPaths.time = 1.0f;
        motionPaths.position = 1.0f;
        readView(motionPaths);
        this.mEndMotionPath.setBounds((float) cw.getX(), (float) cw.getY(), (float) cw.getWidth(), (float) cw.getHeight());
        this.mEndMotionPath.applyParameters(constraintSet.getParameters(this.mId));
        this.mEndPoint.setState(cw, constraintSet, this.mId);
    }

    private float getAdjustedPosition(float position, float[] velocity) {
        if (velocity != null) {
            velocity[0] = 1.0f;
        } else if (((double) this.mStaggerScale) != 1.0d) {
            if (position < this.mStaggerOffset) {
                position = 0.0f;
            }
            float f = this.mStaggerOffset;
            if (position > f && ((double) position) < 1.0d) {
                position = (position - f) * this.mStaggerScale;
            }
        }
        float adjusted = position;
        Easing easing = this.mStartMotionPath.mKeyFrameEasing;
        float start = 0.0f;
        float end = Float.NaN;
        Iterator<MotionPaths> it = this.mMotionPaths.iterator();
        while (it.hasNext()) {
            MotionPaths frame = it.next();
            if (frame.mKeyFrameEasing != null) {
                if (frame.time < position) {
                    easing = frame.mKeyFrameEasing;
                    start = frame.time;
                } else if (Float.isNaN(end)) {
                    end = frame.time;
                }
            }
        }
        if (easing != null) {
            if (Float.isNaN(end)) {
                end = 1.0f;
            }
            float offset = (position - start) / (end - start);
            adjusted = ((end - start) * ((float) easing.get((double) offset))) + start;
            if (velocity != null) {
                velocity[0] = (float) easing.getDiff((double) offset);
            }
        }
        return adjusted;
    }

    public boolean interpolate(View child, float global_position, long time, KeyCache keyCache) {
        boolean timeAnimation;
        TimeCycleSplineSet.PathRotate timePathRotate;
        int i;
        float position = getAdjustedPosition(global_position, null);
        HashMap<String, SplineSet> hashMap = this.mAttributesMap;
        if (hashMap != null) {
            for (SplineSet aSpline : hashMap.values()) {
                aSpline.setProperty(child, position);
            }
        }
        HashMap<String, TimeCycleSplineSet> hashMap2 = this.mTimeCycleAttributesMap;
        if (hashMap2 != null) {
            boolean timeAnimation2 = false;
            TimeCycleSplineSet.PathRotate timePathRotate2 = null;
            for (TimeCycleSplineSet aSpline2 : hashMap2.values()) {
                if (aSpline2 instanceof TimeCycleSplineSet.PathRotate) {
                    timePathRotate2 = (TimeCycleSplineSet.PathRotate) aSpline2;
                } else {
                    timeAnimation2 |= aSpline2.setProperty(child, position, time, keyCache);
                }
            }
            timeAnimation = timeAnimation2;
            timePathRotate = timePathRotate2;
        } else {
            timeAnimation = false;
            timePathRotate = null;
        }
        CurveFit[] curveFitArr = this.mSpline;
        if (curveFitArr != null) {
            curveFitArr[0].getPos((double) position, this.mInterpolateData);
            this.mSpline[0].getSlope((double) position, this.mInterpolateVelocity);
            CurveFit curveFit = this.mArcSpline;
            if (curveFit != null) {
                double[] dArr = this.mInterpolateData;
                if (dArr.length > 0) {
                    curveFit.getPos((double) position, dArr);
                    this.mArcSpline.getSlope((double) position, this.mInterpolateVelocity);
                }
            }
            this.mStartMotionPath.setView(child, this.mInterpolateVariables, this.mInterpolateData, this.mInterpolateVelocity, null);
            HashMap<String, SplineSet> hashMap3 = this.mAttributesMap;
            if (hashMap3 != null) {
                for (SplineSet aSpline3 : hashMap3.values()) {
                    if (aSpline3 instanceof SplineSet.PathRotate) {
                        double[] dArr2 = this.mInterpolateVelocity;
                        ((SplineSet.PathRotate) aSpline3).setPathRotate(child, position, dArr2[0], dArr2[1]);
                    }
                }
            }
            if (timePathRotate != null) {
                double[] dArr3 = this.mInterpolateVelocity;
                i = 0;
                timeAnimation = timePathRotate.setPathRotate(child, keyCache, position, time, dArr3[0], dArr3[1]) | timeAnimation;
            } else {
                i = 0;
            }
            int i2 = 1;
            while (true) {
                CurveFit[] curveFitArr2 = this.mSpline;
                if (i2 >= curveFitArr2.length) {
                    break;
                }
                curveFitArr2[i2].getPos((double) position, this.mValuesBuff);
                this.mStartMotionPath.attributes.get(this.mAttributeNames[i2 - 1]).setInterpolatedValue(child, this.mValuesBuff);
                i2++;
            }
            if (this.mStartPoint.mVisibilityMode == 0) {
                if (position <= 0.0f) {
                    child.setVisibility(this.mStartPoint.visibility);
                } else if (position >= 1.0f) {
                    child.setVisibility(this.mEndPoint.visibility);
                } else if (this.mEndPoint.visibility != this.mStartPoint.visibility) {
                    child.setVisibility(i);
                }
            }
            if (this.mKeyTriggers != null) {
                int i3 = 0;
                while (true) {
                    KeyTrigger[] keyTriggerArr = this.mKeyTriggers;
                    if (i3 >= keyTriggerArr.length) {
                        break;
                    }
                    keyTriggerArr[i3].conditionallyFire(position, child);
                    i3++;
                }
            }
        } else {
            float float_l = this.mStartMotionPath.x + ((this.mEndMotionPath.x - this.mStartMotionPath.x) * position);
            float float_t = this.mStartMotionPath.y + ((this.mEndMotionPath.y - this.mStartMotionPath.y) * position);
            int l = (int) (float_l + 0.5f);
            int t = (int) (float_t + 0.5f);
            int r = (int) (float_l + 0.5f + this.mStartMotionPath.width + ((this.mEndMotionPath.width - this.mStartMotionPath.width) * position));
            int b = (int) (0.5f + float_t + this.mStartMotionPath.height + ((this.mEndMotionPath.height - this.mStartMotionPath.height) * position));
            int width = r - l;
            int height = b - t;
            if (!(this.mEndMotionPath.width == this.mStartMotionPath.width && this.mEndMotionPath.height == this.mStartMotionPath.height)) {
                child.measure(View.MeasureSpec.makeMeasureSpec(width, 1073741824), View.MeasureSpec.makeMeasureSpec(height, 1073741824));
            }
            child.layout(l, t, r, b);
        }
        HashMap<String, KeyCycleOscillator> hashMap4 = this.mCycleMap;
        if (hashMap4 != null) {
            for (KeyCycleOscillator osc : hashMap4.values()) {
                if (osc instanceof KeyCycleOscillator.PathRotateSet) {
                    double[] dArr4 = this.mInterpolateVelocity;
                    ((KeyCycleOscillator.PathRotateSet) osc).setPathRotate(child, position, dArr4[0], dArr4[1]);
                } else {
                    osc.setProperty(child, position);
                }
            }
        }
        return timeAnimation;
    }

    public void getDpDt(float position, float locationX, float locationY, float[] mAnchorDpDt) {
        double[] dArr;
        float position2 = getAdjustedPosition(position, this.mVelocity);
        CurveFit[] curveFitArr = this.mSpline;
        if (curveFitArr != null) {
            curveFitArr[0].getSlope((double) position2, this.mInterpolateVelocity);
            this.mSpline[0].getPos((double) position2, this.mInterpolateData);
            float v = this.mVelocity[0];
            int i = 0;
            while (true) {
                dArr = this.mInterpolateVelocity;
                if (i >= dArr.length) {
                    break;
                }
                dArr[i] = dArr[i] * ((double) v);
                i++;
            }
            CurveFit curveFit = this.mArcSpline;
            if (curveFit != null) {
                double[] dArr2 = this.mInterpolateData;
                if (dArr2.length > 0) {
                    curveFit.getPos((double) position2, dArr2);
                    this.mArcSpline.getSlope((double) position2, this.mInterpolateVelocity);
                    this.mStartMotionPath.setDpDt(locationX, locationY, mAnchorDpDt, this.mInterpolateVariables, this.mInterpolateVelocity, this.mInterpolateData);
                    return;
                }
                return;
            }
            this.mStartMotionPath.setDpDt(locationX, locationY, mAnchorDpDt, this.mInterpolateVariables, dArr, this.mInterpolateData);
            return;
        }
        float dleft = this.mEndMotionPath.x - this.mStartMotionPath.x;
        float dTop = this.mEndMotionPath.y - this.mStartMotionPath.y;
        float dWidth = this.mEndMotionPath.width - this.mStartMotionPath.width;
        float dHeight = this.mEndMotionPath.height - this.mStartMotionPath.height;
        mAnchorDpDt[0] = ((1.0f - locationX) * dleft) + ((dleft + dWidth) * locationX);
        mAnchorDpDt[1] = ((1.0f - locationY) * dTop) + ((dTop + dHeight) * locationY);
    }

    public void getPostLayoutDvDp(float position, int width, int height, float locationX, float locationY, float[] mAnchorDpDt) {
        VelocityMatrix vmat;
        float position2 = getAdjustedPosition(position, this.mVelocity);
        HashMap<String, SplineSet> hashMap = this.mAttributesMap;
        KeyCycleOscillator osc_sy = null;
        SplineSet trans_x = hashMap == null ? null : hashMap.get("translationX");
        HashMap<String, SplineSet> hashMap2 = this.mAttributesMap;
        SplineSet trans_y = hashMap2 == null ? null : hashMap2.get("translationY");
        HashMap<String, SplineSet> hashMap3 = this.mAttributesMap;
        SplineSet rotation = hashMap3 == null ? null : hashMap3.get("rotation");
        HashMap<String, SplineSet> hashMap4 = this.mAttributesMap;
        SplineSet scale_x = hashMap4 == null ? null : hashMap4.get("scaleX");
        HashMap<String, SplineSet> hashMap5 = this.mAttributesMap;
        SplineSet scale_y = hashMap5 == null ? null : hashMap5.get("scaleY");
        HashMap<String, KeyCycleOscillator> hashMap6 = this.mCycleMap;
        KeyCycleOscillator osc_x = hashMap6 == null ? null : hashMap6.get("translationX");
        HashMap<String, KeyCycleOscillator> hashMap7 = this.mCycleMap;
        KeyCycleOscillator osc_y = hashMap7 == null ? null : hashMap7.get("translationY");
        HashMap<String, KeyCycleOscillator> hashMap8 = this.mCycleMap;
        KeyCycleOscillator osc_r = hashMap8 == null ? null : hashMap8.get("rotation");
        HashMap<String, KeyCycleOscillator> hashMap9 = this.mCycleMap;
        KeyCycleOscillator osc_sx = hashMap9 == null ? null : hashMap9.get("scaleX");
        HashMap<String, KeyCycleOscillator> hashMap10 = this.mCycleMap;
        if (hashMap10 != null) {
            osc_sy = hashMap10.get("scaleY");
        }
        VelocityMatrix vmat2 = new VelocityMatrix();
        vmat2.clear();
        vmat2.setRotationVelocity(rotation, position2);
        vmat2.setTranslationVelocity(trans_x, trans_y, position2);
        vmat2.setScaleVelocity(scale_x, scale_y, position2);
        vmat2.setRotationVelocity(osc_r, position2);
        vmat2.setTranslationVelocity(osc_x, osc_y, position2);
        vmat2.setScaleVelocity(osc_sx, osc_sy, position2);
        CurveFit curveFit = this.mArcSpline;
        if (curveFit != null) {
            double[] dArr = this.mInterpolateData;
            if (dArr.length > 0) {
                curveFit.getPos((double) position2, dArr);
                this.mArcSpline.getSlope((double) position2, this.mInterpolateVelocity);
                vmat = vmat2;
                this.mStartMotionPath.setDpDt(locationX, locationY, mAnchorDpDt, this.mInterpolateVariables, this.mInterpolateVelocity, this.mInterpolateData);
            } else {
                vmat = vmat2;
            }
            vmat.applyTransform(locationX, locationY, width, height, mAnchorDpDt);
        } else if (this.mSpline != null) {
            float position3 = getAdjustedPosition(position2, this.mVelocity);
            this.mSpline[0].getSlope((double) position3, this.mInterpolateVelocity);
            this.mSpline[0].getPos((double) position3, this.mInterpolateData);
            float v = this.mVelocity[0];
            int i = 0;
            while (true) {
                double[] dArr2 = this.mInterpolateVelocity;
                if (i < dArr2.length) {
                    dArr2[i] = dArr2[i] * ((double) v);
                    i++;
                } else {
                    this.mStartMotionPath.setDpDt(locationX, locationY, mAnchorDpDt, this.mInterpolateVariables, dArr2, this.mInterpolateData);
                    vmat2.applyTransform(locationX, locationY, width, height, mAnchorDpDt);
                    return;
                }
            }
        } else {
            float dleft = this.mEndMotionPath.x - this.mStartMotionPath.x;
            float dTop = this.mEndMotionPath.y - this.mStartMotionPath.y;
            mAnchorDpDt[0] = ((1.0f - locationX) * dleft) + ((dleft + (this.mEndMotionPath.width - this.mStartMotionPath.width)) * locationX);
            mAnchorDpDt[1] = ((1.0f - locationY) * dTop) + ((dTop + (this.mEndMotionPath.height - this.mStartMotionPath.height)) * locationY);
            vmat2.clear();
            vmat2.setRotationVelocity(rotation, position2);
            vmat2.setTranslationVelocity(trans_x, trans_y, position2);
            vmat2.setScaleVelocity(scale_x, scale_y, position2);
            vmat2.setRotationVelocity(osc_r, position2);
            vmat2.setTranslationVelocity(osc_x, osc_y, position2);
            vmat2.setScaleVelocity(osc_sx, osc_sy, position2);
            vmat2.applyTransform(locationX, locationY, width, height, mAnchorDpDt);
        }
    }

    public int getDrawPath() {
        int mode = this.mStartMotionPath.mDrawPath;
        Iterator<MotionPaths> it = this.mMotionPaths.iterator();
        while (it.hasNext()) {
            mode = Math.max(mode, it.next().mDrawPath);
        }
        return Math.max(mode, this.mEndMotionPath.mDrawPath);
    }

    public void setDrawPath(int debugMode) {
        this.mStartMotionPath.mDrawPath = debugMode;
    }

    String name() {
        return this.mView.getContext().getResources().getResourceEntryName(this.mView.getId());
    }

    public void positionKeyframe(View view, KeyPositionBase key, float x, float y, String[] attribute, float[] value) {
        RectF start = new RectF();
        start.left = this.mStartMotionPath.x;
        start.top = this.mStartMotionPath.y;
        start.right = start.left + this.mStartMotionPath.width;
        start.bottom = start.top + this.mStartMotionPath.height;
        RectF end = new RectF();
        end.left = this.mEndMotionPath.x;
        end.top = this.mEndMotionPath.y;
        end.right = end.left + this.mEndMotionPath.width;
        end.bottom = end.top + this.mEndMotionPath.height;
        key.positionAttributes(view, start, end, x, y, attribute, value);
    }

    public int getkeyFramePositions(int[] type, float[] pos) {
        int i = 0;
        int count = 0;
        Iterator<Key> it = this.mKeyList.iterator();
        while (it.hasNext()) {
            Key key = it.next();
            type[i] = key.mFramePosition + (key.mType * 1000);
            this.mSpline[0].getPos((double) (((float) key.mFramePosition) / 100.0f), this.mInterpolateData);
            this.mStartMotionPath.getCenter(this.mInterpolateVariables, this.mInterpolateData, pos, count);
            count += 2;
            i++;
        }
        return i;
    }

    public int getKeyFrameInfo(int type, int[] info) {
        int count = 0;
        int cursor = 0;
        float[] pos = new float[2];
        Iterator<Key> it = this.mKeyList.iterator();
        while (it.hasNext()) {
            Key key = it.next();
            if (key.mType == type || type != -1) {
                info[cursor] = 0;
                int cursor2 = cursor + 1;
                info[cursor2] = key.mType;
                int cursor3 = cursor2 + 1;
                info[cursor3] = key.mFramePosition;
                this.mSpline[0].getPos((double) (((float) key.mFramePosition) / 100.0f), this.mInterpolateData);
                this.mStartMotionPath.getCenter(this.mInterpolateVariables, this.mInterpolateData, pos, 0);
                int cursor4 = cursor3 + 1;
                info[cursor4] = Float.floatToIntBits(pos[0]);
                int cursor5 = cursor4 + 1;
                info[cursor5] = Float.floatToIntBits(pos[1]);
                if (key instanceof KeyPosition) {
                    KeyPosition kp = (KeyPosition) key;
                    int cursor6 = cursor5 + 1;
                    info[cursor6] = kp.mPositionType;
                    int cursor7 = cursor6 + 1;
                    info[cursor7] = Float.floatToIntBits(kp.mPercentX);
                    cursor5 = cursor7 + 1;
                    info[cursor5] = Float.floatToIntBits(kp.mPercentY);
                }
                cursor = cursor5 + 1;
                info[cursor] = cursor - cursor;
                count++;
            }
        }
        return count;
    }
}
