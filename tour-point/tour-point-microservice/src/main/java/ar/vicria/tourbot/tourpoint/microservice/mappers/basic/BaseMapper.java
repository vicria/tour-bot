package ar.vicria.tourbot.tourpoint.microservice.mappers.basic;

import org.mapstruct.MappingTarget;

/**
 * Базовый класс для генерации Map
 *
 * @param <DTO> DTO class
 * @param <ENTITY> Entity class
 * @author VPashkevich
 * @since 1.16.0
 */

public interface BaseMapper<DTO, ENTITY> {

    DTO toDto(ENTITY entity);

    ENTITY fromDto(DTO dto);

    DTO updateEntity(@MappingTarget ENTITY entity, DTO dto);

}
