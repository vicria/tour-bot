package ar.vicria.telegram.microservice.services;

import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubteBot {

    private static String ANSWER = "<b>Маршрут:</b>\n    от %s \n    до %s";
    private static String TIME = "\n<b>займет %s минут</b>";
    private static String DISTANCE = "\nподробный маршрут: %s";
    private static String LAST = "\n<b>Последняя станция</b> направления: %s";
    private static StationDto FROM = new StationDto();
    private static StationDto TO = new StationDto();
    private String red = "\uD83D\uDD34";
    private String yellow = "\uD83D\uDFE1";
    private String green = "\uD83D\uDFE2";
    private final RestToSubte rest;
    private List<StationDto> stationDtos;
    private Map<String, List<StationDto>> directions;
    private List<String> lines;
    private String line;

    @PostConstruct
    private void init() {
        stationDtos = rest.get().stream()
                .peek(dto -> {
                    String icon = dto.getLine().equals("green") ? green : dto.getLine().equals("yellow") ? yellow : red;
                    dto.setLine(icon);
                })
                .collect(Collectors.toList());
        directions = stationDtos.stream()
                .collect(Collectors.groupingBy(StationDto::getLine, Collectors.toList()));
        lines = stationDtos.stream()
                .map(StationDto::getLine).distinct().collect(Collectors.toList());
    }


    SendMessage process(String msg, String chatId) {
        if (msg.equals("/start")) {
            return answer(chatId);
        } else if (msg.equals("Маршрут")){
            return postMenu(chatId);
        }
        return  SendMessage.builder()
                .chatId(chatId)
                .text("Выберите пункт из меню")
                .build();
    }

    SendMessage answer(String chatId) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("Меню Subte")
                .build();
        List<String> menu = Arrays.asList("Маршрут", "Обратная связь", "О возможностях бота");
        return getSendMessage(message, new ArrayList<>(menu));
    }

    EditMessageText answer2(Integer msgId, String chatId) {
        String from = FROM != null && Optional.ofNullable(FROM.getName()).isPresent() ? FROM.toString() : "-";
        String to = TO != null && Optional.ofNullable(TO.getName()).isPresent() ? TO.toString() : "-";
        String text = String.format(ANSWER, from, to);

        if (FROM != null && TO != null
                && Optional.ofNullable(FROM.getName()).isPresent()
                && Optional.ofNullable(TO.getName()).isPresent()) {
            RouteDto send = rest.send(FROM.getName(), TO.getName());
            text += String.format(TIME, send.getTotalTime());
            text += String.format(DISTANCE, String.join(" -> ", send.getRoute()));
            text += String.format(LAST, send.getLastStation());
        }
        EditMessageText editMessageText = EditMessageText.builder()
                .messageId(msgId)
                .chatId(chatId)
                .text(text)
                .build();

        editMessageText.setText(text);
        editMessageText.setParseMode("HTML");
        return editMessageText;
    }

    BotApiMethod processQuery(AnswerData answerData, Integer msgId, String station, String chatId) {
        if (answerData.getQuestionId().equals("1") && answerData.getAnswerCode() == 1) {
            FROM = null;
            return postBranches(msgId, chatId);
        } else if (answerData.getQuestionId().equals("1") && answerData.getAnswerCode() == 2) {
            TO = null;
            return postBranches(msgId, chatId);
        } else if (answerData.getQuestionId().equals("2")) {
            line = lines.get(answerData.getAnswerCode());
            return postStations(msgId, chatId);
        } else {
            StationDto stationDto = directions.get(line).get(answerData.getAnswerCode());
            if (FROM == null) {
                FROM = stationDto;
            }
            if (TO == null) {
                TO = stationDto;
            }
            if (Optional.ofNullable(FROM.getName()).isPresent() && Optional.ofNullable(TO.getName()).isPresent()) {
                EditMessageText messageText = answer2(msgId, chatId);
                FROM = new StationDto();
                TO = new StationDto();
                return messageText;
            } else {
                return editMenu(msgId, chatId);
            }
        }
    }

    /**
     * меню по линиям метро
     */
    public SendMessage postMenu(String chatId) {
        return postQuestionFirst(
                "Выберите направление",
                "1",
                Arrays.asList(new Answer("От", 1), new Answer("До", 2)),
                chatId);
    }

    /**
     * меню по линиям метро
     */
    private EditMessageText editMenu(Integer msgId, String chatId) {
        String from = FROM != null && Optional.ofNullable(FROM.getName()).isPresent() ? FROM.toString() : "-";
        String to = TO != null && Optional.ofNullable(TO.getName()).isPresent() ? TO.toString() : "-";
        String text = String.format(ANSWER, from, to);
        return postQuestionEdit(msgId,
                text + "\nВыберите направление",
                "1",
                Arrays.asList(new Answer("От", 1), new Answer("До", 2)),
                chatId);
    }

    public SendMessage postQuestionFirst(String questionText,
                                         String questionId,
                                         List<Answer> answers,
                                         String chatId) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(questionText)
                .build();

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        answers.forEach(answer -> {
            InlineKeyboardButton button = InlineKeyboardButton.builder()
                    .text(answer.getText())
                    .callbackData(AnswerData.serialize(questionId, answer))
                    .build();
            rows.add(Collections.singletonList(button));
        });

        markupInline.setKeyboard(rows);
        message.setReplyMarkup(markupInline);
        return message;
    }

    public EditMessageText postQuestionEdit(Integer messageId,
                                            String questionText,
                                            String questionId,
                                            List<Answer> answers,
                                            String chatId) {
        EditMessageText editMessageText = EditMessageText.builder()
                .messageId(messageId)
                .chatId(chatId)
                .text(questionText)
                .build();
        editMessageText.setParseMode("HTML");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        answers.forEach(answer -> {
            InlineKeyboardButton button = InlineKeyboardButton.builder()
                    .text(answer.getText())
                    .callbackData(AnswerData.serialize(questionId, answer))
                    .build();
            rows.add(Collections.singletonList(button));
        });

        markupInline.setKeyboard(rows);
        editMessageText.setReplyMarkup(markupInline);
        return editMessageText;
    }

    private SendMessage getSendMessage(SendMessage message, List<String> buttonNames) {
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

    private SendMessage getButtons(SendMessage message, List<String> buttonNames) {
        List<KeyboardRow> rows = new ArrayList<>();
        for (String button : buttonNames) {
            KeyboardRow row = new KeyboardRow();
            row.add(KeyboardButton.builder().text(button).build());
            rows.add(row);
        }

        message.setReplyMarkup(new ReplyKeyboardMarkup(rows));
        message.setText("menu");
        message.setParseMode("HTML");

        return message;
    }

    /**
     * меню по линиям метро
     */
    private EditMessageText postBranches(Integer msgId, String chatId) {
        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            Answer answer = new Answer(lines.get(i), i);
            answers.add(answer);
        }
        return postQuestionEdit(msgId, "Выберите ветку", "2", answers, chatId);
    }

    /**
     * меню по станциям метро
     */
    private EditMessageText postStations(Integer msgId, String chatId) {
        List<@NotBlank String> collect = directions.get(line).stream()
                .map(StationDto::getName)
                .collect(Collectors.toList());

        List<Answer> answers = new ArrayList<>();
        for (String station : collect) {
            Answer answer = new Answer(station, collect.indexOf(station));
            answers.add(answer);
        }

        return postQuestionEdit(msgId, "Выберите станцию", "3", answers, chatId);
    }
}
