package com.facebook.drawee.generic;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import com.facebook.drawee.R;
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.imagepipeline.systrace.FrescoSystrace;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class GenericDraweeHierarchyInflater {
    public static GenericDraweeHierarchy inflateHierarchy(Context context, @Nullable AttributeSet attrs) {
        return inflateBuilder(context, attrs).build();
    }

    public static GenericDraweeHierarchyBuilder inflateBuilder(Context context, @Nullable AttributeSet attrs) {
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.beginSection("GenericDraweeHierarchyBuilder#inflateBuilder");
        }
        GenericDraweeHierarchyBuilder builder = updateBuilder(new GenericDraweeHierarchyBuilder(context.getResources()), context, attrs);
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.endSection();
        }
        return builder;
    }

    /* JADX WARN: Removed duplicated region for block: B:182:0x0276  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x02a1  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x02a4 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:206:0x02ab A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:210:0x02b2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:214:0x02b9 A[ADDED_TO_REGION] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static GenericDraweeHierarchyBuilder updateBuilder(GenericDraweeHierarchyBuilder builder, Context context, @Nullable AttributeSet attrs) {
        int progressBarAutoRotateInterval;
        int roundedCornerRadius;
        boolean z;
        Throwable th;
        boolean roundBottomLeft;
        Context context2 = context;
        boolean roundTopLeft = true;
        boolean roundTopRight = true;
        boolean roundBottomLeft2 = true;
        boolean roundBottomRight = true;
        boolean roundTopStart = true;
        boolean roundTopEnd = true;
        boolean roundBottomStart = true;
        boolean roundBottomEnd = true;
        if (attrs != null) {
            TypedArray gdhAttrs = context2.obtainStyledAttributes(attrs, R.styleable.GenericDraweeHierarchy);
            try {
                int i = 0;
                int roundedCornerRadius2 = 0;
                progressBarAutoRotateInterval = 0;
                for (int indexCount = gdhAttrs.getIndexCount(); i < indexCount; indexCount = indexCount) {
                    try {
                        int attr = gdhAttrs.getIndex(i);
                        if (attr == R.styleable.GenericDraweeHierarchy_actualImageScaleType) {
                            builder.setActualImageScaleType(getScaleTypeFromXml(gdhAttrs, attr));
                        } else if (attr == R.styleable.GenericDraweeHierarchy_placeholderImage) {
                            builder.setPlaceholderImage(getDrawable(context2, gdhAttrs, attr));
                        } else if (attr == R.styleable.GenericDraweeHierarchy_pressedStateOverlayImage) {
                            builder.setPressedStateOverlay(getDrawable(context2, gdhAttrs, attr));
                        } else if (attr == R.styleable.GenericDraweeHierarchy_progressBarImage) {
                            builder.setProgressBarImage(getDrawable(context2, gdhAttrs, attr));
                        } else if (attr == R.styleable.GenericDraweeHierarchy_fadeDuration) {
                            z = false;
                            try {
                                builder.setFadeDuration(gdhAttrs.getInt(attr, 0));
                            } catch (Throwable th2) {
                                th = th2;
                                gdhAttrs.recycle();
                                if (Build.VERSION.SDK_INT < 17) {
                                }
                                if (roundTopLeft) {
                                }
                                if (roundTopRight) {
                                }
                                if (roundBottomRight) {
                                }
                                if (roundBottomLeft2) {
                                    z = roundBottomLeft;
                                }
                                throw th;
                            }
                        } else if (attr == R.styleable.GenericDraweeHierarchy_viewAspectRatio) {
                            builder.setDesiredAspectRatio(gdhAttrs.getFloat(attr, 0.0f));
                        } else if (attr == R.styleable.GenericDraweeHierarchy_placeholderImageScaleType) {
                            builder.setPlaceholderImageScaleType(getScaleTypeFromXml(gdhAttrs, attr));
                        } else if (attr == R.styleable.GenericDraweeHierarchy_retryImage) {
                            builder.setRetryImage(getDrawable(context2, gdhAttrs, attr));
                        } else if (attr == R.styleable.GenericDraweeHierarchy_retryImageScaleType) {
                            builder.setRetryImageScaleType(getScaleTypeFromXml(gdhAttrs, attr));
                        } else if (attr == R.styleable.GenericDraweeHierarchy_failureImage) {
                            builder.setFailureImage(getDrawable(context2, gdhAttrs, attr));
                        } else if (attr == R.styleable.GenericDraweeHierarchy_failureImageScaleType) {
                            builder.setFailureImageScaleType(getScaleTypeFromXml(gdhAttrs, attr));
                        } else if (attr == R.styleable.GenericDraweeHierarchy_progressBarImageScaleType) {
                            builder.setProgressBarImageScaleType(getScaleTypeFromXml(gdhAttrs, attr));
                        } else if (attr == R.styleable.GenericDraweeHierarchy_progressBarAutoRotateInterval) {
                            progressBarAutoRotateInterval = gdhAttrs.getInteger(attr, progressBarAutoRotateInterval);
                        } else if (attr == R.styleable.GenericDraweeHierarchy_backgroundImage) {
                            builder.setBackground(getDrawable(context2, gdhAttrs, attr));
                        } else if (attr == R.styleable.GenericDraweeHierarchy_overlayImage) {
                            builder.setOverlay(getDrawable(context2, gdhAttrs, attr));
                        } else if (attr == R.styleable.GenericDraweeHierarchy_roundAsCircle) {
                            try {
                                getRoundingParams(builder).setRoundAsCircle(gdhAttrs.getBoolean(attr, false));
                            } catch (Throwable th3) {
                                th = th3;
                                z = false;
                                gdhAttrs.recycle();
                                if (Build.VERSION.SDK_INT < 17) {
                                }
                                if (roundTopLeft) {
                                }
                                if (roundTopRight) {
                                }
                                if (roundBottomRight) {
                                }
                                if (roundBottomLeft2) {
                                }
                                throw th;
                            }
                        } else if (attr == R.styleable.GenericDraweeHierarchy_roundedCornerRadius) {
                            try {
                                roundedCornerRadius2 = gdhAttrs.getDimensionPixelSize(attr, roundedCornerRadius2);
                            } catch (Throwable th4) {
                                th = th4;
                                z = false;
                                gdhAttrs.recycle();
                                if (Build.VERSION.SDK_INT < 17) {
                                    roundBottomLeft = true;
                                    if (context.getResources().getConfiguration().getLayoutDirection() == 1) {
                                        if (!roundTopLeft || !roundTopEnd) {
                                        }
                                        if (!roundTopRight || !roundTopStart) {
                                        }
                                        if (!roundBottomRight || !roundBottomStart) {
                                        }
                                        if (roundBottomLeft2 && roundBottomEnd) {
                                            z = true;
                                        }
                                        throw th;
                                    }
                                } else {
                                    roundBottomLeft = true;
                                }
                                if (roundTopLeft || !roundTopStart) {
                                }
                                if (roundTopRight || !roundTopEnd) {
                                }
                                if (roundBottomRight || !roundBottomEnd) {
                                }
                                if (roundBottomLeft2 && roundBottomStart) {
                                    z = roundBottomLeft;
                                }
                                throw th;
                            }
                        } else {
                            try {
                                if (attr == R.styleable.GenericDraweeHierarchy_roundTopLeft) {
                                    roundTopLeft = gdhAttrs.getBoolean(attr, roundTopLeft);
                                    roundedCornerRadius2 = roundedCornerRadius2;
                                } else if (attr == R.styleable.GenericDraweeHierarchy_roundTopRight) {
                                    roundTopRight = gdhAttrs.getBoolean(attr, roundTopRight);
                                    roundedCornerRadius2 = roundedCornerRadius2;
                                } else if (attr == R.styleable.GenericDraweeHierarchy_roundBottomLeft) {
                                    roundBottomLeft2 = gdhAttrs.getBoolean(attr, roundBottomLeft2);
                                    roundedCornerRadius2 = roundedCornerRadius2;
                                } else if (attr == R.styleable.GenericDraweeHierarchy_roundBottomRight) {
                                    roundBottomRight = gdhAttrs.getBoolean(attr, roundBottomRight);
                                    roundedCornerRadius2 = roundedCornerRadius2;
                                } else if (attr == R.styleable.GenericDraweeHierarchy_roundTopStart) {
                                    roundTopStart = gdhAttrs.getBoolean(attr, roundTopStart);
                                    roundedCornerRadius2 = roundedCornerRadius2;
                                } else if (attr == R.styleable.GenericDraweeHierarchy_roundTopEnd) {
                                    roundTopEnd = gdhAttrs.getBoolean(attr, roundTopEnd);
                                    roundedCornerRadius2 = roundedCornerRadius2;
                                } else if (attr == R.styleable.GenericDraweeHierarchy_roundBottomStart) {
                                    roundBottomStart = gdhAttrs.getBoolean(attr, roundBottomStart);
                                    roundedCornerRadius2 = roundedCornerRadius2;
                                } else if (attr == R.styleable.GenericDraweeHierarchy_roundBottomEnd) {
                                    roundBottomEnd = gdhAttrs.getBoolean(attr, roundBottomEnd);
                                    roundedCornerRadius2 = roundedCornerRadius2;
                                } else if (attr == R.styleable.GenericDraweeHierarchy_roundWithOverlayColor) {
                                    roundedCornerRadius2 = roundedCornerRadius2;
                                    getRoundingParams(builder).setOverlayColor(gdhAttrs.getColor(attr, 0));
                                } else {
                                    roundedCornerRadius2 = roundedCornerRadius2;
                                    if (attr == R.styleable.GenericDraweeHierarchy_roundingBorderWidth) {
                                        getRoundingParams(builder).setBorderWidth((float) gdhAttrs.getDimensionPixelSize(attr, 0));
                                    } else if (attr == R.styleable.GenericDraweeHierarchy_roundingBorderColor) {
                                        getRoundingParams(builder).setBorderColor(gdhAttrs.getColor(attr, 0));
                                    } else if (attr == R.styleable.GenericDraweeHierarchy_roundingBorderPadding) {
                                        getRoundingParams(builder).setPadding((float) gdhAttrs.getDimensionPixelSize(attr, 0));
                                    }
                                }
                            } catch (Throwable th5) {
                                th = th5;
                                z = false;
                                gdhAttrs.recycle();
                                if (Build.VERSION.SDK_INT < 17) {
                                }
                                if (roundTopLeft) {
                                }
                                if (roundTopRight) {
                                }
                                if (roundBottomRight) {
                                }
                                if (roundBottomLeft2) {
                                }
                                throw th;
                            }
                        }
                        i++;
                        context2 = context;
                    } catch (Throwable th6) {
                        th = th6;
                        z = false;
                    }
                }
                boolean z2 = false;
                gdhAttrs.recycle();
                if (Build.VERSION.SDK_INT < 17 || context.getResources().getConfiguration().getLayoutDirection() != 1) {
                    roundTopLeft = roundTopLeft && roundTopStart;
                    roundTopRight = roundTopRight && roundTopEnd;
                    roundBottomRight = roundBottomRight && roundBottomEnd;
                    if (roundBottomLeft2 && roundBottomStart) {
                        z2 = true;
                    }
                    roundBottomLeft2 = z2;
                    roundedCornerRadius = roundedCornerRadius2;
                } else {
                    roundTopLeft = roundTopLeft && roundTopEnd;
                    roundTopRight = roundTopRight && roundTopStart;
                    roundBottomRight = roundBottomRight && roundBottomStart;
                    if (roundBottomLeft2 && roundBottomEnd) {
                        z2 = true;
                    }
                    roundBottomLeft2 = z2;
                    roundedCornerRadius = roundedCornerRadius2;
                }
            } catch (Throwable th7) {
                th = th7;
                z = false;
            }
        } else {
            roundedCornerRadius = 0;
            progressBarAutoRotateInterval = 0;
        }
        if (builder.getProgressBarImage() != null && progressBarAutoRotateInterval > 0) {
            builder.setProgressBarImage(new AutoRotateDrawable(builder.getProgressBarImage(), progressBarAutoRotateInterval));
        }
        if (roundedCornerRadius > 0) {
            getRoundingParams(builder).setCornersRadii(roundTopLeft ? (float) roundedCornerRadius : 0.0f, roundTopRight ? (float) roundedCornerRadius : 0.0f, roundBottomRight ? (float) roundedCornerRadius : 0.0f, roundBottomLeft2 ? (float) roundedCornerRadius : 0.0f);
        }
        return builder;
    }

    private static RoundingParams getRoundingParams(GenericDraweeHierarchyBuilder builder) {
        if (builder.getRoundingParams() == null) {
            builder.setRoundingParams(new RoundingParams());
        }
        return builder.getRoundingParams();
    }

    @Nullable
    private static Drawable getDrawable(Context context, TypedArray gdhAttrs, int attrId) {
        int resourceId = gdhAttrs.getResourceId(attrId, 0);
        if (resourceId == 0) {
            return null;
        }
        return context.getResources().getDrawable(resourceId);
    }

    @Nullable
    private static ScalingUtils.ScaleType getScaleTypeFromXml(TypedArray gdhAttrs, int attrId) {
        switch (gdhAttrs.getInt(attrId, -2)) {
            case -1:
                return null;
            case 0:
                return ScalingUtils.ScaleType.FIT_XY;
            case 1:
                return ScalingUtils.ScaleType.FIT_START;
            case 2:
                return ScalingUtils.ScaleType.FIT_CENTER;
            case 3:
                return ScalingUtils.ScaleType.FIT_END;
            case 4:
                return ScalingUtils.ScaleType.CENTER;
            case 5:
                return ScalingUtils.ScaleType.CENTER_INSIDE;
            case 6:
                return ScalingUtils.ScaleType.CENTER_CROP;
            case 7:
                return ScalingUtils.ScaleType.FOCUS_CROP;
            case 8:
                return ScalingUtils.ScaleType.FIT_BOTTOM_START;
            default:
                throw new RuntimeException("XML attribute not specified!");
        }
    }
}
