package ar.vicria.telegram.microservice.services;

import ar.vicria.subte.dto.DistanceDto;
import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

//TODO временно
@Slf4j
@Service
public class RestToSubte {

    private final RestTemplate restTemplate = new RestTemplate();

    public RouteDto send(String from, String to){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("from", "Santa Fe Carlos Jáuregui");
//        map.add("to", "9 de Julio");

        DistanceDto distanceDto = new DistanceDto();
        distanceDto.setFrom(from);
        distanceDto.setTo(to);

        HttpEntity<DistanceDto> requestEntity = new HttpEntity<>(distanceDto, headers);


        ResponseEntity<RouteDto> response = restTemplate.postForEntity("http://localhost:8082/distance/count",
                requestEntity, RouteDto.class);

        return response.getBody();
    }

    public List<StationDto> get(){
        ResponseEntity<StationDto[]> response = restTemplate.getForEntity("http://localhost:8082/stations/all",
                StationDto[].class);

        return Arrays.asList(response.getBody());
    }
}
