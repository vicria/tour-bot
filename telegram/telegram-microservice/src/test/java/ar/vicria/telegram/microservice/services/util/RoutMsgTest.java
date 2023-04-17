package ar.vicria.telegram.microservice.services.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoutMsgTest {

    @Test
    public void isFullTrue() {
        RoutMsg routMsg = new RoutMsg();
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
        RoutMsg routMsg = new RoutMsg();
        boolean full = routMsg.isFull();
        assertFalse(full);

        String text = routMsg.toString();
        assertEquals("<b>Маршрут:</b>", text);
    }

    @Test
    public void testToString() {
        RoutMsg routMsg = new RoutMsg();
        routMsg.setTo(true);
        routMsg.setFrom(true);
        routMsg.setLineFrom("blue");
        routMsg.setLineTo("blue");
        routMsg.setStationFrom("Gorkovskaya");
        routMsg.setStationTo("Petrogradskaya");
        String text = routMsg.toString();
        assertEquals("<b>Маршрут:</b>\nот blue Gorkovskaya \nдо blue Petrogradskaya ", text);
    }

    /**
     * Все значения
     */
    @Test
    public void test1() {
        String msg = "<b>Маршрут:</b>\nот \uD83D\uDD34 Станция \nдо \uD83D\uDD34 Ста н ция Выберите";
        var telegram = new RoutMsg(msg);

        assertEquals("\uD83D\uDD34", telegram.getLineFrom());
        assertEquals("Станция", telegram.getStationFrom());
        assertEquals("\uD83D\uDD34", telegram.getLineTo());
        assertEquals("Ста н ция", telegram.getStationTo());
        assertTrue(telegram.isFrom());
        assertTrue(telegram.isTo());
    }

    @Test
    public void test2() {
        String msg = "<b>Маршрут:</b>\nот \uD83D\uDD34 Станция \nдо - Выберите";
        var telegram = new RoutMsg(msg);

        assertEquals("\uD83D\uDD34", telegram.getLineFrom());
        assertEquals("Станция", telegram.getStationFrom());
        assertNull(telegram.getLineTo());
        assertNull(telegram.getStationTo());
    }

    @Test
    public void test3() {
        String msg = "<b>Маршрут:</b>\nот - \nдо \uD83D\uDD34 Станция Выберите";
        var telegram = new RoutMsg(msg);

        assertNull(telegram.getLineFrom());
        assertNull(telegram.getStationFrom());
        assertEquals("\uD83D\uDD34", telegram.getLineTo());
        assertEquals("Станция", telegram.getStationTo());
    }

    @Test
    public void test4() {
        String msg = "<b>Маршрут:</b>\nот - \nдо \uD83D\uDD34 Выберите";
        var telegram = new RoutMsg(msg);

        assertNull(telegram.getLineFrom());
        assertNull(telegram.getStationFrom());
        assertEquals("\uD83D\uDD34", telegram.getLineTo());
        assertNull(telegram.getStationTo());
    }

    @Test
    public void test0() {
        String msg = "Маршрут:\nот -  \nдо - \nВыберите ветку";
        var telegram = new RoutMsg(msg);

        assertNull(telegram.getLineFrom());
        assertNull(telegram.getStationFrom());
        assertNull(telegram.getLineTo());
        assertNull(telegram.getStationTo());
    }

    @Test
    public void test5() {
        String msg = "<b>Маршрут:</b>\nот \uD83D\uDD34 \nдо - Выберите";
        var telegram = new RoutMsg(msg);

        assertEquals("\uD83D\uDD34", telegram.getLineFrom());
        assertNull(telegram.getStationFrom());
        assertNull(telegram.getLineTo());
        assertNull(telegram.getStationTo());
    }

    @Test
    public void test6() {
        var telegram = new RoutMsg();
        telegram.setFrom(true);
        telegram.setTo(true);
        telegram.setLineFrom("\uD83D\uDD34");
        telegram.setLineTo("\uD83D\uDD34");
        telegram.setStationFrom("Start");
        telegram.setStationTo("Finish");

        assertEquals("<b>Маршрут:</b>\nот \uD83D\uDD34 Start \nдо \uD83D\uDD34 Finish ", telegram.toString());
    }

    @Test
    public void test7() {
        var telegram = new RoutMsg();
        telegram.setFrom(true);
        telegram.setTo(true);
        telegram.setLineFrom("\uD83D\uDD34");
        telegram.setStationFrom("Start");

        assertEquals("<b>Маршрут:</b>\nот \uD83D\uDD34 Start \nдо -  ", telegram.toString());
    }
    @Test
    public void test8() {
        var telegram = new RoutMsg();
        telegram.setFrom(true);
        telegram.setTo(true);
        telegram.setLineTo("\uD83D\uDD34");
        telegram.setStationTo("Finish");

        assertEquals("<b>Маршрут:</b>\nот -  \nдо \uD83D\uDD34 Finish ", telegram.toString());
    }
    @Test
    public void test9() {
        var telegram = new RoutMsg();
        telegram.setFrom(true);
        telegram.setTo(true);
        telegram.setLineFrom("\uD83D\uDD34");
        telegram.setLineTo("\uD83D\uDD34");

        assertEquals("<b>Маршрут:</b>\nот \uD83D\uDD34  \nдо \uD83D\uDD34  ", telegram.toString());
    }

    @Test
    public void test10() {
        String msg = "Маршрут:\nот -  \nВыберите ветку";
        var telegram = new RoutMsg(msg);
        telegram.setFrom(true);
        assertNull(telegram.getLineFrom());
        assertNull(telegram.getStationFrom());
        assertNull(telegram.getLineTo());
        assertNull(telegram.getStationTo());
        assertFalse(telegram.isTo());
    }

    @Test
    public void test11() {
        String msg = "Маршрут:\nдо -  \nВыберите ветку";
        var telegram = new RoutMsg(msg);

        assertNull(telegram.getLineFrom());
        assertNull(telegram.getStationFrom());
        assertNull(telegram.getLineTo());
        assertNull(telegram.getStationTo());
        assertFalse(telegram.isFrom());
    }

    @Test
    public void test12() {
        String msg = "Маршрут:\nот \uD83D\uDD34  \nВыберите ветку";
        var telegram = new RoutMsg(msg);

        assertEquals("\uD83D\uDD34", telegram.getLineFrom());
        assertNull(telegram.getStationFrom());
        assertNull(telegram.getLineTo());
        assertNull(telegram.getStationTo());
    }

    @Test
    public void test13() {
        String msg = "Маршрут:\nот \uD83D\uDD34  \nзаймет";
        var telegram = new RoutMsg(msg);

        assertEquals("\uD83D\uDD34", telegram.getLineFrom());
        assertNull(telegram.getStationFrom());
        assertNull(telegram.getLineTo());
        assertNull(telegram.getStationTo());
    }
}