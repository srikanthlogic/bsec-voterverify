package com.facebook.imagepipeline.datasource;

import android.graphics.Bitmap;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public abstract class BaseBitmapReferenceDataSubscriber extends BaseDataSubscriber<CloseableReference<CloseableImage>> {
    protected abstract void onNewResultImpl(@Nullable CloseableReference<Bitmap> closeableReference);

    @Override // com.facebook.datasource.BaseDataSubscriber
    public void onNewResultImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
        if (dataSource.isFinished()) {
            CloseableReference<CloseableImage> closeableImageRef = dataSource.getResult();
            CloseableReference<Bitmap> bitmapReference = null;
            if (closeableImageRef != null && (closeableImageRef.get() instanceof CloseableStaticBitmap)) {
                bitmapReference = ((CloseableStaticBitmap) closeableImageRef.get()).cloneUnderlyingBitmapReference();
            }
            try {
                onNewResultImpl(bitmapReference);
            } finally {
                CloseableReference.closeSafely(bitmapReference);
                CloseableReference.closeSafely(closeableImageRef);
            }
        }
    }
}
