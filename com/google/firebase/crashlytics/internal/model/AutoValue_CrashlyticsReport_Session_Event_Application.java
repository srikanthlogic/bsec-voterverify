package com.google.firebase.crashlytics.internal.model;

import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
/* loaded from: classes3.dex */
final class AutoValue_CrashlyticsReport_Session_Event_Application extends CrashlyticsReport.Session.Event.Application {
    private final Boolean background;
    private final ImmutableList<CrashlyticsReport.CustomAttribute> customAttributes;
    private final CrashlyticsReport.Session.Event.Application.Execution execution;
    private final ImmutableList<CrashlyticsReport.CustomAttribute> internalKeys;
    private final int uiOrientation;

    private AutoValue_CrashlyticsReport_Session_Event_Application(CrashlyticsReport.Session.Event.Application.Execution execution, ImmutableList<CrashlyticsReport.CustomAttribute> customAttributes, ImmutableList<CrashlyticsReport.CustomAttribute> internalKeys, Boolean background, int uiOrientation) {
        this.execution = execution;
        this.customAttributes = customAttributes;
        this.internalKeys = internalKeys;
        this.background = background;
        this.uiOrientation = uiOrientation;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application
    public CrashlyticsReport.Session.Event.Application.Execution getExecution() {
        return this.execution;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application
    public ImmutableList<CrashlyticsReport.CustomAttribute> getCustomAttributes() {
        return this.customAttributes;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application
    public ImmutableList<CrashlyticsReport.CustomAttribute> getInternalKeys() {
        return this.internalKeys;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application
    public Boolean getBackground() {
        return this.background;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application
    public int getUiOrientation() {
        return this.uiOrientation;
    }

    public String toString() {
        return "Application{execution=" + this.execution + ", customAttributes=" + this.customAttributes + ", internalKeys=" + this.internalKeys + ", background=" + this.background + ", uiOrientation=" + this.uiOrientation + "}";
    }

    public boolean equals(Object o) {
        ImmutableList<CrashlyticsReport.CustomAttribute> immutableList;
        ImmutableList<CrashlyticsReport.CustomAttribute> immutableList2;
        Boolean bool;
        if (o == this) {
            return true;
        }
        if (!(o instanceof CrashlyticsReport.Session.Event.Application)) {
            return false;
        }
        CrashlyticsReport.Session.Event.Application that = (CrashlyticsReport.Session.Event.Application) o;
        if (!this.execution.equals(that.getExecution()) || ((immutableList = this.customAttributes) != null ? !immutableList.equals(that.getCustomAttributes()) : that.getCustomAttributes() != null) || ((immutableList2 = this.internalKeys) != null ? !immutableList2.equals(that.getInternalKeys()) : that.getInternalKeys() != null) || ((bool = this.background) != null ? !bool.equals(that.getBackground()) : that.getBackground() != null) || this.uiOrientation != that.getUiOrientation()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int h$ = ((1 * 1000003) ^ this.execution.hashCode()) * 1000003;
        ImmutableList<CrashlyticsReport.CustomAttribute> immutableList = this.customAttributes;
        int i = 0;
        int h$2 = (h$ ^ (immutableList == null ? 0 : immutableList.hashCode())) * 1000003;
        ImmutableList<CrashlyticsReport.CustomAttribute> immutableList2 = this.internalKeys;
        int h$3 = (h$2 ^ (immutableList2 == null ? 0 : immutableList2.hashCode())) * 1000003;
        Boolean bool = this.background;
        if (bool != null) {
            i = bool.hashCode();
        }
        return ((h$3 ^ i) * 1000003) ^ this.uiOrientation;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application
    public CrashlyticsReport.Session.Event.Application.Builder toBuilder() {
        return new Builder(this);
    }

    /* loaded from: classes3.dex */
    static final class Builder extends CrashlyticsReport.Session.Event.Application.Builder {
        private Boolean background;
        private ImmutableList<CrashlyticsReport.CustomAttribute> customAttributes;
        private CrashlyticsReport.Session.Event.Application.Execution execution;
        private ImmutableList<CrashlyticsReport.CustomAttribute> internalKeys;
        private Integer uiOrientation;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder() {
        }

        private Builder(CrashlyticsReport.Session.Event.Application source) {
            this.execution = source.getExecution();
            this.customAttributes = source.getCustomAttributes();
            this.internalKeys = source.getInternalKeys();
            this.background = source.getBackground();
            this.uiOrientation = Integer.valueOf(source.getUiOrientation());
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Builder
        public CrashlyticsReport.Session.Event.Application.Builder setExecution(CrashlyticsReport.Session.Event.Application.Execution execution) {
            if (execution != null) {
                this.execution = execution;
                return this;
            }
            throw new NullPointerException("Null execution");
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Builder
        public CrashlyticsReport.Session.Event.Application.Builder setCustomAttributes(ImmutableList<CrashlyticsReport.CustomAttribute> customAttributes) {
            this.customAttributes = customAttributes;
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Builder
        public CrashlyticsReport.Session.Event.Application.Builder setInternalKeys(ImmutableList<CrashlyticsReport.CustomAttribute> internalKeys) {
            this.internalKeys = internalKeys;
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Builder
        public CrashlyticsReport.Session.Event.Application.Builder setBackground(Boolean background) {
            this.background = background;
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Builder
        public CrashlyticsReport.Session.Event.Application.Builder setUiOrientation(int uiOrientation) {
            this.uiOrientation = Integer.valueOf(uiOrientation);
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Builder
        public CrashlyticsReport.Session.Event.Application build() {
            String missing = "";
            if (this.execution == null) {
                missing = missing + " execution";
            }
            if (this.uiOrientation == null) {
                missing = missing + " uiOrientation";
            }
            if (missing.isEmpty()) {
                return new AutoValue_CrashlyticsReport_Session_Event_Application(this.execution, this.customAttributes, this.internalKeys, this.background, this.uiOrientation.intValue());
            }
            throw new IllegalStateException("Missing required properties:" + missing);
        }
    }
}
