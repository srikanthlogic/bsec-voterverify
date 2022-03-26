package kotlin.text;

import com.google.android.gms.measurement.api.AppMeasurementSdk;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Strings.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001BY\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012:\u0010\b\u001a6\u0012\u0004\u0012\u00020\u0004\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0006\u0018\u00010\r0\t¢\u0006\u0002\b\u000e¢\u0006\u0002\u0010\u000fJ\u000f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00020\u0011H\u0096\u0002RB\u0010\b\u001a6\u0012\u0004\u0012\u00020\u0004\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0006\u0018\u00010\r0\t¢\u0006\u0002\b\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lkotlin/text/DelimitedRangesSequence;", "Lkotlin/sequences/Sequence;", "Lkotlin/ranges/IntRange;", "input", "", "startIndex", "", "limit", "getNextMatch", "Lkotlin/Function2;", "Lkotlin/ParameterName;", AppMeasurementSdk.ConditionalUserProperty.NAME, "currentIndex", "Lkotlin/Pair;", "Lkotlin/ExtensionFunctionType;", "(Ljava/lang/CharSequence;IILkotlin/jvm/functions/Function2;)V", "iterator", "", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class DelimitedRangesSequence implements Sequence<IntRange> {
    private final Function2<CharSequence, Integer, Pair<Integer, Integer>> getNextMatch;
    private final CharSequence input;
    private final int limit;
    private final int startIndex;

    /* JADX WARN: Multi-variable type inference failed */
    public DelimitedRangesSequence(CharSequence input, int startIndex, int limit, Function2<? super CharSequence, ? super Integer, Pair<Integer, Integer>> function2) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        Intrinsics.checkParameterIsNotNull(function2, "getNextMatch");
        this.input = input;
        this.startIndex = startIndex;
        this.limit = limit;
        this.getNextMatch = function2;
    }

    @Override // kotlin.sequences.Sequence
    public Iterator<IntRange> iterator() {
        return new Object() { // from class: kotlin.text.DelimitedRangesSequence$iterator$1
            private int counter;
            private int currentStartIndex;
            private IntRange nextItem;
            private int nextSearchIndex;
            private int nextState = -1;

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* JADX WARN: Incorrect args count in method signature: ()V */
            {
                this.currentStartIndex = RangesKt.coerceIn(DelimitedRangesSequence.this.startIndex, 0, DelimitedRangesSequence.this.input.length());
                this.nextSearchIndex = this.currentStartIndex;
            }

            public final int getNextState() {
                return this.nextState;
            }

            public final void setNextState(int i) {
                this.nextState = i;
            }

            public final int getCurrentStartIndex() {
                return this.currentStartIndex;
            }

            public final void setCurrentStartIndex(int i) {
                this.currentStartIndex = i;
            }

            public final int getNextSearchIndex() {
                return this.nextSearchIndex;
            }

            public final void setNextSearchIndex(int i) {
                this.nextSearchIndex = i;
            }

            public final IntRange getNextItem() {
                return this.nextItem;
            }

            public final void setNextItem(IntRange intRange) {
                this.nextItem = intRange;
            }

            public final int getCounter() {
                return this.counter;
            }

            public final void setCounter(int i) {
                this.counter = i;
            }

            /* JADX WARN: Code restructure failed: missing block: B:8:0x0025, code lost:
                if (r6.counter < r6.this$0.limit) goto L_0x0027;
             */
            /* Code decompiled incorrectly, please refer to instructions dump */
            private final void calcNext() {
                int i = 0;
                if (this.nextSearchIndex < 0) {
                    this.nextState = 0;
                    this.nextItem = null;
                    return;
                }
                if (DelimitedRangesSequence.this.limit > 0) {
                    this.counter++;
                }
                if (this.nextSearchIndex <= DelimitedRangesSequence.this.input.length()) {
                    Pair match = (Pair) DelimitedRangesSequence.this.getNextMatch.invoke(DelimitedRangesSequence.this.input, Integer.valueOf(this.nextSearchIndex));
                    if (match == null) {
                        this.nextItem = new IntRange(this.currentStartIndex, StringsKt.getLastIndex(DelimitedRangesSequence.this.input));
                        this.nextSearchIndex = -1;
                    } else {
                        int index = ((Number) match.component1()).intValue();
                        int length = ((Number) match.component2()).intValue();
                        this.nextItem = RangesKt.until(this.currentStartIndex, index);
                        this.currentStartIndex = index + length;
                        int i2 = this.currentStartIndex;
                        if (length == 0) {
                            i = 1;
                        }
                        this.nextSearchIndex = i2 + i;
                    }
                    this.nextState = 1;
                }
                this.nextItem = new IntRange(this.currentStartIndex, StringsKt.getLastIndex(DelimitedRangesSequence.this.input));
                this.nextSearchIndex = -1;
                this.nextState = 1;
            }

            @Override // java.util.Iterator
            public IntRange next() {
                if (this.nextState == -1) {
                    calcNext();
                }
                if (this.nextState != 0) {
                    IntRange result = this.nextItem;
                    if (result != null) {
                        this.nextItem = null;
                        this.nextState = -1;
                        return result;
                    }
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.ranges.IntRange");
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.nextState == -1) {
                    calcNext();
                }
                return this.nextState == 1;
            }
        };
    }
}
