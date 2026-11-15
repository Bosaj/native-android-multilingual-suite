package com.mohavic.tp1_eniad.domain;

import java.util.Objects;

/** Clean-architecture service resolving localized greetings with RTL awareness. */
public final class LocaleGreetingService {
    private final LocalizationCatalog catalog;
    private final TelemetryRecorder telemetry;

    public LocaleGreetingService(LocalizationCatalog catalog, TelemetryRecorder telemetry) {
        this.catalog = Objects.requireNonNull(catalog, "catalog must not be null");
        this.telemetry = Objects.requireNonNull(telemetry, "telemetry must not be null");
    }

    public GreetingResolution resolve(String languageTag) {
        long started = System.nanoTime();
        SupportedLocale locale = SupportedLocale.fromLanguageTag(languageTag);
        String greeting = catalog.getGreeting(locale);
        if (greeting == null || greeting.trim().isEmpty()) {
            greeting = catalog.getGreeting(SupportedLocale.DEFAULT);
        }
        long elapsed = System.nanoTime() - started;
        telemetry.recordResolution(locale, elapsed);
        return new GreetingResolution(locale, greeting, locale.isRtl(), languageTag);
    }
}
