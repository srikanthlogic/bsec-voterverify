package org.zz.mxfingerdriver;

import android.content.Context;
import android.os.Handler;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import java.util.Calendar;
import org.zz.cipher.DES3;
import org.zz.protocol.MXCommand;
import org.zz.tool.CodecUnit;
import org.zz.tool.LogUnit;
import org.zz.tool.StringUnit;
/* loaded from: classes3.dex */
public class MXVirtualComProtocol {
    private Handler m_fHandler;
    private UsbBase m_usbBase;

    public MXVirtualComProtocol(Context context) {
        this.m_fHandler = null;
        this.m_fHandler = null;
        this.m_usbBase = new UsbBase(context);
    }

    public MXVirtualComProtocol(Context context, Handler bioHandler) {
        this.m_fHandler = null;
        this.m_fHandler = bioHandler;
        LogUnit.SetHandler(bioHandler);
        this.m_usbBase = new UsbBase(context, bioHandler);
    }

    public void unRegUsbMonitor() {
        this.m_usbBase.unRegUsbMonitor();
    }

    public void zzSetLog() {
        LogUnit.LOG_MSG = true;
    }

    public int mxGetDevNum(int vid, int pid) {
        return this.m_usbBase.getDevNum(vid, pid);
    }

    public int zzOpenDev(int vid, int pid, int iInterfaceNo) {
        return this.m_usbBase.openDev(vid, pid, iInterfaceNo);
    }

    public int zzCloseDev() {
        return this.m_usbBase.closeDev();
    }

    public int sendEndpointSize() {
        return this.m_usbBase.sendEndpointSize();
    }

    public int recvEndpointSize() {
        return this.m_usbBase.recvEndpointSize();
    }

    public int zzSendPacketP2(byte bCmd, byte[] bSendBuf, int iDataLen, byte P2, byte[] Key) {
        byte[] bEncSendData = null;
        int iEncSendDataLen = 0;
        if (P2 == 0) {
            if (iDataLen > 0) {
                iEncSendDataLen = iDataLen;
                bEncSendData = new byte[iDataLen];
                System.arraycopy(bSendBuf, 0, bEncSendData, 0, iDataLen);
            }
        } else if (iDataLen > 0) {
            bEncSendData = DES3.encryptMode(Key, bSendBuf);
            if (bEncSendData == null) {
                return 37;
            }
            iEncSendDataLen = bEncSendData.length;
        }
        return zzSendPacket(bCmd, bEncSendData, iEncSendDataLen, P2);
    }

    public int zzSendPacket(byte bCmd, byte[] bSendBuf, int iDataLen, byte P2) {
        int iSendEndpointSize = this.m_usbBase.sendEndpointSize();
        LogUnit.SendMsg("iSendEndpointSize：" + iSendEndpointSize);
        byte[] bSendBufTmp = new byte[((iDataLen + 7) * 2) + iSendEndpointSize + 2];
        int iSendLen = ComposePackage(bCmd, bSendBuf, iDataLen, bSendBufTmp, P2);
        LogUnit.SendMsg("iSendLen：" + iSendLen);
        if (iSendLen > 0) {
            byte[] bTmp = new byte[iSendLen];
            for (int i = 0; i < iSendLen; i++) {
                bTmp[i] = bSendBufTmp[i];
            }
            LogUnit.SendMsg(StringUnit.hex2str(bTmp));
        }
        this.m_usbBase.clearBuffer();
        return this.m_usbBase.sendDataByLength(bSendBufTmp, iSendLen, 1000);
    }

    public int zzRecvPacketP2(byte[] bResult, byte[] bRecvBuf, int[] iRecvDataLen, int iTimeOut, byte P2, byte[] Key) {
        return zzRecvPacketWaitP2(bResult, bRecvBuf, iRecvDataLen, iTimeOut, false, P2, Key);
    }

