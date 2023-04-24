package ar.vicria.subte.microservice.services;

import ar.vicria.subte.dto.ConnectionDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.subte.microservice.entities.Connection;
import ar.vicria.subte.microservice.mappers.ConnectionMapper;
import ar.vicria.subte.microservice.repositories.ConnectionRepository;
import ar.vicria.subte.microservice.services.basic.BaseMappedService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ConnectionService between stations.
 */
@Service
@Transactional
public class ConnectionService extends BaseMappedService<Connection,
        ConnectionDto, String, ConnectionRepository, ConnectionMapper> {

    private final StationService stationService;

    /**
     * Constructor.
     *
     * @param stationService StationService
     * @param repository     ConnectionRepository
     * @param mapper         ConnectionMapper
     */
    public ConnectionService(ConnectionRepository repository,
                             ConnectionMapper mapper, StationService stationService) {
        super(repository, mapper);
        this.stationService = stationService;
    }

    /**
     * Create connection.
     *
     * @param dto connection
     * @return connection created
     */
    public ConnectionDto create(ConnectionDto dto) {
        String stationFrom = dto.getStationFrom().getName();
        String stationTo = dto.getStationTo().getName();
        String stationLast = dto.getLastStation().getName();

        StationDto stationFromDto = stationService.getByNameAndLine(stationFrom, dto.getStationFrom().getLine());
        dto.setStationFrom(stationFromDto);
        StationDto stationToDto = stationService.getByNameAndLine(stationTo, dto.getStationTo().getLine());
        dto.setStationTo(stationToDto);
        StationDto stationLastDto = stationService.getByNameAndLine(stationLast, dto.getLastStation().getLine());
        dto.setLastStation(stationLastDto);
        return dto;
    }
}
