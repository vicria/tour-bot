package ar.vicria.telegram.microservice.configurations;

import ar.vicria.subte.resources.StationResource;
import ar.vicria.telegram.microservice.properties.TelegramProperties;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign client config.
 */
@Configuration
@EnableFeignClients(clients = StationResource.class)
@RequiredArgsConstructor
public class FeignConfig {
    private final TelegramProperties telegramProperties;

    /**
     * RequestInterceptor to dynamically replace default placeholder url in Feign client for url from properties.
     *
     * @return RequestInterceptor bean instance.
     */
    @Bean
    public RequestInterceptor targetReplacingInterceptor() {
        return template -> template.target(telegramProperties.getSubteUrl());
    }
}
