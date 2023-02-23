package ar.vicria.telegram.microservice.configuration;

import ar.vicria.telegram.microservice.properties.TelegramProperties;
import ar.vicria.telegram.resources.AdapterResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramConnector extends TelegramLongPollingBot implements AdapterResource{

    private final TelegramProperties properties;

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
            .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
//            log.error("Unable to send message", e);
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
//            log.error("Unable to send message", e);
        }
    }

//    public void postFiles(List<String> paths, String chatId) {
//        for (String path : paths) {
//            DWInputStreamFile dwInputStreamFile = storageService.readDWMeta(path);
//            SendDocument message = SendDocument.builder()
//                .chatId(chatId)
//                .document(new InputFile(dwInputStreamFile.getInputStream(), dwInputStreamFile.getMeta().getName()))
//                .build();
//            try {
//                execute(message);
//            } catch (TelegramApiException e) {
//                log.error("Unable to send message", e);
//            }
//            try {
//                dwInputStreamFile.getInputStream().close();
//            } catch (IOException e) {
//                log.error("Unable to close stream", e);
//            }
//        }
//    }

//    public void postQuestion(String questionText,
//                             String questionId,
//                             List<Answer> answers,
//                             String chatId) {
//        SendMessage message = SendMessage.builder()
//            .chatId(chatId)
//            .text(questionText)
//            .build();
//
//        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
//
//        answers.forEach(answer -> {
//            InlineKeyboardButton button = InlineKeyboardButton.builder()
//                .text(answer.getText())
//                .callbackData(AnswerData.serialize(questionId, answer))
//                .build();
//            rows.add(Collections.singletonList(button));
//        });
//
//        markupInline.setKeyboard(rows);
//        message.setReplyMarkup(markupInline);
//
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            log.error("Unable to send request message", e);
//        }
//    }

//    private void processInputMessage(String username, String chatId, String inputMessage) {
//        log.info("Message from {}: {}", username, inputMessage);
//
//        if (AnswerData.match(inputMessage)) {
//            AnswerData answerData = AnswerData.deserialize(inputMessage);
//            String questionId = answerData.getQuestionId();
//            AnswerCode answerCode = answerData.getAnswerCode();
//            log.debug("Received answer: question id = {}; answer code = {}", questionId, answerCode);
//
//            if (answerHandler != null) {
//                answerHandler.handleAnswer(questionId, answerCode, chatId);
//                String msg = messageSource.getMessage("whswd.telegram.answer.received");
//                postText(msg, chatId);
//            }
//        }
//    }

//    private void processCallbackQuery(CallbackQuery callbackQuery) {
//        Message message = callbackQuery.getMessage();
//        String chatId = String.valueOf(callbackQuery.getMessage().getChat().getId());
//        String username = callbackQuery.getMessage().getText();
//        String messageText = callbackQuery.getData();
//        if (!personRegistrar.isRegisteredPerson(chatId)) {
//            log.info("Message from not registered person '{}': {}", username, messageText);
//            postRequestContactMessage(chatId);
//            return;
//        }
//
//        log.info("Message from {}: {}", callbackQuery.getFrom().getUserName(), callbackQuery.getMessage().getText());
//
//        if (AnswerData.match(messageText)) {
//            AnswerData answerData = AnswerData.deserialize(messageText);
//            String questionId = answerData.getQuestionId();
//            AnswerCode answerCode = answerData.getAnswerCode();
//            log.debug("Received answer: question id = {}; answer code = {}", questionId, answerCode);
//
//            if (answerHandler != null) {
//                answerHandler.handleAnswer(questionId, answerCode, chatId);
//                updateText(message.getMessageId(), message.getText(), chatId);
//            }
//        }
//    }

//    private void postRequestContactMessage(String chatId) {
//        SendMessage message = SendMessage.builder()
//            .chatId(chatId)
//            .text(messageSource.getMessage("whswd.telegram.send.contact"))
//            .build();
//
//        KeyboardRow row = new KeyboardRow();
//        String buttonText = messageSource.getMessage("whswd.telegram.send.contact.button");
//        row.add(KeyboardButton.builder().text(buttonText).requestContact(true).build());
//        message.setReplyMarkup(new ReplyKeyboardMarkup(Collections.singletonList(row)));
//
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            log.error("Unable to send invite message", e);
//        }
//    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            sendMessage(
                    "Привет, " + message.getFrom().getFirstName() + ". Ты написал_а: "+ message.getText(),
                    message.getFrom().getId().toString());
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
