package kotlin.collections;

import androidx.exifinterface.media.ExifInterface;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
/* compiled from: AbstractMap.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0010\"\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010&\n\u0002\b\b\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0003\b'\u0018\u0000 )*\u0004\b\u0000\u0010\u0001*\u0006\b\u0001\u0010\u0002 \u00012\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u0003:\u0001)B\u0007\b\u0004¢\u0006\u0002\u0010\u0004J\u001f\u0010\u0013\u001a\u00020\u00142\u0010\u0010\u0015\u001a\f\u0012\u0002\b\u0003\u0012\u0002\b\u0003\u0018\u00010\u0016H\u0000¢\u0006\u0002\b\u0017J\u0015\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u001aJ\u0015\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00028\u0001H\u0016¢\u0006\u0002\u0010\u001aJ\u0013\u0010\u001d\u001a\u00020\u00142\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0096\u0002J\u0018\u0010 \u001a\u0004\u0018\u00018\u00012\u0006\u0010\u0019\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010!J\b\u0010\"\u001a\u00020\rH\u0016J#\u0010#\u001a\u0010\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u0001\u0018\u00010\u00162\u0006\u0010\u0019\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010$J\b\u0010%\u001a\u00020\u0014H\u0016J\b\u0010&\u001a\u00020'H\u0016J\u0012\u0010&\u001a\u00020'2\b\u0010(\u001a\u0004\u0018\u00010\u001fH\u0002J\u001c\u0010&\u001a\u00020'2\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0016H\bR\u0016\u0010\u0005\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n\u0012\u0004\u0012\u00028\u0001\u0018\u00010\bX\u0088\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00000\u00068VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\u00020\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00010\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012¨\u0006*"}, d2 = {"Lkotlin/collections/AbstractMap;", "K", ExifInterface.GPS_MEASUREMENT_INTERRUPTED, "", "()V", "_keys", "", "_values", "", "keys", "getKeys", "()Ljava/util/Set;", "size", "", "getSize", "()I", "values", "getValues", "()Ljava/util/Collection;", "containsEntry", "", "entry", "", "containsEntry$kotlin_stdlib", "containsKey", "key", "(Ljava/lang/Object;)Z", "containsValue", "value", "equals", "other", "", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", "hashCode", "implFindEntry", "(Ljava/lang/Object;)Ljava/util/Map$Entry;", "isEmpty", "toString", "", "o", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public abstract class AbstractMap<K, V> implements Map<K, V>, KMappedMarker {
    public static final Companion Companion = new Companion(null);
    private volatile Set<? extends K> _keys;
    private volatile Collection<? extends V> _values;

    @Override // java.util.Map
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public abstract Set getEntries();

    @Override // java.util.Map
    public V put(K k, V v) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Map
    public V remove(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    protected AbstractMap() {
    }

    @Override // java.util.Map
    public final /* bridge */ Set<Map.Entry<K, V>> entrySet() {
        return getEntries();
    }

    @Override // java.util.Map
    public final /* bridge */ Set<K> keySet() {
        return getKeys();
    }

    @Override // java.util.Map
    public final /* bridge */ int size() {
        return getSize();
    }

    @Override // java.util.Map
    public final /* bridge */ Collection<V> values() {
        return getValues();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return implFindEntry(key) != null;
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        Iterable<Map.Entry> $this$any$iv = entrySet();
        if (($this$any$iv instanceof Collection) && ((Collection) $this$any$iv).isEmpty()) {
            return false;
        }
        for (Map.Entry it : $this$any$iv) {
            if (Intrinsics.areEqual(it.getValue(), value)) {
                return true;
            }
        }
        return false;
    }

    public final boolean containsEntry$kotlin_stdlib(Map.Entry<?, ?> entry) {
        if (!(entry instanceof Map.Entry)) {
            return false;
        }
        Object key = entry.getKey();
        Object value = entry.getValue();
        Object ourValue = get(key);
        if (!Intrinsics.areEqual(value, ourValue)) {
            return false;
        }
        if (ourValue != null || containsKey(key)) {
            return true;
        }
        return false;
    }

    @Override // java.util.Map, java.lang.Object
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!((other instanceof Map) && size() == ((Map) other).size())) {
            return false;
        }
        Iterable<Map.Entry> $this$all$iv = ((Map) other).entrySet();
        if (($this$all$iv instanceof Collection) && ((Collection) $this$all$iv).isEmpty()) {
            return true;
        }
        for (Map.Entry it : $this$all$iv) {
            if (!containsEntry$kotlin_stdlib(it)) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public V get(Object key) {
        Map.Entry<K, V> implFindEntry = implFindEntry(key);
        if (implFindEntry != null) {
            return implFindEntry.getValue();
        }
        return null;
    }

    @Override // java.util.Map, java.lang.Object
    public int hashCode() {
        return entrySet().hashCode();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return size() == 0;
    }

    public int getSize() {
        return entrySet().size();
    }

    public Set<K> getKeys() {
        if (this._keys == null) {
            this._keys = new AbstractSet<K>() { // from class: kotlin.collections.AbstractMap$keys$1
                @Override // kotlin.collections.AbstractCollection, java.util.Collection
                public boolean contains(Object element) {
                    return AbstractMap.this.containsKey(element);
                }

                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0011: RETURN  
                      (wrap: kotlin.collections.AbstractMap$keys$1$iterator$1 : 0x000f: CONSTRUCTOR  (r1v1 kotlin.collections.AbstractMap$keys$1$iterator$1 A[REMOVE]) = 
                      (wrap: java.util.Iterator : 0x0006: INVOKE  (r0v2 'entryIterator' java.util.Iterator A[REMOVE]) = 
                      (wrap: java.util.Set : 0x0002: INVOKE  (r0v1 java.util.Set A[REMOVE]) = 
                      (wrap: kotlin.collections.AbstractMap : 0x0000: IGET  (r0v0 kotlin.collections.AbstractMap A[REMOVE]) = (r2v0 'this' kotlin.collections.AbstractMap$keys$1 A[IMMUTABLE_TYPE, THIS]) kotlin.collections.AbstractMap$keys$1.this$0 kotlin.collections.AbstractMap)
                     type: VIRTUAL call: kotlin.collections.AbstractMap.entrySet():java.util.Set)
                     type: INTERFACE call: java.util.Set.iterator():java.util.Iterator)
                     call: kotlin.collections.AbstractMap$keys$1$iterator$1.<init>(java.util.Iterator):void type: CONSTRUCTOR)
                     in method: kotlin.collections.AbstractMap$keys$1.iterator():java.util.Iterator<K>, file: classes3.dex
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:270)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:233)
                    	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:90)
                    	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                    	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:255)
                    	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:248)
                    	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:369)
                    	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:304)
                    Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: kotlin.collections.AbstractMap$keys$1$iterator$1, state: NOT_LOADED
                    	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:268)
                    	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:668)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:378)
                    	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:132)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:328)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:263)
                    	... 10 more
                    */
                @Override // kotlin.collections.AbstractSet, kotlin.collections.AbstractCollection, java.util.Collection, java.lang.Iterable
                public java.util.Iterator<K> iterator() {
                    /*
                        r2 = this;
                        kotlin.collections.AbstractMap r0 = kotlin.collections.AbstractMap.this
                        java.util.Set r0 = r0.entrySet()
                        java.util.Iterator r0 = r0.iterator()
                        kotlin.collections.AbstractMap$keys$1$iterator$1 r1 = new kotlin.collections.AbstractMap$keys$1$iterator$1
                        r1.<init>(r0)
                        java.util.Iterator r1 = (java.util.Iterator) r1
                        return r1
                    */
                    throw new UnsupportedOperationException("Method not decompiled: kotlin.collections.AbstractMap$keys$1.iterator():java.util.Iterator");
                }

                @Override // kotlin.collections.AbstractCollection
                public int getSize() {
                    return AbstractMap.this.size();
                }
            };
        }
        Set set = (Set<? extends K>) this._keys;
        if (set == null) {
            Intrinsics.throwNpe();
        }
        return set;
    }

    @Override // java.lang.Object
    public String toString() {
        return CollectionsKt.joinToString$default(entrySet(), ", ", "{", "}", 0, null, new Function1<Map.Entry<? extends K, ? extends V>, String>() { // from class: kotlin.collections.AbstractMap$toString$1
            public final String invoke(Map.Entry<? extends K, ? extends V> entry) {
                Intrinsics.checkParameterIsNotNull(entry, "it");
                return AbstractMap.this.toString((Map.Entry) entry);
            }
        }, 24, null);
    }

    public final String toString(Map.Entry<? extends K, ? extends V> entry) {
        return toString(entry.getKey()) + "=" + toString(entry.getValue());
    }

    private final String toString(Object o) {
        return o == this ? "(this Map)" : String.valueOf(o);
    }

    public Collection<V> getValues() {
        if (this._values == null) {
            this._values = new AbstractCollection<V>() { // from class: kotlin.collections.AbstractMap$values$1
                @Override // kotlin.collections.AbstractCollection, java.util.Collection
                public boolean contains(Object element) {
                    return AbstractMap.this.containsValue(element);
                }

                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0011: RETURN  
                      (wrap: kotlin.collections.AbstractMap$values$1$iterator$1 : 0x000f: CONSTRUCTOR  (r1v1 kotlin.collections.AbstractMap$values$1$iterator$1 A[REMOVE]) = 
                      (wrap: java.util.Iterator : 0x0006: INVOKE  (r0v2 'entryIterator' java.util.Iterator A[REMOVE]) = 
                      (wrap: java.util.Set : 0x0002: INVOKE  (r0v1 java.util.Set A[REMOVE]) = 
                      (wrap: kotlin.collections.AbstractMap : 0x0000: IGET  (r0v0 kotlin.collections.AbstractMap A[REMOVE]) = (r2v0 'this' kotlin.collections.AbstractMap$values$1 A[IMMUTABLE_TYPE, THIS]) kotlin.collections.AbstractMap$values$1.this$0 kotlin.collections.AbstractMap)
                     type: VIRTUAL call: kotlin.collections.AbstractMap.entrySet():java.util.Set)
                     type: INTERFACE call: java.util.Set.iterator():java.util.Iterator)
                     call: kotlin.collections.AbstractMap$values$1$iterator$1.<init>(java.util.Iterator):void type: CONSTRUCTOR)
                     in method: kotlin.collections.AbstractMap$values$1.iterator():java.util.Iterator<V>, file: classes3.dex
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:270)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:233)
                    	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:90)
                    	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                    	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:255)
                    	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:248)
                    	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:369)
                    	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:304)
                    Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: kotlin.collections.AbstractMap$values$1$iterator$1, state: NOT_LOADED
                    	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:268)
                    	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:668)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:378)
                    	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:132)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:328)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:263)
                    	... 10 more
                    */
                @Override // kotlin.collections.AbstractCollection, java.util.Collection, java.lang.Iterable
                public java.util.Iterator<V> iterator() {
                    /*
                        r2 = this;
                        kotlin.collections.AbstractMap r0 = kotlin.collections.AbstractMap.this
                        java.util.Set r0 = r0.entrySet()
                        java.util.Iterator r0 = r0.iterator()
                        kotlin.collections.AbstractMap$values$1$iterator$1 r1 = new kotlin.collections.AbstractMap$values$1$iterator$1
                        r1.<init>(r0)
                        java.util.Iterator r1 = (java.util.Iterator) r1
                        return r1
                    */
                    throw new UnsupportedOperationException("Method not decompiled: kotlin.collections.AbstractMap$values$1.iterator():java.util.Iterator");
                }

                @Override // kotlin.collections.AbstractCollection
                public int getSize() {
                    return AbstractMap.this.size();
                }
            };
        }
        Collection collection = (Collection<? extends V>) this._values;
        if (collection == null) {
            Intrinsics.throwNpe();
        }
        return collection;
    }

    private final Map.Entry<K, V> implFindEntry(K k) {
        Object element$iv;
        Iterator<T> it = entrySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                element$iv = null;
                break;
            }
            element$iv = it.next();
            if (Intrinsics.areEqual(((Map.Entry) element$iv).getKey(), k)) {
                break;
            }
        }
        return (Map.Entry) element$iv;
    }

    /* compiled from: AbstractMap.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010&\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J'\u0010\u0003\u001a\u00020\u00042\u000e\u0010\u0005\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u0001H\u0000¢\u0006\u0002\b\bJ\u001d\u0010\t\u001a\u00020\n2\u000e\u0010\u0005\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0006H\u0000¢\u0006\u0002\b\u000bJ\u001d\u0010\f\u001a\u00020\r2\u000e\u0010\u0005\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0006H\u0000¢\u0006\u0002\b\u000e¨\u0006\u000f"}, d2 = {"Lkotlin/collections/AbstractMap$Companion;", "", "()V", "entryEquals", "", "e", "", "other", "entryEquals$kotlin_stdlib", "entryHashCode", "", "entryHashCode$kotlin_stdlib", "entryToString", "", "entryToString$kotlin_stdlib", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        public final int entryHashCode$kotlin_stdlib(Map.Entry<?, ?> entry) {
            Intrinsics.checkParameterIsNotNull(entry, "e");
            Object key = entry.getKey();
            int i = 0;
            int hashCode = key != null ? key.hashCode() : 0;
            Object value = entry.getValue();
            if (value != null) {
                i = value.hashCode();
            }
            return hashCode ^ i;
        }

        public final String entryToString$kotlin_stdlib(Map.Entry<?, ?> entry) {
            Intrinsics.checkParameterIsNotNull(entry, "e");
            StringBuilder sb = new StringBuilder();
            sb.append(entry.getKey());
            sb.append('=');
            sb.append(entry.getValue());
            return sb.toString();
        }

        public final boolean entryEquals$kotlin_stdlib(Map.Entry<?, ?> entry, Object other) {
            Intrinsics.checkParameterIsNotNull(entry, "e");
            if ((other instanceof Map.Entry) && Intrinsics.areEqual(entry.getKey(), ((Map.Entry) other).getKey()) && Intrinsics.areEqual(entry.getValue(), ((Map.Entry) other).getValue())) {
                return true;
            }
            return false;
        }
    }
}
