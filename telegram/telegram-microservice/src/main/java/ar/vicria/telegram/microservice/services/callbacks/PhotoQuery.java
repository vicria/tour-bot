package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerDto;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.List;

public class PhotoQuery extends Query{
    /**
     * Constrictor.
     *
     * @param rowUtil util for telegram menu
     */
    protected PhotoQuery(RowUtil rowUtil) {
        super(rowUtil);
    }

    @Override
    public boolean supports(AnswerData answerData, String msg) {
        return false;
    }

    @Override
    String question(RoutMsg request) {
        return null;
    }

    @Override
    List<AnswerDto> answer(String... option) {
        return null;
    }

    @Override
    public EditMessageText process(Integer msgId, String chatId, String msg, AnswerData answerData) {
        return null;
    }
}
