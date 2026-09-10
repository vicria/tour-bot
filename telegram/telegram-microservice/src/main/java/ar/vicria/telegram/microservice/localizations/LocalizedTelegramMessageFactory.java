package ar.vicria.telegram.microservice.localizations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Фабрика по инициализации всех языков сообщений.
 */
@Slf4j
@Component
@Scope("prototype")
public class LocalizedTelegramMessageFactory {

    private final MessageSource ms = new MessageSource();
    private final List<LocalizedTelegramMessage> localizedMessages;

    /**
     * Конструктор.
     */
    public LocalizedTelegramMessageFactory() {
        this.localizedMessages = createLocalizedTelegramMessages();
        log.info("Создана фабрика: {}, локалей: {}", this.hashCode(), localizedMessages.size());
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
        LocalizedTelegramMessage defaultLocale = localizedMessages.stream()
                .filter(text -> text.getLocale().equals(Locale.ENGLISH))
                .findFirst()
                .get();
        if (sentence == null || sentence.isBlank()) {
            return defaultLocale;
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
                        Arrays.stream((sentence.split("\\s")))
                                .anyMatch(word -> word.matches(
                                        "\\b" + Pattern.quote(wordToFind) + "\\b")))
                .findAny()
                .orElse("en");

        return localizedMessages.stream()
                .filter(text -> text.getCommon().equals(lang)
                        || text.getButtonTo().equals(lang)
                        || text.getButtonFrom().equals(lang)
                        || text.getTakeTimeWord().equals(lang))
                .findFirst()
                .orElse(defaultLocale);
    }
}
