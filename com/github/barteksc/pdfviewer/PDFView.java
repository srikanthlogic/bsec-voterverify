package com.github.barteksc.pdfviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import com.github.barteksc.pdfviewer.exception.PageRenderingException;
import com.github.barteksc.pdfviewer.link.DefaultLinkHandler;
import com.github.barteksc.pdfviewer.link.LinkHandler;
import com.github.barteksc.pdfviewer.listener.Callbacks;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnLongPressListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.model.PagePart;
import com.github.barteksc.pdfviewer.scroll.ScrollHandle;
import com.github.barteksc.pdfviewer.source.AssetSource;
import com.github.barteksc.pdfviewer.source.ByteArraySource;
import com.github.barteksc.pdfviewer.source.DocumentSource;
import com.github.barteksc.pdfviewer.source.FileSource;
import com.github.barteksc.pdfviewer.source.InputStreamSource;
import com.github.barteksc.pdfviewer.source.UriSource;
import com.github.barteksc.pdfviewer.util.Constants;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.github.barteksc.pdfviewer.util.MathUtils;
import com.github.barteksc.pdfviewer.util.SnapEdge;
import com.github.barteksc.pdfviewer.util.Util;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import com.shockwave.pdfium.util.Size;
import com.shockwave.pdfium.util.SizeF;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public class PDFView extends RelativeLayout {
    public static final float DEFAULT_MAX_SCALE;
    public static final float DEFAULT_MID_SCALE;
    public static final float DEFAULT_MIN_SCALE;
    private static final String TAG = PDFView.class.getSimpleName();
    private AnimationManager animationManager;
    CacheManager cacheManager;
    private int currentPage;
    private Paint debugPaint;
    private DecodingAsyncTask decodingAsyncTask;
    private DragPinchManager dragPinchManager;
    private PagesLoader pagesLoader;
    private Paint paint;
    PdfFile pdfFile;
    private PdfiumCore pdfiumCore;
    RenderingHandler renderingHandler;
    private ScrollHandle scrollHandle;
    private Configurator waitingDocumentConfigurator;
    private float minZoom = 1.0f;
    private float midZoom = 1.75f;
    private float maxZoom = 3.0f;
    private ScrollDir scrollDir = ScrollDir.NONE;
    private float currentXOffset = 0.0f;
    private float currentYOffset = 0.0f;
    private float zoom = 1.0f;
    private boolean recycled = true;
    private State state = State.DEFAULT;
    Callbacks callbacks = new Callbacks();
    private FitPolicy pageFitPolicy = FitPolicy.WIDTH;
    private boolean fitEachPage = false;
    private int defaultPage = 0;
    private boolean swipeVertical = true;
    private boolean enableSwipe = true;
    private boolean doubletapEnabled = true;
    private boolean nightMode = false;
    private boolean pageSnap = true;
    private boolean isScrollHandleInit = false;
    private boolean bestQuality = false;
    private boolean annotationRendering = false;
    private boolean renderDuringScale = false;
    private boolean enableAntialiasing = true;
    private PaintFlagsDrawFilter antialiasFilter = new PaintFlagsDrawFilter(0, 3);
    private int spacingPx = 0;
    private boolean autoSpacing = false;
    private boolean pageFling = true;
    private List<Integer> onDrawPagesNums = new ArrayList(10);
    private boolean hasSize = false;
    private HandlerThread renderingHandlerThread = new HandlerThread("PDF renderer");

    /* loaded from: classes.dex */
    public enum ScrollDir {
        NONE,
        START,
        END
    }

    /* loaded from: classes.dex */
    public enum State {
        DEFAULT,
        LOADED,
        SHOWN,
        ERROR
    }

    public ScrollHandle getScrollHandle() {
        return this.scrollHandle;
    }

    public PDFView(Context context, AttributeSet set) {
        super(context, set);
        if (!isInEditMode()) {
            this.cacheManager = new CacheManager();
            this.animationManager = new AnimationManager(this);
            this.dragPinchManager = new DragPinchManager(this, this.animationManager);
            this.pagesLoader = new PagesLoader(this);
            this.paint = new Paint();
            this.debugPaint = new Paint();
            this.debugPaint.setStyle(Paint.Style.STROKE);
            this.pdfiumCore = new PdfiumCore(context);
            setWillNotDraw(false);
        }
    }

    public void load(DocumentSource docSource, String password) {
        load(docSource, password, null);
    }

    public void load(DocumentSource docSource, String password, int[] userPages) {
        if (this.recycled) {
            this.recycled = false;
            this.decodingAsyncTask = new DecodingAsyncTask(docSource, password, userPages, this, this.pdfiumCore);
            this.decodingAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
            return;
        }
        throw new IllegalStateException("Don't call load on a PDF View without recycling it first.");
    }

    public void jumpTo(int page, boolean withAnimation) {
        PdfFile pdfFile = this.pdfFile;
        if (pdfFile != null) {
            int page2 = pdfFile.determineValidPageNumberFrom(page);
            float offset = page2 == 0 ? 0.0f : -this.pdfFile.getPageOffset(page2, this.zoom);
            if (this.swipeVertical) {
                if (withAnimation) {
                    this.animationManager.startYAnimation(this.currentYOffset, offset);
                } else {
                    moveTo(this.currentXOffset, offset);
                }
            } else if (withAnimation) {
                this.animationManager.startXAnimation(this.currentXOffset, offset);
            } else {
                moveTo(offset, this.currentYOffset);
            }
            showPage(page2);
        }
    }

    public void jumpTo(int page) {
        jumpTo(page, false);
    }

    void showPage(int pageNb) {
        if (!this.recycled) {
            this.currentPage = this.pdfFile.determineValidPageNumberFrom(pageNb);
            loadPages();
            if (this.scrollHandle != null && !documentFitsView()) {
                this.scrollHandle.setPageNum(this.currentPage + 1);
            }
            this.callbacks.callOnPageChange(this.currentPage, this.pdfFile.getPagesCount());
        }
    }

    public float getPositionOffset() {
        float offset;
        if (this.swipeVertical) {
            offset = (-this.currentYOffset) / (this.pdfFile.getDocLen(this.zoom) - ((float) getHeight()));
        } else {
            offset = (-this.currentXOffset) / (this.pdfFile.getDocLen(this.zoom) - ((float) getWidth()));
        }
        return MathUtils.limit(offset, 0.0f, 1.0f);
    }

    public void setPositionOffset(float progress, boolean moveHandle) {
        if (this.swipeVertical) {
            moveTo(this.currentXOffset, ((-this.pdfFile.getDocLen(this.zoom)) + ((float) getHeight())) * progress, moveHandle);
        } else {
            moveTo(((-this.pdfFile.getDocLen(this.zoom)) + ((float) getWidth())) * progress, this.currentYOffset, moveHandle);
        }
        loadPageByOffset();
    }

    public void setPositionOffset(float progress) {
        setPositionOffset(progress, true);
    }

    public void stopFling() {
        this.animationManager.stopFling();
    }

    public int getPageCount() {
        PdfFile pdfFile = this.pdfFile;
        if (pdfFile == null) {
            return 0;
        }
        return pdfFile.getPagesCount();
    }

    public void setSwipeEnabled(boolean enableSwipe) {
        this.enableSwipe = enableSwipe;
    }

    public void setNightMode(boolean nightMode) {
        this.nightMode = nightMode;
        if (nightMode) {
            this.paint.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(new float[]{-1.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, -1.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, -1.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f})));
            return;
        }
        this.paint.setColorFilter(null);
    }

    void enableDoubletap(boolean enableDoubletap) {
        this.doubletapEnabled = enableDoubletap;
    }

    public boolean isDoubletapEnabled() {
        return this.doubletapEnabled;
    }

    public void onPageError(PageRenderingException ex) {
        if (!this.callbacks.callOnPageError(ex.getPage(), ex.getCause())) {
            String str = TAG;
            Log.e(str, "Cannot open page " + ex.getPage(), ex.getCause());
        }
    }

    public void recycle() {
        this.waitingDocumentConfigurator = null;
        this.animationManager.stopAll();
        this.dragPinchManager.disable();
        RenderingHandler renderingHandler = this.renderingHandler;
        if (renderingHandler != null) {
            renderingHandler.stop();
            this.renderingHandler.removeMessages(1);
        }
        DecodingAsyncTask decodingAsyncTask = this.decodingAsyncTask;
        if (decodingAsyncTask != null) {
            decodingAsyncTask.cancel(true);
        }
        this.cacheManager.recycle();
        ScrollHandle scrollHandle = this.scrollHandle;
        if (scrollHandle != null && this.isScrollHandleInit) {
            scrollHandle.destroyLayout();
        }
        PdfFile pdfFile = this.pdfFile;
        if (pdfFile != null) {
            pdfFile.dispose();
            this.pdfFile = null;
        }
        this.renderingHandler = null;
        this.scrollHandle = null;
        this.isScrollHandleInit = false;
        this.currentYOffset = 0.0f;
        this.currentXOffset = 0.0f;
        this.zoom = 1.0f;
        this.recycled = true;
        this.callbacks = new Callbacks();
        this.state = State.DEFAULT;
    }

    public boolean isRecycled() {
        return this.recycled;
    }

    @Override // android.view.View
    public void computeScroll() {
        super.computeScroll();
        if (!isInEditMode()) {
            this.animationManager.computeFling();
        }
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onDetachedFromWindow() {
        recycle();
        if (this.renderingHandlerThread != null) {
            if (Build.VERSION.SDK_INT >= 18) {
                this.renderingHandlerThread.quitSafely();
            } else {
                this.renderingHandlerThread.quit();
            }
            this.renderingHandlerThread = null;
        }
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        float relativeCenterPointInStripYOffset;
        float relativeCenterPointInStripXOffset;
        this.hasSize = true;
        Configurator configurator = this.waitingDocumentConfigurator;
        if (configurator != null) {
            configurator.load();
        }
        if (!isInEditMode() && this.state == State.SHOWN) {
            float centerPointInStripXOffset = (-this.currentXOffset) + (((float) oldw) * 0.5f);
            float centerPointInStripYOffset = (-this.currentYOffset) + (((float) oldh) * 0.5f);
            if (this.swipeVertical) {
                relativeCenterPointInStripXOffset = centerPointInStripXOffset / this.pdfFile.getMaxPageWidth();
                relativeCenterPointInStripYOffset = centerPointInStripYOffset / this.pdfFile.getDocLen(this.zoom);
            } else {
                relativeCenterPointInStripXOffset = centerPointInStripXOffset / this.pdfFile.getDocLen(this.zoom);
                relativeCenterPointInStripYOffset = centerPointInStripYOffset / this.pdfFile.getMaxPageHeight();
            }
            this.animationManager.stopAll();
            this.pdfFile.recalculatePageSizes(new Size(w, h));
            if (this.swipeVertical) {
                this.currentXOffset = ((-relativeCenterPointInStripXOffset) * this.pdfFile.getMaxPageWidth()) + (((float) w) * 0.5f);
                this.currentYOffset = ((-relativeCenterPointInStripYOffset) * this.pdfFile.getDocLen(this.zoom)) + (((float) h) * 0.5f);
            } else {
                this.currentXOffset = ((-relativeCenterPointInStripXOffset) * this.pdfFile.getDocLen(this.zoom)) + (((float) w) * 0.5f);
                this.currentYOffset = ((-relativeCenterPointInStripYOffset) * this.pdfFile.getMaxPageHeight()) + (((float) h) * 0.5f);
            }
            moveTo(this.currentXOffset, this.currentYOffset);
            loadPageByOffset();
        }
    }

    @Override // android.view.View
    public boolean canScrollHorizontally(int direction) {
        if (this.pdfFile == null) {
            return true;
        }
        if (this.swipeVertical) {
            if (direction < 0 && this.currentXOffset < 0.0f) {
                return true;
            }
            if (direction <= 0 || this.currentXOffset + toCurrentScale(this.pdfFile.getMaxPageWidth()) <= ((float) getWidth())) {
                return false;
            }
            return true;
        } else if (direction < 0 && this.currentXOffset < 0.0f) {
            return true;
        } else {
            if (direction <= 0 || this.currentXOffset + this.pdfFile.getDocLen(this.zoom) <= ((float) getWidth())) {
                return false;
            }
            return true;
        }
    }

    @Override // android.view.View
    public boolean canScrollVertically(int direction) {
        if (this.pdfFile == null) {
            return true;
        }
        if (this.swipeVertical) {
            if (direction < 0 && this.currentYOffset < 0.0f) {
                return true;
            }
            if (direction <= 0 || this.currentYOffset + this.pdfFile.getDocLen(this.zoom) <= ((float) getHeight())) {
                return false;
            }
            return true;
        } else if (direction < 0 && this.currentYOffset < 0.0f) {
            return true;
        } else {
            if (direction <= 0 || this.currentYOffset + toCurrentScale(this.pdfFile.getMaxPageHeight()) <= ((float) getHeight())) {
                return false;
            }
            return true;
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (!isInEditMode()) {
            if (this.enableAntialiasing) {
                canvas.setDrawFilter(this.antialiasFilter);
            }
            Drawable bg = getBackground();
            if (bg == null) {
                canvas.drawColor(this.nightMode ? ViewCompat.MEASURED_STATE_MASK : -1);
            } else {
                bg.draw(canvas);
            }
            if (!this.recycled && this.state == State.SHOWN) {
                float currentXOffset = this.currentXOffset;
                float currentYOffset = this.currentYOffset;
                canvas.translate(currentXOffset, currentYOffset);
                for (PagePart part : this.cacheManager.getThumbnails()) {
                    drawPart(canvas, part);
                }
                for (PagePart part2 : this.cacheManager.getPageParts()) {
                    drawPart(canvas, part2);
                    if (this.callbacks.getOnDrawAll() != null && !this.onDrawPagesNums.contains(Integer.valueOf(part2.getPage()))) {
                        this.onDrawPagesNums.add(Integer.valueOf(part2.getPage()));
                    }
                }
                for (Integer page : this.onDrawPagesNums) {
                    drawWithListener(canvas, page.intValue(), this.callbacks.getOnDrawAll());
                }
                this.onDrawPagesNums.clear();
                drawWithListener(canvas, this.currentPage, this.callbacks.getOnDraw());
                canvas.translate(-currentXOffset, -currentYOffset);
            }
        }
    }

    private void drawWithListener(Canvas canvas, int page, OnDrawListener listener) {
        float translateY;
        float translateX;
        if (listener != null) {
            if (this.swipeVertical) {
                translateX = 0.0f;
                translateY = this.pdfFile.getPageOffset(page, this.zoom);
            } else {
                translateY = 0.0f;
                translateX = this.pdfFile.getPageOffset(page, this.zoom);
            }
            canvas.translate(translateX, translateY);
            SizeF size = this.pdfFile.getPageSize(page);
            listener.onLayerDrawn(canvas, toCurrentScale(size.getWidth()), toCurrentScale(size.getHeight()), page);
            canvas.translate(-translateX, -translateY);
        }
    }

    private void drawPart(Canvas canvas, PagePart part) {
        float localTranslationX;
        float localTranslationY;
        RectF pageRelativeBounds = part.getPageRelativeBounds();
        Bitmap renderedBitmap = part.getRenderedBitmap();
        if (!renderedBitmap.isRecycled()) {
            SizeF size = this.pdfFile.getPageSize(part.getPage());
            if (this.swipeVertical) {
                localTranslationY = this.pdfFile.getPageOffset(part.getPage(), this.zoom);
                localTranslationX = toCurrentScale(this.pdfFile.getMaxPageWidth() - size.getWidth()) / 2.0f;
            } else {
                localTranslationX = this.pdfFile.getPageOffset(part.getPage(), this.zoom);
                localTranslationY = toCurrentScale(this.pdfFile.getMaxPageHeight() - size.getHeight()) / 2.0f;
            }
            canvas.translate(localTranslationX, localTranslationY);
            Rect srcRect = new Rect(0, 0, renderedBitmap.getWidth(), renderedBitmap.getHeight());
            float offsetX = toCurrentScale(pageRelativeBounds.left * size.getWidth());
            float offsetY = toCurrentScale(pageRelativeBounds.top * size.getHeight());
            RectF dstRect = new RectF((float) ((int) offsetX), (float) ((int) offsetY), (float) ((int) (offsetX + toCurrentScale(pageRelativeBounds.width() * size.getWidth()))), (float) ((int) (offsetY + toCurrentScale(pageRelativeBounds.height() * size.getHeight()))));
            float translationX = this.currentXOffset + localTranslationX;
            float translationY = this.currentYOffset + localTranslationY;
            if (dstRect.left + translationX >= ((float) getWidth()) || dstRect.right + translationX <= 0.0f || dstRect.top + translationY >= ((float) getHeight()) || dstRect.bottom + translationY <= 0.0f) {
                canvas.translate(-localTranslationX, -localTranslationY);
                return;
            }
            canvas.drawBitmap(renderedBitmap, srcRect, dstRect, this.paint);
            if (Constants.DEBUG_MODE) {
                this.debugPaint.setColor(part.getPage() % 2 == 0 ? SupportMenu.CATEGORY_MASK : -16776961);
                canvas.drawRect(dstRect, this.debugPaint);
            }
            canvas.translate(-localTranslationX, -localTranslationY);
        }
    }

    public void loadPages() {
        RenderingHandler renderingHandler;
        if (this.pdfFile != null && (renderingHandler = this.renderingHandler) != null) {
            renderingHandler.removeMessages(1);
            this.cacheManager.makeANewSet();
            this.pagesLoader.loadPages();
            redraw();
        }
    }

    public void loadComplete(PdfFile pdfFile) {
        this.state = State.LOADED;
        this.pdfFile = pdfFile;
        if (!this.renderingHandlerThread.isAlive()) {
            this.renderingHandlerThread.start();
        }
        this.renderingHandler = new RenderingHandler(this.renderingHandlerThread.getLooper(), this);
        this.renderingHandler.start();
        ScrollHandle scrollHandle = this.scrollHandle;
        if (scrollHandle != null) {
            scrollHandle.setupLayout(this);
            this.isScrollHandleInit = true;
        }
        this.dragPinchManager.enable();
        this.callbacks.callOnLoadComplete(pdfFile.getPagesCount());
        jumpTo(this.defaultPage, false);
    }

    public void loadError(Throwable t) {
        this.state = State.ERROR;
        OnErrorListener onErrorListener = this.callbacks.getOnError();
        recycle();
        invalidate();
        if (onErrorListener != null) {
            onErrorListener.onError(t);
        } else {
            Log.e("PDFView", "load pdf error", t);
        }
    }

    void redraw() {
        invalidate();
    }

    public void onBitmapRendered(PagePart part) {
        if (this.state == State.LOADED) {
            this.state = State.SHOWN;
            this.callbacks.callOnRender(this.pdfFile.getPagesCount());
        }
        if (part.isThumbnail()) {
            this.cacheManager.cacheThumbnail(part);
        } else {
            this.cacheManager.cachePart(part);
        }
        redraw();
    }

    public void moveTo(float offsetX, float offsetY) {
        moveTo(offsetX, offsetY, true);
    }

    public void moveTo(float offsetX, float offsetY, boolean moveHandle) {
        if (this.swipeVertical) {
            float scaledPageWidth = toCurrentScale(this.pdfFile.getMaxPageWidth());
            if (scaledPageWidth < ((float) getWidth())) {
                offsetX = ((float) (getWidth() / 2)) - (scaledPageWidth / 2.0f);
            } else if (offsetX > 0.0f) {
                offsetX = 0.0f;
            } else if (offsetX + scaledPageWidth < ((float) getWidth())) {
                offsetX = ((float) getWidth()) - scaledPageWidth;
            }
            float contentHeight = this.pdfFile.getDocLen(this.zoom);
            if (contentHeight < ((float) getHeight())) {
                offsetY = (((float) getHeight()) - contentHeight) / 2.0f;
            } else if (offsetY > 0.0f) {
                offsetY = 0.0f;
            } else if (offsetY + contentHeight < ((float) getHeight())) {
                offsetY = (-contentHeight) + ((float) getHeight());
            }
            float f = this.currentYOffset;
            if (offsetY < f) {
                this.scrollDir = ScrollDir.END;
            } else if (offsetY > f) {
                this.scrollDir = ScrollDir.START;
            } else {
                this.scrollDir = ScrollDir.NONE;
            }
        } else {
            float scaledPageHeight = toCurrentScale(this.pdfFile.getMaxPageHeight());
            if (scaledPageHeight < ((float) getHeight())) {
                offsetY = ((float) (getHeight() / 2)) - (scaledPageHeight / 2.0f);
            } else if (offsetY > 0.0f) {
                offsetY = 0.0f;
            } else if (offsetY + scaledPageHeight < ((float) getHeight())) {
                offsetY = ((float) getHeight()) - scaledPageHeight;
            }
            float contentWidth = this.pdfFile.getDocLen(this.zoom);
            if (contentWidth < ((float) getWidth())) {
                offsetX = (((float) getWidth()) - contentWidth) / 2.0f;
            } else if (offsetX > 0.0f) {
                offsetX = 0.0f;
            } else if (offsetX + contentWidth < ((float) getWidth())) {
                offsetX = (-contentWidth) + ((float) getWidth());
            }
            float f2 = this.currentXOffset;
            if (offsetX < f2) {
                this.scrollDir = ScrollDir.END;
            } else if (offsetX > f2) {
                this.scrollDir = ScrollDir.START;
            } else {
                this.scrollDir = ScrollDir.NONE;
            }
        }
        this.currentXOffset = offsetX;
        this.currentYOffset = offsetY;
        float positionOffset = getPositionOffset();
        if (moveHandle && this.scrollHandle != null && !documentFitsView()) {
            this.scrollHandle.setScroll(positionOffset);
        }
        this.callbacks.callOnPageScroll(getCurrentPage(), positionOffset);
        redraw();
    }

    public void loadPageByOffset() {
        float screenCenter;
        float offset;
        if (this.pdfFile.getPagesCount() != 0) {
            if (this.swipeVertical) {
                offset = this.currentYOffset;
                screenCenter = ((float) getHeight()) / 2.0f;
            } else {
                offset = this.currentXOffset;
                screenCenter = ((float) getWidth()) / 2.0f;
            }
            int page = this.pdfFile.getPageAtOffset(-(offset - screenCenter), this.zoom);
            if (page < 0 || page > this.pdfFile.getPagesCount() - 1 || page == getCurrentPage()) {
                loadPages();
            } else {
                showPage(page);
            }
        }
    }

    public void performPageSnap() {
        PdfFile pdfFile;
        int centerPage;
        SnapEdge edge;
        if (this.pageSnap && (pdfFile = this.pdfFile) != null && pdfFile.getPagesCount() != 0 && (edge = findSnapEdge((centerPage = findFocusPage(this.currentXOffset, this.currentYOffset)))) != SnapEdge.NONE) {
            float offset = snapOffsetForPage(centerPage, edge);
            if (this.swipeVertical) {
                this.animationManager.startYAnimation(this.currentYOffset, -offset);
            } else {
                this.animationManager.startXAnimation(this.currentXOffset, -offset);
            }
        }
    }

    public SnapEdge findSnapEdge(int page) {
        if (!this.pageSnap || page < 0) {
            return SnapEdge.NONE;
        }
        float currentOffset = this.swipeVertical ? this.currentYOffset : this.currentXOffset;
        float offset = -this.pdfFile.getPageOffset(page, this.zoom);
        int length = this.swipeVertical ? getHeight() : getWidth();
        float pageLength = this.pdfFile.getPageLength(page, this.zoom);
        if (((float) length) >= pageLength) {
            return SnapEdge.CENTER;
        }
        if (currentOffset >= offset) {
            return SnapEdge.START;
        }
        if (offset - pageLength > currentOffset - ((float) length)) {
            return SnapEdge.END;
        }
        return SnapEdge.NONE;
    }

    public float snapOffsetForPage(int pageIndex, SnapEdge edge) {
        float offset = this.pdfFile.getPageOffset(pageIndex, this.zoom);
        float length = (float) (this.swipeVertical ? getHeight() : getWidth());
        float pageLength = this.pdfFile.getPageLength(pageIndex, this.zoom);
        if (edge == SnapEdge.CENTER) {
            return (offset - (length / 2.0f)) + (pageLength / 2.0f);
        }
        if (edge == SnapEdge.END) {
            return (offset - length) + pageLength;
        }
        return offset;
    }

    public int findFocusPage(float xOffset, float yOffset) {
        float currOffset = this.swipeVertical ? yOffset : xOffset;
        float length = (float) (this.swipeVertical ? getHeight() : getWidth());
        if (currOffset > -1.0f) {
            return 0;
        }
        if (currOffset < (-this.pdfFile.getDocLen(this.zoom)) + length + 1.0f) {
            return this.pdfFile.getPagesCount() - 1;
        }
        return this.pdfFile.getPageAtOffset(-(currOffset - (length / 2.0f)), this.zoom);
    }

    public boolean pageFillsScreen() {
        float start = -this.pdfFile.getPageOffset(this.currentPage, this.zoom);
        float end = start - this.pdfFile.getPageLength(this.currentPage, this.zoom);
        if (isSwipeVertical()) {
            float f = this.currentYOffset;
            return start > f && end < f - ((float) getHeight());
        }
        float f2 = this.currentXOffset;
        return start > f2 && end < f2 - ((float) getWidth());
    }

    public void moveRelativeTo(float dx, float dy) {
        moveTo(this.currentXOffset + dx, this.currentYOffset + dy);
    }

    public void zoomTo(float zoom) {
        this.zoom = zoom;
    }

    public void zoomCenteredTo(float zoom, PointF pivot) {
        float dzoom = zoom / this.zoom;
        zoomTo(zoom);
        moveTo((this.currentXOffset * dzoom) + (pivot.x - (pivot.x * dzoom)), (this.currentYOffset * dzoom) + (pivot.y - (pivot.y * dzoom)));
    }

    public void zoomCenteredRelativeTo(float dzoom, PointF pivot) {
        zoomCenteredTo(this.zoom * dzoom, pivot);
    }

    public boolean documentFitsView() {
        float len = this.pdfFile.getDocLen(1.0f);
        return this.swipeVertical ? len < ((float) getHeight()) : len < ((float) getWidth());
    }

    public void fitToWidth(int page) {
        if (this.state != State.SHOWN) {
            Log.e(TAG, "Cannot fit, document not rendered yet");
            return;
        }
        zoomTo(((float) getWidth()) / this.pdfFile.getPageSize(page).getWidth());
        jumpTo(page);
    }

    public SizeF getPageSize(int pageIndex) {
        PdfFile pdfFile = this.pdfFile;
        if (pdfFile == null) {
            return new SizeF(0.0f, 0.0f);
        }
        return pdfFile.getPageSize(pageIndex);
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public float getCurrentXOffset() {
        return this.currentXOffset;
    }

    public float getCurrentYOffset() {
        return this.currentYOffset;
    }

    public float toRealScale(float size) {
        return size / this.zoom;
    }

    public float toCurrentScale(float size) {
        return this.zoom * size;
    }

    public float getZoom() {
        return this.zoom;
    }

    public boolean isZooming() {
        return this.zoom != this.minZoom;
    }

    public void setDefaultPage(int defaultPage) {
        this.defaultPage = defaultPage;
    }

    public void resetZoom() {
        zoomTo(this.minZoom);
    }

    public void resetZoomWithAnimation() {
        zoomWithAnimation(this.minZoom);
    }

    public void zoomWithAnimation(float centerX, float centerY, float scale) {
        this.animationManager.startZoomAnimation(centerX, centerY, this.zoom, scale);
    }

    public void zoomWithAnimation(float scale) {
        this.animationManager.startZoomAnimation((float) (getWidth() / 2), (float) (getHeight() / 2), this.zoom, scale);
    }

    public void setScrollHandle(ScrollHandle scrollHandle) {
        this.scrollHandle = scrollHandle;
    }

    public int getPageAtPositionOffset(float positionOffset) {
        PdfFile pdfFile = this.pdfFile;
        return pdfFile.getPageAtOffset(pdfFile.getDocLen(this.zoom) * positionOffset, this.zoom);
    }

    public float getMinZoom() {
        return this.minZoom;
    }

    public void setMinZoom(float minZoom) {
        this.minZoom = minZoom;
    }

    public float getMidZoom() {
        return this.midZoom;
    }

    public void setMidZoom(float midZoom) {
        this.midZoom = midZoom;
    }

    public float getMaxZoom() {
        return this.maxZoom;
    }

    public void setMaxZoom(float maxZoom) {
        this.maxZoom = maxZoom;
    }

    public void useBestQuality(boolean bestQuality) {
        this.bestQuality = bestQuality;
    }

    public boolean isBestQuality() {
        return this.bestQuality;
    }

    public boolean isSwipeVertical() {
        return this.swipeVertical;
    }

    public boolean isSwipeEnabled() {
        return this.enableSwipe;
    }

    public void setSwipeVertical(boolean swipeVertical) {
        this.swipeVertical = swipeVertical;
    }

    public void enableAnnotationRendering(boolean annotationRendering) {
        this.annotationRendering = annotationRendering;
    }

    public boolean isAnnotationRendering() {
        return this.annotationRendering;
    }

    public void enableRenderDuringScale(boolean renderDuringScale) {
        this.renderDuringScale = renderDuringScale;
    }

    public boolean isAntialiasing() {
        return this.enableAntialiasing;
    }

    public void enableAntialiasing(boolean enableAntialiasing) {
        this.enableAntialiasing = enableAntialiasing;
    }

    public int getSpacingPx() {
        return this.spacingPx;
    }

    public boolean isAutoSpacingEnabled() {
        return this.autoSpacing;
    }

    public void setPageFling(boolean pageFling) {
        this.pageFling = pageFling;
    }

    public boolean isPageFlingEnabled() {
        return this.pageFling;
    }

    public void setSpacing(int spacingDp) {
        this.spacingPx = Util.getDP(getContext(), spacingDp);
    }

    public void setAutoSpacing(boolean autoSpacing) {
        this.autoSpacing = autoSpacing;
    }

    public void setPageFitPolicy(FitPolicy pageFitPolicy) {
        this.pageFitPolicy = pageFitPolicy;
    }

    public FitPolicy getPageFitPolicy() {
        return this.pageFitPolicy;
    }

    public void setFitEachPage(boolean fitEachPage) {
        this.fitEachPage = fitEachPage;
    }

    public boolean isFitEachPage() {
        return this.fitEachPage;
    }

    public boolean isPageSnap() {
        return this.pageSnap;
    }

    public void setPageSnap(boolean pageSnap) {
        this.pageSnap = pageSnap;
    }

    public boolean doRenderDuringScale() {
        return this.renderDuringScale;
    }

    public PdfDocument.Meta getDocumentMeta() {
        PdfFile pdfFile = this.pdfFile;
        if (pdfFile == null) {
            return null;
        }
        return pdfFile.getMetaData();
    }

    public List<PdfDocument.Bookmark> getTableOfContents() {
        PdfFile pdfFile = this.pdfFile;
        if (pdfFile == null) {
            return Collections.emptyList();
        }
        return pdfFile.getBookmarks();
    }

    public List<PdfDocument.Link> getLinks(int page) {
        PdfFile pdfFile = this.pdfFile;
        if (pdfFile == null) {
            return Collections.emptyList();
        }
        return pdfFile.getPageLinks(page);
    }

    public Configurator fromAsset(String assetName) {
        return new Configurator(new AssetSource(assetName));
    }

    public Configurator fromFile(File file) {
        return new Configurator(new FileSource(file));
    }

    public Configurator fromUri(Uri uri) {
        return new Configurator(new UriSource(uri));
    }

    public Configurator fromBytes(byte[] bytes) {
        return new Configurator(new ByteArraySource(bytes));
    }

    public Configurator fromStream(InputStream stream) {
        return new Configurator(new InputStreamSource(stream));
    }

    public Configurator fromSource(DocumentSource docSource) {
        return new Configurator(docSource);
    }

    /* loaded from: classes.dex */
    public class Configurator {
        private boolean annotationRendering;
        private boolean antialiasing;
        private boolean autoSpacing;
        private int defaultPage;
        private final DocumentSource documentSource;
        private boolean enableDoubletap;
        private boolean enableSwipe;
        private boolean fitEachPage;
        private LinkHandler linkHandler;
        private boolean nightMode;
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
        private FitPolicy pageFitPolicy;
        private boolean pageFling;
        private int[] pageNumbers;
        private boolean pageSnap;
        private String password;
        private ScrollHandle scrollHandle;
        private int spacing;
        private boolean swipeHorizontal;

        private Configurator(DocumentSource documentSource) {
            PDFView.this = this$0;
            this.pageNumbers = null;
            this.enableSwipe = true;
            this.enableDoubletap = true;
            this.linkHandler = new DefaultLinkHandler(PDFView.this);
            this.defaultPage = 0;
            this.swipeHorizontal = false;
            this.annotationRendering = false;
            this.password = null;
            this.scrollHandle = null;
            this.antialiasing = true;
            this.spacing = 0;
            this.autoSpacing = false;
            this.pageFitPolicy = FitPolicy.WIDTH;
            this.fitEachPage = false;
            this.pageFling = false;
            this.pageSnap = false;
            this.nightMode = false;
            this.documentSource = documentSource;
        }

        public Configurator pages(int... pageNumbers) {
            this.pageNumbers = pageNumbers;
            return this;
        }

        public Configurator enableSwipe(boolean enableSwipe) {
            this.enableSwipe = enableSwipe;
            return this;
        }

        public Configurator enableDoubletap(boolean enableDoubletap) {
            this.enableDoubletap = enableDoubletap;
            return this;
        }

        public Configurator enableAnnotationRendering(boolean annotationRendering) {
            this.annotationRendering = annotationRendering;
            return this;
        }

        public Configurator onDraw(OnDrawListener onDrawListener) {
            this.onDrawListener = onDrawListener;
            return this;
        }

        public Configurator onDrawAll(OnDrawListener onDrawAllListener) {
            this.onDrawAllListener = onDrawAllListener;
            return this;
        }

        public Configurator onLoad(OnLoadCompleteListener onLoadCompleteListener) {
            this.onLoadCompleteListener = onLoadCompleteListener;
            return this;
        }

        public Configurator onPageScroll(OnPageScrollListener onPageScrollListener) {
            this.onPageScrollListener = onPageScrollListener;
            return this;
        }

        public Configurator onError(OnErrorListener onErrorListener) {
            this.onErrorListener = onErrorListener;
            return this;
        }

        public Configurator onPageError(OnPageErrorListener onPageErrorListener) {
            this.onPageErrorListener = onPageErrorListener;
            return this;
        }

        public Configurator onPageChange(OnPageChangeListener onPageChangeListener) {
            this.onPageChangeListener = onPageChangeListener;
            return this;
        }

        public Configurator onRender(OnRenderListener onRenderListener) {
            this.onRenderListener = onRenderListener;
            return this;
        }

        public Configurator onTap(OnTapListener onTapListener) {
            this.onTapListener = onTapListener;
            return this;
        }

        public Configurator onLongPress(OnLongPressListener onLongPressListener) {
            this.onLongPressListener = onLongPressListener;
            return this;
        }

        public Configurator linkHandler(LinkHandler linkHandler) {
            this.linkHandler = linkHandler;
            return this;
        }

        public Configurator defaultPage(int defaultPage) {
            this.defaultPage = defaultPage;
            return this;
        }

        public Configurator swipeHorizontal(boolean swipeHorizontal) {
            this.swipeHorizontal = swipeHorizontal;
            return this;
        }

        public Configurator password(String password) {
            this.password = password;
            return this;
        }

        public Configurator scrollHandle(ScrollHandle scrollHandle) {
            this.scrollHandle = scrollHandle;
            return this;
        }

        public Configurator enableAntialiasing(boolean antialiasing) {
            this.antialiasing = antialiasing;
            return this;
        }

        public Configurator spacing(int spacing) {
            this.spacing = spacing;
            return this;
        }

        public Configurator autoSpacing(boolean autoSpacing) {
            this.autoSpacing = autoSpacing;
            return this;
        }

        public Configurator pageFitPolicy(FitPolicy pageFitPolicy) {
            this.pageFitPolicy = pageFitPolicy;
            return this;
        }

        public Configurator fitEachPage(boolean fitEachPage) {
            this.fitEachPage = fitEachPage;
            return this;
        }

        public Configurator pageSnap(boolean pageSnap) {
            this.pageSnap = pageSnap;
            return this;
        }

        public Configurator pageFling(boolean pageFling) {
            this.pageFling = pageFling;
            return this;
        }

        public Configurator nightMode(boolean nightMode) {
            this.nightMode = nightMode;
            return this;
        }

        public Configurator disableLongpress() {
            PDFView.this.dragPinchManager.disableLongpress();
            return this;
        }

        public void load() {
            if (!PDFView.this.hasSize) {
                PDFView.this.waitingDocumentConfigurator = this;
                return;
            }
            PDFView.this.recycle();
            PDFView.this.callbacks.setOnLoadComplete(this.onLoadCompleteListener);
            PDFView.this.callbacks.setOnError(this.onErrorListener);
            PDFView.this.callbacks.setOnDraw(this.onDrawListener);
            PDFView.this.callbacks.setOnDrawAll(this.onDrawAllListener);
            PDFView.this.callbacks.setOnPageChange(this.onPageChangeListener);
            PDFView.this.callbacks.setOnPageScroll(this.onPageScrollListener);
            PDFView.this.callbacks.setOnRender(this.onRenderListener);
            PDFView.this.callbacks.setOnTap(this.onTapListener);
            PDFView.this.callbacks.setOnLongPress(this.onLongPressListener);
            PDFView.this.callbacks.setOnPageError(this.onPageErrorListener);
            PDFView.this.callbacks.setLinkHandler(this.linkHandler);
            PDFView.this.setSwipeEnabled(this.enableSwipe);
            PDFView.this.setNightMode(this.nightMode);
            PDFView.this.enableDoubletap(this.enableDoubletap);
            PDFView.this.setDefaultPage(this.defaultPage);
            PDFView.this.setSwipeVertical(!this.swipeHorizontal);
            PDFView.this.enableAnnotationRendering(this.annotationRendering);
            PDFView.this.setScrollHandle(this.scrollHandle);
            PDFView.this.enableAntialiasing(this.antialiasing);
            PDFView.this.setSpacing(this.spacing);
            PDFView.this.setAutoSpacing(this.autoSpacing);
            PDFView.this.setPageFitPolicy(this.pageFitPolicy);
            PDFView.this.setFitEachPage(this.fitEachPage);
            PDFView.this.setPageSnap(this.pageSnap);
            PDFView.this.setPageFling(this.pageFling);
            int[] iArr = this.pageNumbers;
            if (iArr != null) {
                PDFView.this.load(this.documentSource, this.password, iArr);
            } else {
                PDFView.this.load(this.documentSource, this.password);
            }
        }
    }
}
