package ar.vicria.telegram.resources;

/**
 * Интерфейс адаптера
 */
public interface AdapterResource {

    /**
     * Отправка сообщения.
     *
     * @param msg текст сообщения
     * @param chatId идентификатор пользователя
     */
    void sendMessage(String msg, String chatId);
}
