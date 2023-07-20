package ar.vicria.subte.microservice.kafka.producer;

import ar.vicria.subte.dto.RouteDto;
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
public class TelegramTopicKafkaProducer {

    private final KafkaTemplate<String, RouteDto> kafkaTemplate;

    /**
     * send response with time on roud.
     *
     * @param distanceDto response
     */
    public void sendAnswerWithRoadCounting(RouteDto distanceDto) {
        String topicName = "telegram_road_message_edit" + "_topic";
        kafkaTemplate.setDefaultTopic(topicName);
        kafkaTemplate.send(MessageBuilder
                .withPayload(distanceDto)
                .build());
    }

}