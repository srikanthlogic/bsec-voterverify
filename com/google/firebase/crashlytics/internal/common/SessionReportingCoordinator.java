package com.google.firebase.crashlytics.internal.common;

import android.app.ApplicationExitInfo;
import android.content.Context;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.crashlytics.internal.Logger;
import com.google.firebase.crashlytics.internal.log.LogFileManager;
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
import com.google.firebase.crashlytics.internal.model.ImmutableList;
import com.google.firebase.crashlytics.internal.persistence.CrashlyticsReportPersistence;
import com.google.firebase.crashlytics.internal.persistence.FileStore;
import com.google.firebase.crashlytics.internal.send.DataTransportCrashlyticsReportSender;
import com.google.firebase.crashlytics.internal.settings.SettingsDataProvider;
import com.google.firebase.crashlytics.internal.stacktrace.StackTraceTrimmingStrategy;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
/* loaded from: classes3.dex */
public class SessionReportingCoordinator implements CrashlyticsLifecycleEvents {
    private static final int EVENT_THREAD_IMPORTANCE;
    private static final String EVENT_TYPE_CRASH;
    private static final String EVENT_TYPE_LOGGED;
    private static final int MAX_CHAINED_EXCEPTION_DEPTH;
    private final CrashlyticsReportDataCapture dataCapture;
    private final LogFileManager logFileManager;
    private final UserMetadata reportMetadata;
    private final CrashlyticsReportPersistence reportPersistence;
    private final DataTransportCrashlyticsReportSender reportsSender;

    public static SessionReportingCoordinator create(Context context, IdManager idManager, FileStore fileStore, AppData appData, LogFileManager logFileManager, UserMetadata userMetadata, StackTraceTrimmingStrategy stackTraceTrimmingStrategy, SettingsDataProvider settingsProvider) {
        return new SessionReportingCoordinator(new CrashlyticsReportDataCapture(context, idManager, appData, stackTraceTrimmingStrategy), new CrashlyticsReportPersistence(new File(fileStore.getFilesDirPath()), settingsProvider), DataTransportCrashlyticsReportSender.create(context), logFileManager, userMetadata);
    }

    SessionReportingCoordinator(CrashlyticsReportDataCapture dataCapture, CrashlyticsReportPersistence reportPersistence, DataTransportCrashlyticsReportSender reportsSender, LogFileManager logFileManager, UserMetadata reportMetadata) {
        this.dataCapture = dataCapture;
        this.reportPersistence = reportPersistence;
        this.reportsSender = reportsSender;
        this.logFileManager = logFileManager;
        this.reportMetadata = reportMetadata;
    }

    @Override // com.google.firebase.crashlytics.internal.common.CrashlyticsLifecycleEvents
    public void onBeginSession(String sessionId, long timestampSeconds) {
        this.reportPersistence.persistReport(this.dataCapture.captureReportData(sessionId, timestampSeconds));
    }

    @Override // com.google.firebase.crashlytics.internal.common.CrashlyticsLifecycleEvents
    public void onLog(long timestamp, String log) {
        this.logFileManager.writeToLog(timestamp, log);
    }

    @Override // com.google.firebase.crashlytics.internal.common.CrashlyticsLifecycleEvents
    public void onCustomKey(String key, String value) {
        this.reportMetadata.setCustomKey(key, value);
    }

    @Override // com.google.firebase.crashlytics.internal.common.CrashlyticsLifecycleEvents
    public void onUserId(String userId) {
        this.reportMetadata.setUserId(userId);
    }

    public void persistFatalEvent(Throwable event, Thread thread, String sessionId, long timestamp) {
        Logger logger = Logger.getLogger();
        logger.v("Persisting fatal event for session " + sessionId);
        persistEvent(event, thread, sessionId, "crash", timestamp, true);
    }

