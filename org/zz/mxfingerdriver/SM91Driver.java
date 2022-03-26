package org.zz.mxfingerdriver;

import android.content.Context;
import android.os.Handler;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import com.nitgen.SDK.AndroidBSP.NBioBSPJNI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.zz.cipher.RSAEncrypt;
import org.zz.protocol.MXCommand;
import org.zz.tool.LogUnit;
/* loaded from: classes3.dex */
public class SM91Driver {
    private byte g_p2;
    private byte[] g_sessionkey;
    private Handler m_fHandler;
    MXVirtualComProtocol m_virtualCom;

    public SM91Driver(Context context) {
        this.m_fHandler = null;
        this.g_p2 = 0;
        this.g_sessionkey = new byte[16];
        this.m_fHandler = null;
        this.m_virtualCom = new MXVirtualComProtocol(context);
    }

    public SM91Driver(Context context, Handler bioHandler) {
        this.m_fHandler = null;
        this.g_p2 = 0;
        this.g_sessionkey = new byte[16];
        this.m_fHandler = bioHandler;
        this.m_virtualCom = new MXVirtualComProtocol(context, this.m_fHandler);
        LogUnit.SetHandler(bioHandler);
    }

    public void unRegUsbMonitor() {
        this.m_virtualCom.unRegUsbMonitor();
    }

    public int zzOpenDev() {
        LogUnit.SendMsg("zzOpenDev");
        int nRet = this.m_virtualCom.zzOpenDev(MXCommand.VENDORID, MXCommand.PRODUCTID, 1);
        if (nRet != 0) {
            return nRet;
        }
        return nRet;
    }

    public int zzCloseDev() {
        return this.m_virtualCom.zzCloseDev();
    }

    public int zzSendEndpointSize() {
        return this.m_virtualCom.sendEndpointSize();
    }

    public int zzRecvEndpointSize() {
        return this.m_virtualCom.recvEndpointSize();
    }

    public int zzUpImage(byte[] bImgBuf, int[] iImgWidth, int[] iImgHeigth, int iTimeOut) {
        int nRet = zzUpImageWithCmd(MXCommand.CMD_ID_GET_IMG_31, bImgBuf, iImgWidth, iImgHeigth, iTimeOut);
        if (nRet == 15) {
            return zzUpImageWithCmd(MXCommand.CMD_ID_GET_IMG_31, bImgBuf, iImgWidth, iImgHeigth, iTimeOut);
        }
        return nRet;
    }

    public int zzUpImage30(byte[] bImgBuf, int[] iImgWidth, int[] iImgHeigth, int iTimeOut) {
        int nRet = zzUpImageWithCmd(MXCommand.CMD_ID_GET_IMG_30, bImgBuf, iImgWidth, iImgHeigth, iTimeOut);
        if (nRet == 15) {
            return zzUpImageWithCmd(MXCommand.CMD_ID_GET_IMG_30, bImgBuf, iImgWidth, iImgHeigth, iTimeOut);
        }
        return nRet;
    }

    public int zzUpImage85(byte[] bImgBuf, int[] iImgWidth, int[] iImgHeigth, int iTimeOut) {
        int nRet = zzUpImageWithCmd(MXCommand.CCB_CMD_GET_STQC_IMAGE_85, bImgBuf, iImgWidth, iImgHeigth, iTimeOut);
        if (nRet == 15) {
            return zzUpImageWithCmd(MXCommand.CCB_CMD_GET_STQC_IMAGE_85, bImgBuf, iImgWidth, iImgHeigth, iTimeOut);
        }
        return nRet;
    }

    public int zzUpImage86(byte[] bImgBuf, int[] iImgWidth, int[] iImgHeigth, int iTimeOut) {
        int nRet = zzUpImageWithCmd(MXCommand.CCB_CMD_GET_STQC_IMAGE_86, bImgBuf, iImgWidth, iImgHeigth, iTimeOut);
        if (nRet == 15) {
            return zzUpImageWithCmd(MXCommand.CCB_CMD_GET_STQC_IMAGE_86, bImgBuf, iImgWidth, iImgHeigth, iTimeOut);
        }
        return nRet;
    }

