package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import java.util.HashMap;
/* loaded from: classes.dex */
public class Barrier extends HelperWidget {
    public static final int BOTTOM;
    public static final int LEFT;
    public static final int RIGHT;
    public static final int TOP;
    private static final boolean USE_RELAX_GONE;
    private static final boolean USE_RESOLUTION;
    private int mBarrierType = 0;
    private boolean mAllowsGoneWidget = true;
    private int mMargin = 0;
    boolean resolved = false;

    public Barrier() {
    }

    public Barrier(String debugName) {
        setDebugName(debugName);
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public boolean allowedInBarrier() {
        return true;
    }

    public int getBarrierType() {
        return this.mBarrierType;
    }

    public void setBarrierType(int barrierType) {
        this.mBarrierType = barrierType;
    }

    public void setAllowsGoneWidget(boolean allowsGoneWidget) {
        this.mAllowsGoneWidget = allowsGoneWidget;
    }

    public boolean allowsGoneWidget() {
        return this.mAllowsGoneWidget;
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public boolean isResolvedHorizontally() {
        return this.resolved;
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public boolean isResolvedVertically() {
        return this.resolved;
    }

    @Override // androidx.constraintlayout.solver.widgets.HelperWidget, androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void copy(ConstraintWidget src, HashMap<ConstraintWidget, ConstraintWidget> map) {
        super.copy(src, map);
        Barrier srcBarrier = (Barrier) src;
        this.mBarrierType = srcBarrier.mBarrierType;
        this.mAllowsGoneWidget = srcBarrier.mAllowsGoneWidget;
        this.mMargin = srcBarrier.mMargin;
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public String toString() {
        String debug = "[Barrier] " + getDebugName() + " {";
        for (int i = 0; i < this.mWidgetsCount; i++) {
            ConstraintWidget widget = this.mWidgets[i];
            if (i > 0) {
                debug = debug + ", ";
            }
            debug = debug + widget.getDebugName();
        }
        return debug + "}";
    }

    public void markWidgets() {
        for (int i = 0; i < this.mWidgetsCount; i++) {
            ConstraintWidget widget = this.mWidgets[i];
            int i2 = this.mBarrierType;
            if (i2 == 0 || i2 == 1) {
                widget.setInBarrier(0, true);
            } else if (i2 == 2 || i2 == 3) {
                widget.setInBarrier(1, true);
            }
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void addToSolver(LinearSystem system, boolean optimize) {
        this.mListAnchors[0] = this.mLeft;
        this.mListAnchors[2] = this.mTop;
        this.mListAnchors[1] = this.mRight;
        this.mListAnchors[3] = this.mBottom;
        for (int i = 0; i < this.mListAnchors.length; i++) {
            this.mListAnchors[i].mSolverVariable = system.createObjectVariable(this.mListAnchors[i]);
        }
        int i2 = this.mBarrierType;
        if (i2 >= 0 && i2 < 4) {
            ConstraintAnchor position = this.mListAnchors[this.mBarrierType];
            if (!this.resolved) {
                allSolved();
            }
            if (this.resolved) {
                this.resolved = false;
                int i3 = this.mBarrierType;
                if (i3 == 0 || i3 == 1) {
                    system.addEquality(this.mLeft.mSolverVariable, this.mX);
                    system.addEquality(this.mRight.mSolverVariable, this.mX);
                } else if (i3 == 2 || i3 == 3) {
                    system.addEquality(this.mTop.mSolverVariable, this.mY);
                    system.addEquality(this.mBottom.mSolverVariable, this.mY);
                }
            } else {
                boolean hasMatchConstraintWidgets = false;
                int i4 = 0;
                while (true) {
                    if (i4 >= this.mWidgetsCount) {
                        break;
                    }
                    ConstraintWidget widget = this.mWidgets[i4];
                    if (this.mAllowsGoneWidget || widget.allowedInBarrier()) {
                        int i5 = this.mBarrierType;
                        if ((i5 == 0 || i5 == 1) && widget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && widget.mLeft.mTarget != null && widget.mRight.mTarget != null) {
                            hasMatchConstraintWidgets = true;
                            break;
                        }
                        int i6 = this.mBarrierType;
                        if ((i6 == 2 || i6 == 3) && widget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && widget.mTop.mTarget != null && widget.mBottom.mTarget != null) {
                            hasMatchConstraintWidgets = true;
                            break;
                        }
                    }
                    i4++;
                }
                boolean mHasHorizontalCenteredDependents = this.mLeft.hasCenteredDependents() || this.mRight.hasCenteredDependents();
                boolean mHasVerticalCenteredDependents = this.mTop.hasCenteredDependents() || this.mBottom.hasCenteredDependents();
                int equalityOnReferencesStrength = 5;
                if (!(!hasMatchConstraintWidgets && ((this.mBarrierType == 0 && mHasHorizontalCenteredDependents) || ((this.mBarrierType == 2 && mHasVerticalCenteredDependents) || ((this.mBarrierType == 1 && mHasHorizontalCenteredDependents) || (this.mBarrierType == 3 && mHasVerticalCenteredDependents)))))) {
                    equalityOnReferencesStrength = 4;
                }
                for (int i7 = 0; i7 < this.mWidgetsCount; i7++) {
                    ConstraintWidget widget2 = this.mWidgets[i7];
                    if (this.mAllowsGoneWidget || widget2.allowedInBarrier()) {
                        SolverVariable target = system.createObjectVariable(widget2.mListAnchors[this.mBarrierType]);
                        widget2.mListAnchors[this.mBarrierType].mSolverVariable = target;
                        int margin = 0;
                        if (widget2.mListAnchors[this.mBarrierType].mTarget != null && widget2.mListAnchors[this.mBarrierType].mTarget.mOwner == this) {
                            margin = 0 + widget2.mListAnchors[this.mBarrierType].mMargin;
                        }
                        int i8 = this.mBarrierType;
                        if (i8 == 0 || i8 == 2) {
                            system.addLowerBarrier(position.mSolverVariable, target, this.mMargin - margin, hasMatchConstraintWidgets);
                        } else {
                            system.addGreaterBarrier(position.mSolverVariable, target, this.mMargin + margin, hasMatchConstraintWidgets);
                        }
                        system.addEquality(position.mSolverVariable, target, this.mMargin + margin, equalityOnReferencesStrength);
                    }
                }
                int i9 = this.mBarrierType;
                if (i9 == 0) {
                    system.addEquality(this.mRight.mSolverVariable, this.mLeft.mSolverVariable, 0, 8);
                    system.addEquality(this.mLeft.mSolverVariable, this.mParent.mRight.mSolverVariable, 0, 4);
                    system.addEquality(this.mLeft.mSolverVariable, this.mParent.mLeft.mSolverVariable, 0, 0);
                } else if (i9 == 1) {
                    system.addEquality(this.mLeft.mSolverVariable, this.mRight.mSolverVariable, 0, 8);
                    system.addEquality(this.mLeft.mSolverVariable, this.mParent.mLeft.mSolverVariable, 0, 4);
                    system.addEquality(this.mLeft.mSolverVariable, this.mParent.mRight.mSolverVariable, 0, 0);
                } else if (i9 == 2) {
                    system.addEquality(this.mBottom.mSolverVariable, this.mTop.mSolverVariable, 0, 8);
                    system.addEquality(this.mTop.mSolverVariable, this.mParent.mBottom.mSolverVariable, 0, 4);
                    system.addEquality(this.mTop.mSolverVariable, this.mParent.mTop.mSolverVariable, 0, 0);
                } else if (i9 == 3) {
                    system.addEquality(this.mTop.mSolverVariable, this.mBottom.mSolverVariable, 0, 8);
                    system.addEquality(this.mTop.mSolverVariable, this.mParent.mTop.mSolverVariable, 0, 4);
                    system.addEquality(this.mTop.mSolverVariable, this.mParent.mBottom.mSolverVariable, 0, 0);
                }
            }
        }
    }

    public void setMargin(int margin) {
        this.mMargin = margin;
    }

    public int getMargin() {
        return this.mMargin;
    }

    public int getOrientation() {
        int i = this.mBarrierType;
        if (i == 0 || i == 1) {
            return 0;
        }
        if (i == 2 || i == 3) {
            return 1;
        }
        return -1;
    }

    public boolean allSolved() {
        boolean hasAllWidgetsResolved = true;
        for (int i = 0; i < this.mWidgetsCount; i++) {
            ConstraintWidget widget = this.mWidgets[i];
            if (this.mAllowsGoneWidget || widget.allowedInBarrier()) {
                int i2 = this.mBarrierType;
                if ((i2 == 0 || i2 == 1) && !widget.isResolvedHorizontally()) {
                    hasAllWidgetsResolved = false;
                } else {
                    int i3 = this.mBarrierType;
                    if ((i3 == 2 || i3 == 3) && !widget.isResolvedVertically()) {
                        hasAllWidgetsResolved = false;
                    }
                }
            }
        }
        if (!hasAllWidgetsResolved || this.mWidgetsCount <= 0) {
            return false;
        }
        int barrierPosition = 0;
        boolean initialized = false;
        for (int i4 = 0; i4 < this.mWidgetsCount; i4++) {
            ConstraintWidget widget2 = this.mWidgets[i4];
            if (this.mAllowsGoneWidget || widget2.allowedInBarrier()) {
                if (!initialized) {
                    int i5 = this.mBarrierType;
                    if (i5 == 0) {
                        barrierPosition = widget2.getAnchor(ConstraintAnchor.Type.LEFT).getFinalValue();
                    } else if (i5 == 1) {
                        barrierPosition = widget2.getAnchor(ConstraintAnchor.Type.RIGHT).getFinalValue();
                    } else if (i5 == 2) {
                        barrierPosition = widget2.getAnchor(ConstraintAnchor.Type.TOP).getFinalValue();
                    } else if (i5 == 3) {
                        barrierPosition = widget2.getAnchor(ConstraintAnchor.Type.BOTTOM).getFinalValue();
                    }
                    initialized = true;
                }
                int i6 = this.mBarrierType;
                if (i6 == 0) {
                    barrierPosition = Math.min(barrierPosition, widget2.getAnchor(ConstraintAnchor.Type.LEFT).getFinalValue());
                } else if (i6 == 1) {
                    barrierPosition = Math.max(barrierPosition, widget2.getAnchor(ConstraintAnchor.Type.RIGHT).getFinalValue());
                } else if (i6 == 2) {
                    barrierPosition = Math.min(barrierPosition, widget2.getAnchor(ConstraintAnchor.Type.TOP).getFinalValue());
                } else if (i6 == 3) {
                    barrierPosition = Math.max(barrierPosition, widget2.getAnchor(ConstraintAnchor.Type.BOTTOM).getFinalValue());
                }
            }
        }
        int barrierPosition2 = barrierPosition + this.mMargin;
        int i7 = this.mBarrierType;
        if (i7 == 0 || i7 == 1) {
            setFinalHorizontal(barrierPosition2, barrierPosition2);
        } else {
            setFinalVertical(barrierPosition2, barrierPosition2);
        }
        this.resolved = true;
        return true;
    }
}
