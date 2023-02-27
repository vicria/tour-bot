package ar.vicria.telegram.microservice.services;

import ar.vicria.telegram.microservice.properties.TelegramProperties;
import ar.vicria.telegram.resources.AdapterResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramConnector extends TelegramLongPollingBot implements AdapterResource {

    private final TelegramProperties properties;
    private final SubteBot subteBot;

    @PostConstruct
    private void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Unable to register telegram bot", e);
        }
    }

    @Override
    public void sendMessage(String text, String chatId) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode(text)
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Unable to send message", e);
        }
    }

    public void updateText(Integer messageId, String text, String chatId) {
        EditMessageText message = EditMessageText.builder()
                .messageId(messageId)
                .chatId(chatId)
                .text(text)
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Unable to send message", e);
        }
    }

    /**
     * Метод получение контакта от пользователя
     *
     * @param chatId - id пользователя
     */
    private void postRequestContactMessage(String chatId) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("Для использования бота необходимо зарегистрироваться")
                .build();

        KeyboardRow row = new KeyboardRow();
        String buttonText = "Отправить свой контакт для регистрации";
        row.add(KeyboardButton.builder().text(buttonText).requestContact(true).build());
        message.setReplyMarkup(new ReplyKeyboardMarkup(Collections.singletonList(row)));

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Unable to send invite message", e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            log.warn("Received answer: name = {}; text = {}", message.getFrom().getFirstName(), message.getText());

            String chatId = message.getFrom().getId().toString();
            String msg = message.getText();

            SendMessage process = subteBot.process(msg, chatId);

            try {
                execute(process);
            } catch (TelegramApiException e) {
                log.error("Unable to send message", e);
            }
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        updates.forEach(this::onUpdateReceived);
    }

    @Override
    public String getBotToken() {
        return properties.getBotToken();
    }

    @Override
    public String getBotUsername() {
        return properties.getBotUserName();
    }
}
