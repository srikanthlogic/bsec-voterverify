package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.Cache;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class WidgetContainer extends ConstraintWidget {
    public ArrayList<ConstraintWidget> mChildren = new ArrayList<>();

    public WidgetContainer() {
    }

    public WidgetContainer(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public WidgetContainer(int width, int height) {
        super(width, height);
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void reset() {
        this.mChildren.clear();
        super.reset();
    }

    public void add(ConstraintWidget widget) {
        this.mChildren.add(widget);
        if (widget.getParent() != null) {
            ((WidgetContainer) widget.getParent()).remove(widget);
        }
        widget.setParent(this);
    }

    public void add(ConstraintWidget... widgets) {
        for (ConstraintWidget constraintWidget : widgets) {
            add(constraintWidget);
        }
    }

    public void remove(ConstraintWidget widget) {
        this.mChildren.remove(widget);
        widget.reset();
    }

    public ArrayList<ConstraintWidget> getChildren() {
        return this.mChildren;
    }

    /* JADX INFO: Multiple debug info for r4v0 'this'  androidx.constraintlayout.solver.widgets.WidgetContainer: [D('container' androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer), D('item' androidx.constraintlayout.solver.widgets.ConstraintWidget)] */
    public ConstraintWidgetContainer getRootConstraintContainer() {
        ConstraintWidget parent = getParent();
        ConstraintWidgetContainer container = null;
        if (this instanceof ConstraintWidgetContainer) {
            container = (ConstraintWidgetContainer) this;
        }
        while (parent != null) {
            parent = parent.getParent();
            if (parent instanceof ConstraintWidgetContainer) {
                container = (ConstraintWidgetContainer) parent;
            }
        }
        return container;
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void setOffset(int x, int y) {
        super.setOffset(x, y);
        int count = this.mChildren.size();
        for (int i = 0; i < count; i++) {
            this.mChildren.get(i).setOffset(getRootX(), getRootY());
        }
    }

    public void layout() {
        ArrayList<ConstraintWidget> arrayList = this.mChildren;
        if (arrayList != null) {
            int count = arrayList.size();
            for (int i = 0; i < count; i++) {
                ConstraintWidget widget = this.mChildren.get(i);
                if (widget instanceof WidgetContainer) {
                    ((WidgetContainer) widget).layout();
                }
            }
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void resetSolverVariables(Cache cache) {
        super.resetSolverVariables(cache);
        int count = this.mChildren.size();
        for (int i = 0; i < count; i++) {
            this.mChildren.get(i).resetSolverVariables(cache);
        }
    }

    public void removeAllChildren() {
        this.mChildren.clear();
    }
}
