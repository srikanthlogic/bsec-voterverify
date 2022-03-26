package com.shockwave.pdfium;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.Surface;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.util.Size;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes3.dex */
public class PdfiumCore {
    private static final String FD_FIELD_NAME = "descriptor";
    private static final Object lock;
    private static Field mFdField;
    private int mCurrentDpi;
    private static final String TAG = PdfiumCore.class.getName();
    private static final Class FD_CLASS = FileDescriptor.class;

    private native void nativeCloseDocument(long j);

    private native void nativeClosePage(long j);

    private native void nativeClosePages(long[] jArr);

    private native long nativeGetBookmarkDestIndex(long j, long j2);

    private native String nativeGetBookmarkTitle(long j);

    private native Integer nativeGetDestPageIndex(long j, long j2);

    private native String nativeGetDocumentMetaText(long j, String str);

    private native Long nativeGetFirstChildBookmark(long j, Long l);

    private native RectF nativeGetLinkRect(long j);

    private native String nativeGetLinkURI(long j, long j2);

    private native int nativeGetPageCount(long j);

    private native int nativeGetPageHeightPixel(long j, int i);

    private native int nativeGetPageHeightPoint(long j);

    private native long[] nativeGetPageLinks(long j);

    private native Size nativeGetPageSizeByIndex(long j, int i, int i2);

    private native int nativeGetPageWidthPixel(long j, int i);

    private native int nativeGetPageWidthPoint(long j);

    private native Long nativeGetSiblingBookmark(long j, long j2);

    private native long nativeLoadPage(long j, int i);

    private native long[] nativeLoadPages(long j, int i, int i2);

    private native long nativeOpenDocument(int i, String str);

    private native long nativeOpenMemDocument(byte[] bArr, String str);

    private native Point nativePageCoordsToDevice(long j, int i, int i2, int i3, int i4, int i5, double d, double d2);

    private native void nativeRenderPage(long j, Surface surface, int i, int i2, int i3, int i4, int i5, boolean z);

    private native void nativeRenderPageBitmap(long j, Bitmap bitmap, int i, int i2, int i3, int i4, int i5, boolean z);

    static {
        try {
            System.loadLibrary("c++_shared");
            System.loadLibrary("modpng");
            System.loadLibrary("modft2");
            System.loadLibrary("modpdfium");
            System.loadLibrary("jniPdfium");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Native libraries failed to load - " + e);
        }
        lock = new Object();
        mFdField = null;
    }

    public static int getNumFd(ParcelFileDescriptor fdObj) {
        try {
            if (mFdField == null) {
                mFdField = FD_CLASS.getDeclaredField(FD_FIELD_NAME);
                mFdField.setAccessible(true);
            }
            return mFdField.getInt(fdObj.getFileDescriptor());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
            return -1;
        }
    }

    public PdfiumCore(Context ctx) {
        this.mCurrentDpi = ctx.getResources().getDisplayMetrics().densityDpi;
        Log.d(TAG, "Starting PdfiumAndroid 1.9.0");
    }

    public PdfDocument newDocument(ParcelFileDescriptor fd) throws IOException {
        return newDocument(fd, (String) null);
    }

    public PdfDocument newDocument(ParcelFileDescriptor fd, String password) throws IOException {
        PdfDocument document = new PdfDocument();
        document.parcelFileDescriptor = fd;
        synchronized (lock) {
            document.mNativeDocPtr = nativeOpenDocument(getNumFd(fd), password);
        }
        return document;
    }

    public PdfDocument newDocument(byte[] data) throws IOException {
        return newDocument(data, (String) null);
    }

    public PdfDocument newDocument(byte[] data, String password) throws IOException {
        PdfDocument document = new PdfDocument();
        synchronized (lock) {
            document.mNativeDocPtr = nativeOpenMemDocument(data, password);
        }
        return document;
    }

    public int getPageCount(PdfDocument doc) {
        int nativeGetPageCount;
        synchronized (lock) {
            nativeGetPageCount = nativeGetPageCount(doc.mNativeDocPtr);
        }
        return nativeGetPageCount;
    }

    public long openPage(PdfDocument doc, int pageIndex) {
        long pagePtr;
        synchronized (lock) {
            pagePtr = nativeLoadPage(doc.mNativeDocPtr, pageIndex);
            doc.mNativePagesPtr.put(Integer.valueOf(pageIndex), Long.valueOf(pagePtr));
        }
        return pagePtr;
    }

    public long[] openPage(PdfDocument doc, int fromIndex, int toIndex) {
        long[] pagesPtr;
        synchronized (lock) {
            pagesPtr = nativeLoadPages(doc.mNativeDocPtr, fromIndex, toIndex);
            int pageIndex = fromIndex;
            for (long page : pagesPtr) {
                if (pageIndex > toIndex) {
                    break;
                }
                doc.mNativePagesPtr.put(Integer.valueOf(pageIndex), Long.valueOf(page));
                pageIndex++;
            }
        }
        return pagesPtr;
    }

