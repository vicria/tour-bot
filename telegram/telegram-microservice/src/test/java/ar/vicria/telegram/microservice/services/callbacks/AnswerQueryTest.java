package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.stream.Collectors;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AnswerQueryTest {

    @Mock
    private LocalizedTelegramMessageFactory localizedFactory;

    private final RowUtil rowUtil = new RowUtil();

    @Mock
    private RestToSubte rest;

    @Mock
    private StationQuery stationQuery;



    @BeforeEach
    public void local() {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        LocaleContextHolder.setLocale(locale);
        LocaleContextHolder.setDefaultLocale(locale);
        var localizedTelegramMessage = new LocalizedTelegramMessage(locale);
        Mockito.when(localizedFactory.getLocalized()).thenReturn(localizedTelegramMessage);

    }
    /**
     * All data line by line in resources.
     *
     * @param testInfo checking 2 types of input in msg
     */
    @ParameterizedTest
    @CsvSource(value = {
            "from",
            "to",
    })
    void processTest(String testInfo) {
        StationDto dto1 = new StationDto("H\uD83D\uDFE1", "Las Heras");
        StationDto dto2 = new StationDto("B\uD83D\uDD34", "Florida");
        var listDtos = List.of(dto1, dto2);
        Mockito.when(rest.get()).thenReturn(listDtos);
        StationDto from = dto1;
        StationDto to = dto2;
        RouteDto routeDto = new RouteDto();
        routeDto.setTotalTime(19);
        Mockito.when(rest.send(from, to)).thenReturn(routeDto);
        AnswerQuery answerQuery = new AnswerQuery(rowUtil, stationQuery, rest);
        answerQuery.setLocalizedFactory(localizedFactory);

        String msg = "";
        var msgId = 12;
        var chatId = "444";
        if(testInfo.equals("from")) {
             msg = "Route\n" +
                    "from H\uD83D\uDFE1 Las Heras \n" +
                    "to B\uD83D\uDD34  \n" +
                    "Select a station";
        }
        if(testInfo.equals("to")){
             msg = "Route\n" +
                    "from H\uD83D\uDFE1  \n" +
                    "to B\uD83D\uDD34 Florida \n" +
                    "Select a station";
        }
        AnswerData answerData = new AnswerData("123" , 0);


        List<StationDto> dtoList = List.of(dto1, dto2);
        var directions = dtoList.stream()
                .collect(Collectors.groupingBy(StationDto::getLine, Collectors.toList()));
        Mockito.when(stationQuery.getDirections()).thenReturn(directions);
        var ansToCheck = answerQuery.process(msgId, chatId , msg, answerData);

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId("444");
        editMessageText.setMessageId(12);
        editMessageText.setParseMode("HTML");
        editMessageText.setText("<b>Route</b>\n" +
                "from H\uD83D\uDFE1 Las Heras \n" +
                "to B\uD83D\uDD34 Florida \n" +
                "will take 19 minutes");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Details");
        inlineKeyboardButton1.setCallbackData("/answer#AnswerQuery#0");
        var listOfButtons = List.of(inlineKeyboardButton1);
        inlineKeyboardMarkup.setKeyboard(List.of(listOfButtons));
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        var expectedAns = editMessageText;
        Assertions.assertEquals(expectedAns, ansToCheck);

    }
}
