package com.github.barteksc.pdfviewer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.view.animation.DecelerateInterpolator;
import android.widget.OverScroller;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class AnimationManager {
    private ValueAnimator animation;
    private boolean flinging = false;
    private boolean pageFlinging = false;
    private PDFView pdfView;
    private OverScroller scroller;

    public AnimationManager(PDFView pdfView) {
        this.pdfView = pdfView;
        this.scroller = new OverScroller(pdfView.getContext());
    }

    public void startXAnimation(float xFrom, float xTo) {
        stopAll();
        this.animation = ValueAnimator.ofFloat(xFrom, xTo);
        XAnimation xAnimation = new XAnimation();
        this.animation.setInterpolator(new DecelerateInterpolator());
        this.animation.addUpdateListener(xAnimation);
        this.animation.addListener(xAnimation);
        this.animation.setDuration(400L);
        this.animation.start();
    }

    public void startYAnimation(float yFrom, float yTo) {
        stopAll();
        this.animation = ValueAnimator.ofFloat(yFrom, yTo);
        YAnimation yAnimation = new YAnimation();
        this.animation.setInterpolator(new DecelerateInterpolator());
        this.animation.addUpdateListener(yAnimation);
        this.animation.addListener(yAnimation);
        this.animation.setDuration(400L);
        this.animation.start();
    }

    public void startZoomAnimation(float centerX, float centerY, float zoomFrom, float zoomTo) {
        stopAll();
        this.animation = ValueAnimator.ofFloat(zoomFrom, zoomTo);
        this.animation.setInterpolator(new DecelerateInterpolator());
        ZoomAnimation zoomAnim = new ZoomAnimation(centerX, centerY);
        this.animation.addUpdateListener(zoomAnim);
        this.animation.addListener(zoomAnim);
        this.animation.setDuration(400L);
        this.animation.start();
    }

    public void startFlingAnimation(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
        stopAll();
        this.flinging = true;
        this.scroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
    }

    public void startPageFlingAnimation(float targetOffset) {
        if (this.pdfView.isSwipeVertical()) {
            startYAnimation(this.pdfView.getCurrentYOffset(), targetOffset);
        } else {
            startXAnimation(this.pdfView.getCurrentXOffset(), targetOffset);
        }
        this.pageFlinging = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void computeFling() {
        if (this.scroller.computeScrollOffset()) {
            this.pdfView.moveTo((float) this.scroller.getCurrX(), (float) this.scroller.getCurrY());
            this.pdfView.loadPageByOffset();
        } else if (this.flinging) {
            this.flinging = false;
            this.pdfView.loadPages();
            hideHandle();
            this.pdfView.performPageSnap();
        }
    }

    public void stopAll() {
        ValueAnimator valueAnimator = this.animation;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.animation = null;
        }
        stopFling();
    }

    public void stopFling() {
        this.flinging = false;
        this.scroller.forceFinished(true);
    }

    public boolean isFlinging() {
        return this.flinging || this.pageFlinging;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class XAnimation extends AnimatorListenerAdapter implements ValueAnimator.AnimatorUpdateListener {
        XAnimation() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator animation) {
            AnimationManager.this.pdfView.moveTo(((Float) animation.getAnimatedValue()).floatValue(), AnimationManager.this.pdfView.getCurrentYOffset());
            AnimationManager.this.pdfView.loadPageByOffset();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animation) {
            AnimationManager.this.pdfView.loadPages();
            AnimationManager.this.pageFlinging = false;
            AnimationManager.this.hideHandle();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animation) {
            AnimationManager.this.pdfView.loadPages();
            AnimationManager.this.pageFlinging = false;
            AnimationManager.this.hideHandle();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class YAnimation extends AnimatorListenerAdapter implements ValueAnimator.AnimatorUpdateListener {
        YAnimation() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator animation) {
            AnimationManager.this.pdfView.moveTo(AnimationManager.this.pdfView.getCurrentXOffset(), ((Float) animation.getAnimatedValue()).floatValue());
            AnimationManager.this.pdfView.loadPageByOffset();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animation) {
            AnimationManager.this.pdfView.loadPages();
            AnimationManager.this.pageFlinging = false;
            AnimationManager.this.hideHandle();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animation) {
            AnimationManager.this.pdfView.loadPages();
            AnimationManager.this.pageFlinging = false;
            AnimationManager.this.hideHandle();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class ZoomAnimation implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
        private final float centerX;
        private final float centerY;

        public ZoomAnimation(float centerX, float centerY) {
            this.centerX = centerX;
            this.centerY = centerY;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator animation) {
            AnimationManager.this.pdfView.zoomCenteredTo(((Float) animation.getAnimatedValue()).floatValue(), new PointF(this.centerX, this.centerY));
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animation) {
            AnimationManager.this.pdfView.loadPages();
            AnimationManager.this.hideHandle();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animation) {
            AnimationManager.this.pdfView.loadPages();
            AnimationManager.this.pdfView.performPageSnap();
            AnimationManager.this.hideHandle();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animation) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animation) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideHandle() {
        if (this.pdfView.getScrollHandle() != null) {
            this.pdfView.getScrollHandle().hideDelayed();
        }
    }
}