    public int zzRecvPacketWaitP2(byte[] bResult, byte[] bRecvBuf, int[] iRecvDataLen, int iTimeOut, boolean bWait, byte P2, byte[] Key) {
        int[] iEncRecvBufLen = new int[1];
        byte[] bEncRecvBuf = new byte[102400];
        int nRet = zzRecvPacketWait_In(bResult, bEncRecvBuf, iEncRecvBufLen, iTimeOut, bWait);
        if (nRet != 0) {
            return nRet;
        }
        if (P2 == 0) {
            if (iEncRecvBufLen[0] > 0) {
                iRecvDataLen[0] = iEncRecvBufLen[0];
                System.arraycopy(bEncRecvBuf, 0, bRecvBuf, 0, iRecvDataLen[0]);
            }
        } else if (iEncRecvBufLen[0] > 0) {
            byte[] encData = new byte[iEncRecvBufLen[0]];
            System.arraycopy(bEncRecvBuf, 0, encData, 0, iEncRecvBufLen[0]);
            byte[] decData = DES3.decryptMode(Key, encData);
            if (decData == null) {
                return 37;
            }
            iRecvDataLen[0] = decData.length;
            System.arraycopy(decData, 0, bRecvBuf, 0, iRecvDataLen[0]);
        }
        return nRet;
    }

    /* JADX INFO: Multiple debug info for r1v11 byte: [D('i' int), D('nRecvCheckSum' byte)] */
    /* JADX INFO: Multiple debug info for r4v4 int: [D('iRecv' int), D('iEncLen' int)] */
    public int zzRecvPacketWait_In(byte[] bResult, byte[] bRecvBuf, int[] iRecvDataLen, int iTimeOut, boolean bWait) {
        MXVirtualComProtocol mXVirtualComProtocol = this;
        int iRecvEndpointSize = mXVirtualComProtocol.m_usbBase.recvEndpointSize();
        LogUnit.SendMsg("包大小：" + iRecvEndpointSize);
        byte[] bRecvBufPackage = new byte[iRecvEndpointSize * 2];
        int iFirstRecvBytes = mXVirtualComProtocol.m_usbBase.recvDataByLength(bRecvBufPackage, iRecvEndpointSize, iTimeOut, bWait);
        LogUnit.SendMsg("第一包实际收到数据长度：" + iFirstRecvBytes);
        if (iFirstRecvBytes > 0) {
            byte[] bTmp = new byte[iFirstRecvBytes];
            for (int i = 0; i < iFirstRecvBytes; i++) {
                bTmp[i] = bRecvBufPackage[i];
            }
            LogUnit.SendMsg(hex2str(bTmp));
        }
        if (iFirstRecvBytes == 0) {
            return 41;
        }
        if (iFirstRecvBytes < 12) {
            return 15;
        }
        if (MXCommand.PACK_START_FLAG != bRecvBufPackage[0]) {
            return 12;
        }
        byte[] RawPackTmp = new byte[2];
        byte[] EncPackTmp = new byte[4];
        int i2 = 0;
        while (i2 < 4) {
            EncPackTmp[i2] = bRecvBufPackage[i2 + 1];
            i2++;
            mXVirtualComProtocol = this;
            iFirstRecvBytes = iFirstRecvBytes;
        }
        CodecUnit.DecData(EncPackTmp, 2, RawPackTmp);
        int iDataLen = ((JUnsigned(RawPackTmp[0]) << 8) + JUnsigned(RawPackTmp[1])) - 2;
        LogUnit.SendMsg("数据长度：" + iDataLen);
        int iTotalDataLen = (iDataLen * 2) + 12;
        LogUnit.SendMsg("总数据长度：" + iTotalDataLen);
        byte[] bTotalRecvBuf = new byte[iTotalDataLen];
        int i3 = 0;
        while (i3 < iFirstRecvBytes) {
            bTotalRecvBuf[i3] = bRecvBufPackage[i3];
            i3++;
            mXVirtualComProtocol = this;
            iFirstRecvBytes = iFirstRecvBytes;
        }
        if (iTotalDataLen > iRecvEndpointSize) {
            int iRestRecvBytes = ((iDataLen * 2) + 12) - iFirstRecvBytes;
            LogUnit.SendMsg("还需接收数据长度：" + iRestRecvBytes);
            byte[] bRecvTmp = new byte[iRestRecvBytes];
            int iRecv = mXVirtualComProtocol.m_usbBase.recvDataByLength(bRecvTmp, iRestRecvBytes, 2000, true);
            LogUnit.SendMsg("实际收到数据长度：" + iRecv);
            if (iRestRecvBytes != iRecv) {
                byte[] bTmp2 = new byte[iRestRecvBytes];
                for (int i4 = 0; i4 < iRestRecvBytes; i4++) {
                    bTmp2[i4] = bRecvTmp[i4];
                }
                LogUnit.SendMsg(hex2str(bTmp2));
                return 15;
            }
            if (iRestRecvBytes > 0) {
                byte[] bTmp3 = new byte[iRestRecvBytes];
                for (int i5 = 0; i5 < iRestRecvBytes; i5++) {
                    bTmp3[i5] = bRecvTmp[i5];
                }
                LogUnit.SendMsg(hex2str(bTmp3));
            }
            for (int i6 = 0; i6 < iRestRecvBytes; i6++) {
                bTotalRecvBuf[iFirstRecvBytes + i6] = bRecvTmp[i6];
            }
        }
        int iEncLen = iTotalDataLen - 2;
        int iRawLen = iEncLen / 2;
        byte[] RawPack = new byte[iRawLen];
        byte[] EncPack = new byte[iEncLen];
        int i7 = 0;
        while (i7 < iEncLen) {
            EncPack[i7] = bTotalRecvBuf[i7 + 1];
            i7++;
            iFirstRecvBytes = iFirstRecvBytes;
        }
        CodecUnit.DecData(EncPack, iRawLen, RawPack);
        int i8 = 0;
        byte bCalCheckSum = 0;
        while (i8 < iRawLen - 1) {
            bCalCheckSum = (byte) (RawPack[i8] ^ bCalCheckSum);
            i8++;
            iRecvEndpointSize = iRecvEndpointSize;
            iFirstRecvBytes = iFirstRecvBytes;
        }
        int iDataLen2 = (JUnsigned(RawPack[0]) << 8) + JUnsigned(RawPack[1]);
        byte SW1 = RawPack[2];
        byte b = RawPack[3];
        int iRawLen2 = 4;
        char c = 0;
        iRecvDataLen[0] = iDataLen2 - 2;
        LogUnit.SendMsg("iRecvDataLen=" + iRecvDataLen[0]);
        if (iRecvDataLen[0] > 0) {
            int i9 = 0;
            while (i9 < iRecvDataLen[c]) {
                bRecvBuf[i9] = RawPack[4 + i9];
                i9++;
                c = 0;
            }
            iRawLen2 = 4 + iRecvDataLen[c];
        }
        byte nRecvCheckSum = RawPack[iRawLen2];
        int iRawLen3 = iRawLen2 + 1;
        if (bCalCheckSum != nRecvCheckSum) {
            LogUnit.SendMsg("bCalCheckSum=" + String.format("%02x", Byte.valueOf(bCalCheckSum)));
            LogUnit.SendMsg("nRecvCheckSum=" + String.format("%02x", Byte.valueOf(nRecvCheckSum)));
            return 14;
        } else if (MXCommand.PACK_END_FLAG != bTotalRecvBuf[iTotalDataLen - 1]) {
            return 13;
        } else {
            bResult[0] = SW1;
            return 0;
        }
    }

