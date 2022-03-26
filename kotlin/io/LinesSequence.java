package kotlin.io;

import java.io.BufferedReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ReadWrite.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u000f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00020\u0007H\u0096\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lkotlin/io/LinesSequence;", "Lkotlin/sequences/Sequence;", "", "reader", "Ljava/io/BufferedReader;", "(Ljava/io/BufferedReader;)V", "iterator", "", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class LinesSequence implements Sequence<String> {
    private final BufferedReader reader;

    public LinesSequence(BufferedReader reader) {
        Intrinsics.checkParameterIsNotNull(reader, "reader");
        this.reader = reader;
    }

    @Override // kotlin.sequences.Sequence
    public Iterator<String> iterator() {
        return new Object() { // from class: kotlin.io.LinesSequence$iterator$1
            private boolean done;
            private String nextValue;

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.nextValue == null && !this.done) {
                    this.nextValue = LinesSequence.this.reader.readLine();
                    if (this.nextValue == null) {
                        this.done = true;
                    }
                }
                if (this.nextValue != null) {
                    return true;
                }
                return false;
            }

            @Override // java.util.Iterator
            public String next() {
                if (hasNext()) {
                    String answer = this.nextValue;
                    this.nextValue = null;
                    if (answer == null) {
                        Intrinsics.throwNpe();
                    }
                    return answer;
                }
                throw new NoSuchElementException();
            }
        };
    }
}
