package androidx.camera.core;

import android.media.Image;
import android.media.ImageReader;
import android.view.Surface;
import androidx.camera.core.impl.ImageReaderProxy;
import androidx.camera.core.impl.utils.MainThreadAsyncHandler;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class AndroidImageReaderProxy implements ImageReaderProxy {
    private final ImageReader mImageReader;

    public AndroidImageReaderProxy(ImageReader imageReader) {
        this.mImageReader = imageReader;
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public synchronized ImageProxy acquireLatestImage() {
        Image image;
        try {
            image = this.mImageReader.acquireLatestImage();
        } catch (RuntimeException e) {
            if (isImageReaderContextNotInitializedException(e)) {
                image = null;
            } else {
                throw e;
            }
        }
        if (image == null) {
            return null;
        }
        return new AndroidImageProxy(image);
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public synchronized ImageProxy acquireNextImage() {
        Image image;
        try {
            image = this.mImageReader.acquireNextImage();
        } catch (RuntimeException e) {
            if (isImageReaderContextNotInitializedException(e)) {
                image = null;
            } else {
                throw e;
            }
        }
        if (image == null) {
            return null;
        }
        return new AndroidImageProxy(image);
    }

    private boolean isImageReaderContextNotInitializedException(RuntimeException e) {
        return "ImageReaderContext is not initialized".equals(e.getMessage());
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public synchronized void close() {
        this.mImageReader.close();
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public synchronized int getHeight() {
        return this.mImageReader.getHeight();
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public synchronized int getWidth() {
        return this.mImageReader.getWidth();
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public synchronized int getImageFormat() {
        return this.mImageReader.getImageFormat();
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public synchronized int getMaxImages() {
        return this.mImageReader.getMaxImages();
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public synchronized Surface getSurface() {
        return this.mImageReader.getSurface();
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public synchronized void setOnImageAvailableListener(ImageReaderProxy.OnImageAvailableListener listener, Executor executor) {
        this.mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener(executor, listener) { // from class: androidx.camera.core.-$$Lambda$AndroidImageReaderProxy$1j5V93NALzdEKViOyKnqPUbnDDk
            private final /* synthetic */ Executor f$1;
            private final /* synthetic */ ImageReaderProxy.OnImageAvailableListener f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // android.media.ImageReader.OnImageAvailableListener
            public final void onImageAvailable(ImageReader imageReader) {
                AndroidImageReaderProxy.this.lambda$setOnImageAvailableListener$1$AndroidImageReaderProxy(this.f$1, this.f$2, imageReader);
            }
        }, MainThreadAsyncHandler.getInstance());
    }

    public /* synthetic */ void lambda$setOnImageAvailableListener$0$AndroidImageReaderProxy(ImageReaderProxy.OnImageAvailableListener listener) {
        listener.onImageAvailable(this);
    }

    public /* synthetic */ void lambda$setOnImageAvailableListener$1$AndroidImageReaderProxy(Executor executor, ImageReaderProxy.OnImageAvailableListener listener, ImageReader imageReader) {
        executor.execute(new Runnable(listener) { // from class: androidx.camera.core.-$$Lambda$AndroidImageReaderProxy$RyV51268LxqXqf_5yS6H_QoqpDY
            private final /* synthetic */ ImageReaderProxy.OnImageAvailableListener f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                AndroidImageReaderProxy.this.lambda$setOnImageAvailableListener$0$AndroidImageReaderProxy(this.f$1);
            }
        });
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public synchronized void clearOnImageAvailableListener() {
        this.mImageReader.setOnImageAvailableListener(null, null);
    }
}
