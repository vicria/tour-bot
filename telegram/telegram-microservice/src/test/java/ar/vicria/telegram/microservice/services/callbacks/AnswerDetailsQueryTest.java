package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.ConnectionDto;
import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.kafka.producer.SubteRoadTopicKafkaProducer;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AnswerDetailsQueryTest {

    @Mock
    private LocalizedTelegramMessageFactory localizedFactory;

    private final RowUtil rowUtil = new RowUtil();

    @Mock
    private RestToSubte rest;

    @Mock
    private SubteRoadTopicKafkaProducer kafkaProducer;


    @BeforeEach
    public void local() {
//        Locale locale = new Locale("en");
//        Locale.setDefault(locale);
//        LocaleContextHolder.setLocale(locale);
//        LocaleContextHolder.setDefaultLocale(locale);
//        var localizedTelegramMessage = new LocalizedTelegramMessage(locale);
//        Mockito.when(localizedFactory.getLocalized()).thenReturn(localizedTelegramMessage);

    }

    @Test
    void processTest() {

        String msg = "Route from E\uD83D\uDFE3 Station1 to D\uD83D\uDFE2 Station5 will take 10 minutes";


        StationDto station1 = new StationDto("E\uD83D\uDFE3", "Station1");
        StationDto station2 = new StationDto("B\uD83D\uDD34", "Station2");
        StationDto station3 = new StationDto("B\uD83D\uDD34", "Station3");
        StationDto station4 = new StationDto("B\uD83D\uDD34", "Station4");
        StationDto station5 = new StationDto("D\uD83D\uDFE2", "Station5");
        List<StationDto> route = List.of(station1, station2,
                station3, station4, station5);
        Mockito.when(rest.get()).thenReturn(route);

        ConnectionDto connection1 = new ConnectionDto(station2, station1, 1.0, null);
        ConnectionDto connection2 = new ConnectionDto(station3, station2, 2.0, station4);
        ConnectionDto connection3 = new ConnectionDto(station4, station3, 3.0, station4);
        ConnectionDto connection4 = new ConnectionDto(station5, station4, 4.0, null);
        ConnectionDto connection5 = new ConnectionDto(station1, station5, 5.0, null);
        List<ConnectionDto> transitions = List.of(connection1, connection4);
        RouteDto routeDto = new RouteDto();
        routeDto.setRoute(route);
        routeDto.setTotalTime(10);
        routeDto.setTransitions(transitions);
//        Mockito.when(rest.send(station1, station5)).thenReturn(routeDto);


        AnswerDetailsQuery answerDetailsQuery = new AnswerDetailsQuery(rowUtil, kafkaProducer, rest);
        answerDetailsQuery.setLocalizedFactory(localizedFactory);

        var msgId = 12;
        var chatId = "444";
        AnswerData answerData = new AnswerData("123", 0);

        var ansToCheck = answerDetailsQuery.process(msgId, chatId, msg, answerData);

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText("<b>Route</b>\n" +
                "from E\uD83D\uDFE3 Station1 \n" +
                "to D\uD83D\uDFE2 Station5 \n" +
                "will take 10 minutes\n" +
                "\n" +
                "detailed route: \n" +
                "E\uD83D\uDFE3 Station1\n" +
                "--->Transition, 1.0 minutes--->\n" +
                "B\uD83D\uDD34 Station2 -> Station3 -> Station4\n" +
                "--->Transition, 4.0 minutes--->\n" +
                "D\uD83D\uDFE2 Station5");
        var expectedAns = Optional.empty();
        Assertions.assertEquals(expectedAns, ansToCheck);
    }


    @Test
    void question() {

        String msg = "Route from E\uD83D\uDFE3 Station1 to D\uD83D\uDFE2 Station5 will take 10 minutes";


        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        LocaleContextHolder.setLocale(locale);
        LocaleContextHolder.setDefaultLocale(locale);
        var localizedTelegramMessage = new LocalizedTelegramMessage(locale);
        Mockito.when(localizedFactory.getLocalized()).thenReturn(localizedTelegramMessage);

        StationDto station1 = new StationDto("E\uD83D\uDFE3", "Station1");
        StationDto station2 = new StationDto("B\uD83D\uDD34", "Station2");
        StationDto station3 = new StationDto("B\uD83D\uDD34", "Station3");
        StationDto station4 = new StationDto("B\uD83D\uDD34", "Station4");
        StationDto station5 = new StationDto("D\uD83D\uDFE2", "Station5");
        List<StationDto> route = List.of(station1, station2,
                station3, station4, station5);
        Mockito.when(rest.get()).thenReturn(route);

        ConnectionDto connection1 = new ConnectionDto(station2, station1, 1.0, null);
        ConnectionDto connection2 = new ConnectionDto(station3, station2, 2.0, station4);
        ConnectionDto connection3 = new ConnectionDto(station4, station3, 3.0, station4);
        ConnectionDto connection4 = new ConnectionDto(station5, station4, 4.0, null);
        ConnectionDto connection5 = new ConnectionDto(station1, station5, 5.0, null);
        List<ConnectionDto> transitions = List.of(connection1, connection4);
        RouteDto routeDto = new RouteDto();
        routeDto.setRoute(route);
        routeDto.setTotalTime(10);
        routeDto.setTransitions(transitions);
        Mockito.when(rest.send(station1, station5)).thenReturn(routeDto);


        AnswerDetailsQuery answerDetailsQuery = new AnswerDetailsQuery(rowUtil, kafkaProducer, rest);
        answerDetailsQuery.setLocalizedFactory(localizedFactory);
//
//        var msgId = 12;
//        var chatId = "444";
//        AnswerData answerData = new AnswerData("123", 0);

        RoutMsg request = new RoutMsg(msg);

        var ansToCheck = answerDetailsQuery.question(request, new RouteDto());

        String expectedAns = "<b>Route</b>\n" +
                "from E\uD83D\uDFE3 Station1 \n" +
                "to D\uD83D\uDFE2 Station5 \n" +
                "will take 10 minutes\n" +
                "\n" +
                "detailed route: \n" +
                "E\uD83D\uDFE3 Station1\n" +
                "--->Transition, 1.0 minutes--->\n" +
                "B\uD83D\uDD34 Station2 -> Station3 -> Station4\n" +
                "--->Transition, 4.0 minutes--->\n" +
                "D\uD83D\uDFE2 Station5";
//        var expectedAns = Optional.of(editMessageText);


        Assertions.assertEquals(expectedAns, ansToCheck);

    }
}
