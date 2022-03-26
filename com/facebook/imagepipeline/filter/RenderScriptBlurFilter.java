package com.facebook.imagepipeline.filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import com.facebook.common.internal.Preconditions;
/* loaded from: classes.dex */
public abstract class RenderScriptBlurFilter {
    public static final int BLUR_MAX_RADIUS = 25;

    public static void blurBitmap(Bitmap dest, Bitmap src, Context context, int radius) {
        Preconditions.checkNotNull(dest);
        Preconditions.checkNotNull(src);
        Preconditions.checkNotNull(context);
        Preconditions.checkArgument(radius > 0 && radius <= 25);
        RenderScript rs = null;
        try {
            rs = RenderScript.create(context);
            ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            Allocation allIn = Allocation.createFromBitmap(rs, src);
            Allocation allOut = Allocation.createFromBitmap(rs, dest);
            blurScript.setRadius((float) radius);
            blurScript.setInput(allIn);
            blurScript.forEach(allOut);
            allOut.copyTo(dest);
        } finally {
            if (rs != null) {
                rs.destroy();
            }
        }
    }

    public static boolean canUseRenderScript() {
        return Build.VERSION.SDK_INT >= 17;
    }
}
