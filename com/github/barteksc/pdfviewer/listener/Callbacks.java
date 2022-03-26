package com.github.barteksc.pdfviewer.listener;

import android.view.MotionEvent;
import com.github.barteksc.pdfviewer.link.LinkHandler;
import com.github.barteksc.pdfviewer.model.LinkTapEvent;
/* loaded from: classes.dex */
public class Callbacks {
    private LinkHandler linkHandler;
    private OnDrawListener onDrawAllListener;
    private OnDrawListener onDrawListener;
    private OnErrorListener onErrorListener;
    private OnLoadCompleteListener onLoadCompleteListener;
    private OnLongPressListener onLongPressListener;
    private OnPageChangeListener onPageChangeListener;
    private OnPageErrorListener onPageErrorListener;
    private OnPageScrollListener onPageScrollListener;
    private OnRenderListener onRenderListener;
    private OnTapListener onTapListener;

    public void setOnLoadComplete(OnLoadCompleteListener onLoadCompleteListener) {
        this.onLoadCompleteListener = onLoadCompleteListener;
    }

    public void callOnLoadComplete(int pagesCount) {
        OnLoadCompleteListener onLoadCompleteListener = this.onLoadCompleteListener;
        if (onLoadCompleteListener != null) {
            onLoadCompleteListener.loadComplete(pagesCount);
        }
    }

    public void setOnError(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    public OnErrorListener getOnError() {
        return this.onErrorListener;
    }

    public void setOnPageError(OnPageErrorListener onPageErrorListener) {
        this.onPageErrorListener = onPageErrorListener;
    }

    public boolean callOnPageError(int page, Throwable error) {
        OnPageErrorListener onPageErrorListener = this.onPageErrorListener;
        if (onPageErrorListener == null) {
            return false;
        }
        onPageErrorListener.onPageError(page, error);
        return true;
    }

    public void setOnRender(OnRenderListener onRenderListener) {
        this.onRenderListener = onRenderListener;
    }

    public void callOnRender(int pagesCount) {
        OnRenderListener onRenderListener = this.onRenderListener;
        if (onRenderListener != null) {
            onRenderListener.onInitiallyRendered(pagesCount);
        }
    }

    public void setOnPageChange(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public void callOnPageChange(int page, int pagesCount) {
        OnPageChangeListener onPageChangeListener = this.onPageChangeListener;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageChanged(page, pagesCount);
        }
    }

    public void setOnPageScroll(OnPageScrollListener onPageScrollListener) {
        this.onPageScrollListener = onPageScrollListener;
    }

    public void callOnPageScroll(int currentPage, float offset) {
        OnPageScrollListener onPageScrollListener = this.onPageScrollListener;
        if (onPageScrollListener != null) {
            onPageScrollListener.onPageScrolled(currentPage, offset);
        }
    }

    public void setOnDraw(OnDrawListener onDrawListener) {
        this.onDrawListener = onDrawListener;
    }

    public OnDrawListener getOnDraw() {
        return this.onDrawListener;
    }

    public void setOnDrawAll(OnDrawListener onDrawAllListener) {
        this.onDrawAllListener = onDrawAllListener;
    }

    public OnDrawListener getOnDrawAll() {
        return this.onDrawAllListener;
    }

    public void setOnTap(OnTapListener onTapListener) {
        this.onTapListener = onTapListener;
    }

    public boolean callOnTap(MotionEvent event) {
        OnTapListener onTapListener = this.onTapListener;
        return onTapListener != null && onTapListener.onTap(event);
    }

    public void setOnLongPress(OnLongPressListener onLongPressListener) {
        this.onLongPressListener = onLongPressListener;
    }

    public void callOnLongPress(MotionEvent event) {
        OnLongPressListener onLongPressListener = this.onLongPressListener;
        if (onLongPressListener != null) {
            onLongPressListener.onLongPress(event);
        }
    }

    public void setLinkHandler(LinkHandler linkHandler) {
        this.linkHandler = linkHandler;
    }

    public void callLinkHandler(LinkTapEvent event) {
        LinkHandler linkHandler = this.linkHandler;
        if (linkHandler != null) {
            linkHandler.handleLinkEvent(event);
        }
    }
}
