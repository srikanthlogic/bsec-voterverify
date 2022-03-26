package com.google.common.graph;

import com.google.errorprone.annotations.DoNotMock;
@DoNotMock("Implement with a lambda, or use GraphBuilder to build a Graph with the desired edges")
/* loaded from: classes.dex */
public interface PredecessorsFunction<N> {
    @Override // com.google.common.graph.Graph
    Iterable<? extends N> predecessors(N n);
}
