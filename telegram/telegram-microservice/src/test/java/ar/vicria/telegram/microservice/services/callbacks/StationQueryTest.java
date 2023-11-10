package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.messages.RoutMessage;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StationQueryTest {

    @Mock
    public RestTemplate restTemplate;

    @Mock
    public LocalizedTelegramMessageFactory factory;

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

        StationDto stationDto1 = new StationDto("линия1", "станция1");
        StationDto stationDto2 = new StationDto("линия1", "станция2");
        StationDto stationDto3 = new StationDto("линия2", "станция1");
        StationDto stationDto4 = new StationDto("линия3", "станция1");

        ResponseEntity<StationDto[]> responseEntity = new ResponseEntity<>(new StationDto[]
                {stationDto1, stationDto2, stationDto3, stationDto4}, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(StationDto[].class))).thenReturn(responseEntity);

        RouteDto routeDto = new RouteDto();
        routeDto.setTotalTime(5);
        routeDto.setRoute(Arrays.asList(new StationDto("", "Станция"), new StationDto("", "станция2")));
        ResponseEntity<RouteDto> responseEntity2 = new ResponseEntity<>(routeDto, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), anyObject(), eq(RouteDto.class))).thenReturn(responseEntity2);

        RestToSubte restToSubte = new RestToSubte(restTemplate);
        RowUtil rowUtil = new RowUtil();
        RoutMessage routMessage = new RoutMessage(rowUtil);

        answerDetailsQuery = new AnswerDetailsQuery(rowUtil, restToSubte);
        branchQuery = new BranchQuery(rowUtil, restToSubte, routMessage);
        branchQuery.setLocalizedFactory(factory);
        stationQuery = new StationQuery(rowUtil, restToSubte, branchQuery);
        stationQuery.setLocalizedFactory(factory);
        defaultQuery = new DefaultQuery(rowUtil);
        answerQuery = new AnswerQuery(rowUtil, stationQuery, restToSubte);
    }

    @Test
    public void getDirections() {
        StationDto stationDto1 = new StationDto("линия1", "станция1");
        StationDto stationDto2 = new StationDto("линия1", "станция2");
        StationDto stationDto3 = new StationDto("линия2", "станция1");
        StationDto stationDto4 = new StationDto("линия3", "станция1");

        List<StationDto> stations = new ArrayList<>();
        stations.add(stationDto1);
        stations.add(stationDto2);
        stations.add(stationDto3);
        stations.add(stationDto4);

        Map<String, List<StationDto>> expectedDirections = stations.stream()
                .collect(Collectors.groupingBy(StationDto::getLine, Collectors.toList()));

        assertEquals(expectedDirections, stationQuery.getDirections());
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
            "BranchQuery        |<b>Маршрут:</b> от \uD83D\uDD34 Станция до - Выберите   | StationQuery",
            "BranchQuery        |<b>Маршрут:</b> от - до \uD83D\uDD34 Станция Выберите   | StationQuery",
            "BranchQuery        |branch                                                  | StationQuery",
    }, delimiter = '|')
    public void supports(String id, String msg, String name) {
        LocaleContextHolder.setLocale(Locale.forLanguageTag("ru"));
        var testData = new AnswerData(id, 0);
        supports(testData, msg, name);
    }

    /**
     * All data line by line in resources.
     *
     * @param questionId  last class sent msg
     * @param msg last msg from user
     * @param answerCode Code of last class member
     * @param keyboardSize size of keyboard in the next msg
     */
    @ParameterizedTest
    @CsvSource(value = {
            "BranchQuery   |   <b>Маршрут:</b> от - Выберите              |  1  |  1",
            "BranchQuery   |   <b>Маршрут:</b> до - Выберите              |  1  |  1",
    }, delimiter = '|')
    public void process(String questionId, String msg, String answerCode, String keyboardSize) {
        var testData = new AnswerData(questionId, Integer.parseInt(answerCode));
        process(testData, msg, Integer.parseInt(keyboardSize));
    }

    void process(AnswerData data, String msg, Integer keyboardSize) {
        List<Query> queries = allQuery();
        EditMessageText edit = queries.stream()
                .filter(query -> query.supports(data, msg))
                .findFirst()
                .map(query -> query.process(1, "chatId", msg, data))
                .get(); //have default

        @NonNull List<List<InlineKeyboardButton>> keyboard = edit.getReplyMarkup().getKeyboard();
        assertEquals(keyboardSize, keyboard.size());
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

    List<Query> allQuery() {
        return new ArrayList<>(List.of(answerDetailsQuery, branchQuery, stationQuery, answerQuery, defaultQuery));
    }
}