package okhttp3;

import java.io.File;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
/* compiled from: RequestBody.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\n\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016¨\u0006\n"}, d2 = {"okhttp3/RequestBody$Companion$asRequestBody$1", "Lokhttp3/RequestBody;", "contentLength", "", "contentType", "Lokhttp3/MediaType;", "writeTo", "", "sink", "Lokio/BufferedSink;", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class RequestBody$Companion$asRequestBody$1 extends RequestBody {
    final /* synthetic */ MediaType $contentType;
    final /* synthetic */ File $this_asRequestBody;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RequestBody$Companion$asRequestBody$1(File $receiver, MediaType $captured_local_variable$1) {
        this.$this_asRequestBody = $receiver;
        this.$contentType = $captured_local_variable$1;
    }

    @Override // okhttp3.RequestBody
    public MediaType contentType() {
        return this.$contentType;
    }

    @Override // okhttp3.RequestBody
    public long contentLength() {
        return this.$this_asRequestBody.length();
    }

    @Override // okhttp3.RequestBody
    public void writeTo(BufferedSink sink) {
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        Source source = Okio.source(this.$this_asRequestBody);
        th = null;
        try {
            sink.writeAll(source);
        } finally {
            try {
                throw th;
            } finally {
            }
        }
    }
}
