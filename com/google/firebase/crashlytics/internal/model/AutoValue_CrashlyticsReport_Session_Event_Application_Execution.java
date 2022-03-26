package com.google.firebase.crashlytics.internal.model;

import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
/* loaded from: classes3.dex */
final class AutoValue_CrashlyticsReport_Session_Event_Application_Execution extends CrashlyticsReport.Session.Event.Application.Execution {
    private final CrashlyticsReport.ApplicationExitInfo appExitInfo;
    private final ImmutableList<CrashlyticsReport.Session.Event.Application.Execution.BinaryImage> binaries;
    private final CrashlyticsReport.Session.Event.Application.Execution.Exception exception;
    private final CrashlyticsReport.Session.Event.Application.Execution.Signal signal;
    private final ImmutableList<CrashlyticsReport.Session.Event.Application.Execution.Thread> threads;

    private AutoValue_CrashlyticsReport_Session_Event_Application_Execution(ImmutableList<CrashlyticsReport.Session.Event.Application.Execution.Thread> threads, CrashlyticsReport.Session.Event.Application.Execution.Exception exception, CrashlyticsReport.ApplicationExitInfo appExitInfo, CrashlyticsReport.Session.Event.Application.Execution.Signal signal, ImmutableList<CrashlyticsReport.Session.Event.Application.Execution.BinaryImage> binaries) {
        this.threads = threads;
        this.exception = exception;
        this.appExitInfo = appExitInfo;
        this.signal = signal;
        this.binaries = binaries;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Execution
    public ImmutableList<CrashlyticsReport.Session.Event.Application.Execution.Thread> getThreads() {
        return this.threads;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Execution
    public CrashlyticsReport.Session.Event.Application.Execution.Exception getException() {
        return this.exception;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Execution
    public CrashlyticsReport.ApplicationExitInfo getAppExitInfo() {
        return this.appExitInfo;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Execution
    public CrashlyticsReport.Session.Event.Application.Execution.Signal getSignal() {
        return this.signal;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Execution
    public ImmutableList<CrashlyticsReport.Session.Event.Application.Execution.BinaryImage> getBinaries() {
        return this.binaries;
    }

    public String toString() {
        return "Execution{threads=" + this.threads + ", exception=" + this.exception + ", appExitInfo=" + this.appExitInfo + ", signal=" + this.signal + ", binaries=" + this.binaries + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CrashlyticsReport.Session.Event.Application.Execution)) {
            return false;
        }
        CrashlyticsReport.Session.Event.Application.Execution that = (CrashlyticsReport.Session.Event.Application.Execution) o;
        ImmutableList<CrashlyticsReport.Session.Event.Application.Execution.Thread> immutableList = this.threads;
        if (immutableList != null ? immutableList.equals(that.getThreads()) : that.getThreads() == null) {
            CrashlyticsReport.Session.Event.Application.Execution.Exception exception = this.exception;
            if (exception != null ? exception.equals(that.getException()) : that.getException() == null) {
                CrashlyticsReport.ApplicationExitInfo applicationExitInfo = this.appExitInfo;
                if (applicationExitInfo != null ? applicationExitInfo.equals(that.getAppExitInfo()) : that.getAppExitInfo() == null) {
                    if (this.signal.equals(that.getSignal()) && this.binaries.equals(that.getBinaries())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int hashCode() {
        int h$ = 1 * 1000003;
        ImmutableList<CrashlyticsReport.Session.Event.Application.Execution.Thread> immutableList = this.threads;
        int i = 0;
        int h$2 = (h$ ^ (immutableList == null ? 0 : immutableList.hashCode())) * 1000003;
        CrashlyticsReport.Session.Event.Application.Execution.Exception exception = this.exception;
        int h$3 = (h$2 ^ (exception == null ? 0 : exception.hashCode())) * 1000003;
        CrashlyticsReport.ApplicationExitInfo applicationExitInfo = this.appExitInfo;
        if (applicationExitInfo != null) {
            i = applicationExitInfo.hashCode();
        }
        return ((((h$3 ^ i) * 1000003) ^ this.signal.hashCode()) * 1000003) ^ this.binaries.hashCode();
    }

    /* loaded from: classes3.dex */
    static final class Builder extends CrashlyticsReport.Session.Event.Application.Execution.Builder {
        private CrashlyticsReport.ApplicationExitInfo appExitInfo;
        private ImmutableList<CrashlyticsReport.Session.Event.Application.Execution.BinaryImage> binaries;
        private CrashlyticsReport.Session.Event.Application.Execution.Exception exception;
        private CrashlyticsReport.Session.Event.Application.Execution.Signal signal;
        private ImmutableList<CrashlyticsReport.Session.Event.Application.Execution.Thread> threads;

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Execution.Builder
        public CrashlyticsReport.Session.Event.Application.Execution.Builder setThreads(ImmutableList<CrashlyticsReport.Session.Event.Application.Execution.Thread> threads) {
            this.threads = threads;
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Execution.Builder
        public CrashlyticsReport.Session.Event.Application.Execution.Builder setException(CrashlyticsReport.Session.Event.Application.Execution.Exception exception) {
            this.exception = exception;
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Execution.Builder
        public CrashlyticsReport.Session.Event.Application.Execution.Builder setAppExitInfo(CrashlyticsReport.ApplicationExitInfo appExitInfo) {
            this.appExitInfo = appExitInfo;
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Execution.Builder
        public CrashlyticsReport.Session.Event.Application.Execution.Builder setSignal(CrashlyticsReport.Session.Event.Application.Execution.Signal signal) {
            if (signal != null) {
                this.signal = signal;
                return this;
            }
            throw new NullPointerException("Null signal");
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Execution.Builder
        public CrashlyticsReport.Session.Event.Application.Execution.Builder setBinaries(ImmutableList<CrashlyticsReport.Session.Event.Application.Execution.BinaryImage> binaries) {
            if (binaries != null) {
                this.binaries = binaries;
                return this;
            }
            throw new NullPointerException("Null binaries");
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Execution.Builder
        public CrashlyticsReport.Session.Event.Application.Execution build() {
            String missing = "";
            if (this.signal == null) {
                missing = missing + " signal";
            }
            if (this.binaries == null) {
                missing = missing + " binaries";
            }
            if (missing.isEmpty()) {
                return new AutoValue_CrashlyticsReport_Session_Event_Application_Execution(this.threads, this.exception, this.appExitInfo, this.signal, this.binaries);
            }
            throw new IllegalStateException("Missing required properties:" + missing);
        }
    }
}
