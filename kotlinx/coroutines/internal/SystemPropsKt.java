package kotlinx.coroutines.internal;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
/* compiled from: SystemProps.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\u001a\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0000\u001a\u0018\u0010\u0004\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0007H\u0000\u001a,\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00012\b\b\u0002\u0010\t\u001a\u00020\u00012\b\b\u0002\u0010\n\u001a\u00020\u0001H\u0000\u001a,\u0010\u0004\u001a\u00020\u000b2\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u000b2\b\b\u0002\u0010\t\u001a\u00020\u000b2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0000\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003¨\u0006\f"}, d2 = {"AVAILABLE_PROCESSORS", "", "getAVAILABLE_PROCESSORS", "()I", "systemProp", "", "propertyName", "", "defaultValue", "minValue", "maxValue", "", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class SystemPropsKt {
    private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    public static final int getAVAILABLE_PROCESSORS() {
        return AVAILABLE_PROCESSORS;
    }

    public static final String systemProp(String propertyName) {
        Intrinsics.checkParameterIsNotNull(propertyName, "propertyName");
        try {
            return System.getProperty(propertyName);
        } catch (SecurityException e) {
            return null;
        }
    }

    public static final boolean systemProp(String propertyName, boolean defaultValue) {
        Intrinsics.checkParameterIsNotNull(propertyName, "propertyName");
        try {
            String property = System.getProperty(propertyName);
            return property != null ? Boolean.parseBoolean(property) : defaultValue;
        } catch (SecurityException e) {
            return defaultValue;
        }
    }

    public static /* synthetic */ int systemProp$default(String str, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 4) != 0) {
            i2 = 1;
        }
        if ((i4 & 8) != 0) {
            i3 = Integer.MAX_VALUE;
        }
        return systemProp(str, i, i2, i3);
    }

    public static final int systemProp(String propertyName, int defaultValue, int minValue, int maxValue) {
        Intrinsics.checkParameterIsNotNull(propertyName, "propertyName");
        return (int) systemProp(propertyName, (long) defaultValue, (long) minValue, (long) maxValue);
    }

    public static /* synthetic */ long systemProp$default(String str, long j, long j2, long j3, int i, Object obj) {
        long j4;
        long j5 = (i & 4) != 0 ? 1 : j2;
        if ((i & 8) != 0) {
            j4 = Long.MAX_VALUE;
        } else {
            j4 = j3;
        }
        return systemProp(str, j, j5, j4);
    }

    public static final long systemProp(String propertyName, long defaultValue, long minValue, long maxValue) {
        Intrinsics.checkParameterIsNotNull(propertyName, "propertyName");
        String value = systemProp(propertyName);
        if (value == null) {
            return defaultValue;
        }
        Long longOrNull = StringsKt.toLongOrNull(value);
        if (longOrNull != null) {
            long parsed = longOrNull.longValue();
            if (minValue <= parsed && maxValue >= parsed) {
                return parsed;
            }
            throw new IllegalStateException(("System property '" + propertyName + "' should be in range " + minValue + ".." + maxValue + ", but is '" + parsed + '\'').toString());
        }
        throw new IllegalStateException(("System property '" + propertyName + "' has unrecognized value '" + value + '\'').toString());
    }
}
