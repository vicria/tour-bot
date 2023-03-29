package ar.vicria.subte.microservice.controllers;

import ar.vicria.subte.dto.DistanceDto;
import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.microservice.services.DistanceService;
import ar.vicria.subte.resources.DistanceResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Расчет дистанции между станциями (кратчайший)
 */
@RestController
public class DistanceController implements DistanceResource {

    private final DistanceService service;

    public DistanceController(DistanceService service) {
        this.service = service;
    }

    @Override
    @PostMapping("/distance/count")
    @ResponseStatus(HttpStatus.OK)
    public RouteDto count(@RequestBody DistanceDto dto) {
        return service.count(dto);
    }


}
