package ar.vicria.telegram.microservice.rb;

import ar.vicria.telegram.microservice.rb.Messages;
import lombok.Getter;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
* Компонент для получения локализованных сообщений из бандла с сообщениями.
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
     * @param basenames all bundles
     */
    private MessageSource(String... basenames) {
        super();
        setUseCodeAsDefaultMessage(true);
        setDefaultEncoding("UTF-8");
        setDefaultLocale(Locale.ENGLISH);
        addBasenames(basenames);
    }

    /**
     * Получение сообщения без указания языка (дефолт).
     * @param code code for localization
     * @return locale message
     */
    public String getMessage(final String code, final Locale locale) {
        return getMessage(code, new Object[]{new Object()}, locale);
    }
}