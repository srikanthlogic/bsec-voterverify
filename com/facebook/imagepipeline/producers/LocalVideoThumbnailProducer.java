package com.facebook.imagepipeline.producers;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.UriUtil;
import com.facebook.imagepipeline.bitmaps.SimpleBitmapReleaser;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class LocalVideoThumbnailProducer implements Producer<CloseableReference<CloseableImage>> {
    static final String CREATED_THUMBNAIL = "createdThumbnail";
    public static final String PRODUCER_NAME = "VideoThumbnailProducer";
    private final ContentResolver mContentResolver;
    private final Executor mExecutor;

    public LocalVideoThumbnailProducer(Executor executor, ContentResolver contentResolver) {
        this.mExecutor = executor;
        this.mContentResolver = contentResolver;
    }

    @Override // com.facebook.imagepipeline.producers.Producer
    public void produceResults(Consumer<CloseableReference<CloseableImage>> consumer, ProducerContext producerContext) {
        final ProducerListener listener = producerContext.getListener();
        final String requestId = producerContext.getId();
        final ImageRequest imageRequest = producerContext.getImageRequest();
        final StatefulProducerRunnable cancellableProducerRunnable = new StatefulProducerRunnable<CloseableReference<CloseableImage>>(consumer, PRODUCER_NAME, listener, requestId) { // from class: com.facebook.imagepipeline.producers.LocalVideoThumbnailProducer.1
            /* JADX INFO: Access modifiers changed from: protected */
            public void onSuccess(CloseableReference<CloseableImage> result) {
                super.onSuccess((AnonymousClass1) result);
                listener.onUltimateProducerReached(requestId, LocalVideoThumbnailProducer.PRODUCER_NAME, result != null);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.facebook.imagepipeline.producers.StatefulProducerRunnable, com.facebook.common.executors.StatefulRunnable
            public void onFailure(Exception e) {
                super.onFailure(e);
                listener.onUltimateProducerReached(requestId, LocalVideoThumbnailProducer.PRODUCER_NAME, false);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.facebook.common.executors.StatefulRunnable
            @Nullable
            public CloseableReference<CloseableImage> getResult() throws Exception {
                Bitmap thumbnailBitmap;
                String path = LocalVideoThumbnailProducer.this.getLocalFilePath(imageRequest);
                if (path == null || (thumbnailBitmap = ThumbnailUtils.createVideoThumbnail(path, LocalVideoThumbnailProducer.calculateKind(imageRequest))) == null) {
                    return null;
                }
                return CloseableReference.of(new CloseableStaticBitmap(thumbnailBitmap, SimpleBitmapReleaser.getInstance(), ImmutableQualityInfo.FULL_QUALITY, 0));
            }

            /* JADX INFO: Access modifiers changed from: protected */
            public Map<String, String> getExtraMapOnSuccess(CloseableReference<CloseableImage> result) {
                return ImmutableMap.of(LocalVideoThumbnailProducer.CREATED_THUMBNAIL, String.valueOf(result != null));
            }

            /* JADX INFO: Access modifiers changed from: protected */
            public void disposeResult(CloseableReference<CloseableImage> result) {
                CloseableReference.closeSafely(result);
            }
        };
        producerContext.addCallbacks(new BaseProducerContextCallbacks() { // from class: com.facebook.imagepipeline.producers.LocalVideoThumbnailProducer.2
            @Override // com.facebook.imagepipeline.producers.BaseProducerContextCallbacks, com.facebook.imagepipeline.producers.ProducerContextCallbacks
            public void onCancellationRequested() {
                cancellableProducerRunnable.cancel();
            }
        });
        this.mExecutor.execute(cancellableProducerRunnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int calculateKind(ImageRequest imageRequest) {
        if (imageRequest.getPreferredWidth() > 96 || imageRequest.getPreferredHeight() > 96) {
            return 1;
        }
        return 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Nullable
    public String getLocalFilePath(ImageRequest imageRequest) {
        Uri uri = imageRequest.getSourceUri();
        if (UriUtil.isLocalFileUri(uri)) {
            return imageRequest.getSourceFile().getPath();
        }
        if (!UriUtil.isLocalContentUri(uri)) {
            return null;
        }
        String selection = null;
        String[] selectionArgs = null;
        if (Build.VERSION.SDK_INT >= 19 && "com.android.providers.media.documents".equals(uri.getAuthority())) {
            String documentId = DocumentsContract.getDocumentId(uri);
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            selection = "_id=?";
            selectionArgs = new String[]{documentId.split(":")[1]};
        }
        Cursor cursor = this.mContentResolver.query(uri, new String[]{"_data"}, selection, selectionArgs, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (cursor == null) {
            return null;
        }
        cursor.close();
        return null;
    }
}
