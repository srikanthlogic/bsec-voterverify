package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.UnmodifiableIterator;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public abstract class Traverser<N> {

    /* loaded from: classes.dex */
    private enum Order {
        PREORDER,
        POSTORDER
    }

    public abstract Iterable<N> breadthFirst(Iterable<? extends N> iterable);

    public abstract Iterable<N> breadthFirst(N n);

    public abstract Iterable<N> depthFirstPostOrder(Iterable<? extends N> iterable);

    public abstract Iterable<N> depthFirstPostOrder(N n);

    public abstract Iterable<N> depthFirstPreOrder(Iterable<? extends N> iterable);

    public abstract Iterable<N> depthFirstPreOrder(N n);

    public static <N> Traverser<N> forGraph(SuccessorsFunction<N> graph) {
        Preconditions.checkNotNull(graph);
        return new GraphTraverser(graph);
    }

    public static <N> Traverser<N> forTree(SuccessorsFunction<N> tree) {
        Preconditions.checkNotNull(tree);
        if (tree instanceof BaseGraph) {
            Preconditions.checkArgument(((BaseGraph) tree).isDirected(), "Undirected graphs can never be trees.");
        }
        if (tree instanceof Network) {
            Preconditions.checkArgument(((Network) tree).isDirected(), "Undirected networks can never be trees.");
        }
        return new TreeTraverser(tree);
    }

    private Traverser() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class GraphTraverser<N> extends Traverser<N> {
        private final SuccessorsFunction<N> graph;

        GraphTraverser(SuccessorsFunction<N> graph) {
            super();
            this.graph = (SuccessorsFunction) Preconditions.checkNotNull(graph);
        }

        @Override // com.google.common.graph.Traverser
        public Iterable<N> breadthFirst(N startNode) {
            Preconditions.checkNotNull(startNode);
            return breadthFirst((Iterable) ImmutableSet.of(startNode));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.graph.Traverser
        public Iterable<N> breadthFirst(final Iterable<? extends N> startNodes) {
            Preconditions.checkNotNull(startNodes);
            if (Iterables.isEmpty(startNodes)) {
                return ImmutableSet.of();
            }
            Iterator<? extends N> it = startNodes.iterator();
            while (it.hasNext()) {
                checkThatNodeIsInGraph(it.next());
            }
            return new Iterable<N>() { // from class: com.google.common.graph.Traverser.GraphTraverser.1
                @Override // java.lang.Iterable
                public Iterator<N> iterator() {
                    return new BreadthFirstIterator(startNodes);
                }
            };
        }

        @Override // com.google.common.graph.Traverser
        public Iterable<N> depthFirstPreOrder(N startNode) {
            Preconditions.checkNotNull(startNode);
            return depthFirstPreOrder((Iterable) ImmutableSet.of(startNode));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.graph.Traverser
        public Iterable<N> depthFirstPreOrder(final Iterable<? extends N> startNodes) {
            Preconditions.checkNotNull(startNodes);
            if (Iterables.isEmpty(startNodes)) {
                return ImmutableSet.of();
            }
            Iterator<? extends N> it = startNodes.iterator();
            while (it.hasNext()) {
                checkThatNodeIsInGraph(it.next());
            }
            return new Iterable<N>() { // from class: com.google.common.graph.Traverser.GraphTraverser.2
                @Override // java.lang.Iterable
                public Iterator<N> iterator() {
                    return new DepthFirstIterator(startNodes, Order.PREORDER);
                }
            };
        }

        @Override // com.google.common.graph.Traverser
        public Iterable<N> depthFirstPostOrder(N startNode) {
            Preconditions.checkNotNull(startNode);
            return depthFirstPostOrder((Iterable) ImmutableSet.of(startNode));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.graph.Traverser
        public Iterable<N> depthFirstPostOrder(final Iterable<? extends N> startNodes) {
            Preconditions.checkNotNull(startNodes);
            if (Iterables.isEmpty(startNodes)) {
                return ImmutableSet.of();
            }
            Iterator<? extends N> it = startNodes.iterator();
            while (it.hasNext()) {
                checkThatNodeIsInGraph(it.next());
            }
            return new Iterable<N>() { // from class: com.google.common.graph.Traverser.GraphTraverser.3
                @Override // java.lang.Iterable
                public Iterator<N> iterator() {
                    return new DepthFirstIterator(startNodes, Order.POSTORDER);
                }
            };
        }

        private void checkThatNodeIsInGraph(N startNode) {
            this.graph.successors(startNode);
        }

        /* loaded from: classes.dex */
        private final class BreadthFirstIterator extends UnmodifiableIterator<N> {
            private final Queue<N> queue = new ArrayDeque();
            private final Set<N> visited = new HashSet();

            /* JADX WARN: Multi-variable type inference failed */
            BreadthFirstIterator(Iterable<? extends N> roots) {
                for (Object obj : roots) {
                    if (this.visited.add(obj)) {
                        this.queue.add(obj);
                    }
                }
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return !this.queue.isEmpty();
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Iterator
            public N next() {
                N current = this.queue.remove();
                for (Object obj : GraphTraverser.this.graph.successors(current)) {
                    if (this.visited.add(obj)) {
                        this.queue.add(obj);
                    }
                }
                return current;
            }
        }

        /* loaded from: classes.dex */
        private final class DepthFirstIterator extends AbstractIterator<N> {
            private final Order order;
            private final Deque<GraphTraverser<N>.DepthFirstIterator.NodeAndSuccessors> stack = new ArrayDeque();
            private final Set<N> visited = new HashSet();

            DepthFirstIterator(Iterable<? extends N> roots, Order order) {
                this.stack.push(new NodeAndSuccessors(null, roots));
                this.order = order;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.AbstractIterator
            protected N computeNext() {
                while (!this.stack.isEmpty()) {
                    GraphTraverser<N>.DepthFirstIterator.NodeAndSuccessors nodeAndSuccessors = this.stack.getFirst();
                    boolean firstVisit = this.visited.add(nodeAndSuccessors.node);
                    boolean produceNode = true;
                    boolean lastVisit = !nodeAndSuccessors.successorIterator.hasNext();
                    if ((!firstVisit || this.order != Order.PREORDER) && (!lastVisit || this.order != Order.POSTORDER)) {
                        produceNode = false;
                    }
                    if (lastVisit) {
                        this.stack.pop();
                    } else {
                        Object next = nodeAndSuccessors.successorIterator.next();
                        if (!this.visited.contains(next)) {
                            this.stack.push(withSuccessors(next));
                        }
                    }
                    if (produceNode && nodeAndSuccessors.node != null) {
                        return nodeAndSuccessors.node;
                    }
                }
                return (N) endOfData();
            }

            GraphTraverser<N>.DepthFirstIterator.NodeAndSuccessors withSuccessors(N node) {
                return new NodeAndSuccessors(node, GraphTraverser.this.graph.successors(node));
            }

            /* JADX INFO: Access modifiers changed from: private */
            /* loaded from: classes.dex */
            public final class NodeAndSuccessors {
                @NullableDecl
                final N node;
                final Iterator<? extends N> successorIterator;

                NodeAndSuccessors(@NullableDecl N node, Iterable<? extends N> successors) {
                    this.node = node;
                    this.successorIterator = successors.iterator();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class TreeTraverser<N> extends Traverser<N> {
        private final SuccessorsFunction<N> tree;

        TreeTraverser(SuccessorsFunction<N> tree) {
            super();
            this.tree = (SuccessorsFunction) Preconditions.checkNotNull(tree);
        }

        @Override // com.google.common.graph.Traverser
        public Iterable<N> breadthFirst(N startNode) {
            Preconditions.checkNotNull(startNode);
            return breadthFirst((Iterable) ImmutableSet.of(startNode));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.graph.Traverser
        public Iterable<N> breadthFirst(final Iterable<? extends N> startNodes) {
            Preconditions.checkNotNull(startNodes);
            if (Iterables.isEmpty(startNodes)) {
                return ImmutableSet.of();
            }
            Iterator<? extends N> it = startNodes.iterator();
            while (it.hasNext()) {
                checkThatNodeIsInTree(it.next());
            }
            return new Iterable<N>() { // from class: com.google.common.graph.Traverser.TreeTraverser.1
                @Override // java.lang.Iterable
                public Iterator<N> iterator() {
                    return new BreadthFirstIterator(startNodes);
                }
            };
        }

        @Override // com.google.common.graph.Traverser
        public Iterable<N> depthFirstPreOrder(N startNode) {
            Preconditions.checkNotNull(startNode);
            return depthFirstPreOrder((Iterable) ImmutableSet.of(startNode));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.graph.Traverser
        public Iterable<N> depthFirstPreOrder(final Iterable<? extends N> startNodes) {
            Preconditions.checkNotNull(startNodes);
            if (Iterables.isEmpty(startNodes)) {
                return ImmutableSet.of();
            }
            Iterator<? extends N> it = startNodes.iterator();
            while (it.hasNext()) {
                checkThatNodeIsInTree(it.next());
            }
            return new Iterable<N>() { // from class: com.google.common.graph.Traverser.TreeTraverser.2
                @Override // java.lang.Iterable
                public Iterator<N> iterator() {
                    return new DepthFirstPreOrderIterator(startNodes);
                }
            };
        }

        @Override // com.google.common.graph.Traverser
        public Iterable<N> depthFirstPostOrder(N startNode) {
            Preconditions.checkNotNull(startNode);
            return depthFirstPostOrder((Iterable) ImmutableSet.of(startNode));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.graph.Traverser
        public Iterable<N> depthFirstPostOrder(final Iterable<? extends N> startNodes) {
            Preconditions.checkNotNull(startNodes);
            if (Iterables.isEmpty(startNodes)) {
                return ImmutableSet.of();
            }
            Iterator<? extends N> it = startNodes.iterator();
            while (it.hasNext()) {
                checkThatNodeIsInTree(it.next());
            }
            return new Iterable<N>() { // from class: com.google.common.graph.Traverser.TreeTraverser.3
                @Override // java.lang.Iterable
                public Iterator<N> iterator() {
                    return new DepthFirstPostOrderIterator(startNodes);
                }
            };
        }

        private void checkThatNodeIsInTree(N startNode) {
            this.tree.successors(startNode);
        }

        /* loaded from: classes.dex */
        private final class BreadthFirstIterator extends UnmodifiableIterator<N> {
            private final Queue<N> queue = new ArrayDeque();

            /* JADX WARN: Multi-variable type inference failed */
            BreadthFirstIterator(Iterable<? extends N> roots) {
                Iterator<? extends N> it = roots.iterator();
                while (it.hasNext()) {
                    this.queue.add(it.next());
                }
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return !this.queue.isEmpty();
            }

            @Override // java.util.Iterator
            public N next() {
                N current = this.queue.remove();
                Iterables.addAll(this.queue, TreeTraverser.this.tree.successors(current));
                return current;
            }
        }

        /* loaded from: classes.dex */
        private final class DepthFirstPreOrderIterator extends UnmodifiableIterator<N> {
            private final Deque<Iterator<? extends N>> stack = new ArrayDeque();

            DepthFirstPreOrderIterator(Iterable<? extends N> roots) {
                this.stack.addLast(roots.iterator());
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return !this.stack.isEmpty();
            }

            @Override // java.util.Iterator
            public N next() {
                Iterator<? extends N> iterator = this.stack.getLast();
                N result = (N) Preconditions.checkNotNull(iterator.next());
                if (!iterator.hasNext()) {
                    this.stack.removeLast();
                }
                Iterator<? extends N> childIterator = TreeTraverser.this.tree.successors(result).iterator();
                if (childIterator.hasNext()) {
                    this.stack.addLast(childIterator);
                }
                return result;
            }
        }

        /* loaded from: classes.dex */
        private final class DepthFirstPostOrderIterator extends AbstractIterator<N> {
            private final ArrayDeque<TreeTraverser<N>.DepthFirstPostOrderIterator.NodeAndChildren> stack = new ArrayDeque<>();

            DepthFirstPostOrderIterator(Iterable<? extends N> roots) {
                this.stack.addLast(new NodeAndChildren(null, roots));
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.AbstractIterator
            protected N computeNext() {
                while (!this.stack.isEmpty()) {
                    TreeTraverser<N>.DepthFirstPostOrderIterator.NodeAndChildren top = this.stack.getLast();
                    if (top.childIterator.hasNext()) {
                        this.stack.addLast(withChildren(top.childIterator.next()));
                    } else {
                        this.stack.removeLast();
                        if (top.node != null) {
                            return top.node;
                        }
                    }
                }
                return (N) endOfData();
            }

            TreeTraverser<N>.DepthFirstPostOrderIterator.NodeAndChildren withChildren(N node) {
                return new NodeAndChildren(node, TreeTraverser.this.tree.successors(node));
            }

            /* JADX INFO: Access modifiers changed from: private */
            /* loaded from: classes.dex */
            public final class NodeAndChildren {
                final Iterator<? extends N> childIterator;
                @NullableDecl
                final N node;

                NodeAndChildren(@NullableDecl N node, Iterable<? extends N> children) {
                    this.node = node;
                    this.childIterator = children.iterator();
                }
            }
        }
    }
}
