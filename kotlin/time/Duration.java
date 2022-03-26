package kotlin.time;

import androidx.exifinterface.media.ExifInterface;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.DoubleCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import org.apache.commons.io.FilenameUtils;
/* compiled from: Duration.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b&\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0012\b\u0087@\u0018\u0000 s2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001sB\u0014\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005J\u001b\u0010%\u001a\u00020\t2\u0006\u0010&\u001a\u00020\u0000H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b'\u0010(J\u001b\u0010)\u001a\u00020\u00002\u0006\u0010*\u001a\u00020\u0003H\u0086\u0002ø\u0001\u0000¢\u0006\u0004\b+\u0010,J\u001b\u0010)\u001a\u00020\u00002\u0006\u0010*\u001a\u00020\tH\u0086\u0002ø\u0001\u0000¢\u0006\u0004\b+\u0010-J\u001b\u0010)\u001a\u00020\u00032\u0006\u0010&\u001a\u00020\u0000H\u0086\u0002ø\u0001\u0000¢\u0006\u0004\b.\u0010,J\u0013\u0010/\u001a\u0002002\b\u0010&\u001a\u0004\u0018\u000101HÖ\u0003J\t\u00102\u001a\u00020\tHÖ\u0001J\r\u00103\u001a\u000200¢\u0006\u0004\b4\u00105J\r\u00106\u001a\u000200¢\u0006\u0004\b7\u00105J\r\u00108\u001a\u000200¢\u0006\u0004\b9\u00105J\r\u0010:\u001a\u000200¢\u0006\u0004\b;\u00105J\u001b\u0010<\u001a\u00020\u00002\u0006\u0010&\u001a\u00020\u0000H\u0086\u0002ø\u0001\u0000¢\u0006\u0004\b=\u0010,J\u001b\u0010>\u001a\u00020\u00002\u0006\u0010&\u001a\u00020\u0000H\u0086\u0002ø\u0001\u0000¢\u0006\u0004\b?\u0010,J\u0017\u0010@\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002¢\u0006\u0004\bA\u0010(J\u001b\u0010B\u001a\u00020\u00002\u0006\u0010*\u001a\u00020\u0003H\u0086\u0002ø\u0001\u0000¢\u0006\u0004\bC\u0010,J\u001b\u0010B\u001a\u00020\u00002\u0006\u0010*\u001a\u00020\tH\u0086\u0002ø\u0001\u0000¢\u0006\u0004\bC\u0010-J\u008d\u0001\u0010D\u001a\u0002HE\"\u0004\b\u0000\u0010E2u\u0010F\u001aq\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(J\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(K\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(L\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(M\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(N\u0012\u0004\u0012\u0002HE0GH\u0086\b¢\u0006\u0004\bO\u0010PJx\u0010D\u001a\u0002HE\"\u0004\b\u0000\u0010E2`\u0010F\u001a\\\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(K\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(L\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(M\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(N\u0012\u0004\u0012\u0002HE0QH\u0086\b¢\u0006\u0004\bO\u0010RJc\u0010D\u001a\u0002HE\"\u0004\b\u0000\u0010E2K\u0010F\u001aG\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(L\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(M\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(N\u0012\u0004\u0012\u0002HE0SH\u0086\b¢\u0006\u0004\bO\u0010TJN\u0010D\u001a\u0002HE\"\u0004\b\u0000\u0010E26\u0010F\u001a2\u0012\u0013\u0012\u00110V¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(M\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(N\u0012\u0004\u0012\u0002HE0UH\u0086\b¢\u0006\u0004\bO\u0010WJ\u0019\u0010X\u001a\u00020\u00032\n\u0010Y\u001a\u00060Zj\u0002`[¢\u0006\u0004\b\\\u0010]J\u0019\u0010^\u001a\u00020\t2\n\u0010Y\u001a\u00060Zj\u0002`[¢\u0006\u0004\b_\u0010`J\r\u0010a\u001a\u00020b¢\u0006\u0004\bc\u0010dJ\u0019\u0010e\u001a\u00020V2\n\u0010Y\u001a\u00060Zj\u0002`[¢\u0006\u0004\bf\u0010gJ\r\u0010h\u001a\u00020V¢\u0006\u0004\bi\u0010jJ\r\u0010k\u001a\u00020V¢\u0006\u0004\bl\u0010jJ\u000f\u0010m\u001a\u00020bH\u0016¢\u0006\u0004\bn\u0010dJ#\u0010m\u001a\u00020b2\n\u0010Y\u001a\u00060Zj\u0002`[2\b\b\u0002\u0010o\u001a\u00020\t¢\u0006\u0004\bn\u0010pJ\u0013\u0010q\u001a\u00020\u0000H\u0086\u0002ø\u0001\u0000¢\u0006\u0004\br\u0010\u0005R\u0014\u0010\u0006\u001a\u00020\u00008Fø\u0001\u0000¢\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005R\u001a\u0010\b\u001a\u00020\t8@X\u0081\u0004¢\u0006\f\u0012\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0005R\u0011\u0010\u0010\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0005R\u0011\u0010\u0012\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0005R\u0011\u0010\u0014\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u0005R\u0011\u0010\u0016\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0005R\u0011\u0010\u0018\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u0005R\u0011\u0010\u001a\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u0005R\u001a\u0010\u001c\u001a\u00020\t8@X\u0081\u0004¢\u0006\f\u0012\u0004\b\u001d\u0010\u000b\u001a\u0004\b\u001e\u0010\rR\u001a\u0010\u001f\u001a\u00020\t8@X\u0081\u0004¢\u0006\f\u0012\u0004\b \u0010\u000b\u001a\u0004\b!\u0010\rR\u001a\u0010\"\u001a\u00020\t8@X\u0081\u0004¢\u0006\f\u0012\u0004\b#\u0010\u000b\u001a\u0004\b$\u0010\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0080\u0004¢\u0006\u0002\n\u0000ø\u0001\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006t"}, d2 = {"Lkotlin/time/Duration;", "", "value", "", "constructor-impl", "(D)D", "absoluteValue", "getAbsoluteValue-impl", "hoursComponent", "", "hoursComponent$annotations", "()V", "getHoursComponent-impl", "(D)I", "inDays", "getInDays-impl", "inHours", "getInHours-impl", "inMicroseconds", "getInMicroseconds-impl", "inMilliseconds", "getInMilliseconds-impl", "inMinutes", "getInMinutes-impl", "inNanoseconds", "getInNanoseconds-impl", "inSeconds", "getInSeconds-impl", "minutesComponent", "minutesComponent$annotations", "getMinutesComponent-impl", "nanosecondsComponent", "nanosecondsComponent$annotations", "getNanosecondsComponent-impl", "secondsComponent", "secondsComponent$annotations", "getSecondsComponent-impl", "compareTo", "other", "compareTo-LRDsOJo", "(DD)I", "div", "scale", "div-impl", "(DD)D", "(DI)D", "div-LRDsOJo", "equals", "", "", "hashCode", "isFinite", "isFinite-impl", "(D)Z", "isInfinite", "isInfinite-impl", "isNegative", "isNegative-impl", "isPositive", "isPositive-impl", "minus", "minus-LRDsOJo", "plus", "plus-LRDsOJo", "precision", "precision-impl", "times", "times-impl", "toComponents", ExifInterface.GPS_DIRECTION_TRUE, "action", "Lkotlin/Function5;", "Lkotlin/ParameterName;", AppMeasurementSdk.ConditionalUserProperty.NAME, "days", "hours", "minutes", "seconds", "nanoseconds", "toComponents-impl", "(DLkotlin/jvm/functions/Function5;)Ljava/lang/Object;", "Lkotlin/Function4;", "(DLkotlin/jvm/functions/Function4;)Ljava/lang/Object;", "Lkotlin/Function3;", "(DLkotlin/jvm/functions/Function3;)Ljava/lang/Object;", "Lkotlin/Function2;", "", "(DLkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "toDouble", "unit", "Ljava/util/concurrent/TimeUnit;", "Lkotlin/time/DurationUnit;", "toDouble-impl", "(DLjava/util/concurrent/TimeUnit;)D", "toInt", "toInt-impl", "(DLjava/util/concurrent/TimeUnit;)I", "toIsoString", "", "toIsoString-impl", "(D)Ljava/lang/String;", "toLong", "toLong-impl", "(DLjava/util/concurrent/TimeUnit;)J", "toLongMilliseconds", "toLongMilliseconds-impl", "(D)J", "toLongNanoseconds", "toLongNanoseconds-impl", "toString", "toString-impl", "decimals", "(DLjava/util/concurrent/TimeUnit;I)Ljava/lang/String;", "unaryMinus", "unaryMinus-impl", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class Duration implements Comparable<Duration> {
    private final double value;
    public static final Companion Companion = new Companion(null);
    private static final double ZERO = m966constructorimpl(0.0d);
    private static final double INFINITE = m966constructorimpl(DoubleCompanionObject.INSTANCE.getPOSITIVE_INFINITY());

    /* renamed from: box-impl */
    public static final /* synthetic */ Duration m964boximpl(double d) {
        return new Duration(d);
    }

    /* renamed from: equals-impl */
    public static boolean m970equalsimpl(double d, Object obj) {
        return (obj instanceof Duration) && Double.compare(d, ((Duration) obj).m1009unboximpl()) == 0;
    }

    /* renamed from: equals-impl0 */
    public static final boolean m971equalsimpl0(double d, double d2) {
        return Double.compare(d, d2) == 0;
    }

    /* renamed from: hashCode-impl */
    public static int m984hashCodeimpl(double d) {
        long doubleToLongBits = Double.doubleToLongBits(d);
        return (int) (doubleToLongBits ^ (doubleToLongBits >>> 32));
    }

    public static /* synthetic */ void hoursComponent$annotations() {
    }

    public static /* synthetic */ void minutesComponent$annotations() {
    }

    public static /* synthetic */ void nanosecondsComponent$annotations() {
    }

    public static /* synthetic */ void secondsComponent$annotations() {
    }

    /* renamed from: compareTo-LRDsOJo */
    public int m1008compareToLRDsOJo(double d) {
        return m965compareToLRDsOJo(this.value, d);
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        return m970equalsimpl(this.value, obj);
    }

    @Override // java.lang.Object
    public int hashCode() {
        return m984hashCodeimpl(this.value);
    }

    @Override // java.lang.Object
    public String toString() {
        return m1004toStringimpl(this.value);
    }

    /* renamed from: unbox-impl */
    public final /* synthetic */ double m1009unboximpl() {
        return this.value;
    }

    private /* synthetic */ Duration(double value) {
        this.value = value;
    }

    /* renamed from: constructor-impl */
    public static double m966constructorimpl(double value) {
        return value;
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(Duration duration) {
        return m1008compareToLRDsOJo(duration.m1009unboximpl());
    }

    /* compiled from: Duration.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J&\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\n\u0010\r\u001a\u00060\u000ej\u0002`\u000f2\n\u0010\u0010\u001a\u00060\u000ej\u0002`\u000fR\u0016\u0010\u0003\u001a\u00020\u0004ø\u0001\u0000¢\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u0005\u0010\u0006R\u0016\u0010\b\u001a\u00020\u0004ø\u0001\u0000¢\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\t\u0010\u0006\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0011"}, d2 = {"Lkotlin/time/Duration$Companion;", "", "()V", "INFINITE", "Lkotlin/time/Duration;", "getINFINITE", "()D", "D", "ZERO", "getZERO", "convert", "", "value", "sourceUnit", "Ljava/util/concurrent/TimeUnit;", "Lkotlin/time/DurationUnit;", "targetUnit", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        public final double getZERO() {
            return Duration.ZERO;
        }

        public final double getINFINITE() {
            return Duration.INFINITE;
        }

        public final double convert(double value, TimeUnit sourceUnit, TimeUnit targetUnit) {
            Intrinsics.checkParameterIsNotNull(sourceUnit, "sourceUnit");
            Intrinsics.checkParameterIsNotNull(targetUnit, "targetUnit");
            return DurationUnitKt.convertDurationUnit(value, sourceUnit, targetUnit);
        }
    }

    /* renamed from: unaryMinus-impl */
    public static final double m1007unaryMinusimpl(double $this) {
        return m966constructorimpl(-$this);
    }

    /* renamed from: plus-LRDsOJo */
    public static final double m990plusLRDsOJo(double $this, double other) {
        return m966constructorimpl($this + other);
    }

    /* renamed from: minus-LRDsOJo */
    public static final double m989minusLRDsOJo(double $this, double other) {
        return m966constructorimpl($this - other);
    }

    /* renamed from: times-impl */
    public static final double m993timesimpl(double $this, int scale) {
        return m966constructorimpl(((double) scale) * $this);
    }

    /* renamed from: times-impl */
    public static final double m992timesimpl(double $this, double scale) {
        return m966constructorimpl($this * scale);
    }

    /* renamed from: div-impl */
    public static final double m969divimpl(double $this, int scale) {
        return m966constructorimpl($this / ((double) scale));
    }

    /* renamed from: div-impl */
    public static final double m968divimpl(double $this, double scale) {
        return m966constructorimpl($this / scale);
    }

    /* renamed from: div-LRDsOJo */
    public static final double m967divLRDsOJo(double $this, double other) {
        return $this / other;
    }

    /* renamed from: isNegative-impl */
    public static final boolean m987isNegativeimpl(double $this) {
        return $this < ((double) 0);
    }

    /* renamed from: isPositive-impl */
    public static final boolean m988isPositiveimpl(double $this) {
        return $this > ((double) 0);
    }

    /* renamed from: isInfinite-impl */
    public static final boolean m986isInfiniteimpl(double $this) {
        return Double.isInfinite($this);
    }

    /* renamed from: isFinite-impl */
    public static final boolean m985isFiniteimpl(double $this) {
        return !Double.isInfinite($this) && !Double.isNaN($this);
    }

    /* renamed from: getAbsoluteValue-impl */
    public static final double m972getAbsoluteValueimpl(double $this) {
        return m987isNegativeimpl($this) ? m1007unaryMinusimpl($this) : $this;
    }

    /* renamed from: compareTo-LRDsOJo */
    public static int m965compareToLRDsOJo(double $this, double other) {
        return Double.compare($this, other);
    }

    /* renamed from: toComponents-impl */
    public static final <T> T m997toComponentsimpl(double $this, Function5<? super Integer, ? super Integer, ? super Integer, ? super Integer, ? super Integer, ? extends T> function5) {
        Intrinsics.checkParameterIsNotNull(function5, "action");
        return (T) function5.invoke(Integer.valueOf((int) m974getInDaysimpl($this)), Integer.valueOf(m973getHoursComponentimpl($this)), Integer.valueOf(m981getMinutesComponentimpl($this)), Integer.valueOf(m983getSecondsComponentimpl($this)), Integer.valueOf(m982getNanosecondsComponentimpl($this)));
    }

    /* renamed from: toComponents-impl */
    public static final <T> T m996toComponentsimpl(double $this, Function4<? super Integer, ? super Integer, ? super Integer, ? super Integer, ? extends T> function4) {
        Intrinsics.checkParameterIsNotNull(function4, "action");
        return (T) function4.invoke(Integer.valueOf((int) m975getInHoursimpl($this)), Integer.valueOf(m981getMinutesComponentimpl($this)), Integer.valueOf(m983getSecondsComponentimpl($this)), Integer.valueOf(m982getNanosecondsComponentimpl($this)));
    }

    /* renamed from: toComponents-impl */
    public static final <T> T m995toComponentsimpl(double $this, Function3<? super Integer, ? super Integer, ? super Integer, ? extends T> function3) {
        Intrinsics.checkParameterIsNotNull(function3, "action");
        return (T) function3.invoke(Integer.valueOf((int) m978getInMinutesimpl($this)), Integer.valueOf(m983getSecondsComponentimpl($this)), Integer.valueOf(m982getNanosecondsComponentimpl($this)));
    }

    /* renamed from: toComponents-impl */
    public static final <T> T m994toComponentsimpl(double $this, Function2<? super Long, ? super Integer, ? extends T> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "action");
        return (T) function2.invoke(Long.valueOf((long) m980getInSecondsimpl($this)), Integer.valueOf(m982getNanosecondsComponentimpl($this)));
    }

    /* renamed from: getHoursComponent-impl */
    public static final int m973getHoursComponentimpl(double $this) {
        return (int) (m975getInHoursimpl($this) % ((double) 24));
    }

    /* renamed from: getMinutesComponent-impl */
    public static final int m981getMinutesComponentimpl(double $this) {
        return (int) (m978getInMinutesimpl($this) % ((double) 60));
    }

    /* renamed from: getSecondsComponent-impl */
    public static final int m983getSecondsComponentimpl(double $this) {
        return (int) (m980getInSecondsimpl($this) % ((double) 60));
    }

    /* renamed from: getNanosecondsComponent-impl */
    public static final int m982getNanosecondsComponentimpl(double $this) {
        return (int) (m979getInNanosecondsimpl($this) % 1.0E9d);
    }

    /* renamed from: toDouble-impl */
    public static final double m998toDoubleimpl(double $this, TimeUnit unit) {
        Intrinsics.checkParameterIsNotNull(unit, "unit");
        return DurationUnitKt.convertDurationUnit($this, DurationKt.getStorageUnit(), unit);
    }

    /* renamed from: toLong-impl */
    public static final long m1001toLongimpl(double $this, TimeUnit unit) {
        Intrinsics.checkParameterIsNotNull(unit, "unit");
        return (long) m998toDoubleimpl($this, unit);
    }

    /* renamed from: toInt-impl */
    public static final int m999toIntimpl(double $this, TimeUnit unit) {
        Intrinsics.checkParameterIsNotNull(unit, "unit");
        return (int) m998toDoubleimpl($this, unit);
    }

    /* renamed from: getInDays-impl */
    public static final double m974getInDaysimpl(double $this) {
        return m998toDoubleimpl($this, TimeUnit.DAYS);
    }

    /* renamed from: getInHours-impl */
    public static final double m975getInHoursimpl(double $this) {
        return m998toDoubleimpl($this, TimeUnit.HOURS);
    }

    /* renamed from: getInMinutes-impl */
    public static final double m978getInMinutesimpl(double $this) {
        return m998toDoubleimpl($this, TimeUnit.MINUTES);
    }

    /* renamed from: getInSeconds-impl */
    public static final double m980getInSecondsimpl(double $this) {
        return m998toDoubleimpl($this, TimeUnit.SECONDS);
    }

    /* renamed from: getInMilliseconds-impl */
    public static final double m977getInMillisecondsimpl(double $this) {
        return m998toDoubleimpl($this, TimeUnit.MILLISECONDS);
    }

    /* renamed from: getInMicroseconds-impl */
    public static final double m976getInMicrosecondsimpl(double $this) {
        return m998toDoubleimpl($this, TimeUnit.MICROSECONDS);
    }

    /* renamed from: getInNanoseconds-impl */
    public static final double m979getInNanosecondsimpl(double $this) {
        return m998toDoubleimpl($this, TimeUnit.NANOSECONDS);
    }

    /* renamed from: toLongNanoseconds-impl */
    public static final long m1003toLongNanosecondsimpl(double $this) {
        return m1001toLongimpl($this, TimeUnit.NANOSECONDS);
    }

    /* renamed from: toLongMilliseconds-impl */
    public static final long m1002toLongMillisecondsimpl(double $this) {
        return m1001toLongimpl($this, TimeUnit.MILLISECONDS);
    }

    /* renamed from: toString-impl */
    public static String m1004toStringimpl(double $this) {
        TimeUnit unit;
        String str;
        if (m986isInfiniteimpl($this)) {
            return String.valueOf($this);
        }
        if ($this == 0.0d) {
            return "0s";
        }
        double absNs = m979getInNanosecondsimpl(m972getAbsoluteValueimpl($this));
        boolean scientific = false;
        int maxDecimals = 0;
        if (absNs < 1.0E-6d) {
            unit = TimeUnit.SECONDS;
            scientific = true;
        } else if (absNs < ((double) 1)) {
            unit = TimeUnit.NANOSECONDS;
            maxDecimals = 7;
        } else if (absNs < 1000.0d) {
            unit = TimeUnit.NANOSECONDS;
        } else if (absNs < 1000000.0d) {
            unit = TimeUnit.MICROSECONDS;
        } else if (absNs < 1.0E9d) {
            unit = TimeUnit.MILLISECONDS;
        } else if (absNs < 1.0E12d) {
            unit = TimeUnit.SECONDS;
        } else if (absNs < 6.0E13d) {
            unit = TimeUnit.MINUTES;
        } else if (absNs < 3.6E15d) {
            unit = TimeUnit.HOURS;
        } else if (absNs < 8.64E20d) {
            unit = TimeUnit.DAYS;
        } else {
            unit = TimeUnit.DAYS;
            scientific = true;
        }
        double value = m998toDoubleimpl($this, unit);
        StringBuilder sb = new StringBuilder();
        if (scientific) {
            str = FormatToDecimalsKt.formatScientific(value);
        } else if (maxDecimals > 0) {
            str = FormatToDecimalsKt.formatUpToDecimals(value, maxDecimals);
        } else {
            str = FormatToDecimalsKt.formatToExactDecimals(value, m991precisionimpl($this, Math.abs(value)));
        }
        sb.append(str);
        sb.append(DurationUnitKt.shortName(unit));
        return sb.toString();
    }

    /* renamed from: precision-impl */
    private static final int m991precisionimpl(double $this, double value) {
        if (value < ((double) 1)) {
            return 3;
        }
        if (value < ((double) 10)) {
            return 2;
        }
        if (value < ((double) 100)) {
            return 1;
        }
        return 0;
    }

    /* renamed from: toString-impl$default */
    public static /* synthetic */ String m1006toStringimpl$default(double d, TimeUnit timeUnit, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        return m1005toStringimpl(d, timeUnit, i);
    }

    /* renamed from: toString-impl */
    public static final String m1005toStringimpl(double $this, TimeUnit unit, int decimals) {
        String str;
        Intrinsics.checkParameterIsNotNull(unit, "unit");
        if (!(decimals >= 0)) {
            throw new IllegalArgumentException(("decimals must be not negative, but was " + decimals).toString());
        } else if (m986isInfiniteimpl($this)) {
            return String.valueOf($this);
        } else {
            double number = m998toDoubleimpl($this, unit);
            StringBuilder sb = new StringBuilder();
            if (Math.abs(number) < 1.0E14d) {
                str = FormatToDecimalsKt.formatToExactDecimals(number, RangesKt.coerceAtMost(decimals, 12));
            } else {
                str = FormatToDecimalsKt.formatScientific(number);
            }
            sb.append(str);
            sb.append(DurationUnitKt.shortName(unit));
            return sb.toString();
        }
    }

    /* renamed from: toIsoString-impl */
    public static final String m1000toIsoStringimpl(double $this) {
        StringBuilder $this$buildString = new StringBuilder();
        if (m987isNegativeimpl($this)) {
            $this$buildString.append('-');
        }
        $this$buildString.append("PT");
        double $this$iv = m972getAbsoluteValueimpl($this);
        int hours = (int) m975getInHoursimpl($this$iv);
        int minutes = m981getMinutesComponentimpl($this$iv);
        int seconds = m983getSecondsComponentimpl($this$iv);
        int nanoseconds = m982getNanosecondsComponentimpl($this$iv);
        boolean hasMinutes = true;
        boolean hasHours = hours != 0;
        boolean hasSeconds = (seconds == 0 && nanoseconds == 0) ? false : true;
        if (minutes == 0 && (!hasSeconds || !hasHours)) {
            hasMinutes = false;
        }
        if (hasHours) {
            $this$buildString.append(hours);
            $this$buildString.append('H');
        }
        if (hasMinutes) {
            $this$buildString.append(minutes);
            $this$buildString.append('M');
        }
        if (hasSeconds || (!hasHours && !hasMinutes)) {
            $this$buildString.append(seconds);
            if (nanoseconds != 0) {
                $this$buildString.append(FilenameUtils.EXTENSION_SEPARATOR);
                String nss = StringsKt.padStart(String.valueOf(nanoseconds), 9, '0');
                if (nanoseconds % 1000000 == 0) {
                    $this$buildString.append((CharSequence) nss, 0, 3);
                    Intrinsics.checkExpressionValueIsNotNull($this$buildString, "this.append(value, startIndex, endIndex)");
                } else if (nanoseconds % 1000 == 0) {
                    $this$buildString.append((CharSequence) nss, 0, 6);
                    Intrinsics.checkExpressionValueIsNotNull($this$buildString, "this.append(value, startIndex, endIndex)");
                } else {
                    $this$buildString.append(nss);
                }
            }
            $this$buildString.append('S');
        }
        String sb = $this$buildString.toString();
        Intrinsics.checkExpressionValueIsNotNull(sb, "StringBuilder().apply(builderAction).toString()");
        return sb;
    }
}
