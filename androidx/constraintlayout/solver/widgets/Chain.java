package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.ArrayRow;
import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class Chain {
    private static final boolean DEBUG = false;
    public static final boolean USE_CHAIN_OPTIMIZATION = false;

    public static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem system, ArrayList<ConstraintWidget> widgets, int orientation) {
        ChainHead[] chainsArray;
        int chainsSize;
        int offset;
        if (orientation == 0) {
            offset = 0;
            chainsSize = constraintWidgetContainer.mHorizontalChainsSize;
            chainsArray = constraintWidgetContainer.mHorizontalChainsArray;
        } else {
            offset = 2;
            chainsSize = constraintWidgetContainer.mVerticalChainsSize;
            chainsArray = constraintWidgetContainer.mVerticalChainsArray;
        }
        for (int i = 0; i < chainsSize; i++) {
            ChainHead first = chainsArray[i];
            first.define();
            if (widgets == null || (widgets != null && widgets.contains(first.mFirst))) {
                applyChainConstraints(constraintWidgetContainer, system, orientation, offset, first);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:306:0x0680  */
    /* JADX WARN: Removed duplicated region for block: B:307:0x0685  */
    /* JADX WARN: Removed duplicated region for block: B:310:0x068c  */
    /* JADX WARN: Removed duplicated region for block: B:311:0x0691  */
    /* JADX WARN: Removed duplicated region for block: B:313:0x0694  */
    /* JADX WARN: Removed duplicated region for block: B:318:0x06ac  */
    /* JADX WARN: Removed duplicated region for block: B:320:0x06b0  */
    /* JADX WARN: Removed duplicated region for block: B:321:0x06bc  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    static void applyChainConstraints(ConstraintWidgetContainer container, LinearSystem system, int orientation, int offset, ChainHead chainHead) {
        boolean isChainSpread;
        boolean isChainSpreadInside;
        boolean isChainPacked;
        boolean done;
        ConstraintWidget widget;
        ConstraintWidget widget2;
        ArrayList<ConstraintWidget> listMatchConstraints;
        SolverVariable beginTarget;
        SolverVariable endTarget;
        ConstraintAnchor end;
        ConstraintAnchor end2;
        ConstraintAnchor endTarget2;
        int endPointsStrength;
        ConstraintWidget widget3;
        ConstraintWidget previousVisibleWidget;
        ConstraintWidget next;
        ConstraintWidget next2;
        SolverVariable beginNextTarget;
        SolverVariable beginNext;
        ConstraintAnchor beginNextAnchor;
        int strength;
        ConstraintWidget next3;
        int i;
        ConstraintWidget widget4;
        ConstraintWidget previousVisibleWidget2;
        ConstraintWidget next4;
        int nextMargin;
        SolverVariable beginTarget2;
        SolverVariable beginNextTarget2;
        SolverVariable beginNext2;
        ConstraintAnchor beginNextAnchor2;
        int margin1;
        int margin2;
        int strength2;
        float bias;
        ConstraintWidget widget5;
        int count;
        ArrayList<ConstraintWidget> listMatchConstraints2;
        ConstraintWidget firstMatchConstraintsWidget;
        int margin;
        ConstraintWidget previousMatchConstraintsWidget;
        float totalWeights;
        int strength3;
        ConstraintWidget next5;
        ConstraintWidget first = chainHead.mFirst;
        ConstraintWidget last = chainHead.mLast;
        ConstraintWidget firstVisibleWidget = chainHead.mFirstVisibleWidget;
        ConstraintWidget lastVisibleWidget = chainHead.mLastVisibleWidget;
        ConstraintWidget head = chainHead.mHead;
        float totalWeights2 = chainHead.mTotalWeight;
        ConstraintWidget firstMatchConstraintsWidget2 = chainHead.mFirstMatchConstraintWidget;
        ConstraintWidget previousMatchConstraintsWidget2 = chainHead.mLastMatchConstraintWidget;
        boolean isWrapContent = container.mListDimensionBehaviors[orientation] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (orientation == 0) {
            isChainSpread = head.mHorizontalChainStyle == 0;
            isChainSpreadInside = head.mHorizontalChainStyle == 1;
            isChainPacked = head.mHorizontalChainStyle == 2;
            widget = first;
            done = false;
        } else {
            isChainSpread = head.mVerticalChainStyle == 0;
            isChainSpreadInside = head.mVerticalChainStyle == 1;
            isChainPacked = head.mVerticalChainStyle == 2;
            widget = first;
            done = false;
        }
        while (!done) {
            ConstraintAnchor begin = widget.mListAnchors[offset];
            int strength4 = 4;
            if (isChainPacked) {
                strength4 = 1;
            }
            int margin3 = begin.getMargin();
            boolean isSpreadOnly = widget.mListDimensionBehaviors[orientation] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && widget.mResolvedMatchConstraintDefault[orientation] == 0;
            if (begin.mTarget == null || widget == first) {
                margin = margin3;
            } else {
                margin = margin3 + begin.mTarget.getMargin();
            }
            if (!(!isChainPacked || widget == first || widget == firstVisibleWidget)) {
                strength4 = 8;
            }
            if (begin.mTarget != null) {
                if (widget == firstVisibleWidget) {
                    totalWeights = totalWeights2;
                    previousMatchConstraintsWidget = previousMatchConstraintsWidget2;
                    system.addGreaterThan(begin.mSolverVariable, begin.mTarget.mSolverVariable, margin, 6);
                } else {
                    totalWeights = totalWeights2;
                    previousMatchConstraintsWidget = previousMatchConstraintsWidget2;
                    system.addGreaterThan(begin.mSolverVariable, begin.mTarget.mSolverVariable, margin, 8);
                }
                if (!isSpreadOnly || isChainPacked) {
                    strength3 = strength4;
                } else {
                    strength3 = 5;
                }
                system.addEquality(begin.mSolverVariable, begin.mTarget.mSolverVariable, margin, strength3);
            } else {
                totalWeights = totalWeights2;
                previousMatchConstraintsWidget = previousMatchConstraintsWidget2;
                strength3 = strength4;
            }
            if (isWrapContent) {
                if (widget.getVisibility() != 8 && widget.mListDimensionBehaviors[orientation] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    system.addGreaterThan(widget.mListAnchors[offset + 1].mSolverVariable, widget.mListAnchors[offset].mSolverVariable, 0, 5);
                }
                system.addGreaterThan(widget.mListAnchors[offset].mSolverVariable, container.mListAnchors[offset].mSolverVariable, 0, 8);
            }
            ConstraintAnchor nextAnchor = widget.mListAnchors[offset + 1].mTarget;
            if (nextAnchor != null) {
                ConstraintWidget next6 = nextAnchor.mOwner;
                if (next6.mListAnchors[offset].mTarget == null || next6.mListAnchors[offset].mTarget.mOwner != widget) {
                    next5 = null;
                } else {
                    next5 = next6;
                }
            } else {
                next5 = null;
            }
            if (next5 != null) {
                widget = next5;
            } else {
                done = true;
            }
            totalWeights2 = totalWeights;
            previousMatchConstraintsWidget2 = previousMatchConstraintsWidget;
        }
        float totalWeights3 = totalWeights2;
        if (!(lastVisibleWidget == null || last.mListAnchors[offset + 1].mTarget == null)) {
            ConstraintAnchor end3 = lastVisibleWidget.mListAnchors[offset + 1];
            if ((lastVisibleWidget.mListDimensionBehaviors[orientation] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && lastVisibleWidget.mResolvedMatchConstraintDefault[orientation] == 0) && !isChainPacked && end3.mTarget.mOwner == container) {
                system.addEquality(end3.mSolverVariable, end3.mTarget.mSolverVariable, -end3.getMargin(), 5);
            } else if (isChainPacked && end3.mTarget.mOwner == container) {
                system.addEquality(end3.mSolverVariable, end3.mTarget.mSolverVariable, -end3.getMargin(), 4);
            }
            system.addLowerThan(end3.mSolverVariable, last.mListAnchors[offset + 1].mTarget.mSolverVariable, -end3.getMargin(), 6);
        }
        if (isWrapContent) {
            system.addGreaterThan(container.mListAnchors[offset + 1].mSolverVariable, last.mListAnchors[offset + 1].mSolverVariable, last.mListAnchors[offset + 1].getMargin(), 8);
        }
        ArrayList<ConstraintWidget> listMatchConstraints3 = chainHead.mWeightedMatchConstraintsWidgets;
        if (listMatchConstraints3 != null) {
            int count2 = listMatchConstraints3.size();
            if (count2 > 1) {
                ConstraintWidget lastMatch = null;
                float lastWeight = 0.0f;
                if (chainHead.mHasUndefinedWeights && !chainHead.mHasComplexMatchWeights) {
                    totalWeights3 = (float) chainHead.mWidgetsMatchCount;
                }
                int i2 = 0;
                while (i2 < count2) {
                    ConstraintWidget match = listMatchConstraints3.get(i2);
                    float currentWeight = match.mWeight[orientation];
                    if (currentWeight >= 0.0f) {
                        count = count2;
                        widget5 = widget;
                        listMatchConstraints2 = listMatchConstraints3;
                    } else if (chainHead.mHasComplexMatchWeights) {
                        count = count2;
                        widget5 = widget;
                        listMatchConstraints2 = listMatchConstraints3;
                        system.addEquality(match.mListAnchors[offset + 1].mSolverVariable, match.mListAnchors[offset].mSolverVariable, 0, 4);
                        firstMatchConstraintsWidget = firstMatchConstraintsWidget2;
                        i2++;
                        firstMatchConstraintsWidget2 = firstMatchConstraintsWidget;
                        listMatchConstraints3 = listMatchConstraints2;
                        count2 = count;
                        widget = widget5;
                    } else {
                        count = count2;
                        widget5 = widget;
                        listMatchConstraints2 = listMatchConstraints3;
                        currentWeight = 1.0f;
                    }
                    if (currentWeight == 0.0f) {
                        firstMatchConstraintsWidget = firstMatchConstraintsWidget2;
                        system.addEquality(match.mListAnchors[offset + 1].mSolverVariable, match.mListAnchors[offset].mSolverVariable, 0, 8);
                    } else {
                        firstMatchConstraintsWidget = firstMatchConstraintsWidget2;
                        if (lastMatch != null) {
                            SolverVariable begin2 = lastMatch.mListAnchors[offset].mSolverVariable;
                            SolverVariable end4 = lastMatch.mListAnchors[offset + 1].mSolverVariable;
                            SolverVariable nextBegin = match.mListAnchors[offset].mSolverVariable;
                            SolverVariable nextEnd = match.mListAnchors[offset + 1].mSolverVariable;
                            ArrayRow row = system.createRow();
                            row.createRowEqualMatchDimensions(lastWeight, totalWeights3, currentWeight, begin2, end4, nextBegin, nextEnd);
                            system.addConstraint(row);
                        }
                        lastWeight = currentWeight;
                        lastMatch = match;
                    }
                    i2++;
                    firstMatchConstraintsWidget2 = firstMatchConstraintsWidget;
                    listMatchConstraints3 = listMatchConstraints2;
                    count2 = count;
                    widget = widget5;
                }
                widget2 = widget;
                listMatchConstraints = listMatchConstraints3;
            } else {
                widget2 = widget;
                listMatchConstraints = listMatchConstraints3;
            }
        } else {
            widget2 = widget;
            listMatchConstraints = listMatchConstraints3;
        }
        if (firstVisibleWidget != null && (firstVisibleWidget == lastVisibleWidget || isChainPacked)) {
            ConstraintAnchor begin3 = first.mListAnchors[offset];
            ConstraintAnchor end5 = last.mListAnchors[offset + 1];
            SolverVariable beginTarget3 = begin3.mTarget != null ? begin3.mTarget.mSolverVariable : null;
            SolverVariable endTarget3 = end5.mTarget != null ? end5.mTarget.mSolverVariable : null;
            ConstraintAnchor begin4 = firstVisibleWidget.mListAnchors[offset];
            ConstraintAnchor end6 = lastVisibleWidget.mListAnchors[offset + 1];
            if (beginTarget3 != null && endTarget3 != null) {
                if (orientation == 0) {
                    bias = head.mHorizontalBiasPercent;
                } else {
                    bias = head.mVerticalBiasPercent;
                }
                system.addCentering(begin4.mSolverVariable, beginTarget3, begin4.getMargin(), bias, endTarget3, end6.mSolverVariable, end6.getMargin(), 7);
            }
            if (!((isChainSpread && !isChainSpreadInside) || firstVisibleWidget == null || firstVisibleWidget == lastVisibleWidget)) {
                ConstraintAnchor begin5 = firstVisibleWidget.mListAnchors[offset];
                ConstraintAnchor end7 = lastVisibleWidget.mListAnchors[offset + 1];
                beginTarget = begin5.mTarget == null ? begin5.mTarget.mSolverVariable : null;
                SolverVariable endTarget4 = end7.mTarget == null ? end7.mTarget.mSolverVariable : null;
                if (last == lastVisibleWidget) {
                    ConstraintAnchor realEnd = last.mListAnchors[offset + 1];
                    endTarget = realEnd.mTarget != null ? realEnd.mTarget.mSolverVariable : null;
                } else {
                    endTarget = endTarget4;
                }
                if (firstVisibleWidget != lastVisibleWidget) {
                    begin5 = firstVisibleWidget.mListAnchors[offset];
                    end = firstVisibleWidget.mListAnchors[offset + 1];
                } else {
                    end = end7;
                }
                if (beginTarget == null && endTarget != null) {
                    int beginMargin = begin5.getMargin();
                    if (lastVisibleWidget == null) {
                        lastVisibleWidget = last;
                    }
                    system.addCentering(begin5.mSolverVariable, beginTarget, beginMargin, 0.5f, endTarget, end.mSolverVariable, lastVisibleWidget.mListAnchors[offset + 1].getMargin(), 5);
                    return;
                }
            }
            return;
        }
        if (!isChainSpread || firstVisibleWidget == null) {
            int i3 = 8;
            if (isChainSpreadInside && firstVisibleWidget != null) {
                boolean applyFixedEquality = chainHead.mWidgetsMatchCount > 0 && chainHead.mWidgetsCount == chainHead.mWidgetsMatchCount;
                ConstraintWidget widget6 = firstVisibleWidget;
                ConstraintWidget previousVisibleWidget3 = firstVisibleWidget;
                while (widget6 != null) {
                    ConstraintWidget next7 = widget6.mNextChainWidget[orientation];
                    while (next7 != null && next7.getVisibility() == i3) {
                        next7 = next7.mNextChainWidget[orientation];
                    }
                    if (widget6 == firstVisibleWidget || widget6 == lastVisibleWidget || next7 == null) {
                        previousVisibleWidget = previousVisibleWidget3;
                        widget3 = widget6;
                        next = next7;
                    } else {
                        if (next7 == lastVisibleWidget) {
                            next2 = null;
                        } else {
                            next2 = next7;
                        }
                        ConstraintAnchor beginAnchor = widget6.mListAnchors[offset];
                        SolverVariable begin6 = beginAnchor.mSolverVariable;
                        if (beginAnchor.mTarget != null) {
                            SolverVariable solverVariable = beginAnchor.mTarget.mSolverVariable;
                        }
                        SolverVariable beginTarget4 = previousVisibleWidget3.mListAnchors[offset + 1].mSolverVariable;
                        SolverVariable beginNext3 = null;
                        int beginMargin2 = beginAnchor.getMargin();
                        int nextMargin2 = widget6.mListAnchors[offset + 1].getMargin();
                        if (next2 != null) {
                            ConstraintAnchor beginNextAnchor3 = next2.mListAnchors[offset];
                            SolverVariable beginNext4 = beginNextAnchor3.mSolverVariable;
                            beginNextTarget = beginNextAnchor3.mTarget != null ? beginNextAnchor3.mTarget.mSolverVariable : null;
                            beginNext = beginNext4;
                            beginNextAnchor = beginNextAnchor3;
                        } else {
                            ConstraintAnchor beginNextAnchor4 = lastVisibleWidget.mListAnchors[offset];
                            if (beginNextAnchor4 != null) {
                                beginNext3 = beginNextAnchor4.mSolverVariable;
                            }
                            beginNextAnchor = beginNextAnchor4;
                            beginNextTarget = widget6.mListAnchors[offset + 1].mSolverVariable;
                            beginNext = beginNext3;
                        }
                        if (beginNextAnchor != null) {
                            nextMargin2 += beginNextAnchor.getMargin();
                        }
                        if (previousVisibleWidget3 != null) {
                            beginMargin2 += previousVisibleWidget3.mListAnchors[offset + 1].getMargin();
                        }
                        if (applyFixedEquality) {
                            strength = 8;
                        } else {
                            strength = 4;
                        }
                        if (begin6 == null || beginTarget4 == null || beginNext == null || beginNextTarget == null) {
                            next3 = next2;
                            previousVisibleWidget = previousVisibleWidget3;
                            widget3 = widget6;
                        } else {
                            next3 = next2;
                            previousVisibleWidget = previousVisibleWidget3;
                            widget3 = widget6;
                            system.addCentering(begin6, beginTarget4, beginMargin2, 0.5f, beginNext, beginNextTarget, nextMargin2, strength);
                        }
                        next = next3;
                    }
                    if (widget3.getVisibility() != 8) {
                        previousVisibleWidget3 = widget3;
                    } else {
                        previousVisibleWidget3 = previousVisibleWidget;
                    }
                    widget6 = next;
                    i3 = 8;
                }
                ConstraintAnchor begin7 = firstVisibleWidget.mListAnchors[offset];
                ConstraintAnchor beginTarget5 = first.mListAnchors[offset].mTarget;
                ConstraintAnchor end8 = lastVisibleWidget.mListAnchors[offset + 1];
                ConstraintAnchor endTarget5 = last.mListAnchors[offset + 1].mTarget;
                if (beginTarget5 == null) {
                    endPointsStrength = 5;
                    endTarget2 = endTarget5;
                    end2 = end8;
                } else if (firstVisibleWidget != lastVisibleWidget) {
                    system.addEquality(begin7.mSolverVariable, beginTarget5.mSolverVariable, begin7.getMargin(), 5);
                    endPointsStrength = 5;
                    endTarget2 = endTarget5;
                    end2 = end8;
                } else if (endTarget5 != null) {
                    endPointsStrength = 5;
                    endTarget2 = endTarget5;
                    end2 = end8;
                    system.addCentering(begin7.mSolverVariable, beginTarget5.mSolverVariable, begin7.getMargin(), 0.5f, end8.mSolverVariable, endTarget5.mSolverVariable, end8.getMargin(), 5);
                } else {
                    endPointsStrength = 5;
                    endTarget2 = endTarget5;
                    end2 = end8;
                }
                if (endTarget2 != null && firstVisibleWidget != lastVisibleWidget) {
                    system.addEquality(end2.mSolverVariable, endTarget2.mSolverVariable, -end2.getMargin(), endPointsStrength);
                }
            }
        } else {
            boolean applyFixedEquality2 = chainHead.mWidgetsMatchCount > 0 && chainHead.mWidgetsCount == chainHead.mWidgetsMatchCount;
            ConstraintWidget previousVisibleWidget4 = firstVisibleWidget;
            for (ConstraintWidget widget7 = firstVisibleWidget; widget7 != null; widget7 = next4) {
                ConstraintWidget next8 = widget7.mNextChainWidget[orientation];
                while (true) {
                    if (next8 == null) {
                        i = 8;
                        break;
                    }
                    i = 8;
                    if (next8.getVisibility() != 8) {
                        break;
                    }
                    next8 = next8.mNextChainWidget[orientation];
                }
                if (next8 != null || widget7 == lastVisibleWidget) {
                    ConstraintAnchor beginAnchor2 = widget7.mListAnchors[offset];
                    SolverVariable begin8 = beginAnchor2.mSolverVariable;
                    SolverVariable beginTarget6 = beginAnchor2.mTarget != null ? beginAnchor2.mTarget.mSolverVariable : null;
                    if (previousVisibleWidget4 != widget7) {
                        beginTarget2 = previousVisibleWidget4.mListAnchors[offset + 1].mSolverVariable;
                    } else if (widget7 == firstVisibleWidget && previousVisibleWidget4 == widget7) {
                        beginTarget2 = first.mListAnchors[offset].mTarget != null ? first.mListAnchors[offset].mTarget.mSolverVariable : null;
                    } else {
                        beginTarget2 = beginTarget6;
                    }
                    SolverVariable beginNext5 = null;
                    int beginMargin3 = beginAnchor2.getMargin();
                    int nextMargin3 = widget7.mListAnchors[offset + 1].getMargin();
                    if (next8 != null) {
                        ConstraintAnchor beginNextAnchor5 = next8.mListAnchors[offset];
                        beginNextAnchor2 = beginNextAnchor5;
                        beginNext2 = beginNextAnchor5.mSolverVariable;
                        beginNextTarget2 = widget7.mListAnchors[offset + 1].mSolverVariable;
                    } else {
                        ConstraintAnchor beginNextAnchor6 = last.mListAnchors[offset + 1].mTarget;
                        if (beginNextAnchor6 != null) {
                            beginNext5 = beginNextAnchor6.mSolverVariable;
                        }
                        beginNextAnchor2 = beginNextAnchor6;
                        beginNext2 = beginNext5;
                        beginNextTarget2 = widget7.mListAnchors[offset + 1].mSolverVariable;
                    }
                    if (beginNextAnchor2 != null) {
                        nextMargin3 += beginNextAnchor2.getMargin();
                    }
                    if (previousVisibleWidget4 != null) {
                        beginMargin3 += previousVisibleWidget4.mListAnchors[offset + 1].getMargin();
                    }
                    if (begin8 == null || beginTarget2 == null || beginNext2 == null || beginNextTarget2 == null) {
                        next4 = next8;
                        previousVisibleWidget2 = previousVisibleWidget4;
                        widget4 = widget7;
                        nextMargin = 8;
                    } else {
                        if (widget7 == firstVisibleWidget) {
                            margin1 = firstVisibleWidget.mListAnchors[offset].getMargin();
                        } else {
                            margin1 = beginMargin3;
                        }
                        if (widget7 == lastVisibleWidget) {
                            margin2 = lastVisibleWidget.mListAnchors[offset + 1].getMargin();
                        } else {
                            margin2 = nextMargin3;
                        }
                        if (applyFixedEquality2) {
                            strength2 = 8;
                        } else {
                            strength2 = 5;
                        }
                        nextMargin = 8;
                        next4 = next8;
                        previousVisibleWidget2 = previousVisibleWidget4;
                        widget4 = widget7;
                        system.addCentering(begin8, beginTarget2, margin1, 0.5f, beginNext2, beginNextTarget2, margin2, strength2);
                    }
                } else {
                    nextMargin = i;
                    next4 = next8;
                    previousVisibleWidget2 = previousVisibleWidget4;
                    widget4 = widget7;
                }
                if (widget4.getVisibility() != nextMargin) {
                    previousVisibleWidget4 = widget4;
                } else {
                    previousVisibleWidget4 = previousVisibleWidget2;
                }
            }
        }
        if (isChainSpread) {
        }
        ConstraintAnchor begin52 = firstVisibleWidget.mListAnchors[offset];
        ConstraintAnchor end72 = lastVisibleWidget.mListAnchors[offset + 1];
        if (begin52.mTarget == null) {
        }
        if (end72.mTarget == null) {
        }
        if (last == lastVisibleWidget) {
        }
        if (firstVisibleWidget != lastVisibleWidget) {
        }
        if (beginTarget == null) {
        }
    }
}
