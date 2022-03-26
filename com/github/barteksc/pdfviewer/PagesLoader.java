package com.github.barteksc.pdfviewer;

import android.graphics.RectF;
import com.github.barteksc.pdfviewer.util.Constants;
import com.github.barteksc.pdfviewer.util.MathUtils;
import com.github.barteksc.pdfviewer.util.Util;
import com.shockwave.pdfium.util.SizeF;
import java.util.LinkedList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class PagesLoader {
    private int cacheOrder;
    private float pageRelativePartHeight;
    private float pageRelativePartWidth;
    private float partRenderHeight;
    private float partRenderWidth;
    private PDFView pdfView;
    private final int preloadOffset;
    private final RectF thumbnailRect = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
    private float xOffset;
    private float yOffset;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class Holder {
        int col;
        int row;

        private Holder() {
        }

        public String toString() {
            return "Holder{row=" + this.row + ", col=" + this.col + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class RenderRange {
        GridSize gridSize;
        Holder leftTop;
        int page = 0;
        Holder rightBottom;

        RenderRange() {
            this.gridSize = new GridSize();
            this.leftTop = new Holder();
            this.rightBottom = new Holder();
        }

        public String toString() {
            return "RenderRange{page=" + this.page + ", gridSize=" + this.gridSize + ", leftTop=" + this.leftTop + ", rightBottom=" + this.rightBottom + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class GridSize {
        int cols;
        int rows;

        private GridSize() {
        }

        public String toString() {
            return "GridSize{rows=" + this.rows + ", cols=" + this.cols + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PagesLoader(PDFView pdfView) {
        this.pdfView = pdfView;
        this.preloadOffset = Util.getDP(pdfView.getContext(), Constants.PRELOAD_OFFSET);
    }

    private void getPageColsRows(GridSize grid, int pageIndex) {
        SizeF size = this.pdfView.pdfFile.getPageSize(pageIndex);
        float partHeight = (Constants.PART_SIZE * (1.0f / size.getHeight())) / this.pdfView.getZoom();
        float partWidth = (Constants.PART_SIZE * (1.0f / size.getWidth())) / this.pdfView.getZoom();
        grid.rows = MathUtils.ceil(1.0f / partHeight);
        grid.cols = MathUtils.ceil(1.0f / partWidth);
    }

    private void calculatePartSize(GridSize grid) {
        this.pageRelativePartWidth = 1.0f / ((float) grid.cols);
        this.pageRelativePartHeight = 1.0f / ((float) grid.rows);
        this.partRenderWidth = Constants.PART_SIZE / this.pageRelativePartWidth;
        this.partRenderHeight = Constants.PART_SIZE / this.pageRelativePartHeight;
    }

    private List<RenderRange> getRenderRangeList(float firstXOffset, float firstYOffset, float lastXOffset, float lastYOffset) {
        float pageLastYOffset;
        float pageFirstYOffset;
        float pageFirstXOffset;
        float pageLastXOffset;
        float fixedLastYOffset;
        float fixedLastXOffset;
        float pageOffset;
        float fixedFirstXOffset = -MathUtils.max(firstXOffset, 0.0f);
        float fixedFirstYOffset = -MathUtils.max(firstYOffset, 0.0f);
        float fixedLastXOffset2 = -MathUtils.max(lastXOffset, 0.0f);
        float fixedLastYOffset2 = -MathUtils.max(lastYOffset, 0.0f);
        float offsetFirst = this.pdfView.isSwipeVertical() ? fixedFirstYOffset : fixedFirstXOffset;
        float offsetLast = this.pdfView.isSwipeVertical() ? fixedLastYOffset2 : fixedLastXOffset2;
        int firstPage = this.pdfView.pdfFile.getPageAtOffset(offsetFirst, this.pdfView.getZoom());
        int lastPage = this.pdfView.pdfFile.getPageAtOffset(offsetLast, this.pdfView.getZoom());
        int pageCount = (lastPage - firstPage) + 1;
        List<RenderRange> renderRanges = new LinkedList<>();
        int page = firstPage;
        while (page <= lastPage) {
            RenderRange range = new RenderRange();
            range.page = page;
            if (page == firstPage) {
                pageFirstXOffset = fixedFirstXOffset;
                pageFirstYOffset = fixedFirstYOffset;
                if (pageCount == 1) {
                    pageLastYOffset = fixedLastYOffset2;
                    pageLastXOffset = fixedLastXOffset2;
                } else {
                    float pageOffset2 = this.pdfView.pdfFile.getPageOffset(page, this.pdfView.getZoom());
                    SizeF pageSize = this.pdfView.pdfFile.getScaledPageSize(page, this.pdfView.getZoom());
                    if (this.pdfView.isSwipeVertical()) {
                        pageLastYOffset = pageOffset2 + pageSize.getHeight();
                        pageOffset = fixedLastXOffset2;
                    } else {
                        pageOffset = pageOffset2 + pageSize.getWidth();
                        pageLastYOffset = fixedLastYOffset2;
                    }
                    pageLastXOffset = pageOffset;
                }
            } else if (page == lastPage) {
                float pageOffset3 = this.pdfView.pdfFile.getPageOffset(page, this.pdfView.getZoom());
                if (this.pdfView.isSwipeVertical()) {
                    pageFirstXOffset = fixedFirstXOffset;
                    pageFirstYOffset = pageOffset3;
                } else {
                    pageFirstYOffset = fixedFirstYOffset;
                    pageFirstXOffset = pageOffset3;
                }
                pageLastYOffset = fixedLastYOffset2;
                pageLastXOffset = fixedLastXOffset2;
            } else {
                float pageOffset4 = this.pdfView.pdfFile.getPageOffset(page, this.pdfView.getZoom());
                SizeF pageSize2 = this.pdfView.pdfFile.getScaledPageSize(page, this.pdfView.getZoom());
                if (this.pdfView.isSwipeVertical()) {
                    pageFirstXOffset = fixedFirstXOffset;
                    pageFirstYOffset = pageOffset4;
                    pageLastXOffset = fixedLastXOffset2;
                    pageLastYOffset = pageOffset4 + pageSize2.getHeight();
                } else {
                    pageFirstXOffset = pageOffset4;
                    pageFirstYOffset = fixedFirstYOffset;
                    pageLastXOffset = pageSize2.getWidth() + pageOffset4;
                    pageLastYOffset = fixedLastYOffset2;
                }
            }
            getPageColsRows(range.gridSize, range.page);
            SizeF scaledPageSize = this.pdfView.pdfFile.getScaledPageSize(range.page, this.pdfView.getZoom());
            float rowHeight = scaledPageSize.getHeight() / ((float) range.gridSize.rows);
            float colWidth = scaledPageSize.getWidth() / ((float) range.gridSize.cols);
            float secondaryOffset = this.pdfView.pdfFile.getSecondaryPageOffset(page, this.pdfView.getZoom());
            if (this.pdfView.isSwipeVertical()) {
                fixedLastXOffset = fixedLastXOffset2;
                fixedLastYOffset = fixedLastYOffset2;
                range.leftTop.row = MathUtils.floor(Math.abs(pageFirstYOffset - this.pdfView.pdfFile.getPageOffset(range.page, this.pdfView.getZoom())) / rowHeight);
                range.leftTop.col = MathUtils.floor(MathUtils.min(pageFirstXOffset - secondaryOffset, 0.0f) / colWidth);
                range.rightBottom.row = MathUtils.ceil(Math.abs(pageLastYOffset - this.pdfView.pdfFile.getPageOffset(range.page, this.pdfView.getZoom())) / rowHeight);
                range.rightBottom.col = MathUtils.floor(MathUtils.min(pageLastXOffset - secondaryOffset, 0.0f) / colWidth);
            } else {
                fixedLastXOffset = fixedLastXOffset2;
                fixedLastYOffset = fixedLastYOffset2;
                range.leftTop.col = MathUtils.floor(Math.abs(pageFirstXOffset - this.pdfView.pdfFile.getPageOffset(range.page, this.pdfView.getZoom())) / colWidth);
                range.leftTop.row = MathUtils.floor(MathUtils.min(pageFirstYOffset - secondaryOffset, 0.0f) / rowHeight);
                range.rightBottom.col = MathUtils.floor(Math.abs(pageLastXOffset - this.pdfView.pdfFile.getPageOffset(range.page, this.pdfView.getZoom())) / colWidth);
                range.rightBottom.row = MathUtils.floor(MathUtils.min(pageLastYOffset - secondaryOffset, 0.0f) / rowHeight);
            }
            renderRanges.add(range);
            page++;
            fixedFirstXOffset = fixedFirstXOffset;
            fixedFirstYOffset = fixedFirstYOffset;
            fixedLastXOffset2 = fixedLastXOffset;
            fixedLastYOffset2 = fixedLastYOffset;
        }
        return renderRanges;
    }

    private void loadVisible() {
        float scaledPreloadOffset = (float) this.preloadOffset;
        float f = this.xOffset;
        float firstXOffset = (-f) + scaledPreloadOffset;
        float lastXOffset = ((-f) - ((float) this.pdfView.getWidth())) - scaledPreloadOffset;
        float f2 = this.yOffset;
        List<RenderRange> rangeList = getRenderRangeList(firstXOffset, (-f2) + scaledPreloadOffset, lastXOffset, ((-f2) - ((float) this.pdfView.getHeight())) - scaledPreloadOffset);
        for (RenderRange range : rangeList) {
            loadThumbnail(range.page);
        }
        int parts = 0;
        for (RenderRange range2 : rangeList) {
            calculatePartSize(range2.gridSize);
            parts += loadPage(range2.page, range2.leftTop.row, range2.rightBottom.row, range2.leftTop.col, range2.rightBottom.col, Constants.Cache.CACHE_SIZE - parts);
            if (parts >= Constants.Cache.CACHE_SIZE) {
                return;
            }
        }
    }

    private int loadPage(int page, int firstRow, int lastRow, int firstCol, int lastCol, int nbOfPartsLoadable) {
        int loaded = 0;
        int row = firstRow;
        while (row <= lastRow) {
            int loaded2 = loaded;
            for (int col = firstCol; col <= lastCol; col++) {
                if (loadCell(page, row, col, this.pageRelativePartWidth, this.pageRelativePartHeight)) {
                    loaded2++;
                }
                if (loaded2 >= nbOfPartsLoadable) {
                    return loaded2;
                }
            }
            row++;
            loaded = loaded2;
        }
        return loaded;
    }

    private boolean loadCell(int page, int row, int col, float pageRelativePartWidth, float pageRelativePartHeight) {
        float relX = ((float) col) * pageRelativePartWidth;
        float relY = ((float) row) * pageRelativePartHeight;
        float relWidth = pageRelativePartWidth;
        float relHeight = pageRelativePartHeight;
        float renderWidth = this.partRenderWidth;
        float renderHeight = this.partRenderHeight;
        if (relX + relWidth > 1.0f) {
            relWidth = 1.0f - relX;
        }
        if (relY + relHeight > 1.0f) {
            relHeight = 1.0f - relY;
        }
        float renderWidth2 = renderWidth * relWidth;
        float renderHeight2 = renderHeight * relHeight;
        RectF pageRelativeBounds = new RectF(relX, relY, relX + relWidth, relY + relHeight);
        if (renderWidth2 <= 0.0f || renderHeight2 <= 0.0f) {
            return false;
        }
        if (!this.pdfView.cacheManager.upPartIfContained(page, pageRelativeBounds, this.cacheOrder)) {
            this.pdfView.renderingHandler.addRenderingTask(page, renderWidth2, renderHeight2, pageRelativeBounds, false, this.cacheOrder, this.pdfView.isBestQuality(), this.pdfView.isAnnotationRendering());
        }
        this.cacheOrder++;
        return true;
    }

    private void loadThumbnail(int page) {
        SizeF pageSize = this.pdfView.pdfFile.getPageSize(page);
        float thumbnailWidth = pageSize.getWidth() * Constants.THUMBNAIL_RATIO;
        float thumbnailHeight = pageSize.getHeight() * Constants.THUMBNAIL_RATIO;
        if (!this.pdfView.cacheManager.containsThumbnail(page, this.thumbnailRect)) {
            this.pdfView.renderingHandler.addRenderingTask(page, thumbnailWidth, thumbnailHeight, this.thumbnailRect, true, 0, this.pdfView.isBestQuality(), this.pdfView.isAnnotationRendering());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void loadPages() {
        this.cacheOrder = 1;
        this.xOffset = -MathUtils.max(this.pdfView.getCurrentXOffset(), 0.0f);
        this.yOffset = -MathUtils.max(this.pdfView.getCurrentYOffset(), 0.0f);
        loadVisible();
    }
}
