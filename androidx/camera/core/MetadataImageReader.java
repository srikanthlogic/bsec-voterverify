package androidx.camera.core;

import android.media.ImageReader;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.Surface;
import androidx.camera.core.ForwardingImageProxy;
import androidx.camera.core.impl.CameraCaptureCallback;
import androidx.camera.core.impl.CameraCaptureResult;
import androidx.camera.core.impl.ImageReaderProxy;
import androidx.camera.core.internal.CameraCaptureResultImageInfo;
import androidx.core.util.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class MetadataImageReader implements ImageReaderProxy, ForwardingImageProxy.OnImageCloseListener {
    private static final String TAG;
    private final List<ImageProxy> mAcquiredImageProxies;
    private CameraCaptureCallback mCameraCaptureCallback;
    private boolean mClosed;
    private Executor mExecutor;
    private int mImageProxiesIndex;
    private final ImageReaderProxy mImageReaderProxy;
    ImageReaderProxy.OnImageAvailableListener mListener;
    private final Object mLock;
    private final List<ImageProxy> mMatchedImageProxies;
    private final LongSparseArray<ImageInfo> mPendingImageInfos;
    private final LongSparseArray<ImageProxy> mPendingImages;
    private ImageReaderProxy.OnImageAvailableListener mTransformedListener;

    public MetadataImageReader(int width, int height, int format, int maxImages) {
        this(createImageReaderProxy(width, height, format, maxImages));
    }

    private static ImageReaderProxy createImageReaderProxy(int width, int height, int format, int maxImages) {
        return new AndroidImageReaderProxy(ImageReader.newInstance(width, height, format, maxImages));
    }

    MetadataImageReader(ImageReaderProxy imageReaderProxy) {
        this.mLock = new Object();
        this.mCameraCaptureCallback = new CameraCaptureCallback() { // from class: androidx.camera.core.MetadataImageReader.1
            @Override // androidx.camera.core.impl.CameraCaptureCallback
            public void onCaptureCompleted(CameraCaptureResult cameraCaptureResult) {
                super.onCaptureCompleted(cameraCaptureResult);
                MetadataImageReader.this.resultIncoming(cameraCaptureResult);
            }
        };
        this.mTransformedListener = new ImageReaderProxy.OnImageAvailableListener() { // from class: androidx.camera.core.-$$Lambda$MetadataImageReader$DONJ_Tb-b_WYV8EceE_yMZTG_HA
            @Override // androidx.camera.core.impl.ImageReaderProxy.OnImageAvailableListener
            public final void onImageAvailable(ImageReaderProxy imageReaderProxy2) {
                MetadataImageReader.this.lambda$new$0$MetadataImageReader(imageReaderProxy2);
            }
        };
        this.mClosed = false;
        this.mPendingImageInfos = new LongSparseArray<>();
        this.mPendingImages = new LongSparseArray<>();
        this.mAcquiredImageProxies = new ArrayList();
        this.mImageReaderProxy = imageReaderProxy;
        this.mImageProxiesIndex = 0;
        this.mMatchedImageProxies = new ArrayList(getMaxImages());
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public ImageProxy acquireLatestImage() {
        synchronized (this.mLock) {
            if (this.mMatchedImageProxies.isEmpty()) {
                return null;
            }
            if (this.mImageProxiesIndex < this.mMatchedImageProxies.size()) {
                List<ImageProxy> toClose = new ArrayList<>();
                for (int i = 0; i < this.mMatchedImageProxies.size() - 1; i++) {
                    if (!this.mAcquiredImageProxies.contains(this.mMatchedImageProxies.get(i))) {
                        toClose.add(this.mMatchedImageProxies.get(i));
                    }
                }
                for (ImageProxy image : toClose) {
                    image.close();
                }
                this.mImageProxiesIndex = this.mMatchedImageProxies.size() - 1;
                List<ImageProxy> list = this.mMatchedImageProxies;
                int i2 = this.mImageProxiesIndex;
                this.mImageProxiesIndex = i2 + 1;
                ImageProxy acquiredImage = list.get(i2);
                this.mAcquiredImageProxies.add(acquiredImage);
                return acquiredImage;
            }
            throw new IllegalStateException("Maximum image number reached.");
        }
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public ImageProxy acquireNextImage() {
        synchronized (this.mLock) {
            if (this.mMatchedImageProxies.isEmpty()) {
                return null;
            }
            if (this.mImageProxiesIndex < this.mMatchedImageProxies.size()) {
                List<ImageProxy> list = this.mMatchedImageProxies;
                int i = this.mImageProxiesIndex;
                this.mImageProxiesIndex = i + 1;
                ImageProxy acquiredImage = list.get(i);
                this.mAcquiredImageProxies.add(acquiredImage);
                return acquiredImage;
            }
            throw new IllegalStateException("Maximum image number reached.");
        }
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public void close() {
        synchronized (this.mLock) {
            if (!this.mClosed) {
                for (ImageProxy image : new ArrayList<>(this.mMatchedImageProxies)) {
                    image.close();
                }
                this.mMatchedImageProxies.clear();
                this.mImageReaderProxy.close();
                this.mClosed = true;
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
            this.mListener = (ImageReaderProxy.OnImageAvailableListener) Preconditions.checkNotNull(listener);
            this.mExecutor = (Executor) Preconditions.checkNotNull(executor);
            this.mImageReaderProxy.setOnImageAvailableListener(this.mTransformedListener, executor);
        }
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public void clearOnImageAvailableListener() {
        synchronized (this.mLock) {
            this.mListener = null;
            this.mExecutor = null;
        }
    }

    @Override // androidx.camera.core.ForwardingImageProxy.OnImageCloseListener
    public void onImageClose(ImageProxy image) {
        synchronized (this.mLock) {
            dequeImageProxy(image);
        }
    }

    private void enqueueImageProxy(SettableImageProxy image) {
        Executor executor;
        ImageReaderProxy.OnImageAvailableListener listener;
        synchronized (this.mLock) {
            if (this.mMatchedImageProxies.size() < getMaxImages()) {
                image.addOnImageCloseListener(this);
                this.mMatchedImageProxies.add(image);
                listener = this.mListener;
                executor = this.mExecutor;
            } else {
                Log.d("TAG", "Maximum image number reached.");
                image.close();
                listener = null;
                executor = null;
            }
        }
        if (listener == null) {
            return;
        }
        if (executor != null) {
            executor.execute(new Runnable(listener) { // from class: androidx.camera.core.-$$Lambda$MetadataImageReader$8HL2vw_DsR0m0aXoFgsFnlVPkmY
                private final /* synthetic */ ImageReaderProxy.OnImageAvailableListener f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    MetadataImageReader.this.lambda$enqueueImageProxy$1$MetadataImageReader(this.f$1);
                }
            });
        } else {
            listener.onImageAvailable(this);
        }
    }

    public /* synthetic */ void lambda$enqueueImageProxy$1$MetadataImageReader(ImageReaderProxy.OnImageAvailableListener listener) {
        listener.onImageAvailable(this);
    }

    private void dequeImageProxy(ImageProxy image) {
        synchronized (this.mLock) {
            int index = this.mMatchedImageProxies.indexOf(image);
            if (index >= 0) {
                this.mMatchedImageProxies.remove(index);
                if (index <= this.mImageProxiesIndex) {
                    this.mImageProxiesIndex--;
                }
            }
            this.mAcquiredImageProxies.remove(image);
        }
    }

    public CameraCaptureCallback getCameraCaptureCallback() {
        return this.mCameraCaptureCallback;
    }

    /* renamed from: imageIncoming */
    public void lambda$new$0$MetadataImageReader(ImageReaderProxy imageReader) {
        synchronized (this.mLock) {
            if (!this.mClosed) {
                int numAcquired = 0;
                do {
                    ImageProxy image = null;
                    try {
                        image = imageReader.acquireNextImage();
                    } catch (IllegalStateException e) {
                        Log.d(TAG, "Failed to acquire next image.", e);
                        if (0 != 0) {
                            numAcquired++;
                            this.mPendingImages.put(image.getImageInfo().getTimestamp(), null);
                        }
                    }
                    if (image != null) {
                        numAcquired++;
                        this.mPendingImages.put(image.getImageInfo().getTimestamp(), image);
                        matchImages();
                    }
                    if (image == null) {
                        break;
                    }
                } while (numAcquired < imageReader.getMaxImages());
            }
        }
    }

    void resultIncoming(CameraCaptureResult cameraCaptureResult) {
        synchronized (this.mLock) {
            if (!this.mClosed) {
                this.mPendingImageInfos.put(cameraCaptureResult.getTimestamp(), new CameraCaptureResultImageInfo(cameraCaptureResult));
                matchImages();
            }
        }
    }

    private void removeStaleData() {
        synchronized (this.mLock) {
            if (!(this.mPendingImages.size() == 0 || this.mPendingImageInfos.size() == 0)) {
                boolean z = false;
                Long minImageProxyTimestamp = Long.valueOf(this.mPendingImages.keyAt(0));
                Long minImageInfoTimestamp = Long.valueOf(this.mPendingImageInfos.keyAt(0));
                if (!minImageInfoTimestamp.equals(minImageProxyTimestamp)) {
                    z = true;
                }
                Preconditions.checkArgument(z);
                if (minImageInfoTimestamp.longValue() > minImageProxyTimestamp.longValue()) {
                    for (int i = this.mPendingImages.size() - 1; i >= 0; i--) {
                        if (this.mPendingImages.keyAt(i) < minImageInfoTimestamp.longValue()) {
                            this.mPendingImages.valueAt(i).close();
                            this.mPendingImages.removeAt(i);
                        }
                    }
                } else {
                    for (int i2 = this.mPendingImageInfos.size() - 1; i2 >= 0; i2--) {
                        if (this.mPendingImageInfos.keyAt(i2) < minImageProxyTimestamp.longValue()) {
                            this.mPendingImageInfos.removeAt(i2);
                        }
                    }
                }
            }
        }
    }

    private void matchImages() {
        synchronized (this.mLock) {
            for (int i = this.mPendingImageInfos.size() - 1; i >= 0; i--) {
                ImageInfo imageInfo = this.mPendingImageInfos.valueAt(i);
                long timestamp = imageInfo.getTimestamp();
                ImageProxy image = this.mPendingImages.get(timestamp);
                if (image != null) {
                    this.mPendingImages.remove(timestamp);
                    this.mPendingImageInfos.removeAt(i);
                    enqueueImageProxy(new SettableImageProxy(image, imageInfo));
                }
            }
            removeStaleData();
        }
    }
}
