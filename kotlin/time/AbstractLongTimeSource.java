package kotlin.time;

import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: TimeSources.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\b'\u0018\u00002\u00020\u0001:\u0001\fB\u0011\u0012\n\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\b\u001a\u00020\tH\u0016J\b\u0010\n\u001a\u00020\u000bH$R\u0018\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004X\u0084\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\r"}, d2 = {"Lkotlin/time/AbstractLongTimeSource;", "Lkotlin/time/TimeSource;", "unit", "Ljava/util/concurrent/TimeUnit;", "Lkotlin/time/DurationUnit;", "(Ljava/util/concurrent/TimeUnit;)V", "getUnit", "()Ljava/util/concurrent/TimeUnit;", "markNow", "Lkotlin/time/TimeMark;", "read", "", "LongTimeMark", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public abstract class AbstractLongTimeSource implements TimeSource {
    private final TimeUnit unit;

    protected abstract long read();

    public AbstractLongTimeSource(TimeUnit unit) {
        Intrinsics.checkParameterIsNotNull(unit, "unit");
        this.unit = unit;
    }

    protected final TimeUnit getUnit() {
        return this.unit;
    }

    /* compiled from: TimeSources.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0002\u0018\u00002\u00020\u0001B \u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007ø\u0001\u0000¢\u0006\u0002\u0010\bJ\u0010\u0010\n\u001a\u00020\u0007H\u0016ø\u0001\u0000¢\u0006\u0002\u0010\u000bJ\u001b\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u0007H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\u000e\u0010\u000fR\u0013\u0010\u0006\u001a\u00020\u0007X\u0082\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0010"}, d2 = {"Lkotlin/time/AbstractLongTimeSource$LongTimeMark;", "Lkotlin/time/TimeMark;", "startedAt", "", "timeSource", "Lkotlin/time/AbstractLongTimeSource;", "offset", "Lkotlin/time/Duration;", "(JLkotlin/time/AbstractLongTimeSource;DLkotlin/jvm/internal/DefaultConstructorMarker;)V", "D", "elapsedNow", "()D", "plus", "duration", "plus-LRDsOJo", "(D)Lkotlin/time/TimeMark;", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    private static final class LongTimeMark extends TimeMark {
        private final double offset;
        private final long startedAt;
        private final AbstractLongTimeSource timeSource;

        private LongTimeMark(long startedAt, AbstractLongTimeSource timeSource, double offset) {
            this.startedAt = startedAt;
            this.timeSource = timeSource;
            this.offset = offset;
        }

        public /* synthetic */ LongTimeMark(long startedAt, AbstractLongTimeSource timeSource, double offset, DefaultConstructorMarker $constructor_marker) {
            this(startedAt, timeSource, offset);
        }

        @Override // kotlin.time.TimeMark
        public double elapsedNow() {
            return Duration.m989minusLRDsOJo(DurationKt.toDuration(this.timeSource.read() - this.startedAt, this.timeSource.getUnit()), this.offset);
        }

        @Override // kotlin.time.TimeMark
        /* renamed from: plus-LRDsOJo */
        public TimeMark mo963plusLRDsOJo(double duration) {
            return new LongTimeMark(this.startedAt, this.timeSource, Duration.m990plusLRDsOJo(this.offset, duration), null);
        }
    }

    @Override // kotlin.time.TimeSource
    public TimeMark markNow() {
        return new LongTimeMark(read(), this, Duration.Companion.getZERO(), null);
    }
}
