package ar.vicria.telegram.microservice.services;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
class AnswerDataTest {

    @Test
    void deserialize() {
        String msg = "/answer#RoutMessage#1";
        var answerData = AnswerData.deserialize(msg);
        assertEquals("RoutMessage", answerData.getQuestionId());
        assertEquals(1, answerData.getAnswerCode());
    }

    @Test
    void deserialize2() {
        String msg = "/answer#Test#500";
        var answerData = AnswerData.deserialize(msg);
        assertEquals("Test", answerData.getQuestionId());
        assertEquals(500, answerData.getAnswerCode());
    }

    @Test
    void matchTrue() {
        String msg = "/answer#Test#500";
        var answerData = AnswerData.match(msg);
        assertTrue(answerData);
    }

    @Test
    void matchFalse() {
        String msg = "/question#Test#500";
        var answerData = AnswerData.match(msg);
        assertFalse(answerData);

        msg = "/answer/Test/500";
        answerData = AnswerData.match(msg);
        assertFalse(answerData);

        msg = "/answer#Test#qwe";
        answerData = AnswerData.match(msg);
        assertFalse(answerData);
    }

    @Test
    void serialize() {
        String serialize = AnswerData.serialize("id", new Answer("question", 3));
        assertEquals("/answer#id#3", serialize);
    }
}