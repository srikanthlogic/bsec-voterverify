package com.google.firebase.crashlytics.internal.model;

import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
/* loaded from: classes3.dex */
final class AutoValue_CrashlyticsReport_Session_Application extends CrashlyticsReport.Session.Application {
    private final String developmentPlatform;
    private final String developmentPlatformVersion;
    private final String displayVersion;
    private final String identifier;
    private final String installationUuid;
    private final CrashlyticsReport.Session.Application.Organization organization;
    private final String version;

    private AutoValue_CrashlyticsReport_Session_Application(String identifier, String version, String displayVersion, CrashlyticsReport.Session.Application.Organization organization, String installationUuid, String developmentPlatform, String developmentPlatformVersion) {
        this.identifier = identifier;
        this.version = version;
        this.displayVersion = displayVersion;
        this.organization = organization;
        this.installationUuid = installationUuid;
        this.developmentPlatform = developmentPlatform;
        this.developmentPlatformVersion = developmentPlatformVersion;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Application
    public String getIdentifier() {
        return this.identifier;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Application
    public String getVersion() {
        return this.version;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Application
    public String getDisplayVersion() {
        return this.displayVersion;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Application
    public CrashlyticsReport.Session.Application.Organization getOrganization() {
        return this.organization;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Application
    public String getInstallationUuid() {
        return this.installationUuid;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Application
    public String getDevelopmentPlatform() {
        return this.developmentPlatform;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Application
    public String getDevelopmentPlatformVersion() {
        return this.developmentPlatformVersion;
    }

    public String toString() {
        return "Application{identifier=" + this.identifier + ", version=" + this.version + ", displayVersion=" + this.displayVersion + ", organization=" + this.organization + ", installationUuid=" + this.installationUuid + ", developmentPlatform=" + this.developmentPlatform + ", developmentPlatformVersion=" + this.developmentPlatformVersion + "}";
    }

    public boolean equals(Object o) {
        String str;
        CrashlyticsReport.Session.Application.Organization organization;
        String str2;
        String str3;
        if (o == this) {
            return true;
        }
        if (!(o instanceof CrashlyticsReport.Session.Application)) {
            return false;
        }
        CrashlyticsReport.Session.Application that = (CrashlyticsReport.Session.Application) o;
        if (this.identifier.equals(that.getIdentifier()) && this.version.equals(that.getVersion()) && ((str = this.displayVersion) != null ? str.equals(that.getDisplayVersion()) : that.getDisplayVersion() == null) && ((organization = this.organization) != null ? organization.equals(that.getOrganization()) : that.getOrganization() == null) && ((str2 = this.installationUuid) != null ? str2.equals(that.getInstallationUuid()) : that.getInstallationUuid() == null) && ((str3 = this.developmentPlatform) != null ? str3.equals(that.getDevelopmentPlatform()) : that.getDevelopmentPlatform() == null)) {
            String str4 = this.developmentPlatformVersion;
            if (str4 == null) {
                if (that.getDevelopmentPlatformVersion() == null) {
                    return true;
                }
            } else if (str4.equals(that.getDevelopmentPlatformVersion())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int h$ = ((((1 * 1000003) ^ this.identifier.hashCode()) * 1000003) ^ this.version.hashCode()) * 1000003;
        String str = this.displayVersion;
        int i = 0;
        int h$2 = (h$ ^ (str == null ? 0 : str.hashCode())) * 1000003;
        CrashlyticsReport.Session.Application.Organization organization = this.organization;
        int h$3 = (h$2 ^ (organization == null ? 0 : organization.hashCode())) * 1000003;
        String str2 = this.installationUuid;
        int h$4 = (h$3 ^ (str2 == null ? 0 : str2.hashCode())) * 1000003;
        String str3 = this.developmentPlatform;
        int h$5 = (h$4 ^ (str3 == null ? 0 : str3.hashCode())) * 1000003;
        String str4 = this.developmentPlatformVersion;
        if (str4 != null) {
            i = str4.hashCode();
        }
        return h$5 ^ i;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Application
    protected CrashlyticsReport.Session.Application.Builder toBuilder() {
        return new Builder(this);
    }

    /* loaded from: classes3.dex */
    static final class Builder extends CrashlyticsReport.Session.Application.Builder {
        private String developmentPlatform;
        private String developmentPlatformVersion;
        private String displayVersion;
        private String identifier;
        private String installationUuid;
        private CrashlyticsReport.Session.Application.Organization organization;
        private String version;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder() {
        }

        private Builder(CrashlyticsReport.Session.Application source) {
            this.identifier = source.getIdentifier();
            this.version = source.getVersion();
            this.displayVersion = source.getDisplayVersion();
            this.organization = source.getOrganization();
            this.installationUuid = source.getInstallationUuid();
            this.developmentPlatform = source.getDevelopmentPlatform();
            this.developmentPlatformVersion = source.getDevelopmentPlatformVersion();
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Application.Builder
        public CrashlyticsReport.Session.Application.Builder setIdentifier(String identifier) {
            if (identifier != null) {
                this.identifier = identifier;
                return this;
            }
            throw new NullPointerException("Null identifier");
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Application.Builder
        public CrashlyticsReport.Session.Application.Builder setVersion(String version) {
            if (version != null) {
                this.version = version;
                return this;
            }
            throw new NullPointerException("Null version");
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Application.Builder
        public CrashlyticsReport.Session.Application.Builder setDisplayVersion(String displayVersion) {
            this.displayVersion = displayVersion;
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Application.Builder
        public CrashlyticsReport.Session.Application.Builder setOrganization(CrashlyticsReport.Session.Application.Organization organization) {
            this.organization = organization;
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Application.Builder
        public CrashlyticsReport.Session.Application.Builder setInstallationUuid(String installationUuid) {
            this.installationUuid = installationUuid;
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Application.Builder
        public CrashlyticsReport.Session.Application.Builder setDevelopmentPlatform(String developmentPlatform) {
            this.developmentPlatform = developmentPlatform;
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Application.Builder
        public CrashlyticsReport.Session.Application.Builder setDevelopmentPlatformVersion(String developmentPlatformVersion) {
            this.developmentPlatformVersion = developmentPlatformVersion;
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Application.Builder
        public CrashlyticsReport.Session.Application build() {
            String missing = "";
            if (this.identifier == null) {
                missing = missing + " identifier";
            }
            if (this.version == null) {
                missing = missing + " version";
            }
            if (missing.isEmpty()) {
                return new AutoValue_CrashlyticsReport_Session_Application(this.identifier, this.version, this.displayVersion, this.organization, this.installationUuid, this.developmentPlatform, this.developmentPlatformVersion);
            }
            throw new IllegalStateException("Missing required properties:" + missing);
        }
    }
}