    public int zzRecvImagePacketP2(byte bCmd, byte[] bResult, byte[] bImgDataBuf, int[] iImgWidth, int[] iImgHeight, int iTimeOut, byte P2, byte[] Key) {
        byte[] bEncRecvBuf = new byte[1920100];
        int nRet = zzRecvImagePacket(bCmd, bResult, bEncRecvBuf, iImgWidth, iImgHeight, iTimeOut, P2);
        if (nRet != 0) {
            return nRet;
        }
        int nTotalRecvDataLen = iImgWidth[0] * iImgHeight[0];
        if (P2 == 1) {
            nTotalRecvDataLen = (nTotalRecvDataLen + 8) - (nTotalRecvDataLen % 8);
        }
        if (P2 == 0) {
            if (nTotalRecvDataLen > 0) {
                System.arraycopy(bEncRecvBuf, 0, bImgDataBuf, 0, nTotalRecvDataLen);
            }
        } else if (nTotalRecvDataLen > 0) {
            byte[] encData = new byte[nTotalRecvDataLen];
            System.arraycopy(bEncRecvBuf, 0, encData, 0, nTotalRecvDataLen);
            byte[] decData = DES3.decryptMode(Key, encData);
            if (decData == null) {
                return 37;
            }
            System.arraycopy(decData, 0, bImgDataBuf, 0, decData.length);
        }
        return nRet;
    }

