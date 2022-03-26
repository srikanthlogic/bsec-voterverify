package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
/* loaded from: classes.dex */
public abstract class WidgetRun implements Dependency {
    protected ConstraintWidget.DimensionBehaviour dimensionBehavior;
    public int matchConstraintsType;
    RunGroup runGroup;
    ConstraintWidget widget;
    DimensionDependency dimension = new DimensionDependency(this);
    public int orientation = 0;
    boolean resolved = false;
    public DependencyNode start = new DependencyNode(this);
    public DependencyNode end = new DependencyNode(this);
    protected RunType mRunType = RunType.NONE;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum RunType {
        NONE,
        START,
        END,
        CENTER
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void apply();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void applyToWidget();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void clear();

    abstract void reset();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean supportsWrapComputation();

    public WidgetRun(ConstraintWidget widget) {
        this.widget = widget;
    }

    public boolean isDimensionResolved() {
        return this.dimension.resolved;
    }

    public boolean isCenterConnection() {
        int connections = 0;
        int count = this.start.targets.size();
        for (int i = 0; i < count; i++) {
            if (this.start.targets.get(i).run != this) {
                connections++;
            }
        }
        int count2 = this.end.targets.size();
        for (int i2 = 0; i2 < count2; i2++) {
            if (this.end.targets.get(i2).run != this) {
                connections++;
            }
        }
        return connections >= 2;
    }

    public long wrapSize(int direction) {
        if (!this.dimension.resolved) {
            return 0;
        }
        long size = (long) this.dimension.value;
        if (isCenterConnection()) {
            return size + ((long) (this.start.margin - this.end.margin));
        }
        if (direction == 0) {
            return size + ((long) this.start.margin);
        }
        return size - ((long) this.end.margin);
    }

    protected final DependencyNode getTarget(ConstraintAnchor anchor) {
        if (anchor.mTarget == null) {
            return null;
        }
        ConstraintWidget targetWidget = anchor.mTarget.mOwner;
        int i = AnonymousClass1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[anchor.mTarget.mType.ordinal()];
        if (i == 1) {
            return targetWidget.horizontalRun.start;
        }
        if (i == 2) {
            return targetWidget.horizontalRun.end;
        }
        if (i == 3) {
            return targetWidget.verticalRun.start;
        }
        if (i == 4) {
            return targetWidget.verticalRun.baseline;
        }
        if (i != 5) {
            return null;
        }
        return targetWidget.verticalRun.end;
    }

    /* renamed from: androidx.constraintlayout.solver.widgets.analyzer.WidgetRun$1 */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type = new int[ConstraintAnchor.Type.values().length];

        static {
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.TOP.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.BASELINE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.BOTTOM.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    protected void updateRunCenter(Dependency dependency, ConstraintAnchor startAnchor, ConstraintAnchor endAnchor, int orientation) {
        float bias;
        DependencyNode startTarget = getTarget(startAnchor);
        DependencyNode endTarget = getTarget(endAnchor);
        if (startTarget.resolved && endTarget.resolved) {
            int startPos = startTarget.value + startAnchor.getMargin();
            int endPos = endTarget.value - endAnchor.getMargin();
            int distance = endPos - startPos;
            if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                resolveDimension(orientation, distance);
            }
            if (this.dimension.resolved) {
                if (this.dimension.value == distance) {
                    this.start.resolve(startPos);
                    this.end.resolve(endPos);
                    return;
                }
                ConstraintWidget constraintWidget = this.widget;
                if (orientation == 0) {
                    bias = constraintWidget.getHorizontalBiasPercent();
                } else {
                    bias = constraintWidget.getVerticalBiasPercent();
                }
                if (startTarget == endTarget) {
                    startPos = startTarget.value;
                    endPos = endTarget.value;
                    bias = 0.5f;
                }
                this.start.resolve((int) (((float) startPos) + 0.5f + (((float) ((endPos - startPos) - this.dimension.value)) * bias)));
                this.end.resolve(this.start.value + this.dimension.value);
            }
        }
    }

    private void resolveDimension(int orientation, int distance) {
        int value;
        int i = this.matchConstraintsType;
        if (i == 0) {
            this.dimension.resolve(getLimitedDimension(distance, orientation));
        } else if (i == 1) {
            this.dimension.resolve(Math.min(getLimitedDimension(this.dimension.wrapValue, orientation), distance));
        } else if (i == 2) {
            ConstraintWidget parent = this.widget.getParent();
            if (parent != null) {
                WidgetRun run = orientation == 0 ? parent.horizontalRun : parent.verticalRun;
                if (run.dimension.resolved) {
                    ConstraintWidget constraintWidget = this.widget;
                    this.dimension.resolve(getLimitedDimension((int) ((((float) run.dimension.value) * (orientation == 0 ? constraintWidget.mMatchConstraintPercentWidth : constraintWidget.mMatchConstraintPercentHeight)) + 0.5f), orientation));
                }
            }
        } else if (i == 3) {
            if (this.widget.horizontalRun.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || this.widget.horizontalRun.matchConstraintsType != 3 || this.widget.verticalRun.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || this.widget.verticalRun.matchConstraintsType != 3) {
                ConstraintWidget constraintWidget2 = this.widget;
                WidgetRun run2 = orientation == 0 ? constraintWidget2.verticalRun : constraintWidget2.horizontalRun;
                if (run2.dimension.resolved) {
                    float ratio = this.widget.getDimensionRatio();
                    if (orientation == 1) {
                        value = (int) ((((float) run2.dimension.value) / ratio) + 0.5f);
                    } else {
                        value = (int) ((((float) run2.dimension.value) * ratio) + 0.5f);
                    }
                    this.dimension.resolve(value);
                }
            }
        }
    }

    protected void updateRunStart(Dependency dependency) {
    }

    protected void updateRunEnd(Dependency dependency) {
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.Dependency
    public void update(Dependency dependency) {
    }

    protected final int getLimitedDimension(int dimension, int orientation) {
        if (orientation == 0) {
            int max = this.widget.mMatchConstraintMaxWidth;
            int value = Math.max(this.widget.mMatchConstraintMinWidth, dimension);
            if (max > 0) {
                value = Math.min(max, dimension);
            }
            if (value != dimension) {
                return value;
            }
            return dimension;
        }
        int max2 = this.widget.mMatchConstraintMaxHeight;
        int value2 = Math.max(this.widget.mMatchConstraintMinHeight, dimension);
        if (max2 > 0) {
            value2 = Math.min(max2, dimension);
        }
        if (value2 != dimension) {
            return value2;
        }
        return dimension;
    }

    protected final DependencyNode getTarget(ConstraintAnchor anchor, int orientation) {
        if (anchor.mTarget == null) {
            return null;
        }
        ConstraintWidget targetWidget = anchor.mTarget.mOwner;
        WidgetRun run = orientation == 0 ? targetWidget.horizontalRun : targetWidget.verticalRun;
        int i = AnonymousClass1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[anchor.mTarget.mType.ordinal()];
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    if (i != 5) {
                        return null;
                    }
                }
            }
            return run.end;
        }
        return run.start;
    }

    protected final void addTarget(DependencyNode node, DependencyNode target, int margin) {
        node.targets.add(target);
        node.margin = margin;
        target.dependencies.add(node);
    }

    protected final void addTarget(DependencyNode node, DependencyNode target, int marginFactor, DimensionDependency dimensionDependency) {
        node.targets.add(target);
        node.targets.add(this.dimension);
        node.marginFactor = marginFactor;
        node.marginDependency = dimensionDependency;
        target.dependencies.add(node);
        dimensionDependency.dependencies.add(node);
    }

    public long getWrapDimension() {
        if (this.dimension.resolved) {
            return (long) this.dimension.value;
        }
        return 0;
    }

    public boolean isResolved() {
        return this.resolved;
    }
}
