package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.DistanceDto;
import ar.vicria.subte.dto.RouteDto;
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
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Final text about the rout with details.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AnswerDetailsQuery extends Query {

    private final SubteRoadTopicKafkaProducer kafkaProducer;

    //todo убрать в ресурсы и сделать локализацию
    private final static String TIME = "\n<b>займет %s минут</b>";
    private final static String DISTANCE = "\nподробный маршрут: %s";
    private final static String LAST = "\n<b>Последняя станция</b> направления: %s";

    private final RestToSubte rest;
    private final Map<String, StationDto> stations;

    /**
     * Constructor.
     *  @param rowUtil util class for menu
     * @param kafkaProducer
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
        var from = stations.get(String.join(" ", request.getStationFrom(), request.getLineFrom()));
        var to = stations.get(String.join(" ", request.getStationTo(), request.getLineTo()));
        RouteDto send = rest.send(from, to);
        return request.toString()
                + String.format(TIME, send.getTotalTime())
                + String.format(DISTANCE,
                send.getRoute().stream()
                                .map(StationDto::getName).collect(Collectors.joining(" -> ")));
        //todo подробности пересадки
//                + String.format(LAST, send.getLastStation());
    }

    @Override
    public List<AnswerDto> answer(String... option) {
        return Collections.singletonList(new AnswerDto("Скрыть", 0));
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
