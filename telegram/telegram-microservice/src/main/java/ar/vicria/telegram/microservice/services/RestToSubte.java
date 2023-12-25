package ar.vicria.telegram.microservice.services;

import ar.vicria.telegram.microservice.properties.TelegramProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//TODO временно

/**
 * Временный сервис, почти удалён.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RestToSubte {

    private final RestTemplate restTemplate;
    private final TelegramProperties properties;
}
