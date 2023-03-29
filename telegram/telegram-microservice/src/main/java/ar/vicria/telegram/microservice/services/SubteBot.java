package ar.vicria.telegram.microservice.services;

import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubteBot {

    private static String ANSWER = "<b>Маршрут:</b>\n    от %s \n    до %s";
    private static String TIME = "\n<b>займет %s минут</b>";
    private static String DISTANCE = "\nподробный маршрут: %s";
    private static String FROM = "-";
    private static String TO = "-";
    String red = "\uD83D\uDD34";
    String yellow = "\uD83D\uDFE1";
    String green = "\uD83D\uDFE2";
    private final RestToSubte rest;


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
        if (FROM != null && TO != null && !TO.equals("-")) {
            FROM = FROM.replaceAll(red,"");
            FROM = FROM.replaceAll(green,"");
            FROM = FROM.replaceAll(yellow,"");
            TO = TO.replaceAll(red,"");
            TO = TO.replaceAll(green,"");
            TO = TO.replaceAll(yellow,"");
            RouteDto send = rest.send(FROM, TO);
            text += String.format(TIME, send.getTotalTime());
            text += String.format(DISTANCE, String.join(" -> ", send.getRoute()));
        }
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
        
        List<String> directions = rest.get().stream()
                .peek(dto -> {
                    String icon = dto.getLine().equals("green") ? green : dto.getLine().equals("yellow") ? yellow : red;
                    dto.setName(dto.getName() + icon);
                })
                .map(StationDto::getName)
                .collect(Collectors.toList());

        return getSendMessage(message, directions);
    }
}
