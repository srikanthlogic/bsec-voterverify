package com.github.barteksc.pdfviewer;

import android.graphics.RectF;
import com.github.barteksc.pdfviewer.model.PagePart;
import com.github.barteksc.pdfviewer.util.Constants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class CacheManager {
    private final Object passiveActiveLock = new Object();
    private final PagePartComparator orderComparator = new PagePartComparator();
    private final PriorityQueue<PagePart> activeCache = new PriorityQueue<>(Constants.Cache.CACHE_SIZE, this.orderComparator);
    private final PriorityQueue<PagePart> passiveCache = new PriorityQueue<>(Constants.Cache.CACHE_SIZE, this.orderComparator);
    private final List<PagePart> thumbnails = new ArrayList();

    public void cachePart(PagePart part) {
        synchronized (this.passiveActiveLock) {
            makeAFreeSpace();
            this.activeCache.offer(part);
        }
    }

    public void makeANewSet() {
        synchronized (this.passiveActiveLock) {
            this.passiveCache.addAll(this.activeCache);
            this.activeCache.clear();
        }
    }

    private void makeAFreeSpace() {
        synchronized (this.passiveActiveLock) {
            while (this.activeCache.size() + this.passiveCache.size() >= Constants.Cache.CACHE_SIZE && !this.passiveCache.isEmpty()) {
                this.passiveCache.poll().getRenderedBitmap().recycle();
            }
            while (this.activeCache.size() + this.passiveCache.size() >= Constants.Cache.CACHE_SIZE && !this.activeCache.isEmpty()) {
                this.activeCache.poll().getRenderedBitmap().recycle();
            }
        }
    }

    public void cacheThumbnail(PagePart part) {
        synchronized (this.thumbnails) {
            while (this.thumbnails.size() >= Constants.Cache.THUMBNAILS_CACHE_SIZE) {
                this.thumbnails.remove(0).getRenderedBitmap().recycle();
            }
            addWithoutDuplicates(this.thumbnails, part);
        }
    }

    public boolean upPartIfContained(int page, RectF pageRelativeBounds, int toOrder) {
        PagePart fakePart = new PagePart(page, null, pageRelativeBounds, false, 0);
        synchronized (this.passiveActiveLock) {
            PagePart found = find(this.passiveCache, fakePart);
            boolean z = true;
            if (found != null) {
                this.passiveCache.remove(found);
                found.setCacheOrder(toOrder);
                this.activeCache.offer(found);
                return true;
            }
            if (find(this.activeCache, fakePart) == null) {
                z = false;
            }
            return z;
        }
    }

    public boolean containsThumbnail(int page, RectF pageRelativeBounds) {
        PagePart fakePart = new PagePart(page, null, pageRelativeBounds, true, 0);
        synchronized (this.thumbnails) {
            for (PagePart part : this.thumbnails) {
                if (part.equals(fakePart)) {
                    return true;
                }
            }
            return false;
        }
    }

    private void addWithoutDuplicates(Collection<PagePart> collection, PagePart newPart) {
        for (PagePart part : collection) {
            if (part.equals(newPart)) {
                newPart.getRenderedBitmap().recycle();
                return;
            }
        }
        collection.add(newPart);
    }

    private static PagePart find(PriorityQueue<PagePart> vector, PagePart fakePart) {
        Iterator<PagePart> it = vector.iterator();
        while (it.hasNext()) {
            PagePart part = it.next();
            if (part.equals(fakePart)) {
                return part;
            }
        }
        return null;
    }

    public List<PagePart> getPageParts() {
        List<PagePart> parts;
        synchronized (this.passiveActiveLock) {
            parts = new ArrayList<>(this.passiveCache);
            parts.addAll(this.activeCache);
        }
        return parts;
    }

    public List<PagePart> getThumbnails() {
        List<PagePart> list;
        synchronized (this.thumbnails) {
            list = this.thumbnails;
        }
        return list;
    }

    public void recycle() {
        synchronized (this.passiveActiveLock) {
            Iterator<PagePart> it = this.passiveCache.iterator();
            while (it.hasNext()) {
                it.next().getRenderedBitmap().recycle();
            }
            this.passiveCache.clear();
            Iterator<PagePart> it2 = this.activeCache.iterator();
            while (it2.hasNext()) {
                it2.next().getRenderedBitmap().recycle();
            }
            this.activeCache.clear();
        }
        synchronized (this.thumbnails) {
            for (PagePart part : this.thumbnails) {
                part.getRenderedBitmap().recycle();
            }
            this.thumbnails.clear();
        }
    }

    /* loaded from: classes.dex */
    class PagePartComparator implements Comparator<PagePart> {
        PagePartComparator() {
        }

        public int compare(PagePart part1, PagePart part2) {
            if (part1.getCacheOrder() == part2.getCacheOrder()) {
                return 0;
            }
            return part1.getCacheOrder() > part2.getCacheOrder() ? 1 : -1;
        }
    }
}
