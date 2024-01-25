package ar.vicria.telegram.microservice.properties;

import ar.vicria.properties.utils.AppProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * Telegram properties for connection.
 */
@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "ar.vicria.adapter.telegram")
public class TelegramProperties extends AppProperties {
    /**
     * Имя бота.
     */
    @NotBlank
    private String botUserName;
    /**
     * Токен.
     */
    @NotBlank
    private String botToken;

    @NotBlank
    private String subteGet;

}
