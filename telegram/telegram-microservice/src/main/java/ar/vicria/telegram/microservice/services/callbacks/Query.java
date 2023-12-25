package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.Localized;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerDto;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;
import java.util.Optional;

/**
 * Base class for responding on callback query messages.
 * @param <T> type for creating text msg.
 */
public abstract class Query<T> implements Localized {

    /**
     * id for discussion and answers.
     *
     * @return id
     */
    public String queryId() {
        return this.getClass().getSimpleName();
    }

    private final RowUtil rowUtil;
    private final LocalizedTelegramMessageFactory localizedTelegramMessageFactory;

    @Override
    public LocalizedTelegramMessageFactory localizedFactory() {
        return localizedTelegramMessageFactory;
    }

    /**
     * Constrictor.
     *
     * @param rowUtil util for telegram menu
     * @param localizedTelegramMessageFactory localized telegram message factory.
     */
    protected Query(RowUtil rowUtil,
                    LocalizedTelegramMessageFactory localizedTelegramMessageFactory) {
        this.rowUtil = rowUtil;
        this.localizedTelegramMessageFactory = localizedTelegramMessageFactory;
    }

    /**
     * Rule for search a class.
     *
     * @param msg        not required
     * @param answerData answer in query, what the button pressed
     * @return use this class or not
     */
    public abstract boolean supports(AnswerData answerData, String msg);

    /**
     * text in message.
     *
     * @param request what application know about rout now
     * @return text
     */
    abstract String question(T request);

    /**
     * Buttons.
     *
     * @param option condition for choosing buttons names
     * @return text and numbers of buttons
     */
    abstract List<AnswerDto> answer(String... option);

    EditMessageText postQuestionEdit(Integer messageId,
                                     String questionText,
                                     String questionId,
                                     List<AnswerDto> answers,
                                     String chatId) {
        EditMessageText editMessageText = EditMessageText.builder()
                .messageId(messageId)
                .chatId(chatId)
                .text(questionText)
                .build();

        InlineKeyboardMarkup rows = rowUtil.createRows(answers, questionId);
        editMessageText.setParseMode("HTML");
        editMessageText.setReplyMarkup(rows);
        return editMessageText;
    }

    /**
     * Generation a message for user.
     *
     * @param chatId     number of user chat
     * @param answerData user pressed the button
     * @param msg        new msg for user
     * @param msgId      for message must to edit
     * @return message for sending
     */
    public abstract Optional<BotApiMethod> process(Integer msgId, String chatId, String msg, AnswerData answerData);

    public EditMessageText createEditMsg(Integer msgId, T response, String chatId) {
        return postQuestionEdit(msgId, question(response), queryId(), answer(), chatId);
    }
}
