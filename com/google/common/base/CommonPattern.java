package com.google.common.base;
/* loaded from: classes.dex */
public abstract class CommonPattern {
    public abstract int flags();

    public abstract CommonMatcher matcher(CharSequence charSequence);

    public abstract String pattern();

    @Override // java.lang.Object
    public abstract String toString();

    public static CommonPattern compile(String pattern) {
        return Platform.compilePattern(pattern);
    }

    public static boolean isPcreLike() {
        return Platform.patternCompilerIsPcreLike();
    }
}
