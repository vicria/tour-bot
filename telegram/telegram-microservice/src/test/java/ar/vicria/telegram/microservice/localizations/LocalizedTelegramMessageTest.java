package ar.vicria.telegram.microservice.localizations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalizedTelegramMessageTest {

    @ParameterizedTest
    @CsvSource(value = {
            "\\nВыберите ветку   |   \\nВыберите направление   |   Выберите     ",
            "\\nВыберите ветку   |   \\n                       |   ''           ",
    }, delimiter = '|')
    void getCommon(String string1, String string2, String expectedCommon) {
        String common = LocalizedTelegramMessage.getCommon(string1, string2);
        assertEquals(expectedCommon, common);
    }
}