package ar.vicria.telegram.microservice.localizations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class MessageSourceTest {

    @ParameterizedTest
    @CsvSource({
            "answerdetailsquery.distance,'\ndetailed route: %s'",
            "answerdetailsquery.hide, Hide",
            "answerquery.time, '\n<b>will take %s minutes</b>'",
            "answerquery.moredetailed, More detailed",
            "branchquery.selectabranch, '\nSelect a branch '",
            "branchquery.route, Route",
            "stationquery.selectastation, '\nSelect a station'",
            "stationquery.from, from",
            "routmessage.selectadirection, Select a direction",
            "routmessage.route, Route",
            "routmessage.from, From",
            "routmessage.to, To",
            "startmessage.menusubte, Menu Subte",
            "startmessage.route, Route",
            "startmessage.feedback, Feedback",
            "startmessage.aboutthecapabilitiesofthebot, About the capabilities of the bot",
            "routmsg.from, from",
            "routmsg.to, to",
            "routmsg.select, Select",
            "routmsg.willtake, will take",
            "routmsg.route, <b>Route:</b>",
            "telegramconnector.selectanitemfromthemenu, Select an item from the menu"
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
            "take-time, '\n<b>займет %s минут</b>'",
            "button.details, Подробнее",
            "text.select-branch, '\nВыберите ветку'",
            "button.route, Маршрут",
            "text.select-route, '\nВыберите станцию'",
            "button.from, от",
            "button.to, до",
            "text.select-direction, Выберите направление",
            "text.start, Меню Subte",
            "button.route, Маршрут",
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

}