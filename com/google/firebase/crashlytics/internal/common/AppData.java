package com.google.firebase.crashlytics.internal.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.google.firebase.crashlytics.internal.unity.UnityVersionProvider;
/* loaded from: classes3.dex */
public class AppData {
    public final String buildId;
    public final String googleAppId;
    public final String installerPackageName;
    public final String packageName;
    public final UnityVersionProvider unityVersionProvider;
    public final String versionCode;
    public final String versionName;

    public static AppData create(Context context, IdManager idManager, String googleAppId, String buildId, UnityVersionProvider unityVersionProvider) throws PackageManager.NameNotFoundException {
        String packageName = context.getPackageName();
        String installerPackageName = idManager.getInstallerPackageName();
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        return new AppData(googleAppId, buildId, installerPackageName, packageName, Integer.toString(packageInfo.versionCode), packageInfo.versionName == null ? IdManager.DEFAULT_VERSION_NAME : packageInfo.versionName, unityVersionProvider);
    }

    public AppData(String googleAppId, String buildId, String installerPackageName, String packageName, String versionCode, String versionName, UnityVersionProvider unityVersionProvider) {
        this.googleAppId = googleAppId;
        this.buildId = buildId;
        this.installerPackageName = installerPackageName;
        this.packageName = packageName;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.unityVersionProvider = unityVersionProvider;
    }
}
