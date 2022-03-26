package com.google.firebase.crashlytics.internal.send;

import com.google.android.datatransport.Transformer;
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
import java.nio.charset.Charset;
/* compiled from: lambda */
/* renamed from: com.google.firebase.crashlytics.internal.send.-$$Lambda$DataTransportCrashlyticsReportSender$J-l-tQxiH58gc27V_xiyanKrAKU  reason: invalid class name */
/* loaded from: classes3.dex */
public final /* synthetic */ class $$Lambda$DataTransportCrashlyticsReportSender$JltQxiH58gc27V_xiyanKrAKU implements Transformer {
    public static final /* synthetic */ $$Lambda$DataTransportCrashlyticsReportSender$JltQxiH58gc27V_xiyanKrAKU INSTANCE = new $$Lambda$DataTransportCrashlyticsReportSender$JltQxiH58gc27V_xiyanKrAKU();

    private /* synthetic */ $$Lambda$DataTransportCrashlyticsReportSender$JltQxiH58gc27V_xiyanKrAKU() {
    }

    @Override // com.google.android.datatransport.Transformer
    public final Object apply(Object obj) {
        return DataTransportCrashlyticsReportSender.TRANSFORM.reportToJson((CrashlyticsReport) obj).getBytes(Charset.forName("UTF-8"));
    }
}
