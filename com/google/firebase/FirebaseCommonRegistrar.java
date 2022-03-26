package com.google.firebase;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.heartbeatinfo.DefaultHeartBeatInfo;
import com.google.firebase.platforminfo.DefaultUserAgentPublisher;
import com.google.firebase.platforminfo.KotlinDetector;
import com.google.firebase.platforminfo.LibraryVersionComponent;
import java.util.ArrayList;
import java.util.List;
import kotlinx.coroutines.DebugKt;
import org.apache.commons.io.IOUtils;
/* loaded from: classes3.dex */
public class FirebaseCommonRegistrar implements ComponentRegistrar {
    private static final String ANDROID_INSTALLER;
    private static final String ANDROID_PLATFORM;
    private static final String DEVICE_BRAND;
    private static final String DEVICE_MODEL;
    private static final String DEVICE_NAME;
    private static final String FIREBASE_ANDROID;
    private static final String FIREBASE_COMMON;
    private static final String KOTLIN;
    private static final String MIN_SDK;
    private static final String TARGET_SDK;

    @Override // com.google.firebase.components.ComponentRegistrar
    public List<Component<?>> getComponents() {
        List<Component<?>> result = new ArrayList<>();
        result.add(DefaultUserAgentPublisher.component());
        result.add(DefaultHeartBeatInfo.component());
        result.add(LibraryVersionComponent.create(FIREBASE_ANDROID, String.valueOf(Build.VERSION.SDK_INT)));
        result.add(LibraryVersionComponent.create(FIREBASE_COMMON, BuildConfig.VERSION_NAME));
        result.add(LibraryVersionComponent.create(DEVICE_NAME, safeValue(Build.PRODUCT)));
        result.add(LibraryVersionComponent.create(DEVICE_MODEL, safeValue(Build.DEVICE)));
        result.add(LibraryVersionComponent.create(DEVICE_BRAND, safeValue(Build.BRAND)));
        result.add(LibraryVersionComponent.fromContext(TARGET_SDK, $$Lambda$FirebaseCommonRegistrar$MJj2GWKO_yLkSyf6AZfNviARrgQ.INSTANCE));
        result.add(LibraryVersionComponent.fromContext(MIN_SDK, $$Lambda$FirebaseCommonRegistrar$pGT1RcP4RapBpOq2V73IRqI1I.INSTANCE));
        result.add(LibraryVersionComponent.fromContext(ANDROID_PLATFORM, $$Lambda$FirebaseCommonRegistrar$OMGxGzs72JnsFA__aYRvT3a3SZo.INSTANCE));
        result.add(LibraryVersionComponent.fromContext(ANDROID_INSTALLER, $$Lambda$FirebaseCommonRegistrar$0SsttI_xA8sAI74ZXlgAQ_rvhA.INSTANCE));
        String kotlinVersion = KotlinDetector.detectVersion();
        if (kotlinVersion != null) {
            result.add(LibraryVersionComponent.create(KOTLIN, kotlinVersion));
        }
        return result;
    }

    public static /* synthetic */ String lambda$getComponents$0(Context ctx) {
        ApplicationInfo info = ctx.getApplicationInfo();
        if (info != null) {
            return String.valueOf(info.targetSdkVersion);
        }
        return "";
    }

    public static /* synthetic */ String lambda$getComponents$1(Context ctx) {
        ApplicationInfo info = ctx.getApplicationInfo();
        if (info == null || Build.VERSION.SDK_INT < 24) {
            return "";
        }
        return String.valueOf(info.minSdkVersion);
    }

    public static /* synthetic */ String lambda$getComponents$2(Context ctx) {
        if (Build.VERSION.SDK_INT >= 16 && ctx.getPackageManager().hasSystemFeature("android.hardware.type.television")) {
            return "tv";
        }
        if (Build.VERSION.SDK_INT >= 20 && ctx.getPackageManager().hasSystemFeature("android.hardware.type.watch")) {
            return "watch";
        }
        if (Build.VERSION.SDK_INT >= 23 && ctx.getPackageManager().hasSystemFeature("android.hardware.type.automotive")) {
            return DebugKt.DEBUG_PROPERTY_VALUE_AUTO;
        }
        if (Build.VERSION.SDK_INT < 26 || !ctx.getPackageManager().hasSystemFeature("android.hardware.type.embedded")) {
            return "";
        }
        return "embedded";
    }

    private static String safeValue(String value) {
        return value.replace(' ', '_').replace(IOUtils.DIR_SEPARATOR_UNIX, '_');
    }
}
