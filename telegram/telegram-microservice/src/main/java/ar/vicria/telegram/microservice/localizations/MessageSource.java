package ar.vicria.telegram.microservice.localizations;

import lombok.Getter;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Компонент для получения локализованных сообщений из бандла с сообщениями.
 *
 * @author abishkam
 * @since 1.0.0
 */
@Getter
public class MessageSource extends ResourceBundleMessageSource {

    private static final String BASENAME = "messages";
    private static final Pattern BUNDLE_LANGUAGE_PATTERN = Pattern.compile(BASENAME + "_(.*)\\.properties");

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
     * Доступные локализации.
     * @return список доступных локализаций
     */
    public List<Locale> getAvailableLocales() {
        var resourceResolver = new PathMatchingResourcePatternResolver();
        try {
            return Arrays.stream(resourceResolver.getResources("classpath*:" + BASENAME + "_*.properties"))
                    .map(Resource::getFilename)
                    .filter(Objects::nonNull)
                    .map(BUNDLE_LANGUAGE_PATTERN::matcher)
                    .filter(Matcher::find)
                    .map(matcher -> matcher.group(1))
                    .map(Locale::forLanguageTag)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}