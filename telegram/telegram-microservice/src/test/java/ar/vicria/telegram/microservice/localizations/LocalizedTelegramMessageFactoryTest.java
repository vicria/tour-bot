package ar.vicria.telegram.microservice.localizations;

import org.junit.jupiter.api.Test;
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
            "desde       |   es2  ",
    }, delimiter = '|')
    void getLocalizedByWord(String msg, String lang) {
        var factory = new LocalizedTelegramMessageFactory();
        var localizedByWord = factory.getLocalizedByWord(msg);
        assertNotNull(localizedByWord);
        assertEquals(new Locale(lang), localizedByWord.getLocale());
    }

    @Test
    void getLocalizedByWordBug6() {
        var factory = new LocalizedTelegramMessageFactory();
        String msg = "Ruta\n" +
                "desde H\uD83D\uDFE1 Humberto 1 \n" +
                "hasta -  \n" +
                "Seleccione una línea";
        var localizedByWord = factory.getLocalizedByWord(msg);
        assertNotNull(localizedByWord.getLocale());
        assertEquals(new Locale("es"), localizedByWord.getLocale());
    }
}