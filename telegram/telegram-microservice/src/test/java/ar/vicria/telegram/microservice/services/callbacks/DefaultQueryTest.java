package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Все поля дефолтного метода.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DefaultQueryTest {

    @InjectMocks
    private RoutMsg routMsg;

    @Mock
    public LocalizedTelegramMessageFactory factory;

    @BeforeEach
    public void local() {
        var localizedTelegramMessage = new LocalizedTelegramMessage(Locale.forLanguageTag("ru"));
        when(factory.getLocalized()).thenReturn(localizedTelegramMessage);
        when(factory.getLocalizedByWord(anyString())).thenReturn(localizedTelegramMessage);
        LocaleContextHolder.setLocale(Locale.forLanguageTag("ru"));
        routMsg.setLocalizedFactory(factory);
    }

    @Test
    public void process() {
        var query = new DefaultQuery(new RowUtil());
        query.setLocalizedFactory(factory);
        query.answer();
        query.process(123, "chatId",
                query.question(routMsg),
                new AnswerData("id", 0));
    }
}