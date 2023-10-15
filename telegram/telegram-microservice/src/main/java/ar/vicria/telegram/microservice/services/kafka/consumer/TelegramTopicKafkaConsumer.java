package ar.vicria.telegram.microservice.services.kafka.consumer;

import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.callbacks.Query;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
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
    private final LocalizedTelegramMessageFactory localizedFactory;

    /**
     * consumer.
     *
     * @param routeDto RouteDto
     */
    @KafkaListener(topics = {"telegram_road_message_edit_topic"})
    public void consume(RouteDto routeDto) {
        log.debug("=> consuming {}", routeDto);

        //todo validate(routeDto);
        StationDto start = routeDto.getRoute().get(0);
        int size = routeDto.getRoute().size() - 1;
        StationDto end = routeDto.getRoute().get(size);

        RoutMsg routMsg = new RoutMsg();
        routMsg.setLocalizedFactory(localizedFactory);
        routMsg.setFrom(true);
        routMsg.setTo(true);
        routMsg.setLineTo(end.getLine());
        routMsg.setLineFrom(start.getLine());
        routMsg.setStationTo(end.getName());
        routMsg.setStationFrom(start.getName());

        callbacks.stream()
                .filter(c -> c.getClass().getName().equals(routeDto.getClazzName()))
                //todo refactoring
                .peek(clazz -> Query.setTime(routeDto.getTotalTime()))
                .findFirst()
                .map(c -> c.createEditMsg(routeDto.getMsgId(), routMsg, routeDto.getChatId()))
                .ifPresent(msg -> adapterResource.updateText(routeDto.getMsgId(), msg, routeDto.getChatId()));

        log.debug("=> consumed {}", routeDto);
    }
}
