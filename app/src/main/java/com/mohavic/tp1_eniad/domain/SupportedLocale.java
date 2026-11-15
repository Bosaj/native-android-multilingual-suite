package com.mohavic.tp1_eniad.domain;

import java.util.Locale;

/** Canonical supported locales with RTL metadata and BCP-47 tag matching. */
public enum SupportedLocale {
    DEFAULT("und", false),
    ARABIC("ar", true),
    ENGLISH("en", false),
    FRENCH("fr", false);

    private final String languageCode;
    private final boolean rtl;

    SupportedLocale(String languageCode, boolean rtl) {
        this.languageCode = languageCode;
        this.rtl = rtl;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public boolean isRtl() {
        return rtl;
    }

    public static SupportedLocale fromLanguageTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            throw new IllegalArgumentException("Language tag must not be null or blank");
        }
        String normalized = tag.trim().replace('_', '-').toLowerCase(Locale.ROOT);
        int sep = normalized.indexOf('-');
        String primary = sep > 0 ? normalized.substring(0, sep) : normalized;
        for (SupportedLocale value : values()) {
            if (value.languageCode.equals(primary)) {
                return value;
            }
        }
        return DEFAULT;
    }
}
