package ar.vicria.tourbot.tourpoint.microservice.controllers;

import ar.vicria.tourbot.tourpoint.dto.PointDto;
import ar.vicria.tourbot.tourpoint.microservice.services.PointService;
import ar.vicria.tourbot.tourpoint.resources.PointResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointController implements PointResource {

    private final PointService service;

    public PointController(PointService service) {
        this.service = service;
    }

    @Override
    @GetMapping("/points/{id}")
    public PointDto getOne(@PathVariable("id") String id) {
        return service.getOne(id);
    }

    @Override
    @PostMapping("/points")
    @ResponseStatus(HttpStatus.CREATED)
    public PointDto create(PointDto dto) {
        return service.create(dto);
    }

    @Override
    @PatchMapping("/points/{id}")
    public PointDto update(@PathVariable("id") String id, PointDto dto) {
        return service.update(id, dto);
    }

    @Override
    @DeleteMapping("/points/{id}")
    public PointDto archive(@PathVariable("id") String id) {
        return service.archive(id);
    }

    @Override
    @PostMapping("/points/{id}/restore")
    public PointDto restore(@PathVariable("id") String id) {
        return service.restore(id);
    }

}
