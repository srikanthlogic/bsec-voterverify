package com.google.common.escape;

import com.google.common.base.Function;
import com.google.errorprone.annotations.DoNotMock;
@DoNotMock("Use Escapers.nullEscaper() or another methods from the *Escapers classes")
/* loaded from: classes.dex */
public abstract class Escaper {
    private final Function<String, String> asFunction = new Function<String, String>() { // from class: com.google.common.escape.Escaper.1
        public String apply(String from) {
            return Escaper.this.escape(from);
        }
    };

    public abstract String escape(String str);

    public final Function<String, String> asFunction() {
        return this.asFunction;
    }
}
