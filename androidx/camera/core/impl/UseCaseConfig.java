package androidx.camera.core.impl;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExtendableBuilder;
import androidx.camera.core.UseCase;
import androidx.camera.core.impl.CaptureConfig;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.internal.TargetConfig;
import androidx.camera.core.internal.UseCaseEventConfig;
/* loaded from: classes.dex */
public interface UseCaseConfig<T extends UseCase> extends TargetConfig<T>, UseCaseEventConfig, ImageInputConfig {
    public static final Config.Option<SessionConfig> OPTION_DEFAULT_SESSION_CONFIG = Config.Option.create("camerax.core.useCase.defaultSessionConfig", SessionConfig.class);
    public static final Config.Option<CaptureConfig> OPTION_DEFAULT_CAPTURE_CONFIG = Config.Option.create("camerax.core.useCase.defaultCaptureConfig", CaptureConfig.class);
    public static final Config.Option<SessionConfig.OptionUnpacker> OPTION_SESSION_CONFIG_UNPACKER = Config.Option.create("camerax.core.useCase.sessionConfigUnpacker", SessionConfig.OptionUnpacker.class);
    public static final Config.Option<CaptureConfig.OptionUnpacker> OPTION_CAPTURE_CONFIG_UNPACKER = Config.Option.create("camerax.core.useCase.captureConfigUnpacker", CaptureConfig.OptionUnpacker.class);
    public static final Config.Option<Integer> OPTION_SURFACE_OCCUPANCY_PRIORITY = Config.Option.create("camerax.core.useCase.surfaceOccupancyPriority", Integer.TYPE);
    public static final Config.Option<CameraSelector> OPTION_CAMERA_SELECTOR = Config.Option.create("camerax.core.useCase.cameraSelector", CameraSelector.class);

    /* loaded from: classes.dex */
    public interface Builder<T extends UseCase, C extends UseCaseConfig<T>, B> extends TargetConfig.Builder<T, B>, ExtendableBuilder<T>, UseCaseEventConfig.Builder<B> {
        C getUseCaseConfig();

        B setCameraSelector(CameraSelector cameraSelector);

        B setCaptureOptionUnpacker(CaptureConfig.OptionUnpacker optionUnpacker);

        B setDefaultCaptureConfig(CaptureConfig captureConfig);

        B setDefaultSessionConfig(SessionConfig sessionConfig);

        B setSessionOptionUnpacker(SessionConfig.OptionUnpacker optionUnpacker);

        B setSurfaceOccupancyPriority(int i);
    }

    default SessionConfig getDefaultSessionConfig(SessionConfig valueIfMissing) {
        return (SessionConfig) retrieveOption(OPTION_DEFAULT_SESSION_CONFIG, valueIfMissing);
    }

    default SessionConfig getDefaultSessionConfig() {
        return (SessionConfig) retrieveOption(OPTION_DEFAULT_SESSION_CONFIG);
    }

    default CaptureConfig getDefaultCaptureConfig(CaptureConfig valueIfMissing) {
        return (CaptureConfig) retrieveOption(OPTION_DEFAULT_CAPTURE_CONFIG, valueIfMissing);
    }

    default CaptureConfig getDefaultCaptureConfig() {
        return (CaptureConfig) retrieveOption(OPTION_DEFAULT_CAPTURE_CONFIG);
    }

    default SessionConfig.OptionUnpacker getSessionOptionUnpacker(SessionConfig.OptionUnpacker valueIfMissing) {
        return (SessionConfig.OptionUnpacker) retrieveOption(OPTION_SESSION_CONFIG_UNPACKER, valueIfMissing);
    }

    default SessionConfig.OptionUnpacker getSessionOptionUnpacker() {
        return (SessionConfig.OptionUnpacker) retrieveOption(OPTION_SESSION_CONFIG_UNPACKER);
    }

    default CaptureConfig.OptionUnpacker getCaptureOptionUnpacker(CaptureConfig.OptionUnpacker valueIfMissing) {
        return (CaptureConfig.OptionUnpacker) retrieveOption(OPTION_CAPTURE_CONFIG_UNPACKER, valueIfMissing);
    }

    default CaptureConfig.OptionUnpacker getCaptureOptionUnpacker() {
        return (CaptureConfig.OptionUnpacker) retrieveOption(OPTION_CAPTURE_CONFIG_UNPACKER);
    }

    default int getSurfaceOccupancyPriority(int valueIfMissing) {
        return ((Integer) retrieveOption(OPTION_SURFACE_OCCUPANCY_PRIORITY, Integer.valueOf(valueIfMissing))).intValue();
    }

    default int getSurfaceOccupancyPriority() {
        return ((Integer) retrieveOption(OPTION_SURFACE_OCCUPANCY_PRIORITY)).intValue();
    }

    default CameraSelector getCameraSelector(CameraSelector valueIfMissing) {
        return (CameraSelector) retrieveOption(OPTION_CAMERA_SELECTOR, valueIfMissing);
    }

    default CameraSelector getCameraSelector() {
        return (CameraSelector) retrieveOption(OPTION_CAMERA_SELECTOR);
    }
}
