package ar.vicria.telegram.microservice.services.kafka.producer;

import ar.vicria.kafka.KafkaProperties;
import ar.vicria.subte.dto.DistanceDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * Producer kafka топика subteRoadTopic.
 * Событие DistanceDto.
 */
@Service
public class SubteRoadTopicKafkaProducer {
    private final KafkaTemplate<String, DistanceDto> kafkaTemplate;

    /**
     * Constructor.
     *
     * @param kafkaTemplate kafka template.
     * @param properties    kafka properties.
     */
    public SubteRoadTopicKafkaProducer(KafkaTemplate<String, DistanceDto> kafkaTemplate, KafkaProperties properties) {
        kafkaTemplate.setDefaultTopic(properties.getSubteRoadTopic());
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * send request to subte.
     *
     * @param distanceDto request with way
     */
    public void sendDistanceForCounting(DistanceDto distanceDto) {
        kafkaTemplate.send(MessageBuilder
                .withPayload(distanceDto)
                .build());
    }

}