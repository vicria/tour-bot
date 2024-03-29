package ar.vicria.subte.microservice.services;

import ar.vicria.subte.dto.ConnectionDto;
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

    /**
     * Constructor.
     *
     * @param repository     ConnectionRepository
     * @param mapper         ConnectionMapper
     */
    public ConnectionService(ConnectionRepository repository,
                             ConnectionMapper mapper) {
        super(repository, mapper);
    }
}
