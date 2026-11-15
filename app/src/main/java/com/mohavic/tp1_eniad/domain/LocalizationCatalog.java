package com.mohavic.tp1_eniad.domain;

/** Repository contract decoupling greeting resolution from Android Context resources. */
public interface LocalizationCatalog {
    String getGreeting(SupportedLocale locale);
}
