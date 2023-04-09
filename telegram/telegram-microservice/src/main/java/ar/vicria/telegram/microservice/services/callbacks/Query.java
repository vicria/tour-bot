package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.telegram.microservice.services.Answer;
import ar.vicria.telegram.microservice.services.AnswerData;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

public abstract class Query {

    public String queryId() {
        return this.getClass().getSimpleName();
    }

    private final RowUtil rowUtil;

    protected Query(RowUtil rowUtil) {
        this.rowUtil = rowUtil;
    }

    public abstract boolean supports(AnswerData answerData, String msg);

    abstract String question(RoutMsg request);

    abstract List<Answer> answer(String... option);

    EditMessageText postQuestionEdit(Integer messageId,
                                     String questionText,
                                     String questionId,
                                     List<Answer> answers,
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

    public abstract EditMessageText process(Integer msgId, String chatId, String msg, AnswerData answerData);

}
