package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.DistanceDto;
import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.subte.resources.StationResource;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Final text about the rout without details.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AnswerQuery extends Query<RouteDto> {

    private final SubteRoadTopicKafkaProducer kafkaProducer;
    private final StationQuery stationQuery;
    private final Map<String, StationDto> stations;

    /**
     * Constructor.
     *
     * @param rowUtil         util class for menu
     * @param kafkaProducer   send msg to subte
     * @param stationQuery    question about station
     * @param stationResource Feign client to subte
     */
    public AnswerQuery(
            RowUtil rowUtil,
            SubteRoadTopicKafkaProducer kafkaProducer,
            StationQuery stationQuery,
            StationResource stationResource,
            LocalizedTelegramMessageFactory factory
    ) {
        super(rowUtil, factory);
        this.kafkaProducer = kafkaProducer;
        this.stationQuery = stationQuery;
        stations = stationResource.getAll().stream()
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
    public String question(RouteDto response) {
        LocalizedTelegramMessage localized = localizedFactory().getLocalized();
        RoutMsg routMsg = new RoutMsg(response);
        return routMsg.toString()
                + String.format(localized.getTakeTime(), response.getTotalTime());
    }

    @Override
    public List<AnswerDto> answer(String... option) {
        LocalizedTelegramMessage localized = localizedFactory().getLocalized();
        return Collections.singletonList(new AnswerDto(localized.getButtonDetails(), 0));
    }

    @Override
    public Optional<BotApiMethod> process(Integer msgId, String chatId, String msg, AnswerData answerData) {
        var response = new RoutMsg(msg);
        Map<String, List<StationDto>> directions = stationQuery.getDirections();
        if (response.getStationFrom() == null) {
            StationDto stationDto = directions.get(response.getLineFrom()).get(answerData.getAnswerCode());
            response.setStationFrom(stationDto.getName());
        } else {
            StationDto stationDto = directions.get(response.getLineTo()).get(answerData.getAnswerCode());
            response.setStationTo(stationDto.getName());
        }
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
