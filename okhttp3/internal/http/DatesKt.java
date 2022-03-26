package okhttp3.internal.http;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.Util;
/* compiled from: dates.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000+\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\n\u001a\u000e\u0010\f\u001a\u0004\u0018\u00010\r*\u00020\u0005H\u0000\u001a\f\u0010\u000e\u001a\u00020\u0005*\u00020\rH\u0000\"\u0018\u0010\u0000\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0003\"\u0016\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00050\u0001X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0006\"\u000e\u0010\u0007\u001a\u00020\bX\u0080T¢\u0006\u0002\n\u0000\"\u0010\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u000b¨\u0006\u000f"}, d2 = {"BROWSER_COMPATIBLE_DATE_FORMATS", "", "Ljava/text/DateFormat;", "[Ljava/text/DateFormat;", "BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS", "", "[Ljava/lang/String;", "MAX_DATE", "", "STANDARD_DATE_FORMAT", "okhttp3/internal/http/DatesKt$STANDARD_DATE_FORMAT$1", "Lokhttp3/internal/http/DatesKt$STANDARD_DATE_FORMAT$1;", "toHttpDateOrNull", "Ljava/util/Date;", "toHttpDateString", "okhttp"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class DatesKt {
    public static final long MAX_DATE = 253402300799999L;
    private static final DatesKt$STANDARD_DATE_FORMAT$1 STANDARD_DATE_FORMAT = new DatesKt$STANDARD_DATE_FORMAT$1();
    private static final String[] BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS = {"EEE, dd MMM yyyy HH:mm:ss zzz", "EEEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy", "EEE, dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MMM-yyyy HH-mm-ss z", "EEE, dd MMM yy HH:mm:ss z", "EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z", "EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z", "EEE dd MMM yy HH:mm:ss z", "EEE,dd-MMM-yy HH:mm:ss z", "EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z", "EEE MMM d yyyy HH:mm:ss z"};
    private static final DateFormat[] BROWSER_COMPATIBLE_DATE_FORMATS = new DateFormat[BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS.length];

    /* JADX INFO: Multiple debug info for r3v5 int: [D('result' java.lang.Object), D('i' int)] */
    public static final Date toHttpDateOrNull(String $this$toHttpDateOrNull) {
        Throwable th;
        Intrinsics.checkParameterIsNotNull($this$toHttpDateOrNull, "$this$toHttpDateOrNull");
        if ($this$toHttpDateOrNull.length() == 0) {
            return null;
        }
        ParsePosition position = new ParsePosition(0);
        Date parse = STANDARD_DATE_FORMAT.get().parse($this$toHttpDateOrNull, position);
        if (position.getIndex() == $this$toHttpDateOrNull.length()) {
            return parse;
        }
        synchronized (BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS) {
            try {
                int length = BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS.length;
                for (int i = 0; i < length; i++) {
                    try {
                        SimpleDateFormat format = BROWSER_COMPATIBLE_DATE_FORMATS[i];
                        if (format == null) {
                            SimpleDateFormat $this$apply = new SimpleDateFormat(BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS[i], Locale.US);
                            $this$apply.setTimeZone(Util.UTC);
                            format = $this$apply;
                            BROWSER_COMPATIBLE_DATE_FORMATS[i] = format;
                        }
                        position.setIndex(0);
                        Date parse2 = format.parse($this$toHttpDateOrNull, position);
                        if (position.getIndex() != 0) {
                            return parse2;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                }
                Unit unit = Unit.INSTANCE;
                return null;
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    public static final String toHttpDateString(Date $this$toHttpDateString) {
        Intrinsics.checkParameterIsNotNull($this$toHttpDateString, "$this$toHttpDateString");
        String format = STANDARD_DATE_FORMAT.get().format($this$toHttpDateString);
        Intrinsics.checkExpressionValueIsNotNull(format, "STANDARD_DATE_FORMAT.get().format(this)");
        return format;
    }
}
