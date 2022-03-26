package com.google.firebase.heartbeatinfo;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public final class AutoValue_SdkHeartBeatResult extends SdkHeartBeatResult {
    private final long millis;
    private final String sdkName;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoValue_SdkHeartBeatResult(String sdkName, long millis) {
        if (sdkName != null) {
            this.sdkName = sdkName;
            this.millis = millis;
            return;
        }
        throw new NullPointerException("Null sdkName");
    }

    @Override // com.google.firebase.heartbeatinfo.SdkHeartBeatResult
    public String getSdkName() {
        return this.sdkName;
    }

    @Override // com.google.firebase.heartbeatinfo.SdkHeartBeatResult
    public long getMillis() {
        return this.millis;
    }

    @Override // java.lang.Object
    public String toString() {
        return "SdkHeartBeatResult{sdkName=" + this.sdkName + ", millis=" + this.millis + "}";
    }

    @Override // java.lang.Object
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SdkHeartBeatResult)) {
            return false;
        }
        SdkHeartBeatResult that = (SdkHeartBeatResult) o;
        if (!this.sdkName.equals(that.getSdkName()) || this.millis != that.getMillis()) {
            return false;
        }
        return true;
    }

    @Override // java.lang.Object
    public int hashCode() {
        long j = this.millis;
        return (((1 * 1000003) ^ this.sdkName.hashCode()) * 1000003) ^ ((int) (j ^ (j >>> 32)));
    }
}