    public int zzUpImageWithCmd(byte bCmd, byte[] bImgBuf, int[] iImgWidth, int[] iImgHeigth, int iTimeOut) {
        LogUnit.SendMsg("bCmd = " + ((int) bCmd));
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(bCmd, null, 0, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        int nRet2 = this.m_virtualCom.zzRecvImagePacketP2(bCmd, bResult, bImgBuf, iImgWidth, iImgHeigth, iTimeOut, (byte) 0, this.g_sessionkey);
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        if (nRet2 == 41) {
            zzDevFree();
        }
        if (nRet2 != 0) {
            return nRet2;
        }
        return bResult[0];
    }

    public int zzUpImageL0(byte[] bImgBuf, int[] iImgWidth, int[] iImgHeigth, int iTimeOut) {
        byte bCmd = MXCommand.L1_CCB_GET_STQCIMG_EX;
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(bCmd, new byte[]{(byte) (iTimeOut >> 24), (byte) (iTimeOut >> 16), (byte) (iTimeOut >> 8), (byte) iTimeOut}, 4, this.g_p2, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        int nRet2 = this.m_virtualCom.zzRecvImagePacketP2(bCmd, bResult, bImgBuf, iImgWidth, iImgHeigth, iTimeOut, this.g_p2, this.g_sessionkey);
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        if (nRet2 == 41) {
            zzDevFree();
        }
        if (nRet2 != 0) {
            return nRet2;
        }
        return bResult[0];
    }

    public void cancelUpImageL0() {
        this.m_virtualCom.cancelRecv();
    }

    public int zzGetDevVersion(byte[] bVersion) {
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CMD_ID_READ_DESCRIPT, null, 0, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketP2(bResult, bRecvBuf, iRecvDataLen, 1000, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            byte[] bTmp = new byte[iRecvDataLen[0]];
            for (int i = 0; i < iRecvDataLen[0]; i++) {
                bTmp[i] = bRecvBuf[i];
                bVersion[i] = bRecvBuf[i];
            }
            bVersion[iRecvDataLen[0]] = 0;
            LogUnit.SendMsg(new String(bTmp));
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public static int bytes2int(byte[] bytes) {
        if (bytes.length != 4) {
            return 0;
        }
        return (bytes[0] & 255) | ((bytes[1] & 255) << 8) | ((bytes[2] & 255) << 16) | ((bytes[3] & 255) << 24);
    }

    public int zzGetDevInfo(int[] deviceId, int[] imageWidth, int[] imageHeight, int[] ppi, int[] hwVer, int[] fwVer) {
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CMD_GET_DEV_INFO, null, 0, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketP2(bResult, bRecvBuf, iRecvDataLen, 1000, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            byte[] Temp = new byte[4];
            System.arraycopy(bRecvBuf, 0, Temp, 0, 4);
            deviceId[0] = bytes2int(Temp);
            System.arraycopy(bRecvBuf, 4, Temp, 0, 4);
            imageWidth[0] = bytes2int(Temp);
            System.arraycopy(bRecvBuf, 8, Temp, 0, 4);
            imageHeight[0] = bytes2int(Temp);
            System.arraycopy(bRecvBuf, 12, Temp, 0, 4);
            ppi[0] = bytes2int(Temp);
            System.arraycopy(bRecvBuf, 16, Temp, 0, 4);
            hwVer[0] = bytes2int(Temp);
            System.arraycopy(bRecvBuf, 20, Temp, 0, 4);
            fwVer[0] = bytes2int(Temp);
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzGetDevZ32UUID(byte[] bVersion) {
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_GET_Z32UUID, null, 0, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketP2(bResult, bRecvBuf, iRecvDataLen, 1000, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            for (int i = 0; i < iRecvDataLen[0]; i++) {
                bVersion[i] = bRecvBuf[i];
            }
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzGetDevZ32Version(byte[] bVersion) {
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_GETVERSION_Z32, null, 0, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketP2(bResult, bRecvBuf, iRecvDataLen, 1000, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            byte[] bTmp = new byte[iRecvDataLen[0]];
            for (int i = 0; i < iRecvDataLen[0]; i++) {
                bTmp[i] = bRecvBuf[i];
                bVersion[i] = bRecvBuf[i];
            }
            bVersion[iRecvDataLen[0]] = 0;
            LogUnit.SendMsg(new String(bTmp));
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public byte[] getMD5(byte[] srcBuf) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(srcBuf);
        return md.digest();
    }

    public int zzGetDevUUID(byte[] bVersion) {
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_UNIQUE_ID, null, 0, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketP2(bResult, bRecvBuf, iRecvDataLen, 1000, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            for (int i = 0; i < iRecvDataLen[0]; i++) {
                bVersion[i] = bRecvBuf[i];
            }
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzGetDevSerial(byte[] bVersion) {
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CMD_ID_UP_ID, null, 0, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketP2(bResult, bRecvBuf, iRecvDataLen, 1000, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            byte[] bTmp = new byte[iRecvDataLen[0]];
            for (int i = 0; i < iRecvDataLen[0]; i++) {
                bTmp[i] = bRecvBuf[i];
                bVersion[i] = bRecvBuf[i];
            }
            bVersion[iRecvDataLen[0]] = 0;
            LogUnit.SendMsg(new String(bTmp));
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzDevFree() {
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CMD_ID_FREE, null, 0, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketP2(bResult, bRecvBuf, iRecvDataLen, 100, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            byte[] bTmp = new byte[iRecvDataLen[0]];
            for (int i = 0; i < iRecvDataLen[0]; i++) {
                bTmp[i] = bRecvBuf[i];
            }
            LogUnit.SendMsg(new String(bTmp));
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzSetDevMode(int iDevMode) {
        LogUnit.SendMsg("zzSendPacket");
        byte[] szTmp = new byte[2];
        szTmp[0] = (byte) iDevMode;
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_SET_CURRENT_MODEL, szTmp, 1, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketP2(bResult, bRecvBuf, iRecvDataLen, 100, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            byte[] bTmp = new byte[iRecvDataLen[0]];
            for (int i = 0; i < iRecvDataLen[0]; i++) {
                bTmp[i] = bRecvBuf[i];
            }
            LogUnit.SendMsg(new String(bTmp));
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int zzGetDevMode(int[] iDevMode) {
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_GET_CURRENT_MODEL, null, 0, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketP2(bResult, bRecvBuf, iRecvDataLen, 100, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            iDevMode[0] = bRecvBuf[0];
            byte[] bTmp = new byte[iRecvDataLen[0]];
            for (int i = 0; i < iRecvDataLen[0]; i++) {
                bTmp[i] = bRecvBuf[i];
            }
            LogUnit.SendMsg(new String(bTmp));
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzDevLampOnOff(int iOnOff) {
        LogUnit.SendMsg("zzSendPacket");
        byte[] szTmp = new byte[2];
        szTmp[0] = (byte) iOnOff;
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CMD_ID_LAMP_ONOFF, szTmp, 1, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketP2(bResult, bRecvBuf, iRecvDataLen, 100, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            byte[] bTmp = new byte[iRecvDataLen[0]];
            for (int i = 0; i < iRecvDataLen[0]; i++) {
                bTmp[i] = bRecvBuf[i];
            }
            LogUnit.SendMsg(new String(bTmp));
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzDevSensorOpen() {
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_OPEN_SENSOR, null, 0, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketP2(bResult, bRecvBuf, iRecvDataLen, 100, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            byte[] bTmp = new byte[iRecvDataLen[0]];
            for (int i = 0; i < iRecvDataLen[0]; i++) {
                bTmp[i] = bRecvBuf[i];
            }
            LogUnit.SendMsg(new String(bTmp));
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzDevSensorClose() {
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_CLOSE_SENSOR, null, 0, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketP2(bResult, bRecvBuf, iRecvDataLen, 100, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            byte[] bTmp = new byte[iRecvDataLen[0]];
            for (int i = 0; i < iRecvDataLen[0]; i++) {
                bTmp[i] = bRecvBuf[i];
            }
            LogUnit.SendMsg(new String(bTmp));
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzBase64Encode(byte[] inputData, int inputDataLen, byte[] OutputSignedddata, int[] OutputSigneddatalen) {
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_IN_BASE64ENCODE, inputData, inputDataLen, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketWaitP2(bResult, bRecvBuf, iRecvDataLen, 1000, true, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            OutputSigneddatalen[0] = iRecvDataLen[0];
            for (int i = 0; i < iRecvDataLen[0]; i++) {
                OutputSignedddata[i] = bRecvBuf[i];
            }
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzGetRandomData(int InputRandomDataLen, byte[] OutputRandomData, int[] OutputRandomDataLen) {
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_GENRANDOMDATA, new byte[]{(byte) (InputRandomDataLen % 256), (byte) (InputRandomDataLen / 256)}, 2, this.g_p2, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketWaitP2(bResult, bRecvBuf, iRecvDataLen, 1000, true, this.g_p2, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            OutputRandomDataLen[0] = iRecvDataLen[0];
            for (int i = 0; i < iRecvDataLen[0]; i++) {
                OutputRandomData[i] = bRecvBuf[i];
            }
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzDigest(byte bAlgType, byte[] bDataBuf, int iDataLen, byte[] bDigestContent, int[] iDigestLen) {
        LogUnit.SendMsg("zzSendPacket");
        int iSendLen = iDataLen + 5;
        byte[] bSendData = new byte[iSendLen];
        bSendData[0] = bAlgType;
        byte[] bDataLen = intToByteArray(iDataLen);
        for (int i = 0; i < 4; i++) {
            bSendData[i + 1] = bDataLen[i];
        }
        for (int i2 = 0; i2 < iDataLen; i2++) {
            bSendData[i2 + 5] = bDataBuf[i2];
        }
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_DIGEST, bSendData, iSendLen, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketWaitP2(bResult, bRecvBuf, iRecvDataLen, 1000, true, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            iDigestLen[0] = byteArrayToInt(bRecvBuf, 0);
            for (int i3 = 0; i3 < iRecvDataLen[0] - 4; i3++) {
                bDigestContent[i3] = bRecvBuf[i3 + 4];
            }
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzAES_GCM_NOPADDING_Encryption(byte[] key, int keyLen, byte[] InputData, int inputDataLen, byte[] iv, int ivDataLen, byte[] aad, int aadDataLen, byte[] OutEncryptData, int[] OutEncyrptedDataLen) {
        LogUnit.SendMsg("zzSendPacket");
        int iSendLen = keyLen + 4 + 4 + inputDataLen + 4 + ivDataLen + 4 + aadDataLen;
        byte[] bSendData = new byte[iSendLen];
        byte[] bkeyLen = intToByteArray(keyLen);
        for (int i = 0; i < 4; i++) {
            bSendData[0 + i] = bkeyLen[i];
        }
        int iOffsize = 0 + 4;
        for (int i2 = 0; i2 < keyLen; i2++) {
            bSendData[iOffsize + i2] = key[i2];
        }
        int iOffsize2 = iOffsize + keyLen;
        byte[] binputDataLen = intToByteArray(inputDataLen);
        for (int i3 = 0; i3 < 4; i3++) {
            bSendData[iOffsize2 + i3] = binputDataLen[i3];
        }
        int iOffsize3 = iOffsize2 + 4;
        for (int i4 = 0; i4 < inputDataLen; i4++) {
            bSendData[iOffsize3 + i4] = InputData[i4];
        }
        int iOffsize4 = iOffsize3 + inputDataLen;
        byte[] bivDataLen = intToByteArray(ivDataLen);
        for (int i5 = 0; i5 < 4; i5++) {
            bSendData[iOffsize4 + i5] = bivDataLen[i5];
        }
        int iOffsize5 = iOffsize4 + 4;
        for (int i6 = 0; i6 < ivDataLen; i6++) {
            bSendData[iOffsize5 + i6] = iv[i6];
        }
        int iOffsize6 = iOffsize5 + ivDataLen;
        byte[] baadDataLen = intToByteArray(aadDataLen);
        for (int i7 = 0; i7 < 4; i7++) {
            bSendData[iOffsize6 + i7] = baadDataLen[i7];
        }
        int iOffsize7 = iOffsize6 + 4;
        for (int i8 = 0; i8 < aadDataLen; i8++) {
            bSendData[iOffsize7 + i8] = aad[i8];
        }
        int i9 = iOffsize7 + aadDataLen;
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_AESGCM_EN, bSendData, iSendLen, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[iSendLen + 64];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketWaitP2(bResult, bRecvBuf, iRecvDataLen, PathInterpolatorCompat.MAX_NUM_POINTS, true, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            OutEncyrptedDataLen[0] = byteArrayToInt(bRecvBuf, 0);
            for (int i10 = 0; i10 < iRecvDataLen[0] - 4; i10++) {
                OutEncryptData[i10] = bRecvBuf[i10 + 4];
            }
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzAES_GCM_NOPADDING_Decryption(byte[] key, int keyLen, byte[] InputData, int inputDataLen, byte[] iv, int ivDataLen, byte[] aad, int aadDataLen, byte[] OutDecryptData, int[] OutDecyrptedDataLen) {
        LogUnit.SendMsg("zzSendPacket");
        int iSendLen = keyLen + 4 + 4 + inputDataLen + 4 + ivDataLen + 4 + aadDataLen;
        byte[] bSendData = new byte[iSendLen];
        byte[] bkeyLen = intToByteArray(keyLen);
        for (int i = 0; i < 4; i++) {
            bSendData[0 + i] = bkeyLen[i];
        }
        int iOffsize = 0 + 4;
        for (int i2 = 0; i2 < keyLen; i2++) {
            bSendData[iOffsize + i2] = key[i2];
        }
        int iOffsize2 = iOffsize + keyLen;
        byte[] binputDataLen = intToByteArray(inputDataLen);
        for (int i3 = 0; i3 < 4; i3++) {
            bSendData[iOffsize2 + i3] = binputDataLen[i3];
        }
        int iOffsize3 = iOffsize2 + 4;
        for (int i4 = 0; i4 < inputDataLen; i4++) {
            bSendData[iOffsize3 + i4] = InputData[i4];
        }
        int iOffsize4 = iOffsize3 + inputDataLen;
        byte[] bivDataLen = intToByteArray(ivDataLen);
        for (int i5 = 0; i5 < 4; i5++) {
            bSendData[iOffsize4 + i5] = bivDataLen[i5];
        }
        int iOffsize5 = iOffsize4 + 4;
        for (int i6 = 0; i6 < ivDataLen; i6++) {
            bSendData[iOffsize5 + i6] = iv[i6];
        }
        int iOffsize6 = iOffsize5 + ivDataLen;
        byte[] baadDataLen = intToByteArray(aadDataLen);
        for (int i7 = 0; i7 < 4; i7++) {
            bSendData[iOffsize6 + i7] = baadDataLen[i7];
        }
        int iOffsize7 = iOffsize6 + 4;
        for (int i8 = 0; i8 < aadDataLen; i8++) {
            bSendData[iOffsize7 + i8] = aad[i8];
        }
        int i9 = iOffsize7 + aadDataLen;
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_AESGCM_DE, bSendData, iSendLen, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[iSendLen + 64];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketWaitP2(bResult, bRecvBuf, iRecvDataLen, PathInterpolatorCompat.MAX_NUM_POINTS, true, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            OutDecyrptedDataLen[0] = byteArrayToInt(bRecvBuf, 0);
            for (int i10 = 0; i10 < iRecvDataLen[0] - 4; i10++) {
                OutDecryptData[i10] = bRecvBuf[i10 + 4];
            }
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzSymmetryEncrypt(byte bAlgType, byte[] bKeyData, byte bKeyLen, byte[] bDataBuf, int iDataLen, byte[] bEnData, int[] bEnDataLen) {
        if (bKeyLen != 32) {
            return 19;
        }
        if (iDataLen % 16 != 0) {
            return 20;
        }
        LogUnit.SendMsg("zzSendPacket");
        int iSendLen = bKeyLen + 2 + 4 + iDataLen;
        byte[] bSendData = new byte[iSendLen];
        bSendData[0] = bAlgType;
        bSendData[1] = bKeyLen;
        for (int i = 0; i < bKeyLen; i++) {
            bSendData[i + 2] = bKeyData[i];
        }
        byte[] bDataLen = intToByteArray(iDataLen);
        for (int i2 = 0; i2 < 4; i2++) {
            bSendData[bKeyLen + 2 + i2] = bDataLen[i2];
        }
        for (int i3 = 0; i3 < iDataLen; i3++) {
            bSendData[bKeyLen + 6 + i3] = bDataBuf[i3];
        }
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_ENCRYPT, bSendData, iSendLen, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[iDataLen + 64];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketWaitP2(bResult, bRecvBuf, iRecvDataLen, 1000, true, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            bEnDataLen[0] = byteArrayToInt(bRecvBuf, 0);
            for (int i4 = 0; i4 < iRecvDataLen[0] - 4; i4++) {
                bEnData[i4] = bRecvBuf[i4 + 4];
            }
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzSymmetryDecrypt(byte bAlgType, byte[] bKeyData, byte bKeyLen, byte[] bDataBuf, int iDataLen, byte[] bDeData, int[] bDeDataLen) {
        if (bKeyLen != 32) {
            return 19;
        }
        LogUnit.SendMsg("zzSendPacket");
        int iSendLen = bKeyLen + 2 + 4 + iDataLen;
        byte[] bSendData = new byte[iSendLen];
        bSendData[0] = bAlgType;
        bSendData[1] = bKeyLen;
        for (int i = 0; i < bKeyLen; i++) {
            bSendData[i + 2] = bKeyData[i];
        }
        byte[] bDataLen = intToByteArray(iDataLen);
        for (int i2 = 0; i2 < 4; i2++) {
            bSendData[bKeyLen + 2 + i2] = bDataLen[i2];
        }
        for (int i3 = 0; i3 < iDataLen; i3++) {
            bSendData[bKeyLen + 6 + i3] = bDataBuf[i3];
        }
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_DECRYPT, bSendData, iSendLen, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[iDataLen + 64];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketWaitP2(bResult, bRecvBuf, iRecvDataLen, 1000, true, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            bDeDataLen[0] = byteArrayToInt(bRecvBuf, 0);
            for (int i4 = 0; i4 < iRecvDataLen[0] - 4; i4++) {
                bDeData[i4] = bRecvBuf[i4 + 4];
            }
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzGenHMAC(byte[] bKeyData, int iKeyLen, byte[] bDataBuf, int iDataLen, byte[] bHMACData, int[] bHMACDataLen) {
        LogUnit.SendMsg("zzSendPacket");
        int iSendLen = iKeyLen + 4 + 4 + iDataLen;
        if (iSendLen > 2048) {
            return 15;
        }
        byte[] bSendData = new byte[iSendLen];
        byte[] bKeyLen = intToByteArray(iKeyLen);
        for (int i = 0; i < 4; i++) {
            bSendData[i] = bKeyLen[i];
        }
        for (int i2 = 0; i2 < iKeyLen; i2++) {
            bSendData[i2 + 4] = bKeyData[i2];
        }
        byte[] bDataLen = intToByteArray(iDataLen);
        for (int i3 = 0; i3 < 4; i3++) {
            bSendData[iKeyLen + 4 + i3] = bDataLen[i3];
        }
        for (int i4 = 0; i4 < iDataLen; i4++) {
            bSendData[iKeyLen + 8 + i4] = bDataBuf[i4];
        }
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_GETHMAC, bSendData, iSendLen, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[iDataLen + 64];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketWaitP2(bResult, bRecvBuf, iRecvDataLen, 1000, true, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            bHMACDataLen[0] = byteArrayToInt(bRecvBuf, 0);
            for (int i5 = 0; i5 < iRecvDataLen[0] - 4; i5++) {
                bHMACData[i5] = bRecvBuf[i5 + 4];
            }
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzGenRSAKeyPair(byte bKeyIndex) {
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_GEN_RSAKEYPAIR, new byte[]{bKeyIndex}, 1, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketWaitP2(bResult, bRecvBuf, iRecvDataLen, 17000, false, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            byte[] bTmp = new byte[iRecvDataLen[0]];
            for (int i = 0; i < iRecvDataLen[0]; i++) {
                bTmp[i] = bRecvBuf[i];
            }
            LogUnit.SendMsg(new String(bTmp));
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzExportRSAPubKey(byte bKeyIndex, byte[] bPubKeyData, int[] iPubKeyDataLen) {
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_OUT_RSAPUBKEY, new byte[]{bKeyIndex}, 1, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketWaitP2(bResult, bRecvBuf, iRecvDataLen, 1000, true, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            iPubKeyDataLen[0] = byteArrayToInt(bRecvBuf, 0);
            for (int i = 0; i < iRecvDataLen[0] - 4; i++) {
                bPubKeyData[i] = bRecvBuf[i + 4];
            }
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzSignData(byte bKeyIndex, byte[] PreSignData, int PreSignDataLen, byte[] SignData, int[] SignDataLen) {
        if (PreSignDataLen > 245) {
            return 20;
        }
        LogUnit.SendMsg("zzSendPacket");
        int iSendLen = PreSignDataLen + 5;
        byte[] bSendData = new byte[iSendLen];
        bSendData[0] = bKeyIndex;
        byte[] bDataLen = intToByteArray(PreSignDataLen);
        for (int i = 0; i < 4; i++) {
            bSendData[i + 1] = bDataLen[i];
        }
        for (int i2 = 0; i2 < PreSignDataLen; i2++) {
            bSendData[i2 + 5] = PreSignData[i2];
        }
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_SIGNDATA, bSendData, iSendLen, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketWaitP2(bResult, bRecvBuf, iRecvDataLen, 1000, true, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            SignDataLen[0] = byteArrayToInt(bRecvBuf, 0);
            for (int i3 = 0; i3 < iRecvDataLen[0] - 4; i3++) {
                SignData[i3] = bRecvBuf[i3 + 4];
            }
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzRSAPubKeyEncrypt(byte bKeyIndex, byte[] bDataBuf, int iDataLen, byte[] bEnData, int[] iEnDataLen) {
        if (iDataLen > 245) {
            return 20;
        }
        LogUnit.SendMsg("zzSendPacket");
        int iSendLen = iDataLen + 5;
        byte[] bSendData = new byte[iSendLen];
        bSendData[0] = bKeyIndex;
        byte[] bDataLen = intToByteArray(iDataLen);
        for (int i = 0; i < 4; i++) {
            bSendData[i + 1] = bDataLen[i];
        }
        for (int i2 = 0; i2 < iDataLen; i2++) {
            bSendData[i2 + 5] = bDataBuf[i2];
        }
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_RSA_PUBKEY_EN, bSendData, iSendLen, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketWaitP2(bResult, bRecvBuf, iRecvDataLen, 1000, true, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            iEnDataLen[0] = byteArrayToInt(bRecvBuf, 0);
            for (int i3 = 0; i3 < iRecvDataLen[0] - 4; i3++) {
                bEnData[i3] = bRecvBuf[i3 + 4];
            }
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzRSAPriKeyDecrypt(byte bKeyIndex, byte[] bDataBuf, int iDataLen, byte[] bEnData, int[] iEnDataLen) {
        if (iDataLen != 256) {
            return 20;
        }
        LogUnit.SendMsg("zzSendPacket");
        int iSendLen = iDataLen + 5;
        byte[] bSendData = new byte[iSendLen];
        bSendData[0] = bKeyIndex;
        byte[] bDataLen = intToByteArray(iDataLen);
        for (int i = 0; i < 4; i++) {
            bSendData[i + 1] = bDataLen[i];
        }
        for (int i2 = 0; i2 < iDataLen; i2++) {
            bSendData[i2 + 5] = bDataBuf[i2];
        }
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_RSA_PRIKEY_DE, bSendData, iSendLen, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketWaitP2(bResult, bRecvBuf, iRecvDataLen, 1000, true, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            iEnDataLen[0] = byteArrayToInt(bRecvBuf, 0);
            for (int i3 = 0; i3 < iRecvDataLen[0] - 4; i3++) {
                bEnData[i3] = bRecvBuf[i3 + 4];
            }
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzRSAPriKeyEncrypt(byte bKeyIndex, byte[] bDataBuf, int iDataLen, byte[] bEnData, int[] iEnDataLen) {
        if (iDataLen > 245) {
            return 20;
        }
        LogUnit.SendMsg("zzSendPacket");
        int iSendLen = iDataLen + 5;
        byte[] bSendData = new byte[iSendLen];
        bSendData[0] = bKeyIndex;
        byte[] bDataLen = intToByteArray(iDataLen);
        for (int i = 0; i < 4; i++) {
            bSendData[i + 1] = bDataLen[i];
        }
        for (int i2 = 0; i2 < iDataLen; i2++) {
            bSendData[i2 + 5] = bDataBuf[i2];
        }
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_RSA_PRIKEY_EN, bSendData, iSendLen, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketWaitP2(bResult, bRecvBuf, iRecvDataLen, 1000, true, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            iEnDataLen[0] = byteArrayToInt(bRecvBuf, 0);
            for (int i3 = 0; i3 < iRecvDataLen[0] - 4; i3++) {
                bEnData[i3] = bRecvBuf[i3 + 4];
            }
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzRSAPubKeyDecrypt(byte bKeyIndex, byte[] bDataBuf, int iDataLen, byte[] bEnData, int[] iEnDataLen) {
        if (iDataLen != 256) {
            return 20;
        }
        LogUnit.SendMsg("zzSendPacket");
        int iSendLen = iDataLen + 5;
        byte[] bSendData = new byte[iSendLen];
        bSendData[0] = bKeyIndex;
        byte[] bDataLen = intToByteArray(iDataLen);
        for (int i = 0; i < 4; i++) {
            bSendData[i + 1] = bDataLen[i];
        }
        for (int i2 = 0; i2 < iDataLen; i2++) {
            bSendData[i2 + 5] = bDataBuf[i2];
        }
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_RSA_PUBKEY_DE, bSendData, iSendLen, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketWaitP2(bResult, bRecvBuf, iRecvDataLen, 1000, true, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            iEnDataLen[0] = byteArrayToInt(bRecvBuf, 0);
            for (int i3 = 0; i3 < iRecvDataLen[0] - 4; i3++) {
                bEnData[i3] = bRecvBuf[i3 + 4];
            }
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzExtPubKeyEn(byte[] publicKey, int publicKeyLen, byte[] inputContent, int inputContentLen, byte[] outputContent, int[] outputContentLen) {
        if (inputContentLen > 245) {
            return 20;
        }
        LogUnit.SendMsg("zzSendPacket");
        int iSendLen = publicKeyLen + 4 + 4 + inputContentLen;
        byte[] bSendData = new byte[iSendLen];
        byte[] bpublicKeyLen = intToByteArray(publicKeyLen);
        for (int i = 0; i < 4; i++) {
            bSendData[i] = bpublicKeyLen[i];
        }
        for (int i2 = 0; i2 < publicKeyLen; i2++) {
            bSendData[i2 + 4] = publicKey[i2];
        }
        byte[] binputContentLen = intToByteArray(inputContentLen);
        for (int i3 = 0; i3 < 4; i3++) {
            bSendData[publicKeyLen + 4 + i3] = binputContentLen[i3];
        }
        for (int i4 = 0; i4 < inputContentLen; i4++) {
            bSendData[publicKeyLen + 4 + 4 + i4] = inputContent[i4];
        }
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_RSA_EXTPUBKEY_EN, bSendData, iSendLen, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketWaitP2(bResult, bRecvBuf, iRecvDataLen, 1000, true, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            outputContentLen[0] = byteArrayToInt(bRecvBuf, 0);
            for (int i5 = 0; i5 < iRecvDataLen[0] - 4; i5++) {
                outputContent[i5] = bRecvBuf[i5 + 4];
            }
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzImportCertificate(byte iFileType, byte[] fileContent, int fileContentLen) {
        LogUnit.SendMsg("zzSendPacket");
        int iSendLen = fileContentLen + 5;
        byte[] bSendData = new byte[iSendLen];
        bSendData[0] = iFileType;
        byte[] bfileContentLen = intToByteArray(fileContentLen);
        for (int i = 0; i < 4; i++) {
            bSendData[i + 1] = bfileContentLen[i];
        }
        for (int i2 = 0; i2 < fileContentLen; i2++) {
            bSendData[i2 + 5] = fileContent[i2];
        }
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_IMPORTCERTIFICATE, bSendData, iSendLen, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketWaitP2(bResult, new byte[8192], new int[1], 1000, true, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzExportCertificate(byte iFileType, byte[] fileContent, int[] fileContentLen) {
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_EXPORTCERTIFICATE, new byte[]{iFileType}, 1, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketWaitP2(bResult, bRecvBuf, iRecvDataLen, 1000, true, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            fileContentLen[0] = byteArrayToInt(bRecvBuf, 0);
            for (int i = 0; i < iRecvDataLen[0] - 4; i++) {
                fileContent[i] = bRecvBuf[i + 4];
            }
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public static byte[] intToByteArray(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) ((value >> (i * 8)) & 255);
        }
        return b;
    }

    public static int byteArrayToInt(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            value += (b[i + offset] & 255) << (i * 8);
        }
        return value;
    }

    public byte[] GenRandNum(int iNum) {
        byte[] rand = new byte[iNum];
        Random r = new Random();
        for (int i = 0; i < iNum; i++) {
            rand[i] = (byte) r.nextInt(127);
            if (rand[i] == 0) {
                rand[i] = (byte) (i + 1);
            }
        }
        return rand;
    }

    public int zzGetPublicKey(byte[] PubKeyData, int[] PubKeyLen) {
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_GET_PUBLIC_KEY, null, 0, (byte) 0, null);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketP2(bResult, bRecvBuf, iRecvDataLen, 1000, (byte) 0, null);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            PubKeyLen[0] = iRecvDataLen[0];
            byte[] bTmp = new byte[iRecvDataLen[0]];
            for (int i = 0; i < iRecvDataLen[0]; i++) {
                bTmp[i] = bRecvBuf[i];
                PubKeyData[i] = bRecvBuf[i];
            }
            LogUnit.SendMsg(new String(bTmp));
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzDownEncSessionKey(byte[] PubKeyData, int PubKeyLen, byte[] SessionKey, int SessionKeyLen) {
        try {
            byte[] encSessionKey = RSAEncrypt.encrypt(RSAEncrypt.DeviceRSAPubKeyToJavaPubKey(PubKeyData), SessionKey);
            if (encSessionKey == null) {
                return 25;
            }
            LogUnit.SendMsg("zzSendPacket");
            int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_DOWN_ENC_SESSION_KEY, encSessionKey, encSessionKey.length, (byte) 0, null);
            if (nRet <= 0) {
                LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
                return nRet;
            }
            LogUnit.SendMsg("zzRecvPacket");
            byte[] bResult = new byte[1];
            int nRet2 = this.m_virtualCom.zzRecvPacketP2(bResult, new byte[8192], new int[1], 1000, (byte) 0, null);
            if (nRet2 != 0) {
                LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
                return nRet2;
            }
            LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
            LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
            return bResult[0];
        } catch (Exception e) {
            e.printStackTrace();
            return 25;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int zzGetEncryptMode(int[] iEncryptMode) {
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CMD_GET_ENCRYPTMODE, null, 0, (byte) 0, null);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int nRet2 = this.m_virtualCom.zzRecvPacketP2(bResult, bRecvBuf, new int[1], 1000, (byte) 0, null);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        iEncryptMode[0] = bRecvBuf[0];
        LogUnit.SendMsg("iEncryptMode,nRet=" + iEncryptMode[0]);
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzSetEncryptMode(int iEncryptMode) {
        byte[] PubKeyData = new byte[NBioBSPJNI.ERROR.NBioAPIERROR_USER_BACK];
        int[] PubKeyLen = {NBioBSPJNI.ERROR.NBioAPIERROR_USER_BACK};
        int nRet = zzGetPublicKey(PubKeyData, PubKeyLen);
        if (nRet != 0) {
            return nRet;
        }
        byte[] SessionKey = GenRandNum(16);
        System.arraycopy(SessionKey, 0, this.g_sessionkey, 0, SessionKey.length);
        int nRet2 = zzDownEncSessionKey(PubKeyData, PubKeyLen[0], SessionKey, SessionKey.length);
        if (nRet2 != 0) {
            return nRet2;
        }
        LogUnit.SendMsg("zzSendPacket");
        byte[] bSendData = {(byte) iEncryptMode};
        int nRet3 = this.m_virtualCom.zzSendPacketP2(MXCommand.CMD_SET_ENCRYPTMODE, bSendData, bSendData.length, (byte) 1, this.g_sessionkey);
        if (nRet3 <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet3);
            return nRet3;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        int nRet4 = this.m_virtualCom.zzRecvPacketP2(bResult, new byte[8192], new int[1], 1000, (byte) 1, this.g_sessionkey);
        if (nRet4 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet4);
            return nRet4;
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet4);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzGetEncryptModeAndSetKey() {
        int[] iEncryptMode = new int[1];
        int nRet = zzGetEncryptMode(iEncryptMode);
        if (nRet != 0) {
            return nRet;
        }
        if (iEncryptMode[0] == 0) {
            this.g_p2 = 0;
        } else {
            this.g_p2 = 1;
        }
        if (this.g_p2 == 1) {
            byte[] PubKeyData = new byte[NBioBSPJNI.ERROR.NBioAPIERROR_USER_BACK];
            int[] PubKeyLen = {NBioBSPJNI.ERROR.NBioAPIERROR_USER_BACK};
            int nRet2 = zzGetPublicKey(PubKeyData, PubKeyLen);
            if (nRet2 != 0) {
                return nRet2;
            }
            byte[] SessionKey = GenRandNum(16);
            System.arraycopy(SessionKey, 0, this.g_sessionkey, 0, SessionKey.length);
            nRet = zzDownEncSessionKey(PubKeyData, PubKeyLen[0], SessionKey, SessionKey.length);
            if (nRet != 0) {
                return nRet;
            }
        }
        return nRet;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int zzGetL0L1Mode(int[] iMode) {
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.L1_CMD_GET_DEVICEMODE, null, 0, (byte) 0, null);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketP2(bResult, bRecvBuf, iRecvDataLen, 100, (byte) 0, null);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            iMode[0] = bRecvBuf[0];
            byte[] bTmp = new byte[iRecvDataLen[0]];
            for (int i = 0; i < iRecvDataLen[0]; i++) {
                bTmp[i] = bRecvBuf[i];
            }
            LogUnit.SendMsg(new String(bTmp));
        }
        return bResult[0];
    }

    public int zzCancel() {
        LogUnit.SendMsg("zzCancel");
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.L1_CMD_CANCEL_CAPTURE, null, 0, (byte) 0, null);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketP2(bResult, bRecvBuf, iRecvDataLen, 100, (byte) 0, null);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            byte[] bTmp = new byte[iRecvDataLen[0]];
            for (int i = 0; i < iRecvDataLen[0]; i++) {
                bTmp[i] = bRecvBuf[i];
            }
            LogUnit.SendMsg(new String(bTmp));
        }
        return bResult[0];
    }

    public int zzGetProductPublicKey(byte[] PubKeyData, int[] PubKeyLen) {
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_READ_OTP, null, 0, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        byte[] bRecvBuf = new byte[8192];
        int[] iRecvDataLen = new int[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketP2(bResult, bRecvBuf, iRecvDataLen, 1000, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        if (iRecvDataLen[0] > 0) {
            PubKeyLen[0] = iRecvDataLen[0];
            for (int i = 0; i < iRecvDataLen[0]; i++) {
                PubKeyData[i] = bRecvBuf[i];
            }
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int zzSetRC4Key() {
        LogUnit.SendMsg("zzSendPacket");
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CCB_CMD_PRODUCT_KEY_SET, null, 0, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketP2(bResult, new byte[8192], new int[1], 1000, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        }
        LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
        LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
        return bResult[0];
    }

    public int GetTzFromEquip(byte[] oFeatureBuf, int[] oFeatureBufLen, int iTimeOut) {
        LogUnit.SendMsg("zzSendPacket");
        byte[] SendBuffer = new byte[100];
        SendBuffer[3] = (byte) (iTimeOut & 255);
        int num = iTimeOut >> 8;
        SendBuffer[2] = (byte) (num & 255);
        int num2 = num >> 8;
        SendBuffer[1] = (byte) (num2 & 255);
        SendBuffer[0] = (byte) ((num2 >> 8) & 255);
        int nRet = this.m_virtualCom.zzSendPacketP2(MXCommand.CMD_DEV_GET_FMR_DATA, SendBuffer, 4, (byte) 0, this.g_sessionkey);
        if (nRet <= 0) {
            LogUnit.SendMsg("zzSendPacket failed,nRet=" + nRet);
            return nRet;
        }
        LogUnit.SendMsg("zzRecvPacket");
        byte[] bResult = new byte[1];
        int nRet2 = this.m_virtualCom.zzRecvPacketP2(bResult, oFeatureBuf, oFeatureBufLen, iTimeOut, (byte) 0, this.g_sessionkey);
        if (nRet2 != 0) {
            LogUnit.SendMsg("zzRecvPacket failed,nRet=" + nRet2);
            return nRet2;
        } else if (bResult[0] != 0) {
            LogUnit.SendMsg("bResult failed,bResult=" + ((int) bResult[0]));
            return bResult[0];
        } else {
            LogUnit.SendMsg("zzRecvPacket,nRet=" + nRet2);
            LogUnit.SendMsg("bResult=" + ((int) bResult[0]));
            return bResult[0];
        }
    }
}
