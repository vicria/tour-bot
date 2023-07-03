package ar.vicria.telegram.microservice.localizations;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LocalizedTelegramMessageFactoryTest {

    @ParameterizedTest
    @CsvSource(value = {
            "from        |   en  ",
            "до          |   ru  ",
            "desde       |   es  ",
    }, delimiter = '|')
    void getLocalizedByWord(String msg, String lang) {
        var factory = new LocalizedTelegramMessageFactory();
        var localizedByWord = factory.getLocalizedByWord(msg);
        assertNotNull(localizedByWord);
        assertEquals(new Locale(lang), localizedByWord.getLocale());
    }
}