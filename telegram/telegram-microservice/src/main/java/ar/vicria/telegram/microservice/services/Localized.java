package ar.vicria.telegram.microservice.services;

import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import lombok.Getter;

/**
 * Локализованные классы.
 */
@Getter
public class Localized {

    /**
     * Фабрика для локализации.
     */
    protected LocalizedTelegramMessageFactory localizedFactory;

    /**
     * Используемый бин.
     *
     * @param localizedFactory - бин
     */
    public void setLocalizedFactory(LocalizedTelegramMessageFactory localizedFactory) {
        this.localizedFactory = localizedFactory;
    }
}
