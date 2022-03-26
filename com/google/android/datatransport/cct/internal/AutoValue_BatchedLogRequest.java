package com.google.android.datatransport.cct.internal;

import com.google.firebase.encoders.annotations.Encodable;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AutoValue_BatchedLogRequest extends BatchedLogRequest {
    private final List<LogRequest> logRequests;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoValue_BatchedLogRequest(List<LogRequest> logRequests) {
        if (logRequests != null) {
            this.logRequests = logRequests;
            return;
        }
        throw new NullPointerException("Null logRequests");
    }

    @Override // com.google.android.datatransport.cct.internal.BatchedLogRequest
    @Encodable.Field(name = "logRequest")
    public List<LogRequest> getLogRequests() {
        return this.logRequests;
    }

    public String toString() {
        return "BatchedLogRequest{logRequests=" + this.logRequests + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof BatchedLogRequest) {
            return this.logRequests.equals(((BatchedLogRequest) o).getLogRequests());
        }
        return false;
    }

    public int hashCode() {
        return (1 * 1000003) ^ this.logRequests.hashCode();
    }
}
