package com.squareup.picasso;

import java.io.IOException;
import okhttp3.Request;
import okhttp3.Response;
/* loaded from: classes3.dex */
public interface Downloader {
    Response load(Request request) throws IOException;

    void shutdown();
}
