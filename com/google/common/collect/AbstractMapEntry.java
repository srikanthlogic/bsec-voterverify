package com.google.common.collect;

import com.google.common.base.Objects;
import java.util.Map;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class AbstractMapEntry<K, V> implements Map.Entry<K, V> {
    @Override // java.util.Map.Entry
    public abstract K getKey();

    @Override // java.util.Map.Entry
    public abstract V getValue();

    @Override // java.util.Map.Entry
    public V setValue(V value) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map.Entry, java.lang.Object
    public boolean equals(@NullableDecl Object object) {
        if (!(object instanceof Map.Entry)) {
            return false;
        }
        Map.Entry<?, ?> that = (Map.Entry) object;
        if (!Objects.equal(getKey(), that.getKey()) || !Objects.equal(getValue(), that.getValue())) {
            return false;
        }
        return true;
    }

    @Override // java.util.Map.Entry, java.lang.Object
    public int hashCode() {
        K k = getKey();
        V v = getValue();
        int i = 0;
        int hashCode = k == null ? 0 : k.hashCode();
        if (v != null) {
            i = v.hashCode();
        }
        return i ^ hashCode;
    }

    @Override // java.lang.Object
    public String toString() {
        return getKey() + "=" + getValue();
    }
}
