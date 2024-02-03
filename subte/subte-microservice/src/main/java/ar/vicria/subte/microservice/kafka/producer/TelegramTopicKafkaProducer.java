package ar.vicria.subte.microservice.kafka.producer;

import ar.vicria.kafka.KafkaProperties;
import ar.vicria.subte.dto.RouteDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * Producer kafka топика roadMessageTopic.
 * Событие RouteDto.
 */
@Service
public class TelegramTopicKafkaProducer {

    private final KafkaTemplate<String, RouteDto> kafkaTemplate;

    /**
     * Constructor.
     *
     * @param kafkaTemplate kafka template.
     * @param properties    kafka properties.
     */
    public TelegramTopicKafkaProducer(KafkaTemplate<String, RouteDto> kafkaTemplate, KafkaProperties properties) {
        kafkaTemplate.setDefaultTopic(properties.getRoadMessageTopic());
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * send response with time on route.
     *
     * @param routeDto response
     */
    public void sendAnswerWithRoadCounting(RouteDto routeDto) {
        kafkaTemplate.send(MessageBuilder
                .withPayload(routeDto)
                .build());
    }

}