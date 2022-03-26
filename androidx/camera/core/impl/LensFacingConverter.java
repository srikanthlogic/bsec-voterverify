package androidx.camera.core.impl;
/* loaded from: classes.dex */
public class LensFacingConverter {
    private LensFacingConverter() {
    }

    public static Integer[] values() {
        return new Integer[]{0, 1};
    }

    public static int valueOf(String name) {
        if (name != null) {
            char c = 65535;
            int hashCode = name.hashCode();
            if (hashCode != 2030823) {
                if (hashCode == 67167753 && name.equals("FRONT")) {
                    c = 0;
                }
            } else if (name.equals("BACK")) {
                c = 1;
            }
            if (c == 0) {
                return 0;
            }
            if (c == 1) {
                return 1;
            }
            throw new IllegalArgumentException("Unknown len facing name " + name);
        }
        throw new NullPointerException("name cannot be null");
    }

    public static String nameOf(int lensFacing) {
        if (lensFacing == 0) {
            return "FRONT";
        }
        if (lensFacing == 1) {
            return "BACK";
        }
        throw new IllegalArgumentException("Unknown lens facing " + lensFacing);
    }
}
