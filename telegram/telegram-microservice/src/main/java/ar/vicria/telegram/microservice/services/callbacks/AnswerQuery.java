package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerDto;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Final text about the rout without details.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AnswerQuery extends Query {

    private final static String TIME = "\n<b>займет %s минут</b>";

    private final StationQuery stationQuery;
    private final RestToSubte rest;

    /**
     * Constructor.
     *
     * @param rowUtil      util class for menu
     * @param stationQuery question about station
     * @param rest         rest client to subte
     */
    public AnswerQuery(RowUtil rowUtil, StationQuery stationQuery, RestToSubte rest) {
        super(rowUtil);
        this.stationQuery = stationQuery;
        this.rest = rest;
    }

    @Override
    public boolean supports(AnswerData answerData, String msg) {
        var response = new RoutMsg(msg);
        return answerData.getQuestionId().equals("AnswerDetailsQuery")
                || (answerData.getQuestionId().equals("StationQuery")
                && response.getLineFrom() != null
                && response.getLineTo() != null);
    }

    @Override
    public String question(RoutMsg request) {
        RouteDto send = rest.send(request.getStationFrom(), request.getStationTo());
        return request.toString()
                + String.format(TIME, send.getTotalTime());
    }

    @Override
    public List<AnswerDto> answer(String... option) {
        return Collections.singletonList(new AnswerDto("Подробнее", 0));
    }

    @Override
    public EditMessageText process(Integer msgId, String chatId, String msg, AnswerData answerData) {
        var response = new RoutMsg(msg);
        if (!response.isFull()) {
            Map<String, List<StationDto>> directions = stationQuery.getDirections();
            if (response.getStationFrom() == null) {
                StationDto stationDto = directions.get(response.getLineFrom()).get(answerData.getAnswerCode());
                response.setStationFrom(stationDto.getName());
            } else {
                StationDto stationDto = directions.get(response.getLineTo()).get(answerData.getAnswerCode());
                response.setStationTo(stationDto.getName());
            }
        }
        return postQuestionEdit(msgId, question(response), queryId(), answer(), chatId);
    }
}
