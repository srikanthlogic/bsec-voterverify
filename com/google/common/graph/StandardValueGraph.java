package com.google.common.graph;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class StandardValueGraph<N, V> extends AbstractValueGraph<N, V> {
    private final boolean allowsSelfLoops;
    protected long edgeCount;
    private final boolean isDirected;
    protected final MapIteratorCache<N, GraphConnections<N, V>> nodeConnections;
    private final ElementOrder<N> nodeOrder;

    public StandardValueGraph(AbstractGraphBuilder<? super N> builder) {
        this(builder, builder.nodeOrder.createMap(builder.expectedNodeCount.or((Optional<Integer>) 10).intValue()), 0);
    }

    public StandardValueGraph(AbstractGraphBuilder<? super N> builder, Map<N, GraphConnections<N, V>> nodeConnections, long edgeCount) {
        this.isDirected = builder.directed;
        this.allowsSelfLoops = builder.allowsSelfLoops;
        this.nodeOrder = (ElementOrder<N>) builder.nodeOrder.cast();
        this.nodeConnections = nodeConnections instanceof TreeMap ? new MapRetrievalCache<>(nodeConnections) : new MapIteratorCache<>(nodeConnections);
        this.edgeCount = Graphs.checkNonNegative(edgeCount);
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public Set<N> nodes() {
        return this.nodeConnections.unmodifiableKeySet();
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public boolean isDirected() {
        return this.isDirected;
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public boolean allowsSelfLoops() {
        return this.allowsSelfLoops;
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public ElementOrder<N> nodeOrder() {
        return this.nodeOrder;
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public Set<N> adjacentNodes(N node) {
        return checkedConnections(node).adjacentNodes();
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.PredecessorsFunction, com.google.common.graph.Graph
    public Set<N> predecessors(N node) {
        return checkedConnections(node).predecessors();
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.SuccessorsFunction, com.google.common.graph.Graph
    public Set<N> successors(N node) {
        return checkedConnections(node).successors();
    }

    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public Set<EndpointPair<N>> incidentEdges(N node) {
        final GraphConnections<N, V> connections = checkedConnections(node);
        return new IncidentEdgeSet<N>(this, node) { // from class: com.google.common.graph.StandardValueGraph.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set, java.lang.Iterable
            public Iterator<EndpointPair<N>> iterator() {
                return connections.incidentEdgeIterator(this.node);
            }
        };
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public boolean hasEdgeConnecting(N nodeU, N nodeV) {
        return hasEdgeConnecting_internal(Preconditions.checkNotNull(nodeU), Preconditions.checkNotNull(nodeV));
    }

    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
        Preconditions.checkNotNull(endpoints);
        return isOrderingCompatible(endpoints) && hasEdgeConnecting_internal(endpoints.nodeU(), endpoints.nodeV());
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.ValueGraph
    @NullableDecl
    public V edgeValueOrDefault(N nodeU, N nodeV, @NullableDecl V defaultValue) {
        return (V) edgeValueOrDefault_internal(Preconditions.checkNotNull(nodeU), Preconditions.checkNotNull(nodeV), defaultValue);
    }

    @Override // com.google.common.graph.ValueGraph
    @NullableDecl
    public V edgeValueOrDefault(EndpointPair<N> endpoints, @NullableDecl V defaultValue) {
        validateEndpoints(endpoints);
        return edgeValueOrDefault_internal(endpoints.nodeU(), endpoints.nodeV(), defaultValue);
    }

    @Override // com.google.common.graph.AbstractBaseGraph
    protected long edgeCount() {
        return this.edgeCount;
    }

    protected final GraphConnections<N, V> checkedConnections(N node) {
        GraphConnections<N, V> connections = this.nodeConnections.get(node);
        if (connections != null) {
            return connections;
        }
        Preconditions.checkNotNull(node);
        throw new IllegalArgumentException("Node " + node + " is not an element of this graph.");
    }

    protected final boolean containsNode(@NullableDecl N node) {
        return this.nodeConnections.containsKey(node);
    }

    protected final boolean hasEdgeConnecting_internal(N nodeU, N nodeV) {
        GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
        return connectionsU != null && connectionsU.successors().contains(nodeV);
    }

    protected final V edgeValueOrDefault_internal(N nodeU, N nodeV, V defaultValue) {
        GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
        V value = connectionsU == null ? null : connectionsU.value(nodeV);
        return value == null ? defaultValue : value;
    }
}
