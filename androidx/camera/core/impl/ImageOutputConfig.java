package androidx.camera.core.impl;

import android.util.Pair;
import android.util.Rational;
import android.util.Size;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.impl.Config;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
/* loaded from: classes.dex */
public interface ImageOutputConfig extends ReadableConfig {
    public static final int INVALID_ROTATION;
    public static final Rational DEFAULT_ASPECT_RATIO_LANDSCAPE = new Rational(4, 3);
    public static final Rational DEFAULT_ASPECT_RATIO_PORTRAIT = new Rational(3, 4);
    public static final Config.Option<Rational> OPTION_TARGET_ASPECT_RATIO_CUSTOM = Config.Option.create("camerax.core.imageOutput.targetAspectRatioCustom", Rational.class);
    public static final Config.Option<Integer> OPTION_TARGET_ASPECT_RATIO = Config.Option.create("camerax.core.imageOutput.targetAspectRatio", AspectRatio.class);
    public static final Config.Option<Integer> OPTION_TARGET_ROTATION = Config.Option.create("camerax.core.imageOutput.targetRotation", Integer.TYPE);
    public static final Config.Option<Size> OPTION_TARGET_RESOLUTION = Config.Option.create("camerax.core.imageOutput.targetResolution", Size.class);
    public static final Config.Option<Size> OPTION_DEFAULT_RESOLUTION = Config.Option.create("camerax.core.imageOutput.defaultResolution", Size.class);
    public static final Config.Option<Size> OPTION_MAX_RESOLUTION = Config.Option.create("camerax.core.imageOutput.maxResolution", Size.class);
    public static final Config.Option<List<Pair<Integer, Size[]>>> OPTION_SUPPORTED_RESOLUTIONS = Config.Option.create("camerax.core.imageOutput.supportedResolutions", List.class);

    /* loaded from: classes.dex */
    public interface Builder<B> {
        B setDefaultResolution(Size size);

        B setMaxResolution(Size size);

        B setSupportedResolutions(List<Pair<Integer, Size[]>> list);

        B setTargetAspectRatio(int i);

        B setTargetAspectRatioCustom(Rational rational);

        B setTargetResolution(Size size);

        B setTargetRotation(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface RotationValue {
    }

    default Rational getTargetAspectRatioCustom(Rational valueIfMissing) {
        return (Rational) retrieveOption(OPTION_TARGET_ASPECT_RATIO_CUSTOM, valueIfMissing);
    }

    default Rational getTargetAspectRatioCustom() {
        return (Rational) retrieveOption(OPTION_TARGET_ASPECT_RATIO_CUSTOM);
    }

    default boolean hasTargetAspectRatio() {
        return containsOption(OPTION_TARGET_ASPECT_RATIO);
    }

    default int getTargetAspectRatio() {
        return ((Integer) retrieveOption(OPTION_TARGET_ASPECT_RATIO)).intValue();
    }

    default int getTargetRotation(int valueIfMissing) {
        return ((Integer) retrieveOption(OPTION_TARGET_ROTATION, Integer.valueOf(valueIfMissing))).intValue();
    }

    default int getTargetRotation() {
        return ((Integer) retrieveOption(OPTION_TARGET_ROTATION)).intValue();
    }

    default Size getTargetResolution(Size valueIfMissing) {
        return (Size) retrieveOption(OPTION_TARGET_RESOLUTION, valueIfMissing);
    }

    default Size getTargetResolution() {
        return (Size) retrieveOption(OPTION_TARGET_RESOLUTION);
    }

    default Size getDefaultResolution(Size valueIfMissing) {
        return (Size) retrieveOption(OPTION_DEFAULT_RESOLUTION, valueIfMissing);
    }

    default Size getDefaultResolution() {
        return (Size) retrieveOption(OPTION_DEFAULT_RESOLUTION);
    }

    default Size getMaxResolution(Size valueIfMissing) {
        return (Size) retrieveOption(OPTION_MAX_RESOLUTION, valueIfMissing);
    }

    default Size getMaxResolution() {
        return (Size) retrieveOption(OPTION_MAX_RESOLUTION);
    }

    default List<Pair<Integer, Size[]>> getSupportedResolutions(List<Pair<Integer, Size[]>> valueIfMissing) {
        return (List) retrieveOption(OPTION_SUPPORTED_RESOLUTIONS, valueIfMissing);
    }

    default List<Pair<Integer, Size[]>> getSupportedResolutions() {
        return (List) retrieveOption(OPTION_SUPPORTED_RESOLUTIONS);
    }
}
