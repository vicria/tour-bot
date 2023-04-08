package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.properties.SubteProperties;
import ar.vicria.telegram.microservice.services.Answer;
import ar.vicria.telegram.microservice.services.AnswerData;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.messages.RoutMessage;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BranchQuery extends Query {

    private final String queryId = "BranchQuery" + UUID.randomUUID().toString();

    String queryId() {
        return queryId;
    }

    @Getter
    private List<String> lines;
    private final RoutMessage routMessage;
    private Map<String, List<StationDto>> directions;

    public BranchQuery(RowUtil rowUtil, RestToSubte rest, SubteProperties properties, RoutMessage routMessage) {
        super(rowUtil);
        this.routMessage = routMessage;
        List<StationDto> stations = rest.get().stream()
                //todo delete. change line names in db
                .peek(dto -> {
                    String icon = dto.getLine().equals("green") ? properties.getGreen()
                            : dto.getLine().equals("yellow") ? properties.getYellow() : properties.getRed();
                    dto.setLine(icon);
                })
                .collect(Collectors.toList());
        lines = stations.stream()
                .map(StationDto::getLine).distinct()
                .collect(Collectors.toList());
        directions = stations.stream()
                .collect(Collectors.groupingBy(StationDto::getLine, Collectors.toList()));
    }

    @Override
    public boolean supports(AnswerData answerData, String msg) {
        var response = new RoutMsg(msg);
        return routMessage.queryId().equals(answerData.getQuestionId()) ||
                (answerData.getQuestionId()).contains("StationQuery")
                        && (response.getLineFrom() == null || response.getLineTo() == null);
    }

    @Override
    public String question(RoutMsg request) {
        return request.toString()
                + "\nВыберите ветку ";
    }

    @Override
    public List<Answer> answer(String... option) {
        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            Answer answer = new Answer(lines.get(i), i);
            answers.add(answer);
        }
        return answers;
    }

    @Override
    public EditMessageText process(Integer msgId, String chatId, String msg, AnswerData answerData) {
        var request = new RoutMsg(msg);
        if (msg.contains("Маршрут")) {
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
        return postQuestionEdit(msgId, question(request), queryId(), answer(), chatId);
    }
}
