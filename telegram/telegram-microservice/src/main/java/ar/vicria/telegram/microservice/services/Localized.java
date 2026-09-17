package ar.vicria.telegram.microservice.services;

import ar.vicria.telegram.microservice.localizations.LocalizedMessageRegistry;
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
     * Реестр локализованных сообщений.
     */
    @Autowired
    protected LocalizedMessageRegistry localizedMessageRegistry;
}
