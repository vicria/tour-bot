package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.DistanceDto;
import ar.vicria.subte.dto.ConnectionDto;
import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerDto;
import ar.vicria.telegram.microservice.services.kafka.producer.SubteRoadTopicKafkaProducer;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Final text about the rout without details.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AnswerQuery extends Query {

    private final SubteRoadTopicKafkaProducer kafkaProducer;
    private final StationQuery stationQuery;
    private final Map<String, StationDto> stations;
    private final RestToSubte rest;

    /**
     * Constructor.
     *
     * @param rowUtil       util class for menu
     * @param kafkaProducer send msg to subte
     * @param stationQuery  question about station
     * @param rest          rest client to subte
     */
    public AnswerQuery(
            RowUtil rowUtil,
            SubteRoadTopicKafkaProducer kafkaProducer,
            StationQuery stationQuery,
            RestToSubte rest
    ) {
        super(rowUtil);
        this.kafkaProducer = kafkaProducer;
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
        public String question(RoutMsg request, RouteDto send) {
        LocalizedTelegramMessage localized = localizedFactory.getLocalized();
//        var from = stations.get(String.join(" ", request.getStationFrom(), request.getLineFrom()));
//        var to = stations.get(String.join(" ", request.getStationTo(), request.getLineTo()));
//        RouteDto send = rest.send(from, to);

        List<String> linesList = createLinesList(send);

        List<ConnectionDto> transitionsList = send.getTransitions();

        StringBuilder allLinesRoad = new StringBuilder("\n");
        for (int i = 1; i < linesList.size(); i++) {
            ConnectionDto transition = getTransition(linesList, transitionsList, i);
            allLinesRoad
                    .append(transition.getStationFrom().toString())
                    .append("\n--->")
                    .append(localized.getTextTransition())
                    .append("--->\n")
                    .append(transition.getStationTo().toString())
                    .append("\n\n");
        }
        return request.toString()
                + String.format(localized.getTakeTime(), send.getTotalTime())
                + "\n"
                + allLinesRoad;
    }

    @Override
    public List<AnswerDto> answer(String... option) {
        LocalizedTelegramMessage localized = localizedFactory.getLocalized();
        return Collections.singletonList(new AnswerDto(localized.getButtonDetails(), 0));
    }

    @Override
    public Optional<BotApiMethod> process(Integer msgId, String chatId, String msg, AnswerData answerData) {
        var response = new RoutMsg(msg);
//        var finalResponse = new RouteDto();
//        ArrayList<StationDto> route = new ArrayList<>();
        if (!response.isFull()) {
            Map<String, List<StationDto>> directions = stationQuery.getDirections();
            if (response.getStationFrom() == null) {
                StationDto stationDto = directions.get(response.getLineFrom()).get(answerData.getAnswerCode());
                response.setStationFrom(stationDto.getName());
//                route.add(stationDto);
//                finalResponse.setRoute(route);
            } else {
                StationDto stationDto = directions.get(response.getLineTo()).get(answerData.getAnswerCode());
                response.setStationTo(stationDto.getName());
//                route.add(stationDto);
//                finalResponse.setRoute(route);

            }
            sendToSubte(response, msgId, chatId);
            return Optional.empty();
        }
//        RoutMsg finalResponse = RoutMsg.fullRoutMsgMaker(response);
        return Optional.ofNullable(createEditMsg(msgId, response, chatId, new RouteDto()));
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
