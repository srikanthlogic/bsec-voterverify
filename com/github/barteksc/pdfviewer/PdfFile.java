package com.github.barteksc.pdfviewer;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.SparseBooleanArray;
import com.github.barteksc.pdfviewer.exception.PageRenderingException;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.github.barteksc.pdfviewer.util.PageSizeCalculator;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import com.shockwave.pdfium.util.Size;
import com.shockwave.pdfium.util.SizeF;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class PdfFile {
    private static final Object lock = new Object();
    private boolean autoSpacing;
    private final boolean fitEachPage;
    private boolean isVertical;
    private int[] originalUserPages;
    private final FitPolicy pageFitPolicy;
    private PdfDocument pdfDocument;
    private PdfiumCore pdfiumCore;
    private int spacingPx;
    private int pagesCount = 0;
    private List<Size> originalPageSizes = new ArrayList();
    private List<SizeF> pageSizes = new ArrayList();
    private SparseBooleanArray openedPages = new SparseBooleanArray();
    private Size originalMaxWidthPageSize = new Size(0, 0);
    private Size originalMaxHeightPageSize = new Size(0, 0);
    private SizeF maxHeightPageSize = new SizeF(0.0f, 0.0f);
    private SizeF maxWidthPageSize = new SizeF(0.0f, 0.0f);
    private List<Float> pageOffsets = new ArrayList();
    private List<Float> pageSpacing = new ArrayList();
    private float documentLength = 0.0f;

    public PdfFile(PdfiumCore pdfiumCore, PdfDocument pdfDocument, FitPolicy pageFitPolicy, Size viewSize, int[] originalUserPages, boolean isVertical, int spacing, boolean autoSpacing, boolean fitEachPage) {
        this.pdfiumCore = pdfiumCore;
        this.pdfDocument = pdfDocument;
        this.pageFitPolicy = pageFitPolicy;
        this.originalUserPages = originalUserPages;
        this.isVertical = isVertical;
        this.spacingPx = spacing;
        this.autoSpacing = autoSpacing;
        this.fitEachPage = fitEachPage;
        setup(viewSize);
    }

    private void setup(Size viewSize) {
        int[] iArr = this.originalUserPages;
        if (iArr != null) {
            this.pagesCount = iArr.length;
        } else {
            this.pagesCount = this.pdfiumCore.getPageCount(this.pdfDocument);
        }
        for (int i = 0; i < this.pagesCount; i++) {
            Size pageSize = this.pdfiumCore.getPageSize(this.pdfDocument, documentPage(i));
            if (pageSize.getWidth() > this.originalMaxWidthPageSize.getWidth()) {
                this.originalMaxWidthPageSize = pageSize;
            }
            if (pageSize.getHeight() > this.originalMaxHeightPageSize.getHeight()) {
                this.originalMaxHeightPageSize = pageSize;
            }
            this.originalPageSizes.add(pageSize);
        }
        recalculatePageSizes(viewSize);
    }

    public void recalculatePageSizes(Size viewSize) {
        this.pageSizes.clear();
        PageSizeCalculator calculator = new PageSizeCalculator(this.pageFitPolicy, this.originalMaxWidthPageSize, this.originalMaxHeightPageSize, viewSize, this.fitEachPage);
        this.maxWidthPageSize = calculator.getOptimalMaxWidthPageSize();
        this.maxHeightPageSize = calculator.getOptimalMaxHeightPageSize();
        for (Size size : this.originalPageSizes) {
            this.pageSizes.add(calculator.calculate(size));
        }
        if (this.autoSpacing) {
            prepareAutoSpacing(viewSize);
        }
        prepareDocLen();
        preparePagesOffset();
    }

    public int getPagesCount() {
        return this.pagesCount;
    }

    public SizeF getPageSize(int pageIndex) {
        if (documentPage(pageIndex) < 0) {
            return new SizeF(0.0f, 0.0f);
        }
        return this.pageSizes.get(pageIndex);
    }

    public SizeF getScaledPageSize(int pageIndex, float zoom) {
        SizeF size = getPageSize(pageIndex);
        return new SizeF(size.getWidth() * zoom, size.getHeight() * zoom);
    }

    public SizeF getMaxPageSize() {
        return this.isVertical ? this.maxWidthPageSize : this.maxHeightPageSize;
    }

    public float getMaxPageWidth() {
        return getMaxPageSize().getWidth();
    }

    public float getMaxPageHeight() {
        return getMaxPageSize().getHeight();
    }

    private void prepareAutoSpacing(Size viewSize) {
        float f;
        this.pageSpacing.clear();
        for (int i = 0; i < getPagesCount(); i++) {
            SizeF pageSize = this.pageSizes.get(i);
            if (this.isVertical) {
                f = ((float) viewSize.getHeight()) - pageSize.getHeight();
            } else {
                f = ((float) viewSize.getWidth()) - pageSize.getWidth();
            }
            float spacing = Math.max(0.0f, f);
            if (i < getPagesCount() - 1) {
                spacing += (float) this.spacingPx;
            }
            this.pageSpacing.add(Float.valueOf(spacing));
        }
    }

    private void prepareDocLen() {
        float length = 0.0f;
        for (int i = 0; i < getPagesCount(); i++) {
            SizeF pageSize = this.pageSizes.get(i);
            length += this.isVertical ? pageSize.getHeight() : pageSize.getWidth();
            if (this.autoSpacing) {
                length += this.pageSpacing.get(i).floatValue();
            } else if (i < getPagesCount() - 1) {
                length += (float) this.spacingPx;
            }
        }
        this.documentLength = length;
    }

    private void preparePagesOffset() {
        float f;
        this.pageOffsets.clear();
        float offset = 0.0f;
        for (int i = 0; i < getPagesCount(); i++) {
            SizeF pageSize = this.pageSizes.get(i);
            float size = this.isVertical ? pageSize.getHeight() : pageSize.getWidth();
            if (this.autoSpacing) {
                offset += this.pageSpacing.get(i).floatValue() / 2.0f;
                if (i == 0) {
                    offset -= ((float) this.spacingPx) / 2.0f;
                } else if (i == getPagesCount() - 1) {
                    offset += ((float) this.spacingPx) / 2.0f;
                }
                this.pageOffsets.add(Float.valueOf(offset));
                f = this.pageSpacing.get(i).floatValue() / 2.0f;
            } else {
                this.pageOffsets.add(Float.valueOf(offset));
                f = (float) this.spacingPx;
            }
            offset += f + size;
        }
    }

    public float getDocLen(float zoom) {
        return this.documentLength * zoom;
    }

    public float getPageLength(int pageIndex, float zoom) {
        SizeF size = getPageSize(pageIndex);
        return (this.isVertical ? size.getHeight() : size.getWidth()) * zoom;
    }

    public float getPageSpacing(int pageIndex, float zoom) {
        return (this.autoSpacing ? this.pageSpacing.get(pageIndex).floatValue() : (float) this.spacingPx) * zoom;
    }

    public float getPageOffset(int pageIndex, float zoom) {
        if (documentPage(pageIndex) < 0) {
            return 0.0f;
        }
        return this.pageOffsets.get(pageIndex).floatValue() * zoom;
    }

    public float getSecondaryPageOffset(int pageIndex, float zoom) {
        SizeF pageSize = getPageSize(pageIndex);
        if (this.isVertical) {
            return ((getMaxPageWidth() - pageSize.getWidth()) * zoom) / 2.0f;
        }
        return ((getMaxPageHeight() - pageSize.getHeight()) * zoom) / 2.0f;
    }

    public int getPageAtOffset(float offset, float zoom) {
        int currentPage = 0;
        int i = 0;
        while (i < getPagesCount() && (this.pageOffsets.get(i).floatValue() * zoom) - (getPageSpacing(i, zoom) / 2.0f) < offset) {
            currentPage++;
            i++;
        }
        int currentPage2 = currentPage - 1;
        if (currentPage2 >= 0) {
            return currentPage2;
        }
        return 0;
    }

    public boolean openPage(int pageIndex) throws PageRenderingException {
        int docPage = documentPage(pageIndex);
        if (docPage < 0) {
            return false;
        }
        synchronized (lock) {
            if (this.openedPages.indexOfKey(docPage) >= 0) {
                return false;
            }
            try {
                this.pdfiumCore.openPage(this.pdfDocument, docPage);
                this.openedPages.put(docPage, true);
                return true;
            } catch (Exception e) {
                this.openedPages.put(docPage, false);
                throw new PageRenderingException(pageIndex, e);
            }
        }
    }

    public boolean pageHasError(int pageIndex) {
        return !this.openedPages.get(documentPage(pageIndex), false);
    }

    public void renderPageBitmap(Bitmap bitmap, int pageIndex, Rect bounds, boolean annotationRendering) {
        this.pdfiumCore.renderPageBitmap(this.pdfDocument, bitmap, documentPage(pageIndex), bounds.left, bounds.top, bounds.width(), bounds.height(), annotationRendering);
    }

    public PdfDocument.Meta getMetaData() {
        PdfDocument pdfDocument = this.pdfDocument;
        if (pdfDocument == null) {
            return null;
        }
        return this.pdfiumCore.getDocumentMeta(pdfDocument);
    }

    public List<PdfDocument.Bookmark> getBookmarks() {
        PdfDocument pdfDocument = this.pdfDocument;
        if (pdfDocument == null) {
            return new ArrayList();
        }
        return this.pdfiumCore.getTableOfContents(pdfDocument);
    }

    public List<PdfDocument.Link> getPageLinks(int pageIndex) {
        return this.pdfiumCore.getPageLinks(this.pdfDocument, documentPage(pageIndex));
    }

    public RectF mapRectToDevice(int pageIndex, int startX, int startY, int sizeX, int sizeY, RectF rect) {
        return this.pdfiumCore.mapRectToDevice(this.pdfDocument, documentPage(pageIndex), startX, startY, sizeX, sizeY, 0, rect);
    }

    public void dispose() {
        PdfDocument pdfDocument;
        PdfiumCore pdfiumCore = this.pdfiumCore;
        if (!(pdfiumCore == null || (pdfDocument = this.pdfDocument) == null)) {
            pdfiumCore.closeDocument(pdfDocument);
        }
        this.pdfDocument = null;
        this.originalUserPages = null;
    }

    public int determineValidPageNumberFrom(int userPage) {
        if (userPage <= 0) {
            return 0;
        }
        int[] iArr = this.originalUserPages;
        if (iArr != null) {
            if (userPage >= iArr.length) {
                return iArr.length - 1;
            }
        } else if (userPage >= getPagesCount()) {
            return getPagesCount() - 1;
        }
        return userPage;
    }

    public int documentPage(int userPage) {
        int documentPage = userPage;
        int[] iArr = this.originalUserPages;
        if (iArr != null) {
            if (userPage < 0 || userPage >= iArr.length) {
                return -1;
            }
            documentPage = iArr[userPage];
        }
        if (documentPage < 0 || userPage >= getPagesCount()) {
            return -1;
        }
        return documentPage;
    }
}
