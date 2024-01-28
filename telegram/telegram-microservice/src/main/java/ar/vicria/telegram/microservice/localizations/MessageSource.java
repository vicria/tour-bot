package ar.vicria.telegram.microservice.localizations;

import lombok.Getter;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Компонент для получения локализованных сообщений из бандла с сообщениями.
 *
 * @author abishkam
 * @since 1.0.0
 */
@Getter
public class MessageSource extends ResourceBundleMessageSource {

    private static final String BASENAME = "messages";

    /**
     * Конструктор.
     */
    public MessageSource() {
        this(BASENAME);
    }

    /**
     * Конструктор.
     *
     * @param basenames all bundles
     */
    private MessageSource(String... basenames) {
        super();
        setUseCodeAsDefaultMessage(true);
        setDefaultEncoding("UTF-8");
        addBasenames(basenames);
    }

    /**
     * Получение сообщения без указания языка (дефолт).
     *
     * @param code   code for localization
     * @param locale localization of a class
     * @return locale message
     */
    public String getMessage(final String code, final Locale locale) {
        return getMessage(code, new Object[]{new Object()}, locale);
    }

    /**
     * Доступные локализации.
     *
     * @return список доступных локализаций
     */
    public List<Locale> getAvailableLocales() {
        List<Locale> availableLocales = new ArrayList<>();
        //todo сделать энам или научиться определять бандлы
        List<Locale> locales = new ArrayList<>();
        locales.add(Locale.forLanguageTag("ru"));
        locales.add(Locale.forLanguageTag("en"));
        locales.add(Locale.forLanguageTag("es"));

        ResourceBundle.Control control = ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES);

        // Добавить только те локализации, для которых есть соответствующие файлы ресурсов
        for (Locale locale : locales) {
            try {
                ResourceBundle bundle = ResourceBundle.getBundle(BASENAME, locale, control);
                availableLocales.add(locale);
            } catch (MissingResourceException e) {
                // Файл ресурсов для данной локализации не найден
            }
        }
        return availableLocales;
    }

}