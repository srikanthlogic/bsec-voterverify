package okhttp3.internal.http;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import kotlin.Metadata;
import okhttp3.internal.Util;
/* compiled from: dates.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H\u0014¨\u0006\u0004"}, d2 = {"okhttp3/internal/http/DatesKt$STANDARD_DATE_FORMAT$1", "Ljava/lang/ThreadLocal;", "Ljava/text/DateFormat;", "initialValue", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class DatesKt$STANDARD_DATE_FORMAT$1 extends ThreadLocal<DateFormat> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // java.lang.ThreadLocal
    public DateFormat initialValue() {
        SimpleDateFormat $this$apply = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        $this$apply.setLenient(false);
        $this$apply.setTimeZone(Util.UTC);
        return $this$apply;
    }
}
