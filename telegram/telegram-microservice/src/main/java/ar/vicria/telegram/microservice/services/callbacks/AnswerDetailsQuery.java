package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.DistanceDto;
import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.subte.resources.StationResource;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
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
public class AnswerDetailsQuery extends Query<RouteDto> {

    private final SubteRoadTopicKafkaProducer kafkaProducer;
    private final Map<String, StationDto> stations;

    /**
     * Constructor.
     * @param rowUtil         util class for menu
     * @param kafkaProducer   producer
     * @param stationResource Feign client to subte
     */
    public AnswerDetailsQuery(RowUtil rowUtil,
                              SubteRoadTopicKafkaProducer kafkaProducer,
                              LocalizedTelegramMessageFactory factory,
                              StationResource stationResource) {
        super(rowUtil, factory);
        this.kafkaProducer = kafkaProducer;
        stations = stationResource.getAll().stream()
                .collect(Collectors.toMap(StationDto::toString, dto -> dto));
    }

    @Override
    public boolean supports(AnswerData answerData, String msg) {
        return answerData.getQuestionId().equals("AnswerQuery");
    }

    @Override
    public String question(RouteDto response) {
        LocalizedTelegramMessage localized = localizedFactory().getLocalized();
        RoutMsg routMsg = new RoutMsg(response);
        return routMsg.toString()
                + String.format(localized.getTakeTime(), response.getTotalTime())
                + String.format(localized.getDistanceDetails(),
                response.getRoute().stream()
                        .map(StationDto::getName).collect(Collectors.joining(" -> ")));
        //todo подробности пересадки
//                + String.format(LAST, send.getLastStation());
    }

    @Override
    public List<AnswerDto> answer(String... option) {
        LocalizedTelegramMessage localized = localizedFactory().getLocalized();
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
