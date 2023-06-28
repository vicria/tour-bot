package ar.vicria.telegram.microservice.rb;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class MessagesTest {

    @Test
    public void checkUnknownLocale(){
        Messages msg = new Messages(Locale.TAIWAN);

        assertEquals(msg.getLocale(), Locale.TAIWAN);

        assertEquals(msg, Messages.getInitMessage(Locale.TAIWAN));

        assertEquals(msg.getAdqDistance(), "\ndetailed route: %s");
        assertEquals(msg.getAdqHide(),"Hide");
        assertEquals(msg.getAdqTime(), "\n<b>will take %s minutes</b>");
        assertEquals(msg.getAqMoredetailed(),"More detailed");
        assertEquals(msg.getAqTime(), "\n<b>will take %s minutes</b>");
        assertEquals(msg.getBqSelectbranch(),"\nSelect a branch ");
        assertEquals(msg.getBqRoute(),"Route");
        assertEquals(msg.getSqSelectstation(),  "\nSelect a station");
        assertEquals(msg.getSqFrom(), "from");
        assertEquals(msg.getRmessageSelectDirection(),"Select a direction");
        assertEquals(msg.getRmessageRoute(),"Route");
        assertEquals(msg.getRmessageFrom(),"From");
        assertEquals(msg.getRmessageTo(),"To");
        assertEquals(msg.getSmMenuSubte(),"Menu Subte");
        assertEquals(msg.getSmRoute(),"Route");
        assertEquals(msg.getSmFeedback(),"Feedback");
        assertEquals(msg.getSmAboutCapabilities(),"About the capabilities of the bot");
        assertEquals(msg.getRmsgRoute(), "<b>Route:</b>");
        assertEquals(msg.getRmsgSelect(),"Select");
        assertEquals(msg.getRmsgWillTake(),"will take");
        assertEquals(msg.getRmsgFrom(),"from");
        assertEquals(msg.getRmsgTo(),"to");
        assertEquals(msg.getTgcSelectItem(),"Select an item from the menu");
    }

    @Test
    public void checkRuLocale(){
        Messages msg = new Messages(Locale.forLanguageTag("ru"));

        assertEquals(msg.getLocale(), Locale.forLanguageTag("ru"));

        assertEquals(msg, Messages.getInitMessage(Locale.forLanguageTag("ru")));

        assertEquals(msg.getAdqDistance(), "\nподробный маршрут: %s");
        assertEquals(msg.getAdqHide(),"Скрыть");
        assertEquals(msg.getAdqTime(), "\n<b>займет %s минут</b>");
        assertEquals(msg.getAqMoredetailed(),"Подробнее");
        assertEquals(msg.getAqTime(), "\n<b>займет %s минут</b>");
        assertEquals(msg.getBqSelectbranch(),"\nВыберите ветку ");
        assertEquals(msg.getBqRoute(),"Маршрут");
        assertEquals(msg.getSqSelectstation(),  "\nВыберите станцию");
        assertEquals(msg.getSqFrom(), "от");
        assertEquals(msg.getRmessageSelectDirection(),"Выберите направление");
        assertEquals(msg.getRmessageRoute(),"Маршрут");
        assertEquals(msg.getRmessageFrom(),"От");
        assertEquals(msg.getRmessageTo(),"До");
        assertEquals(msg.getSmMenuSubte(),"Меню Subte");
        assertEquals(msg.getSmRoute(),"Маршрут");
        assertEquals(msg.getSmFeedback(),"Обратная связь");
        assertEquals(msg.getSmAboutCapabilities(),"О возможностях бота");
        assertEquals(msg.getRmsgRoute(), "<b>Маршрут:</b>");
        assertEquals(msg.getRmsgSelect(),"Выберите");
        assertEquals(msg.getRmsgWillTake(),"займет");
        assertEquals(msg.getRmsgFrom(),"от");
        assertEquals(msg.getRmsgTo(),"до");
        assertEquals(msg.getTgcSelectItem(),"Выберите пункт из меню");
    }

    @Test
    public void checkTwin(){
        Messages msg = new Messages(Locale.ENGLISH);

        assertEquals(msg, msg);
    }

    @Test
    public void checkDifferenceLocale(){
        Messages msgRu = new Messages(Locale.forLanguageTag("ru"));
        Messages msgEn = new Messages(Locale.ENGLISH);

        assertNotEquals(msgRu, msgEn);
    }

    @Test
    public void checkNull(){
        Messages msg = new Messages(Locale.ENGLISH);

        assertNotEquals(msg, null);
    }

    @Test
    public void checkClass(){
        Messages msg = new Messages(Locale.ENGLISH);

        assertNotEquals(msg, new String());
    }

    @Test
    public void checkLines(){
        Messages msg = new Messages(Locale.ENGLISH);

        assertNotEquals(msg, new String());
    }
}