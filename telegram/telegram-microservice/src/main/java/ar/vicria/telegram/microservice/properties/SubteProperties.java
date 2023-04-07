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
//todo delete
@ConfigurationProperties(prefix = "ar.vicria.adapter.subte")
public class SubteProperties extends AppProperties {
    /**
     * Обозначение красной линии
     */
    @NotBlank
    private String red;
    /**
     * Обозначение зеленой линии
     */
    @NotBlank
    private String green;

    /**
     * Обозначение зеленой линии
     */
    @NotBlank
    private String yellow;

}
