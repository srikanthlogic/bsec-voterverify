package kotlin.sequences;

import androidx.exifinterface.media.ExifInterface;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Sequences.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\b\u0002\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B+\u0012\u000e\u0010\u0004\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u0005\u0012\u0014\u0010\u0006\u001a\u0010\u0012\u0004\u0012\u00028\u0000\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u0007¢\u0006\u0002\u0010\bJ\u000f\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00000\nH\u0096\u0002R\u0016\u0010\u0004\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0006\u001a\u0010\u0012\u0004\u0012\u00028\u0000\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lkotlin/sequences/GeneratorSequence;", ExifInterface.GPS_DIRECTION_TRUE, "", "Lkotlin/sequences/Sequence;", "getInitialValue", "Lkotlin/Function0;", "getNextValue", "Lkotlin/Function1;", "(Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;)V", "iterator", "", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class GeneratorSequence<T> implements Sequence<T> {
    private final Function0<T> getInitialValue;
    private final Function1<T, T> getNextValue;

    /* JADX WARN: Multi-variable type inference failed */
    public GeneratorSequence(Function0<? extends T> function0, Function1<? super T, ? extends T> function1) {
        Intrinsics.checkParameterIsNotNull(function0, "getInitialValue");
        Intrinsics.checkParameterIsNotNull(function1, "getNextValue");
        this.getInitialValue = function0;
        this.getNextValue = function1;
    }

    @Override // kotlin.sequences.Sequence
    public Iterator<T> iterator() {
        return new Object() { // from class: kotlin.sequences.GeneratorSequence$iterator$1
            private T nextItem;
            private int nextState = -2;

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }

            public final T getNextItem() {
                return this.nextItem;
            }

            public final void setNextItem(T t) {
                this.nextItem = t;
            }

            public final int getNextState() {
                return this.nextState;
            }

            public final void setNextState(int i) {
                this.nextState = i;
            }

            /* JADX WARN: Multi-variable type inference failed */
            private final void calcNext() {
                T t;
                if (this.nextState == -2) {
                    t = GeneratorSequence.this.getInitialValue.invoke();
                } else {
                    Function1 function1 = GeneratorSequence.this.getNextValue;
                    T t2 = this.nextItem;
                    if (t2 == 0) {
                        Intrinsics.throwNpe();
                    }
                    t = function1.invoke(t2);
                }
                this.nextItem = t;
                this.nextState = this.nextItem == 0 ? 0 : 1;
            }

            @Override // java.util.Iterator
            public T next() {
                if (this.nextState < 0) {
                    calcNext();
                }
                if (this.nextState != 0) {
                    T t = this.nextItem;
                    if (t != 0) {
                        this.nextState = -1;
                        return t;
                    }
                    throw new TypeCastException("null cannot be cast to non-null type T");
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.nextState < 0) {
                    calcNext();
                }
                return this.nextState == 1;
            }
        };
    }
}
