package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.junit.jupiter.api.Test;

/**
 * Все поля дефолтного метода.
 */
public class DefaultQueryTest {

    @Test
    public void process() {
        var query = new DefaultQuery(new RowUtil());
        query.answer();
        query.process(123, "chatId",
                query.question(new RoutMsg()), new AnswerData("id", 0));
    }
}