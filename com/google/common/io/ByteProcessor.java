package com.google.common.io;

import com.google.errorprone.annotations.DoNotMock;
import java.io.IOException;
@DoNotMock("Implement it normally")
/* loaded from: classes3.dex */
public interface ByteProcessor<T> {
    T getResult();

    boolean processBytes(byte[] bArr, int i, int i2) throws IOException;
}
