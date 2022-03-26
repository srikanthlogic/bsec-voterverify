package com.google.firebase.platforminfo;

import android.content.Context;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.ComponentFactory;
import com.google.firebase.components.Dependency;
import com.google.firebase.platforminfo.LibraryVersionComponent;
/* loaded from: classes3.dex */
public class LibraryVersionComponent {

    /* loaded from: classes3.dex */
    public interface VersionExtractor<T> {
        String extract(T t);
    }

    private LibraryVersionComponent() {
    }

    public static Component<?> create(String sdkName, String version) {
        return Component.intoSet(LibraryVersion.create(sdkName, version), LibraryVersion.class);
    }

    public static Component<?> fromContext(String sdkName, VersionExtractor<Context> extractor) {
        return Component.intoSetBuilder(LibraryVersion.class).add(Dependency.required(Context.class)).factory(new ComponentFactory(sdkName, extractor) { // from class: com.google.firebase.platforminfo.-$$Lambda$LibraryVersionComponent$rayo-DysRFXf1L6r-IJ9iU3u71g
            private final /* synthetic */ String f$0;
            private final /* synthetic */ LibraryVersionComponent.VersionExtractor f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return LibraryVersion.create(this.f$0, this.f$1.extract((Context) componentContainer.get(Context.class)));
            }
        }).build();
    }
}
