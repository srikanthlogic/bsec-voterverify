package kotlin.comparisons;

import androidx.exifinterface.media.ExifInterface;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import java.util.Comparator;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Comparisons.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000<\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a;\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u00022\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\b\u001aY\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u000226\u0010\u0007\u001a\u001c\u0012\u0018\b\u0001\u0012\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u00050\b\"\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005¢\u0006\u0002\u0010\t\u001aW\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n2\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0014\b\u0004\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\b\u001a;\u0010\f\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u00022\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\b\u001aW\u0010\f\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n2\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0014\b\u0004\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\b\u001a-\u0010\r\u001a\u00020\u000e\"\f\b\u0000\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00062\b\u0010\u000f\u001a\u0004\u0018\u0001H\u00022\b\u0010\u0010\u001a\u0004\u0018\u0001H\u0002¢\u0006\u0002\u0010\u0011\u001a>\u0010\u0012\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u000f\u001a\u0002H\u00022\u0006\u0010\u0010\u001a\u0002H\u00022\u0018\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\b¢\u0006\u0002\u0010\u0013\u001aY\u0010\u0012\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u000f\u001a\u0002H\u00022\u0006\u0010\u0010\u001a\u0002H\u000226\u0010\u0007\u001a\u001c\u0012\u0018\b\u0001\u0012\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u00050\b\"\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005¢\u0006\u0002\u0010\u0014\u001aZ\u0010\u0012\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n2\u0006\u0010\u000f\u001a\u0002H\u00022\u0006\u0010\u0010\u001a\u0002H\u00022\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\b¢\u0006\u0002\u0010\u0015\u001aG\u0010\u0016\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u000f\u001a\u0002H\u00022\u0006\u0010\u0010\u001a\u0002H\u00022 \u0010\u0007\u001a\u001c\u0012\u0018\b\u0001\u0012\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u00050\bH\u0002¢\u0006\u0004\b\u0017\u0010\u0014\u001a&\u0010\u0018\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006\u001a-\u0010\u0019\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001j\n\u0012\u0006\u0012\u0004\u0018\u0001H\u0002`\u0003\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006H\u0087\b\u001a@\u0010\u0019\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001j\n\u0012\u0006\u0012\u0004\u0018\u0001H\u0002`\u0003\"\b\b\u0000\u0010\u0002*\u00020\u001a2\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0003\u001a-\u0010\u001b\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001j\n\u0012\u0006\u0012\u0004\u0018\u0001H\u0002`\u0003\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006H\u0087\b\u001a@\u0010\u001b\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001j\n\u0012\u0006\u0012\u0004\u0018\u0001H\u0002`\u0003\"\b\b\u0000\u0010\u0002*\u00020\u001a2\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0003\u001a&\u0010\u001c\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006\u001a0\u0010\u001d\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\u001aO\u0010\u001e\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0003H\u0086\u0004\u001aO\u0010\u001f\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\b\u001ak\u0010\u001f\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0014\b\u0004\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\b\u001aO\u0010 \u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\b\u001ak\u0010 \u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0014\b\u0004\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\b\u001am\u0010!\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u000328\b\u0004\u0010\"\u001a2\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(\u000f\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u000e0#H\u0087\b\u001aO\u0010&\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0003H\u0086\u0004¨\u0006'"}, d2 = {"compareBy", "Ljava/util/Comparator;", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/Comparator;", "selector", "Lkotlin/Function1;", "", "selectors", "", "([Lkotlin/jvm/functions/Function1;)Ljava/util/Comparator;", "K", "comparator", "compareByDescending", "compareValues", "", "a", "b", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)I", "compareValuesBy", "(Ljava/lang/Object;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)I", "(Ljava/lang/Object;Ljava/lang/Object;[Lkotlin/jvm/functions/Function1;)I", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/util/Comparator;Lkotlin/jvm/functions/Function1;)I", "compareValuesByImpl", "compareValuesByImpl$ComparisonsKt__ComparisonsKt", "naturalOrder", "nullsFirst", "", "nullsLast", "reverseOrder", "reversed", "then", "thenBy", "thenByDescending", "thenComparator", "comparison", "Lkotlin/Function2;", "Lkotlin/ParameterName;", AppMeasurementSdk.ConditionalUserProperty.NAME, "thenDescending", "kotlin-stdlib"}, k = 5, mv = {1, 1, 16}, xi = 1, xs = "kotlin/comparisons/ComparisonsKt")
/* loaded from: classes3.dex */
class ComparisonsKt__ComparisonsKt {
    public static final <T> int compareValuesBy(T t, T t2, Function1<? super T, ? extends Comparable<?>>... function1Arr) {
        Intrinsics.checkParameterIsNotNull(function1Arr, "selectors");
        if (function1Arr.length > 0) {
            return compareValuesByImpl$ComparisonsKt__ComparisonsKt(t, t2, function1Arr);
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }

    public static final <T> int compareValuesByImpl$ComparisonsKt__ComparisonsKt(T t, T t2, Function1<? super T, ? extends Comparable<?>>[] function1Arr) {
        for (Function1<? super T, ? extends Comparable<?>> function1 : function1Arr) {
            int diff = ComparisonsKt.compareValues((Comparable) function1.invoke(t), (Comparable) function1.invoke(t2));
            if (diff != 0) {
                return diff;
            }
        }
        return 0;
    }

    private static final <T> int compareValuesBy(T t, T t2, Function1<? super T, ? extends Comparable<?>> function1) {
        return ComparisonsKt.compareValues((Comparable) function1.invoke(t), (Comparable) function1.invoke(t2));
    }

    private static final <T, K> int compareValuesBy(T t, T t2, Comparator<? super K> comparator, Function1<? super T, ? extends K> function1) {
        return comparator.compare((Object) function1.invoke(t), (Object) function1.invoke(t2));
    }

    public static final <T extends Comparable<?>> int compareValues(T t, T t2) {
        if (t == t2) {
            return 0;
        }
        if (t == null) {
            return -1;
        }
        if (t2 == null) {
            return 1;
        }
        return t.compareTo(t2);
    }

    public static final <T> Comparator<T> compareBy(Function1<? super T, ? extends Comparable<?>>... function1Arr) {
        Intrinsics.checkParameterIsNotNull(function1Arr, "selectors");
        if (function1Arr.length > 0) {
            return new Comparator<T>(function1Arr) { // from class: kotlin.comparisons.ComparisonsKt__ComparisonsKt$compareBy$1
                final /* synthetic */ Function1[] $selectors;

                /* JADX INFO: Access modifiers changed from: package-private */
                {
                    this.$selectors = r1;
                }

                @Override // java.util.Comparator
                public final int compare(T t, T t2) {
                    return ComparisonsKt__ComparisonsKt.compareValuesByImpl$ComparisonsKt__ComparisonsKt(t, t2, this.$selectors);
                }
            };
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }

    private static final <T> Comparator<T> compareBy(Function1<? super T, ? extends Comparable<?>> function1) {
        return new ComparisonsKt__ComparisonsKt$compareBy$2(function1);
    }

    private static final <T, K> Comparator<T> compareBy(Comparator<? super K> comparator, Function1<? super T, ? extends K> function1) {
        return new Comparator<T>(comparator, function1) { // from class: kotlin.comparisons.ComparisonsKt__ComparisonsKt$compareBy$3
            final /* synthetic */ Comparator $comparator;
            final /* synthetic */ Function1 $selector;

            {
                this.$comparator = r1;
                this.$selector = r2;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                return this.$comparator.compare(this.$selector.invoke(t), this.$selector.invoke(t2));
            }
        };
    }

    private static final <T> Comparator<T> compareByDescending(Function1<? super T, ? extends Comparable<?>> function1) {
        return new ComparisonsKt__ComparisonsKt$compareByDescending$1(function1);
    }

    private static final <T, K> Comparator<T> compareByDescending(Comparator<? super K> comparator, Function1<? super T, ? extends K> function1) {
        return new Comparator<T>(comparator, function1) { // from class: kotlin.comparisons.ComparisonsKt__ComparisonsKt$compareByDescending$2
            final /* synthetic */ Comparator $comparator;
            final /* synthetic */ Function1 $selector;

            {
                this.$comparator = r1;
                this.$selector = r2;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                return this.$comparator.compare(this.$selector.invoke(t2), this.$selector.invoke(t));
            }
        };
    }

    private static final <T> Comparator<T> thenBy(Comparator<T> comparator, Function1<? super T, ? extends Comparable<?>> function1) {
        return new Comparator<T>(comparator, function1) { // from class: kotlin.comparisons.ComparisonsKt__ComparisonsKt$thenBy$1
            final /* synthetic */ Function1 $selector;
            final /* synthetic */ Comparator $this_thenBy;

            {
                this.$this_thenBy = r1;
                this.$selector = r2;
            }

            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int previousCompare = this.$this_thenBy.compare(t, t2);
                return previousCompare != 0 ? previousCompare : ComparisonsKt.compareValues((Comparable) this.$selector.invoke(t), (Comparable) this.$selector.invoke(t2));
            }
        };
    }

    private static final <T, K> Comparator<T> thenBy(Comparator<T> comparator, Comparator<? super K> comparator2, Function1<? super T, ? extends K> function1) {
        return new Comparator<T>(comparator, comparator2, function1) { // from class: kotlin.comparisons.ComparisonsKt__ComparisonsKt$thenBy$2
            final /* synthetic */ Comparator $comparator;
            final /* synthetic */ Function1 $selector;
            final /* synthetic */ Comparator $this_thenBy;

            {
                this.$this_thenBy = r1;
                this.$comparator = r2;
                this.$selector = r3;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int previousCompare = this.$this_thenBy.compare(t, t2);
                return previousCompare != 0 ? previousCompare : this.$comparator.compare(this.$selector.invoke(t), this.$selector.invoke(t2));
            }
        };
    }

    private static final <T> Comparator<T> thenByDescending(Comparator<T> comparator, Function1<? super T, ? extends Comparable<?>> function1) {
        return new Comparator<T>(comparator, function1) { // from class: kotlin.comparisons.ComparisonsKt__ComparisonsKt$thenByDescending$1
            final /* synthetic */ Function1 $selector;
            final /* synthetic */ Comparator $this_thenByDescending;

            {
                this.$this_thenByDescending = r1;
                this.$selector = r2;
            }

            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int previousCompare = this.$this_thenByDescending.compare(t, t2);
                return previousCompare != 0 ? previousCompare : ComparisonsKt.compareValues((Comparable) this.$selector.invoke(t2), (Comparable) this.$selector.invoke(t));
            }
        };
    }

    private static final <T, K> Comparator<T> thenByDescending(Comparator<T> comparator, Comparator<? super K> comparator2, Function1<? super T, ? extends K> function1) {
        return new Comparator<T>(comparator, comparator2, function1) { // from class: kotlin.comparisons.ComparisonsKt__ComparisonsKt$thenByDescending$2
            final /* synthetic */ Comparator $comparator;
            final /* synthetic */ Function1 $selector;
            final /* synthetic */ Comparator $this_thenByDescending;

            {
                this.$this_thenByDescending = r1;
                this.$comparator = r2;
                this.$selector = r3;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int previousCompare = this.$this_thenByDescending.compare(t, t2);
                return previousCompare != 0 ? previousCompare : this.$comparator.compare(this.$selector.invoke(t2), this.$selector.invoke(t));
            }
        };
    }

    private static final <T> Comparator<T> thenComparator(Comparator<T> comparator, Function2<? super T, ? super T, Integer> function2) {
        return new Comparator<T>(comparator, function2) { // from class: kotlin.comparisons.ComparisonsKt__ComparisonsKt$thenComparator$1
            final /* synthetic */ Function2 $comparison;
            final /* synthetic */ Comparator $this_thenComparator;

            {
                this.$this_thenComparator = r1;
                this.$comparison = r2;
            }

            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int previousCompare = this.$this_thenComparator.compare(t, t2);
                return previousCompare != 0 ? previousCompare : ((Number) this.$comparison.invoke(t, t2)).intValue();
            }
        };
    }

    public static final <T> Comparator<T> then(Comparator<T> comparator, Comparator<? super T> comparator2) {
        Intrinsics.checkParameterIsNotNull(comparator, "$this$then");
        Intrinsics.checkParameterIsNotNull(comparator2, "comparator");
        return new Comparator<T>(comparator, comparator2) { // from class: kotlin.comparisons.ComparisonsKt__ComparisonsKt$then$1
            final /* synthetic */ Comparator $comparator;
            final /* synthetic */ Comparator $this_then;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$this_then = r1;
                this.$comparator = r2;
            }

            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int previousCompare = this.$this_then.compare(t, t2);
                return previousCompare != 0 ? previousCompare : this.$comparator.compare(t, t2);
            }
        };
    }

    public static final <T> Comparator<T> thenDescending(Comparator<T> comparator, Comparator<? super T> comparator2) {
        Intrinsics.checkParameterIsNotNull(comparator, "$this$thenDescending");
        Intrinsics.checkParameterIsNotNull(comparator2, "comparator");
        return new Comparator<T>(comparator, comparator2) { // from class: kotlin.comparisons.ComparisonsKt__ComparisonsKt$thenDescending$1
            final /* synthetic */ Comparator $comparator;
            final /* synthetic */ Comparator $this_thenDescending;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$this_thenDescending = r1;
                this.$comparator = r2;
            }

            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int previousCompare = this.$this_thenDescending.compare(t, t2);
                return previousCompare != 0 ? previousCompare : this.$comparator.compare(t2, t);
            }
        };
    }

    public static final <T> Comparator<T> nullsFirst(Comparator<? super T> comparator) {
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        return new Comparator<T>(comparator) { // from class: kotlin.comparisons.ComparisonsKt__ComparisonsKt$nullsFirst$1
            final /* synthetic */ Comparator $comparator;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$comparator = r1;
            }

            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                if (t == t2) {
                    return 0;
                }
                if (t == null) {
                    return -1;
                }
                if (t2 == null) {
                    return 1;
                }
                return this.$comparator.compare(t, t2);
            }
        };
    }

    private static final <T extends Comparable<? super T>> Comparator<T> nullsFirst() {
        return ComparisonsKt.nullsFirst(ComparisonsKt.naturalOrder());
    }

    public static final <T> Comparator<T> nullsLast(Comparator<? super T> comparator) {
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        return new Comparator<T>(comparator) { // from class: kotlin.comparisons.ComparisonsKt__ComparisonsKt$nullsLast$1
            final /* synthetic */ Comparator $comparator;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$comparator = r1;
            }

            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                if (t == t2) {
                    return 0;
                }
                if (t == null) {
                    return 1;
                }
                if (t2 == null) {
                    return -1;
                }
                return this.$comparator.compare(t, t2);
            }
        };
    }

    private static final <T extends Comparable<? super T>> Comparator<T> nullsLast() {
        return ComparisonsKt.nullsLast(ComparisonsKt.naturalOrder());
    }

    public static final <T extends Comparable<? super T>> Comparator<T> naturalOrder() {
        NaturalOrderComparator naturalOrderComparator = NaturalOrderComparator.INSTANCE;
        if (naturalOrderComparator != null) {
            return naturalOrderComparator;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.Comparator<T> /* = java.util.Comparator<T> */");
    }

    public static final <T extends Comparable<? super T>> Comparator<T> reverseOrder() {
        ReverseOrderComparator reverseOrderComparator = ReverseOrderComparator.INSTANCE;
        if (reverseOrderComparator != null) {
            return reverseOrderComparator;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.Comparator<T> /* = java.util.Comparator<T> */");
    }

    public static final <T> Comparator<T> reversed(Comparator<T> comparator) {
        Intrinsics.checkParameterIsNotNull(comparator, "$this$reversed");
        if (comparator instanceof ReversedComparator) {
            return ((ReversedComparator) comparator).getComparator();
        }
        if (Intrinsics.areEqual(comparator, NaturalOrderComparator.INSTANCE)) {
            ReverseOrderComparator reverseOrderComparator = ReverseOrderComparator.INSTANCE;
            if (reverseOrderComparator != null) {
                return reverseOrderComparator;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Comparator<T> /* = java.util.Comparator<T> */");
        } else if (!Intrinsics.areEqual(comparator, ReverseOrderComparator.INSTANCE)) {
            return new ReversedComparator(comparator);
        } else {
            NaturalOrderComparator naturalOrderComparator = NaturalOrderComparator.INSTANCE;
            if (naturalOrderComparator != null) {
                return naturalOrderComparator;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Comparator<T> /* = java.util.Comparator<T> */");
        }
    }
}
