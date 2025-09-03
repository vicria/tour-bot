package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerDto;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    public StationQuery(
            RowUtil rowUtil,
            RestToSubte rest,
            BranchQuery branchQuery
    ) {
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
        LocalizedTelegramMessage localized = localizedFactory.getLocalized();
        return request.toString()
                + localized.getTextSelectRoute();
    }

    @Override
    public List<AnswerDto> answer(String... option) {
        return directions.get(option[0]).stream()
                .filter(stationDto -> !Objects.equals(option[1], stationDto.getName()))
                .map(stationDto -> new AnswerDto(stationDto.getName(), directions.get(option[0]).indexOf(stationDto)))
                .collect(Collectors.toList());
    }

    @Override
    public EditMessageText process(Integer msgId, String chatId, String msg, AnswerData answerData) {
        LocalizedTelegramMessage localized = localizedFactory.getLocalized();
        RoutMsg telegramMsg = new RoutMsg(msg);
        String line = branchQuery.getLines().get(answerData.getAnswerCode());
        String from = msg.substring(msg.indexOf(" -") - localized.getButtonFrom().length(), msg.indexOf(" -"));
        String firstSelectedStation = telegramMsg.getStationFrom() != null
                ? telegramMsg.getStationFrom()
                : telegramMsg.getStationTo();
        if (from.equals(localized.getButtonFrom())) {
            telegramMsg.setLineFrom(line);
        } else {
            telegramMsg.setLineTo(line);
        }
        return postQuestionEdit(msgId, question(telegramMsg), queryId(), answer(line, firstSelectedStation), chatId);
    }
}
