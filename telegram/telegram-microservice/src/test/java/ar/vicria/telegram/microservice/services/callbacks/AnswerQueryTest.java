package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.ConnectionDto;
import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.kafka.producer.SubteRoadTopicKafkaProducer;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AnswerQueryTest {

    @Mock
    private LocalizedTelegramMessageFactory localizedFactory;

    @Mock
    private SubteRoadTopicKafkaProducer kafkaProducer;

    private final RowUtil rowUtil = new RowUtil();

    @Mock
    private RestToSubte rest;

    @Mock
    private StationQuery stationQuery;

//    @BeforeEach
//    public void local() {
//        Locale locale = new Locale("en");
//        Locale.setDefault(locale);
//        LocaleContextHolder.setLocale(locale);
//        LocaleContextHolder.setDefaultLocale(locale);
//        var localizedTelegramMessage = new LocalizedTelegramMessage(locale);
//        Mockito.when(localizedFactory.getLocalized()).thenReturn(localizedTelegramMessage);
//
//    }

    /**
     * All data line by line in resources.
     *
     * @param msg checking 2 types of input in msg
     */
    @ParameterizedTest
    @CsvSource(value = {
            "Route from H\uD83D\uDFE1 Las Heras to B\uD83D\uDD34 Select a station | H\uD83D\uDFE1 Las Heras | B\uD83D\uDD34 Florida",
            "Route from H\uD83D\uDFE1 to B\uD83D\uDD34 Florida Select a station   | H\uD83D\uDFE1 Las Heras | B\uD83D\uDD34 Florida",
            "Route from B\uD83D\uDD34 Florida to H\uD83D\uDFE1 Select a station   | B\uD83D\uDD34 Florida   | H\uD83D\uDFE1 Las Heras",
            "Route from B\uD83D\uDD34 to H\uD83D\uDFE1 Las Heras Select a station | B\uD83D\uDD34 Florida   | H\uD83D\uDFE1 Las Heras",
    }, delimiter = '|')
    void processTest(String msg, String from, String to) {
        Mockito.reset();

        String line1 = from.substring(0, from.indexOf(" "));
        String name1 = from.substring(from.indexOf(" ") + 1);
        String line2 = to.substring(0, to.indexOf(" "));
        String name2 = to.substring(to.indexOf(" ") + 1);

        StationDto stationFrom = new StationDto(line1, name1);
        StationDto stationTo = new StationDto(line2, name2);
        var listDtos = List.of(stationFrom, stationTo);
        Mockito.when(rest.get()).thenReturn(listDtos);

        List<StationDto> route = List.of(stationFrom, stationTo);

        ConnectionDto connection1 = new ConnectionDto(stationTo, stationFrom, 4.0, null);
        List<ConnectionDto> transitions = List.of(connection1);
        RouteDto routeDto = new RouteDto();
        routeDto.setRoute(route);
        routeDto.setTotalTime(19);
        routeDto.setTransitions(transitions);
//        Mockito.when(rest.send(stationFrom, stationTo)).thenReturn(routeDto);


        AnswerQuery answerQuery = new AnswerQuery(rowUtil, kafkaProducer, stationQuery, rest);
        answerQuery.setLocalizedFactory(localizedFactory);

        var msgId = 12;
        var chatId = "444";
        AnswerData answerData = new AnswerData("123", 0);


        List<StationDto> dtoList = List.of(stationFrom, stationTo);
        var directions = dtoList.stream()
                .collect(Collectors.groupingBy(StationDto::getLine, Collectors.toList()));
        Mockito.when(stationQuery.getDirections()).thenReturn(directions);


//        Mockito.when(kafkaProducer.sendDistanceForCounting(distanceDto);)
        var ansToCheck = answerQuery.process(msgId, chatId, msg, answerData);
//
//        EditMessageText editMessageText = new EditMessageText();
//        editMessageText.setText("<b>Route</b>\n" +
//                "from " + line1 + " " + name1 + " \n" +
//                "to " + line2 + " " + name2 + " \n" +
//                "will take 19 minutes" +
//                "\n\n" + name1 + " " + line1
//                + "\n--->Transition--->\n"
//                + name2 + " " + line2 + "\n\n");
//        editMessageText.setParseMode("HTML");
//        editMessageText.setMessageId(12);
//        editMessageText.setChatId("444");
//        List<List<InlineKeyboardButton>> keyboard = List.of();
//        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
//                .keyboard(keyboard)
//                .build();
//        editMessageText.setReplyMarkup(inlineKeyboardMarkup);

//        var expectedAns = Optional.of(editMessageText);

        var expectedAns = Optional.empty();

        Assertions.assertEquals(expectedAns, ansToCheck);

        //todo догадываюсь что этот тест создан не правильно, но как мне можно проверить происходящее внутри кроме как проверять каждый метод внутри метода отдельно
    }

    @Test
    void processTest1() {
        String msg = "Route from H\uD83D\uDFE1 Las Heras to B\uD83D\uDD34 Florida will take 19 minutes";

        //todo Mockito.reset в первом тесте не работает и оно выбивает ошибка за использование ненужного мока, как убрать это и сделать лучше? та де проблема в DetailsQuery
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        LocaleContextHolder.setLocale(locale);
        LocaleContextHolder.setDefaultLocale(locale);
        var localizedTelegramMessage = new LocalizedTelegramMessage(locale);
        Mockito.when(localizedFactory.getLocalized()).thenReturn(localizedTelegramMessage);


        String line1 = "H\uD83D\uDFE1";
        String name1 = "Las Heras";
        String line2 = "B\uD83D\uDD34";
        String name2 = "Florida";

        StationDto stationFrom = new StationDto(line1, name1);
        StationDto stationTo = new StationDto(line2, name2);
        var listDtos = List.of(stationFrom, stationTo);
        Mockito.when(rest.get()).thenReturn(listDtos);

        List<StationDto> route = List.of(stationFrom, stationTo);

        ConnectionDto connection1 = new ConnectionDto(stationTo, stationFrom, 4.0, null);
        List<ConnectionDto> transitions = List.of(connection1);
        RouteDto routeDto = new RouteDto();
        routeDto.setRoute(route);
        routeDto.setTotalTime(19);
        routeDto.setTransitions(transitions);
        Mockito.when(rest.send(stationFrom, stationTo)).thenReturn(routeDto);


        AnswerQuery answerQuery = new AnswerQuery(rowUtil, kafkaProducer, stationQuery, rest);
        answerQuery.setLocalizedFactory(localizedFactory);

        var msgId = 12;
        var chatId = "444";
        AnswerData answerData = new AnswerData("123", 0);


        List<StationDto> dtoList = List.of(stationFrom, stationTo);
        var directions = dtoList.stream()
                .collect(Collectors.groupingBy(StationDto::getLine, Collectors.toList()));
//        Mockito.when(stationQuery.getDirections()).thenReturn(directions);


//        Mockito.when(kafkaProducer.sendDistanceForCounting(distanceDto);)
        var ansToCheck = answerQuery.process(msgId, chatId, msg, answerData);

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText("<b>Route</b>\n" +
                "from " + line1 + " " + name1 + " \n" +
                "to " + line2 + " " + name2 + " \n" +
                "will take 19 minutes" +
                "\n\n" + name1 + " " + line1
                + "\n--->Transition--->\n"
                + name2 + " " + line2 + "\n\n");
        editMessageText.setParseMode("HTML");
        editMessageText.setMessageId(12);
        editMessageText.setChatId("444");
        InlineKeyboardButton button = InlineKeyboardButton.builder()
                .text("Details")
                .callbackData("/answer#AnswerQuery#0")
                .build();
        List<InlineKeyboardButton> row1 = List.of(button);
        List<List<InlineKeyboardButton>> keyboard = List.of(row1);
        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);

        var expectedAns = Optional.of(editMessageText);

        Assertions.assertEquals(expectedAns, ansToCheck);
    }
}
