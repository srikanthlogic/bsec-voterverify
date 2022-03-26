package androidx.camera.core.impl;

import androidx.camera.core.ImageInfo;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.impl.utils.futures.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public final class SingleImageProxyBundle implements ImageProxyBundle {
    private final int mCaptureId;
    private final ImageProxy mImageProxy;

    public SingleImageProxyBundle(ImageProxy imageProxy) {
        ImageInfo imageInfo = imageProxy.getImageInfo();
        if (imageInfo != null) {
            Object tag = imageInfo.getTag();
            if (tag == null) {
                throw new IllegalArgumentException("ImageProxy has no associated tag");
            } else if (tag instanceof Integer) {
                this.mCaptureId = ((Integer) tag).intValue();
                this.mImageProxy = imageProxy;
            } else {
                throw new IllegalArgumentException("ImageProxy has tag that isn't an integer");
            }
        } else {
            throw new IllegalArgumentException("ImageProxy has no associated ImageInfo");
        }
    }

    SingleImageProxyBundle(ImageProxy imageProxy, int captureId) {
        this.mCaptureId = captureId;
        this.mImageProxy = imageProxy;
    }

    public void close() {
        this.mImageProxy.close();
    }

    @Override // androidx.camera.core.impl.ImageProxyBundle
    public ListenableFuture<ImageProxy> getImageProxy(int captureId) {
        if (captureId != this.mCaptureId) {
            return Futures.immediateFailedFuture(new IllegalArgumentException("Capture id does not exist in the bundle"));
        }
        return Futures.immediateFuture(this.mImageProxy);
    }

    @Override // androidx.camera.core.impl.ImageProxyBundle
    public List<Integer> getCaptureIds() {
        return Collections.singletonList(Integer.valueOf(this.mCaptureId));
    }
}
