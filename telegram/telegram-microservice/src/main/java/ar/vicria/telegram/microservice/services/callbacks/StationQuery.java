package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerDto;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import jakarta.validation.constraints.NotBlank;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Question about station.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StationQuery extends Query<RoutMsg> {

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
     * @param rowUtil          util class for menu
     * @param rest             rest client to subte
     * @param branchQuery      question about line
     */
    public StationQuery(
            RowUtil rowUtil,
            RestToSubte rest,
            BranchQuery branchQuery,
            LocalizedTelegramMessageFactory factory
    ) {
        super(rowUtil, factory);
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
        LocalizedTelegramMessage localized = localizedFactory().getLocalized();
        return request.toString()
                + localized.getTextSelectRoute();
    }

    @Override
    public List<AnswerDto> answer(String... option) {
        List<@NotBlank String> collect = directions.get(option[0]).stream()
                .map(StationDto::getName)
                .toList();

        List<AnswerDto> answers = new ArrayList<>();
        for (String station : collect) {
            AnswerDto answer = new AnswerDto(station, collect.indexOf(station));
            answers.add(answer);
        }
        return answers;
    }

    @Override
    public Optional<BotApiMethod> process(Integer msgId, String chatId, String msg, AnswerData answerData) {
        LocalizedTelegramMessage localized = localizedFactory().getLocalized();
        RoutMsg telegramMsg = new RoutMsg(msg);
        String line = branchQuery.getLines().get(answerData.getAnswerCode());
        String from = msg.substring(msg.indexOf(" -") - localized.getButtonFrom().length(), msg.indexOf(" -"));
        if (from.equals(localized.getButtonFrom())) {
            telegramMsg.setLineFrom(line);
        } else {
            telegramMsg.setLineTo(line);
        }
        return Optional.ofNullable(postQuestionEdit(msgId, question(telegramMsg), queryId(), answer(line), chatId));
    }
}
