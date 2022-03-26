package com.facebook.drawee.backends.pipeline.info.internal;

import com.facebook.drawee.backends.pipeline.info.ImageOriginListener;
import com.facebook.drawee.backends.pipeline.info.ImagePerfMonitor;
import com.facebook.drawee.backends.pipeline.info.ImagePerfState;
/* loaded from: classes.dex */
public class ImagePerfImageOriginListener implements ImageOriginListener {
    private final ImagePerfMonitor mImagePerfMonitor;
    private final ImagePerfState mImagePerfState;

    public ImagePerfImageOriginListener(ImagePerfState imagePerfState, ImagePerfMonitor imagePerfMonitor) {
        this.mImagePerfState = imagePerfState;
        this.mImagePerfMonitor = imagePerfMonitor;
    }

    @Override // com.facebook.drawee.backends.pipeline.info.ImageOriginListener
    public void onImageLoaded(String controllerId, int imageOrigin, boolean successful) {
        this.mImagePerfState.setImageOrigin(imageOrigin);
        this.mImagePerfMonitor.notifyStatusUpdated(this.mImagePerfState, 1);
    }
}
