package com.futuremind.recyclerviewfastscroll;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    List<ScrollerListener> listeners = new ArrayList();
    int oldScrollState = 0;
    private final FastScroller scroller;

    /* loaded from: classes.dex */
    public interface ScrollerListener {
        void onScroll(float f);
    }

    public RecyclerViewScrollListener(FastScroller scroller) {
        this.scroller = scroller;
    }

    public void addScrollerListener(ScrollerListener listener) {
        this.listeners.add(listener);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
    public void onScrollStateChanged(RecyclerView recyclerView, int newScrollState) {
        super.onScrollStateChanged(recyclerView, newScrollState);
        if (newScrollState == 0 && this.oldScrollState != 0) {
            this.scroller.getViewProvider().onScrollFinished();
        } else if (newScrollState != 0 && this.oldScrollState == 0) {
            this.scroller.getViewProvider().onScrollStarted();
        }
        this.oldScrollState = newScrollState;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
    public void onScrolled(RecyclerView rv, int dx, int dy) {
        if (this.scroller.shouldUpdateHandlePosition()) {
            updateHandlePosition(rv);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateHandlePosition(RecyclerView rv) {
        float relativePos;
        if (this.scroller.isVertical()) {
            relativePos = ((float) rv.computeVerticalScrollOffset()) / ((float) (rv.computeVerticalScrollRange() - rv.computeVerticalScrollExtent()));
        } else {
            relativePos = ((float) rv.computeHorizontalScrollOffset()) / ((float) (rv.computeHorizontalScrollRange() - rv.computeHorizontalScrollExtent()));
        }
        this.scroller.setScrollerPosition(relativePos);
        notifyListeners(relativePos);
    }

    public void notifyListeners(float relativePos) {
        for (ScrollerListener listener : this.listeners) {
            listener.onScroll(relativePos);
        }
    }
}
