package ar.vicria.telegram.microservice.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AnswerData {

    private static final String PREFIX = "/answer";
    private static final String DELIMITER = "#";

    private final String questionId;
    private final Integer answerCode;

    public static String serialize(String questionId, Answer answer) {
        return PREFIX + DELIMITER + questionId + DELIMITER + answer.getCode();
    }

    public static boolean match(String text) {
        return text != null && text.startsWith(PREFIX);
    }

    public static AnswerData deserialize(String text) {
        //todo: replace with regex
        String[] parts = text.split("#");
        return new AnswerData(parts[1], Integer.valueOf(parts[2]));
    }
}
