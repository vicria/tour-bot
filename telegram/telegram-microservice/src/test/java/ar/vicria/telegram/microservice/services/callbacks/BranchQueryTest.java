package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.messages.RoutMessage;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

/**
 * todo
 */
@ExtendWith(MockitoExtension.class)
public class BranchQueryTest {

//    @InjectMocks
    BranchQuery branchQuery;

    @Mock
    LocalizedTelegramMessageFactory localizedFactory;


    @BeforeEach
    public void local() {
        Locale locale = new Locale("RU");
        var localizedTelegramMessage = new LocalizedTelegramMessage(locale);
        Mockito.when(localizedFactory.getLocalized()).thenReturn(localizedTelegramMessage);

        RowUtil rowUtil = new RowUtil();

        RestTemplate restTemplate = new RestTemplate();
        RoutMessage routMessage = new RoutMessage(rowUtil);


        RestToSubte restToSubte = new RestToSubte(restTemplate);
        branchQuery = new BranchQuery(rowUtil, restToSubte, routMessage);
    }

//    @Test
//    public void process() {
//    }
//А
//    @ParameterizedTest
//    @CsvSource(value = {
//            "AnswerQuery        | msg                                                    | AnswerDetailsQuery",
//            "AnswerDetailsQuery | msg                                                    | AnswerQuery",
//            "StationQuery       | <b>Маршрут:</b> от \uD83D\uDD34 "
//                    + "Станция до \uD83D\uDD34 Станция Выберите              | AnswerQuery",
//            "RoutMessage        |rout                                                    | BranchQuery",
//            "StationQuery       |<b>Маршрут:</b> от \uD83D\uDD34 Выберите                | BranchQuery",
//            "StationQuery       |<b>Маршрут:</b> до \uD83D\uDD34 Выберите                | BranchQuery",
//            "BranchQuery        |<b>Маршрут:</b> от \uD83D\uDD34 Станция до - Выберите   | StationQuery",
//            "BranchQuery        |<b>Маршрут:</b> от - до \uD83D\uDD34 Станция Выберите   | StationQuery",
//            "BranchQuery        |branch                                                  | StationQuery",
//            "123                |msg                                                     | DefaultQuery",
//    }, delimiter = '|')

    @Test
    @Disabled
    void questionTest(){
        RoutMsg routMsg = new RoutMsg();
        var ansToCheck = branchQuery.question(routMsg);
        var expectedAns = "exp";
        Assertions.assertEquals(expectedAns, ansToCheck);
    }

    @Test
    void answerTest(){
        var ansToCheck = branchQuery.answer();
    }
}