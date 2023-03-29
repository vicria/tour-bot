package ar.vicria.subte.microservice.controllers;

import ar.vicria.subte.dto.StationDto;
import ar.vicria.subte.microservice.services.StationService;
import ar.vicria.subte.resources.StationResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StationController implements StationResource {

    private final StationService service;

    public StationController(StationService service) {
        this.service = service;
    }

    @Override
    @GetMapping("/stations/{id}")
    public StationDto getOne(@PathVariable("id") String id) {
        return service.getOne(id);
    }

    @Override
    @GetMapping("/stations/all")
    public List<StationDto> getAll() {
        return service.getAllAsDto();
    }

    //todo handler exception unique constraint "subte_station_name_uindex"
    @Override
    @PostMapping("/stations")
    @ResponseStatus(HttpStatus.CREATED)
    public StationDto create(@RequestBody StationDto dto) {
        return service.create(dto);
    }

    @Override
    @PatchMapping("/stations/{id}")
    public StationDto update(@PathVariable("id") String id, @RequestBody StationDto dto) {
        return service.update(id, dto);
    }

}
