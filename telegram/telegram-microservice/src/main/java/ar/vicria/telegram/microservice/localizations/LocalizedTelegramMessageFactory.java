package ar.vicria.telegram.microservice.localizations;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Фабрика по инициализации всех языков сообщений.
 */
@Component
public class LocalizedTelegramMessageFactory {

    private final Map<Locale, LocalizedTelegramMessage> localizedMessages;
    private final Map<String, LocalizedTelegramMessage> detectionIndex;
    private final LocalizedTelegramMessage defaultLocale;

    /**
     * Конструктор для использования в Spring-контексте: {@link MessageSource}
     * внедряется и переиспользуется для всех локалей.
     *
     * @param ms общий message source
     */
    public LocalizedTelegramMessageFactory(MessageSource ms) {
        this.localizedMessages = createLocalizedTelegramMessages(ms);
        this.detectionIndex = createDetectionIndex(localizedMessages);
        this.defaultLocale = localizedMessages.get(Locale.ENGLISH);
        if (this.defaultLocale == null) {
            throw new IllegalArgumentException("must create English localization");
        }
    }

    /**
     * Конструктор для использования вне Spring-контекста (например, в тестах).
     */
    public LocalizedTelegramMessageFactory() {
        this(new MessageSource());
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
     * Создает набор LocalizedTelegramMessage компонентов с разными локализациями.
     *
     * @param ms общий message source
     * @return локализации, доступные в classpath, по языку
     */
    private static Map<Locale, LocalizedTelegramMessage> createLocalizedTelegramMessages(MessageSource ms) {
        Map<Locale, LocalizedTelegramMessage> messages = new LinkedHashMap<>();
        for (Locale locale : ms.getAvailableLocales()) {
            messages.put(locale, new LocalizedTelegramMessage(locale, ms));
        }
        return messages;
    }

    /**
     * Индекс "слово-маркер языка" -> локализация, для определения языка по тексту.
     *
     * @param localizedMessages доступные локализации
     * @return индекс маркеров
     */
    private static Map<String, LocalizedTelegramMessage> createDetectionIndex(
            Map<Locale, LocalizedTelegramMessage> localizedMessages) {
        Map<String, LocalizedTelegramMessage> index = new LinkedHashMap<>();
        for (LocalizedTelegramMessage localized : localizedMessages.values()) {
            for (String token : localized.getDetectionTokens()) {
                index.putIfAbsent(token, localized);
            }
        }
        return index;
    }

    /**
     * Получить набор локализованных сообщений.
     *
     * @return сообщения для ответа пользователю
     */
    public LocalizedTelegramMessage getLocalized() {
        return localizedMessages.getOrDefault(LocaleContextHolder.getLocale(), defaultLocale);
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
        return Arrays.stream(sentence.split("\\s"))
                .flatMap(word -> detectionIndex.entrySet().stream()
                        .filter(entry -> word.matches("\\b" + Pattern.quote(entry.getKey()) + "\\b")))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(getDefaultLocale());
    }
}
