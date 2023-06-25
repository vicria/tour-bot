package ar.vicria.telegram.microservice.localizations;

import lombok.Getter;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

/**
 * Компонент для получения локализованных сообщений из бандла с сообщениями.
 *
 * @author abishkam
 * @since 1.0.0
 */
@Getter
public class MessageSource extends ResourceBundleMessageSource {

    /**
     * Конструктор.
     */
    public MessageSource() {
        this("messages");
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
        setDefaultLocale(new Locale("ru"));
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
}