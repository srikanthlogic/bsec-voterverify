package kotlin.collections;

import androidx.exifinterface.media.ExifInterface;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
/* compiled from: SlidingWindow.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010(\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0000\u001aH\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00070\u0006\"\u0004\b\u0000\u0010\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\b0\u00062\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0000\u001aD\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00070\u000e\"\u0004\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\u000e2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0000¨\u0006\u000f"}, d2 = {"checkWindowSizeStep", "", "size", "", "step", "windowedIterator", "", "", ExifInterface.GPS_DIRECTION_TRUE, "iterator", "partialWindows", "", "reuseBuffer", "windowedSequence", "Lkotlin/sequences/Sequence;", "kotlin-stdlib"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class SlidingWindowKt {
    public static final void checkWindowSizeStep(int size, int step) {
        String str;
        if (!(size > 0 && step > 0)) {
            if (size != step) {
                str = "Both size " + size + " and step " + step + " must be greater than zero.";
            } else {
                str = "size " + size + " must be greater than zero.";
            }
            throw new IllegalArgumentException(str.toString());
        }
    }

    public static final <T> Sequence<List<T>> windowedSequence(Sequence<? extends T> sequence, int size, int step, boolean partialWindows, boolean reuseBuffer) {
        Intrinsics.checkParameterIsNotNull(sequence, "$this$windowedSequence");
        checkWindowSizeStep(size, step);
        return new Sequence<List<? extends T>>(size, step, partialWindows, reuseBuffer) { // from class: kotlin.collections.SlidingWindowKt$windowedSequence$$inlined$Sequence$1
            final /* synthetic */ boolean $partialWindows$inlined;
            final /* synthetic */ boolean $reuseBuffer$inlined;
            final /* synthetic */ int $size$inlined;
            final /* synthetic */ int $step$inlined;

            {
                this.$size$inlined = r2;
                this.$step$inlined = r3;
                this.$partialWindows$inlined = r4;
                this.$reuseBuffer$inlined = r5;
            }

            @Override // kotlin.sequences.Sequence
            public Iterator<List<? extends T>> iterator() {
                return SlidingWindowKt.windowedIterator(Sequence.this.iterator(), this.$size$inlined, this.$step$inlined, this.$partialWindows$inlined, this.$reuseBuffer$inlined);
            }
        };
    }

    public static final <T> Iterator<List<T>> windowedIterator(Iterator<? extends T> it, int size, int step, boolean partialWindows, boolean reuseBuffer) {
        Intrinsics.checkParameterIsNotNull(it, "iterator");
        if (!it.hasNext()) {
            return EmptyIterator.INSTANCE;
        }
        return SequencesKt.iterator(new SlidingWindowKt$windowedIterator$1(size, step, it, reuseBuffer, partialWindows, null));
    }
}
