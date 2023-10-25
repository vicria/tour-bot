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

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RoutMessageTest {


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
    public void process() {
        RoutMessage routMessage = new RoutMessage(new RowUtil());
        routMessage.setLocalizedFactory(factory);
        SendMessage message = routMessage.process("id");
        assertEquals(routMessage.question(), message.getText());
        assertEquals("id", message.getChatId());
    }

    @Test
    public void supports() {
        RoutMessage routMessage = new RoutMessage(new RowUtil());
        routMessage.setLocalizedFactory(factory);
        String expectedMessageText = "Маршрут";
        assertTrue(routMessage.supports(expectedMessageText));
    }
}