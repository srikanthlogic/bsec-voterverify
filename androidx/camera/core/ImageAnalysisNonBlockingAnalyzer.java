package androidx.camera.core;

import androidx.camera.core.ForwardingImageProxy;
import androidx.camera.core.ImageAnalysisNonBlockingAnalyzer;
import androidx.camera.core.impl.ImageReaderProxy;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.impl.utils.futures.FutureCallback;
import androidx.camera.core.impl.utils.futures.Futures;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: classes.dex */
public final class ImageAnalysisNonBlockingAnalyzer extends ImageAnalysisAbstractAnalyzer {
    private static final String TAG;
    final Executor mBackgroundExecutor;
    private ImageProxy mCachedImage;
    private final AtomicReference<CacheAnalyzingImageProxy> mPostedImage = new AtomicReference<>();
    private final AtomicLong mPostedImageTimestamp = new AtomicLong();

    public ImageAnalysisNonBlockingAnalyzer(Executor executor) {
        this.mBackgroundExecutor = executor;
        open();
    }

    @Override // androidx.camera.core.impl.ImageReaderProxy.OnImageAvailableListener
    public void onImageAvailable(ImageReaderProxy imageReaderProxy) {
        ImageProxy imageProxy = imageReaderProxy.acquireLatestImage();
        if (imageProxy != null) {
            analyze(imageProxy);
        }
    }

    @Override // androidx.camera.core.ImageAnalysisAbstractAnalyzer
    public synchronized void open() {
        super.open();
        this.mCachedImage = null;
        this.mPostedImageTimestamp.set(-1);
        this.mPostedImage.set(null);
    }

    @Override // androidx.camera.core.ImageAnalysisAbstractAnalyzer
    public synchronized void close() {
        super.close();
        if (this.mCachedImage != null) {
            this.mCachedImage.close();
            this.mCachedImage = null;
        }
    }

    public synchronized void analyzeCachedImage() {
        if (this.mCachedImage != null) {
            ImageProxy cachedImage = this.mCachedImage;
            this.mCachedImage = null;
            analyze(cachedImage);
        }
    }

    private synchronized void analyze(ImageProxy imageProxy) {
        if (isClosed()) {
            imageProxy.close();
            return;
        }
        CacheAnalyzingImageProxy postedImage = this.mPostedImage.get();
        if (postedImage != null && imageProxy.getImageInfo().getTimestamp() <= this.mPostedImageTimestamp.get()) {
            imageProxy.close();
        } else if (postedImage == null || postedImage.isClosed()) {
            final CacheAnalyzingImageProxy newPostedImage = new CacheAnalyzingImageProxy(imageProxy, this);
            this.mPostedImage.set(newPostedImage);
            this.mPostedImageTimestamp.set(newPostedImage.getImageInfo().getTimestamp());
            Futures.addCallback(analyzeImage(newPostedImage), new FutureCallback<Void>() { // from class: androidx.camera.core.ImageAnalysisNonBlockingAnalyzer.1
                public void onSuccess(Void result) {
                }

                @Override // androidx.camera.core.impl.utils.futures.FutureCallback
                public void onFailure(Throwable t) {
                    newPostedImage.close();
                }
            }, CameraXExecutors.directExecutor());
        } else {
            if (this.mCachedImage != null) {
                this.mCachedImage.close();
            }
            this.mCachedImage = imageProxy;
        }
    }

    /* loaded from: classes.dex */
    public static class CacheAnalyzingImageProxy extends ForwardingImageProxy {
        private boolean mClosed = false;
        WeakReference<ImageAnalysisNonBlockingAnalyzer> mNonBlockingAnalyzerWeakReference;

        CacheAnalyzingImageProxy(ImageProxy image, ImageAnalysisNonBlockingAnalyzer nonBlockingAnalyzer) {
            super(image);
            this.mNonBlockingAnalyzerWeakReference = new WeakReference<>(nonBlockingAnalyzer);
            addOnImageCloseListener(new ForwardingImageProxy.OnImageCloseListener() { // from class: androidx.camera.core.-$$Lambda$ImageAnalysisNonBlockingAnalyzer$CacheAnalyzingImageProxy$Pq3gquMkypA8mh_f3dMKr3oV0M8
                @Override // androidx.camera.core.ForwardingImageProxy.OnImageCloseListener
                public final void onImageClose(ImageProxy imageProxy) {
                    ImageAnalysisNonBlockingAnalyzer.CacheAnalyzingImageProxy.this.lambda$new$0$ImageAnalysisNonBlockingAnalyzer$CacheAnalyzingImageProxy(imageProxy);
                }
            });
        }

        public /* synthetic */ void lambda$new$0$ImageAnalysisNonBlockingAnalyzer$CacheAnalyzingImageProxy(ImageProxy imageProxy) {
            this.mClosed = true;
            ImageAnalysisNonBlockingAnalyzer analyzer = this.mNonBlockingAnalyzerWeakReference.get();
            if (analyzer != null) {
                Executor executor = analyzer.mBackgroundExecutor;
                Objects.requireNonNull(analyzer);
                executor.execute(new Runnable() { // from class: androidx.camera.core.-$$Lambda$XqijcvI3c7o9krbxaHVHifcwgaY
                    @Override // java.lang.Runnable
                    public final void run() {
                        ImageAnalysisNonBlockingAnalyzer.this.analyzeCachedImage();
                    }
                });
            }
        }

        boolean isClosed() {
            return this.mClosed;
        }
    }
}
