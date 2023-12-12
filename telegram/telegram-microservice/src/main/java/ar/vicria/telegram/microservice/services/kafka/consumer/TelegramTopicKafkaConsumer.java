package ar.vicria.telegram.microservice.services.kafka.consumer;

import ar.vicria.subte.dto.ConnectionDto;
import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.callbacks.Query;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.resources.AdapterResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
//                .stream().peek(c-> c.setText(c+addTransition(routeDto)))
//                .findFirst()
                .ifPresent(msg -> adapterResource.updateText(routeDto.getMsgId(), msg, routeDto.getChatId()));

        log.debug("=> consumed {}", routeDto);
    }


    //---------------------------------===========================-------------------------------------
//
    public String addTransition(RouteDto send) {
        LocalizedTelegramMessage localized = localizedFactory.getLocalized();

        List<String> linesList = createLinesList(send);
        List<ConnectionDto> transitionsList = send.getTransitions();

        StringBuilder allLinesRoad = new StringBuilder();
        String firstLine = linesList.stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("There is no first line"));

        allLinesRoad.append("\n")
                .append(firstLine)
                .append(" ")
                .append(send.getRoute().stream()
                        .filter(station -> station.getLine().equals(firstLine))
                        .map(StationDto::getName).collect(Collectors.joining(" -> ")));


        for (int i = 1; i < linesList.size(); i++) {

            ConnectionDto transition = getTransition(linesList, transitionsList, i);

            allLinesRoad.append("\n--->")
                    .append(localized.getTextTransition())
                    .append(", ")
                    .append(transition.getTravelTime())
                    .append(" ")
                    .append(localized.getTextMinutes())
                    .append("--->");

            String line = linesList.get(i);
            allLinesRoad.append("\n")
                    .append(linesList.get(i))
                    .append(" ")
                    .append(send.getRoute().stream()
                            .filter(station -> station.getLine().equals(line))
                            .map(StationDto::getName).collect(Collectors.joining(" -> ")));


        }
        return allLinesRoad.toString();
    }

    public ConnectionDto getTransition(List<String> linesList, List<ConnectionDto> connectionsList, int cycle) {
        try {
            String lineTo = linesList.get(cycle);
            return connectionsList.stream()
                    .filter(connectionDto -> connectionDto
                            .getStationFrom()
                            .getLine()
                            .equals(linesList.get(cycle - 1)) && connectionDto
                            .getStationTo()
                            .getLine()
                            .equals(lineTo))
                    .reduce((e1, e2) -> e2)
                    .orElseThrow(() -> new NoSuchElementException("There is no such connection"));
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException("List of lines is empty");

        }

    }


    protected List<String> createLinesList(RouteDto send) {
        List<String> linesList = List.of(send.getRoute().stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("There is no lines"))
                .getLine());

        boolean isRouteOnOneLine = send.getRoute().stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("There is no first station"))
                .getLine()
                .equals(send.getRoute().stream()
                        .reduce((e1, e2) -> e2)
                        .orElseThrow(() -> new NoSuchElementException("There is no last station"))
                        .getLine());

        if (!isRouteOnOneLine) {
            linesList = send.getRoute().stream()
                    .map(StationDto::getLine)
                    .distinct()
                    .collect(Collectors.toList());
        }
        return linesList;
    }

}
