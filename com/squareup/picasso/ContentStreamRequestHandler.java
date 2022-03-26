package com.squareup.picasso;

import android.content.Context;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestHandler;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import okio.Okio;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class ContentStreamRequestHandler extends RequestHandler {
    final Context context;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContentStreamRequestHandler(Context context) {
        this.context = context;
    }

    @Override // com.squareup.picasso.RequestHandler
    public boolean canHandleRequest(Request data) {
        return "content".equals(data.uri.getScheme());
    }

    @Override // com.squareup.picasso.RequestHandler
    public RequestHandler.Result load(Request request, int networkPolicy) throws IOException {
        return new RequestHandler.Result(Okio.source(getInputStream(request)), Picasso.LoadedFrom.DISK);
    }

    InputStream getInputStream(Request request) throws FileNotFoundException {
        return this.context.getContentResolver().openInputStream(request.uri);
    }
}