    public int getPageWidth(PdfDocument doc, int index) {
        synchronized (lock) {
            Long pagePtr = doc.mNativePagesPtr.get(Integer.valueOf(index));
            if (pagePtr == null) {
                return 0;
            }
            return nativeGetPageWidthPixel(pagePtr.longValue(), this.mCurrentDpi);
        }
    }

    public int getPageHeight(PdfDocument doc, int index) {
        synchronized (lock) {
            Long pagePtr = doc.mNativePagesPtr.get(Integer.valueOf(index));
            if (pagePtr == null) {
                return 0;
            }
            return nativeGetPageHeightPixel(pagePtr.longValue(), this.mCurrentDpi);
        }
    }

    public int getPageWidthPoint(PdfDocument doc, int index) {
        synchronized (lock) {
            Long pagePtr = doc.mNativePagesPtr.get(Integer.valueOf(index));
            if (pagePtr == null) {
                return 0;
            }
            return nativeGetPageWidthPoint(pagePtr.longValue());
        }
    }

    public int getPageHeightPoint(PdfDocument doc, int index) {
        synchronized (lock) {
            Long pagePtr = doc.mNativePagesPtr.get(Integer.valueOf(index));
            if (pagePtr == null) {
                return 0;
            }
            return nativeGetPageHeightPoint(pagePtr.longValue());
        }
    }

    public Size getPageSize(PdfDocument doc, int index) {
        Size nativeGetPageSizeByIndex;
        synchronized (lock) {
            nativeGetPageSizeByIndex = nativeGetPageSizeByIndex(doc.mNativeDocPtr, index, this.mCurrentDpi);
        }
        return nativeGetPageSizeByIndex;
    }

    public void renderPage(PdfDocument doc, Surface surface, int pageIndex, int startX, int startY, int drawSizeX, int drawSizeY) {
        renderPage(doc, surface, pageIndex, startX, startY, drawSizeX, drawSizeY, false);
    }

    public void renderPage(PdfDocument doc, Surface surface, int pageIndex, int startX, int startY, int drawSizeX, int drawSizeY, boolean renderAnnot) {
        Throwable th;
        NullPointerException e;
        Exception e2;
        synchronized (lock) {
            try {
            } catch (Throwable th2) {
                th = th2;
            }
            try {
                try {
                    nativeRenderPage(doc.mNativePagesPtr.get(Integer.valueOf(pageIndex)).longValue(), surface, this.mCurrentDpi, startX, startY, drawSizeX, drawSizeY, renderAnnot);
                } catch (NullPointerException e3) {
                    e = e3;
                    Log.e(TAG, "mContext may be null");
                    e.printStackTrace();
                } catch (Exception e4) {
                    e2 = e4;
                    Log.e(TAG, "Exception throw from native");
                    e2.printStackTrace();
                }
            } catch (NullPointerException e5) {
                e = e5;
            } catch (Exception e6) {
                e2 = e6;
            } catch (Throwable th3) {
                th = th3;
                throw th;
            }
        }
    }

    public void renderPageBitmap(PdfDocument doc, Bitmap bitmap, int pageIndex, int startX, int startY, int drawSizeX, int drawSizeY) {
        renderPageBitmap(doc, bitmap, pageIndex, startX, startY, drawSizeX, drawSizeY, false);
    }

    public void renderPageBitmap(PdfDocument doc, Bitmap bitmap, int pageIndex, int startX, int startY, int drawSizeX, int drawSizeY, boolean renderAnnot) {
        Throwable th;
        NullPointerException e;
        Exception e2;
        synchronized (lock) {
            try {
            } catch (Throwable th2) {
                th = th2;
            }
            try {
                try {
                    nativeRenderPageBitmap(doc.mNativePagesPtr.get(Integer.valueOf(pageIndex)).longValue(), bitmap, this.mCurrentDpi, startX, startY, drawSizeX, drawSizeY, renderAnnot);
                } catch (NullPointerException e3) {
                    e = e3;
                    Log.e(TAG, "mContext may be null");
                    e.printStackTrace();
                } catch (Exception e4) {
                    e2 = e4;
                    Log.e(TAG, "Exception throw from native");
                    e2.printStackTrace();
                }
            } catch (NullPointerException e5) {
                e = e5;
            } catch (Exception e6) {
                e2 = e6;
            } catch (Throwable th3) {
                th = th3;
                throw th;
            }
        }
    }

