package androidx.camera.core;

import android.graphics.Rect;
import android.media.Image;
import androidx.camera.core.ImageProxy;
import java.util.HashSet;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class ForwardingImageProxy implements ImageProxy {
    protected final ImageProxy mImage;
    private final Set<OnImageCloseListener> mOnImageCloseListeners = new HashSet();

    /* loaded from: classes.dex */
    public interface OnImageCloseListener {
        void onImageClose(ImageProxy imageProxy);
    }

    public ForwardingImageProxy(ImageProxy image) {
        this.mImage = image;
    }

    @Override // androidx.camera.core.ImageProxy, java.lang.AutoCloseable
    public void close() {
        synchronized (this) {
            this.mImage.close();
        }
        notifyOnImageCloseListeners();
    }

    @Override // androidx.camera.core.ImageProxy
    public synchronized Rect getCropRect() {
        return this.mImage.getCropRect();
    }

    @Override // androidx.camera.core.ImageProxy
    public synchronized void setCropRect(Rect rect) {
        this.mImage.setCropRect(rect);
    }

    @Override // androidx.camera.core.ImageProxy
    public synchronized int getFormat() {
        return this.mImage.getFormat();
    }

    @Override // androidx.camera.core.ImageProxy
    public synchronized int getHeight() {
        return this.mImage.getHeight();
    }

    @Override // androidx.camera.core.ImageProxy
    public synchronized int getWidth() {
        return this.mImage.getWidth();
    }

    @Override // androidx.camera.core.ImageProxy
    public synchronized ImageProxy.PlaneProxy[] getPlanes() {
        return this.mImage.getPlanes();
    }

    @Override // androidx.camera.core.ImageProxy
    public synchronized ImageInfo getImageInfo() {
        return this.mImage.getImageInfo();
    }

    @Override // androidx.camera.core.ImageProxy
    public synchronized Image getImage() {
        return this.mImage.getImage();
    }

    synchronized void addOnImageCloseListener(OnImageCloseListener listener) {
        this.mOnImageCloseListeners.add(listener);
    }

    protected void notifyOnImageCloseListeners() {
        Set<OnImageCloseListener> onImageCloseListeners;
        synchronized (this) {
            onImageCloseListeners = new HashSet<>(this.mOnImageCloseListeners);
        }
        for (OnImageCloseListener listener : onImageCloseListeners) {
            listener.onImageClose(this);
        }
    }
}
