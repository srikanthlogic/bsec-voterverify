package androidx.camera.core;

import android.view.Surface;
import androidx.camera.core.ForwardingImageProxy;
import androidx.camera.core.impl.ImageReaderProxy;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class SafeCloseImageReaderProxy implements ImageReaderProxy {
    private final ImageReaderProxy mImageReaderProxy;
    private final Surface mSurface;
    private final Object mLock = new Object();
    private volatile int mOutstandingImages = 0;
    private volatile boolean mIsClosed = false;
    private ForwardingImageProxy.OnImageCloseListener mImageCloseListener = new ForwardingImageProxy.OnImageCloseListener() { // from class: androidx.camera.core.-$$Lambda$SafeCloseImageReaderProxy$pee6fgxnPrlEv40Nqz6ZqBNSPGI
        @Override // androidx.camera.core.ForwardingImageProxy.OnImageCloseListener
        public final void onImageClose(ImageProxy imageProxy) {
            SafeCloseImageReaderProxy.this.lambda$new$0$SafeCloseImageReaderProxy(imageProxy);
        }
    };

    public /* synthetic */ void lambda$new$0$SafeCloseImageReaderProxy(ImageProxy image) {
        synchronized (this.mLock) {
            this.mOutstandingImages--;
            if (this.mIsClosed && this.mOutstandingImages == 0) {
                close();
            }
        }
    }

    public SafeCloseImageReaderProxy(ImageReaderProxy imageReaderProxy) {
        this.mImageReaderProxy = imageReaderProxy;
        this.mSurface = imageReaderProxy.getSurface();
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public ImageProxy acquireLatestImage() {
        ImageProxy wrapImageProxy;
        synchronized (this.mLock) {
            wrapImageProxy = wrapImageProxy(this.mImageReaderProxy.acquireLatestImage());
        }
        return wrapImageProxy;
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public ImageProxy acquireNextImage() {
        ImageProxy wrapImageProxy;
        synchronized (this.mLock) {
            wrapImageProxy = wrapImageProxy(this.mImageReaderProxy.acquireNextImage());
        }
        return wrapImageProxy;
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public void close() {
        synchronized (this.mLock) {
            this.mSurface.release();
            this.mImageReaderProxy.close();
        }
    }

    private ImageProxy wrapImageProxy(ImageProxy imageProxy) {
        synchronized (this.mLock) {
            if (imageProxy == null) {
                return null;
            }
            this.mOutstandingImages++;
            SingleCloseImageProxy singleCloseImageProxy = new SingleCloseImageProxy(imageProxy);
            singleCloseImageProxy.addOnImageCloseListener(this.mImageCloseListener);
            return singleCloseImageProxy;
        }
    }

    public void safeClose() {
        synchronized (this.mLock) {
            this.mIsClosed = true;
            this.mImageReaderProxy.clearOnImageAvailableListener();
            if (this.mOutstandingImages == 0) {
                close();
            }
        }
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public int getHeight() {
        int height;
        synchronized (this.mLock) {
            height = this.mImageReaderProxy.getHeight();
        }
        return height;
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public int getWidth() {
        int width;
        synchronized (this.mLock) {
            width = this.mImageReaderProxy.getWidth();
        }
        return width;
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public int getImageFormat() {
        int imageFormat;
        synchronized (this.mLock) {
            imageFormat = this.mImageReaderProxy.getImageFormat();
        }
        return imageFormat;
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public int getMaxImages() {
        int maxImages;
        synchronized (this.mLock) {
            maxImages = this.mImageReaderProxy.getMaxImages();
        }
        return maxImages;
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public Surface getSurface() {
        Surface surface;
        synchronized (this.mLock) {
            surface = this.mImageReaderProxy.getSurface();
        }
        return surface;
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public void setOnImageAvailableListener(ImageReaderProxy.OnImageAvailableListener listener, Executor executor) {
        synchronized (this.mLock) {
            this.mImageReaderProxy.setOnImageAvailableListener(new ImageReaderProxy.OnImageAvailableListener(listener) { // from class: androidx.camera.core.-$$Lambda$SafeCloseImageReaderProxy$VO_yHRAHpdG2MqCxQDiZeplGJKU
                private final /* synthetic */ ImageReaderProxy.OnImageAvailableListener f$1;

                {
                    this.f$1 = r2;
                }

                @Override // androidx.camera.core.impl.ImageReaderProxy.OnImageAvailableListener
                public final void onImageAvailable(ImageReaderProxy imageReaderProxy) {
                    SafeCloseImageReaderProxy.this.lambda$setOnImageAvailableListener$1$SafeCloseImageReaderProxy(this.f$1, imageReaderProxy);
                }
            }, executor);
        }
    }

    public /* synthetic */ void lambda$setOnImageAvailableListener$1$SafeCloseImageReaderProxy(ImageReaderProxy.OnImageAvailableListener listener, ImageReaderProxy imageReader) {
        listener.onImageAvailable(this);
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public void clearOnImageAvailableListener() {
        synchronized (this.mLock) {
            this.mImageReaderProxy.clearOnImageAvailableListener();
        }
    }
}
