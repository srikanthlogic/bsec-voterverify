package com.google.firebase.crashlytics.internal;

import com.google.firebase.crashlytics.internal.model.StaticSessionData;
import com.google.firebase.inject.Deferred;
import com.google.firebase.inject.Provider;
import java.io.File;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: classes3.dex */
public final class CrashlyticsNativeComponentDeferredProxy implements CrashlyticsNativeComponent {
    private static final NativeSessionFileProvider MISSING_NATIVE_SESSION_FILE_PROVIDER = new MissingNativeSessionFileProvider();
    private final AtomicReference<CrashlyticsNativeComponent> availableNativeComponent = new AtomicReference<>(null);
    private final Deferred<CrashlyticsNativeComponent> deferredNativeComponent;

    public CrashlyticsNativeComponentDeferredProxy(Deferred<CrashlyticsNativeComponent> deferredNativeComponent) {
        this.deferredNativeComponent = deferredNativeComponent;
        this.deferredNativeComponent.whenAvailable(new Deferred.DeferredHandler() { // from class: com.google.firebase.crashlytics.internal.-$$Lambda$CrashlyticsNativeComponentDeferredProxy$7NRuOGLlJiZHqRwz-2rcS10so9w
            @Override // com.google.firebase.inject.Deferred.DeferredHandler
            public final void handle(Provider provider) {
                CrashlyticsNativeComponentDeferredProxy.this.lambda$new$0$CrashlyticsNativeComponentDeferredProxy(provider);
            }
        });
    }

    public /* synthetic */ void lambda$new$0$CrashlyticsNativeComponentDeferredProxy(Provider nativeComponent) {
        Logger.getLogger().d("Crashlytics native component now available.");
        this.availableNativeComponent.set((CrashlyticsNativeComponent) nativeComponent.get());
    }

    @Override // com.google.firebase.crashlytics.internal.CrashlyticsNativeComponent
    public boolean hasCrashDataForSession(String sessionId) {
        CrashlyticsNativeComponent component = this.availableNativeComponent.get();
        return component != null && component.hasCrashDataForSession(sessionId);
    }

    @Override // com.google.firebase.crashlytics.internal.CrashlyticsNativeComponent
    public void openSession(String sessionId, String generator, long startedAtSeconds, StaticSessionData sessionData) {
        Logger logger = Logger.getLogger();
        logger.v("Deferring native open session: " + sessionId);
        this.deferredNativeComponent.whenAvailable(new Deferred.DeferredHandler(sessionId, generator, startedAtSeconds, sessionData) { // from class: com.google.firebase.crashlytics.internal.-$$Lambda$CrashlyticsNativeComponentDeferredProxy$l0rJcDMfc71DRoE6xf7c-GA-4l0
            private final /* synthetic */ String f$0;
            private final /* synthetic */ String f$1;
            private final /* synthetic */ long f$2;
            private final /* synthetic */ StaticSessionData f$3;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r5;
            }

            @Override // com.google.firebase.inject.Deferred.DeferredHandler
            public final void handle(Provider provider) {
                ((CrashlyticsNativeComponent) provider.get()).openSession(this.f$0, this.f$1, this.f$2, this.f$3);
            }
        });
    }

    @Override // com.google.firebase.crashlytics.internal.CrashlyticsNativeComponent
    public void finalizeSession(String sessionId) {
        this.deferredNativeComponent.whenAvailable(new Deferred.DeferredHandler(sessionId) { // from class: com.google.firebase.crashlytics.internal.-$$Lambda$CrashlyticsNativeComponentDeferredProxy$Bbzn4T42cRyF4nhKt1XyCbcuXUw
            private final /* synthetic */ String f$0;

            {
                this.f$0 = r1;
            }

            @Override // com.google.firebase.inject.Deferred.DeferredHandler
            public final void handle(Provider provider) {
                ((CrashlyticsNativeComponent) provider.get()).finalizeSession(this.f$0);
            }
        });
    }

    @Override // com.google.firebase.crashlytics.internal.CrashlyticsNativeComponent
    public NativeSessionFileProvider getSessionFileProvider(String sessionId) {
        CrashlyticsNativeComponent component = this.availableNativeComponent.get();
        if (component == null) {
            return MISSING_NATIVE_SESSION_FILE_PROVIDER;
        }
        return component.getSessionFileProvider(sessionId);
    }

    /* loaded from: classes3.dex */
    private static final class MissingNativeSessionFileProvider implements NativeSessionFileProvider {
        private MissingNativeSessionFileProvider() {
        }

        @Override // com.google.firebase.crashlytics.internal.NativeSessionFileProvider
        public File getMinidumpFile() {
            return null;
        }

        @Override // com.google.firebase.crashlytics.internal.NativeSessionFileProvider
        public File getBinaryImagesFile() {
            return null;
        }

        @Override // com.google.firebase.crashlytics.internal.NativeSessionFileProvider
        public File getMetadataFile() {
            return null;
        }

        @Override // com.google.firebase.crashlytics.internal.NativeSessionFileProvider
        public File getSessionFile() {
            return null;
        }

        @Override // com.google.firebase.crashlytics.internal.NativeSessionFileProvider
        public File getAppFile() {
            return null;
        }

        @Override // com.google.firebase.crashlytics.internal.NativeSessionFileProvider
        public File getDeviceFile() {
            return null;
        }

        @Override // com.google.firebase.crashlytics.internal.NativeSessionFileProvider
        public File getOsFile() {
            return null;
        }
    }
}
