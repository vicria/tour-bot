package ar.vicria.telegram.microservice.services.util;

import ar.vicria.telegram.microservice.services.Answer;
import ar.vicria.telegram.microservice.services.AnswerData;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class RowUtil {

    public InlineKeyboardMarkup createRows(List<Answer> answers, String questionId) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        int numColumns = 2;
        int numRows = (int) Math.ceil((double) answers.size() / numColumns);

        for (int row = 0; row < numRows; row++) {
            int start = row * numColumns;
            int end = Math.min(start + numColumns, answers.size());

            List<InlineKeyboardButton> currentRow = new ArrayList<>();
            for (int j = start; j < end; j++) {
                Answer answer = answers.get(j);
                InlineKeyboardButton button = InlineKeyboardButton.builder()
                        .text(answer.getText())
                        .callbackData(AnswerData.serialize(questionId, answer))
                        .build();
                currentRow.add(button);
            }
            rows.add(currentRow);
        }
        markupInline.setKeyboard(rows);
        return markupInline;
    }
}
