package com.futuremind.recyclerviewfastscroll;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.futuremind.recyclerviewfastscroll.RecyclerViewScrollListener;
import com.futuremind.recyclerviewfastscroll.viewprovider.DefaultScrollerViewProvider;
import com.futuremind.recyclerviewfastscroll.viewprovider.ScrollerViewProvider;
/* loaded from: classes.dex */
public class FastScroller extends LinearLayout {
    private static final int STYLE_NONE;
    private View bubble;
    private int bubbleColor;
    private int bubbleOffset;
    private int bubbleTextAppearance;
    private TextView bubbleTextView;
    private View handle;
    private int handleColor;
    private boolean manuallyChangingPosition;
    private int maxVisibility;
    private RecyclerView recyclerView;
    private final RecyclerViewScrollListener scrollListener;
    private int scrollerOrientation;
    private SectionTitleProvider titleProvider;
    private ScrollerViewProvider viewProvider;

    public FastScroller(Context context) {
        this(context, null);
    }

    public FastScroller(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /* JADX WARN: Finally extract failed */
    public FastScroller(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.scrollListener = new RecyclerViewScrollListener(this);
        setClipChildren(false);
        TypedArray style = context.obtainStyledAttributes(attrs, R.styleable.fastscroll__fastScroller, R.attr.fastscroll__style, 0);
        try {
            this.bubbleColor = style.getColor(R.styleable.fastscroll__fastScroller_fastscroll__bubbleColor, -1);
            this.handleColor = style.getColor(R.styleable.fastscroll__fastScroller_fastscroll__handleColor, -1);
            this.bubbleTextAppearance = style.getResourceId(R.styleable.fastscroll__fastScroller_fastscroll__bubbleTextAppearance, -1);
            style.recycle();
            this.maxVisibility = getVisibility();
            setViewProvider(new DefaultScrollerViewProvider());
        } catch (Throwable th) {
            style.recycle();
            throw th;
        }
    }

    public void setViewProvider(ScrollerViewProvider viewProvider) {
        removeAllViews();
        this.viewProvider = viewProvider;
        viewProvider.setFastScroller(this);
        this.bubble = viewProvider.provideBubbleView(this);
        this.handle = viewProvider.provideHandleView(this);
        this.bubbleTextView = viewProvider.provideBubbleTextView();
        addView(this.bubble);
        addView(this.handle);
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        if (recyclerView.getAdapter() instanceof SectionTitleProvider) {
            this.titleProvider = (SectionTitleProvider) recyclerView.getAdapter();
        }
        recyclerView.addOnScrollListener(this.scrollListener);
        invalidateVisibility();
        recyclerView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() { // from class: com.futuremind.recyclerviewfastscroll.FastScroller.1
            @Override // android.view.ViewGroup.OnHierarchyChangeListener
            public void onChildViewAdded(View parent, View child) {
                FastScroller.this.invalidateVisibility();
            }

            @Override // android.view.ViewGroup.OnHierarchyChangeListener
            public void onChildViewRemoved(View parent, View child) {
                FastScroller.this.invalidateVisibility();
            }
        });
    }

    @Override // android.widget.LinearLayout
    public void setOrientation(int orientation) {
        this.scrollerOrientation = orientation;
        super.setOrientation(orientation == 0 ? 1 : 0);
    }

    public void setBubbleColor(int color) {
        this.bubbleColor = color;
        invalidate();
    }

    public void setHandleColor(int color) {
        this.handleColor = color;
        invalidate();
    }

    public void setBubbleTextAppearance(int textAppearanceResourceId) {
        this.bubbleTextAppearance = textAppearanceResourceId;
        invalidate();
    }

    public void addScrollerListener(RecyclerViewScrollListener.ScrollerListener listener) {
        this.scrollListener.addScrollerListener(listener);
    }

