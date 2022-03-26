package com.facebook.drawee.debug;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import com.facebook.drawee.debug.listener.ImageLoadingTimeListener;
import com.facebook.drawee.drawable.ScalingUtils;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class DebugControllerOverlayDrawable extends Drawable implements ImageLoadingTimeListener {
    private static final float IMAGE_SIZE_THRESHOLD_NOT_OK = 0.5f;
    private static final float IMAGE_SIZE_THRESHOLD_OK = 0.1f;
    private static final int MAX_LINE_WIDTH_EM = 8;
    private static final int MAX_NUMBER_OF_LINES = 9;
    private static final int MAX_TEXT_SIZE_PX = 40;
    private static final int MIN_TEXT_SIZE_PX = 10;
    private static final String NO_CONTROLLER_ID = "none";
    private static final int OUTLINE_COLOR = -26624;
    private static final int OUTLINE_STROKE_WIDTH_PX = 2;
    static final int OVERLAY_COLOR_IMAGE_ALMOST_OK = 1728026624;
    static final int OVERLAY_COLOR_IMAGE_NOT_OK = 1727284022;
    static final int OVERLAY_COLOR_IMAGE_OK = 1716301648;
    private static final int TEXT_COLOR = -1;
    private static final int TEXT_LINE_SPACING_PX = 8;
    private static final int TEXT_PADDING_PX = 10;
    private String mControllerId;
    private int mCurrentTextXPx;
    private int mCurrentTextYPx;
    private long mFinalImageTimeMs;
    private int mFrameCount;
    private int mHeightPx;
    private String mImageFormat;
    private String mImageId;
    private int mImageSizeBytes;
    private int mLineIncrementPx;
    private int mLoopCount;
    private String mOrigin;
    private ScalingUtils.ScaleType mScaleType;
    private int mStartTextXPx;
    private int mStartTextYPx;
    private int mWidthPx;
    private int mTextGravity = 80;
    private final Paint mPaint = new Paint(1);
    private final Matrix mMatrix = new Matrix();
    private final Rect mRect = new Rect();
    private final RectF mRectF = new RectF();

    public DebugControllerOverlayDrawable() {
        reset();
    }

    public void reset() {
        this.mWidthPx = -1;
        this.mHeightPx = -1;
        this.mImageSizeBytes = -1;
        this.mFrameCount = -1;
        this.mLoopCount = -1;
        this.mImageFormat = null;
        setControllerId(null);
        this.mFinalImageTimeMs = -1;
        this.mOrigin = null;
        invalidateSelf();
    }

    public void setTextGravity(int textGravity) {
        this.mTextGravity = textGravity;
        invalidateSelf();
    }

    public void setControllerId(@Nullable String controllerId) {
        this.mControllerId = controllerId != null ? controllerId : NO_CONTROLLER_ID;
        invalidateSelf();
    }

    public void setImageId(@Nullable String imageId) {
        this.mImageId = imageId;
        invalidateSelf();
    }

    public void setDimensions(int widthPx, int heightPx) {
        this.mWidthPx = widthPx;
        this.mHeightPx = heightPx;
        invalidateSelf();
    }

    public void setAnimationInfo(int frameCount, int loopCount) {
        this.mFrameCount = frameCount;
        this.mLoopCount = loopCount;
        invalidateSelf();
    }

    public void setOrigin(String s) {
        this.mOrigin = s;
        invalidateSelf();
    }

    public void setImageSize(int imageSizeBytes) {
        this.mImageSizeBytes = imageSizeBytes;
    }

    public void setImageFormat(@Nullable String imageFormat) {
        this.mImageFormat = imageFormat;
    }

    public void setScaleType(ScalingUtils.ScaleType scaleType) {
        this.mScaleType = scaleType;
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        prepareDebugTextParameters(bounds, 9, 8);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(2.0f);
        this.mPaint.setColor(OUTLINE_COLOR);
        canvas.drawRect((float) bounds.left, (float) bounds.top, (float) bounds.right, (float) bounds.bottom, this.mPaint);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setColor(determineOverlayColor(this.mWidthPx, this.mHeightPx, this.mScaleType));
        canvas.drawRect((float) bounds.left, (float) bounds.top, (float) bounds.right, (float) bounds.bottom, this.mPaint);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setStrokeWidth(0.0f);
        this.mPaint.setColor(-1);
        this.mCurrentTextXPx = this.mStartTextXPx;
        this.mCurrentTextYPx = this.mStartTextYPx;
        String str = this.mImageId;
        if (str != null) {
            addDebugText(canvas, "IDs: %s, %s", this.mControllerId, str);
        } else {
            addDebugText(canvas, "ID: %s", this.mControllerId);
        }
        addDebugText(canvas, "D: %dx%d", Integer.valueOf(bounds.width()), Integer.valueOf(bounds.height()));
        addDebugText(canvas, "I: %dx%d", Integer.valueOf(this.mWidthPx), Integer.valueOf(this.mHeightPx));
        addDebugText(canvas, "I: %d KiB", Integer.valueOf(this.mImageSizeBytes / 1024));
        String str2 = this.mImageFormat;
        if (str2 != null) {
            addDebugText(canvas, "i format: %s", str2);
        }
        int i = this.mFrameCount;
        if (i > 0) {
            addDebugText(canvas, "anim: f %d, l %d", Integer.valueOf(i), Integer.valueOf(this.mLoopCount));
        }
        ScalingUtils.ScaleType scaleType = this.mScaleType;
        if (scaleType != null) {
            addDebugText(canvas, "scale: %s", scaleType);
        }
        long j = this.mFinalImageTimeMs;
        if (j >= 0) {
            addDebugText(canvas, "t: %d ms", Long.valueOf(j));
        }
        String str3 = this.mOrigin;
        if (str3 != null) {
            addDebugText(canvas, "origin: %s", str3);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter cf) {
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    private void prepareDebugTextParameters(Rect bounds, int numberOfLines, int maxLineLengthEm) {
        int textSizePx = Math.min(40, Math.max(10, Math.min(bounds.width() / maxLineLengthEm, bounds.height() / numberOfLines)));
        this.mPaint.setTextSize((float) textSizePx);
        this.mLineIncrementPx = textSizePx + 8;
        if (this.mTextGravity == 80) {
            this.mLineIncrementPx *= -1;
        }
        this.mStartTextXPx = bounds.left + 10;
        this.mStartTextYPx = this.mTextGravity == 80 ? bounds.bottom - 10 : bounds.top + 10 + 10;
    }

    private void addDebugText(Canvas canvas, String text, @Nullable Object... args) {
        if (args == null) {
            canvas.drawText(text, (float) this.mCurrentTextXPx, (float) this.mCurrentTextYPx, this.mPaint);
        } else {
            canvas.drawText(String.format(text, args), (float) this.mCurrentTextXPx, (float) this.mCurrentTextYPx, this.mPaint);
        }
        this.mCurrentTextYPx += this.mLineIncrementPx;
    }

    int determineOverlayColor(int imageWidth, int imageHeight, @Nullable ScalingUtils.ScaleType scaleType) {
        int visibleDrawnAreaWidth = getBounds().width();
        int visibleDrawnAreaHeight = getBounds().height();
        if (visibleDrawnAreaWidth <= 0 || visibleDrawnAreaHeight <= 0 || imageWidth <= 0 || imageHeight <= 0) {
            return OVERLAY_COLOR_IMAGE_NOT_OK;
        }
        if (scaleType != null) {
            Rect rect = this.mRect;
            rect.top = 0;
            rect.left = 0;
            rect.right = visibleDrawnAreaWidth;
            rect.bottom = visibleDrawnAreaHeight;
            this.mMatrix.reset();
            scaleType.getTransform(this.mMatrix, this.mRect, imageWidth, imageHeight, 0.0f, 0.0f);
            RectF rectF = this.mRectF;
            rectF.top = 0.0f;
            rectF.left = 0.0f;
            rectF.right = (float) imageWidth;
            rectF.bottom = (float) imageHeight;
            this.mMatrix.mapRect(rectF);
            visibleDrawnAreaWidth = Math.min(visibleDrawnAreaWidth, (int) this.mRectF.width());
            visibleDrawnAreaHeight = Math.min(visibleDrawnAreaHeight, (int) this.mRectF.height());
        }
        float scaledImageWidthThresholdOk = ((float) visibleDrawnAreaWidth) * IMAGE_SIZE_THRESHOLD_OK;
        float scaledImageWidthThresholdNotOk = ((float) visibleDrawnAreaWidth) * IMAGE_SIZE_THRESHOLD_NOT_OK;
        float scaledImageHeightThresholdOk = ((float) visibleDrawnAreaHeight) * IMAGE_SIZE_THRESHOLD_OK;
        float scaledImageHeightThresholdNotOk = ((float) visibleDrawnAreaHeight) * IMAGE_SIZE_THRESHOLD_NOT_OK;
        int absWidthDifference = Math.abs(imageWidth - visibleDrawnAreaWidth);
        int absHeightDifference = Math.abs(imageHeight - visibleDrawnAreaHeight);
        if (((float) absWidthDifference) < scaledImageWidthThresholdOk && ((float) absHeightDifference) < scaledImageHeightThresholdOk) {
            return OVERLAY_COLOR_IMAGE_OK;
        }
        if (((float) absWidthDifference) >= scaledImageWidthThresholdNotOk || ((float) absHeightDifference) >= scaledImageHeightThresholdNotOk) {
            return OVERLAY_COLOR_IMAGE_NOT_OK;
        }
        return OVERLAY_COLOR_IMAGE_ALMOST_OK;
    }

    public void setFinalImageTimeMs(long finalImageTimeMs) {
        this.mFinalImageTimeMs = finalImageTimeMs;
    }

    @Override // com.facebook.drawee.debug.listener.ImageLoadingTimeListener
    public void onFinalImageSet(long finalImageTimeMs) {
        this.mFinalImageTimeMs = finalImageTimeMs;
        invalidateSelf();
    }
}
