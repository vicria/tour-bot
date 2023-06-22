package ar.vicria.telegram.microservice.rb;

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
            "answerdetailsquery.distance,'\nподробный маршрут: %s'",
            "answerdetailsquery.hide, Скрыть",
            "answerquery.time, '\n<b>займет %s минут</b>'",
            "answerquery.moredetailed, Подробнее",
            "branchquery.selectabranch, '\nВыберите ветку '",
            "branchquery.route, Маршрут",
            "stationquery.selectastation, '\nВыберите станцию'",
            "stationquery.from, от",
            "routmessage.selectadirection, Выберите направление",
            "routmessage.route, Маршрут",
            "routmessage.from, От",
            "routmessage.to, До",
            "startmessage.menusubte, Меню Subte",
            "startmessage.route, Маршрут",
            "startmessage.feedback, Обратная связь",
            "startmessage.aboutthecapabilitiesofthebot, О возможностях бота",
            "routmsg.from, от",
            "routmsg.to, до",
            "routmsg.select, Выберите",
            "routmsg.willtake, займет",
            "routmsg.route, <b>Маршрут:</b>",
            "telegramconnector.selectanitemfromthemenu, Выберите пункт из меню"
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