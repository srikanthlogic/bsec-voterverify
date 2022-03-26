package kotlin.sequences;

import androidx.exifinterface.media.ExifInterface;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Sequences.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010(\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u0002*\u0004\b\u0002\u0010\u00032\b\u0012\u0004\u0012\u0002H\u00030\u0004BA\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0007\u0012\u0018\u0010\b\u001a\u0014\u0012\u0004\u0012\u00028\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00020\t0\u0007¢\u0006\u0002\u0010\nJ\u000f\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00020\tH\u0096\u0002R \u0010\b\u001a\u0014\u0012\u0004\u0012\u00028\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00020\t0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lkotlin/sequences/FlatteningSequence;", ExifInterface.GPS_DIRECTION_TRUE, "R", ExifInterface.LONGITUDE_EAST, "Lkotlin/sequences/Sequence;", "sequence", "transformer", "Lkotlin/Function1;", "iterator", "", "(Lkotlin/sequences/Sequence;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class FlatteningSequence<T, R, E> implements Sequence<E> {
    private final Function1<R, Iterator<E>> iterator;
    private final Sequence<T> sequence;
    private final Function1<T, R> transformer;

    /* JADX WARN: Multi-variable type inference failed */
    public FlatteningSequence(Sequence<? extends T> sequence, Function1<? super T, ? extends R> function1, Function1<? super R, ? extends Iterator<? extends E>> function12) {
        Intrinsics.checkParameterIsNotNull(sequence, "sequence");
        Intrinsics.checkParameterIsNotNull(function1, "transformer");
        Intrinsics.checkParameterIsNotNull(function12, "iterator");
        this.sequence = sequence;
        this.transformer = function1;
        this.iterator = function12;
    }

    @Override // kotlin.sequences.Sequence
    public Iterator<E> iterator() {
        return new Object() { // from class: kotlin.sequences.FlatteningSequence$iterator$1
            private Iterator<? extends E> itemIterator;
            private final Iterator<T> iterator;

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* JADX WARN: Incorrect args count in method signature: ()V */
            {
                this.iterator = FlatteningSequence.this.sequence.iterator();
            }

            public final Iterator<T> getIterator() {
                return this.iterator;
            }

            public final Iterator<E> getItemIterator() {
                return this.itemIterator;
            }

            public final void setItemIterator(Iterator<? extends E> it) {
                this.itemIterator = it;
            }

            /* JADX WARN: Type inference failed for: r0v4, types: [E, java.lang.Object] */
            @Override // java.util.Iterator
            public E next() {
                if (ensureItemIterator()) {
                    Iterator<? extends E> it = this.itemIterator;
                    if (it == 0) {
                        Intrinsics.throwNpe();
                    }
                    return it.next();
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return ensureItemIterator();
            }

            private final boolean ensureItemIterator() {
                Iterator<? extends E> it = this.itemIterator;
                if (it != 0 && !it.hasNext()) {
                    this.itemIterator = (Iterator) 0;
                }
                while (this.itemIterator == null) {
                    if (!this.iterator.hasNext()) {
                        return false;
                    }
                    Iterator nextItemIterator = (Iterator) FlatteningSequence.this.iterator.invoke(FlatteningSequence.this.transformer.invoke(this.iterator.next()));
                    if (nextItemIterator.hasNext()) {
                        this.itemIterator = nextItemIterator;
                        return true;
                    }
                }
                return true;
            }
        };
    }
}
