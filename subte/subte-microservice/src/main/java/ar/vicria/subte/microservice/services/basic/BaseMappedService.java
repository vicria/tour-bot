package ar.vicria.subte.microservice.services.basic;

import ar.vicria.subte.microservice.entities.BaseEntity;
import ar.vicria.subte.dto.basic.BaseDto;
import ar.vicria.subte.microservice.mappers.basic.BaseMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class BaseMappedService<ENTITY extends BaseEntity,
        DTO extends BaseDto,
        ID extends Serializable,
        REPO extends JpaRepository<ENTITY, ID>,
        MAPPER extends BaseMapper<DTO, ENTITY>> {

    protected REPO repository;
    protected MAPPER mapper;

    public BaseMappedService(REPO repository, MAPPER mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public DTO getOne(ID id) {
        return toDto(getByIdOrThrow(id));
    }

    public boolean exist(ID id) {
        return findOne(id).isPresent();
    }

    public Optional<DTO> findOne(ID id) {
        return findById(id).map(this::toDto);
    }

    protected Optional<ENTITY> findById(ID id) {
        return repository.findById(id);
    }

    protected ENTITY getByIdOrThrow(ID id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Entity with id %s not found", id)));
    }

    protected Iterable<ENTITY> getAll() {
        return repository.findAll();
    }

    public List<DTO> getAllAsDto() {
        return StreamSupport.stream(getAll().spliterator(), false)
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    public ENTITY fromDto(DTO dto) {
        return mapper.fromDto(dto);
    }

    public DTO toDto(ENTITY entity) {
        return mapper.toDto(entity);
    }

    protected ENTITY create(ENTITY entity) {
        ENTITY ENTITY = save(entity);
        return ENTITY;
    }

    @Transactional
    public DTO create(DTO dto) {
        return toDto(create(fromDto(dto)));
    }

    protected ENTITY save(ENTITY entity) {
        return repository.save(entity);
    }

    public DTO update(ID id, DTO dto) {
        ENTITY readEntity = getByIdOrThrow(id);
        DTO dto1 = toDto(readEntity);
        ENTITY domain = update(mapper.fromDto(dto1));
        return toDto(domain);
    }

    protected ENTITY update(ENTITY entity) {
        ENTITY ENTITY = save(entity);
        return ENTITY;
    }

    public void delete(ID id) {
        ENTITY ENTITY = getByIdOrThrow(id);
        repository.delete(ENTITY);
    }
}