    public void persistNonFatalEvent(Throwable event, Thread thread, String sessionId, long timestamp) {
        Logger logger = Logger.getLogger();
        logger.v("Persisting non-fatal event for session " + sessionId);
        persistEvent(event, thread, sessionId, EVENT_TYPE_LOGGED, timestamp, false);
    }

    public void persistAppExitInfoEvent(String sessionId, ApplicationExitInfo applicationExitInfo, LogFileManager logFileManagerForSession, UserMetadata userMetadataForSession) {
        if (applicationExitInfo.getTimestamp() >= this.reportPersistence.getStartTimestampMillis(sessionId) && applicationExitInfo.getReason() == 6) {
            CrashlyticsReport.Session.Event capturedEvent = this.dataCapture.captureAnrEventData(convertApplicationExitInfo(applicationExitInfo));
            Logger logger = Logger.getLogger();
            logger.d("Persisting anr for session " + sessionId);
            this.reportPersistence.persistEvent(addLogsAndCustomKeysToEvent(capturedEvent, logFileManagerForSession, userMetadataForSession), sessionId, true);
        }
    }

    public void finalizeSessionWithNativeEvent(String sessionId, List<NativeSessionFile> nativeSessionFiles) {
        ArrayList<CrashlyticsReport.FilesPayload.File> nativeFiles = new ArrayList<>();
        for (NativeSessionFile nativeSessionFile : nativeSessionFiles) {
            CrashlyticsReport.FilesPayload.File filePayload = nativeSessionFile.asFilePayload();
            if (filePayload != null) {
                nativeFiles.add(filePayload);
            }
        }
        this.reportPersistence.finalizeSessionWithNativeEvent(sessionId, CrashlyticsReport.FilesPayload.builder().setFiles(ImmutableList.from(nativeFiles)).build());
    }

    public void persistUserId(String sessionId) {
        String userId = this.reportMetadata.getUserId();
        if (userId == null) {
            Logger.getLogger().v("Could not persist user ID; no user ID available");
        } else {
            this.reportPersistence.persistUserIdForSession(userId, sessionId);
        }
    }

    public void finalizeSessions(long timestamp, String currentSessionId) {
        this.reportPersistence.finalizeReports(currentSessionId, timestamp);
    }

    public List<String> listSortedOpenSessionIds() {
        return this.reportPersistence.listSortedOpenSessionIds();
    }

    public boolean hasReportsToSend() {
        return this.reportPersistence.hasFinalizedReports();
    }

    public void removeAllReports() {
        this.reportPersistence.deleteAllReports();
    }

    public Task<Void> sendReports(Executor reportSendCompleteExecutor) {
        List<CrashlyticsReportWithSessionId> reportsToSend = this.reportPersistence.loadFinalizedReports();
        ArrayList arrayList = new ArrayList();
        for (CrashlyticsReportWithSessionId reportToSend : reportsToSend) {
            arrayList.add(this.reportsSender.sendReport(reportToSend).continueWith(reportSendCompleteExecutor, new Continuation() { // from class: com.google.firebase.crashlytics.internal.common.-$$Lambda$SessionReportingCoordinator$2TWemv_1VA0ASKWrGsWON-xeX8o
                @Override // com.google.android.gms.tasks.Continuation
                public final Object then(Task task) {
                    return Boolean.valueOf(SessionReportingCoordinator.this.onReportSendComplete(task));
                }
            }));
        }
        return Tasks.whenAll(arrayList);
    }

    private CrashlyticsReport.Session.Event addLogsAndCustomKeysToEvent(CrashlyticsReport.Session.Event capturedEvent) {
        return addLogsAndCustomKeysToEvent(capturedEvent, this.logFileManager, this.reportMetadata);
    }