    /* JADX INFO: Multiple debug info for r2v6 int: [D('i' int), D('iRemainImgLen' int)] */
    /* JADX INFO: Multiple debug info for r6v8 byte: [D('i' int), D('nRecvCheckSum' byte)] */
    public int zzRecvImagePacket(byte bCmd, byte[] bResult, byte[] bImgDataBuf, int[] iImgWidth, int[] iImgHeight, int iTimeOut, byte P2) {
        int iRecvBytes;
        MXVirtualComProtocol mXVirtualComProtocol = this;
        int iRecvEndpointSize = mXVirtualComProtocol.m_usbBase.recvEndpointSize();
        LogUnit.SendMsg("包大小：" + iRecvEndpointSize);
        byte[] bRecvBufPackage = new byte[iRecvEndpointSize];
        if (bCmd == MXCommand.L1_CCB_GET_STQCIMG_EX) {
            iRecvBytes = mXVirtualComProtocol.m_usbBase.recvDataByPackageWithCancel(bRecvBufPackage, iRecvEndpointSize, iTimeOut);
        } else {
            iRecvBytes = mXVirtualComProtocol.m_usbBase.recvDataByPackage(bRecvBufPackage, iRecvEndpointSize, iTimeOut);
        }
        LogUnit.SendMsg("第一包实际收到数据长度：" + iRecvBytes);
        if (iRecvBytes == 41) {
            return 41;
        }
        if (iRecvBytes == 38) {
            return 38;
        }
        if (iRecvBytes != iRecvEndpointSize) {
            if (iRecvBytes <= 4 || bRecvBufPackage[3] == 0) {
                return 15;
            }
            return bRecvBufPackage[3];
        } else if (iRecvBytes == 11) {
            return 41;
        } else {
            byte bCalCheckSum = 0;
            int i = 0;
            while (i < iRecvBytes - 1) {
                bCalCheckSum = (byte) (bRecvBufPackage[i + 1] ^ bCalCheckSum);
                i++;
                mXVirtualComProtocol = this;
                iRecvBytes = iRecvBytes;
            }
            if (MXCommand.PACK_START_FLAG != bRecvBufPackage[0]) {
                return 12;
            }
            int JUnsigned = JUnsigned(bRecvBufPackage[2]) + (JUnsigned(bRecvBufPackage[1]) << 8);
            byte SW1 = bRecvBufPackage[3];
            byte b = bRecvBufPackage[4];
            int iImageWidth = (JUnsigned(bRecvBufPackage[5]) * 256) + JUnsigned(bRecvBufPackage[6]);
            int iImageHeight = (JUnsigned(bRecvBufPackage[7]) * 256) + JUnsigned(bRecvBufPackage[8]);
            LogUnit.SendMsg("图像宽：" + iImageWidth);
            LogUnit.SendMsg("图像高：" + iImageHeight);
            iImgWidth[0] = iImageWidth;
            iImgHeight[0] = iImageHeight;
            int nTotalRecvDataLen = iImageWidth * iImageHeight;
            if (P2 == 1) {
                nTotalRecvDataLen = (nTotalRecvDataLen + 8) - (nTotalRecvDataLen % 8);
            }
            LogUnit.SendMsg("图像数据长度：" + nTotalRecvDataLen);
            int iRecvImgLen = iRecvEndpointSize + -9;
            LogUnit.SendMsg("已接收图像数据长度：" + iRecvImgLen);
            int i2 = 0;
            while (i2 < iRecvImgLen) {
                bImgDataBuf[i2] = bRecvBufPackage[i2 + 9];
                i2++;
                mXVirtualComProtocol = this;
                iRecvBytes = iRecvBytes;
            }
            int iRemainImgLen = nTotalRecvDataLen - iRecvImgLen;
            if (iRemainImgLen <= 0) {
                return 15;
            }
            LogUnit.SendMsg("还需接收图像数据长度：" + iRemainImgLen);
            byte[] bRecvTmp = new byte[iRemainImgLen + 2];
            int iRecv = mXVirtualComProtocol.m_usbBase.recvDataByLength(bRecvTmp, iRemainImgLen + 2, PathInterpolatorCompat.MAX_NUM_POINTS, true);
            LogUnit.SendMsg("实际收到数据长度：" + iRecv);
            if (iRecv < iRemainImgLen + 2) {
                return 15;
            }
            if (iRecv > iRemainImgLen + 2) {
                return 18;
            }
            int i3 = 0;
            while (i3 < iRemainImgLen) {
                bImgDataBuf[iRecvImgLen + i3] = bRecvTmp[i3];
                bCalCheckSum = (byte) (bRecvTmp[i3] ^ bCalCheckSum);
                i3++;
                iRecv = iRecv;
                iRemainImgLen = iRemainImgLen;
            }
            byte nRecvCheckSum = bRecvTmp[iRemainImgLen];
            byte bPackageEndFlag = bRecvTmp[iRemainImgLen + 1];
            if (bCalCheckSum != nRecvCheckSum) {
                LogUnit.SendMsg("bCalCheckSum=" + String.format("%02x", Byte.valueOf(bCalCheckSum)));
                LogUnit.SendMsg("nRecvCheckSum=" + String.format("%02x", Byte.valueOf(nRecvCheckSum)));
            }
            if (MXCommand.PACK_END_FLAG != bPackageEndFlag) {
                return 13;
            }
            bResult[0] = SW1;
            return 0;
        }
    }

