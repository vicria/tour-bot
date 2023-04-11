package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.RouteDto;
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

/**
 * Final text about the rout with details.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AnswerDetailsQuery extends Query {

    //todo убрать в ресурсы и сделать локализацию
    private final static String TIME = "\n<b>займет %s минут</b>";
    private final static String DISTANCE = "\nподробный маршрут: %s";
    private final static String LAST = "\n<b>Последняя станция</b> направления: %s";

    private final RestToSubte rest;

    /**
     * Constructor.
     *
     * @param rowUtil      util class for menu
     * @param rest         rest client to subte
     */
    public AnswerDetailsQuery(RowUtil rowUtil, RestToSubte rest) {
        super(rowUtil);
        this.rest = rest;
    }

    @Override
    public boolean supports(AnswerData answerData, String msg) {
        return answerData.getQuestionId().contains("AnswerQuery");
    }

    @Override
    public String question(RoutMsg request) {
        RouteDto send = rest.send(request.getStationFrom(), request.getStationTo());
        return request.toString()
                + String.format(TIME, send.getTotalTime())
                + String.format(DISTANCE, String.join(" -> ", send.getRoute()));
        //todo подробности пересадки
//                + String.format(LAST, send.getLastStation());
    }

    @Override
    public List<AnswerDto> answer(String... option) {
        return Collections.singletonList(new AnswerDto("Скрыть", 0));
    }

    @Override
    public EditMessageText process(Integer msgId, String chatId, String msg, AnswerData answerData) {
        var response = new RoutMsg(msg);
        return postQuestionEdit(msgId, question(response), queryId(), answer(), chatId);
    }
}
