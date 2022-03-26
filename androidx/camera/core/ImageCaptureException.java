package androidx.camera.core;
/* loaded from: classes.dex */
public class ImageCaptureException extends Exception {
    private final int mImageCaptureError;

    public ImageCaptureException(int imageCaptureError, String message, Throwable cause) {
        super(message, cause);
        this.mImageCaptureError = imageCaptureError;
    }

    public int getImageCaptureError() {
        return this.mImageCaptureError;
    }
}
