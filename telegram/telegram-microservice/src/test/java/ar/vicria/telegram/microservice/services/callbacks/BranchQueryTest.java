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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.Locale;

/**
 * todo
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
public class BranchQueryTest {


    @Mock
    LocalizedTelegramMessageFactory localizedFactory;

    RowUtil rowUtil = new RowUtil();

    @Mock
    RoutMessage routMessage;

    @Mock
    RestToSubte rest;



    @BeforeEach
    public void local() {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        LocaleContextHolder.setLocale(locale);
        LocaleContextHolder.setDefaultLocale(locale);
        var localizedTelegramMessage = new LocalizedTelegramMessage(locale);
        Mockito.when(localizedFactory.getLocalized()).thenReturn(localizedTelegramMessage);

    }


    @Test
    void questionTest(){

        BranchQuery branchQuery = new BranchQuery(rowUtil, rest, routMessage);
        branchQuery.setLocalizedFactory(localizedFactory);

        RoutMsg routMsg = new RoutMsg();
        routMsg.setStationFrom("Станция1");
        routMsg.setStationTo("Станция2");
        routMsg.setLineFrom("Линия1");
        routMsg.setLineTo("Линия2");
        routMsg.setLocalizedFactory(localizedFactory);

        var ansToCheck = branchQuery.question(routMsg);
        var expectedAns = "<b>Route</b>\n" +
                "Select a branch";
        Assertions.assertEquals(expectedAns, ansToCheck);
    }

    /**
     * All data line by line in resources.
     *
     * @param questionMessage  questionMessage
     * @param sAnswerCode  String value of AnswerCode
     * @param expectedAdition helps to test message purpose
     */
    @ParameterizedTest
    @CsvSource(value = {
            "RoutMessage | 1 | from",
            "RoutMessage | 2 | to",
            }, delimiter = '|')
    void processTest1(String questionMessage, String  sAnswerCode, String expectedAdition){
        int answerCode = Integer.parseInt(sAnswerCode);

        BranchQuery branchQuery = new BranchQuery(rowUtil, rest, routMessage);
        branchQuery.setLocalizedFactory(localizedFactory);

        AnswerData answerData = new AnswerData(questionMessage, answerCode);
        var ansToCheck = branchQuery.process(17, "444", "Select a direction", answerData);

        EditMessageText expectedAns = new EditMessageText();
        expectedAns.setChatId("444");
        expectedAns.setMessageId(17);
            expectedAns.setText("<b>Route</b>\n" +
                    expectedAdition+" -  \n" +
                    "Select a branch");
        expectedAns.setParseMode("HTML");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of());
        expectedAns.setReplyMarkup(inlineKeyboardMarkup);
        Assertions.assertEquals(expectedAns, ansToCheck);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "from",
            "to",
    })
    void processTest2(String msgDirection){

        var listOfStationDto = List.of(new StationDto("H\uD83D\uDFE1", "station1")
                , new StationDto("line2", "station2"));
        Mockito.when(rest.get()).thenReturn(listOfStationDto);

        BranchQuery branchQuery = new BranchQuery(rowUtil, rest, routMessage);
        branchQuery.setLocalizedFactory(localizedFactory);


        AnswerData answerData = new AnswerData("StationQuery", 0);
        String msgButtonRoad = localizedFactory.getLocalized().getButtonRoute();

        var ansToCheck = branchQuery.process(123, "444", "Route\n" +
                msgDirection +" H\uD83D\uDFE1  \n" +
                "Select a station"+
                msgButtonRoad, answerData);


        EditMessageText expectedAns = new EditMessageText();
        expectedAns.setChatId("444");
        expectedAns.setMessageId(123);
        if (msgDirection.equals("from")) {
            expectedAns.setText("<b>Route</b>\n" +
                    "from H\uD83D\uDFE1 station1 \n" +
                    "to -  \n" +
                    "Select a branch");
        } else if (msgDirection.equals("to")) {
            expectedAns.setText("<b>Route</b>\n" +
                    "from -  \n" +
                    "to H\uD83D\uDFE1 station1 \n" +
                    "Select a branch");
        }
        expectedAns.setParseMode("HTML");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("H\uD83D\uDFE1");
        inlineKeyboardButton1.setCallbackData("/answer#BranchQuery#0");
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setText("line2");
        inlineKeyboardButton2.setCallbackData("/answer#BranchQuery#1");
        var listOfButtons = List.of(inlineKeyboardButton1,inlineKeyboardButton2);
        inlineKeyboardMarkup.setKeyboard(List.of(listOfButtons));
        expectedAns.setReplyMarkup(inlineKeyboardMarkup);
        Assertions.assertEquals(expectedAns, ansToCheck);
    }
}