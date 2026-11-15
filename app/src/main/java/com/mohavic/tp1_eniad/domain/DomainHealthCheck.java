package com.mohavic.tp1_eniad.domain;

/** Container healthcheck and standalone smoke verification entrypoint. */
public final class DomainHealthCheck {
    private DomainHealthCheck() {
    }

    public static void main(String[] args) {
        ServiceLocator locator = ServiceLocator.createDefault();
        LocaleGreetingService service = locator.greetingService();
        GreetingResolution ar = service.resolve("ar-MA");
        GreetingResolution en = service.resolve("en-US");
        GreetingResolution def = service.resolve("fr");
        if (!ar.isRtl() || en.isRtl() || ar.getMessage().isEmpty()
                || en.getMessage().isEmpty() || def.getMessage().isEmpty()) {
            throw new IllegalStateException("Multilingual domain health check failed");
        }
        System.out.println("{\"status\":\"ok\",\"locales\":[\"ar\",\"en\",\"default\"]}");
    }
}
