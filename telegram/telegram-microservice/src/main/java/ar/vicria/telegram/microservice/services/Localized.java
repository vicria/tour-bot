package ar.vicria.telegram.microservice.services;

import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;

/**
 * Локализованные классы.
 */
public interface Localized {

    /**
     * Фабрика для локализации.
     * @return LocalizedTelegramMessageFactory
     */
    LocalizedTelegramMessageFactory localizedFactory();
}
