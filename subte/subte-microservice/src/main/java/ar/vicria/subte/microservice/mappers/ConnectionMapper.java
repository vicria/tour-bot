package ar.vicria.subte.microservice.mappers;

import ar.vicria.subte.dto.ConnectionDto;
import ar.vicria.subte.microservice.entities.Connection;
import ar.vicria.subte.microservice.mappers.basic.BaseMapper;
import ar.vicria.subte.microservice.mappers.basic.MappingConfig;
import org.mapstruct.Mapper;

/**
 * Mapper between {@link Connection} and {@link ConnectionDto} classes.
 */
@Mapper(config = MappingConfig.class,
        uses = StationMapper.class)
public interface ConnectionMapper extends BaseMapper<ConnectionDto, Connection> {

}