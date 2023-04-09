package ar.vicria.telegram.microservice.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@RequiredArgsConstructor
public class AnswerData {

    private static final String PREFIX = "/answer";
    private static final String DELIMITER = "#";

    private final String questionId;
    private final Integer answerCode;

    public static String serialize(String questionId, Answer answer) {
        List<String> builder = new ArrayList<>() {
        };
        builder.add(PREFIX);
        builder.add(questionId);
        builder.add(answer.getCode().toString());
        return String.join(DELIMITER, builder);
    }

    public static boolean match(String text) {
        if (text == null || !text.startsWith(PREFIX)) {
            return false;
        }
        String[] parts = text.split(Pattern.quote(DELIMITER));
        if (parts.length != 3) {
            return false;
        }
        try {
            Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }


    public static AnswerData deserialize(String text) {
        Pattern pattern = Pattern.compile("^" + Pattern.quote(PREFIX + DELIMITER)
                + "([^" + DELIMITER + "]+)" + Pattern.quote(DELIMITER) + "(\\d+)$");
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            String questionId = matcher.group(1);
            Integer answerCode = Integer.valueOf(matcher.group(2));
            return new AnswerData(questionId, answerCode);
        } else {
            throw new IllegalArgumentException("Invalid input: " + text);
        }
    }
}
