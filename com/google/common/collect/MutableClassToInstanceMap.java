package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Primitives;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public final class MutableClassToInstanceMap<B> extends ForwardingMap<Class<? extends B>, B> implements ClassToInstanceMap<B>, Serializable {
    private final Map<Class<? extends B>, B> delegate;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
    public /* bridge */ /* synthetic */ Object put(Object obj, Object obj2) {
        return put((Class<? extends Class<? extends B>>) ((Class) obj), (Class<? extends B>) obj2);
    }

    public static <B> MutableClassToInstanceMap<B> create() {
        return new MutableClassToInstanceMap<>(new HashMap());
    }

    public static <B> MutableClassToInstanceMap<B> create(Map<Class<? extends B>, B> backingMap) {
        return new MutableClassToInstanceMap<>(backingMap);
    }

    private MutableClassToInstanceMap(Map<Class<? extends B>, B> delegate) {
        this.delegate = (Map) Preconditions.checkNotNull(delegate);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    public Map<Class<? extends B>, B> delegate() {
        return this.delegate;
    }

    static <B> Map.Entry<Class<? extends B>, B> checkedEntry(final Map.Entry<Class<? extends B>, B> entry) {
        return new ForwardingMapEntry<Class<? extends B>, B>() { // from class: com.google.common.collect.MutableClassToInstanceMap.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.common.collect.ForwardingMapEntry, com.google.common.collect.ForwardingObject
            public Map.Entry<Class<? extends B>, B> delegate() {
                return entry;
            }

            @Override // com.google.common.collect.ForwardingMapEntry, java.util.Map.Entry
            public B setValue(B value) {
                return (B) super.setValue(MutableClassToInstanceMap.cast(getKey(), value));
            }
        };
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map
    public Set<Map.Entry<Class<? extends B>, B>> entrySet() {
        return new ForwardingSet<Map.Entry<Class<? extends B>, B>>() { // from class: com.google.common.collect.MutableClassToInstanceMap.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
            public Set<Map.Entry<Class<? extends B>, B>> delegate() {
                return MutableClassToInstanceMap.this.delegate().entrySet();
            }

            @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Map.Entry<Class<? extends B>, B>> iterator() {
                return new TransformedIterator<Map.Entry<Class<? extends B>, B>, Map.Entry<Class<? extends B>, B>>(delegate().iterator()) { // from class: com.google.common.collect.MutableClassToInstanceMap.2.1
                    @Override // com.google.common.collect.TransformedIterator
                    /* bridge */ /* synthetic */ Object transform(Object obj) {
                        return transform((Map.Entry) ((Map.Entry) obj));
                    }

                    Map.Entry<Class<? extends B>, B> transform(Map.Entry<Class<? extends B>, B> from) {
                        return MutableClassToInstanceMap.checkedEntry(from);
                    }
                };
            }

            @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
            public Object[] toArray() {
                return standardToArray();
            }

            @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
            public <T> T[] toArray(T[] array) {
                return (T[]) standardToArray(array);
            }
        };
    }

    public B put(Class<? extends B> key, B value) {
        return (B) super.put((MutableClassToInstanceMap<B>) key, (Class<? extends B>) cast(key, value));
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
    public void putAll(Map<? extends Class<? extends B>, ? extends B> map) {
        Map<Class<? extends B>, B> copy = new LinkedHashMap<>(map);
        for (Map.Entry<? extends Class<? extends B>, B> entry : copy.entrySet()) {
            cast(entry.getKey(), entry.getValue());
        }
        super.putAll(copy);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ClassToInstanceMap
    public <T extends B> T putInstance(Class<T> type, T value) {
        return (T) cast(type, put((Class<? extends Class<T>>) type, (Class<T>) value));
    }

    @Override // com.google.common.collect.ClassToInstanceMap
    public <T extends B> T getInstance(Class<T> type) {
        return (T) cast(type, get(type));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <B, T extends B> T cast(Class<T> type, B value) {
        return (T) Primitives.wrap(type).cast(value);
    }

    private Object writeReplace() {
        return new SerializedForm(delegate());
    }

    /* loaded from: classes.dex */
    private static final class SerializedForm<B> implements Serializable {
        private static final long serialVersionUID = 0;
        private final Map<Class<? extends B>, B> backingMap;

        SerializedForm(Map<Class<? extends B>, B> backingMap) {
            this.backingMap = backingMap;
        }

        Object readResolve() {
            return MutableClassToInstanceMap.create(this.backingMap);
        }
    }
}
