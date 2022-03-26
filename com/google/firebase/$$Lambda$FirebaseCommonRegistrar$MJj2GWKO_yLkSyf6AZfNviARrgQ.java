package com.google.firebase;

import android.content.Context;
import com.google.firebase.platforminfo.LibraryVersionComponent;
/* compiled from: lambda */
/* renamed from: com.google.firebase.-$$Lambda$FirebaseCommonRegistrar$MJj2GWKO_yLkSyf6AZfNviARrgQ  reason: invalid class name */
/* loaded from: classes3.dex */
public final /* synthetic */ class $$Lambda$FirebaseCommonRegistrar$MJj2GWKO_yLkSyf6AZfNviARrgQ implements LibraryVersionComponent.VersionExtractor {
    public static final /* synthetic */ $$Lambda$FirebaseCommonRegistrar$MJj2GWKO_yLkSyf6AZfNviARrgQ INSTANCE = new $$Lambda$FirebaseCommonRegistrar$MJj2GWKO_yLkSyf6AZfNviARrgQ();

    private /* synthetic */ $$Lambda$FirebaseCommonRegistrar$MJj2GWKO_yLkSyf6AZfNviARrgQ() {
    }

    @Override // com.google.firebase.platforminfo.LibraryVersionComponent.VersionExtractor
    public final String extract(Object obj) {
        return FirebaseCommonRegistrar.lambda$getComponents$0((Context) obj);
    }
}
