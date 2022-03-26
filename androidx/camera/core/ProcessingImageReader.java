package androidx.camera.core;

import android.media.ImageReader;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import androidx.camera.core.ProcessingImageReader;
import androidx.camera.core.impl.CameraCaptureCallback;
import androidx.camera.core.impl.CaptureBundle;
import androidx.camera.core.impl.CaptureProcessor;
import androidx.camera.core.impl.CaptureStage;
import androidx.camera.core.impl.ImageReaderProxy;
import androidx.camera.core.impl.utils.futures.FutureCallback;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.core.util.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class ProcessingImageReader implements ImageReaderProxy {
    private static final String TAG;
    private final List<Integer> mCaptureIdList;
    final CaptureProcessor mCaptureProcessor;
    private FutureCallback<List<ImageProxy>> mCaptureStageReadyCallback;
    private boolean mClosed;
    Executor mExecutor;
    private ImageReaderProxy.OnImageAvailableListener mImageProcessedListener;
    private final ImageReaderProxy mInputImageReader;
    ImageReaderProxy.OnImageAvailableListener mListener;
    final Object mLock;
    private final ImageReaderProxy mOutputImageReader;
    final Executor mPostProcessExecutor;
    SettableImageProxyBundle mSettableImageProxyBundle;
    private ImageReaderProxy.OnImageAvailableListener mTransformedListener;

    public ProcessingImageReader(int width, int height, int format, int maxImages, Executor postProcessExecutor, CaptureBundle captureBundle, CaptureProcessor captureProcessor) {
        this(new MetadataImageReader(width, height, format, maxImages), postProcessExecutor, captureBundle, captureProcessor);
    }

    ProcessingImageReader(ImageReaderProxy imageReader, Executor postProcessExecutor, CaptureBundle captureBundle, CaptureProcessor captureProcessor) {
        this.mLock = new Object();
        this.mTransformedListener = new ImageReaderProxy.OnImageAvailableListener() { // from class: androidx.camera.core.ProcessingImageReader.1
            @Override // androidx.camera.core.impl.ImageReaderProxy.OnImageAvailableListener
            public void onImageAvailable(ImageReaderProxy reader) {
                ProcessingImageReader.this.imageIncoming(reader);
            }
        };
        this.mImageProcessedListener = new ImageReaderProxy.OnImageAvailableListener() { // from class: androidx.camera.core.ProcessingImageReader.2
            @Override // androidx.camera.core.impl.ImageReaderProxy.OnImageAvailableListener
            public void onImageAvailable(ImageReaderProxy reader) {
                ImageReaderProxy.OnImageAvailableListener listener;
                Executor executor;
                synchronized (ProcessingImageReader.this.mLock) {
                    listener = ProcessingImageReader.this.mListener;
                    executor = ProcessingImageReader.this.mExecutor;
                    ProcessingImageReader.this.mSettableImageProxyBundle.reset();
                    ProcessingImageReader.this.setupSettableImageProxyBundleCallbacks();
                }
                if (listener == null) {
                    return;
                }
                if (executor != null) {
                    executor.execute(new Runnable(listener) { // from class: androidx.camera.core.-$$Lambda$ProcessingImageReader$2$Absc6rRf6C4HWC5BIoaUAtQpadI
                        private final /* synthetic */ ImageReaderProxy.OnImageAvailableListener f$1;

                        {
                            this.f$1 = r2;
                        }

                        @Override // java.lang.Runnable
                        public final void run() {
                            ProcessingImageReader.AnonymousClass2.this.lambda$onImageAvailable$0$ProcessingImageReader$2(this.f$1);
                        }
                    });
                } else {
                    listener.onImageAvailable(ProcessingImageReader.this);
                }
            }

            public /* synthetic */ void lambda$onImageAvailable$0$ProcessingImageReader$2(ImageReaderProxy.OnImageAvailableListener listener) {
                listener.onImageAvailable(ProcessingImageReader.this);
            }
        };
        this.mCaptureStageReadyCallback = new FutureCallback<List<ImageProxy>>() { // from class: androidx.camera.core.ProcessingImageReader.3
            public void onSuccess(List<ImageProxy> imageProxyList) {
                SettableImageProxyBundle settableImageProxyBundle;
                synchronized (ProcessingImageReader.this.mLock) {
                    settableImageProxyBundle = ProcessingImageReader.this.mSettableImageProxyBundle;
                }
                ProcessingImageReader.this.mCaptureProcessor.process(settableImageProxyBundle);
            }

            @Override // androidx.camera.core.impl.utils.futures.FutureCallback
            public void onFailure(Throwable throwable) {
            }
        };
        this.mClosed = false;
        this.mSettableImageProxyBundle = new SettableImageProxyBundle(Collections.emptyList());
        this.mCaptureIdList = new ArrayList();
        if (imageReader.getMaxImages() >= captureBundle.getCaptureStages().size()) {
            this.mInputImageReader = imageReader;
            this.mOutputImageReader = new AndroidImageReaderProxy(ImageReader.newInstance(imageReader.getWidth(), imageReader.getHeight(), imageReader.getImageFormat(), imageReader.getMaxImages()));
            this.mPostProcessExecutor = postProcessExecutor;
            this.mCaptureProcessor = captureProcessor;
            this.mCaptureProcessor.onOutputSurface(this.mOutputImageReader.getSurface(), getImageFormat());
            this.mCaptureProcessor.onResolutionUpdate(new Size(this.mInputImageReader.getWidth(), this.mInputImageReader.getHeight()));
            setCaptureBundle(captureBundle);
            return;
        }
        throw new IllegalArgumentException("MetadataImageReader is smaller than CaptureBundle.");
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public ImageProxy acquireLatestImage() {
        ImageProxy acquireLatestImage;
        synchronized (this.mLock) {
            acquireLatestImage = this.mOutputImageReader.acquireLatestImage();
        }
        return acquireLatestImage;
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public ImageProxy acquireNextImage() {
        ImageProxy acquireNextImage;
        synchronized (this.mLock) {
            acquireNextImage = this.mOutputImageReader.acquireNextImage();
        }
        return acquireNextImage;
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public void close() {
        synchronized (this.mLock) {
            if (!this.mClosed) {
                this.mInputImageReader.close();
                this.mOutputImageReader.close();
                this.mSettableImageProxyBundle.close();
                this.mClosed = true;
            }
        }
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public int getHeight() {
        int height;
        synchronized (this.mLock) {
            height = this.mInputImageReader.getHeight();
        }
        return height;
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public int getWidth() {
        int width;
        synchronized (this.mLock) {
            width = this.mInputImageReader.getWidth();
        }
        return width;
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public int getImageFormat() {
        int imageFormat;
        synchronized (this.mLock) {
            imageFormat = this.mInputImageReader.getImageFormat();
        }
        return imageFormat;
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public int getMaxImages() {
        int maxImages;
        synchronized (this.mLock) {
            maxImages = this.mInputImageReader.getMaxImages();
        }
        return maxImages;
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public Surface getSurface() {
        Surface surface;
        synchronized (this.mLock) {
            surface = this.mInputImageReader.getSurface();
        }
        return surface;
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public void setOnImageAvailableListener(ImageReaderProxy.OnImageAvailableListener listener, Executor executor) {
        synchronized (this.mLock) {
            this.mListener = (ImageReaderProxy.OnImageAvailableListener) Preconditions.checkNotNull(listener);
            this.mExecutor = (Executor) Preconditions.checkNotNull(executor);
            this.mInputImageReader.setOnImageAvailableListener(this.mTransformedListener, executor);
            this.mOutputImageReader.setOnImageAvailableListener(this.mImageProcessedListener, executor);
        }
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy
    public void clearOnImageAvailableListener() {
        synchronized (this.mLock) {
            this.mListener = null;
            this.mExecutor = null;
            this.mInputImageReader.clearOnImageAvailableListener();
            this.mOutputImageReader.clearOnImageAvailableListener();
            this.mSettableImageProxyBundle.close();
        }
    }

    public void setCaptureBundle(CaptureBundle captureBundle) {
        synchronized (this.mLock) {
            if (captureBundle.getCaptureStages() != null) {
                if (this.mInputImageReader.getMaxImages() >= captureBundle.getCaptureStages().size()) {
                    this.mCaptureIdList.clear();
                    for (CaptureStage captureStage : captureBundle.getCaptureStages()) {
                        if (captureStage != null) {
                            this.mCaptureIdList.add(Integer.valueOf(captureStage.getId()));
                        }
                    }
                } else {
                    throw new IllegalArgumentException("CaptureBundle is lager than InputImageReader.");
                }
            }
            this.mSettableImageProxyBundle = new SettableImageProxyBundle(this.mCaptureIdList);
            setupSettableImageProxyBundleCallbacks();
        }
    }

    public CameraCaptureCallback getCameraCaptureCallback() {
        synchronized (this.mLock) {
            if (!(this.mInputImageReader instanceof MetadataImageReader)) {
                return null;
            }
            return ((MetadataImageReader) this.mInputImageReader).getCameraCaptureCallback();
        }
    }

    void setupSettableImageProxyBundleCallbacks() {
        List<ListenableFuture<ImageProxy>> futureList = new ArrayList<>();
        for (Integer id : this.mCaptureIdList) {
            futureList.add(this.mSettableImageProxyBundle.getImageProxy(id.intValue()));
        }
        Futures.addCallback(Futures.allAsList(futureList), this.mCaptureStageReadyCallback, this.mPostProcessExecutor);
    }

    void imageIncoming(ImageReaderProxy imageReader) {
        synchronized (this.mLock) {
            if (!this.mClosed) {
                ImageProxy image = null;
                try {
                    ImageProxy image2 = imageReader.acquireNextImage();
                    if (image2 != null) {
                        Integer tag = (Integer) image2.getImageInfo().getTag();
                        if (!this.mCaptureIdList.contains(tag)) {
                            Log.w(TAG, "ImageProxyBundle does not contain this id: " + tag);
                            image2.close();
                        } else {
                            this.mSettableImageProxyBundle.addImageProxy(image2);
                        }
                    }
                } catch (IllegalStateException e) {
                    Log.e(TAG, "Failed to acquire latest image.", e);
                    if (0 != 0) {
                        Integer tag2 = (Integer) image.getImageInfo().getTag();
                        if (!this.mCaptureIdList.contains(tag2)) {
                            Log.w(TAG, "ImageProxyBundle does not contain this id: " + tag2);
                            image.close();
                        } else {
                            this.mSettableImageProxyBundle.addImageProxy(null);
                        }
                    }
                }
            }
        }
    }
}
