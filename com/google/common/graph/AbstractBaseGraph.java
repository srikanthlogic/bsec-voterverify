package com.google.common.graph;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import java.util.AbstractSet;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public abstract class AbstractBaseGraph<N> implements BaseGraph<N> {
    protected long edgeCount() {
        long degreeSum = 0;
        for (N node : nodes()) {
            degreeSum += (long) degree(node);
        }
        Preconditions.checkState((1 & degreeSum) == 0);
        return degreeSum >>> 1;
    }

    @Override // com.google.common.graph.BaseGraph
    public Set<EndpointPair<N>> edges() {
        return new AbstractSet<EndpointPair<N>>() { // from class: com.google.common.graph.AbstractBaseGraph.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set, java.lang.Iterable
            public UnmodifiableIterator<EndpointPair<N>> iterator() {
                return EndpointPairIterator.of(AbstractBaseGraph.this);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return Ints.saturatedCast(AbstractBaseGraph.this.edgeCount());
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(Object o) {
                throw new UnsupportedOperationException();
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(@NullableDecl Object obj) {
                if (!(obj instanceof EndpointPair)) {
                    return false;
                }
                EndpointPair<?> endpointPair = (EndpointPair) obj;
                if (!AbstractBaseGraph.this.isOrderingCompatible(endpointPair) || !AbstractBaseGraph.this.nodes().contains(endpointPair.nodeU()) || !AbstractBaseGraph.this.successors((AbstractBaseGraph) endpointPair.nodeU()).contains(endpointPair.nodeV())) {
                    return false;
                }
                return true;
            }
        };
    }

    @Override // com.google.common.graph.BaseGraph
    public ElementOrder<N> incidentEdgeOrder() {
        return ElementOrder.unordered();
    }

    @Override // com.google.common.graph.BaseGraph
    public Set<EndpointPair<N>> incidentEdges(N node) {
        Preconditions.checkNotNull(node);
        Preconditions.checkArgument(nodes().contains(node), "Node %s is not an element of this graph.", node);
        return new IncidentEdgeSet<N>(this, node) { // from class: com.google.common.graph.AbstractBaseGraph.2
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set, java.lang.Iterable
            public UnmodifiableIterator<EndpointPair<N>> iterator() {
                if (this.graph.isDirected()) {
                    return Iterators.unmodifiableIterator(Iterators.concat(Iterators.transform(this.graph.predecessors((BaseGraph) this.node).iterator(), new Function<N, EndpointPair<N>>() { // from class: com.google.common.graph.AbstractBaseGraph.2.1
                        @Override // com.google.common.base.Function
                        public EndpointPair<N> apply(N predecessor) {
                            return EndpointPair.ordered(predecessor, AnonymousClass2.this.node);
                        }
                    }), Iterators.transform(Sets.difference(this.graph.successors((BaseGraph) this.node), ImmutableSet.of(this.node)).iterator(), new Function<N, EndpointPair<N>>() { // from class: com.google.common.graph.AbstractBaseGraph.2.2
                        @Override // com.google.common.base.Function
                        public EndpointPair<N> apply(N successor) {
                            return EndpointPair.ordered(AnonymousClass2.this.node, successor);
                        }
                    })));
                }
                return Iterators.unmodifiableIterator(Iterators.transform(this.graph.adjacentNodes(this.node).iterator(), new Function<N, EndpointPair<N>>() { // from class: com.google.common.graph.AbstractBaseGraph.2.3
                    @Override // com.google.common.base.Function
                    public EndpointPair<N> apply(N adjacentNode) {
                        return EndpointPair.unordered(AnonymousClass2.this.node, adjacentNode);
                    }
                }));
            }
        };
    }

    @Override // com.google.common.graph.BaseGraph
    public int degree(N node) {
        if (isDirected()) {
            return IntMath.saturatedAdd(predecessors((AbstractBaseGraph<N>) node).size(), successors((AbstractBaseGraph<N>) node).size());
        }
        Set<N> neighbors = adjacentNodes(node);
        return IntMath.saturatedAdd(neighbors.size(), (!allowsSelfLoops() || !neighbors.contains(node)) ? 0 : 1);
    }

    @Override // com.google.common.graph.BaseGraph
    public int inDegree(N node) {
        return isDirected() ? predecessors((AbstractBaseGraph<N>) node).size() : degree(node);
    }

    @Override // com.google.common.graph.BaseGraph
    public int outDegree(N node) {
        return isDirected() ? successors((AbstractBaseGraph<N>) node).size() : degree(node);
    }

    @Override // com.google.common.graph.BaseGraph
    public boolean hasEdgeConnecting(N nodeU, N nodeV) {
        Preconditions.checkNotNull(nodeU);
        Preconditions.checkNotNull(nodeV);
        return nodes().contains(nodeU) && successors((AbstractBaseGraph<N>) nodeU).contains(nodeV);
    }

    @Override // com.google.common.graph.BaseGraph
    public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
        Preconditions.checkNotNull(endpoints);
        if (!isOrderingCompatible(endpoints)) {
            return false;
        }
        N nodeU = endpoints.nodeU();
        N nodeV = endpoints.nodeV();
        if (!nodes().contains(nodeU) || !successors((AbstractBaseGraph<N>) nodeU).contains(nodeV)) {
            return false;
        }
        return true;
    }

    protected final void validateEndpoints(EndpointPair<?> endpoints) {
        Preconditions.checkNotNull(endpoints);
        Preconditions.checkArgument(isOrderingCompatible(endpoints), "Mismatch: unordered endpoints cannot be used with directed graphs");
    }

    protected final boolean isOrderingCompatible(EndpointPair<?> endpoints) {
        return endpoints.isOrdered() || !isDirected();
    }
}
