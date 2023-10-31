package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.messages.RoutMessage;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AnswerQueryTest {

    @Mock
    public RestTemplate restTemplate;

    @Mock
    public LocalizedTelegramMessageFactory factory;

    @BeforeEach
    void init() {
        LocaleContextHolder.setLocale(Locale.forLanguageTag("ru"));
        factory = Mockito.mock(LocalizedTelegramMessageFactory.class);
        var localizedTelegramMessage = new LocalizedTelegramMessage(Locale.forLanguageTag("ru"));
        when(factory.getLocalized()).thenReturn(localizedTelegramMessage);
    }

//    @Test
//    void process() {
//        RowUtil rowUtil = new RowUtil();
//        RestToSubte restToSubte = new RestToSubte(restTemplate);
//        RoutMessage routMessage = new RoutMessage(rowUtil);
//
//        BranchQuery branchQuery = new BranchQuery(rowUtil, restToSubte, routMessage);
//        StationQuery stationQuery = new StationQuery(rowUtil, restToSubte, branchQuery);
//        AnswerQuery answerQuery = new AnswerQuery(rowUtil, stationQuery, restToSubte);
//
//        Chat chat = new Chat();
//        chat.setType("text");
//        chat.setId(123L);
//
//        Message message = new Message();
//        message.setMessageId(1);
//        message.setText("");// what text is here?
//        message.setChat(chat);
//
//        AnswerData answerData = AnswerData.deserialize(message.getText());
//
//        EditMessageText actualMessage = answerQuery.process(message.getMessageId(),
//                String.valueOf(message.getChatId()), message.getText(), answerData);
//    }
}