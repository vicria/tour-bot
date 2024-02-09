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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StartMessageTest {

    @Mock
    public LocalizedTelegramMessageFactory factory;

    private StartMessage startMessage;

    @Test
    void process() {
        startMessage = new StartMessage(new RowUtil(), factory);
        final ExecutorService executorService =
                new ThreadPoolExecutor(0, 8, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        List<String> locales = Arrays.asList("ru", "es", "en");
        final Map<String, Object> futureMap = new ConcurrentHashMap<>(locales.size());
        List<CompletableFuture<Void>> collect = locales.stream()
                .map(locale -> {
                    CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> changeLocale(locale), executorService);
                    return voidCompletableFuture.thenAcceptAsync(error -> futureMap.computeIfAbsent(locale, s -> error));
                })
                .toList();
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
}