package ar.vicria.telegram.microservice.services.messages;

import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerDto;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.util.Arrays;
import java.util.List;

/**
 * generate first message for discussion about rout.
 */
@Component
public class RoutMessage extends TextMessage {

    /**
     * Constrictor.
     * @param rowUtil util for telegram menu
     */
    public RoutMessage(RowUtil rowUtil) {
        super(rowUtil);
    }

    /**
     * buttons.
     * @return buttons
     */
    public List<AnswerDto> answer() {
        return Arrays.asList(new AnswerDto("От", 1), new AnswerDto("До", 2));
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
    public SendPhoto process(String chatId) {
        return (SendPhoto) postQuestionFirst(
                question(),
                queryId(),
                answer(),
                chatId);
    }
}
