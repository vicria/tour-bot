package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.ConnectionDto;
import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AnswerQueryTest {

    @Mock
    private LocalizedTelegramMessageFactory localizedFactory;

    private final RowUtil rowUtil = new RowUtil();

    @Mock
    private RestToSubte rest;

    @Mock
    private StationQuery stationQuery;


    @BeforeEach
    public void local() {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        LocaleContextHolder.setLocale(locale);
        LocaleContextHolder.setDefaultLocale(locale);
        var localizedTelegramMessage = new LocalizedTelegramMessage(locale);
        Mockito.when(localizedFactory.getLocalized()).thenReturn(localizedTelegramMessage);

    }

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
        Mockito.when(rest.send(stationFrom, stationTo)).thenReturn(routeDto);


        AnswerQuery answerQuery = new AnswerQuery(rowUtil, stationQuery, rest);
        answerQuery.setLocalizedFactory(localizedFactory);

        var msgId = 12;
        var chatId = "444";
        AnswerData answerData = new AnswerData("123", 0);


        List<StationDto> dtoList = List.of(stationFrom, stationTo);
        var directions = dtoList.stream()
                .collect(Collectors.groupingBy(StationDto::getLine, Collectors.toList()));
        Mockito.when(stationQuery.getDirections()).thenReturn(directions);
        var ansToCheck = answerQuery.process(msgId, chatId, msg, answerData);

        String expectedAns = "<b>Route</b>\n" +
                "from " + line1 + " " + name1 + " \n" +
                "to " + line2 + " " + name2 + " \n" +
                "will take 19 minutes" +
                "\n\n" + name1 + " " + line1
                + "\n--->Transition--->\n"
                + name2 + " " + line2 + "\n\n";
        Assertions.assertEquals(expectedAns, ansToCheck.getText());

    }
}
