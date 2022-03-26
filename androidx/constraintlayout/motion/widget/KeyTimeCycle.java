package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import androidx.constraintlayout.motion.widget.TimeCycleSplineSet;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.R;
import androidx.core.app.NotificationCompat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
/* loaded from: classes.dex */
public class KeyTimeCycle extends Key {
    public static final int KEY_TYPE = 3;
    static final String NAME = "KeyTimeCycle";
    private static final String TAG = "KeyTimeCycle";
    private String mTransitionEasing;
    private int mCurveFit = -1;
    private float mAlpha = Float.NaN;
    private float mElevation = Float.NaN;
    private float mRotation = Float.NaN;
    private float mRotationX = Float.NaN;
    private float mRotationY = Float.NaN;
    private float mTransitionPathRotate = Float.NaN;
    private float mScaleX = Float.NaN;
    private float mScaleY = Float.NaN;
    private float mTranslationX = Float.NaN;
    private float mTranslationY = Float.NaN;
    private float mTranslationZ = Float.NaN;
    private float mProgress = Float.NaN;
    private int mWaveShape = 0;
    private float mWavePeriod = Float.NaN;
    private float mWaveOffset = 0.0f;

    public KeyTimeCycle() {
        this.mType = 3;
        this.mCustomConstraints = new HashMap();
    }

    @Override // androidx.constraintlayout.motion.widget.Key
    public void load(Context context, AttributeSet attrs) {
        Loader.read(this, context.obtainStyledAttributes(attrs, R.styleable.KeyTimeCycle));
    }

    @Override // androidx.constraintlayout.motion.widget.Key
    public void getAttributeNames(HashSet<String> attributes) {
        if (!Float.isNaN(this.mAlpha)) {
            attributes.add("alpha");
        }
        if (!Float.isNaN(this.mElevation)) {
            attributes.add("elevation");
        }
        if (!Float.isNaN(this.mRotation)) {
            attributes.add("rotation");
        }
        if (!Float.isNaN(this.mRotationX)) {
            attributes.add("rotationX");
        }
        if (!Float.isNaN(this.mRotationY)) {
            attributes.add("rotationY");
        }
        if (!Float.isNaN(this.mTranslationX)) {
            attributes.add("translationX");
        }
        if (!Float.isNaN(this.mTranslationY)) {
            attributes.add("translationY");
        }
        if (!Float.isNaN(this.mTranslationZ)) {
            attributes.add("translationZ");
        }
        if (!Float.isNaN(this.mTransitionPathRotate)) {
            attributes.add("transitionPathRotate");
        }
        if (!Float.isNaN(this.mScaleX)) {
            attributes.add("scaleX");
        }
        if (!Float.isNaN(this.mScaleY)) {
            attributes.add("scaleY");
        }
        if (!Float.isNaN(this.mProgress)) {
            attributes.add(NotificationCompat.CATEGORY_PROGRESS);
        }
        if (this.mCustomConstraints.size() > 0) {
            Iterator it = this.mCustomConstraints.keySet().iterator();
            while (it.hasNext()) {
                attributes.add("CUSTOM," + ((String) it.next()));
            }
        }
    }

    @Override // androidx.constraintlayout.motion.widget.Key
    public void setInterpolation(HashMap<String, Integer> interpolation) {
        if (this.mCurveFit != -1) {
            if (!Float.isNaN(this.mAlpha)) {
                interpolation.put("alpha", Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mElevation)) {
                interpolation.put("elevation", Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mRotation)) {
                interpolation.put("rotation", Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mRotationX)) {
                interpolation.put("rotationX", Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mRotationY)) {
                interpolation.put("rotationY", Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mTranslationX)) {
                interpolation.put("translationX", Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mTranslationY)) {
                interpolation.put("translationY", Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mTranslationZ)) {
                interpolation.put("translationZ", Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mTransitionPathRotate)) {
                interpolation.put("transitionPathRotate", Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mScaleX)) {
                interpolation.put("scaleX", Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mScaleX)) {
                interpolation.put("scaleY", Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mProgress)) {
                interpolation.put(NotificationCompat.CATEGORY_PROGRESS, Integer.valueOf(this.mCurveFit));
            }
            if (this.mCustomConstraints.size() > 0) {
                Iterator it = this.mCustomConstraints.keySet().iterator();
                while (it.hasNext()) {
                    interpolation.put("CUSTOM," + ((String) it.next()), Integer.valueOf(this.mCurveFit));
                }
            }
        }
    }

