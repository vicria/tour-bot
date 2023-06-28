package ar.vicria.telegram.microservice.services.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FormatTextTest {

    @Test
    void bold() {
        String notBold = "Маршрут";
        String bold = FormatText.bold(notBold);
        assertEquals("<b>" + notBold + "</b>", bold);
    }
}