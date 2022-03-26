package com.google.firebase.crashlytics.internal.send;

import android.content.Context;
import com.google.android.datatransport.Encoding;
import com.google.android.datatransport.Event;
import com.google.android.datatransport.Transformer;
import com.google.android.datatransport.Transport;
import com.google.android.datatransport.TransportScheduleCallback;
import com.google.android.datatransport.cct.CCTDestination;
import com.google.android.datatransport.runtime.TransportRuntime;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.crashlytics.internal.common.CrashlyticsReportWithSessionId;
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
import com.google.firebase.crashlytics.internal.model.serialization.CrashlyticsReportJsonTransform;
/* loaded from: classes3.dex */
public class DataTransportCrashlyticsReportSender {
    private static final String CRASHLYTICS_TRANSPORT_NAME;
    private final Transport<CrashlyticsReport> transport;
    private final Transformer<CrashlyticsReport, byte[]> transportTransform;
    private static final CrashlyticsReportJsonTransform TRANSFORM = new CrashlyticsReportJsonTransform();
    private static final String CRASHLYTICS_ENDPOINT = mergeStrings("hts/cahyiseot-agolai.o/1frlglgc/aclg", "tp:/rsltcrprsp.ogepscmv/ieo/eaybtho");
    private static final String CRASHLYTICS_API_KEY = mergeStrings("AzSBpY4F0rHiHFdinTvM", "IayrSTFL9eJ69YeSUO2");
    private static final Transformer<CrashlyticsReport, byte[]> DEFAULT_TRANSFORM = $$Lambda$DataTransportCrashlyticsReportSender$JltQxiH58gc27V_xiyanKrAKU.INSTANCE;

    public static DataTransportCrashlyticsReportSender create(Context context) {
        TransportRuntime.initialize(context);
        return new DataTransportCrashlyticsReportSender(TransportRuntime.getInstance().newFactory(new CCTDestination(CRASHLYTICS_ENDPOINT, CRASHLYTICS_API_KEY)).getTransport(CRASHLYTICS_TRANSPORT_NAME, CrashlyticsReport.class, Encoding.of("json"), DEFAULT_TRANSFORM), DEFAULT_TRANSFORM);
    }

    DataTransportCrashlyticsReportSender(Transport<CrashlyticsReport> transport, Transformer<CrashlyticsReport, byte[]> transportTransform) {
        this.transport = transport;
        this.transportTransform = transportTransform;
    }

    public Task<CrashlyticsReportWithSessionId> sendReport(CrashlyticsReportWithSessionId reportWithSessionId) {
        CrashlyticsReport report = reportWithSessionId.getReport();
        TaskCompletionSource<CrashlyticsReportWithSessionId> tcs = new TaskCompletionSource<>();
        this.transport.schedule(Event.ofUrgent(report), new TransportScheduleCallback(reportWithSessionId) { // from class: com.google.firebase.crashlytics.internal.send.-$$Lambda$DataTransportCrashlyticsReportSender$STxxV2AgBuv04UYSWiLJKKG3YWM
            private final /* synthetic */ CrashlyticsReportWithSessionId f$1;

            {
                this.f$1 = r2;
            }

            @Override // com.google.android.datatransport.TransportScheduleCallback
            public final void onSchedule(Exception exc) {
                DataTransportCrashlyticsReportSender.lambda$sendReport$1(TaskCompletionSource.this, this.f$1, exc);
            }
        });
        return tcs.getTask();
    }

    public static /* synthetic */ void lambda$sendReport$1(TaskCompletionSource tcs, CrashlyticsReportWithSessionId reportWithSessionId, Exception error) {
        if (error != null) {
            tcs.trySetException(error);
        } else {
            tcs.trySetResult(reportWithSessionId);
        }
    }

    private static String mergeStrings(String part1, String part2) {
        int sizeDiff = part1.length() - part2.length();
        if (sizeDiff < 0 || sizeDiff > 1) {
            throw new IllegalArgumentException("Invalid input received");
        }
        StringBuilder url = new StringBuilder(part1.length() + part2.length());
        for (int i = 0; i < part1.length(); i++) {
            url.append(part1.charAt(i));
            if (part2.length() > i) {
                url.append(part2.charAt(i));
            }
        }
        return url.toString();
    }
}
