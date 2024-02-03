package ar.vicria.kafka;

import ar.vicria.properties.utils.AppProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * Kafka properties.
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "ar.vicria.kafka")
public class KafkaProperties extends AppProperties {
    /**
     * Topic name for RouteDto event.
     */
    @NotBlank
    private String roadMessageTopic;
    /**
     * Topic name for DistanceDto event.
     */
    @NotBlank
    private String subteRoadTopic;
}
