package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.DistanceDto;
import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerDto;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

    private final RestToSubte rest;
    private final RowUtil rowUtil;

    /**
     * Constrictor.
     *
     * @param rest access to subte
     * @param rowUtil util for telegram menu
     */
    protected Query(RestToSubte rest, RowUtil rowUtil) {
        this.rest = rest;
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

    public byte[] transition(StationDto from, StationDto to) {
        RouteDto send = rest.send(from, to);
        return send.getImg();
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
                                      String chatId,
                                      DistanceDto dto) {

        InputMediaPhoto mediaPhoto = new InputMediaPhoto();

        File file = new File("telegram\\telegram-microservice"
                + "\\src\\main\\resources\\images\\subte.png");

        Optional.ofNullable(dto).ifPresentOrElse(
            (distance) ->
                    Optional.ofNullable(dto.getFrom()).ifPresentOrElse( // для тестов
                        (x) -> mediaPhoto.setMedia(
                                new ByteArrayInputStream(transition(dto.getFrom(), dto.getTo())),
                                "map-subte.png"),
                        () -> mediaPhoto.setMedia(file, "map-subte.png")
                        ),
            () -> mediaPhoto.setMedia(file, "map-subte.png")
        );


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
    public abstract EditMessageMedia process(Integer msgId,
                                             String chatId,
                                             String msg,
                                             AnswerData answerData) throws IOException;

}
