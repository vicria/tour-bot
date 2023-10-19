package ar.vicria.telegram.microservice.services.messages;

import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.i18n.LocaleContextHolder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StartMessageTest {

    @Mock
    public LocalizedTelegramMessageFactory factory;

    @BeforeEach
    public void local() {
        var localizedTelegramMessage = new LocalizedTelegramMessage(Locale.forLanguageTag("ru"));
        when(factory.getLocalized()).thenReturn(localizedTelegramMessage);
        when(factory.getLocalizedByWord(anyString())).thenReturn(localizedTelegramMessage);
        LocaleContextHolder.setLocale(Locale.forLanguageTag("ru"));
    }

    @Test
    void supports() {
        StartMessage startMessage =new StartMessage(new RowUtil());

        String expectedMessageText = "/start";
        assertTrue(startMessage.supports(expectedMessageText));
    }

    @Test
    void process() {
        StartMessage startMessage = new StartMessage(new RowUtil());
        startMessage.setLocalizedFactory(factory);
        SendMessage message = startMessage.process("id");
        assertEquals(startMessage.question(), message.getText());
    }

    @Test
    void listTest() {
        List<TextMessage> textMessages = new ArrayList<>();

        StartMessage startMessage = new StartMessage(new RowUtil());
        startMessage.setLocalizedFactory(factory);
        textMessages.add(startMessage);

        RoutMessage routMessage = new RoutMessage(new RowUtil());
        routMessage.setLocalizedFactory(factory);
        textMessages.add(routMessage);

        String msg = "/start";

        TextMessage expectedMessage = textMessages.stream()
                .filter(tm -> tm.supports(msg))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("This command is not supported yet!"));

        String expectedClassName = expectedMessage.getClass().getName();
        String actualClassName = startMessage.getClass().getName();

        assertEquals(expectedClassName, actualClassName);
    }

    @Test
    void question() {

    }
}