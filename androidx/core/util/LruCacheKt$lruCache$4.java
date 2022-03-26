package androidx.core.util;

import android.util.LruCache;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: LruCache.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0001J\u0017\u0010\u0002\u001a\u0004\u0018\u00018\u00012\u0006\u0010\u0003\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010\u0004J/\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00028\u00002\u0006\u0010\t\u001a\u00028\u00012\b\u0010\n\u001a\u0004\u0018\u00018\u0001H\u0014¢\u0006\u0002\u0010\u000bJ\u001d\u0010\f\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00028\u00002\u0006\u0010\u000e\u001a\u00028\u0001H\u0014¢\u0006\u0002\u0010\u000f¨\u0006\u0010"}, d2 = {"androidx/core/util/LruCacheKt$lruCache$4", "Landroid/util/LruCache;", "create", "key", "(Ljava/lang/Object;)Ljava/lang/Object;", "entryRemoved", "", "evicted", "", "oldValue", "newValue", "(ZLjava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V", "sizeOf", "", "value", "(Ljava/lang/Object;Ljava/lang/Object;)I", "core-ktx_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class LruCacheKt$lruCache$4 extends LruCache<K, V> {
    final /* synthetic */ Function1 $create;
    final /* synthetic */ int $maxSize;
    final /* synthetic */ Function4 $onEntryRemoved;
    final /* synthetic */ Function2 $sizeOf;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public LruCacheKt$lruCache$4(Function2 $captured_local_variable$0, Function1 $captured_local_variable$1, Function4 $captured_local_variable$2, int $captured_local_variable$3, int $super_call_param$4) {
        super($super_call_param$4);
        this.$sizeOf = $captured_local_variable$0;
        this.$create = $captured_local_variable$1;
        this.$onEntryRemoved = $captured_local_variable$2;
        this.$maxSize = $captured_local_variable$3;
    }

    @Override // android.util.LruCache
    protected int sizeOf(K k, V v) {
        Intrinsics.checkParameterIsNotNull(k, "key");
        Intrinsics.checkParameterIsNotNull(v, "value");
        return ((Number) this.$sizeOf.invoke(k, v)).intValue();
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [V, java.lang.Object] */
    @Override // android.util.LruCache
    protected V create(K k) {
        Intrinsics.checkParameterIsNotNull(k, "key");
        return this.$create.invoke(k);
    }

    @Override // android.util.LruCache
    protected void entryRemoved(boolean evicted, K k, V v, V v2) {
        Intrinsics.checkParameterIsNotNull(k, "key");
        Intrinsics.checkParameterIsNotNull(v, "oldValue");
        this.$onEntryRemoved.invoke(Boolean.valueOf(evicted), k, v, v2);
    }
}
