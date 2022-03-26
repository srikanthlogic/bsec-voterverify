package com.google.firebase.crashlytics.internal.model;

import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
/* loaded from: classes3.dex */
final class AutoValue_CrashlyticsReport_Session_Event_Log extends CrashlyticsReport.Session.Event.Log {
    private final String content;

    private AutoValue_CrashlyticsReport_Session_Event_Log(String content) {
        this.content = content;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Log
    public String getContent() {
        return this.content;
    }

    public String toString() {
        return "Log{content=" + this.content + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof CrashlyticsReport.Session.Event.Log) {
            return this.content.equals(((CrashlyticsReport.Session.Event.Log) o).getContent());
        }
        return false;
    }

    public int hashCode() {
        return (1 * 1000003) ^ this.content.hashCode();
    }

    /* loaded from: classes3.dex */
    static final class Builder extends CrashlyticsReport.Session.Event.Log.Builder {
        private String content;

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Log.Builder
        public CrashlyticsReport.Session.Event.Log.Builder setContent(String content) {
            if (content != null) {
                this.content = content;
                return this;
            }
            throw new NullPointerException("Null content");
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Log.Builder
        public CrashlyticsReport.Session.Event.Log build() {
            String missing = "";
            if (this.content == null) {
                missing = missing + " content";
            }
            if (missing.isEmpty()) {
                return new AutoValue_CrashlyticsReport_Session_Event_Log(this.content);
            }
            throw new IllegalStateException("Missing required properties:" + missing);
        }
    }
}
