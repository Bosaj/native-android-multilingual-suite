package com.mohavic.tp1_eniad.domain;

/** Observability contract for locale resolution counts and latency measurements. */
public interface TelemetryRecorder {
    void recordResolution(SupportedLocale locale, long durationNanos);

    long countFor(SupportedLocale locale);

    long totalCount();

    double maxLatencyMillis();
}
