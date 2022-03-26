package com.google.firebase;

import android.content.Context;
import com.google.firebase.platforminfo.LibraryVersionComponent;
/* compiled from: lambda */
/* renamed from: com.google.firebase.-$$Lambda$FirebaseCommonRegistrar$0SsttI_xA8sAI74ZXlgAQ_-rvhA */
/* loaded from: classes3.dex */
public final /* synthetic */ class $$Lambda$FirebaseCommonRegistrar$0SsttI_xA8sAI74ZXlgAQ_rvhA implements LibraryVersionComponent.VersionExtractor {
    public static final /* synthetic */ $$Lambda$FirebaseCommonRegistrar$0SsttI_xA8sAI74ZXlgAQ_rvhA INSTANCE = new $$Lambda$FirebaseCommonRegistrar$0SsttI_xA8sAI74ZXlgAQ_rvhA();

    private /* synthetic */ $$Lambda$FirebaseCommonRegistrar$0SsttI_xA8sAI74ZXlgAQ_rvhA() {
    }

    @Override // com.google.firebase.platforminfo.LibraryVersionComponent.VersionExtractor
    public final String extract(Object obj) {
        return r1.getPackageManager().getInstallerPackageName(((Context) obj).getPackageName());
    }
}
