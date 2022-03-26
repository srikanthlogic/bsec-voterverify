package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.solver.Metrics;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.Guideline;
import androidx.constraintlayout.solver.widgets.Optimizer;
import androidx.constraintlayout.solver.widgets.VirtualLayout;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import androidx.exifinterface.media.ExifInterface;
import java.util.ArrayList;
import java.util.HashMap;
/* loaded from: classes.dex */
public class ConstraintLayout extends ViewGroup {
    private static final boolean DEBUG;
    private static final boolean DEBUG_DRAW_CONSTRAINTS;
    public static final int DESIGN_INFO_ID;
    private static final boolean MEASURE;
    private static final String TAG;
    private static final boolean USE_CONSTRAINTS_HELPER;
    public static final String VERSION;
    private ConstraintsChangedListener mConstraintsChangedListener;
    private Metrics mMetrics;
    SparseArray<View> mChildrenByIds = new SparseArray<>();
    private ArrayList<ConstraintHelper> mConstraintHelpers = new ArrayList<>(4);
    protected ConstraintWidgetContainer mLayoutWidget = new ConstraintWidgetContainer();
    private int mMinWidth = 0;
    private int mMinHeight = 0;
    private int mMaxWidth = Integer.MAX_VALUE;
    private int mMaxHeight = Integer.MAX_VALUE;
    protected boolean mDirtyHierarchy = true;
    private int mOptimizationLevel = 257;
    private ConstraintSet mConstraintSet = null;
    protected ConstraintLayoutStates mConstraintLayoutSpec = null;
    private int mConstraintSetId = -1;
    private HashMap<String, Integer> mDesignIds = new HashMap<>();
    private int mLastMeasureWidth = -1;
    private int mLastMeasureHeight = -1;
    int mLastMeasureWidthSize = -1;
    int mLastMeasureHeightSize = -1;
    int mLastMeasureWidthMode = 0;
    int mLastMeasureHeightMode = 0;
    private SparseArray<ConstraintWidget> mTempMapIdToWidget = new SparseArray<>();
    Measurer mMeasurer = new Measurer(this);
    private int mOnMeasureWidthMeasureSpec = 0;
    private int mOnMeasureHeightMeasureSpec = 0;

    public void setDesignInformation(int type, Object value1, Object value2) {
        if (type == 0 && (value1 instanceof String) && (value2 instanceof Integer)) {
            if (this.mDesignIds == null) {
                this.mDesignIds = new HashMap<>();
            }
            String name = (String) value1;
            int index = name.indexOf("/");
            if (index != -1) {
                name = name.substring(index + 1);
            }
            this.mDesignIds.put(name, Integer.valueOf(((Integer) value2).intValue()));
        }
    }

    public Object getDesignInformation(int type, Object value) {
        if (type != 0 || !(value instanceof String)) {
            return null;
        }
        String name = (String) value;
        HashMap<String, Integer> hashMap = this.mDesignIds;
        if (hashMap == null || !hashMap.containsKey(name)) {
            return null;
        }
        return this.mDesignIds.get(name);
    }

    public ConstraintLayout(Context context) {
        super(context);
        init(null, 0, 0);
    }

    public ConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public ConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    public ConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    @Override // android.view.View
    public void setId(int id) {
        this.mChildrenByIds.remove(getId());
        super.setId(id);
        this.mChildrenByIds.put(getId(), this);
    }

    /* loaded from: classes.dex */
    public class Measurer implements BasicMeasure.Measurer {
        ConstraintLayout layout;
        int layoutHeightSpec;
        int layoutWidthSpec;
        int paddingBottom;
        int paddingHeight;
        int paddingTop;
        int paddingWidth;

        public void captureLayoutInfos(int widthSpec, int heightSpec, int top, int bottom, int width, int height) {
            this.paddingTop = top;
            this.paddingBottom = bottom;
            this.paddingWidth = width;
            this.paddingHeight = height;
            this.layoutWidthSpec = widthSpec;
            this.layoutHeightSpec = heightSpec;
        }

        public Measurer(ConstraintLayout l) {
            ConstraintLayout.this = this$0;
            this.layout = l;
        }

        /* JADX INFO: Multiple debug info for r0v13 int: [D('ratio' float), D('width' int)] */
        /* JADX WARN: Removed duplicated region for block: B:145:0x0213 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:146:0x0214  */
        @Override // androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure.Measurer
        /* Code decompiled incorrectly, please refer to instructions dump */
        public final void measure(ConstraintWidget widget, BasicMeasure.Measure measure) {
            boolean horizontalUseRatio;
            boolean verticalUseRatio;
            int widthPadding;
            int verticalSpec;
            int verticalSpec2;
            int verticalSpec3;
            int horizontalSpec;
            int i;
            int verticalSpec4;
            if (widget != null) {
                if (widget.getVisibility() == 8 && !widget.isInPlaceholder()) {
                    measure.measuredWidth = 0;
                    measure.measuredHeight = 0;
                    measure.measuredBaseline = 0;
                } else if (widget.getParent() != null) {
                    ConstraintWidget.DimensionBehaviour horizontalBehavior = measure.horizontalBehavior;
                    ConstraintWidget.DimensionBehaviour verticalBehavior = measure.verticalBehavior;
                    int horizontalDimension = measure.horizontalDimension;
                    int verticalDimension = measure.verticalDimension;
                    int horizontalSpec2 = 0;
                    int verticalSpec5 = 0;
                    int heightPadding = this.paddingTop + this.paddingBottom;
                    int widthPadding2 = this.paddingWidth;
                    View child = (View) widget.getCompanionWidget();
                    int i2 = AnonymousClass1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour[horizontalBehavior.ordinal()];
                    if (i2 == 1) {
                        horizontalSpec2 = View.MeasureSpec.makeMeasureSpec(horizontalDimension, 1073741824);
                    } else if (i2 == 2) {
                        horizontalSpec2 = ViewGroup.getChildMeasureSpec(this.layoutWidthSpec, widthPadding2, -2);
                    } else if (i2 == 3) {
                        horizontalSpec2 = ViewGroup.getChildMeasureSpec(this.layoutWidthSpec, widget.getHorizontalMargin() + widthPadding2, -1);
                    } else if (i2 == 4) {
                        horizontalSpec2 = ViewGroup.getChildMeasureSpec(this.layoutWidthSpec, widthPadding2, -2);
                        boolean shouldDoWrap = widget.mMatchConstraintDefaultWidth == 1;
                        if (measure.measureStrategy == BasicMeasure.Measure.TRY_GIVEN_DIMENSIONS || measure.measureStrategy == BasicMeasure.Measure.USE_GIVEN_DIMENSIONS) {
                            if (measure.measureStrategy == BasicMeasure.Measure.USE_GIVEN_DIMENSIONS || !shouldDoWrap || (shouldDoWrap && (child.getMeasuredHeight() == widget.getHeight())) || (child instanceof Placeholder) || widget.isResolvedHorizontally()) {
                                horizontalSpec2 = View.MeasureSpec.makeMeasureSpec(widget.getWidth(), 1073741824);
                            }
                        }
                    }
                    int i3 = AnonymousClass1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour[verticalBehavior.ordinal()];
                    if (i3 == 1) {
                        verticalSpec5 = View.MeasureSpec.makeMeasureSpec(verticalDimension, 1073741824);
                    } else if (i3 == 2) {
                        verticalSpec5 = ViewGroup.getChildMeasureSpec(this.layoutHeightSpec, heightPadding, -2);
                    } else if (i3 == 3) {
                        verticalSpec5 = ViewGroup.getChildMeasureSpec(this.layoutHeightSpec, widget.getVerticalMargin() + heightPadding, -1);
                    } else if (i3 == 4) {
                        verticalSpec5 = ViewGroup.getChildMeasureSpec(this.layoutHeightSpec, heightPadding, -2);
                        boolean shouldDoWrap2 = widget.mMatchConstraintDefaultHeight == 1;
                        if (measure.measureStrategy == BasicMeasure.Measure.TRY_GIVEN_DIMENSIONS || measure.measureStrategy == BasicMeasure.Measure.USE_GIVEN_DIMENSIONS) {
                            if (measure.measureStrategy == BasicMeasure.Measure.USE_GIVEN_DIMENSIONS || !shouldDoWrap2 || (shouldDoWrap2 && (child.getMeasuredWidth() == widget.getWidth())) || (child instanceof Placeholder) || widget.isResolvedVertically()) {
                                verticalSpec5 = View.MeasureSpec.makeMeasureSpec(widget.getHeight(), 1073741824);
                            }
                        }
                    }
                    ConstraintWidgetContainer container = (ConstraintWidgetContainer) widget.getParent();
                    if (container != null && Optimizer.enabled(ConstraintLayout.this.mOptimizationLevel, 256) && child.getMeasuredWidth() == widget.getWidth() && child.getMeasuredWidth() < container.getWidth() && child.getMeasuredHeight() == widget.getHeight() && child.getMeasuredHeight() < container.getHeight() && child.getBaseline() == widget.getBaselineDistance() && !widget.isMeasureRequested()) {
                        if (isSimilarSpec(widget.getLastHorizontalMeasureSpec(), horizontalSpec2, widget.getWidth()) && isSimilarSpec(widget.getLastVerticalMeasureSpec(), verticalSpec5, widget.getHeight())) {
                            measure.measuredWidth = widget.getWidth();
                            measure.measuredHeight = widget.getHeight();
                            measure.measuredBaseline = widget.getBaselineDistance();
                            return;
                        }
                    }
                    boolean horizontalMatchConstraints = horizontalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
                    boolean verticalMatchConstraints = verticalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
                    boolean verticalDimensionKnown = verticalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_PARENT || verticalBehavior == ConstraintWidget.DimensionBehaviour.FIXED;
                    boolean horizontalDimensionKnown = horizontalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_PARENT || horizontalBehavior == ConstraintWidget.DimensionBehaviour.FIXED;
                    if (horizontalMatchConstraints && widget.mDimensionRatio > 0.0f) {
                        horizontalUseRatio = true;
                        if (verticalMatchConstraints && widget.mDimensionRatio > 0.0f) {
                            verticalUseRatio = true;
                            if (child == null) {
                                LayoutParams params = (LayoutParams) child.getLayoutParams();
                                if (measure.measureStrategy == BasicMeasure.Measure.TRY_GIVEN_DIMENSIONS || measure.measureStrategy == BasicMeasure.Measure.USE_GIVEN_DIMENSIONS || !horizontalMatchConstraints || widget.mMatchConstraintDefaultWidth != 0 || !verticalMatchConstraints || widget.mMatchConstraintDefaultHeight != 0) {
                                    if (!(child instanceof VirtualLayout) || !(widget instanceof VirtualLayout)) {
                                        child.measure(horizontalSpec2, verticalSpec5);
                                    } else {
                                        ((VirtualLayout) child).onMeasure((VirtualLayout) widget, horizontalSpec2, verticalSpec5);
                                    }
                                    widget.setLastMeasureSpec(horizontalSpec2, verticalSpec5);
                                    int w = child.getMeasuredWidth();
                                    int h = child.getMeasuredHeight();
                                    int baseline = child.getBaseline();
                                    if (widget.mMatchConstraintMinWidth > 0) {
                                        verticalSpec3 = verticalSpec5;
                                        verticalSpec = Math.max(widget.mMatchConstraintMinWidth, w);
                                    } else {
                                        verticalSpec3 = verticalSpec5;
                                        verticalSpec = w;
                                    }
                                    if (widget.mMatchConstraintMaxWidth > 0) {
                                        verticalSpec = Math.min(widget.mMatchConstraintMaxWidth, verticalSpec);
                                    }
                                    if (widget.mMatchConstraintMinHeight > 0) {
                                        widthPadding = Math.max(widget.mMatchConstraintMinHeight, h);
                                    } else {
                                        widthPadding = h;
                                    }
                                    if (widget.mMatchConstraintMaxHeight > 0) {
                                        widthPadding = Math.min(widget.mMatchConstraintMaxHeight, widthPadding);
                                    }
                                    if (!Optimizer.enabled(ConstraintLayout.this.mOptimizationLevel, 1)) {
                                        if (horizontalUseRatio && verticalDimensionKnown) {
                                            verticalSpec = (int) ((((float) widthPadding) * widget.mDimensionRatio) + 0.5f);
                                        } else if (verticalUseRatio && horizontalDimensionKnown) {
                                            widthPadding = (int) ((((float) verticalSpec) / widget.mDimensionRatio) + 0.5f);
                                        }
                                    }
                                    if (w == verticalSpec && h == widthPadding) {
                                        verticalSpec2 = baseline;
                                    } else {
                                        if (w != verticalSpec) {
                                            i = 1073741824;
                                            horizontalSpec = View.MeasureSpec.makeMeasureSpec(verticalSpec, 1073741824);
                                        } else {
                                            i = 1073741824;
                                            horizontalSpec = horizontalSpec2;
                                        }
                                        if (h != widthPadding) {
                                            verticalSpec4 = View.MeasureSpec.makeMeasureSpec(widthPadding, i);
                                        } else {
                                            verticalSpec4 = verticalSpec3;
                                        }
                                        child.measure(horizontalSpec, verticalSpec4);
                                        widget.setLastMeasureSpec(horizontalSpec, verticalSpec4);
                                        verticalSpec = child.getMeasuredWidth();
                                        widthPadding = child.getMeasuredHeight();
                                        verticalSpec2 = child.getBaseline();
                                    }
                                } else {
                                    verticalSpec = 0;
                                    widthPadding = 0;
                                    verticalSpec2 = 0;
                                }
                                boolean hasBaseline = verticalSpec2 != -1;
                                measure.measuredNeedsSolverPass = (verticalSpec == measure.horizontalDimension && widthPadding == measure.verticalDimension) ? false : true;
                                if (params.needsBaseline) {
                                    hasBaseline = true;
                                }
                                if (!(!hasBaseline || verticalSpec2 == -1 || widget.getBaselineDistance() == verticalSpec2)) {
                                    measure.measuredNeedsSolverPass = true;
                                }
                                measure.measuredWidth = verticalSpec;
                                measure.measuredHeight = widthPadding;
                                measure.measuredHasBaseline = hasBaseline;
                                measure.measuredBaseline = verticalSpec2;
                                return;
                            }
                            return;
                        }
                        verticalUseRatio = false;
                        if (child == null) {
                        }
                    }
                    horizontalUseRatio = false;
                    if (verticalMatchConstraints) {
                        verticalUseRatio = true;
                        if (child == null) {
                        }
                    }
                    verticalUseRatio = false;
                    if (child == null) {
                    }
                }
            }
        }

