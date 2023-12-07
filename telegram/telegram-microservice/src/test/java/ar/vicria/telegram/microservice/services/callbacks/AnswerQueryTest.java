package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.kafka.producer.SubteRoadTopicKafkaProducer;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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

    /**
     * All data line by line in resources.
     */
    @Test
    void processTest() {
        String msg = "Route from H\uD83D\uDFE1 Las Heras to B\uD83D\uDD34 Select a station";
        String from = "H\uD83D\uDFE1 Las Heras";
        String to = "B\uD83D\uDD34 Florida";

        String line1 = from.substring(0, from.indexOf(" "));
        String name1 = from.substring(from.indexOf(" ") + 1);
        String line2 = to.substring(0, to.indexOf(" "));
        String name2 = to.substring(to.indexOf(" ") + 1);

        StationDto stationFrom = new StationDto(line1, name1);
        StationDto stationTo = new StationDto(line2, name2);
        var listDtos = List.of(stationFrom, stationTo);
        Mockito.when(rest.get()).thenReturn(listDtos);

        AnswerQuery answerQuery = new AnswerQuery(rowUtil, kafkaProducer, stationQuery, rest);
        answerQuery.setLocalizedFactory(localizedFactory);

        var msgId = 12;
        var chatId = "444";
        AnswerData answerData = new AnswerData("123", 0);


        List<StationDto> dtoList = List.of(stationFrom, stationTo);
        var directions = dtoList.stream()
                .collect(Collectors.groupingBy(StationDto::getLine, Collectors.toList()));
        Mockito.when(stationQuery.getDirections()).thenReturn(directions);
        var ansToCheck = answerQuery.process(msgId, chatId, msg, answerData);

        //ответ приходит отдельно, когда отреагирует модуль subte
        Assertions.assertEquals(Optional.empty(), ansToCheck);

    }
}
