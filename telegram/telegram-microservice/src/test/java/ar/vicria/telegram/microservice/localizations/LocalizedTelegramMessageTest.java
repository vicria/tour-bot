package ar.vicria.telegram.microservice.localizations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalizedTelegramMessageTest {

    @Test
    void getCommon() {
        String common = LocalizedTelegramMessage.getCommon(
                "\\nВыберите ветку",
                "Выберите направление"
        );

        assertEquals("Выберите", common);
    }

    @Test
    void getCommonMatchOnSecondWord() {
        String common = LocalizedTelegramMessage.getCommon("Foo Bar", "Qux Bar");

        assertEquals("Bar", common);
    }

    @Test
    void getCommonNoMatch() {
        String common = LocalizedTelegramMessage.getCommon("Foo Bar", "Baz Qux");

        assertEquals("", common);
    }
}