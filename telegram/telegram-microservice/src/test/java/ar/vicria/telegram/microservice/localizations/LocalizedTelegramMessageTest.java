package ar.vicria.telegram.microservice.localizations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalizedTelegramMessageTest {

    @Test
    void getCommon1() {
        String common = LocalizedTelegramMessage.getCommon(
                "\\nВыберите ветку",
                "Выберите направление"
        );

        assertEquals("Выберите", common);
    }

    @Test
    void getCommon2() {
        String common = LocalizedTelegramMessage.getCommon(
                "\\n Выберите ветку",
                "Поменяйте направление"
        );

        assertEquals("", common);
    }
}