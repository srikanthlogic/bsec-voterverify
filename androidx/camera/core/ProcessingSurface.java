package androidx.camera.core;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import androidx.camera.core.impl.CameraCaptureCallback;
import androidx.camera.core.impl.CaptureProcessor;
import androidx.camera.core.impl.CaptureStage;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.ImageReaderProxy;
import androidx.camera.core.impl.SingleImageProxyBundle;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.impl.utils.futures.FutureCallback;
import androidx.camera.core.impl.utils.futures.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class ProcessingSurface extends DeferrableSurface {
    private static final int MAX_IMAGES;
    private static final String TAG;
    private final CameraCaptureCallback mCameraCaptureCallback;
    final CaptureProcessor mCaptureProcessor;
    final CaptureStage mCaptureStage;
    private final Handler mImageReaderHandler;
    final MetadataImageReader mInputImageReader;
    final Surface mInputSurface;
    private final DeferrableSurface mOutputDeferrableSurface;
    private final Size mResolution;
    final Object mLock = new Object();
    private final ImageReaderProxy.OnImageAvailableListener mTransformedListener = new ImageReaderProxy.OnImageAvailableListener() { // from class: androidx.camera.core.-$$Lambda$ProcessingSurface$g7Iq8eLl8MFZ63JqJ0Kpek7IMD0
        @Override // androidx.camera.core.impl.ImageReaderProxy.OnImageAvailableListener
        public final void onImageAvailable(ImageReaderProxy imageReaderProxy) {
            ProcessingSurface.this.lambda$new$0$ProcessingSurface(imageReaderProxy);
        }
    };
    boolean mReleased = false;

    public /* synthetic */ void lambda$new$0$ProcessingSurface(ImageReaderProxy reader) {
        synchronized (this.mLock) {
            imageIncoming(reader);
        }
    }

    public ProcessingSurface(int width, int height, int format, Handler handler, CaptureStage captureStage, CaptureProcessor captureProcessor, DeferrableSurface outputSurface) {
        this.mResolution = new Size(width, height);
        if (handler != null) {
            this.mImageReaderHandler = handler;
        } else {
            Looper looper = Looper.myLooper();
            if (looper != null) {
                this.mImageReaderHandler = new Handler(looper);
            } else {
                throw new IllegalStateException("Creating a ProcessingSurface requires a non-null Handler, or be created  on a thread with a Looper.");
            }
        }
        Executor executor = CameraXExecutors.newHandlerExecutor(this.mImageReaderHandler);
        this.mInputImageReader = new MetadataImageReader(width, height, format, 2);
        this.mInputImageReader.setOnImageAvailableListener(this.mTransformedListener, executor);
        this.mInputSurface = this.mInputImageReader.getSurface();
        this.mCameraCaptureCallback = this.mInputImageReader.getCameraCaptureCallback();
        this.mCaptureProcessor = captureProcessor;
        this.mCaptureProcessor.onResolutionUpdate(this.mResolution);
        this.mCaptureStage = captureStage;
        this.mOutputDeferrableSurface = outputSurface;
        Futures.addCallback(outputSurface.getSurface(), new FutureCallback<Surface>() { // from class: androidx.camera.core.ProcessingSurface.1
            public void onSuccess(Surface surface) {
                synchronized (ProcessingSurface.this.mLock) {
                    ProcessingSurface.this.mCaptureProcessor.onOutputSurface(surface, 1);
                }
            }

            @Override // androidx.camera.core.impl.utils.futures.FutureCallback
            public void onFailure(Throwable t) {
                Log.e(ProcessingSurface.TAG, "Failed to extract Listenable<Surface>.", t);
            }
        }, CameraXExecutors.directExecutor());
        getTerminationFuture().addListener(new Runnable() { // from class: androidx.camera.core.-$$Lambda$ProcessingSurface$fleJ7Fv2BvhRan9diypF10B_VWk
            @Override // java.lang.Runnable
            public final void run() {
                ProcessingSurface.this.release();
            }
        }, CameraXExecutors.directExecutor());
    }

    @Override // androidx.camera.core.impl.DeferrableSurface
    public ListenableFuture<Surface> provideSurface() {
        ListenableFuture<Surface> immediateFuture;
        synchronized (this.mLock) {
            immediateFuture = Futures.immediateFuture(this.mInputSurface);
        }
        return immediateFuture;
    }

    public CameraCaptureCallback getCameraCaptureCallback() {
        CameraCaptureCallback cameraCaptureCallback;
        synchronized (this.mLock) {
            if (!this.mReleased) {
                cameraCaptureCallback = this.mCameraCaptureCallback;
            } else {
                throw new IllegalStateException("ProcessingSurface already released!");
            }
        }
        return cameraCaptureCallback;
    }

    public void release() {
        synchronized (this.mLock) {
            if (!this.mReleased) {
                this.mInputImageReader.close();
                this.mInputSurface.release();
                this.mOutputDeferrableSurface.close();
                this.mReleased = true;
            }
        }
    }

    void imageIncoming(ImageReaderProxy imageReader) {
        if (!this.mReleased) {
            ImageProxy image = null;
            try {
                image = imageReader.acquireNextImage();
            } catch (IllegalStateException e) {
                Log.e(TAG, "Failed to acquire next image.", e);
            }
            if (image != null) {
                ImageInfo imageInfo = image.getImageInfo();
                if (imageInfo == null) {
                    image.close();
                    return;
                }
                Object tagObject = imageInfo.getTag();
                if (tagObject == null) {
                    image.close();
                } else if (!(tagObject instanceof Integer)) {
                    image.close();
                } else {
                    Integer tag = (Integer) tagObject;
                    if (this.mCaptureStage.getId() != tag.intValue()) {
                        Log.w(TAG, "ImageProxyBundle does not contain this id: " + tag);
                        image.close();
                        return;
                    }
                    SingleImageProxyBundle imageProxyBundle = new SingleImageProxyBundle(image);
                    this.mCaptureProcessor.process(imageProxyBundle);
                    imageProxyBundle.close();
                }
            }
        }
    }
}
