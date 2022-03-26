package com.squareup.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public final class PicassoDrawable extends BitmapDrawable {
    private static final Paint DEBUG_PAINT = new Paint();
    private static final float FADE_DURATION = 200.0f;
    int alpha = 255;
    boolean animating;
    private final boolean debugging;
    private final float density;
    private final Picasso.LoadedFrom loadedFrom;
    Drawable placeholder;
    long startTimeMillis;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setBitmap(ImageView target, Context context, Bitmap bitmap, Picasso.LoadedFrom loadedFrom, boolean noFade, boolean debugging) {
        Drawable placeholder = target.getDrawable();
        if (placeholder instanceof Animatable) {
            ((Animatable) placeholder).stop();
        }
        target.setImageDrawable(new PicassoDrawable(context, bitmap, placeholder, loadedFrom, noFade, debugging));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setPlaceholder(ImageView target, Drawable placeholderDrawable) {
        target.setImageDrawable(placeholderDrawable);
        if (target.getDrawable() instanceof Animatable) {
            ((Animatable) target.getDrawable()).start();
        }
    }

    PicassoDrawable(Context context, Bitmap bitmap, Drawable placeholder, Picasso.LoadedFrom loadedFrom, boolean noFade, boolean debugging) {
        super(context.getResources(), bitmap);
        this.debugging = debugging;
        this.density = context.getResources().getDisplayMetrics().density;
        this.loadedFrom = loadedFrom;
        if (loadedFrom != Picasso.LoadedFrom.MEMORY && !noFade) {
            this.placeholder = placeholder;
            this.animating = true;
            this.startTimeMillis = SystemClock.uptimeMillis();
        }
    }

    @Override // android.graphics.drawable.BitmapDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (!this.animating) {
            super.draw(canvas);
        } else {
            float normalized = ((float) (SystemClock.uptimeMillis() - this.startTimeMillis)) / FADE_DURATION;
            if (normalized >= 1.0f) {
                this.animating = false;
                this.placeholder = null;
                super.draw(canvas);
            } else {
                Drawable drawable = this.placeholder;
                if (drawable != null) {
                    drawable.draw(canvas);
                }
                super.setAlpha((int) (((float) this.alpha) * normalized));
                super.draw(canvas);
                super.setAlpha(this.alpha);
            }
        }
        if (this.debugging) {
            drawDebugIndicator(canvas);
        }
    }

    @Override // android.graphics.drawable.BitmapDrawable, android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        this.alpha = alpha;
        Drawable drawable = this.placeholder;
        if (drawable != null) {
            drawable.setAlpha(alpha);
        }
        super.setAlpha(alpha);
    }

    @Override // android.graphics.drawable.BitmapDrawable, android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter cf) {
        Drawable drawable = this.placeholder;
        if (drawable != null) {
            drawable.setColorFilter(cf);
        }
        super.setColorFilter(cf);
    }

    @Override // android.graphics.drawable.BitmapDrawable, android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect bounds) {
        Drawable drawable = this.placeholder;
        if (drawable != null) {
            drawable.setBounds(bounds);
        }
        super.onBoundsChange(bounds);
    }

    private void drawDebugIndicator(Canvas canvas) {
        DEBUG_PAINT.setColor(-1);
        canvas.drawPath(getTrianglePath(0, 0, (int) (this.density * 16.0f)), DEBUG_PAINT);
        DEBUG_PAINT.setColor(this.loadedFrom.debugColor);
        canvas.drawPath(getTrianglePath(0, 0, (int) (this.density * 15.0f)), DEBUG_PAINT);
    }

    private static Path getTrianglePath(int x1, int y1, int width) {
        Path path = new Path();
        path.moveTo((float) x1, (float) y1);
        path.lineTo((float) (x1 + width), (float) y1);
        path.lineTo((float) x1, (float) (y1 + width));
        return path;
    }
}
