package ar.vicria.telegram.microservice.services;

import ar.vicria.subte.dto.DistanceDto;
import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.configuration.CacheConfig;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Ticker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ar.vicria.telegram.microservice.configuration.CacheConfig.ROUTE_CACHE;
import static ar.vicria.telegram.microservice.configuration.CacheConfig.ROUTE_CACHE_EXPIRE_DURATION_MINUTES;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {RestToSubte.class, CacheConfig.class, RestToSubteTest.TestCaffeineConfig.class})
class RestToSubteTest {
    private static final StationDto FROM_STATION = new StationDto("line1", "name1");
    private static final StationDto TO_STATION = new StationDto("line2", "name2");

    @Autowired
    Ticker ticker;
    @Autowired
    CacheManager cacheManager;

    @MockBean
    RestTemplate restTemplate;
    @SpyBean
    RestToSubte rest;

    Cache<Object, Object> cache;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void init() {
        cache = (Cache<Object, Object>) Objects.requireNonNull(cacheManager.getCache(ROUTE_CACHE)).getNativeCache();
        cache.invalidateAll();
    }

    private void configureRestTemplateMock(StationDto from, StationDto to, RouteDto result) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        DistanceDto distanceDto = new DistanceDto();
        distanceDto.setFrom(from);
        distanceDto.setTo(to);
        HttpEntity<DistanceDto> requestEntity = new HttpEntity<>(distanceDto, headers);
        Mockito.when(restTemplate.postForEntity("http://subte:8082/distance/count",
                requestEntity, RouteDto.class)).thenReturn(ResponseEntity.ok(result));
    }

    private void advanceTicker(long nanos) {
        ((AdvancingTicker) ticker).advance(nanos);
    }

    @Test
    void sendCacheTest() {
        RouteDto result = new RouteDto(List.of(FROM_STATION, TO_STATION), 1, TO_STATION);
        configureRestTemplateMock(FROM_STATION, TO_STATION, result);

        RouteDto restResult = rest.send(FROM_STATION, TO_STATION);
        assertEquals(result, restResult);

        restResult = rest.send(FROM_STATION, TO_STATION);
        assertEquals(result, restResult);

        Mockito.verify(rest, Mockito.times(1)).send(FROM_STATION, TO_STATION);
        Mockito.verify(restTemplate, Mockito.times(1))
                .postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any());
    }

    @Test
    void sendCacheEvictTest() {
        RouteDto result = new RouteDto(List.of(FROM_STATION, TO_STATION), 1, TO_STATION);
        configureRestTemplateMock(FROM_STATION, TO_STATION, result);

        RouteDto restResult = rest.send(FROM_STATION, TO_STATION);
        assertEquals(result, restResult);

        result.setTotalTime(2);
        configureRestTemplateMock(FROM_STATION, TO_STATION, result);

        advanceTicker(Duration.of(ROUTE_CACHE_EXPIRE_DURATION_MINUTES, ChronoUnit.MINUTES).toNanos());
        cache.cleanUp();

        restResult = rest.send(FROM_STATION, TO_STATION);
        assertEquals(result, restResult);

        Mockito.verify(rest, Mockito.times(2)).send(FROM_STATION, TO_STATION);
        Mockito.verify(restTemplate, Mockito.times(2))
                .postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any());
    }

    @Configuration
    static class TestCaffeineConfig {
        @Bean
        @Primary
        public Ticker fakeTicker() {
            return new AdvancingTicker();
        }
    }

    static class AdvancingTicker implements Ticker {
        private long advance;

        public void advance(long nanos) {
            advance += nanos;
        }

        @Override
        public long read() {
            return System.nanoTime() + advance;
        }
    }
}