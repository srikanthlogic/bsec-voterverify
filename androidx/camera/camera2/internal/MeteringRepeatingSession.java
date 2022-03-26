package androidx.camera.camera2.internal;

import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Surface;
import androidx.camera.core.UseCase;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.ImmediateSurface;
import androidx.camera.core.impl.MutableOptionsBundle;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.UseCaseConfig;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.impl.utils.futures.FutureCallback;
import androidx.camera.core.impl.utils.futures.Futures;
/* loaded from: classes.dex */
class MeteringRepeatingSession {
    private DeferrableSurface mDeferrableSurface;
    private final SessionConfig mSessionConfig;
    private static final String TAG = "MeteringRepeating";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);

    /* JADX INFO: Access modifiers changed from: package-private */
    public MeteringRepeatingSession() {
        MeteringRepeatingConfig configWithDefaults = new MeteringRepeatingConfig();
        final SurfaceTexture surfaceTexture = new SurfaceTexture(0);
        surfaceTexture.setDefaultBufferSize(0, 0);
        final Surface surface = new Surface(surfaceTexture);
        SessionConfig.Builder builder = SessionConfig.Builder.createFrom(configWithDefaults);
        builder.setTemplateType(1);
        this.mDeferrableSurface = new ImmediateSurface(surface);
        Futures.addCallback(this.mDeferrableSurface.getTerminationFuture(), new FutureCallback<Void>() { // from class: androidx.camera.camera2.internal.MeteringRepeatingSession.1
            public void onSuccess(Void result) {
                surface.release();
                surfaceTexture.release();
            }

            @Override // androidx.camera.core.impl.utils.futures.FutureCallback
            public void onFailure(Throwable t) {
                throw new IllegalStateException("Future should never fail. Did it get completed by GC?", t);
            }
        }, CameraXExecutors.directExecutor());
        builder.addSurface(this.mDeferrableSurface);
        this.mSessionConfig = builder.build();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SessionConfig getSessionConfig() {
        return this.mSessionConfig;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getName() {
        return TAG;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clear() {
        if (DEBUG) {
            Log.d(TAG, "MeteringRepeating clear!");
        }
        DeferrableSurface deferrableSurface = this.mDeferrableSurface;
        if (deferrableSurface != null) {
            deferrableSurface.close();
        }
        this.mDeferrableSurface = null;
    }

    /* loaded from: classes.dex */
    private static class MeteringRepeatingConfig implements UseCaseConfig<UseCase> {
        private final Config mConfig;

        MeteringRepeatingConfig() {
            MutableOptionsBundle mutableOptionsBundle = MutableOptionsBundle.create();
            mutableOptionsBundle.insertOption(UseCaseConfig.OPTION_SESSION_CONFIG_UNPACKER, new Camera2SessionOptionUnpacker());
            this.mConfig = mutableOptionsBundle;
        }

        @Override // androidx.camera.core.impl.ReadableConfig
        public Config getConfig() {
            return this.mConfig;
        }
    }
}
