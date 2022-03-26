package com.facebook.imagepipeline.producers;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import com.example.aadhaarfpoffline.tatvik.database.DBHelper;
import com.facebook.common.memory.PooledByteBufferFactory;
import com.facebook.common.util.UriUtil;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.request.ImageRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class LocalContentUriFetchProducer extends LocalFetchProducer {
    public static final String PRODUCER_NAME = "LocalContentUriFetchProducer";
    private static final String[] PROJECTION = {DBHelper.Key_ID, "_data"};
    private final ContentResolver mContentResolver;

    public LocalContentUriFetchProducer(Executor executor, PooledByteBufferFactory pooledByteBufferFactory, ContentResolver contentResolver) {
        super(executor, pooledByteBufferFactory);
        this.mContentResolver = contentResolver;
    }

    @Override // com.facebook.imagepipeline.producers.LocalFetchProducer
    protected EncodedImage getEncodedImage(ImageRequest imageRequest) throws IOException {
        EncodedImage cameraImage;
        InputStream inputStream;
        Uri uri = imageRequest.getSourceUri();
        if (UriUtil.isLocalContactUri(uri)) {
            if (uri.toString().endsWith("/photo")) {
                inputStream = this.mContentResolver.openInputStream(uri);
            } else if (uri.toString().endsWith("/display_photo")) {
                try {
                    inputStream = this.mContentResolver.openAssetFileDescriptor(uri, "r").createInputStream();
                } catch (IOException e) {
                    throw new IOException("Contact photo does not exist: " + uri);
                }
            } else {
                inputStream = ContactsContract.Contacts.openContactPhotoInputStream(this.mContentResolver, uri);
                if (inputStream == null) {
                    throw new IOException("Contact photo does not exist: " + uri);
                }
            }
            return getEncodedImage(inputStream, -1);
        } else if (!UriUtil.isLocalCameraUri(uri) || (cameraImage = getCameraImage(uri)) == null) {
            return getEncodedImage(this.mContentResolver.openInputStream(uri), -1);
        } else {
            return cameraImage;
        }
    }

    @Nullable
    private EncodedImage getCameraImage(Uri uri) throws IOException {
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
            if (pathname != null) {
                return getEncodedImage(new FileInputStream(pathname), getLength(pathname));
            }
            return null;
        } finally {
            cursor.close();
        }
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
}
