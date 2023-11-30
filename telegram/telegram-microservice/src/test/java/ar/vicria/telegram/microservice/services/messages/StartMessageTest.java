package ar.vicria.telegram.microservice.services.messages;

import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.Locale;


@ExtendWith(MockitoExtension.class)
@Slf4j
public class StartMessageTest {

    @InjectMocks
    StartMessage startMessage = new StartMessage(new RowUtil());
    @Mock
    LocalizedTelegramMessageFactory localizedFactory;


    @BeforeEach
    public void local() {
        Locale locale = new Locale("RU");
        var localizedTelegramMessage = new LocalizedTelegramMessage(locale);
        Mockito.when(localizedFactory.getLocalized()).thenReturn(localizedTelegramMessage);
    }


    @Test
    void supportTest(){

        Mockito.reset(localizedFactory);
        var ansToCheck = startMessage.supports("/start");
        Assertions.assertTrue(ansToCheck);
    }

    @Test
    void answerTest(){

        var ansToCheck = startMessage.answer();

        List<String> expectedAns = List.of("Маршрут", "Обратная связь", "О возможностях бота");

        Assertions.assertEquals(expectedAns, ansToCheck);
    }

    @Test
    void questionTest(){
        var ansToCheck = startMessage.question();
        String expectedAns = "Меню Subte";
        Assertions.assertEquals(expectedAns, ansToCheck);


    }

    @Test
    void processSendMessageTest(){
        var ansToCheck = startMessage.process("444");
        var buttonNames = List.of("Маршрут", "Обратная связь", "О возможностях бота");
        SendMessage message = SendMessage.builder()
                .chatId("444")
                .text("Меню Subte")
                .build();
        SendMessage expectedAns = startMessage.sendMessage(message, buttonNames);
        Assertions.assertEquals(expectedAns, ansToCheck);
    }

    @Test
    void  processTextTest(){
        var ansToCheck = startMessage.question();

        SendMessage message = startMessage.process("444");
        var expectedAns = message.getText();
        Assertions.assertEquals(expectedAns, ansToCheck);
    }

}
