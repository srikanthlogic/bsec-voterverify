package org.zz.mxfingerdriver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.util.Log;
import java.util.Calendar;
import org.zz.tool.LogUnit;
/* loaded from: classes3.dex */
public class UsbBase {
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private Context m_ctx;
    private Handler m_fHandler;
    private int m_iSendEndpointSize = 0;
    private int m_iRecvEndpointSize = 0;
    private UsbDevice m_usbDevice = null;
    private UsbInterface m_usbInterface = null;
    private UsbEndpoint m_inEndpoint = null;
    private UsbEndpoint m_outEndpoint = null;
    private UsbDeviceConnection m_connection = null;
    private boolean m_bcancelRecv = false;
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() { // from class: org.zz.mxfingerdriver.UsbBase.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbBase.ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra("device");
                    if (!intent.getBooleanExtra("permission", false)) {
                        Log.d("MIAXIS", "permission denied for device " + device);
                    }
                }
            }
            if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action) && ((UsbDevice) intent.getParcelableExtra("device")) != null) {
                UsbBase.this.m_connection.releaseInterface(UsbBase.this.m_usbInterface);
                UsbBase.this.m_connection.close();
            }
        }
    };

    public UsbBase(Context context) {
        this.m_ctx = null;
        this.m_fHandler = null;
        this.m_ctx = context;
        this.m_fHandler = null;
        regUsbMonitor();
    }

    public UsbBase(Context context, Handler bioHandler) {
        this.m_ctx = null;
        this.m_fHandler = null;
        this.m_ctx = context;
        this.m_fHandler = bioHandler;
        LogUnit.SetHandler(bioHandler);
        regUsbMonitor();
    }

    public String getDevAttribute() {
        Context context = this.m_ctx;
        if (context == null) {
            return null;
        }
        int iDevNum = 0;
        String strDevInfo = "";
        UsbManager usbManager = (UsbManager) context.getSystemService("usb");
        for (UsbDevice device : usbManager.getDeviceList().values()) {
            if (usbManager.hasPermission(device)) {
                iDevNum++;
                String strDevInfo2 = String.valueOf(strDevInfo) + ("describeContents:" + device.describeContents()) + "\r\n";
                String strDevInfo3 = String.valueOf(strDevInfo2) + ("DeviceProtocol:" + device.getDeviceProtocol()) + "\r\n";
                String strDevInfo4 = String.valueOf(strDevInfo3) + ("DeviceClass:" + device.getDeviceClass()) + "\r\n";
                String strDevInfo5 = String.valueOf(strDevInfo4) + ("DeviceSubclass:" + device.getDeviceSubclass()) + "\r\n";
                String strDevInfo6 = String.valueOf(strDevInfo5) + ("InterfaceCount:" + device.getInterfaceCount()) + "\r\n";
                String strDevInfo7 = String.valueOf(strDevInfo6) + ("DeviceId:" + device.getDeviceId()) + "\r\n";
                String strDevInfo8 = String.valueOf(strDevInfo7) + ("DeviceName:" + device.getDeviceName()) + "\r\n";
                String strDevInfo9 = String.valueOf(strDevInfo8) + ("VendorId:" + device.getVendorId()) + "\r\n";
                strDevInfo = String.valueOf(strDevInfo9) + ("ProductId:" + device.getProductId()) + "\r\n";
                LogUnit.SendMsg("Device\r\n" + strDevInfo);
            } else {
                usbManager.requestPermission(device, PendingIntent.getBroadcast(this.m_ctx, 0, new Intent(ACTION_USB_PERMISSION), 0));
                return null;
            }
        }
        return strDevInfo;
    }

    public int getDevNum(int vid, int pid) {
        Context context = this.m_ctx;
        if (context == null) {
            return 102;
        }
        int iDevNum = 0;
        UsbManager usbManager = (UsbManager) context.getSystemService("usb");
        for (UsbDevice device : usbManager.getDeviceList().values()) {
            if (!usbManager.hasPermission(device)) {
                usbManager.requestPermission(device, PendingIntent.getBroadcast(this.m_ctx, 0, new Intent(ACTION_USB_PERMISSION), 0));
                return 101;
            } else if (vid == device.getVendorId() && pid == device.getProductId()) {
                iDevNum++;
            }
        }
        return iDevNum;
    }

    public int openDev(int vid, int pid, int iInterfaceNo) {
        LogUnit.SendMsg("openDev");
        Context context = this.m_ctx;
        if (context == null) {
            LogUnit.SendMsg("ERRCODE_NO_CONTEXT");
            return 102;
        }
        UsbManager usbManager = (UsbManager) context.getSystemService("usb");
        for (UsbDevice device : usbManager.getDeviceList().values()) {
            usbManager.requestPermission(device, PendingIntent.getBroadcast(this.m_ctx, 0, new Intent(ACTION_USB_PERMISSION), 0));
            if (usbManager.hasPermission(device)) {
                LogUnit.SendMsg("dName: " + device.getDeviceName());
                LogUnit.SendMsg("vid: " + device.getVendorId() + "\t pid: " + device.getProductId());
                if (vid == device.getVendorId() && pid == device.getProductId()) {
                    this.m_usbDevice = device;
                    this.m_usbInterface = this.m_usbDevice.getInterface(iInterfaceNo);
                    this.m_inEndpoint = this.m_usbInterface.getEndpoint(0);
                    this.m_outEndpoint = this.m_usbInterface.getEndpoint(1);
                    this.m_connection = usbManager.openDevice(this.m_usbDevice);
                    this.m_connection.claimInterface(this.m_usbInterface, true);
                    this.m_iSendEndpointSize = this.m_outEndpoint.getMaxPacketSize();
                    this.m_iRecvEndpointSize = this.m_inEndpoint.getMaxPacketSize();
                    LogUnit.SendMsg("-------------------------------------------");
                    LogUnit.SendMsg("SendEndpointSize: " + this.m_iSendEndpointSize);
                    LogUnit.SendMsg("RecvEndpointSize: " + this.m_iRecvEndpointSize);
                    return 0;
                }
            } else {
                usbManager.requestPermission(device, PendingIntent.getBroadcast(this.m_ctx, 0, new Intent(ACTION_USB_PERMISSION), 0));
                return 101;
            }
        }
        return 100;
    }

    public int sendEndpointSize() {
        return this.m_iSendEndpointSize;
    }

    public int recvEndpointSize() {
        return this.m_iRecvEndpointSize;
    }

    public int clearBuffer() {
        int iEndpointSize = recvEndpointSize();
        do {
        } while (this.m_connection.bulkTransfer(this.m_inEndpoint, new byte[iEndpointSize], iEndpointSize, 5) >= 0);
        return 0;
    }

    public int sendDataByPackage(byte[] bSendBuf, int iSendLen, int iTimeOut) {
        int iEndpointSize;
        if (iSendLen > bSendBuf.length || iSendLen > (iEndpointSize = sendEndpointSize())) {
            return 18;
        }
        byte[] bSendBufTmp = new byte[iEndpointSize];
        System.arraycopy(bSendBuf, 0, bSendBufTmp, 0, iSendLen);
        return this.m_connection.bulkTransfer(this.m_outEndpoint, bSendBufTmp, iEndpointSize, 1000);
    }

    public int sendDataByLength(byte[] bSendBuf, int iSendLen, int iTimeOut) {
        if (iSendLen > bSendBuf.length) {
            return 18;
        }
        int iSendDataLen = 0;
        int iEndpointSize = sendEndpointSize();
        byte[] bSendBufTmp = new byte[iEndpointSize];
        for (int i = 0; i < iSendLen; i += iEndpointSize) {
            int nDataLen = iSendLen - i;
            if (nDataLen > iEndpointSize) {
                nDataLen = iEndpointSize;
            }
            System.arraycopy(bSendBuf, i, bSendBufTmp, 0, nDataLen);
            int iRV = this.m_connection.bulkTransfer(this.m_outEndpoint, bSendBufTmp, nDataLen, 1000);
            if (iRV != nDataLen) {
                LogUnit.SendMsg("====iRV：" + iRV);
                return 10;
            }
            iSendDataLen += iRV;
        }
        return iSendDataLen;
    }

    public int recvDataByLength(byte[] bRecvBuf, int iRecvLen, int iTimeOut, boolean bWait) {
        if (iRecvLen > bRecvBuf.length) {
            return 18;
        }
        int i = 0;
        int iRemainImgLen = iRecvLen;
        int iRecvDataLen = 0;
        byte[] bRecvBufTmp = new byte[iRecvLen];
        Calendar time1 = Calendar.getInstance();
        while (true) {
            if (iRemainImgLen <= 0) {
                break;
            }
            int iRecvSize = this.m_connection.bulkTransfer(this.m_inEndpoint, bRecvBufTmp, iRecvLen, iTimeOut);
            if (iRecvSize >= 0) {
                LogUnit.SendMsg("i=" + i + ",iRecvSize：" + iRecvSize);
                i++;
                System.arraycopy(bRecvBufTmp, 0, bRecvBuf, iRecvDataLen, iRecvSize);
                iRecvDataLen += iRecvSize;
                iRemainImgLen = iRecvLen - iRecvDataLen;
                if (Calendar.getInstance().getTimeInMillis() - time1.getTimeInMillis() > ((long) iTimeOut) || (!bWait && iRecvDataLen >= 12)) {
                    break;
                }
            } else {
                LogUnit.SendMsg("====iRecvSize：" + iRecvSize);
                break;
            }
        }
        return iRecvDataLen;
    }

    public int recvDataByPackage(byte[] bRecvBuf, int iRecvLen, int iTimeOut) {
        if (iRecvLen > bRecvBuf.length) {
            return 18;
        }
        int iRecvDataLen = 0;
        int iEndpointSize = recvEndpointSize();
        byte[] bRecvBufTmp = new byte[iEndpointSize];
        for (int i = 0; i < iRecvLen; i += iEndpointSize) {
            int nDataLen = iRecvLen - i;
            if (nDataLen > iEndpointSize) {
                nDataLen = iEndpointSize;
            }
            int iRV = this.m_connection.bulkTransfer(this.m_inEndpoint, bRecvBufTmp, nDataLen, iTimeOut);
            if (iRV < 0) {
                return 41;
            }
            LogUnit.SendMsg("iRV：" + iRV);
            System.arraycopy(bRecvBufTmp, 0, bRecvBuf, i, iRV);
            iRecvDataLen += iRV;
        }
        return iRecvDataLen;
    }

    public int recvDataByPackageWithCancel(byte[] bRecvBuf, int iRecvLen, int iTimeOut) {
        int iRV;
        int i = iRecvLen;
        if (i > bRecvBuf.length) {
            return 18;
        }
        int iRecvDataLen = 0;
        int iEndpointSize = recvEndpointSize();
        byte[] bRecvBufTmp = new byte[iEndpointSize];
        int i2 = 0;
        while (i2 < i) {
            int nDataLen = i - i2;
            if (nDataLen > iEndpointSize) {
                nDataLen = iEndpointSize;
            }
            int iCancelOrTimeout = 0;
            int iTestNum = 0;
            this.m_bcancelRecv = false;
            Calendar time1 = Calendar.getInstance();
            while (true) {
                iRV = this.m_connection.bulkTransfer(this.m_inEndpoint, bRecvBufTmp, nDataLen, 1000);
                if (iRV > 0) {
                    LogUnit.SendMsg("bulkTransfer,iRV:" + iRV);
                    break;
                }
                iTestNum++;
                LogUnit.SendMsg("iRV：" + iRV);
                LogUnit.SendMsg("iTestNum:" + iTestNum);
                long bt_time = (Calendar.getInstance().getTimeInMillis() - time1.getTimeInMillis()) + 100;
                LogUnit.SendMsg("bt_time:" + bt_time);
                if (bt_time + 100 >= ((long) iTimeOut)) {
                    LogUnit.SendMsg("timeout");
                    iCancelOrTimeout = 41;
                    break;
                } else if (this.m_bcancelRecv) {
                    LogUnit.SendMsg("cancel");
                    iCancelOrTimeout = 38;
                    break;
                } else {
                    iCancelOrTimeout = iRV;
                    nDataLen = nDataLen;
                }
            }
            LogUnit.SendMsg("======iRV：" + iRV);
            if (iRV < 0) {
                return iCancelOrTimeout;
            }
            System.arraycopy(bRecvBufTmp, 0, bRecvBuf, i2, iRV);
            iRecvDataLen += iRV;
            i2 += iEndpointSize;
            i = iRecvLen;
        }
        return iRecvDataLen;
    }

    public void cancelRecvDataByPackage() {
        this.m_bcancelRecv = true;
    }

    public int closeDev() {
        UsbDeviceConnection usbDeviceConnection = this.m_connection;
        if (usbDeviceConnection == null) {
            return 0;
        }
        usbDeviceConnection.releaseInterface(this.m_usbInterface);
        this.m_connection.close();
        this.m_connection = null;
        return 0;
    }

    public static String hex2str(byte[] hex) {
        StringBuilder sb = new StringBuilder();
        int length = hex.length;
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02x ", Byte.valueOf(hex[i])));
        }
        return sb.toString();
    }

    private void regUsbMonitor() {
        this.m_ctx.registerReceiver(this.mUsbReceiver, new IntentFilter(ACTION_USB_PERMISSION));
    }

    public void unRegUsbMonitor() {
        this.m_ctx.unregisterReceiver(this.mUsbReceiver);
    }
}
