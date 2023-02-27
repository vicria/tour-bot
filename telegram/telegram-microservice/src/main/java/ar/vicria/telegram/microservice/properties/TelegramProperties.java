package ar.vicria.telegram.microservice.properties;

import ar.vicria.properties.utils.AppProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "ar.vicria.adapter.telegram")
public class TelegramProperties extends AppProperties {
    /**
     * Имя бота
     */
    @NotBlank
    private String botUserName;
    /**
     * Токен
     */
    @NotBlank
    private String botToken;

}
