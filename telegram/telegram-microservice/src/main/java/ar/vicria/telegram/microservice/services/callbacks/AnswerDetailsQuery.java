package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.RouteDto;
import ar.vicria.telegram.microservice.services.Answer;
import ar.vicria.telegram.microservice.services.AnswerData;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.Collections;
import java.util.List;

@Service
public class AnswerDetailsQuery extends Query {

    private final String TIME = "\n<b>займет %s минут</b>";
    private final String DISTANCE = "\nподробный маршрут: %s";
    private final String LAST = "\n<b>Последняя станция</b> направления: %s";

    private final RestToSubte rest;

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
    public List<Answer> answer(String... option) {
        return Collections.singletonList(new Answer("Скрыть", 0));
    }

    @Override
    public EditMessageText process(Integer msgId, String chatId, String msg, AnswerData answerData) {
        var response = new RoutMsg(msg);
        return postQuestionEdit(msgId, question(response), queryId(), answer(), chatId);
    }
}
