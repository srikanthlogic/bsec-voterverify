package com.google.firebase.components;

import com.google.firebase.inject.Provider;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public abstract class AbstractComponentContainer implements ComponentContainer {
    @Override // com.google.firebase.components.ComponentContainer
    public <T> T get(Class<T> anInterface) {
        Provider<T> provider = getProvider(anInterface);
        if (provider == null) {
            return null;
        }
        return provider.get();
    }

    @Override // com.google.firebase.components.ComponentContainer
    public <T> Set<T> setOf(Class<T> anInterface) {
        return setOfProvider(anInterface).get();
    }
}
