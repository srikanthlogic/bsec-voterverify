package org.zz.jni;
/* loaded from: classes3.dex */
public class MXImageProcess {
    public native void CalSamplingData(byte[] bArr, int i, int i2, byte[] bArr2);

    public native int CalcFingerMeanVar(byte[] bArr, int i, int i2, int i3);

    public native int ImgArea(byte[] bArr, int i, int i2);

    public native int ImgEnchance(byte[] bArr, int i, int i2);

    public native int compareWithPreviousImage_zz(byte[] bArr, byte[] bArr2, int i);

    public native int compareWithPreviousImage_zzWithThreshold(byte[] bArr, byte[] bArr2, int i, int i2);

    static {
        System.loadLibrary("MXImageProcess");
    }
}
