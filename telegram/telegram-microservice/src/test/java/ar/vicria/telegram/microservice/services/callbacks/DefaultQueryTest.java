package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.telegram.microservice.services.RestToSubte;
import ar.vicria.telegram.microservice.services.callbacks.dto.AnswerData;
import ar.vicria.telegram.microservice.services.util.RoutMsg;
import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Все поля дефолтного метода.
 */
public class DefaultQueryTest {

    @Test
    public void process() throws IOException {
        var query = new DefaultQuery(new RowUtil(), new RestToSubte(new RestTemplate()));
        query.answer();
        query.process(123, "chatId",
                query.question(new RoutMsg()), new AnswerData("id", 0));
    }
}