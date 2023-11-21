package ar.vicria.telegram.microservice.services.callbacks;

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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

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
            "Route from H\uD83D\uDFE1 Las Heras to B\uD83D\uDD34 Select a station",
            "Route from H\uD83D\uDFE1 to B\uD83D\uDD34 Florida Select a station",
    })
    void processTest(String msg) {
        StationDto from = new StationDto("H\uD83D\uDFE1", "Las Heras");
        StationDto to = new StationDto("B\uD83D\uDD34", "Florida");
        var listDtos = List.of(from, to);
        Mockito.when(rest.get()).thenReturn(listDtos);

        RouteDto routeDto = new RouteDto();
        routeDto.setTotalTime(19);
        Mockito.when(rest.send(from, to)).thenReturn(routeDto);

        AnswerQuery answerQuery = new AnswerQuery(rowUtil, stationQuery, rest);
        answerQuery.setLocalizedFactory(localizedFactory);

        var msgId = 12;
        var chatId = "444";
        AnswerData answerData = new AnswerData("123" , 0);


        List<StationDto> dtoList = List.of(from, to);
        var directions = dtoList.stream()
                .collect(Collectors.groupingBy(StationDto::getLine, Collectors.toList()));
        Mockito.when(stationQuery.getDirections()).thenReturn(directions);
        var ansToCheck = answerQuery.process(msgId, chatId , msg, answerData);

        EditMessageText expectedAns = new EditMessageText();
        expectedAns.setText("<b>Route</b>\n" +
                "from H\uD83D\uDFE1 Las Heras \n" +
                "to B\uD83D\uDD34 Florida \n" +
                "will take 19 minutes");
        Assertions.assertEquals(expectedAns.getText(), ansToCheck.getText());

    }
}
