package ar.vicria.telegram.microservice.localizations;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Фабрика по инициализации всех языков сообщений.
 */
@Component
public class LocalizedTelegramMessageFactory {

    private final MessageSource ms = new MessageSource();
    private final List<LocalizedTelegramMessage> localizedMessages;
    private LocalizedTelegramMessage defaultLocale;

    /**
     * Конструктор.
     */
    public LocalizedTelegramMessageFactory() {
        this.localizedMessages = createLocalizedTelegramMessages();
        this.defaultLocale = this.localizedMessages.stream()
                .filter(text -> text.getLocale().equals(Locale.ENGLISH))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("don't have any localization"));
    }

    /**
     * Default language.
     *
     * @return LocalizedTelegramMessage
     */
    public LocalizedTelegramMessage getDefaultLocale() {
        return defaultLocale;
    }

    /**
     * Создает список LocalizedTelegramMessage компонентов с разными локализациями.
     *
     * @return список LocalizedTelegramMessage компонентов
     */
    private List<LocalizedTelegramMessage> createLocalizedTelegramMessages() {
        List<LocalizedTelegramMessage> localizedTelegramMessages = new ArrayList<>();
        List<Locale> availableLocales = ms.getAvailableLocales();

        // Создаем LocalizedTelegramMessage компоненты для каждой локализации
        for (Locale locale : availableLocales) {
            LocalizedTelegramMessage localizedTelegramMessage = new LocalizedTelegramMessage(locale);
            localizedTelegramMessages.add(localizedTelegramMessage);
        }

        return localizedTelegramMessages;
    }

    /**
     * Получить набор локализованных сообщений.
     *
     * @return сообщения для ответа пользователю
     */
    public LocalizedTelegramMessage getLocalized() {
        Locale locale = LocaleContextHolder.getLocale();
        return localizedMessages.stream()
                .filter(text -> text.getLocale().equals(locale))
                .findFirst()
                .orElse(localizedMessages.stream()
                        .filter(text -> text.getLocale().equals(Locale.ENGLISH))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("must create English localization")));
    }

    /**
     * Получить набор локализованных сообщений через слово.
     *
     * @param sentence предложение для определения локализации
     * @return сообщения для ответа пользователю
     */
    public LocalizedTelegramMessage getLocalizedByWord(String sentence) {
        if (sentence == null || sentence.isBlank()) {
            return getDefaultLocale();
        }
        List<String> commonWords = new ArrayList<>();
        localizedMessages.forEach(loc -> {
            commonWords.add(loc.getCommon());
            commonWords.add(loc.getButtonTo());
            commonWords.add(loc.getButtonFrom());
            commonWords.add(loc.getTakeTimeWord());
        });

        String lang = commonWords.stream()
                .filter(wordToFind ->
                        Arrays.asList((sentence.split("\\s"))).contains(wordToFind))
                .findAny()
                .orElse("en");
        return localizedMessages.stream()
                .filter(text -> text.getCommon().equals(lang)
                        || text.getButtonTo().equals(lang)
                        || text.getButtonFrom().equals(lang)
                        || text.getTakeTimeWord().equals(lang))
                .findFirst()
                .orElse(getDefaultLocale());
    }
}
