package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class StandardMutableNetwork<N, E> extends StandardNetwork<N, E> implements MutableNetwork<N, E> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public StandardMutableNetwork(NetworkBuilder<? super N, ? super E> builder) {
        super(builder);
    }

    @Override // com.google.common.graph.MutableNetwork
    public boolean addNode(N node) {
        Preconditions.checkNotNull(node, "node");
        if (containsNode(node)) {
            return false;
        }
        addNodeInternal(node);
        return true;
    }

    private NetworkConnections<N, E> addNodeInternal(N node) {
        NetworkConnections<N, E> connections = newConnections();
        Preconditions.checkState(this.nodeConnections.put(node, connections) == null);
        return connections;
    }

    @Override // com.google.common.graph.MutableNetwork
    public boolean addEdge(N nodeU, N nodeV, E edge) {
        Preconditions.checkNotNull(nodeU, "nodeU");
        Preconditions.checkNotNull(nodeV, "nodeV");
        Preconditions.checkNotNull(edge, "edge");
        boolean z = false;
        if (containsEdge(edge)) {
            EndpointPair<N> existingIncidentNodes = incidentNodes(edge);
            EndpointPair<N> newIncidentNodes = EndpointPair.of(this, nodeU, nodeV);
            Preconditions.checkArgument(existingIncidentNodes.equals(newIncidentNodes), "Edge %s already exists between the following nodes: %s, so it cannot be reused to connect the following nodes: %s.", edge, existingIncidentNodes, newIncidentNodes);
            return false;
        }
        NetworkConnections<N, E> connectionsU = (NetworkConnections) this.nodeConnections.get(nodeU);
        if (!allowsParallelEdges()) {
            if (connectionsU == null || !connectionsU.successors().contains(nodeV)) {
                z = true;
            }
            Preconditions.checkArgument(z, "Nodes %s and %s are already connected by a different edge. To construct a graph that allows parallel edges, call allowsParallelEdges(true) on the Builder.", nodeU, nodeV);
        }
        boolean isSelfLoop = nodeU.equals(nodeV);
        if (!allowsSelfLoops()) {
            Preconditions.checkArgument(!isSelfLoop, "Cannot add self-loop edge on node %s, as self-loops are not allowed. To construct a graph that allows self-loops, call allowsSelfLoops(true) on the Builder.", nodeU);
        }
        if (connectionsU == null) {
            connectionsU = addNodeInternal(nodeU);
        }
        connectionsU.addOutEdge(edge, nodeV);
        NetworkConnections<N, E> connectionsV = (NetworkConnections) this.nodeConnections.get(nodeV);
        if (connectionsV == null) {
            connectionsV = addNodeInternal(nodeV);
        }
        connectionsV.addInEdge(edge, nodeU, isSelfLoop);
        this.edgeToReferenceNode.put(edge, nodeU);
        return true;
    }

    @Override // com.google.common.graph.MutableNetwork
    public boolean addEdge(EndpointPair<N> endpoints, E edge) {
        validateEndpoints(endpoints);
        return addEdge(endpoints.nodeU(), endpoints.nodeV(), edge);
    }

    @Override // com.google.common.graph.MutableNetwork
    public boolean removeNode(N node) {
        Preconditions.checkNotNull(node, "node");
        NetworkConnections<N, E> connections = (NetworkConnections) this.nodeConnections.get(node);
        if (connections == null) {
            return false;
        }
        UnmodifiableIterator<E> it = ImmutableList.copyOf((Collection) connections.incidentEdges()).iterator();
        while (it.hasNext()) {
            removeEdge(it.next());
        }
        this.nodeConnections.remove(node);
        return true;
    }

    @Override // com.google.common.graph.MutableNetwork
    public boolean removeEdge(E edge) {
        Preconditions.checkNotNull(edge, "edge");
        Object obj = this.edgeToReferenceNode.get(edge);
        boolean z = false;
        if (obj == null) {
            return false;
        }
        NetworkConnections<N, E> connectionsU = (NetworkConnections) this.nodeConnections.get(obj);
        N nodeV = connectionsU.adjacentNode(edge);
        NetworkConnections<N, E> connectionsV = (NetworkConnections) this.nodeConnections.get(nodeV);
        connectionsU.removeOutEdge(edge);
        if (allowsSelfLoops() && obj.equals(nodeV)) {
            z = true;
        }
        connectionsV.removeInEdge(edge, z);
        this.edgeToReferenceNode.remove(edge);
        return true;
    }

    private NetworkConnections<N, E> newConnections() {
        if (isDirected()) {
            if (allowsParallelEdges()) {
                return DirectedMultiNetworkConnections.of();
            }
            return DirectedNetworkConnections.of();
        } else if (allowsParallelEdges()) {
            return UndirectedMultiNetworkConnections.of();
        } else {
            return UndirectedNetworkConnections.of();
        }
    }
}
