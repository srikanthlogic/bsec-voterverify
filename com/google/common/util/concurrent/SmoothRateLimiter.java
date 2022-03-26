package com.google.common.util.concurrent;

import com.google.common.math.LongMath;
import com.google.common.util.concurrent.RateLimiter;
import java.util.concurrent.TimeUnit;
/* loaded from: classes3.dex */
abstract class SmoothRateLimiter extends RateLimiter {
    double maxPermits;
    private long nextFreeTicketMicros;
    double stableIntervalMicros;
    double storedPermits;

    abstract double coolDownIntervalMicros();

    abstract void doSetRate(double d, double d2);

    abstract long storedPermitsToWaitTime(double d, double d2);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static final class SmoothWarmingUp extends SmoothRateLimiter {
        private double coldFactor;
        private double slope;
        private double thresholdPermits;
        private final long warmupPeriodMicros;

        /* JADX INFO: Access modifiers changed from: package-private */
        public SmoothWarmingUp(RateLimiter.SleepingStopwatch stopwatch, long warmupPeriod, TimeUnit timeUnit, double coldFactor) {
            super(stopwatch);
            this.warmupPeriodMicros = timeUnit.toMicros(warmupPeriod);
            this.coldFactor = coldFactor;
        }

        @Override // com.google.common.util.concurrent.SmoothRateLimiter
        void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
            double oldMaxPermits = this.maxPermits;
            double coldIntervalMicros = this.coldFactor * stableIntervalMicros;
            long j = this.warmupPeriodMicros;
            this.thresholdPermits = (((double) j) * 0.5d) / stableIntervalMicros;
            this.maxPermits = this.thresholdPermits + ((((double) j) * 2.0d) / (stableIntervalMicros + coldIntervalMicros));
            this.slope = (coldIntervalMicros - stableIntervalMicros) / (this.maxPermits - this.thresholdPermits);
            if (oldMaxPermits == Double.POSITIVE_INFINITY) {
                this.storedPermits = 0.0d;
            } else {
                this.storedPermits = oldMaxPermits == 0.0d ? this.maxPermits : (this.storedPermits * this.maxPermits) / oldMaxPermits;
            }
        }

        @Override // com.google.common.util.concurrent.SmoothRateLimiter
        long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
            double permitsToTake2 = permitsToTake;
            double availablePermitsAboveThreshold = storedPermits - this.thresholdPermits;
            long micros = 0;
            if (availablePermitsAboveThreshold > 0.0d) {
                double permitsAboveThresholdToTake = Math.min(availablePermitsAboveThreshold, permitsToTake2);
                micros = (long) ((permitsAboveThresholdToTake * (permitsToTime(availablePermitsAboveThreshold) + permitsToTime(availablePermitsAboveThreshold - permitsAboveThresholdToTake))) / 2.0d);
                permitsToTake2 -= permitsAboveThresholdToTake;
            }
            return micros + ((long) (this.stableIntervalMicros * permitsToTake2));
        }

        private double permitsToTime(double permits) {
            return this.stableIntervalMicros + (this.slope * permits);
        }

        @Override // com.google.common.util.concurrent.SmoothRateLimiter
        double coolDownIntervalMicros() {
            return ((double) this.warmupPeriodMicros) / this.maxPermits;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static final class SmoothBursty extends SmoothRateLimiter {
        final double maxBurstSeconds;

        /* JADX INFO: Access modifiers changed from: package-private */
        public SmoothBursty(RateLimiter.SleepingStopwatch stopwatch, double maxBurstSeconds) {
            super(stopwatch);
            this.maxBurstSeconds = maxBurstSeconds;
        }

        @Override // com.google.common.util.concurrent.SmoothRateLimiter
        void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
            double oldMaxPermits = this.maxPermits;
            this.maxPermits = this.maxBurstSeconds * permitsPerSecond;
            if (oldMaxPermits == Double.POSITIVE_INFINITY) {
                this.storedPermits = this.maxPermits;
                return;
            }
            double d = 0.0d;
            if (oldMaxPermits != 0.0d) {
                d = (this.storedPermits * this.maxPermits) / oldMaxPermits;
            }
            this.storedPermits = d;
        }

        @Override // com.google.common.util.concurrent.SmoothRateLimiter
        long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
            return 0;
        }

        @Override // com.google.common.util.concurrent.SmoothRateLimiter
        double coolDownIntervalMicros() {
            return this.stableIntervalMicros;
        }
    }

    private SmoothRateLimiter(RateLimiter.SleepingStopwatch stopwatch) {
        super(stopwatch);
        this.nextFreeTicketMicros = 0;
    }

    @Override // com.google.common.util.concurrent.RateLimiter
    final void doSetRate(double permitsPerSecond, long nowMicros) {
        resync(nowMicros);
        double stableIntervalMicros = ((double) TimeUnit.SECONDS.toMicros(1)) / permitsPerSecond;
        this.stableIntervalMicros = stableIntervalMicros;
        doSetRate(permitsPerSecond, stableIntervalMicros);
    }

    @Override // com.google.common.util.concurrent.RateLimiter
    final double doGetRate() {
        return ((double) TimeUnit.SECONDS.toMicros(1)) / this.stableIntervalMicros;
    }

    @Override // com.google.common.util.concurrent.RateLimiter
    final long queryEarliestAvailable(long nowMicros) {
        return this.nextFreeTicketMicros;
    }

    @Override // com.google.common.util.concurrent.RateLimiter
    final long reserveEarliestAvailable(int requiredPermits, long nowMicros) {
        resync(nowMicros);
        long returnValue = this.nextFreeTicketMicros;
        double storedPermitsToSpend = Math.min((double) requiredPermits, this.storedPermits);
        this.nextFreeTicketMicros = LongMath.saturatedAdd(this.nextFreeTicketMicros, storedPermitsToWaitTime(this.storedPermits, storedPermitsToSpend) + ((long) (this.stableIntervalMicros * (((double) requiredPermits) - storedPermitsToSpend))));
        this.storedPermits -= storedPermitsToSpend;
        return returnValue;
    }

    void resync(long nowMicros) {
        long j = this.nextFreeTicketMicros;
        if (nowMicros > j) {
            this.storedPermits = Math.min(this.maxPermits, this.storedPermits + (((double) (nowMicros - j)) / coolDownIntervalMicros()));
            this.nextFreeTicketMicros = nowMicros;
        }
    }
}
