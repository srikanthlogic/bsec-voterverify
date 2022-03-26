package com.google.firebase.crashlytics.internal.common;

import android.content.Context;
/* loaded from: classes3.dex */
class InstallerPackageNameProvider {
    private static final String NO_INSTALLER_PACKAGE_NAME = "";
    private String installerPackageName;

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized String getInstallerPackageName(Context appContext) {
        if (this.installerPackageName == null) {
            this.installerPackageName = loadInstallerPackageName(appContext);
        }
        return "".equals(this.installerPackageName) ? null : this.installerPackageName;
    }

    private static String loadInstallerPackageName(Context appContext) {
        String installerPackageName = appContext.getPackageManager().getInstallerPackageName(appContext.getPackageName());
        return installerPackageName == null ? "" : installerPackageName;
    }
}
