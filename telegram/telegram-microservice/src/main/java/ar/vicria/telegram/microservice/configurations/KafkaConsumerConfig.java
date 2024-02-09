package ar.vicria.telegram.microservice.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Настройки брокера.
 */
@Configuration
public class KafkaConsumerConfig {
    private static final int RETRY_INTERVAL_MS = 1000;
    private static final int MAX_ATTEMPTS = 10;

    /**
     * Autoconfig.
     *
     * @param kafkaProperties - properties default
     * @param sslBundles      SSL bundles provider
     * @param mapper          ObjectMapper instance
     * @param <V>             dto for response
     * @return listening
     */
    @Bean
    public <V> ConsumerFactory<String, V> consumerFactory(KafkaProperties kafkaProperties,
                                                          ObjectProvider<SslBundles> sslBundles,
                                                          ObjectMapper mapper) {
        StringDeserializer stringDeserializer = new StringDeserializer();
        JsonDeserializer<V> jsonDeserializer = new JsonDeserializer<>(mapper);
        Map<String, Object> configs = kafkaProperties.buildConsumerProperties(sslBundles.getIfAvailable());
        return new DefaultKafkaConsumerFactory<>(configs, stringDeserializer, jsonDeserializer);
    }

    /**
     * kafka listener container factory.
     *
     * @param consumerFactory factory with dto
     * @param <V>             dto
     * @return listening
     */
    @Bean
    public <V> ConcurrentKafkaListenerContainerFactory<String, V> kafkaListenerContainerFactory(
            ConsumerFactory<String, V> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, V> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    /**
     * Kafka retry configuration.
     *
     * @param template Kafka template
     * @param <V>      dto
     * @return Kafka retry configuration
     */
    @Bean
    public <V> RetryTopicConfiguration retryTopicConfiguration(KafkaTemplate<String, V> template) {
        return RetryTopicConfigurationBuilder
                .newInstance()
                .fixedBackOff(RETRY_INTERVAL_MS)
                .maxAttempts(MAX_ATTEMPTS)
                .retryOn(TimeoutException.class)
                .traversingCauses(true)
                .useSingleTopicForSameIntervals()
                .create(template);
    }
}