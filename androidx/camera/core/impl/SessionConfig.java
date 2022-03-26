package androidx.camera.core.impl;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.util.Log;
import androidx.camera.core.impl.CaptureConfig;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public final class SessionConfig {
    private final List<CameraDevice.StateCallback> mDeviceStateCallbacks;
    private final List<ErrorListener> mErrorListeners;
    private final CaptureConfig mRepeatingCaptureConfig;
    private final List<CameraCaptureSession.StateCallback> mSessionStateCallbacks;
    private final List<CameraCaptureCallback> mSingleCameraCaptureCallbacks;
    private final List<DeferrableSurface> mSurfaces;

    /* loaded from: classes.dex */
    public interface ErrorListener {
        void onError(SessionConfig sessionConfig, SessionError sessionError);
    }

    /* loaded from: classes.dex */
    public interface OptionUnpacker {
        void unpack(UseCaseConfig<?> useCaseConfig, Builder builder);
    }

    /* loaded from: classes.dex */
    public enum SessionError {
        SESSION_ERROR_SURFACE_NEEDS_RESET,
        SESSION_ERROR_UNKNOWN
    }

    SessionConfig(List<DeferrableSurface> surfaces, List<CameraDevice.StateCallback> deviceStateCallbacks, List<CameraCaptureSession.StateCallback> sessionStateCallbacks, List<CameraCaptureCallback> singleCameraCaptureCallbacks, List<ErrorListener> errorListeners, CaptureConfig repeatingCaptureConfig) {
        this.mSurfaces = surfaces;
        this.mDeviceStateCallbacks = Collections.unmodifiableList(deviceStateCallbacks);
        this.mSessionStateCallbacks = Collections.unmodifiableList(sessionStateCallbacks);
        this.mSingleCameraCaptureCallbacks = Collections.unmodifiableList(singleCameraCaptureCallbacks);
        this.mErrorListeners = Collections.unmodifiableList(errorListeners);
        this.mRepeatingCaptureConfig = repeatingCaptureConfig;
    }

    public static SessionConfig defaultEmptySessionConfig() {
        return new SessionConfig(new ArrayList(), new ArrayList(0), new ArrayList(0), new ArrayList(0), new ArrayList(0), new CaptureConfig.Builder().build());
    }

    public List<DeferrableSurface> getSurfaces() {
        return Collections.unmodifiableList(this.mSurfaces);
    }

    public Config getImplementationOptions() {
        return this.mRepeatingCaptureConfig.getImplementationOptions();
    }

    public int getTemplateType() {
        return this.mRepeatingCaptureConfig.getTemplateType();
    }

    public List<CameraDevice.StateCallback> getDeviceStateCallbacks() {
        return this.mDeviceStateCallbacks;
    }

    public List<CameraCaptureSession.StateCallback> getSessionStateCallbacks() {
        return this.mSessionStateCallbacks;
    }

    public List<CameraCaptureCallback> getRepeatingCameraCaptureCallbacks() {
        return this.mRepeatingCaptureConfig.getCameraCaptureCallbacks();
    }

    public List<ErrorListener> getErrorListeners() {
        return this.mErrorListeners;
    }

    public List<CameraCaptureCallback> getSingleCameraCaptureCallbacks() {
        return this.mSingleCameraCaptureCallbacks;
    }

    public CaptureConfig getRepeatingCaptureConfig() {
        return this.mRepeatingCaptureConfig;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class BaseBuilder {
        final Set<DeferrableSurface> mSurfaces = new HashSet();
        final CaptureConfig.Builder mCaptureConfigBuilder = new CaptureConfig.Builder();
        final List<CameraDevice.StateCallback> mDeviceStateCallbacks = new ArrayList();
        final List<CameraCaptureSession.StateCallback> mSessionStateCallbacks = new ArrayList();
        final List<ErrorListener> mErrorListeners = new ArrayList();
        final List<CameraCaptureCallback> mSingleCameraCaptureCallbacks = new ArrayList();

        BaseBuilder() {
        }
    }

    /* loaded from: classes.dex */
    public static class Builder extends BaseBuilder {
        public static Builder createFrom(UseCaseConfig<?> config) {
            OptionUnpacker unpacker = config.getSessionOptionUnpacker(null);
            if (unpacker != null) {
                Builder builder = new Builder();
                unpacker.unpack(config, builder);
                return builder;
            }
            throw new IllegalStateException("Implementation is missing option unpacker for " + config.getTargetName(config.toString()));
        }

        public void setTemplateType(int templateType) {
            this.mCaptureConfigBuilder.setTemplateType(templateType);
        }

        public void setTag(Object tag) {
            this.mCaptureConfigBuilder.setTag(tag);
        }

        public void addDeviceStateCallback(CameraDevice.StateCallback deviceStateCallback) {
            if (!this.mDeviceStateCallbacks.contains(deviceStateCallback)) {
                this.mDeviceStateCallbacks.add(deviceStateCallback);
                return;
            }
            throw new IllegalArgumentException("Duplicate device state callback.");
        }

        public void addAllDeviceStateCallbacks(Collection<CameraDevice.StateCallback> deviceStateCallbacks) {
            for (CameraDevice.StateCallback callback : deviceStateCallbacks) {
                addDeviceStateCallback(callback);
            }
        }

        public void addSessionStateCallback(CameraCaptureSession.StateCallback sessionStateCallback) {
            if (!this.mSessionStateCallbacks.contains(sessionStateCallback)) {
                this.mSessionStateCallbacks.add(sessionStateCallback);
                return;
            }
            throw new IllegalArgumentException("Duplicate session state callback.");
        }

        public void addAllSessionStateCallbacks(List<CameraCaptureSession.StateCallback> sessionStateCallbacks) {
            for (CameraCaptureSession.StateCallback callback : sessionStateCallbacks) {
                addSessionStateCallback(callback);
            }
        }

        public void addRepeatingCameraCaptureCallback(CameraCaptureCallback cameraCaptureCallback) {
            this.mCaptureConfigBuilder.addCameraCaptureCallback(cameraCaptureCallback);
        }

        public void addAllRepeatingCameraCaptureCallbacks(Collection<CameraCaptureCallback> cameraCaptureCallbacks) {
            this.mCaptureConfigBuilder.addAllCameraCaptureCallbacks(cameraCaptureCallbacks);
        }

        public void addCameraCaptureCallback(CameraCaptureCallback cameraCaptureCallback) {
            this.mCaptureConfigBuilder.addCameraCaptureCallback(cameraCaptureCallback);
            this.mSingleCameraCaptureCallbacks.add(cameraCaptureCallback);
        }

        public void addAllCameraCaptureCallbacks(Collection<CameraCaptureCallback> cameraCaptureCallbacks) {
            this.mCaptureConfigBuilder.addAllCameraCaptureCallbacks(cameraCaptureCallbacks);
            this.mSingleCameraCaptureCallbacks.addAll(cameraCaptureCallbacks);
        }

        public List<CameraCaptureCallback> getSingleCameraCaptureCallbacks() {
            return Collections.unmodifiableList(this.mSingleCameraCaptureCallbacks);
        }

        public void addErrorListener(ErrorListener errorListener) {
            this.mErrorListeners.add(errorListener);
        }

        public void addSurface(DeferrableSurface surface) {
            this.mSurfaces.add(surface);
            this.mCaptureConfigBuilder.addSurface(surface);
        }

        public void addNonRepeatingSurface(DeferrableSurface surface) {
            this.mSurfaces.add(surface);
        }

        public void removeSurface(DeferrableSurface surface) {
            this.mSurfaces.remove(surface);
            this.mCaptureConfigBuilder.removeSurface(surface);
        }

        public void clearSurfaces() {
            this.mSurfaces.clear();
            this.mCaptureConfigBuilder.clearSurfaces();
        }

        public void setImplementationOptions(Config config) {
            this.mCaptureConfigBuilder.setImplementationOptions(config);
        }

        public void addImplementationOptions(Config config) {
            this.mCaptureConfigBuilder.addImplementationOptions(config);
        }

        public SessionConfig build() {
            return new SessionConfig(new ArrayList(this.mSurfaces), this.mDeviceStateCallbacks, this.mSessionStateCallbacks, this.mSingleCameraCaptureCallbacks, this.mErrorListeners, this.mCaptureConfigBuilder.build());
        }
    }

    /* loaded from: classes.dex */
    public static final class ValidatingBuilder extends BaseBuilder {
        private static final String TAG;
        private boolean mValid = true;
        private boolean mTemplateSet = false;

        public void add(SessionConfig sessionConfig) {
            CaptureConfig captureConfig = sessionConfig.getRepeatingCaptureConfig();
            if (captureConfig.getTemplateType() != -1) {
                if (!this.mTemplateSet) {
                    this.mCaptureConfigBuilder.setTemplateType(captureConfig.getTemplateType());
                    this.mTemplateSet = true;
                } else if (this.mCaptureConfigBuilder.getTemplateType() != captureConfig.getTemplateType()) {
                    Log.d(TAG, "Invalid configuration due to template type: " + this.mCaptureConfigBuilder.getTemplateType() + " != " + captureConfig.getTemplateType());
                    this.mValid = false;
                }
            }
            Object tag = sessionConfig.getRepeatingCaptureConfig().getTag();
            if (tag != null) {
                this.mCaptureConfigBuilder.setTag(tag);
            }
            this.mDeviceStateCallbacks.addAll(sessionConfig.getDeviceStateCallbacks());
            this.mSessionStateCallbacks.addAll(sessionConfig.getSessionStateCallbacks());
            this.mCaptureConfigBuilder.addAllCameraCaptureCallbacks(sessionConfig.getRepeatingCameraCaptureCallbacks());
            this.mSingleCameraCaptureCallbacks.addAll(sessionConfig.getSingleCameraCaptureCallbacks());
            this.mErrorListeners.addAll(sessionConfig.getErrorListeners());
            this.mSurfaces.addAll(sessionConfig.getSurfaces());
            this.mCaptureConfigBuilder.getSurfaces().addAll(captureConfig.getSurfaces());
            if (!this.mSurfaces.containsAll(this.mCaptureConfigBuilder.getSurfaces())) {
                Log.d(TAG, "Invalid configuration due to capture request surfaces are not a subset of surfaces");
                this.mValid = false;
            }
            this.mCaptureConfigBuilder.addImplementationOptions(captureConfig.getImplementationOptions());
        }

        public boolean isValid() {
            return this.mTemplateSet && this.mValid;
        }

        public SessionConfig build() {
            if (this.mValid) {
                return new SessionConfig(new ArrayList(this.mSurfaces), this.mDeviceStateCallbacks, this.mSessionStateCallbacks, this.mSingleCameraCaptureCallbacks, this.mErrorListeners, this.mCaptureConfigBuilder.build());
            }
            throw new IllegalArgumentException("Unsupported session configuration combination");
        }
    }
}
