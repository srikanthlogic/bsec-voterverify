package com.github.barteksc.pdfviewer.model;

import android.graphics.Bitmap;
import android.graphics.RectF;
/* loaded from: classes.dex */
public class PagePart {
    private int cacheOrder;
    private int page;
    private RectF pageRelativeBounds;
    private Bitmap renderedBitmap;
    private boolean thumbnail;

    public PagePart(int page, Bitmap renderedBitmap, RectF pageRelativeBounds, boolean thumbnail, int cacheOrder) {
        this.page = page;
        this.renderedBitmap = renderedBitmap;
        this.pageRelativeBounds = pageRelativeBounds;
        this.thumbnail = thumbnail;
        this.cacheOrder = cacheOrder;
    }

    public int getCacheOrder() {
        return this.cacheOrder;
    }

    public int getPage() {
        return this.page;
    }

    public Bitmap getRenderedBitmap() {
        return this.renderedBitmap;
    }

    public RectF getPageRelativeBounds() {
        return this.pageRelativeBounds;
    }

    public boolean isThumbnail() {
        return this.thumbnail;
    }

    public void setCacheOrder(int cacheOrder) {
        this.cacheOrder = cacheOrder;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PagePart)) {
            return false;
        }
        PagePart part = (PagePart) obj;
        if (part.getPage() == this.page && part.getPageRelativeBounds().left == this.pageRelativeBounds.left && part.getPageRelativeBounds().right == this.pageRelativeBounds.right && part.getPageRelativeBounds().top == this.pageRelativeBounds.top && part.getPageRelativeBounds().bottom == this.pageRelativeBounds.bottom) {
            return true;
        }
        return false;
    }
}
