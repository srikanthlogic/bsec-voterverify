package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.motion.utils.Easing;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.HelperWidget;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import androidx.constraintlayout.widget.R;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class ConstraintSet {
    private static final int ALPHA;
    private static final int ANIMATE_RELATIVE_TO;
    private static final int BARRIER_ALLOWS_GONE_WIDGETS;
    private static final int BARRIER_DIRECTION;
    private static final int BARRIER_MARGIN;
    private static final int BARRIER_TYPE;
    public static final int BASELINE;
    private static final int BASELINE_TO_BASELINE;
    public static final int BOTTOM;
    private static final int BOTTOM_MARGIN;
    private static final int BOTTOM_TO_BOTTOM;
    private static final int BOTTOM_TO_TOP;
    public static final int CHAIN_PACKED;
    public static final int CHAIN_SPREAD;
    public static final int CHAIN_SPREAD_INSIDE;
    private static final int CHAIN_USE_RTL;
    private static final int CIRCLE;
    private static final int CIRCLE_ANGLE;
    private static final int CIRCLE_RADIUS;
    private static final int CONSTRAINED_HEIGHT;
    private static final int CONSTRAINED_WIDTH;
    private static final int CONSTRAINT_REFERENCED_IDS;
    private static final int CONSTRAINT_TAG;
    private static final boolean DEBUG;
    private static final int DIMENSION_RATIO;
    private static final int DRAW_PATH;
    private static final int EDITOR_ABSOLUTE_X;
    private static final int EDITOR_ABSOLUTE_Y;
    private static final int ELEVATION;
    public static final int END;
    private static final int END_MARGIN;
    private static final int END_TO_END;
    private static final int END_TO_START;
    private static final String ERROR_MESSAGE;
    public static final int GONE;
    private static final int GONE_BOTTOM_MARGIN;
    private static final int GONE_END_MARGIN;
    private static final int GONE_LEFT_MARGIN;
    private static final int GONE_RIGHT_MARGIN;
    private static final int GONE_START_MARGIN;
    private static final int GONE_TOP_MARGIN;
    private static final int GUIDE_BEGIN;
    private static final int GUIDE_END;
    private static final int GUIDE_PERCENT;
    private static final int HEIGHT_DEFAULT;
    private static final int HEIGHT_MAX;
    private static final int HEIGHT_MIN;
    private static final int HEIGHT_PERCENT;
    public static final int HORIZONTAL;
    private static final int HORIZONTAL_BIAS;
    public static final int HORIZONTAL_GUIDELINE;
    private static final int HORIZONTAL_STYLE;
    private static final int HORIZONTAL_WEIGHT;
    public static final int INVISIBLE;
    private static final int LAYOUT_HEIGHT;
    private static final int LAYOUT_VISIBILITY;
    private static final int LAYOUT_WIDTH;
    public static final int LEFT;
    private static final int LEFT_MARGIN;
    private static final int LEFT_TO_LEFT;
    private static final int LEFT_TO_RIGHT;
    public static final int MATCH_CONSTRAINT;
    public static final int MATCH_CONSTRAINT_SPREAD;
    public static final int MATCH_CONSTRAINT_WRAP;
    private static final int MOTION_STAGGER;
    private static final int ORIENTATION;
    public static final int PARENT_ID;
    private static final int PATH_MOTION_ARC;
    private static final int PROGRESS;
    public static final int RIGHT;
    private static final int RIGHT_MARGIN;
    private static final int RIGHT_TO_LEFT;
    private static final int RIGHT_TO_RIGHT;
    private static final int ROTATION;
    private static final int ROTATION_X;
    private static final int ROTATION_Y;
    private static final int SCALE_X;
    private static final int SCALE_Y;
    public static final int START;
    private static final int START_MARGIN;
    private static final int START_TO_END;
    private static final int START_TO_START;
    private static final String TAG;
    public static final int TOP;
    private static final int TOP_MARGIN;
    private static final int TOP_TO_BOTTOM;
    private static final int TOP_TO_TOP;
    private static final int TRANSFORM_PIVOT_X;
    private static final int TRANSFORM_PIVOT_Y;
    private static final int TRANSITION_EASING;
    private static final int TRANSITION_PATH_ROTATE;
    private static final int TRANSLATION_X;
    private static final int TRANSLATION_Y;
    private static final int TRANSLATION_Z;
    public static final int UNSET;
    private static final int UNUSED;
    public static final int VERTICAL;
    private static final int VERTICAL_BIAS;
    public static final int VERTICAL_GUIDELINE;
    private static final int VERTICAL_STYLE;
    private static final int VERTICAL_WEIGHT;
    private static final int VIEW_ID;
    private static final int VISIBILITY_MODE;
    public static final int VISIBILITY_MODE_IGNORE;
    public static final int VISIBILITY_MODE_NORMAL;
    public static final int VISIBLE;
    private static final int WIDTH_DEFAULT;
    private static final int WIDTH_MAX;
    private static final int WIDTH_MIN;
    private static final int WIDTH_PERCENT;
    public static final int WRAP_CONTENT;
    private boolean mValidate;
    private static final int[] VISIBILITY_FLAGS = {0, 4, 8};
    private static SparseIntArray mapToConstant = new SparseIntArray();
    private HashMap<String, ConstraintAttribute> mSavedAttributes = new HashMap<>();
    private boolean mForceId = true;
    private HashMap<Integer, Constraint> mConstraints = new HashMap<>();

    static {
        mapToConstant.append(R.styleable.Constraint_layout_constraintLeft_toLeftOf, 25);
        mapToConstant.append(R.styleable.Constraint_layout_constraintLeft_toRightOf, 26);
        mapToConstant.append(R.styleable.Constraint_layout_constraintRight_toLeftOf, 29);
        mapToConstant.append(R.styleable.Constraint_layout_constraintRight_toRightOf, 30);
        mapToConstant.append(R.styleable.Constraint_layout_constraintTop_toTopOf, 36);
        mapToConstant.append(R.styleable.Constraint_layout_constraintTop_toBottomOf, 35);
        mapToConstant.append(R.styleable.Constraint_layout_constraintBottom_toTopOf, 4);
        mapToConstant.append(R.styleable.Constraint_layout_constraintBottom_toBottomOf, 3);
        mapToConstant.append(R.styleable.Constraint_layout_constraintBaseline_toBaselineOf, 1);
        mapToConstant.append(R.styleable.Constraint_layout_editor_absoluteX, 6);
        mapToConstant.append(R.styleable.Constraint_layout_editor_absoluteY, 7);
        mapToConstant.append(R.styleable.Constraint_layout_constraintGuide_begin, 17);
        mapToConstant.append(R.styleable.Constraint_layout_constraintGuide_end, 18);
        mapToConstant.append(R.styleable.Constraint_layout_constraintGuide_percent, 19);
        mapToConstant.append(R.styleable.Constraint_android_orientation, 27);
        mapToConstant.append(R.styleable.Constraint_layout_constraintStart_toEndOf, 32);
        mapToConstant.append(R.styleable.Constraint_layout_constraintStart_toStartOf, 33);
        mapToConstant.append(R.styleable.Constraint_layout_constraintEnd_toStartOf, 10);
        mapToConstant.append(R.styleable.Constraint_layout_constraintEnd_toEndOf, 9);
        mapToConstant.append(R.styleable.Constraint_layout_goneMarginLeft, 13);
        mapToConstant.append(R.styleable.Constraint_layout_goneMarginTop, 16);
        mapToConstant.append(R.styleable.Constraint_layout_goneMarginRight, 14);
        mapToConstant.append(R.styleable.Constraint_layout_goneMarginBottom, 11);
        mapToConstant.append(R.styleable.Constraint_layout_goneMarginStart, 15);
        mapToConstant.append(R.styleable.Constraint_layout_goneMarginEnd, 12);
        mapToConstant.append(R.styleable.Constraint_layout_constraintVertical_weight, 40);
        mapToConstant.append(R.styleable.Constraint_layout_constraintHorizontal_weight, 39);
        mapToConstant.append(R.styleable.Constraint_layout_constraintHorizontal_chainStyle, 41);
        mapToConstant.append(R.styleable.Constraint_layout_constraintVertical_chainStyle, 42);
        mapToConstant.append(R.styleable.Constraint_layout_constraintHorizontal_bias, 20);
        mapToConstant.append(R.styleable.Constraint_layout_constraintVertical_bias, 37);
        mapToConstant.append(R.styleable.Constraint_layout_constraintDimensionRatio, 5);
        mapToConstant.append(R.styleable.Constraint_layout_constraintLeft_creator, 82);
        mapToConstant.append(R.styleable.Constraint_layout_constraintTop_creator, 82);
        mapToConstant.append(R.styleable.Constraint_layout_constraintRight_creator, 82);
        mapToConstant.append(R.styleable.Constraint_layout_constraintBottom_creator, 82);
        mapToConstant.append(R.styleable.Constraint_layout_constraintBaseline_creator, 82);
        mapToConstant.append(R.styleable.Constraint_android_layout_marginLeft, 24);
        mapToConstant.append(R.styleable.Constraint_android_layout_marginRight, 28);
        mapToConstant.append(R.styleable.Constraint_android_layout_marginStart, 31);
        mapToConstant.append(R.styleable.Constraint_android_layout_marginEnd, 8);
        mapToConstant.append(R.styleable.Constraint_android_layout_marginTop, 34);
        mapToConstant.append(R.styleable.Constraint_android_layout_marginBottom, 2);
        mapToConstant.append(R.styleable.Constraint_android_layout_width, 23);
        mapToConstant.append(R.styleable.Constraint_android_layout_height, 21);
        mapToConstant.append(R.styleable.Constraint_android_visibility, 22);
        mapToConstant.append(R.styleable.Constraint_android_alpha, 43);
        mapToConstant.append(R.styleable.Constraint_android_elevation, 44);
        mapToConstant.append(R.styleable.Constraint_android_rotationX, 45);
        mapToConstant.append(R.styleable.Constraint_android_rotationY, 46);
        mapToConstant.append(R.styleable.Constraint_android_rotation, 60);
        mapToConstant.append(R.styleable.Constraint_android_scaleX, 47);
        mapToConstant.append(R.styleable.Constraint_android_scaleY, 48);
        mapToConstant.append(R.styleable.Constraint_android_transformPivotX, 49);
        mapToConstant.append(R.styleable.Constraint_android_transformPivotY, 50);
        mapToConstant.append(R.styleable.Constraint_android_translationX, 51);
        mapToConstant.append(R.styleable.Constraint_android_translationY, 52);
        mapToConstant.append(R.styleable.Constraint_android_translationZ, 53);
        mapToConstant.append(R.styleable.Constraint_layout_constraintWidth_default, 54);
        mapToConstant.append(R.styleable.Constraint_layout_constraintHeight_default, 55);
        mapToConstant.append(R.styleable.Constraint_layout_constraintWidth_max, 56);
        mapToConstant.append(R.styleable.Constraint_layout_constraintHeight_max, 57);
        mapToConstant.append(R.styleable.Constraint_layout_constraintWidth_min, 58);
        mapToConstant.append(R.styleable.Constraint_layout_constraintHeight_min, 59);
        mapToConstant.append(R.styleable.Constraint_layout_constraintCircle, 61);
        mapToConstant.append(R.styleable.Constraint_layout_constraintCircleRadius, 62);
        mapToConstant.append(R.styleable.Constraint_layout_constraintCircleAngle, 63);
        mapToConstant.append(R.styleable.Constraint_animate_relativeTo, 64);
        mapToConstant.append(R.styleable.Constraint_transitionEasing, 65);
        mapToConstant.append(R.styleable.Constraint_drawPath, 66);
        mapToConstant.append(R.styleable.Constraint_transitionPathRotate, 67);
        mapToConstant.append(R.styleable.Constraint_motionStagger, 79);
        mapToConstant.append(R.styleable.Constraint_android_id, 38);
        mapToConstant.append(R.styleable.Constraint_motionProgress, 68);
        mapToConstant.append(R.styleable.Constraint_layout_constraintWidth_percent, 69);
        mapToConstant.append(R.styleable.Constraint_layout_constraintHeight_percent, 70);
        mapToConstant.append(R.styleable.Constraint_chainUseRtl, 71);
        mapToConstant.append(R.styleable.Constraint_barrierDirection, 72);
        mapToConstant.append(R.styleable.Constraint_barrierMargin, 73);
        mapToConstant.append(R.styleable.Constraint_constraint_referenced_ids, 74);
        mapToConstant.append(R.styleable.Constraint_barrierAllowsGoneWidgets, 75);
        mapToConstant.append(R.styleable.Constraint_pathMotionArc, 76);
        mapToConstant.append(R.styleable.Constraint_layout_constraintTag, 77);
        mapToConstant.append(R.styleable.Constraint_visibilityMode, 78);
        mapToConstant.append(R.styleable.Constraint_layout_constrainedWidth, 80);
        mapToConstant.append(R.styleable.Constraint_layout_constrainedHeight, 81);
    }

    public HashMap<String, ConstraintAttribute> getCustomAttributeSet() {
        return this.mSavedAttributes;
    }

    public Constraint getParameters(int mId) {
        return get(mId);
    }

    public void readFallback(ConstraintSet set) {
        for (Integer key : set.mConstraints.keySet()) {
            int id = key.intValue();
            Constraint parent = set.mConstraints.get(key);
            if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
                this.mConstraints.put(Integer.valueOf(id), new Constraint());
            }
            Constraint constraint = this.mConstraints.get(Integer.valueOf(id));
            if (!constraint.layout.mApply) {
                constraint.layout.copyFrom(parent.layout);
            }
            if (!constraint.propertySet.mApply) {
                constraint.propertySet.copyFrom(parent.propertySet);
            }
            if (!constraint.transform.mApply) {
                constraint.transform.copyFrom(parent.transform);
            }
            if (!constraint.motion.mApply) {
                constraint.motion.copyFrom(parent.motion);
            }
            for (String s : parent.mCustomConstraints.keySet()) {
                if (!constraint.mCustomConstraints.containsKey(s)) {
                    constraint.mCustomConstraints.put(s, parent.mCustomConstraints.get(s));
                }
            }
        }
    }

    public void readFallback(ConstraintLayout constraintLayout) {
        int count = constraintLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = constraintLayout.getChildAt(i);
            ConstraintLayout.LayoutParams param = (ConstraintLayout.LayoutParams) view.getLayoutParams();
            int id = view.getId();
            if (!this.mForceId || id != -1) {
                if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
                    this.mConstraints.put(Integer.valueOf(id), new Constraint());
                }
                Constraint constraint = this.mConstraints.get(Integer.valueOf(id));
                if (!constraint.layout.mApply) {
                    constraint.fillFrom(id, param);
                    if (view instanceof ConstraintHelper) {
                        constraint.layout.mReferenceIds = ((ConstraintHelper) view).getReferencedIds();
                        if (view instanceof Barrier) {
                            Barrier barrier = (Barrier) view;
                            constraint.layout.mBarrierAllowsGoneWidgets = barrier.allowsGoneWidget();
                            constraint.layout.mBarrierDirection = barrier.getType();
                            constraint.layout.mBarrierMargin = barrier.getMargin();
                        }
                    }
                    constraint.layout.mApply = true;
                }
                if (!constraint.propertySet.mApply) {
                    constraint.propertySet.visibility = view.getVisibility();
                    constraint.propertySet.alpha = view.getAlpha();
                    constraint.propertySet.mApply = true;
                }
                if (Build.VERSION.SDK_INT >= 17 && !constraint.transform.mApply) {
                    constraint.transform.mApply = true;
                    constraint.transform.rotation = view.getRotation();
                    constraint.transform.rotationX = view.getRotationX();
                    constraint.transform.rotationY = view.getRotationY();
                    constraint.transform.scaleX = view.getScaleX();
                    constraint.transform.scaleY = view.getScaleY();
                    float pivotX = view.getPivotX();
                    float pivotY = view.getPivotY();
                    if (!(((double) pivotX) == 0.0d && ((double) pivotY) == 0.0d)) {
                        constraint.transform.transformPivotX = pivotX;
                        constraint.transform.transformPivotY = pivotY;
                    }
                    constraint.transform.translationX = view.getTranslationX();
                    constraint.transform.translationY = view.getTranslationY();
                    if (Build.VERSION.SDK_INT >= 21) {
                        constraint.transform.translationZ = view.getTranslationZ();
                        if (constraint.transform.applyElevation) {
                            constraint.transform.elevation = view.getElevation();
                        }
                    }
                }
            } else {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
        }
    }

    /* loaded from: classes.dex */
    public static class Layout {
        private static final int BARRIER_ALLOWS_GONE_WIDGETS;
        private static final int BARRIER_DIRECTION;
        private static final int BARRIER_MARGIN;
        private static final int BASELINE_TO_BASELINE;
        private static final int BOTTOM_MARGIN;
        private static final int BOTTOM_TO_BOTTOM;
        private static final int BOTTOM_TO_TOP;
        private static final int CHAIN_USE_RTL;
        private static final int CIRCLE;
        private static final int CIRCLE_ANGLE;
        private static final int CIRCLE_RADIUS;
        private static final int CONSTRAINT_REFERENCED_IDS;
        private static final int DIMENSION_RATIO;
        private static final int EDITOR_ABSOLUTE_X;
        private static final int EDITOR_ABSOLUTE_Y;
        private static final int END_MARGIN;
        private static final int END_TO_END;
        private static final int END_TO_START;
        private static final int GONE_BOTTOM_MARGIN;
        private static final int GONE_END_MARGIN;
        private static final int GONE_LEFT_MARGIN;
        private static final int GONE_RIGHT_MARGIN;
        private static final int GONE_START_MARGIN;
        private static final int GONE_TOP_MARGIN;
        private static final int GUIDE_BEGIN;
        private static final int GUIDE_END;
        private static final int GUIDE_PERCENT;
        private static final int HEIGHT_PERCENT;
        private static final int HORIZONTAL_BIAS;
        private static final int HORIZONTAL_STYLE;
        private static final int HORIZONTAL_WEIGHT;
        private static final int LAYOUT_HEIGHT;
        private static final int LAYOUT_WIDTH;
        private static final int LEFT_MARGIN;
        private static final int LEFT_TO_LEFT;
        private static final int LEFT_TO_RIGHT;
        private static final int ORIENTATION;
        private static final int RIGHT_MARGIN;
        private static final int RIGHT_TO_LEFT;
        private static final int RIGHT_TO_RIGHT;
        private static final int START_MARGIN;
        private static final int START_TO_END;
        private static final int START_TO_START;
        private static final int TOP_MARGIN;
        private static final int TOP_TO_BOTTOM;
        private static final int TOP_TO_TOP;
        public static final int UNSET;
        private static final int UNUSED;
        private static final int VERTICAL_BIAS;
        private static final int VERTICAL_STYLE;
        private static final int VERTICAL_WEIGHT;
        private static final int WIDTH_PERCENT;
        private static SparseIntArray mapToConstant = new SparseIntArray();
        public String mConstraintTag;
        public int mHeight;
        public String mReferenceIdString;
        public int[] mReferenceIds;
        public int mWidth;
        public boolean mIsGuideline = false;
        public boolean mApply = false;
        public int guideBegin = -1;
        public int guideEnd = -1;
        public float guidePercent = -1.0f;
        public int leftToLeft = -1;
        public int leftToRight = -1;
        public int rightToLeft = -1;
        public int rightToRight = -1;
        public int topToTop = -1;
        public int topToBottom = -1;
        public int bottomToTop = -1;
        public int bottomToBottom = -1;
        public int baselineToBaseline = -1;
        public int startToEnd = -1;
        public int startToStart = -1;
        public int endToStart = -1;
        public int endToEnd = -1;
        public float horizontalBias = 0.5f;
        public float verticalBias = 0.5f;
        public String dimensionRatio = null;
        public int circleConstraint = -1;
        public int circleRadius = 0;
        public float circleAngle = 0.0f;
        public int editorAbsoluteX = -1;
        public int editorAbsoluteY = -1;
        public int orientation = -1;
        public int leftMargin = -1;
        public int rightMargin = -1;
        public int topMargin = -1;
        public int bottomMargin = -1;
        public int endMargin = -1;
        public int startMargin = -1;
        public int goneLeftMargin = -1;
        public int goneTopMargin = -1;
        public int goneRightMargin = -1;
        public int goneBottomMargin = -1;
        public int goneEndMargin = -1;
        public int goneStartMargin = -1;
        public float verticalWeight = -1.0f;
        public float horizontalWeight = -1.0f;
        public int horizontalChainStyle = 0;
        public int verticalChainStyle = 0;
        public int widthDefault = 0;
        public int heightDefault = 0;
        public int widthMax = -1;
        public int heightMax = -1;
        public int widthMin = -1;
        public int heightMin = -1;
        public float widthPercent = 1.0f;
        public float heightPercent = 1.0f;
        public int mBarrierDirection = -1;
        public int mBarrierMargin = 0;
        public int mHelperType = -1;
        public boolean constrainedWidth = false;
        public boolean constrainedHeight = false;
        public boolean mBarrierAllowsGoneWidgets = true;

        public void copyFrom(Layout src) {
            this.mIsGuideline = src.mIsGuideline;
            this.mWidth = src.mWidth;
            this.mApply = src.mApply;
            this.mHeight = src.mHeight;
            this.guideBegin = src.guideBegin;
            this.guideEnd = src.guideEnd;
            this.guidePercent = src.guidePercent;
            this.leftToLeft = src.leftToLeft;
            this.leftToRight = src.leftToRight;
            this.rightToLeft = src.rightToLeft;
            this.rightToRight = src.rightToRight;
            this.topToTop = src.topToTop;
            this.topToBottom = src.topToBottom;
            this.bottomToTop = src.bottomToTop;
            this.bottomToBottom = src.bottomToBottom;
            this.baselineToBaseline = src.baselineToBaseline;
            this.startToEnd = src.startToEnd;
            this.startToStart = src.startToStart;
            this.endToStart = src.endToStart;
            this.endToEnd = src.endToEnd;
            this.horizontalBias = src.horizontalBias;
            this.verticalBias = src.verticalBias;
            this.dimensionRatio = src.dimensionRatio;
            this.circleConstraint = src.circleConstraint;
            this.circleRadius = src.circleRadius;
            this.circleAngle = src.circleAngle;
            this.editorAbsoluteX = src.editorAbsoluteX;
            this.editorAbsoluteY = src.editorAbsoluteY;
            this.orientation = src.orientation;
            this.leftMargin = src.leftMargin;
            this.rightMargin = src.rightMargin;
            this.topMargin = src.topMargin;
            this.bottomMargin = src.bottomMargin;
            this.endMargin = src.endMargin;
            this.startMargin = src.startMargin;
            this.goneLeftMargin = src.goneLeftMargin;
            this.goneTopMargin = src.goneTopMargin;
            this.goneRightMargin = src.goneRightMargin;
            this.goneBottomMargin = src.goneBottomMargin;
            this.goneEndMargin = src.goneEndMargin;
            this.goneStartMargin = src.goneStartMargin;
            this.verticalWeight = src.verticalWeight;
            this.horizontalWeight = src.horizontalWeight;
            this.horizontalChainStyle = src.horizontalChainStyle;
            this.verticalChainStyle = src.verticalChainStyle;
            this.widthDefault = src.widthDefault;
            this.heightDefault = src.heightDefault;
            this.widthMax = src.widthMax;
            this.heightMax = src.heightMax;
            this.widthMin = src.widthMin;
            this.heightMin = src.heightMin;
            this.widthPercent = src.widthPercent;
            this.heightPercent = src.heightPercent;
            this.mBarrierDirection = src.mBarrierDirection;
            this.mBarrierMargin = src.mBarrierMargin;
            this.mHelperType = src.mHelperType;
            this.mConstraintTag = src.mConstraintTag;
            int[] iArr = src.mReferenceIds;
            if (iArr != null) {
                this.mReferenceIds = Arrays.copyOf(iArr, iArr.length);
            } else {
                this.mReferenceIds = null;
            }
            this.mReferenceIdString = src.mReferenceIdString;
            this.constrainedWidth = src.constrainedWidth;
            this.constrainedHeight = src.constrainedHeight;
            this.mBarrierAllowsGoneWidgets = src.mBarrierAllowsGoneWidgets;
        }

        static {
            mapToConstant.append(R.styleable.Layout_layout_constraintLeft_toLeftOf, 24);
            mapToConstant.append(R.styleable.Layout_layout_constraintLeft_toRightOf, 25);
            mapToConstant.append(R.styleable.Layout_layout_constraintRight_toLeftOf, 28);
            mapToConstant.append(R.styleable.Layout_layout_constraintRight_toRightOf, 29);
            mapToConstant.append(R.styleable.Layout_layout_constraintTop_toTopOf, 35);
            mapToConstant.append(R.styleable.Layout_layout_constraintTop_toBottomOf, 34);
            mapToConstant.append(R.styleable.Layout_layout_constraintBottom_toTopOf, 4);
            mapToConstant.append(R.styleable.Layout_layout_constraintBottom_toBottomOf, 3);
            mapToConstant.append(R.styleable.Layout_layout_constraintBaseline_toBaselineOf, 1);
            mapToConstant.append(R.styleable.Layout_layout_editor_absoluteX, 6);
            mapToConstant.append(R.styleable.Layout_layout_editor_absoluteY, 7);
            mapToConstant.append(R.styleable.Layout_layout_constraintGuide_begin, 17);
            mapToConstant.append(R.styleable.Layout_layout_constraintGuide_end, 18);
            mapToConstant.append(R.styleable.Layout_layout_constraintGuide_percent, 19);
            mapToConstant.append(R.styleable.Layout_android_orientation, 26);
            mapToConstant.append(R.styleable.Layout_layout_constraintStart_toEndOf, 31);
            mapToConstant.append(R.styleable.Layout_layout_constraintStart_toStartOf, 32);
            mapToConstant.append(R.styleable.Layout_layout_constraintEnd_toStartOf, 10);
            mapToConstant.append(R.styleable.Layout_layout_constraintEnd_toEndOf, 9);
            mapToConstant.append(R.styleable.Layout_layout_goneMarginLeft, 13);
            mapToConstant.append(R.styleable.Layout_layout_goneMarginTop, 16);
            mapToConstant.append(R.styleable.Layout_layout_goneMarginRight, 14);
            mapToConstant.append(R.styleable.Layout_layout_goneMarginBottom, 11);
            mapToConstant.append(R.styleable.Layout_layout_goneMarginStart, 15);
            mapToConstant.append(R.styleable.Layout_layout_goneMarginEnd, 12);
            mapToConstant.append(R.styleable.Layout_layout_constraintVertical_weight, 38);
            mapToConstant.append(R.styleable.Layout_layout_constraintHorizontal_weight, 37);
            mapToConstant.append(R.styleable.Layout_layout_constraintHorizontal_chainStyle, 39);
            mapToConstant.append(R.styleable.Layout_layout_constraintVertical_chainStyle, 40);
            mapToConstant.append(R.styleable.Layout_layout_constraintHorizontal_bias, 20);
            mapToConstant.append(R.styleable.Layout_layout_constraintVertical_bias, 36);
            mapToConstant.append(R.styleable.Layout_layout_constraintDimensionRatio, 5);
            mapToConstant.append(R.styleable.Layout_layout_constraintLeft_creator, 76);
            mapToConstant.append(R.styleable.Layout_layout_constraintTop_creator, 76);
            mapToConstant.append(R.styleable.Layout_layout_constraintRight_creator, 76);
            mapToConstant.append(R.styleable.Layout_layout_constraintBottom_creator, 76);
            mapToConstant.append(R.styleable.Layout_layout_constraintBaseline_creator, 76);
            mapToConstant.append(R.styleable.Layout_android_layout_marginLeft, 23);
            mapToConstant.append(R.styleable.Layout_android_layout_marginRight, 27);
            mapToConstant.append(R.styleable.Layout_android_layout_marginStart, 30);
            mapToConstant.append(R.styleable.Layout_android_layout_marginEnd, 8);
            mapToConstant.append(R.styleable.Layout_android_layout_marginTop, 33);
            mapToConstant.append(R.styleable.Layout_android_layout_marginBottom, 2);
            mapToConstant.append(R.styleable.Layout_android_layout_width, 22);
            mapToConstant.append(R.styleable.Layout_android_layout_height, 21);
            mapToConstant.append(R.styleable.Layout_layout_constraintCircle, 61);
            mapToConstant.append(R.styleable.Layout_layout_constraintCircleRadius, 62);
            mapToConstant.append(R.styleable.Layout_layout_constraintCircleAngle, 63);
            mapToConstant.append(R.styleable.Layout_layout_constraintWidth_percent, 69);
            mapToConstant.append(R.styleable.Layout_layout_constraintHeight_percent, 70);
            mapToConstant.append(R.styleable.Layout_chainUseRtl, 71);
            mapToConstant.append(R.styleable.Layout_barrierDirection, 72);
            mapToConstant.append(R.styleable.Layout_barrierMargin, 73);
            mapToConstant.append(R.styleable.Layout_constraint_referenced_ids, 74);
            mapToConstant.append(R.styleable.Layout_barrierAllowsGoneWidgets, 75);
        }

        void fillFromAttributeList(Context context, AttributeSet attrs) {
            TypedArray a2 = context.obtainStyledAttributes(attrs, R.styleable.Layout);
            this.mApply = true;
            int N = a2.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a2.getIndex(i);
                int i2 = mapToConstant.get(attr);
                if (i2 == 80) {
                    this.constrainedWidth = a2.getBoolean(attr, this.constrainedWidth);
                } else if (i2 != 81) {
                    switch (i2) {
                        case 1:
                            this.baselineToBaseline = ConstraintSet.lookupID(a2, attr, this.baselineToBaseline);
                            continue;
                        case 2:
                            this.bottomMargin = a2.getDimensionPixelSize(attr, this.bottomMargin);
                            continue;
                        case 3:
                            this.bottomToBottom = ConstraintSet.lookupID(a2, attr, this.bottomToBottom);
                            continue;
                        case 4:
                            this.bottomToTop = ConstraintSet.lookupID(a2, attr, this.bottomToTop);
                            continue;
                        case 5:
                            this.dimensionRatio = a2.getString(attr);
                            continue;
                        case 6:
                            this.editorAbsoluteX = a2.getDimensionPixelOffset(attr, this.editorAbsoluteX);
                            continue;
                        case 7:
                            this.editorAbsoluteY = a2.getDimensionPixelOffset(attr, this.editorAbsoluteY);
                            continue;
                        case 8:
                            if (Build.VERSION.SDK_INT >= 17) {
                                this.endMargin = a2.getDimensionPixelSize(attr, this.endMargin);
                                break;
                            } else {
                                continue;
                            }
                        case 9:
                            this.endToEnd = ConstraintSet.lookupID(a2, attr, this.endToEnd);
                            continue;
                        case 10:
                            this.endToStart = ConstraintSet.lookupID(a2, attr, this.endToStart);
                            continue;
                        case 11:
                            this.goneBottomMargin = a2.getDimensionPixelSize(attr, this.goneBottomMargin);
                            continue;
                        case 12:
                            this.goneEndMargin = a2.getDimensionPixelSize(attr, this.goneEndMargin);
                            continue;
                        case 13:
                            this.goneLeftMargin = a2.getDimensionPixelSize(attr, this.goneLeftMargin);
                            continue;
                        case 14:
                            this.goneRightMargin = a2.getDimensionPixelSize(attr, this.goneRightMargin);
                            continue;
                        case 15:
                            this.goneStartMargin = a2.getDimensionPixelSize(attr, this.goneStartMargin);
                            continue;
                        case 16:
                            this.goneTopMargin = a2.getDimensionPixelSize(attr, this.goneTopMargin);
                            continue;
                        case 17:
                            this.guideBegin = a2.getDimensionPixelOffset(attr, this.guideBegin);
                            continue;
                        case 18:
                            this.guideEnd = a2.getDimensionPixelOffset(attr, this.guideEnd);
                            continue;
                        case 19:
                            this.guidePercent = a2.getFloat(attr, this.guidePercent);
                            continue;
                        case 20:
                            this.horizontalBias = a2.getFloat(attr, this.horizontalBias);
                            continue;
                        case 21:
                            this.mHeight = a2.getLayoutDimension(attr, this.mHeight);
                            continue;
                        case 22:
                            this.mWidth = a2.getLayoutDimension(attr, this.mWidth);
                            continue;
                        case 23:
                            this.leftMargin = a2.getDimensionPixelSize(attr, this.leftMargin);
                            continue;
                        case 24:
                            this.leftToLeft = ConstraintSet.lookupID(a2, attr, this.leftToLeft);
                            continue;
                        case 25:
                            this.leftToRight = ConstraintSet.lookupID(a2, attr, this.leftToRight);
                            continue;
                        case 26:
                            this.orientation = a2.getInt(attr, this.orientation);
                            continue;
                        case 27:
                            this.rightMargin = a2.getDimensionPixelSize(attr, this.rightMargin);
                            continue;
                        case 28:
                            this.rightToLeft = ConstraintSet.lookupID(a2, attr, this.rightToLeft);
                            continue;
                        case 29:
                            this.rightToRight = ConstraintSet.lookupID(a2, attr, this.rightToRight);
                            continue;
                        case 30:
                            if (Build.VERSION.SDK_INT >= 17) {
                                this.startMargin = a2.getDimensionPixelSize(attr, this.startMargin);
                                break;
                            } else {
                                continue;
                            }
                        case 31:
                            this.startToEnd = ConstraintSet.lookupID(a2, attr, this.startToEnd);
                            continue;
                        case 32:
                            this.startToStart = ConstraintSet.lookupID(a2, attr, this.startToStart);
                            continue;
                        case 33:
                            this.topMargin = a2.getDimensionPixelSize(attr, this.topMargin);
                            continue;
                        case 34:
                            this.topToBottom = ConstraintSet.lookupID(a2, attr, this.topToBottom);
                            continue;
                        case 35:
                            this.topToTop = ConstraintSet.lookupID(a2, attr, this.topToTop);
                            continue;
                        case 36:
                            this.verticalBias = a2.getFloat(attr, this.verticalBias);
                            continue;
                        case 37:
                            this.horizontalWeight = a2.getFloat(attr, this.horizontalWeight);
                            continue;
                        case 38:
                            this.verticalWeight = a2.getFloat(attr, this.verticalWeight);
                            continue;
                        case 39:
                            this.horizontalChainStyle = a2.getInt(attr, this.horizontalChainStyle);
                            continue;
                        case 40:
                            this.verticalChainStyle = a2.getInt(attr, this.verticalChainStyle);
                            continue;
                        default:
                            switch (i2) {
                                case 54:
                                    this.widthDefault = a2.getInt(attr, this.widthDefault);
                                    continue;
                                case 55:
                                    this.heightDefault = a2.getInt(attr, this.heightDefault);
                                    continue;
                                case 56:
                                    this.widthMax = a2.getDimensionPixelSize(attr, this.widthMax);
                                    continue;
                                case 57:
                                    this.heightMax = a2.getDimensionPixelSize(attr, this.heightMax);
                                    continue;
                                case 58:
                                    this.widthMin = a2.getDimensionPixelSize(attr, this.widthMin);
                                    continue;
                                case 59:
                                    this.heightMin = a2.getDimensionPixelSize(attr, this.heightMin);
                                    continue;
                                default:
                                    switch (i2) {
                                        case 61:
                                            this.circleConstraint = ConstraintSet.lookupID(a2, attr, this.circleConstraint);
                                            continue;
                                        case 62:
                                            this.circleRadius = a2.getDimensionPixelSize(attr, this.circleRadius);
                                            continue;
                                        case 63:
                                            this.circleAngle = a2.getFloat(attr, this.circleAngle);
                                            continue;
                                        default:
                                            switch (i2) {
                                                case 69:
                                                    this.widthPercent = a2.getFloat(attr, 1.0f);
                                                    continue;
                                                case 70:
                                                    this.heightPercent = a2.getFloat(attr, 1.0f);
                                                    continue;
                                                case 71:
                                                    Log.e(ConstraintSet.TAG, "CURRENTLY UNSUPPORTED");
                                                    continue;
                                                case 72:
                                                    this.mBarrierDirection = a2.getInt(attr, this.mBarrierDirection);
                                                    continue;
                                                case 73:
                                                    this.mBarrierMargin = a2.getDimensionPixelSize(attr, this.mBarrierMargin);
                                                    continue;
                                                case 74:
                                                    this.mReferenceIdString = a2.getString(attr);
                                                    continue;
                                                case 75:
                                                    this.mBarrierAllowsGoneWidgets = a2.getBoolean(attr, this.mBarrierAllowsGoneWidgets);
                                                    continue;
                                                case 76:
                                                    Log.w(ConstraintSet.TAG, "unused attribute 0x" + Integer.toHexString(attr) + "   " + mapToConstant.get(attr));
                                                    continue;
                                                case 77:
                                                    this.mConstraintTag = a2.getString(attr);
                                                    continue;
                                                default:
                                                    Log.w(ConstraintSet.TAG, "Unknown attribute 0x" + Integer.toHexString(attr) + "   " + mapToConstant.get(attr));
                                                    continue;
                                                    continue;
                                                    continue;
                                                    continue;
                                            }
                                    }
                            }
                    }
                } else {
                    this.constrainedHeight = a2.getBoolean(attr, this.constrainedHeight);
                }
            }
            a2.recycle();
        }

        public void dump(MotionScene scene, StringBuilder stringBuilder) {
            Field[] fields = getClass().getDeclaredFields();
            stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
            for (Field field : fields) {
                String name = field.getName();
                if (!Modifier.isStatic(field.getModifiers())) {
                    try {
                        Object value = field.get(this);
                        Class<?> type = field.getType();
                        if (type == Integer.TYPE) {
                            Integer iValue = (Integer) value;
                            if (iValue.intValue() != -1) {
                                String stringid = scene.lookUpConstraintName(iValue.intValue());
                                stringBuilder.append("    ");
                                stringBuilder.append(name);
                                stringBuilder.append(" = \"");
                                stringBuilder.append(stringid == null ? iValue : stringid);
                                stringBuilder.append("\"\n");
                            }
                        } else if (type == Float.TYPE) {
                            Float fValue = (Float) value;
                            if (fValue.floatValue() != -1.0f) {
                                stringBuilder.append("    ");
                                stringBuilder.append(name);
                                stringBuilder.append(" = \"");
                                stringBuilder.append(fValue);
                                stringBuilder.append("\"\n");
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class Transform {
        private static final int ELEVATION;
        private static final int ROTATION;
        private static final int ROTATION_X;
        private static final int ROTATION_Y;
        private static final int SCALE_X;
        private static final int SCALE_Y;
        private static final int TRANSFORM_PIVOT_X;
        private static final int TRANSFORM_PIVOT_Y;
        private static final int TRANSLATION_X;
        private static final int TRANSLATION_Y;
        private static final int TRANSLATION_Z;
        private static SparseIntArray mapToConstant = new SparseIntArray();
        public boolean mApply = false;
        public float rotation = 0.0f;
        public float rotationX = 0.0f;
        public float rotationY = 0.0f;
        public float scaleX = 1.0f;
        public float scaleY = 1.0f;
        public float transformPivotX = Float.NaN;
        public float transformPivotY = Float.NaN;
        public float translationX = 0.0f;
        public float translationY = 0.0f;
        public float translationZ = 0.0f;
        public boolean applyElevation = false;
        public float elevation = 0.0f;

        public void copyFrom(Transform src) {
            this.mApply = src.mApply;
            this.rotation = src.rotation;
            this.rotationX = src.rotationX;
            this.rotationY = src.rotationY;
            this.scaleX = src.scaleX;
            this.scaleY = src.scaleY;
            this.transformPivotX = src.transformPivotX;
            this.transformPivotY = src.transformPivotY;
            this.translationX = src.translationX;
            this.translationY = src.translationY;
            this.translationZ = src.translationZ;
            this.applyElevation = src.applyElevation;
            this.elevation = src.elevation;
        }

        static {
            mapToConstant.append(R.styleable.Transform_android_rotation, 1);
            mapToConstant.append(R.styleable.Transform_android_rotationX, 2);
            mapToConstant.append(R.styleable.Transform_android_rotationY, 3);
            mapToConstant.append(R.styleable.Transform_android_scaleX, 4);
            mapToConstant.append(R.styleable.Transform_android_scaleY, 5);
            mapToConstant.append(R.styleable.Transform_android_transformPivotX, 6);
            mapToConstant.append(R.styleable.Transform_android_transformPivotY, 7);
            mapToConstant.append(R.styleable.Transform_android_translationX, 8);
            mapToConstant.append(R.styleable.Transform_android_translationY, 9);
            mapToConstant.append(R.styleable.Transform_android_translationZ, 10);
            mapToConstant.append(R.styleable.Transform_android_elevation, 11);
        }

        void fillFromAttributeList(Context context, AttributeSet attrs) {
            TypedArray a2 = context.obtainStyledAttributes(attrs, R.styleable.Transform);
            this.mApply = true;
            int N = a2.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a2.getIndex(i);
                switch (mapToConstant.get(attr)) {
                    case 1:
                        this.rotation = a2.getFloat(attr, this.rotation);
                        break;
                    case 2:
                        this.rotationX = a2.getFloat(attr, this.rotationX);
                        break;
                    case 3:
                        this.rotationY = a2.getFloat(attr, this.rotationY);
                        break;
                    case 4:
                        this.scaleX = a2.getFloat(attr, this.scaleX);
                        break;
                    case 5:
                        this.scaleY = a2.getFloat(attr, this.scaleY);
                        break;
                    case 6:
                        this.transformPivotX = a2.getDimension(attr, this.transformPivotX);
                        break;
                    case 7:
                        this.transformPivotY = a2.getDimension(attr, this.transformPivotY);
                        break;
                    case 8:
                        this.translationX = a2.getDimension(attr, this.translationX);
                        break;
                    case 9:
                        this.translationY = a2.getDimension(attr, this.translationY);
                        break;
                    case 10:
                        if (Build.VERSION.SDK_INT >= 21) {
                            this.translationZ = a2.getDimension(attr, this.translationZ);
                            break;
                        } else {
                            break;
                        }
                    case 11:
                        if (Build.VERSION.SDK_INT >= 21) {
                            this.applyElevation = true;
                            this.elevation = a2.getDimension(attr, this.elevation);
                            break;
                        } else {
                            break;
                        }
                }
            }
            a2.recycle();
        }
    }

    /* loaded from: classes.dex */
    public static class PropertySet {
        public boolean mApply = false;
        public int visibility = 0;
        public int mVisibilityMode = 0;
        public float alpha = 1.0f;
        public float mProgress = Float.NaN;

        public void copyFrom(PropertySet src) {
            this.mApply = src.mApply;
            this.visibility = src.visibility;
            this.alpha = src.alpha;
            this.mProgress = src.mProgress;
            this.mVisibilityMode = src.mVisibilityMode;
        }

        void fillFromAttributeList(Context context, AttributeSet attrs) {
            TypedArray a2 = context.obtainStyledAttributes(attrs, R.styleable.PropertySet);
            this.mApply = true;
            int N = a2.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a2.getIndex(i);
                if (attr == R.styleable.PropertySet_android_alpha) {
                    this.alpha = a2.getFloat(attr, this.alpha);
                } else if (attr == R.styleable.PropertySet_android_visibility) {
                    this.visibility = a2.getInt(attr, this.visibility);
                    this.visibility = ConstraintSet.VISIBILITY_FLAGS[this.visibility];
                } else if (attr == R.styleable.PropertySet_visibilityMode) {
                    this.mVisibilityMode = a2.getInt(attr, this.mVisibilityMode);
                } else if (attr == R.styleable.PropertySet_motionProgress) {
                    this.mProgress = a2.getFloat(attr, this.mProgress);
                }
            }
            a2.recycle();
        }
    }

    /* loaded from: classes.dex */
    public static class Motion {
        private static final int ANIMATE_RELATIVE_TO;
        private static final int MOTION_DRAW_PATH;
        private static final int MOTION_STAGGER;
        private static final int PATH_MOTION_ARC;
        private static final int TRANSITION_EASING;
        private static final int TRANSITION_PATH_ROTATE;
        private static SparseIntArray mapToConstant = new SparseIntArray();
        public boolean mApply = false;
        public int mAnimateRelativeTo = -1;
        public String mTransitionEasing = null;
        public int mPathMotionArc = -1;
        public int mDrawPath = 0;
        public float mMotionStagger = Float.NaN;
        public float mPathRotate = Float.NaN;

        public void copyFrom(Motion src) {
            this.mApply = src.mApply;
            this.mAnimateRelativeTo = src.mAnimateRelativeTo;
            this.mTransitionEasing = src.mTransitionEasing;
            this.mPathMotionArc = src.mPathMotionArc;
            this.mDrawPath = src.mDrawPath;
            this.mPathRotate = src.mPathRotate;
            this.mMotionStagger = src.mMotionStagger;
        }

        static {
            mapToConstant.append(R.styleable.Motion_motionPathRotate, 1);
            mapToConstant.append(R.styleable.Motion_pathMotionArc, 2);
            mapToConstant.append(R.styleable.Motion_transitionEasing, 3);
            mapToConstant.append(R.styleable.Motion_drawPath, 4);
            mapToConstant.append(R.styleable.Motion_animate_relativeTo, 5);
            mapToConstant.append(R.styleable.Motion_motionStagger, 6);
        }

        void fillFromAttributeList(Context context, AttributeSet attrs) {
            TypedArray a2 = context.obtainStyledAttributes(attrs, R.styleable.Motion);
            this.mApply = true;
            int N = a2.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a2.getIndex(i);
                switch (mapToConstant.get(attr)) {
                    case 1:
                        this.mPathRotate = a2.getFloat(attr, this.mPathRotate);
                        break;
                    case 2:
                        this.mPathMotionArc = a2.getInt(attr, this.mPathMotionArc);
                        break;
                    case 3:
                        if (a2.peekValue(attr).type == 3) {
                            this.mTransitionEasing = a2.getString(attr);
                            break;
                        } else {
                            this.mTransitionEasing = Easing.NAMED_EASING[a2.getInteger(attr, 0)];
                            break;
                        }
                    case 4:
                        this.mDrawPath = a2.getInt(attr, 0);
                        break;
                    case 5:
                        this.mAnimateRelativeTo = ConstraintSet.lookupID(a2, attr, this.mAnimateRelativeTo);
                        break;
                    case 6:
                        this.mMotionStagger = a2.getFloat(attr, this.mMotionStagger);
                        break;
                }
            }
            a2.recycle();
        }
    }

    /* loaded from: classes.dex */
    public static class Constraint {
        int mViewId;
        public final PropertySet propertySet = new PropertySet();
        public final Motion motion = new Motion();
        public final Layout layout = new Layout();
        public final Transform transform = new Transform();
        public HashMap<String, ConstraintAttribute> mCustomConstraints = new HashMap<>();

        private ConstraintAttribute get(String attributeName, ConstraintAttribute.AttributeType attributeType) {
            if (this.mCustomConstraints.containsKey(attributeName)) {
                ConstraintAttribute ret = this.mCustomConstraints.get(attributeName);
                if (ret.getType() == attributeType) {
                    return ret;
                }
                throw new IllegalArgumentException("ConstraintAttribute is already a " + ret.getType().name());
            }
            ConstraintAttribute ret2 = new ConstraintAttribute(attributeName, attributeType);
            this.mCustomConstraints.put(attributeName, ret2);
            return ret2;
        }

        public void setStringValue(String attributeName, String value) {
            get(attributeName, ConstraintAttribute.AttributeType.STRING_TYPE).setStringValue(value);
        }

        public void setFloatValue(String attributeName, float value) {
            get(attributeName, ConstraintAttribute.AttributeType.FLOAT_TYPE).setFloatValue(value);
        }

        public void setIntValue(String attributeName, int value) {
            get(attributeName, ConstraintAttribute.AttributeType.INT_TYPE).setIntValue(value);
        }

        public void setColorValue(String attributeName, int value) {
            get(attributeName, ConstraintAttribute.AttributeType.COLOR_TYPE).setColorValue(value);
        }

        public Constraint clone() {
            Constraint clone = new Constraint();
            clone.layout.copyFrom(this.layout);
            clone.motion.copyFrom(this.motion);
            clone.propertySet.copyFrom(this.propertySet);
            clone.transform.copyFrom(this.transform);
            clone.mViewId = this.mViewId;
            return clone;
        }

        public void fillFromConstraints(ConstraintHelper helper, int viewId, Constraints.LayoutParams param) {
            fillFromConstraints(viewId, param);
            if (helper instanceof Barrier) {
                Layout layout = this.layout;
                layout.mHelperType = 1;
                Barrier barrier = (Barrier) helper;
                layout.mBarrierDirection = barrier.getType();
                this.layout.mReferenceIds = barrier.getReferencedIds();
                this.layout.mBarrierMargin = barrier.getMargin();
            }
        }

        public void fillFromConstraints(int viewId, Constraints.LayoutParams param) {
            fillFrom(viewId, param);
            this.propertySet.alpha = param.alpha;
            this.transform.rotation = param.rotation;
            this.transform.rotationX = param.rotationX;
            this.transform.rotationY = param.rotationY;
            this.transform.scaleX = param.scaleX;
            this.transform.scaleY = param.scaleY;
            this.transform.transformPivotX = param.transformPivotX;
            this.transform.transformPivotY = param.transformPivotY;
            this.transform.translationX = param.translationX;
            this.transform.translationY = param.translationY;
            this.transform.translationZ = param.translationZ;
            this.transform.elevation = param.elevation;
            this.transform.applyElevation = param.applyElevation;
        }

        public void fillFrom(int viewId, ConstraintLayout.LayoutParams param) {
            this.mViewId = viewId;
            this.layout.leftToLeft = param.leftToLeft;
            this.layout.leftToRight = param.leftToRight;
            this.layout.rightToLeft = param.rightToLeft;
            this.layout.rightToRight = param.rightToRight;
            this.layout.topToTop = param.topToTop;
            this.layout.topToBottom = param.topToBottom;
            this.layout.bottomToTop = param.bottomToTop;
            this.layout.bottomToBottom = param.bottomToBottom;
            this.layout.baselineToBaseline = param.baselineToBaseline;
            this.layout.startToEnd = param.startToEnd;
            this.layout.startToStart = param.startToStart;
            this.layout.endToStart = param.endToStart;
            this.layout.endToEnd = param.endToEnd;
            this.layout.horizontalBias = param.horizontalBias;
            this.layout.verticalBias = param.verticalBias;
            this.layout.dimensionRatio = param.dimensionRatio;
            this.layout.circleConstraint = param.circleConstraint;
            this.layout.circleRadius = param.circleRadius;
            this.layout.circleAngle = param.circleAngle;
            this.layout.editorAbsoluteX = param.editorAbsoluteX;
            this.layout.editorAbsoluteY = param.editorAbsoluteY;
            this.layout.orientation = param.orientation;
            this.layout.guidePercent = param.guidePercent;
            this.layout.guideBegin = param.guideBegin;
            this.layout.guideEnd = param.guideEnd;
            this.layout.mWidth = param.width;
            this.layout.mHeight = param.height;
            this.layout.leftMargin = param.leftMargin;
            this.layout.rightMargin = param.rightMargin;
            this.layout.topMargin = param.topMargin;
            this.layout.bottomMargin = param.bottomMargin;
            this.layout.verticalWeight = param.verticalWeight;
            this.layout.horizontalWeight = param.horizontalWeight;
            this.layout.verticalChainStyle = param.verticalChainStyle;
            this.layout.horizontalChainStyle = param.horizontalChainStyle;
            this.layout.constrainedWidth = param.constrainedWidth;
            this.layout.constrainedHeight = param.constrainedHeight;
            this.layout.widthDefault = param.matchConstraintDefaultWidth;
            this.layout.heightDefault = param.matchConstraintDefaultHeight;
            this.layout.widthMax = param.matchConstraintMaxWidth;
            this.layout.heightMax = param.matchConstraintMaxHeight;
            this.layout.widthMin = param.matchConstraintMinWidth;
            this.layout.heightMin = param.matchConstraintMinHeight;
            this.layout.widthPercent = param.matchConstraintPercentWidth;
            this.layout.heightPercent = param.matchConstraintPercentHeight;
            this.layout.mConstraintTag = param.constraintTag;
            this.layout.goneTopMargin = param.goneTopMargin;
            this.layout.goneBottomMargin = param.goneBottomMargin;
            this.layout.goneLeftMargin = param.goneLeftMargin;
            this.layout.goneRightMargin = param.goneRightMargin;
            this.layout.goneStartMargin = param.goneStartMargin;
            this.layout.goneEndMargin = param.goneEndMargin;
            if (Build.VERSION.SDK_INT >= 17) {
                this.layout.endMargin = param.getMarginEnd();
                this.layout.startMargin = param.getMarginStart();
            }
        }

        public void applyTo(ConstraintLayout.LayoutParams param) {
            param.leftToLeft = this.layout.leftToLeft;
            param.leftToRight = this.layout.leftToRight;
            param.rightToLeft = this.layout.rightToLeft;
            param.rightToRight = this.layout.rightToRight;
            param.topToTop = this.layout.topToTop;
            param.topToBottom = this.layout.topToBottom;
            param.bottomToTop = this.layout.bottomToTop;
            param.bottomToBottom = this.layout.bottomToBottom;
            param.baselineToBaseline = this.layout.baselineToBaseline;
            param.startToEnd = this.layout.startToEnd;
            param.startToStart = this.layout.startToStart;
            param.endToStart = this.layout.endToStart;
            param.endToEnd = this.layout.endToEnd;
            param.leftMargin = this.layout.leftMargin;
            param.rightMargin = this.layout.rightMargin;
            param.topMargin = this.layout.topMargin;
            param.bottomMargin = this.layout.bottomMargin;
            param.goneStartMargin = this.layout.goneStartMargin;
            param.goneEndMargin = this.layout.goneEndMargin;
            param.goneTopMargin = this.layout.goneTopMargin;
            param.goneBottomMargin = this.layout.goneBottomMargin;
            param.horizontalBias = this.layout.horizontalBias;
            param.verticalBias = this.layout.verticalBias;
            param.circleConstraint = this.layout.circleConstraint;
            param.circleRadius = this.layout.circleRadius;
            param.circleAngle = this.layout.circleAngle;
            param.dimensionRatio = this.layout.dimensionRatio;
            param.editorAbsoluteX = this.layout.editorAbsoluteX;
            param.editorAbsoluteY = this.layout.editorAbsoluteY;
            param.verticalWeight = this.layout.verticalWeight;
            param.horizontalWeight = this.layout.horizontalWeight;
            param.verticalChainStyle = this.layout.verticalChainStyle;
            param.horizontalChainStyle = this.layout.horizontalChainStyle;
            param.constrainedWidth = this.layout.constrainedWidth;
            param.constrainedHeight = this.layout.constrainedHeight;
            param.matchConstraintDefaultWidth = this.layout.widthDefault;
            param.matchConstraintDefaultHeight = this.layout.heightDefault;
            param.matchConstraintMaxWidth = this.layout.widthMax;
            param.matchConstraintMaxHeight = this.layout.heightMax;
            param.matchConstraintMinWidth = this.layout.widthMin;
            param.matchConstraintMinHeight = this.layout.heightMin;
            param.matchConstraintPercentWidth = this.layout.widthPercent;
            param.matchConstraintPercentHeight = this.layout.heightPercent;
            param.orientation = this.layout.orientation;
            param.guidePercent = this.layout.guidePercent;
            param.guideBegin = this.layout.guideBegin;
            param.guideEnd = this.layout.guideEnd;
            param.width = this.layout.mWidth;
            param.height = this.layout.mHeight;
            if (this.layout.mConstraintTag != null) {
                param.constraintTag = this.layout.mConstraintTag;
            }
            if (Build.VERSION.SDK_INT >= 17) {
                param.setMarginStart(this.layout.startMargin);
                param.setMarginEnd(this.layout.endMargin);
            }
            param.validate();
        }
    }

    public void clone(Context context, int constraintLayoutId) {
        clone((ConstraintLayout) LayoutInflater.from(context).inflate(constraintLayoutId, (ViewGroup) null));
    }

    public void clone(ConstraintSet set) {
        this.mConstraints.clear();
        for (Integer key : set.mConstraints.keySet()) {
            this.mConstraints.put(key, set.mConstraints.get(key).clone());
        }
    }

    public void clone(ConstraintLayout constraintLayout) {
        int count = constraintLayout.getChildCount();
        this.mConstraints.clear();
        for (int i = 0; i < count; i++) {
            View view = constraintLayout.getChildAt(i);
            ConstraintLayout.LayoutParams param = (ConstraintLayout.LayoutParams) view.getLayoutParams();
            int id = view.getId();
            if (!this.mForceId || id != -1) {
                if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
                    this.mConstraints.put(Integer.valueOf(id), new Constraint());
                }
                Constraint constraint = this.mConstraints.get(Integer.valueOf(id));
                constraint.mCustomConstraints = ConstraintAttribute.extractAttributes(this.mSavedAttributes, view);
                constraint.fillFrom(id, param);
                constraint.propertySet.visibility = view.getVisibility();
                if (Build.VERSION.SDK_INT >= 17) {
                    constraint.propertySet.alpha = view.getAlpha();
                    constraint.transform.rotation = view.getRotation();
                    constraint.transform.rotationX = view.getRotationX();
                    constraint.transform.rotationY = view.getRotationY();
                    constraint.transform.scaleX = view.getScaleX();
                    constraint.transform.scaleY = view.getScaleY();
                    float pivotX = view.getPivotX();
                    float pivotY = view.getPivotY();
                    if (!(((double) pivotX) == 0.0d && ((double) pivotY) == 0.0d)) {
                        constraint.transform.transformPivotX = pivotX;
                        constraint.transform.transformPivotY = pivotY;
                    }
                    constraint.transform.translationX = view.getTranslationX();
                    constraint.transform.translationY = view.getTranslationY();
                    if (Build.VERSION.SDK_INT >= 21) {
                        constraint.transform.translationZ = view.getTranslationZ();
                        if (constraint.transform.applyElevation) {
                            constraint.transform.elevation = view.getElevation();
                        }
                    }
                }
                if (view instanceof Barrier) {
                    Barrier barrier = (Barrier) view;
                    constraint.layout.mBarrierAllowsGoneWidgets = barrier.allowsGoneWidget();
                    constraint.layout.mReferenceIds = barrier.getReferencedIds();
                    constraint.layout.mBarrierDirection = barrier.getType();
                    constraint.layout.mBarrierMargin = barrier.getMargin();
                }
            } else {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
        }
    }

    public void clone(Constraints constraints) {
        int count = constraints.getChildCount();
        this.mConstraints.clear();
        for (int i = 0; i < count; i++) {
            View view = constraints.getChildAt(i);
            Constraints.LayoutParams param = (Constraints.LayoutParams) view.getLayoutParams();
            int id = view.getId();
            if (!this.mForceId || id != -1) {
                if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
                    this.mConstraints.put(Integer.valueOf(id), new Constraint());
                }
                Constraint constraint = this.mConstraints.get(Integer.valueOf(id));
                if (view instanceof ConstraintHelper) {
                    constraint.fillFromConstraints((ConstraintHelper) view, id, param);
                }
                constraint.fillFromConstraints(id, param);
            } else {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
        }
    }

    public void applyTo(ConstraintLayout constraintLayout) {
        applyToInternal(constraintLayout, true);
        constraintLayout.setConstraintSet(null);
        constraintLayout.requestLayout();
    }

    public void applyToWithoutCustom(ConstraintLayout constraintLayout) {
        applyToInternal(constraintLayout, false);
        constraintLayout.setConstraintSet(null);
    }

    public void applyCustomAttributes(ConstraintLayout constraintLayout) {
        int count = constraintLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = constraintLayout.getChildAt(i);
            int id = view.getId();
            if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
                Log.v(TAG, "id unknown " + Debug.getName(view));
            } else if (this.mForceId && id == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            } else if (this.mConstraints.containsKey(Integer.valueOf(id))) {
                ConstraintAttribute.setAttributes(view, this.mConstraints.get(Integer.valueOf(id)).mCustomConstraints);
            }
        }
    }

    public void applyToHelper(ConstraintHelper helper, ConstraintWidget child, ConstraintLayout.LayoutParams layoutParams, SparseArray<ConstraintWidget> mapIdToWidget) {
        int id = helper.getId();
        if (this.mConstraints.containsKey(Integer.valueOf(id))) {
            Constraint constraint = this.mConstraints.get(Integer.valueOf(id));
            if (child instanceof HelperWidget) {
                helper.loadParameters(constraint, (HelperWidget) child, layoutParams, mapIdToWidget);
            }
        }
    }

    public void applyToLayoutParams(int id, ConstraintLayout.LayoutParams layoutParams) {
        if (this.mConstraints.containsKey(Integer.valueOf(id))) {
            this.mConstraints.get(Integer.valueOf(id)).applyTo(layoutParams);
        }
    }

    public void applyToInternal(ConstraintLayout constraintLayout, boolean applyPostLayout) {
        int count = constraintLayout.getChildCount();
        HashSet<Integer> used = new HashSet<>(this.mConstraints.keySet());
        for (int i = 0; i < count; i++) {
            View view = constraintLayout.getChildAt(i);
            int id = view.getId();
            if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
                Log.w(TAG, "id unknown " + Debug.getName(view));
            } else if (this.mForceId && id == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            } else if (id != -1) {
                if (this.mConstraints.containsKey(Integer.valueOf(id))) {
                    used.remove(Integer.valueOf(id));
                    Constraint constraint = this.mConstraints.get(Integer.valueOf(id));
                    if (view instanceof Barrier) {
                        constraint.layout.mHelperType = 1;
                    }
                    if (constraint.layout.mHelperType != -1 && constraint.layout.mHelperType == 1) {
                        Barrier barrier = (Barrier) view;
                        barrier.setId(id);
                        barrier.setType(constraint.layout.mBarrierDirection);
                        barrier.setMargin(constraint.layout.mBarrierMargin);
                        barrier.setAllowsGoneWidget(constraint.layout.mBarrierAllowsGoneWidgets);
                        if (constraint.layout.mReferenceIds != null) {
                            barrier.setReferencedIds(constraint.layout.mReferenceIds);
                        } else if (constraint.layout.mReferenceIdString != null) {
                            constraint.layout.mReferenceIds = convertReferenceString(barrier, constraint.layout.mReferenceIdString);
                            barrier.setReferencedIds(constraint.layout.mReferenceIds);
                        }
                    }
                    ConstraintLayout.LayoutParams param = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                    param.validate();
                    constraint.applyTo(param);
                    if (applyPostLayout) {
                        ConstraintAttribute.setAttributes(view, constraint.mCustomConstraints);
                    }
                    view.setLayoutParams(param);
                    if (constraint.propertySet.mVisibilityMode == 0) {
                        view.setVisibility(constraint.propertySet.visibility);
                    }
                    if (Build.VERSION.SDK_INT >= 17) {
                        view.setAlpha(constraint.propertySet.alpha);
                        view.setRotation(constraint.transform.rotation);
                        view.setRotationX(constraint.transform.rotationX);
                        view.setRotationY(constraint.transform.rotationY);
                        view.setScaleX(constraint.transform.scaleX);
                        view.setScaleY(constraint.transform.scaleY);
                        if (!Float.isNaN(constraint.transform.transformPivotX)) {
                            view.setPivotX(constraint.transform.transformPivotX);
                        }
                        if (!Float.isNaN(constraint.transform.transformPivotY)) {
                            view.setPivotY(constraint.transform.transformPivotY);
                        }
                        view.setTranslationX(constraint.transform.translationX);
                        view.setTranslationY(constraint.transform.translationY);
                        if (Build.VERSION.SDK_INT >= 21) {
                            view.setTranslationZ(constraint.transform.translationZ);
                            if (constraint.transform.applyElevation) {
                                view.setElevation(constraint.transform.elevation);
                            }
                        }
                    }
                } else {
                    Log.v(TAG, "WARNING NO CONSTRAINTS for view " + id);
                }
            }
        }
        Iterator<Integer> it = used.iterator();
        while (it.hasNext()) {
            Integer id2 = it.next();
            Constraint constraint2 = this.mConstraints.get(id2);
            if (constraint2.layout.mHelperType != -1 && constraint2.layout.mHelperType == 1) {
                Barrier barrier2 = new Barrier(constraintLayout.getContext());
                barrier2.setId(id2.intValue());
                if (constraint2.layout.mReferenceIds != null) {
                    barrier2.setReferencedIds(constraint2.layout.mReferenceIds);
                } else if (constraint2.layout.mReferenceIdString != null) {
                    constraint2.layout.mReferenceIds = convertReferenceString(barrier2, constraint2.layout.mReferenceIdString);
                    barrier2.setReferencedIds(constraint2.layout.mReferenceIds);
                }
                barrier2.setType(constraint2.layout.mBarrierDirection);
                barrier2.setMargin(constraint2.layout.mBarrierMargin);
                ConstraintLayout.LayoutParams param2 = constraintLayout.generateDefaultLayoutParams();
                barrier2.validateParams();
                constraint2.applyTo(param2);
                constraintLayout.addView(barrier2, param2);
            }
            if (constraint2.layout.mIsGuideline) {
                Guideline g = new Guideline(constraintLayout.getContext());
                g.setId(id2.intValue());
                ConstraintLayout.LayoutParams param3 = constraintLayout.generateDefaultLayoutParams();
                constraint2.applyTo(param3);
                constraintLayout.addView(g, param3);
            }
        }
    }

    public void center(int centerID, int firstID, int firstSide, int firstMargin, int secondId, int secondSide, int secondMargin, float bias) {
        if (firstMargin < 0) {
            throw new IllegalArgumentException("margin must be > 0");
        } else if (secondMargin < 0) {
            throw new IllegalArgumentException("margin must be > 0");
        } else if (bias <= 0.0f || bias > 1.0f) {
            throw new IllegalArgumentException("bias must be between 0 and 1 inclusive");
        } else if (firstSide == 1 || firstSide == 2) {
            connect(centerID, 1, firstID, firstSide, firstMargin);
            connect(centerID, 2, secondId, secondSide, secondMargin);
            this.mConstraints.get(Integer.valueOf(centerID)).layout.horizontalBias = bias;
        } else if (firstSide == 6 || firstSide == 7) {
            connect(centerID, 6, firstID, firstSide, firstMargin);
            connect(centerID, 7, secondId, secondSide, secondMargin);
            this.mConstraints.get(Integer.valueOf(centerID)).layout.horizontalBias = bias;
        } else {
            connect(centerID, 3, firstID, firstSide, firstMargin);
            connect(centerID, 4, secondId, secondSide, secondMargin);
            this.mConstraints.get(Integer.valueOf(centerID)).layout.verticalBias = bias;
        }
    }

    public void centerHorizontally(int centerID, int leftId, int leftSide, int leftMargin, int rightId, int rightSide, int rightMargin, float bias) {
        connect(centerID, 1, leftId, leftSide, leftMargin);
        connect(centerID, 2, rightId, rightSide, rightMargin);
        this.mConstraints.get(Integer.valueOf(centerID)).layout.horizontalBias = bias;
    }

    public void centerHorizontallyRtl(int centerID, int startId, int startSide, int startMargin, int endId, int endSide, int endMargin, float bias) {
        connect(centerID, 6, startId, startSide, startMargin);
        connect(centerID, 7, endId, endSide, endMargin);
        this.mConstraints.get(Integer.valueOf(centerID)).layout.horizontalBias = bias;
    }

    public void centerVertically(int centerID, int topId, int topSide, int topMargin, int bottomId, int bottomSide, int bottomMargin, float bias) {
        connect(centerID, 3, topId, topSide, topMargin);
        connect(centerID, 4, bottomId, bottomSide, bottomMargin);
        this.mConstraints.get(Integer.valueOf(centerID)).layout.verticalBias = bias;
    }

    public void createVerticalChain(int topId, int topSide, int bottomId, int bottomSide, int[] chainIds, float[] weights, int style) {
        if (chainIds.length < 2) {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        } else if (weights == null || weights.length == chainIds.length) {
            if (weights != null) {
                get(chainIds[0]).layout.verticalWeight = weights[0];
            }
            get(chainIds[0]).layout.verticalChainStyle = style;
            connect(chainIds[0], 3, topId, topSide, 0);
            for (int i = 1; i < chainIds.length; i++) {
                int i2 = chainIds[i];
                connect(chainIds[i], 3, chainIds[i - 1], 4, 0);
                connect(chainIds[i - 1], 4, chainIds[i], 3, 0);
                if (weights != null) {
                    get(chainIds[i]).layout.verticalWeight = weights[i];
                }
            }
            connect(chainIds[chainIds.length - 1], 4, bottomId, bottomSide, 0);
        } else {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        }
    }

    public void createHorizontalChain(int leftId, int leftSide, int rightId, int rightSide, int[] chainIds, float[] weights, int style) {
        createHorizontalChain(leftId, leftSide, rightId, rightSide, chainIds, weights, style, 1, 2);
    }

    public void createHorizontalChainRtl(int startId, int startSide, int endId, int endSide, int[] chainIds, float[] weights, int style) {
        createHorizontalChain(startId, startSide, endId, endSide, chainIds, weights, style, 6, 7);
    }

    private void createHorizontalChain(int leftId, int leftSide, int rightId, int rightSide, int[] chainIds, float[] weights, int style, int left, int right) {
        if (chainIds.length < 2) {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        } else if (weights == null || weights.length == chainIds.length) {
            if (weights != null) {
                get(chainIds[0]).layout.horizontalWeight = weights[0];
            }
            get(chainIds[0]).layout.horizontalChainStyle = style;
            connect(chainIds[0], left, leftId, leftSide, -1);
            for (int i = 1; i < chainIds.length; i++) {
                int i2 = chainIds[i];
                connect(chainIds[i], left, chainIds[i - 1], right, -1);
                connect(chainIds[i - 1], right, chainIds[i], left, -1);
                if (weights != null) {
                    get(chainIds[i]).layout.horizontalWeight = weights[i];
                }
            }
            connect(chainIds[chainIds.length - 1], right, rightId, rightSide, -1);
        } else {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        }
    }

    public void connect(int startID, int startSide, int endID, int endSide, int margin) {
        if (!this.mConstraints.containsKey(Integer.valueOf(startID))) {
            this.mConstraints.put(Integer.valueOf(startID), new Constraint());
        }
        Constraint constraint = this.mConstraints.get(Integer.valueOf(startID));
        switch (startSide) {
            case 1:
                if (endSide == 1) {
                    constraint.layout.leftToLeft = endID;
                    constraint.layout.leftToRight = -1;
                } else if (endSide == 2) {
                    constraint.layout.leftToRight = endID;
                    constraint.layout.leftToLeft = -1;
                } else {
                    throw new IllegalArgumentException("Left to " + sideToString(endSide) + " undefined");
                }
                constraint.layout.leftMargin = margin;
                return;
            case 2:
                if (endSide == 1) {
                    constraint.layout.rightToLeft = endID;
                    constraint.layout.rightToRight = -1;
                } else if (endSide == 2) {
                    constraint.layout.rightToRight = endID;
                    constraint.layout.rightToLeft = -1;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
                constraint.layout.rightMargin = margin;
                return;
            case 3:
                if (endSide == 3) {
                    constraint.layout.topToTop = endID;
                    constraint.layout.topToBottom = -1;
                    constraint.layout.baselineToBaseline = -1;
                } else if (endSide == 4) {
                    constraint.layout.topToBottom = endID;
                    constraint.layout.topToTop = -1;
                    constraint.layout.baselineToBaseline = -1;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
                constraint.layout.topMargin = margin;
                return;
            case 4:
                if (endSide == 4) {
                    constraint.layout.bottomToBottom = endID;
                    constraint.layout.bottomToTop = -1;
                    constraint.layout.baselineToBaseline = -1;
                } else if (endSide == 3) {
                    constraint.layout.bottomToTop = endID;
                    constraint.layout.bottomToBottom = -1;
                    constraint.layout.baselineToBaseline = -1;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
                constraint.layout.bottomMargin = margin;
                return;
            case 5:
                if (endSide == 5) {
                    constraint.layout.baselineToBaseline = endID;
                    constraint.layout.bottomToBottom = -1;
                    constraint.layout.bottomToTop = -1;
                    constraint.layout.topToTop = -1;
                    constraint.layout.topToBottom = -1;
                    return;
                }
                throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
            case 6:
                if (endSide == 6) {
                    constraint.layout.startToStart = endID;
                    constraint.layout.startToEnd = -1;
                } else if (endSide == 7) {
                    constraint.layout.startToEnd = endID;
                    constraint.layout.startToStart = -1;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
                constraint.layout.startMargin = margin;
                return;
            case 7:
                if (endSide == 7) {
                    constraint.layout.endToEnd = endID;
                    constraint.layout.endToStart = -1;
                } else if (endSide == 6) {
                    constraint.layout.endToStart = endID;
                    constraint.layout.endToEnd = -1;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
                constraint.layout.endMargin = margin;
                return;
            default:
                throw new IllegalArgumentException(sideToString(startSide) + " to " + sideToString(endSide) + " unknown");
        }
    }

    public void connect(int startID, int startSide, int endID, int endSide) {
        if (!this.mConstraints.containsKey(Integer.valueOf(startID))) {
            this.mConstraints.put(Integer.valueOf(startID), new Constraint());
        }
        Constraint constraint = this.mConstraints.get(Integer.valueOf(startID));
        switch (startSide) {
            case 1:
                if (endSide == 1) {
                    constraint.layout.leftToLeft = endID;
                    constraint.layout.leftToRight = -1;
                    return;
                } else if (endSide == 2) {
                    constraint.layout.leftToRight = endID;
                    constraint.layout.leftToLeft = -1;
                    return;
                } else {
                    throw new IllegalArgumentException("left to " + sideToString(endSide) + " undefined");
                }
            case 2:
                if (endSide == 1) {
                    constraint.layout.rightToLeft = endID;
                    constraint.layout.rightToRight = -1;
                    return;
                } else if (endSide == 2) {
                    constraint.layout.rightToRight = endID;
                    constraint.layout.rightToLeft = -1;
                    return;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
            case 3:
                if (endSide == 3) {
                    constraint.layout.topToTop = endID;
                    constraint.layout.topToBottom = -1;
                    constraint.layout.baselineToBaseline = -1;
                    return;
                } else if (endSide == 4) {
                    constraint.layout.topToBottom = endID;
                    constraint.layout.topToTop = -1;
                    constraint.layout.baselineToBaseline = -1;
                    return;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
            case 4:
                if (endSide == 4) {
                    constraint.layout.bottomToBottom = endID;
                    constraint.layout.bottomToTop = -1;
                    constraint.layout.baselineToBaseline = -1;
                    return;
                } else if (endSide == 3) {
                    constraint.layout.bottomToTop = endID;
                    constraint.layout.bottomToBottom = -1;
                    constraint.layout.baselineToBaseline = -1;
                    return;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
            case 5:
                if (endSide == 5) {
                    constraint.layout.baselineToBaseline = endID;
                    constraint.layout.bottomToBottom = -1;
                    constraint.layout.bottomToTop = -1;
                    constraint.layout.topToTop = -1;
                    constraint.layout.topToBottom = -1;
                    return;
                }
                throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
            case 6:
                if (endSide == 6) {
                    constraint.layout.startToStart = endID;
                    constraint.layout.startToEnd = -1;
                    return;
                } else if (endSide == 7) {
                    constraint.layout.startToEnd = endID;
                    constraint.layout.startToStart = -1;
                    return;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
            case 7:
                if (endSide == 7) {
                    constraint.layout.endToEnd = endID;
                    constraint.layout.endToStart = -1;
                    return;
                } else if (endSide == 6) {
                    constraint.layout.endToStart = endID;
                    constraint.layout.endToEnd = -1;
                    return;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
            default:
                throw new IllegalArgumentException(sideToString(startSide) + " to " + sideToString(endSide) + " unknown");
        }
    }

    public void centerHorizontally(int viewId, int toView) {
        if (toView == 0) {
            center(viewId, 0, 1, 0, 0, 2, 0, 0.5f);
        } else {
            center(viewId, toView, 2, 0, toView, 1, 0, 0.5f);
        }
    }

    public void centerHorizontallyRtl(int viewId, int toView) {
        if (toView == 0) {
            center(viewId, 0, 6, 0, 0, 7, 0, 0.5f);
        } else {
            center(viewId, toView, 7, 0, toView, 6, 0, 0.5f);
        }
    }

    public void centerVertically(int viewId, int toView) {
        if (toView == 0) {
            center(viewId, 0, 3, 0, 0, 4, 0, 0.5f);
        } else {
            center(viewId, toView, 4, 0, toView, 3, 0, 0.5f);
        }
    }

    public void clear(int viewId) {
        this.mConstraints.remove(Integer.valueOf(viewId));
    }

    public void clear(int viewId, int anchor) {
        if (this.mConstraints.containsKey(Integer.valueOf(viewId))) {
            Constraint constraint = this.mConstraints.get(Integer.valueOf(viewId));
            switch (anchor) {
                case 1:
                    constraint.layout.leftToRight = -1;
                    constraint.layout.leftToLeft = -1;
                    constraint.layout.leftMargin = -1;
                    constraint.layout.goneLeftMargin = -1;
                    return;
                case 2:
                    constraint.layout.rightToRight = -1;
                    constraint.layout.rightToLeft = -1;
                    constraint.layout.rightMargin = -1;
                    constraint.layout.goneRightMargin = -1;
                    return;
                case 3:
                    constraint.layout.topToBottom = -1;
                    constraint.layout.topToTop = -1;
                    constraint.layout.topMargin = -1;
                    constraint.layout.goneTopMargin = -1;
                    return;
                case 4:
                    constraint.layout.bottomToTop = -1;
                    constraint.layout.bottomToBottom = -1;
                    constraint.layout.bottomMargin = -1;
                    constraint.layout.goneBottomMargin = -1;
                    return;
                case 5:
                    constraint.layout.baselineToBaseline = -1;
                    return;
                case 6:
                    constraint.layout.startToEnd = -1;
                    constraint.layout.startToStart = -1;
                    constraint.layout.startMargin = -1;
                    constraint.layout.goneStartMargin = -1;
                    return;
                case 7:
                    constraint.layout.endToStart = -1;
                    constraint.layout.endToEnd = -1;
                    constraint.layout.endMargin = -1;
                    constraint.layout.goneEndMargin = -1;
                    return;
                default:
                    throw new IllegalArgumentException("unknown constraint");
            }
        }
    }

    public void setMargin(int viewId, int anchor, int value) {
        Constraint constraint = get(viewId);
        switch (anchor) {
            case 1:
                constraint.layout.leftMargin = value;
                return;
            case 2:
                constraint.layout.rightMargin = value;
                return;
            case 3:
                constraint.layout.topMargin = value;
                return;
            case 4:
                constraint.layout.bottomMargin = value;
                return;
            case 5:
                throw new IllegalArgumentException("baseline does not support margins");
            case 6:
                constraint.layout.startMargin = value;
                return;
            case 7:
                constraint.layout.endMargin = value;
                return;
            default:
                throw new IllegalArgumentException("unknown constraint");
        }
    }

    public void setGoneMargin(int viewId, int anchor, int value) {
        Constraint constraint = get(viewId);
        switch (anchor) {
            case 1:
                constraint.layout.goneLeftMargin = value;
                return;
            case 2:
                constraint.layout.goneRightMargin = value;
                return;
            case 3:
                constraint.layout.goneTopMargin = value;
                return;
            case 4:
                constraint.layout.goneBottomMargin = value;
                return;
            case 5:
                throw new IllegalArgumentException("baseline does not support margins");
            case 6:
                constraint.layout.goneStartMargin = value;
                return;
            case 7:
                constraint.layout.goneEndMargin = value;
                return;
            default:
                throw new IllegalArgumentException("unknown constraint");
        }
    }

    public void setHorizontalBias(int viewId, float bias) {
        get(viewId).layout.horizontalBias = bias;
    }

    public void setVerticalBias(int viewId, float bias) {
        get(viewId).layout.verticalBias = bias;
    }

    public void setDimensionRatio(int viewId, String ratio) {
        get(viewId).layout.dimensionRatio = ratio;
    }

    public void setVisibility(int viewId, int visibility) {
        get(viewId).propertySet.visibility = visibility;
    }

    public void setVisibilityMode(int viewId, int visibilityMode) {
        get(viewId).propertySet.mVisibilityMode = visibilityMode;
    }

    public int getVisibilityMode(int viewId) {
        return get(viewId).propertySet.mVisibilityMode;
    }

    public int getVisibility(int viewId) {
        return get(viewId).propertySet.visibility;
    }

    public int getHeight(int viewId) {
        return get(viewId).layout.mHeight;
    }

    public int getWidth(int viewId) {
        return get(viewId).layout.mWidth;
    }

    public void setAlpha(int viewId, float alpha) {
        get(viewId).propertySet.alpha = alpha;
    }

    public boolean getApplyElevation(int viewId) {
        return get(viewId).transform.applyElevation;
    }

    public void setApplyElevation(int viewId, boolean apply) {
        if (Build.VERSION.SDK_INT >= 21) {
            get(viewId).transform.applyElevation = apply;
        }
    }

    public void setElevation(int viewId, float elevation) {
        if (Build.VERSION.SDK_INT >= 21) {
            get(viewId).transform.elevation = elevation;
            get(viewId).transform.applyElevation = true;
        }
    }

    public void setRotation(int viewId, float rotation) {
        get(viewId).transform.rotation = rotation;
    }

    public void setRotationX(int viewId, float rotationX) {
        get(viewId).transform.rotationX = rotationX;
    }

    public void setRotationY(int viewId, float rotationY) {
        get(viewId).transform.rotationY = rotationY;
    }

    public void setScaleX(int viewId, float scaleX) {
        get(viewId).transform.scaleX = scaleX;
    }

    public void setScaleY(int viewId, float scaleY) {
        get(viewId).transform.scaleY = scaleY;
    }

    public void setTransformPivotX(int viewId, float transformPivotX) {
        get(viewId).transform.transformPivotX = transformPivotX;
    }

    public void setTransformPivotY(int viewId, float transformPivotY) {
        get(viewId).transform.transformPivotY = transformPivotY;
    }

    public void setTransformPivot(int viewId, float transformPivotX, float transformPivotY) {
        Constraint constraint = get(viewId);
        constraint.transform.transformPivotY = transformPivotY;
        constraint.transform.transformPivotX = transformPivotX;
    }

    public void setTranslationX(int viewId, float translationX) {
        get(viewId).transform.translationX = translationX;
    }

    public void setTranslationY(int viewId, float translationY) {
        get(viewId).transform.translationY = translationY;
    }

    public void setTranslation(int viewId, float translationX, float translationY) {
        Constraint constraint = get(viewId);
        constraint.transform.translationX = translationX;
        constraint.transform.translationY = translationY;
    }

    public void setTranslationZ(int viewId, float translationZ) {
        if (Build.VERSION.SDK_INT >= 21) {
            get(viewId).transform.translationZ = translationZ;
        }
    }

    public void setEditorAbsoluteX(int viewId, int position) {
        get(viewId).layout.editorAbsoluteX = position;
    }

    public void setEditorAbsoluteY(int viewId, int position) {
        get(viewId).layout.editorAbsoluteY = position;
    }

    public void constrainHeight(int viewId, int height) {
        get(viewId).layout.mHeight = height;
    }

    public void constrainWidth(int viewId, int width) {
        get(viewId).layout.mWidth = width;
    }

    public void constrainCircle(int viewId, int id, int radius, float angle) {
        Constraint constraint = get(viewId);
        constraint.layout.circleConstraint = id;
        constraint.layout.circleRadius = radius;
        constraint.layout.circleAngle = angle;
    }

    public void constrainMaxHeight(int viewId, int height) {
        get(viewId).layout.heightMax = height;
    }

    public void constrainMaxWidth(int viewId, int width) {
        get(viewId).layout.widthMax = width;
    }

    public void constrainMinHeight(int viewId, int height) {
        get(viewId).layout.heightMin = height;
    }

    public void constrainMinWidth(int viewId, int width) {
        get(viewId).layout.widthMin = width;
    }

    public void constrainPercentWidth(int viewId, float percent) {
        get(viewId).layout.widthPercent = percent;
    }

    public void constrainPercentHeight(int viewId, float percent) {
        get(viewId).layout.heightPercent = percent;
    }

    public void constrainDefaultHeight(int viewId, int height) {
        get(viewId).layout.heightDefault = height;
    }

    public void constrainedWidth(int viewId, boolean constrained) {
        get(viewId).layout.constrainedWidth = constrained;
    }

    public void constrainedHeight(int viewId, boolean constrained) {
        get(viewId).layout.constrainedHeight = constrained;
    }

    public void constrainDefaultWidth(int viewId, int width) {
        get(viewId).layout.widthDefault = width;
    }

    public void setHorizontalWeight(int viewId, float weight) {
        get(viewId).layout.horizontalWeight = weight;
    }

    public void setVerticalWeight(int viewId, float weight) {
        get(viewId).layout.verticalWeight = weight;
    }

    public void setHorizontalChainStyle(int viewId, int chainStyle) {
        get(viewId).layout.horizontalChainStyle = chainStyle;
    }

    public void setVerticalChainStyle(int viewId, int chainStyle) {
        get(viewId).layout.verticalChainStyle = chainStyle;
    }

    public void addToHorizontalChain(int viewId, int leftId, int rightId) {
        connect(viewId, 1, leftId, leftId == 0 ? 1 : 2, 0);
        connect(viewId, 2, rightId, rightId == 0 ? 2 : 1, 0);
        if (leftId != 0) {
            connect(leftId, 2, viewId, 1, 0);
        }
        if (rightId != 0) {
            connect(rightId, 1, viewId, 2, 0);
        }
    }

    public void addToHorizontalChainRTL(int viewId, int leftId, int rightId) {
        connect(viewId, 6, leftId, leftId == 0 ? 6 : 7, 0);
        connect(viewId, 7, rightId, rightId == 0 ? 7 : 6, 0);
        if (leftId != 0) {
            connect(leftId, 7, viewId, 6, 0);
        }
        if (rightId != 0) {
            connect(rightId, 6, viewId, 7, 0);
        }
    }

    public void addToVerticalChain(int viewId, int topId, int bottomId) {
        connect(viewId, 3, topId, topId == 0 ? 3 : 4, 0);
        connect(viewId, 4, bottomId, bottomId == 0 ? 4 : 3, 0);
        if (topId != 0) {
            connect(topId, 4, viewId, 3, 0);
        }
        if (bottomId != 0) {
            connect(bottomId, 3, viewId, 4, 0);
        }
    }

    public void removeFromVerticalChain(int viewId) {
        if (this.mConstraints.containsKey(Integer.valueOf(viewId))) {
            Constraint constraint = this.mConstraints.get(Integer.valueOf(viewId));
            int topId = constraint.layout.topToBottom;
            int bottomId = constraint.layout.bottomToTop;
            if (!(topId == -1 && bottomId == -1)) {
                if (topId != -1 && bottomId != -1) {
                    connect(topId, 4, bottomId, 3, 0);
                    connect(bottomId, 3, topId, 4, 0);
                } else if (!(topId == -1 && bottomId == -1)) {
                    if (constraint.layout.bottomToBottom != -1) {
                        connect(topId, 4, constraint.layout.bottomToBottom, 4, 0);
                    } else if (constraint.layout.topToTop != -1) {
                        connect(bottomId, 3, constraint.layout.topToTop, 3, 0);
                    }
                }
            }
        }
        clear(viewId, 3);
        clear(viewId, 4);
    }

    public void removeFromHorizontalChain(int viewId) {
        if (this.mConstraints.containsKey(Integer.valueOf(viewId))) {
            Constraint constraint = this.mConstraints.get(Integer.valueOf(viewId));
            int leftId = constraint.layout.leftToRight;
            int rightId = constraint.layout.rightToLeft;
            if (leftId == -1 && rightId == -1) {
                int startId = constraint.layout.startToEnd;
                int endId = constraint.layout.endToStart;
                if (!(startId == -1 && endId == -1)) {
                    if (startId != -1 && endId != -1) {
                        connect(startId, 7, endId, 6, 0);
                        connect(endId, 6, leftId, 7, 0);
                    } else if (!(leftId == -1 && endId == -1)) {
                        if (constraint.layout.rightToRight != -1) {
                            connect(leftId, 7, constraint.layout.rightToRight, 7, 0);
                        } else if (constraint.layout.leftToLeft != -1) {
                            connect(endId, 6, constraint.layout.leftToLeft, 6, 0);
                        }
                    }
                }
                clear(viewId, 6);
                clear(viewId, 7);
                return;
            }
            if (leftId != -1 && rightId != -1) {
                connect(leftId, 2, rightId, 1, 0);
                connect(rightId, 1, leftId, 2, 0);
            } else if (!(leftId == -1 && rightId == -1)) {
                if (constraint.layout.rightToRight != -1) {
                    connect(leftId, 2, constraint.layout.rightToRight, 2, 0);
                } else if (constraint.layout.leftToLeft != -1) {
                    connect(rightId, 1, constraint.layout.leftToLeft, 1, 0);
                }
            }
            clear(viewId, 1);
            clear(viewId, 2);
        }
    }

    public void create(int guidelineID, int orientation) {
        Constraint constraint = get(guidelineID);
        constraint.layout.mIsGuideline = true;
        constraint.layout.orientation = orientation;
    }

    public void createBarrier(int id, int direction, int margin, int... referenced) {
        Constraint constraint = get(id);
        constraint.layout.mHelperType = 1;
        constraint.layout.mBarrierDirection = direction;
        constraint.layout.mBarrierMargin = margin;
        constraint.layout.mIsGuideline = false;
        constraint.layout.mReferenceIds = referenced;
    }

    public void setGuidelineBegin(int guidelineID, int margin) {
        get(guidelineID).layout.guideBegin = margin;
        get(guidelineID).layout.guideEnd = -1;
        get(guidelineID).layout.guidePercent = -1.0f;
    }

    public void setGuidelineEnd(int guidelineID, int margin) {
        get(guidelineID).layout.guideEnd = margin;
        get(guidelineID).layout.guideBegin = -1;
        get(guidelineID).layout.guidePercent = -1.0f;
    }

    public void setGuidelinePercent(int guidelineID, float ratio) {
        get(guidelineID).layout.guidePercent = ratio;
        get(guidelineID).layout.guideEnd = -1;
        get(guidelineID).layout.guideBegin = -1;
    }

    public int[] getReferencedIds(int id) {
        Constraint constraint = get(id);
        if (constraint.layout.mReferenceIds == null) {
            return new int[0];
        }
        return Arrays.copyOf(constraint.layout.mReferenceIds, constraint.layout.mReferenceIds.length);
    }

    public void setReferencedIds(int id, int... referenced) {
        get(id).layout.mReferenceIds = referenced;
    }

    public void setBarrierType(int id, int type) {
        get(id).layout.mHelperType = type;
    }

    public void removeAttribute(String attributeName) {
        this.mSavedAttributes.remove(attributeName);
    }

    public void setIntValue(int viewId, String attributeName, int value) {
        get(viewId).setIntValue(attributeName, value);
    }

    public void setColorValue(int viewId, String attributeName, int value) {
        get(viewId).setColorValue(attributeName, value);
    }

    public void setFloatValue(int viewId, String attributeName, float value) {
        get(viewId).setFloatValue(attributeName, value);
    }

    public void setStringValue(int viewId, String attributeName, String value) {
        get(viewId).setStringValue(attributeName, value);
    }

    private void addAttributes(ConstraintAttribute.AttributeType attributeType, String... attributeName) {
        for (int i = 0; i < attributeName.length; i++) {
            if (this.mSavedAttributes.containsKey(attributeName[i])) {
                ConstraintAttribute constraintAttribute = this.mSavedAttributes.get(attributeName[i]);
                if (constraintAttribute.getType() != attributeType) {
                    throw new IllegalArgumentException("ConstraintAttribute is already a " + constraintAttribute.getType().name());
                }
            } else {
                this.mSavedAttributes.put(attributeName[i], new ConstraintAttribute(attributeName[i], attributeType));
            }
        }
    }

    public void parseIntAttributes(Constraint set, String attributes) {
        String[] sp = attributes.split(",");
        for (int i = 0; i < sp.length; i++) {
            String[] attr = sp[i].split("=");
            if (attr.length != 2) {
                Log.w(TAG, " Unable to parse " + sp[i]);
            } else {
                set.setFloatValue(attr[0], (float) Integer.decode(attr[1]).intValue());
            }
        }
    }

    public void parseColorAttributes(Constraint set, String attributes) {
        String[] sp = attributes.split(",");
        for (int i = 0; i < sp.length; i++) {
            String[] attr = sp[i].split("=");
            if (attr.length != 2) {
                Log.w(TAG, " Unable to parse " + sp[i]);
            } else {
                set.setColorValue(attr[0], Color.parseColor(attr[1]));
            }
        }
    }

    public void parseFloatAttributes(Constraint set, String attributes) {
        String[] sp = attributes.split(",");
        for (int i = 0; i < sp.length; i++) {
            String[] attr = sp[i].split("=");
            if (attr.length != 2) {
                Log.w(TAG, " Unable to parse " + sp[i]);
            } else {
                set.setFloatValue(attr[0], Float.parseFloat(attr[1]));
            }
        }
    }

    public void parseStringAttributes(Constraint set, String attributes) {
        String[] sp = splitString(attributes);
        for (int i = 0; i < sp.length; i++) {
            String[] attr = sp[i].split("=");
            Log.w(TAG, " Unable to parse " + sp[i]);
            set.setStringValue(attr[0], attr[1]);
        }
    }

    private static String[] splitString(String str) {
        char[] chars = str.toCharArray();
        ArrayList<String> list = new ArrayList<>();
        boolean indouble = false;
        int start = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ',' && !indouble) {
                list.add(new String(chars, start, i - start));
                start = i + 1;
            } else if (chars[i] == '\"') {
                indouble = !indouble;
            }
        }
        list.add(new String(chars, start, chars.length - start));
        return (String[]) list.toArray(new String[list.size()]);
    }

    public void addIntAttributes(String... attributeName) {
        addAttributes(ConstraintAttribute.AttributeType.INT_TYPE, attributeName);
    }

    public void addColorAttributes(String... attributeName) {
        addAttributes(ConstraintAttribute.AttributeType.COLOR_TYPE, attributeName);
    }

    public void addFloatAttributes(String... attributeName) {
        addAttributes(ConstraintAttribute.AttributeType.FLOAT_TYPE, attributeName);
    }

    public void addStringAttributes(String... attributeName) {
        addAttributes(ConstraintAttribute.AttributeType.STRING_TYPE, attributeName);
    }

    private Constraint get(int id) {
        if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
            this.mConstraints.put(Integer.valueOf(id), new Constraint());
        }
        return this.mConstraints.get(Integer.valueOf(id));
    }

    private String sideToString(int side) {
        switch (side) {
            case 1:
                return "left";
            case 2:
                return "right";
            case 3:
                return "top";
            case 4:
                return "bottom";
            case 5:
                return "baseline";
            case 6:
                return "start";
            case 7:
                return "end";
            default:
                return "undefined";
        }
    }

    public void load(Context context, int resourceId) {
        XmlPullParser parser = context.getResources().getXml(resourceId);
        try {
            int eventType = parser.getEventType();
            while (eventType != 1) {
                if (eventType == 0) {
                    parser.getName();
                } else if (eventType == 2) {
                    String tagName = parser.getName();
                    Constraint constraint = fillFromAttributeList(context, Xml.asAttributeSet(parser));
                    if (tagName.equalsIgnoreCase("Guideline")) {
                        constraint.layout.mIsGuideline = true;
                    }
                    this.mConstraints.put(Integer.valueOf(constraint.mViewId), constraint);
                } else if (eventType != 3) {
                }
                eventType = parser.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void load(Context context, XmlPullParser parser) {
        char c;
        Constraint constraint = null;
        try {
            int eventType = parser.getEventType();
            while (eventType != 1) {
                if (eventType == 0) {
                    parser.getName();
                } else if (eventType == 2) {
                    String tagName = parser.getName();
                    switch (tagName.hashCode()) {
                        case -2025855158:
                            if (tagName.equals("Layout")) {
                                c = 5;
                                break;
                            }
                            c = 65535;
                            break;
                        case -1984451626:
                            if (tagName.equals("Motion")) {
                                c = 6;
                                break;
                            }
                            c = 65535;
                            break;
                        case -1269513683:
                            if (tagName.equals("PropertySet")) {
                                c = 3;
                                break;
                            }
                            c = 65535;
                            break;
                        case -1238332596:
                            if (tagName.equals("Transform")) {
                                c = 4;
                                break;
                            }
                            c = 65535;
                            break;
                        case -71750448:
                            if (tagName.equals("Guideline")) {
                                c = 1;
                                break;
                            }
                            c = 65535;
                            break;
                        case 1331510167:
                            if (tagName.equals("Barrier")) {
                                c = 2;
                                break;
                            }
                            c = 65535;
                            break;
                        case 1791837707:
                            if (tagName.equals("CustomAttribute")) {
                                c = 7;
                                break;
                            }
                            c = 65535;
                            break;
                        case 1803088381:
                            if (tagName.equals("Constraint")) {
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
                            constraint = fillFromAttributeList(context, Xml.asAttributeSet(parser));
                            break;
                        case 1:
                            constraint = fillFromAttributeList(context, Xml.asAttributeSet(parser));
                            constraint.layout.mIsGuideline = true;
                            constraint.layout.mApply = true;
                            break;
                        case 2:
                            constraint = fillFromAttributeList(context, Xml.asAttributeSet(parser));
                            constraint.layout.mHelperType = 1;
                            break;
                        case 3:
                            if (constraint != null) {
                                constraint.propertySet.fillFromAttributeList(context, Xml.asAttributeSet(parser));
                                break;
                            } else {
                                throw new RuntimeException(ERROR_MESSAGE + parser.getLineNumber());
                            }
                        case 4:
                            if (constraint != null) {
                                constraint.transform.fillFromAttributeList(context, Xml.asAttributeSet(parser));
                                break;
                            } else {
                                throw new RuntimeException(ERROR_MESSAGE + parser.getLineNumber());
                            }
                        case 5:
                            if (constraint != null) {
                                constraint.layout.fillFromAttributeList(context, Xml.asAttributeSet(parser));
                                break;
                            } else {
                                throw new RuntimeException(ERROR_MESSAGE + parser.getLineNumber());
                            }
                        case 6:
                            if (constraint != null) {
                                constraint.motion.fillFromAttributeList(context, Xml.asAttributeSet(parser));
                                break;
                            } else {
                                throw new RuntimeException(ERROR_MESSAGE + parser.getLineNumber());
                            }
                        case 7:
                            if (constraint != null) {
                                ConstraintAttribute.parse(context, parser, constraint.mCustomConstraints);
                                break;
                            } else {
                                throw new RuntimeException(ERROR_MESSAGE + parser.getLineNumber());
                            }
                    }
                } else if (eventType == 3) {
                    String tagName2 = parser.getName();
                    if (!TAG.equals(tagName2)) {
                        if (tagName2.equalsIgnoreCase("Constraint")) {
                            this.mConstraints.put(Integer.valueOf(constraint.mViewId), constraint);
                            constraint = null;
                        }
                    } else {
                        return;
                    }
                }
                eventType = parser.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
    }

    public static int lookupID(TypedArray a2, int index, int def) {
        int ret = a2.getResourceId(index, def);
        if (ret == -1) {
            return a2.getInt(index, -1);
        }
        return ret;
    }

    private Constraint fillFromAttributeList(Context context, AttributeSet attrs) {
        Constraint c = new Constraint();
        TypedArray a2 = context.obtainStyledAttributes(attrs, R.styleable.Constraint);
        populateConstraint(context, c, a2);
        a2.recycle();
        return c;
    }

    private void populateConstraint(Context ctx, Constraint c, TypedArray a2) {
        int N = a2.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a2.getIndex(i);
            if (!(attr == R.styleable.Constraint_android_id || R.styleable.Constraint_android_layout_marginStart == attr || R.styleable.Constraint_android_layout_marginEnd == attr)) {
                c.motion.mApply = true;
                c.layout.mApply = true;
                c.propertySet.mApply = true;
                c.transform.mApply = true;
            }
            switch (mapToConstant.get(attr)) {
                case 1:
                    c.layout.baselineToBaseline = lookupID(a2, attr, c.layout.baselineToBaseline);
                    break;
                case 2:
                    c.layout.bottomMargin = a2.getDimensionPixelSize(attr, c.layout.bottomMargin);
                    break;
                case 3:
                    c.layout.bottomToBottom = lookupID(a2, attr, c.layout.bottomToBottom);
                    break;
                case 4:
                    c.layout.bottomToTop = lookupID(a2, attr, c.layout.bottomToTop);
                    break;
                case 5:
                    c.layout.dimensionRatio = a2.getString(attr);
                    break;
                case 6:
                    c.layout.editorAbsoluteX = a2.getDimensionPixelOffset(attr, c.layout.editorAbsoluteX);
                    break;
                case 7:
                    c.layout.editorAbsoluteY = a2.getDimensionPixelOffset(attr, c.layout.editorAbsoluteY);
                    break;
                case 8:
                    if (Build.VERSION.SDK_INT >= 17) {
                        c.layout.endMargin = a2.getDimensionPixelSize(attr, c.layout.endMargin);
                        break;
                    } else {
                        break;
                    }
                case 9:
                    c.layout.endToEnd = lookupID(a2, attr, c.layout.endToEnd);
                    break;
                case 10:
                    c.layout.endToStart = lookupID(a2, attr, c.layout.endToStart);
                    break;
                case 11:
                    c.layout.goneBottomMargin = a2.getDimensionPixelSize(attr, c.layout.goneBottomMargin);
                    break;
                case 12:
                    c.layout.goneEndMargin = a2.getDimensionPixelSize(attr, c.layout.goneEndMargin);
                    break;
                case 13:
                    c.layout.goneLeftMargin = a2.getDimensionPixelSize(attr, c.layout.goneLeftMargin);
                    break;
                case 14:
                    c.layout.goneRightMargin = a2.getDimensionPixelSize(attr, c.layout.goneRightMargin);
                    break;
                case 15:
                    c.layout.goneStartMargin = a2.getDimensionPixelSize(attr, c.layout.goneStartMargin);
                    break;
                case 16:
                    c.layout.goneTopMargin = a2.getDimensionPixelSize(attr, c.layout.goneTopMargin);
                    break;
                case 17:
                    c.layout.guideBegin = a2.getDimensionPixelOffset(attr, c.layout.guideBegin);
                    break;
                case 18:
                    c.layout.guideEnd = a2.getDimensionPixelOffset(attr, c.layout.guideEnd);
                    break;
                case 19:
                    c.layout.guidePercent = a2.getFloat(attr, c.layout.guidePercent);
                    break;
                case 20:
                    c.layout.horizontalBias = a2.getFloat(attr, c.layout.horizontalBias);
                    break;
                case 21:
                    c.layout.mHeight = a2.getLayoutDimension(attr, c.layout.mHeight);
                    break;
                case 22:
                    c.propertySet.visibility = a2.getInt(attr, c.propertySet.visibility);
                    c.propertySet.visibility = VISIBILITY_FLAGS[c.propertySet.visibility];
                    break;
                case 23:
                    c.layout.mWidth = a2.getLayoutDimension(attr, c.layout.mWidth);
                    break;
                case 24:
                    c.layout.leftMargin = a2.getDimensionPixelSize(attr, c.layout.leftMargin);
                    break;
                case 25:
                    c.layout.leftToLeft = lookupID(a2, attr, c.layout.leftToLeft);
                    break;
                case 26:
                    c.layout.leftToRight = lookupID(a2, attr, c.layout.leftToRight);
                    break;
                case 27:
                    c.layout.orientation = a2.getInt(attr, c.layout.orientation);
                    break;
                case 28:
                    c.layout.rightMargin = a2.getDimensionPixelSize(attr, c.layout.rightMargin);
                    break;
                case 29:
                    c.layout.rightToLeft = lookupID(a2, attr, c.layout.rightToLeft);
                    break;
                case 30:
                    c.layout.rightToRight = lookupID(a2, attr, c.layout.rightToRight);
                    break;
                case 31:
                    if (Build.VERSION.SDK_INT >= 17) {
                        c.layout.startMargin = a2.getDimensionPixelSize(attr, c.layout.startMargin);
                        break;
                    } else {
                        break;
                    }
                case 32:
                    c.layout.startToEnd = lookupID(a2, attr, c.layout.startToEnd);
                    break;
                case 33:
                    c.layout.startToStart = lookupID(a2, attr, c.layout.startToStart);
                    break;
                case 34:
                    c.layout.topMargin = a2.getDimensionPixelSize(attr, c.layout.topMargin);
                    break;
                case 35:
                    c.layout.topToBottom = lookupID(a2, attr, c.layout.topToBottom);
                    break;
                case 36:
                    c.layout.topToTop = lookupID(a2, attr, c.layout.topToTop);
                    break;
                case 37:
                    c.layout.verticalBias = a2.getFloat(attr, c.layout.verticalBias);
                    break;
                case 38:
                    c.mViewId = a2.getResourceId(attr, c.mViewId);
                    break;
                case 39:
                    c.layout.horizontalWeight = a2.getFloat(attr, c.layout.horizontalWeight);
                    break;
                case 40:
                    c.layout.verticalWeight = a2.getFloat(attr, c.layout.verticalWeight);
                    break;
                case 41:
                    c.layout.horizontalChainStyle = a2.getInt(attr, c.layout.horizontalChainStyle);
                    break;
                case 42:
                    c.layout.verticalChainStyle = a2.getInt(attr, c.layout.verticalChainStyle);
                    break;
                case 43:
                    c.propertySet.alpha = a2.getFloat(attr, c.propertySet.alpha);
                    break;
                case 44:
                    if (Build.VERSION.SDK_INT >= 21) {
                        c.transform.applyElevation = true;
                        c.transform.elevation = a2.getDimension(attr, c.transform.elevation);
                        break;
                    } else {
                        break;
                    }
                case 45:
                    c.transform.rotationX = a2.getFloat(attr, c.transform.rotationX);
                    break;
                case 46:
                    c.transform.rotationY = a2.getFloat(attr, c.transform.rotationY);
                    break;
                case 47:
                    c.transform.scaleX = a2.getFloat(attr, c.transform.scaleX);
                    break;
                case 48:
                    c.transform.scaleY = a2.getFloat(attr, c.transform.scaleY);
                    break;
                case 49:
                    c.transform.transformPivotX = a2.getDimension(attr, c.transform.transformPivotX);
                    break;
                case 50:
                    c.transform.transformPivotY = a2.getDimension(attr, c.transform.transformPivotY);
                    break;
                case 51:
                    c.transform.translationX = a2.getDimension(attr, c.transform.translationX);
                    break;
                case 52:
                    c.transform.translationY = a2.getDimension(attr, c.transform.translationY);
                    break;
                case 53:
                    if (Build.VERSION.SDK_INT >= 21) {
                        c.transform.translationZ = a2.getDimension(attr, c.transform.translationZ);
                        break;
                    } else {
                        break;
                    }
                case 54:
                    c.layout.widthDefault = a2.getInt(attr, c.layout.widthDefault);
                    break;
                case 55:
                    c.layout.heightDefault = a2.getInt(attr, c.layout.heightDefault);
                    break;
                case 56:
                    c.layout.widthMax = a2.getDimensionPixelSize(attr, c.layout.widthMax);
                    break;
                case 57:
                    c.layout.heightMax = a2.getDimensionPixelSize(attr, c.layout.heightMax);
                    break;
                case 58:
                    c.layout.widthMin = a2.getDimensionPixelSize(attr, c.layout.widthMin);
                    break;
                case 59:
                    c.layout.heightMin = a2.getDimensionPixelSize(attr, c.layout.heightMin);
                    break;
                case 60:
                    c.transform.rotation = a2.getFloat(attr, c.transform.rotation);
                    break;
                case 61:
                    c.layout.circleConstraint = lookupID(a2, attr, c.layout.circleConstraint);
                    break;
                case 62:
                    c.layout.circleRadius = a2.getDimensionPixelSize(attr, c.layout.circleRadius);
                    break;
                case 63:
                    c.layout.circleAngle = a2.getFloat(attr, c.layout.circleAngle);
                    break;
                case 64:
                    c.motion.mAnimateRelativeTo = lookupID(a2, attr, c.motion.mAnimateRelativeTo);
                    break;
                case 65:
                    if (a2.peekValue(attr).type == 3) {
                        c.motion.mTransitionEasing = a2.getString(attr);
                        break;
                    } else {
                        c.motion.mTransitionEasing = Easing.NAMED_EASING[a2.getInteger(attr, 0)];
                        break;
                    }
                case 66:
                    c.motion.mDrawPath = a2.getInt(attr, 0);
                    break;
                case 67:
                    c.motion.mPathRotate = a2.getFloat(attr, c.motion.mPathRotate);
                    break;
                case 68:
                    c.propertySet.mProgress = a2.getFloat(attr, c.propertySet.mProgress);
                    break;
                case 69:
                    c.layout.widthPercent = a2.getFloat(attr, 1.0f);
                    break;
                case 70:
                    c.layout.heightPercent = a2.getFloat(attr, 1.0f);
                    break;
                case 71:
                    Log.e(TAG, "CURRENTLY UNSUPPORTED");
                    break;
                case 72:
                    c.layout.mBarrierDirection = a2.getInt(attr, c.layout.mBarrierDirection);
                    break;
                case 73:
                    c.layout.mBarrierMargin = a2.getDimensionPixelSize(attr, c.layout.mBarrierMargin);
                    break;
                case 74:
                    c.layout.mReferenceIdString = a2.getString(attr);
                    break;
                case 75:
                    c.layout.mBarrierAllowsGoneWidgets = a2.getBoolean(attr, c.layout.mBarrierAllowsGoneWidgets);
                    break;
                case 76:
                    c.motion.mPathMotionArc = a2.getInt(attr, c.motion.mPathMotionArc);
                    break;
                case 77:
                    c.layout.mConstraintTag = a2.getString(attr);
                    break;
                case 78:
                    c.propertySet.mVisibilityMode = a2.getInt(attr, c.propertySet.mVisibilityMode);
                    break;
                case 79:
                    c.motion.mMotionStagger = a2.getFloat(attr, c.motion.mMotionStagger);
                    break;
                case 80:
                    c.layout.constrainedWidth = a2.getBoolean(attr, c.layout.constrainedWidth);
                    break;
                case 81:
                    c.layout.constrainedHeight = a2.getBoolean(attr, c.layout.constrainedHeight);
                    break;
                case 82:
                    Log.w(TAG, "unused attribute 0x" + Integer.toHexString(attr) + "   " + mapToConstant.get(attr));
                    break;
                default:
                    Log.w(TAG, "Unknown attribute 0x" + Integer.toHexString(attr) + "   " + mapToConstant.get(attr));
                    break;
            }
        }
    }

    /* JADX INFO: Multiple debug info for r7v0 int: [D('constraintLayout' androidx.constraintlayout.widget.ConstraintLayout), D('count' int)] */
    private int[] convertReferenceString(View view, String referenceIdString) {
        Object value;
        String[] split = referenceIdString.split(",");
        Context context = view.getContext();
        int[] tags = new int[split.length];
        int count = 0;
        int i = 0;
        while (i < split.length) {
            String idString = split[i].trim();
            int tag = 0;
            try {
                tag = R.id.class.getField(idString).getInt(null);
            } catch (Exception e) {
            }
            if (tag == 0) {
                tag = context.getResources().getIdentifier(idString, "id", context.getPackageName());
            }
            if (tag == 0 && view.isInEditMode() && (view.getParent() instanceof ConstraintLayout) && (value = ((ConstraintLayout) view.getParent()).getDesignInformation(0, idString)) != null && (value instanceof Integer)) {
                tag = ((Integer) value).intValue();
            }
            tags[count] = tag;
            i++;
            count++;
        }
        if (count != split.length) {
            return Arrays.copyOf(tags, count);
        }
        return tags;
    }

    public Constraint getConstraint(int id) {
        if (this.mConstraints.containsKey(Integer.valueOf(id))) {
            return this.mConstraints.get(Integer.valueOf(id));
        }
        return null;
    }

    public int[] getKnownIds() {
        Integer[] arr = (Integer[]) this.mConstraints.keySet().toArray(new Integer[0]);
        int[] array = new int[arr.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = arr[i].intValue();
        }
        return array;
    }

    public boolean isForceId() {
        return this.mForceId;
    }

    public void setForceId(boolean forceId) {
        this.mForceId = forceId;
    }

    public void setValidateOnParse(boolean validate) {
        this.mValidate = validate;
    }

    public void dump(MotionScene scene, int... ids) {
        HashSet<Integer> set;
        Set<Integer> keys = this.mConstraints.keySet();
        if (ids.length != 0) {
            set = new HashSet<>();
            for (int id : ids) {
                set.add(Integer.valueOf(id));
            }
        } else {
            set = new HashSet<>(keys);
        }
        System.out.println(set.size() + " constraints");
        StringBuilder stringBuilder = new StringBuilder();
        Integer[] numArr = (Integer[]) set.toArray(new Integer[0]);
        for (Integer id2 : numArr) {
            stringBuilder.append("<Constraint id=");
            stringBuilder.append(id2);
            stringBuilder.append(" \n");
            this.mConstraints.get(id2).layout.dump(scene, stringBuilder);
            stringBuilder.append("/>\n");
        }
        System.out.println(stringBuilder.toString());
    }
}
