package kotlin.internal;

import androidx.exifinterface.media.ExifInterface;
import kotlin.KotlinVersion;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.apache.commons.io.FilenameUtils;
/* compiled from: PlatformImplementations.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0004\u001a \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0005H\u0001\u001a\"\u0010\b\u001a\u0002H\t\"\n\b\u0000\u0010\t\u0018\u0001*\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0083\b¢\u0006\u0002\u0010\f\u001a\b\u0010\r\u001a\u00020\u0005H\u0002\"\u0010\u0010\u0000\u001a\u00020\u00018\u0000X\u0081\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"IMPLEMENTATIONS", "Lkotlin/internal/PlatformImplementations;", "apiVersionIsAtLeast", "", "major", "", "minor", "patch", "castToBaseType", ExifInterface.GPS_DIRECTION_TRUE, "", "instance", "(Ljava/lang/Object;)Ljava/lang/Object;", "getJavaVersion", "kotlin-stdlib"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class PlatformImplementationsKt {
    public static final PlatformImplementations IMPLEMENTATIONS;

    static {
        PlatformImplementations platformImplementations;
        Object newInstance;
        Object newInstance2;
        int version = getJavaVersion();
        if (version >= 65544) {
            try {
                newInstance = Class.forName("kotlin.internal.jdk8.JDK8PlatformImplementations").newInstance();
                Intrinsics.checkExpressionValueIsNotNull(newInstance, "Class.forName(\"kotlin.in…entations\").newInstance()");
            } catch (ClassNotFoundException e) {
                try {
                    Object newInstance3 = Class.forName("kotlin.internal.JRE8PlatformImplementations").newInstance();
                    Intrinsics.checkExpressionValueIsNotNull(newInstance3, "Class.forName(\"kotlin.in…entations\").newInstance()");
                    try {
                        if (newInstance3 != null) {
                            platformImplementations = (PlatformImplementations) newInstance3;
                        } else {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.internal.PlatformImplementations");
                        }
                    } catch (ClassCastException e2) {
                        ClassLoader classLoader = newInstance3.getClass().getClassLoader();
                        ClassLoader classLoader2 = PlatformImplementations.class.getClassLoader();
                        Throwable initCause = new ClassCastException("Instance classloader: " + classLoader + ", base type classloader: " + classLoader2).initCause(e2);
                        Intrinsics.checkExpressionValueIsNotNull(initCause, "ClassCastException(\"Inst…baseTypeCL\").initCause(e)");
                        throw initCause;
                    }
                } catch (ClassNotFoundException e3) {
                }
            }
            try {
                if (newInstance != null) {
                    platformImplementations = (PlatformImplementations) newInstance;
                    IMPLEMENTATIONS = platformImplementations;
                }
                throw new TypeCastException("null cannot be cast to non-null type kotlin.internal.PlatformImplementations");
            } catch (ClassCastException e4) {
                ClassLoader classLoader3 = newInstance.getClass().getClassLoader();
                ClassLoader classLoader4 = PlatformImplementations.class.getClassLoader();
                Throwable initCause2 = new ClassCastException("Instance classloader: " + classLoader3 + ", base type classloader: " + classLoader4).initCause(e4);
                Intrinsics.checkExpressionValueIsNotNull(initCause2, "ClassCastException(\"Inst…baseTypeCL\").initCause(e)");
                throw initCause2;
            }
        }
        if (version >= 65543) {
            try {
                newInstance2 = Class.forName("kotlin.internal.jdk7.JDK7PlatformImplementations").newInstance();
                Intrinsics.checkExpressionValueIsNotNull(newInstance2, "Class.forName(\"kotlin.in…entations\").newInstance()");
                try {
                } catch (ClassCastException e5) {
                    ClassLoader classLoader5 = newInstance2.getClass().getClassLoader();
                    ClassLoader classLoader6 = PlatformImplementations.class.getClassLoader();
                    Throwable initCause3 = new ClassCastException("Instance classloader: " + classLoader5 + ", base type classloader: " + classLoader6).initCause(e5);
                    Intrinsics.checkExpressionValueIsNotNull(initCause3, "ClassCastException(\"Inst…baseTypeCL\").initCause(e)");
                    throw initCause3;
                }
            } catch (ClassNotFoundException e6) {
                try {
                    Object newInstance4 = Class.forName("kotlin.internal.JRE7PlatformImplementations").newInstance();
                    Intrinsics.checkExpressionValueIsNotNull(newInstance4, "Class.forName(\"kotlin.in…entations\").newInstance()");
                    try {
                        if (newInstance4 != null) {
                            platformImplementations = (PlatformImplementations) newInstance4;
                        } else {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.internal.PlatformImplementations");
                        }
                    } catch (ClassCastException e7) {
                        ClassLoader classLoader7 = newInstance4.getClass().getClassLoader();
                        ClassLoader classLoader8 = PlatformImplementations.class.getClassLoader();
                        Throwable initCause4 = new ClassCastException("Instance classloader: " + classLoader7 + ", base type classloader: " + classLoader8).initCause(e7);
                        Intrinsics.checkExpressionValueIsNotNull(initCause4, "ClassCastException(\"Inst…baseTypeCL\").initCause(e)");
                        throw initCause4;
                    }
                } catch (ClassNotFoundException e8) {
                }
            }
            if (newInstance2 != null) {
                platformImplementations = (PlatformImplementations) newInstance2;
                IMPLEMENTATIONS = platformImplementations;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.internal.PlatformImplementations");
        }
        platformImplementations = new PlatformImplementations();
        IMPLEMENTATIONS = platformImplementations;
    }

    private static final /* synthetic */ <T> T castToBaseType(Object instance) {
        try {
            Intrinsics.reifiedOperationMarker(1, ExifInterface.GPS_DIRECTION_TRUE);
            return (T) instance;
        } catch (ClassCastException e) {
            ClassLoader instanceCL = instance.getClass().getClassLoader();
            Intrinsics.reifiedOperationMarker(4, ExifInterface.GPS_DIRECTION_TRUE);
            ClassLoader baseTypeCL = Object.class.getClassLoader();
            Throwable initCause = new ClassCastException("Instance classloader: " + instanceCL + ", base type classloader: " + baseTypeCL).initCause(e);
            Intrinsics.checkExpressionValueIsNotNull(initCause, "ClassCastException(\"Inst…baseTypeCL\").initCause(e)");
            throw initCause;
        }
    }

    private static final int getJavaVersion() {
        String version = System.getProperty("java.specification.version");
        if (version == null) {
            return 65542;
        }
        int firstDot = StringsKt.indexOf$default((CharSequence) version, (char) FilenameUtils.EXTENSION_SEPARATOR, 0, false, 6, (Object) null);
        if (firstDot < 0) {
            try {
                return 65536 * Integer.parseInt(version);
            } catch (NumberFormatException e) {
                return 65542;
            }
        } else {
            int secondDot = StringsKt.indexOf$default((CharSequence) version, (char) FilenameUtils.EXTENSION_SEPARATOR, firstDot + 1, false, 4, (Object) null);
            if (secondDot < 0) {
                secondDot = version.length();
            }
            if (version != null) {
                String firstPart = version.substring(0, firstDot);
                Intrinsics.checkExpressionValueIsNotNull(firstPart, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                int i = firstDot + 1;
                if (version != null) {
                    String secondPart = version.substring(i, secondDot);
                    Intrinsics.checkExpressionValueIsNotNull(secondPart, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    try {
                        return Integer.parseInt(secondPart) + (Integer.parseInt(firstPart) * 65536);
                    } catch (NumberFormatException e2) {
                        return 65542;
                    }
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
        }
    }

    public static final boolean apiVersionIsAtLeast(int major, int minor, int patch) {
        return KotlinVersion.CURRENT.isAtLeast(major, minor, patch);
    }
}
