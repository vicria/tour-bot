package ar.vicria.telegram.microservice.localizations;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Реестр локализованных сообщений: поиск по текущей локали или по слову
 * в тексте. Набор языков собирается заранее бином
 * {@link LocalizationConfig#localizedTelegramMessages}.
 */
@Component
public class LocalizedMessageRegistry {

    private final Map<String, LocalizedTelegramMessage> byLanguage;
    private final LocalizedTelegramMessage defaultMessage;

    /**
     * Конструктор.
     *
     * @param localizedTelegramMessages локализованные сообщения для всех доступных языков
     */
    public LocalizedMessageRegistry(List<LocalizedTelegramMessage> localizedTelegramMessages) {
        this.byLanguage = localizedTelegramMessages.stream()
                .collect(Collectors.toMap(msg -> msg.getLocale().getLanguage(), msg -> msg));
        this.defaultMessage = byLanguage.get(Locale.ENGLISH.getLanguage());
        if (this.defaultMessage == null) {
            throw new IllegalArgumentException("must create English localization");
        }
    }

    /**
     * Получить набор локализованных сообщений для текущей локали.
     *
     * @return сообщения для ответа пользователю
     */
    public LocalizedTelegramMessage getLocalized() {
        String language = LocaleContextHolder.getLocale().getLanguage();
        return byLanguage.getOrDefault(language, defaultMessage);
    }

    /**
     * Получить набор локализованных сообщений через слово.
     *
     * @param sentence предложение для определения локализации
     * @return сообщения для ответа пользователю
     */
    public LocalizedTelegramMessage getLocalizedByWord(String sentence) {
        if (sentence == null || sentence.isBlank()) {
            return defaultMessage;
        }
        List<String> commonWords = new ArrayList<>();
        byLanguage.values().forEach(loc -> {
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

        return byLanguage.values().stream()
                .filter(text -> text.getCommon().equals(lang)
                        || text.getButtonTo().equals(lang)
                        || text.getButtonFrom().equals(lang)
                        || text.getTakeTimeWord().equals(lang))
                .findFirst()
                .orElse(defaultMessage);
    }
}
