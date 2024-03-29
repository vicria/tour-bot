package ar.vicria.telegram.microservice.services.messages;

import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.Localized;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerDto;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for responding on text messages.
 */
public abstract class TextMessage implements Localized {

    private final LocalizedTelegramMessageFactory localizedTelegramMessageFactory;

    /**
     * id for discussion and answers.
     *
     * @return id
     */
    public String queryId() {
        return this.getClass().getSimpleName();
    }

    private final RowUtil rowUtil;

    /**
     * LocalizedTelegramMessageFactory.
     *
     * @return LocalizedTelegramMessageFactory
     */
    @Override
    public LocalizedTelegramMessageFactory localizedFactory() {
        return localizedTelegramMessageFactory;
    }


    /**
     * Constrictor.
     *
     * @param rowUtil                         util for telegram menu
     * @param localizedTelegramMessageFactory localizedTelegramMessageFactory
     */
    protected TextMessage(RowUtil rowUtil, LocalizedTelegramMessageFactory localizedTelegramMessageFactory) {
        this.localizedTelegramMessageFactory = localizedTelegramMessageFactory;
        this.rowUtil = rowUtil;
    }

    /**
     * Rule for search a class.
     *
     * @param msg not required
     * @return use this class or not
     */
    public abstract boolean supports(String msg);

    /**
     * Generation a message for user.
     *
     * @param chatId number of user chat
     * @return message for sending
     */
    public abstract SendMessage process(String chatId);

    /**
     * text in message.
     *
     * @return text
     */
    abstract String question();

    /**
     * default method for query message.
     *
     * @param questionText text in msg
     * @param questionId   id for discussion and answers
     * @param answers      buttons
     * @param chatId       number of user chat
     * @return msg
     */
    SendMessage postQuestionFirst(String questionText,
                                  String questionId,
                                  List<AnswerDto> answers,
                                  String chatId) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(questionText)
                .build();

        InlineKeyboardMarkup rows = rowUtil.createRows(answers, questionId);
        message.setReplyMarkup(rows);
        return message;
    }

    /**
     * Simple msg for user.
     *
     * @param message     text in msg
     * @param buttonNames text on buttons
     * @return msg
     */
    SendMessage sendMessage(SendMessage message, List<String> buttonNames) {
        List<KeyboardRow> rows = new ArrayList<>();
        for (String button : buttonNames) {
            KeyboardRow row = new KeyboardRow();
            row.add(KeyboardButton.builder().text(button).build());
            rows.add(row);
        }

        message.setReplyMarkup(new ReplyKeyboardMarkup(rows));
        message.setParseMode("HTML");
        return message;
    }
}
