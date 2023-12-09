package ar.vicria.telegram.microservice.services;

import ar.vicria.subte.dto.DistanceDto;
import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static ar.vicria.telegram.microservice.configuration.CacheConfig.ROUTE_CACHE;

//TODO временно

/**
 * Временный сервис до Кафки.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RestToSubte {

    private final RestTemplate restTemplate;

    /**
     * send.
     *
     * @param from from
     * @param to   to
     * @return RouteDto
     */
    @Cacheable(cacheNames = ROUTE_CACHE)
    public RouteDto send(StationDto from, StationDto to) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DistanceDto distanceDto = new DistanceDto();
        distanceDto.setFrom(from);
        distanceDto.setTo(to);

        HttpEntity<DistanceDto> requestEntity = new HttpEntity<>(distanceDto, headers);

        ResponseEntity<RouteDto> response = restTemplate.postForEntity("http://subte:8082/distance/count",
                requestEntity, RouteDto.class);

        return response.getBody();
    }

    /**
     * get.
     *
     * @return List StationDto
     */
    public List<StationDto> get() {
        ResponseEntity<StationDto[]> response = restTemplate.getForEntity("http://subte:8082/stations/all",
                StationDto[].class);

        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }
}
