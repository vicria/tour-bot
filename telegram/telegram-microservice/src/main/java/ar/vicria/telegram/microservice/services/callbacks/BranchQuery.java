package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerDto;
import ar.vicria.telegram.microservice.services.messages.RoutMessage;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import lombok.Getter;
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
 * Question about the line in subway.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BranchQuery extends Query<RoutMsg> {

    @Getter
    private List<String> lines;
    private final RoutMessage routMessage;
    private Map<String, List<StationDto>> directions;

    /**
     * Constructor.
     *
     * @param rowUtil     util class for menu
     * @param rest        rest client to subte
     * @param routMessage first question about rout
     */
    public BranchQuery(RowUtil rowUtil,
                       RestToSubte rest,
                       RoutMessage routMessage,
                       LocalizedTelegramMessageFactory factory) {
        super(rowUtil, factory);
        this.routMessage = routMessage;
        lines = rest.get().stream()
                .map(StationDto::getLine)
                .distinct()
                .toList();
        directions = rest.get().stream()
                .collect(Collectors.groupingBy(StationDto::getLine, Collectors.toList()));
    }

    @Override
    public boolean supports(AnswerData answerData, String msg) {
        var response = new RoutMsg(msg);
        return routMessage.queryId().equals(answerData.getQuestionId())
                || (answerData.getQuestionId()).equals("StationQuery")
                && (response.getLineFrom() == null || response.getLineTo() == null);
    }

    @Override
    public String question(RoutMsg request) {
        LocalizedTelegramMessage localized = localizedFactory().getLocalized();
        return request.toString()
                + localized.getTextSelectBranch();
    }

    @Override
    public List<AnswerDto> answer(String... option) {
        List<AnswerDto> answers = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            AnswerDto answer = new AnswerDto(lines.get(i), i);
            answers.add(answer);
        }
        return answers;
    }

    @Override
    public Optional<BotApiMethod> process(Integer msgId, String chatId, String msg, AnswerData answerData) {
        LocalizedTelegramMessage localized = localizedFactory().getLocalized();
        var request = new RoutMsg(msg);
        if (msg.contains(localized.getButtonRoute())) {
            if (request.isFrom()) {
                StationDto stationDto = this.directions.get(request.getLineFrom()).get(answerData.getAnswerCode());
                request.setStationFrom(stationDto.getName());
                request.setTo(true);
            } else {
                StationDto stationDto = this.directions.get(request.getLineTo()).get(answerData.getAnswerCode());
                request.setStationTo(stationDto.getName());
                request.setFrom(true);
            }
        } else {
            if (answerData.getAnswerCode() == 1) {
                request.setFrom(true);
            } else {
                request.setTo(true);
            }
        }
        return Optional.ofNullable(postQuestionEdit(msgId, question(request), queryId(), answer(), chatId));
    }
}
