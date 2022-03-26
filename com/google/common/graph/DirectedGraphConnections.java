package com.google.common.graph;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.graph.ElementOrder;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public final class DirectedGraphConnections<N, V> implements GraphConnections<N, V> {
    private static final Object PRED = new Object();
    private final Map<N, Object> adjacentNodeValues;
    @NullableDecl
    private final List<NodeConnection<N>> orderedNodeConnections;
    private int predecessorCount;
    private int successorCount;

    /* loaded from: classes.dex */
    public static final class PredAndSucc {
        private final Object successorValue;

        PredAndSucc(Object successorValue) {
            this.successorValue = successorValue;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class NodeConnection<N> {
        final N node;

        NodeConnection(N node) {
            this.node = (N) Preconditions.checkNotNull(node);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public static final class Pred<N> extends NodeConnection<N> {
            Pred(N node) {
                super(node);
            }

            public boolean equals(Object that) {
                if (that instanceof Pred) {
                    return this.node.equals(((Pred) that).node);
                }
                return false;
            }

            public int hashCode() {
                return Pred.class.hashCode() + this.node.hashCode();
            }
        }

        /* loaded from: classes.dex */
        public static final class Succ<N> extends NodeConnection<N> {
            Succ(N node) {
                super(node);
            }

            public boolean equals(Object that) {
                if (that instanceof Succ) {
                    return this.node.equals(((Succ) that).node);
                }
                return false;
            }

            public int hashCode() {
                return Succ.class.hashCode() + this.node.hashCode();
            }
        }
    }

    private DirectedGraphConnections(Map<N, Object> adjacentNodeValues, @NullableDecl List<NodeConnection<N>> orderedNodeConnections, int predecessorCount, int successorCount) {
        this.adjacentNodeValues = (Map) Preconditions.checkNotNull(adjacentNodeValues);
        this.orderedNodeConnections = orderedNodeConnections;
        this.predecessorCount = Graphs.checkNonNegative(predecessorCount);
        this.successorCount = Graphs.checkNonNegative(successorCount);
        Preconditions.checkState(predecessorCount <= adjacentNodeValues.size() && successorCount <= adjacentNodeValues.size());
    }

    public static <N, V> DirectedGraphConnections<N, V> of(ElementOrder<N> incidentEdgeOrder) {
        List<NodeConnection<N>> orderedNodeConnections;
        int i = AnonymousClass8.$SwitchMap$com$google$common$graph$ElementOrder$Type[incidentEdgeOrder.type().ordinal()];
        if (i == 1) {
            orderedNodeConnections = null;
        } else if (i == 2) {
            orderedNodeConnections = new ArrayList<>();
        } else {
            throw new AssertionError(incidentEdgeOrder.type());
        }
        return new DirectedGraphConnections<>(new HashMap(4, 1.0f), orderedNodeConnections, 0, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.common.graph.DirectedGraphConnections$8 */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass8 {
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

    /* JADX WARN: Multi-variable type inference failed */
    public static <N, V> DirectedGraphConnections<N, V> ofImmutable(N thisNode, Iterable<EndpointPair<N>> incidentEdges, Function<N, V> successorNodeToValueFn) {
        Preconditions.checkNotNull(thisNode);
        Preconditions.checkNotNull(successorNodeToValueFn);
        Map<N, Object> adjacentNodeValues = new HashMap<>();
        ImmutableList.Builder<NodeConnection<N>> orderedNodeConnectionsBuilder = ImmutableList.builder();
        int predecessorCount = 0;
        int successorCount = 0;
        for (EndpointPair<N> incidentEdge : incidentEdges) {
            if (incidentEdge.nodeU().equals(thisNode) && incidentEdge.nodeV().equals(thisNode)) {
                adjacentNodeValues.put(thisNode, new PredAndSucc(successorNodeToValueFn.apply(thisNode)));
                orderedNodeConnectionsBuilder.add((ImmutableList.Builder<NodeConnection<N>>) new NodeConnection.Pred<>(thisNode));
                orderedNodeConnectionsBuilder.add((ImmutableList.Builder<NodeConnection<N>>) new NodeConnection.Succ<>(thisNode));
                predecessorCount++;
                successorCount++;
            } else if (incidentEdge.nodeV().equals(thisNode)) {
                N predecessor = incidentEdge.nodeU();
                Object existingValue = adjacentNodeValues.put(predecessor, PRED);
                if (existingValue != null) {
                    adjacentNodeValues.put(predecessor, new PredAndSucc(existingValue));
                }
                orderedNodeConnectionsBuilder.add((ImmutableList.Builder<NodeConnection<N>>) new NodeConnection.Pred<>(predecessor));
                predecessorCount++;
            } else {
                Preconditions.checkArgument(incidentEdge.nodeU().equals(thisNode));
                N successor = incidentEdge.nodeV();
                V value = successorNodeToValueFn.apply(successor);
                Object existingValue2 = adjacentNodeValues.put(successor, value);
                if (existingValue2 != null) {
                    Preconditions.checkArgument(existingValue2 == PRED);
                    adjacentNodeValues.put(successor, new PredAndSucc(value));
                }
                orderedNodeConnectionsBuilder.add((ImmutableList.Builder<NodeConnection<N>>) new NodeConnection.Succ<>(successor));
                successorCount++;
            }
        }
        return new DirectedGraphConnections<>(adjacentNodeValues, orderedNodeConnectionsBuilder.build(), predecessorCount, successorCount);
    }

    @Override // com.google.common.graph.GraphConnections
    public Set<N> adjacentNodes() {
        if (this.orderedNodeConnections == null) {
            return Collections.unmodifiableSet(this.adjacentNodeValues.keySet());
        }
        return new AbstractSet<N>() { // from class: com.google.common.graph.DirectedGraphConnections.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set, java.lang.Iterable
            public UnmodifiableIterator<N> iterator() {
                final Iterator<NodeConnection<N>> nodeConnections = DirectedGraphConnections.this.orderedNodeConnections.iterator();
                final Set<N> seenNodes = new HashSet<>();
                return new AbstractIterator<N>() { // from class: com.google.common.graph.DirectedGraphConnections.1.1
                    @Override // com.google.common.collect.AbstractIterator
                    protected N computeNext() {
                        while (nodeConnections.hasNext()) {
                            NodeConnection<N> nodeConnection = (NodeConnection) nodeConnections.next();
                            if (seenNodes.add(nodeConnection.node)) {
                                return nodeConnection.node;
                            }
                        }
                        return endOfData();
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return DirectedGraphConnections.this.adjacentNodeValues.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(@NullableDecl Object obj) {
                return DirectedGraphConnections.this.adjacentNodeValues.containsKey(obj);
            }
        };
    }

    @Override // com.google.common.graph.GraphConnections
    public Set<N> predecessors() {
        return new AbstractSet<N>() { // from class: com.google.common.graph.DirectedGraphConnections.2
            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set, java.lang.Iterable
            public UnmodifiableIterator<N> iterator() {
                if (DirectedGraphConnections.this.orderedNodeConnections == null) {
                    final Iterator<Map.Entry<N, Object>> entries = DirectedGraphConnections.this.adjacentNodeValues.entrySet().iterator();
                    return new AbstractIterator<N>() { // from class: com.google.common.graph.DirectedGraphConnections.2.1
                        @Override // com.google.common.collect.AbstractIterator
                        protected N computeNext() {
                            while (entries.hasNext()) {
                                Map.Entry<N, Object> entry = (Map.Entry) entries.next();
                                if (DirectedGraphConnections.isPredecessor(entry.getValue())) {
                                    return entry.getKey();
                                }
                            }
                            return endOfData();
                        }
                    };
                }
                final Iterator<NodeConnection<N>> nodeConnections = DirectedGraphConnections.this.orderedNodeConnections.iterator();
                return new AbstractIterator<N>() { // from class: com.google.common.graph.DirectedGraphConnections.2.2
                    @Override // com.google.common.collect.AbstractIterator
                    protected N computeNext() {
                        while (nodeConnections.hasNext()) {
                            NodeConnection<N> nodeConnection = (NodeConnection) nodeConnections.next();
                            if (nodeConnection instanceof NodeConnection.Pred) {
                                return nodeConnection.node;
                            }
                        }
                        return endOfData();
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return DirectedGraphConnections.this.predecessorCount;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(@NullableDecl Object obj) {
                return DirectedGraphConnections.isPredecessor(DirectedGraphConnections.this.adjacentNodeValues.get(obj));
            }
        };
    }

    @Override // com.google.common.graph.GraphConnections
    public Set<N> successors() {
        return new AbstractSet<N>() { // from class: com.google.common.graph.DirectedGraphConnections.3
            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set, java.lang.Iterable
            public UnmodifiableIterator<N> iterator() {
                if (DirectedGraphConnections.this.orderedNodeConnections == null) {
                    final Iterator<Map.Entry<N, Object>> entries = DirectedGraphConnections.this.adjacentNodeValues.entrySet().iterator();
                    return new AbstractIterator<N>() { // from class: com.google.common.graph.DirectedGraphConnections.3.1
                        @Override // com.google.common.collect.AbstractIterator
                        protected N computeNext() {
                            while (entries.hasNext()) {
                                Map.Entry<N, Object> entry = (Map.Entry) entries.next();
                                if (DirectedGraphConnections.isSuccessor(entry.getValue())) {
                                    return entry.getKey();
                                }
                            }
                            return endOfData();
                        }
                    };
                }
                final Iterator<NodeConnection<N>> nodeConnections = DirectedGraphConnections.this.orderedNodeConnections.iterator();
                return new AbstractIterator<N>() { // from class: com.google.common.graph.DirectedGraphConnections.3.2
                    @Override // com.google.common.collect.AbstractIterator
                    protected N computeNext() {
                        while (nodeConnections.hasNext()) {
                            NodeConnection<N> nodeConnection = (NodeConnection) nodeConnections.next();
                            if (nodeConnection instanceof NodeConnection.Succ) {
                                return nodeConnection.node;
                            }
                        }
                        return endOfData();
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return DirectedGraphConnections.this.successorCount;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(@NullableDecl Object obj) {
                return DirectedGraphConnections.isSuccessor(DirectedGraphConnections.this.adjacentNodeValues.get(obj));
            }
        };
    }

    @Override // com.google.common.graph.GraphConnections
    public Iterator<EndpointPair<N>> incidentEdgeIterator(final N thisNode) {
        final Iterator<EndpointPair<N>> resultWithDoubleSelfLoop;
        Preconditions.checkNotNull(thisNode);
        List<NodeConnection<N>> list = this.orderedNodeConnections;
        if (list == null) {
            resultWithDoubleSelfLoop = Iterators.concat(Iterators.transform(predecessors().iterator(), new Function<N, EndpointPair<N>>() { // from class: com.google.common.graph.DirectedGraphConnections.4
                @Override // com.google.common.base.Function
                public EndpointPair<N> apply(N predecessor) {
                    return EndpointPair.ordered(predecessor, thisNode);
                }
            }), Iterators.transform(successors().iterator(), new Function<N, EndpointPair<N>>() { // from class: com.google.common.graph.DirectedGraphConnections.5
                @Override // com.google.common.base.Function
                public EndpointPair<N> apply(N successor) {
                    return EndpointPair.ordered(thisNode, successor);
                }
            }));
        } else {
            resultWithDoubleSelfLoop = Iterators.transform(list.iterator(), new Function<NodeConnection<N>, EndpointPair<N>>() { // from class: com.google.common.graph.DirectedGraphConnections.6
                @Override // com.google.common.base.Function
                public /* bridge */ /* synthetic */ Object apply(Object obj) {
                    return apply((NodeConnection) ((NodeConnection) obj));
                }

                public EndpointPair<N> apply(NodeConnection<N> connection) {
                    if (connection instanceof NodeConnection.Succ) {
                        return EndpointPair.ordered(thisNode, connection.node);
                    }
                    return EndpointPair.ordered(connection.node, thisNode);
                }
            });
        }
        final AtomicBoolean alreadySeenSelfLoop = new AtomicBoolean(false);
        return new AbstractIterator<EndpointPair<N>>() { // from class: com.google.common.graph.DirectedGraphConnections.7
            @Override // com.google.common.collect.AbstractIterator
            public EndpointPair<N> computeNext() {
                while (resultWithDoubleSelfLoop.hasNext()) {
                    EndpointPair<N> edge = (EndpointPair) resultWithDoubleSelfLoop.next();
                    if (!edge.nodeU().equals(edge.nodeV())) {
                        return edge;
                    }
                    if (!alreadySeenSelfLoop.getAndSet(true)) {
                        return edge;
                    }
                }
                return endOfData();
            }
        };
    }

    @Override // com.google.common.graph.GraphConnections
    public V value(N node) {
        Preconditions.checkNotNull(node);
        V v = (V) this.adjacentNodeValues.get(node);
        if (v == PRED) {
            return null;
        }
        if (v instanceof PredAndSucc) {
            return (V) ((PredAndSucc) v).successorValue;
        }
        return v;
    }

    @Override // com.google.common.graph.GraphConnections
    public void removePredecessor(N node) {
        boolean removedPredecessor;
        Preconditions.checkNotNull(node);
        Object previousValue = this.adjacentNodeValues.get(node);
        if (previousValue == PRED) {
            this.adjacentNodeValues.remove(node);
            removedPredecessor = true;
        } else if (previousValue instanceof PredAndSucc) {
            this.adjacentNodeValues.put(node, ((PredAndSucc) previousValue).successorValue);
            removedPredecessor = true;
        } else {
            removedPredecessor = false;
        }
        if (removedPredecessor) {
            int i = this.predecessorCount - 1;
            this.predecessorCount = i;
            Graphs.checkNonNegative(i);
            List<NodeConnection<N>> list = this.orderedNodeConnections;
            if (list != null) {
                list.remove(new NodeConnection.Pred(node));
            }
        }
    }

    @Override // com.google.common.graph.GraphConnections
    public V removeSuccessor(Object node) {
        Object removedValue;
        Object obj;
        Preconditions.checkNotNull(node);
        Object previousValue = this.adjacentNodeValues.get(node);
        if (previousValue == null || previousValue == (obj = PRED)) {
            removedValue = (V) null;
        } else if (previousValue instanceof PredAndSucc) {
            this.adjacentNodeValues.put(node, obj);
            removedValue = (V) ((PredAndSucc) previousValue).successorValue;
        } else {
            this.adjacentNodeValues.remove(node);
            removedValue = (V) previousValue;
        }
        if (removedValue != null) {
            int i = this.successorCount - 1;
            this.successorCount = i;
            Graphs.checkNonNegative(i);
            List<NodeConnection<N>> list = this.orderedNodeConnections;
            if (list != null) {
                list.remove(new NodeConnection.Succ(node));
            }
        }
        return (V) removedValue;
    }

    @Override // com.google.common.graph.GraphConnections
    public void addPredecessor(N node, V unused) {
        boolean addedPredecessor;
        Object previousValue = this.adjacentNodeValues.put(node, PRED);
        if (previousValue == null) {
            addedPredecessor = true;
        } else if (previousValue instanceof PredAndSucc) {
            this.adjacentNodeValues.put(node, previousValue);
            addedPredecessor = false;
        } else if (previousValue != PRED) {
            this.adjacentNodeValues.put(node, new PredAndSucc(previousValue));
            addedPredecessor = true;
        } else {
            addedPredecessor = false;
        }
        if (addedPredecessor) {
            int i = this.predecessorCount + 1;
            this.predecessorCount = i;
            Graphs.checkPositive(i);
            List<NodeConnection<N>> list = this.orderedNodeConnections;
            if (list != null) {
                list.add(new NodeConnection.Pred(node));
            }
        }
    }

    @Override // com.google.common.graph.GraphConnections
    public V addSuccessor(N node, V value) {
        Object previousSuccessor;
        Object previousValue = this.adjacentNodeValues.put(node, value);
        if (previousValue == null) {
            previousSuccessor = (V) null;
        } else if (previousValue instanceof PredAndSucc) {
            this.adjacentNodeValues.put(node, new PredAndSucc(value));
            previousSuccessor = (V) ((PredAndSucc) previousValue).successorValue;
        } else if (previousValue == PRED) {
            this.adjacentNodeValues.put(node, new PredAndSucc(value));
            previousSuccessor = (V) null;
        } else {
            previousSuccessor = (V) previousValue;
        }
        if (previousSuccessor == null) {
            int i = this.successorCount + 1;
            this.successorCount = i;
            Graphs.checkPositive(i);
            List<NodeConnection<N>> list = this.orderedNodeConnections;
            if (list != null) {
                list.add(new NodeConnection.Succ(node));
            }
        }
        return (V) previousSuccessor;
    }

    public static boolean isPredecessor(@NullableDecl Object value) {
        return value == PRED || (value instanceof PredAndSucc);
    }

    public static boolean isSuccessor(@NullableDecl Object value) {
        return (value == PRED || value == null) ? false : true;
    }
}
