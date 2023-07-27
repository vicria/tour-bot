package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
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

import java.util.Arrays;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AnswerDetailsQueryTest {

    @Mock
    public RestTemplate restTemplate;

    @Mock
    public LocalizedTelegramMessageFactory factory;

    private AnswerDetailsQuery answerDetailsQuery;

    @BeforeEach
    void local() {
        LocaleContextHolder.setLocale(Locale.forLanguageTag("ru"));

        factory = mock(LocalizedTelegramMessageFactory.class);
        var localizedTelegramMessage = new LocalizedTelegramMessage(Locale.forLanguageTag("ru"));
        when(factory.getLocalized()).thenReturn(localizedTelegramMessage);


        ResponseEntity<StationDto[]> responseEntity = new ResponseEntity<>(new StationDto[]{}, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(StationDto[].class))).thenReturn(responseEntity);

        RouteDto routeDtoForQuestion = new RouteDto();
        routeDtoForQuestion.setTotalTime(5);
        routeDtoForQuestion.setRoute(Arrays.asList(
                new StationDto("H\uD83D\uDFE1", "Corrientes"),
                new StationDto("B\uD83D\uDD34", "Pueyrredón")));

        ResponseEntity<RouteDto> responseEntity2 = new ResponseEntity<>(routeDtoForQuestion, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), anyObject(), eq(RouteDto.class)))
                .thenReturn(responseEntity2);

        RestToSubte restToSubte = new RestToSubte(restTemplate);
        RowUtil rowUtil = new RowUtil();


        answerDetailsQuery = new AnswerDetailsQuery(rowUtil, restToSubte);
        answerDetailsQuery.setLocalizedFactory(factory);
    }


    @ParameterizedTest
    @CsvSource(value = {"<b>Маршрут</b> от H\uD83D\uDFE1 Corrientes до B\uD83D\uDD34 Pueyrredón займет"})
    public void directionWithTransition(String msg) {
        String directionForTest = "<b>Маршрут</b>\n" +
                "от H\uD83D\uDFE1 Corrientes \n" +
                "до B\uD83D\uDD34 Pueyrredón \n" +
                "займет 5 минут\n" +
                "подробный маршрут: Corrientes H\uD83D\uDFE1 -> H\uD83D\uDFE1---\uD83D\uDEB6---B\uD83D\uDD34 -> Pueyrredón B\uD83D\uDD34";

        RoutMsg request = new RoutMsg(msg);
        String direction = answerDetailsQuery.question(request);

        assertNotNull(direction);
        assertEquals(directionForTest, direction);
    }
}