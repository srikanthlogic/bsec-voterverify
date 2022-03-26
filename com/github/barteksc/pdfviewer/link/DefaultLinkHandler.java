package com.github.barteksc.pdfviewer.link;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.model.LinkTapEvent;
/* loaded from: classes.dex */
public class DefaultLinkHandler implements LinkHandler {
    private static final String TAG = DefaultLinkHandler.class.getSimpleName();
    private PDFView pdfView;

    public DefaultLinkHandler(PDFView pdfView) {
        this.pdfView = pdfView;
    }

    @Override // com.github.barteksc.pdfviewer.link.LinkHandler
    public void handleLinkEvent(LinkTapEvent event) {
        String uri = event.getLink().getUri();
        Integer page = event.getLink().getDestPageIdx();
        if (uri != null && !uri.isEmpty()) {
            handleUri(uri);
        } else if (page != null) {
            handlePage(page.intValue());
        }
    }

    private void handleUri(String uri) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(uri));
        Context context = this.pdfView.getContext();
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
            return;
        }
        String str = TAG;
        Log.w(str, "No activity found for URI: " + uri);
    }

    private void handlePage(int page) {
        this.pdfView.jumpTo(page);
    }
}
