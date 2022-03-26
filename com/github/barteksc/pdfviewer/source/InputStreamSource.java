package com.github.barteksc.pdfviewer.source;

import android.content.Context;
import com.github.barteksc.pdfviewer.util.Util;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public class InputStreamSource implements DocumentSource {
    private InputStream inputStream;

    public InputStreamSource(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override // com.github.barteksc.pdfviewer.source.DocumentSource
    public PdfDocument createDocument(Context context, PdfiumCore core, String password) throws IOException {
        return core.newDocument(Util.toByteArray(this.inputStream), password);
    }
}
