package com.google.android.datatransport.cct.internal;

import android.util.JsonReader;
import android.util.JsonToken;
import java.io.IOException;
import java.io.Reader;
/* loaded from: classes.dex */
public abstract class LogResponse {
    private static final String LOG_TAG;

    public abstract long getNextRequestWaitMillis();

    static LogResponse create(long nextRequestWaitMillis) {
        return new AutoValue_LogResponse(nextRequestWaitMillis);
    }

    public static LogResponse fromJson(Reader reader) throws IOException {
        JsonReader jsonReader = new JsonReader(reader);
        try {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                if (!jsonReader.nextName().equals("nextRequestWaitMillis")) {
                    jsonReader.skipValue();
                } else if (jsonReader.peek() == JsonToken.STRING) {
                    return create(Long.parseLong(jsonReader.nextString()));
                } else {
                    return create(jsonReader.nextLong());
                }
            }
            throw new IOException("Response is missing nextRequestWaitMillis field.");
        } finally {
            jsonReader.close();
        }
    }
}
