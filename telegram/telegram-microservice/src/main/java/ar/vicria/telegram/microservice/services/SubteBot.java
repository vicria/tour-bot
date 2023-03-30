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

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
    private Set<String> lines;
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
                .map(StationDto::getLine)
                .collect(Collectors.toSet());
    }


    SendMessage process(String msg, String chatId) {
        SendMessage send;
        if (msg.equals("От")) {
            FROM = null;
            send = postBranches(chatId);
        } else if (msg.equals("До")) {
            TO = null;
            send = postBranches(chatId);
        } else if (lines.contains(msg)) {
            line = msg;
            send = postStations(chatId);
        } else {
            if (FROM == null) {
                FROM = findByNameAndLine(msg, line);
            }
            if (TO == null) {
                TO = findByNameAndLine(msg, line);
            }
            send = postMenu(chatId);
        }

        return send;
    }

    private StationDto findByNameAndLine(String name, String line) {
        return stationDtos.stream()
                .filter(stationDto -> stationDto.getName().equals(name))
                .filter(stationDto -> stationDto.getLine().equals(line))
                .findAny()
                .orElseThrow(() -> new RuntimeException("This station does not exist"));
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

        return getSendMessage(message, new ArrayList<>(lines));
    }

    /**
     * меню по станциям метро
     */
    private SendMessage postStations(String chatId) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("text")
                .build();

        List<@NotBlank String> collect = directions.get(line).stream()
                .map(StationDto::getName)
                .collect(Collectors.toList());
        return getSendMessage(message, collect);
    }
}
