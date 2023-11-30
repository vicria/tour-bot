package ar.vicria.telegram.microservice.localizations;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MessagesTest {

    @Test
    public void checkUnknownLocale() {
        LocalizedTelegramMessage msg = new LocalizedTelegramMessage(Locale.ENGLISH);

        assertEquals(msg.getLocale(), Locale.ENGLISH);

//        assertEquals(msg, LocalizedTelegramMessage.getInitMessage(Locale.TAIWAN));

        assertEquals("\ndetailed route: %s", msg.getDistanceDetails());
        assertEquals("Hide", msg.getButtonHide());
        assertEquals("\nwill take %s minutes", msg.getTakeTime());
        assertEquals("Details", msg.getButtonDetails());
        assertEquals("\nSelect a branch", msg.getTextSelectBranch());
        assertEquals("Route", msg.getButtonRoute());
        assertEquals("\nSelect a station", msg.getTextSelectRoute());
        assertEquals("Select a direction", msg.getTextSelectDirection());
        assertEquals("Subte Menu", msg.getTextStart());
        assertEquals("Route", msg.getButtonRoute());
        assertEquals("Feedback", msg.getButtonFeedback());
        assertEquals("Bot Capabilities", msg.getButtonCapabilities());
//        assertEquals(msg.getTextSelectMenu(), "Select");
//        assertEquals(msg.getTakeTimeWord(), "will take");
        assertEquals("from", msg.getButtonFrom());
        assertEquals("to", msg.getButtonTo());
        assertEquals("Select an item from the menu", msg.getTextSelectMenu());
        assertEquals("Transition", msg.getTextTransition());
        assertEquals("minutes", msg.getTextMinutes());
    }

    @Test
    public void checkRuLocale() {
        LocalizedTelegramMessage msg = new LocalizedTelegramMessage(Locale.forLanguageTag("ru"));

        assertEquals(msg.getLocale(), Locale.forLanguageTag("ru"));

        assertEquals("\nподробный маршрут: %s", msg.getDistanceDetails());
        assertEquals("Скрыть", msg.getButtonHide());
        assertEquals("Подробнее", msg.getButtonDetails());
        assertEquals("\nзаймет %s минут", msg.getTakeTime());
        assertEquals("\nВыберите ветку", msg.getTextSelectBranch());
        assertEquals("\nВыберите станцию", msg.getTextSelectRoute());
        assertEquals("Выберите направление", msg.getTextSelectDirection());
        assertEquals("Маршрут", msg.getButtonRoute());
        assertEquals("Меню Subte", msg.getTextStart());
        assertEquals("Обратная связь", msg.getButtonFeedback());
        assertEquals("О возможностях бота", msg.getButtonCapabilities());
        assertEquals("Выберите", msg.getCommon());
        assertEquals("займет", msg.getTakeTimeWord());
        assertEquals("от", msg.getButtonFrom());
        assertEquals("до", msg.getButtonTo());
        assertEquals("Выберите пункт из меню", msg.getTextSelectMenu());
        assertEquals("Переход", msg.getTextTransition());
        assertEquals("минут", msg.getTextMinutes());

//        assertEquals(msg, LocalizedTelegramMessage.getInitMessage(Locale.forLanguageTag("ru")));
    }

    @Test
    public void checkTwin() {
        LocalizedTelegramMessage msg = new LocalizedTelegramMessage(Locale.ENGLISH);

        assertEquals(msg, msg);
    }

    @Test
    public void checkDifferenceLocale() {
        LocalizedTelegramMessage msgRu = new LocalizedTelegramMessage(Locale.forLanguageTag("ru"));
        LocalizedTelegramMessage msgEn = new LocalizedTelegramMessage(Locale.ENGLISH);

        assertNotEquals(msgRu, msgEn);
    }

    @Test
    public void checkNull() {
        LocalizedTelegramMessage msg = new LocalizedTelegramMessage(Locale.ENGLISH);

        assertNotEquals(msg, null);
    }

    @Test
    public void checkClass() {
        LocalizedTelegramMessage msg = new LocalizedTelegramMessage(Locale.ENGLISH);

        assertNotEquals(msg, "");
    }

    @Test
    public void checkLines() {
        LocalizedTelegramMessage msg = new LocalizedTelegramMessage(Locale.ENGLISH);

        assertNotEquals(msg, "");
    }

}