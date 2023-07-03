package ar.vicria.telegram.microservice.services.messages;

import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerDto;
import ar.vicria.telegram.microservice.services.util.RowUtil;
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
        LocalizedTelegramMessage localized = localizedFactory.getLocalized();
        return Arrays.asList(new AnswerDto(localized.getButtonFrom(), 1), new AnswerDto(localized.getButtonTo(), 2));
    }

    @Override
    public String question() {
        LocalizedTelegramMessage localized = localizedFactory.getLocalized();
        return localized.getTextSelectDirection();
    }

    @Override
    public boolean supports(String msg) {
        LocalizedTelegramMessage localized = localizedFactory.getLocalized();
        return msg.equals(localized.getButtonRoute());
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
