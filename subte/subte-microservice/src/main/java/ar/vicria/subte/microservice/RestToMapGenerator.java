package ar.vicria.subte.microservice;

import ar.vicria.subte.dto.RouteDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class RestToMapGenerator {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * send.
     *
     * @return RouteDto
     */
    public RouteDto send(RouteDto routeDto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RouteDto> requestEntity = new HttpEntity<>(routeDto, headers);
        ResponseEntity<byte[]> image = restTemplate.postForEntity("http://map-generator:8083/image", requestEntity, byte[].class);

        routeDto.setImg(image.getBody());
        return routeDto;
    }
}