        private boolean isSimilarSpec(int lastMeasureSpec, int spec, int widgetSize) {
            if (lastMeasureSpec == spec) {
                return true;
            }
            int lastMode = View.MeasureSpec.getMode(lastMeasureSpec);
            View.MeasureSpec.getSize(lastMeasureSpec);
            int mode = View.MeasureSpec.getMode(spec);
            int size = View.MeasureSpec.getSize(spec);
            if (mode != 1073741824) {
                return false;
            }
            if ((lastMode == Integer.MIN_VALUE || lastMode == 0) && widgetSize == size) {
                return true;
            }
            return false;
        }

        @Override // androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure.Measurer
        public final void didMeasures() {
            int widgetsCount = this.layout.getChildCount();
            for (int i = 0; i < widgetsCount; i++) {
                View child = this.layout.getChildAt(i);
                if (child instanceof Placeholder) {
                    ((Placeholder) child).updatePostMeasure(this.layout);
                }
            }
            int helperCount = this.layout.mConstraintHelpers.size();
            if (helperCount > 0) {
                for (int i2 = 0; i2 < helperCount; i2++) {
                    ((ConstraintHelper) this.layout.mConstraintHelpers.get(i2)).updatePostMeasure(this.layout);
                }
            }
        }
    }

    /* renamed from: androidx.constraintlayout.widget.ConstraintLayout$1 */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour = new int[ConstraintWidget.DimensionBehaviour.values().length];

