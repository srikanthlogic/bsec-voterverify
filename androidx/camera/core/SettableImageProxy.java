package androidx.camera.core;

import android.graphics.Rect;
import android.util.Size;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SettableImageProxy extends ForwardingImageProxy {
    private Rect mCropRect;
    private final int mHeight;
    private final ImageInfo mImageInfo;
    private final int mWidth;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SettableImageProxy(ImageProxy imageProxy, ImageInfo imageInfo) {
        this(imageProxy, null, imageInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SettableImageProxy(ImageProxy imageProxy, Size resolution, ImageInfo imageInfo) {
        super(imageProxy);
        if (resolution == null) {
            this.mWidth = super.getWidth();
            this.mHeight = super.getHeight();
        } else {
            this.mWidth = resolution.getWidth();
            this.mHeight = resolution.getHeight();
        }
        this.mImageInfo = imageInfo;
    }

    @Override // androidx.camera.core.ForwardingImageProxy, androidx.camera.core.ImageProxy
    public synchronized Rect getCropRect() {
        if (this.mCropRect == null) {
            return new Rect(0, 0, getWidth(), getHeight());
        }
        return new Rect(this.mCropRect);
    }

    @Override // androidx.camera.core.ForwardingImageProxy, androidx.camera.core.ImageProxy
    public synchronized void setCropRect(Rect cropRect) {
        if (cropRect != null) {
            cropRect = new Rect(cropRect);
            if (!cropRect.intersect(0, 0, getWidth(), getHeight())) {
                cropRect.setEmpty();
            }
        }
        this.mCropRect = cropRect;
    }

    @Override // androidx.camera.core.ForwardingImageProxy, androidx.camera.core.ImageProxy
    public synchronized int getWidth() {
        return this.mWidth;
    }

    @Override // androidx.camera.core.ForwardingImageProxy, androidx.camera.core.ImageProxy
    public synchronized int getHeight() {
        return this.mHeight;
    }

    @Override // androidx.camera.core.ForwardingImageProxy, androidx.camera.core.ImageProxy
    public ImageInfo getImageInfo() {
        return this.mImageInfo;
    }
}
