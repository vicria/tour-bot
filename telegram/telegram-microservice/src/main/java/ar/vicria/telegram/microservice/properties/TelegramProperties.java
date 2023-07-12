package ar.vicria.telegram.microservice.properties;

import ar.vicria.properties.utils.AppProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * Telegram properties for connection.
 */
@Getter
@Setter
@Component
@Validated
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

    @NotBlank
    private String subtePost;

}
