package com.facebook.drawee.backends.pipeline.debug;

import com.facebook.drawee.backends.pipeline.info.ImageOriginListener;
import com.facebook.drawee.backends.pipeline.info.ImageOriginUtils;
/* loaded from: classes.dex */
public class DebugOverlayImageOriginListener implements ImageOriginListener {
    private int mImageOrigin = 1;

    @Override // com.facebook.drawee.backends.pipeline.info.ImageOriginListener
    public void onImageLoaded(String controllerId, int imageOrigin, boolean successful) {
        this.mImageOrigin = imageOrigin;
    }

    public String getImageOrigin() {
        return ImageOriginUtils.toString(this.mImageOrigin);
    }
}
