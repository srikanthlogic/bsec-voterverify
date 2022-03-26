package com.google.common.graph;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.graph.ElementOrder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
final class UndirectedGraphConnections<N, V> implements GraphConnections<N, V> {
    private final Map<N, V> adjacentNodeValues;

    private UndirectedGraphConnections(Map<N, V> adjacentNodeValues) {
        this.adjacentNodeValues = (Map) Preconditions.checkNotNull(adjacentNodeValues);
    }

    /* renamed from: com.google.common.graph.UndirectedGraphConnections$2  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$google$common$graph$ElementOrder$Type = new int[ElementOrder.Type.values().length];

        static {
            try {
                $SwitchMap$com$google$common$graph$ElementOrder$Type[ElementOrder.Type.UNORDERED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$google$common$graph$ElementOrder$Type[ElementOrder.Type.STABLE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <N, V> UndirectedGraphConnections<N, V> of(ElementOrder<N> incidentEdgeOrder) {
        int i = AnonymousClass2.$SwitchMap$com$google$common$graph$ElementOrder$Type[incidentEdgeOrder.type().ordinal()];
        if (i == 1) {
            return new UndirectedGraphConnections<>(new HashMap(2, 1.0f));
        }
        if (i == 2) {
            return new UndirectedGraphConnections<>(new LinkedHashMap(2, 1.0f));
        }
        throw new AssertionError(incidentEdgeOrder.type());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <N, V> UndirectedGraphConnections<N, V> ofImmutable(Map<N, V> adjacentNodeValues) {
        return new UndirectedGraphConnections<>(ImmutableMap.copyOf(adjacentNodeValues));
    }

    @Override // com.google.common.graph.GraphConnections
    public Set<N> adjacentNodes() {
        return Collections.unmodifiableSet(this.adjacentNodeValues.keySet());
    }

    @Override // com.google.common.graph.GraphConnections
    public Set<N> predecessors() {
        return adjacentNodes();
    }

    @Override // com.google.common.graph.GraphConnections
    public Set<N> successors() {
        return adjacentNodes();
    }

    @Override // com.google.common.graph.GraphConnections
    public Iterator<EndpointPair<N>> incidentEdgeIterator(final N thisNode) {
        return Iterators.transform(this.adjacentNodeValues.keySet().iterator(), new Function<N, EndpointPair<N>>() { // from class: com.google.common.graph.UndirectedGraphConnections.1
            @Override // com.google.common.base.Function
            public EndpointPair<N> apply(N incidentNode) {
                return EndpointPair.unordered(thisNode, incidentNode);
            }
        });
    }

    @Override // com.google.common.graph.GraphConnections
    public V value(N node) {
        return this.adjacentNodeValues.get(node);
    }

    @Override // com.google.common.graph.GraphConnections
    public void removePredecessor(N node) {
        removeSuccessor(node);
    }

    @Override // com.google.common.graph.GraphConnections
    public V removeSuccessor(N node) {
        return this.adjacentNodeValues.remove(node);
    }

    @Override // com.google.common.graph.GraphConnections
    public void addPredecessor(N node, V value) {
        addSuccessor(node, value);
    }

    @Override // com.google.common.graph.GraphConnections
    public V addSuccessor(N node, V value) {
        return this.adjacentNodeValues.put(node, value);
    }
}
