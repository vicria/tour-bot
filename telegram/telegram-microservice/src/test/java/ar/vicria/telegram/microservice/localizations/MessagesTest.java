package ar.vicria.telegram.microservice.localizations;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessagesTest {

    @Test
    @Disabled
    public void checkUnknownLocale() {
        LocalizedTelegramMessage msg = new LocalizedTelegramMessage(Locale.TAIWAN);

        assertEquals(msg.getLocale(), Locale.TAIWAN);

        assertEquals(msg, LocalizedTelegramMessage.getInitMessage(Locale.TAIWAN));

        assertEquals(msg.getDistanceDetails(), "\ndetailed route: %s");
        assertEquals(msg.getButtonHide(), "Hide");
        assertEquals(msg.getTakeTime(), "\n<b>will take %s minutes</b>");
        assertEquals(msg.getButtonDetails(), "More detailed");
        assertEquals(msg.getTakeTime(), "\n<b>will take %s minutes</b>");
        assertEquals(msg.getTextSelectBranch(), "\nSelect a branch ");
        assertEquals(msg.getButtonRoute(), "Route");
        assertEquals(msg.getTextSelectRoute(), "\nSelect a station");
        assertEquals(msg.getButtonFrom(), "from");
        assertEquals(msg.getTextSelectDirection(), "Select a direction");
        assertEquals(msg.getButtonRoute(), "Route");
        assertEquals(msg.getButtonFrom(), "From");
        assertEquals(msg.getButtonTo(), "To");
        assertEquals(msg.getTextStart(), "Menu Subte");
        assertEquals(msg.getButtonRoute(), "Route");
        assertEquals(msg.getButtonFeedback(), "Feedback");
        assertEquals(msg.getButtonCapabilities(), "About the capabilities of the bot");
        assertEquals(msg.getButtonRoute(), "<b>Route:</b>");
        assertEquals(msg.getTextSelectMenu(), "Select");
        assertEquals(msg.getTakeTimeWord(), "will take");
        assertEquals(msg.getButtonFrom(), "from");
        assertEquals(msg.getButtonTo(), "to");
        assertEquals(msg.getTextSelectMenu(), "Select an item from the menu");
    }

    @Test
    public void checkRuLocale() {
        LocalizedTelegramMessage msg = new LocalizedTelegramMessage(Locale.forLanguageTag("ru"));

        assertEquals(msg.getLocale(), Locale.forLanguageTag("ru"));

        assertEquals("\nподробный маршрут: %s", msg.getDistanceDetails());
        assertEquals("Скрыть", msg.getButtonHide());
        assertEquals("\n<b>займет %s минут</b>", msg.getTakeTime());
        assertEquals("Подробнее", msg.getButtonDetails());
        assertEquals("\n<b>займет %s минут</b>", msg.getTakeTime());
        assertEquals("\nВыберите ветку", msg.getTextSelectBranch());
        assertEquals("\nВыберите станцию", msg.getTextSelectRoute());
        assertEquals("Выберите направление", msg.getTextSelectDirection());
//        assertEquals(msg.getButtonRoute(),"Маршрут");
        assertEquals("Меню Subte", msg.getTextStart());
//        assertEquals("Маршрут", msg.getButtonRoute());
        assertEquals("Обратная связь", msg.getButtonFeedback());
        assertEquals("О возможностях бота", msg.getButtonCapabilities());
        assertEquals("<b>Маршрут:</b>", msg.getButtonRoute());
//        assertEquals(msg.getTextSelectMenu(),"Выберите"); //todo getRmsgSelect
        assertEquals("займет", msg.getTakeTimeWord());
        assertEquals("от", msg.getButtonFrom());
        assertEquals("до", msg.getButtonTo());
        assertEquals("Выберите пункт из меню", msg.getTextSelectMenu());

//        assertEquals(msg, LocalizedTelegramMessage.getInitMessage(Locale.forLanguageTag("ru")));
    }

}