    @Override // android.widget.LinearLayout, android.view.View, android.view.ViewGroup
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        initHandleMovement();
        this.bubbleOffset = this.viewProvider.getBubbleOffset();
        applyStyling();
        this.scrollListener.updateHandlePosition(this.recyclerView);
    }

    private void applyStyling() {
        int i = this.bubbleColor;
        if (i != -1) {
            setBackgroundTint(this.bubbleTextView, i);
        }
        int i2 = this.handleColor;
        if (i2 != -1) {
            setBackgroundTint(this.handle, i2);
        }
        int i3 = this.bubbleTextAppearance;
        if (i3 != -1) {
            TextViewCompat.setTextAppearance(this.bubbleTextView, i3);
        }
    }

    private void setBackgroundTint(View view, int color) {
        Drawable background = DrawableCompat.wrap(view.getBackground());
        if (background != null) {
            DrawableCompat.setTint(background.mutate(), color);
            Utils.setBackground(view, background);
        }
    }

    private void initHandleMovement() {
        this.handle.setOnTouchListener(new View.OnTouchListener() { // from class: com.futuremind.recyclerviewfastscroll.FastScroller.2
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View v, MotionEvent event) {
                FastScroller.this.requestDisallowInterceptTouchEvent(true);
                if (event.getAction() == 0 || event.getAction() == 2) {
                    if (FastScroller.this.titleProvider != null && event.getAction() == 0) {
                        FastScroller.this.viewProvider.onHandleGrabbed();
                    }
                    FastScroller.this.manuallyChangingPosition = true;
                    float relativePos = FastScroller.this.getRelativeTouchPosition(event);
                    FastScroller.this.setScrollerPosition(relativePos);
                    FastScroller.this.setRecyclerViewPosition(relativePos);
                    return true;
                } else if (event.getAction() != 1) {
                    return false;
                } else {
                    FastScroller.this.manuallyChangingPosition = false;
                    if (FastScroller.this.titleProvider != null) {
                        FastScroller.this.viewProvider.onHandleReleased();
                    }
                    return true;
                }
            }
        });
    }

    public float getRelativeTouchPosition(MotionEvent event) {
        if (isVertical()) {
            return (event.getRawY() - Utils.getViewRawY(this.handle)) / ((float) (getHeight() - this.handle.getHeight()));
        }
        return (event.getRawX() - Utils.getViewRawX(this.handle)) / ((float) (getWidth() - this.handle.getWidth()));
    }

    @Override // android.view.View
    public void setVisibility(int visibility) {
        this.maxVisibility = visibility;
        invalidateVisibility();
    }

    public void invalidateVisibility() {
        if (this.recyclerView.getAdapter() == null || this.recyclerView.getAdapter().getItemCount() == 0 || this.recyclerView.getChildAt(0) == null || isRecyclerViewNotScrollable() || this.maxVisibility != 0) {
            super.setVisibility(4);
        } else {
            super.setVisibility(0);
        }
    }

    private boolean isRecyclerViewNotScrollable() {
        return isVertical() ? this.recyclerView.getChildAt(0).getHeight() * this.recyclerView.getAdapter().getItemCount() <= this.recyclerView.getHeight() : this.recyclerView.getChildAt(0).getWidth() * this.recyclerView.getAdapter().getItemCount() <= this.recyclerView.getWidth();
    }

    public void setRecyclerViewPosition(float relativePos) {
        TextView textView;
        RecyclerView recyclerView = this.recyclerView;
        if (recyclerView != null) {
            int itemCount = recyclerView.getAdapter().getItemCount();
            int targetPos = (int) Utils.getValueInRange(0.0f, (float) (itemCount - 1), (float) ((int) (((float) itemCount) * relativePos)));
            this.recyclerView.scrollToPosition(targetPos);
            SectionTitleProvider sectionTitleProvider = this.titleProvider;
            if (sectionTitleProvider != null && (textView = this.bubbleTextView) != null) {
                textView.setText(sectionTitleProvider.getSectionTitle(targetPos));
            }
        }
    }

    public void setScrollerPosition(float relativePos) {
        if (isVertical()) {
            this.bubble.setY(Utils.getValueInRange(0.0f, (float) (getHeight() - this.bubble.getHeight()), (((float) (getHeight() - this.handle.getHeight())) * relativePos) + ((float) this.bubbleOffset)));
            this.handle.setY(Utils.getValueInRange(0.0f, (float) (getHeight() - this.handle.getHeight()), ((float) (getHeight() - this.handle.getHeight())) * relativePos));
            return;
        }
        this.bubble.setX(Utils.getValueInRange(0.0f, (float) (getWidth() - this.bubble.getWidth()), (((float) (getWidth() - this.handle.getWidth())) * relativePos) + ((float) this.bubbleOffset)));
        this.handle.setX(Utils.getValueInRange(0.0f, (float) (getWidth() - this.handle.getWidth()), ((float) (getWidth() - this.handle.getWidth())) * relativePos));
    }

    public boolean isVertical() {
        return this.scrollerOrientation == 1;
    }

    public boolean shouldUpdateHandlePosition() {
        return this.handle != null && !this.manuallyChangingPosition && this.recyclerView.getChildCount() > 0;
    }

    public ScrollerViewProvider getViewProvider() {
        return this.viewProvider;
    }
}
