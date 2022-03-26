package com.google.firebase.crashlytics.internal.network;

import com.google.firebase.crashlytics.internal.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
/* loaded from: classes3.dex */
public class HttpGetRequest {
    private static final int DEFAULT_TIMEOUT_MS = 10000;
    private static final String METHOD_GET = "GET";
    private static final int READ_BUFFER_SIZE = 8192;
    private final Map<String, String> headers = new HashMap();
    private final Map<String, String> queryParams;
    private final String url;

    public HttpGetRequest(String url, Map<String, String> queryParams) {
        this.url = url;
        this.queryParams = queryParams;
    }

    public HttpGetRequest header(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    public HttpGetRequest header(Map.Entry<String, String> entry) {
        return header(entry.getKey(), entry.getValue());
    }

    public HttpResponse execute() throws IOException {
        InputStream stream = null;
        HttpsURLConnection connection = null;
        String body = null;
        try {
            String urlWithParams = createUrlWithParams(this.url, this.queryParams);
            Logger logger = Logger.getLogger();
            logger.v("GET Request URL: " + urlWithParams);
            connection = (HttpsURLConnection) new URL(urlWithParams).openConnection();
            connection.setReadTimeout(DEFAULT_TIMEOUT_MS);
            connection.setConnectTimeout(DEFAULT_TIMEOUT_MS);
            connection.setRequestMethod(METHOD_GET);
            for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
            connection.connect();
            int responseCode = connection.getResponseCode();
            stream = connection.getInputStream();
            if (stream != null) {
                body = readStream(stream);
            }
            return new HttpResponse(responseCode, body);
        } finally {
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String createUrlWithParams(String url, Map<String, String> queryParams) {
        String queryParamsString = createParamsString(queryParams);
        if (queryParamsString.isEmpty()) {
            return url;
        }
        if (url.contains("?")) {
            if (!url.endsWith("&")) {
                queryParamsString = "&" + queryParamsString;
            }
            return url + queryParamsString;
        }
        return url + "?" + queryParamsString;
    }

    private String createParamsString(Map<String, String> queryParams) {
        String str;
        StringBuilder paramsString = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = queryParams.entrySet().iterator();
        Map.Entry<String, String> entry = iterator.next();
        StringBuilder sb = new StringBuilder();
        sb.append(entry.getKey());
        sb.append("=");
        sb.append(entry.getValue() != null ? entry.getValue() : "");
        paramsString.append(sb.toString());
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry2 = iterator.next();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("&");
            sb2.append(entry2.getKey());
            sb2.append("=");
            if (entry2.getValue() != null) {
                str = entry2.getValue();
            } else {
                str = "";
            }
            sb2.append(str);
            paramsString.append(sb2.toString());
        }
        return paramsString.toString();
    }

    private String readStream(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        char[] charBuffer = new char[8192];
        StringBuilder result = new StringBuilder();
        while (true) {
            int charsRead = reader.read(charBuffer);
            if (charsRead == -1) {
                return result.toString();
            }
            result.append(charBuffer, 0, charsRead);
        }
    }
}
