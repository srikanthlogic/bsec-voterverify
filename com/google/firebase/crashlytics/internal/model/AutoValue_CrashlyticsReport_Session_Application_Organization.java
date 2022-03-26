package com.google.firebase.crashlytics.internal.model;

import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
/* loaded from: classes3.dex */
final class AutoValue_CrashlyticsReport_Session_Application_Organization extends CrashlyticsReport.Session.Application.Organization {
    private final String clsId;

    private AutoValue_CrashlyticsReport_Session_Application_Organization(String clsId) {
        this.clsId = clsId;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Application.Organization
    public String getClsId() {
        return this.clsId;
    }

    public String toString() {
        return "Organization{clsId=" + this.clsId + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof CrashlyticsReport.Session.Application.Organization) {
            return this.clsId.equals(((CrashlyticsReport.Session.Application.Organization) o).getClsId());
        }
        return false;
    }

    public int hashCode() {
        return (1 * 1000003) ^ this.clsId.hashCode();
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Application.Organization
    protected CrashlyticsReport.Session.Application.Organization.Builder toBuilder() {
        return new Builder(this);
    }

    /* loaded from: classes3.dex */
    static final class Builder extends CrashlyticsReport.Session.Application.Organization.Builder {
        private String clsId;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder() {
        }

        private Builder(CrashlyticsReport.Session.Application.Organization source) {
            this.clsId = source.getClsId();
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Application.Organization.Builder
        public CrashlyticsReport.Session.Application.Organization.Builder setClsId(String clsId) {
            if (clsId != null) {
                this.clsId = clsId;
                return this;
            }
            throw new NullPointerException("Null clsId");
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Application.Organization.Builder
        public CrashlyticsReport.Session.Application.Organization build() {
            String missing = "";
            if (this.clsId == null) {
                missing = missing + " clsId";
            }
            if (missing.isEmpty()) {
                return new AutoValue_CrashlyticsReport_Session_Application_Organization(this.clsId);
            }
            throw new IllegalStateException("Missing required properties:" + missing);
        }
    }
}
