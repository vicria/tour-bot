package ar.vicria.tourbot.tourpoint.microservice.mappers;

import ar.vicria.tourbot.tourpoint.dto.PointDto;
import ar.vicria.tourbot.tourpoint.microservice.entities.Point;
import ar.vicria.tourbot.tourpoint.microservice.mappers.basic.BaseMapper;
import ar.vicria.tourbot.tourpoint.microservice.mappers.basic.MappingConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper between {@link Point} and {@link PointDto} classes.
 */
@Mapper(config = MappingConfig.class)
public interface PointMapper extends BaseMapper<PointDto, Point> {

    @Override
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    PointDto updateEntity(@MappingTarget Point entity, PointDto dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    Point fromDto(PointDto dto);

}