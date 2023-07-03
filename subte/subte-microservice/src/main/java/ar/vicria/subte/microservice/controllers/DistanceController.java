package ar.vicria.subte.microservice.controllers;

import ar.vicria.subte.dto.DistanceDto;
import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.microservice.services.DistanceService;
import ar.vicria.subte.resources.DistanceResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Расчет дистанции между станциями (кратчайший).
 */
@RestController
public class DistanceController implements DistanceResource {

    private final DistanceService service;
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Constructor.
     * @param service DistanceService
     */
    public DistanceController(DistanceService service) {
        this.service = service;
    }

    @Override
    @PostMapping("/distance/count")
    @ResponseStatus(HttpStatus.OK)
    public RouteDto count(@RequestBody DistanceDto dto) {

        RouteDto routeDto = service.count(dto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RouteDto> requestEntity = new HttpEntity<>(routeDto, headers);
        ResponseEntity<byte[]> image = restTemplate.postForEntity("http://map-generator:8083/image", requestEntity, byte[].class);

        routeDto.setImg(image.getBody());

        return routeDto;
    }


}
