package com.mohavic.tp1_eniad.domain;

import java.util.Objects;

/** Immutable DTO describing a resolved localized greeting response. */
public final class GreetingResolution {
    private final SupportedLocale locale;
    private final String message;
    private final boolean rtl;
    private final String requestedTag;

    public GreetingResolution(
            SupportedLocale locale,
            String message,
            boolean rtl,
            String requestedTag) {
        this.locale = Objects.requireNonNull(locale, "locale must not be null");
        this.message = Objects.requireNonNull(message, "message must not be null");
        this.rtl = rtl;
        this.requestedTag = Objects.requireNonNull(requestedTag, "requestedTag must not be null");
    }

    public SupportedLocale getLocale() {
        return locale;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRtl() {
        return rtl;
    }

    public String getRequestedTag() {
        return requestedTag;
    }
}
