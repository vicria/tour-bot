package ar.vicria.tourbot.tourpoint.resources;

import ar.vicria.tourbot.tourpoint.dto.PointDto;

/**
 * Сертификаты
 */
public interface PointResource {
    
    /**
     * Получение информации о сертификате.
     *
     * @param id идентификатор сертификата
     * @return информация о сертификате
     */
    PointDto getOne(String id);

    /**
     * Создание нового сертификата.
     *
     * @param dto форма сертификата
     * @return информация о сертификате
     */
    PointDto create(PointDto dto);

    /**
     * Обновление сертификата.
     *
     * @param id идентификатор сертификата
     * @param dto форма сертификата
     * @return информация о сертификате
     */
    PointDto update(String id, PointDto dto);

    /**
     * Архивирование сертификата.
     *
     * @param id идентификатор сертификата
     * @return информация о сертификате
     */
    PointDto archive(String id);

    /**
     * Восстановление сертификата.
     *
     * @param id идентификатор сертификата
     * @return информация о сертификате
     */
    PointDto restore(String id);

}
