package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.ConnectionDto;
import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerDto;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Final text about the rout with details.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AnswerDetailsQuery extends Query {

    private final RestToSubte rest;
    private final Map<String, StationDto> stations;

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


        return request.toString()
                + String.format(localized.getTakeTime(), send.getTotalTime())
                + "\n"
                + String.format(localized.getDistanceDetails(),
                addTransition1(send));
        //todo подробности пересадки
        //      + String.format(LAST, send.getLastStation());
    }


    private String addTransition1(RouteDto send) {
        LocalizedTelegramMessage localized = localizedFactory.getLocalized();

        List<String> linesList = createLinesList(send);
        List<ConnectionDto> transitionsList = send.getTransitions();

        StringBuilder allLinesRoad = new StringBuilder();

        for (int i=1; i<linesList.size(); i++) {
            String s = linesList.get(i);
            allLinesRoad.append("\n")
                    .append(linesList.get(i))
                    .append(" ")
                    .append(send.getRoute().stream()
                    .filter(station -> station.getLine().equals(s))
                    .map(StationDto::getName).collect(Collectors.joining(" -> ")));

            ConnectionDto transition = getTransition(linesList, transitionsList,i);

                allLinesRoad.append("\n--->").append(localized.getTextTransition()).append(", ").append(transition.getTravelTime()).append(" ").append(localized.getTextMinutes()).append("--->");

        }
        return allLinesRoad.toString();
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
