package com.github.barteksc.pdfviewer;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.github.barteksc.pdfviewer.exception.PageRenderingException;
import com.github.barteksc.pdfviewer.model.PagePart;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class RenderingHandler extends Handler {
    static final int MSG_RENDER_TASK = 1;
    private static final String TAG = RenderingHandler.class.getName();
    private PDFView pdfView;
    private RectF renderBounds = new RectF();
    private Rect roundedRenderBounds = new Rect();
    private Matrix renderMatrix = new Matrix();
    private boolean running = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RenderingHandler(Looper looper, PDFView pdfView) {
        super(looper);
        this.pdfView = pdfView;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addRenderingTask(int page, float width, float height, RectF bounds, boolean thumbnail, int cacheOrder, boolean bestQuality, boolean annotationRendering) {
        sendMessage(obtainMessage(1, new RenderingTask(width, height, bounds, page, thumbnail, cacheOrder, bestQuality, annotationRendering)));
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        try {
            final PagePart part = proceed((RenderingTask) message.obj);
            if (part == null) {
                return;
            }
            if (this.running) {
                this.pdfView.post(new Runnable() { // from class: com.github.barteksc.pdfviewer.RenderingHandler.1
                    @Override // java.lang.Runnable
                    public void run() {
                        RenderingHandler.this.pdfView.onBitmapRendered(part);
                    }
                });
            } else {
                part.getRenderedBitmap().recycle();
            }
        } catch (PageRenderingException ex) {
            while (true) {
                this.pdfView.post(new Runnable() { // from class: com.github.barteksc.pdfviewer.RenderingHandler.2
                    @Override // java.lang.Runnable
                    public void run() {
                        RenderingHandler.this.pdfView.onPageError(ex);
                    }
                });
                return;
            }
        }
    }

    private PagePart proceed(RenderingTask renderingTask) throws PageRenderingException {
        PdfFile pdfFile = this.pdfView.pdfFile;
        pdfFile.openPage(renderingTask.page);
        int w = Math.round(renderingTask.width);
        int h = Math.round(renderingTask.height);
        if (w == 0 || h == 0 || pdfFile.pageHasError(renderingTask.page)) {
            return null;
        }
        try {
            Bitmap render = Bitmap.createBitmap(w, h, renderingTask.bestQuality ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            calculateBounds(w, h, renderingTask.bounds);
            pdfFile.renderPageBitmap(render, renderingTask.page, this.roundedRenderBounds, renderingTask.annotationRendering);
            return new PagePart(renderingTask.page, render, renderingTask.bounds, renderingTask.thumbnail, renderingTask.cacheOrder);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Cannot create bitmap", e);
            return null;
        }
    }

    private void calculateBounds(int width, int height, RectF pageSliceBounds) {
        this.renderMatrix.reset();
        this.renderMatrix.postTranslate((-pageSliceBounds.left) * ((float) width), (-pageSliceBounds.top) * ((float) height));
        this.renderMatrix.postScale(1.0f / pageSliceBounds.width(), 1.0f / pageSliceBounds.height());
        this.renderBounds.set(0.0f, 0.0f, (float) width, (float) height);
        this.renderMatrix.mapRect(this.renderBounds);
        this.renderBounds.round(this.roundedRenderBounds);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stop() {
        this.running = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void start() {
        this.running = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class RenderingTask {
        boolean annotationRendering;
        boolean bestQuality;
        RectF bounds;
        int cacheOrder;
        float height;
        int page;
        boolean thumbnail;
        float width;

        RenderingTask(float width, float height, RectF bounds, int page, boolean thumbnail, int cacheOrder, boolean bestQuality, boolean annotationRendering) {
            this.page = page;
            this.width = width;
            this.height = height;
            this.bounds = bounds;
            this.thumbnail = thumbnail;
            this.cacheOrder = cacheOrder;
            this.bestQuality = bestQuality;
            this.annotationRendering = annotationRendering;
        }
    }
}
