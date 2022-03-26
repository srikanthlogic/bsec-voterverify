package com.google.firebase.crashlytics.internal.model;

import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
/* loaded from: classes3.dex */
final class AutoValue_CrashlyticsReport_Session_User extends CrashlyticsReport.Session.User {
    private final String identifier;

    private AutoValue_CrashlyticsReport_Session_User(String identifier) {
        this.identifier = identifier;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.User
    public String getIdentifier() {
        return this.identifier;
    }

    public String toString() {
        return "User{identifier=" + this.identifier + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof CrashlyticsReport.Session.User) {
            return this.identifier.equals(((CrashlyticsReport.Session.User) o).getIdentifier());
        }
        return false;
    }

    public int hashCode() {
        return (1 * 1000003) ^ this.identifier.hashCode();
    }

    /* loaded from: classes3.dex */
    static final class Builder extends CrashlyticsReport.Session.User.Builder {
        private String identifier;

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.User.Builder
        public CrashlyticsReport.Session.User.Builder setIdentifier(String identifier) {
            if (identifier != null) {
                this.identifier = identifier;
                return this;
            }
            throw new NullPointerException("Null identifier");
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.User.Builder
        public CrashlyticsReport.Session.User build() {
            String missing = "";
            if (this.identifier == null) {
                missing = missing + " identifier";
            }
            if (missing.isEmpty()) {
                return new AutoValue_CrashlyticsReport_Session_User(this.identifier);
            }
            throw new IllegalStateException("Missing required properties:" + missing);
        }
    }
}
