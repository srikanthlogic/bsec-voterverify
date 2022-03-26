package com.google.firebase.crashlytics.internal.persistence;

import com.google.firebase.crashlytics.internal.Logger;
import com.google.firebase.crashlytics.internal.common.CrashlyticsReportWithSessionId;
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
import com.google.firebase.crashlytics.internal.model.ImmutableList;
import com.google.firebase.crashlytics.internal.model.serialization.CrashlyticsReportJsonTransform;
import com.google.firebase.crashlytics.internal.settings.SettingsDataProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes3.dex */
public class CrashlyticsReportPersistence {
    private static final String APP_EXIT_INFO_FILE_NAME;
    private static final String EVENT_COUNTER_FORMAT;
    private static final int EVENT_COUNTER_WIDTH;
    private static final String EVENT_FILE_NAME_PREFIX;
    private static final String EVENT_TYPE_ANR;
    private static final int MAX_OPEN_SESSIONS;
    private static final String NATIVE_REPORTS_DIRECTORY;
    private static final String NORMAL_EVENT_SUFFIX;
    private static final String OPEN_SESSIONS_DIRECTORY_NAME;
    private static final String PRIORITY_EVENT_SUFFIX;
    private static final String PRIORITY_REPORTS_DIRECTORY;
    private static final String REPORTS_DIRECTORY;
    private static final String REPORT_FILE_NAME;
    private static final String SESSION_START_TIMESTAMP_FILE_NAME;
    private static final String USER_FILE_NAME;
    private static final String WORKING_DIRECTORY_NAME;
    private final AtomicInteger eventCounter = new AtomicInteger(0);
    private final File nativeReportsDirectory;
    private final File openSessionsDirectory;
    private final File priorityReportsDirectory;
    private final File reportsDirectory;
    private final SettingsDataProvider settingsDataProvider;
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final int EVENT_NAME_LENGTH = "event".length() + 10;
    private static final CrashlyticsReportJsonTransform TRANSFORM = new CrashlyticsReportJsonTransform();
    private static final Comparator<? super File> LATEST_SESSION_ID_FIRST_COMPARATOR = $$Lambda$CrashlyticsReportPersistence$cus4xBFpkzHLsoBdoVy1SdmpDs.INSTANCE;
    private static final FilenameFilter EVENT_FILE_FILTER = $$Lambda$CrashlyticsReportPersistence$yrLDYcvA2rHplfuqiXhfFmNn2UQ.INSTANCE;

    public CrashlyticsReportPersistence(File rootDirectory, SettingsDataProvider settingsDataProvider) {
        File workingDirectory = new File(rootDirectory, WORKING_DIRECTORY_NAME);
        this.openSessionsDirectory = new File(workingDirectory, OPEN_SESSIONS_DIRECTORY_NAME);
        this.priorityReportsDirectory = new File(workingDirectory, PRIORITY_REPORTS_DIRECTORY);
        this.reportsDirectory = new File(workingDirectory, REPORTS_DIRECTORY);
        this.nativeReportsDirectory = new File(workingDirectory, NATIVE_REPORTS_DIRECTORY);
        this.settingsDataProvider = settingsDataProvider;
    }

    public void persistReport(CrashlyticsReport report) {
        CrashlyticsReport.Session session = report.getSession();
        if (session == null) {
            Logger.getLogger().d("Could not get session for report");
            return;
        }
        String sessionId = session.getIdentifier();
        try {
            File sessionDirectory = prepareDirectory(getSessionDirectoryById(sessionId));
            writeTextFile(new File(sessionDirectory, REPORT_FILE_NAME), TRANSFORM.reportToJson(report));
            writeTextFile(new File(sessionDirectory, SESSION_START_TIMESTAMP_FILE_NAME), "", session.getStartedAt());
        } catch (IOException e) {
            Logger logger = Logger.getLogger();
            logger.d("Could not persist report for session " + sessionId, e);
        }
    }

    public void persistEvent(CrashlyticsReport.Session.Event event, String sessionId) {
        persistEvent(event, sessionId, false);
    }

    public void persistEvent(CrashlyticsReport.Session.Event event, String sessionId, boolean isHighPriority) {
        int maxEventsToKeep = this.settingsDataProvider.getSettings().getSessionData().maxCustomExceptionEvents;
        File sessionDirectory = getSessionDirectoryById(sessionId);
        try {
            writeTextFile(new File(sessionDirectory, generateEventFilename(this.eventCounter.getAndIncrement(), isHighPriority)), TRANSFORM.eventToJson(event));
        } catch (IOException e) {
            Logger logger = Logger.getLogger();
            logger.w("Could not persist event for session " + sessionId, e);
        }
        trimEvents(sessionDirectory, maxEventsToKeep);
    }

