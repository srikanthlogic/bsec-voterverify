package org.tatvik.fp;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import androidx.core.view.InputDeviceCompat;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import com.facebook.common.statfs.StatFsHelper;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.google.common.base.Ascii;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;
import org.zz.jni.MXImageProcess;
import org.zz.jni.zzFingerAlg;
import org.zz.mxfingerdriver.CallbackMessage;
import org.zz.mxfingerdriver.MXImage;
import org.zz.mxfingerdriver.SM91Driver;
import org.zz.tool.LogUnit;
/* loaded from: classes3.dex */
public class TMF20API extends Observable {
    private static final int DEVICE_RESIDUAL_THRESHOLD = 1;
    Context context;
    private Handler m_fHandler;
    SM91Driver m_sm91Driver;
    zzFingerAlg tmfScannerAlgo;
    private boolean m_bCancelGetImage = false;
    private MXImageProcess m_imgProcess = new MXImageProcess();

    static {
        System.loadLibrary("TMF20Alg");
    }

    public void notifyDataChange(Observer observer, CallbackMessage message) {
        if (observer != null) {
            setChanged();
            notifyObservers(message);
        }
    }

    public TMF20API(Context context) {
        this.m_fHandler = null;
        this.m_fHandler = null;
        this.m_sm91Driver = new SM91Driver(context);
        System.loadLibrary("MXImageProcess");
        this.context = context;
    }

    public TMF20API(Context context, Handler bioHandler) {
        this.m_fHandler = null;
        this.m_fHandler = bioHandler;
        this.m_sm91Driver = new SM91Driver(context, this.m_fHandler);
        LogUnit.SetHandler(bioHandler);
        System.loadLibrary("MXImageProcess");
        this.context = context;
    }

