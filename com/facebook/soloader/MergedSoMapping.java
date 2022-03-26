package com.facebook.soloader;

import javax.annotation.Nullable;
/* loaded from: classes.dex */
class MergedSoMapping {
    MergedSoMapping() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public static String mapLibName(String preMergedLibName) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void invokeJniOnload(String preMergedLibName) {
        throw new IllegalArgumentException("Unknown library: " + preMergedLibName);
    }
}
