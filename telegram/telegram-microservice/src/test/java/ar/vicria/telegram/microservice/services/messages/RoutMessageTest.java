package ar.vicria.telegram.microservice.services.messages;

import ar.vicria.telegram.microservice.services.util.RowUtil;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoutMessageTest {

    @Test
    public void process() {
        RoutMessage routMessage = new RoutMessage(new RowUtil());
        SendMessage message = routMessage.process("id");
        assertEquals(routMessage.question(), message.getText());
    }

}