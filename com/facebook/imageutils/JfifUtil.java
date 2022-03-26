package com.facebook.imageutils;

import androidx.recyclerview.widget.ItemTouchHelper;
import com.alcorlink.camera.AlErrorCode;
import com.facebook.common.internal.Preconditions;
import com.sec.biometric.license.SecBiometricLicenseManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public class JfifUtil {
    public static final int APP1_EXIF_MAGIC = 1165519206;
    public static final int MARKER_APP1 = 225;
    public static final int MARKER_EOI = 217;
    public static final int MARKER_ESCAPE_BYTE = 0;
    public static final int MARKER_FIRST_BYTE = 255;
    public static final int MARKER_RST0 = 208;
    public static final int MARKER_RST7 = 215;
    public static final int MARKER_SOFn = 192;
    public static final int MARKER_SOI = 216;
    public static final int MARKER_SOS = 218;
    public static final int MARKER_TEM = 1;

    private JfifUtil() {
    }

    public static int getAutoRotateAngleFromOrientation(int orientation) {
        return TiffUtil.getAutoRotateAngleFromOrientation(orientation);
    }

    public static int getOrientation(byte[] jpeg) {
        return getOrientation(new ByteArrayInputStream(jpeg));
    }

    public static int getOrientation(InputStream is) {
        try {
            int length = moveToAPP1EXIF(is);
            if (length == 0) {
                return 0;
            }
            return TiffUtil.readOrientationFromTIFF(is, length);
        } catch (IOException e) {
            return 0;
        }
    }

    public static boolean moveToMarker(InputStream is, int markerToFind) throws IOException {
        Preconditions.checkNotNull(is);
        while (StreamProcessor.readPackedInt(is, 1, false) == 255) {
            int marker = 255;
            while (marker == 255) {
                marker = StreamProcessor.readPackedInt(is, 1, false);
            }
            if ((markerToFind == 192 && isSOFn(marker)) || marker == markerToFind) {
                return true;
            }
            if (!(marker == 216 || marker == 1)) {
                if (marker == 217 || marker == 218) {
                    return false;
                }
                is.skip((long) (StreamProcessor.readPackedInt(is, 2, false) - 2));
            }
        }
        return false;
    }

    private static boolean isSOFn(int marker) {
        switch (marker) {
            case 192:
            case AlErrorCode.ERR_NO_DEVICE:
            case AlErrorCode.ERR_INVALID_DEVICE:
            case AlErrorCode.ERR_NO_SPACE:
            case AlErrorCode.ERR_NOT_INIT:
            case AlErrorCode.ERR_TRANSFER:
            case AlErrorCode.ERR_NOT_FOUND:
            case SecBiometricLicenseManager.ERROR_INVALID_LICENSE:
            case 202:
            case SecBiometricLicenseManager.ERROR_LICENSE_TERMINATED:
            case 205:
            case 206:
            case 207:
                return true;
            case AlErrorCode.ERR_IN_USE:
            case ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION /* 200 */:
            case SecBiometricLicenseManager.ERROR_INVALID_PACKAGE_NAME:
            default:
                return false;
        }
    }

    private static int moveToAPP1EXIF(InputStream is) throws IOException {
        int length;
        if (moveToMarker(is, MARKER_APP1) && (length = StreamProcessor.readPackedInt(is, 2, false) - 2) > 6) {
            int magic = StreamProcessor.readPackedInt(is, 4, false);
            int zero = StreamProcessor.readPackedInt(is, 2, false);
            int length2 = (length - 4) - 2;
            if (magic == 1165519206 && zero == 0) {
                return length2;
            }
        }
        return 0;
    }
}
