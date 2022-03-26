package androidx.camera.core.impl;

import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageReaderProxyProvider;
import androidx.camera.core.impl.Config;
import androidx.camera.core.internal.IoConfig;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class ImageCaptureConfig implements UseCaseConfig<ImageCapture>, ImageOutputConfig, IoConfig {
    private final OptionsBundle mConfig;
    public static final Config.Option<Integer> OPTION_IMAGE_CAPTURE_MODE = Config.Option.create("camerax.core.imageCapture.captureMode", Integer.TYPE);
    public static final Config.Option<Integer> OPTION_FLASH_MODE = Config.Option.create("camerax.core.imageCapture.flashMode", Integer.TYPE);
    public static final Config.Option<CaptureBundle> OPTION_CAPTURE_BUNDLE = Config.Option.create("camerax.core.imageCapture.captureBundle", CaptureBundle.class);
    public static final Config.Option<CaptureProcessor> OPTION_CAPTURE_PROCESSOR = Config.Option.create("camerax.core.imageCapture.captureProcessor", CaptureProcessor.class);
    public static final Config.Option<Integer> OPTION_BUFFER_FORMAT = Config.Option.create("camerax.core.imageCapture.bufferFormat", Integer.class);
    public static final Config.Option<Integer> OPTION_MAX_CAPTURE_STAGES = Config.Option.create("camerax.core.imageCapture.maxCaptureStages", Integer.class);
    public static final Config.Option<ImageReaderProxyProvider> OPTION_IMAGE_READER_PROXY_PROVIDER = Config.Option.create("camerax.core.imageAnalysis.imageReaderProxyProvider", ImageReaderProxyProvider.class);

    public ImageCaptureConfig(OptionsBundle config) {
        this.mConfig = config;
    }

    @Override // androidx.camera.core.impl.ReadableConfig
    public Config getConfig() {
        return this.mConfig;
    }

    public boolean hasCaptureMode() {
        return containsOption(OPTION_IMAGE_CAPTURE_MODE);
    }

    public int getCaptureMode() {
        return ((Integer) retrieveOption(OPTION_IMAGE_CAPTURE_MODE)).intValue();
    }

    public int getFlashMode() {
        return ((Integer) retrieveOption(OPTION_FLASH_MODE)).intValue();
    }

    public CaptureBundle getCaptureBundle(CaptureBundle valueIfMissing) {
        return (CaptureBundle) retrieveOption(OPTION_CAPTURE_BUNDLE, valueIfMissing);
    }

    public CaptureBundle getCaptureBundle() {
        return (CaptureBundle) retrieveOption(OPTION_CAPTURE_BUNDLE);
    }

    public CaptureProcessor getCaptureProcessor(CaptureProcessor valueIfMissing) {
        return (CaptureProcessor) retrieveOption(OPTION_CAPTURE_PROCESSOR, valueIfMissing);
    }

    public CaptureProcessor getCaptureProcessor() {
        return (CaptureProcessor) retrieveOption(OPTION_CAPTURE_PROCESSOR);
    }

    public Integer getBufferFormat(Integer valueIfMissing) {
        return (Integer) retrieveOption(OPTION_BUFFER_FORMAT, valueIfMissing);
    }

    public Integer getBufferFormat() {
        return (Integer) retrieveOption(OPTION_BUFFER_FORMAT);
    }

    @Override // androidx.camera.core.impl.ImageInputConfig
    public int getInputFormat() {
        return ((Integer) retrieveOption(OPTION_INPUT_FORMAT)).intValue();
    }

    public int getMaxCaptureStages(int valueIfMissing) {
        return ((Integer) retrieveOption(OPTION_MAX_CAPTURE_STAGES, Integer.valueOf(valueIfMissing))).intValue();
    }

    public int getMaxCaptureStages() {
        return ((Integer) retrieveOption(OPTION_MAX_CAPTURE_STAGES)).intValue();
    }

    public ImageReaderProxyProvider getImageReaderProxyProvider() {
        return (ImageReaderProxyProvider) retrieveOption(OPTION_IMAGE_READER_PROXY_PROVIDER, null);
    }

    @Override // androidx.camera.core.internal.IoConfig
    public Executor getIoExecutor(Executor valueIfMissing) {
        return (Executor) retrieveOption(OPTION_IO_EXECUTOR, valueIfMissing);
    }

    @Override // androidx.camera.core.internal.IoConfig
    public Executor getIoExecutor() {
        return (Executor) retrieveOption(OPTION_IO_EXECUTOR);
    }
}
