package com.google.android.datatransport.cct.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AutoValue_LogResponse extends LogResponse {
    private final long nextRequestWaitMillis;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoValue_LogResponse(long nextRequestWaitMillis) {
        this.nextRequestWaitMillis = nextRequestWaitMillis;
    }

    @Override // com.google.android.datatransport.cct.internal.LogResponse
    public long getNextRequestWaitMillis() {
        return this.nextRequestWaitMillis;
    }

    public String toString() {
        return "LogResponse{nextRequestWaitMillis=" + this.nextRequestWaitMillis + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LogResponse) || this.nextRequestWaitMillis != ((LogResponse) o).getNextRequestWaitMillis()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        long j = this.nextRequestWaitMillis;
        return (1 * 1000003) ^ ((int) (j ^ (j >>> 32)));
    }
}
