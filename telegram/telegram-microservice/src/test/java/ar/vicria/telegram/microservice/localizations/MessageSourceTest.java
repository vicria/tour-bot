package ar.vicria.telegram.microservice.localizations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class MessageSourceTest {

    @ParameterizedTest
    @CsvSource({
            "distance-details,'\ndetailed route: %s'",
            "button.hide, Hide",
            "take-time, '\nwill take %s minutes'",
            "button.details, Details",
            "text.select-branch, '\nSelect a branch'",
            "button.route, Route",
            "text.select-route, '\nSelect a station'",
            "button.from, from",
            "button.to, to",
            "text.select-direction, Select a direction",
            "text.start, Subte Menu",
            "button.feedback, Feedback",
            "button.capabilities, Bot Capabilities",
            "text.select-menu, Select an item from the menu",

    })
    public void rbEnTest(String key, String value){
        MessageSource messageSource = new MessageSource();
        assertEquals(messageSource.getMessage(() -> new String[]{key}, Locale.ENGLISH), value);
        assertEquals(messageSource.getMessage(key, new Object[]{}, Locale.ENGLISH), value);
        assertEquals(messageSource.getMessage(key, Locale.ENGLISH), value);
    }

    @ParameterizedTest
    @CsvSource({
            "distance-details,'\nподробный маршрут: %s'",
            "button.hide, Скрыть",
            "take-time, '\nзаймет %s минут'",
            "button.details, Подробнее",
            "text.select-branch, '\nВыберите ветку'",
            "button.route, Маршрут",
            "text.select-route, '\nВыберите станцию'",
            "button.from, от",
            "button.to, до",
            "text.select-direction, Выберите направление",
            "text.start, Меню Subte",
            "button.feedback, Обратная связь",
            "button.capabilities, О возможностях бота",
            "text.select-menu, Выберите пункт из меню"
    })
    public void rbRuTest(String key, String value){
        MessageSource messageSource = new MessageSource();
        assertEquals(messageSource.getMessage(() -> new String[]{key},Locale.forLanguageTag("ru")), value);
        assertEquals(messageSource.getMessage(key, new Object[]{},Locale.forLanguageTag("ru")), value);
        assertEquals(messageSource.getMessage(key, Locale.forLanguageTag("ru")), value);
    }


    @Test
    public void msSizeTest(){
        MessageSource messageSource = new MessageSource();
        assertEquals(messageSource.getBasenameSet().size(), 1);
    }

    @Test
    void getAvailableLocales() {
        MessageSource messageSource = new MessageSource();
        List<Locale> availableLocales = messageSource.getAvailableLocales();

        assertEquals(availableLocales.size(), 2);
        assertTrue(availableLocales.stream().anyMatch(l -> l.equals(Locale.ENGLISH)));
    }
}