package ar.vicria.subte.microservice.services;

import ar.vicria.subte.dto.ConnectionDto;
import ar.vicria.subte.microservice.entities.Connection;
import ar.vicria.subte.microservice.mappers.ConnectionMapper;
import ar.vicria.subte.microservice.repositories.ConnectionRepository;
import ar.vicria.subte.microservice.services.basic.BaseMappedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConnectionService extends BaseMappedService<Connection,
        ConnectionDto, String, ConnectionRepository, ConnectionMapper> {


    @Autowired
    public ConnectionService(ConnectionRepository repository,
                             ConnectionMapper mapper) {
        super(repository, mapper);
    }
}
