package com.google.android.material.bottomsheet;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.ViewDragHelper;
import com.google.android.material.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class BottomSheetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
    private static final float HIDE_FRICTION = 0.1f;
    private static final float HIDE_THRESHOLD = 0.5f;
    public static final int PEEK_HEIGHT_AUTO = -1;
    public static final int STATE_COLLAPSED = 4;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_EXPANDED = 3;
    public static final int STATE_HALF_EXPANDED = 6;
    public static final int STATE_HIDDEN = 5;
    public static final int STATE_SETTLING = 2;
    int activePointerId;
    private BottomSheetCallback callback;
    int collapsedOffset;
    int fitToContentsOffset;
    int halfExpandedOffset;
    boolean hideable;
    private boolean ignoreEvents;
    private Map<View, Integer> importantForAccessibilityMap;
    private int initialY;
    private int lastNestedScrollDy;
    private int lastPeekHeight;
    private float maximumVelocity;
    private boolean nestedScrolled;
    WeakReference<View> nestedScrollingChildRef;
    int parentHeight;
    private int peekHeight;
    private boolean peekHeightAuto;
    private int peekHeightMin;
    private boolean skipCollapsed;
    boolean touchingScrollingChild;
    private VelocityTracker velocityTracker;
    ViewDragHelper viewDragHelper;
    WeakReference<V> viewRef;
    private boolean fitToContents = true;
    int state = 4;
    private final ViewDragHelper.Callback dragCallback = new ViewDragHelper.Callback() { // from class: com.google.android.material.bottomsheet.BottomSheetBehavior.2
        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public boolean tryCaptureView(View child, int pointerId) {
            View scroll;
            if (BottomSheetBehavior.this.state == 1 || BottomSheetBehavior.this.touchingScrollingChild) {
                return false;
            }
            if (BottomSheetBehavior.this.state != 3 || BottomSheetBehavior.this.activePointerId != pointerId || (scroll = BottomSheetBehavior.this.nestedScrollingChildRef.get()) == null || !scroll.canScrollVertically(-1)) {
                return BottomSheetBehavior.this.viewRef != null && BottomSheetBehavior.this.viewRef.get() == child;
            }
            return false;
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            BottomSheetBehavior.this.dispatchOnSlide(top);
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onViewDragStateChanged(int state) {
            if (state == 1) {
                BottomSheetBehavior.this.setStateInternal(1);
            }
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int targetState;
            int top;
            if (yvel < 0.0f) {
                if (BottomSheetBehavior.this.fitToContents) {
                    top = BottomSheetBehavior.this.fitToContentsOffset;
                    targetState = 3;
                } else if (releasedChild.getTop() > BottomSheetBehavior.this.halfExpandedOffset) {
                    targetState = 6;
                    top = BottomSheetBehavior.this.halfExpandedOffset;
                } else {
                    targetState = 3;
                    top = 0;
                }
            } else if (BottomSheetBehavior.this.hideable && BottomSheetBehavior.this.shouldHide(releasedChild, yvel) && (releasedChild.getTop() > BottomSheetBehavior.this.collapsedOffset || Math.abs(xvel) < Math.abs(yvel))) {
                top = BottomSheetBehavior.this.parentHeight;
                targetState = 5;
            } else if (yvel == 0.0f || Math.abs(xvel) > Math.abs(yvel)) {
                int currentTop = releasedChild.getTop();
                if (BottomSheetBehavior.this.fitToContents) {
                    if (Math.abs(currentTop - BottomSheetBehavior.this.fitToContentsOffset) < Math.abs(currentTop - BottomSheetBehavior.this.collapsedOffset)) {
                        targetState = 3;
                        top = BottomSheetBehavior.this.fitToContentsOffset;
                    } else {
                        targetState = 4;
                        top = BottomSheetBehavior.this.collapsedOffset;
                    }
                } else if (currentTop < BottomSheetBehavior.this.halfExpandedOffset) {
                    if (currentTop < Math.abs(currentTop - BottomSheetBehavior.this.collapsedOffset)) {
                        targetState = 3;
                        top = 0;
                    } else {
                        targetState = 6;
                        top = BottomSheetBehavior.this.halfExpandedOffset;
                    }
                } else if (Math.abs(currentTop - BottomSheetBehavior.this.halfExpandedOffset) < Math.abs(currentTop - BottomSheetBehavior.this.collapsedOffset)) {
                    targetState = 6;
                    top = BottomSheetBehavior.this.halfExpandedOffset;
                } else {
                    targetState = 4;
                    top = BottomSheetBehavior.this.collapsedOffset;
                }
            } else {
                top = BottomSheetBehavior.this.collapsedOffset;
                targetState = 4;
            }
            if (BottomSheetBehavior.this.viewDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top)) {
                BottomSheetBehavior.this.setStateInternal(2);
                ViewCompat.postOnAnimation(releasedChild, new SettleRunnable(releasedChild, targetState));
                return;
            }
            BottomSheetBehavior.this.setStateInternal(targetState);
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public int clampViewPositionVertical(View child, int top, int dy) {
            return MathUtils.clamp(top, BottomSheetBehavior.this.getExpandedOffset(), BottomSheetBehavior.this.hideable ? BottomSheetBehavior.this.parentHeight : BottomSheetBehavior.this.collapsedOffset);
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return child.getLeft();
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public int getViewVerticalDragRange(View child) {
            if (BottomSheetBehavior.this.hideable) {
                return BottomSheetBehavior.this.parentHeight;
            }
            return BottomSheetBehavior.this.collapsedOffset;
        }
    };

    /* loaded from: classes.dex */
    public static abstract class BottomSheetCallback {
        public abstract void onSlide(View view, float f);

        public abstract void onStateChanged(View view, int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface State {
    }

    public BottomSheetBehavior() {
    }

    public BottomSheetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a2 = context.obtainStyledAttributes(attrs, R.styleable.BottomSheetBehavior_Layout);
        TypedValue value = a2.peekValue(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight);
        if (value == null || value.data != -1) {
            setPeekHeight(a2.getDimensionPixelSize(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight, -1));
        } else {
            setPeekHeight(value.data);
        }
        setHideable(a2.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
        setFitToContents(a2.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_fitToContents, true));
        setSkipCollapsed(a2.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
        a2.recycle();
        this.maximumVelocity = (float) ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public Parcelable onSaveInstanceState(CoordinatorLayout parent, V child) {
        return new SavedState(super.onSaveInstanceState(parent, child), this.state);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onRestoreInstanceState(CoordinatorLayout parent, V child, Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(parent, child, ss.getSuperState());
        if (ss.state == 1 || ss.state == 2) {
            this.state = 4;
        } else {
            this.state = ss.state;
        }
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        if (ViewCompat.getFitsSystemWindows(parent) && !ViewCompat.getFitsSystemWindows(child)) {
            child.setFitsSystemWindows(true);
        }
        int savedTop = child.getTop();
        parent.onLayoutChild(child, layoutDirection);
        this.parentHeight = parent.getHeight();
        if (this.peekHeightAuto) {
            if (this.peekHeightMin == 0) {
                this.peekHeightMin = parent.getResources().getDimensionPixelSize(R.dimen.design_bottom_sheet_peek_height_min);
            }
            this.lastPeekHeight = Math.max(this.peekHeightMin, this.parentHeight - ((parent.getWidth() * 9) / 16));
        } else {
            this.lastPeekHeight = this.peekHeight;
        }
        this.fitToContentsOffset = Math.max(0, this.parentHeight - child.getHeight());
        this.halfExpandedOffset = this.parentHeight / 2;
        calculateCollapsedOffset();
        int i = this.state;
        if (i == 3) {
            ViewCompat.offsetTopAndBottom(child, getExpandedOffset());
        } else if (i == 6) {
            ViewCompat.offsetTopAndBottom(child, this.halfExpandedOffset);
        } else if (!this.hideable || i != 5) {
            int i2 = this.state;
            if (i2 == 4) {
                ViewCompat.offsetTopAndBottom(child, this.collapsedOffset);
            } else if (i2 == 1 || i2 == 2) {
                ViewCompat.offsetTopAndBottom(child, savedTop - child.getTop());
            }
        } else {
            ViewCompat.offsetTopAndBottom(child, this.parentHeight);
        }
        if (this.viewDragHelper == null) {
            this.viewDragHelper = ViewDragHelper.create(parent, this.dragCallback);
        }
        this.viewRef = new WeakReference<>(child);
        this.nestedScrollingChildRef = new WeakReference<>(findScrollingChild(child));
        return true;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, V child, MotionEvent event) {
        ViewDragHelper viewDragHelper;
        if (!child.isShown()) {
            this.ignoreEvents = true;
            return false;
        }
        int action = event.getActionMasked();
        if (action == 0) {
            reset();
        }
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.velocityTracker.addMovement(event);
        View scroll = null;
        if (action == 0) {
            int initialX = (int) event.getX();
            this.initialY = (int) event.getY();
            WeakReference<View> weakReference = this.nestedScrollingChildRef;
            View scroll2 = weakReference != null ? weakReference.get() : null;
            if (scroll2 != null && parent.isPointInChildBounds(scroll2, initialX, this.initialY)) {
                this.activePointerId = event.getPointerId(event.getActionIndex());
                this.touchingScrollingChild = true;
            }
            this.ignoreEvents = this.activePointerId == -1 && !parent.isPointInChildBounds(child, initialX, this.initialY);
        } else if (action == 1 || action == 3) {
            this.touchingScrollingChild = false;
            this.activePointerId = -1;
            if (this.ignoreEvents) {
                this.ignoreEvents = false;
                return false;
            }
        }
        if (!this.ignoreEvents && (viewDragHelper = this.viewDragHelper) != null && viewDragHelper.shouldInterceptTouchEvent(event)) {
            return true;
        }
        WeakReference<View> weakReference2 = this.nestedScrollingChildRef;
        if (weakReference2 != null) {
            scroll = weakReference2.get();
        }
        return action == 2 && scroll != null && !this.ignoreEvents && this.state != 1 && !parent.isPointInChildBounds(scroll, (int) event.getX(), (int) event.getY()) && this.viewDragHelper != null && Math.abs(((float) this.initialY) - event.getY()) > ((float) this.viewDragHelper.getTouchSlop());
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onTouchEvent(CoordinatorLayout parent, V child, MotionEvent event) {
        if (!child.isShown()) {
            return false;
        }
        int action = event.getActionMasked();
        if (this.state == 1 && action == 0) {
            return true;
        }
        ViewDragHelper viewDragHelper = this.viewDragHelper;
        if (viewDragHelper != null) {
            viewDragHelper.processTouchEvent(event);
        }
        if (action == 0) {
            reset();
        }
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.velocityTracker.addMovement(event);
        if (action == 2 && !this.ignoreEvents && Math.abs(((float) this.initialY) - event.getY()) > ((float) this.viewDragHelper.getTouchSlop())) {
            this.viewDragHelper.captureChildView(child, event.getPointerId(event.getActionIndex()));
        }
        return !this.ignoreEvents;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V child, View directTargetChild, View target, int axes, int type) {
        this.lastNestedScrollDy = 0;
        this.nestedScrolled = false;
        if ((axes & 2) != 0) {
            return true;
        }
        return false;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V child, View target, int dx, int dy, int[] consumed, int type) {
        if (type != 1 && target == this.nestedScrollingChildRef.get()) {
            int currentTop = child.getTop();
            int newTop = currentTop - dy;
            if (dy > 0) {
                if (newTop < getExpandedOffset()) {
                    consumed[1] = currentTop - getExpandedOffset();
                    ViewCompat.offsetTopAndBottom(child, -consumed[1]);
                    setStateInternal(3);
                } else {
                    consumed[1] = dy;
                    ViewCompat.offsetTopAndBottom(child, -dy);
                    setStateInternal(1);
                }
            } else if (dy < 0 && !target.canScrollVertically(-1)) {
                int i = this.collapsedOffset;
                if (newTop <= i || this.hideable) {
                    consumed[1] = dy;
                    ViewCompat.offsetTopAndBottom(child, -dy);
                    setStateInternal(1);
                } else {
                    consumed[1] = currentTop - i;
                    ViewCompat.offsetTopAndBottom(child, -consumed[1]);
                    setStateInternal(4);
                }
            }
            dispatchOnSlide(child.getTop());
            this.lastNestedScrollDy = dy;
            this.nestedScrolled = true;
        }
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V child, View target, int type) {
        int targetState;
        int top;
        if (child.getTop() == getExpandedOffset()) {
            setStateInternal(3);
        } else if (target == this.nestedScrollingChildRef.get() && this.nestedScrolled) {
            if (this.lastNestedScrollDy > 0) {
                top = getExpandedOffset();
                targetState = 3;
            } else if (this.hideable && shouldHide(child, getYVelocity())) {
                top = this.parentHeight;
                targetState = 5;
            } else if (this.lastNestedScrollDy == 0) {
                int currentTop = child.getTop();
                if (!this.fitToContents) {
                    int top2 = this.halfExpandedOffset;
                    if (currentTop < top2) {
                        if (currentTop < Math.abs(currentTop - this.collapsedOffset)) {
                            targetState = 3;
                            top = 0;
                        } else {
                            targetState = 6;
                            top = this.halfExpandedOffset;
                        }
                    } else if (Math.abs(currentTop - top2) < Math.abs(currentTop - this.collapsedOffset)) {
                        targetState = 6;
                        top = this.halfExpandedOffset;
                    } else {
                        targetState = 4;
                        top = this.collapsedOffset;
                    }
                } else if (Math.abs(currentTop - this.fitToContentsOffset) < Math.abs(currentTop - this.collapsedOffset)) {
                    targetState = 3;
                    top = this.fitToContentsOffset;
                } else {
                    targetState = 4;
                    top = this.collapsedOffset;
                }
            } else {
                top = this.collapsedOffset;
                targetState = 4;
            }
            if (this.viewDragHelper.smoothSlideViewTo(child, child.getLeft(), top)) {
                setStateInternal(2);
                ViewCompat.postOnAnimation(child, new SettleRunnable(child, targetState));
            } else {
                setStateInternal(targetState);
            }
            this.nestedScrolled = false;
        }
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, V child, View target, float velocityX, float velocityY) {
        return target == this.nestedScrollingChildRef.get() && (this.state != 3 || super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY));
    }

    public boolean isFitToContents() {
        return this.fitToContents;
    }

    public void setFitToContents(boolean fitToContents) {
        if (this.fitToContents != fitToContents) {
            this.fitToContents = fitToContents;
            if (this.viewRef != null) {
                calculateCollapsedOffset();
            }
            setStateInternal((!this.fitToContents || this.state != 6) ? this.state : 3);
        }
    }

    public final void setPeekHeight(int peekHeight) {
        WeakReference<V> weakReference;
        V view;
        boolean layout = false;
        if (peekHeight == -1) {
            if (!this.peekHeightAuto) {
                this.peekHeightAuto = true;
                layout = true;
            }
        } else if (this.peekHeightAuto || this.peekHeight != peekHeight) {
            this.peekHeightAuto = false;
            this.peekHeight = Math.max(0, peekHeight);
            this.collapsedOffset = this.parentHeight - peekHeight;
            layout = true;
        }
        if (layout && this.state == 4 && (weakReference = this.viewRef) != null && (view = weakReference.get()) != null) {
            view.requestLayout();
        }
    }

    public final int getPeekHeight() {
        if (this.peekHeightAuto) {
            return -1;
        }
        return this.peekHeight;
    }

    public void setHideable(boolean hideable) {
        this.hideable = hideable;
    }

    public boolean isHideable() {
        return this.hideable;
    }

    public void setSkipCollapsed(boolean skipCollapsed) {
        this.skipCollapsed = skipCollapsed;
    }

    public boolean getSkipCollapsed() {
        return this.skipCollapsed;
    }

    public void setBottomSheetCallback(BottomSheetCallback callback) {
        this.callback = callback;
    }

    public final void setState(final int state) {
        if (state != this.state) {
            WeakReference<V> weakReference = this.viewRef;
            if (weakReference != null) {
                final V child = weakReference.get();
                if (child != null) {
                    ViewParent parent = child.getParent();
                    if (parent == null || !parent.isLayoutRequested() || !ViewCompat.isAttachedToWindow(child)) {
                        startSettlingAnimation(child, state);
                    } else {
                        child.post(new Runnable() { // from class: com.google.android.material.bottomsheet.BottomSheetBehavior.1
                            @Override // java.lang.Runnable
                            public void run() {
                                BottomSheetBehavior.this.startSettlingAnimation(child, state);
                            }
                        });
                    }
                }
            } else if (state == 4 || state == 3 || state == 6 || (this.hideable && state == 5)) {
                this.state = state;
            }
        }
    }

    public final int getState() {
        return this.state;
    }

    void setStateInternal(int state) {
        BottomSheetCallback bottomSheetCallback;
        if (this.state != state) {
            this.state = state;
            if (state == 6 || state == 3) {
                updateImportantForAccessibility(true);
            } else if (state == 5 || state == 4) {
                updateImportantForAccessibility(false);
            }
            View bottomSheet = this.viewRef.get();
            if (bottomSheet != null && (bottomSheetCallback = this.callback) != null) {
                bottomSheetCallback.onStateChanged(bottomSheet, state);
            }
        }
    }

    private void calculateCollapsedOffset() {
        if (this.fitToContents) {
            this.collapsedOffset = Math.max(this.parentHeight - this.lastPeekHeight, this.fitToContentsOffset);
        } else {
            this.collapsedOffset = this.parentHeight - this.lastPeekHeight;
        }
    }

    private void reset() {
        this.activePointerId = -1;
        VelocityTracker velocityTracker = this.velocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.velocityTracker = null;
        }
    }

    boolean shouldHide(View child, float yvel) {
        if (this.skipCollapsed) {
            return true;
        }
        if (child.getTop() >= this.collapsedOffset && Math.abs((((float) child.getTop()) + (HIDE_FRICTION * yvel)) - ((float) this.collapsedOffset)) / ((float) this.peekHeight) > HIDE_THRESHOLD) {
            return true;
        }
        return false;
    }

    View findScrollingChild(View view) {
        if (ViewCompat.isNestedScrollingEnabled(view)) {
            return view;
        }
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        ViewGroup group = (ViewGroup) view;
        int count = group.getChildCount();
        for (int i = 0; i < count; i++) {
            View scrollingChild = findScrollingChild(group.getChildAt(i));
            if (scrollingChild != null) {
                return scrollingChild;
            }
        }
        return null;
    }

    private float getYVelocity() {
        VelocityTracker velocityTracker = this.velocityTracker;
        if (velocityTracker == null) {
            return 0.0f;
        }
        velocityTracker.computeCurrentVelocity(1000, this.maximumVelocity);
        return this.velocityTracker.getYVelocity(this.activePointerId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getExpandedOffset() {
        if (this.fitToContents) {
            return this.fitToContentsOffset;
        }
        return 0;
    }

    void startSettlingAnimation(View child, int state) {
        int top;
        if (state == 4) {
            top = this.collapsedOffset;
        } else if (state == 6) {
            top = this.halfExpandedOffset;
            if (this.fitToContents && top <= this.fitToContentsOffset) {
                state = 3;
                top = this.fitToContentsOffset;
            }
        } else if (state == 3) {
            top = getExpandedOffset();
        } else if (!this.hideable || state != 5) {
            throw new IllegalArgumentException("Illegal state argument: " + state);
        } else {
            top = this.parentHeight;
        }
        if (this.viewDragHelper.smoothSlideViewTo(child, child.getLeft(), top)) {
            setStateInternal(2);
            ViewCompat.postOnAnimation(child, new SettleRunnable(child, state));
            return;
        }
        setStateInternal(state);
    }

    void dispatchOnSlide(int top) {
        BottomSheetCallback bottomSheetCallback;
        View bottomSheet = this.viewRef.get();
        if (bottomSheet != null && (bottomSheetCallback = this.callback) != null) {
            int i = this.collapsedOffset;
            if (top > i) {
                bottomSheetCallback.onSlide(bottomSheet, ((float) (i - top)) / ((float) (this.parentHeight - i)));
            } else {
                bottomSheetCallback.onSlide(bottomSheet, ((float) (i - top)) / ((float) (i - getExpandedOffset())));
            }
        }
    }

    int getPeekHeightMin() {
        return this.peekHeightMin;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class SettleRunnable implements Runnable {
        private final int targetState;
        private final View view;

        SettleRunnable(View view, int targetState) {
            this.view = view;
            this.targetState = targetState;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (BottomSheetBehavior.this.viewDragHelper == null || !BottomSheetBehavior.this.viewDragHelper.continueSettling(true)) {
                BottomSheetBehavior.this.setStateInternal(this.targetState);
            } else {
                ViewCompat.postOnAnimation(this.view, this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: com.google.android.material.bottomsheet.BottomSheetBehavior.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.ClassLoaderCreator
            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, (ClassLoader) null);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        final int state;

        public SavedState(Parcel source) {
            this(source, (ClassLoader) null);
        }

        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            this.state = source.readInt();
        }

        public SavedState(Parcelable superState, int state) {
            super(superState);
            this.state = state;
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.state);
        }
    }

    public static <V extends View> BottomSheetBehavior<V> from(V view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params).getBehavior();
            if (behavior instanceof BottomSheetBehavior) {
                return (BottomSheetBehavior) behavior;
            }
            throw new IllegalArgumentException("The view is not associated with BottomSheetBehavior");
        }
        throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
    }

    private void updateImportantForAccessibility(boolean expanded) {
        WeakReference<V> weakReference = this.viewRef;
        if (weakReference != null) {
            ViewParent viewParent = weakReference.get().getParent();
            if (viewParent instanceof CoordinatorLayout) {
                CoordinatorLayout parent = (CoordinatorLayout) viewParent;
                int childCount = parent.getChildCount();
                if (Build.VERSION.SDK_INT >= 16 && expanded) {
                    if (this.importantForAccessibilityMap == null) {
                        this.importantForAccessibilityMap = new HashMap(childCount);
                    } else {
                        return;
                    }
                }
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    if (child != this.viewRef.get()) {
                        if (!expanded) {
                            Map<View, Integer> map = this.importantForAccessibilityMap;
                            if (map != null && map.containsKey(child)) {
                                ViewCompat.setImportantForAccessibility(child, this.importantForAccessibilityMap.get(child).intValue());
                            }
                        } else {
                            if (Build.VERSION.SDK_INT >= 16) {
                                this.importantForAccessibilityMap.put(child, Integer.valueOf(child.getImportantForAccessibility()));
                            }
                            ViewCompat.setImportantForAccessibility(child, 4);
                        }
                    }
                }
                if (!expanded) {
                    this.importantForAccessibilityMap = null;
                }
            }
        }
    }
}
