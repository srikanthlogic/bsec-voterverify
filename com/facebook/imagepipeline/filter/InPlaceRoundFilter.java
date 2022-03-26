package com.facebook.imagepipeline.filter;

import android.graphics.Bitmap;
import com.facebook.common.internal.Preconditions;
/* loaded from: classes.dex */
public final class InPlaceRoundFilter {
    private InPlaceRoundFilter() {
    }

    /* JADX INFO: Multiple debug info for r11v3 int: [D('centerX' int), D('offC' int)] */
    /* JADX INFO: Multiple debug info for r14v2 int: [D('cYpY' int), D('offD' int)] */
    /* JADX INFO: Multiple debug info for r6v5 int: [D('offA' int), D('maxY' int)] */
    /* JADX INFO: Multiple debug info for r7v5 int: [D('offB' int), D('maxX' int)] */
    public static void roundBitmapInPlace(Bitmap bitmap) {
        Preconditions.checkNotNull(bitmap);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int radius = Math.min(w, h) / 2;
        int offC = w / 2;
        int centerY = h / 2;
        if (radius != 0) {
            Preconditions.checkArgument(radius >= 1);
            Preconditions.checkArgument(w > 0 && ((float) w) <= 2048.0f);
            Preconditions.checkArgument(h > 0 && ((float) h) <= 2048.0f);
            Preconditions.checkArgument(offC > 0 && offC < w);
            Preconditions.checkArgument(centerY > 0 && centerY < h);
            int[] pixels = new int[w * h];
            bitmap.getPixels(pixels, 0, w, 0, 0, w, h);
            int x = radius - 1;
            int offB = offC + x;
            int maxY = centerY + x;
            Preconditions.checkArgument(offC - x >= 0 && centerY - x >= 0 && offB < w && maxY < h);
            int rInc = (-radius) * 2;
            int[] transparentColor = new int[w];
            int dx = 1;
            int dy = 1;
            int err = 1 + rInc;
            int x2 = x;
            int y = 0;
            while (x2 >= y) {
                int cXpX = offC + x2;
                int cXmX = offC - x2;
                int cXpY = offC + y;
                int cXmY = offC - y;
                int cYpX = centerY + x2;
                int cYmX = centerY - x2;
                int cYpY = centerY + y;
                int cYmY = centerY - y;
                Preconditions.checkArgument(x2 >= 0 && cXpY < w && cXmY >= 0 && cYpY < h && cYmY >= 0);
                int maxY2 = w * cYpY;
                int maxX = w * cYmY;
                int offC2 = w * cYpX;
                int offD = w * cYmX;
                System.arraycopy(transparentColor, 0, pixels, maxY2, cXmX);
                System.arraycopy(transparentColor, 0, pixels, maxX, cXmX);
                System.arraycopy(transparentColor, 0, pixels, offC2, cXmY);
                System.arraycopy(transparentColor, 0, pixels, offD, cXmY);
                System.arraycopy(transparentColor, 0, pixels, maxY2 + cXpX, w - cXpX);
                System.arraycopy(transparentColor, 0, pixels, maxX + cXpX, w - cXpX);
                System.arraycopy(transparentColor, 0, pixels, offC2 + cXpY, w - cXpY);
                System.arraycopy(transparentColor, 0, pixels, offD + cXpY, w - cXpY);
                if (err <= 0) {
                    y++;
                    dy += 2;
                    err += dy;
                }
                if (err > 0) {
                    x2--;
                    dx += 2;
                    err += dx + rInc;
                    maxY = maxY;
                    offB = offB;
                    offC = offC;
                    h = h;
                } else {
                    maxY = maxY;
                    offB = offB;
                    offC = offC;
                    h = h;
                }
            }
            int h2 = h;
            for (int i = centerY - radius; i >= 0; i--) {
                System.arraycopy(transparentColor, 0, pixels, i * w, w);
            }
            int i2 = centerY + radius;
            while (i2 < h2) {
                System.arraycopy(transparentColor, 0, pixels, i2 * w, w);
                i2++;
                h2 = h2;
            }
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h2);
        }
    }
}
