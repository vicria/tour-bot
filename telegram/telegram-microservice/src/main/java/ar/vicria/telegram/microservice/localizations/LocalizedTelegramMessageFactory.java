package ar.vicria.telegram.microservice.localizations;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Фабрика по инициализации всех языков сообщений.
 */
@Component
public class LocalizedTelegramMessageFactory {
    private final MessageSource ms = new MessageSource();

    /**
     * Создает список LocalizedTelegramMessage компонентов с разными локализациями.
     *
     * @return список LocalizedTelegramMessage компонентов
     */
    public List<LocalizedTelegramMessage> createLocalizedTelegramMessages() {
        List<LocalizedTelegramMessage> localizedTelegramMessages = new ArrayList<>();
        List<Locale> availableLocales = ms.getAvailableLocales();

        // Создаем LocalizedTelegramMessage компоненты для каждой локализации
        for (Locale locale : availableLocales) {
            LocalizedTelegramMessage localizedTelegramMessage = new LocalizedTelegramMessage(locale);
            localizedTelegramMessages.add(localizedTelegramMessage);
        }

        return localizedTelegramMessages;
    }
}
