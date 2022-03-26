package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.Cache;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.analyzer.Grouping;
import androidx.constraintlayout.solver.widgets.analyzer.WidgetGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
/* loaded from: classes.dex */
public class ConstraintAnchor {
    private static final boolean ALLOW_BINARY;
    private static final int UNSET_GONE_MARGIN;
    private int mFinalValue;
    private boolean mHasFinalValue;
    public final ConstraintWidget mOwner;
    SolverVariable mSolverVariable;
    public ConstraintAnchor mTarget;
    public final Type mType;
    private HashSet<ConstraintAnchor> mDependents = null;
    public int mMargin = 0;
    int mGoneMargin = -1;

    /* loaded from: classes.dex */
    public enum Type {
        NONE,
        LEFT,
        TOP,
        RIGHT,
        BOTTOM,
        BASELINE,
        CENTER,
        CENTER_X,
        CENTER_Y
    }

    public void findDependents(int orientation, ArrayList<WidgetGroup> list, WidgetGroup group) {
        HashSet<ConstraintAnchor> hashSet = this.mDependents;
        if (hashSet != null) {
            Iterator<ConstraintAnchor> it = hashSet.iterator();
            while (it.hasNext()) {
                Grouping.findDependents(it.next().mOwner, orientation, list, group);
            }
        }
    }

    public HashSet<ConstraintAnchor> getDependents() {
        return this.mDependents;
    }

    public boolean hasDependents() {
        HashSet<ConstraintAnchor> hashSet = this.mDependents;
        if (hashSet != null && hashSet.size() > 0) {
            return true;
        }
        return false;
    }

    public boolean hasCenteredDependents() {
        HashSet<ConstraintAnchor> hashSet = this.mDependents;
        if (hashSet == null) {
            return false;
        }
        Iterator<ConstraintAnchor> it = hashSet.iterator();
        while (it.hasNext()) {
            if (it.next().getOpposite().isConnected()) {
                return true;
            }
        }
        return false;
    }

    public void setFinalValue(int finalValue) {
        this.mFinalValue = finalValue;
        this.mHasFinalValue = true;
    }

    public int getFinalValue() {
        if (!this.mHasFinalValue) {
            return 0;
        }
        return this.mFinalValue;
    }

    public void resetFinalResolution() {
        this.mHasFinalValue = false;
        this.mFinalValue = 0;
    }

    public boolean hasFinalValue() {
        return this.mHasFinalValue;
    }

    public void copyFrom(ConstraintAnchor source, HashMap<ConstraintWidget, ConstraintWidget> map) {
        HashSet<ConstraintAnchor> hashSet;
        ConstraintAnchor constraintAnchor = this.mTarget;
        if (!(constraintAnchor == null || (hashSet = constraintAnchor.mDependents) == null)) {
            hashSet.remove(this);
        }
        ConstraintAnchor constraintAnchor2 = source.mTarget;
        if (constraintAnchor2 != null) {
            this.mTarget = map.get(source.mTarget.mOwner).getAnchor(constraintAnchor2.getType());
        } else {
            this.mTarget = null;
        }
        ConstraintAnchor constraintAnchor3 = this.mTarget;
        if (constraintAnchor3 != null) {
            if (constraintAnchor3.mDependents == null) {
                constraintAnchor3.mDependents = new HashSet<>();
            }
            this.mTarget.mDependents.add(this);
        }
        this.mMargin = source.mMargin;
        this.mGoneMargin = source.mGoneMargin;
    }

    public ConstraintAnchor(ConstraintWidget owner, Type type) {
        this.mOwner = owner;
        this.mType = type;
    }

    public SolverVariable getSolverVariable() {
        return this.mSolverVariable;
    }

    public void resetSolverVariable(Cache cache) {
        SolverVariable solverVariable = this.mSolverVariable;
        if (solverVariable == null) {
            this.mSolverVariable = new SolverVariable(SolverVariable.Type.UNRESTRICTED, (String) null);
        } else {
            solverVariable.reset();
        }
    }

    public ConstraintWidget getOwner() {
        return this.mOwner;
    }

    public Type getType() {
        return this.mType;
    }

    public int getMargin() {
        ConstraintAnchor constraintAnchor;
        if (this.mOwner.getVisibility() == 8) {
            return 0;
        }
        if (this.mGoneMargin <= -1 || (constraintAnchor = this.mTarget) == null || constraintAnchor.mOwner.getVisibility() != 8) {
            return this.mMargin;
        }
        return this.mGoneMargin;
    }

    public ConstraintAnchor getTarget() {
        return this.mTarget;
    }

    public void reset() {
        HashSet<ConstraintAnchor> hashSet;
        ConstraintAnchor constraintAnchor = this.mTarget;
        if (!(constraintAnchor == null || (hashSet = constraintAnchor.mDependents) == null)) {
            hashSet.remove(this);
            if (this.mTarget.mDependents.size() == 0) {
                this.mTarget.mDependents = null;
            }
        }
        this.mDependents = null;
        this.mTarget = null;
        this.mMargin = 0;
        this.mGoneMargin = -1;
        this.mHasFinalValue = false;
        this.mFinalValue = 0;
    }

    public boolean connect(ConstraintAnchor toAnchor, int margin, int goneMargin, boolean forceConnection) {
        if (toAnchor == null) {
            reset();
            return true;
        } else if (!forceConnection && !isValidConnection(toAnchor)) {
            return false;
        } else {
            this.mTarget = toAnchor;
            ConstraintAnchor constraintAnchor = this.mTarget;
            if (constraintAnchor.mDependents == null) {
                constraintAnchor.mDependents = new HashSet<>();
            }
            HashSet<ConstraintAnchor> hashSet = this.mTarget.mDependents;
            if (hashSet != null) {
                hashSet.add(this);
            }
            if (margin > 0) {
                this.mMargin = margin;
            } else {
                this.mMargin = 0;
            }
            this.mGoneMargin = goneMargin;
            return true;
        }
    }

