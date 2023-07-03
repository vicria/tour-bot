package ar.vicria.telegram.microservice.services;

import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

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
}
