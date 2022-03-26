package com.google.android.material.appbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;
/* loaded from: classes.dex */
abstract class HeaderBehavior<V extends View> extends ViewOffsetBehavior<V> {
    private static final int INVALID_POINTER = -1;
    private Runnable flingRunnable;
    private boolean isBeingDragged;
    private int lastMotionY;
    OverScroller scroller;
    private VelocityTracker velocityTracker;
    private int activePointerId = -1;
    private int touchSlop = -1;

    public HeaderBehavior() {
    }

    public HeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x002c, code lost:
        if (r3 != 3) goto L_0x0083;
     */
    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    /* Code decompiled incorrectly, please refer to instructions dump */
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, V child, MotionEvent ev) {
        int pointerIndex;
        if (this.touchSlop < 0) {
            this.touchSlop = ViewConfiguration.get(parent.getContext()).getScaledTouchSlop();
        }
        if (ev.getAction() == 2 && this.isBeingDragged) {
            return true;
        }
        int actionMasked = ev.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    int activePointerId = this.activePointerId;
                    if (!(activePointerId == -1 || (pointerIndex = ev.findPointerIndex(activePointerId)) == -1)) {
                        int y = (int) ev.getY(pointerIndex);
                        if (Math.abs(y - this.lastMotionY) > this.touchSlop) {
                            this.isBeingDragged = true;
                            this.lastMotionY = y;
                        }
                    }
                }
            }
            this.isBeingDragged = false;
            this.activePointerId = -1;
            VelocityTracker velocityTracker = this.velocityTracker;
            if (velocityTracker != null) {
                velocityTracker.recycle();
                this.velocityTracker = null;
            }
        } else {
            this.isBeingDragged = false;
            int x = (int) ev.getX();
            int y2 = (int) ev.getY();
            if (canDragView(child) && parent.isPointInChildBounds(child, x, y2)) {
                this.lastMotionY = y2;
                this.activePointerId = ev.getPointerId(0);
                ensureVelocityTracker();
            }
        }
        VelocityTracker velocityTracker2 = this.velocityTracker;
        if (velocityTracker2 != null) {
            velocityTracker2.addMovement(ev);
        }
        return this.isBeingDragged;
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0021, code lost:
        if (r0 != 3) goto L_0x00af;
     */
    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    /* Code decompiled incorrectly, please refer to instructions dump */
    public boolean onTouchEvent(CoordinatorLayout parent, V child, MotionEvent ev) {
        int i;
        if (this.touchSlop < 0) {
            this.touchSlop = ViewConfiguration.get(parent.getContext()).getScaledTouchSlop();
        }
        int actionMasked = ev.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked == 1) {
                VelocityTracker velocityTracker = this.velocityTracker;
                if (velocityTracker != null) {
                    velocityTracker.addMovement(ev);
                    this.velocityTracker.computeCurrentVelocity(1000);
                    fling(parent, child, -getScrollRangeForDragFling(child), 0, this.velocityTracker.getYVelocity(this.activePointerId));
                }
            } else if (actionMasked == 2) {
                int activePointerIndex = ev.findPointerIndex(this.activePointerId);
                if (activePointerIndex == -1) {
                    return false;
                }
                int y = (int) ev.getY(activePointerIndex);
                int dy = this.lastMotionY - y;
                if (!this.isBeingDragged && Math.abs(dy) > (i = this.touchSlop)) {
                    this.isBeingDragged = true;
                    dy = dy > 0 ? dy - i : dy + i;
                }
                if (this.isBeingDragged) {
                    this.lastMotionY = y;
                    scroll(parent, child, dy, getMaxDragOffset(child), 0);
                }
            }
            this.isBeingDragged = false;
            this.activePointerId = -1;
            VelocityTracker velocityTracker2 = this.velocityTracker;
            if (velocityTracker2 != null) {
                velocityTracker2.recycle();
                this.velocityTracker = null;
            }
        } else {
            int y2 = (int) ev.getY();
            if (!parent.isPointInChildBounds(child, (int) ev.getX(), y2) || !canDragView(child)) {
                return false;
            }
            this.lastMotionY = y2;
            this.activePointerId = ev.getPointerId(0);
            ensureVelocityTracker();
        }
        VelocityTracker velocityTracker3 = this.velocityTracker;
        if (velocityTracker3 != null) {
            velocityTracker3.addMovement(ev);
        }
        return true;
    }

    int setHeaderTopBottomOffset(CoordinatorLayout parent, V header, int newOffset) {
        return setHeaderTopBottomOffset(parent, header, newOffset, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    int setHeaderTopBottomOffset(CoordinatorLayout parent, V header, int newOffset, int minOffset, int maxOffset) {
        int newOffset2;
        int curOffset = getTopAndBottomOffset();
        if (minOffset == 0 || curOffset < minOffset || curOffset > maxOffset || curOffset == (newOffset2 = MathUtils.clamp(newOffset, minOffset, maxOffset))) {
            return 0;
        }
        setTopAndBottomOffset(newOffset2);
        return curOffset - newOffset2;
    }

    int getTopBottomOffsetForScrollingSibling() {
        return getTopAndBottomOffset();
    }

    final int scroll(CoordinatorLayout coordinatorLayout, V header, int dy, int minOffset, int maxOffset) {
        return setHeaderTopBottomOffset(coordinatorLayout, header, getTopBottomOffsetForScrollingSibling() - dy, minOffset, maxOffset);
    }

    final boolean fling(CoordinatorLayout coordinatorLayout, V layout, int minOffset, int maxOffset, float velocityY) {
        Runnable runnable = this.flingRunnable;
        if (runnable != null) {
            layout.removeCallbacks(runnable);
            this.flingRunnable = null;
        }
        if (this.scroller == null) {
            this.scroller = new OverScroller(layout.getContext());
        }
        this.scroller.fling(0, getTopAndBottomOffset(), 0, Math.round(velocityY), 0, 0, minOffset, maxOffset);
        if (this.scroller.computeScrollOffset()) {
            this.flingRunnable = new FlingRunnable(coordinatorLayout, layout);
            ViewCompat.postOnAnimation(layout, this.flingRunnable);
            return true;
        }
        onFlingFinished(coordinatorLayout, layout);
        return false;
    }

    void onFlingFinished(CoordinatorLayout parent, V layout) {
    }

    boolean canDragView(V view) {
        return false;
    }

    int getMaxDragOffset(V view) {
        return -view.getHeight();
    }

    int getScrollRangeForDragFling(V view) {
        return view.getHeight();
    }

    private void ensureVelocityTracker() {
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Incorrect field signature: TV; */
    /* loaded from: classes.dex */
    public class FlingRunnable implements Runnable {
        private final View layout;
        private final CoordinatorLayout parent;

        FlingRunnable(CoordinatorLayout parent, V layout) {
            this.parent = parent;
            this.layout = layout;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.lang.Runnable
        public void run() {
            if (this.layout != null && HeaderBehavior.this.scroller != null) {
                if (HeaderBehavior.this.scroller.computeScrollOffset()) {
                    HeaderBehavior headerBehavior = HeaderBehavior.this;
                    headerBehavior.setHeaderTopBottomOffset(this.parent, this.layout, headerBehavior.scroller.getCurrY());
                    ViewCompat.postOnAnimation(this.layout, this);
                    return;
                }
                HeaderBehavior.this.onFlingFinished(this.parent, this.layout);
            }
        }
    }
}
