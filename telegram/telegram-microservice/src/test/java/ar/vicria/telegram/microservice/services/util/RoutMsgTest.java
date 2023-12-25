package ar.vicria.telegram.microservice.services.util;

import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RoutMsgTest {

    private RoutMsg routMsg;

    @Mock
    public LocalizedTelegramMessageFactory factory;

    @BeforeEach
    public void local() {
        var localizedTelegramMessage = new LocalizedTelegramMessage(Locale.forLanguageTag("ru"));
        when(factory.getLocalized()).thenReturn(localizedTelegramMessage);
        when(factory.getLocalizedByWord(anyString())).thenReturn(localizedTelegramMessage);
        LocaleContextHolder.setLocale(Locale.forLanguageTag("ru"));
        routMsg = new RoutMsg();
    }

    @Test
    public void isFullTrue() {
        routMsg.setTo(true);
        routMsg.setFrom(true);
        routMsg.setLineFrom("blue");
        routMsg.setLineTo("blue");
        routMsg.setStationFrom("Gorkovskaya");
        routMsg.setStationTo("Petrogradskaya");
        boolean full = routMsg.isFull();
        assertTrue(full);
    }

    @Test
    public void isFullFalse() {
        RoutMsg routMsg = new RoutMsg();
        routMsg.setTo(true);
        routMsg.setFrom(true);
        routMsg.setLineTo("blue");
        routMsg.setStationFrom("Gorkovskaya");
        routMsg.setStationTo("Petrogradskaya");
        boolean full = routMsg.isFull();
        assertFalse(full);
    }

    @Test
    public void isFullFalse2() {
        boolean full = routMsg.isFull();
        assertFalse(full);

        String text = routMsg.toString();
        assertEquals("<b>Маршрут</b>", text);
    }

    @Test
    public void testToString() {
        routMsg.setTo(true);
        routMsg.setFrom(true);
        routMsg.setLineFrom("blue");
        routMsg.setLineTo("blue");
        routMsg.setStationFrom("Gorkovskaya");
        routMsg.setStationTo("Petrogradskaya");
        String text = routMsg.toString();
        assertEquals("<b>Маршрут</b>\nот blue Gorkovskaya \nдо blue Petrogradskaya ", text);
    }

    /**
     * Все значения
     */
    @Test
    public void test1() {
        String msg = "<b>Маршрут</b>\nот \uD83D\uDD34 Станция \nдо \uD83D\uDD34 Ста н ция Выберите";
        routMsg = new RoutMsg(msg);
        assertEquals("\uD83D\uDD34", routMsg.getLineFrom());
        assertEquals("Станция", routMsg.getStationFrom());
        assertEquals("\uD83D\uDD34", routMsg.getLineTo());
        assertEquals("Ста н ция", routMsg.getStationTo());
        assertTrue(routMsg.isFrom());
        assertTrue(routMsg.isTo());
    }

    @Test
    public void test2() {
        String msg = "<b>Маршрут</b>\nот \uD83D\uDD34 Станция \nдо - Выберите";
        routMsg = new RoutMsg(msg);
        assertEquals("\uD83D\uDD34", routMsg.getLineFrom());
        assertEquals("Станция", routMsg.getStationFrom());
        assertNull(routMsg.getLineTo());
        assertNull(routMsg.getStationTo());
    }

    @Test
    public void test3() {
        String msg = "<b>Маршрут</b>\nот - \nдо \uD83D\uDD34 Станция Выберите";
        routMsg = new RoutMsg(msg);
        assertNull(routMsg.getLineFrom());
        assertNull(routMsg.getStationFrom());
        assertEquals("\uD83D\uDD34", routMsg.getLineTo());
        assertEquals("Станция", routMsg.getStationTo());
    }

    @Test
    public void test4() {
        String msg = "<b>Маршрут</b>\nот - \nдо \uD83D\uDD34 Выберите";
        routMsg = new RoutMsg(msg);


        assertNull(routMsg.getLineFrom());
        assertNull(routMsg.getStationFrom());
        assertEquals("\uD83D\uDD34", routMsg.getLineTo());
        assertNull(routMsg.getStationTo());
    }

    @Test
    public void test0() {
        String msg = "Маршрут\nот -  \nдо - \nВыберите ветку";
        routMsg = new RoutMsg(msg);


        assertNull(routMsg.getLineFrom());
        assertNull(routMsg.getStationFrom());
        assertNull(routMsg.getLineTo());
        assertNull(routMsg.getStationTo());
    }

    @Test
    public void test5() {
        String msg = "<b>Маршрут</b>\nот \uD83D\uDD34 \nдо - Выберите";
        routMsg = new RoutMsg(msg);
        assertEquals("\uD83D\uDD34", routMsg.getLineFrom());
        assertNull(routMsg.getStationFrom());
        assertNull(routMsg.getLineTo());
        assertNull(routMsg.getStationTo());
    }

    @Test
    public void test6() {
        var routMsg = new RoutMsg();

        routMsg.setFrom(true);
        routMsg.setTo(true);
        routMsg.setLineFrom("\uD83D\uDD34");
        routMsg.setLineTo("\uD83D\uDD34");
        routMsg.setStationFrom("Start");
        routMsg.setStationTo("Finish");

        assertEquals("<b>Маршрут</b>\nот \uD83D\uDD34 Start \nдо \uD83D\uDD34 Finish ", routMsg.toString());
    }

    @Test
    public void test7() {
        var routMsg = new RoutMsg();

        routMsg.setFrom(true);
        routMsg.setTo(true);
        routMsg.setLineFrom("\uD83D\uDD34");
        routMsg.setStationFrom("Start");

        assertEquals("<b>Маршрут</b>\nот \uD83D\uDD34 Start \nдо -  ", routMsg.toString());
    }

    @Test
    public void test8() {
        var routMsg = new RoutMsg();

        routMsg.setFrom(true);
        routMsg.setTo(true);
        routMsg.setLineTo("\uD83D\uDD34");
        routMsg.setStationTo("Finish");

        assertEquals("<b>Маршрут</b>\nот -  \nдо \uD83D\uDD34 Finish ", routMsg.toString());
    }

    @Test
    public void test9() {
        var routMsg = new RoutMsg();

        routMsg.setFrom(true);
        routMsg.setTo(true);
        routMsg.setLineFrom("\uD83D\uDD34");
        routMsg.setLineTo("\uD83D\uDD34");

        assertEquals("<b>Маршрут</b>\nот \uD83D\uDD34  \nдо \uD83D\uDD34  ", routMsg.toString());
    }

    @Test
    public void test10() {
        String msg = "Маршрут\nот -  \nВыберите ветку";
        routMsg = new RoutMsg(msg);
        routMsg.setFrom(true);
        assertNull(routMsg.getLineFrom());
        assertNull(routMsg.getStationFrom());
        assertNull(routMsg.getLineTo());
        assertNull(routMsg.getStationTo());
        assertFalse(routMsg.isTo());
    }

    @Test
    public void test11() {
        String msg = "Маршрут\nдо -  \nВыберите ветку";
        routMsg = new RoutMsg(msg);
        assertNull(routMsg.getLineFrom());
        assertNull(routMsg.getStationFrom());
        assertNull(routMsg.getLineTo());
        assertNull(routMsg.getStationTo());
        assertFalse(routMsg.isFrom());
    }

    @Test
    public void test12() {
        String msg = "Маршрут\nот \uD83D\uDD34  \nВыберите ветку";
        routMsg = new RoutMsg(msg);


        assertEquals("\uD83D\uDD34", routMsg.getLineFrom());
        assertNull(routMsg.getStationFrom());
        assertNull(routMsg.getLineTo());
        assertNull(routMsg.getStationTo());
    }

    @Test
    public void test13() {
        String msg = "Маршрут\nот \uD83D\uDD34  \nзаймет";
        routMsg = new RoutMsg(msg);


        assertEquals("\uD83D\uDD34", routMsg.getLineFrom());
        assertNull(routMsg.getStationFrom());
        assertNull(routMsg.getLineTo());
        assertNull(routMsg.getStationTo());
    }
}