package ar.vicria.telegram.microservice.localizations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocalizedMessageRegistryTest {

    private static LocalizedMessageRegistry newRegistry() {
        MessageSource ms = new MessageSource();
        List<LocalizedTelegramMessage> messages = ms.getAvailableLocales().stream()
                .map(locale -> new LocalizedTelegramMessage(locale, ms))
                .collect(Collectors.toList());
        return new LocalizedMessageRegistry(messages);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "from        |   en  ",
            "до          |   ru  ",
            "desde       |   es  ",
    }, delimiter = '|')
    void getLocalizedByWord(String msg, String lang) {
        var registry = newRegistry();
        var localizedByWord = registry.getLocalizedByWord(msg);
        assertNotNull(localizedByWord);
        assertEquals(lang, localizedByWord.getLocale().getLanguage());
    }

    @Test
    void getLocalizedByWordBug6() {
        var registry = newRegistry();
        String msg = "Ruta\n" +
                "desde H🟡 Humberto 1 \n" +
                "hasta -  \n" +
                "Seleccione una línea";
        var localizedByWord = registry.getLocalizedByWord(msg);
        assertNotNull(localizedByWord.getLocale());
        assertEquals("es", localizedByWord.getLocale().getLanguage());
    }

    @ParameterizedTest
    @CsvSource({
            "en-US, en",
            "es-MX, es",
            "ru-UA, ru",
    })
    void getLocalizedFallsBackToBaseLanguage(String languageTag, String expectedLanguage) {
        var registry = newRegistry();
        LocaleContextHolder.setLocale(Locale.forLanguageTag(languageTag));
        try {
            assertEquals(expectedLanguage, registry.getLocalized().getLocale().getLanguage());
        } finally {
            LocaleContextHolder.resetLocaleContext();
        }
    }

    @Test
    void constructorRequiresEnglishLocalization() {
        MessageSource ms = new MessageSource();
        List<LocalizedTelegramMessage> withoutEnglish = List.of(
                new LocalizedTelegramMessage(Locale.forLanguageTag("ru"), ms));

        assertThrows(IllegalArgumentException.class, () -> new LocalizedMessageRegistry(withoutEnglish));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "'' ",
            "'   '",
    })
    void getLocalizedByWordReturnsDefaultForNullOrBlank(String sentence) {
        var registry = newRegistry();
        var defaultMessage = registry.getLocalizedByWord(sentence);
        assertEquals("en", defaultMessage.getLocale().getLanguage());
    }

    @Test
    void getLocalizedByWordNullReturnsDefault() {
        var registry = newRegistry();
        assertEquals("en", registry.getLocalizedByWord(null).getLocale().getLanguage());
    }

    @Test
    void getLocalizedByWordMatchesOnlyByTakeTimeWord() {
        var registry = newRegistry();
        String msg = "поезд займет много времени";
        var localizedByWord = registry.getLocalizedByWord(msg);
        assertEquals("ru", localizedByWord.getLocale().getLanguage());
    }
}
