package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.util.RowUtil;
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

@ExtendWith(MockitoExtension.class)
public class StationQueryTest {

    @Mock
    private LocalizedTelegramMessageFactory localizedFactory;
    RowUtil rowUtil = new RowUtil();
    @Mock
    RestToSubte rest;
    @Mock
    BranchQuery branchQuery;

    @BeforeEach
    public void locale() {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        LocaleContextHolder.setLocale(locale);
        LocaleContextHolder.setDefaultLocale(locale);
        var localizedTelegramMessage = new LocalizedTelegramMessage(locale);
        Mockito.when(localizedFactory.getLocalized()).thenReturn(localizedTelegramMessage);

    }


    @Test
    public void supportTest() {
        Mockito.reset(localizedFactory);
        String questionId = "123";
        Integer answerCode = 456;
        AnswerData answerData = new AnswerData(questionId, answerCode);


        var listOfStationDto = List.of(new StationDto("H\uD83D\uDFE1", "name1"));
        Mockito.when(rest.get()).thenReturn(listOfStationDto);

        StationQuery stationQuery = new StationQuery(rowUtil, rest, branchQuery);

        Mockito.when(branchQuery.queryId()).thenReturn("branchQuery");

        String msg = "message";
        var ansToCheck = stationQuery.supports(answerData, msg);
        Assertions.assertFalse(ansToCheck);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "from",
            "to",
    })
    public void processTest(String testInfo) {
        AnswerData answerData = new AnswerData("123", 0);
        var listOfStationDto = List.of(new StationDto("H\uD83D\uDFE1", "name1"));
        Mockito.when(rest.get()).thenReturn(listOfStationDto);
        StationQuery stationQuery = new StationQuery(rowUtil, rest, branchQuery);
        stationQuery.setLocalizedFactory(localizedFactory);

        Mockito.when(branchQuery.getLines()).thenReturn(List.of("H\uD83D\uDFE1"));

        String msg = "Route\n" +
                testInfo+" -  \n" +
                "Select a branch";

        var ansToCheck = stationQuery.process(12, "444", msg, answerData);
        EditMessageText ans = new EditMessageText();
        ans.setText("<b>Route</b>\n" +
                testInfo+ " H\uD83D\uDFE1  \n" +
                "Select a station");
        ans.setParseMode("HTML");
        ans.setMessageId(12);
        ans.setChatId("444");
        InlineKeyboardButton button = InlineKeyboardButton.builder()
                .text("name1")
                .callbackData("/answer#StationQuery#0")
                .build();
        List<InlineKeyboardButton> row1 = List.of(button);
        List<List<InlineKeyboardButton>> keyboard = List.of(row1);
        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();
        ans.setReplyMarkup(inlineKeyboardMarkup);

        var expectedAns1 = Optional.of(ans);

        Assertions.assertEquals(expectedAns1.get(), ansToCheck.get());
    }
}