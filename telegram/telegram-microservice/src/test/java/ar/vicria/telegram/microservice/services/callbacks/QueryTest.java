package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.messages.RoutMessage;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import lombok.NonNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QueryTest {

    /**
     * All data line by line in resources.
     *
     * @param id   last class sent msg
     * @param msg  last msg from user
     * @param name next class after press
     */
    @ParameterizedTest
    @CsvSource({
            "AnswerQuery,msg,AnswerDetailsQuery",
            "AnswerDetailsQuery,msg,AnswerQuery",
            "StationQuery,<b>Маршрут:</b> от \uD83D\uDD34 Станция до \uD83D\uDD34 Станция Выберите,AnswerQuery",
            "RoutMessage,rout,BranchQuery",
            "StationQuery,<b>Маршрут:</b> от \uD83D\uDD34 Выберите,BranchQuery",
            "StationQuery,<b>Маршрут:</b> до \uD83D\uDD34 Выберите,BranchQuery",
            "BranchQuery,<b>Маршрут:</b> от \uD83D\uDD34 Станция до - Выберите,StationQuery",
            "BranchQuery,<b>Маршрут:</b> от - до \uD83D\uDD34 Станция Выберите,StationQuery",
            "BranchQuery,branch,StationQuery",
            "123,msg,DefaultQuery",
    })
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
            "AnswerDetailsQuery,<b>Маршрут:</b> от \uD83D\uDD34 Станция до \uD83D\uDD34 Станция Выберите,AnswerQuery",
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
        when(restTemplate.postForEntity(anyString(), anyObject(), eq(RouteDto.class))).thenReturn(responseEntity2);

        RestToSubte restToSubte = new RestToSubte(restTemplate);
        RoutMessage routMessage = new RoutMessage(rowUtil);

        Query answerDetailsQuery = new AnswerDetailsQuery(rowUtil, restToSubte);
        BranchQuery branchQuery = new BranchQuery(rowUtil, restToSubte, routMessage);
        StationQuery stationQuery = new StationQuery(rowUtil, restToSubte, branchQuery);
        DefaultQuery defaultQuery = new DefaultQuery(rowUtil);
        Query answerQuery = new AnswerQuery(rowUtil, stationQuery, restToSubte);

        return new ArrayList<>(List.of(answerDetailsQuery, branchQuery, stationQuery, answerQuery, defaultQuery));
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
        EditMessageText edit = queries.stream()
                .filter(query -> query.supports(data, msg))
                .findFirst()
                .map(query -> query.process(1, "chatId", msg, data))
                .get(); //have default

        @NonNull List<List<InlineKeyboardButton>> keyboard = edit.getReplyMarkup().getKeyboard();
        assertEquals(1, keyboard.size());
    }

}