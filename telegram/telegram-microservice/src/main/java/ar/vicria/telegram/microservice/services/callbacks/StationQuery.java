package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.properties.SubteProperties;
import ar.vicria.telegram.microservice.services.Answer;
import ar.vicria.telegram.microservice.services.AnswerData;
import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StationQuery extends Query {

    private Map<String, List<StationDto>> directions;
    private final BranchQuery branchQuery;

    public Map<String, List<StationDto>> getDirections() {
        return directions;
    }

    public StationQuery(RowUtil rowUtil, RestToSubte rest, SubteProperties properties, BranchQuery branchQuery) {
        super(rowUtil);
        this.branchQuery = branchQuery;
        directions = rest.get().stream()
                //todo delete. change line names in db
                .peek(dto -> {
                    String icon = dto.getLine().equals("green") ? properties.getGreen()
                            : dto.getLine().equals("yellow") ? properties.getYellow() : properties.getRed();
                    dto.setLine(icon);
                })
                .collect(Collectors.groupingBy(StationDto::getLine, Collectors.toList()));
    }

    @Override
    public boolean supports(AnswerData answerData, String msg) {
        return branchQuery.queryId().equals(answerData.getQuestionId());
    }

    @Override
    public String question(RoutMsg request) {
        return request.toString()
                + "\nВыберите станцию";
    }

    @Override
    public List<Answer> answer(String... option) {
        List<@NotBlank String> collect = directions.get(option[0]).stream()
                .map(StationDto::getName)
                .collect(Collectors.toList());

        List<Answer> answers = new ArrayList<>();
        for (String station : collect) {
            Answer answer = new Answer(station, collect.indexOf(station));
            answers.add(answer);
        }
        return answers;
    }

    @Override
    public EditMessageText process(Integer msgId, String chatId, String msg, AnswerData answerData) {
        RoutMsg telegramMsg = new RoutMsg(msg);
        String line = branchQuery.getLines().get(answerData.getAnswerCode());
        if (msg.substring(msg.indexOf(" -") - 2, msg.indexOf(" -")).equals("от")) {
            telegramMsg.setLineFrom(line);
        } else {
            telegramMsg.setLineTo(line);
        }
        return postQuestionEdit(msgId, question(telegramMsg), queryId(), answer(line), chatId);
    }
}
