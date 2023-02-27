package ar.vicria.telegram.microservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubteBot {

    private static String ANSWER = "<b>Маршрут:</b>\n    от %s \n    до %s \n<b>займет 15 минут</b>";
    private static String FROM = "-";
    private static String TO = "-";


    SendMessage process(String msg, String chatId) {
        SendMessage send;
        if (msg.equals("От")) {
            FROM = null;
            send = postBranches(chatId);
        } else if (msg.equals("До")) {
            TO = null;
            send = postBranches(chatId);
        } else {
            if (FROM == null) {
                FROM = msg;
            }
            if (TO == null) {
                TO = msg;
            }
            send = postMenu(chatId);
        }

        return send;
    }

    /**
     * меню по линиям метро
     */
    private SendMessage postMenu(String chatId) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("text")
                .build();

        List<String> directions = new ArrayList<>();
        directions.add("От");
        directions.add("До");

        return getSendMessage(message, directions);
    }

    private SendMessage getSendMessage(SendMessage message, List<String> buttonNames) {
        List<KeyboardRow> rows = new ArrayList<>();
        for (String button : buttonNames) {
            KeyboardRow row = new KeyboardRow();
            row.add(KeyboardButton.builder().text(button).build());
            rows.add(row);
        }

        message.setReplyMarkup(new ReplyKeyboardMarkup(rows));
        String text = String.format(ANSWER, FROM != null ? FROM : "-", TO != null ? TO : "-");
        message.setText(text);
        message.setParseMode("HTML");

        return message;
    }

    /**
     * меню по линиям метро
     */
    private SendMessage postBranches(String chatId) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("text")
                .build();

        List<String> directions = new ArrayList<>();
        directions.add("Медрано \uD83D\uDD34");
        directions.add("Лас Эрас \uD83D\uDFE1");
        directions.add("Санта Фэ \uD83D\uDFE1\uD83D\uDFE2");

        return getSendMessage(message, directions);
    }
}
