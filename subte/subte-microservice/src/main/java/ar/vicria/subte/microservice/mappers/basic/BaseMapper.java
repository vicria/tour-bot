package ar.vicria.subte.microservice.mappers.basic;

/**
 * Базовый класс для генерации Map.
 *
 * @param <DTO> DTO class
 * @param <ENTITY> Entity class
 */
public interface BaseMapper<DTO, ENTITY> {

    /**
     * Из сущности в ДТО.
     * @param entity сущность
     * @return дто
     */
    DTO toDto(ENTITY entity);

    /**
     * Из дто в сущность.
     * @param dto дто
     * @return сущность
     */
    ENTITY fromDto(DTO dto);

}
