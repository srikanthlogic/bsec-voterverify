package com.facebook.imagepipeline.filter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import androidx.core.view.ViewCompat;
import com.facebook.common.internal.Preconditions;
/* loaded from: classes.dex */
public final class XferRoundFilter {
    private XferRoundFilter() {
    }

    public static void xferRoundBitmap(Bitmap output, Bitmap source, boolean enableAntiAliasing) {
        Paint circlePaint;
        Paint xfermodePaint;
        Preconditions.checkNotNull(source);
        Preconditions.checkNotNull(output);
        output.setHasAlpha(true);
        if (enableAntiAliasing) {
            circlePaint = new Paint(1);
            xfermodePaint = new Paint(1);
        } else {
            circlePaint = new Paint();
            xfermodePaint = new Paint();
        }
        circlePaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        xfermodePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Canvas canvas = new Canvas(output);
        float xCenter = ((float) source.getWidth()) / 2.0f;
        float yCenter = ((float) source.getHeight()) / 2.0f;
        canvas.drawCircle(xCenter, yCenter, Math.min(xCenter, yCenter), circlePaint);
        canvas.drawBitmap(source, 0.0f, 0.0f, xfermodePaint);
    }

    public static boolean canUseXferRoundFilter() {
        return Build.VERSION.SDK_INT >= 12;
    }
}
