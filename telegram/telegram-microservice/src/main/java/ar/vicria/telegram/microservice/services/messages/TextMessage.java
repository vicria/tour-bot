package ar.vicria.telegram.microservice.services.messages;

import ar.vicria.telegram.microservice.services.Answer;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public abstract class TextMessage {


    public String queryId() {
        return this.getClass().getSimpleName();
    }

    private final RowUtil rowUtil;

    protected TextMessage(RowUtil rowUtil) {
        this.rowUtil = rowUtil;
    }

    public abstract boolean supports(String msg);

    public abstract SendMessage process(String chatId);

    abstract String question();

    SendMessage postQuestionFirst(String questionText,
                                  String questionId,
                                  List<Answer> answers,
                                  String chatId) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(questionText)
                .build();

        InlineKeyboardMarkup rows = rowUtil.createRows(answers, questionId);
        message.setReplyMarkup(rows);
        return message;
    }

    SendMessage getSendMessage(SendMessage message, List<String> buttonNames) {
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
