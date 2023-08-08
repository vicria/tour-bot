package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerDto;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Final text about the rout with details.
 */
@Component
@ConfigurationProperties(prefix = "transition")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AnswerDetailsQuery extends Query {

    private final RestToSubte rest;
    private final Map<String, StationDto> stations;

    @Autowired
    private Environment environment;


    /**
     * Constructor.
     *
     * @param rowUtil util class for menu
     * @param rest    rest client to subte
     */
    public AnswerDetailsQuery(RowUtil rowUtil, RestToSubte rest) {
        super(rowUtil);
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

        return request
                + String.format(localized.getTakeTime(), send.getTotalTime())
                + String.format(localized.getDistanceDetails(), addTransition(send));

        // todo подробности пересадки
//                + String.format(LAST, send.getLastStation());
    }

    private String addTransition(RouteDto send) {
        StringBuilder distanceDetails = new StringBuilder();
        StationDto endStation = send.getRoute().get(send.getRoute().size() - 1);
        Queue<Double> transitionsTime = new ArrayDeque<>(send.getTimeOfTransitions());

        LinkedHashMap<String, List<StationDto>> stationsOfLine = send.getRoute().stream()
                .collect(Collectors.groupingBy(
                        StationDto::getLine,
                        LinkedHashMap::new,
                        Collectors.toList()));


        for (String line : stationsOfLine.keySet()) {
            StringBuilder appendLine = appendLine(line, stationsOfLine.get(line));

            if (!stationsOfLine.get(line).contains(endStation) && !transitionsTime.isEmpty()) {
                distanceDetails
                        .append("\n")
                        .append(appendLine)
                        .append(" ")
                        .append(line)
                        .append("\n\t  ---->\uD83D\uDEB6 ")
                        .append(transitionsTime.poll().intValue())
                        .append("' ---->");
            } else {
                distanceDetails.append("\n")
                        .append(appendLine)
                        .append(" ")
                        .append(endStation.getLine());
            }
        }
        return distanceDetails.toString();
    }

    private StringBuilder appendLine(String line, List<StationDto> stations) {
        StringBuilder stringBuilder = new StringBuilder(line);
        StationDto last = stations.get(stations.size() - 1);
        stations.stream()
                .map(StationDto::getName)
                .takeWhile(name -> !name.equals(last.getName()))
                .forEach(st -> stringBuilder.append(st).append("-->"));
        return stringBuilder
                .append(last.getName());
    }

    @Override
    public List<AnswerDto> answer(String... option) {
        LocalizedTelegramMessage localized = localizedFactory.getLocalized();
        return Collections.singletonList(new AnswerDto(localized.getButtonHide(), 0));
    }

    @Override
    public EditMessageText process(Integer msgId, String chatId, String msg, AnswerData answerData) {
        var response = new RoutMsg(msg);
        return postQuestionEdit(msgId, question(response), queryId(), answer(), chatId);
    }
}