    public void closeDocument(PdfDocument doc) {
        synchronized (lock) {
            for (Integer index : doc.mNativePagesPtr.keySet()) {
                nativeClosePage(doc.mNativePagesPtr.get(index).longValue());
            }
            doc.mNativePagesPtr.clear();
            nativeCloseDocument(doc.mNativeDocPtr);
            if (doc.parcelFileDescriptor != null) {
                try {
                    doc.parcelFileDescriptor.close();
                } catch (IOException e) {
                }
                doc.parcelFileDescriptor = null;
            }
        }
    }

    public PdfDocument.Meta getDocumentMeta(PdfDocument doc) {
        PdfDocument.Meta meta;
        synchronized (lock) {
            meta = new PdfDocument.Meta();
            meta.title = nativeGetDocumentMetaText(doc.mNativeDocPtr, "Title");
            meta.author = nativeGetDocumentMetaText(doc.mNativeDocPtr, "Author");
            meta.subject = nativeGetDocumentMetaText(doc.mNativeDocPtr, "Subject");
            meta.keywords = nativeGetDocumentMetaText(doc.mNativeDocPtr, "Keywords");
            meta.creator = nativeGetDocumentMetaText(doc.mNativeDocPtr, "Creator");
            meta.producer = nativeGetDocumentMetaText(doc.mNativeDocPtr, "Producer");
            meta.creationDate = nativeGetDocumentMetaText(doc.mNativeDocPtr, "CreationDate");
            meta.modDate = nativeGetDocumentMetaText(doc.mNativeDocPtr, "ModDate");
        }
        return meta;
    }

    public List<PdfDocument.Bookmark> getTableOfContents(PdfDocument doc) {
        List<PdfDocument.Bookmark> topLevel;
        synchronized (lock) {
            topLevel = new ArrayList<>();
            Long first = nativeGetFirstChildBookmark(doc.mNativeDocPtr, null);
            if (first != null) {
                recursiveGetBookmark(topLevel, doc, first.longValue());
            }
        }
        return topLevel;
    }

    private void recursiveGetBookmark(List<PdfDocument.Bookmark> tree, PdfDocument doc, long bookmarkPtr) {
        PdfDocument.Bookmark bookmark = new PdfDocument.Bookmark();
        bookmark.mNativePtr = bookmarkPtr;
        bookmark.title = nativeGetBookmarkTitle(bookmarkPtr);
        bookmark.pageIdx = nativeGetBookmarkDestIndex(doc.mNativeDocPtr, bookmarkPtr);
        tree.add(bookmark);
        Long child = nativeGetFirstChildBookmark(doc.mNativeDocPtr, Long.valueOf(bookmarkPtr));
        if (child != null) {
            recursiveGetBookmark(bookmark.getChildren(), doc, child.longValue());
        }
        Long sibling = nativeGetSiblingBookmark(doc.mNativeDocPtr, bookmarkPtr);
        if (sibling != null) {
            recursiveGetBookmark(tree, doc, sibling.longValue());
        }
    }

    public List<PdfDocument.Link> getPageLinks(PdfDocument doc, int pageIndex) {
        synchronized (lock) {
            List<PdfDocument.Link> links = new ArrayList<>();
            Long nativePagePtr = doc.mNativePagesPtr.get(Integer.valueOf(pageIndex));
            if (nativePagePtr == null) {
                return links;
            }
            long[] linkPtrs = nativeGetPageLinks(nativePagePtr.longValue());
            for (long linkPtr : linkPtrs) {
                Integer index = nativeGetDestPageIndex(doc.mNativeDocPtr, linkPtr);
                String uri = nativeGetLinkURI(doc.mNativeDocPtr, linkPtr);
                RectF rect = nativeGetLinkRect(linkPtr);
                if (!(rect == null || (index == null && uri == null))) {
                    links.add(new PdfDocument.Link(rect, index, uri));
                }
            }
            return links;
        }
    }

    public Point mapPageCoordsToDevice(PdfDocument doc, int pageIndex, int startX, int startY, int sizeX, int sizeY, int rotate, double pageX, double pageY) {
        return nativePageCoordsToDevice(doc.mNativePagesPtr.get(Integer.valueOf(pageIndex)).longValue(), startX, startY, sizeX, sizeY, rotate, pageX, pageY);
    }

    public RectF mapRectToDevice(PdfDocument doc, int pageIndex, int startX, int startY, int sizeX, int sizeY, int rotate, RectF coords) {
        Point leftTop = mapPageCoordsToDevice(doc, pageIndex, startX, startY, sizeX, sizeY, rotate, (double) coords.left, (double) coords.top);
        Point rightBottom = mapPageCoordsToDevice(doc, pageIndex, startX, startY, sizeX, sizeY, rotate, (double) coords.right, (double) coords.bottom);
        return new RectF((float) leftTop.x, (float) leftTop.y, (float) rightBottom.x, (float) rightBottom.y);
    }
}
