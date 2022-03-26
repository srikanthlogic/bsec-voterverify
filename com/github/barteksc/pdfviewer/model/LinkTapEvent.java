package com.github.barteksc.pdfviewer.model;

import android.graphics.RectF;
import com.shockwave.pdfium.PdfDocument;
/* loaded from: classes.dex */
public class LinkTapEvent {
    private float documentX;
    private float documentY;
    private PdfDocument.Link link;
    private RectF mappedLinkRect;
    private float originalX;
    private float originalY;

    public LinkTapEvent(float originalX, float originalY, float documentX, float documentY, RectF mappedLinkRect, PdfDocument.Link link) {
        this.originalX = originalX;
        this.originalY = originalY;
        this.documentX = documentX;
        this.documentY = documentY;
        this.mappedLinkRect = mappedLinkRect;
        this.link = link;
    }

    public float getOriginalX() {
        return this.originalX;
    }

    public float getOriginalY() {
        return this.originalY;
    }

    public float getDocumentX() {
        return this.documentX;
    }

    public float getDocumentY() {
        return this.documentY;
    }

    public RectF getMappedLinkRect() {
        return this.mappedLinkRect;
    }

    public PdfDocument.Link getLink() {
        return this.link;
    }
}
