package ar.vicria.subte.microservice.services;

import ar.vicria.subte.dto.StationDto;
import ar.vicria.subte.microservice.entities.Station;
import ar.vicria.subte.microservice.mappers.StationMapper;
import ar.vicria.subte.microservice.repositories.StationRepository;
import ar.vicria.subte.microservice.services.basic.BaseMappedService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * All methods for stations.
 */
@Service
@Transactional
public class StationService extends BaseMappedService<Station,
        StationDto, String, StationRepository, StationMapper> {


    /**
     * Constructor.
     *
     * @param repository StationRepository
     * @param mapper     StationMapper
     */
    public StationService(StationRepository repository,
                          StationMapper mapper) {
        super(repository, mapper);
    }

    /**
     * Поиск станции по имени и линии.
     * @param name - имя станции
     * @param line - имя и цвет станции
     * @return станция
     */
    public StationDto getByNameAndLine(String name, String line) {
        Station byName = repository.getByNameAndLine(name, line);
        return mapper.toDto(byName);
    }
}
