package ar.vicria.telegram.microservice.services.messages;

import ar.vicria.telegram.microservice.rb.Messages;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerDto;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

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
        Messages ms = Messages.getInitMessage(LocaleContextHolder.getLocale());
        return Arrays.asList(new AnswerDto(ms.getRmessageFrom(), 1), new AnswerDto(ms.getRmessageTo(), 2));
    }

    @Override
    public String question() {
        Messages ms = Messages.getInitMessage(LocaleContextHolder.getLocale());
        return ms.getRmessageSelectDirection();
    }

    @Override
    public boolean supports(String msg) {
        Messages ms = Messages.getInitMessage(LocaleContextHolder.getLocale());
        return msg.equals(ms.getRmessageRoute());
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
