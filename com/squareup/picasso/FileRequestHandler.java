package com.squareup.picasso;

import android.content.Context;
import android.net.Uri;
import androidx.exifinterface.media.ExifInterface;
import com.facebook.common.util.UriUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestHandler;
import java.io.IOException;
import okio.Okio;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class FileRequestHandler extends ContentStreamRequestHandler {
    /* JADX INFO: Access modifiers changed from: package-private */
    public FileRequestHandler(Context context) {
        super(context);
    }

    @Override // com.squareup.picasso.ContentStreamRequestHandler, com.squareup.picasso.RequestHandler
    public boolean canHandleRequest(Request data) {
        return UriUtil.LOCAL_FILE_SCHEME.equals(data.uri.getScheme());
    }

    @Override // com.squareup.picasso.ContentStreamRequestHandler, com.squareup.picasso.RequestHandler
    public RequestHandler.Result load(Request request, int networkPolicy) throws IOException {
        return new RequestHandler.Result(null, Okio.source(getInputStream(request)), Picasso.LoadedFrom.DISK, getFileExifRotation(request.uri));
    }

    static int getFileExifRotation(Uri uri) throws IOException {
        return new ExifInterface(uri.getPath()).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
    }
}
