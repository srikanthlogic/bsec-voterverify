package com.google.common.util.concurrent;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AggregateFuture;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes3.dex */
abstract class CollectionFuture<V, C> extends AggregateFuture<V, C> {
    private List<Present<V>> values;

    abstract C combine(List<Present<V>> list);

    CollectionFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed) {
        super(futures, allMustSucceed, true);
        List<Present<V>> list;
        if (futures.isEmpty()) {
            list = ImmutableList.of();
        } else {
            list = Lists.newArrayListWithCapacity(futures.size());
        }
        List<Present<V>> values = list;
        for (int i = 0; i < futures.size(); i++) {
            values.add(null);
        }
        this.values = values;
    }

    @Override // com.google.common.util.concurrent.AggregateFuture
    final void collectOneValue(int index, @NullableDecl V returnValue) {
        List<Present<V>> localValues = this.values;
        if (localValues != null) {
            localValues.set(index, new Present<>(returnValue));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.util.concurrent.AggregateFuture
    final void handleAllCompleted() {
        List<Present<V>> localValues = this.values;
        if (localValues != null) {
            set(combine(localValues));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.util.concurrent.AggregateFuture
    public void releaseResources(AggregateFuture.ReleaseResourcesReason reason) {
        super.releaseResources(reason);
        this.values = null;
    }

    /* loaded from: classes3.dex */
    static final class ListFuture<V> extends CollectionFuture<V, List<V>> {
        /* JADX INFO: Access modifiers changed from: package-private */
        public ListFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed) {
            super(futures, allMustSucceed);
            init();
        }

        @Override // com.google.common.util.concurrent.CollectionFuture
        public List<V> combine(List<Present<V>> values) {
            List<V> result = Lists.newArrayListWithCapacity(values.size());
            Iterator<Present<V>> it = values.iterator();
            while (it.hasNext()) {
                Present<V> element = it.next();
                result.add(element != null ? element.value : null);
            }
            return Collections.unmodifiableList(result);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class Present<V> {
        V value;

        Present(V value) {
            this.value = value;
        }
    }
}