    public void persistUserIdForSession(String userId, String sessionId) {
        try {
            writeTextFile(new File(getSessionDirectoryById(sessionId), USER_FILE_NAME), userId);
        } catch (IOException e) {
            Logger logger = Logger.getLogger();
            logger.w("Could not persist user ID for session " + sessionId, e);
        }
    }

    public List<String> listSortedOpenSessionIds() {
        List<File> openSessionDirectories = getAllFilesInDirectory(this.openSessionsDirectory);
        Collections.sort(openSessionDirectories, LATEST_SESSION_ID_FIRST_COMPARATOR);
        List<String> openSessionIds = new ArrayList<>();
        for (File f : openSessionDirectories) {
            openSessionIds.add(f.getName());
        }
        return openSessionIds;
    }

    public long getStartTimestampMillis(String sessionId) {
        return new File(getSessionDirectoryById(sessionId), SESSION_START_TIMESTAMP_FILE_NAME).lastModified();
    }

    public boolean hasFinalizedReports() {
        return !getAllFinalizedReportFiles().isEmpty();
    }

    public void deleteAllReports() {
        for (File reportFile : getAllFinalizedReportFiles()) {
            reportFile.delete();
        }
    }

    public void deleteFinalizedReport(String sessionId) {
        FilenameFilter filter = new FilenameFilter(sessionId) { // from class: com.google.firebase.crashlytics.internal.persistence.-$$Lambda$CrashlyticsReportPersistence$HQEC8vhswXMGNCPEJGBQkG37DZM
            private final /* synthetic */ String f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.io.FilenameFilter
            public final boolean accept(File file, String str) {
                return str.startsWith(this.f$0);
            }
        };
        for (File reportFile : combineReportFiles(getFilesInDirectory(this.priorityReportsDirectory, filter), getFilesInDirectory(this.nativeReportsDirectory, filter), getFilesInDirectory(this.reportsDirectory, filter))) {
            reportFile.delete();
        }
    }

    public void finalizeReports(String currentSessionId, long sessionEndTime) {
        for (File sessionDirectory : capAndGetOpenSessions(currentSessionId)) {
            Logger logger = Logger.getLogger();
            logger.v("Finalizing report for session " + sessionDirectory.getName());
            synthesizeReport(sessionDirectory, sessionEndTime);
            recursiveDelete(sessionDirectory);
        }
        capFinalizedReports();
    }

    public void finalizeSessionWithNativeEvent(String previousSessionId, CrashlyticsReport.FilesPayload ndkPayload) {
        synthesizeNativeReportFile(new File(getSessionDirectoryById(previousSessionId), REPORT_FILE_NAME), this.nativeReportsDirectory, ndkPayload, previousSessionId);
    }

    public List<CrashlyticsReportWithSessionId> loadFinalizedReports() {
        List<File> allReportFiles = getAllFinalizedReportFiles();
        ArrayList<CrashlyticsReportWithSessionId> allReports = new ArrayList<>();
        allReports.ensureCapacity(allReportFiles.size());
        for (File reportFile : getAllFinalizedReportFiles()) {
            try {
                allReports.add(CrashlyticsReportWithSessionId.create(TRANSFORM.reportFromJson(readTextFile(reportFile)), reportFile.getName()));
            } catch (IOException e) {
                Logger logger = Logger.getLogger();
                logger.w("Could not load report file " + reportFile + "; deleting", e);
                reportFile.delete();
            }
        }
        return allReports;
    }

    private List<File> capAndGetOpenSessions(String currentSessionId) {
        List<File> openSessionDirectories = getFilesInDirectory(this.openSessionsDirectory, new FileFilter(currentSessionId) { // from class: com.google.firebase.crashlytics.internal.persistence.-$$Lambda$CrashlyticsReportPersistence$Wtl6nS_DwzMYjZWqPOq5CDR-xlc
            private final /* synthetic */ String f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.io.FileFilter
            public final boolean accept(File file) {
                return CrashlyticsReportPersistence.lambda$capAndGetOpenSessions$3(this.f$0, file);
            }
        });
        Collections.sort(openSessionDirectories, LATEST_SESSION_ID_FIRST_COMPARATOR);
        if (openSessionDirectories.size() <= 8) {
            return openSessionDirectories;
        }
        for (File openSessionDirectory : openSessionDirectories.subList(8, openSessionDirectories.size())) {
            recursiveDelete(openSessionDirectory);
        }
        return openSessionDirectories.subList(0, 8);
    }

