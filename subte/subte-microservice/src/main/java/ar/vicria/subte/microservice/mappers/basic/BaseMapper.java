package ar.vicria.subte.microservice.mappers.basic;

/**
 * Базовый класс для генерации Map
 *
 * @param <DTO> DTO class
 * @param <ENTITY> Entity class
 */

public interface BaseMapper<DTO, ENTITY> {

    DTO toDto(ENTITY entity);

    ENTITY fromDto(DTO dto);

}
