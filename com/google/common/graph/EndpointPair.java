package com.google.common.graph;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import com.google.errorprone.annotations.Immutable;
import java.util.Iterator;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@Immutable(containerOf = {"N"})
/* loaded from: classes.dex */
public abstract class EndpointPair<N> implements Iterable<N> {
    private final N nodeU;
    private final N nodeV;

    @Override // java.lang.Object
    public abstract boolean equals(@NullableDecl Object obj);

    @Override // java.lang.Object
    public abstract int hashCode();

    public abstract boolean isOrdered();

    public abstract N source();

    public abstract N target();

    private EndpointPair(N nodeU, N nodeV) {
        this.nodeU = (N) Preconditions.checkNotNull(nodeU);
        this.nodeV = (N) Preconditions.checkNotNull(nodeV);
    }

    public static <N> EndpointPair<N> ordered(N source, N target) {
        return new Ordered(source, target);
    }

    public static <N> EndpointPair<N> unordered(N nodeU, N nodeV) {
        return new Unordered(nodeV, nodeU);
    }

    public static <N> EndpointPair<N> of(Graph<?> graph, N nodeU, N nodeV) {
        return graph.isDirected() ? ordered(nodeU, nodeV) : unordered(nodeU, nodeV);
    }

    public static <N> EndpointPair<N> of(Network<?, ?> network, N nodeU, N nodeV) {
        return network.isDirected() ? ordered(nodeU, nodeV) : unordered(nodeU, nodeV);
    }

    public final N nodeU() {
        return this.nodeU;
    }

    public final N nodeV() {
        return this.nodeV;
    }

    public final N adjacentNode(Object node) {
        if (node.equals(this.nodeU)) {
            return this.nodeV;
        }
        if (node.equals(this.nodeV)) {
            return this.nodeU;
        }
        throw new IllegalArgumentException("EndpointPair " + this + " does not contain node " + node);
    }

    @Override // java.lang.Iterable
    public final UnmodifiableIterator<N> iterator() {
        return Iterators.forArray(this.nodeU, this.nodeV);
    }

    /* loaded from: classes.dex */
    public static final class Ordered<N> extends EndpointPair<N> {
        @Override // com.google.common.graph.EndpointPair, java.lang.Iterable
        public /* bridge */ /* synthetic */ Iterator iterator() {
            return EndpointPair.super.iterator();
        }

        private Ordered(N source, N target) {
            super(source, target);
        }

        @Override // com.google.common.graph.EndpointPair
        public N source() {
            return nodeU();
        }

        @Override // com.google.common.graph.EndpointPair
        public N target() {
            return nodeV();
        }

        @Override // com.google.common.graph.EndpointPair
        public boolean isOrdered() {
            return true;
        }

        @Override // com.google.common.graph.EndpointPair, java.lang.Object
        public boolean equals(@NullableDecl Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof EndpointPair)) {
                return false;
            }
            EndpointPair<?> other = (EndpointPair) obj;
            if (isOrdered() != other.isOrdered()) {
                return false;
            }
            if (!source().equals(other.source()) || !target().equals(other.target())) {
                return false;
            }
            return true;
        }

        @Override // com.google.common.graph.EndpointPair, java.lang.Object
        public int hashCode() {
            return Objects.hashCode(source(), target());
        }

        @Override // java.lang.Object
        public String toString() {
            return "<" + source() + " -> " + target() + ">";
        }
    }

    /* loaded from: classes.dex */
    public static final class Unordered<N> extends EndpointPair<N> {
        @Override // com.google.common.graph.EndpointPair, java.lang.Iterable
        public /* bridge */ /* synthetic */ Iterator iterator() {
            return EndpointPair.super.iterator();
        }

        private Unordered(N nodeU, N nodeV) {
            super(nodeU, nodeV);
        }

        @Override // com.google.common.graph.EndpointPair
        public N source() {
            throw new UnsupportedOperationException("Cannot call source()/target() on a EndpointPair from an undirected graph. Consider calling adjacentNode(node) if you already have a node, or nodeU()/nodeV() if you don't.");
        }

        @Override // com.google.common.graph.EndpointPair
        public N target() {
            throw new UnsupportedOperationException("Cannot call source()/target() on a EndpointPair from an undirected graph. Consider calling adjacentNode(node) if you already have a node, or nodeU()/nodeV() if you don't.");
        }

        @Override // com.google.common.graph.EndpointPair
        public boolean isOrdered() {
            return false;
        }

        @Override // com.google.common.graph.EndpointPair, java.lang.Object
        public boolean equals(@NullableDecl Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof EndpointPair)) {
                return false;
            }
            EndpointPair<?> other = (EndpointPair) obj;
            if (isOrdered() != other.isOrdered()) {
                return false;
            }
            if (nodeU().equals(other.nodeU())) {
                return nodeV().equals(other.nodeV());
            }
            if (!nodeU().equals(other.nodeV()) || !nodeV().equals(other.nodeU())) {
                return false;
            }
            return true;
        }

        @Override // com.google.common.graph.EndpointPair, java.lang.Object
        public int hashCode() {
            return nodeU().hashCode() + nodeV().hashCode();
        }

        @Override // java.lang.Object
        public String toString() {
            return "[" + nodeU() + ", " + nodeV() + "]";
        }
    }
}
