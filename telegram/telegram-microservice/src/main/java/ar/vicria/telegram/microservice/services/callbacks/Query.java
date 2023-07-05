package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.RouteDto;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerDto;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

/**
 * Base class for responding on callback query messages.
 */
public abstract class Query {

    /**
     * id for discussion and answers.
     *
     * @return id
     */
    public String queryId() {
        return this.getClass().getSimpleName();
    }

    private final RowUtil rowUtil;

    /**
     * Constrictor.
     *
     * @param rowUtil util for telegram menu
     */
    protected Query(RowUtil rowUtil) {
        this.rowUtil = rowUtil;
    }

    /**
     * Rule for search a class.
     *
     * @param msg        not required
     * @param answerData answer in query, what the button pressed
     * @return use this class or not
     */
    public abstract boolean supports(AnswerData answerData, String msg);

    /**
     * text in message.
     *
     * @param request what application know about rout now
     * @return text
     */
    String question(RoutMsg request) {
        return "";
    }

    String question(RoutMsg request, RouteDto send) {
        return "";
    }

    /**
     * Buttons.
     *
     * @param option condition for choosing buttons names
     * @return text and numbers of buttons
     */
    abstract List<AnswerDto> answer(String... option);

    EditMessageMedia postQuestionEdit(Integer messageId,
                                      String questionText,
                                      String questionId,
                                      List<AnswerDto> answers,
                                      String chatId) {

        File img = new File("D:"
                + "\\apps\\tour-bot"
                + "\\telegram\\telegram-microservice"
                + "\\src\\main\\resources\\images\\subte.png");

        return editMessageMediaCreator(img, messageId, chatId, answers, questionId, questionText);
    }

    EditMessageMedia postQuestionEdit(byte[] img,
                                      Integer messageId,
                                      String questionText,
                                      String questionId,
                                      List<AnswerDto> answers,
                                      String chatId) {

        ByteArrayInputStream bis = new ByteArrayInputStream(img);

        return editMessageMediaCreator(bis, messageId, chatId, answers, questionId, questionText);
    }

    EditMessageMedia editMessageMediaCreator(Object obj,
                                             Integer messageId,
                                             String chatId,
                                             List<AnswerDto> answers,
                                             String questionId,
                                             String questionText) {

        InputMediaPhoto mediaPhoto = new InputMediaPhoto();
        if (questionId.startsWith("Answer")) {
            ByteArrayInputStream bis = (ByteArrayInputStream) obj;
            mediaPhoto.setMedia(bis, "map-subte.png");
        } else {
            File img = (File) obj;
            mediaPhoto.setMedia(img, "subte.png");
        }

        mediaPhoto.setCaption(questionText);
        mediaPhoto.setParseMode("HTML");

        InlineKeyboardMarkup rows = rowUtil.createRows(answers, questionId);

        return EditMessageMedia.builder()
                .messageId(messageId)
                .chatId(chatId)
                .media(mediaPhoto)
                .replyMarkup(rows)
                .build();
    }

    /**
     * Generation a message for user.
     *
     * @param chatId     number of user chat
     * @param answerData user pressed the button
     * @param msg        new msg for user
     * @param msgId      for message must to edit
     * @return message for sending
     */
    public abstract EditMessageMedia process(Integer msgId, String chatId, String msg, AnswerData answerData);

}
