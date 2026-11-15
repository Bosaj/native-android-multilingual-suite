package com.mohavic.tp1_eniad;

import com.mohavic.tp1_eniad.domain.GreetingResolution;
import com.mohavic.tp1_eniad.domain.LocaleGreetingService;
import com.mohavic.tp1_eniad.domain.ServiceLocator;
import com.mohavic.tp1_eniad.domain.SupportedLocale;
import com.mohavic.tp1_eniad.domain.TelemetryRecorder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/** Unit, boundary, DI, and throughput tests for multilingual domain layer. */
public class LocaleGreetingServiceTest {

    @Test
    public void resolvesArabicEnglishRegionalAndFallbackTags() {
        ServiceLocator locator = ServiceLocator.createDefault();
        LocaleGreetingService service = locator.greetingService();

        GreetingResolution arabic = service.resolve("ar-MA");
        assertEquals(SupportedLocale.ARABIC, arabic.getLocale());
        assertTrue(arabic.isRtl());
        assertEquals("\u0627\u0644\u0633\u0644\u0627\u0645 \u0639\u0644\u064a\u0643\u0645", arabic.getMessage());

        GreetingResolution english = service.resolve("en_US");
        assertEquals(SupportedLocale.ENGLISH, english.getLocale());
        assertFalse(english.isRtl());
        assertEquals("Hello", english.getMessage());

        GreetingResolution fallback = service.resolve("es-ES");
        assertEquals(SupportedLocale.DEFAULT, fallback.getLocale());
        assertFalse(fallback.isRtl());
        assertEquals("Salamo Alikom", fallback.getMessage());
    }

    @Test
    public void rejectsNullAndBlankLanguageTagsAndHandlesEmptyCustomCatalog() {
        ServiceLocator locator = ServiceLocator.createDefault();
        LocaleGreetingService service = locator.greetingService();

        assertThrows(IllegalArgumentException.class, () -> service.resolve(null));
        assertThrows(IllegalArgumentException.class, () -> service.resolve("   "));

        LocaleGreetingService fallbackService = new LocaleGreetingService(
                locale -> locale == SupportedLocale.ENGLISH ? "  " : "Salamo Alikom",
                locator.telemetry());
        GreetingResolution resolved = fallbackService.resolve("en");
        assertEquals("Salamo Alikom", resolved.getMessage());
    }

    @Test
    public void recordsTelemetryAndLatencyUnderStressWorkload() {
        ServiceLocator locator = ServiceLocator.createDefault();
        LocaleGreetingService service = locator.greetingService();
        TelemetryRecorder telemetry = locator.telemetry();

        String[] tags = {"ar-MA", "en-GB", "fr-FR", "de-DE"};
        for (int i = 0; i < 1000; i++) {
            service.resolve(tags[i % tags.length]);
        }

        assertEquals(1000L, telemetry.totalCount());
        assertEquals(250L, telemetry.countFor(SupportedLocale.ARABIC));
        assertEquals(250L, telemetry.countFor(SupportedLocale.ENGLISH));
        assertTrue(telemetry.maxLatencyMillis() < 50.0);
    }
}
