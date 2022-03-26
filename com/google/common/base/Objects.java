package com.google.common.base;

import java.util.Arrays;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public final class Objects extends ExtraObjectsMethodsForWeb {
    private Objects() {
    }

    public static boolean equal(@NullableDecl Object a2, @NullableDecl Object b) {
        return a2 == b || (a2 != null && a2.equals(b));
    }

    public static int hashCode(@NullableDecl Object... objects) {
        return Arrays.hashCode(objects);
    }
}
