package com.facebook.drawee.drawable;

import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
/* loaded from: classes.dex */
public class DrawableProperties {
    private static final int UNSET = -1;
    private int mAlpha = -1;
    private boolean mIsSetColorFilter = false;
    private ColorFilter mColorFilter = null;
    private int mDither = -1;
    private int mFilterBitmap = -1;

    public void setAlpha(int alpha) {
        this.mAlpha = alpha;
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mColorFilter = colorFilter;
        this.mIsSetColorFilter = true;
    }

    public void setDither(boolean dither) {
        this.mDither = dither ? 1 : 0;
    }

    public void setFilterBitmap(boolean filterBitmap) {
        this.mFilterBitmap = filterBitmap ? 1 : 0;
    }

    public void applyTo(Drawable drawable) {
        if (drawable != null) {
            int i = this.mAlpha;
            if (i != -1) {
                drawable.setAlpha(i);
            }
            if (this.mIsSetColorFilter) {
                drawable.setColorFilter(this.mColorFilter);
            }
            int i2 = this.mDither;
            boolean z = true;
            if (i2 != -1) {
                drawable.setDither(i2 != 0);
            }
            int i3 = this.mFilterBitmap;
            if (i3 != -1) {
                if (i3 == 0) {
                    z = false;
                }
                drawable.setFilterBitmap(z);
            }
        }
    }
}
