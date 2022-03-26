package androidx.camera.core;

import android.graphics.Rect;
import android.util.Size;
import androidx.camera.core.impl.CameraControlInternal;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.ImageOutputConfig;
import androidx.camera.core.impl.MutableConfig;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.UseCaseConfig;
import androidx.core.util.Preconditions;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public abstract class UseCase {
    private Size mAttachedResolution;
    private CameraInternal mCamera;
    private UseCaseConfig<?> mUseCaseConfig;
    private Rect mViewPortCropRect;
    private final Set<StateChangeCallback> mStateChangeCallbacks = new HashSet();
    private SessionConfig mAttachedSessionConfig = SessionConfig.defaultEmptySessionConfig();
    private State mState = State.INACTIVE;
    private final Object mCameraLock = new Object();

    /* loaded from: classes.dex */
    public interface EventCallback {
        void onBind(String str);

        void onUnbind();
    }

    /* loaded from: classes.dex */
    public enum State {
        ACTIVE,
        INACTIVE
    }

    /* loaded from: classes.dex */
    public interface StateChangeCallback {
        void onUseCaseActive(UseCase useCase);

        void onUseCaseInactive(UseCase useCase);

        void onUseCaseReset(UseCase useCase);

        void onUseCaseUpdated(UseCase useCase);
    }

    protected abstract Size onSuggestedResolutionUpdated(Size size);

    public UseCase(UseCaseConfig<?> useCaseConfig) {
        updateUseCaseConfig(useCaseConfig);
    }

    public UseCaseConfig.Builder<?, ?, ?> getDefaultBuilder(CameraInfo cameraInfo) {
        return null;
    }

    protected final void updateUseCaseConfig(UseCaseConfig<?> useCaseConfig) {
        this.mUseCaseConfig = applyDefaults(useCaseConfig, getDefaultBuilder(getCamera() == null ? null : getCamera().getCameraInfoInternal()));
    }

    public UseCaseConfig<?> applyDefaults(UseCaseConfig<?> userConfig, UseCaseConfig.Builder<?, ?, ?> defaultConfigBuilder) {
        if (defaultConfigBuilder == null) {
            return userConfig;
        }
        MutableConfig defaultMutableConfig = defaultConfigBuilder.getMutableConfig();
        if (userConfig.containsOption(ImageOutputConfig.OPTION_TARGET_ASPECT_RATIO) && defaultMutableConfig.containsOption(ImageOutputConfig.OPTION_TARGET_ASPECT_RATIO_CUSTOM)) {
            defaultMutableConfig.removeOption(ImageOutputConfig.OPTION_TARGET_ASPECT_RATIO_CUSTOM);
        }
        for (Config.Option<?> opt : userConfig.listOptions()) {
            defaultMutableConfig.insertOption(opt, userConfig.getOptionPriority(opt), userConfig.retrieveOption(opt));
        }
        return defaultConfigBuilder.getUseCaseConfig();
    }

    protected void updateSessionConfig(SessionConfig sessionConfig) {
        this.mAttachedSessionConfig = sessionConfig;
    }

    private void addStateChangeCallback(StateChangeCallback callback) {
        this.mStateChangeCallbacks.add(callback);
    }

    private void removeStateChangeCallback(StateChangeCallback callback) {
        this.mStateChangeCallbacks.remove(callback);
    }

    public SessionConfig getSessionConfig() {
        return this.mAttachedSessionConfig;
    }

    protected final void notifyActive() {
        this.mState = State.ACTIVE;
        notifyState();
    }

    protected final void notifyInactive() {
        this.mState = State.INACTIVE;
        notifyState();
    }

    protected final void notifyUpdated() {
        for (StateChangeCallback stateChangeCallback : this.mStateChangeCallbacks) {
            stateChangeCallback.onUseCaseUpdated(this);
        }
    }

    protected final void notifyReset() {
        for (StateChangeCallback stateChangeCallback : this.mStateChangeCallbacks) {
            stateChangeCallback.onUseCaseReset(this);
        }
    }

    /* renamed from: androidx.camera.core.UseCase$1 */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$camera$core$UseCase$State = new int[State.values().length];

        static {
            try {
                $SwitchMap$androidx$camera$core$UseCase$State[State.INACTIVE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$camera$core$UseCase$State[State.ACTIVE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public final void notifyState() {
        int i = AnonymousClass1.$SwitchMap$androidx$camera$core$UseCase$State[this.mState.ordinal()];
        if (i == 1) {
            for (StateChangeCallback stateChangeCallback : this.mStateChangeCallbacks) {
                stateChangeCallback.onUseCaseInactive(this);
            }
        } else if (i == 2) {
            for (StateChangeCallback stateChangeCallback2 : this.mStateChangeCallbacks) {
                stateChangeCallback2.onUseCaseActive(this);
            }
        }
    }

    protected String getCameraId() {
        CameraInternal camera = getCamera();
        return ((CameraInternal) Preconditions.checkNotNull(camera, "No camera attached to use case: " + this)).getCameraInfoInternal().getCameraId();
    }

    protected boolean isCurrentCamera(String cameraId) {
        if (getCamera() == null) {
            return false;
        }
        return Objects.equals(cameraId, getCameraId());
    }

    public void clear() {
    }

    public void onDestroy() {
    }

    public String getName() {
        UseCaseConfig<?> useCaseConfig = this.mUseCaseConfig;
        return useCaseConfig.getTargetName("<UnknownUseCase-" + hashCode() + ">");
    }

    public UseCaseConfig<?> getUseCaseConfig() {
        return this.mUseCaseConfig;
    }

    public CameraInternal getCamera() {
        CameraInternal cameraInternal;
        synchronized (this.mCameraLock) {
            cameraInternal = this.mCamera;
        }
        return cameraInternal;
    }

    public Size getAttachedSurfaceResolution() {
        return this.mAttachedResolution;
    }

    public void updateSuggestedResolution(Size suggestedResolution) {
        this.mAttachedResolution = onSuggestedResolutionUpdated(suggestedResolution);
    }

    protected void onCameraControlReady() {
    }

    public void onAttach(CameraInternal camera) {
        synchronized (this.mCameraLock) {
            this.mCamera = camera;
            addStateChangeCallback(camera);
        }
        updateUseCaseConfig(this.mUseCaseConfig);
        EventCallback eventCallback = this.mUseCaseConfig.getUseCaseEventCallback(null);
        if (eventCallback != null) {
            eventCallback.onBind(camera.getCameraInfoInternal().getCameraId());
        }
        onCameraControlReady();
    }

    public void onDetach(CameraInternal camera) {
        clear();
        EventCallback eventCallback = this.mUseCaseConfig.getUseCaseEventCallback(null);
        if (eventCallback != null) {
            eventCallback.onUnbind();
        }
        synchronized (this.mCameraLock) {
            Preconditions.checkArgument(camera == this.mCamera);
            this.mCamera.detachUseCases(Collections.singleton(this));
            removeStateChangeCallback(this.mCamera);
            this.mCamera = null;
        }
    }

    public void onStateAttached() {
    }

    public void onStateDetached() {
    }

    protected CameraControlInternal getCameraControl() {
        synchronized (this.mCameraLock) {
            if (this.mCamera == null) {
                return CameraControlInternal.DEFAULT_EMPTY_INSTANCE;
            }
            return this.mCamera.getCameraControlInternal();
        }
    }

    public void setViewPortCropRect(Rect viewPortCropRect) {
        this.mViewPortCropRect = viewPortCropRect;
    }

    protected Rect getViewPortCropRect() {
        return this.mViewPortCropRect;
    }

    public int getImageFormat() {
        return this.mUseCaseConfig.getInputFormat();
    }
}
