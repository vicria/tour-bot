package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.ConnectionDto;
import ar.vicria.subte.dto.DistanceDto;
import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerDto;
import ar.vicria.telegram.microservice.services.kafka.producer.SubteRoadTopicKafkaProducer;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Final text about the rout with details.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AnswerDetailsQuery extends Query {

    private final SubteRoadTopicKafkaProducer kafkaProducer;

    private final RestToSubte rest;
    private final Map<String, StationDto> stations;

    /**
     * Constructor.
     *
     * @param rowUtil util class for menu
     * @param kafkaProducer producer
     * @param rest    rest client to subte
     */
    public AnswerDetailsQuery(RowUtil rowUtil, SubteRoadTopicKafkaProducer kafkaProducer, RestToSubte rest) {
        super(rowUtil);
        this.kafkaProducer = kafkaProducer;
        this.rest = rest;
        stations = rest.get().stream()
                .collect(Collectors.toMap(StationDto::toString, dto -> dto));
    }

    @Override
    public boolean supports(AnswerData answerData, String msg) {
        return answerData.getQuestionId().equals("AnswerQuery");
    }

    @Override
    public String question(RoutMsg request) {
        LocalizedTelegramMessage localized = localizedFactory.getLocalized();
        var from = stations.get(String.join(" ", request.getStationFrom(), request.getLineFrom()));
        var to = stations.get(String.join(" ", request.getStationTo(), request.getLineTo()));
        RouteDto send = rest.send(from, to);
        return request.toString()
                + String.format(localized.getTakeTime(), send.getTotalTime())
                + "\n"
                + String.format(localized.getDistanceDetails(),
                send.getRoute().stream()
                        .map(StationDto::getName).collect(Collectors.joining(" -> ")));
    }


    private String addTransition(RouteDto send) {
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

    @Override
    public List<AnswerDto> answer(String... option) {
        LocalizedTelegramMessage localized = localizedFactory.getLocalized();
        return Collections.singletonList(new AnswerDto(localized.getButtonHide(), 0));
    }

    @Override
    public Optional<BotApiMethod> process(Integer msgId, String chatId, String msg, AnswerData answerData) {
        var response = new RoutMsg(msg);
        sendToSubte(response, msgId, chatId);
        return Optional.empty();
    }

    public void sendToSubte(RoutMsg response, Integer msgId, String chatId) {
        if (response.isFull()) {
            var from = stations.get(String.join(" ", response.getStationFrom(), response.getLineFrom()));
            var to = stations.get(String.join(" ", response.getStationTo(), response.getLineTo()));
            DistanceDto distanceDto = new DistanceDto();
            distanceDto.setFrom(from);
            distanceDto.setTo(to);
            distanceDto.setMsgId(msgId);
            distanceDto.setChatId(chatId);
            distanceDto.setClazzName(this.getClass().getName());
            kafkaProducer.sendDistanceForCounting(distanceDto);
        }
    }
}
