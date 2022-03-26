package com.facebook.imagepipeline.producers;

import android.net.Uri;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.producers.NetworkFetcher;
import com.google.common.net.HttpHeaders;
import com.sec.biometric.license.SecBiometricLicenseManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
/* loaded from: classes.dex */
public class HttpUrlConnectionNetworkFetcher extends BaseNetworkFetcher<FetchState> {
    public static final int HTTP_DEFAULT_TIMEOUT = 30000;
    public static final int HTTP_PERMANENT_REDIRECT = 308;
    public static final int HTTP_TEMPORARY_REDIRECT = 307;
    private static final int MAX_REDIRECTS = 5;
    private static final int NUM_NETWORK_THREADS = 3;
    private final ExecutorService mExecutorService;
    private int mHttpConnectionTimeout;

    public HttpUrlConnectionNetworkFetcher() {
        this(Executors.newFixedThreadPool(3));
    }

    public HttpUrlConnectionNetworkFetcher(int httpConnectionTimeout) {
        this(Executors.newFixedThreadPool(3));
        this.mHttpConnectionTimeout = httpConnectionTimeout;
    }

    HttpUrlConnectionNetworkFetcher(ExecutorService executorService) {
        this.mExecutorService = executorService;
    }

    @Override // com.facebook.imagepipeline.producers.NetworkFetcher
    public FetchState createFetchState(Consumer<EncodedImage> consumer, ProducerContext context) {
        return new FetchState(consumer, context);
    }

    @Override // com.facebook.imagepipeline.producers.NetworkFetcher
    public void fetch(final FetchState fetchState, final NetworkFetcher.Callback callback) {
        final Future<?> future = this.mExecutorService.submit(new Runnable() { // from class: com.facebook.imagepipeline.producers.HttpUrlConnectionNetworkFetcher.1
            @Override // java.lang.Runnable
            public void run() {
                HttpUrlConnectionNetworkFetcher.this.fetchSync(fetchState, callback);
            }
        });
        fetchState.getContext().addCallbacks(new BaseProducerContextCallbacks() { // from class: com.facebook.imagepipeline.producers.HttpUrlConnectionNetworkFetcher.2
            @Override // com.facebook.imagepipeline.producers.BaseProducerContextCallbacks, com.facebook.imagepipeline.producers.ProducerContextCallbacks
            public void onCancellationRequested() {
                if (future.cancel(false)) {
                    callback.onCancellation();
                }
            }
        });
    }

    void fetchSync(FetchState fetchState, NetworkFetcher.Callback callback) {
        InputStream is;
        HttpURLConnection connection;
        try {
            connection = null;
            is = null;
            try {
                connection = downloadFrom(fetchState.getUri(), 5);
                if (connection != null) {
                    is = connection.getInputStream();
                    callback.onResponse(is, -1);
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
                if (connection == null) {
                    return;
                }
            } catch (IOException e2) {
                callback.onFailure(e2);
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e3) {
                    }
                }
                if (connection == null) {
                    return;
                }
            }
            connection.disconnect();
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e4) {
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
            throw th;
        }
    }

    private HttpURLConnection downloadFrom(Uri uri, int maxRedirects) throws IOException {
        String message;
        HttpURLConnection connection = openConnectionTo(uri);
        connection.setConnectTimeout(this.mHttpConnectionTimeout);
        int responseCode = connection.getResponseCode();
        if (isHttpSuccess(responseCode)) {
            return connection;
        }
        if (isHttpRedirect(responseCode)) {
            String nextUriString = connection.getHeaderField(HttpHeaders.LOCATION);
            connection.disconnect();
            Uri nextUri = nextUriString == null ? null : Uri.parse(nextUriString);
            String originalScheme = uri.getScheme();
            if (maxRedirects > 0 && nextUri != null && !nextUri.getScheme().equals(originalScheme)) {
                return downloadFrom(nextUri, maxRedirects - 1);
            }
            if (maxRedirects == 0) {
                message = error("URL %s follows too many redirects", uri.toString());
            } else {
                message = error("URL %s returned %d without a valid redirect", uri.toString(), Integer.valueOf(responseCode));
            }
            throw new IOException(message);
        }
        connection.disconnect();
        throw new IOException(String.format("Image URL %s returned HTTP code %d", uri.toString(), Integer.valueOf(responseCode)));
    }

    static HttpURLConnection openConnectionTo(Uri uri) throws IOException {
        return (HttpURLConnection) UriUtil.uriToUrl(uri).openConnection();
    }

    private static boolean isHttpSuccess(int responseCode) {
        return responseCode >= 200 && responseCode < 300;
    }

    private static boolean isHttpRedirect(int responseCode) {
        if (responseCode == 307 || responseCode == 308) {
            return true;
        }
        switch (responseCode) {
            case GenericDraweeHierarchyBuilder.DEFAULT_FADE_DURATION:
            case SecBiometricLicenseManager.ERROR_INTERNAL:
            case 302:
            case 303:
                return true;
            default:
                return false;
        }
    }

    private static String error(String format, Object... args) {
        return String.format(Locale.getDefault(), format, args);
    }
}