    public boolean connect(ConstraintAnchor toAnchor, int margin) {
        return connect(toAnchor, margin, -1, false);
    }

    public boolean isConnected() {
        return this.mTarget != null;
    }

    public boolean isValidConnection(ConstraintAnchor anchor) {
        boolean isCompatible = false;
        if (anchor == null) {
            return false;
        }
        Type target = anchor.getType();
        Type type = this.mType;
        if (target != type) {
            switch (this.mType) {
                case CENTER:
                    if (target == Type.BASELINE || target == Type.CENTER_X || target == Type.CENTER_Y) {
                        return false;
                    }
                    return true;
                case LEFT:
                case RIGHT:
                    boolean isCompatible2 = target == Type.LEFT || target == Type.RIGHT;
                    if (!(anchor.getOwner() instanceof Guideline)) {
                        return isCompatible2;
                    }
                    if (isCompatible2 || target == Type.CENTER_X) {
                        isCompatible = true;
                    }
                    return isCompatible;
                case TOP:
                case BOTTOM:
                    boolean isCompatible3 = target == Type.TOP || target == Type.BOTTOM;
                    if (!(anchor.getOwner() instanceof Guideline)) {
                        return isCompatible3;
                    }
                    if (isCompatible3 || target == Type.CENTER_Y) {
                        isCompatible = true;
                    }
                    return isCompatible;
                case BASELINE:
                case CENTER_X:
                case CENTER_Y:
                case NONE:
                    return false;
                default:
                    throw new AssertionError(this.mType.name());
            }
        } else if (type != Type.BASELINE || (anchor.getOwner().hasBaseline() && getOwner().hasBaseline())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isSideAnchor() {
        switch (this.mType) {
            case CENTER:
            case BASELINE:
            case CENTER_X:
            case CENTER_Y:
            case NONE:
                return false;
            case LEFT:
            case RIGHT:
            case TOP:
            case BOTTOM:
                return true;
            default:
                throw new AssertionError(this.mType.name());
        }
    }

    public boolean isSimilarDimensionConnection(ConstraintAnchor anchor) {
        Type target = anchor.getType();
        if (target == this.mType) {
            return true;
        }
        switch (this.mType) {
            case CENTER:
                if (target != Type.BASELINE) {
                    return true;
                }
                return false;
            case LEFT:
            case RIGHT:
            case CENTER_X:
                if (target == Type.LEFT || target == Type.RIGHT || target == Type.CENTER_X) {
                    return true;
                }
                return false;
            case TOP:
            case BOTTOM:
            case BASELINE:
            case CENTER_Y:
                if (target == Type.TOP || target == Type.BOTTOM || target == Type.CENTER_Y || target == Type.BASELINE) {
                    return true;
                }
                return false;
            case NONE:
                return false;
            default:
                throw new AssertionError(this.mType.name());
        }
    }

    public void setMargin(int margin) {
        if (isConnected()) {
            this.mMargin = margin;
        }
    }

    public void setGoneMargin(int margin) {
        if (isConnected()) {
            this.mGoneMargin = margin;
        }
    }

    public boolean isVerticalAnchor() {
        switch (this.mType) {
            case CENTER:
            case LEFT:
            case RIGHT:
            case CENTER_X:
                return false;
            case TOP:
            case BOTTOM:
            case BASELINE:
            case CENTER_Y:
            case NONE:
                return true;
            default:
                throw new AssertionError(this.mType.name());
        }
    }

    public String toString() {
        return this.mOwner.getDebugName() + ":" + this.mType.toString();
    }

    public boolean isConnectionAllowed(ConstraintWidget target, ConstraintAnchor anchor) {
        return isConnectionAllowed(target);
    }

    public boolean isConnectionAllowed(ConstraintWidget target) {
        if (isConnectionToMe(target, new HashSet<>())) {
            return false;
        }
        ConstraintWidget parent = getOwner().getParent();
        if (parent == target || target.getParent() == parent) {
            return true;
        }
        return false;
    }

    private boolean isConnectionToMe(ConstraintWidget target, HashSet<ConstraintWidget> checked) {
        if (checked.contains(target)) {
            return false;
        }
        checked.add(target);
        if (target == getOwner()) {
            return true;
        }
        ArrayList<ConstraintAnchor> targetAnchors = target.getAnchors();
        int targetAnchorsSize = targetAnchors.size();
        for (int i = 0; i < targetAnchorsSize; i++) {
            ConstraintAnchor anchor = targetAnchors.get(i);
            if (anchor.isSimilarDimensionConnection(this) && anchor.isConnected() && isConnectionToMe(anchor.getTarget().getOwner(), checked)) {
                return true;
            }
        }
        return false;
    }

    public final ConstraintAnchor getOpposite() {
        switch (this.mType) {
            case CENTER:
            case BASELINE:
            case CENTER_X:
            case CENTER_Y:
            case NONE:
                return null;
            case LEFT:
                return this.mOwner.mRight;
            case RIGHT:
                return this.mOwner.mLeft;
            case TOP:
                return this.mOwner.mBottom;
            case BOTTOM:
                return this.mOwner.mTop;
            default:
                throw new AssertionError(this.mType.name());
        }
    }
}
