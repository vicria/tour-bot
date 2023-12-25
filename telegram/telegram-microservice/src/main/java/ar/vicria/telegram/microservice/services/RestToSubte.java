package ar.vicria.telegram.microservice.services;

import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.properties.TelegramProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

//TODO временно

/**
 * Временный сервис до Feing.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RestToSubte {

    private final RestTemplate restTemplate;
    private final TelegramProperties properties;

    /**
     * get.
     *
     * @return List StationDto
     */
    public List<StationDto> get() {
        ResponseEntity<StationDto[]> response = restTemplate.getForEntity(properties.getSubteGet(),
                StationDto[].class);

        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }
}
