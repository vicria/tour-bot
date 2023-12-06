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
import java.util.Optional;

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
        var ansToCheck = branchQuery.process(12, "444", "Select a direction", answerData);

        EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText("<b>Route</b>\n" +
                    expectedAdition+" -  \n" +
                    "Select a branch");
        editMessageText.setParseMode("HTML");
        editMessageText.setMessageId(12);
        editMessageText.setChatId("444");
        List<List<InlineKeyboardButton>> keyboard = List.of();
        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        var expectedAns = Optional.of(editMessageText);
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

        var ansToCheck = branchQuery.process(12, "444", "Route\n" +
                msgDirection +" H\uD83D\uDFE1  \n" +
                "Select a station"+
                msgButtonRoad, answerData);


        EditMessageText editMessageText = new EditMessageText();
        if (msgDirection.equals("from")) {
            editMessageText.setText("<b>Route</b>\n" +
                    "from H\uD83D\uDFE1 station1 \n" +
                    "to -  \n" +
                    "Select a branch");
        } else if (msgDirection.equals("to")) {
            editMessageText.setText("<b>Route</b>\n" +
                    "from -  \n" +
                    "to H\uD83D\uDFE1 station1 \n" +
                    "Select a branch");
        }
        editMessageText.setParseMode("HTML");
        editMessageText.setMessageId(12);
        editMessageText.setChatId("444");
        InlineKeyboardButton button1 = InlineKeyboardButton.builder()
                .text("H\uD83D\uDFE1")
                .callbackData("/answer#BranchQuery#0")
                .build();
        InlineKeyboardButton button2 = InlineKeyboardButton.builder()
                .text("line2")
                .callbackData("/answer#BranchQuery#1")
                .build();

        List<InlineKeyboardButton> row1 = List.of(button1, button2);
        List<List<InlineKeyboardButton>> keyboard = List.of(row1);
        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        var expectedAns = Optional.of(editMessageText);


        Assertions.assertEquals(expectedAns, ansToCheck);
    }
}