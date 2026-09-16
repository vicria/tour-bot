package ar.vicria.telegram.microservice.services;

import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Function;

/**
 * Локализованные классы.
 */
@Getter
@Setter
public class Localized {

    /**
     * Фабрика для локализации.
     */
    @Autowired
    protected LocalizedTelegramMessageFactory localizedFactory;

    /**
     * Создать разбор сообщения с локализацией.
     *
     * @param msg сообщение пользователя
     * @return разбор сообщения
     */
    protected RoutMsg routMsg(String msg) {
        return new RoutMsg(msg, localizedFactory);
    }

    /**
     * Выполнить действие с локализованными сообщениями и уничтожает экземпляр прототипа.
     *
     * @param action действие над сообщениями
     * @param <T>    тип результата
     * @return результат действия
     */
    protected <T> T withLocalized(Function<LocalizedTelegramMessage, T> action) {
        LocalizedTelegramMessage localized = localizedFactory.getLocalized();
        try {
            return action.apply(localized);
        } finally {
            localizedFactory.destroyPrototype(localized);
        }
    }

}