    public boolean isDeviceConnected() {
        if (TMF20ErrorCodes.SUCCESS == tmfIsDeviceConnected()) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v12 */
    /* JADX WARN: Type inference failed for: r2v15 */
    /* JADX WARN: Type inference failed for: r2v16 */
    public CaptureResult captureFingerprint(int timeOutInMills) {
        CaptureResult captResult;
        CaptureResult captResult2;
        Exception e;
        int nRet;
        int nRet2 = -1;
        int IMG_WIDTH = GenericDraweeHierarchyBuilder.DEFAULT_FADE_DURATION;
        int IMG_HEIGHT = StatFsHelper.DEFAULT_DISK_YELLOW_LEVEL_IN_MB;
        int[] deviceId = new int[1];
        int[] imgWidth = new int[1];
        int[] imgHeight = new int[1];
        int[] ppi = new int[1];
        int[] fwVer = new int[1];
        int[] hwVer = new int[1];
        this.tmfScannerAlgo = new zzFingerAlg();
        try {
            nRet2 = tmfGetDeviceInfo(deviceId, imgWidth, imgHeight, ppi, hwVer, fwVer);
            if (TMF20ErrorCodes.SUCCESS == nRet2) {
                IMG_WIDTH = imgWidth[0];
                IMG_HEIGHT = imgHeight[0];
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            IMG_WIDTH = GenericDraweeHierarchyBuilder.DEFAULT_FADE_DURATION;
            IMG_HEIGHT = StatFsHelper.DEFAULT_DISK_YELLOW_LEVEL_IN_MB;
        }
        byte[] isoData = new byte[InputDeviceCompat.SOURCE_GAMEPAD];
        int[] fmrRecordSize = new int[1];
        byte[] imgData = new byte[IMG_WIDTH * IMG_HEIGHT];
        int[] iImgWidth = new int[1];
        int[] iImgHeight = new int[1];
        CaptureResult captResult3 = new CaptureResult();
        try {
            try {
                int nRet3 = tmfCaptureFingerprintwithThreshold(imgData, iImgWidth, iImgHeight, timeOutInMills, 0, 1, (Observer) this.context);
                try {
                    if (TMF20ErrorCodes.SUCCESS == nRet3) {
                        captResult2 = imgData;
                        int nRet4 = this.tmfScannerAlgo.tmfGetTzFMR(captResult2, IMG_WIDTH, IMG_HEIGHT, isoData, fmrRecordSize);
                        try {
                            try {
                                if (TMF20ErrorCodes.FMR_EXTRACTION_SUCCESS == nRet4) {
                                    captResult2 = captResult3;
                                    try {
                                        captResult2.fmrBytes = new byte[fmrRecordSize[0]];
                                        try {
                                            System.arraycopy(isoData, 0, captResult2.fmrBytes, 0, fmrRecordSize[0]);
                                            captResult2.minutiaeCount = 0;
                                            if (captResult2.fmrBytes != null) {
                                                try {
                                                    if (captResult2.fmrBytes.length > 27) {
                                                        captResult2.minutiaeCount = captResult2.fmrBytes[27];
                                                    }
                                                } catch (Exception e2) {
                                                    e = e2;
                                                    e.printStackTrace();
                                                    captResult = captResult2;
                                                    return captResult;
                                                }
                                            }
                                            captResult2.nfiq = 0;
                                            int quality = 0;
                                            quality = 0;
                                            int sample_quality = 0;
                                            if (captResult2.fmrBytes != null && captResult2.fmrBytes.length > 26) {
                                                byte b = captResult2.fmrBytes[26];
                                                int quality2 = b;
                                                if (b <= 5) {
                                                    quality2 = 120 - (b * Ascii.DC4);
                                                }
                                                sample_quality = getNfiqValue(quality2 == 1 ? 1 : 0);
                                                quality = quality2;
                                            }
                                            captResult2.fmrBytes[26] = quality == 1 ? (byte) 1 : 0;
                                            captResult2.nfiq = sample_quality;
                                            captResult2.imageWidth = iImgWidth[0];
                                            captResult2.imageHeight = iImgHeight[0];
                                            captResult2.rawImageBytes = new byte[captResult2.imageWidth * captResult2.imageHeight];
                                            System.arraycopy(imgData, 0, captResult2.rawImageBytes, 0, captResult2.rawImageBytes.length);
                                            captResult2.statusCode = TMF20ErrorCodes.SUCCESS;
                                            captResult2 = captResult2;
                                        } catch (Exception e3) {
                                            e = e3;
                                        }
                                    } catch (Exception e4) {
                                        e = e4;
                                    }
                                } else {
                                    CaptureResult captResult4 = captResult3;
                                    captResult4.statusCode = nRet4;
                                    captResult2 = captResult4;
                                }
                                nRet = nRet4;
                                captResult2 = captResult2;
                            } catch (Exception e5) {
                                e = e5;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            captResult2 = captResult3;
                        }
                    } else {
                        captResult2 = captResult3;
                        try {
                            captResult2.statusCode = nRet3;
                            nRet = nRet3;
                            captResult2 = captResult2;
                        } catch (Exception e7) {
                            e = e7;
                            e.printStackTrace();
                            captResult = captResult2;
                            return captResult;
                        }
                    }
                } catch (Exception e8) {
                    e = e8;
                    captResult2 = captResult3;
                }
                try {
                    captResult2.errorString = getCaptureErrorMessage(nRet);
                    captResult = captResult2;
                } catch (Exception e9) {
                    e = e9;
                    e.printStackTrace();
                    captResult = captResult2;
                    return captResult;
                }
            } catch (Exception e10) {
                e = e10;
                captResult2 = captResult3;
            }
        } catch (Exception e11) {
            e = e11;
            captResult2 = captResult3;
        }
        return captResult;
    }

    public DeviceInfo getDeviceInfo() {
        int nRet;
        DeviceInfo devInfo = new DeviceInfo();
        try {
            nRet = tmfIsDeviceConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TMF20ErrorCodes.SUCCESS == nRet) {
            devInfo.serialNumber = getTMF20SerialNumber();
            devInfo.make = "Tatvik";
            devInfo.model = "TMF20";
            devInfo.errorCode = TMF20ErrorCodes.SUCCESS;
            devInfo.errorString = "Success";
            return devInfo;
        }
        devInfo.errorCode = nRet;
        devInfo.errorString = "Deviceinfo fetching failed";
        return devInfo;
    }

    public boolean matchIsoTemplates(byte[] referenceTemplateData, byte[] claimedTemplateData) {
        try {
            this.tmfScannerAlgo = new zzFingerAlg();
            if (TMF20ErrorCodes.SUCCESS != tmfIsDeviceConnected() || referenceTemplateData == null || claimedTemplateData == null) {
                return false;
            }
            if (TMF20ErrorCodes.SUCCESS == this.tmfScannerAlgo.tmfFingerMatchFMR(referenceTemplateData, claimedTemplateData, 3)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getTMF20SerialNumber() {
        byte[] serialNumber = new byte[15];
        byte[] deviceUUID = new byte[37];
        if (TMF20ErrorCodes.SUCCESS == tmfIsDeviceConnected() && TMF20ErrorCodes.SUCCESS == tmfGetDeviceUniqueCode(deviceUUID, serialNumber)) {
            return new String(serialNumber).replaceFirst("^0*", "").replace("/0", "");
        }
        return "";
    }

    private int getNfiqValue(int fmrQuality) {
        if (fmrQuality == 0) {
            return 0;
        }
        if (fmrQuality <= 5) {
            return fmrQuality;
        }
        int nfiqValue = 6 - ((int) (((double) fmrQuality) / 20.0d));
        if (1 == nfiqValue) {
            return 1;
        }
        if (nfiqValue > 5) {
            return 5;
        }
        return nfiqValue - 1;
    }

    private static String getCaptureErrorMessage(int nErrorCode) {
        if (TMF20ErrorCodes.SUCCESS == nErrorCode) {
            return "TMF20 - Capture Successful";
        }
        if (TMF20ErrorCodes.TIMEOUT == nErrorCode) {
            return "TMF20 - Capture Timeout";
        }
        if (TMF20ErrorCodes.RESIDUAL_FP == nErrorCode) {
            return "TMF20 - Finger is too dry or Scanner is not clean.\nApply moisture on finger and clean the scanner surface";
        }
        if (TMF20ErrorCodes.DEVICE_NOT_CONNECTED == nErrorCode) {
            return "TMF20 - Please Connect TMF20 Device";
        }
        return "TMF20 - Capture Failed. ErrorCode: " + nErrorCode;
    }

    public void unRegUsbMonitor() {
        this.m_sm91Driver.unRegUsbMonitor();
    }

    private int tmfIsDeviceConnected() {
        return tmfGetDeviceVersion(new byte[128]);
    }

    private int tmfGetDeviceVersion(byte[] bVersion) {
        int nRet = this.m_sm91Driver.zzOpenDev();
        if (nRet != 0) {
            return nRet;
        }
        int nRet2 = this.m_sm91Driver.zzGetDevVersion(bVersion);
        this.m_sm91Driver.zzCloseDev();
        return nRet2;
    }

    private int tmfGetDeviceUniqueCode(byte[] bDeviceUUID, byte[] bSerialNumer) {
        byte[] bDeviceUUIDTmp = new byte[128];
        byte[] bSerialNumerTmp = new byte[128];
        int nRet = this.m_sm91Driver.zzOpenDev();
        if (nRet != 0) {
            return nRet;
        }
        int nRet2 = this.m_sm91Driver.zzGetDevUUID(bDeviceUUIDTmp);
        if (nRet2 != 0) {
            this.m_sm91Driver.zzCloseDev();
            return nRet2;
        }
        for (int i = 0; i < 36; i++) {
            bDeviceUUID[i] = bDeviceUUIDTmp[i];
        }
        int nRet3 = this.m_sm91Driver.zzGetDevSerial(bSerialNumerTmp);
        if (nRet3 != 0) {
            this.m_sm91Driver.zzCloseDev();
            return nRet3;
        }
        for (int i2 = 0; i2 < 14; i2++) {
            bSerialNumer[i2] = bSerialNumerTmp[i2];
        }
        this.m_sm91Driver.zzCloseDev();
        return nRet3;
    }

    public int tmfGetDeviceInfo(int[] deviceId, int[] imageWidth, int[] imageHeight, int[] ppi, int[] hwVer, int[] fwVer) {
        byte[] szVersion = new byte[128];
        int nRet = tmfGetDeviceVersion(szVersion);
        if (nRet != 0) {
            return nRet;
        }
        String strTemp = new String(szVersion);
        int StartPoint = strTemp.indexOf("-");
        int EndPoint = strTemp.lastIndexOf("-");
        String strDiff = strTemp.substring(StartPoint + 1, EndPoint);
        String strFirmInt = strTemp.substring(EndPoint + 2, strTemp.length()).replace(".", "").replace("\u0000", "");
        if (strDiff.equals("SIGNED")) {
            deviceId[0] = 7322;
            imageWidth[0] = 300;
            imageHeight[0] = 400;
            ppi[0] = 500;
            hwVer[0] = 2005;
            try {
                fwVer[0] = Integer.parseInt(strFirmInt);
            } catch (NumberFormatException var17) {
                var17.printStackTrace();
            }
        } else {
            int nRet2 = this.m_sm91Driver.zzOpenDev();
            if (nRet2 != 0) {
                return nRet2;
            }
            nRet = this.m_sm91Driver.zzGetDevInfo(deviceId, imageWidth, imageHeight, ppi, hwVer, fwVer);
            this.m_sm91Driver.zzCloseDev();
            if (nRet != 0) {
                return nRet;
            }
        }
        return nRet;
    }

    private int tmfCaptureFingerprint(byte[] bImgBuf, int[] iImgWidth, int[] iImgHeigth, int iTimeOut, int iFlagLeave, Observer callback) {
        int nRet = this.m_sm91Driver.zzOpenDev();
        if (nRet != 0) {
            return nRet;
        }
        int[] iMode = new int[1];
        int nRet2 = this.m_sm91Driver.zzGetL0L1Mode(iMode);
        if (nRet2 != 0) {
            this.m_sm91Driver.zzCloseDev();
            return nRet2;
        } else if (iMode[0] == 1) {
            this.m_sm91Driver.zzCloseDev();
            return 39;
        } else {
            int nRet3 = this.m_sm91Driver.zzGetEncryptModeAndSetKey();
            if (nRet3 != 0) {
                this.m_sm91Driver.zzCloseDev();
                return nRet3;
            }
            int nRet4 = this.m_sm91Driver.zzUpImageL0(bImgBuf, iImgWidth, iImgHeigth, iTimeOut);
            if (nRet4 == 38) {
                this.m_sm91Driver.zzCancel();
            }
            if (nRet4 != 0) {
                this.m_sm91Driver.zzDevSensorClose();
                this.m_sm91Driver.zzCloseDev();
                return nRet4;
            }
            this.m_imgProcess.ImgArea(bImgBuf, iImgWidth[0], iImgHeigth[0]);
            this.m_sm91Driver.zzDevSensorClose();
            this.m_sm91Driver.zzCloseDev();
            return nRet4;
        }
    }

    /* JADX INFO: Multiple debug info for r10v3 byte[]: [D('nRet' int), D('bSampleImage2' byte[])] */
    /* JADX INFO: Multiple debug info for r6v1 byte[]: [D('bSampleImage1' byte[]), D('iArea' int)] */
    public int tmfCaptureFingerprintwithThreshold(byte[] bImgBuf, int[] iImgWidth, int[] iImgHeigth, int iTimeOut, int iFlagLeave, int threshold, Observer callback) {
        int dwWaitTime;
        Calendar time2Tmp;
        int i;
        int nRet;
        int iArea;
        byte[] tmpImgData;
        int i2 = iTimeOut;
        CallbackMessage message = new CallbackMessage();
        if (callback != null) {
            addObserver(callback);
        }
        int nRet2 = this.m_sm91Driver.zzOpenDev();
        if (nRet2 != 0) {
            return nRet2;
        }
        Calendar time1Tmp = Calendar.getInstance();
        int nRet3 = this.m_sm91Driver.zzDevLampOnOff(1);
        Calendar time2Tmp2 = Calendar.getInstance();
        long durationTmp = time2Tmp2.getTimeInMillis() - time1Tmp.getTimeInMillis();
        if (nRet3 != 0) {
            this.m_sm91Driver.zzCloseDev();
            return nRet3;
        }
        message.setMessageType(6);
        message.setMessage(Long.valueOf(durationTmp));
        notifyDataChange(callback, message);
        this.m_bCancelGetImage = false;
        int dwWaitTime2 = PathInterpolatorCompat.MAX_NUM_POINTS;
        if (i2 > 3000) {
            dwWaitTime2 = iTimeOut;
        }
        boolean bFirst = false;
        int iArea2 = 0;
        byte[] bSampleImage1 = new byte[120000];
        int nRet4 = nRet3;
        byte[] bSampleImage2 = new byte[120000];
        int compareRet = -1;
        long duration = -1;
        Calendar time1 = Calendar.getInstance();
        while (true) {
            if (this.m_bCancelGetImage) {
                dwWaitTime = dwWaitTime2;
                time2Tmp = time2Tmp2;
                i = i2;
                break;
            }
            time2Tmp = time2Tmp2;
            if (duration > ((long) i2)) {
                dwWaitTime = dwWaitTime2;
                i = i2;
                break;
            }
            LogUnit.SendMsg("=============================");
            Calendar time1Tmp2 = Calendar.getInstance();
            int nRet5 = this.m_sm91Driver.zzUpImage(bImgBuf, iImgWidth, iImgHeigth, dwWaitTime2);
            Calendar time2Tmp3 = Calendar.getInstance();
            long durationTmp2 = time2Tmp3.getTimeInMillis() - time1Tmp2.getTimeInMillis();
            LogUnit.SendMsg("zzUpImage85 time:" + durationTmp2 + " ms");
            if (nRet5 != 0) {
                LogUnit.SendMsg("==========break nRet=" + nRet5);
                nRet = nRet5;
                i = i2;
                dwWaitTime = dwWaitTime2;
                iArea = iArea2;
                break;
            } else if (!bFirst) {
                this.m_imgProcess.CalcFingerMeanVar(bImgBuf, iImgWidth[0], iImgHeigth[0], 1);
                this.m_imgProcess.CalSamplingData(bImgBuf, iImgWidth[0], iImgHeigth[0], bSampleImage1);
                bFirst = true;
                time2Tmp2 = time2Tmp3;
                time1Tmp = time1Tmp2;
                nRet4 = nRet5;
            } else {
                nRet = nRet5;
                message.setMessageType(2);
                message.setMessage(Long.valueOf(durationTmp2));
                notifyDataChange(callback, message);
                Calendar time1Tmp3 = Calendar.getInstance();
                int iArea3 = this.m_imgProcess.ImgArea(bImgBuf, iImgWidth[0], iImgHeigth[0]);
                long durationTmp3 = Calendar.getInstance().getTimeInMillis() - time1Tmp3.getTimeInMillis();
                LogUnit.SendMsg("ImgArea time:" + durationTmp3 + " ms");
                if (iArea3 > 40) {
                    dwWaitTime = dwWaitTime2;
                    this.m_imgProcess.CalSamplingData(bImgBuf, iImgWidth[0], iImgHeigth[0], bSampleImage2);
                    this.m_imgProcess.CalcFingerMeanVar(bImgBuf, iImgWidth[0], iImgHeigth[0], 1);
                } else {
                    dwWaitTime = dwWaitTime2;
                }
                message.setMessageType(3);
                message.setMessage(Integer.valueOf(iArea3));
                notifyDataChange(callback, message);
                message.setMessageType(4);
                message.setMessage(Long.valueOf(durationTmp3));
                notifyDataChange(callback, message);
                Calendar time1Tmp4 = Calendar.getInstance();
                this.m_imgProcess.ImgEnchance(bImgBuf, iImgWidth[0], iImgHeigth[0]);
                Calendar time2Tmp4 = Calendar.getInstance();
                long durationTmp4 = time2Tmp4.getTimeInMillis() - time1Tmp4.getTimeInMillis();
                LogUnit.SendMsg("ImgEnchance time:" + durationTmp4 + " ms");
                message.setMessageType(5);
                message.setMessage(Long.valueOf(durationTmp4));
                notifyDataChange(callback, message);
                MXImage mxImage = new MXImage();
                mxImage.setWidth(iImgWidth[0]);
                mxImage.setHeight(iImgHeigth[0]);
                mxImage.setImage(bImgBuf);
                CallbackMessage imgMessage = new CallbackMessage();
                imgMessage.setMessageType(1);
                imgMessage.setMessage(mxImage);
                notifyDataChange(callback, imgMessage);
                if (iArea3 > 40) {
                    compareRet = this.m_imgProcess.compareWithPreviousImage_zzWithThreshold(bSampleImage1, bSampleImage2, 576, threshold);
                    i = iTimeOut;
                    iArea = iArea3;
                    break;
                }
                SystemClock.sleep(10);
                Calendar time2 = Calendar.getInstance();
                if (iTimeOut != 0) {
                    duration = time2.getTimeInMillis() - time1.getTimeInMillis();
                }
                i2 = iTimeOut;
                time2Tmp2 = time2Tmp4;
                dwWaitTime2 = dwWaitTime;
                nRet4 = nRet;
                bFirst = bFirst;
                iArea2 = iArea3;
                time1Tmp = time1Tmp4;
            }
        }
        iArea = iArea2;
        nRet = nRet4;
        if (callback != null) {
            deleteObserver(callback);
        }
        if (this.m_bCancelGetImage) {
            this.m_sm91Driver.zzDevSensorClose();
            this.m_sm91Driver.zzDevLampOnOff(0);
            this.m_sm91Driver.zzCloseDev();
            return 38;
        } else if (duration > ((long) i)) {
            this.m_sm91Driver.zzDevSensorClose();
            this.m_sm91Driver.zzDevLampOnOff(0);
            this.m_sm91Driver.zzCloseDev();
            return 41;
        } else if (nRet == 0 && compareRet == 0) {
            this.m_sm91Driver.zzDevSensorClose();
            this.m_sm91Driver.zzDevLampOnOff(0);
            this.m_sm91Driver.zzCloseDev();
            return 30;
        } else {
            if (nRet == 0 && (nRet = this.m_sm91Driver.zzUpImage((tmpImgData = new byte[120000]), iImgWidth, iImgHeigth, dwWaitTime)) == 0 && this.m_imgProcess.ImgArea(tmpImgData, iImgWidth[0], iImgHeigth[0]) > iArea) {
                System.arraycopy(tmpImgData, 0, bImgBuf, 0, iImgWidth[0] * iImgHeigth[0]);
                this.m_imgProcess.ImgEnchance(bImgBuf, iImgWidth[0], iImgHeigth[0]);
            }
            this.m_sm91Driver.zzDevSensorClose();
            this.m_sm91Driver.zzDevLampOnOff(0);
            this.m_sm91Driver.zzCloseDev();
            return nRet;
        }
    }
}
