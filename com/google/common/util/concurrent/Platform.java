package com.google.common.util.concurrent;

import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes3.dex */
final class Platform {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isInstanceOfThrowableClass(@NullableDecl Throwable t, Class<? extends Throwable> expectedClass) {
        return expectedClass.isInstance(t);
    }

    private Platform() {
    }
}
