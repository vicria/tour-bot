package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.properties.TelegramProperties;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.kafka.producer.SubteRoadTopicKafkaProducer;
import ar.vicria.telegram.microservice.services.messages.RoutMessage;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class QueryTest {

    @Mock
    public RestTemplate restTemplate;

    private TelegramProperties properties = new TelegramProperties();

    @Mock
    public LocalizedTelegramMessageFactory factory;

    @Mock
    public SubteRoadTopicKafkaProducer kafkaProducer;

    private Query answerDetailsQuery;
    private BranchQuery branchQuery;
    private StationQuery stationQuery;
    private DefaultQuery defaultQuery;
    private Query answerQuery;

    @BeforeEach
    void local() {
        LocaleContextHolder.setLocale(Locale.forLanguageTag("ru"));

        factory = mock(LocalizedTelegramMessageFactory.class);
        var localizedTelegramMessage = new LocalizedTelegramMessage(Locale.forLanguageTag("ru"));
        when(factory.getLocalized()).thenReturn(localizedTelegramMessage);

        ResponseEntity<StationDto[]> responseEntity = new ResponseEntity<>(new StationDto[]{}, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(StationDto[].class))).thenReturn(responseEntity);

        RouteDto routeDto = new RouteDto();
        routeDto.setTotalTime(5);
        routeDto.setRoute(Arrays.asList(new StationDto("", "Станция"), new StationDto("", "станция2")));
        ResponseEntity<RouteDto> responseEntity2 = new ResponseEntity<>(routeDto, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(RouteDto.class))).thenReturn(responseEntity2);
        properties.setSubteGet("http://localhost:8082/stations/all");
        RestToSubte restToSubte = new RestToSubte(restTemplate, properties);
        RowUtil rowUtil = new RowUtil();
        RoutMessage routMessage = new RoutMessage(rowUtil, factory);


        answerDetailsQuery = new AnswerDetailsQuery(rowUtil, kafkaProducer, restToSubte, factory);
        branchQuery = new BranchQuery(rowUtil, restToSubte, routMessage, factory);
        stationQuery = new StationQuery(rowUtil, restToSubte, branchQuery, factory);
        defaultQuery = new DefaultQuery(rowUtil, factory);
        answerQuery = new AnswerQuery(rowUtil, kafkaProducer, stationQuery, restToSubte, factory);
    }

    /**
     * All data line by line in resources.
     *
     * @param id   last class sent msg
     * @param msg  last msg from user
     * @param name next class after press
     */
    @ParameterizedTest
    @CsvSource(value = {
            "AnswerQuery        | msg                                                    | AnswerDetailsQuery",
            "AnswerDetailsQuery | msg                                                    | AnswerQuery",
            "StationQuery       | <b>Маршрут:</b> от \uD83D\uDD34 "
                                + "Станция до \uD83D\uDD34 Станция Выберите              | AnswerQuery",
            "RoutMessage        |rout                                                    | BranchQuery",
            "StationQuery       |<b>Маршрут:</b> от \uD83D\uDD34 Выберите                | BranchQuery",
            "StationQuery       |<b>Маршрут:</b> до \uD83D\uDD34 Выберите                | BranchQuery",
            "BranchQuery        |<b>Маршрут:</b> от \uD83D\uDD34 Станция до - Выберите   | StationQuery",
            "BranchQuery        |<b>Маршрут:</b> от - до \uD83D\uDD34 Станция Выберите   | StationQuery",
            "BranchQuery        |branch                                                  | StationQuery",
            "123                |msg                                                     | DefaultQuery",
    }, delimiter = '|')
    public void supports(String id, String msg, String name) {
        LocaleContextHolder.setLocale(Locale.forLanguageTag("ru"));
        var testData = new AnswerData(id, 0);
        supports(testData, msg, name);
    }

    /**
     * All data line by line in resources.
     *
     * @param id  last class sent msg
     * @param msg last msg from user
     */
    @ParameterizedTest
    @CsvSource({
            "AnswerQuery,<b>Маршрут:</b> от \uD83D\uDD34 Станция до \uD83D\uDD34 Станция Выберите,AnswerDetailsQuery",
    })
    public void process(String id, String msg) {
        var testData = new AnswerData(id, 0);
        process(testData, msg);
    }

    /**
     * All classes for query
     *
     * @return all
     */
    List<Query> allQuery() {
        RowUtil rowUtil = new RowUtil();

        RestTemplate restTemplate = mock(RestTemplate.class);
        ResponseEntity<StationDto[]> responseEntity = new ResponseEntity<>(new StationDto[]{}, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(StationDto[].class))).thenReturn(responseEntity);

        RouteDto routeDto = new RouteDto();
        routeDto.setTotalTime(5);
        routeDto.setRoute(Arrays.asList(new StationDto("", "Станция"), new StationDto("", "станция2")));
        ResponseEntity<RouteDto> responseEntity2 = new ResponseEntity<>(routeDto, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(RouteDto.class))).thenReturn(responseEntity2);

        RestToSubte restToSubte = new RestToSubte(restTemplate, properties);
        RoutMessage routMessage = new RoutMessage(rowUtil, factory);

        Query answerDetailsQuery = new AnswerDetailsQuery(rowUtil, kafkaProducer, restToSubte, factory);
        BranchQuery branchQuery = new BranchQuery(rowUtil, restToSubte, routMessage, factory);
        StationQuery stationQuery = new StationQuery(rowUtil, restToSubte, branchQuery, factory);
        DefaultQuery defaultQuery = new DefaultQuery(rowUtil, factory);
        Query answerQuery = new AnswerQuery(rowUtil, kafkaProducer, stationQuery, restToSubte, factory);

        return new ArrayList<>(List.of(answerDetailsQuery, answerQuery, branchQuery, stationQuery, defaultQuery));
    }

    /**
     * All data, what pressed user and what class in the end
     *
     * @param data      in the button
     * @param msg       query
     * @param queryName class after filter
     */
    void supports(AnswerData data, String msg, String queryName) {
        List<Query> queries = allQuery();
        Query query1 = queries.stream()
                .filter(query -> query.supports(data, msg))
                .findFirst()
                .get(); //have default
        String name = query1.getClass().getName();
        String[] split = name.split("\\.");
        assertEquals(queryName, split[split.length - 1]);
    }

    /**
     * All data, what pressed user and what class in the end
     *
     * @param data in the button
     * @param msg  query
     */
    void process(AnswerData data, String msg) {
        List<Query> queries = allQuery();
        Optional<BotApiMethod> edit = queries.stream()
                .filter(query -> query.supports(data, msg))
                .findFirst()
                .map(query -> query.process(1, "chatId", msg, data))
                .get(); //have default

        assertEquals(Optional.empty(), edit);
    }

}