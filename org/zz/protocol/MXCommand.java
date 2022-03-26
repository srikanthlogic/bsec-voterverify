package org.zz.protocol;

import com.google.common.base.Ascii;
import com.google.common.primitives.SignedBytes;
/* loaded from: classes3.dex */
public class MXCommand {
    public static int VENDORID = 1046;
    public static int PRODUCTID = 20497;
    public static byte PACK_START_FLAG = 2;
    public static byte PACK_END_FLAG = 3;
    public static byte CMD_ID_RESET = 4;
    public static byte CMD_ID_FREE = 5;
    public static byte CMD_ID_SET_MODE = 6;
    public static byte CMD_ID_READ_STATUS = 7;
    public static byte CMD_ID_READ_ERROR = 8;
    public static byte CMD_ID_READ_DESCRIPT = 9;
    public static byte CMD_ID_ERASE = 10;
    public static byte CMD_ID_GET_MB = Ascii.VT;
    public static byte CMD_ID_GET_TZ = Ascii.FF;
    public static byte CMD_ID_DOWN_ID = Ascii.CR;
    public static byte CMD_ID_UP_ID = Ascii.SO;
    public static byte CMD_ID_DOWN_KEY = Ascii.SI;
    public static byte CMD_ID_SET_SECU = Ascii.DLE;
    public static byte CMD_ID_GET_IMG = Ascii.US;
    public static byte CMD_ID_GET_FULL_IMG = 47;
    public static byte CMD_ID_DETECT = 33;
    public static byte CMD_ID_GET_IMG_30 = 48;
    public static byte CMD_ID_GET_IMG_31 = 49;
    public static byte CMD_ID_LAMP_ONOFF = 60;
    public static byte CCB_CMD_UNIQUE_ID = SignedBytes.MAX_POWER_OF_TWO;
    public static byte CCB_CMD_IN_BASE64ENCODE = 87;
    public static byte CCB_CMD_OPEN_SENSOR = -126;
    public static byte CCB_CMD_CLOSE_SENSOR = -125;
    public static byte CCB_CMD_GET_STQC_IMAGE_85 = -123;
    public static byte CCB_CMD_GET_STQC_IMAGE_86 = -122;
    public static byte CCB_CMD_GET_CURRENT_MODEL = -121;
    public static byte CCB_CMD_SET_CURRENT_MODEL = -120;
    public static byte CCB_CMD_FORMAT_Z32 = 55;
    public static byte CCB_CMD_GENRANDOMDATA = 65;
    public static byte CCB_CMD_IMPORTCERTIFICATE = 66;
    public static byte CCB_CMD_EXPORTCERTIFICATE = 67;
    public static byte CCB_CMD_ENCRYPT = 68;
    public static byte CCB_CMD_DECRYPT = 69;
    public static byte CCB_CMD_DIGEST = 70;
    public static byte CCB_CMD_GEN_RSAKEYPAIR = 71;
    public static byte CCB_CMD_OUT_RSAKEYPAIR = 72;
    public static byte CCB_CMD_IN_RSAKEYPAIR = 73;
    public static byte CCB_CMD_RSA_PUBKEY_EN = 74;
    public static byte CCB_CMD_RSA_PRIKEY_DE = 75;
    public static byte CCB_CMD_RSA_PRIKEY_EN = 76;
    public static byte CCB_CMD_RSA_PUBKEY_DE = 77;
    public static byte CCB_CMD_RSA_EXTPUBKEY_EN = 78;
    public static byte CCB_CMD_RSA_EXTPUBKEY_DE = 79;
    public static byte CCB_CMD_SET_PIN = 80;
    public static byte CCB_CMD_CHANGE_PIN = 81;
    public static byte CCB_CMD_VERIFY_PIN = 82;
    public static byte CCB_CMD_OUT_RSAPUBKEY = 83;
    public static byte CCB_CMD_SIGNDATA = 84;
    public static byte CCB_CMD_VERIFYSIGN = 85;
    public static byte CCB_CMD_GETHMAC = 86;
    public static byte CCB_CMD_AESGCM_EN = 88;
    public static byte CCB_CMD_AESGCM_DE = 89;
    public static byte L1_CMD_CAPTURE = -112;
    public static byte L1_CMD_CANCEL_CAPTURE = -111;
    public static byte L1_CMD_SET_DEVICEMODE = -110;
    public static byte L1_CMD_GET_DEVICEMODE = -109;
    public static byte CCB_CMD_GETVERSION_Z32 = 52;
    public static byte CCB_CMD_GET_Z32UUID = -108;
    public static byte CCB_CMD_GET_PUBLIC_KEY = 112;
    public static byte CCB_CMD_DOWN_ENC_SESSION_KEY_Z32 = 113;
    public static byte CCB_CMD_DOWN_ENC_SESSION_KEY = 114;
    public static byte CMD_SET_ENCRYPTMODE = -107;
    public static byte CMD_GET_ENCRYPTMODE = -106;
    public static byte L1_CCB_GET_STQCIMG_EX = -105;
    public static byte CCB_CMD_READ_OTP = -114;
    public static byte CCB_CMD_PRODUCT_KEY_SET = 102;
    public static byte CMD_GET_DEV_INFO = -103;
    public static byte CMD_DEV_GET_FMR_DATA = -104;
}
