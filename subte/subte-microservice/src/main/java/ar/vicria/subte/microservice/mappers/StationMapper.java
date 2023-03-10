package ar.vicria.subte.microservice.mappers;

import ar.vicria.subte.dto.StationDto;
import ar.vicria.subte.microservice.entities.Station;
import ar.vicria.subte.microservice.mappers.basic.BaseMapper;
import ar.vicria.subte.microservice.mappers.basic.MappingConfig;
import org.mapstruct.Mapper;

/**
 * Mapper between {@link Station} and {@link StationDto} classes.
 */
@Mapper(config = MappingConfig.class)
public interface StationMapper extends BaseMapper<StationDto, Station> {

}