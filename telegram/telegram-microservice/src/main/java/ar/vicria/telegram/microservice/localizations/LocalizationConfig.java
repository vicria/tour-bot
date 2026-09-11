package ar.vicria.telegram.microservice.localizations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Конфигурация построения локализованных сообщений: откуда берутся доступные
 * языки и как для каждого из них собирается {@link LocalizedTelegramMessage}.
 */
@Configuration
public class LocalizationConfig {

    /**
     * Источник сообщений из бандла 'messages'.
     *
     * <p>Бин намеренно назван не {@code messageSource}, чтобы не подменять
     * автоконфигурационный бин Spring Boot того же имени/типа
     * ({@code org.springframework.context.MessageSource}).
     *
     * @return источник сообщений
     */
    @Bean
    public MessageSource telegramMessageSource() {
        return new MessageSource();
    }

    /**
     * Набор локализованных сообщений — по одному на каждый обнаруженный
     * на classpath язык (см. {@link MessageSource#getAvailableLocales()}).
     *
     * @param telegramMessageSource источник сообщений
     * @return локализованные сообщения для всех доступных языков
     */
    @Bean
    public List<LocalizedTelegramMessage> localizedTelegramMessages(MessageSource telegramMessageSource) {
        return telegramMessageSource.getAvailableLocales().stream()
                .map(locale -> new LocalizedTelegramMessage(locale, telegramMessageSource))
                .collect(Collectors.toList());
    }
}
