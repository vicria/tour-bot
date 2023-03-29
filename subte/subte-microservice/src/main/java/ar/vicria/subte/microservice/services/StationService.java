package ar.vicria.subte.microservice.services;

import ar.vicria.subte.dto.StationDto;
import ar.vicria.subte.microservice.entities.Station;
import ar.vicria.subte.microservice.mappers.StationMapper;
import ar.vicria.subte.microservice.repositories.StationRepository;
import ar.vicria.subte.microservice.services.basic.BaseMappedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StationService extends BaseMappedService<Station,
        StationDto, String, StationRepository, StationMapper> {


    @Autowired
    public StationService(StationRepository repository,
                          StationMapper mapper) {
        super(repository, mapper);
    }

    public StationDto getByNameAndLine(String name, String line){
        Station byName = repository.getByNameAndLine(name,line);
        return mapper.toDto(byName);
    }
}
