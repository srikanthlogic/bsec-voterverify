package androidx.camera.core;

import androidx.camera.core.impl.ImageReaderProxy;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.impl.utils.futures.FutureCallback;
import androidx.camera.core.impl.utils.futures.Futures;
/* loaded from: classes.dex */
final class ImageAnalysisBlockingAnalyzer extends ImageAnalysisAbstractAnalyzer {
    @Override // androidx.camera.core.impl.ImageReaderProxy.OnImageAvailableListener
    public void onImageAvailable(ImageReaderProxy imageReaderProxy) {
        final ImageProxy image = imageReaderProxy.acquireNextImage();
        if (image != null) {
            Futures.addCallback(analyzeImage(image), new FutureCallback<Void>() { // from class: androidx.camera.core.ImageAnalysisBlockingAnalyzer.1
                public void onSuccess(Void result) {
                }

                @Override // androidx.camera.core.impl.utils.futures.FutureCallback
                public void onFailure(Throwable t) {
                    image.close();
                }
            }, CameraXExecutors.directExecutor());
        }
    }
}