        static {
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour[ConstraintWidget.DimensionBehaviour.FIXED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour[ConstraintWidget.DimensionBehaviour.WRAP_CONTENT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour[ConstraintWidget.DimensionBehaviour.MATCH_PARENT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour[ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this.mLayoutWidget.setCompanionWidget(this);
        this.mLayoutWidget.setMeasurer(this.mMeasurer);
        this.mChildrenByIds.put(getId(), this);
        this.mConstraintSet = null;
        if (attrs != null) {
            TypedArray a2 = getContext().obtainStyledAttributes(attrs, R.styleable.ConstraintLayout_Layout, defStyleAttr, defStyleRes);
            int N = a2.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a2.getIndex(i);
                if (attr == R.styleable.ConstraintLayout_Layout_android_minWidth) {
                    this.mMinWidth = a2.getDimensionPixelOffset(attr, this.mMinWidth);
                } else if (attr == R.styleable.ConstraintLayout_Layout_android_minHeight) {
                    this.mMinHeight = a2.getDimensionPixelOffset(attr, this.mMinHeight);
                } else if (attr == R.styleable.ConstraintLayout_Layout_android_maxWidth) {
                    this.mMaxWidth = a2.getDimensionPixelOffset(attr, this.mMaxWidth);
                } else if (attr == R.styleable.ConstraintLayout_Layout_android_maxHeight) {
                    this.mMaxHeight = a2.getDimensionPixelOffset(attr, this.mMaxHeight);
                } else if (attr == R.styleable.ConstraintLayout_Layout_layout_optimizationLevel) {
                    this.mOptimizationLevel = a2.getInt(attr, this.mOptimizationLevel);
                } else if (attr == R.styleable.ConstraintLayout_Layout_layoutDescription) {
                    int id = a2.getResourceId(attr, 0);
                    if (id != 0) {
                        try {
                            parseLayoutDescription(id);
                        } catch (Resources.NotFoundException e) {
                            this.mConstraintLayoutSpec = null;
                        }
                    }
                } else if (attr == R.styleable.ConstraintLayout_Layout_constraintSet) {
                    int id2 = a2.getResourceId(attr, 0);
                    try {
                        this.mConstraintSet = new ConstraintSet();
                        this.mConstraintSet.load(getContext(), id2);
                    } catch (Resources.NotFoundException e2) {
                        this.mConstraintSet = null;
                    }
                    this.mConstraintSetId = id2;
                }
            }
            a2.recycle();
        }
        this.mLayoutWidget.setOptimizationLevel(this.mOptimizationLevel);
    }

    protected void parseLayoutDescription(int id) {
        this.mConstraintLayoutSpec = new ConstraintLayoutStates(getContext(), this, id);
    }

    @Override // android.view.ViewGroup
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (Build.VERSION.SDK_INT < 14) {
            onViewAdded(child);
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void removeView(View view) {
        super.removeView(view);
        if (Build.VERSION.SDK_INT < 14) {
            onViewRemoved(view);
        }
    }

    @Override // android.view.ViewGroup
    public void onViewAdded(View view) {
        if (Build.VERSION.SDK_INT >= 14) {
            super.onViewAdded(view);
        }
        ConstraintWidget widget = getViewWidget(view);
        if ((view instanceof Guideline) && !(widget instanceof Guideline)) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            layoutParams.widget = new Guideline();
            layoutParams.isGuideline = true;
            ((Guideline) layoutParams.widget).setOrientation(layoutParams.orientation);
        }
        if (view instanceof ConstraintHelper) {
            ConstraintHelper helper = (ConstraintHelper) view;
            helper.validateParams();
            ((LayoutParams) view.getLayoutParams()).isHelper = true;
            if (!this.mConstraintHelpers.contains(helper)) {
                this.mConstraintHelpers.add(helper);
            }
        }
        this.mChildrenByIds.put(view.getId(), view);
        this.mDirtyHierarchy = true;
    }

    @Override // android.view.ViewGroup
    public void onViewRemoved(View view) {
        if (Build.VERSION.SDK_INT >= 14) {
            super.onViewRemoved(view);
        }
        this.mChildrenByIds.remove(view.getId());
        this.mLayoutWidget.remove(getViewWidget(view));
        this.mConstraintHelpers.remove(view);
        this.mDirtyHierarchy = true;
    }

    public void setMinWidth(int value) {
        if (value != this.mMinWidth) {
            this.mMinWidth = value;
            requestLayout();
        }
    }

    public void setMinHeight(int value) {
        if (value != this.mMinHeight) {
            this.mMinHeight = value;
            requestLayout();
        }
    }

    public int getMinWidth() {
        return this.mMinWidth;
    }

    public int getMinHeight() {
        return this.mMinHeight;
    }

    public void setMaxWidth(int value) {
        if (value != this.mMaxWidth) {
            this.mMaxWidth = value;
            requestLayout();
        }
    }

    public void setMaxHeight(int value) {
        if (value != this.mMaxHeight) {
            this.mMaxHeight = value;
            requestLayout();
        }
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    public int getMaxHeight() {
        return this.mMaxHeight;
    }

    private boolean updateHierarchy() {
        int count = getChildCount();
        boolean recompute = false;
        int i = 0;
        while (true) {
            if (i >= count) {
                break;
            } else if (getChildAt(i).isLayoutRequested()) {
                recompute = true;
                break;
            } else {
                i++;
            }
        }
        if (recompute) {
            setChildrenConstraints();
        }
        return recompute;
    }

    private void setChildrenConstraints() {
        boolean isInEditMode = isInEditMode();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            ConstraintWidget widget = getViewWidget(getChildAt(i));
            if (widget != null) {
                widget.reset();
            }
        }
        if (isInEditMode) {
            for (int i2 = 0; i2 < count; i2++) {
                View view = getChildAt(i2);
                try {
                    String IdAsString = getResources().getResourceName(view.getId());
                    setDesignInformation(0, IdAsString, Integer.valueOf(view.getId()));
                    int slashIndex = IdAsString.indexOf(47);
                    if (slashIndex != -1) {
                        IdAsString = IdAsString.substring(slashIndex + 1);
                    }
                    getTargetWidget(view.getId()).setDebugName(IdAsString);
                } catch (Resources.NotFoundException e) {
                }
            }
        }
        if (this.mConstraintSetId != -1) {
            for (int i3 = 0; i3 < count; i3++) {
                View child = getChildAt(i3);
                if (child.getId() == this.mConstraintSetId && (child instanceof Constraints)) {
                    this.mConstraintSet = ((Constraints) child).getConstraintSet();
                }
            }
        }
        ConstraintSet constraintSet = this.mConstraintSet;
        if (constraintSet != null) {
            constraintSet.applyToInternal(this, true);
        }
        this.mLayoutWidget.removeAllChildren();
        int helperCount = this.mConstraintHelpers.size();
        if (helperCount > 0) {
            for (int i4 = 0; i4 < helperCount; i4++) {
                this.mConstraintHelpers.get(i4).updatePreLayout(this);
            }
        }
        for (int i5 = 0; i5 < count; i5++) {
            View child2 = getChildAt(i5);
            if (child2 instanceof Placeholder) {
                ((Placeholder) child2).updatePreLayout(this);
            }
        }
        this.mTempMapIdToWidget.clear();
        this.mTempMapIdToWidget.put(0, this.mLayoutWidget);
        this.mTempMapIdToWidget.put(getId(), this.mLayoutWidget);
        for (int i6 = 0; i6 < count; i6++) {
            View child3 = getChildAt(i6);
            this.mTempMapIdToWidget.put(child3.getId(), getViewWidget(child3));
        }
        for (int i7 = 0; i7 < count; i7++) {
            View child4 = getChildAt(i7);
            ConstraintWidget widget2 = getViewWidget(child4);
            if (widget2 != null) {
                LayoutParams layoutParams = (LayoutParams) child4.getLayoutParams();
                this.mLayoutWidget.add(widget2);
                applyConstraintsFromLayoutParams(isInEditMode, child4, widget2, layoutParams, this.mTempMapIdToWidget);
            }
        }
    }

    /* JADX INFO: Multiple debug info for r7v14 int: [D('resolveGoneLeftMargin' int), D('resolveGoneRightMargin' int)] */
    public void applyConstraintsFromLayoutParams(boolean isInEditMode, View child, ConstraintWidget widget, LayoutParams layoutParams, SparseArray<ConstraintWidget> idToWidget) {
        int resolveGoneRightMargin;
        int resolveGoneLeftMargin;
        int resolveGoneRightMargin2;
        int resolvedLeftToLeft;
        int resolveGoneRightMargin3;
        float resolvedHorizontalBias;
        int resolvedRightToLeft;
        float resolvedHorizontalBias2;
        int resolvedRightToRight;
        ConstraintWidget target;
        ConstraintWidget target2;
        ConstraintWidget target3;
        ConstraintWidget target4;
        layoutParams.validate();
        layoutParams.helped = false;
        widget.setVisibility(child.getVisibility());
        if (layoutParams.isInPlaceholder) {
            widget.setInPlaceholder(true);
            widget.setVisibility(8);
        }
        widget.setCompanionWidget(child);
        if (child instanceof ConstraintHelper) {
            ((ConstraintHelper) child).resolveRtl(widget, this.mLayoutWidget.isRtl());
        }
        if (layoutParams.isGuideline) {
            Guideline guideline = (Guideline) widget;
            int resolvedGuideBegin = layoutParams.resolvedGuideBegin;
            int resolvedGuideEnd = layoutParams.resolvedGuideEnd;
            float resolvedGuidePercent = layoutParams.resolvedGuidePercent;
            if (Build.VERSION.SDK_INT < 17) {
                resolvedGuideBegin = layoutParams.guideBegin;
                resolvedGuideEnd = layoutParams.guideEnd;
                resolvedGuidePercent = layoutParams.guidePercent;
            }
            if (resolvedGuidePercent != -1.0f) {
                guideline.setGuidePercent(resolvedGuidePercent);
            } else if (resolvedGuideBegin != -1) {
                guideline.setGuideBegin(resolvedGuideBegin);
            } else if (resolvedGuideEnd != -1) {
                guideline.setGuideEnd(resolvedGuideEnd);
            }
        } else {
            int resolvedLeftToLeft2 = layoutParams.resolvedLeftToLeft;
            int resolvedLeftToRight = layoutParams.resolvedLeftToRight;
            int resolvedRightToLeft2 = layoutParams.resolvedRightToLeft;
            int resolvedRightToRight2 = layoutParams.resolvedRightToRight;
            int resolveGoneLeftMargin2 = layoutParams.resolveGoneLeftMargin;
            int resolveGoneRightMargin4 = layoutParams.resolveGoneRightMargin;
            float resolvedHorizontalBias3 = layoutParams.resolvedHorizontalBias;
            if (Build.VERSION.SDK_INT < 17) {
                int resolvedLeftToLeft3 = layoutParams.leftToLeft;
                int resolvedLeftToRight2 = layoutParams.leftToRight;
                int resolvedRightToLeft3 = layoutParams.rightToLeft;
                resolvedRightToRight2 = layoutParams.rightToRight;
                int resolveGoneLeftMargin3 = layoutParams.goneLeftMargin;
                int resolveGoneRightMargin5 = layoutParams.goneRightMargin;
                float resolvedHorizontalBias4 = layoutParams.horizontalBias;
                if (resolvedLeftToLeft3 == -1 && resolvedLeftToRight2 == -1) {
                    if (layoutParams.startToStart != -1) {
                        resolvedLeftToLeft3 = layoutParams.startToStart;
                    } else if (layoutParams.startToEnd != -1) {
                        resolvedLeftToRight2 = layoutParams.startToEnd;
                    }
                }
                if (resolvedRightToLeft3 == -1 && resolvedRightToRight2 == -1) {
                    if (layoutParams.endToStart != -1) {
                        resolvedLeftToLeft = resolvedLeftToLeft3;
                        resolveGoneRightMargin2 = resolvedLeftToRight2;
                        resolveGoneRightMargin = resolveGoneRightMargin5;
                        resolveGoneRightMargin3 = layoutParams.endToStart;
                        resolvedHorizontalBias = resolvedHorizontalBias4;
                        resolveGoneLeftMargin = resolveGoneLeftMargin3;
                    } else if (layoutParams.endToEnd != -1) {
                        resolvedRightToRight2 = layoutParams.endToEnd;
                        resolvedLeftToLeft = resolvedLeftToLeft3;
                        resolveGoneRightMargin2 = resolvedLeftToRight2;
                        resolveGoneRightMargin = resolveGoneRightMargin5;
                        resolveGoneRightMargin3 = resolvedRightToLeft3;
                        resolvedHorizontalBias = resolvedHorizontalBias4;
                        resolveGoneLeftMargin = resolveGoneLeftMargin3;
                    }
                }
                resolvedLeftToLeft = resolvedLeftToLeft3;
                resolveGoneRightMargin2 = resolvedLeftToRight2;
                resolveGoneRightMargin = resolveGoneRightMargin5;
                resolveGoneRightMargin3 = resolvedRightToLeft3;
                resolvedHorizontalBias = resolvedHorizontalBias4;
                resolveGoneLeftMargin = resolveGoneLeftMargin3;
            } else {
                resolvedLeftToLeft = resolvedLeftToLeft2;
                resolveGoneRightMargin = resolveGoneRightMargin4;
                resolveGoneRightMargin2 = resolvedLeftToRight;
                resolveGoneRightMargin3 = resolvedRightToLeft2;
                resolvedHorizontalBias = resolvedHorizontalBias3;
                resolveGoneLeftMargin = resolveGoneLeftMargin2;
            }
            if (layoutParams.circleConstraint != -1) {
                ConstraintWidget target5 = idToWidget.get(layoutParams.circleConstraint);
                if (target5 != null) {
                    widget.connectCircularConstraint(target5, layoutParams.circleAngle, layoutParams.circleRadius);
                }
            } else {
                if (resolvedLeftToLeft != -1) {
                    ConstraintWidget target6 = idToWidget.get(resolvedLeftToLeft);
                    if (target6 != null) {
                        resolvedHorizontalBias2 = resolvedHorizontalBias;
                        resolvedRightToRight = resolvedRightToRight2;
                        resolvedRightToLeft = resolveGoneRightMargin3;
                        widget.immediateConnect(ConstraintAnchor.Type.LEFT, target6, ConstraintAnchor.Type.LEFT, layoutParams.leftMargin, resolveGoneLeftMargin);
                    } else {
                        resolvedHorizontalBias2 = resolvedHorizontalBias;
                        resolvedRightToRight = resolvedRightToRight2;
                        resolvedRightToLeft = resolveGoneRightMargin3;
                    }
                } else {
                    resolvedHorizontalBias2 = resolvedHorizontalBias;
                    resolvedRightToRight = resolvedRightToRight2;
                    resolvedRightToLeft = resolveGoneRightMargin3;
                    if (!(resolveGoneRightMargin2 == -1 || (target4 = idToWidget.get(resolveGoneRightMargin2)) == null)) {
                        widget.immediateConnect(ConstraintAnchor.Type.LEFT, target4, ConstraintAnchor.Type.RIGHT, layoutParams.leftMargin, resolveGoneLeftMargin);
                    }
                }
                if (resolvedRightToLeft != -1) {
                    ConstraintWidget target7 = idToWidget.get(resolvedRightToLeft);
                    if (target7 != null) {
                        widget.immediateConnect(ConstraintAnchor.Type.RIGHT, target7, ConstraintAnchor.Type.LEFT, layoutParams.rightMargin, resolveGoneRightMargin);
                    }
                } else if (!(resolvedRightToRight == -1 || (target3 = idToWidget.get(resolvedRightToRight)) == null)) {
                    widget.immediateConnect(ConstraintAnchor.Type.RIGHT, target3, ConstraintAnchor.Type.RIGHT, layoutParams.rightMargin, resolveGoneRightMargin);
                }
                if (layoutParams.topToTop != -1) {
                    ConstraintWidget target8 = idToWidget.get(layoutParams.topToTop);
                    if (target8 != null) {
                        widget.immediateConnect(ConstraintAnchor.Type.TOP, target8, ConstraintAnchor.Type.TOP, layoutParams.topMargin, layoutParams.goneTopMargin);
                    }
                } else if (!(layoutParams.topToBottom == -1 || (target2 = idToWidget.get(layoutParams.topToBottom)) == null)) {
                    widget.immediateConnect(ConstraintAnchor.Type.TOP, target2, ConstraintAnchor.Type.BOTTOM, layoutParams.topMargin, layoutParams.goneTopMargin);
                }
                if (layoutParams.bottomToTop != -1) {
                    ConstraintWidget target9 = idToWidget.get(layoutParams.bottomToTop);
                    if (target9 != null) {
                        widget.immediateConnect(ConstraintAnchor.Type.BOTTOM, target9, ConstraintAnchor.Type.TOP, layoutParams.bottomMargin, layoutParams.goneBottomMargin);
                    }
                } else if (!(layoutParams.bottomToBottom == -1 || (target = idToWidget.get(layoutParams.bottomToBottom)) == null)) {
                    widget.immediateConnect(ConstraintAnchor.Type.BOTTOM, target, ConstraintAnchor.Type.BOTTOM, layoutParams.bottomMargin, layoutParams.goneBottomMargin);
                }
                if (layoutParams.baselineToBaseline != -1) {
                    View view = this.mChildrenByIds.get(layoutParams.baselineToBaseline);
                    ConstraintWidget target10 = idToWidget.get(layoutParams.baselineToBaseline);
                    if (!(target10 == null || view == null || !(view.getLayoutParams() instanceof LayoutParams))) {
                        LayoutParams targetParams = (LayoutParams) view.getLayoutParams();
                        layoutParams.needsBaseline = true;
                        targetParams.needsBaseline = true;
                        widget.getAnchor(ConstraintAnchor.Type.BASELINE).connect(target10.getAnchor(ConstraintAnchor.Type.BASELINE), 0, -1, true);
                        widget.setHasBaseline(true);
                        targetParams.widget.setHasBaseline(true);
                        widget.getAnchor(ConstraintAnchor.Type.TOP).reset();
                        widget.getAnchor(ConstraintAnchor.Type.BOTTOM).reset();
                    }
                }
                if (resolvedHorizontalBias2 >= 0.0f) {
                    widget.setHorizontalBiasPercent(resolvedHorizontalBias2);
                }
                if (layoutParams.verticalBias >= 0.0f) {
                    widget.setVerticalBiasPercent(layoutParams.verticalBias);
                }
            }
            if (isInEditMode && !(layoutParams.editorAbsoluteX == -1 && layoutParams.editorAbsoluteY == -1)) {
                widget.setOrigin(layoutParams.editorAbsoluteX, layoutParams.editorAbsoluteY);
            }
            if (layoutParams.horizontalDimensionFixed) {
                widget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                widget.setWidth(layoutParams.width);
                if (layoutParams.width == -2) {
                    widget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                }
            } else if (layoutParams.width == -1) {
                if (layoutParams.constrainedWidth) {
                    widget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                } else {
                    widget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                }
                widget.getAnchor(ConstraintAnchor.Type.LEFT).mMargin = layoutParams.leftMargin;
                widget.getAnchor(ConstraintAnchor.Type.RIGHT).mMargin = layoutParams.rightMargin;
            } else {
                widget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                widget.setWidth(0);
            }
            if (layoutParams.verticalDimensionFixed) {
                widget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                widget.setHeight(layoutParams.height);
                if (layoutParams.height == -2) {
                    widget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                }
            } else if (layoutParams.height == -1) {
                if (layoutParams.constrainedHeight) {
                    widget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                } else {
                    widget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                }
                widget.getAnchor(ConstraintAnchor.Type.TOP).mMargin = layoutParams.topMargin;
                widget.getAnchor(ConstraintAnchor.Type.BOTTOM).mMargin = layoutParams.bottomMargin;
            } else {
                widget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                widget.setHeight(0);
            }
            widget.setDimensionRatio(layoutParams.dimensionRatio);
            widget.setHorizontalWeight(layoutParams.horizontalWeight);
            widget.setVerticalWeight(layoutParams.verticalWeight);
            widget.setHorizontalChainStyle(layoutParams.horizontalChainStyle);
            widget.setVerticalChainStyle(layoutParams.verticalChainStyle);
            widget.setHorizontalMatchStyle(layoutParams.matchConstraintDefaultWidth, layoutParams.matchConstraintMinWidth, layoutParams.matchConstraintMaxWidth, layoutParams.matchConstraintPercentWidth);
            widget.setVerticalMatchStyle(layoutParams.matchConstraintDefaultHeight, layoutParams.matchConstraintMinHeight, layoutParams.matchConstraintMaxHeight, layoutParams.matchConstraintPercentHeight);
        }
    }

    private final ConstraintWidget getTargetWidget(int id) {
        if (id == 0) {
            return this.mLayoutWidget;
        }
        View view = this.mChildrenByIds.get(id);
        if (view == null && (view = findViewById(id)) != null && view != this && view.getParent() == this) {
            onViewAdded(view);
        }
        if (view == this) {
            return this.mLayoutWidget;
        }
        if (view == null) {
            return null;
        }
        return ((LayoutParams) view.getLayoutParams()).widget;
    }

    public final ConstraintWidget getViewWidget(View view) {
        if (view == this) {
            return this.mLayoutWidget;
        }
        if (view == null) {
            return null;
        }
        return ((LayoutParams) view.getLayoutParams()).widget;
    }

    public void fillMetrics(Metrics metrics) {
        this.mMetrics = metrics;
        this.mLayoutWidget.fillMetrics(metrics);
    }

    public void resolveSystem(ConstraintWidgetContainer layout, int optimizationLevel, int widthMeasureSpec, int heightMeasureSpec) {
        int paddingX;
        int paddingX2;
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int paddingY = Math.max(0, getPaddingTop());
        int paddingBottom = Math.max(0, getPaddingBottom());
        int paddingHeight = paddingY + paddingBottom;
        int paddingWidth = getPaddingWidth();
        this.mMeasurer.captureLayoutInfos(widthMeasureSpec, heightMeasureSpec, paddingY, paddingBottom, paddingWidth, paddingHeight);
        if (Build.VERSION.SDK_INT >= 17) {
            int paddingStart = Math.max(0, getPaddingStart());
            int paddingEnd = Math.max(0, getPaddingEnd());
            if (paddingStart <= 0 && paddingEnd <= 0) {
                paddingX2 = Math.max(0, getPaddingLeft());
            } else if (isRtl()) {
                paddingX2 = paddingEnd;
            } else {
                paddingX2 = paddingStart;
            }
            paddingX = paddingX2;
        } else {
            paddingX = Math.max(0, getPaddingLeft());
        }
        int widthSize2 = widthSize - paddingWidth;
        int heightSize2 = heightSize - paddingHeight;
        setSelfDimensionBehaviour(layout, widthMode, widthSize2, heightMode, heightSize2);
        layout.measure(optimizationLevel, widthMode, widthSize2, heightMode, heightSize2, this.mLastMeasureWidth, this.mLastMeasureHeight, paddingX, paddingY);
    }

    public void resolveMeasuredDimension(int widthMeasureSpec, int heightMeasureSpec, int measuredWidth, int measuredHeight, boolean isWidthMeasuredTooSmall, boolean isHeightMeasuredTooSmall) {
        int heightPadding = this.mMeasurer.paddingHeight;
        int androidLayoutWidth = measuredWidth + this.mMeasurer.paddingWidth;
        int androidLayoutHeight = measuredHeight + heightPadding;
        if (Build.VERSION.SDK_INT >= 11) {
            int resolvedWidthSize = resolveSizeAndState(androidLayoutWidth, widthMeasureSpec, 0);
            int resolvedHeightSize = resolveSizeAndState(androidLayoutHeight, heightMeasureSpec, 0 << 16);
            int resolvedWidthSize2 = resolvedWidthSize & ViewCompat.MEASURED_SIZE_MASK;
            int resolvedHeightSize2 = resolvedHeightSize & ViewCompat.MEASURED_SIZE_MASK;
            int resolvedWidthSize3 = Math.min(this.mMaxWidth, resolvedWidthSize2);
            int resolvedHeightSize3 = Math.min(this.mMaxHeight, resolvedHeightSize2);
            if (isWidthMeasuredTooSmall) {
                resolvedWidthSize3 |= 16777216;
            }
            if (isHeightMeasuredTooSmall) {
                resolvedHeightSize3 |= 16777216;
            }
            setMeasuredDimension(resolvedWidthSize3, resolvedHeightSize3);
            this.mLastMeasureWidth = resolvedWidthSize3;
            this.mLastMeasureHeight = resolvedHeightSize3;
            return;
        }
        setMeasuredDimension(androidLayoutWidth, androidLayoutHeight);
        this.mLastMeasureWidth = androidLayoutWidth;
        this.mLastMeasureHeight = androidLayoutHeight;
    }

    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!this.mDirtyHierarchy) {
            int count = getChildCount();
            int i = 0;
            while (true) {
                if (i >= count) {
                    break;
                } else if (getChildAt(i).isLayoutRequested()) {
                    this.mDirtyHierarchy = true;
                    break;
                } else {
                    i++;
                }
            }
        }
        if (!this.mDirtyHierarchy) {
            if (this.mOnMeasureWidthMeasureSpec == widthMeasureSpec && this.mOnMeasureHeightMeasureSpec == heightMeasureSpec) {
                resolveMeasuredDimension(widthMeasureSpec, heightMeasureSpec, this.mLayoutWidget.getWidth(), this.mLayoutWidget.getHeight(), this.mLayoutWidget.isWidthMeasuredTooSmall(), this.mLayoutWidget.isHeightMeasuredTooSmall());
                return;
            } else if (this.mOnMeasureWidthMeasureSpec == widthMeasureSpec && View.MeasureSpec.getMode(widthMeasureSpec) == 1073741824 && View.MeasureSpec.getMode(heightMeasureSpec) == Integer.MIN_VALUE && View.MeasureSpec.getMode(this.mOnMeasureHeightMeasureSpec) == Integer.MIN_VALUE && View.MeasureSpec.getSize(heightMeasureSpec) >= this.mLayoutWidget.getHeight()) {
                this.mOnMeasureWidthMeasureSpec = widthMeasureSpec;
                this.mOnMeasureHeightMeasureSpec = heightMeasureSpec;
                resolveMeasuredDimension(widthMeasureSpec, heightMeasureSpec, this.mLayoutWidget.getWidth(), this.mLayoutWidget.getHeight(), this.mLayoutWidget.isWidthMeasuredTooSmall(), this.mLayoutWidget.isHeightMeasuredTooSmall());
                return;
            }
        }
        this.mOnMeasureWidthMeasureSpec = widthMeasureSpec;
        this.mOnMeasureHeightMeasureSpec = heightMeasureSpec;
        this.mLayoutWidget.setRtl(isRtl());
        if (this.mDirtyHierarchy) {
            this.mDirtyHierarchy = false;
            if (updateHierarchy()) {
                this.mLayoutWidget.updateHierarchy();
            }
        }
        resolveSystem(this.mLayoutWidget, this.mOptimizationLevel, widthMeasureSpec, heightMeasureSpec);
        resolveMeasuredDimension(widthMeasureSpec, heightMeasureSpec, this.mLayoutWidget.getWidth(), this.mLayoutWidget.getHeight(), this.mLayoutWidget.isWidthMeasuredTooSmall(), this.mLayoutWidget.isHeightMeasuredTooSmall());
    }

    public boolean isRtl() {
        if (Build.VERSION.SDK_INT < 17) {
            return false;
        }
        if (!((getContext().getApplicationInfo().flags & 4194304) != 0) || 1 != getLayoutDirection()) {
            return false;
        }
        return true;
    }

    private int getPaddingWidth() {
        int widthPadding = Math.max(0, getPaddingLeft()) + Math.max(0, getPaddingRight());
        int rtlPadding = 0;
        if (Build.VERSION.SDK_INT >= 17) {
            rtlPadding = Math.max(0, getPaddingStart()) + Math.max(0, getPaddingEnd());
        }
        if (rtlPadding > 0) {
            return rtlPadding;
        }
        return widthPadding;
    }

    protected void setSelfDimensionBehaviour(ConstraintWidgetContainer layout, int widthMode, int widthSize, int heightMode, int heightSize) {
        int heightPadding = this.mMeasurer.paddingHeight;
        int widthPadding = this.mMeasurer.paddingWidth;
        ConstraintWidget.DimensionBehaviour widthBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
        ConstraintWidget.DimensionBehaviour heightBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
        int desiredWidth = 0;
        int desiredHeight = 0;
        int childCount = getChildCount();
        if (widthMode == Integer.MIN_VALUE) {
            widthBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            desiredWidth = widthSize;
            if (childCount == 0) {
                desiredWidth = Math.max(0, this.mMinWidth);
            }
        } else if (widthMode == 0) {
            widthBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            if (childCount == 0) {
                desiredWidth = Math.max(0, this.mMinWidth);
            }
        } else if (widthMode == 1073741824) {
            desiredWidth = Math.min(this.mMaxWidth - widthPadding, widthSize);
        }
        if (heightMode == Integer.MIN_VALUE) {
            heightBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            desiredHeight = heightSize;
            if (childCount == 0) {
                desiredHeight = Math.max(0, this.mMinHeight);
            }
        } else if (heightMode == 0) {
            heightBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            if (childCount == 0) {
                desiredHeight = Math.max(0, this.mMinHeight);
            }
        } else if (heightMode == 1073741824) {
            desiredHeight = Math.min(this.mMaxHeight - heightPadding, heightSize);
        }
        if (!(desiredWidth == layout.getWidth() && desiredHeight == layout.getHeight())) {
            layout.invalidateMeasures();
        }
        layout.setX(0);
        layout.setY(0);
        layout.setMaxWidth(this.mMaxWidth - widthPadding);
        layout.setMaxHeight(this.mMaxHeight - heightPadding);
        layout.setMinWidth(0);
        layout.setMinHeight(0);
        layout.setHorizontalDimensionBehaviour(widthBehaviour);
        layout.setWidth(desiredWidth);
        layout.setVerticalDimensionBehaviour(heightBehaviour);
        layout.setHeight(desiredHeight);
        layout.setMinWidth(this.mMinWidth - widthPadding);
        layout.setMinHeight(this.mMinHeight - heightPadding);
    }

    public void setState(int id, int screenWidth, int screenHeight) {
        ConstraintLayoutStates constraintLayoutStates = this.mConstraintLayoutSpec;
        if (constraintLayoutStates != null) {
            constraintLayoutStates.updateConstraints(id, (float) screenWidth, (float) screenHeight);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        View content;
        int widgetsCount = getChildCount();
        boolean isInEditMode = isInEditMode();
        for (int i = 0; i < widgetsCount; i++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            ConstraintWidget widget = params.widget;
            if ((child.getVisibility() != 8 || params.isGuideline || params.isHelper || params.isVirtualGroup || isInEditMode) && !params.isInPlaceholder) {
                int l = widget.getX();
                int t = widget.getY();
                int r = widget.getWidth() + l;
                int b = widget.getHeight() + t;
                child.layout(l, t, r, b);
                if ((child instanceof Placeholder) && (content = ((Placeholder) child).getContent()) != null) {
                    content.setVisibility(0);
                    content.layout(l, t, r, b);
                }
            }
        }
        int helperCount = this.mConstraintHelpers.size();
        if (helperCount > 0) {
            for (int i2 = 0; i2 < helperCount; i2++) {
                this.mConstraintHelpers.get(i2).updatePostLayout(this);
            }
        }
    }

    public void setOptimizationLevel(int level) {
        this.mOptimizationLevel = level;
        this.mLayoutWidget.setOptimizationLevel(level);
    }

    public int getOptimizationLevel() {
        return this.mLayoutWidget.getOptimizationLevel();
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public void setConstraintSet(ConstraintSet set) {
        this.mConstraintSet = set;
    }

    public View getViewById(int id) {
        return this.mChildrenByIds.get(id);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        float ch;
        float cw;
        int count;
        int helperCount;
        ConstraintLayout constraintLayout = this;
        ArrayList<ConstraintHelper> arrayList = constraintLayout.mConstraintHelpers;
        if (arrayList != null && (helperCount = arrayList.size()) > 0) {
            for (int i = 0; i < helperCount; i++) {
                constraintLayout.mConstraintHelpers.get(i).updatePreDraw(constraintLayout);
            }
        }
        super.dispatchDraw(canvas);
        if (isInEditMode()) {
            int count2 = getChildCount();
            float cw2 = (float) getWidth();
            float ch2 = (float) getHeight();
            int i2 = 0;
            while (i2 < count2) {
                View child = constraintLayout.getChildAt(i2);
                if (child.getVisibility() == 8) {
                    count = count2;
                    cw = cw2;
                    ch = ch2;
                } else {
                    Object tag = child.getTag();
                    if (tag == null || !(tag instanceof String)) {
                        count = count2;
                        cw = cw2;
                        ch = ch2;
                    } else {
                        String[] split = ((String) tag).split(",");
                        if (split.length == 4) {
                            int x = Integer.parseInt(split[0]);
                            int x2 = (int) ((((float) x) / 1080.0f) * cw2);
                            int y = (int) ((((float) Integer.parseInt(split[1])) / 1920.0f) * ch2);
                            int w = (int) ((((float) Integer.parseInt(split[2])) / 1080.0f) * cw2);
                            int h = (int) ((((float) Integer.parseInt(split[3])) / 1920.0f) * ch2);
                            Paint paint = new Paint();
                            paint.setColor(SupportMenu.CATEGORY_MASK);
                            count = count2;
                            cw = cw2;
                            ch = ch2;
                            canvas.drawLine((float) x2, (float) y, (float) (x2 + w), (float) y, paint);
                            canvas.drawLine((float) (x2 + w), (float) y, (float) (x2 + w), (float) (y + h), paint);
                            canvas.drawLine((float) (x2 + w), (float) (y + h), (float) x2, (float) (y + h), paint);
                            canvas.drawLine((float) x2, (float) (y + h), (float) x2, (float) y, paint);
                            paint.setColor(-16711936);
                            canvas.drawLine((float) x2, (float) y, (float) (x2 + w), (float) (y + h), paint);
                            canvas.drawLine((float) x2, (float) (y + h), (float) (x2 + w), (float) y, paint);
                        } else {
                            count = count2;
                            cw = cw2;
                            ch = ch2;
                        }
                    }
                }
                i2++;
                constraintLayout = this;
                count2 = count;
                cw2 = cw;
                ch2 = ch;
            }
        }
    }

    public void setOnConstraintsChanged(ConstraintsChangedListener constraintsChangedListener) {
        this.mConstraintsChangedListener = constraintsChangedListener;
        ConstraintLayoutStates constraintLayoutStates = this.mConstraintLayoutSpec;
        if (constraintLayoutStates != null) {
            constraintLayoutStates.setOnConstraintsChanged(constraintsChangedListener);
        }
    }

    public void loadLayoutDescription(int layoutDescription) {
        if (layoutDescription != 0) {
            try {
                this.mConstraintLayoutSpec = new ConstraintLayoutStates(getContext(), this, layoutDescription);
            } catch (Resources.NotFoundException e) {
                this.mConstraintLayoutSpec = null;
            }
        } else {
            this.mConstraintLayoutSpec = null;
        }
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public static final int BASELINE;
        public static final int BOTTOM;
        public static final int CHAIN_PACKED;
        public static final int CHAIN_SPREAD;
        public static final int CHAIN_SPREAD_INSIDE;
        public static final int END;
        public static final int HORIZONTAL;
        public static final int LEFT;
        public static final int MATCH_CONSTRAINT;
        public static final int MATCH_CONSTRAINT_PERCENT;
        public static final int MATCH_CONSTRAINT_SPREAD;
        public static final int MATCH_CONSTRAINT_WRAP;
        public static final int PARENT_ID;
        public static final int RIGHT;
        public static final int START;
        public static final int TOP;
        public static final int UNSET;
        public static final int VERTICAL;
        public int baselineToBaseline;
        public int bottomToBottom;
        public int bottomToTop;
        public float circleAngle;
        public int circleConstraint;
        public int circleRadius;
        public boolean constrainedHeight;
        public boolean constrainedWidth;
        public String constraintTag;
        public String dimensionRatio;
        int dimensionRatioSide;
        float dimensionRatioValue;
        public int editorAbsoluteX;
        public int editorAbsoluteY;
        public int endToEnd;
        public int endToStart;
        public int goneBottomMargin;
        public int goneEndMargin;
        public int goneLeftMargin;
        public int goneRightMargin;
        public int goneStartMargin;
        public int goneTopMargin;
        public int guideBegin;
        public int guideEnd;
        public float guidePercent;
        public boolean helped;
        public float horizontalBias;
        public int horizontalChainStyle;
        boolean horizontalDimensionFixed;
        public float horizontalWeight;
        boolean isGuideline;
        boolean isHelper;
        boolean isInPlaceholder;
        boolean isVirtualGroup;
        public int leftToLeft;
        public int leftToRight;
        public int matchConstraintDefaultHeight;
        public int matchConstraintDefaultWidth;
        public int matchConstraintMaxHeight;
        public int matchConstraintMaxWidth;
        public int matchConstraintMinHeight;
        public int matchConstraintMinWidth;
        public float matchConstraintPercentHeight;
        public float matchConstraintPercentWidth;
        boolean needsBaseline;
        public int orientation;
        int resolveGoneLeftMargin;
        int resolveGoneRightMargin;
        int resolvedGuideBegin;
        int resolvedGuideEnd;
        float resolvedGuidePercent;
        float resolvedHorizontalBias;
        int resolvedLeftToLeft;
        int resolvedLeftToRight;
        int resolvedRightToLeft;
        int resolvedRightToRight;
        public int rightToLeft;
        public int rightToRight;
        public int startToEnd;
        public int startToStart;
        public int topToBottom;
        public int topToTop;
        public float verticalBias;
        public int verticalChainStyle;
        boolean verticalDimensionFixed;
        public float verticalWeight;
        ConstraintWidget widget;

        public ConstraintWidget getConstraintWidget() {
            return this.widget;
        }

        public void setWidgetDebugName(String text) {
            this.widget.setDebugName(text);
        }

        public void reset() {
            ConstraintWidget constraintWidget = this.widget;
            if (constraintWidget != null) {
                constraintWidget.reset();
            }
        }

        public LayoutParams(LayoutParams source) {
            super((ViewGroup.MarginLayoutParams) source);
            this.guideBegin = -1;
            this.guideEnd = -1;
            this.guidePercent = -1.0f;
            this.leftToLeft = -1;
            this.leftToRight = -1;
            this.rightToLeft = -1;
            this.rightToRight = -1;
            this.topToTop = -1;
            this.topToBottom = -1;
            this.bottomToTop = -1;
            this.bottomToBottom = -1;
            this.baselineToBaseline = -1;
            this.circleConstraint = -1;
            this.circleRadius = 0;
            this.circleAngle = 0.0f;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneStartMargin = -1;
            this.goneEndMargin = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.dimensionRatioValue = 0.0f;
            this.dimensionRatioSide = 1;
            this.horizontalWeight = -1.0f;
            this.verticalWeight = -1.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.matchConstraintPercentWidth = 1.0f;
            this.matchConstraintPercentHeight = 1.0f;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.constrainedWidth = false;
            this.constrainedHeight = false;
            this.constraintTag = null;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.isHelper = false;
            this.isInPlaceholder = false;
            this.isVirtualGroup = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
            this.helped = false;
            this.guideBegin = source.guideBegin;
            this.guideEnd = source.guideEnd;
            this.guidePercent = source.guidePercent;
            this.leftToLeft = source.leftToLeft;
            this.leftToRight = source.leftToRight;
            this.rightToLeft = source.rightToLeft;
            this.rightToRight = source.rightToRight;
            this.topToTop = source.topToTop;
            this.topToBottom = source.topToBottom;
            this.bottomToTop = source.bottomToTop;
            this.bottomToBottom = source.bottomToBottom;
            this.baselineToBaseline = source.baselineToBaseline;
            this.circleConstraint = source.circleConstraint;
            this.circleRadius = source.circleRadius;
            this.circleAngle = source.circleAngle;
            this.startToEnd = source.startToEnd;
            this.startToStart = source.startToStart;
            this.endToStart = source.endToStart;
            this.endToEnd = source.endToEnd;
            this.goneLeftMargin = source.goneLeftMargin;
            this.goneTopMargin = source.goneTopMargin;
            this.goneRightMargin = source.goneRightMargin;
            this.goneBottomMargin = source.goneBottomMargin;
            this.goneStartMargin = source.goneStartMargin;
            this.goneEndMargin = source.goneEndMargin;
            this.horizontalBias = source.horizontalBias;
            this.verticalBias = source.verticalBias;
            this.dimensionRatio = source.dimensionRatio;
            this.dimensionRatioValue = source.dimensionRatioValue;
            this.dimensionRatioSide = source.dimensionRatioSide;
            this.horizontalWeight = source.horizontalWeight;
            this.verticalWeight = source.verticalWeight;
            this.horizontalChainStyle = source.horizontalChainStyle;
            this.verticalChainStyle = source.verticalChainStyle;
            this.constrainedWidth = source.constrainedWidth;
            this.constrainedHeight = source.constrainedHeight;
            this.matchConstraintDefaultWidth = source.matchConstraintDefaultWidth;
            this.matchConstraintDefaultHeight = source.matchConstraintDefaultHeight;
            this.matchConstraintMinWidth = source.matchConstraintMinWidth;
            this.matchConstraintMaxWidth = source.matchConstraintMaxWidth;
            this.matchConstraintMinHeight = source.matchConstraintMinHeight;
            this.matchConstraintMaxHeight = source.matchConstraintMaxHeight;
            this.matchConstraintPercentWidth = source.matchConstraintPercentWidth;
            this.matchConstraintPercentHeight = source.matchConstraintPercentHeight;
            this.editorAbsoluteX = source.editorAbsoluteX;
            this.editorAbsoluteY = source.editorAbsoluteY;
            this.orientation = source.orientation;
            this.horizontalDimensionFixed = source.horizontalDimensionFixed;
            this.verticalDimensionFixed = source.verticalDimensionFixed;
            this.needsBaseline = source.needsBaseline;
            this.isGuideline = source.isGuideline;
            this.resolvedLeftToLeft = source.resolvedLeftToLeft;
            this.resolvedLeftToRight = source.resolvedLeftToRight;
            this.resolvedRightToLeft = source.resolvedRightToLeft;
            this.resolvedRightToRight = source.resolvedRightToRight;
            this.resolveGoneLeftMargin = source.resolveGoneLeftMargin;
            this.resolveGoneRightMargin = source.resolveGoneRightMargin;
            this.resolvedHorizontalBias = source.resolvedHorizontalBias;
            this.constraintTag = source.constraintTag;
            this.widget = source.widget;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Table {
            public static final int ANDROID_ORIENTATION;
            public static final int LAYOUT_CONSTRAINED_HEIGHT;
            public static final int LAYOUT_CONSTRAINED_WIDTH;
            public static final int LAYOUT_CONSTRAINT_BASELINE_CREATOR;
            public static final int LAYOUT_CONSTRAINT_BASELINE_TO_BASELINE_OF;
            public static final int LAYOUT_CONSTRAINT_BOTTOM_CREATOR;
            public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_BOTTOM_OF;
            public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_TOP_OF;
            public static final int LAYOUT_CONSTRAINT_CIRCLE;
            public static final int LAYOUT_CONSTRAINT_CIRCLE_ANGLE;
            public static final int LAYOUT_CONSTRAINT_CIRCLE_RADIUS;
            public static final int LAYOUT_CONSTRAINT_DIMENSION_RATIO;
            public static final int LAYOUT_CONSTRAINT_END_TO_END_OF;
            public static final int LAYOUT_CONSTRAINT_END_TO_START_OF;
            public static final int LAYOUT_CONSTRAINT_GUIDE_BEGIN;
            public static final int LAYOUT_CONSTRAINT_GUIDE_END;
            public static final int LAYOUT_CONSTRAINT_GUIDE_PERCENT;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_DEFAULT;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_MAX;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_MIN;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_PERCENT;
            public static final int LAYOUT_CONSTRAINT_HORIZONTAL_BIAS;
            public static final int LAYOUT_CONSTRAINT_HORIZONTAL_CHAINSTYLE;
            public static final int LAYOUT_CONSTRAINT_HORIZONTAL_WEIGHT;
            public static final int LAYOUT_CONSTRAINT_LEFT_CREATOR;
            public static final int LAYOUT_CONSTRAINT_LEFT_TO_LEFT_OF;
            public static final int LAYOUT_CONSTRAINT_LEFT_TO_RIGHT_OF;
            public static final int LAYOUT_CONSTRAINT_RIGHT_CREATOR;
            public static final int LAYOUT_CONSTRAINT_RIGHT_TO_LEFT_OF;
            public static final int LAYOUT_CONSTRAINT_RIGHT_TO_RIGHT_OF;
            public static final int LAYOUT_CONSTRAINT_START_TO_END_OF;
            public static final int LAYOUT_CONSTRAINT_START_TO_START_OF;
            public static final int LAYOUT_CONSTRAINT_TAG;
            public static final int LAYOUT_CONSTRAINT_TOP_CREATOR;
            public static final int LAYOUT_CONSTRAINT_TOP_TO_BOTTOM_OF;
            public static final int LAYOUT_CONSTRAINT_TOP_TO_TOP_OF;
            public static final int LAYOUT_CONSTRAINT_VERTICAL_BIAS;
            public static final int LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE;
            public static final int LAYOUT_CONSTRAINT_VERTICAL_WEIGHT;
            public static final int LAYOUT_CONSTRAINT_WIDTH_DEFAULT;
            public static final int LAYOUT_CONSTRAINT_WIDTH_MAX;
            public static final int LAYOUT_CONSTRAINT_WIDTH_MIN;
            public static final int LAYOUT_CONSTRAINT_WIDTH_PERCENT;
            public static final int LAYOUT_EDITOR_ABSOLUTEX;
            public static final int LAYOUT_EDITOR_ABSOLUTEY;
            public static final int LAYOUT_GONE_MARGIN_BOTTOM;
            public static final int LAYOUT_GONE_MARGIN_END;
            public static final int LAYOUT_GONE_MARGIN_LEFT;
            public static final int LAYOUT_GONE_MARGIN_RIGHT;
            public static final int LAYOUT_GONE_MARGIN_START;
            public static final int LAYOUT_GONE_MARGIN_TOP;
            public static final int UNUSED;
            public static final SparseIntArray map = new SparseIntArray();

            private Table() {
            }

            static {
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
                map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
                map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
                map.append(R.styleable.ConstraintLayout_Layout_android_orientation, 1);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
                map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
                map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
                map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
                map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
                map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
                map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTag, 51);
            }
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            int value;
            int i;
            int commaIndex;
            int i2 = -1;
            this.guideBegin = -1;
            this.guideEnd = -1;
            this.guidePercent = -1.0f;
            this.leftToLeft = -1;
            this.leftToRight = -1;
            this.rightToLeft = -1;
            this.rightToRight = -1;
            this.topToTop = -1;
            this.topToBottom = -1;
            this.bottomToTop = -1;
            this.bottomToBottom = -1;
            this.baselineToBaseline = -1;
            this.circleConstraint = -1;
            int i3 = 0;
            this.circleRadius = 0;
            this.circleAngle = 0.0f;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneStartMargin = -1;
            this.goneEndMargin = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.dimensionRatioValue = 0.0f;
            this.dimensionRatioSide = 1;
            this.horizontalWeight = -1.0f;
            this.verticalWeight = -1.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.matchConstraintPercentWidth = 1.0f;
            this.matchConstraintPercentHeight = 1.0f;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.constrainedWidth = false;
            this.constrainedHeight = false;
            this.constraintTag = null;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.isHelper = false;
            this.isInPlaceholder = false;
            this.isVirtualGroup = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
            this.helped = false;
            TypedArray a2 = c.obtainStyledAttributes(attrs, R.styleable.ConstraintLayout_Layout);
            int N = a2.getIndexCount();
            int i4 = 0;
            while (i4 < N) {
                int attr = a2.getIndex(i4);
                switch (Table.map.get(attr)) {
                    case 0:
                        i = i2;
                        value = i3;
                        break;
                    case 1:
                        i = i2;
                        value = i3;
                        this.orientation = a2.getInt(attr, this.orientation);
                        break;
                    case 2:
                        value = i3;
                        this.circleConstraint = a2.getResourceId(attr, this.circleConstraint);
                        i = -1;
                        if (this.circleConstraint != -1) {
                            break;
                        } else {
                            this.circleConstraint = a2.getInt(attr, -1);
                            break;
                        }
                    case 3:
                        value = i3;
                        this.circleRadius = a2.getDimensionPixelSize(attr, this.circleRadius);
                        i = -1;
                        break;
                    case 4:
                        value = i3;
                        this.circleAngle = a2.getFloat(attr, this.circleAngle) % 360.0f;
                        float f = this.circleAngle;
                        if (f >= 0.0f) {
                            i = -1;
                            break;
                        } else {
                            this.circleAngle = (360.0f - f) % 360.0f;
                            i = -1;
                            break;
                        }
                    case 5:
                        value = i3;
                        this.guideBegin = a2.getDimensionPixelOffset(attr, this.guideBegin);
                        i = -1;
                        break;
                    case 6:
                        value = i3;
                        this.guideEnd = a2.getDimensionPixelOffset(attr, this.guideEnd);
                        i = -1;
                        break;
                    case 7:
                        value = i3;
                        this.guidePercent = a2.getFloat(attr, this.guidePercent);
                        i = -1;
                        break;
                    case 8:
                        value = i3;
                        this.leftToLeft = a2.getResourceId(attr, this.leftToLeft);
                        if (this.leftToLeft != i2) {
                            i = -1;
                            break;
                        } else {
                            this.leftToLeft = a2.getInt(attr, i2);
                            i = -1;
                            break;
                        }
                    case 9:
                        i = i2;
                        value = i3;
                        this.leftToRight = a2.getResourceId(attr, this.leftToRight);
                        if (this.leftToRight != i) {
                            break;
                        } else {
                            this.leftToRight = a2.getInt(attr, i);
                            break;
                        }
                    case 10:
                        i = i2;
                        value = i3;
                        this.rightToLeft = a2.getResourceId(attr, this.rightToLeft);
                        if (this.rightToLeft != i) {
                            break;
                        } else {
                            this.rightToLeft = a2.getInt(attr, i);
                            break;
                        }
                    case 11:
                        i = i2;
                        value = i3;
                        this.rightToRight = a2.getResourceId(attr, this.rightToRight);
                        if (this.rightToRight != i) {
                            break;
                        } else {
                            this.rightToRight = a2.getInt(attr, i);
                            break;
                        }
                    case 12:
                        i = i2;
                        value = i3;
                        this.topToTop = a2.getResourceId(attr, this.topToTop);
                        if (this.topToTop != i) {
                            break;
                        } else {
                            this.topToTop = a2.getInt(attr, i);
                            break;
                        }
                    case 13:
                        i = i2;
                        value = i3;
                        this.topToBottom = a2.getResourceId(attr, this.topToBottom);
                        if (this.topToBottom != i) {
                            break;
                        } else {
                            this.topToBottom = a2.getInt(attr, i);
                            break;
                        }
                    case 14:
                        i = i2;
                        value = i3;
                        this.bottomToTop = a2.getResourceId(attr, this.bottomToTop);
                        if (this.bottomToTop != i) {
                            break;
                        } else {
                            this.bottomToTop = a2.getInt(attr, i);
                            break;
                        }
                    case 15:
                        i = i2;
                        value = i3;
                        this.bottomToBottom = a2.getResourceId(attr, this.bottomToBottom);
                        if (this.bottomToBottom != i) {
                            break;
                        } else {
                            this.bottomToBottom = a2.getInt(attr, i);
                            break;
                        }
                    case 16:
                        i = i2;
                        value = i3;
                        this.baselineToBaseline = a2.getResourceId(attr, this.baselineToBaseline);
                        if (this.baselineToBaseline != i) {
                            break;
                        } else {
                            this.baselineToBaseline = a2.getInt(attr, i);
                            break;
                        }
                    case 17:
                        i = i2;
                        value = i3;
                        this.startToEnd = a2.getResourceId(attr, this.startToEnd);
                        if (this.startToEnd != i) {
                            break;
                        } else {
                            this.startToEnd = a2.getInt(attr, i);
                            break;
                        }
                    case 18:
                        i = i2;
                        value = i3;
                        this.startToStart = a2.getResourceId(attr, this.startToStart);
                        if (this.startToStart != i) {
                            break;
                        } else {
                            this.startToStart = a2.getInt(attr, i);
                            break;
                        }
                    case 19:
                        i = i2;
                        value = i3;
                        this.endToStart = a2.getResourceId(attr, this.endToStart);
                        if (this.endToStart != i) {
                            break;
                        } else {
                            this.endToStart = a2.getInt(attr, i);
                            break;
                        }
                    case 20:
                        value = i3;
                        this.endToEnd = a2.getResourceId(attr, this.endToEnd);
                        i = -1;
                        if (this.endToEnd != -1) {
                            break;
                        } else {
                            this.endToEnd = a2.getInt(attr, -1);
                            break;
                        }
                    case 21:
                        value = i3;
                        this.goneLeftMargin = a2.getDimensionPixelSize(attr, this.goneLeftMargin);
                        i = -1;
                        break;
                    case 22:
                        value = i3;
                        this.goneTopMargin = a2.getDimensionPixelSize(attr, this.goneTopMargin);
                        i = -1;
                        break;
                    case 23:
                        value = i3;
                        this.goneRightMargin = a2.getDimensionPixelSize(attr, this.goneRightMargin);
                        i = -1;
                        break;
                    case 24:
                        value = i3;
                        this.goneBottomMargin = a2.getDimensionPixelSize(attr, this.goneBottomMargin);
                        i = -1;
                        break;
                    case 25:
                        value = i3;
                        this.goneStartMargin = a2.getDimensionPixelSize(attr, this.goneStartMargin);
                        i = -1;
                        break;
                    case 26:
                        value = i3;
                        this.goneEndMargin = a2.getDimensionPixelSize(attr, this.goneEndMargin);
                        i = -1;
                        break;
                    case 27:
                        value = i3;
                        this.constrainedWidth = a2.getBoolean(attr, this.constrainedWidth);
                        i = -1;
                        break;
                    case 28:
                        value = i3;
                        this.constrainedHeight = a2.getBoolean(attr, this.constrainedHeight);
                        i = -1;
                        break;
                    case 29:
                        value = i3;
                        this.horizontalBias = a2.getFloat(attr, this.horizontalBias);
                        i = -1;
                        break;
                    case 30:
                        value = i3;
                        this.verticalBias = a2.getFloat(attr, this.verticalBias);
                        i = -1;
                        break;
                    case 31:
                        value = 0;
                        this.matchConstraintDefaultWidth = a2.getInt(attr, 0);
                        if (this.matchConstraintDefaultWidth != 1) {
                            i = -1;
                            break;
                        } else {
                            Log.e(ConstraintLayout.TAG, "layout_constraintWidth_default=\"wrap\" is deprecated.\nUse layout_width=\"WRAP_CONTENT\" and layout_constrainedWidth=\"true\" instead.");
                            i = -1;
                            break;
                        }
                    case 32:
                        this.matchConstraintDefaultHeight = a2.getInt(attr, 0);
                        if (this.matchConstraintDefaultHeight != 1) {
                            value = 0;
                            i = -1;
                            break;
                        } else {
                            Log.e(ConstraintLayout.TAG, "layout_constraintHeight_default=\"wrap\" is deprecated.\nUse layout_height=\"WRAP_CONTENT\" and layout_constrainedHeight=\"true\" instead.");
                            value = 0;
                            i = -1;
                            break;
                        }
                    case 33:
                        try {
                            this.matchConstraintMinWidth = a2.getDimensionPixelSize(attr, this.matchConstraintMinWidth);
                            value = 0;
                            i = -1;
                            break;
                        } catch (Exception e) {
                            if (a2.getInt(attr, this.matchConstraintMinWidth) == -2) {
                                this.matchConstraintMinWidth = -2;
                            }
                            value = 0;
                            i = -1;
                            break;
                        }
                    case 34:
                        try {
                            this.matchConstraintMaxWidth = a2.getDimensionPixelSize(attr, this.matchConstraintMaxWidth);
                            value = 0;
                            i = -1;
                            break;
                        } catch (Exception e2) {
                            if (a2.getInt(attr, this.matchConstraintMaxWidth) == -2) {
                                this.matchConstraintMaxWidth = -2;
                            }
                            value = 0;
                            i = -1;
                            break;
                        }
                    case 35:
                        this.matchConstraintPercentWidth = Math.max(0.0f, a2.getFloat(attr, this.matchConstraintPercentWidth));
                        this.matchConstraintDefaultWidth = 2;
                        value = 0;
                        i = -1;
                        break;
                    case 36:
                        try {
                            this.matchConstraintMinHeight = a2.getDimensionPixelSize(attr, this.matchConstraintMinHeight);
                            value = 0;
                            i = -1;
                            break;
                        } catch (Exception e3) {
                            if (a2.getInt(attr, this.matchConstraintMinHeight) == -2) {
                                this.matchConstraintMinHeight = -2;
                            }
                            value = 0;
                            i = -1;
                            break;
                        }
                    case 37:
                        try {
                            this.matchConstraintMaxHeight = a2.getDimensionPixelSize(attr, this.matchConstraintMaxHeight);
                            value = 0;
                            i = -1;
                            break;
                        } catch (Exception e4) {
                            if (a2.getInt(attr, this.matchConstraintMaxHeight) == -2) {
                                this.matchConstraintMaxHeight = -2;
                            }
                            value = 0;
                            i = -1;
                            break;
                        }
                    case 38:
                        this.matchConstraintPercentHeight = Math.max(0.0f, a2.getFloat(attr, this.matchConstraintPercentHeight));
                        this.matchConstraintDefaultHeight = 2;
                        value = 0;
                        i = -1;
                        break;
                    case 39:
                        value = 0;
                        i = -1;
                        break;
                    case 40:
                        value = 0;
                        i = -1;
                        break;
                    case 41:
                        value = 0;
                        i = -1;
                        break;
                    case 42:
                        value = 0;
                        i = -1;
                        break;
                    case 43:
                    default:
                        i = i2;
                        value = i3;
                        break;
                    case 44:
                        this.dimensionRatio = a2.getString(attr);
                        this.dimensionRatioValue = Float.NaN;
                        this.dimensionRatioSide = i2;
                        String str = this.dimensionRatio;
                        if (str == null) {
                            value = 0;
                            i = -1;
                            break;
                        } else {
                            int len = str.length();
                            int commaIndex2 = this.dimensionRatio.indexOf(44);
                            if (commaIndex2 <= 0 || commaIndex2 >= len - 1) {
                                commaIndex = 0;
                            } else {
                                String dimension = this.dimensionRatio.substring(i3, commaIndex2);
                                if (dimension.equalsIgnoreCase(ExifInterface.LONGITUDE_WEST)) {
                                    this.dimensionRatioSide = i3;
                                } else if (dimension.equalsIgnoreCase("H")) {
                                    this.dimensionRatioSide = 1;
                                }
                                commaIndex = commaIndex2 + 1;
                            }
                            int colonIndex = this.dimensionRatio.indexOf(58);
                            if (colonIndex < 0 || colonIndex >= len - 1) {
                                String r = this.dimensionRatio.substring(commaIndex);
                                if (r.length() > 0) {
                                    try {
                                        this.dimensionRatioValue = Float.parseFloat(r);
                                    } catch (NumberFormatException e5) {
                                    }
                                }
                            } else {
                                String nominator = this.dimensionRatio.substring(commaIndex, colonIndex);
                                String denominator = this.dimensionRatio.substring(colonIndex + 1);
                                if (nominator.length() > 0 && denominator.length() > 0) {
                                    try {
                                        float nominatorValue = Float.parseFloat(nominator);
                                        float denominatorValue = Float.parseFloat(denominator);
                                        if (nominatorValue > 0.0f && denominatorValue > 0.0f) {
                                            if (this.dimensionRatioSide == 1) {
                                                this.dimensionRatioValue = Math.abs(denominatorValue / nominatorValue);
                                            } else {
                                                this.dimensionRatioValue = Math.abs(nominatorValue / denominatorValue);
                                            }
                                        }
                                    } catch (NumberFormatException e6) {
                                    }
                                }
                            }
                            value = 0;
                            i = -1;
                            break;
                        }
                        break;
                    case 45:
                        this.horizontalWeight = a2.getFloat(attr, this.horizontalWeight);
                        i = i2;
                        value = i3;
                        break;
                    case 46:
                        this.verticalWeight = a2.getFloat(attr, this.verticalWeight);
                        i = i2;
                        value = i3;
                        break;
                    case 47:
                        this.horizontalChainStyle = a2.getInt(attr, i3);
                        i = i2;
                        value = i3;
                        break;
                    case 48:
                        this.verticalChainStyle = a2.getInt(attr, i3);
                        i = i2;
                        value = i3;
                        break;
                    case 49:
                        this.editorAbsoluteX = a2.getDimensionPixelOffset(attr, this.editorAbsoluteX);
                        i = i2;
                        value = i3;
                        break;
                    case 50:
                        this.editorAbsoluteY = a2.getDimensionPixelOffset(attr, this.editorAbsoluteY);
                        i = i2;
                        value = i3;
                        break;
                    case 51:
                        this.constraintTag = a2.getString(attr);
                        i = i2;
                        value = i3;
                        break;
                }
                i4++;
                i3 = value;
                i2 = i;
            }
            a2.recycle();
            validate();
        }

        public void validate() {
            this.isGuideline = false;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            if (this.width == -2 && this.constrainedWidth) {
                this.horizontalDimensionFixed = false;
                if (this.matchConstraintDefaultWidth == 0) {
                    this.matchConstraintDefaultWidth = 1;
                }
            }
            if (this.height == -2 && this.constrainedHeight) {
                this.verticalDimensionFixed = false;
                if (this.matchConstraintDefaultHeight == 0) {
                    this.matchConstraintDefaultHeight = 1;
                }
            }
            if (this.width == 0 || this.width == -1) {
                this.horizontalDimensionFixed = false;
                if (this.width == 0 && this.matchConstraintDefaultWidth == 1) {
                    this.width = -2;
                    this.constrainedWidth = true;
                }
            }
            if (this.height == 0 || this.height == -1) {
                this.verticalDimensionFixed = false;
                if (this.height == 0 && this.matchConstraintDefaultHeight == 1) {
                    this.height = -2;
                    this.constrainedHeight = true;
                }
            }
            if (this.guidePercent != -1.0f || this.guideBegin != -1 || this.guideEnd != -1) {
                this.isGuideline = true;
                this.horizontalDimensionFixed = true;
                this.verticalDimensionFixed = true;
                if (!(this.widget instanceof Guideline)) {
                    this.widget = new Guideline();
                }
                ((Guideline) this.widget).setOrientation(this.orientation);
            }
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.guideBegin = -1;
            this.guideEnd = -1;
            this.guidePercent = -1.0f;
            this.leftToLeft = -1;
            this.leftToRight = -1;
            this.rightToLeft = -1;
            this.rightToRight = -1;
            this.topToTop = -1;
            this.topToBottom = -1;
            this.bottomToTop = -1;
            this.bottomToBottom = -1;
            this.baselineToBaseline = -1;
            this.circleConstraint = -1;
            this.circleRadius = 0;
            this.circleAngle = 0.0f;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneStartMargin = -1;
            this.goneEndMargin = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.dimensionRatioValue = 0.0f;
            this.dimensionRatioSide = 1;
            this.horizontalWeight = -1.0f;
            this.verticalWeight = -1.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.matchConstraintPercentWidth = 1.0f;
            this.matchConstraintPercentHeight = 1.0f;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.constrainedWidth = false;
            this.constrainedHeight = false;
            this.constraintTag = null;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.isHelper = false;
            this.isInPlaceholder = false;
            this.isVirtualGroup = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
            this.helped = false;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            this.guideBegin = -1;
            this.guideEnd = -1;
            this.guidePercent = -1.0f;
            this.leftToLeft = -1;
            this.leftToRight = -1;
            this.rightToLeft = -1;
            this.rightToRight = -1;
            this.topToTop = -1;
            this.topToBottom = -1;
            this.bottomToTop = -1;
            this.bottomToBottom = -1;
            this.baselineToBaseline = -1;
            this.circleConstraint = -1;
            this.circleRadius = 0;
            this.circleAngle = 0.0f;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneStartMargin = -1;
            this.goneEndMargin = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.dimensionRatioValue = 0.0f;
            this.dimensionRatioSide = 1;
            this.horizontalWeight = -1.0f;
            this.verticalWeight = -1.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.matchConstraintPercentWidth = 1.0f;
            this.matchConstraintPercentHeight = 1.0f;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.constrainedWidth = false;
            this.constrainedHeight = false;
            this.constraintTag = null;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.isHelper = false;
            this.isInPlaceholder = false;
            this.isVirtualGroup = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
            this.helped = false;
        }

        @Override // android.view.ViewGroup.MarginLayoutParams, android.view.ViewGroup.LayoutParams
        public void resolveLayoutDirection(int layoutDirection) {
            int preLeftMargin = this.leftMargin;
            int preRightMargin = this.rightMargin;
            boolean isRtl = false;
            if (Build.VERSION.SDK_INT >= 17) {
                super.resolveLayoutDirection(layoutDirection);
                isRtl = 1 == getLayoutDirection();
            }
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolveGoneLeftMargin = this.goneLeftMargin;
            this.resolveGoneRightMargin = this.goneRightMargin;
            this.resolvedHorizontalBias = this.horizontalBias;
            this.resolvedGuideBegin = this.guideBegin;
            this.resolvedGuideEnd = this.guideEnd;
            this.resolvedGuidePercent = this.guidePercent;
            if (isRtl) {
                boolean startEndDefined = false;
                int i = this.startToEnd;
                if (i != -1) {
                    this.resolvedRightToLeft = i;
                    startEndDefined = true;
                } else {
                    int i2 = this.startToStart;
                    if (i2 != -1) {
                        this.resolvedRightToRight = i2;
                        startEndDefined = true;
                    }
                }
                int i3 = this.endToStart;
                if (i3 != -1) {
                    this.resolvedLeftToRight = i3;
                    startEndDefined = true;
                }
                int i4 = this.endToEnd;
                if (i4 != -1) {
                    this.resolvedLeftToLeft = i4;
                    startEndDefined = true;
                }
                int i5 = this.goneStartMargin;
                if (i5 != -1) {
                    this.resolveGoneRightMargin = i5;
                }
                int i6 = this.goneEndMargin;
                if (i6 != -1) {
                    this.resolveGoneLeftMargin = i6;
                }
                if (startEndDefined) {
                    this.resolvedHorizontalBias = 1.0f - this.horizontalBias;
                }
                if (this.isGuideline && this.orientation == 1) {
                    float f = this.guidePercent;
                    if (f != -1.0f) {
                        this.resolvedGuidePercent = 1.0f - f;
                        this.resolvedGuideBegin = -1;
                        this.resolvedGuideEnd = -1;
                    } else {
                        int i7 = this.guideBegin;
                        if (i7 != -1) {
                            this.resolvedGuideEnd = i7;
                            this.resolvedGuideBegin = -1;
                            this.resolvedGuidePercent = -1.0f;
                        } else {
                            int i8 = this.guideEnd;
                            if (i8 != -1) {
                                this.resolvedGuideBegin = i8;
                                this.resolvedGuideEnd = -1;
                                this.resolvedGuidePercent = -1.0f;
                            }
                        }
                    }
                }
            } else {
                int i9 = this.startToEnd;
                if (i9 != -1) {
                    this.resolvedLeftToRight = i9;
                }
                int i10 = this.startToStart;
                if (i10 != -1) {
                    this.resolvedLeftToLeft = i10;
                }
                int i11 = this.endToStart;
                if (i11 != -1) {
                    this.resolvedRightToLeft = i11;
                }
                int i12 = this.endToEnd;
                if (i12 != -1) {
                    this.resolvedRightToRight = i12;
                }
                int i13 = this.goneStartMargin;
                if (i13 != -1) {
                    this.resolveGoneLeftMargin = i13;
                }
                int i14 = this.goneEndMargin;
                if (i14 != -1) {
                    this.resolveGoneRightMargin = i14;
                }
            }
            if (this.endToStart == -1 && this.endToEnd == -1 && this.startToStart == -1 && this.startToEnd == -1) {
                int i15 = this.rightToLeft;
                if (i15 != -1) {
                    this.resolvedRightToLeft = i15;
                    if (this.rightMargin <= 0 && preRightMargin > 0) {
                        this.rightMargin = preRightMargin;
                    }
                } else {
                    int i16 = this.rightToRight;
                    if (i16 != -1) {
                        this.resolvedRightToRight = i16;
                        if (this.rightMargin <= 0 && preRightMargin > 0) {
                            this.rightMargin = preRightMargin;
                        }
                    }
                }
                int i17 = this.leftToLeft;
                if (i17 != -1) {
                    this.resolvedLeftToLeft = i17;
                    if (this.leftMargin <= 0 && preLeftMargin > 0) {
                        this.leftMargin = preLeftMargin;
                        return;
                    }
                    return;
                }
                int i18 = this.leftToRight;
                if (i18 != -1) {
                    this.resolvedLeftToRight = i18;
                    if (this.leftMargin <= 0 && preLeftMargin > 0) {
                        this.leftMargin = preLeftMargin;
                    }
                }
            }
        }

        public String getConstraintTag() {
            return this.constraintTag;
        }
    }

    @Override // android.view.ViewParent, android.view.View
    public void requestLayout() {
        markHierarchyDirty();
        super.requestLayout();
    }

    @Override // android.view.View
    public void forceLayout() {
        markHierarchyDirty();
        super.forceLayout();
    }

    private void markHierarchyDirty() {
        this.mDirtyHierarchy = true;
        this.mLastMeasureWidth = -1;
        this.mLastMeasureHeight = -1;
        this.mLastMeasureWidthSize = -1;
        this.mLastMeasureHeightSize = -1;
        this.mLastMeasureWidthMode = 0;
        this.mLastMeasureHeightMode = 0;
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }
}