    /* JADX INFO: Multiple debug info for r1v6 int: [D('i' int), D('iRemainImgLen' int)] */
    /* JADX INFO: Multiple debug info for r4v17 byte: [D('i' int), D('nRecvCheckSum' byte)] */
    public int zzRecvL1Packet(byte[] bResult, byte[] bRecvBuf, int[] iRecvDataLen, int iTimeOut) {
        MXVirtualComProtocol mXVirtualComProtocol = this;
        int iRecvEndpointSize = mXVirtualComProtocol.m_usbBase.recvEndpointSize();
        LogUnit.SendMsg("包大小：" + iRecvEndpointSize);
        byte[] bRecvBufPackage = new byte[iRecvEndpointSize];
        int iRecvBytes = mXVirtualComProtocol.m_usbBase.recvDataByPackageWithCancel(bRecvBufPackage, iRecvEndpointSize, iTimeOut);
        LogUnit.SendMsg("第一包实际收到数据长度：" + iRecvBytes);
        if (iRecvBytes > 0) {
            byte[] bTmp = new byte[iRecvBytes];
            for (int i = 0; i < iRecvBytes; i++) {
                bTmp[i] = bRecvBufPackage[i];
            }
            LogUnit.SendMsg(hex2str(bTmp));
        }
        if (iRecvBytes == 41) {
            return 41;
        }
        if (iRecvBytes == 38) {
            return 38;
        }
        if (iRecvBytes == iRecvEndpointSize) {
            byte bCalCheckSum = 0;
            int i2 = 0;
            while (i2 < iRecvBytes - 1) {
                bCalCheckSum = (byte) (bRecvBufPackage[i2 + 1] ^ bCalCheckSum);
                i2++;
                mXVirtualComProtocol = this;
                iRecvBytes = iRecvBytes;
            }
            if (MXCommand.PACK_START_FLAG != bRecvBufPackage[0]) {
                return 12;
            }
            int JUnsigned = JUnsigned(bRecvBufPackage[2]) + (JUnsigned(bRecvBufPackage[1]) << 8);
            byte SW1 = bRecvBufPackage[3];
            byte b = bRecvBufPackage[4];
            int a2 = JUnsigned(bRecvBufPackage[5]);
            int iActualDataLen = (a2 * 256 * 256 * 256) + (JUnsigned(bRecvBufPackage[6]) * 256 * 256) + (JUnsigned(bRecvBufPackage[7]) * 256) + JUnsigned(bRecvBufPackage[8]);
            iRecvDataLen[0] = iActualDataLen;
            LogUnit.SendMsg("数据长度：" + iActualDataLen);
            int iRecvImgLen = iRecvEndpointSize + -9;
            LogUnit.SendMsg("已接收数据长度：" + iRecvImgLen);
            int i3 = 0;
            while (i3 < iRecvImgLen) {
                bRecvBuf[i3] = bRecvBufPackage[i3 + 9];
                i3++;
                mXVirtualComProtocol = this;
                iRecvBytes = iRecvBytes;
                a2 = a2;
            }
            int iRemainImgLen = iActualDataLen - iRecvImgLen;
            if (iRemainImgLen <= 0) {
                return 15;
            }
            LogUnit.SendMsg("还需接收数据长度：" + iRemainImgLen);
            byte[] bRecvTmp = new byte[iRemainImgLen + 2];
            int iRecv = mXVirtualComProtocol.m_usbBase.recvDataByLength(bRecvTmp, iRemainImgLen + 2, 6000, true);
            LogUnit.SendMsg("实际收到数据长度：" + iRecv);
            if (iRecv < iRemainImgLen + 2) {
                return 15;
            }
            if (iRecv > iRemainImgLen + 2) {
                return 18;
            }
            int i4 = 0;
            while (i4 < iRemainImgLen) {
                bRecvBuf[iRecvImgLen + i4] = bRecvTmp[i4];
                bCalCheckSum = (byte) (bRecvTmp[i4] ^ bCalCheckSum);
                i4++;
                iRecv = iRecv;
                iRemainImgLen = iRemainImgLen;
            }
            byte nRecvCheckSum = bRecvTmp[iRemainImgLen];
            byte bPackageEndFlag = bRecvTmp[iRemainImgLen + 1];
            if (bCalCheckSum != nRecvCheckSum) {
                LogUnit.SendMsg("bCalCheckSum=" + String.format("%02x", Byte.valueOf(bCalCheckSum)));
                LogUnit.SendMsg("nRecvCheckSum=" + String.format("%02x", Byte.valueOf(nRecvCheckSum)));
            }
            if (MXCommand.PACK_END_FLAG != bPackageEndFlag) {
                return 13;
            }
            bResult[0] = SW1;
            return 0;
        } else if (iRecvBytes <= 4 || bRecvBufPackage[3] == 0) {
            return 15;
        } else {
            return bRecvBufPackage[3];
        }
    }

