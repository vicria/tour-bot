package ar.vicria.telegram.microservice.services.messages;

import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Arrays;
import java.util.List;

/**
 * Text msg after /start.
 */
@Component
public class StartMessage extends TextMessage {

    /**
     * Constrictor.
     * @param rowUtil util for telegram menu
     */
    protected StartMessage(RowUtil rowUtil) {
        super(rowUtil);
    }

    @Override
    public boolean supports(String msg) {
        return msg.equals("/start");
    }

    @Override
    public SendMessage process(String chatId) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(question())
                .build();
        return sendMessage(message, answer());
    }

    @Override
    public String question() {
        return "Меню Subte";
    }

    /**
     * buttons.
     * @return buttons
     */
    public List<String> answer() {
        return Arrays.asList("Маршрут", "Обратная связь", "О возможностях бота");
    }
}
