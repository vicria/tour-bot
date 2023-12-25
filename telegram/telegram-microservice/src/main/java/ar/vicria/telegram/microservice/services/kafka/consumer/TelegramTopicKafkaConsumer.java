package ar.vicria.telegram.microservice.services.kafka.consumer;

import ar.vicria.subte.dto.RouteDto;
import ar.vicria.telegram.microservice.services.callbacks.Query;
import ar.vicria.telegram.resources.AdapterResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Потребитель kafka топика telegram_road_message_edit_topic.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramTopicKafkaConsumer {

    private final List<Query> callbacks;
    private final AdapterResource adapterResource;

    /**
     * consumer.
     *
     * @param routeDto RouteDto
     */
    @KafkaListener(topics = {"telegram_road_message_edit_topic"})
    public void consume(RouteDto routeDto) {
        log.debug("=> consuming {}", routeDto);

        //todo validate(routeDto);

        callbacks.stream()
                .filter(c -> c.getClass().getName().equals(routeDto.getClazzName()))
                .findFirst()
                .ifPresent(clazz -> {
                    var msg = clazz.createEditMsg(routeDto.getMsgId(), routeDto, routeDto.getChatId());
                    adapterResource.updateText(routeDto.getMsgId(), msg, routeDto.getChatId());
                });

        log.debug("=> consumed {}", routeDto);
    }
}
