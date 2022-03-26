package com.github.barteksc.pdfviewer;

import android.os.AsyncTask;
import com.github.barteksc.pdfviewer.source.DocumentSource;
import com.shockwave.pdfium.PdfiumCore;
import com.shockwave.pdfium.util.Size;
import java.lang.ref.WeakReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class DecodingAsyncTask extends AsyncTask<Void, Void, Throwable> {
    private boolean cancelled = false;
    private DocumentSource docSource;
    private String password;
    private PdfFile pdfFile;
    private WeakReference<PDFView> pdfViewReference;
    private PdfiumCore pdfiumCore;
    private int[] userPages;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DecodingAsyncTask(DocumentSource docSource, String password, int[] userPages, PDFView pdfView, PdfiumCore pdfiumCore) {
        this.docSource = docSource;
        this.userPages = userPages;
        this.pdfViewReference = new WeakReference<>(pdfView);
        this.password = password;
        this.pdfiumCore = pdfiumCore;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Throwable doInBackground(Void... params) {
        try {
            PDFView pdfView = this.pdfViewReference.get();
            if (pdfView == null) {
                return new NullPointerException("pdfView == null");
            }
            this.pdfFile = new PdfFile(this.pdfiumCore, this.docSource.createDocument(pdfView.getContext(), this.pdfiumCore, this.password), pdfView.getPageFitPolicy(), getViewSize(pdfView), this.userPages, pdfView.isSwipeVertical(), pdfView.getSpacingPx(), pdfView.isAutoSpacingEnabled(), pdfView.isFitEachPage());
            return null;
        } catch (Throwable t) {
            return t;
        }
    }

    private Size getViewSize(PDFView pdfView) {
        return new Size(pdfView.getWidth(), pdfView.getHeight());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onPostExecute(Throwable t) {
        PDFView pdfView = this.pdfViewReference.get();
        if (pdfView == null) {
            return;
        }
        if (t != null) {
            pdfView.loadError(t);
        } else if (!this.cancelled) {
            pdfView.loadComplete(this.pdfFile);
        }
    }

    @Override // android.os.AsyncTask
    protected void onCancelled() {
        this.cancelled = true;
    }
}
