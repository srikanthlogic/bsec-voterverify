package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Queue;
@Deprecated
/* loaded from: classes.dex */
public abstract class TreeTraverser<T> {
    public abstract Iterable<T> children(T t);

    @Deprecated
    public static <T> TreeTraverser<T> using(final Function<T, ? extends Iterable<T>> nodeToChildrenFunction) {
        Preconditions.checkNotNull(nodeToChildrenFunction);
        return new TreeTraverser<T>() { // from class: com.google.common.collect.TreeTraverser.1
            @Override // com.google.common.collect.TreeTraverser
            public Iterable<T> children(T root) {
                return (Iterable) Function.this.apply(root);
            }
        };
    }

    @Deprecated
    public final FluentIterable<T> preOrderTraversal(final T root) {
        Preconditions.checkNotNull(root);
        return new FluentIterable<T>() { // from class: com.google.common.collect.TreeTraverser.2
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.lang.Iterable
            public UnmodifiableIterator<T> iterator() {
                return TreeTraverser.this.preOrderIterator(root);
            }
        };
    }

    UnmodifiableIterator<T> preOrderIterator(T root) {
        return new PreOrderIterator(root);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class PreOrderIterator extends UnmodifiableIterator<T> {
        private final Deque<Iterator<T>> stack = new ArrayDeque();

        PreOrderIterator(T root) {
            this.stack.addLast(Iterators.singletonIterator(Preconditions.checkNotNull(root)));
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return !this.stack.isEmpty();
        }

        @Override // java.util.Iterator
        public T next() {
            Iterator<T> itr = this.stack.getLast();
            T result = (T) Preconditions.checkNotNull(itr.next());
            if (!itr.hasNext()) {
                this.stack.removeLast();
            }
            Iterator<T> childItr = TreeTraverser.this.children(result).iterator();
            if (childItr.hasNext()) {
                this.stack.addLast(childItr);
            }
            return result;
        }
    }

    @Deprecated
    public final FluentIterable<T> postOrderTraversal(final T root) {
        Preconditions.checkNotNull(root);
        return new FluentIterable<T>() { // from class: com.google.common.collect.TreeTraverser.3
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.lang.Iterable
            public UnmodifiableIterator<T> iterator() {
                return TreeTraverser.this.postOrderIterator(root);
            }
        };
    }

    UnmodifiableIterator<T> postOrderIterator(T root) {
        return new PostOrderIterator(root);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class PostOrderNode<T> {
        final Iterator<T> childIterator;
        final T root;

        PostOrderNode(T root, Iterator<T> childIterator) {
            this.root = (T) Preconditions.checkNotNull(root);
            this.childIterator = (Iterator) Preconditions.checkNotNull(childIterator);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class PostOrderIterator extends AbstractIterator<T> {
        private final ArrayDeque<PostOrderNode<T>> stack = new ArrayDeque<>();

        PostOrderIterator(T root) {
            this.stack.addLast(expand(root));
        }

        @Override // com.google.common.collect.AbstractIterator
        protected T computeNext() {
            while (!this.stack.isEmpty()) {
                PostOrderNode<T> top = this.stack.getLast();
                if (top.childIterator.hasNext()) {
                    this.stack.addLast(expand(top.childIterator.next()));
                } else {
                    this.stack.removeLast();
                    return top.root;
                }
            }
            return endOfData();
        }

        private PostOrderNode<T> expand(T t) {
            return new PostOrderNode<>(t, TreeTraverser.this.children(t).iterator());
        }
    }

    @Deprecated
    public final FluentIterable<T> breadthFirstTraversal(final T root) {
        Preconditions.checkNotNull(root);
        return new FluentIterable<T>() { // from class: com.google.common.collect.TreeTraverser.4
            @Override // java.lang.Iterable
            public UnmodifiableIterator<T> iterator() {
                return new BreadthFirstIterator(root);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class BreadthFirstIterator extends UnmodifiableIterator<T> implements PeekingIterator<T> {
        private final Queue<T> queue = new ArrayDeque();

        BreadthFirstIterator(T root) {
            this.queue.add(root);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return !this.queue.isEmpty();
        }

        @Override // com.google.common.collect.PeekingIterator
        public T peek() {
            return this.queue.element();
        }

        @Override // java.util.Iterator, com.google.common.collect.PeekingIterator
        public T next() {
            T result = this.queue.remove();
            Iterables.addAll(this.queue, TreeTraverser.this.children(result));
            return result;
        }
    }
}
