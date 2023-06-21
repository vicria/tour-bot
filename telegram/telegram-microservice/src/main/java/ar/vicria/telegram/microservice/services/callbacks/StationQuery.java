package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.rb.Messages;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerDto;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Question about station.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StationQuery extends Query {

    private Map<String, List<StationDto>> directions;
    private final BranchQuery branchQuery;

    /**
     * all directions.
     *
     * @return directions
     */
    public Map<String, List<StationDto>> getDirections() {
        return directions;
    }

    /**
     * Constructor.
     *
     * @param rowUtil     util class for menu
     * @param rest        rest client to subte
     * @param branchQuery question about line
     */
    public StationQuery(RowUtil rowUtil, RestToSubte rest, BranchQuery branchQuery) {
        super(rowUtil);
        this.branchQuery = branchQuery;
        directions = rest.get().stream()
                .collect(Collectors.groupingBy(StationDto::getLine, Collectors.toList()));
    }

    @Override
    public boolean supports(AnswerData answerData, String msg) {
        return branchQuery.queryId().equals(answerData.getQuestionId());
    }

    @Override
    public String question(RoutMsg request) {
        Messages ms = Messages.getInitMessage(LocaleContextHolder.getLocale());
        return request.toString()
                + ms.getSqSelectstation();
    }

    @Override
    public List<AnswerDto> answer(String... option) {
        List<@NotBlank String> collect = directions.get(option[0]).stream()
                .map(StationDto::getName)
                .collect(Collectors.toList());

        List<AnswerDto> answers = new ArrayList<>();
        for (String station : collect) {
            AnswerDto answer = new AnswerDto(station, collect.indexOf(station));
            answers.add(answer);
        }
        return answers;
    }

    @Override
    public EditMessageText process(Integer msgId, String chatId, String msg, AnswerData answerData) {
        Messages ms = Messages.getInitMessage(LocaleContextHolder.getLocale());
        RoutMsg telegramMsg = new RoutMsg(msg);
        String line = branchQuery.getLines().get(answerData.getAnswerCode());
        if (msg.substring(msg.indexOf(" -") - ms.getSqFrom().length(), msg.indexOf(" -")).equals(ms.getSqFrom())) {
            telegramMsg.setLineFrom(line);
        } else {
            telegramMsg.setLineTo(line);
        }
        return postQuestionEdit(msgId, question(telegramMsg), queryId(), answer(line), chatId);
    }
}
