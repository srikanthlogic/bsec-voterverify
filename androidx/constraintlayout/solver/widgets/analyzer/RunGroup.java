package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public class RunGroup {
    public static final int BASELINE;
    public static final int END;
    public static final int START;
    public static int index;
    int direction;
    WidgetRun firstRun;
    int groupIndex;
    WidgetRun lastRun;
    public int position = 0;
    public boolean dual = false;
    ArrayList<WidgetRun> runs = new ArrayList<>();

    public RunGroup(WidgetRun run, int dir) {
        this.firstRun = null;
        this.lastRun = null;
        this.groupIndex = 0;
        int i = index;
        this.groupIndex = i;
        index = i + 1;
        this.firstRun = run;
        this.lastRun = run;
        this.direction = dir;
    }

    public void add(WidgetRun run) {
        this.runs.add(run);
        this.lastRun = run;
    }

    private long traverseStart(DependencyNode node, long startPosition) {
        WidgetRun run = node.run;
        if (run instanceof HelperReferences) {
            return startPosition;
        }
        long position = startPosition;
        int count = node.dependencies.size();
        for (int i = 0; i < count; i++) {
            Dependency dependency = node.dependencies.get(i);
            if (dependency instanceof DependencyNode) {
                DependencyNode nextNode = (DependencyNode) dependency;
                if (nextNode.run != run) {
                    position = Math.max(position, traverseStart(nextNode, ((long) nextNode.margin) + startPosition));
                }
            }
        }
        if (node != run.start) {
            return position;
        }
        long dimension = run.getWrapDimension();
        return Math.max(Math.max(position, traverseStart(run.end, startPosition + dimension)), (startPosition + dimension) - ((long) run.end.margin));
    }

    private long traverseEnd(DependencyNode node, long startPosition) {
        WidgetRun run = node.run;
        if (run instanceof HelperReferences) {
            return startPosition;
        }
        long position = startPosition;
        int count = node.dependencies.size();
        for (int i = 0; i < count; i++) {
            Dependency dependency = node.dependencies.get(i);
            if (dependency instanceof DependencyNode) {
                DependencyNode nextNode = (DependencyNode) dependency;
                if (nextNode.run != run) {
                    position = Math.min(position, traverseEnd(nextNode, ((long) nextNode.margin) + startPosition));
                }
            }
        }
        if (node != run.end) {
            return position;
        }
        long dimension = run.getWrapDimension();
        return Math.min(Math.min(position, traverseEnd(run.start, startPosition - dimension)), (startPosition - dimension) - ((long) run.start.margin));
    }

    /* JADX INFO: Multiple debug info for r1v10 long: [D('dimension' long), D('minPosition' long)] */
    /* JADX INFO: Multiple debug info for r1v14 long: [D('maxPosition' long), D('dimension' long)] */
    public long computeWrapSize(ConstraintWidgetContainer container, int orientation) {
        long gap;
        WidgetRun widgetRun = this.firstRun;
        if (widgetRun instanceof ChainRun) {
            if (((ChainRun) widgetRun).orientation != orientation) {
                return 0;
            }
        } else if (orientation == 0) {
            if (!(widgetRun instanceof HorizontalWidgetRun)) {
                return 0;
            }
        } else if (!(widgetRun instanceof VerticalWidgetRun)) {
            return 0;
        }
        DependencyNode containerStart = orientation == 0 ? container.horizontalRun.start : container.verticalRun.start;
        DependencyNode containerEnd = orientation == 0 ? container.horizontalRun.end : container.verticalRun.end;
        boolean runWithStartTarget = this.firstRun.start.targets.contains(containerStart);
        boolean runWithEndTarget = this.firstRun.end.targets.contains(containerEnd);
        long dimension = this.firstRun.getWrapDimension();
        if (runWithStartTarget && runWithEndTarget) {
            long maxPosition = traverseStart(this.firstRun.start, 0);
            long minPosition = traverseEnd(this.firstRun.end, 0);
            long endGap = maxPosition - dimension;
            if (endGap >= ((long) (-this.firstRun.end.margin))) {
                endGap += (long) this.firstRun.end.margin;
            }
            long startGap = ((-minPosition) - dimension) - ((long) this.firstRun.start.margin);
            if (startGap >= ((long) this.firstRun.start.margin)) {
                startGap -= (long) this.firstRun.start.margin;
            }
            float bias = this.firstRun.widget.getBiasPercent(orientation);
            if (bias > 0.0f) {
                gap = (long) ((((float) startGap) / bias) + (((float) endGap) / (1.0f - bias)));
            } else {
                gap = 0;
            }
            return (((long) this.firstRun.start.margin) + ((((long) ((((float) gap) * bias) + 0.5f)) + dimension) + ((long) ((((float) gap) * (1.0f - bias)) + 0.5f)))) - ((long) this.firstRun.end.margin);
        } else if (runWithStartTarget) {
            return Math.max(traverseStart(this.firstRun.start, (long) this.firstRun.start.margin), ((long) this.firstRun.start.margin) + dimension);
        } else {
            if (!runWithEndTarget) {
                return (((long) this.firstRun.start.margin) + this.firstRun.getWrapDimension()) - ((long) this.firstRun.end.margin);
            }
            return Math.max(-traverseEnd(this.firstRun.end, (long) this.firstRun.end.margin), ((long) (-this.firstRun.end.margin)) + dimension);
        }
    }

    private boolean defineTerminalWidget(WidgetRun run, int orientation) {
        if (!run.widget.isTerminalWidget[orientation]) {
            return false;
        }
        for (Dependency dependency : run.start.dependencies) {
            if (dependency instanceof DependencyNode) {
                DependencyNode node = (DependencyNode) dependency;
                if (node.run != run && node == node.run.start) {
                    if (run instanceof ChainRun) {
                        Iterator<WidgetRun> it = ((ChainRun) run).widgets.iterator();
                        while (it.hasNext()) {
                            defineTerminalWidget(it.next(), orientation);
                        }
                    } else if (!(run instanceof HelperReferences)) {
                        run.widget.isTerminalWidget[orientation] = false;
                    }
                    defineTerminalWidget(node.run, orientation);
                }
            }
        }
        for (Dependency dependency2 : run.end.dependencies) {
            if (dependency2 instanceof DependencyNode) {
                DependencyNode node2 = (DependencyNode) dependency2;
                if (node2.run != run && node2 == node2.run.start) {
                    if (run instanceof ChainRun) {
                        Iterator<WidgetRun> it2 = ((ChainRun) run).widgets.iterator();
                        while (it2.hasNext()) {
                            defineTerminalWidget(it2.next(), orientation);
                        }
                    } else if (!(run instanceof HelperReferences)) {
                        run.widget.isTerminalWidget[orientation] = false;
                    }
                    defineTerminalWidget(node2.run, orientation);
                }
            }
        }
        return false;
    }

    public void defineTerminalWidgets(boolean horizontalCheck, boolean verticalCheck) {
        if (horizontalCheck) {
            WidgetRun widgetRun = this.firstRun;
            if (widgetRun instanceof HorizontalWidgetRun) {
                defineTerminalWidget(widgetRun, 0);
            }
        }
        if (verticalCheck) {
            WidgetRun widgetRun2 = this.firstRun;
            if (widgetRun2 instanceof VerticalWidgetRun) {
                defineTerminalWidget(widgetRun2, 1);
            }
        }
    }
}
