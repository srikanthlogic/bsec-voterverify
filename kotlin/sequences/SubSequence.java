package kotlin.sequences;

import androidx.exifinterface.media.ExifInterface;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Sequences.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010(\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B#\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006¢\u0006\u0002\u0010\bJ\u0016\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010\r\u001a\u00020\u0006H\u0016J\u000f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00000\u000fH\u0096\u0002J\u0016\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010\r\u001a\u00020\u0006H\u0016R\u0014\u0010\t\u001a\u00020\u00068BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lkotlin/sequences/SubSequence;", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/sequences/Sequence;", "Lkotlin/sequences/DropTakeSequence;", "sequence", "startIndex", "", "endIndex", "(Lkotlin/sequences/Sequence;II)V", "count", "getCount", "()I", "drop", "n", "iterator", "", "take", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class SubSequence<T> implements Sequence<T>, DropTakeSequence<T> {
    private final int endIndex;
    private final Sequence<T> sequence;
    private final int startIndex;

    /* JADX WARN: Multi-variable type inference failed */
    public SubSequence(Sequence<? extends T> sequence, int startIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull(sequence, "sequence");
        this.sequence = sequence;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        boolean z = true;
        if (this.startIndex >= 0) {
            if (this.endIndex >= 0) {
                if (!(this.endIndex < this.startIndex ? false : z)) {
                    throw new IllegalArgumentException(("endIndex should be not less than startIndex, but was " + this.endIndex + " < " + this.startIndex).toString());
                }
                return;
            }
            throw new IllegalArgumentException(("endIndex should be non-negative, but is " + this.endIndex).toString());
        }
        throw new IllegalArgumentException(("startIndex should be non-negative, but is " + this.startIndex).toString());
    }

    private final int getCount() {
        return this.endIndex - this.startIndex;
    }

    @Override // kotlin.sequences.DropTakeSequence
    public Sequence<T> drop(int n) {
        return n >= getCount() ? SequencesKt.emptySequence() : new SubSequence(this.sequence, this.startIndex + n, this.endIndex);
    }

    @Override // kotlin.sequences.DropTakeSequence
    public Sequence<T> take(int n) {
        if (n >= getCount()) {
            return this;
        }
        Sequence<T> sequence = this.sequence;
        int i = this.startIndex;
        return new SubSequence(sequence, i, i + n);
    }

    @Override // kotlin.sequences.Sequence
    public Iterator<T> iterator() {
        return new Object() { // from class: kotlin.sequences.SubSequence$iterator$1
            private final Iterator<T> iterator;
            private int position;

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* JADX WARN: Incorrect args count in method signature: ()V */
            {
                this.iterator = SubSequence.this.sequence.iterator();
            }

            public final Iterator<T> getIterator() {
                return this.iterator;
            }

            public final int getPosition() {
                return this.position;
            }

            public final void setPosition(int i) {
                this.position = i;
            }

            private final void drop() {
                while (this.position < SubSequence.this.startIndex && this.iterator.hasNext()) {
                    this.iterator.next();
                    this.position++;
                }
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                drop();
                return this.position < SubSequence.this.endIndex && this.iterator.hasNext();
            }

            /* JADX WARN: Type inference failed for: r0v6, types: [T, java.lang.Object] */
            @Override // java.util.Iterator
            public T next() {
                drop();
                if (this.position < SubSequence.this.endIndex) {
                    this.position++;
                    return this.iterator.next();
                }
                throw new NoSuchElementException();
            }
        };
    }
}
