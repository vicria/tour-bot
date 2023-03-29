package ar.vicria.subte.microservice.controllers;

import ar.vicria.subte.dto.ConnectionDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.subte.microservice.services.ConnectionService;
import ar.vicria.subte.microservice.services.StationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
public class ConnectionController {

    private final ConnectionService service;
    private final StationService stationService;

    public ConnectionController(ConnectionService service, StationService stationService) {
        this.service = service;
        this.stationService = stationService;
    }

    @GetMapping("/connections/{id}")
    public ConnectionDto getOne(@PathVariable("id") String id) {
        return service.getOne(id);
    }

    @GetMapping("/connections/all")
    public List<ConnectionDto> getAll() {
        return service.getAllAsDto();
    }

    @PostMapping("/connections")
    @ResponseStatus(HttpStatus.CREATED)
    public ConnectionDto create(@RequestBody ConnectionDto dto) {
        @NotBlank String stationFrom = dto.getStationFrom().getName();
        @NotBlank String stationTo = dto.getStationTo().getName();
        @NotBlank String stationLast = dto.getLastStation().getName();

        StationDto stationFromDto = stationService.getByNameAndLine(stationFrom,dto.getStationFrom().getLine());
        dto.setStationFrom(stationFromDto);
        StationDto stationToDto = stationService.getByNameAndLine(stationTo,dto.getStationTo().getLine());
        dto.setStationTo(stationToDto);
        StationDto stationLastDto = stationService.getByNameAndLine(stationLast,dto.getLastStation().getLine());
        dto.setLastStation(stationLastDto);
        return service.create(dto);
    }

    @PatchMapping("/connections/{id}")
    public ConnectionDto update(@PathVariable("id") String id, @RequestBody ConnectionDto dto) {
        return service.update(id, dto);
    }

}
