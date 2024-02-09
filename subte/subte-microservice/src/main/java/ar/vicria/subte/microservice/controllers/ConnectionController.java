package ar.vicria.subte.microservice.controllers;

import ar.vicria.subte.dto.ConnectionDto;
import ar.vicria.subte.microservice.services.ConnectionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for connections between stations.
 */
@RestController
public class ConnectionController {

    private final ConnectionService service;

    /**
     * Constructor.
     *
     * @param service ConnectionService
     */
    public ConnectionController(ConnectionService service) {
        this.service = service;
    }

    /**
     * get one connection by ID.
     * @param id - id
     * @return connection
     */
    @GetMapping("/connections/{id}")
    public ConnectionDto getOne(@PathVariable("id") String id) {
        return service.getOne(id);
    }

    /**
     * Get All connections.
     * @return All connections
     */
    @GetMapping("/connections/all")
    public List<ConnectionDto> getAll() {
        return service.getAllAsDto();
    }

    /**
     * Create connection.
     * @param dto connection
     * @return connection created
     */
    @PostMapping("/connections")
    @ResponseStatus(HttpStatus.CREATED)
    public ConnectionDto create(@RequestBody @Valid ConnectionDto dto) {
        return service.create(dto);
    }

    /**
     * Edit connection.
     * @param dto connection
     * @return connection edited
     */
    @PatchMapping("/connections/")
    public ConnectionDto update(@RequestBody ConnectionDto dto) {
        return service.update(dto);
    }

}
