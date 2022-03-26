package com.squareup.picasso;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import com.facebook.imagepipeline.memory.BitmapCounterConfig;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestHandler;
import java.io.IOException;
import okio.Okio;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class MediaStoreRequestHandler extends ContentStreamRequestHandler {
    private static final String[] CONTENT_ORIENTATION = {"orientation"};

    /* JADX INFO: Access modifiers changed from: package-private */
    public MediaStoreRequestHandler(Context context) {
        super(context);
    }

    @Override // com.squareup.picasso.ContentStreamRequestHandler, com.squareup.picasso.RequestHandler
    public boolean canHandleRequest(Request data) {
        Uri uri = data.uri;
        return "content".equals(uri.getScheme()) && "media".equals(uri.getAuthority());
    }

    @Override // com.squareup.picasso.ContentStreamRequestHandler, com.squareup.picasso.RequestHandler
    public RequestHandler.Result load(Request request, int networkPolicy) throws IOException {
        Bitmap bitmap;
        ContentResolver contentResolver = this.context.getContentResolver();
        int exifOrientation = getExifOrientation(contentResolver, request.uri);
        String mimeType = contentResolver.getType(request.uri);
        int kind = 1;
        boolean isVideo = mimeType != null && mimeType.startsWith("video/");
        if (request.hasSize()) {
            PicassoKind picassoKind = getPicassoKind(request.targetWidth, request.targetHeight);
            if (!isVideo && picassoKind == PicassoKind.FULL) {
                return new RequestHandler.Result(null, Okio.source(getInputStream(request)), Picasso.LoadedFrom.DISK, exifOrientation);
            }
            long id = ContentUris.parseId(request.uri);
            BitmapFactory.Options options = createBitmapOptions(request);
            options.inJustDecodeBounds = true;
            calculateInSampleSize(request.targetWidth, request.targetHeight, picassoKind.width, picassoKind.height, options, request);
            if (isVideo) {
                if (picassoKind != PicassoKind.FULL) {
                    kind = picassoKind.androidKind;
                }
                bitmap = MediaStore.Video.Thumbnails.getThumbnail(contentResolver, id, kind, options);
            } else {
                bitmap = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, id, picassoKind.androidKind, options);
            }
            if (bitmap != null) {
                return new RequestHandler.Result(bitmap, null, Picasso.LoadedFrom.DISK, exifOrientation);
            }
        }
        return new RequestHandler.Result(null, Okio.source(getInputStream(request)), Picasso.LoadedFrom.DISK, exifOrientation);
    }

    static PicassoKind getPicassoKind(int targetWidth, int targetHeight) {
        if (targetWidth <= PicassoKind.MICRO.width && targetHeight <= PicassoKind.MICRO.height) {
            return PicassoKind.MICRO;
        }
        if (targetWidth > PicassoKind.MINI.width || targetHeight > PicassoKind.MINI.height) {
            return PicassoKind.FULL;
        }
        return PicassoKind.MINI;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0024, code lost:
        r0.close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    static int getExifOrientation(ContentResolver contentResolver, Uri uri) {
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(uri, CONTENT_ORIENTATION, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int i = cursor.getInt(0);
                if (cursor != null) {
                    cursor.close();
                }
                return i;
            }
            return 0;
        } catch (RuntimeException e) {
            if (cursor != null) {
                cursor.close();
            }
            return 0;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public enum PicassoKind {
        MICRO(3, 96, 96),
        MINI(1, 512, BitmapCounterConfig.DEFAULT_MAX_BITMAP_COUNT),
        FULL(2, -1, -1);
        
        final int androidKind;
        final int height;
        final int width;

        PicassoKind(int androidKind, int width, int height) {
            this.androidKind = androidKind;
            this.width = width;
            this.height = height;
        }
    }
}
