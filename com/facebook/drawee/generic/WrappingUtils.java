package com.facebook.drawee.generic;

import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.drawable.DrawableParent;
import com.facebook.drawee.drawable.ForwardingDrawable;
import com.facebook.drawee.drawable.MatrixDrawable;
import com.facebook.drawee.drawable.Rounded;
import com.facebook.drawee.drawable.RoundedBitmapDrawable;
import com.facebook.drawee.drawable.RoundedColorDrawable;
import com.facebook.drawee.drawable.RoundedCornersDrawable;
import com.facebook.drawee.drawable.RoundedNinePatchDrawable;
import com.facebook.drawee.drawable.ScaleTypeDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.imagepipeline.systrace.FrescoSystrace;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class WrappingUtils {
    private static final String TAG = "WrappingUtils";
    private static final Drawable sEmptyDrawable = new ColorDrawable(0);

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public static Drawable maybeWrapWithScaleType(@Nullable Drawable drawable, @Nullable ScalingUtils.ScaleType scaleType) {
        return maybeWrapWithScaleType(drawable, scaleType, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public static Drawable maybeWrapWithScaleType(@Nullable Drawable drawable, @Nullable ScalingUtils.ScaleType scaleType, @Nullable PointF focusPoint) {
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.beginSection("WrappingUtils#maybeWrapWithScaleType");
        }
        if (drawable == null || scaleType == null) {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.endSection();
            }
            return drawable;
        }
        ScaleTypeDrawable scaleTypeDrawable = new ScaleTypeDrawable(drawable, scaleType);
        if (focusPoint != null) {
            scaleTypeDrawable.setFocusPoint(focusPoint);
        }
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.endSection();
        }
        return scaleTypeDrawable;
    }

    @Nullable
    static Drawable maybeWrapWithMatrix(@Nullable Drawable drawable, @Nullable Matrix matrix) {
        if (drawable == null || matrix == null) {
            return drawable;
        }
        return new MatrixDrawable(drawable, matrix);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ScaleTypeDrawable wrapChildWithScaleType(DrawableParent parent, ScalingUtils.ScaleType scaleType) {
        Drawable child = maybeWrapWithScaleType(parent.setDrawable(sEmptyDrawable), scaleType);
        parent.setDrawable(child);
        Preconditions.checkNotNull(child, "Parent has no child drawable!");
        return (ScaleTypeDrawable) child;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void updateOverlayColorRounding(DrawableParent parent, @Nullable RoundingParams roundingParams) {
        Drawable child = parent.getDrawable();
        if (roundingParams == null || roundingParams.getRoundingMethod() != RoundingParams.RoundingMethod.OVERLAY_COLOR) {
            if (child instanceof RoundedCornersDrawable) {
                parent.setDrawable(((RoundedCornersDrawable) child).setCurrent(sEmptyDrawable));
                sEmptyDrawable.setCallback(null);
            }
        } else if (child instanceof RoundedCornersDrawable) {
            RoundedCornersDrawable roundedCornersDrawable = (RoundedCornersDrawable) child;
            applyRoundingParams(roundedCornersDrawable, roundingParams);
            roundedCornersDrawable.setOverlayColor(roundingParams.getOverlayColor());
        } else {
            parent.setDrawable(maybeWrapWithRoundedOverlayColor(parent.setDrawable(sEmptyDrawable), roundingParams));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void updateLeafRounding(DrawableParent parent, @Nullable RoundingParams roundingParams, Resources resources) {
        DrawableParent parent2 = findDrawableParentForLeaf(parent);
        Drawable child = parent2.getDrawable();
        if (roundingParams == null || roundingParams.getRoundingMethod() != RoundingParams.RoundingMethod.BITMAP_ONLY) {
            if (child instanceof Rounded) {
                resetRoundingParams((Rounded) child);
            }
        } else if (child instanceof Rounded) {
            applyRoundingParams((Rounded) child, roundingParams);
        } else if (child != null) {
            parent2.setDrawable(sEmptyDrawable);
            parent2.setDrawable(applyLeafRounding(child, roundingParams, resources));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Drawable maybeWrapWithRoundedOverlayColor(@Nullable Drawable drawable, @Nullable RoundingParams roundingParams) {
        try {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.beginSection("WrappingUtils#maybeWrapWithRoundedOverlayColor");
            }
            if (!(drawable == null || roundingParams == null || roundingParams.getRoundingMethod() != RoundingParams.RoundingMethod.OVERLAY_COLOR)) {
                RoundedCornersDrawable roundedCornersDrawable = new RoundedCornersDrawable(drawable);
                applyRoundingParams(roundedCornersDrawable, roundingParams);
                roundedCornersDrawable.setOverlayColor(roundingParams.getOverlayColor());
                return roundedCornersDrawable;
            }
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.endSection();
            }
            return drawable;
        } finally {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.endSection();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Drawable maybeApplyLeafRounding(@Nullable Drawable drawable, @Nullable RoundingParams roundingParams, Resources resources) {
        try {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.beginSection("WrappingUtils#maybeApplyLeafRounding");
            }
            if (!(drawable == null || roundingParams == null || roundingParams.getRoundingMethod() != RoundingParams.RoundingMethod.BITMAP_ONLY)) {
                if (drawable instanceof ForwardingDrawable) {
                    DrawableParent parent = findDrawableParentForLeaf((ForwardingDrawable) drawable);
                    parent.setDrawable(applyLeafRounding(parent.setDrawable(sEmptyDrawable), roundingParams, resources));
                    return drawable;
                }
                Drawable applyLeafRounding = applyLeafRounding(drawable, roundingParams, resources);
                if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.endSection();
                }
                return applyLeafRounding;
            }
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.endSection();
            }
            return drawable;
        } finally {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.endSection();
            }
        }
    }

    private static Drawable applyLeafRounding(Drawable drawable, RoundingParams roundingParams, Resources resources) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            RoundedBitmapDrawable roundedBitmapDrawable = new RoundedBitmapDrawable(resources, bitmapDrawable.getBitmap(), bitmapDrawable.getPaint());
            applyRoundingParams(roundedBitmapDrawable, roundingParams);
            return roundedBitmapDrawable;
        } else if (drawable instanceof NinePatchDrawable) {
            RoundedNinePatchDrawable roundedNinePatchDrawable = new RoundedNinePatchDrawable((NinePatchDrawable) drawable);
            applyRoundingParams(roundedNinePatchDrawable, roundingParams);
            return roundedNinePatchDrawable;
        } else if (!(drawable instanceof ColorDrawable) || Build.VERSION.SDK_INT < 11) {
            FLog.w(TAG, "Don't know how to round that drawable: %s", drawable);
            return drawable;
        } else {
            RoundedColorDrawable roundedColorDrawable = RoundedColorDrawable.fromColorDrawable((ColorDrawable) drawable);
            applyRoundingParams(roundedColorDrawable, roundingParams);
            return roundedColorDrawable;
        }
    }

    static void applyRoundingParams(Rounded rounded, RoundingParams roundingParams) {
        rounded.setCircle(roundingParams.getRoundAsCircle());
        rounded.setRadii(roundingParams.getCornersRadii());
        rounded.setBorder(roundingParams.getBorderColor(), roundingParams.getBorderWidth());
        rounded.setPadding(roundingParams.getPadding());
        rounded.setScaleDownInsideBorders(roundingParams.getScaleDownInsideBorders());
    }

    static void resetRoundingParams(Rounded rounded) {
        rounded.setCircle(false);
        rounded.setRadius(0.0f);
        rounded.setBorder(0, 0.0f);
        rounded.setPadding(0.0f);
        rounded.setScaleDownInsideBorders(false);
    }

    static DrawableParent findDrawableParentForLeaf(DrawableParent parent) {
        while (true) {
            Drawable child = parent.getDrawable();
            if (child == parent || !(child instanceof DrawableParent)) {
                break;
            }
            parent = (DrawableParent) child;
        }
        return parent;
    }
}
