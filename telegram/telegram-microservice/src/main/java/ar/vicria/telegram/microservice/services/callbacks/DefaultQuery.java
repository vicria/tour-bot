package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerDto;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Use for default.
 */
@Component
public class DefaultQuery extends Query {

    public DefaultQuery(RowUtil rowUtil,
                        LocalizedTelegramMessageFactory factory) {
        super(rowUtil, factory);
    }

    @Override
    public boolean supports(AnswerData answerData, String msg) {
        return true;
    }

    @Override
    public String question(Object request) {
        return request.toString();
    }

    @Override
    public List<AnswerDto> answer(String... option) {
        return new ArrayList<>();
    }

    @Override
    public Optional<BotApiMethod> process(Integer msgId, String chatId, String msg, AnswerData answerData) {
        return Optional.ofNullable(postQuestionEdit(msgId, question(new RoutMsg(msg)), queryId(), answer(), chatId));
    }
}
