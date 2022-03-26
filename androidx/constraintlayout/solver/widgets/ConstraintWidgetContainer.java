package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.Metrics;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyGraph;
import androidx.constraintlayout.solver.widgets.analyzer.Direct;
import androidx.constraintlayout.solver.widgets.analyzer.Grouping;
import java.io.PrintStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class ConstraintWidgetContainer extends WidgetContainer {
    private static final boolean DEBUG;
    static final boolean DEBUG_GRAPH;
    private static final boolean DEBUG_LAYOUT;
    private static final int MAX_ITERATIONS;
    static int mycounter = 0;
    private WeakReference<ConstraintAnchor> horizontalWrapMax;
    private WeakReference<ConstraintAnchor> horizontalWrapMin;
    BasicMeasure mBasicMeasureSolver;
    int mDebugSolverPassCount;
    public DependencyGraph mDependencyGraph;
    public boolean mGroupsWrapOptimized;
    private boolean mHeightMeasuredTooSmall;
    ChainHead[] mHorizontalChainsArray;
    public int mHorizontalChainsSize;
    public boolean mHorizontalWrapOptimized;
    private boolean mIsRtl;
    public BasicMeasure.Measure mMeasure;
    protected BasicMeasure.Measurer mMeasurer;
    public Metrics mMetrics;
    private int mOptimizationLevel;
    int mPaddingBottom;
    int mPaddingLeft;
    int mPaddingRight;
    int mPaddingTop;
    public boolean mSkipSolver;
    protected LinearSystem mSystem;
    ChainHead[] mVerticalChainsArray;
    public int mVerticalChainsSize;
    public boolean mVerticalWrapOptimized;
    private boolean mWidthMeasuredTooSmall;
    public int mWrapFixedHeight;
    public int mWrapFixedWidth;
    private WeakReference<ConstraintAnchor> verticalWrapMax;
    private WeakReference<ConstraintAnchor> verticalWrapMin;

    public void invalidateGraph() {
        this.mDependencyGraph.invalidateGraph();
    }

    public void invalidateMeasures() {
        this.mDependencyGraph.invalidateMeasures();
    }

    public boolean directMeasure(boolean optimizeWrap) {
        return this.mDependencyGraph.directMeasure(optimizeWrap);
    }

    public boolean directMeasureSetup(boolean optimizeWrap) {
        return this.mDependencyGraph.directMeasureSetup(optimizeWrap);
    }

    public boolean directMeasureWithOrientation(boolean optimizeWrap, int orientation) {
        return this.mDependencyGraph.directMeasureWithOrientation(optimizeWrap, orientation);
    }

    public void defineTerminalWidgets() {
        this.mDependencyGraph.defineTerminalWidgets(getHorizontalDimensionBehaviour(), getVerticalDimensionBehaviour());
    }

    public long measure(int optimizationLevel, int widthMode, int widthSize, int heightMode, int heightSize, int lastMeasureWidth, int lastMeasureHeight, int paddingX, int paddingY) {
        this.mPaddingLeft = paddingX;
        this.mPaddingTop = paddingY;
        return this.mBasicMeasureSolver.solverMeasure(this, optimizationLevel, paddingX, paddingY, widthMode, widthSize, heightMode, heightSize, lastMeasureWidth, lastMeasureHeight);
    }

    public void updateHierarchy() {
        this.mBasicMeasureSolver.updateHierarchy(this);
    }

    public void setMeasurer(BasicMeasure.Measurer measurer) {
        this.mMeasurer = measurer;
        this.mDependencyGraph.setMeasurer(measurer);
    }

    public BasicMeasure.Measurer getMeasurer() {
        return this.mMeasurer;
    }

    public void fillMetrics(Metrics metrics) {
        this.mMetrics = metrics;
        this.mSystem.fillMetrics(metrics);
    }

    public ConstraintWidgetContainer() {
        this.mBasicMeasureSolver = new BasicMeasure(this);
        this.mDependencyGraph = new DependencyGraph(this);
        this.mMeasurer = null;
        this.mIsRtl = false;
        this.mSystem = new LinearSystem();
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mVerticalChainsArray = new ChainHead[4];
        this.mHorizontalChainsArray = new ChainHead[4];
        this.mGroupsWrapOptimized = false;
        this.mHorizontalWrapOptimized = false;
        this.mVerticalWrapOptimized = false;
        this.mWrapFixedWidth = 0;
        this.mWrapFixedHeight = 0;
        this.mOptimizationLevel = 257;
        this.mSkipSolver = false;
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
        this.mDebugSolverPassCount = 0;
        this.verticalWrapMin = null;
        this.horizontalWrapMin = null;
        this.verticalWrapMax = null;
        this.horizontalWrapMax = null;
        this.mMeasure = new BasicMeasure.Measure();
    }

    public ConstraintWidgetContainer(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.mBasicMeasureSolver = new BasicMeasure(this);
        this.mDependencyGraph = new DependencyGraph(this);
        this.mMeasurer = null;
        this.mIsRtl = false;
        this.mSystem = new LinearSystem();
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mVerticalChainsArray = new ChainHead[4];
        this.mHorizontalChainsArray = new ChainHead[4];
        this.mGroupsWrapOptimized = false;
        this.mHorizontalWrapOptimized = false;
        this.mVerticalWrapOptimized = false;
        this.mWrapFixedWidth = 0;
        this.mWrapFixedHeight = 0;
        this.mOptimizationLevel = 257;
        this.mSkipSolver = false;
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
        this.mDebugSolverPassCount = 0;
        this.verticalWrapMin = null;
        this.horizontalWrapMin = null;
        this.verticalWrapMax = null;
        this.horizontalWrapMax = null;
        this.mMeasure = new BasicMeasure.Measure();
    }

    public ConstraintWidgetContainer(int width, int height) {
        super(width, height);
        this.mBasicMeasureSolver = new BasicMeasure(this);
        this.mDependencyGraph = new DependencyGraph(this);
        this.mMeasurer = null;
        this.mIsRtl = false;
        this.mSystem = new LinearSystem();
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mVerticalChainsArray = new ChainHead[4];
        this.mHorizontalChainsArray = new ChainHead[4];
        this.mGroupsWrapOptimized = false;
        this.mHorizontalWrapOptimized = false;
        this.mVerticalWrapOptimized = false;
        this.mWrapFixedWidth = 0;
        this.mWrapFixedHeight = 0;
        this.mOptimizationLevel = 257;
        this.mSkipSolver = false;
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
        this.mDebugSolverPassCount = 0;
        this.verticalWrapMin = null;
        this.horizontalWrapMin = null;
        this.verticalWrapMax = null;
        this.horizontalWrapMax = null;
        this.mMeasure = new BasicMeasure.Measure();
    }

    public ConstraintWidgetContainer(String debugName, int width, int height) {
        super(width, height);
        this.mBasicMeasureSolver = new BasicMeasure(this);
        this.mDependencyGraph = new DependencyGraph(this);
        this.mMeasurer = null;
        this.mIsRtl = false;
        this.mSystem = new LinearSystem();
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mVerticalChainsArray = new ChainHead[4];
        this.mHorizontalChainsArray = new ChainHead[4];
        this.mGroupsWrapOptimized = false;
        this.mHorizontalWrapOptimized = false;
        this.mVerticalWrapOptimized = false;
        this.mWrapFixedWidth = 0;
        this.mWrapFixedHeight = 0;
        this.mOptimizationLevel = 257;
        this.mSkipSolver = false;
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
        this.mDebugSolverPassCount = 0;
        this.verticalWrapMin = null;
        this.horizontalWrapMin = null;
        this.verticalWrapMax = null;
        this.horizontalWrapMax = null;
        this.mMeasure = new BasicMeasure.Measure();
        setDebugName(debugName);
    }

    public void setOptimizationLevel(int value) {
        this.mOptimizationLevel = value;
        LinearSystem linearSystem = this.mSystem;
        LinearSystem.USE_DEPENDENCY_ORDERING = optimizeFor(512);
    }

    public int getOptimizationLevel() {
        return this.mOptimizationLevel;
    }

    public boolean optimizeFor(int feature) {
        return (this.mOptimizationLevel & feature) == feature;
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public String getType() {
        return "ConstraintLayout";
    }

    @Override // androidx.constraintlayout.solver.widgets.WidgetContainer, androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void reset() {
        this.mSystem.reset();
        this.mPaddingLeft = 0;
        this.mPaddingRight = 0;
        this.mPaddingTop = 0;
        this.mPaddingBottom = 0;
        this.mSkipSolver = false;
        super.reset();
    }

    public boolean isWidthMeasuredTooSmall() {
        return this.mWidthMeasuredTooSmall;
    }

    public boolean isHeightMeasuredTooSmall() {
        return this.mHeightMeasuredTooSmall;
    }

    public void addVerticalWrapMinVariable(ConstraintAnchor top) {
        WeakReference<ConstraintAnchor> weakReference = this.verticalWrapMin;
        if (weakReference == null || weakReference.get() == null || top.getFinalValue() > this.verticalWrapMin.get().getFinalValue()) {
            this.verticalWrapMin = new WeakReference<>(top);
        }
    }

    public void addHorizontalWrapMinVariable(ConstraintAnchor left) {
        WeakReference<ConstraintAnchor> weakReference = this.horizontalWrapMin;
        if (weakReference == null || weakReference.get() == null || left.getFinalValue() > this.horizontalWrapMin.get().getFinalValue()) {
            this.horizontalWrapMin = new WeakReference<>(left);
        }
    }

    public void addVerticalWrapMaxVariable(ConstraintAnchor bottom) {
        WeakReference<ConstraintAnchor> weakReference = this.verticalWrapMax;
        if (weakReference == null || weakReference.get() == null || bottom.getFinalValue() > this.verticalWrapMax.get().getFinalValue()) {
            this.verticalWrapMax = new WeakReference<>(bottom);
        }
    }

    public void addHorizontalWrapMaxVariable(ConstraintAnchor right) {
        WeakReference<ConstraintAnchor> weakReference = this.horizontalWrapMax;
        if (weakReference == null || weakReference.get() == null || right.getFinalValue() > this.horizontalWrapMax.get().getFinalValue()) {
            this.horizontalWrapMax = new WeakReference<>(right);
        }
    }

    private void addMinWrap(ConstraintAnchor constraintAnchor, SolverVariable parentMin) {
        this.mSystem.addGreaterThan(this.mSystem.createObjectVariable(constraintAnchor), parentMin, 0, 5);
    }

    private void addMaxWrap(ConstraintAnchor constraintAnchor, SolverVariable parentMax) {
        this.mSystem.addGreaterThan(parentMax, this.mSystem.createObjectVariable(constraintAnchor), 0, 5);
    }

    public boolean addChildrenToSolver(LinearSystem system) {
        int orientation;
        boolean optimize = optimizeFor(64);
        addToSolver(system, optimize);
        int count = this.mChildren.size();
        boolean hasBarriers = false;
        for (int i = 0; i < count; i++) {
            ConstraintWidget widget = (ConstraintWidget) this.mChildren.get(i);
            widget.setInBarrier(0, false);
            widget.setInBarrier(1, false);
            if (widget instanceof Barrier) {
                hasBarriers = true;
            }
        }
        if (hasBarriers) {
            for (int i2 = 0; i2 < count; i2++) {
                ConstraintWidget widget2 = (ConstraintWidget) this.mChildren.get(i2);
                if (widget2 instanceof Barrier) {
                    ((Barrier) widget2).markWidgets();
                }
            }
        }
        for (int i3 = 0; i3 < count; i3++) {
            ConstraintWidget widget3 = (ConstraintWidget) this.mChildren.get(i3);
            if (widget3.addFirst()) {
                widget3.addToSolver(system, optimize);
            }
        }
        if (LinearSystem.USE_DEPENDENCY_ORDERING) {
            HashSet<ConstraintWidget> widgetsToAdd = new HashSet<>();
            for (int i4 = 0; i4 < count; i4++) {
                ConstraintWidget widget4 = (ConstraintWidget) this.mChildren.get(i4);
                if (!widget4.addFirst()) {
                    widgetsToAdd.add(widget4);
                }
            }
            if (getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                orientation = 0;
            } else {
                orientation = 1;
            }
            addChildrenToSolverByDependency(this, system, widgetsToAdd, orientation, false);
            Iterator<ConstraintWidget> it = widgetsToAdd.iterator();
            while (it.hasNext()) {
                ConstraintWidget widget5 = it.next();
                Optimizer.checkMatchParent(this, system, widget5);
                widget5.addToSolver(system, optimize);
            }
        } else {
            for (int i5 = 0; i5 < count; i5++) {
                ConstraintWidget widget6 = (ConstraintWidget) this.mChildren.get(i5);
                if (widget6 instanceof ConstraintWidgetContainer) {
                    ConstraintWidget.DimensionBehaviour horizontalBehaviour = widget6.mListDimensionBehaviors[0];
                    ConstraintWidget.DimensionBehaviour verticalBehaviour = widget6.mListDimensionBehaviors[1];
                    if (horizontalBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        widget6.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                    }
                    if (verticalBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        widget6.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                    }
                    widget6.addToSolver(system, optimize);
                    if (horizontalBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        widget6.setHorizontalDimensionBehaviour(horizontalBehaviour);
                    }
                    if (verticalBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        widget6.setVerticalDimensionBehaviour(verticalBehaviour);
                    }
                } else {
                    Optimizer.checkMatchParent(this, system, widget6);
                    if (!widget6.addFirst()) {
                        widget6.addToSolver(system, optimize);
                    }
                }
            }
        }
        if (this.mHorizontalChainsSize > 0) {
            Chain.applyChainConstraints(this, system, null, 0);
        }
        if (this.mVerticalChainsSize > 0) {
            Chain.applyChainConstraints(this, system, null, 1);
        }
        return true;
    }

    public void updateChildrenFromSolver(LinearSystem system, boolean[] flags) {
        flags[2] = false;
        boolean optimize = optimizeFor(64);
        updateFromSolver(system, optimize);
        int count = this.mChildren.size();
        for (int i = 0; i < count; i++) {
            ((ConstraintWidget) this.mChildren.get(i)).updateFromSolver(system, optimize);
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void updateFromRuns(boolean updateHorizontal, boolean updateVertical) {
        super.updateFromRuns(updateHorizontal, updateVertical);
        int count = this.mChildren.size();
        for (int i = 0; i < count; i++) {
            ((ConstraintWidget) this.mChildren.get(i)).updateFromRuns(updateHorizontal, updateVertical);
        }
    }

    public void setPadding(int left, int top, int right, int bottom) {
        this.mPaddingLeft = left;
        this.mPaddingTop = top;
        this.mPaddingRight = right;
        this.mPaddingBottom = bottom;
    }

    public void setRtl(boolean isRtl) {
        this.mIsRtl = isRtl;
    }

    public boolean isRtl() {
        return this.mIsRtl;
    }

    public static boolean measure(ConstraintWidget widget, BasicMeasure.Measurer measurer, BasicMeasure.Measure measure, int measureStrategy) {
        int measuredWidth;
        int measuredHeight;
        if (measurer == null) {
            return false;
        }
        measure.horizontalBehavior = widget.getHorizontalDimensionBehaviour();
        measure.verticalBehavior = widget.getVerticalDimensionBehaviour();
        measure.horizontalDimension = widget.getWidth();
        measure.verticalDimension = widget.getHeight();
        measure.measuredNeedsSolverPass = false;
        measure.measureStrategy = measureStrategy;
        boolean horizontalMatchConstraints = measure.horizontalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        boolean verticalMatchConstraints = measure.verticalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        boolean horizontalUseRatio = horizontalMatchConstraints && widget.mDimensionRatio > 0.0f;
        boolean verticalUseRatio = verticalMatchConstraints && widget.mDimensionRatio > 0.0f;
        if (horizontalMatchConstraints && widget.hasDanglingDimension(0) && widget.mMatchConstraintDefaultWidth == 0 && !horizontalUseRatio) {
            horizontalMatchConstraints = false;
            measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            if (verticalMatchConstraints && widget.mMatchConstraintDefaultHeight == 0) {
                measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
            }
        }
        if (verticalMatchConstraints && widget.hasDanglingDimension(1) && widget.mMatchConstraintDefaultHeight == 0 && !verticalUseRatio) {
            verticalMatchConstraints = false;
            measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            if (horizontalMatchConstraints && widget.mMatchConstraintDefaultWidth == 0) {
                measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
            }
        }
        if (widget.isResolvedHorizontally()) {
            horizontalMatchConstraints = false;
            measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
        }
        if (widget.isResolvedVertically()) {
            verticalMatchConstraints = false;
            measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
        }
        if (horizontalUseRatio) {
            if (widget.mResolvedMatchConstraintDefault[0] == 4) {
                measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
            } else if (!verticalMatchConstraints) {
                if (measure.verticalBehavior == ConstraintWidget.DimensionBehaviour.FIXED) {
                    measuredHeight = measure.verticalDimension;
                } else {
                    measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    measurer.measure(widget, measure);
                    measuredHeight = measure.measuredHeight;
                }
                measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
                if (widget.mDimensionRatioSide == 0 || widget.mDimensionRatioSide == -1) {
                    measure.horizontalDimension = (int) (widget.getDimensionRatio() * ((float) measuredHeight));
                } else {
                    measure.horizontalDimension = (int) (widget.getDimensionRatio() / ((float) measuredHeight));
                }
            }
        }
        if (verticalUseRatio) {
            if (widget.mResolvedMatchConstraintDefault[1] == 4) {
                measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
            } else if (!horizontalMatchConstraints) {
                if (measure.horizontalBehavior == ConstraintWidget.DimensionBehaviour.FIXED) {
                    measuredWidth = measure.horizontalDimension;
                } else {
                    measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    measurer.measure(widget, measure);
                    measuredWidth = measure.measuredWidth;
                }
                measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
                if (widget.mDimensionRatioSide == 0 || widget.mDimensionRatioSide == -1) {
                    measure.verticalDimension = (int) (((float) measuredWidth) / widget.getDimensionRatio());
                } else {
                    measure.verticalDimension = (int) (((float) measuredWidth) * widget.getDimensionRatio());
                }
            }
        }
        measurer.measure(widget, measure);
        widget.setWidth(measure.measuredWidth);
        widget.setHeight(measure.measuredHeight);
        widget.setHasBaseline(measure.measuredHasBaseline);
        widget.setBaselineDistance(measure.measuredBaseline);
        measure.measureStrategy = BasicMeasure.Measure.SELF_DIMENSIONS;
        return measure.measuredNeedsSolverPass;
    }

    @Override // androidx.constraintlayout.solver.widgets.WidgetContainer
    public void layout() {
        int preh;
        int preh2;
        int prew;
        boolean needsSolving;
        int count;
        boolean needsSolving2;
        int i = 0;
        this.mX = 0;
        this.mY = 0;
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
        int count2 = this.mChildren.size();
        int prew2 = Math.max(0, getWidth());
        int preh3 = Math.max(0, getHeight());
        ConstraintWidget.DimensionBehaviour originalVerticalDimensionBehaviour = this.mListDimensionBehaviors[1];
        ConstraintWidget.DimensionBehaviour originalHorizontalDimensionBehaviour = this.mListDimensionBehaviors[0];
        Metrics metrics = this.mMetrics;
        if (metrics != null) {
            metrics.layouts++;
        }
        if (Optimizer.enabled(this.mOptimizationLevel, 1)) {
            Direct.solvingPass(this, getMeasurer());
            int i2 = 0;
            while (i2 < count2) {
                ConstraintWidget child = (ConstraintWidget) this.mChildren.get(i2);
                if (child.isMeasureRequested() && !(child instanceof Guideline) && !(child instanceof Barrier) && !(child instanceof VirtualLayout) && !child.isInVirtualLayout()) {
                    if (((child.getDimensionBehaviour(i) != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || child.mMatchConstraintDefaultWidth == 1 || child.getDimensionBehaviour(1) != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || child.mMatchConstraintDefaultHeight == 1) ? i : 1) == 0) {
                        measure(child, this.mMeasurer, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
                    }
                }
                i2++;
                i = 0;
            }
        }
        if (count2 <= 2 || (!(originalHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || originalVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) || !Optimizer.enabled(this.mOptimizationLevel, 1024) || !Grouping.simpleSolvingPass(this, getMeasurer()))) {
            preh2 = prew2;
            prew = 0;
            preh = preh3;
        } else {
            if (originalHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                if (prew2 >= getWidth() || prew2 <= 0) {
                    prew2 = getWidth();
                } else {
                    setWidth(prew2);
                    this.mWidthMeasuredTooSmall = true;
                }
            }
            if (originalVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                if (preh3 >= getHeight() || preh3 <= 0) {
                    preh3 = getHeight();
                } else {
                    setHeight(preh3);
                    this.mHeightMeasuredTooSmall = true;
                }
            }
            preh2 = prew2;
            prew = 1;
            preh = preh3;
        }
        boolean useGraphOptimizer = optimizeFor(64) || optimizeFor(128);
        LinearSystem linearSystem = this.mSystem;
        linearSystem.graphOptimizer = false;
        linearSystem.newgraphOptimizer = false;
        if (this.mOptimizationLevel != 0 && useGraphOptimizer) {
            linearSystem.newgraphOptimizer = true;
        }
        List<ConstraintWidget> allChildren = this.mChildren;
        boolean hasWrapContent = getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        resetChains();
        for (int i3 = 0; i3 < count2; i3++) {
            ConstraintWidget widget = (ConstraintWidget) this.mChildren.get(i3);
            if (widget instanceof WidgetContainer) {
                ((WidgetContainer) widget).layout();
            }
        }
        boolean optimize = optimizeFor(64);
        boolean needsSolving3 = true;
        int wrap_override = prew;
        int width = 0;
        while (needsSolving3) {
            int countSolve = width + 1;
            try {
                this.mSystem.reset();
                resetChains();
                createObjectVariables(this.mSystem);
                for (int i4 = 0; i4 < count2; i4++) {
                    ((ConstraintWidget) this.mChildren.get(i4)).createObjectVariables(this.mSystem);
                }
                boolean needsSolving4 = addChildrenToSolver(this.mSystem);
                if (!(this.verticalWrapMin == null || this.verticalWrapMin.get() == null)) {
                    addMinWrap(this.verticalWrapMin.get(), this.mSystem.createObjectVariable(this.mTop));
                    this.verticalWrapMin = null;
                }
                if (!(this.verticalWrapMax == null || this.verticalWrapMax.get() == null)) {
                    addMaxWrap(this.verticalWrapMax.get(), this.mSystem.createObjectVariable(this.mBottom));
                    this.verticalWrapMax = null;
                }
                if (!(this.horizontalWrapMin == null || this.horizontalWrapMin.get() == null)) {
                    addMinWrap(this.horizontalWrapMin.get(), this.mSystem.createObjectVariable(this.mLeft));
                    this.horizontalWrapMin = null;
                }
                if (!(this.horizontalWrapMax == null || this.horizontalWrapMax.get() == null)) {
                    addMaxWrap(this.horizontalWrapMax.get(), this.mSystem.createObjectVariable(this.mRight));
                    this.horizontalWrapMax = null;
                }
                if (needsSolving4) {
                    this.mSystem.minimize();
                }
                needsSolving = needsSolving4;
            } catch (Exception e) {
                e.printStackTrace();
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                needsSolving = needsSolving3;
                sb.append("EXCEPTION : ");
                sb.append(e);
                printStream.println(sb.toString());
            }
            if (needsSolving) {
                updateChildrenFromSolver(this.mSystem, Optimizer.flags);
            } else {
                updateFromSolver(this.mSystem, optimize);
                for (int i5 = 0; i5 < count2; i5++) {
                    ((ConstraintWidget) this.mChildren.get(i5)).updateFromSolver(this.mSystem, optimize);
                }
            }
            boolean needsSolving5 = false;
            if (!hasWrapContent || countSolve >= 8 || !Optimizer.flags[2]) {
                needsSolving2 = false;
                count = count2;
            } else {
                int i6 = 0;
                int maxY = 0;
                int maxX = 0;
                while (i6 < count2) {
                    ConstraintWidget widget2 = (ConstraintWidget) this.mChildren.get(i6);
                    maxX = Math.max(maxX, widget2.mX + widget2.getWidth());
                    maxY = Math.max(maxY, widget2.mY + widget2.getHeight());
                    i6++;
                    needsSolving5 = needsSolving5;
                    count2 = count2;
                }
                needsSolving2 = needsSolving5;
                count = count2;
                int maxX2 = Math.max(this.mMinWidth, maxX);
                int maxY2 = Math.max(this.mMinHeight, maxY);
                if (originalHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && getWidth() < maxX2) {
                    setWidth(maxX2);
                    this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    wrap_override = 1;
                    needsSolving2 = true;
                }
                if (originalVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && getHeight() < maxY2) {
                    setHeight(maxY2);
                    this.mListDimensionBehaviors[1] = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    wrap_override = 1;
                    needsSolving2 = true;
                }
            }
            int width2 = Math.max(this.mMinWidth, getWidth());
            if (width2 > getWidth()) {
                setWidth(width2);
                this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.FIXED;
                wrap_override = 1;
                needsSolving2 = true;
            }
            int height = Math.max(this.mMinHeight, getHeight());
            if (height > getHeight()) {
                setHeight(height);
                this.mListDimensionBehaviors[1] = ConstraintWidget.DimensionBehaviour.FIXED;
                wrap_override = 1;
                needsSolving2 = true;
            }
            if (wrap_override == 0) {
                if (this.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && preh2 > 0 && getWidth() > preh2) {
                    this.mWidthMeasuredTooSmall = true;
                    wrap_override = 1;
                    this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.FIXED;
                    setWidth(preh2);
                    needsSolving2 = true;
                }
                if (this.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || preh <= 0 || getHeight() <= preh) {
                    needsSolving3 = needsSolving2;
                } else {
                    this.mHeightMeasuredTooSmall = true;
                    this.mListDimensionBehaviors[1] = ConstraintWidget.DimensionBehaviour.FIXED;
                    setHeight(preh);
                    needsSolving3 = true;
                    wrap_override = 1;
                }
            } else {
                needsSolving3 = needsSolving2;
            }
            width = countSolve;
            count2 = count;
        }
        this.mChildren = (ArrayList) allChildren;
        if (wrap_override != 0) {
            this.mListDimensionBehaviors[0] = originalHorizontalDimensionBehaviour;
            this.mListDimensionBehaviors[1] = originalVerticalDimensionBehaviour;
        }
        resetSolverVariables(this.mSystem.getCache());
    }

    public boolean handlesInternalConstraints() {
        return false;
    }

    public ArrayList<Guideline> getVerticalGuidelines() {
        ArrayList<Guideline> guidelines = new ArrayList<>();
        int mChildrenSize = this.mChildren.size();
        for (int i = 0; i < mChildrenSize; i++) {
            ConstraintWidget widget = (ConstraintWidget) this.mChildren.get(i);
            if (widget instanceof Guideline) {
                Guideline guideline = (Guideline) widget;
                if (guideline.getOrientation() == 1) {
                    guidelines.add(guideline);
                }
            }
        }
        return guidelines;
    }

    public ArrayList<Guideline> getHorizontalGuidelines() {
        ArrayList<Guideline> guidelines = new ArrayList<>();
        int mChildrenSize = this.mChildren.size();
        for (int i = 0; i < mChildrenSize; i++) {
            ConstraintWidget widget = (ConstraintWidget) this.mChildren.get(i);
            if (widget instanceof Guideline) {
                Guideline guideline = (Guideline) widget;
                if (guideline.getOrientation() == 0) {
                    guidelines.add(guideline);
                }
            }
        }
        return guidelines;
    }

    public LinearSystem getSystem() {
        return this.mSystem;
    }

    private void resetChains() {
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
    }

    public void addChain(ConstraintWidget constraintWidget, int type) {
        if (type == 0) {
            addHorizontalChain(constraintWidget);
        } else if (type == 1) {
            addVerticalChain(constraintWidget);
        }
    }

    private void addHorizontalChain(ConstraintWidget widget) {
        int i = this.mHorizontalChainsSize + 1;
        ChainHead[] chainHeadArr = this.mHorizontalChainsArray;
        if (i >= chainHeadArr.length) {
            this.mHorizontalChainsArray = (ChainHead[]) Arrays.copyOf(chainHeadArr, chainHeadArr.length * 2);
        }
        this.mHorizontalChainsArray[this.mHorizontalChainsSize] = new ChainHead(widget, 0, isRtl());
        this.mHorizontalChainsSize++;
    }

    private void addVerticalChain(ConstraintWidget widget) {
        int i = this.mVerticalChainsSize + 1;
        ChainHead[] chainHeadArr = this.mVerticalChainsArray;
        if (i >= chainHeadArr.length) {
            this.mVerticalChainsArray = (ChainHead[]) Arrays.copyOf(chainHeadArr, chainHeadArr.length * 2);
        }
        this.mVerticalChainsArray[this.mVerticalChainsSize] = new ChainHead(widget, 1, isRtl());
        this.mVerticalChainsSize++;
    }
}
