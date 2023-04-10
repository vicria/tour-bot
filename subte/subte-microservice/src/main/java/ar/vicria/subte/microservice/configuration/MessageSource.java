package ar.vicria.subte.microservice.configuration;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Компонент для получения локализованных сообщений из бандла с сообщениями.
 */
@Component
public class MessageSource extends ResourceBundleMessageSource {

    /**
     * Конструктор.
     */
    public MessageSource() {
        this("messages", "validationMessages");
    }

    /**
     * Конструктор.
     * @param basenames all bundles
     */
    public MessageSource(String... basenames) {
        super();
        setUseCodeAsDefaultMessage(true);
        setDefaultEncoding("UTF-8");
        addBasenames(basenames);
    }

    /**
     * Получение сообщения без указания языка (дефолт).
     * @param args parameters
     * @param code code for localization
     * @return locale message
     */
    public String getMessage(final String code, final Object... args) {
        return super.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    /**
     * Получение сообщения с указанием языка.
     * @param args parameters
     * @param code code for localization
     * @param locale locale ru,en...
     * @return locale message
     */
    public String getMessage(final String code, Locale locale, final Object... args) {
        return super.getMessage(code, args, locale);
    }

}