    private CrashlyticsReport.Session.Event addLogsAndCustomKeysToEvent(CrashlyticsReport.Session.Event capturedEvent, LogFileManager logFileManager, UserMetadata reportMetadata) {
        CrashlyticsReport.Session.Event.Builder eventBuilder = capturedEvent.toBuilder();
        String content = logFileManager.getLogString();
        if (content != null) {
            eventBuilder.setLog(CrashlyticsReport.Session.Event.Log.builder().setContent(content).build());
        } else {
            Logger.getLogger().v("No log data to include with this event.");
        }
        List<CrashlyticsReport.CustomAttribute> sortedCustomAttributes = getSortedCustomAttributes(reportMetadata.getCustomKeys());
        List<CrashlyticsReport.CustomAttribute> sortedInternalKeys = getSortedCustomAttributes(reportMetadata.getInternalKeys());
        if (!sortedCustomAttributes.isEmpty()) {
            eventBuilder.setApp(capturedEvent.getApp().toBuilder().setCustomAttributes(ImmutableList.from(sortedCustomAttributes)).setInternalKeys(ImmutableList.from(sortedInternalKeys)).build());
        }
        return eventBuilder.build();
    }

    private void persistEvent(Throwable event, Thread thread, String sessionId, String eventType, long timestamp, boolean includeAllThreads) {
        this.reportPersistence.persistEvent(addLogsAndCustomKeysToEvent(this.dataCapture.captureEventData(event, thread, eventType, timestamp, 4, 8, includeAllThreads)), sessionId, eventType.equals("crash"));
    }

    public boolean onReportSendComplete(Task<CrashlyticsReportWithSessionId> task) {
        if (task.isSuccessful()) {
            CrashlyticsReportWithSessionId report = task.getResult();
            Logger logger = Logger.getLogger();
            logger.d("Crashlytics report successfully enqueued to DataTransport: " + report.getSessionId());
            this.reportPersistence.deleteFinalizedReport(report.getSessionId());
            return true;
        }
        Logger.getLogger().w("Crashlytics report could not be enqueued to DataTransport", task.getException());
        return false;
    }

    private static List<CrashlyticsReport.CustomAttribute> getSortedCustomAttributes(Map<String, String> attributes) {
        ArrayList<CrashlyticsReport.CustomAttribute> attributesList = new ArrayList<>();
        attributesList.ensureCapacity(attributes.size());
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            attributesList.add(CrashlyticsReport.CustomAttribute.builder().setKey(entry.getKey()).setValue(entry.getValue()).build());
        }
        Collections.sort(attributesList, $$Lambda$SessionReportingCoordinator$TX0lZug2FS1EXHwsWNz9vWcgWJw.INSTANCE);
        return attributesList;
    }

    private static CrashlyticsReport.ApplicationExitInfo convertApplicationExitInfo(ApplicationExitInfo applicationExitInfo) {
        String traceFile = null;
        try {
            traceFile = convertInputStreamToString(applicationExitInfo.getTraceInputStream());
        } catch (IOException | NullPointerException e) {
            Logger logger = Logger.getLogger();
            logger.w("Could not get input trace in application exit info: " + applicationExitInfo.toString() + " Error: " + e);
        }
        return CrashlyticsReport.ApplicationExitInfo.builder().setImportance(applicationExitInfo.getImportance()).setProcessName(applicationExitInfo.getProcessName()).setReasonCode(applicationExitInfo.getReason()).setTimestamp(applicationExitInfo.getTimestamp()).setPid(applicationExitInfo.getPid()).setPss(applicationExitInfo.getPss()).setRss(applicationExitInfo.getRss()).setTraceFile(traceFile).build();
    }

    public static String convertInputStreamToString(InputStream inputStream) throws IOException, NullPointerException {
        StringBuilder stringBuilder = new StringBuilder();
        Reader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName(StandardCharsets.UTF_8.name())));
        while (true) {
            try {
                int c = reader.read();
                if (c != -1) {
                    stringBuilder.append((char) c);
                } else {
                    String sb = stringBuilder.toString();
                    reader.close();
                    return sb;
                }
            } catch (Throwable th) {
                try {
                    reader.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
    }
}
