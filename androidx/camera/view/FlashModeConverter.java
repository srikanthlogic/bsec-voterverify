package androidx.camera.view;
/* loaded from: classes.dex */
final class FlashModeConverter {
    private FlashModeConverter() {
    }

    public static int valueOf(String name) {
        if (name != null) {
            char c = 65535;
            int hashCode = name.hashCode();
            if (hashCode != 2527) {
                if (hashCode != 78159) {
                    if (hashCode == 2020783 && name.equals("AUTO")) {
                        c = 0;
                    }
                } else if (name.equals("OFF")) {
                    c = 2;
                }
            } else if (name.equals("ON")) {
                c = 1;
            }
            if (c == 0) {
                return 0;
            }
            if (c == 1) {
                return 1;
            }
            if (c == 2) {
                return 2;
            }
            throw new IllegalArgumentException("Unknown flash mode name " + name);
        }
        throw new NullPointerException("name cannot be null");
    }

    public static String nameOf(int flashMode) {
        if (flashMode == 0) {
            return "AUTO";
        }
        if (flashMode == 1) {
            return "ON";
        }
        if (flashMode == 2) {
            return "OFF";
        }
        throw new IllegalArgumentException("Unknown flash mode " + flashMode);
    }
}
