package ar.vicria.subte.microservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

/**
 * Настройки брокера.
 */
@Configuration
public class KafkaProducerConfig {

    /**
     * default properties.
     *
     * @param kafkaProperties properties
     * @param sslBundles      SSL bundles provider
     * @param <V>             dto
     * @return request
     */
    @Bean
    public <V> DefaultKafkaProducerFactory<String, V> producerFactory(KafkaProperties kafkaProperties,
                                                                      ObjectProvider<SslBundles> sslBundles) {
        return new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties(sslBundles.getIfAvailable()));
    }

    /**
     * kafka template for request dto.
     *
     * @param kafkaProperties default
     * @param objectMapper    default mapper for all dto
     * @param sslBundles      SSL bundles provider
     * @param <V>             dto
     * @return Kafka Template
     */
    @Bean
    public <V> KafkaTemplate<String, V> kafkaTemplate(KafkaProperties kafkaProperties, ObjectMapper objectMapper,
                                                      ObjectProvider<SslBundles> sslBundles) {
        KafkaTemplate<String, V> template = new KafkaTemplate<>(producerFactory(kafkaProperties, sslBundles));
        template.setMessageConverter(new StringJsonMessageConverter(objectMapper));
        return template;
    }
}
