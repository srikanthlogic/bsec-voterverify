package com.google.firebase;

import android.content.Context;
import com.google.firebase.platforminfo.LibraryVersionComponent;
/* compiled from: lambda */
/* renamed from: com.google.firebase.-$$Lambda$FirebaseCommonRegistrar$OMGxGzs72JnsFA__aYRvT3a3SZo  reason: invalid class name */
/* loaded from: classes3.dex */
public final /* synthetic */ class $$Lambda$FirebaseCommonRegistrar$OMGxGzs72JnsFA__aYRvT3a3SZo implements LibraryVersionComponent.VersionExtractor {
    public static final /* synthetic */ $$Lambda$FirebaseCommonRegistrar$OMGxGzs72JnsFA__aYRvT3a3SZo INSTANCE = new $$Lambda$FirebaseCommonRegistrar$OMGxGzs72JnsFA__aYRvT3a3SZo();

    private /* synthetic */ $$Lambda$FirebaseCommonRegistrar$OMGxGzs72JnsFA__aYRvT3a3SZo() {
    }

    @Override // com.google.firebase.platforminfo.LibraryVersionComponent.VersionExtractor
    public final String extract(Object obj) {
        return FirebaseCommonRegistrar.lambda$getComponents$2((Context) obj);
    }
}