    @Override // androidx.constraintlayout.motion.widget.Key
    public void addValues(HashMap<String, SplineSet> splines) {
        throw new IllegalArgumentException(" KeyTimeCycles do not support SplineSet");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x006c, code lost:
        if (r1.equals("elevation") != false) goto L_0x00ce;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public void addTimeValues(HashMap<String, TimeCycleSplineSet> splines) {
        Iterator<String> it = splines.keySet().iterator();
        while (it.hasNext()) {
            String s = it.next();
            TimeCycleSplineSet splineSet = splines.get(s);
            char c = 1;
            if (s.startsWith("CUSTOM")) {
                ConstraintAttribute cvalue = (ConstraintAttribute) this.mCustomConstraints.get(s.substring("CUSTOM".length() + 1));
                if (cvalue != null) {
                    ((TimeCycleSplineSet.CustomSet) splineSet).setPoint(this.mFramePosition, cvalue, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                }
            } else {
                switch (s.hashCode()) {
                    case -1249320806:
                        if (s.equals("rotationX")) {
                            c = 3;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1249320805:
                        if (s.equals("rotationY")) {
                            c = 4;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1225497657:
                        if (s.equals("translationX")) {
                            c = '\b';
                            break;
                        }
                        c = 65535;
                        break;
                    case -1225497656:
                        if (s.equals("translationY")) {
                            c = '\t';
                            break;
                        }
                        c = 65535;
                        break;
                    case -1225497655:
                        if (s.equals("translationZ")) {
                            c = '\n';
                            break;
                        }
                        c = 65535;
                        break;
                    case -1001078227:
                        if (s.equals(NotificationCompat.CATEGORY_PROGRESS)) {
                            c = 11;
                            break;
                        }
                        c = 65535;
                        break;
                    case -908189618:
                        if (s.equals("scaleX")) {
                            c = 6;
                            break;
                        }
                        c = 65535;
                        break;
                    case -908189617:
                        if (s.equals("scaleY")) {
                            c = 7;
                            break;
                        }
                        c = 65535;
                        break;
                    case -40300674:
                        if (s.equals("rotation")) {
                            c = 2;
                            break;
                        }
                        c = 65535;
                        break;
                    case -4379043:
                        break;
                    case 37232917:
                        if (s.equals("transitionPathRotate")) {
                            c = 5;
                            break;
                        }
                        c = 65535;
                        break;
                    case 92909918:
                        if (s.equals("alpha")) {
                            c = 0;
                            break;
                        }
                        c = 65535;
                        break;
                    default:
                        c = 65535;
                        break;
                }
                switch (c) {
                    case 0:
                        if (!Float.isNaN(this.mAlpha)) {
                            splineSet.setPoint(this.mFramePosition, this.mAlpha, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case 1:
                        if (!Float.isNaN(this.mElevation)) {
                            splineSet.setPoint(this.mFramePosition, this.mElevation, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case 2:
                        if (!Float.isNaN(this.mRotation)) {
                            splineSet.setPoint(this.mFramePosition, this.mRotation, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case 3:
                        if (!Float.isNaN(this.mRotationX)) {
                            splineSet.setPoint(this.mFramePosition, this.mRotationX, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case 4:
                        if (!Float.isNaN(this.mRotationY)) {
                            splineSet.setPoint(this.mFramePosition, this.mRotationY, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case 5:
                        if (!Float.isNaN(this.mTransitionPathRotate)) {
                            splineSet.setPoint(this.mFramePosition, this.mTransitionPathRotate, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case 6:
                        if (!Float.isNaN(this.mScaleX)) {
                            splineSet.setPoint(this.mFramePosition, this.mScaleX, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case 7:
                        if (!Float.isNaN(this.mScaleY)) {
                            splineSet.setPoint(this.mFramePosition, this.mScaleY, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case '\b':
                        if (!Float.isNaN(this.mTranslationX)) {
                            splineSet.setPoint(this.mFramePosition, this.mTranslationX, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case '\t':
                        if (!Float.isNaN(this.mTranslationY)) {
                            splineSet.setPoint(this.mFramePosition, this.mTranslationY, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case '\n':
                        if (!Float.isNaN(this.mTranslationZ)) {
                            splineSet.setPoint(this.mFramePosition, this.mTranslationZ, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case 11:
                        if (!Float.isNaN(this.mProgress)) {
                            splineSet.setPoint(this.mFramePosition, this.mProgress, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    default:
                        Log.e("KeyTimeCycles", "UNKNOWN addValues \"" + s + "\"");
                        continue;
                }
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // androidx.constraintlayout.motion.widget.Key
    public void setValue(String tag, Object value) {
        char c;
        switch (tag.hashCode()) {
            case -1812823328:
                if (tag.equals("transitionEasing")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case -1249320806:
                if (tag.equals("rotationX")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -1249320805:
                if (tag.equals("rotationY")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -1225497657:
                if (tag.equals("translationX")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case -1225497656:
                if (tag.equals("translationY")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case -1001078227:
                if (tag.equals(NotificationCompat.CATEGORY_PROGRESS)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -908189618:
                if (tag.equals("scaleX")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case -908189617:
                if (tag.equals("scaleY")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -40300674:
                if (tag.equals("rotation")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -4379043:
                if (tag.equals("elevation")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 37232917:
                if (tag.equals("transitionPathRotate")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 92909918:
                if (tag.equals("alpha")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 579057826:
                if (tag.equals("curveFit")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1317633238:
                if (tag.equals("mTranslationZ")) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                this.mAlpha = toFloat(value);
                return;
            case 1:
                this.mCurveFit = toInt(value);
                return;
            case 2:
                this.mElevation = toFloat(value);
                return;
            case 3:
                this.mProgress = toFloat(value);
                return;
            case 4:
                this.mRotation = toFloat(value);
                return;
            case 5:
                this.mRotationX = toFloat(value);
                return;
            case 6:
                this.mRotationY = toFloat(value);
                return;
            case 7:
                this.mScaleX = toFloat(value);
                return;
            case '\b':
                this.mScaleY = toFloat(value);
                return;
            case '\t':
                this.mTransitionEasing = value.toString();
                return;
            case '\n':
                this.mTransitionPathRotate = toFloat(value);
                return;
            case 11:
                this.mTranslationX = toFloat(value);
                return;
            case '\f':
                this.mTranslationY = toFloat(value);
                return;
            case '\r':
                this.mTranslationZ = toFloat(value);
                return;
            default:
                return;
        }
    }

    /* loaded from: classes.dex */
    private static class Loader {
        private static final int ANDROID_ALPHA = 1;
        private static final int ANDROID_ELEVATION = 2;
        private static final int ANDROID_ROTATION = 4;
        private static final int ANDROID_ROTATION_X = 5;
        private static final int ANDROID_ROTATION_Y = 6;
        private static final int ANDROID_SCALE_X = 7;
        private static final int ANDROID_SCALE_Y = 14;
        private static final int ANDROID_TRANSLATION_X = 15;
        private static final int ANDROID_TRANSLATION_Y = 16;
        private static final int ANDROID_TRANSLATION_Z = 17;
        private static final int CURVE_FIT = 13;
        private static final int FRAME_POSITION = 12;
        private static final int PROGRESS = 18;
        private static final int TARGET_ID = 10;
        private static final int TRANSITION_EASING = 9;
        private static final int TRANSITION_PATH_ROTATE = 8;
        private static final int WAVE_OFFSET = 21;
        private static final int WAVE_PERIOD = 20;
        private static final int WAVE_SHAPE = 19;
        private static SparseIntArray mAttrMap = new SparseIntArray();

        private Loader() {
        }

        static {
            mAttrMap.append(R.styleable.KeyTimeCycle_android_alpha, 1);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_elevation, 2);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_rotation, 4);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_rotationX, 5);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_rotationY, 6);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_scaleX, 7);
            mAttrMap.append(R.styleable.KeyTimeCycle_transitionPathRotate, 8);
            mAttrMap.append(R.styleable.KeyTimeCycle_transitionEasing, 9);
            mAttrMap.append(R.styleable.KeyTimeCycle_motionTarget, 10);
            mAttrMap.append(R.styleable.KeyTimeCycle_framePosition, 12);
            mAttrMap.append(R.styleable.KeyTimeCycle_curveFit, 13);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_scaleY, 14);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_translationX, 15);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_translationY, 16);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_translationZ, 17);
            mAttrMap.append(R.styleable.KeyTimeCycle_motionProgress, 18);
            mAttrMap.append(R.styleable.KeyTimeCycle_wavePeriod, 20);
            mAttrMap.append(R.styleable.KeyTimeCycle_waveOffset, 21);
            mAttrMap.append(R.styleable.KeyTimeCycle_waveShape, 19);
        }

        public static void read(KeyTimeCycle c, TypedArray a2) {
            int N = a2.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a2.getIndex(i);
                switch (mAttrMap.get(attr)) {
                    case 1:
                        c.mAlpha = a2.getFloat(attr, c.mAlpha);
                        break;
                    case 2:
                        c.mElevation = a2.getDimension(attr, c.mElevation);
                        break;
                    case 3:
                    case 11:
                    default:
                        Log.e("KeyTimeCycle", "unused attribute 0x" + Integer.toHexString(attr) + "   " + mAttrMap.get(attr));
                        break;
                    case 4:
                        c.mRotation = a2.getFloat(attr, c.mRotation);
                        break;
                    case 5:
                        c.mRotationX = a2.getFloat(attr, c.mRotationX);
                        break;
                    case 6:
                        c.mRotationY = a2.getFloat(attr, c.mRotationY);
                        break;
                    case 7:
                        c.mScaleX = a2.getFloat(attr, c.mScaleX);
                        break;
                    case 8:
                        c.mTransitionPathRotate = a2.getFloat(attr, c.mTransitionPathRotate);
                        break;
                    case 9:
                        c.mTransitionEasing = a2.getString(attr);
                        break;
                    case 10:
                        if (!MotionLayout.IS_IN_EDIT_MODE) {
                            if (a2.peekValue(attr).type == 3) {
                                c.mTargetString = a2.getString(attr);
                                break;
                            } else {
                                c.mTargetId = a2.getResourceId(attr, c.mTargetId);
                                break;
                            }
                        } else {
                            c.mTargetId = a2.getResourceId(attr, c.mTargetId);
                            if (c.mTargetId == -1) {
                                c.mTargetString = a2.getString(attr);
                                break;
                            } else {
                                break;
                            }
                        }
                    case 12:
                        c.mFramePosition = a2.getInt(attr, c.mFramePosition);
                        break;
                    case 13:
                        c.mCurveFit = a2.getInteger(attr, c.mCurveFit);
                        break;
                    case 14:
                        c.mScaleY = a2.getFloat(attr, c.mScaleY);
                        break;
                    case 15:
                        c.mTranslationX = a2.getDimension(attr, c.mTranslationX);
                        break;
                    case 16:
                        c.mTranslationY = a2.getDimension(attr, c.mTranslationY);
                        break;
                    case 17:
                        if (Build.VERSION.SDK_INT >= 21) {
                            c.mTranslationZ = a2.getDimension(attr, c.mTranslationZ);
                            break;
                        } else {
                            break;
                        }
                    case 18:
                        c.mProgress = a2.getFloat(attr, c.mProgress);
                        break;
                    case 19:
                        c.mWaveShape = a2.getInt(attr, c.mWaveShape);
                        break;
                    case 20:
                        c.mWavePeriod = a2.getFloat(attr, c.mWavePeriod);
                        break;
                    case 21:
                        if (a2.peekValue(attr).type == 5) {
                            c.mWaveOffset = a2.getDimension(attr, c.mWaveOffset);
                            break;
                        } else {
                            c.mWaveOffset = a2.getFloat(attr, c.mWaveOffset);
                            break;
                        }
                }
            }
        }
    }
}
