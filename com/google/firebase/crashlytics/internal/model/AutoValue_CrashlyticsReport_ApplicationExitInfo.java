package com.google.firebase.crashlytics.internal.model;

import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
/* loaded from: classes3.dex */
final class AutoValue_CrashlyticsReport_ApplicationExitInfo extends CrashlyticsReport.ApplicationExitInfo {
    private final int importance;
    private final int pid;
    private final String processName;
    private final long pss;
    private final int reasonCode;
    private final long rss;
    private final long timestamp;
    private final String traceFile;

    private AutoValue_CrashlyticsReport_ApplicationExitInfo(int pid, String processName, int reasonCode, int importance, long pss, long rss, long timestamp, String traceFile) {
        this.pid = pid;
        this.processName = processName;
        this.reasonCode = reasonCode;
        this.importance = importance;
        this.pss = pss;
        this.rss = rss;
        this.timestamp = timestamp;
        this.traceFile = traceFile;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.ApplicationExitInfo
    public int getPid() {
        return this.pid;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.ApplicationExitInfo
    public String getProcessName() {
        return this.processName;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.ApplicationExitInfo
    public int getReasonCode() {
        return this.reasonCode;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.ApplicationExitInfo
    public int getImportance() {
        return this.importance;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.ApplicationExitInfo
    public long getPss() {
        return this.pss;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.ApplicationExitInfo
    public long getRss() {
        return this.rss;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.ApplicationExitInfo
    public long getTimestamp() {
        return this.timestamp;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.ApplicationExitInfo
    public String getTraceFile() {
        return this.traceFile;
    }

    public String toString() {
        return "ApplicationExitInfo{pid=" + this.pid + ", processName=" + this.processName + ", reasonCode=" + this.reasonCode + ", importance=" + this.importance + ", pss=" + this.pss + ", rss=" + this.rss + ", timestamp=" + this.timestamp + ", traceFile=" + this.traceFile + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CrashlyticsReport.ApplicationExitInfo)) {
            return false;
        }
        CrashlyticsReport.ApplicationExitInfo that = (CrashlyticsReport.ApplicationExitInfo) o;
        if (this.pid == that.getPid() && this.processName.equals(that.getProcessName()) && this.reasonCode == that.getReasonCode() && this.importance == that.getImportance() && this.pss == that.getPss() && this.rss == that.getRss() && this.timestamp == that.getTimestamp()) {
            String str = this.traceFile;
            if (str == null) {
                if (that.getTraceFile() == null) {
                    return true;
                }
            } else if (str.equals(that.getTraceFile())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        long j = this.pss;
        long j2 = this.rss;
        long j3 = this.timestamp;
        int h$ = ((((((((((((((1 * 1000003) ^ this.pid) * 1000003) ^ this.processName.hashCode()) * 1000003) ^ this.reasonCode) * 1000003) ^ this.importance) * 1000003) ^ ((int) (j ^ (j >>> 32)))) * 1000003) ^ ((int) (j2 ^ (j2 >>> 32)))) * 1000003) ^ ((int) (j3 ^ (j3 >>> 32)))) * 1000003;
        String str = this.traceFile;
        return h$ ^ (str == null ? 0 : str.hashCode());
    }

    /* loaded from: classes3.dex */
    static final class Builder extends CrashlyticsReport.ApplicationExitInfo.Builder {
        private Integer importance;
        private Integer pid;
        private String processName;
        private Long pss;
        private Integer reasonCode;
        private Long rss;
        private Long timestamp;
        private String traceFile;

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.ApplicationExitInfo.Builder
        public CrashlyticsReport.ApplicationExitInfo.Builder setPid(int pid) {
            this.pid = Integer.valueOf(pid);
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.ApplicationExitInfo.Builder
        public CrashlyticsReport.ApplicationExitInfo.Builder setProcessName(String processName) {
            if (processName != null) {
                this.processName = processName;
                return this;
            }
            throw new NullPointerException("Null processName");
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.ApplicationExitInfo.Builder
        public CrashlyticsReport.ApplicationExitInfo.Builder setReasonCode(int reasonCode) {
            this.reasonCode = Integer.valueOf(reasonCode);
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.ApplicationExitInfo.Builder
        public CrashlyticsReport.ApplicationExitInfo.Builder setImportance(int importance) {
            this.importance = Integer.valueOf(importance);
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.ApplicationExitInfo.Builder
        public CrashlyticsReport.ApplicationExitInfo.Builder setPss(long pss) {
            this.pss = Long.valueOf(pss);
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.ApplicationExitInfo.Builder
        public CrashlyticsReport.ApplicationExitInfo.Builder setRss(long rss) {
            this.rss = Long.valueOf(rss);
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.ApplicationExitInfo.Builder
        public CrashlyticsReport.ApplicationExitInfo.Builder setTimestamp(long timestamp) {
            this.timestamp = Long.valueOf(timestamp);
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.ApplicationExitInfo.Builder
        public CrashlyticsReport.ApplicationExitInfo.Builder setTraceFile(String traceFile) {
            this.traceFile = traceFile;
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.ApplicationExitInfo.Builder
        public CrashlyticsReport.ApplicationExitInfo build() {
            String missing = "";
            if (this.pid == null) {
                missing = missing + " pid";
            }
            if (this.processName == null) {
                missing = missing + " processName";
            }
            if (this.reasonCode == null) {
                missing = missing + " reasonCode";
            }
            if (this.importance == null) {
                missing = missing + " importance";
            }
            if (this.pss == null) {
                missing = missing + " pss";
            }
            if (this.rss == null) {
                missing = missing + " rss";
            }
            if (this.timestamp == null) {
                missing = missing + " timestamp";
            }
            if (missing.isEmpty()) {
                return new AutoValue_CrashlyticsReport_ApplicationExitInfo(this.pid.intValue(), this.processName, this.reasonCode.intValue(), this.importance.intValue(), this.pss.longValue(), this.rss.longValue(), this.timestamp.longValue(), this.traceFile);
            }
            throw new IllegalStateException("Missing required properties:" + missing);
        }
    }
}
