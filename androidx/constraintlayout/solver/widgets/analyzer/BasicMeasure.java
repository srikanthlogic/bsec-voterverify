package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.widgets.Barrier;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.Guideline;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.constraintlayout.solver.widgets.Optimizer;
import androidx.constraintlayout.solver.widgets.VirtualLayout;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class BasicMeasure {
    public static final int AT_MOST;
    private static final boolean DEBUG;
    public static final int EXACTLY;
    public static final int FIXED;
    public static final int MATCH_PARENT;
    private static final int MODE_SHIFT;
    public static final int UNSPECIFIED;
    public static final int WRAP_CONTENT;
    private ConstraintWidgetContainer constraintWidgetContainer;
    private final ArrayList<ConstraintWidget> mVariableDimensionsWidgets = new ArrayList<>();
    private Measure mMeasure = new Measure();

    /* loaded from: classes.dex */
    public static class Measure {
        public static int SELF_DIMENSIONS = 0;
        public static int TRY_GIVEN_DIMENSIONS = 1;
        public static int USE_GIVEN_DIMENSIONS = 2;
        public ConstraintWidget.DimensionBehaviour horizontalBehavior;
        public int horizontalDimension;
        public int measureStrategy;
        public int measuredBaseline;
        public boolean measuredHasBaseline;
        public int measuredHeight;
        public boolean measuredNeedsSolverPass;
        public int measuredWidth;
        public ConstraintWidget.DimensionBehaviour verticalBehavior;
        public int verticalDimension;
    }

    /* loaded from: classes.dex */
    public interface Measurer {
        void didMeasures();

        void measure(ConstraintWidget constraintWidget, Measure measure);
    }

    public void updateHierarchy(ConstraintWidgetContainer layout) {
        this.mVariableDimensionsWidgets.clear();
        int childCount = layout.mChildren.size();
        for (int i = 0; i < childCount; i++) {
            ConstraintWidget widget = (ConstraintWidget) layout.mChildren.get(i);
            if (widget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || widget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                this.mVariableDimensionsWidgets.add(widget);
            }
        }
        layout.invalidateGraph();
    }

    public BasicMeasure(ConstraintWidgetContainer constraintWidgetContainer) {
        this.constraintWidgetContainer = constraintWidgetContainer;
    }

    private void measureChildren(ConstraintWidgetContainer layout) {
        int childCount = layout.mChildren.size();
        boolean optimize = layout.optimizeFor(64);
        Measurer measurer = layout.getMeasurer();
        for (int i = 0; i < childCount; i++) {
            ConstraintWidget child = (ConstraintWidget) layout.mChildren.get(i);
            if (!(child instanceof Guideline) && !(child instanceof Barrier) && !child.isInVirtualLayout() && (!optimize || child.horizontalRun == null || child.verticalRun == null || !child.horizontalRun.dimension.resolved || !child.verticalRun.dimension.resolved)) {
                boolean skip = false;
                ConstraintWidget.DimensionBehaviour widthBehavior = child.getDimensionBehaviour(0);
                ConstraintWidget.DimensionBehaviour heightBehavior = child.getDimensionBehaviour(1);
                if (widthBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && child.mMatchConstraintDefaultWidth != 1 && heightBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && child.mMatchConstraintDefaultHeight != 1) {
                    skip = true;
                }
                if (!skip && layout.optimizeFor(1) && !(child instanceof VirtualLayout)) {
                    if (widthBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && child.mMatchConstraintDefaultWidth == 0 && heightBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && !child.isInHorizontalChain()) {
                        skip = true;
                    }
                    if (heightBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && child.mMatchConstraintDefaultHeight == 0 && widthBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && !child.isInHorizontalChain()) {
                        skip = true;
                    }
                    if ((widthBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || heightBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) && child.mDimensionRatio > 0.0f) {
                        skip = true;
                    }
                }
                if (!skip) {
                    measure(measurer, child, Measure.SELF_DIMENSIONS);
                    if (layout.mMetrics != null) {
                        layout.mMetrics.measuredWidgets++;
                    }
                }
            }
        }
        measurer.didMeasures();
    }

    private void solveLinearSystem(ConstraintWidgetContainer layout, String reason, int w, int h) {
        int minWidth = layout.getMinWidth();
        int minHeight = layout.getMinHeight();
        layout.setMinWidth(0);
        layout.setMinHeight(0);
        layout.setWidth(w);
        layout.setHeight(h);
        layout.setMinWidth(minWidth);
        layout.setMinHeight(minHeight);
        this.constraintWidgetContainer.layout();
    }

    /* JADX WARN: Code restructure failed: missing block: B:41:0x009c, code lost:
        r18 = false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public long solverMeasure(ConstraintWidgetContainer layout, int optimizationLevel, int paddingX, int paddingY, int widthMode, int widthSize, int heightMode, int heightSize, int lastMeasureWidth, int lastMeasureHeight) {
        boolean optimize;
        int heightSize2;
        int heightSize3;
        int widthSize2;
        int optimizations;
        long layoutTime;
        int startingWidth;
        int startingHeight;
        int j;
        Measurer measurer;
        int maxIterations;
        boolean optimize2;
        int sizeDependentWidgetsCount;
        int measureStrategy;
        int startingHeight2;
        int startingWidth2;
        int optimizations2;
        boolean needSolverPass;
        boolean preWidth;
        boolean allSolved;
        boolean allSolved2;
        Measurer measurer2 = layout.getMeasurer();
        long layoutTime2 = 0;
        int childCount = layout.mChildren.size();
        int startingWidth3 = layout.getWidth();
        int startingHeight3 = layout.getHeight();
        boolean optimizeWrap = Optimizer.enabled(optimizationLevel, 128);
        boolean optimize3 = optimizeWrap || Optimizer.enabled(optimizationLevel, 64);
        if (optimize3) {
            int i = 0;
            while (true) {
                if (i >= childCount) {
                    optimize = optimize3;
                    break;
                }
                ConstraintWidget child = (ConstraintWidget) layout.mChildren.get(i);
                boolean ratio = (child.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) && (child.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) && child.getDimensionRatio() > 0.0f;
                if (!child.isInHorizontalChain() || !ratio) {
                    if (child.isInVerticalChain() && ratio) {
                        optimize = false;
                        break;
                    } else if (child instanceof VirtualLayout) {
                        optimize = false;
                        break;
                    } else if (child.isInHorizontalChain() || child.isInVerticalChain()) {
                        break;
                    } else {
                        i++;
                        optimize3 = optimize3;
                    }
                } else {
                    optimize = false;
                    break;
                }
            }
        } else {
            optimize = optimize3;
        }
        if (optimize && LinearSystem.sMetrics != null) {
            LinearSystem.sMetrics.measures++;
        }
        boolean allSolved3 = false;
        boolean optimize4 = optimize & ((widthMode == 1073741824 && heightMode == 1073741824) || optimizeWrap);
        int computations = 0;
        if (optimize4) {
            int widthSize3 = Math.min(layout.getMaxWidth(), widthSize);
            int heightSize4 = Math.min(layout.getMaxHeight(), heightSize);
            if (widthMode == 1073741824 && layout.getWidth() != widthSize3) {
                layout.setWidth(widthSize3);
                layout.invalidateGraph();
            }
            if (heightMode == 1073741824 && layout.getHeight() != heightSize4) {
                layout.setHeight(heightSize4);
                layout.invalidateGraph();
            }
            if (widthMode == 1073741824 && heightMode == 1073741824) {
                allSolved = layout.directMeasure(optimizeWrap);
                computations = 2;
            } else {
                allSolved = layout.directMeasureSetup(optimizeWrap);
                if (widthMode == 1073741824) {
                    allSolved &= layout.directMeasureWithOrientation(optimizeWrap, 0);
                    computations = 0 + 1;
                }
                if (heightMode == 1073741824) {
                    allSolved &= layout.directMeasureWithOrientation(optimizeWrap, 1);
                    computations++;
                }
            }
            if (allSolved) {
                allSolved2 = allSolved;
                layout.updateFromRuns(widthMode == 1073741824, heightMode == 1073741824);
            } else {
                allSolved2 = allSolved;
            }
            allSolved3 = allSolved2;
            heightSize2 = heightSize4;
            heightSize3 = widthSize3;
            widthSize2 = computations;
        } else {
            heightSize3 = widthSize;
            heightSize2 = heightSize;
            widthSize2 = 0;
        }
        if (allSolved3 && widthSize2 == 2) {
            return 0;
        }
        int optimizations3 = layout.getOptimizationLevel();
        if (childCount > 0) {
            measureChildren(layout);
        }
        updateHierarchy(layout);
        int sizeDependentWidgetsCount2 = this.mVariableDimensionsWidgets.size();
        if (childCount > 0) {
            solveLinearSystem(layout, "First pass", startingWidth3, startingHeight3);
        }
        if (sizeDependentWidgetsCount2 > 0) {
            boolean containerWrapWidth = layout.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            boolean containerWrapHeight = layout.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            int i2 = 0;
            boolean needSolverPass2 = false;
            int minWidth = Math.max(layout.getWidth(), this.constraintWidgetContainer.getMinWidth());
            int minHeight = Math.max(layout.getHeight(), this.constraintWidgetContainer.getMinHeight());
            while (i2 < sizeDependentWidgetsCount2) {
                ConstraintWidget widget = this.mVariableDimensionsWidgets.get(i2);
                if (!(widget instanceof VirtualLayout)) {
                    optimizations2 = optimizations3;
                    startingWidth2 = startingWidth3;
                    startingHeight2 = startingHeight3;
                } else {
                    int preWidth2 = widget.getWidth();
                    optimizations2 = optimizations3;
                    int preHeight = widget.getHeight();
                    startingWidth2 = startingWidth3;
                    boolean needSolverPass3 = measure(measurer2, widget, Measure.TRY_GIVEN_DIMENSIONS) | needSolverPass2;
                    if (layout.mMetrics != null) {
                        needSolverPass = needSolverPass3;
                        startingHeight2 = startingHeight3;
                        layout.mMetrics.measuredMatchWidgets++;
                    } else {
                        needSolverPass = needSolverPass3;
                        startingHeight2 = startingHeight3;
                    }
                    int measuredWidth = widget.getWidth();
                    int measuredHeight = widget.getHeight();
                    if (measuredWidth != preWidth2) {
                        widget.setWidth(measuredWidth);
                        if (containerWrapWidth && widget.getRight() > minWidth) {
                            minWidth = Math.max(minWidth, widget.getRight() + widget.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin());
                        }
                        preWidth = true;
                    } else {
                        preWidth = needSolverPass;
                    }
                    if (measuredHeight != preHeight) {
                        widget.setHeight(measuredHeight);
                        if (containerWrapHeight && widget.getBottom() > minHeight) {
                            minHeight = Math.max(minHeight, widget.getBottom() + widget.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin());
                        }
                        preWidth = true;
                    }
                    needSolverPass2 = preWidth | ((VirtualLayout) widget).needSolverPass();
                }
                i2++;
                layoutTime2 = layoutTime2;
                optimizations3 = optimizations2;
                startingWidth3 = startingWidth2;
                startingHeight3 = startingHeight2;
            }
            optimizations = optimizations3;
            layoutTime = layoutTime2;
            int maxIterations2 = 2;
            int j2 = 0;
            while (true) {
                if (j2 >= maxIterations2) {
                    startingWidth = startingWidth3;
                    startingHeight = startingHeight3;
                    break;
                }
                int i3 = 0;
                while (i3 < sizeDependentWidgetsCount2) {
                    ConstraintWidget widget2 = this.mVariableDimensionsWidgets.get(i3);
                    if ((!(widget2 instanceof Helper) || (widget2 instanceof VirtualLayout)) && !(widget2 instanceof Guideline) && widget2.getVisibility() != 8 && ((!optimize4 || !widget2.horizontalRun.dimension.resolved || !widget2.verticalRun.dimension.resolved) && !(widget2 instanceof VirtualLayout))) {
                        int preWidth3 = widget2.getWidth();
                        int preHeight2 = widget2.getHeight();
                        sizeDependentWidgetsCount = sizeDependentWidgetsCount2;
                        int preBaselineDistance = widget2.getBaselineDistance();
                        int measureStrategy2 = Measure.TRY_GIVEN_DIMENSIONS;
                        optimize2 = optimize4;
                        if (j2 == maxIterations2 - 1) {
                            measureStrategy = Measure.USE_GIVEN_DIMENSIONS;
                        } else {
                            measureStrategy = measureStrategy2;
                        }
                        needSolverPass2 |= measure(measurer2, widget2, measureStrategy);
                        maxIterations = maxIterations2;
                        if (layout.mMetrics != null) {
                            measurer = measurer2;
                            j = j2;
                            layout.mMetrics.measuredMatchWidgets++;
                        } else {
                            measurer = measurer2;
                            j = j2;
                        }
                        int measuredWidth2 = widget2.getWidth();
                        int measuredHeight2 = widget2.getHeight();
                        if (measuredWidth2 != preWidth3) {
                            widget2.setWidth(measuredWidth2);
                            if (containerWrapWidth && widget2.getRight() > minWidth) {
                                minWidth = Math.max(minWidth, widget2.getRight() + widget2.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin());
                            }
                            needSolverPass2 = true;
                        }
                        if (measuredHeight2 != preHeight2) {
                            widget2.setHeight(measuredHeight2);
                            if (containerWrapHeight && widget2.getBottom() > minHeight) {
                                minHeight = Math.max(minHeight, widget2.getBottom() + widget2.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin());
                            }
                            needSolverPass2 = true;
                        }
                        if (widget2.hasBaseline() && preBaselineDistance != widget2.getBaselineDistance()) {
                            needSolverPass2 = true;
                        }
                    } else {
                        maxIterations = maxIterations2;
                        sizeDependentWidgetsCount = sizeDependentWidgetsCount2;
                        measurer = measurer2;
                        j = j2;
                        optimize2 = optimize4;
                    }
                    i3++;
                    sizeDependentWidgetsCount2 = sizeDependentWidgetsCount;
                    optimize4 = optimize2;
                    maxIterations2 = maxIterations;
                    measurer2 = measurer;
                    j2 = j;
                }
                if (!needSolverPass2) {
                    startingWidth = startingWidth3;
                    startingHeight = startingHeight3;
                    break;
                }
                solveLinearSystem(layout, "intermediate pass", startingWidth3, startingHeight3);
                needSolverPass2 = false;
                j2++;
                sizeDependentWidgetsCount2 = sizeDependentWidgetsCount2;
                optimize4 = optimize4;
                maxIterations2 = maxIterations2;
                measurer2 = measurer2;
            }
            if (needSolverPass2) {
                solveLinearSystem(layout, "2nd pass", startingWidth, startingHeight);
                boolean needSolverPass4 = false;
                if (layout.getWidth() < minWidth) {
                    layout.setWidth(minWidth);
                    needSolverPass4 = true;
                }
                if (layout.getHeight() < minHeight) {
                    layout.setHeight(minHeight);
                    needSolverPass4 = true;
                }
                if (needSolverPass4) {
                    solveLinearSystem(layout, "3rd pass", startingWidth, startingHeight);
                }
            }
        } else {
            optimizations = optimizations3;
            layoutTime = 0;
        }
        layout.setOptimizationLevel(optimizations);
        return layoutTime;
    }

    private boolean measure(Measurer measurer, ConstraintWidget widget, int measureStrategy) {
        this.mMeasure.horizontalBehavior = widget.getHorizontalDimensionBehaviour();
        this.mMeasure.verticalBehavior = widget.getVerticalDimensionBehaviour();
        this.mMeasure.horizontalDimension = widget.getWidth();
        this.mMeasure.verticalDimension = widget.getHeight();
        Measure measure = this.mMeasure;
        measure.measuredNeedsSolverPass = false;
        measure.measureStrategy = measureStrategy;
        boolean horizontalMatchConstraints = measure.horizontalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        boolean verticalMatchConstraints = this.mMeasure.verticalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        boolean horizontalUseRatio = horizontalMatchConstraints && widget.mDimensionRatio > 0.0f;
        boolean verticalUseRatio = verticalMatchConstraints && widget.mDimensionRatio > 0.0f;
        if (horizontalUseRatio && widget.mResolvedMatchConstraintDefault[0] == 4) {
            this.mMeasure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
        }
        if (verticalUseRatio && widget.mResolvedMatchConstraintDefault[1] == 4) {
            this.mMeasure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
        }
        measurer.measure(widget, this.mMeasure);
        widget.setWidth(this.mMeasure.measuredWidth);
        widget.setHeight(this.mMeasure.measuredHeight);
        widget.setHasBaseline(this.mMeasure.measuredHasBaseline);
        widget.setBaselineDistance(this.mMeasure.measuredBaseline);
        this.mMeasure.measureStrategy = Measure.SELF_DIMENSIONS;
        return this.mMeasure.measuredNeedsSolverPass;
    }
}
