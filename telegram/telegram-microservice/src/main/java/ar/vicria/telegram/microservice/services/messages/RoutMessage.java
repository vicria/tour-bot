package ar.vicria.telegram.microservice.services.messages;

import ar.vicria.telegram.microservice.services.Answer;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class RoutMessage extends TextMessage {

    public String queryId() {
        return queryId;
    }

    private final String queryId = "RoutMessage" + UUID.randomUUID().toString();

    public RoutMessage(RowUtil rowUtil) {
        super(rowUtil);
    }

    public List<Answer> answer() {
        return Arrays.asList(new Answer("От", 1), new Answer("До", 2));
    }

    @Override
    public String question() {
        return "Выберите направление";
    }

    @Override
    public boolean supports(String msg) {
        return msg.equals("Маршрут");
    }

    @Override
    public SendMessage process(String chatId) {
        return postQuestionFirst(
                question(),
                queryId(),
                answer(),
                chatId);
    }
}
