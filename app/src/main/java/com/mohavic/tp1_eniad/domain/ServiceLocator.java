package com.mohavic.tp1_eniad.domain;

import java.util.Objects;

/** Lightweight dependency injection container for Android UI and CLI hosts. */
public final class ServiceLocator {
    private final LocalizationCatalog catalog;
    private final TelemetryRecorder telemetry;
    private final LocaleGreetingService greetingService;

    private static volatile ServiceLocator defaultInstance;

    public ServiceLocator(LocalizationCatalog catalog, TelemetryRecorder telemetry) {
        this.catalog = Objects.requireNonNull(catalog, "catalog must not be null");
        this.telemetry = Objects.requireNonNull(telemetry, "telemetry must not be null");
        this.greetingService = new LocaleGreetingService(this.catalog, this.telemetry);
    }

    public static ServiceLocator createDefault() {
        return new ServiceLocator(
                new DefaultLocalizationCatalog(),
                new InMemoryTelemetryRecorder());
    }

    public static ServiceLocator defaultInstance() {
        ServiceLocator current = defaultInstance;
        if (current == null) {
            synchronized (ServiceLocator.class) {
                if (defaultInstance == null) {
                    defaultInstance = createDefault();
                }
                current = defaultInstance;
            }
        }
        return current;
    }

    public LocalizationCatalog catalog() {
        return catalog;
    }

    public TelemetryRecorder telemetry() {
        return telemetry;
    }

    public LocaleGreetingService greetingService() {
        return greetingService;
    }
}
