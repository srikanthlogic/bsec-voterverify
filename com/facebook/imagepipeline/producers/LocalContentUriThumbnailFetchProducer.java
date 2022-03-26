package com.facebook.imagepipeline.producers;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import com.example.aadhaarfpoffline.tatvik.database.DBHelper;
import com.facebook.common.logging.FLog;
import com.facebook.common.memory.PooledByteBufferFactory;
import com.facebook.common.util.UriUtil;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.BitmapCounterConfig;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imageutils.JfifUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class LocalContentUriThumbnailFetchProducer extends LocalFetchProducer implements ThumbnailProducer<EncodedImage> {
    private static final int NO_THUMBNAIL = 0;
    public static final String PRODUCER_NAME = "LocalContentUriThumbnailFetchProducer";
    private final ContentResolver mContentResolver;
    private static final Class<?> TAG = LocalContentUriThumbnailFetchProducer.class;
    private static final String[] PROJECTION = {DBHelper.Key_ID, "_data"};
    private static final String[] THUMBNAIL_PROJECTION = {"_data"};
    private static final Rect MINI_THUMBNAIL_DIMENSIONS = new Rect(0, 0, 512, BitmapCounterConfig.DEFAULT_MAX_BITMAP_COUNT);
    private static final Rect MICRO_THUMBNAIL_DIMENSIONS = new Rect(0, 0, 96, 96);

    public LocalContentUriThumbnailFetchProducer(Executor executor, PooledByteBufferFactory pooledByteBufferFactory, ContentResolver contentResolver) {
        super(executor, pooledByteBufferFactory);
        this.mContentResolver = contentResolver;
    }

    @Override // com.facebook.imagepipeline.producers.ThumbnailProducer
    public boolean canProvideImageForSize(ResizeOptions resizeOptions) {
        return ThumbnailSizeChecker.isImageBigEnough(MINI_THUMBNAIL_DIMENSIONS.width(), MINI_THUMBNAIL_DIMENSIONS.height(), resizeOptions);
    }

    @Override // com.facebook.imagepipeline.producers.LocalFetchProducer
    @Nullable
    protected EncodedImage getEncodedImage(ImageRequest imageRequest) throws IOException {
        EncodedImage cameraImage;
        Uri uri = imageRequest.getSourceUri();
        if (!UriUtil.isLocalCameraUri(uri) || (cameraImage = getCameraImage(uri, imageRequest.getResizeOptions())) == null) {
            return null;
        }
        return cameraImage;
    }

    @Nullable
    private EncodedImage getCameraImage(Uri uri, ResizeOptions resizeOptions) throws IOException {
        EncodedImage thumbnail;
        Cursor cursor = this.mContentResolver.query(uri, PROJECTION, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            String pathname = cursor.getString(cursor.getColumnIndex("_data"));
            if (resizeOptions == null || (thumbnail = getThumbnail(resizeOptions, cursor.getInt(cursor.getColumnIndex(DBHelper.Key_ID)))) == null) {
                return null;
            }
            thumbnail.setRotationAngle(getRotationAngle(pathname));
            return thumbnail;
        } finally {
            cursor.close();
        }
    }

    @Nullable
    private EncodedImage getThumbnail(ResizeOptions resizeOptions, int imageId) throws IOException {
        int thumbnailKind = getThumbnailKind(resizeOptions);
        if (thumbnailKind == 0) {
            return null;
        }
        Cursor thumbnailCursor = null;
        try {
            thumbnailCursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(this.mContentResolver, (long) imageId, thumbnailKind, THUMBNAIL_PROJECTION);
            if (thumbnailCursor == null) {
                return null;
            }
            thumbnailCursor.moveToFirst();
            if (thumbnailCursor.getCount() > 0) {
                String thumbnailUri = thumbnailCursor.getString(thumbnailCursor.getColumnIndex("_data"));
                if (new File(thumbnailUri).exists()) {
                    EncodedImage encodedImage = getEncodedImage(new FileInputStream(thumbnailUri), getLength(thumbnailUri));
                    if (thumbnailCursor != null) {
                        thumbnailCursor.close();
                    }
                    return encodedImage;
                }
            }
            if (thumbnailCursor != null) {
                thumbnailCursor.close();
            }
            return null;
        } finally {
            if (thumbnailCursor != null) {
                thumbnailCursor.close();
            }
        }
    }

    private static int getThumbnailKind(ResizeOptions resizeOptions) {
        if (ThumbnailSizeChecker.isImageBigEnough(MICRO_THUMBNAIL_DIMENSIONS.width(), MICRO_THUMBNAIL_DIMENSIONS.height(), resizeOptions)) {
            return 3;
        }
        if (ThumbnailSizeChecker.isImageBigEnough(MINI_THUMBNAIL_DIMENSIONS.width(), MINI_THUMBNAIL_DIMENSIONS.height(), resizeOptions)) {
            return 1;
        }
        return 0;
    }

    private static int getLength(String pathname) {
        if (pathname == null) {
            return -1;
        }
        return (int) new File(pathname).length();
    }

    @Override // com.facebook.imagepipeline.producers.LocalFetchProducer
    protected String getProducerName() {
        return PRODUCER_NAME;
    }

    private static int getRotationAngle(String pathname) {
        if (pathname != null) {
            try {
                return JfifUtil.getAutoRotateAngleFromOrientation(new ExifInterface(pathname).getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 1));
            } catch (IOException ioe) {
                FLog.e(TAG, ioe, "Unable to retrieve thumbnail rotation for %s", pathname);
            }
        }
        return 0;
    }
}
