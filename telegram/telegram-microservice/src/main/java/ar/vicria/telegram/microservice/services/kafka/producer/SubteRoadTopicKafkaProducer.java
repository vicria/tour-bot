package ar.vicria.telegram.microservice.services.kafka.producer;

import ar.vicria.subte.dto.DistanceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * Producer kafka топика telegram_road_message_edit_topic
 * Событие DistanceDto.
 */
@Service
@RequiredArgsConstructor
public class SubteRoadTopicKafkaProducer {

    private final KafkaTemplate<String, DistanceDto> kafkaTemplate;

    public void sendDistanceForCounting(DistanceDto distanceDto) {
        String topicName = "subte_road" + "_topic";
        kafkaTemplate.setDefaultTopic(topicName);
        kafkaTemplate.send(MessageBuilder
                .withPayload(distanceDto)
                .build());
    }

}