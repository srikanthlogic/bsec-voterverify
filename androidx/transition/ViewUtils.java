package androidx.transition;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.util.Property;
import android.view.View;
import androidx.core.view.ViewCompat;
import java.lang.reflect.Field;
/* loaded from: classes.dex */
public class ViewUtils {
    static final Property<View, Rect> CLIP_BOUNDS;
    private static final ViewUtilsBase IMPL;
    private static final String TAG;
    static final Property<View, Float> TRANSITION_ALPHA;
    private static final int VISIBILITY_MASK;
    private static Field sViewFlagsField;
    private static boolean sViewFlagsFieldFetched;

    static {
        if (Build.VERSION.SDK_INT >= 22) {
            IMPL = new ViewUtilsApi22();
        } else if (Build.VERSION.SDK_INT >= 21) {
            IMPL = new ViewUtilsApi21();
        } else if (Build.VERSION.SDK_INT >= 19) {
            IMPL = new ViewUtilsApi19();
        } else {
            IMPL = new ViewUtilsBase();
        }
        TRANSITION_ALPHA = new Property<View, Float>(Float.class, "translationAlpha") { // from class: androidx.transition.ViewUtils.1
            public Float get(View view) {
                return Float.valueOf(ViewUtils.getTransitionAlpha(view));
            }

            public void set(View view, Float alpha) {
                ViewUtils.setTransitionAlpha(view, alpha.floatValue());
            }
        };
        CLIP_BOUNDS = new Property<View, Rect>(Rect.class, "clipBounds") { // from class: androidx.transition.ViewUtils.2
            public Rect get(View view) {
                return ViewCompat.getClipBounds(view);
            }

            public void set(View view, Rect clipBounds) {
                ViewCompat.setClipBounds(view, clipBounds);
            }
        };
    }

    public static ViewOverlayImpl getOverlay(View view) {
        if (Build.VERSION.SDK_INT >= 18) {
            return new ViewOverlayApi18(view);
        }
        return ViewOverlayApi14.createFrom(view);
    }

    public static WindowIdImpl getWindowId(View view) {
        if (Build.VERSION.SDK_INT >= 18) {
            return new WindowIdApi18(view);
        }
        return new WindowIdApi14(view.getWindowToken());
    }

    public static void setTransitionAlpha(View view, float alpha) {
        IMPL.setTransitionAlpha(view, alpha);
    }

    public static float getTransitionAlpha(View view) {
        return IMPL.getTransitionAlpha(view);
    }

    public static void saveNonTransitionAlpha(View view) {
        IMPL.saveNonTransitionAlpha(view);
    }

    public static void clearNonTransitionAlpha(View view) {
        IMPL.clearNonTransitionAlpha(view);
    }

    public static void setTransitionVisibility(View view, int visibility) {
        fetchViewFlagsField();
        Field field = sViewFlagsField;
        if (field != null) {
            try {
                sViewFlagsField.setInt(view, (field.getInt(view) & -13) | visibility);
            } catch (IllegalAccessException e) {
            }
        }
    }

    public static void transformMatrixToGlobal(View view, Matrix matrix) {
        IMPL.transformMatrixToGlobal(view, matrix);
    }

    public static void transformMatrixToLocal(View view, Matrix matrix) {
        IMPL.transformMatrixToLocal(view, matrix);
    }

    public static void setAnimationMatrix(View v, Matrix m) {
        IMPL.setAnimationMatrix(v, m);
    }

    public static void setLeftTopRightBottom(View v, int left, int top, int right, int bottom) {
        IMPL.setLeftTopRightBottom(v, left, top, right, bottom);
    }

    private static void fetchViewFlagsField() {
        if (!sViewFlagsFieldFetched) {
            try {
                sViewFlagsField = View.class.getDeclaredField("mViewFlags");
                sViewFlagsField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                Log.i(TAG, "fetchViewFlagsField: ");
            }
            sViewFlagsFieldFetched = true;
        }
    }

    private ViewUtils() {
    }
}
