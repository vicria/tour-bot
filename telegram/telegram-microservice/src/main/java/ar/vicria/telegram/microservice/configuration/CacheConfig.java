package ar.vicria.telegram.microservice.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Cache configuration.
 */
@Configuration
@EnableCaching
public class CacheConfig {
    /**
     * Route cache name.
     */
    public static final String ROUTE_CACHE = "route";
    /**
     * Route cache elements expiration duration.
     */
    public static final int ROUTE_CACHE_EXPIRE_DURATION_MINUTES = 15;

    private static final int ROUTE_CACHE_MAXIMUM_SIZE = 10000;

    /**
     * Caffeine cache manager bean configuration.
     *
     * @param caffeine Caffeine cache builder instance
     * @return Caffeine cache manager instance.
     */
    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCacheNames(List.of(ROUTE_CACHE));
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }

    /**
     * Caffeine cache builder bean configuration.
     *
     * @param ticker Ticker instance.
     *
     * @return Caffeine cache builder instance.
     */
    @Bean
    public Caffeine<Object, Object> caffeine(Ticker ticker) {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder();
        caffeine.ticker(ticker);
        caffeine.maximumSize(ROUTE_CACHE_MAXIMUM_SIZE);
        caffeine.expireAfterAccess(Duration.of(ROUTE_CACHE_EXPIRE_DURATION_MINUTES, ChronoUnit.MINUTES));
        return caffeine;
    }

    /**
     * Ticker for cache. Explicitly defined for testing purposes.
     *
     * @return Ticker instance.
     */
    @Bean
    public Ticker ticker() {
        return Ticker.systemTicker();
    }
}
