package kotlin.sequences;

import androidx.exifinterface.media.ExifInterface;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Sequences.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B'\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007J\u000f\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\tH\u0096\u0002R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"Lkotlin/sequences/DropWhileSequence;", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/sequences/Sequence;", "sequence", "predicate", "Lkotlin/Function1;", "", "(Lkotlin/sequences/Sequence;Lkotlin/jvm/functions/Function1;)V", "iterator", "", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class DropWhileSequence<T> implements Sequence<T> {
    private final Function1<T, Boolean> predicate;
    private final Sequence<T> sequence;

    /* JADX WARN: Multi-variable type inference failed */
    public DropWhileSequence(Sequence<? extends T> sequence, Function1<? super T, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(sequence, "sequence");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        this.sequence = sequence;
        this.predicate = function1;
    }

    @Override // kotlin.sequences.Sequence
    public Iterator<T> iterator() {
        return new Object() { // from class: kotlin.sequences.DropWhileSequence$iterator$1
            private int dropState = -1;
            private final Iterator<T> iterator;
            private T nextItem;

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* JADX WARN: Incorrect args count in method signature: ()V */
            {
                this.iterator = DropWhileSequence.this.sequence.iterator();
            }

            public final Iterator<T> getIterator() {
                return this.iterator;
            }

            public final int getDropState() {
                return this.dropState;
            }

            public final void setDropState(int i) {
                this.dropState = i;
            }

            public final T getNextItem() {
                return this.nextItem;
            }

            public final void setNextItem(T t) {
                this.nextItem = t;
            }

            /* JADX WARN: Type inference failed for: r0v4, types: [T, java.lang.Object] */
            /* JADX WARN: Unknown variable types count: 1 */
            /* Code decompiled incorrectly, please refer to instructions dump */
            private final void drop() {
                while (this.iterator.hasNext()) {
                    ?? next = this.iterator.next();
                    if (!((Boolean) DropWhileSequence.this.predicate.invoke(next)).booleanValue()) {
                        this.nextItem = next;
                        this.dropState = 1;
                        return;
                    }
                }
                this.dropState = 0;
            }

            /* JADX WARN: Type inference failed for: r0v3, types: [T, java.lang.Object] */
            @Override // java.util.Iterator
            public T next() {
                if (this.dropState == -1) {
                    drop();
                }
                if (this.dropState != 1) {
                    return this.iterator.next();
                }
                T t = this.nextItem;
                this.nextItem = null;
                this.dropState = 0;
                return t;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.dropState == -1) {
                    drop();
                }
                return this.dropState == 1 || this.iterator.hasNext();
            }
        };
    }
}
