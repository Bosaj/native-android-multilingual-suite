package com.mohavic.tp1_eniad.domain;

import java.util.EnumMap;
import java.util.Map;

/** In-memory catalog mirroring values, values-ar, and values-en resource strings. */
public final class DefaultLocalizationCatalog implements LocalizationCatalog {
    private final Map<SupportedLocale, String> messages;

    public DefaultLocalizationCatalog() {
        this.messages = new EnumMap<>(SupportedLocale.class);
        this.messages.put(SupportedLocale.DEFAULT, "Salamo Alikom");
        this.messages.put(SupportedLocale.ARABIC, "\u0627\u0644\u0633\u0644\u0627\u0645 \u0639\u0644\u064a\u0643\u0645");
        this.messages.put(SupportedLocale.ENGLISH, "Hello");
        this.messages.put(SupportedLocale.FRENCH, "Salamo Alikom");
    }

    @Override
    public String getGreeting(SupportedLocale locale) {
        if (locale == null) {
            throw new IllegalArgumentException("locale must not be null");
        }
        String msg = messages.get(locale);
        return (msg != null && !msg.trim().isEmpty())
                ? msg
                : messages.get(SupportedLocale.DEFAULT);
    }
}
