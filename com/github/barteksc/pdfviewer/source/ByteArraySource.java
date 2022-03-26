package com.github.barteksc.pdfviewer.source;

import android.content.Context;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import java.io.IOException;
/* loaded from: classes.dex */
public class ByteArraySource implements DocumentSource {
    private byte[] data;

    public ByteArraySource(byte[] data) {
        this.data = data;
    }

    @Override // com.github.barteksc.pdfviewer.source.DocumentSource
    public PdfDocument createDocument(Context context, PdfiumCore core, String password) throws IOException {
        return core.newDocument(this.data, password);
    }
}
