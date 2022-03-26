package kotlin.sequences;

import androidx.exifinterface.media.ExifInterface;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.apache.commons.io.FilenameUtils;
/* compiled from: Sequences.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010(\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B\u001b\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010\t\u001a\u00020\u0006H\u0016J\u000f\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00000\u000bH\u0096\u0002J\u0016\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010\t\u001a\u00020\u0006H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lkotlin/sequences/DropSequence;", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/sequences/Sequence;", "Lkotlin/sequences/DropTakeSequence;", "sequence", "count", "", "(Lkotlin/sequences/Sequence;I)V", "drop", "n", "iterator", "", "take", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class DropSequence<T> implements Sequence<T>, DropTakeSequence<T> {
    private final int count;
    private final Sequence<T> sequence;

    /* JADX WARN: Multi-variable type inference failed */
    public DropSequence(Sequence<? extends T> sequence, int count) {
        Intrinsics.checkParameterIsNotNull(sequence, "sequence");
        this.sequence = sequence;
        this.count = count;
        if (!(this.count >= 0)) {
            throw new IllegalArgumentException(("count must be non-negative, but was " + this.count + FilenameUtils.EXTENSION_SEPARATOR).toString());
        }
    }

    @Override // kotlin.sequences.DropTakeSequence
    public Sequence<T> drop(int n) {
        int n1 = this.count + n;
        return n1 < 0 ? new DropSequence(this, n) : new DropSequence(this.sequence, n1);
    }

    @Override // kotlin.sequences.DropTakeSequence
    public Sequence<T> take(int n) {
        int i = this.count;
        int n1 = i + n;
        return n1 < 0 ? new TakeSequence(this, n) : new SubSequence(this.sequence, i, n1);
    }

    @Override // kotlin.sequences.Sequence
    public Iterator<T> iterator() {
        return new Object() { // from class: kotlin.sequences.DropSequence$iterator$1
            private final Iterator<T> iterator;
            private int left;

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* JADX WARN: Incorrect args count in method signature: ()V */
            {
                this.iterator = DropSequence.this.sequence.iterator();
                this.left = DropSequence.this.count;
            }

            public final Iterator<T> getIterator() {
                return this.iterator;
            }

            public final int getLeft() {
                return this.left;
            }

            public final void setLeft(int i) {
                this.left = i;
            }

            private final void drop() {
                while (this.left > 0 && this.iterator.hasNext()) {
                    this.iterator.next();
                    this.left--;
                }
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [T, java.lang.Object] */
            @Override // java.util.Iterator
            public T next() {
                drop();
                return this.iterator.next();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                drop();
                return this.iterator.hasNext();
            }
        };
    }
}