    public static /* synthetic */ boolean lambda$capAndGetOpenSessions$3(String currentSessionId, File f) {
        return f.isDirectory() && !f.getName().equals(currentSessionId);
    }

    private void capFinalizedReports() {
        int maxReportsToKeep = this.settingsDataProvider.getSettings().getSessionData().maxCompleteSessionsCount;
        List<File> finalizedReportFiles = getAllFinalizedReportFiles();
        int fileCount = finalizedReportFiles.size();
        if (fileCount > maxReportsToKeep) {
            for (File reportFile : finalizedReportFiles.subList(maxReportsToKeep, fileCount)) {
                reportFile.delete();
            }
        }
    }

    private List<File> getAllFinalizedReportFiles() {
        return sortAndCombineReportFiles(combineReportFiles(getAllFilesInDirectory(this.priorityReportsDirectory), getAllFilesInDirectory(this.nativeReportsDirectory)), getAllFilesInDirectory(this.reportsDirectory));
    }

    private File getSessionDirectoryById(String sessionId) {
        return new File(this.openSessionsDirectory, sessionId);
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x00e1  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00e4  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private void synthesizeReport(File sessionDirectory, long sessionEndTime) {
        String userId;
        List<File> eventFiles = getFilesInDirectory(sessionDirectory, EVENT_FILE_FILTER);
        if (eventFiles.isEmpty()) {
            Logger.getLogger().v("Session " + sessionDirectory.getName() + " has no events.");
            return;
        }
        Collections.sort(eventFiles);
        List<CrashlyticsReport.Session.Event> events = new ArrayList<>();
        boolean isHighPriorityReport = false;
        for (File eventFile : eventFiles) {
            try {
                events.add(TRANSFORM.eventFromJson(readTextFile(eventFile)));
                isHighPriorityReport = isHighPriorityReport || isHighPriorityEventFile(eventFile.getName());
            } catch (IOException e) {
                Logger.getLogger().w("Could not add event to report for " + eventFile, e);
            }
        }
        if (events.isEmpty()) {
            Logger.getLogger().w("Could not parse event files for session " + sessionDirectory.getName());
            return;
        }
        File userIdFile = new File(sessionDirectory, USER_FILE_NAME);
        if (userIdFile.isFile()) {
            try {
                userId = readTextFile(userIdFile);
            } catch (IOException e2) {
                Logger.getLogger().w("Could not read user ID file in " + sessionDirectory.getName(), e2);
            }
            synthesizeReportFile(new File(sessionDirectory, REPORT_FILE_NAME), !isHighPriorityReport ? this.priorityReportsDirectory : this.reportsDirectory, events, sessionEndTime, isHighPriorityReport, userId);
        }
        userId = null;
        synthesizeReportFile(new File(sessionDirectory, REPORT_FILE_NAME), !isHighPriorityReport ? this.priorityReportsDirectory : this.reportsDirectory, events, sessionEndTime, isHighPriorityReport, userId);
    }

    private static void synthesizeNativeReportFile(File reportFile, File outputDirectory, CrashlyticsReport.FilesPayload ndkPayload, String previousSessionId) {
        try {
            writeTextFile(new File(prepareDirectory(outputDirectory), previousSessionId), TRANSFORM.reportToJson(TRANSFORM.reportFromJson(readTextFile(reportFile)).withNdkPayload(ndkPayload)));
        } catch (IOException e) {
            Logger logger = Logger.getLogger();
            logger.w("Could not synthesize final native report file for " + reportFile, e);
        }
    }

    private static void synthesizeReportFile(File reportFile, File outputDirectory, List<CrashlyticsReport.Session.Event> events, long sessionEndTime, boolean isCrashed, String userId) {
        try {
            CrashlyticsReport report = TRANSFORM.reportFromJson(readTextFile(reportFile)).withSessionEndFields(sessionEndTime, isCrashed, userId).withEvents(ImmutableList.from(events));
            CrashlyticsReport.Session session = report.getSession();
            if (session != null) {
                writeTextFile(new File(prepareDirectory(outputDirectory), session.getIdentifier()), TRANSFORM.reportToJson(report));
            }
        } catch (IOException e) {
            Logger logger = Logger.getLogger();
            logger.w("Could not synthesize final report file for " + reportFile, e);
        }
    }

    private static List<File> sortAndCombineReportFiles(List<File>... reports) {
        for (List<File> reportList : reports) {
            Collections.sort(reportList, LATEST_SESSION_ID_FIRST_COMPARATOR);
        }
        return combineReportFiles(reports);
    }

    private static List<File> combineReportFiles(List<File>... reports) {
        ArrayList<File> allReportsFiles = new ArrayList<>();
        int totalReports = 0;
        for (List<File> reportList : reports) {
            totalReports += reportList.size();
        }
        allReportsFiles.ensureCapacity(totalReports);
        for (List<File> reportList2 : reports) {
            allReportsFiles.addAll(reportList2);
        }
        return allReportsFiles;
    }

    private static boolean isHighPriorityEventFile(String fileName) {
        return fileName.startsWith("event") && fileName.endsWith(PRIORITY_EVENT_SUFFIX);
    }

    public static boolean isNormalPriorityEventFile(File dir, String name) {
        return name.startsWith("event") && !name.endsWith(PRIORITY_EVENT_SUFFIX);
    }

    private static String generateEventFilename(int eventNumber, boolean isHighPriority) {
        String paddedEventNumber = String.format(Locale.US, EVENT_COUNTER_FORMAT, Integer.valueOf(eventNumber));
        String prioritySuffix = isHighPriority ? PRIORITY_EVENT_SUFFIX : "";
        return "event" + paddedEventNumber + prioritySuffix;
    }

    private static int trimEvents(File sessionDirectory, int maximum) {
        List<File> normalPriorityEventFiles = getFilesInDirectory(sessionDirectory, $$Lambda$CrashlyticsReportPersistence$5ewmHYO883ri5BmwN_Gn2GLWEFU.INSTANCE);
        Collections.sort(normalPriorityEventFiles, $$Lambda$CrashlyticsReportPersistence$01Lorz603_5ziNugbvmzIHT6dw.INSTANCE);
        return capFilesCount(normalPriorityEventFiles, maximum);
    }

    private static String getEventNameWithoutPriority(String eventFileName) {
        return eventFileName.substring(0, EVENT_NAME_LENGTH);
    }

    public static int oldestEventFileFirst(File f1, File f2) {
        return getEventNameWithoutPriority(f1.getName()).compareTo(getEventNameWithoutPriority(f2.getName()));
    }

    private static List<File> getAllFilesInDirectory(File directory) {
        return getFilesInDirectory(directory, (FileFilter) null);
    }

    private static List<File> getFilesInDirectory(File directory, FilenameFilter filter) {
        if (!directory.isDirectory()) {
            return Collections.emptyList();
        }
        File[] files = filter == null ? directory.listFiles() : directory.listFiles(filter);
        return files != null ? Arrays.asList(files) : Collections.emptyList();
    }

    private static List<File> getFilesInDirectory(File directory, FileFilter filter) {
        if (!directory.isDirectory()) {
            return Collections.emptyList();
        }
        File[] files = filter == null ? directory.listFiles() : directory.listFiles(filter);
        return files != null ? Arrays.asList(files) : Collections.emptyList();
    }

    private static File prepareDirectory(File directory) throws IOException {
        if (makeDirectory(directory)) {
            return directory;
        }
        throw new IOException("Could not create directory " + directory);
    }

    private static boolean makeDirectory(File directory) {
        return directory.exists() || directory.mkdirs();
    }

    private static void writeTextFile(File file, String text) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), UTF_8);
        try {
            writer.write(text);
            writer.close();
        } catch (Throwable th) {
            try {
                writer.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private static void writeTextFile(File file, String text, long lastModifiedTimestampSeconds) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), UTF_8);
        try {
            writer.write(text);
            file.setLastModified(convertTimestampFromSecondsToMs(lastModifiedTimestampSeconds));
            writer.close();
        } catch (Throwable th) {
            try {
                writer.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private static String readTextFile(File file) throws IOException {
        byte[] readBuffer = new byte[8192];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileInputStream fileInput = new FileInputStream(file);
        while (true) {
            try {
                int read = fileInput.read(readBuffer);
                if (read > 0) {
                    bos.write(readBuffer, 0, read);
                } else {
                    String str = new String(bos.toByteArray(), UTF_8);
                    fileInput.close();
                    return str;
                }
            } catch (Throwable th) {
                try {
                    fileInput.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
    }

    private static int capFilesCount(List<File> files, int maximum) {
        int numRetained = files.size();
        for (File f : files) {
            if (numRetained <= maximum) {
                return numRetained;
            }
            recursiveDelete(f);
            numRetained--;
        }
        return numRetained;
    }

    private static void recursiveDelete(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    recursiveDelete(f);
                }
            }
            file.delete();
        }
    }

    private static long convertTimestampFromSecondsToMs(long timestampSeconds) {
        return 1000 * timestampSeconds;
    }
}