    public void cancelRecv() {
        this.m_usbBase.cancelRecvDataByPackage();
    }

    public static int ComposePackage(byte bCmd, byte[] bSendBuf, int iSendLen, byte[] bDataPack, byte P2) {
        byte[] RawPack = new byte[iSendLen + 7];
        byte[] EncPack = new byte[(iSendLen + 7) * 2];
        RawPack[0] = (byte) ((iSendLen + 4) >> 8);
        int nRawPackLen = 0 + 1;
        RawPack[nRawPackLen] = (byte) ((iSendLen + 4) & 255);
        int nRawPackLen2 = nRawPackLen + 1;
        RawPack[nRawPackLen2] = bCmd;
        int nRawPackLen3 = nRawPackLen2 + 1;
        RawPack[nRawPackLen3] = P2;
        int nRawPackLen4 = nRawPackLen3 + 1 + 2;
        if (iSendLen > 0) {
            for (int i = 0; i < iSendLen; i++) {
                RawPack[nRawPackLen4 + i] = bSendBuf[i];
            }
        }
        int nRawPackLen5 = nRawPackLen4 + iSendLen;
        byte nCheckSum = 0;
        for (int i2 = 0; i2 < nRawPackLen5; i2++) {
            nCheckSum = (byte) (RawPack[i2] ^ nCheckSum);
        }
        RawPack[nRawPackLen5] = nCheckSum;
        int nRawPackLen6 = nRawPackLen5 + 1;
        CodecUnit.EncData(RawPack, nRawPackLen6, EncPack);
        int nEncPackLen = nRawPackLen6 * 2;
        bDataPack[0] = MXCommand.PACK_START_FLAG;
        int nPackLen = 0 + 1;
        for (int i3 = 0; i3 < nEncPackLen; i3++) {
            bDataPack[nPackLen + i3] = EncPack[i3];
        }
        int nPackLen2 = nPackLen + nEncPackLen;
        bDataPack[nPackLen2] = MXCommand.PACK_END_FLAG;
        return nPackLen2 + 1;
    }

    public void MySleep(int iTimeout) {
        Calendar time1 = Calendar.getInstance();
        for (long duration = -1; duration <= ((long) iTimeout); duration = Calendar.getInstance().getTimeInMillis() - time1.getTimeInMillis()) {
        }
    }

    public static String hex2str(byte[] hex) {
        StringBuilder sb = new StringBuilder();
        int length = hex.length;
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02x ", Byte.valueOf(hex[i])));
        }
        return sb.toString();
    }

    public static int JUnsigned(int x) {
        if (x >= 0) {
            return x;
        }
        return x + 256;
    }
}
