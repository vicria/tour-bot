package ar.vicria.subte.microservice.services.basic;

import ar.vicria.subte.dto.basic.BaseDto;
import ar.vicria.subte.microservice.entities.BaseEntity;
import ar.vicria.subte.microservice.mappers.basic.BaseMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

/**
 * All CRUD for entity.
 *
 * @param <E> entity
 * @param <D> dto
 * @param <I> id of entity - string
 * @param <R> repository
 * @param <M> mapper
 */
public abstract class BaseMappedService<E extends BaseEntity,
        D extends BaseDto,
        I extends Serializable,
        R extends JpaRepository<E, I>,
        M extends BaseMapper<D, E>> {

    /**
     * Using repository.
     */
    protected R repository;
    /**
     * Using mapper.
     */
    protected M mapper;

    /**
     * Constructor.
     *
     * @param repository Using repository
     * @param mapper     Using mapper
     */
    public BaseMappedService(R repository, M mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Get one dto by id in db.
     *
     * @param id id
     * @return dto
     */
    @Transactional(readOnly = true)
    public D getOne(I id) {
        return toDto(getByIdOrThrow(id));
    }

    /**
     * exist in db.
     *
     * @param id id
     * @return logic field
     */
    public boolean exist(I id) {
        return findOne(id).isPresent();
    }

    private Optional<D> findOne(I id) {
        return findById(id).map(this::toDto);
    }

    private Optional<E> findById(I id) {
        return repository.findById(id);
    }

    private E getByIdOrThrow(I id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Entity with id %s not found", id)));
    }

    private Iterable<E> getAll() {
        return repository.findAll();
    }

    /**
     * Get all dtos from db.
     *
     * @return dtos
     */
    public List<D> getAllAsDto() {
        return StreamSupport.stream(getAll().spliterator(), false)
                .map(this::toDto)
                .toList();
    }


    /**
     * Mapping.
     *
     * @param dto dto
     * @return entity
     */
    public E fromDto(D dto) {
        return mapper.fromDto(dto);
    }

    /**
     * Mapping.
     *
     * @param entity entity
     * @return dto
     */
    public D toDto(E entity) {
        return mapper.toDto(entity);
    }

    private E create(E entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }
        return save(entity);
    }

    /**
     * Create entity.
     *
     * @param dto for creating
     * @return created
     */
    @Transactional
    public D create(D dto) {
        return toDto(create(fromDto(dto)));
    }

    private E save(E entity) {
        return repository.save(entity);
    }

    /**
     * Update entity.
     *
     * @param dto for updating
     * @return updated
     */
    public D update(D dto) {
        E readEntity = getByIdOrThrow((I) dto.getId());
        D dto1 = toDto(readEntity);
        E domain = update(mapper.fromDto(dto1));
        return toDto(domain);
    }

    private E update(E entity) {
        return save(entity);
    }

    /**
     * Update entity.
     *
     * @param id id
     *           Deprecated because of schema. Must have boolean field isArchive
     */
    @Deprecated
    public void delete(I id) {
        E entity = getByIdOrThrow(id);
        repository.delete(entity);
    }
}
