package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.ConnectionDto;
import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.services.Localized;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerDto;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.Optional;

/**
 * Base class for responding on callback query messages.
 */
public abstract class Query extends Localized {

    //todo refactoring
    /**
     * time time.
     */
    public static Integer time;

    public static void setTime(Integer time) {
        Query.time = time;
    }

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
    abstract String question(RoutMsg request);

    /**
     * Buttons.
     *
     * @param option condition for choosing buttons names
     * @return text and numbers of buttons
     */
    abstract List<AnswerDto> answer(String... option);

    EditMessageText postQuestionEdit(Integer messageId,
                                     String questionText,
                                     String questionId,
                                     List<AnswerDto> answers,
                                     String chatId) {
        EditMessageText editMessageText = EditMessageText.builder()
                .messageId(messageId)
                .chatId(chatId)
                .text(questionText)
                .build();

        InlineKeyboardMarkup rows = rowUtil.createRows(answers, questionId);
        editMessageText.setParseMode("HTML");
        editMessageText.setReplyMarkup(rows);
        return editMessageText;
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
    public abstract Optional<BotApiMethod> process(Integer msgId, String chatId, String msg, AnswerData answerData);

    public EditMessageText createEditMsg(Integer msgId, RoutMsg response, String chatId) {
        return postQuestionEdit(msgId, question(response), queryId(), answer(), chatId);
    }
    protected ConnectionDto getTransition(List<String> linesList, List<ConnectionDto> connectionsList, int cycle) {
        String lineTo = linesList.get(cycle);
        return connectionsList.stream()
                .filter(connectionDto -> connectionDto
                        .getStationFrom()
                        .getLine()
                        .equals(linesList.get(cycle - 1)) && connectionDto
                        .getStationTo()
                        .getLine()
                        .equals(lineTo))
                .reduce((e1, e2) -> e2)
                .orElseThrow(() -> new NoSuchElementException("There is no such connection"));

    }

    protected List<String> createLinesList(RouteDto send) {
        List<String> linesList = List.of(send.getRoute().stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("There is no lines"))
                .getLine());

        List<StationDto> route = send.getRoute();
        boolean isRouteOnOneLine = route.stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("There is no first station"))
                .getLine()
                .equals(route.stream()
                        .reduce((e1, e2) -> e2)
                        .orElseThrow(() -> new NoSuchElementException("There is no last station"))
                        .getLine());

        if (!isRouteOnOneLine) {
            linesList = send.getRoute().stream()
                    .map(StationDto::getLine)
                    .distinct()
                    .collect(Collectors.toList());
        }
        return linesList;
    }
}


