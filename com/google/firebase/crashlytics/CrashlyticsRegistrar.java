package com.google.firebase.crashlytics;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.connector.AnalyticsConnector;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.ComponentFactory;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.Dependency;
import com.google.firebase.crashlytics.internal.CrashlyticsNativeComponent;
import com.google.firebase.installations.FirebaseInstallationsApi;
import com.google.firebase.platforminfo.LibraryVersionComponent;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes3.dex */
public class CrashlyticsRegistrar implements ComponentRegistrar {
    @Override // com.google.firebase.components.ComponentRegistrar
    public List<Component<?>> getComponents() {
        return Arrays.asList(Component.builder(FirebaseCrashlytics.class).add(Dependency.required(FirebaseApp.class)).add(Dependency.required(FirebaseInstallationsApi.class)).add(Dependency.deferred(CrashlyticsNativeComponent.class)).add(Dependency.deferred(AnalyticsConnector.class)).factory(new ComponentFactory() { // from class: com.google.firebase.crashlytics.-$$Lambda$CrashlyticsRegistrar$aBuXIXlTYD2Kajd_VBsZ-5OANwA
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return CrashlyticsRegistrar.this.buildCrashlytics(componentContainer);
            }
        }).eagerInDefaultApp().build(), LibraryVersionComponent.create("fire-cls", BuildConfig.VERSION_NAME));
    }

    public FirebaseCrashlytics buildCrashlytics(ComponentContainer container) {
        return FirebaseCrashlytics.init((FirebaseApp) container.get(FirebaseApp.class), (FirebaseInstallationsApi) container.get(FirebaseInstallationsApi.class), container.getDeferred(CrashlyticsNativeComponent.class), container.getDeferred(AnalyticsConnector.class));
    }
}
