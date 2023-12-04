package ar.vicria.telegram.microservice.services.messages;

import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import lombok.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.i18n.LocaleContextHolder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StartMessageTest {

    @Mock
    public LocalizedTelegramMessageFactory factory;

//    private StartMessage startMessage;

    @InjectMocks
    StartMessage startMessage = new StartMessage(new RowUtil());
    @Mock
    LocalizedTelegramMessageFactory localizedFactory;


    @Test
    void process() {
        startMessage = new StartMessage(new RowUtil());
        startMessage.setLocalizedFactory(factory);

        final ExecutorService executorService =
                new ThreadPoolExecutor(0, 8, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        List<String> locales = Arrays.asList("ru", "es", "en");
        final Map<String, Object> futureMap = new ConcurrentHashMap<>(locales.size());
        List<CompletableFuture<Void>> collect = locales.stream()
                .map(locale -> {
                    CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> changeLocale(locale), executorService);
                    return voidCompletableFuture.thenAcceptAsync(error -> futureMap.computeIfAbsent(locale, s -> error));
                })
                .collect(Collectors.toList());
        List<String> errors = new ArrayList<>();
        collect.forEach(future -> {
            try {
                future.get();
            } catch (Exception e) {
                errors.add(e.getMessage());
            }
        });
        assertTrue(errors.isEmpty());
    }

    void changeLocale(String lang) {
        var localizedTelegramMessage = new LocalizedTelegramMessage(Locale.forLanguageTag(lang));
        when(factory.getLocalized()).thenReturn(localizedTelegramMessage);
        when(factory.getLocalizedByWord(anyString())).thenReturn(localizedTelegramMessage);
        LocaleContextHolder.setLocale(Locale.forLanguageTag(lang));

        SendMessage msg = startMessage.process("id");
        @NonNull String text = msg.getText();

        assertEquals(factory.getLocalized().getTextStart(), text);
    }

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
