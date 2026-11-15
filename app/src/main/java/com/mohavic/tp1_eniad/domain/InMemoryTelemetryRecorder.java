package com.mohavic.tp1_eniad.domain;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/** Thread-safe in-memory telemetry recorder for multilingual resolution calls. */
public final class InMemoryTelemetryRecorder implements TelemetryRecorder {
    private final ConcurrentHashMap<SupportedLocale, LongAdder> counts = new ConcurrentHashMap<>();
    private final LongAdder total = new LongAdder();
    private final AtomicLong maxDurationNanos = new AtomicLong(0L);

    @Override
    public void recordResolution(SupportedLocale locale, long durationNanos) {
        counts.computeIfAbsent(locale, key -> new LongAdder()).increment();
        total.increment();
        maxDurationNanos.accumulateAndGet(Math.max(0L, durationNanos), Math::max);
    }

    @Override
    public long countFor(SupportedLocale locale) {
        LongAdder adder = counts.get(locale);
        return adder == null ? 0L : adder.sum();
    }

    @Override
    public long totalCount() {
        return total.sum();
    }

    @Override
    public double maxLatencyMillis() {
        return maxDurationNanos.get() / 1_000_000.0;
    }
}
