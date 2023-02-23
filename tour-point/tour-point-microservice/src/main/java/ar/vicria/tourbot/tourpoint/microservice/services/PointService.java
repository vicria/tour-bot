package ar.vicria.tourbot.tourpoint.microservice.services;

import ar.vicria.tourbot.tourpoint.dto.PointDto;
import ar.vicria.tourbot.tourpoint.microservice.entities.Point;
import ar.vicria.tourbot.tourpoint.microservice.mappers.PointMapper;
import ar.vicria.tourbot.tourpoint.microservice.repositories.PointRepository;
import ar.vicria.tourbot.tourpoint.microservice.services.basic.BaseMappedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PointService extends BaseMappedService<Point,
        PointDto, String, PointRepository, PointMapper> {

    private final PointRepository repository;
    private final PointMapper mapper;

    @Autowired
    public PointService(PointRepository repository,
                        PointMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PointDto create(PointDto dto){
        var PointDto = super.create(dto);
        return PointDto;
    }

    @Override
    @Transactional
    public PointDto update(String id, PointDto dto) {
        var forUpdateDto = getOne(id);
        return super.update(id, forUpdateDto);
    }

    @Transactional
    public PointDto archive(String id) {
        var forArchiveDto = getOne(id);
//        forArchiveDto.setStatus(TrainingsStatus.ARCHIVED);
        return super.update(id, forArchiveDto);
    }

    @Transactional
    public PointDto restore(String id) {
        var forActiveDto = getOne(id);
//        forActiveDto.setStatus(TrainingsStatus.ACTIVE);
        return super.update(id, forActiveDto);
    }

}
