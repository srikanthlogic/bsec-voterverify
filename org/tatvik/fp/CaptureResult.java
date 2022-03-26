package org.tatvik.fp;
/* loaded from: classes3.dex */
public class CaptureResult {
    String errorString;
    byte[] fmrBytes;
    int imageHeight;
    int imageWidth;
    int minutiaeCount;
    int nfiq;
    byte[] rawImageBytes;
    int statusCode;

    public byte[] getFmrBytes() {
        return this.fmrBytes;
    }

    public byte[] getRawImageBytes() {
        return this.rawImageBytes;
    }

    public int getImageWidth() {
        return this.imageWidth;
    }

    public int getImageHeight() {
        return this.imageHeight;
    }

    public int getNfiq() {
        return this.nfiq;
    }

    public int getMinutiaeCount() {
        return this.minutiaeCount;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getErrorString() {
        return this.errorString;
    }
}
