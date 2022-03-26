package androidx.camera.core.impl;

import android.util.Log;
import androidx.camera.core.impl.SessionConfig;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public final class UseCaseAttachState {
    private static final String TAG;
    private final Map<String, UseCaseAttachInfo> mAttachedUseCasesToInfoMap = new HashMap();
    private final String mCameraId;

    /* loaded from: classes.dex */
    public interface AttachStateFilter {
        boolean filter(UseCaseAttachInfo useCaseAttachInfo);
    }

    public UseCaseAttachState(String cameraId) {
        this.mCameraId = cameraId;
    }

    public void setUseCaseActive(String useCaseName, SessionConfig sessionConfig) {
        getOrCreateUseCaseAttachInfo(useCaseName, sessionConfig).setActive(true);
    }

    public void setUseCaseInactive(String useCaseName) {
        if (this.mAttachedUseCasesToInfoMap.containsKey(useCaseName)) {
            UseCaseAttachInfo useCaseAttachInfo = this.mAttachedUseCasesToInfoMap.get(useCaseName);
            useCaseAttachInfo.setActive(false);
            if (!useCaseAttachInfo.getAttached()) {
                this.mAttachedUseCasesToInfoMap.remove(useCaseName);
            }
        }
    }

    public void setUseCaseAttached(String useCaseName, SessionConfig sessionConfig) {
        getOrCreateUseCaseAttachInfo(useCaseName, sessionConfig).setAttached(true);
    }

    public void setUseCaseDetached(String useCaseName) {
        if (this.mAttachedUseCasesToInfoMap.containsKey(useCaseName)) {
            UseCaseAttachInfo useCaseAttachInfo = this.mAttachedUseCasesToInfoMap.get(useCaseName);
            useCaseAttachInfo.setAttached(false);
            if (!useCaseAttachInfo.getActive()) {
                this.mAttachedUseCasesToInfoMap.remove(useCaseName);
            }
        }
    }

    public boolean isUseCaseAttached(String useCaseName) {
        if (!this.mAttachedUseCasesToInfoMap.containsKey(useCaseName)) {
            return false;
        }
        return this.mAttachedUseCasesToInfoMap.get(useCaseName).getAttached();
    }

    public Collection<SessionConfig> getAttachedSessionConfigs() {
        return Collections.unmodifiableCollection(getSessionConfigs($$Lambda$UseCaseAttachState$ECqc7D4wrT6_n5qu4JAviYvI7P8.INSTANCE));
    }

    public Collection<SessionConfig> getActiveAndAttachedSessionConfigs() {
        return Collections.unmodifiableCollection(getSessionConfigs($$Lambda$UseCaseAttachState$LKtf6DM5i1yJ2BbR7MUvVVuV4.INSTANCE));
    }

    public static /* synthetic */ boolean lambda$getActiveAndAttachedSessionConfigs$1(UseCaseAttachInfo useCaseAttachInfo) {
        return useCaseAttachInfo.getActive() && useCaseAttachInfo.getAttached();
    }

    public void updateUseCase(String useCaseName, SessionConfig sessionConfig) {
        if (this.mAttachedUseCasesToInfoMap.containsKey(useCaseName)) {
            UseCaseAttachInfo newUseCaseAttachInfo = new UseCaseAttachInfo(sessionConfig);
            UseCaseAttachInfo oldUseCaseAttachInfo = this.mAttachedUseCasesToInfoMap.get(useCaseName);
            newUseCaseAttachInfo.setAttached(oldUseCaseAttachInfo.getAttached());
            newUseCaseAttachInfo.setActive(oldUseCaseAttachInfo.getActive());
            this.mAttachedUseCasesToInfoMap.put(useCaseName, newUseCaseAttachInfo);
        }
    }

    public SessionConfig.ValidatingBuilder getActiveAndAttachedBuilder() {
        SessionConfig.ValidatingBuilder validatingBuilder = new SessionConfig.ValidatingBuilder();
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, UseCaseAttachInfo> attachedUseCase : this.mAttachedUseCasesToInfoMap.entrySet()) {
            UseCaseAttachInfo useCaseAttachInfo = attachedUseCase.getValue();
            if (useCaseAttachInfo.getActive() && useCaseAttachInfo.getAttached()) {
                validatingBuilder.add(useCaseAttachInfo.getSessionConfig());
                list.add(attachedUseCase.getKey());
            }
        }
        Log.d(TAG, "Active and attached use case: " + list + " for camera: " + this.mCameraId);
        return validatingBuilder;
    }

    public SessionConfig.ValidatingBuilder getAttachedBuilder() {
        SessionConfig.ValidatingBuilder validatingBuilder = new SessionConfig.ValidatingBuilder();
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, UseCaseAttachInfo> attachedUseCase : this.mAttachedUseCasesToInfoMap.entrySet()) {
            UseCaseAttachInfo useCaseAttachInfo = attachedUseCase.getValue();
            if (useCaseAttachInfo.getAttached()) {
                validatingBuilder.add(useCaseAttachInfo.getSessionConfig());
                list.add(attachedUseCase.getKey());
            }
        }
        Log.d(TAG, "All use case: " + list + " for camera: " + this.mCameraId);
        return validatingBuilder;
    }

    private UseCaseAttachInfo getOrCreateUseCaseAttachInfo(String useCaseName, SessionConfig sessionConfig) {
        UseCaseAttachInfo useCaseAttachInfo = this.mAttachedUseCasesToInfoMap.get(useCaseName);
        if (useCaseAttachInfo != null) {
            return useCaseAttachInfo;
        }
        UseCaseAttachInfo useCaseAttachInfo2 = new UseCaseAttachInfo(sessionConfig);
        this.mAttachedUseCasesToInfoMap.put(useCaseName, useCaseAttachInfo2);
        return useCaseAttachInfo2;
    }

    private Collection<SessionConfig> getSessionConfigs(AttachStateFilter attachStateFilter) {
        List<SessionConfig> sessionConfigs = new ArrayList<>();
        for (Map.Entry<String, UseCaseAttachInfo> attachedUseCase : this.mAttachedUseCasesToInfoMap.entrySet()) {
            if (attachStateFilter == null || attachStateFilter.filter(attachedUseCase.getValue())) {
                sessionConfigs.add(attachedUseCase.getValue().getSessionConfig());
            }
        }
        return sessionConfigs;
    }

    /* loaded from: classes.dex */
    public static final class UseCaseAttachInfo {
        private final SessionConfig mSessionConfig;
        private boolean mAttached = false;
        private boolean mActive = false;

        UseCaseAttachInfo(SessionConfig sessionConfig) {
            this.mSessionConfig = sessionConfig;
        }

        SessionConfig getSessionConfig() {
            return this.mSessionConfig;
        }

        public boolean getAttached() {
            return this.mAttached;
        }

        void setAttached(boolean attached) {
            this.mAttached = attached;
        }

        boolean getActive() {
            return this.mActive;
        }

        void setActive(boolean active) {
            this.mActive = active;
        }
    }
}
