package org.zz.tool;

import android.os.Handler;
import android.os.Message;
import java.util.Calendar;
/* loaded from: classes3.dex */
public class LogUnit {
    public static final int SHOW_MSG = 255;
    public static boolean LOG_MSG = false;
    private static Handler m_fHandler = null;

    public static void SetHandler(Handler fHandler) {
        m_fHandler = fHandler;
    }

    public static void SendMsg(String obj) {
        if (LOG_MSG) {
            Message message = new Message();
            message.what = 255;
            message.obj = obj;
            message.arg1 = 0;
            Handler handler = m_fHandler;
            if (handler != null) {
                handler.sendMessage(message);
            }
        }
    }

    public void MySleep(int iTimeout) {
        Calendar time1 = Calendar.getInstance();
        for (long duration = -1; duration <= ((long) iTimeout); duration = Calendar.getInstance().getTimeInMillis() - time1.getTimeInMillis()) {
        }
    }
}
