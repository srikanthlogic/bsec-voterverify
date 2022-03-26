package androidx.core.util;

import android.os.Build;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public class ObjectsCompat {
    private ObjectsCompat() {
    }

    public static boolean equals(Object a2, Object b) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Objects.equals(a2, b);
        }
        return a2 == b || (a2 != null && a2.equals(b));
    }

    public static int hashCode(Object o) {
        if (o != null) {
            return o.hashCode();
        }
        return 0;
    }

    public static int hash(Object... values) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Objects.hash(values);
        }
        return Arrays.hashCode(values);
    }

    public static String toString(Object o, String nullDefault) {
        return o != null ? o.toString() : nullDefault;
    }
}
