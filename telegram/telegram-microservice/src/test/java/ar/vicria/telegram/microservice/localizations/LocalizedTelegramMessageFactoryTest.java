package ar.vicria.telegram.microservice.localizations;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LocalizedTelegramMessageFactoryTest {

    @NullAndEmptySource
    @ParameterizedTest
    @CsvSource({
            "from",
            "до",
            "\\nva a tomar %s minutos",
    })
    void getLocalizedByWord(String msg) {
        var factory = new LocalizedTelegramMessageFactory();
        var localizedByWord = factory.getLocalizedByWord(msg);
        assertNotNull(localizedByWord);
//        assertEquals(new Locale(lang), localizedByWord.getLocale());
    }
}