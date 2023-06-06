package ar.vicria.telegram.microservice.services.kafka.consumer;

import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.resources.AdapterResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Потребитель kafka топика telegram_road_message_edit_topic.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramTopicKafkaConsumer {

    private final AdapterResource adapterResource;
    private final static String TIME = "\n<b>займет %s минут</b>";

    @KafkaListener(topics = {"telegram_road_message_edit_topic"})
    public void consume(RouteDto routeDto) {
        log.debug("=> consuming {}", routeDto);

        //todo validate(routeDto);
        StationDto start = routeDto.getRoute().get(0);
        int size = routeDto.getRoute().size() - 1;
        StationDto end = routeDto.getRoute().get(size);

        String text = RoutMsg.builder()
                .from(true)
                .to(true)
                .lineFrom(start.getLine())
                .lineTo(end.getLine())
                .stationFrom(start.getName())
                .stationTo(end.getName())
                .build().toString();

        adapterResource.updateText(routeDto.getMsgId(),
                text + TIME + routeDto.getTotalTime(),
                routeDto.getChatId());
        log.debug("=> consumed {}", routeDto);
    }
}
