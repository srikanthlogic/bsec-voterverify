package com.google.common.graph;

import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public interface BaseGraph<N> extends SuccessorsFunction<N>, PredecessorsFunction<N> {
    @Override // com.google.common.graph.Graph
    Set<N> adjacentNodes(N n);

    @Override // com.google.common.graph.Graph
    boolean allowsSelfLoops();

    int degree(N n);

    Set<EndpointPair<N>> edges();

    boolean hasEdgeConnecting(EndpointPair<N> endpointPair);

    boolean hasEdgeConnecting(N n, N n2);

    int inDegree(N n);

    ElementOrder<N> incidentEdgeOrder();

    Set<EndpointPair<N>> incidentEdges(N n);

    @Override // com.google.common.graph.Graph
    boolean isDirected();

    @Override // com.google.common.graph.Graph
    ElementOrder<N> nodeOrder();

    @Override // com.google.common.graph.Graph
    Set<N> nodes();

    int outDegree(N n);

    @Override // com.google.common.graph.PredecessorsFunction, com.google.common.graph.Graph
    Set<N> predecessors(N n);

    @Override // com.google.common.graph.SuccessorsFunction, com.google.common.graph.Graph
    Set<N> successors(N n);
}
