package ar.vicria.telegram.microservice.configurations;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * Feign client config.
 */
@EnableFeignClients("ar.vicria.subte.resources")
@Configuration
public class FeignConfig {
}
