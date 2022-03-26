package androidx.camera.camera2.internal;

import android.os.Build;
import android.util.Range;
import android.util.Size;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SupportedSizeConstraints {
    private static final String ALL_MODELS;
    private static final Range<Integer> ALL_API_LEVELS = new Range<>(0, Integer.MAX_VALUE);
    private static final Map<CameraDeviceId, List<ExcludedSizeConstraint>> EXCLUDED_SIZES_MAP = new TreeMap(new Comparator<CameraDeviceId>() { // from class: androidx.camera.camera2.internal.SupportedSizeConstraints.1
        public int compare(CameraDeviceId lhs, CameraDeviceId rhs) {
            int cmp;
            if (lhs.equals(rhs)) {
                return 0;
            }
            int cmp2 = lhs.getBrand().compareTo(rhs.getBrand());
            if (cmp2 != 0) {
                return cmp2;
            }
            int cmp3 = lhs.getDevice().compareTo(rhs.getDevice());
            if (cmp3 != 0) {
                return cmp3;
            }
            if (!SupportedSizeConstraints.ALL_MODELS.equals(lhs.getModel()) && !SupportedSizeConstraints.ALL_MODELS.equals(rhs.getModel()) && (cmp = lhs.getModel().compareTo(rhs.getModel())) != 0) {
                return cmp;
            }
            int cmp4 = lhs.getCameraId().compareTo(rhs.getCameraId());
            if (cmp4 != 0) {
                return cmp4;
            }
            return 0;
        }
    });

    /* loaded from: classes.dex */
    interface Constraint {
        Range<Integer> getAffectedApiLevels();

        Set<Integer> getAffectedFormats();
    }

    static {
        EXCLUDED_SIZES_MAP.put(CameraDeviceId.create("OnePlus", "OnePlus6T", ALL_MODELS, "0"), Collections.singletonList(ExcludedSizeConstraint.create(Collections.singleton(256), ALL_API_LEVELS, Arrays.asList(new Size(4160, 3120), new Size(4000, PathInterpolatorCompat.MAX_NUM_POINTS)))));
        EXCLUDED_SIZES_MAP.put(CameraDeviceId.create("OnePlus", "OnePlus6", ALL_MODELS, "0"), Collections.singletonList(ExcludedSizeConstraint.create(Collections.singleton(256), ALL_API_LEVELS, Arrays.asList(new Size(4160, 3120), new Size(4000, PathInterpolatorCompat.MAX_NUM_POINTS)))));
    }

    public static List<Size> getExcludedSizes(String cameraId, int imageFormat) {
        CameraDeviceId cameraDeviceId = CameraDeviceId.create(Build.BRAND, Build.DEVICE, Build.MODEL, cameraId);
        if (!EXCLUDED_SIZES_MAP.containsKey(cameraDeviceId)) {
            return Collections.emptyList();
        }
        List<Size> excludedSizes = new ArrayList<>();
        for (ExcludedSizeConstraint constraint : EXCLUDED_SIZES_MAP.get(cameraDeviceId)) {
            if (constraint.getAffectedFormats().contains(Integer.valueOf(imageFormat)) && constraint.getAffectedApiLevels().contains((Range<Integer>) Integer.valueOf(Build.VERSION.SDK_INT))) {
                excludedSizes.addAll(constraint.getExcludedSizes());
            }
        }
        return excludedSizes;
    }

    private SupportedSizeConstraints() {
    }

    /* loaded from: classes.dex */
    public static abstract class ExcludedSizeConstraint implements Constraint {
        @Override // androidx.camera.camera2.internal.SupportedSizeConstraints.Constraint
        public abstract Range<Integer> getAffectedApiLevels();

        @Override // androidx.camera.camera2.internal.SupportedSizeConstraints.Constraint
        public abstract Set<Integer> getAffectedFormats();

        public abstract List<Size> getExcludedSizes();

        public static ExcludedSizeConstraint create(Set<Integer> affectedFormats, Range<Integer> affectedApiLevels, List<Size> exclusedSizes) {
            return new AutoValue_SupportedSizeConstraints_ExcludedSizeConstraint(affectedFormats, affectedApiLevels, exclusedSizes);
        }
    }
}
