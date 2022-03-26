package org.zz.mxfingerdriver;
/* loaded from: classes3.dex */
public class CallbackMessage {
    public static final int AREA_CALC_TIME = 4;
    public static final int CLOSE_LAMP = 7;
    public static final int CLOSE_SENSOR = 9;
    public static final int GET_IMG_TIME = 2;
    public static final int IMG_AREA = 3;
    public static final int IMG_DATA = 1;
    public static final int IMG_ENC_TIME = 5;
    public static final int OPEN_LAMP = 6;
    public static final int OPEN_SENSOR = 8;
    private Object message;
    private int messageType;

    public int getMessageType() {
        return this.messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Object getMessage() {
        return this.message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
