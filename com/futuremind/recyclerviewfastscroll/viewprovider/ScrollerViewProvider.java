package com.futuremind.recyclerviewfastscroll.viewprovider;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.futuremind.recyclerviewfastscroll.FastScroller;
/* loaded from: classes.dex */
public abstract class ScrollerViewProvider {
    private ViewBehavior bubbleBehavior;
    private ViewBehavior handleBehavior;
    private FastScroller scroller;

    public abstract int getBubbleOffset();

    protected abstract ViewBehavior provideBubbleBehavior();

    public abstract TextView provideBubbleTextView();

    public abstract View provideBubbleView(ViewGroup viewGroup);

    protected abstract ViewBehavior provideHandleBehavior();

    public abstract View provideHandleView(ViewGroup viewGroup);

    public void setFastScroller(FastScroller scroller) {
        this.scroller = scroller;
    }

    protected Context getContext() {
        return this.scroller.getContext();
    }

    protected FastScroller getScroller() {
        return this.scroller;
    }

    protected ViewBehavior getHandleBehavior() {
        if (this.handleBehavior == null) {
            this.handleBehavior = provideHandleBehavior();
        }
        return this.handleBehavior;
    }

    protected ViewBehavior getBubbleBehavior() {
        if (this.bubbleBehavior == null) {
            this.bubbleBehavior = provideBubbleBehavior();
        }
        return this.bubbleBehavior;
    }

    public void onHandleGrabbed() {
        if (getHandleBehavior() != null) {
            getHandleBehavior().onHandleGrabbed();
        }
        if (getBubbleBehavior() != null) {
            getBubbleBehavior().onHandleGrabbed();
        }
    }

    public void onHandleReleased() {
        if (getHandleBehavior() != null) {
            getHandleBehavior().onHandleReleased();
        }
        if (getBubbleBehavior() != null) {
            getBubbleBehavior().onHandleReleased();
        }
    }

    public void onScrollStarted() {
        if (getHandleBehavior() != null) {
            getHandleBehavior().onScrollStarted();
        }
        if (getBubbleBehavior() != null) {
            getBubbleBehavior().onScrollStarted();
        }
    }

    public void onScrollFinished() {
        if (getHandleBehavior() != null) {
            getHandleBehavior().onScrollFinished();
        }
        if (getBubbleBehavior() != null) {
            getBubbleBehavior().onScrollFinished();
        }
    }
}
