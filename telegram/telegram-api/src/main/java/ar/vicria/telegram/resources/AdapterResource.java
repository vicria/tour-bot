package ar.vicria.telegram.resources;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

/**
 * Интерфейс адаптера.
 */
public interface AdapterResource {

    /**
     * Отправка сообщения.
     *
     * @param msg    текст сообщения
     * @param chatId идентификатор пользователя
     */
    void sendMessage(String msg, String chatId);

    /**
     * Обновление текста сообщения.
     *
     * @param messageId номер обновляемого сообщения
     * @param text      обновленный текст
     * @param chatId    идентификатор чата, где сообщение
     */
    void updateText(Integer messageId, String text, String chatId);

    void updateText(Integer messageId, EditMessageText message, String chatId);

}
