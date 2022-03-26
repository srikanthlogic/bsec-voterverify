package androidx.camera.camera2;

import android.content.Context;
import androidx.camera.camera2.internal.Camera2DeviceSurfaceManager;
import androidx.camera.camera2.internal.ImageAnalysisConfigProvider;
import androidx.camera.camera2.internal.ImageCaptureConfigProvider;
import androidx.camera.camera2.internal.PreviewConfigProvider;
import androidx.camera.camera2.internal.VideoCaptureConfigProvider;
import androidx.camera.core.CameraUnavailableException;
import androidx.camera.core.CameraXConfig;
import androidx.camera.core.InitializationException;
import androidx.camera.core.impl.CameraDeviceSurfaceManager;
import androidx.camera.core.impl.CameraFactory;
import androidx.camera.core.impl.ExtendableUseCaseConfigFactory;
import androidx.camera.core.impl.ImageAnalysisConfig;
import androidx.camera.core.impl.ImageCaptureConfig;
import androidx.camera.core.impl.PreviewConfig;
import androidx.camera.core.impl.UseCaseConfigFactory;
import androidx.camera.core.impl.VideoCaptureConfig;
/* loaded from: classes.dex */
public final class Camera2Config {
    private Camera2Config() {
    }

    public static CameraXConfig defaultConfig() {
        CameraFactory.Provider cameraFactoryProvider = $$Lambda$LGbwJFn1EYEiFTe4QSUMXizKEuU.INSTANCE;
        CameraDeviceSurfaceManager.Provider surfaceManagerProvider = $$Lambda$Camera2Config$mYXXnxW6sa_oF7xhp51ozRSO_ck.INSTANCE;
        return new CameraXConfig.Builder().setCameraFactoryProvider(cameraFactoryProvider).setDeviceSurfaceManagerProvider(surfaceManagerProvider).setUseCaseConfigFactoryProvider($$Lambda$Camera2Config$pKz61QSIMP944Kt2USWec22ELsc.INSTANCE).build();
    }

    public static /* synthetic */ CameraDeviceSurfaceManager lambda$defaultConfig$0(Context context) throws InitializationException {
        try {
            return new Camera2DeviceSurfaceManager(context);
        } catch (CameraUnavailableException e) {
            throw new InitializationException(e);
        }
    }

    public static /* synthetic */ UseCaseConfigFactory lambda$defaultConfig$1(Context context) throws InitializationException {
        ExtendableUseCaseConfigFactory factory = new ExtendableUseCaseConfigFactory();
        factory.installDefaultProvider(ImageAnalysisConfig.class, new ImageAnalysisConfigProvider(context));
        factory.installDefaultProvider(ImageCaptureConfig.class, new ImageCaptureConfigProvider(context));
        factory.installDefaultProvider(VideoCaptureConfig.class, new VideoCaptureConfigProvider(context));
        factory.installDefaultProvider(PreviewConfig.class, new PreviewConfigProvider(context));
        return factory;
    }

    /* loaded from: classes.dex */
    public static final class DefaultProvider implements CameraXConfig.Provider {
        @Override // androidx.camera.core.CameraXConfig.Provider
        public CameraXConfig getCameraXConfig() {
            return Camera2Config.defaultConfig();
        }
    }
}
