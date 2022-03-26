package com.google.common.base;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
abstract class AbstractIterator<T> implements Iterator<T> {
    @NullableDecl
    private T next;
    private State state = State.NOT_READY;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum State {
        READY,
        NOT_READY,
        DONE,
        FAILED
    }

    protected abstract T computeNext();

    @NullableDecl
    protected final T endOfData() {
        this.state = State.DONE;
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.common.base.AbstractIterator$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$google$common$base$AbstractIterator$State = new int[State.values().length];

        static {
            try {
                $SwitchMap$com$google$common$base$AbstractIterator$State[State.DONE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$google$common$base$AbstractIterator$State[State.READY.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        Preconditions.checkState(this.state != State.FAILED);
        int i = AnonymousClass1.$SwitchMap$com$google$common$base$AbstractIterator$State[this.state.ordinal()];
        if (i == 1) {
            return false;
        }
        if (i != 2) {
            return tryToComputeNext();
        }
        return true;
    }

    private boolean tryToComputeNext() {
        this.state = State.FAILED;
        this.next = computeNext();
        if (this.state == State.DONE) {
            return false;
        }
        this.state = State.READY;
        return true;
    }

    @Override // java.util.Iterator
    public final T next() {
        if (hasNext()) {
            this.state = State.NOT_READY;
            T result = this.next;
            this.next = null;
            return result;
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
