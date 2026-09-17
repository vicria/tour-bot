package ar.vicria.telegram.microservice.localizations;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalizationConfigTest {

    private final LocalizationConfig config = new LocalizationConfig();

    @Test
    void telegramMessageSourceIsCreated() {
        MessageSource messageSource = config.telegramMessageSource();

        assertNotNull(messageSource);
        assertEquals(1, messageSource.getBasenameSet().size());
    }

    @Test
    void localizedTelegramMessagesContainOneEntryPerAvailableLocale() {
        MessageSource messageSource = config.telegramMessageSource();

        List<LocalizedTelegramMessage> messages = config.localizedTelegramMessages(messageSource);

        assertEquals(messageSource.getAvailableLocales().size(), messages.size());
        assertTrue(messages.stream()
                .anyMatch(message -> message.getLocale().getLanguage().equals(Locale.ENGLISH.getLanguage())));
    }
}
