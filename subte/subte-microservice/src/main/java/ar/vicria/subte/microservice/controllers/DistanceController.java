package ar.vicria.subte.microservice.controllers;


import ar.vicria.map.api.client.ImgGeneratorClient;
import ar.vicria.subte.dto.DistanceDto;
import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.microservice.services.DistanceService;
import ar.vicria.subte.resources.DistanceResource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Расчет дистанции между станциями (кратчайший).
 */
@RestController
public class DistanceController implements DistanceResource {

    private final DistanceService service;
    private final ImgGeneratorClient img;

    /**
     * Constructor.
     *
     * @param service            DistanceService
     * @param img                ImgGeneratorClient
     */
    public DistanceController(DistanceService service,
                              @Qualifier(value = "imgGeneratorClient") ImgGeneratorClient img) {
        this.service = service;
        this.img = img;
    }

    @Override
    @PostMapping("/distance/count")
    @ResponseStatus(HttpStatus.OK)
    public RouteDto count(@RequestBody DistanceDto dto) {

        RouteDto routeDto = service.count(dto);
        routeDto.setImg(img.getImage(routeDto));

        return routeDto;
    }


}
