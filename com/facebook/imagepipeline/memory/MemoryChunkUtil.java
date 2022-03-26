package com.facebook.imagepipeline.memory;

import com.facebook.common.internal.Preconditions;
/* loaded from: classes.dex */
public class MemoryChunkUtil {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int adjustByteCount(int offset, int count, int memorySize) {
        return Math.min(Math.max(0, memorySize - offset), count);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkBounds(int offset, int otherLength, int otherOffset, int count, int memorySize) {
        boolean z = true;
        Preconditions.checkArgument(count >= 0);
        Preconditions.checkArgument(offset >= 0);
        Preconditions.checkArgument(otherOffset >= 0);
        Preconditions.checkArgument(offset + count <= memorySize);
        if (otherOffset + count > otherLength) {
            z = false;
        }
        Preconditions.checkArgument(z);
    }
}
