package com.google.firebase.heartbeatinfo;

import com.google.firebase.heartbeatinfo.HeartBeatInfo;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public final class AutoValue_HeartBeatResult extends HeartBeatResult {
    private final HeartBeatInfo.HeartBeat heartBeat;
    private final long millis;
    private final String sdkName;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoValue_HeartBeatResult(String sdkName, long millis, HeartBeatInfo.HeartBeat heartBeat) {
        if (sdkName != null) {
            this.sdkName = sdkName;
            this.millis = millis;
            if (heartBeat != null) {
                this.heartBeat = heartBeat;
                return;
            }
            throw new NullPointerException("Null heartBeat");
        }
        throw new NullPointerException("Null sdkName");
    }

    @Override // com.google.firebase.heartbeatinfo.HeartBeatResult
    public String getSdkName() {
        return this.sdkName;
    }

    @Override // com.google.firebase.heartbeatinfo.HeartBeatResult
    public long getMillis() {
        return this.millis;
    }

    @Override // com.google.firebase.heartbeatinfo.HeartBeatResult
    public HeartBeatInfo.HeartBeat getHeartBeat() {
        return this.heartBeat;
    }

    public String toString() {
        return "HeartBeatResult{sdkName=" + this.sdkName + ", millis=" + this.millis + ", heartBeat=" + this.heartBeat + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof HeartBeatResult)) {
            return false;
        }
        HeartBeatResult that = (HeartBeatResult) o;
        if (!this.sdkName.equals(that.getSdkName()) || this.millis != that.getMillis() || !this.heartBeat.equals(that.getHeartBeat())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        long j = this.millis;
        return (((((1 * 1000003) ^ this.sdkName.hashCode()) * 1000003) ^ ((int) (j ^ (j >>> 32)))) * 1000003) ^ this.heartBeat.hashCode();
    }
}
