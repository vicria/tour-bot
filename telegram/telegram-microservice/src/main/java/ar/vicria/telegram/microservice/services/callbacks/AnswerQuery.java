package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.DistanceDto;
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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Final text about the rout without details.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AnswerQuery extends Query {

    private final static String TIME = "\n<b>займет %s минут</b>";

    private final StationQuery stationQuery;
    private final RestToSubte rest;
    private final Map<String, StationDto> stations;

    /**
     * Constructor.
     *
     * @param rowUtil      util class for menu
     * @param stationQuery question about station
     * @param rest         rest client to subte
     */
    public AnswerQuery(RowUtil rowUtil, StationQuery stationQuery, RestToSubte rest) {
        super(rest, rowUtil);
        this.stationQuery = stationQuery;
        this.rest = rest;
        stations = rest.get().stream()
                .collect(Collectors.toMap(StationDto::toString, dto -> dto));
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
        var from = stations.get(String.join(" ", request.getStationFrom(), request.getLineFrom()));
        var to = stations.get(String.join(" ", request.getStationTo(), request.getLineTo()));
        RouteDto send = rest.send(from, to);
        return request.toString()
                + String.format(TIME, send.getTotalTime());
    }

    @Override
    public List<AnswerDto> answer(String... option) {
        return Collections.singletonList(new AnswerDto("Подробнее", 0));
    }

    @Override
    public EditMessageMedia process(Integer msgId,
                                    String chatId,
                                    String msg,
                                    AnswerData answerData) throws IOException {
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

        //todo вынести в другой класс
        DistanceDto dto = new DistanceDto();
        dto.setFrom(stations.get(String.join(" ", response.getStationFrom(), response.getLineFrom())));
        dto.setTo(stations.get(String.join(" ", response.getStationTo(), response.getLineTo())));

        return postQuestionEdit(msgId,
                question(response),
                queryId(),
                answer(),
                chatId,
                dto);
    }


}
