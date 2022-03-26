package com.github.barteksc.pdfviewer.scroll;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.R;
import com.github.barteksc.pdfviewer.util.Util;
/* loaded from: classes.dex */
public class DefaultScrollHandle extends RelativeLayout implements ScrollHandle {
    private static final int DEFAULT_TEXT_SIZE = 16;
    private static final int HANDLE_LONG = 65;
    private static final int HANDLE_SHORT = 40;
    protected Context context;
    private float currentPos;
    private Handler handler;
    private Runnable hidePageScrollerRunnable;
    private boolean inverted;
    private PDFView pdfView;
    private float relativeHandlerMiddle;
    protected TextView textView;

    public DefaultScrollHandle(Context context) {
        this(context, false);
    }

    public DefaultScrollHandle(Context context, boolean inverted) {
        super(context);
        this.relativeHandlerMiddle = 0.0f;
        this.handler = new Handler();
        this.hidePageScrollerRunnable = new Runnable() { // from class: com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle.1
            @Override // java.lang.Runnable
            public void run() {
                DefaultScrollHandle.this.hide();
            }
        };
        this.context = context;
        this.inverted = inverted;
        this.textView = new TextView(context);
        setVisibility(4);
        setTextColor(ViewCompat.MEASURED_STATE_MASK);
        setTextSize(16);
    }

    @Override // com.github.barteksc.pdfviewer.scroll.ScrollHandle
    public void setupLayout(PDFView pdfView) {
        Drawable background;
        int align;
        int height;
        int width;
        if (pdfView.isSwipeVertical()) {
            width = 65;
            height = 40;
            if (this.inverted) {
                align = 9;
                background = ContextCompat.getDrawable(this.context, R.drawable.default_scroll_handle_left);
            } else {
                align = 11;
                background = ContextCompat.getDrawable(this.context, R.drawable.default_scroll_handle_right);
            }
        } else {
            width = 40;
            height = 65;
            if (this.inverted) {
                align = 10;
                background = ContextCompat.getDrawable(this.context, R.drawable.default_scroll_handle_top);
            } else {
                align = 12;
                background = ContextCompat.getDrawable(this.context, R.drawable.default_scroll_handle_bottom);
            }
        }
        if (Build.VERSION.SDK_INT < 16) {
            setBackgroundDrawable(background);
        } else {
            setBackground(background);
        }
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(Util.getDP(this.context, width), Util.getDP(this.context, height));
        lp.setMargins(0, 0, 0, 0);
        RelativeLayout.LayoutParams tvlp = new RelativeLayout.LayoutParams(-2, -2);
        tvlp.addRule(13, -1);
        addView(this.textView, tvlp);
        lp.addRule(align);
        pdfView.addView(this, lp);
        this.pdfView = pdfView;
    }

    @Override // com.github.barteksc.pdfviewer.scroll.ScrollHandle
    public void destroyLayout() {
        this.pdfView.removeView(this);
    }

    @Override // com.github.barteksc.pdfviewer.scroll.ScrollHandle
    public void setScroll(float position) {
        if (!shown()) {
            show();
        } else {
            this.handler.removeCallbacks(this.hidePageScrollerRunnable);
        }
        PDFView pDFView = this.pdfView;
        if (pDFView != null) {
            setPosition(((float) (pDFView.isSwipeVertical() ? this.pdfView.getHeight() : this.pdfView.getWidth())) * position);
        }
    }

    private void setPosition(float pos) {
        float pdfViewSize;
        if (!Float.isInfinite(pos) && !Float.isNaN(pos)) {
            if (this.pdfView.isSwipeVertical()) {
                pdfViewSize = (float) this.pdfView.getHeight();
            } else {
                pdfViewSize = (float) this.pdfView.getWidth();
            }
            float pos2 = pos - this.relativeHandlerMiddle;
            if (pos2 < 0.0f) {
                pos2 = 0.0f;
            } else if (pos2 > pdfViewSize - ((float) Util.getDP(this.context, 40))) {
                pos2 = pdfViewSize - ((float) Util.getDP(this.context, 40));
            }
            if (this.pdfView.isSwipeVertical()) {
                setY(pos2);
            } else {
                setX(pos2);
            }
            calculateMiddle();
            invalidate();
        }
    }

    private void calculateMiddle() {
        float pdfViewSize;
        float viewSize;
        float pos;
        if (this.pdfView.isSwipeVertical()) {
            pos = getY();
            viewSize = (float) getHeight();
            pdfViewSize = (float) this.pdfView.getHeight();
        } else {
            pos = getX();
            viewSize = (float) getWidth();
            pdfViewSize = (float) this.pdfView.getWidth();
        }
        this.relativeHandlerMiddle = ((this.relativeHandlerMiddle + pos) / pdfViewSize) * viewSize;
    }

    @Override // com.github.barteksc.pdfviewer.scroll.ScrollHandle
    public void hideDelayed() {
        this.handler.postDelayed(this.hidePageScrollerRunnable, 1000);
    }

    @Override // com.github.barteksc.pdfviewer.scroll.ScrollHandle
    public void setPageNum(int pageNum) {
        String text = String.valueOf(pageNum);
        if (!this.textView.getText().equals(text)) {
            this.textView.setText(text);
        }
    }

    @Override // com.github.barteksc.pdfviewer.scroll.ScrollHandle
    public boolean shown() {
        return getVisibility() == 0;
    }

    @Override // com.github.barteksc.pdfviewer.scroll.ScrollHandle
    public void show() {
        setVisibility(0);
    }

    @Override // com.github.barteksc.pdfviewer.scroll.ScrollHandle
    public void hide() {
        setVisibility(4);
    }

    public void setTextColor(int color) {
        this.textView.setTextColor(color);
    }

    public void setTextSize(int size) {
        this.textView.setTextSize(1, (float) size);
    }

    private boolean isPDFViewReady() {
        PDFView pDFView = this.pdfView;
        return pDFView != null && pDFView.getPageCount() > 0 && !this.pdfView.documentFitsView();
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x0062  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x007d  */
    @Override // android.view.View
    /* Code decompiled incorrectly, please refer to instructions dump */
    public boolean onTouchEvent(MotionEvent event) {
        if (!isPDFViewReady()) {
            return super.onTouchEvent(event);
        }
        int action = event.getAction();
        if (action != 0) {
            if (action != 1) {
                if (action != 2) {
                    if (action != 3) {
                        if (action != 5) {
                            if (action != 6) {
                                return super.onTouchEvent(event);
                            }
                        }
                    }
                }
                if (!this.pdfView.isSwipeVertical()) {
                    setPosition((event.getRawY() - this.currentPos) + this.relativeHandlerMiddle);
                    this.pdfView.setPositionOffset(this.relativeHandlerMiddle / ((float) getHeight()), false);
                } else {
                    setPosition((event.getRawX() - this.currentPos) + this.relativeHandlerMiddle);
                    this.pdfView.setPositionOffset(this.relativeHandlerMiddle / ((float) getWidth()), false);
                }
                return true;
            }
            hideDelayed();
            this.pdfView.performPageSnap();
            return true;
        }
        this.pdfView.stopFling();
        this.handler.removeCallbacks(this.hidePageScrollerRunnable);
        if (this.pdfView.isSwipeVertical()) {
            this.currentPos = event.getRawY() - getY();
        } else {
            this.currentPos = event.getRawX() - getX();
        }
        if (!this.pdfView.isSwipeVertical()) {
        }
        return true;
    }
}
