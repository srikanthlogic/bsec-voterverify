package com.google.android.datatransport.runtime.backends;

import com.google.android.datatransport.runtime.backends.BackendResponse;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AutoValue_BackendResponse extends BackendResponse {
    private final long nextRequestWaitMillis;
    private final BackendResponse.Status status;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoValue_BackendResponse(BackendResponse.Status status, long nextRequestWaitMillis) {
        if (status != null) {
            this.status = status;
            this.nextRequestWaitMillis = nextRequestWaitMillis;
            return;
        }
        throw new NullPointerException("Null status");
    }

    @Override // com.google.android.datatransport.runtime.backends.BackendResponse
    public BackendResponse.Status getStatus() {
        return this.status;
    }

    @Override // com.google.android.datatransport.runtime.backends.BackendResponse
    public long getNextRequestWaitMillis() {
        return this.nextRequestWaitMillis;
    }

    public String toString() {
        return "BackendResponse{status=" + this.status + ", nextRequestWaitMillis=" + this.nextRequestWaitMillis + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BackendResponse)) {
            return false;
        }
        BackendResponse that = (BackendResponse) o;
        if (!this.status.equals(that.getStatus()) || this.nextRequestWaitMillis != that.getNextRequestWaitMillis()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        long j = this.nextRequestWaitMillis;
        return (((1 * 1000003) ^ this.status.hashCode()) * 1000003) ^ ((int) (j ^ (j >>> 32)));
    }
}
