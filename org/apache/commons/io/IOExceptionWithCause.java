package org.apache.commons.io;

import java.io.IOException;
@Deprecated
/* loaded from: classes3.dex */
public class IOExceptionWithCause extends IOException {
    private static final long serialVersionUID = 1;

    public IOExceptionWithCause(String message, Throwable cause) {
        super(message, cause);
    }

    public IOExceptionWithCause(Throwable cause) {
        super(cause);
    }
}
