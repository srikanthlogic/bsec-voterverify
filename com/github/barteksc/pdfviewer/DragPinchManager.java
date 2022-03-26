package com.github.barteksc.pdfviewer;

import android.graphics.PointF;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import com.github.barteksc.pdfviewer.model.LinkTapEvent;
import com.github.barteksc.pdfviewer.scroll.ScrollHandle;
import com.github.barteksc.pdfviewer.util.Constants;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.util.SizeF;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class DragPinchManager implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {
    private AnimationManager animationManager;
    private GestureDetector gestureDetector;
    private PDFView pdfView;
    private ScaleGestureDetector scaleGestureDetector;
    private boolean scrolling = false;
    private boolean scaling = false;
    private boolean enabled = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DragPinchManager(PDFView pdfView, AnimationManager animationManager) {
        this.pdfView = pdfView;
        this.animationManager = animationManager;
        this.gestureDetector = new GestureDetector(pdfView.getContext(), this);
        this.scaleGestureDetector = new ScaleGestureDetector(pdfView.getContext(), this);
        pdfView.setOnTouchListener(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void enable() {
        this.enabled = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void disable() {
        this.enabled = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void disableLongpress() {
        this.gestureDetector.setIsLongpressEnabled(false);
    }

    @Override // android.view.GestureDetector.OnDoubleTapListener
    public boolean onSingleTapConfirmed(MotionEvent e) {
        ScrollHandle ps;
        boolean onTapHandled = this.pdfView.callbacks.callOnTap(e);
        boolean linkTapped = checkLinkTapped(e.getX(), e.getY());
        if (!onTapHandled && !linkTapped && (ps = this.pdfView.getScrollHandle()) != null && !this.pdfView.documentFitsView()) {
            if (!ps.shown()) {
                ps.show();
            } else {
                ps.hide();
            }
        }
        this.pdfView.performClick();
        return true;
    }

    private boolean checkLinkTapped(float x, float y) {
        int pageY;
        int pageX;
        PdfFile pdfFile = this.pdfView.pdfFile;
        if (pdfFile == null) {
            return false;
        }
        float mappedX = (-this.pdfView.getCurrentXOffset()) + x;
        float mappedY = (-this.pdfView.getCurrentYOffset()) + y;
        int page = pdfFile.getPageAtOffset(this.pdfView.isSwipeVertical() ? mappedY : mappedX, this.pdfView.getZoom());
        SizeF pageSize = pdfFile.getScaledPageSize(page, this.pdfView.getZoom());
        if (this.pdfView.isSwipeVertical()) {
            pageY = (int) pdfFile.getPageOffset(page, this.pdfView.getZoom());
            pageX = (int) pdfFile.getSecondaryPageOffset(page, this.pdfView.getZoom());
        } else {
            pageX = (int) pdfFile.getPageOffset(page, this.pdfView.getZoom());
            pageY = (int) pdfFile.getSecondaryPageOffset(page, this.pdfView.getZoom());
        }
        for (PdfDocument.Link link : pdfFile.getPageLinks(page)) {
            RectF mapped = pdfFile.mapRectToDevice(page, pageX, pageY, (int) pageSize.getWidth(), (int) pageSize.getHeight(), link.getBounds());
            mapped.sort();
            if (mapped.contains(mappedX, mappedY)) {
                this.pdfView.callbacks.callLinkHandler(new LinkTapEvent(x, y, mappedX, mappedY, mapped, link));
                return true;
            }
        }
        return false;
    }

    private void startPageFling(MotionEvent downEvent, MotionEvent ev, float velocityX, float velocityY) {
        int direction;
        float f;
        float f2;
        if (checkDoPageFling(velocityX, velocityY)) {
            int i = -1;
            if (this.pdfView.isSwipeVertical()) {
                if (velocityY <= 0.0f) {
                    i = 1;
                }
                direction = i;
            } else {
                if (velocityX <= 0.0f) {
                    i = 1;
                }
                direction = i;
            }
            if (this.pdfView.isSwipeVertical()) {
                f2 = ev.getY();
                f = downEvent.getY();
            } else {
                f2 = ev.getX();
                f = downEvent.getX();
            }
            float delta = f2 - f;
            int targetPage = Math.max(0, Math.min(this.pdfView.getPageCount() - 1, this.pdfView.findFocusPage(this.pdfView.getCurrentXOffset() - (this.pdfView.getZoom() * delta), this.pdfView.getCurrentYOffset() - (this.pdfView.getZoom() * delta)) + direction));
            this.animationManager.startPageFlingAnimation(-this.pdfView.snapOffsetForPage(targetPage, this.pdfView.findSnapEdge(targetPage)));
        }
    }

    @Override // android.view.GestureDetector.OnDoubleTapListener
    public boolean onDoubleTap(MotionEvent e) {
        if (!this.pdfView.isDoubletapEnabled()) {
            return false;
        }
        if (this.pdfView.getZoom() < this.pdfView.getMidZoom()) {
            this.pdfView.zoomWithAnimation(e.getX(), e.getY(), this.pdfView.getMidZoom());
            return true;
        } else if (this.pdfView.getZoom() < this.pdfView.getMaxZoom()) {
            this.pdfView.zoomWithAnimation(e.getX(), e.getY(), this.pdfView.getMaxZoom());
            return true;
        } else {
            this.pdfView.resetZoomWithAnimation();
            return true;
        }
    }

    @Override // android.view.GestureDetector.OnDoubleTapListener
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onDown(MotionEvent e) {
        this.animationManager.stopFling();
        return true;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onShowPress(MotionEvent e) {
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        this.scrolling = true;
        if (this.pdfView.isZooming() || this.pdfView.isSwipeEnabled()) {
            this.pdfView.moveRelativeTo(-distanceX, -distanceY);
        }
        if (!this.scaling || this.pdfView.doRenderDuringScale()) {
            this.pdfView.loadPageByOffset();
        }
        return true;
    }

    private void onScrollEnd(MotionEvent event) {
        this.pdfView.loadPages();
        hideHandle();
        if (!this.animationManager.isFlinging()) {
            this.pdfView.performPageSnap();
        }
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onLongPress(MotionEvent e) {
        this.pdfView.callbacks.callOnLongPress(e);
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float minX;
        float minY;
        if (!this.pdfView.isSwipeEnabled()) {
            return false;
        }
        if (this.pdfView.isPageFlingEnabled()) {
            if (this.pdfView.pageFillsScreen()) {
                onBoundedFling(velocityX, velocityY);
            } else {
                startPageFling(e1, e2, velocityX, velocityY);
            }
            return true;
        }
        int xOffset = (int) this.pdfView.getCurrentXOffset();
        int yOffset = (int) this.pdfView.getCurrentYOffset();
        PdfFile pdfFile = this.pdfView.pdfFile;
        if (this.pdfView.isSwipeVertical()) {
            minY = -(pdfFile.getDocLen(this.pdfView.getZoom()) - ((float) this.pdfView.getHeight()));
            minX = -(this.pdfView.toCurrentScale(pdfFile.getMaxPageWidth()) - ((float) this.pdfView.getWidth()));
        } else {
            minY = -(this.pdfView.toCurrentScale(pdfFile.getMaxPageHeight()) - ((float) this.pdfView.getHeight()));
            minX = -(pdfFile.getDocLen(this.pdfView.getZoom()) - ((float) this.pdfView.getWidth()));
        }
        this.animationManager.startFlingAnimation(xOffset, yOffset, (int) velocityX, (int) velocityY, (int) minX, 0, (int) minY, 0);
        return true;
    }

    private void onBoundedFling(float velocityX, float velocityY) {
        float minX;
        float maxX;
        float minY;
        float maxY;
        int xOffset = (int) this.pdfView.getCurrentXOffset();
        int yOffset = (int) this.pdfView.getCurrentYOffset();
        PdfFile pdfFile = this.pdfView.pdfFile;
        float pageStart = -pdfFile.getPageOffset(this.pdfView.getCurrentPage(), this.pdfView.getZoom());
        float pageEnd = pageStart - pdfFile.getPageLength(this.pdfView.getCurrentPage(), this.pdfView.getZoom());
        if (this.pdfView.isSwipeVertical()) {
            minX = -(this.pdfView.toCurrentScale(pdfFile.getMaxPageWidth()) - ((float) this.pdfView.getWidth()));
            minY = ((float) this.pdfView.getHeight()) + pageEnd;
            maxX = 0.0f;
            maxY = pageStart;
        } else {
            maxY = 0.0f;
            minX = ((float) this.pdfView.getWidth()) + pageEnd;
            minY = -(this.pdfView.toCurrentScale(pdfFile.getMaxPageHeight()) - ((float) this.pdfView.getHeight()));
            maxX = pageStart;
        }
        this.animationManager.startFlingAnimation(xOffset, yOffset, (int) velocityX, (int) velocityY, (int) minX, (int) maxX, (int) minY, (int) maxY);
    }

    @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
    public boolean onScale(ScaleGestureDetector detector) {
        float dr = detector.getScaleFactor();
        float wantedZoom = this.pdfView.getZoom() * dr;
        float minZoom = Math.min(Constants.Pinch.MINIMUM_ZOOM, this.pdfView.getMinZoom());
        float maxZoom = Math.min(Constants.Pinch.MAXIMUM_ZOOM, this.pdfView.getMaxZoom());
        if (wantedZoom < minZoom) {
            dr = minZoom / this.pdfView.getZoom();
        } else if (wantedZoom > maxZoom) {
            dr = maxZoom / this.pdfView.getZoom();
        }
        this.pdfView.zoomCenteredRelativeTo(dr, new PointF(detector.getFocusX(), detector.getFocusY()));
        return true;
    }

    @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        this.scaling = true;
        return true;
    }

    @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
    public void onScaleEnd(ScaleGestureDetector detector) {
        this.pdfView.loadPages();
        hideHandle();
        this.scaling = false;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View v, MotionEvent event) {
        if (!this.enabled) {
            return false;
        }
        boolean retVal = this.gestureDetector.onTouchEvent(event) || this.scaleGestureDetector.onTouchEvent(event);
        if (event.getAction() == 1 && this.scrolling) {
            this.scrolling = false;
            onScrollEnd(event);
        }
        return retVal;
    }

    private void hideHandle() {
        ScrollHandle scrollHandle = this.pdfView.getScrollHandle();
        if (scrollHandle != null && scrollHandle.shown()) {
            scrollHandle.hideDelayed();
        }
    }

    private boolean checkDoPageFling(float velocityX, float velocityY) {
        float absX = Math.abs(velocityX);
        float absY = Math.abs(velocityY);
        if (this.pdfView.isSwipeVertical()) {
            if (absY > absX) {
                return true;
            }
        } else if (absX > absY) {
            return true;
        }
        return false;
    }
}
