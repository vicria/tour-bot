package ar.vicria.telegram.microservice.services.util;

import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerDto;
import lombok.NonNull;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RowUtilTest {

    private RowUtil rowUtil = new RowUtil();

    /**
     * Test: two buttons write in one row.
     * expected: 1 row, 2 columns
     */
    @Test
    public void createRowsOneRow() {
        var buttons = new ArrayList<AnswerDto>();
        buttons.add(new AnswerDto("name1", 0));
        buttons.add(new AnswerDto("name2", 1));
        InlineKeyboardMarkup keyboard = this.rowUtil.createRows(buttons, "id");
        @NonNull List<List<InlineKeyboardButton>> keyboard1 = keyboard.getKeyboard();
        //rows
        assertEquals(1, keyboard1.size());
        //columns
        assertEquals(2, keyboard1.getFirst().size());
    }

    /**
     * Test: 19 buttons write in ten row.
     * expected: 10 rows, 2 columns
     */
    @Test
    public void createRowsTenRows() {
        var buttons = new ArrayList<AnswerDto>();
        for (int i = 0; i < 19; i++) {
            buttons.add(new AnswerDto("name", i));
        }
        InlineKeyboardMarkup keyboard = this.rowUtil.createRows(buttons, "id");
        @NonNull List<List<InlineKeyboardButton>> keyboard1 = keyboard.getKeyboard();
        //rows
        assertEquals(10, keyboard1.size());
        //columns
        assertEquals(2, keyboard1.getFirst().size());
    }
}