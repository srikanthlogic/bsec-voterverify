package com.facebook.common.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class UriUtil {
    public static final String DATA_SCHEME = "data";
    public static final String HTTPS_SCHEME = "https";
    public static final String HTTP_SCHEME = "http";
    public static final String LOCAL_ASSET_SCHEME = "asset";
    private static final Uri LOCAL_CONTACT_IMAGE_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "display_photo");
    public static final String LOCAL_CONTENT_SCHEME = "content";
    public static final String LOCAL_FILE_SCHEME = "file";
    public static final String LOCAL_RESOURCE_SCHEME = "res";
    public static final String QUALIFIED_RESOURCE_SCHEME = "android.resource";

    @Nullable
    public static URL uriToUrl(@Nullable Uri uri) {
        if (uri == null) {
            return null;
        }
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isNetworkUri(@Nullable Uri uri) {
        String scheme = getSchemeOrNull(uri);
        return HTTPS_SCHEME.equals(scheme) || HTTP_SCHEME.equals(scheme);
    }

    public static boolean isLocalFileUri(@Nullable Uri uri) {
        return LOCAL_FILE_SCHEME.equals(getSchemeOrNull(uri));
    }

    public static boolean isLocalContentUri(@Nullable Uri uri) {
        return "content".equals(getSchemeOrNull(uri));
    }

    public static boolean isLocalContactUri(Uri uri) {
        return isLocalContentUri(uri) && "com.android.contacts".equals(uri.getAuthority()) && !uri.getPath().startsWith(LOCAL_CONTACT_IMAGE_URI.getPath());
    }

    public static boolean isLocalCameraUri(Uri uri) {
        String uriString = uri.toString();
        return uriString.startsWith(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString()) || uriString.startsWith(MediaStore.Images.Media.INTERNAL_CONTENT_URI.toString());
    }

    public static boolean isLocalAssetUri(@Nullable Uri uri) {
        return LOCAL_ASSET_SCHEME.equals(getSchemeOrNull(uri));
    }

    public static boolean isLocalResourceUri(@Nullable Uri uri) {
        return LOCAL_RESOURCE_SCHEME.equals(getSchemeOrNull(uri));
    }

    public static boolean isQualifiedResourceUri(@Nullable Uri uri) {
        return QUALIFIED_RESOURCE_SCHEME.equals(getSchemeOrNull(uri));
    }

    public static boolean isDataUri(@Nullable Uri uri) {
        return DATA_SCHEME.equals(getSchemeOrNull(uri));
    }

    @Nullable
    public static String getSchemeOrNull(@Nullable Uri uri) {
        if (uri == null) {
            return null;
        }
        return uri.getScheme();
    }

    @Nullable
    public static Uri parseUriOrNull(@Nullable String uriAsString) {
        if (uriAsString != null) {
            return Uri.parse(uriAsString);
        }
        return null;
    }

    @Nullable
    public static String getRealPathFromUri(ContentResolver contentResolver, Uri srcUri) {
        int idx;
        String result = null;
        if (isLocalContentUri(srcUri)) {
            Cursor cursor = null;
            try {
                cursor = contentResolver.query(srcUri, null, null, null, null);
                if (!(cursor == null || !cursor.moveToFirst() || (idx = cursor.getColumnIndex("_data")) == -1)) {
                    result = cursor.getString(idx);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if (isLocalFileUri(srcUri)) {
            return srcUri.getPath();
        } else {
            return null;
        }
    }

    public static Uri getUriForFile(File file) {
        return Uri.fromFile(file);
    }

    public static Uri getUriForResourceId(int resourceId) {
        return new Uri.Builder().scheme(LOCAL_RESOURCE_SCHEME).path(String.valueOf(resourceId)).build();
    }

    public static Uri getUriForQualifiedResource(String packageName, int resourceId) {
        return new Uri.Builder().scheme(QUALIFIED_RESOURCE_SCHEME).authority(packageName).path(String.valueOf(resourceId)).build();
    }
}
