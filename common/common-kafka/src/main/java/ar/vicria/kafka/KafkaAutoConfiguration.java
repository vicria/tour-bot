package ar.vicria.kafka;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Kafka auto configuration.
 */
@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaAutoConfiguration {
}
