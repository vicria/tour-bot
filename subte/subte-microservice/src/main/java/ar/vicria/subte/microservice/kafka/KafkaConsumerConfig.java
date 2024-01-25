package ar.vicria.subte.microservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Настройки брокера.
 */
@Configuration
public class KafkaConsumerConfig {

    /**
     * Autoconfig.
     *
     * @param kafkaProperties - properties default
     * @param <V>             dto for response
     * @return listening
     */
    @Bean
    public <V> ConsumerFactory<String, V> consumerFactory(KafkaProperties kafkaProperties) {
        return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties());//TODO: look at this

    }

    /**
     * kafka listener container factory.
     *
     * @param consumerFactory factory with dto
     * @param objectMapper    base mapper for all dto
     * @param <V>             dto
     * @return listening
     */
    @Bean
    public <V> ConcurrentKafkaListenerContainerFactory<String, V> kafkaListenerContainerFactory(
            ConsumerFactory<String, V> consumerFactory,
            ObjectMapper objectMapper
    ) {
        ConcurrentKafkaListenerContainerFactory<String, V> factory = new ConcurrentKafkaListenerContainerFactory<>();
        //        factory.setRetryTemplate(retryTemplate()); //TODO: look at this
        factory.setConsumerFactory(consumerFactory);
        //        factory.setMessageConverter(new StringJsonMessageConverter(objectMapper));//TODO: look at this
        return factory;
    }

    private RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(getSimpleRetryPolicy());
        retryTemplate.setBackOffPolicy(new FixedBackOffPolicy());
        return retryTemplate;
    }

    private RetryPolicy getSimpleRetryPolicy() {
        Map<Class<? extends Throwable>, Boolean> exceptionMap = new HashMap<>();

        exceptionMap.put(TimeoutException.class, true);

        return new SimpleRetryPolicy(10, exceptionMap, true);
    }
}
