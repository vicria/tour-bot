package ar.vicria.telegram.microservice.localizations;

import lombok.Getter;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Компонент для получения локализованных сообщений из бандла с сообщениями.
 *
 * @author abishkam
 * @since 1.0.0
 */
@Getter
@Component
public class MessageSource extends ResourceBundleMessageSource {

    private static final String BASENAME = "messages";

    /**
     * Имя файла бандла, например {@code messages_ru.properties}.
     * Группа 1 содержит код языка, если суффикс есть (для базового
     * {@code messages.properties} без суффикса группа 1 будет {@code null}).
     */
    private static final Pattern BUNDLE_FILE_PATTERN =
            Pattern.compile("^" + Pattern.quote(BASENAME) + "(?:_([a-zA-Z]{2}))?\\.properties$");

    /**
     * Конструктор.
     */
    public MessageSource() {
        this(BASENAME);
    }

    /**
     * Конструктор.
     *
     * @param basenames all bundles
     */
    private MessageSource(String... basenames) {
        super();
        setUseCodeAsDefaultMessage(true);
        setDefaultEncoding("UTF-8");
        setDefaultLocale(new Locale("en"));
        addBasenames(basenames);
    }

    /**
     * Получение сообщения без указания языка (дефолт).
     *
     * @param code   code for localization
     * @param locale localization of a class
     * @return locale message
     */
    public String getMessage(final String code, final Locale locale) {
        return getMessage(code, new Object[]{new Object()}, locale);
    }

    /**
     * Доступные локализации. Список определяется автоматически по файлам
     * бандла {@code messages_XX.properties}, найденным в classpath — без
     * жёстко заданного перечня языков.
     *
     * @return список доступных локализаций
     */
    public List<Locale> getAvailableLocales() {
        List<Locale> availableLocales = new ArrayList<>();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources("classpath*:" + BASENAME + "*.properties");
            for (Resource resource : resources) {
                Matcher matcher = BUNDLE_FILE_PATTERN.matcher(resource.getFilename());
                if (matcher.matches() && matcher.group(1) != null) {
                    availableLocales.add(new Locale(matcher.group(1)));
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось просканировать бандлы '" + BASENAME + "'", e);
        }
        availableLocales.sort(Comparator.comparing(Locale::getLanguage));
        return availableLocales;
    }

}