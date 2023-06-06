package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.DistanceDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerDto;
import ar.vicria.telegram.microservice.services.kafka.producer.SubteRoadTopicKafkaProducer;
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
 * Final text about the rout without details.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AnswerQuery extends Query {

    private final SubteRoadTopicKafkaProducer kafkaProducer;
    private final StationQuery stationQuery;
    private static DistanceDto distanceDto;
    private final Map<String, StationDto> stations;

    /**
     * Constructor.
     *
     * @param rowUtil       util class for menu
     * @param kafkaProducer send msg to subte
     * @param stationQuery  question about station
     * @param rest          rest client to subte
     */
    public AnswerQuery(
            RowUtil rowUtil, SubteRoadTopicKafkaProducer kafkaProducer, StationQuery stationQuery, RestToSubte rest
    ) {
        super(rowUtil);
        this.kafkaProducer = kafkaProducer;
        this.stationQuery = stationQuery;
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

        distanceDto = new DistanceDto();
        distanceDto.setFrom(from);
        distanceDto.setTo(to);

        return request.toString();
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
        String question = question(response);

        distanceDto.setMsgId(msgId);
        distanceDto.setChatId(chatId);
        kafkaProducer.sendDistanceForCounting(distanceDto);

        return postQuestionEdit(msgId, question, queryId(), answer(), chatId);
    }
}
