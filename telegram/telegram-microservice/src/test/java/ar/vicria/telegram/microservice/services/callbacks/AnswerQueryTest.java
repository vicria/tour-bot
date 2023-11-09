package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.messages.RoutMessage;
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
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AnswerQueryTest {

//    @InjectMocks
    private AnswerQuery answerQuery;

    @Mock
    LocalizedTelegramMessageFactory localizedFactory;

//    RoutMsg routMsg = new RoutMsg();
//    RestToSubte restToSubte = new RestToSubte(null);
//    BranchQuery branchQuery = new BranchQuery(null, null, null);
//    @Mock
//    StationQuery stationQuery = Mockito.mock(StationQuery.class);

    private StationQuery stationQuery;




    @BeforeEach
    public void local() {
        Locale locale = new Locale("RU");
        var localizedTelegramMessage = new LocalizedTelegramMessage(locale);
        Mockito.when(localizedFactory.getLocalized()).thenReturn(localizedTelegramMessage);

        RowUtil rowUtil = new RowUtil();

        RestTemplate restTemplate = new RestTemplate();
        RoutMessage routMessage = new RoutMessage(rowUtil);


        RestToSubte restToSubte = new RestToSubte(restTemplate);
        BranchQuery branchQuery = new BranchQuery(rowUtil, restToSubte, routMessage);


        stationQuery = new StationQuery(rowUtil, restToSubte, branchQuery);

    }
//    @ParameterizedTest
//    @CsvSource({
//            "AnswerQuery,<b>Маршрут:</b> от \uD83D\uDD34 Станция до \uD83D\uDD34 Станция Выберите,AnswerDetailsQuery",
//            "AnswerDetailsQuery,<b>Маршрут:</b> от \uD83D\uDD34 Станция до \uD83D\uDD34 Станция Выберите,AnswerQuery",
//    })
    @Test
    void processTest(){

        RoutMsg routMsg = new RoutMsg();
        routMsg.setLineFrom("1");
        routMsg.setLineTo("2");
        routMsg.setStationFrom("3");
        routMsg.setStationTo("4");

        var msgId = 123;
        var chatId = "444";
        var msg = "message";
        AnswerData answerData = new AnswerData("123" , 123);

        StationDto dto1 = new StationDto("линия1", "имя1");
        StationDto dto2 = new StationDto("линия2", "имя2");
        StationDto dto3 = new StationDto("линия3", "имя3");
        List<StationDto> dtoList = List.of(dto1, dto2, dto3);
        var directions = dtoList.stream()
                .collect(Collectors.groupingBy(StationDto::getLine, Collectors.toList()));


        Mockito.when(stationQuery.getDirections()).thenReturn(directions);
        var ans = answerQuery.process(msgId, chatId , msg, answerData);
        log.info(String.valueOf(ans));
        Assertions.assertEquals("123", ans);

    }
}
